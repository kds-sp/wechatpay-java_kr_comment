package com.wechat.pay.java.core.certificate;

import com.wechat.pay.java.core.cipher.Constant;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/** 인증서 제공기의 간단한 구현, 인증서는 메모리 ConcurrentHashMap에 저장됨 */
public final class InMemoryCertificateProvider implements CertificateProvider {

  private final ConcurrentHashMap<BigInteger, X509Certificate> certificates =
      new ConcurrentHashMap<>();
  private final X509Certificate availableCertificate;

  public InMemoryCertificateProvider(List<X509Certificate> certificates) {
    if (certificates.isEmpty()) {
      throw new IllegalArgumentException("The parameter list of constructor is empty.");
    }
    // 가져온 모든 인증서가 사용 가능하다고 가정하고, 가장 오래 사용할 수 있는 것을 선택
    X509Certificate longest = null;
    for (X509Certificate item : certificates) {
      this.certificates.put(item.getSerialNumber(), item);
      if (longest == null || item.getNotAfter().after(longest.getNotAfter())) {
        longest = item;
      }
    }
    availableCertificate = longest;
  }

  /**
   * 인증서 시리얼 번호에 따라 인증서 가져오기
   *
   * @param serialNumber 위챗페이 플랫폼 인증서 시리얼 번호
   * @return X.509 인증서 인스턴스
   */
  @Override
  public X509Certificate getCertificate(String serialNumber) {
    BigInteger key = new BigInteger(serialNumber, Constant.HEX);
    return certificates.get(key);
  }

  /**
   * 최신 사용 가능한 위챗페이 플랫폼 인증서 가져오기
   *
   * @return X.509 인증서 인스턴스
   */
  @Override
  public X509Certificate getAvailableCertificate() {
    return availableCertificate;
  }
}
