package com.wechat.pay.java.service.ecommerceprofitsharing;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.AddReceiverRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.AddReceiverResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.CreateAfterSalesOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.CreateAfterSalesOrderResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.CreateOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.CreateOrderResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.CreateReturnOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.CreateReturnOrderResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.DeleteReceiverRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.DeleteReceiverResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.FinishOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.FinishOrderResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryAfterSalesOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryAfterSalesOrderResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryOrderAmountRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryOrderAmountResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryOrderResponse;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryReturnOrderRequest;
import com.wechat.pay.java.service.ecommerceprofitsharing.model.QueryReturnOrderResponse;

/** EcommerceProfitSharingService사용 예제 */
public class EcommerceProfitSharingServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static EcommerceProfitSharingService service;

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
    service = new EcommerceProfitSharingService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** A/S 분할 요청 */
  public static CreateAfterSalesOrderResponse createAfterSalesOrder() {
    CreateAfterSalesOrderRequest request = new CreateAfterSalesOrderRequest();
    return service.createAfterSalesOrder(request);
  }

  /** 분할 요청 */
  public static CreateOrderResponse createOrder() {
    CreateOrderRequest request = new CreateOrderRequest();
    return service.createOrder(request);
  }

  /** 분할 완료 */
  public static FinishOrderResponse finishOrder() {
    FinishOrderRequest request = new FinishOrderRequest();
    return service.finishOrder(request);
  }

  /** A/S 분할 결과 조회 */
  public static QueryAfterSalesOrderResponse queryAfterSalesOrder() {

    QueryAfterSalesOrderRequest request = new QueryAfterSalesOrderRequest();
    return service.queryAfterSalesOrder(request);
  }

  /** 분할 결과 조회 */
  public static QueryOrderResponse queryOrder() {

    QueryOrderRequest request = new QueryOrderRequest();
    return service.queryOrder(request);
  }

  /** 주문 잔여 대기 분할 금액 조회 */
  public static QueryOrderAmountResponse queryOrderAmount() {

    QueryOrderAmountRequest request = new QueryOrderAmountRequest();
    return service.queryOrderAmount(request);
  }

  /** 분할 수취인 추가 */
  public static AddReceiverResponse addReceiver() {
    AddReceiverRequest request = new AddReceiverRequest();
    return service.addReceiver(request);
  }

  /** 분할 수취인 삭제 */
  public static DeleteReceiverResponse deleteReceiver() {
    DeleteReceiverRequest request = new DeleteReceiverRequest();
    return service.deleteReceiver(request);
  }

  /** 분할 환불 요청 */
  public static CreateReturnOrderResponse createReturnOrder() {
    CreateReturnOrderRequest request = new CreateReturnOrderRequest();
    return service.createReturnOrder(request);
  }

  /** 분할 환불 결과 조회 */
  public static QueryReturnOrderResponse queryReturnOrder() {

    QueryReturnOrderRequest request = new QueryReturnOrderRequest();
    return service.queryReturnOrder(request);
  }
}
