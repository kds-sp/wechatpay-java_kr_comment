package com.wechat.pay.java.service.wexinpayscoreparking;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.wexinpayscoreparking.model.CreateParkingRequest;
import com.wechat.pay.java.service.wexinpayscoreparking.model.CreateTransactionRequest;
import com.wechat.pay.java.service.wexinpayscoreparking.model.Parking;
import com.wechat.pay.java.service.wexinpayscoreparking.model.PlateService;
import com.wechat.pay.java.service.wexinpayscoreparking.model.QueryPlateServiceRequest;
import com.wechat.pay.java.service.wexinpayscoreparking.model.QueryTransactionRequest;
import com.wechat.pay.java.service.wexinpayscoreparking.model.Transaction;

/** WexinPayScoreParkingService사용 예제 */
public class WexinPayScoreParkingServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static WexinPayScoreParkingService service;

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
    service = new WexinPayScoreParkingService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 주차 입장 생성 */
  public static Parking createParking() {
    CreateParkingRequest request = new CreateParkingRequest();
    return service.createParking(request);
  }

  /** 차량 번호판 서비스 개통 정보 조회 */
  public static PlateService queryPlateService() {

    QueryPlateServiceRequest request = new QueryPlateServiceRequest();
    return service.queryPlateService(request);
  }

  /** 요금 차감 접수 */
  public static Transaction createTransaction() {
    CreateTransactionRequest request = new CreateTransactionRequest();
    return service.createTransaction(request);
  }

  /** 주문 조회 */
  public static Transaction queryTransaction() {

    QueryTransactionRequest request = new QueryTransactionRequest();
    return service.queryTransaction(request);
  }
}
