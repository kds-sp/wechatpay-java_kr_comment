package com.wechat.pay.java.core.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlEncoder {

  /**
   * 파라미터를 URL 인코딩
   *
   * @param string 인코딩할 문자열
   * @return 인코딩된 문자열
   */
  public static String urlEncode(String string) {
    try {
      return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
