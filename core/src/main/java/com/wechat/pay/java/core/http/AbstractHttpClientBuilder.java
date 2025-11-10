package com.wechat.pay.java.core.http;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;

public interface AbstractHttpClientBuilder<T extends AbstractHttpClientBuilder<T>> {

  /**
   * 팩토리 복사, 현재 객체의 복사본 생성
   *
   * @return 객체의 복사본
   */
  T newInstance();

  /**
   * 검증기 설정
   *
   * @param validator 검증기
   * @return the AbstractHttpClientBuilder
   */
  T validator(Validator validator);

  /**
   * 자격 증명 생성기 설정
   *
   * @param credential 자격 증명 생성기
   * @return the AbstractHttpClientBuilder
   */
  T credential(Credential credential);

  /**
   * AbstractHttpClient 빌드
   *
   * @return AbstractHttpClient
   */
  AbstractHttpClient build();
}
