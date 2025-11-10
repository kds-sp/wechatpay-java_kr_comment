package com.wechat.pay.java.core.auth;

import com.wechat.pay.java.core.http.HttpHeaders;

/** 검증기 */
public interface Validator {

  /**
   * 응답이 유효한지 검증
   *
   * @param responseHeaders HTTP 응답 헤더
   * @param body HTTP 응답 본문
   * @param <T> 응답 객체 타입
   * @return 응답이 유효한지 여부
   */
  <T> boolean validate(HttpHeaders responseHeaders, String body);

  <T> String getSerialNumber();
}
