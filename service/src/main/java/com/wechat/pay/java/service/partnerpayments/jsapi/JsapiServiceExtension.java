package com.wechat.pay.java.service.partnerpayments.jsapi;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.cipher.Signer;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.HostName;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.util.NonceUtil;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.CloseOrderRequest;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.QueryOrderByIdRequest;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.Transaction;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSAPI 결제의 확장 클래스.
 *
 * <p>JsapiService를 캡슐화하고 향상된 JSAPI 주문 메서드 prepayWithRequestPayment를 제공합니다.
 */
public class JsapiServiceExtension {
  private final Signer signer;
  private final String signType;
  private final JsapiService jsapiService;
  private static final Logger logger = LoggerFactory.getLogger(JsapiServiceExtension.class);

  private JsapiServiceExtension(
      Config config, HttpClient httpClient, HostName hostName, String signType) {
    this.signer = config.createSigner();
    JsapiService.Builder builder = new JsapiService.Builder().config(config);
    this.signType = signType;
    if (httpClient != null) {
      builder.httpClient(httpClient);
    }
    if (hostName != null) {
      builder.hostName(hostName);
    }
    this.jsapiService = builder.build();
  }

  /**
   * JSAPI 결제 주문하고 JSAPI 결제 호출 데이터를 반환합니다. 권장 사용!
   *
   * <p>요청 성공 후, 이 메서드는 사전 결제 거래 세션 식별자 prepay_id와 클라이언트 JSAPI 결제 호출에 필요한 매개변수를 반환합니다. JsapiService.prepay보다 더 간단하고 사용하기 쉬우며, 개발자가 결제 호출 서명을 직접 계산할 필요가 없기 때문입니다.
   *
   * @param request 요청 매개변수 가맹점이 신청한 공개 계정에 해당하는 appid
   * @param requestPaymentAppid 가맹점이 신청한 공개 계정에 해당하는 appid
   * @return PrepayWithRequestPaymentResponse
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public PrepayWithRequestPaymentResponse prepayWithRequestPayment(
      PrepayRequest request, String requestPaymentAppid) {
    String prepayId = jsapiService.prepay(request).getPrepayId();
    long timestamp = Instant.now().getEpochSecond();
    String nonceStr = NonceUtil.createNonce(32);
    String packageVal = "prepay_id=" + prepayId;
    String message =
        requestPaymentAppid + "\n" + timestamp + "\n" + nonceStr + "\n" + packageVal + "\n";
    logger.debug("Message for RequestPayment signatures is[{}]", message);
    String sign = signer.sign(message).getSign();
    PrepayWithRequestPaymentResponse response = new PrepayWithRequestPaymentResponse();
    response.setAppId(requestPaymentAppid);
    response.setTimeStamp(String.valueOf(timestamp));
    response.setNonceStr(nonceStr);
    response.setPackageVal(packageVal);
    response.setSignType(signType);
    response.setPaySign(sign);
    return response;
  }

  /**
   * 위챗페이 주문 번호로 주문 조회
   *
   * @param request 요청 매개변수
   * @return Transaction
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public Transaction queryOrderById(QueryOrderByIdRequest request) {
    return jsapiService.queryOrderById(request);
  }

  /**
   * 가맹점 주문 번호로 주문 조회
   *
   * @param request 요청 매개변수
   * @return Transaction
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public Transaction queryOrderByOutTradeNo(QueryOrderByOutTradeNoRequest request) {
    return jsapiService.queryOrderByOutTradeNo(request);
  }

  /**
   * 주문 닫기
   *
   * @param request 요청 매개변수
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public void closeOrder(CloseOrderRequest request) {
    jsapiService.closeOrder(request);
  }

  public static class Builder {
    private Config config;
    private HttpClient httpClient;
    private HostName hostName;
    private String signType;

    public Builder config(Config config) {
      this.config = config;
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder hostName(HostName hostName) {
      this.hostName = hostName;
      return this;
    }

    public Builder signType(String signType) {
      this.signType = signType;
      return this;
    }

    public JsapiServiceExtension build() {
      return new JsapiServiceExtension(
          config, httpClient, hostName, signType == null ? "RSA" : signType);
    }
  }
}
