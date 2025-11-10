package com.wechat.pay.java.core.notification;

import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.AbstractRSAConfigBuilder;
import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.RSAAutoCertificateProvider;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.http.HttpClient;
import java.nio.charset.StandardCharsets;

/**
 * 알림 콜백 설정 클래스
 *
 * @deprecated RSAAutoCertificateConfig를 사용하세요. 버전 v0.2.4부터 이 클래스는 불필요하며 기능이 불완전합니다.
 *     개발자는 가능한 한 빨리 마이그레이션해야 합니다. 향후 어느 시점에 이 폐기된 코드를 제거할 예정입니다.
 */
@Deprecated
public final class AutoCertificateNotificationConfig extends AbstractNotificationConfig {

  private AutoCertificateNotificationConfig(
      CertificateProvider certificateProvider, AeadCipher aeadAesCipher) {
    super(RSA_SIGN_TYPE, AES_CIPHER_ALGORITHM, certificateProvider, aeadAesCipher);
  }

  public static class Builder extends AbstractRSAConfigBuilder<Builder> {
    protected HttpClient httpClient;
    protected byte[] apiV3Key;

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    @Override
    protected Builder self() {
      return this;
    }

    public AutoCertificateNotificationConfig build() {
      RSAAutoCertificateProvider.Builder builder =
          new RSAAutoCertificateProvider.Builder()
              .apiV3Key(requireNonNull(apiV3Key))
              .privateKey(requireNonNull(privateKey))
              .merchantId(requireNonNull(merchantId))
              .merchantSerialNumber(requireNonNull(merchantSerialNumber));
      if (httpClient != null) {
        builder.httpClient(httpClient);
      }
      return new AutoCertificateNotificationConfig(
          builder.build(), new AeadAesCipher(requireNonNull(apiV3Key)));
    }
  }
}
