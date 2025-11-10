package com.wechat.pay.java.core;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.cipher.Signer;

/** 위챗페이 서비스 호출에 필요한 설정 */
public interface Config {

  /**
   * 민감 정보 암호화기 생성
   *
   * @return 민감 정보 암호화기
   */
  PrivacyEncryptor createEncryptor();

  /**
   * 민감 정보 복호화기 생성
   *
   * @return 민감 정보 복호화기
   */
  PrivacyDecryptor createDecryptor();

  /**
   * 인증 자격 증명 생성기 생성
   *
   * @return 인증 자격 증명 생성기
   */
  Credential createCredential();

  /**
   * 요청 검증기 생성
   *
   * @return 요청 검증기
   */
  Validator createValidator();

  /**
   * 서명기 생성
   *
   * @return 서명기
   */
  Signer createSigner();
}
