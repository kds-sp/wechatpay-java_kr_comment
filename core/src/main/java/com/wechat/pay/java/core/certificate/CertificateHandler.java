package com.wechat.pay.java.core.certificate;

import java.security.cert.X509Certificate;

/** 인증서 처리기 */
public interface CertificateHandler {

  /**
   * 인증서를 String에서 X509Certificate로 변환
   *
   * @param certificate 인증서 문자열
   * @return X509Certificate
   */
  X509Certificate generateCertificate(String certificate);

  /**
   * * 인증서 체인 검증 (검증 비권장, 인증서가 만료되어 적시에 교체하지 않으면 검증 실패로 인해 비즈니스에 영향을 줄 수 있음)
   *
   * @param certificate 위챗페이 플랫폼 인증서
   * @throws com.wechat.pay.java.core.exception.ValidationException 인증서 검증 실패
   */
  void validateCertPath(X509Certificate certificate);
}
