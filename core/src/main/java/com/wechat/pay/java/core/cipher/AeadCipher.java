package com.wechat.pay.java.core.cipher;

/** 인증 암호화기 */
public interface AeadCipher {
  /**
   * 암호화하고 문자열로 변환
   *
   * @param associatedData AAD, 추가 인증 암호화 데이터, 비어 있을 수 있음
   * @param nonce IV, 랜덤 문자열 초기화 벡터
   * @param plaintext 평문
   * @return Base64 인코딩된 암호문
   */
  String encrypt(byte[] associatedData, byte[] nonce, byte[] plaintext);

  /**
   * 복호화하고 문자열로 변환
   *
   * @param associatedData AAD, 추가 인증 암호화 데이터, 비어 있을 수 있음
   * @param nonce IV, 랜덤 문자열 초기화 벡터
   * @param ciphertext 암호문
   * @return UTF-8 인코딩된 평문
   */
  String decrypt(byte[] associatedData, byte[] nonce, byte[] ciphertext);
}
