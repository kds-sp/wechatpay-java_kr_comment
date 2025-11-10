package com.wechat.pay.java.core.notification;

import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.Verifier;

/** 알림 파싱을 위한 설정 */
public interface NotificationConfig {

  /**
   * 서명 타입 가져오기
   *
   * @return 서명 타입
   */
  String getSignType();

  /**
   * 인증 암호화/복호화기 타입 가져오기
   *
   * @return 인증 암호화/복호화기 타입
   */
  String getCipherType();

  /**
   * 서명 검증기 생성
   *
   * @return 서명 검증기
   */
  Verifier createVerifier();

  /**
   * 인증 암호화/복호화기 생성
   *
   * @return 인증 암호화/복호화기
   */
  AeadCipher createAeadCipher();
}
