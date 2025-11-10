package com.wechat.pay.java.core.cipher;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.certificate.CertificateProvider;
import com.wechat.pay.java.core.util.PemUtil;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractVerifier implements Verifier {

  protected static final Logger logger = LoggerFactory.getLogger(AbstractVerifier.class);
  protected final CertificateProvider certificateProvider;
  protected final PublicKey publicKey;
  protected final String publicKeyId;
  protected final String algorithmName;

  /**
   * AbstractVerifier 생성자
   *
   * @param algorithmName Signature 객체를 가져올 때 지정하는 알고리즘, 예: SHA256withRSA
   * @param certificateProvider 서명 검증에 사용되는 위챗페이 플랫폼 인증서 관리자, null이 아님
   */
  protected AbstractVerifier(String algorithmName, CertificateProvider certificateProvider) {
    this.certificateProvider = requireNonNull(certificateProvider);
    this.algorithmName = requireNonNull(algorithmName);
    this.publicKey = null;
    this.publicKeyId = null;
  }

  /**
   * AbstractVerifier 생성자
   *
   * @param algorithmName Signature 객체를 가져올 때 지정하는 알고리즘, 예: SHA256withRSA
   * @param publicKey 서명 검증에 사용되는 위챗페이 플랫폼 공개키, null이 아님
   * @param publicKeyId 서명 검증에 사용되는 위챗페이 플랫폼 공개키 id
   */
  protected AbstractVerifier(String algorithmName, PublicKey publicKey, String publicKeyId) {
    this.publicKey = requireNonNull(publicKey);
    this.publicKeyId = publicKeyId;
    this.algorithmName = requireNonNull(algorithmName);
    this.certificateProvider = null;
  }

  /**
   * AbstractVerifier 생성자, 플랫폼 인증서와 플랫폼 공개키 그레이스케일 전환 단계에서만 사용
   *
   * @param algorithmName Signature 객체를 가져올 때 지정하는 알고리즘, 예: SHA256withRSA
   * @param publicKey 서명 검증에 사용되는 위챗페이 플랫폼 공개키, null이 아님
   * @param publicKeyId 서명 검증에 사용되는 위챗페이 플랫폼 공개키 id
   * @param certificateProvider 서명 검증에 사용되는 위챗페이 플랫폼 인증서 관리자, null이 아님
   */
  protected AbstractVerifier(
      String algorithmName,
      PublicKey publicKey,
      String publicKeyId,
      CertificateProvider certificateProvider) {
    this.publicKey = requireNonNull(publicKey);
    this.publicKeyId = publicKeyId;
    this.algorithmName = requireNonNull(algorithmName);
    this.certificateProvider = requireNonNull(certificateProvider);
  }

  protected boolean verify(X509Certificate certificate, String message, String signature) {
    try {
      Signature sign = Signature.getInstance(algorithmName);
      sign.initVerify(certificate);
      sign.update(message.getBytes(StandardCharsets.UTF_8));
      return sign.verify(Base64.getDecoder().decode(signature));
    } catch (SignatureException e) {
      return false;
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("verify uses an illegal certificate.", e);
    } catch (NoSuchAlgorithmException e) {
      throw new UnsupportedOperationException(
          "The current Java environment does not support " + algorithmName, e);
    }
  }

  private boolean verify(String message, String signature) {
    try {
      Signature sign = Signature.getInstance(algorithmName);
      sign.initVerify(publicKey);
      sign.update(message.getBytes(StandardCharsets.UTF_8));
      return sign.verify(Base64.getDecoder().decode(signature));
    } catch (SignatureException e) {
      return false;
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("verify uses an illegal publickey.", e);
    } catch (NoSuchAlgorithmException e) {
      throw new UnsupportedOperationException(
          "The current Java environment does not support " + algorithmName, e);
    }
  }

  @Override
  public boolean verify(String serialNumber, String message, String signature) {
    // 공개키가 null이 아니면 공개키로 서명 검증
    if (publicKey != null) {
      if (serialNumber.equals(publicKeyId)) {
        return verify(message, signature);
      }
      // 인증서가 null이면 전달된 publicKeyId가 잘못된 것이고, null이 아니면 계속 인증서로 서명 검증
      if (certificateProvider == null) {
        logger.error(
            "publicKeyId[{}] and serialNumber[{}] are not equal", publicKeyId, serialNumber);
        return false;
      }
    }
    // 인증서로 서명 검증
    requireNonNull(certificateProvider);
    X509Certificate certificate = certificateProvider.getCertificate(serialNumber);
    if (certificate == null) {
      logger.error(
          "Verify the signature and get the WechatPay certificate or publicKey corresponding to "
              + "serialNumber[{}] is empty.",
          serialNumber);
      return false;
    }
    return verify(certificate, message, signature);
  }

  @Override
  public String getSerialNumber() {
    if (publicKey != null) {
      return publicKeyId;
    }

    requireNonNull(certificateProvider);
    return PemUtil.getSerialNumber(certificateProvider.getAvailableCertificate());
  }
}
