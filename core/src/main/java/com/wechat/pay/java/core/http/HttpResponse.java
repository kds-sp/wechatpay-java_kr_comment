package com.wechat.pay.java.core.http;

import com.google.gson.JsonSyntaxException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.util.GsonUtil;
import java.util.Objects;

/**
 * HTTP 응답
 *
 * @param <T> 비즈니스 응답 본문 타입
 */
public final class HttpResponse<T> {

  private final HttpRequest request;
  private final HttpHeaders headers;
  private final ResponseBody body;
  private final T serviceResponse;

  private HttpResponse(
      HttpRequest request, HttpHeaders headers, ResponseBody body, T serviceResponse) {
    this.request = request;
    this.headers = headers;
    this.body = body;
    this.serviceResponse = serviceResponse;
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
  public ResponseBody getBody() {
    return body;
  }

  /**
   * 비즈니스 응답 가져오기
   *
   * @return 비즈니스 응답
   */
  public T getServiceResponse() {
    return serviceResponse;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }

  public static class Builder<T> {

    private OriginalResponse originalResponse;
    private Class<T> serviceResponseType;

    public Builder<T> originalResponse(OriginalResponse originalResponse) {
      this.originalResponse = originalResponse;
      return this;
    }

    public Builder<T> serviceResponseType(Class<T> serviceResponseType) {
      this.serviceResponseType = serviceResponseType;
      return this;
    }

    /**
     * HttpResponse 구성
     *
     * @return httpResponse
     */
    public HttpResponse<T> build() {
      Objects.requireNonNull(originalResponse);
      if (originalResponse.getBody() == null
          || originalResponse.getBody().isEmpty()
          || serviceResponseType == null) {
        return new HttpResponse<>(
            originalResponse.getRequest(), originalResponse.getHeaders(), null, null);
      }
      ResponseBody body = new JsonResponseBody.Builder().body(originalResponse.getBody()).build();
      T serviceResponse;
      try {
        serviceResponse =
            GsonUtil.getGson().fromJson(originalResponse.getBody(), serviceResponseType);
      } catch (JsonSyntaxException e) {
        throw new MalformedMessageException(
            String.format("Invalid json response body[%s]", originalResponse.getBody()), e);
      }
      return new HttpResponse<>(
          originalResponse.getRequest(), originalResponse.getHeaders(), body, serviceResponse);
    }
  }
}
