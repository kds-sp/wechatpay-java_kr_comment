package com.wechat.pay.java.core.exception;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.util.GsonUtil;

/** HTTP 요청 전송 성공, 응답 예외 시 발생. 예: 응답 상태 코드가 200 미만이거나 300 이상, 응답 본문 파라미터가 불완전한 경우. */
public class ServiceException extends WechatPayException {

  private static final long serialVersionUID = -7174975090366956652L;

  private final HttpRequest httpRequest;
  private final int httpStatusCode;
  private final String responseBody;
  private String errorCode;
  private String errorMessage;

  /**
   * 응답 상태 코드가 200 미만이거나 300 이상일 때 호출
   *
   * @param httpRequest http 요청
   * @param httpStatusCode http 상태 코드
   * @param responseBody http 응답 본문
   */
  public ServiceException(HttpRequest httpRequest, int httpStatusCode, String responseBody) {
    super(
        String.format(
            "Wrong HttpStatusCode[%d]%nhttpResponseBody[%.1024s]\tHttpRequest[%s]",
            httpStatusCode, responseBody, httpRequest));
    this.httpRequest = httpRequest;
    this.httpStatusCode = httpStatusCode;
    this.responseBody = responseBody;
    if (responseBody != null && !responseBody.isEmpty()) {
      JsonObject jsonObject = GsonUtil.getGson().fromJson(responseBody, JsonObject.class);
      JsonElement code = jsonObject.get("code");
      JsonElement message = jsonObject.get("message");
      this.errorCode = code == null ? null : code.getAsString();
      this.errorMessage = message == null ? null : message.getAsString();
    }
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

  /**
   * HTTP 응답 본문 가져오기
   *
   * @return HTTP 응답 본문
   */
  public String getResponseBody() {
    return responseBody;
  }

  /**
   * HTTP 상태 코드 가져오기
   *
   * @return HTTP 상태 코드
   */
  public int getHttpStatusCode() {
    return httpStatusCode;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
