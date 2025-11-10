package com.wechat.pay.java.core.cipher;

/** 서명 검증기 */
public interface Verifier {

  /**
   * 서명 검증
   *
   * @param serialNumber 서명 검증에 사용되는 인증서 시리얼 번호 또는 공개키 id
   * @param message 서명 정보
   * @param signature 검증할 서명
   * @return 검증 통과 여부
   */
  boolean verify(String serialNumber, String message, String signature);

  String getSerialNumber();
}
