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
import com.wechat.pay.java.core.util.PemUtil;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

/** 알림 콜백 설정 클래스, 이 클래스는 가맹점이 플랫폼 인증서에서 플랫폼 공개키로 전환하는 그레이스케일 단계에서만 사용하며, 그레이스케일 완료 후 RSAPublicKeyNotificationConfig로 전환해야 함 */
public final class RSACombinedNotificationConfig extends AbstractNotificationConfig {

  private RSACombinedNotificationConfig(
      CertificateProvider certificateProvider,
      PublicKey publicKey,
      String publicKeyId,
      AeadCipher aeadAesCipher) {
    super(
        RSA_SIGN_TYPE,
        AES_CIPHER_ALGORITHM,
        certificateProvider,
        publicKey,
        publicKeyId,
        aeadAesCipher);
  }

  public static class Builder extends AbstractRSAConfigBuilder<Builder> {
    protected HttpClient httpClient;
    protected byte[] apiV3Key;

    private PublicKey publicKey;
    private String publicKeyId;

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder publicKey(String publicKey) {
      this.publicKey = PemUtil.loadPublicKeyFromString(publicKey);
      return this;
    }

    public Builder publicKey(PublicKey publicKey) {
      this.publicKey = publicKey;
      return this;
    }

    public Builder publicKeyFromPath(String publicKeyPath) {
      this.publicKey = PemUtil.loadPublicKeyFromPath(publicKeyPath);
      return this;
    }

    public Builder publicKeyId(String publicKeyId) {
      this.publicKeyId = publicKeyId;
      return this;
    }

    @Override
    protected Builder self() {
      return this;
    }

    public RSACombinedNotificationConfig build() {

      RSAAutoCertificateProvider.Builder builder =
          new RSAAutoCertificateProvider.Builder()
              .apiV3Key(requireNonNull(apiV3Key))
              .privateKey(requireNonNull(privateKey))
              .merchantId(requireNonNull(merchantId))
              .merchantSerialNumber(requireNonNull(merchantSerialNumber));
      if (httpClient != null) {
        builder.httpClient(httpClient);
      }
      return new RSACombinedNotificationConfig(
          builder.build(),
          requireNonNull(publicKey),
          requireNonNull(publicKeyId),
          new AeadAesCipher(requireNonNull(apiV3Key)));
    }
  }
}
