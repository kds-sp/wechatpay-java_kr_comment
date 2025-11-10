package com.wechat.pay.java.core.cipher;

import java.security.PrivateKey;

/** RSA-Pkcs1v15 민감 정보 복호화기 */
public final class RSAPkcs1v15Decryptor extends AbstractPrivacyDecryptor {

  public RSAPkcs1v15Decryptor(PrivateKey privateKey) {
    super("RSA/ECB/PKCS1Padding", privateKey);
  }
}
