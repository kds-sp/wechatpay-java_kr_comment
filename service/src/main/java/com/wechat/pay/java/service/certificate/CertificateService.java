package com.wechat.pay.java.service.certificate;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HostName;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.http.HttpResponse;
import com.wechat.pay.java.core.http.MediaType;
import com.wechat.pay.java.core.util.PemUtil;
import com.wechat.pay.java.service.certificate.model.Data;
import com.wechat.pay.java.service.certificate.model.DownloadCertificateResponse;
import com.wechat.pay.java.service.certificate.model.EncryptCertificate;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;

/** 인증서 서비스 */
public class CertificateService {

  private final HttpClient httpClient;
  private final HostName hostName;

  private static final String RSA_URL = "https://api.mch.weixin.qq.com/v3/certificates";

  private CertificateService(HttpClient httpClient, HostName hostName) {
    this.httpClient = requireNonNull(httpClient);
    this.hostName = hostName;
  }

  /** CertificateService 생성자 */
  public static class Builder {

    private HttpClient httpClient;
    private HostName hostName;

    public Builder config(Config config) {
      this.httpClient = new DefaultHttpClientBuilder().config(config).build();
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = requireNonNull(httpClient);
      return this;
    }

    public Builder hostName(HostName hostName) {
      this.hostName = hostName;
      return this;
    }

    public CertificateService build() {
      return new CertificateService(httpClient, hostName);
    }
  }

  /**
   * 위챗페이 플랫폼 인증서 목록 다운로드, RSA 인증서만 다운로드
   *
   * @param apiV3Key 위챗페이 APIv3 키
   * @return 위챗페이 플랫폼 인증서 X509Certificate 목록
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public List<X509Certificate> downloadCertificate(byte[] apiV3Key) {
    AeadCipher aeadCipher = new AeadAesCipher(apiV3Key);
    return downloadCertificate(aeadCipher);
  }

  /**
   * 위챗페이 플랫폼 인증서 목록 다운로드
   *
   * @param requestPath 인증서 다운로드 요청 경로
   * @param aeadCipher 인증 암호화기, 인증서 복호화에 사용
   * @param certificateGenerator 인증서 문자열에서 인증서 객체로의 생성기
   * @return 위챗페이 플랫폼 인증서 X509Certificate 목록
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public List<X509Certificate> downloadCertificate(
      String requestPath,
      AeadCipher aeadCipher,
      Function<String, X509Certificate> certificateGenerator) {
    if (hostName != null) {
      requestPath = requestPath.replaceFirst(HostName.API.getValue(), hostName.getValue());
    }
    HttpRequest request =
        new HttpRequest.Builder()
            .httpMethod(HttpMethod.GET)
            .url(requestPath)
            .addHeader(Constant.ACCEPT, " */*")
            .addHeader(Constant.CONTENT_TYPE, MediaType.APPLICATION_JSON.getValue())
            .build();
    HttpResponse<DownloadCertificateResponse> httpResponse =
        httpClient.execute(request, DownloadCertificateResponse.class);
    List<Data> dataList = httpResponse.getServiceResponse().getData();
    List<X509Certificate> certificates = new ArrayList<>();
    for (Data data : dataList) {
      EncryptCertificate encryptCertificate = data.getEncryptCertificate();
      String decryptCertificate =
          aeadCipher.decrypt(
              encryptCertificate.getAssociatedData().getBytes(StandardCharsets.UTF_8),
              encryptCertificate.getNonce().getBytes(StandardCharsets.UTF_8),
              Base64.getDecoder().decode(encryptCertificate.getCiphertext()));

      certificates.add(certificateGenerator.apply(decryptCertificate));
    }
    return certificates;
  }

  /**
   * 위챗페이 플랫폼 인증서 목록 다운로드, RSA 인증서만 다운로드
   *
   * @param aeadCipher 인증 암호화기, 인증서 복호화에 사용
   * @return 인증서 목록
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public List<X509Certificate> downloadCertificate(AeadCipher aeadCipher) {
    return downloadCertificate(RSA_URL, aeadCipher, PemUtil::loadX509FromString);
  }
}
