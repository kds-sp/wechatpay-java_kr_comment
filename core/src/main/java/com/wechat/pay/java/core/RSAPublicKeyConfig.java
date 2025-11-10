package com.wechat.pay.java.core;

import static com.wechat.pay.java.core.notification.Constant.AES_CIPHER_ALGORITHM;
import static com.wechat.pay.java.core.notification.Constant.RSA_SIGN_TYPE;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.RSAVerifier;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.util.PemUtil;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

/** 위챗페이 플랫폼 공개키를 사용하는 RSA 설정 클래스. 매번 생성 시 플랫폼 공개키 및 플랫폼 공개키 id를 전달해야 하며, 플랫폼 인증서를 사용하는 경우 RSAAutoCertificateConfig 클래스를 사용하는 것을 권장 */
public final class RSAPublicKeyConfig extends AbstractRSAConfig implements NotificationConfig {

  private final PublicKey publicKey;
  private final AeadCipher aeadCipher;
  private final String publicKeyId;

  private RSAPublicKeyConfig(Builder builder) {
    super(
        builder.merchantId,
        builder.privateKey,
        builder.merchantSerialNumber,
        builder.publicKey,
        builder.publicKeyId);
    this.publicKey = builder.publicKey;
    this.publicKeyId = builder.publicKeyId;
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
    return new RSAVerifier(publicKey, publicKeyId);
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
    protected byte[] apiV3Key;
    protected PublicKey publicKey;
    protected String publicKeyId;

    public Builder apiV3Key(String apiV3Key) {
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return self();
    }

    public Builder publicKey(String publicKey) {
      this.publicKey = PemUtil.loadPublicKeyFromString(publicKey);
      return self();
    }

    public Builder publicKey(PublicKey publicKey) {
      this.publicKey = publicKey;
      return self();
    }

    public Builder publicKeyFromPath(String publicKeyPath) {
      this.publicKey = PemUtil.loadPublicKeyFromPath(publicKeyPath);
      return self();
    }

    public Builder publicKeyId(String publicKeyId) {
      this.publicKeyId = publicKeyId;
      return self();
    }

    @Override
    protected Builder self() {
      return this;
    }

    public RSAPublicKeyConfig build() {
      requireNonNull(merchantId);
      requireNonNull(publicKey);
      requireNonNull(publicKeyId);
      requireNonNull(privateKey);
      requireNonNull(merchantSerialNumber);

      return new RSAPublicKeyConfig(this);
    }
  }
}
