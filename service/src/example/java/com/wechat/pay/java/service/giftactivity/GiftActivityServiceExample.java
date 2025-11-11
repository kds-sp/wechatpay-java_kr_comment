package com.wechat.pay.java.service.giftactivity;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.giftactivity.model.AddActivityMerchantRequest;
import com.wechat.pay.java.service.giftactivity.model.AddActivityMerchantResponse;
import com.wechat.pay.java.service.giftactivity.model.CreateFullSendActRequest;
import com.wechat.pay.java.service.giftactivity.model.CreateFullSendActResponse;
import com.wechat.pay.java.service.giftactivity.model.DeleteActivityMerchantRequest;
import com.wechat.pay.java.service.giftactivity.model.DeleteActivityMerchantResponse;
import com.wechat.pay.java.service.giftactivity.model.GetActDetailRequest;
import com.wechat.pay.java.service.giftactivity.model.GetActDetailResponse;
import com.wechat.pay.java.service.giftactivity.model.ListActMchResponse;
import com.wechat.pay.java.service.giftactivity.model.ListActSkuResponse;
import com.wechat.pay.java.service.giftactivity.model.ListActivitiesRequest;
import com.wechat.pay.java.service.giftactivity.model.ListActivitiesResponse;
import com.wechat.pay.java.service.giftactivity.model.ListActivityMerchantRequest;
import com.wechat.pay.java.service.giftactivity.model.ListActivitySkuRequest;
import com.wechat.pay.java.service.giftactivity.model.TerminateActResponse;
import com.wechat.pay.java.service.giftactivity.model.TerminateActivityRequest;

/** GiftActivityService사용 예제 */
public class GiftActivityServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static GiftActivityService service;

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
    service = new GiftActivityService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 활동 쿠폰 발송 가맹점 번호 추가 */
  public static AddActivityMerchantResponse addActivityMerchant() {

    AddActivityMerchantRequest request = new AddActivityMerchantRequest();
    return service.addActivityMerchant(request);
  }

  /** 전체 만원 이상 선물 활동 생성 */
  public static CreateFullSendActResponse createFullSendAct() {
    CreateFullSendActRequest request = new CreateFullSendActRequest();
    return service.createFullSendAct(request);
  }

  /** 활동 쿠폰 발송 가맹점 번호 삭제 */
  public static DeleteActivityMerchantResponse deleteActivityMerchant() {

    DeleteActivityMerchantRequest request = new DeleteActivityMerchantRequest();
    return service.deleteActivityMerchant(request);
  }

  /** 활동 상세 인터페이스 조회 */
  public static GetActDetailResponse getActDetail() {

    GetActDetailRequest request = new GetActDetailRequest();
    return service.getActDetail(request);
  }

  /** 결제 시 선물 활동 목록 조회 */
  public static ListActivitiesResponse listActivities() {

    ListActivitiesRequest request = new ListActivitiesRequest();
    return service.listActivities(request);
  }

  /** 활동 쿠폰 발송 가맹점 번호 조회 */
  public static ListActMchResponse listActivityMerchant() {

    ListActivityMerchantRequest request = new ListActivityMerchantRequest();
    return service.listActivityMerchant(request);
  }

  /** 활동 지정 상품 목록 조회 */
  public static ListActSkuResponse listActivitySku() {

    ListActivitySkuRequest request = new ListActivitySkuRequest();
    return service.listActivitySku(request);
  }

  /** 활동 종료 */
  public static TerminateActResponse terminateActivity() {

    TerminateActivityRequest request = new TerminateActivityRequest();
    return service.terminateActivity(request);
  }
}
