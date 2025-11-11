package com.wechat.pay.java.shangmi;

import com.tencent.kona.KonaProvider;
import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.cipher.AbstractVerifier;
import java.security.Security;

/** 국밀 서명 검증기 */
public class SM2Verifier extends AbstractVerifier {

  static {
    Security.addProvider(new KonaProvider());
  }

  /**
   * SM2Verifier 생성자
   *
   * @param certificateProvider 서명 검증에 사용되는 위챗페이 플랫폼 인증서 관리자, null이 아님
   */
  public SM2Verifier(CertificateProvider certificateProvider) {

    super("SM2", certificateProvider);
  }
}
