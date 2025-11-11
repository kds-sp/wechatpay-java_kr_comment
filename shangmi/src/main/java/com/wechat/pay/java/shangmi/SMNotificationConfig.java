package com.wechat.pay.java.shangmi;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.notification.NotificationConfig;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class SMNotificationConfig implements NotificationConfig {
  public static final String SIGN_TYPE = "WECHATPAY2-SM2-WITH-SM3";
  public static final String CIPHER_ALGORITHM = "AEAD_SM4_GCM";
  private static final int API_V3_KEY_LENGTH_BYTE = 32;
  private final CertificateProvider certificateProvider;
  private final byte[] apiV3Key;

  private SMNotificationConfig(Builder builder) {
    this.certificateProvider = requireNonNull(builder.certificateProvider);
    this.apiV3Key = requireNonNull(builder.apiV3Key);
  }

  @Override
  public String getSignType() {
    return SIGN_TYPE;
  }

  @Override
  public String getCipherType() {
    return CIPHER_ALGORITHM;
  }

  @Override
  public Verifier createVerifier() {
    return new SM2Verifier(certificateProvider);
  }

  @Override
  public AeadCipher createAeadCipher() {
    return new AeadSM4Cipher(apiV3Key);
  }

  public static class Builder {

    private CertificateProvider certificateProvider;

    private List<X509Certificate> certificates = new ArrayList<>();
    private byte[] apiV3Key;

    /**
     * 이전의 위챗페이 플랫폼 인증서를 제거하고 새로운 인증서를 추가합니다.
     *
     * @param wechatpayCertificates 위챗페이 플랫폼 인증서 목록
     * @return Builder
     */
    public Builder wechatPayCertificates(List<X509Certificate> wechatpayCertificates) {
      this.certificates = wechatpayCertificates;
      return this;
    }

    /**
     * 위챗페이 플랫폼 인증서 제공자를 설정합니다.
     *
     * @param certificateProvider 위챗페이 플랫폼 인증서 제공자
     * @return Builder
     */
    public Builder wechatPayCertificateProvider(CertificateProvider certificateProvider) {
      this.certificateProvider = certificateProvider;
      return this;
    }

    /**
     * 위챗페이 플랫폼 인증서 객체를 추가합니다.
     *
     * @param certificate 위챗페이 플랫폼 인증서 객체
     * @return Builder
     */
    public Builder addWechatPayCertificate(X509Certificate certificate) {
      certificates.add(certificate);
      return this;
    }

    /**
     * 문자열 형태의 위챗페이 플랫폼 인증서를 추가합니다.
     *
     * @param certificate 위챗페이 플랫폼 인증서
     * @return Builder
     */
    public Builder addWechatPayCertificate(String certificate) {
      return addWechatPayCertificate(SMPemUtil.loadX509FromString(certificate));
    }

    /**
     * APIv3 키를 설정합니다.
     *
     * @param apiV3Key APIv3 키
     * @return Builder
     */
    public Builder apiV3Key(String apiV3Key) {
      if (apiV3Key.length() != API_V3_KEY_LENGTH_BYTE) {
        throw new IllegalArgumentException(
            "The length of apiV3Key is invalid, it should be 32 bytes.");
      }
      this.apiV3Key = apiV3Key.getBytes(StandardCharsets.UTF_8);
      return this;
    }

    public SMNotificationConfig build() {
      if (certificateProvider == null) {
        if (certificates.isEmpty()) {
          throw new IllegalArgumentException(
              "neither certificate provider nor certificates must not be empty");
        }
        certificateProvider = new InMemoryCertificateProvider(certificates);
      }
      return new SMNotificationConfig(this);
    }
  }
}
