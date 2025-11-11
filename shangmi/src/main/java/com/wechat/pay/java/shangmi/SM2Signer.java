package com.wechat.pay.java.shangmi;

import com.tencent.kona.KonaProvider;
import com.wechat.pay.java.core.cipher.AbstractSigner;
import java.security.PrivateKey;
import java.security.Security;

/** 국밀 서명기 */
public class SM2Signer extends AbstractSigner {
  static {
    Security.addProvider(new KonaProvider());
  }

  /**
   * SM2Signer 생성자
   *
   * @param certificateSerialNumber 가맹점 API 인증서 일련번호
   * @param privateKey 가맹점 API 개인키
   */
  public SM2Signer(String certificateSerialNumber, PrivateKey privateKey) {
    super("SM2-WITH-SM3", "SM2", certificateSerialNumber, privateKey);
  }
}
