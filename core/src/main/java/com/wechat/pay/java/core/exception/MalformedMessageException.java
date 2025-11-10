package com.wechat.pay.java.core.exception;

/** 위챗페이 응답 또는 콜백 메시지 파싱 예외 시 발생, 예: 콜백 알림 파라미터가 올바르지 않거나 응답 타입 오류. */
public class MalformedMessageException extends WechatPayException {

  private static final long serialVersionUID = -1049702516796430238L;

  public MalformedMessageException(String message) {
    super(message);
  }

  public MalformedMessageException(String message, Throwable cause) {
    super(message, cause);
  }
}
