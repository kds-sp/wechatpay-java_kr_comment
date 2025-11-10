package com.wechat.pay.java.core.http;

/** HTTP 요청 본문 */
public interface RequestBody {

  /**
   * 요청 본문의 데이터 타입 가져오기
   *
   * @return 요청 본문의 데이터 타입
   */
  String getContentType();
}
