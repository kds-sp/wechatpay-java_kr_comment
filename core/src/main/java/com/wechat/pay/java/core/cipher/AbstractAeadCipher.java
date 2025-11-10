package com.wechat.pay.java.core.cipher;

import com.wechat.pay.java.core.exception.DecryptionException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/** 연관 데이터를 포함한 인증 암호화기 */
public abstract class AbstractAeadCipher implements AeadCipher {

  private final String algorithm;
  private final String transformation;
  private final int tagLengthBit;
  private final byte[] key;

  protected AbstractAeadCipher(
      String algorithm, String transformation, int tagLengthBit, byte[] key) {
    this.algorithm = algorithm;
    this.transformation = transformation;
    this.tagLengthBit = tagLengthBit;
    this.key = key;
  }

  /**
   * 암호화하고 문자열로 변환
   *
   * @param associatedData AAD, 추가 인증 암호화 데이터, 비어 있을 수 있음
   * @param nonce IV, 랜덤 문자열 초기화 벡터
   * @param plaintext 평문
   * @return Base64 인코딩된 암호문
   */
  public String encrypt(byte[] associatedData, byte[] nonce, byte[] plaintext) {
    try {
      javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(transformation);
      cipher.init(
          javax.crypto.Cipher.ENCRYPT_MODE,
          new SecretKeySpec(key, algorithm),
          new GCMParameterSpec(tagLengthBit, nonce));
      if (associatedData != null) {
        cipher.updateAAD(associatedData);
      }
      return Base64.getEncoder().encodeToString(cipher.doFinal(plaintext));
    } catch (InvalidKeyException
        | InvalidAlgorithmParameterException
        | BadPaddingException
        | IllegalBlockSizeException
        | NoSuchAlgorithmException
        | NoSuchPaddingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * 복호화하고 문자열로 변환
   *
   * @param associatedData AAD, 추가 인증 암호화 데이터, 비어 있을 수 있음
   * @param nonce IV, 랜덤 문자열 초기화 벡터
   * @param ciphertext 암호문
   * @return UTF-8 인코딩된 평문
   */
  public String decrypt(byte[] associatedData, byte[] nonce, byte[] ciphertext) {
    try {
      javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(transformation);
      cipher.init(
          javax.crypto.Cipher.DECRYPT_MODE,
          new SecretKeySpec(key, algorithm),
          new GCMParameterSpec(tagLengthBit, nonce));
      if (associatedData != null) {
        cipher.updateAAD(associatedData);
      }
      return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
    } catch (InvalidKeyException
        | InvalidAlgorithmParameterException
        | NoSuchAlgorithmException
        | NoSuchPaddingException e) {
      throw new IllegalArgumentException(e);
    } catch (BadPaddingException | IllegalBlockSizeException e) {
      throw new DecryptionException("Decryption failed", e);
    }
  }
}
