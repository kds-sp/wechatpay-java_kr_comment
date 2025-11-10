package com.wechat.pay.java.core.certificate;

import java.security.cert.X509Certificate;

/** 위챗페이 플랫폼 인증서 제공기 */
public interface CertificateProvider {

  /**
   * 인증서 시리얼 번호에 따라 인증서 가져오기
   *
   * @param serialNumber 위챗페이 플랫폼 인증서 시리얼 번호
   * @return X.509 인증서 인스턴스
   */
  X509Certificate getCertificate(String serialNumber);

  /**
   * 최신 사용 가능한 위챗페이 플랫폼 인증서 가져오기
   *
   * @return X.509 인증서 인스턴스
   */
  X509Certificate getAvailableCertificate();
}
