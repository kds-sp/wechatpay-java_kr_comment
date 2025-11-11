package com.wechat.pay.java.service.payments.jsapi;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.jsapi.model.CloseOrderRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByIdRequest;
import com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.payments.model.Transaction;

public class JsapiServiceExtensionExample {
  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static JsapiServiceExtension service;

  public static void main(String[] args) {
    // 가맹점 구성 초기화
    Config config =
        new RSAAutoCertificateConfig.Builder()
            .merchantId(merchantId)
            // com.wechat.pay.java.core.util의 함수를 사용하여 로컬 파일에서 가맹점 개인키를 로드, 가맹점 개인키는 요청 서명 생성에 사용됨
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .apiV3Key(apiV3Key)
            .build();
    // 서비스 초기화
    service =
        new JsapiServiceExtension.Builder()
            .config(config)
            .signType("RSA") // 입력하지 않으면 기본값은 RSA
            .build();
    try {
      // ... 인터페이스 호출
      PrepayWithRequestPaymentResponse response = prepayWithRequestPayment();
      System.out.println(response);
    } catch (HttpException e) { // HTTP 요청 전송 실패
      // e.getHttpRequest()를 호출하여 요청을 가져와 로그를 출력하거나 모니터링에 보고, 더 많은 방법은 HttpException 정의 참조
    } catch (ServiceException e) { // 서비스 반환 상태가 200보다 작거나 300보다 크거나 같음, 예: 500
      // e.getResponseBody()를 호출하여 반환 본문을 가져와 로그를 출력하거나 모니터링에 보고, 더 많은 방법은 ServiceException 정의 참조
    } catch (MalformedMessageException e) { // 서비스 반환 성공, 반환 본문 유형이 불법이거나 반환 본문 파싱 실패
      // e.getMessage()를 호출하여 정보를 가져와 로그를 출력하거나 모니터링에 보고, 더 많은 방법은 MalformedMessageException 정의 참조
    }
  }

  /** 주문 닫기 */
  public static void closeOrder() {

    CloseOrderRequest request = new CloseOrderRequest();
    // request.setXxx(val)를 호출하여 필요한 매개변수 설정, 구체적인 매개변수는 Request 정의 참조
    // 인터페이스 호출
    service.closeOrder(request);
  }

  /** JSAPI 결제 주문, JSAPI 결제 데이터 반환 */
  public static PrepayWithRequestPaymentResponse prepayWithRequestPayment() {
    PrepayRequest request = new PrepayRequest();
    // request.setXxx(val)를 호출하여 필요한 매개변수 설정, 구체적인 매개변수는 Request 정의 참조
    // 인터페이스 호출
    return service.prepayWithRequestPayment(request);
  }

  /** 위챗페이 주문 번호로 주문 조회 */
  public static Transaction queryOrderById() {

    QueryOrderByIdRequest request = new QueryOrderByIdRequest();
    // request.setXxx(val)를 호출하여 필요한 매개변수 설정, 구체적인 매개변수는 Request 정의 참조
    // 인터페이스 호출
    return service.queryOrderById(request);
  }

  /** 가맹점 주문 번호로 주문 조회 */
  public static Transaction queryOrderByOutTradeNo() {

    QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
    // request.setXxx(val)를 호출하여 필요한 매개변수 설정, 구체적인 매개변수는 Request 정의 참조
    // 인터페이스 호출
    return service.queryOrderByOutTradeNo(request);
  }
}
