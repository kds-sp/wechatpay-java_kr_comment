package com.wechat.pay.java.core.cipher;

/** 민감 정보 복호화기 */
public interface PrivacyDecryptor {

  /**
   * 복호화하고 문자열로 변환
   *
   * @param ciphertext 암호문
   * @return UTF-8 인코딩된 평문
   */
  String decrypt(String ciphertext);
}
