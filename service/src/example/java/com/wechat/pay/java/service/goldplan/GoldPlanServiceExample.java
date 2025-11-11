package com.wechat.pay.java.service.goldplan;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.goldplan.model.ChangeCustomPageStatusRequest;
import com.wechat.pay.java.service.goldplan.model.ChangeCustomPageStatusResponse;
import com.wechat.pay.java.service.goldplan.model.ChangeGoldPlanStatusRequest;
import com.wechat.pay.java.service.goldplan.model.ChangeGoldPlanStatusResponse;
import com.wechat.pay.java.service.goldplan.model.CloseAdvertisingShowRequest;
import com.wechat.pay.java.service.goldplan.model.OpenAdvertisingShowRequest;
import com.wechat.pay.java.service.goldplan.model.SetAdvertisingIndustryFilterRequest;

/** GoldPlanService사용 예제 */
public class GoldPlanServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static GoldPlanService service;

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
    service = new GoldPlanService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 광고 표시 닫기 */
  public static void closeAdvertisingShow() {
    CloseAdvertisingShowRequest request = new CloseAdvertisingShowRequest();
    service.closeAdvertisingShow(request);
  }

  /** 광고 표시 열기 */
  public static void openAdvertisingShow() {
    OpenAdvertisingShowRequest request = new OpenAdvertisingShowRequest();
    service.openAdvertisingShow(request);
  }

  /** 동종업계 필터 태그 관리 */
  public static void setAdvertisingIndustryFilter() {
    SetAdvertisingIndustryFilterRequest request = new SetAdvertisingIndustryFilterRequest();
    service.setAdvertisingIndustryFilter(request);
  }

  /** 가맹점 영수증 관리 */
  public static ChangeCustomPageStatusResponse changeCustomPageStatus() {
    ChangeCustomPageStatusRequest request = new ChangeCustomPageStatusRequest();
    return service.changeCustomPageStatus(request);
  }

  /** 골드 플랜 관리 */
  public static ChangeGoldPlanStatusResponse changeGoldPlanStatus() {
    ChangeGoldPlanStatusRequest request = new ChangeGoldPlanStatusRequest();
    return service.changeGoldPlanStatus(request);
  }
}
