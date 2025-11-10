package com.wechat.pay.java.core.certificate;

import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 정기적으로 인증서를 업데이트하는 서비스, 정적 함수로 구성된 유틸리티 클래스 */
public class AutoCertificateService {
  private static final Logger log = LoggerFactory.getLogger(AutoCertificateService.class);
  protected static final int UPDATE_INTERVAL_MINUTE = 60;
  private static final Duration defaultUpdateInterval = Duration.ofMinutes(UPDATE_INTERVAL_MINUTE);
  private static final ConcurrentHashMap<String, Map<String, X509Certificate>> certificateMap =
      new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<String, Runnable> downloadWorkerMap =
      new ConcurrentHashMap<>();
  private static final ScheduledThreadPoolExecutor serviceExecutor =
      new ScheduledThreadPoolExecutor(
          1,
          new ThreadFactory() {

            private final AtomicInteger threadCount = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
              Thread t =
                  new Thread(r, "auto-certificate-service-daemon-" + threadCount.incrementAndGet());
              // 사용자 스레드 실행 완료 후 JVM 종료를 막지 않음
              t.setDaemon(true);
              return t;
            }
          });

  static {
    // 취소 시 즉시 작업 큐에서 삭제
    serviceExecutor.setRemoveOnCancelPolicy(true);
  }

  private static ScheduledFuture<?> future;

  private static int updateCount;

  private AutoCertificateService() {
    throw new IllegalStateException("this class cannot be instantiated");
  }

  /**
   * 인증서 다운로드 작업 등록, 첫 등록 시 인증서를 먼저 다운로드합니다. 다운로드가 성공하면 다운로더를 저장하여 정기 업데이트에 사용합니다. 다운로드가 실패하면 예외가 발생합니다.
   * 이미 등록된 경우 현재 전달된 다운로더가 이전 다운로더를 덮어씁니다. 현재 다운로더가 인증서를 다운로드할 수 없으면 정기 업데이트가 실패합니다.
   *
   * @param merchantId 가맹점 번호
   * @param type 호출자가 정의한 인증서 타입, 예: RSA/ShangMi
   * @param downloader 인증서 다운로더
   */
  public static void register(String merchantId, String type, CertificateDownloader downloader) {
    String key = calculateDownloadWorkerMapKey(merchantId, type);
    Runnable worker =
        () -> {
          Map<String, X509Certificate> result = downloader.download();
          certificateMap.put(key, result);
        };

    // 인증서 다운로드하여 설정이 올바른지 검증
    // 오류가 있으면 예외를 발생시킴, fast-fail
    worker.run();
    // 설정 업데이트
    downloadWorkerMap.put(key, worker);

    start(defaultUpdateInterval);
  }

  /**
   * 인증서 다운로드 작업 등록 해제
   *
   * @param merchantId 가맹점 번호
   * @param type 호출자가 정의한 인증서 타입, `register()` 시의 값과 동일해야 함
   */
  public static void unregister(String merchantId, String type) {
    String key = calculateDownloadWorkerMapKey(merchantId, type);
    downloadWorkerMap.remove(key);
  }

  /** 등록된 모든 다운로더와 다운로드된 인증서를 정리하고, 정기 업데이트 작업을 취소합니다. */
  public static void shutdown() {
    downloadWorkerMap.clear();
    certificateMap.clear();
    synchronized (AutoCertificateService.class) {
      if (future != null) {
        future.cancel(false);
        future = null;
      }
    }
  }

  /**
   * 인증서 업데이트의 주기적 작업 시작
   *
   * @param updateInterval 인증서 업데이트 주기
   */
  public static void start(Duration updateInterval) {
    synchronized (AutoCertificateService.class) {
      if (future == null) {
        future =
            serviceExecutor.scheduleAtFixedRate(
                AutoCertificateService::update,
                updateInterval.toMillis() / TimeUnit.SECONDS.toMillis(1),
                updateInterval.toMillis() / TimeUnit.SECONDS.toMillis(1),
                TimeUnit.SECONDS);
      }
    }
  }

  private static void update() {
    log.info("Begin update Certificates. total updates: {}", updateCount);
    downloadWorkerMap.forEach(
        (k, v) -> {
          try {
            v.run();
            log.info("update wechatpay certificate {} done", k);
          } catch (Exception e) {
            log.error("Download and update wechatpay certificate {} failed", k);
          }
        });
    updateCount++;
  }

  private static String calculateDownloadWorkerMapKey(String merchantId, String type) {
    return merchantId + "-" + type;
  }

  private static X509Certificate getAvailableCertificate(
      Map<String, X509Certificate> certificateMap) {
    // 가져온 모든 인증서가 사용 가능하다고 가정하고, 가장 오래 사용할 수 있는 것을 선택
    X509Certificate longest = null;
    for (X509Certificate item : certificateMap.values()) {
      if (longest == null || item.getNotAfter().after(longest.getNotAfter())) {
        longest = item;
      }
    }
    return longest;
  }

  // 인증서 시리얼 번호에 따라 인증서 가져오기
  public static X509Certificate getCertificate(
      String merchantId, String type, String serialNumber) {
    String key = calculateDownloadWorkerMapKey(merchantId, type);
    return certificateMap.get(key).get(serialNumber);
  }

  // 최신 사용 가능한 위챗페이 플랫폼 인증서 가져오기
  public static X509Certificate getAvailableCertificate(String merchantId, String type) {
    String key = calculateDownloadWorkerMapKey(merchantId, type);
    return getAvailableCertificate(certificateMap.get(key));
  }
}
