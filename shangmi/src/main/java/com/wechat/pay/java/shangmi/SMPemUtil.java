package com.wechat.pay.java.shangmi;

import com.tencent.kona.KonaProvider;
import com.wechat.pay.java.core.util.PemUtil;
import java.io.*;
import java.security.*;
import java.security.cert.X509Certificate;

public class SMPemUtil {

  static {
    Security.addProvider(new KonaProvider());
  }

  private SMPemUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * 개인키 문자열에서 EC(타원 곡선) 개인키를 로드합니다.
   *
   * @param keyString 개인키 문자열
   * @return EC(타원 곡선) 개인키. 예: 국밀 개인키
   */
  public static PrivateKey loadPrivateKeyFromString(String keyString) {
    return PemUtil.loadPrivateKeyFromString(keyString, "EC", KonaProvider.NAME);
  }

  /**
   * 개인키 파일 경로에서 개인키를 로드합니다.
   *
   * @param keyPath 개인키 파일 경로
   * @return 개인키
   */
  public static PrivateKey loadPrivateKeyFromPath(String keyPath) {
    return PemUtil.loadPrivateKeyFromPath(keyPath, "EC", KonaProvider.NAME);
  }

  /**
   * 인증서 입력 스트림에서 인증서를 로드합니다.
   *
   * @param inputStream 인증서 입력 스트림
   * @return X509 인증서
   */
  public static X509Certificate loadX509FromStream(InputStream inputStream) {
    return PemUtil.loadX509FromStream(inputStream, KonaProvider.NAME);
  }

  /**
   * 인증서 파일 경로에서 인증서를 로드합니다.
   *
   * @param certificatePath 인증서 파일 절대 경로
   * @return X509 인증서
   */
  public static X509Certificate loadX509FromPath(String certificatePath) {
    return PemUtil.loadX509FromPath(certificatePath, KonaProvider.NAME);
  }

  /**
   * 인증서 문자열에서 인증서를 로드합니다.
   *
   * @param certificateString 인증서 문자열
   * @return X509 인증서
   */
  public static X509Certificate loadX509FromString(String certificateString) {
    return PemUtil.loadX509FromString(certificateString, KonaProvider.NAME);
  }
}
