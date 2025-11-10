package com.wechat.pay.java.core.exception;

/** 서명 검증 실패 시 발생 */
public class ValidationException extends WechatPayException {

  private static final long serialVersionUID = -5439484392712167452L;

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
