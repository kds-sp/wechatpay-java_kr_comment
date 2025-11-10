package com.wechat.pay.java.core.http;

import java.util.Map;
import java.util.Objects;

public class OriginalResponse {

  private final HttpHeaders headers;
  private final HttpRequest request;
  private final String contentType;
  private final int statusCode;
  private final String body;

  private OriginalResponse(
      HttpRequest request, int statusCode, HttpHeaders headers, String contentType, String body) {
    this.request = request;
    this.contentType = contentType;
    this.headers = headers;
    this.statusCode = statusCode;
    this.body = body;
  }

  /**
   * 응답 헤더 가져오기
   *
   * @return 응답 헤더
   */
  public HttpHeaders getHeaders() {
    return headers;
  }

  /**
   * 응답에 해당하는 요청 가져오기
   *
   * @return 요청
   */
  public HttpRequest getRequest() {
    return request;
  }

  /**
   * 응답 본문 가져오기
   *
   * @return 응답 본문
   */
  public String getBody() {
    return body;
  }

  public String getContentType() {
    return contentType;
  }

  public int getStatusCode() {
    return statusCode;
  }

  /** OriginalResponse Builder */
  public static class Builder {

    private HttpRequest request;
    private int statusCode;
    private HttpHeaders headers;
    private String contentType;

    private String jsonBody;

    /**
     * 응답 contentType 설정
     *
     * @param contentType contentType
     * @return OriginalResponse Builder
     */
    public Builder contentType(String contentType) {
      this.contentType = contentType;
      return this;
    }

    /**
     * 응답 headers 설정
     *
     * @param headers headers
     * @return OriginalResponse Builder
     */
    public Builder headers(Map<String, String> headers) {
      this.headers = new HttpHeaders(headers);
      return this;
    }

    /**
     * request 설정
     *
     * @param request request
     * @return OriginalResponse Builder
     */
    public Builder request(HttpRequest request) {
      this.request = request;
      return this;
    }

    /**
     * 응답 statusCode 설정
     *
     * @param statusCode statusCode
     * @return OriginalResponse Builder
     */
    public Builder statusCode(int statusCode) {
      this.statusCode = statusCode;
      return this;
    }

    /**
     * 응답 body 설정
     *
     * @param jsonBody jsonBody
     * @return OriginalResponse Builder
     */
    public Builder body(String jsonBody) {
      this.jsonBody = jsonBody;
      return this;
    }

    /**
     * OriginalResponse 빌드
     *
     * @return OriginalResponse
     */
    public OriginalResponse build() {
      Objects.requireNonNull(request);
      return new OriginalResponse(request, statusCode, headers, contentType, jsonBody);
    }
  }
}
