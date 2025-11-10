package com.wechat.pay.java.core.cipher;

/** 민감 정보 암호화기 */
public interface PrivacyEncryptor {
  /**
   * 암호화하고 문자열로 변환
   *
   * @param plaintext 평문
   * @return Base64 인코딩된 암호문
   */
  String encrypt(String plaintext);

  /**
   * 암호화에 사용되는 공개키가 속한 인증서의 인증서 시리얼 번호 가져오기, 요청의 HTTP 헤더 Wechatpay-Serial에 설정 가능
   *
   * @return 인증서 시리얼 번호
   */
  String getWechatpaySerial();
}
