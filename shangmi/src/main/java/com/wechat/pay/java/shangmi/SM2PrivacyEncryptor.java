package com.wechat.pay.java.shangmi;

import com.tencent.kona.KonaProvider;
import com.wechat.pay.java.core.cipher.AbstractPrivacyEncryptor;
import java.security.PublicKey;
import java.security.Security;

/** 국밀 민감 정보 암호화기 */
public final class SM2PrivacyEncryptor extends AbstractPrivacyEncryptor {

  static {
    Security.addProvider(new KonaProvider());
  }

  /**
   * @param publicKey 요청의 민감 정보 암호화 시 사용되는 위챗페이 국밀 공개키
   * @param wechatpaySerial 위챗페이 국밀 플랫폼 인증서의 인증서 일련번호
   */
  public SM2PrivacyEncryptor(PublicKey publicKey, String wechatpaySerial) {
    super("SM2", publicKey, wechatpaySerial);
  }
}
