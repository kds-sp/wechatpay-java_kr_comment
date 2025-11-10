package com.wechat.pay.java.core.util;

import java.security.SecureRandom;

/** 랜덤 문자열 생성 유틸리티 */
public class NonceUtil {

  private NonceUtil() {}

  private static final char[] SYMBOLS =
      "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  private static final SecureRandom random = new SecureRandom();

  /**
   * SecureRandom을 사용하여 랜덤 문자열 생성
   *
   * @param length 랜덤 문자열 길이
   * @return nonce 랜덤 문자열
   */
  public static String createNonce(int length) {
    char[] buf = new char[length];
    for (int i = 0; i < length; ++i) {
      buf[i] = SYMBOLS[random.nextInt(SYMBOLS.length)];
    }
    return new String(buf);
  }
}
