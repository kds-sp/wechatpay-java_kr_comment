package com.wechat.pay.java.core.cipher;

/** 연관 데이터를 포함한 AES 인증 암호화/복호화기 */
public final class AeadAesCipher extends AbstractAeadCipher {

  private static final String TRANSFORMATION = "AES/GCM/NoPadding";

  private static final int TAG_LENGTH_BIT = 128;
  private static final String ALGORITHM = "AES";

  public AeadAesCipher(byte[] key) {
    super(ALGORITHM, TRANSFORMATION, TAG_LENGTH_BIT, key);
  }
}
