package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.http.apache.ApacheHttpClientAdapter;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/** 기본 HttpClient 빌더 */
public class ApacheHttpClientBuilder implements AbstractHttpClientBuilder<ApacheHttpClientBuilder> {

  private Credential credential;
  private Validator validator;

  private CloseableHttpClient customizeApacheHttpClient;

  static PoolingHttpClientConnectionManager apacheHttpClientConnectionManager =
      new PoolingHttpClientConnectionManager();

  private CloseableHttpClient initDefaultApacheHttpClient() {
    return HttpClientBuilder.create()
        .setConnectionManager(apacheHttpClientConnectionManager)
        .setConnectionManagerShared(true)
        .build();
  }

  /**
   * 팩토리 복사, 현재 객체의 복사본 생성
   *
   * @return 객체의 복사본
   */
  @Override
  public ApacheHttpClientBuilder newInstance() {
    ApacheHttpClientBuilder result = new ApacheHttpClientBuilder();
    result.credential = this.credential;
    result.validator = this.validator;
    result.customizeApacheHttpClient = this.customizeApacheHttpClient;
    return result;
  }

  /**
   * 자격 증명 생성기 설정
   *
   * @param credential 자격 증명 생성기
   * @return apacheHttpClientBuilder
   */
  @Override
  public ApacheHttpClientBuilder credential(Credential credential) {
    this.credential = credential;
    return this;
  }

  /**
   * 검증기 설정
   *
   * @param validator 검증기
   * @return apacheHttpClientBuilder
   */
  @Override
  public ApacheHttpClientBuilder validator(Validator validator) {
    this.validator = validator;
    return this;
  }

  /**
   * apacheHttpClient 설정, 설정하지 않으면 기본으로 생성된 apacheHttpClient 사용
   *
   * @param apacheHttpClient 사용자 정의 apacheHttpClient
   * @return apacheHttpClientBuilder
   */
  public ApacheHttpClientBuilder apacheHttpClient(CloseableHttpClient apacheHttpClient) {
    this.customizeApacheHttpClient = apacheHttpClient;
    return this;
  }

  public ApacheHttpClientBuilder config(Config config) {
    requireNonNull(config);
    this.credential = config.createCredential();
    this.validator = config.createValidator();
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

    CloseableHttpClient httpclient =
        customizeApacheHttpClient == null
            ? initDefaultApacheHttpClient()
            : customizeApacheHttpClient;
    return new ApacheHttpClientAdapter(credential, validator, httpclient);
  }
}
