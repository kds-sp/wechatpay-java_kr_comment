package com.wechat.pay.java.service.merchantexclusivecoupon;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.AssociateTradeInfoRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.AssociateTradeInfoResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CouponCodeInfoRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CouponCodeInfoResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CouponCodeListResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CouponEntity;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CouponListResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CouponSendGovCardResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CreateBusiFavorStockRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.CreateBusiFavorStockResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.DeactivateCouponRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.DeactivateCouponResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.DeleteCouponCodeRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.DeleteCouponCodeResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.DisassociateTradeInfoRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.DisassociateTradeInfoResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.GetCouponNotifyRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.GetCouponNotifyResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ListCouponsByFilterRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ModifyBudgetRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ModifyBudgetResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ModifyStockInfoRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.PayReceiptInfoRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.PayReceiptListRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.QueryCouponCodeListRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.QueryCouponRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.QueryStockRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ReturnCouponRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ReturnCouponResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.ReturnReceiptInfoRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SendCouponRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SendCouponResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SendGovCardRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SetCouponNotifyRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SetCouponNotifyResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.StockGetResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SubsidyPayReceipt;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SubsidyPayReceiptListResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SubsidyPayRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SubsidyReturnReceipt;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.SubsidyReturnRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.UploadCouponCodeRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.UploadCouponCodeResponse;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.UseCouponRequest;
import com.wechat.pay.java.service.merchantexclusivecoupon.model.UseCouponResponse;

/** MerchantExclusiveCouponService사용 예제 */
public class MerchantExclusiveCouponServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static MerchantExclusiveCouponService service;

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
    service = new MerchantExclusiveCouponService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 사전 저장 code 상세 조회 */
  public static CouponCodeInfoResponse couponCodeInfo() {

    CouponCodeInfoRequest request = new CouponCodeInfoRequest();
    return service.couponCodeInfo(request);
  }

  /** 가맹점 쿠폰 생성 */
  public static CreateBusiFavorStockResponse createBusifavorStock() {
    CreateBusiFavorStockRequest request = new CreateBusiFavorStockRequest();
    return service.createBusifavorStock(request);
  }

  /** 사전 저장 code 삭제 */
  public static DeleteCouponCodeResponse deleteCouponCode() {

    DeleteCouponCodeRequest request = new DeleteCouponCodeRequest();
    return service.deleteCouponCode(request);
  }

  /** 배치 예산 수정 */
  public static ModifyBudgetResponse modifyBudget() {

    ModifyBudgetRequest request = new ModifyBudgetRequest();
    return service.modifyBudget(request);
  }

  /** 가맹점 쿠폰 기본 정보 수정 */
  public static void modifyStockInfo() {

    ModifyStockInfoRequest request = new ModifyStockInfoRequest();
    service.modifyStockInfo(request);
  }

  /** 사전 저장 code 목록 조회 */
  public static CouponCodeListResponse queryCouponCodeList() {

    QueryCouponCodeListRequest request = new QueryCouponCodeListRequest();
    return service.queryCouponCodeList(request);
  }

  /** 가맹점 쿠폰 배치 상세 조회 */
  public static StockGetResponse queryStock() {

    QueryStockRequest request = new QueryStockRequest();
    return service.queryStock(request);
  }

  /** 사전 저장 code 업로드 */
  public static UploadCouponCodeResponse uploadCouponCode() {

    UploadCouponCodeRequest request = new UploadCouponCodeRequest();
    return service.uploadCouponCode(request);
  }

  /** 가맹점 쿠폰 이벤트 알림 주소 조회 */
  public static GetCouponNotifyResponse getCouponNotify() {

    GetCouponNotifyRequest request = new GetCouponNotifyRequest();
    return service.getCouponNotify(request);
  }

  /** 가맹점 쿠폰 이벤트 알림 주소 설정 */
  public static SetCouponNotifyResponse setCouponNotify() {
    SetCouponNotifyRequest request = new SetCouponNotifyRequest();
    return service.setCouponNotify(request);
  }

  /** 주문 정보 연결 */
  public static AssociateTradeInfoResponse associateTradeInfo() {
    AssociateTradeInfoRequest request = new AssociateTradeInfoRequest();
    return service.associateTradeInfo(request);
  }

  /** 쿠폰 무효화 */
  public static DeactivateCouponResponse deactivateCoupon() {
    DeactivateCouponRequest request = new DeactivateCouponRequest();
    return service.deactivateCoupon(request);
  }

  /** 주문 정보 연결 해제 */
  public static DisassociateTradeInfoResponse disassociateTradeInfo() {
    DisassociateTradeInfoRequest request = new DisassociateTradeInfoRequest();
    return service.disassociateTradeInfo(request);
  }

  /** 필터 조건에 따라 사용자 쿠폰 조회 */
  public static CouponListResponse listCouponsByFilter() {

    ListCouponsByFilterRequest request = new ListCouponsByFilterRequest();
    return service.listCouponsByFilter(request);
  }

  /** 사용자 쿠폰 상세 조회 */
  public static CouponEntity queryCoupon() {

    QueryCouponRequest request = new QueryCouponRequest();
    return service.queryCoupon(request);
  }

  /** 쿠폰 반환 신청 */
  public static ReturnCouponResponse returnCoupon() {
    ReturnCouponRequest request = new ReturnCouponRequest();
    return service.returnCoupon(request);
  }

  /** 사용자에게 쿠폰 발송 */
  public static SendCouponResponse sendCoupon() {
    SendCouponRequest request = new SendCouponRequest();
    return service.sendCoupon(request);
  }

  /** 정부 소비 카드 발급 */
  public static CouponSendGovCardResponse sendGovCard() {

    SendGovCardRequest request = new SendGovCardRequest();
    return service.sendGovCard(request);
  }

  /** 사용자 쿠폰 사용 */
  public static UseCouponResponse useCoupon() {
    UseCouponRequest request = new UseCouponRequest();
    return service.useCoupon(request);
  }

  /** 가맹점 쿠폰 마케팅 보조금 결제 내역 상세 조회 */
  public static SubsidyPayReceipt payReceiptInfo() {

    PayReceiptInfoRequest request = new PayReceiptInfoRequest();
    return service.payReceiptInfo(request);
  }

  /** 가맹점 쿠폰 마케팅 보조금 결제 내역 목록 조회 */
  public static SubsidyPayReceiptListResponse payReceiptList() {

    PayReceiptListRequest request = new PayReceiptListRequest();
    return service.payReceiptList(request);
  }

  /** 가맹점 쿠폰 마케팅 보조금 환불 내역 상세 조회 */
  public static SubsidyReturnReceipt returnReceiptInfo() {

    ReturnReceiptInfoRequest request = new ReturnReceiptInfoRequest();
    return service.returnReceiptInfo(request);
  }

  /** 가맹점 쿠폰 마케팅 보조금 지급 */
  public static SubsidyPayReceipt subsidyPay() {
    SubsidyPayRequest request = new SubsidyPayRequest();
    return service.subsidyPay(request);
  }

  /** 가맹점 쿠폰 마케팅 보조금 반환 */
  public static SubsidyReturnReceipt subsidyReturn() {
    SubsidyReturnRequest request = new SubsidyReturnRequest();
    return service.subsidyReturn(request);
  }
}
