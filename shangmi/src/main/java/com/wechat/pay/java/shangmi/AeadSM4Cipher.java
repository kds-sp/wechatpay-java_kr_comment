package com.wechat.pay.java.shangmi;

import static com.wechat.pay.java.core.cipher.Constant.HEX;

import com.tencent.kona.KonaProvider;
import com.wechat.pay.java.core.cipher.AbstractAeadCipher;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;

/** 국밀 SM4Gcm 암호화/복호화기 */
public final class AeadSM4Cipher extends AbstractAeadCipher {

  static {
    Security.addProvider(new KonaProvider());
  }

  private static final String TRANSFORMATION = "SM4/GCM/NoPadding";
  private static final int TAG_LENGTH_BIT = 128;
  private static final String ALGORITHM = "SM4";

  /**
   * @param apiV3Key APIv3 키
   */
  public AeadSM4Cipher(byte[] apiV3Key) {
    super(ALGORITHM, TRANSFORMATION, TAG_LENGTH_BIT, covertSM4Key(apiV3Key));
  }

  /**
   * SM3 다이제스트의 앞 128비트를 취하여 APIv3Key를 SM4에서 사용하는 키로 변환합니다.
   *
   * @param apiV3Key APIv3Key
   * @return SM4Gcm의 키
   */
  private static byte[] covertSM4Key(byte[] apiV3Key) {
    try {
      MessageDigest md = MessageDigest.getInstance("SM3", KonaProvider.NAME);
      return Arrays.copyOf(md.digest(apiV3Key), HEX);
    } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
      throw new IllegalStateException(e);
    }
  }
}
