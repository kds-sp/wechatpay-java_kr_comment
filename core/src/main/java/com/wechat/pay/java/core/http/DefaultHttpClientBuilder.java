package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.http.okhttp.OkHttpClientAdapter;
import com.wechat.pay.java.core.http.okhttp.OkHttpMultiDomainInterceptor;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/** 기본 HttpClient 빌더 */
public class DefaultHttpClientBuilder
    implements AbstractHttpClientBuilder<DefaultHttpClientBuilder> {

  private Credential credential;
  private Validator validator;

  // 최대 유휴 연결 maxIdleConnections는 5개로, OkHttp 기본값과 동일하게 유지
  private static final int MAX_IDLE_CONNECTIONS = 5;
  // 연결의 Keep-Alive 유휴 시간은 7초, 위챗페이 서버는 현재 8초
  // 유휴 클라이언트가 서버에 의해 끊어져 불필요한 재시도가 발생하는 것을 방지
  private static final int KEEP_ALIVE_SECONDS = 7;
  private static final okhttp3.OkHttpClient defaultOkHttpClient =
      new OkHttpClient.Builder()
          .connectionPool(
              new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS))
          .build();
  private okhttp3.OkHttpClient customizeOkHttpClient;
  private int readTimeoutMs = -1;
  private int writeTimeoutMs = -1;
  private int connectTimeoutMs = -1;
  private Proxy proxy;
  private boolean retryMultiDomain = false;
  private Boolean retryOnConnectionFailure = null;
  private static final OkHttpMultiDomainInterceptor multiDomainInterceptor =
      new OkHttpMultiDomainInterceptor();

  /**
   * 팩토리 복사, 현재 객체의 복사본 생성
   *
   * @return 객체의 복사본
   */
  @Override
  public DefaultHttpClientBuilder newInstance() {
    DefaultHttpClientBuilder result = new DefaultHttpClientBuilder();
    result.credential = this.credential;
    result.validator = this.validator;
    result.customizeOkHttpClient = this.customizeOkHttpClient;
    result.readTimeoutMs = this.readTimeoutMs;
    result.writeTimeoutMs = this.writeTimeoutMs;
    result.connectTimeoutMs = this.connectTimeoutMs;
    result.proxy = this.proxy;
    return result;
  }

  /**
   * 읽기 타임아웃 설정
   *
   * @param readTimeoutMs 읽기 타임아웃, 단위 밀리초
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder readTimeoutMs(int readTimeoutMs) {
    this.readTimeoutMs = readTimeoutMs;
    return this;
  }

  /**
   * 쓰기 타임아웃 설정
   *
   * @param writeTimeoutMs 쓰기 타임아웃, 단위 밀리초
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder writeTimeoutMs(int writeTimeoutMs) {
    this.writeTimeoutMs = writeTimeoutMs;
    return this;
  }

  /**
   * 연결 타임아웃 설정
   *
   * @param connectTimeoutMs 연결 타임아웃, 단위 밀리초
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder connectTimeoutMs(int connectTimeoutMs) {
    this.connectTimeoutMs = connectTimeoutMs;
    return this;
  }

  /**
   * 자격 증명 생성기 설정
   *
   * @param credential 자격 증명 생성기
   * @return defaultHttpClientBuilder
   */
  @Override
  public DefaultHttpClientBuilder credential(Credential credential) {
    this.credential = credential;
    return this;
  }

  /**
   * 검증기 설정
   *
   * @param validator 검증기
   * @return defaultHttpClientBuilder
   */
  @Override
  public DefaultHttpClientBuilder validator(Validator validator) {
    this.validator = validator;
    return this;
  }

  /**
   * okHttpClient 설정, 이 파라미터를 설정하면 client의 기존 설정을 덮어씀
   *
   * @param okHttpClient 사용자 정의 okHttpClient
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder okHttpClient(okhttp3.OkHttpClient okHttpClient) {
    this.customizeOkHttpClient = okHttpClient;
    return this;
  }

  public DefaultHttpClientBuilder config(Config config) {
    requireNonNull(config);
    this.credential = config.createCredential();
    this.validator = config.createValidator();
    return this;
  }

  public DefaultHttpClientBuilder proxy(Proxy proxy) {
    requireNonNull(proxy);
    this.proxy = proxy;
    return this;
  }

  /**
   * 이중 도메인 재해 복구 활성화
   *
   * @return defaultHttpClientBuilder
   */
  public DefaultHttpClientBuilder enableRetryMultiDomain() {
    this.retryMultiDomain = true;
    return this;
  }

  /** OkHttp가 네트워크 문제 시 재시도하지 않음 */
  public DefaultHttpClientBuilder disableRetryOnConnectionFailure() {
    this.retryOnConnectionFailure = false;
    return this;
  }

  /**
   * 기본 HttpClient 빌드
   *
   * @return httpClient
   */
  @Override
  public AbstractHttpClient build() {
    requireNonNull(credential);
    requireNonNull(validator);
    okhttp3.OkHttpClient.Builder okHttpClientBuilder =
        (customizeOkHttpClient == null ? defaultOkHttpClient : customizeOkHttpClient).newBuilder();
    if (connectTimeoutMs >= 0) {
      okHttpClientBuilder.connectTimeout(connectTimeoutMs, TimeUnit.MILLISECONDS);
    }
    if (readTimeoutMs >= 0) {
      okHttpClientBuilder.readTimeout(readTimeoutMs, TimeUnit.MILLISECONDS);
    }
    if (writeTimeoutMs >= 0) {
      okHttpClientBuilder.writeTimeout(writeTimeoutMs, TimeUnit.MILLISECONDS);
    }
    if (proxy != null) {
      okHttpClientBuilder.proxy(proxy);
    }
    if (retryMultiDomain) {
      okHttpClientBuilder.addInterceptor(multiDomainInterceptor);
    }
    if (retryOnConnectionFailure != null && !retryOnConnectionFailure) {
      okHttpClientBuilder.retryOnConnectionFailure(false);
    }
    return new OkHttpClientAdapter(credential, validator, okHttpClientBuilder.build());
  }
}
