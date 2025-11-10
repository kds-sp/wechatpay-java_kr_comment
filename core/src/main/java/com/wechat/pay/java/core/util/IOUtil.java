package com.wechat.pay.java.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/** I/O 유틸리티 */
public class IOUtil {

  private static final int DEFAULT_BUFFER_SIZE = 8192;

  private IOUtil() {}

  /**
   * 입력 스트림을 바이트 배열로 변환
   *
   * @param inputStream 입력 스트림
   * @return 바이트 배열
   * @throws IOException 바이트 읽기 실패, 스트림 닫기 실패 등
   */
  public static byte[] toByteArray(InputStream inputStream) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    int nRead;
    byte[] data = new byte[DEFAULT_BUFFER_SIZE];
    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }

    return buffer.toByteArray();
  }

  /**
   * 입력 스트림을 문자열로 변환
   *
   * @param inputStream 입력 스트림
   * @return UTF-8 인코딩된 문자열
   * @throws IOException 바이트 읽기 실패, 스트림 닫기 실패 등
   */
  public static String toString(InputStream inputStream) throws IOException {
    return new String(toByteArray(inputStream), StandardCharsets.UTF_8);
  }

  /**
   * 파일 경로에서 문자열 읽기
   *
   * @param path 파일 경로
   * @return UTF-8 인코딩된 문자열
   * @throws IOException 바이트 읽기 실패, 스트림 닫기 실패 등
   */
  public static String loadStringFromPath(String path) throws IOException {
    try (InputStream inputStream = Files.newInputStream(Paths.get(path))) {
      return toString(inputStream);
    }
  }
}
