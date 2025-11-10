package com.wechat.pay.java.core.exception;

import com.wechat.pay.java.core.http.HttpRequest;

/** HTTP 요청 전송 실패 시 발생. 예: 요청 파라미터 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함. */
public class HttpException extends WechatPayException {

  private static final long serialVersionUID = 8583990125724273072L;
  private HttpRequest httpRequest;

  /**
   * 요청 파라미터 구성 실패 시 호출
   *
   * @param message 오류 정보
   * @param cause 실패를 일으킨 원본 예외
   */
  public HttpException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * 요청 전송 실패 시 호출
   *
   * @param httpRequest http 요청
   * @param cause 실패를 일으킨 원본 예외
   */
  public HttpException(HttpRequest httpRequest, Throwable cause) {
    super(String.format("Send Http Request failed,httpRequest[%s]", httpRequest), cause);
    this.httpRequest = httpRequest;
  }

  /**
   * 직렬화 버전 UID 가져오기
   *
   * @return UID
   */
  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  /**
   * HTTP 요청 가져오기
   *
   * @return HTTP 요청
   */
  public HttpRequest getHttpRequest() {
    return httpRequest;
  }
}
