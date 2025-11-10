package com.wechat.pay.java.core.util;

/** 문자열 유틸리티 클래스 */
public class StringUtil {
  private StringUtil() {}

  /**
   * 주어진 객체를 문자열로 변환하며, 각 줄을 4개의 공백으로 들여쓰기 (첫 줄 제외)
   *
   * @param o 객체
   * @return 포맷된 문자열
   */
  public static String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
