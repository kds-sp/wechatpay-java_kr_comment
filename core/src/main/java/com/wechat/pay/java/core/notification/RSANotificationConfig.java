package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.util.PemUtil;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 알림 콜백 설정 클래스
 *
 * @deprecated RSAAutoCertificateConfig를 사용하세요. 개발자는 가능한 한 빨리 마이그레이션해야 하며, 향후 어느 시점에 이 폐기된 코드를 제거할 예정입니다.
 */
public final class RSANotificationConfig extends AbstractNotificationConfig {

  private RSANotificationConfig(CertificateProvider certificateProvider, AeadCipher aeadCipher) {
    super(RSA_SIGN_TYPE, AES_CIPHER_ALGORITHM, certificateProvider, aeadCipher);
  }

  public static class Builder {

    private List<X509Certificate> certificates;
    private byte[] apiV3Key;

    public Builder certificates(X509Certificate... certificates) {
      this.certificates = Arrays.asList(certificates);
      return this;
    }

    public Builder certificates(String... certificates) {
      List<X509Certificate> certificateList = new ArrayList<>();
      for (String certificateString : certificates) {
        X509Certificate x509Certificate = PemUtil.loadX509FromString(certificateString);
        certificateList.add(x509Certificate);
      }
      this.certificates = certificateList;
      return this;
    }

    public Builder certificatesFromPath(String... certificatePaths) {
      List<X509Certificate> certificateList = new ArrayList<>();
      for (String certificatePath : certificatePaths) {
        X509Certificate x509Certificate = PemUtil.loadX509FromPath(certificatePath);
        certificateList.add(x509Certificate);
      }
      this.certificates = certificateList;
      return this;
    }

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public RSANotificationConfig build() {
      CertificateProvider certificateProvider = new InMemoryCertificateProvider(certificates);
      return new RSANotificationConfig(
          certificateProvider, new AeadAesCipher(requireNonNull(apiV3Key)));
    }
  }
}
