package com.wechat.pay.java.service.profitsharing;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.profitsharing.model.AddReceiverRequest;
import com.wechat.pay.java.service.profitsharing.model.AddReceiverResponse;
import com.wechat.pay.java.service.profitsharing.model.CreateOrderRequest;
import com.wechat.pay.java.service.profitsharing.model.CreateReturnOrderRequest;
import com.wechat.pay.java.service.profitsharing.model.DeleteReceiverRequest;
import com.wechat.pay.java.service.profitsharing.model.DeleteReceiverResponse;
import com.wechat.pay.java.service.profitsharing.model.OrdersEntity;
import com.wechat.pay.java.service.profitsharing.model.QueryMerchantRatioRequest;
import com.wechat.pay.java.service.profitsharing.model.QueryMerchantRatioResponse;
import com.wechat.pay.java.service.profitsharing.model.QueryOrderAmountRequest;
import com.wechat.pay.java.service.profitsharing.model.QueryOrderAmountResponse;
import com.wechat.pay.java.service.profitsharing.model.QueryOrderRequest;
import com.wechat.pay.java.service.profitsharing.model.QueryReturnOrderRequest;
import com.wechat.pay.java.service.profitsharing.model.ReturnOrdersEntity;
import com.wechat.pay.java.service.profitsharing.model.SplitBillRequest;
import com.wechat.pay.java.service.profitsharing.model.SplitBillResponse;
import com.wechat.pay.java.service.profitsharing.model.UnfreezeOrderRequest;

/** ProfitsharingService사용 예제 */
public class ProfitsharingServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static ProfitsharingService service;

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
    service = new ProfitsharingService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 분할 청구서 파일 다운로드 주소 조회 */
  public static SplitBillResponse splitBill() {

    SplitBillRequest request = new SplitBillRequest();
    return service.splitBill(request);
  }

  /** 최대 분할 비율 API 조회 */
  public static QueryMerchantRatioResponse queryMerchantRatio() {

    QueryMerchantRatioRequest request = new QueryMerchantRatioRequest();
    return service.queryMerchantRatio(request);
  }

  /** 분할 요청 API */
  public static OrdersEntity createOrder() {
    CreateOrderRequest request = new CreateOrderRequest();
    return service.createOrder(request);
  }

  /** 분할 결과 API 조회 */
  public static OrdersEntity queryOrder() {

    QueryOrderRequest request = new QueryOrderRequest();
    return service.queryOrder(request);
  }

  /** 잔여 자금 해제 API */
  public static OrdersEntity unfreezeOrder() {
    UnfreezeOrderRequest request = new UnfreezeOrderRequest();
    return service.unfreezeOrder(request);
  }

  /** 분할 수취인 추가 API */
  public static AddReceiverResponse addReceiver() {
    AddReceiverRequest request = new AddReceiverRequest();
    return service.addReceiver(request);
  }

  /** 분할 수취인 삭제 API */
  public static DeleteReceiverResponse deleteReceiver() {
    DeleteReceiverRequest request = new DeleteReceiverRequest();
    return service.deleteReceiver(request);
  }

  /** 분할 환불 요청 API */
  public static ReturnOrdersEntity createReturnOrder() {
    CreateReturnOrderRequest request = new CreateReturnOrderRequest();
    return service.createReturnOrder(request);
  }

  /** 분할 환불 결과 API 조회 */
  public static ReturnOrdersEntity queryReturnOrder() {

    QueryReturnOrderRequest request = new QueryReturnOrderRequest();
    return service.queryReturnOrder(request);
  }

  /** 잔여 대기 분할 금액 API 조회 */
  public static QueryOrderAmountResponse queryOrderAmount() {

    QueryOrderAmountRequest request = new QueryOrderAmountRequest();
    return service.queryOrderAmount(request);
  }
}
