package com.wechat.pay.java.service.cashcoupons;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.cashcoupons.model.AvailableMerchantCollection;
import com.wechat.pay.java.service.cashcoupons.model.AvailableSingleitemCollection;
import com.wechat.pay.java.service.cashcoupons.model.Callback;
import com.wechat.pay.java.service.cashcoupons.model.Coupon;
import com.wechat.pay.java.service.cashcoupons.model.CouponCollection;
import com.wechat.pay.java.service.cashcoupons.model.CreateCouponStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.CreateCouponStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.ListAvailableMerchantsRequest;
import com.wechat.pay.java.service.cashcoupons.model.ListAvailableSingleitemsRequest;
import com.wechat.pay.java.service.cashcoupons.model.ListCouponsByFilterRequest;
import com.wechat.pay.java.service.cashcoupons.model.ListStocksRequest;
import com.wechat.pay.java.service.cashcoupons.model.PauseStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.PauseStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.QueryCallbackRequest;
import com.wechat.pay.java.service.cashcoupons.model.QueryCouponRequest;
import com.wechat.pay.java.service.cashcoupons.model.QueryStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.RefundFlowRequest;
import com.wechat.pay.java.service.cashcoupons.model.RefundFlowResponse;
import com.wechat.pay.java.service.cashcoupons.model.RestartStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.RestartStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.SendCouponRequest;
import com.wechat.pay.java.service.cashcoupons.model.SendCouponResponse;
import com.wechat.pay.java.service.cashcoupons.model.SetCallbackRequest;
import com.wechat.pay.java.service.cashcoupons.model.SetCallbackResponse;
import com.wechat.pay.java.service.cashcoupons.model.StartStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.StartStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.Stock;
import com.wechat.pay.java.service.cashcoupons.model.StockCollection;
import com.wechat.pay.java.service.cashcoupons.model.StopStockRequest;
import com.wechat.pay.java.service.cashcoupons.model.StopStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.UseFlowRequest;
import com.wechat.pay.java.service.cashcoupons.model.UseFlowResponse;

/** CashCouponsService사용 예제 */
public class CashCouponsServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static CashCouponsService service;

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
    service = new CashCouponsService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 현금 쿠폰 메시지 알림 주소 조회 */
  public static Callback queryCallback() {

    QueryCallbackRequest request = new QueryCallbackRequest();
    return service.queryCallback(request);
  }

  /** 현금 쿠폰 메시지 알림 주소 설정 */
  public static SetCallbackResponse setCallback() {
    SetCallbackRequest request = new SetCallbackRequest();
    return service.setCallback(request);
  }

  /** 필터 조건에 따라 사용자 쿠폰 조회 */
  public static CouponCollection listCouponsByFilter() {

    ListCouponsByFilterRequest request = new ListCouponsByFilterRequest();
    return service.listCouponsByFilter(request);
  }

  /** 현금 쿠폰 상세 조회 */
  public static Coupon queryCoupon() {

    QueryCouponRequest request = new QueryCouponRequest();
    return service.queryCoupon(request);
  }

  /** 지정 배치의 현금 쿠폰 발송 */
  public static SendCouponResponse sendCoupon() {

    SendCouponRequest request = new SendCouponRequest();
    return service.sendCoupon(request);
  }

  /** 현금 쿠폰 배치 생성 */
  public static CreateCouponStockResponse createCouponStock() {
    CreateCouponStockRequest request = new CreateCouponStockRequest();
    return service.createCouponStock(request);
  }

  /** 현금 쿠폰 사용 가능 가맹점 조회 */
  public static AvailableMerchantCollection listAvailableMerchants() {

    ListAvailableMerchantsRequest request = new ListAvailableMerchantsRequest();
    return service.listAvailableMerchants(request);
  }

  /** 사용 가능한 상품 코드 조회 */
  public static AvailableSingleitemCollection listAvailableSingleitems() {

    ListAvailableSingleitemsRequest request = new ListAvailableSingleitemsRequest();
    return service.listAvailableSingleitems(request);
  }

  /** 조건별 배치 목록 조회 */
  public static StockCollection listStocks() {

    ListStocksRequest request = new ListStocksRequest();
    return service.listStocks(request);
  }

  /** 배치 일시 중지 */
  public static PauseStockResponse pauseStock() {

    PauseStockRequest request = new PauseStockRequest();
    return service.pauseStock(request);
  }

  /** 배치 상세 조회 */
  public static Stock queryStock() {

    QueryStockRequest request = new QueryStockRequest();
    return service.queryStock(request);
  }

  /** 배치 환불 명세 다운로드 */
  public static RefundFlowResponse refundFlow() {

    RefundFlowRequest request = new RefundFlowRequest();
    return service.refundFlow(request);
  }

  /** 배치 재시작 */
  public static RestartStockResponse restartStock() {

    RestartStockRequest request = new RestartStockRequest();
    return service.restartStock(request);
  }

  /** 배치 활성화 시작 */
  public static StartStockResponse startStock() {

    StartStockRequest request = new StartStockRequest();
    return service.startStock(request);
  }

  /** 배치 종료 */
  public static StopStockResponse stopStock() {

    StopStockRequest request = new StopStockRequest();
    return service.stopStock(request);
  }

  /** 배치 사용 명세 다운로드 */
  public static UseFlowResponse useFlow() {

    UseFlowRequest request = new UseFlowRequest();
    return service.useFlow(request);
  }
}
