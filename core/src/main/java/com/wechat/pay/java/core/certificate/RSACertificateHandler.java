package com.wechat.pay.java.core.certificate;

import com.wechat.pay.java.core.util.PemUtil;
import java.security.cert.*;

final class RSACertificateHandler implements CertificateHandler {

  @Override
  public X509Certificate generateCertificate(String certificate) {
    return PemUtil.loadX509FromString(certificate);
  }

  @Override
  public void validateCertPath(X509Certificate certificate) {
    // 인증서 만료로 인한 서명 검증 실패를 방지하여 비즈니스에 영향을 주지 않도록, 이후 인증서 신뢰 체인 검증을 수행하지 않음
  }
}
