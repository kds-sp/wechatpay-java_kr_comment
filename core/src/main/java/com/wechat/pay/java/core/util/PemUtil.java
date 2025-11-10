package com.wechat.pay.java.core.util;

import static com.wechat.pay.java.core.cipher.Constant.HEX;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/** PEM 유틸리티 */
public class PemUtil {

  private PemUtil() {}

  /**
   * 문자열에서 RSA 개인키 로드
   *
   * @param keyString 개인키 문자열
   * @return RSA 개인키
   */
  public static PrivateKey loadPrivateKeyFromString(String keyString) {
    try {
      keyString =
          keyString
              .replace("-----BEGIN PRIVATE KEY-----", "")
              .replace("-----END PRIVATE KEY-----", "")
              .replaceAll("\\s+", "");
      return KeyFactory.getInstance("RSA")
          .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyString)));
    } catch (NoSuchAlgorithmException e) {
      throw new UnsupportedOperationException(e);
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * 문자열에서 지정된 알고리즘의 개인키 로드
   *
   * @param keyString 개인키 문자열
   * @param algorithm 개인키 알고리즘
   * @param provider the provider
   * @return 개인키
   */
  public static PrivateKey loadPrivateKeyFromString(
      String keyString, String algorithm, String provider) {
    try {
      keyString =
          keyString
              .replace("-----BEGIN PRIVATE KEY-----", "")
              .replace("-----END PRIVATE KEY-----", "")
              .replaceAll("\\s+", "");
      return KeyFactory.getInstance(algorithm, provider)
          .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyString)));
    } catch (NoSuchAlgorithmException e) {
      throw new UnsupportedOperationException(e);
    } catch (InvalidKeySpecException | NoSuchProviderException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * 문자열에서 RSA 공개키 로드
   *
   * @param keyString 공개키 문자열
   * @return RSA 공개키
   */
  public static PublicKey loadPublicKeyFromString(String keyString) {
    try {
      keyString =
          keyString
              .replace("-----BEGIN PUBLIC KEY-----", "")
              .replace("-----END PUBLIC KEY-----", "")
              .replaceAll("\\s+", "");
      return KeyFactory.getInstance("RSA")
          .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(keyString)));
    } catch (NoSuchAlgorithmException e) {
      throw new UnsupportedOperationException(e);
    } catch (InvalidKeySpecException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * 파일 경로에서 RSA 개인키 로드
   *
   * @param keyPath 개인키 경로
   * @return RSA 개인키
   */
  public static PrivateKey loadPrivateKeyFromPath(String keyPath) {
    return loadPrivateKeyFromString(readKeyStringFromPath(keyPath));
  }

  /**
   * 파일 경로에서 지정된 알고리즘의 개인키 로드
   *
   * @param keyPath 개인키 경로
   * @param algorithm 개인키 알고리즘
   * @param provider the provider
   * @return 개인키
   */
  public static PrivateKey loadPrivateKeyFromPath(
      String keyPath, String algorithm, String provider) {
    return loadPrivateKeyFromString(readKeyStringFromPath(keyPath), algorithm, provider);
  }

  /**
   * 파일 경로에서 RSA 공개키 로드
   *
   * @param keyPath 공개키 경로
   * @return RSA 공개키
   */
  public static PublicKey loadPublicKeyFromPath(String keyPath) {
    return loadPublicKeyFromString(readKeyStringFromPath(keyPath));
  }

  private static String readKeyStringFromPath(String keyPath) {
    try (FileInputStream inputStream = new FileInputStream(keyPath)) {
      return IOUtil.toString(inputStream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * 입력 스트림에서 X.509 인증서 로드
   *
   * @param inputStream 인증서 입력 스트림
   * @return X.509 인증서
   */
  public static X509Certificate loadX509FromStream(InputStream inputStream) {
    try {
      return (X509Certificate)
          CertificateFactory.getInstance("X.509").generateCertificate(inputStream);
    } catch (CertificateException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * 입력 스트림에서 X.509 인증서 로드
   *
   * @param inputStream 인증서 입력 스트림
   * @param provider the provider
   * @return X.509 인증서
   */
  public static X509Certificate loadX509FromStream(InputStream inputStream, String provider) {
    try {
      return (X509Certificate)
          CertificateFactory.getInstance("X.509", provider).generateCertificate(inputStream);
    } catch (CertificateException | NoSuchProviderException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * 파일 경로에서 X.509 인증서 로드
   *
   * @param certificatePath 인증서 파일 경로
   * @return X.509 인증서
   */
  public static X509Certificate loadX509FromPath(String certificatePath) {
    try (FileInputStream inputStream = new FileInputStream(certificatePath)) {
      return loadX509FromStream(inputStream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * 파일 경로에서 X.509 인증서 로드
   *
   * @param certificatePath 인증서 파일 경로
   * @param provider the provider
   * @return X.509 인증서
   */
  public static X509Certificate loadX509FromPath(String certificatePath, String provider) {
    try (FileInputStream inputStream = new FileInputStream(certificatePath)) {
      return loadX509FromStream(inputStream, provider);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * 문자열에서 X.509 인증서 로드
   *
   * @param certificateString 인증서 문자열
   * @return X.509 인증서
   */
  public static X509Certificate loadX509FromString(String certificateString) {
    try (ByteArrayInputStream inputStream =
        new ByteArrayInputStream(certificateString.getBytes(StandardCharsets.UTF_8))) {
      return loadX509FromStream(inputStream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * 문자열에서 X.509 인증서 로드
   *
   * @param certificateString 인증서 문자열
   * @param provider the provider
   * @return X.509 인증서
   */
  public static X509Certificate loadX509FromString(String certificateString, String provider) {
    try (ByteArrayInputStream inputStream =
        new ByteArrayInputStream(certificateString.getBytes(StandardCharsets.UTF_8))) {
      return loadX509FromStream(inputStream, provider);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static String getSerialNumber(X509Certificate certificate) {
    return certificate.getSerialNumber().toString(HEX).toUpperCase();
  }
}
