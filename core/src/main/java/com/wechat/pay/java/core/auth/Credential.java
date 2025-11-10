package com.wechat.pay.java.core.auth;

import java.net.URI;

/** 인증 자격 증명 생성기 */
public interface Credential {

  /**
   * 인증 타입 가져오기
   *
   * @return 인증 타입
   */
  String getSchema();

  /**
   * 가맹점 번호 가져오기
   *
   * @return 가맹점 번호
   */
  String getMerchantId();

  /**
   * 인증 정보 가져오기
   *
   * @param uri 요청 uri
   * @param httpMethod HTTP 메서드, GET, POST 등
   * @param signBody 서명에 사용되는 요청 본문
   * @return 인증 정보
   */
  String getAuthorization(URI uri, String httpMethod, String signBody);
}
