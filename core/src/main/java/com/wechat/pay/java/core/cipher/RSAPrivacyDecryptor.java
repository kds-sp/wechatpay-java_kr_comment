package com.wechat.pay.java.core.cipher;

import java.security.PrivateKey;

/** RSA 민감 정보 복호화기 */
public final class RSAPrivacyDecryptor extends AbstractPrivacyDecryptor {

  /**
   * RSAPrivacyDecryptor 생성자
   *
   * @param privateKey 응답의 민감 정보 복호화에 사용되는 가맹점 개인키
   */
  public RSAPrivacyDecryptor(PrivateKey privateKey) {
    super("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", privateKey);
  }
}
