package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.RSAAutoCertificateProvider;
import com.wechat.pay.java.core.cipher.*;
import com.wechat.pay.java.core.http.AbstractHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.notification.NotificationConfig;
import java.nio.charset.StandardCharsets;

/**
 * 플랫폼 인증서를 자동으로 다운로드하고 업데이트하는 기능을 가진 RSA 설정 클래스. 매번 생성 시 전달된 가맹점 파라미터를 사용하여 즉시 위챗페이 플랫폼 인증서를 다운로드합니다. 다운로드가 성공하면 SDK는 가맹점 파라미터를
 * AutoCertificateService에 등록하거나 업데이트합니다. 다운로드가 실패하면 예외가 발생합니다. 성능 향상을 위해 설정 클래스를 전역 변수로 사용하여 불필요한 인증서 다운로드를 줄이고 리소스 낭비를 방지하는 것을 권장합니다
 */
public final class RSAAutoCertificateConfig extends AbstractRSAConfig
    implements NotificationConfig {

  private final CertificateProvider certificateProvider;
  private final AeadCipher aeadCipher;

  private RSAAutoCertificateConfig(Builder builder) {
    super(
        builder.merchantId,
        builder.privateKey,
        builder.merchantSerialNumber,
        builder.certificateProvider);
    this.certificateProvider = builder.certificateProvider;
    this.aeadCipher = new AeadAesCipher(builder.apiV3Key);
  }

  /**
   * 서명 타입 가져오기
   *
   * @return 서명 타입
   */
  @Override
  public String getSignType() {
    return RSA_SIGN_TYPE;
  }

  /**
   * 인증 암호화/복호화기 타입 가져오기
   *
   * @return 인증 암호화/복호화기 타입
   */
  @Override
  public String getCipherType() {
    return AES_CIPHER_ALGORITHM;
  }

  /**
   * 서명 검증기 생성
   *
   * @return 서명 검증기
   */
  @Override
  public Verifier createVerifier() {
    return new RSAVerifier(certificateProvider);
  }

  /**
   * 인증 암호화/복호화기 생성
   *
   * @return 인증 암호화/복호화기
   */
  @Override
  public AeadCipher createAeadCipher() {
    return aeadCipher;
  }

  public static class Builder extends AbstractRSAConfigBuilder<Builder> {
    protected HttpClient httpClient;
    protected byte[] apiV3Key;
    protected CertificateProvider certificateProvider;
    protected AbstractHttpClientBuilder<?> httpClientBuilder;

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return self();
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder httpClientBuilder(AbstractHttpClientBuilder<?> builder) {
      httpClientBuilder = builder;
      return this;
    }

    @Override
    protected Builder self() {
      return this;
    }

    public RSAAutoCertificateConfig build() {
      RSAAutoCertificateProvider.Builder providerBuilder =
          new RSAAutoCertificateProvider.Builder()
              .merchantId(requireNonNull(merchantId))
              .apiV3Key(requireNonNull(apiV3Key))
              .privateKey(requireNonNull(privateKey))
              .merchantSerialNumber(requireNonNull(merchantSerialNumber));

      if (httpClient != null) {
        providerBuilder.httpClient(httpClient);
      }

      if (httpClientBuilder != null) {
        providerBuilder.httpClientBuilder(httpClientBuilder);
      }

      certificateProvider = providerBuilder.build();

      return new RSAAutoCertificateConfig(this);
    }
  }
}
