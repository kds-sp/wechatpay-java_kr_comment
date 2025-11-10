package com.wechat.pay.java.core.cipher;

import java.security.PublicKey;

/** RSA 민감 정보 암호화기 */
public final class RSAPrivacyEncryptor extends AbstractPrivacyEncryptor {

  /**
   * RSAPrivacyEncryptor 생성자
   *
   * @param publicKey 요청의 민감 정보 암호화에 사용되는 위챗페이 공개키
   * @param wechatpaySerialNumber 위챗페이 플랫폼 인증서의 인증서 시리얼 번호
   */
  public RSAPrivacyEncryptor(PublicKey publicKey, String wechatpaySerialNumber) {
    super("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", publicKey, wechatpaySerialNumber);
  }
}
