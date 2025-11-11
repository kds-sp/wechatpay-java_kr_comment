package com.wechat.pay.java.shangmi;

import static com.wechat.pay.java.core.cipher.Constant.HEX;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.auth.WechatPay2Validator;
import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.certificate.InMemoryCertificateProvider;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.cipher.Signer;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/** 국밀 구성 */
public final class SMConfig implements Config {

  /** 가맹점 번호 */
  private final String merchantId;

  /** 가맹점 개인키 */
  private final PrivateKey privateKey;

  /** 가맹점 인증서 일련번호 */
  private final String merchantSerialNumber;

  /** 위챗페이 플랫폼 인증서 Provider */
  private final CertificateProvider certificateProvider;

  private SMConfig(Builder builder) {
    this.merchantId = requireNonNull(builder.merchantId);
    this.privateKey = requireNonNull(builder.privateKey);
    this.merchantSerialNumber = requireNonNull(builder.merchantSerialNumber);
    this.certificateProvider = requireNonNull(builder.certificateProvider);
  }

  /**
   * 민감 정보 암호화기를 생성합니다.
   *
   * @return 민감 정보 암호화기
   */
  @Override
  public PrivacyEncryptor createEncryptor() {
    X509Certificate certificate = certificateProvider.getAvailableCertificate();
    return new SM2PrivacyEncryptor(
        certificate.getPublicKey(), certificate.getSerialNumber().toString(HEX));
  }

  /**
   * 민감 정보 복호화기를 생성합니다.
   *
   * @return 민감 정보 복호화기
   */
  @Override
  public PrivacyDecryptor createDecryptor() {
    return new SM2PrivacyDecryptor(privateKey);
  }

  /**
   * 인증 자격 증명 생성기를 생성합니다.
   *
   * @return 인증 자격 증명 생성기
   */
  @Override
  public Credential createCredential() {
    return new WechatPay2Credential(merchantId, new SM2Signer(merchantSerialNumber, privateKey));
  }

  /**
   * 요청 검증기를 생성합니다.
   *
   * @return 요청 검증기
   */
  @Override
  public Validator createValidator() {
    return new WechatPay2Validator(new SM2Verifier(certificateProvider));
  }

  /**
   * 서명기를 생성합니다.
   *
   * @return 서명기
   */
  @Override
  public Signer createSigner() {
    return new SM2Signer(merchantSerialNumber, privateKey);
  }

  public static class Builder {

    private String merchantId;
    private PrivateKey privateKey;
    private String merchantSerialNumber;
    private List<X509Certificate> wechatPayCertificates = new ArrayList<>();
    private CertificateProvider certificateProvider;

    public Builder merchantId(String merchantId) {
      this.merchantId = merchantId;
      return this;
    }

    public Builder privateKey(String privateKey) {
      return privateKey(SMPemUtil.loadPrivateKeyFromString(privateKey));
    }

    public Builder privateKey(PrivateKey privateKey) {
      this.privateKey = privateKey;
      return this;
    }

    /**
     * 개인키를 설정합니다. 지정된 로컬 개인키 파일에서 가져옵니다.
     *
     * @param privateKeyPath 로컬 개인키 파일 경로
     * @return Builder
     */
    public Builder privateKeyFromPath(String privateKeyPath) {
      return privateKey(SMPemUtil.loadPrivateKeyFromPath(privateKeyPath));
    }

    /**
     * 가맹점 API 인증서 일련번호를 설정합니다.
     *
     * @param merchantSerialNumber 가맹점 API 인증서 일련번호
     * @return Builder
     */
    public Builder merchantSerialNumber(String merchantSerialNumber) {
      this.merchantSerialNumber = merchantSerialNumber;
      return this;
    }

    /**
     * 위챗페이 플랫폼 인증서 제공자를 설정합니다.
     *
     * @param provider 위챗페이 플랫폼 인증서 제공자
     * @return Builder
     */
    public Builder wechatPayCertificateProvider(CertificateProvider provider) {
      this.certificateProvider = provider;
      return this;
    }

    /**
     * 위챗페이 플랫폼 인증서 객체를 추가합니다.
     *
     * @param certificate 위챗페이 플랫폼 인증서 객체
     * @return Builder
     */
    public Builder addWechatPayCertificate(X509Certificate certificate) {
      wechatPayCertificates.add(certificate);
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
     * 위챗페이 플랫폼 인증서를 추가합니다. 지정된 로컬 인증서 파일에서 가져옵니다.
     *
     * @param certificatePath 위챗페이 플랫폼 인증서 경로
     * @return Builder
     */
    public Builder addWechatPayCertificateFromPath(String certificatePath) {
      return addWechatPayCertificate(SMPemUtil.loadX509FromPath(certificatePath));
    }

    /**
     * 모든 위챗페이 플랫폼 인증서를 제거하고 새로운 인증서 목록을 추가합니다.
     *
     * @param wechatPayCertificates 위챗페이 플랫폼 인증서 목록
     * @return Builder
     */
    public Builder wechatPayCertificates(List<X509Certificate> wechatPayCertificates) {
      this.wechatPayCertificates = wechatPayCertificates;
      return this;
    }

    public SMConfig build() {
      if (certificateProvider == null) {
        wechatPayCertificateProvider(new InMemoryCertificateProvider(wechatPayCertificates));
      }
      return new SMConfig(this);
    }
  }
}
