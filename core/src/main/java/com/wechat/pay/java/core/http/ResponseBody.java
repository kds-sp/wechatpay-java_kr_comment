package com.wechat.pay.java.core.http;

/** HTTP 응답 본문 */
public interface ResponseBody {

  /**
   * 응답 본문의 데이터 타입 가져오기
   *
   * @return 응답 본문의 데이터 타입
   */
  String getContentType();
}
