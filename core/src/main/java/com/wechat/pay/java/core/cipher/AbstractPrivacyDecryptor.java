package com.wechat.pay.java.core.cipher;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.exception.DecryptionException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public abstract class AbstractPrivacyDecryptor implements PrivacyDecryptor {
  private final PrivateKey privateKey;
  private final Cipher cipher;

  /**
   * 민감 정보 복호화의 추상 클래스 구성
   *
   * @param transformation 암호화에 사용되는 모드
   * @param privateKey 암호화에 사용되는 개인키
   */
  protected AbstractPrivacyDecryptor(String transformation, PrivateKey privateKey) {
    this.privateKey = requireNonNull(privateKey);
    try {
      cipher = Cipher.getInstance(transformation);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new IllegalArgumentException(
          "The current Java environment does not support " + transformation, e);
    }
  }

  @Override
  public String decrypt(String ciphertext) {
    requireNonNull(ciphertext);
    try {
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      return new String(
          cipher.doFinal(Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("The given private key is invalid for decryption", e);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new DecryptionException("Decryption failed", e);
    }
  }
}
