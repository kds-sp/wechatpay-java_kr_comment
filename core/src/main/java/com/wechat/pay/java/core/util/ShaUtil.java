package com.wechat.pay.java.core.util;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** SHA 유틸리티 */
public class ShaUtil {

  private ShaUtil() {}

  public static final int BUFFER_SIZE = 1024;
  public static final String SHA1 = "SHA-1";
  public static final String SHA256 = "SHA-256";

  /**
   * SHA1의 HEX 인코딩 메시지 다이제스트 문자열 생성
   *
   * @param inputStream 메시지 입력 스트림
   * @return HEX 인코딩 메시지 다이제스트 문자열
   * @throws IOException 입력 스트림 읽기 실패, 스트림 닫기 실패 등
   */
  public static String getSha1HexString(InputStream inputStream) throws IOException {
    return getShaHexString(inputStream, SHA1);
  }

  /**
   * SHA1의 HEX 인코딩 메시지 다이제스트 문자열 생성
   *
   * @param source 메시지 입력
   * @return HEX 인코딩 메시지 다이제스트 문자열
   */
  public static String getSha1HexString(byte[] source) {
    return getShaHexString(source, SHA1);
  }

  /**
   * SHA256의 HEX 인코딩 메시지 다이제스트 문자열 생성
   *
   * @param inputStream 메시지 입력 스트림
   * @return HEX 인코딩 메시지 다이제스트 문자열
   * @throws IOException 입력 스트림 읽기 실패, 스트림 닫기 실패 등
   */
  public static String getSha256HexString(InputStream inputStream) throws IOException {
    return getShaHexString(inputStream, SHA256);
  }

  /**
   * SHA256의 HEX 인코딩 메시지 다이제스트 문자열 생성
   *
   * @param source 메시지 입력
   * @return HEX 인코딩 메시지 다이제스트 문자열
   */
  public static String getSha256HexString(byte[] source) {
    return getShaHexString(source, SHA256);
  }

  private static String getShaHexString(InputStream inputStream, String algorithm)
      throws IOException {
    byte[] data = new byte[BUFFER_SIZE];
    int nRead;
    try {
      MessageDigest digest = MessageDigest.getInstance(algorithm);
      while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
        digest.update(data, 0, nRead);
      }
      return toHexString(digest.digest());
    } catch (NoSuchAlgorithmException e) {
      throw new SecurityException(e);
    }
  }

  /**
   * SHA 알고리즘의 HEX 인코딩 메시지 다이제스트 문자열 생성
   *
   * @param source 메시지 바이트 배열
   * @param algorithm 구체적인 SHA 알고리즘, 예: SHA-1, SHA-256
   * @return HEX 인코딩 메시지 다이제스트 문자열
   */
  private static String getShaHexString(byte[] source, String algorithm) {
    requireNonNull(source);
    try {
      MessageDigest digest = MessageDigest.getInstance(algorithm);
      digest.update(source);
      return toHexString(digest.digest());
    } catch (NoSuchAlgorithmException e) {
      throw new SecurityException(e);
    }
  }

  /**
   * 바이트 배열을 HEX 인코딩 문자열로 변환
   *
   * @param bytes 바이트 배열
   * @return HEX 인코딩 문자열
   */
  public static String toHexString(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
