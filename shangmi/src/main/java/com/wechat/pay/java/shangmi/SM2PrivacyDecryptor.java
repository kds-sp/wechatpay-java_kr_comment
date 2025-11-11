package com.wechat.pay.java.shangmi;

import com.tencent.kona.KonaProvider;
import com.wechat.pay.java.core.cipher.AbstractPrivacyDecryptor;
import java.security.PrivateKey;
import java.security.Security;

public final class SM2PrivacyDecryptor extends AbstractPrivacyDecryptor {

  static {
    Security.addProvider(new KonaProvider());
  }

  /**
   * @param privateKey 가맹점 API 개인키
   */
  public SM2PrivacyDecryptor(PrivateKey privateKey) {
    super("SM2", privateKey);
  }
}
