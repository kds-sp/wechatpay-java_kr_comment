package com.wechat.pay.java.core.exception;

/** 위챗페이 예외 기본 클래스 */
public abstract class WechatPayException extends RuntimeException {

  private static final long serialVersionUID = -5896431877288268263L;

  public WechatPayException(String message) {
    super(message);
  }

  public WechatPayException(String message, Throwable cause) {
    super(message, cause);
  }
}
