package com.wechat.pay.java.service.weixinpayscanandride;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.weixinpayscanandride.model.CreateTransactionRequest;
import com.wechat.pay.java.service.weixinpayscanandride.model.QueryTransactionRequest;
import com.wechat.pay.java.service.weixinpayscanandride.model.QueryUserServiceRequest;
import com.wechat.pay.java.service.weixinpayscanandride.model.TransactionsEntity;
import com.wechat.pay.java.service.weixinpayscanandride.model.UserServiceEntity;

/** WeixinPayScanAndRideService사용 예제 */
public class WeixinPayScanAndRideServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static WeixinPayScanAndRideService service;

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
    service = new WeixinPayScanAndRideService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 요금 차감 접수 */
  public static TransactionsEntity createTransaction() {
    CreateTransactionRequest request = new CreateTransactionRequest();
    return service.createTransaction(request);
  }

  /** 주문 조회 */
  public static TransactionsEntity queryTransaction() {

    QueryTransactionRequest request = new QueryTransactionRequest();
    return service.queryTransaction(request);
  }

  /** 사용자 서비스 사용 가능 정보 조회 */
  public static UserServiceEntity queryUserService() {

    QueryUserServiceRequest request = new QueryUserServiceRequest();
    return service.queryUserService(request);
  }
}
