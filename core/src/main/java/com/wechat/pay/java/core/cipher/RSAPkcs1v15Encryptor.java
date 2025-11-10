package com.wechat.pay.java.core.cipher;

import java.security.PublicKey;

/** RSA-Pkcs1v15 민감 정보 암호화기 */
public final class RSAPkcs1v15Encryptor extends AbstractPrivacyEncryptor {

  public RSAPkcs1v15Encryptor(PublicKey publicKey, String wechatpaySerialNumber) {
    super("RSA/ECB/PKCS1Padding", publicKey, wechatpaySerialNumber);
  }
}
