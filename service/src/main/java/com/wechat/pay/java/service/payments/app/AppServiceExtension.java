package com.wechat.pay.java.service.payments.app;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.cipher.Signer;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.HostName;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.util.NonceUtil;
import com.wechat.pay.java.service.payments.app.model.CloseOrderRequest;
import com.wechat.pay.java.service.payments.app.model.PrepayRequest;
import com.wechat.pay.java.service.payments.app.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.app.model.QueryOrderByIdRequest;
import com.wechat.pay.java.service.payments.app.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.payments.model.Transaction;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * APP 결제의 확장 클래스
 *
 * <p>AppService를 캡슐화하고 향상된 APP 주문 메서드 prepayWithRequestPayment를 제공함
 */
public class AppServiceExtension {
  private final Signer signer;
  private final AppService appService;
  private static final Logger logger = LoggerFactory.getLogger(AppServiceExtension.class);

  private AppServiceExtension(Config config, HttpClient httpClient, HostName hostName) {
    this.signer = config.createSigner();
    AppService.Builder builder = new AppService.Builder().config(config);
    if (httpClient != null) {
      builder.httpClient(httpClient);
    }
    if (hostName != null) {
      builder.hostName(hostName);
    }
    this.appService = builder.build();
  }

  /**
   * APP 결제 주문, APP 결제 호출 데이터 반환. 권장 사용!
   *
   * <p>요청 성공 후, 이 메서드는 사전 결제 거래 세션 식별 prepay_id와 클라이언트 APP 결제 호출에 필요한 매개변수를 반환함. AppService.prepay와 비교하여
   * 더 간단하고 사용하기 쉬우며, 개발자가 직접 결제 호출 서명을 계산할 필요가 없음.
   *
   * @param request 요청 파라미터
   * @return PrepayWithRequestPaymentResponse
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 파라미터 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 응답 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 응답 예외. 예: 응답 상태 코드가 200 미만이거나 300 이상.
   * @throws MalformedMessageException 서비스 응답 성공, content-type이 application/json이 아니거나 응답 본문 파싱 실패.
   */
  public PrepayWithRequestPaymentResponse prepayWithRequestPayment(PrepayRequest request) {
    String prepayId = appService.prepay(request).getPrepayId();
    long timestamp = Instant.now().getEpochSecond();
    String nonceStr = NonceUtil.createNonce(32);
    String message =
        request.getAppid() + "\n" + timestamp + "\n" + nonceStr + "\n" + prepayId + "\n";
    logger.debug("Message for RequestPayment signatures is[{}]", message);
    String sign = signer.sign(message).getSign();
    PrepayWithRequestPaymentResponse response = new PrepayWithRequestPaymentResponse();
    response.setAppid(request.getAppid());
    response.setPartnerId(request.getMchid());
    response.setPrepayId(prepayId);
    response.setPackageVal("Sign=WXPay");
    response.setNonceStr(nonceStr);
    response.setTimestamp(String.valueOf(timestamp));
    response.setSign(sign);
    return response;
  }

  /**
   * 위챗페이 주문 번호로 주문 조회
   *
   * @param request 요청 파라미터
   * @return Transaction
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 파라미터 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 응답 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 응답 예외. 예: 응답 상태 코드가 200 미만이거나 300 이상.
   * @throws MalformedMessageException 서비스 응답 성공, content-type이 application/json이 아니거나 응답 본문 파싱 실패.
   */
  public Transaction queryOrderById(QueryOrderByIdRequest request) {
    return appService.queryOrderById(request);
  }

  /**
   * 가맹점 주문 번호로 주문 조회
   *
   * @param request 요청 파라미터
   * @return Transaction
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 파라미터 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 응답 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 응답 예외. 예: 응답 상태 코드가 200 미만이거나 300 이상.
   * @throws MalformedMessageException 서비스 응답 성공, content-type이 application/json이 아니거나 응답 본문 파싱 실패.
   */
  public Transaction queryOrderByOutTradeNo(QueryOrderByOutTradeNoRequest request) {
    return appService.queryOrderByOutTradeNo(request);
  }

  /**
   * 주문 닫기
   *
   * @param request 요청 파라미터
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 파라미터 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 응답 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 응답 예외. 예: 응답 상태 코드가 200 미만이거나 300 이상.
   * @throws MalformedMessageException 서비스 응답 성공, content-type이 application/json이 아니거나 응답 본문 파싱 실패.
   */
  public void closeOrder(CloseOrderRequest request) {
    appService.closeOrder(request);
  }

  public static class Builder {

    private Config config;

    private HttpClient httpClient;
    private HostName hostName;

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

    public AppServiceExtension build() {
      return new AppServiceExtension(config, httpClient, hostName);
    }
  }
}
