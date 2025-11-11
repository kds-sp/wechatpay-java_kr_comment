package com.wechat.pay.java.service.ecommercerefund;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.ecommercerefund.model.CreateRefundRequest;
import com.wechat.pay.java.service.ecommercerefund.model.CreateReturnAdvanceRequest;
import com.wechat.pay.java.service.ecommercerefund.model.QueryRefundByOutRefundNoRequest;
import com.wechat.pay.java.service.ecommercerefund.model.QueryRefundRequest;
import com.wechat.pay.java.service.ecommercerefund.model.QueryReturnAdvanceRequest;
import com.wechat.pay.java.service.ecommercerefund.model.Refund;
import com.wechat.pay.java.service.ecommercerefund.model.Refund4Create;
import com.wechat.pay.java.service.ecommercerefund.model.ReturnAdvance;

/** EcommerceRefundService사용 예제 */
public class EcommerceRefundServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static EcommerceRefundService service;

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
    service = new EcommerceRefundService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 환불 신청 */
  public static Refund4Create createRefund() {
    CreateRefundRequest request = new CreateRefundRequest();
    return service.createRefund(request);
  }

  /** 선불 환불 보충 */
  public static ReturnAdvance createReturnAdvance() {

    CreateReturnAdvanceRequest request = new CreateReturnAdvanceRequest();
    return service.createReturnAdvance(request);
  }

  /** 단일 환불 조회(위챗페이 환불 내역 번호로) */
  public static Refund queryRefund() {

    QueryRefundRequest request = new QueryRefundRequest();
    return service.queryRefund(request);
  }

  /** 단일 환불 조회(가맹점 환불 내역 번호로) */
  public static Refund queryRefundByOutRefundNo() {

    QueryRefundByOutRefundNoRequest request = new QueryRefundByOutRefundNoRequest();
    return service.queryRefundByOutRefundNo(request);
  }

  /** 선불 보충 결과 조회 */
  public static ReturnAdvance queryReturnAdvance() {

    QueryReturnAdvanceRequest request = new QueryReturnAdvanceRequest();
    return service.queryReturnAdvance(request);
  }
}
