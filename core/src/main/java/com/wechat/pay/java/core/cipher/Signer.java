package com.wechat.pay.java.core.cipher;

/** 서명기 */
public interface Signer {

  /**
   * 서명 결과 생성
   *
   * @param message 서명 정보
   * @return 서명 결과
   */
  SignatureResult sign(String message);

  /**
   * 서명 알고리즘 가져오기
   *
   * @return 서명 알고리즘
   */
  String getAlgorithm();
}
