package com.wechat.pay.java.service.retailstore;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.retailstore.model.AddRepresentativeRequest;
import com.wechat.pay.java.service.retailstore.model.AddRepresentativesResponse;
import com.wechat.pay.java.service.retailstore.model.AddStoresRequest;
import com.wechat.pay.java.service.retailstore.model.AddStoresResponse;
import com.wechat.pay.java.service.retailstore.model.ApplyActivityRequest;
import com.wechat.pay.java.service.retailstore.model.ApplyActivityResponse;
import com.wechat.pay.java.service.retailstore.model.CreateMaterialsRequest;
import com.wechat.pay.java.service.retailstore.model.DeleteRepresentativeRequest;
import com.wechat.pay.java.service.retailstore.model.DeleteRepresentativeResponse;
import com.wechat.pay.java.service.retailstore.model.DeleteStoresRequest;
import com.wechat.pay.java.service.retailstore.model.DeleteStoresResponse;
import com.wechat.pay.java.service.retailstore.model.GetStoreRequest;
import com.wechat.pay.java.service.retailstore.model.ListActsByAreaRequest;
import com.wechat.pay.java.service.retailstore.model.ListActsByAreaResponse;
import com.wechat.pay.java.service.retailstore.model.ListRepresentativeRequest;
import com.wechat.pay.java.service.retailstore.model.ListRepresentativeResponse;
import com.wechat.pay.java.service.retailstore.model.ListStoreRequest;
import com.wechat.pay.java.service.retailstore.model.ListStoreResponse;
import com.wechat.pay.java.service.retailstore.model.LockQualificationRequest;
import com.wechat.pay.java.service.retailstore.model.LockQualificationResponse;
import com.wechat.pay.java.service.retailstore.model.Materials;
import com.wechat.pay.java.service.retailstore.model.RetailStoreInfo;
import com.wechat.pay.java.service.retailstore.model.UnlockQualificationRequest;
import com.wechat.pay.java.service.retailstore.model.UnlockQualificationResponse;

/** RetailStoreService사용 예제 */
public class RetailStoreServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static RetailStoreService service;

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
    service = new RetailStoreService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 매장 브랜드 가격 인상 구매 활동 신청 */
  public static ApplyActivityResponse applyActivity() {

    ApplyActivityRequest request = new ApplyActivityRequest();
    return service.applyActivity(request);
  }

  /** 지역별 브랜드 가격 인상 구매 활동 조회 */
  public static ListActsByAreaResponse listActsByArea() {

    ListActsByAreaRequest request = new ListActsByAreaRequest();
    return service.listActsByArea(request);
  }

  /** 브랜드 가격 인상 구매 활동 자격 잠금 */
  public static LockQualificationResponse lockQualification() {
    LockQualificationRequest request = new LockQualificationRequest();
    return service.lockQualification(request);
  }

  /** 브랜드 가격 인상 구매 활동 자격 잠금 해제 */
  public static UnlockQualificationResponse unlockQualification() {
    UnlockQualificationRequest request = new UnlockQualificationRequest();
    return service.unlockQualification(request);
  }

  /** 소매 소규모 매장 활동 업무 대리인 추가 */
  public static AddRepresentativesResponse addRepresentative() {

    AddRepresentativeRequest request = new AddRepresentativeRequest();
    return service.addRepresentative(request);
  }

  /** 소규모 매장 활동 매장 추가 */
  public static AddStoresResponse addStores() {

    AddStoresRequest request = new AddStoresRequest();
    return service.addStores(request);
  }

  /** 소규모 매장 활동 재료 코드 생성 */
  public static Materials createMaterials() {

    CreateMaterialsRequest request = new CreateMaterialsRequest();
    return service.createMaterials(request);
  }

  /** 소매 소규모 매장 활동 업무 대리인 삭제 */
  public static DeleteRepresentativeResponse deleteRepresentative() {

    DeleteRepresentativeRequest request = new DeleteRepresentativeRequest();
    return service.deleteRepresentative(request);
  }

  /** 소규모 매장 활동 매장 삭제 */
  public static DeleteStoresResponse deleteStores() {

    DeleteStoresRequest request = new DeleteStoresRequest();
    return service.deleteStores(request);
  }

  /** 소규모 매장 활동 매장 상세 조회 */
  public static RetailStoreInfo getStore() {

    GetStoreRequest request = new GetStoreRequest();
    return service.getStore(request);
  }

  /** 소매 소규모 매장 활동 업무 대리인 조회 */
  public static ListRepresentativeResponse listRepresentative() {

    ListRepresentativeRequest request = new ListRepresentativeRequest();
    return service.listRepresentative(request);
  }

  /** 소규모 매장 활동 매장 목록 조회 */
  public static ListStoreResponse listStore() {

    ListStoreRequest request = new ListStoreRequest();
    return service.listStore(request);
  }
}
