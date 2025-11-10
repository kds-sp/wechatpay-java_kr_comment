package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.util.GsonUtil;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/** HTTP 요청 */
public final class HttpRequest {

  private final HttpMethod httpMethod;
  private final URL url;
  private final URI uri;
  private final HttpHeaders headers;
  private final RequestBody body;

  private HttpRequest(
      HttpMethod httpMethod, URL url, URI uri, HttpHeaders headers, RequestBody body) {
    this.httpMethod = httpMethod;
    this.url = url;
    this.uri = uri;
    this.headers = headers;
    this.body = body;
  }

  /**
   * HttpMethod 가져오기
   *
   * @return httpMethod
   */
  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  /**
   * 요청 URL 가져오기
   *
   * @return 요청 URL
   */
  public URL getUrl() {
    return url;
  }

  /**
   * 요청 URI 가져오기
   *
   * @return 요청 uri
   */
  public URI getUri() {
    return uri;
  }

  /**
   * 요청 헤더 가져오기
   *
   * @return 요청 헤더
   */
  public HttpHeaders getHeaders() {
    return headers;
  }

  /**
   * 요청 본문 가져오기
   *
   * @return 요청 본문
   */
  public RequestBody getBody() {
    return body;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }

  public static class Builder {

    HttpMethod httpMethod;
    private URL url;
    private HttpHeaders headers = new HttpHeaders();
    private RequestBody body;

    /**
     * httpMethod 설정
     *
     * @param httpMethod httpMethod
     * @return Builder
     */
    public Builder httpMethod(HttpMethod httpMethod) {
      this.httpMethod = httpMethod;
      return this;
    }

    /**
     * url 설정
     *
     * @param url url
     * @return Builder
     */
    public Builder url(URL url) {
      this.url = url;
      return this;
    }

    /**
     * url 설정
     *
     * @param urlString urlString
     * @return Builder
     */
    public Builder url(String urlString) {
      try {
        this.url = new URL(urlString);
        return this;
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException(
            "The urlString passed in when building httpRequest is invalid:" + urlString);
      }
    }

    /**
     * headers 재설정
     *
     * @param headers headers
     * @return Builder
     */
    public Builder headers(HttpHeaders headers) {
      this.headers = headers;
      return this;
    }

    /**
     * header 추가
     *
     * @param name name
     * @param value value
     * @return Builder
     */
    public Builder addHeader(String name, String value) {
      headers.addHeader(name, value);
      return this;
    }

    /**
     * body 설정
     *
     * @param body body
     * @return Builder
     */
    public Builder body(RequestBody body) {
      this.body = body;
      return this;
    }

    /**
     * HttpRequest 빌드
     *
     * @return HttpRequest
     * @throws HttpException url을 uri로 변환 실패
     */
    public HttpRequest build() {
      try {
        return new HttpRequest(
            requireNonNull(httpMethod),
            requireNonNull(url),
            url.toURI(),
            headers == null ? new HttpHeaders() : headers,
            body);
      } catch (URISyntaxException e) {
        throw new HttpException(String.format("Parse url[%s] to uri failed.", url), e);
      }
    }
  }
}
