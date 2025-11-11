package com.wechat.pay.java.service.ecommercesubsidy;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesCancelEntity;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesCancelRequest;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesCreateEntity;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesCreateRequest;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesReturnEntity;
import com.wechat.pay.java.service.ecommercesubsidy.model.SubsidiesReturnRequest;

/** EcommerceSubsidyService사용 예제 */
public class EcommerceSubsidyServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static EcommerceSubsidyService service;

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
    service = new EcommerceSubsidyService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 보조금 취소 */
  public static SubsidiesCancelEntity cancelSubsidy() {
    SubsidiesCancelRequest request = new SubsidiesCancelRequest();
    return service.cancelSubsidy(request);
  }

  /** 보조금 요청 */
  public static SubsidiesCreateEntity createSubsidy() {
    SubsidiesCreateRequest request = new SubsidiesCreateRequest();
    return service.createSubsidy(request);
  }

  /** 보조금 환불 요청 */
  public static SubsidiesReturnEntity returnSubsidy() {
    SubsidiesReturnRequest request = new SubsidiesReturnRequest();
    return service.returnSubsidy(request);
  }
}
