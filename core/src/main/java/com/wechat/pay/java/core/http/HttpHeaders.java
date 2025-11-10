package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/** HTTP 요청 헤더 */
public class HttpHeaders {

  private final Map<String, String> headers = new HashMap<>();

  public HttpHeaders() {}

  public HttpHeaders(Map<String, String> headers) {
    this.headers.putAll(headers);
  }

  /**
   * 요청 헤더 파라미터 추가
   *
   * @param name 파라미터 이름
   * @param value 파라미터 값
   */
  public void addHeader(String name, String value) {
    headers.put(requireNonNull(name), requireNonNull(value));
  }

  /**
   * 요청 헤더 파라미터 가져오기
   *
   * @param name 파라미터 이름
   * @return 파라미터 값
   */
  public String getHeader(String name) {
    return headers.get(name);
  }

  /**
   * 요청 헤더 가져오기
   *
   * @return 요청 헤더
   */
  public Map<String, String> getHeaders() {
    return new HashMap<>(headers);
  }

  @Override
  public String toString() {
    if (headers.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (Entry<String, String> entry : headers.entrySet()) {
      sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
    }
    sb.delete(sb.length() - 1, sb.length()).append("\n");
    return sb.toString();
  }
}
