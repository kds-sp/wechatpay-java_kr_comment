package com.wechat.pay.java.service.payrollcard;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payrollcard.model.AuthenticationEntity;
import com.wechat.pay.java.service.payrollcard.model.CreateTokenRequest;
import com.wechat.pay.java.service.payrollcard.model.CreateTransferBatchRequest;
import com.wechat.pay.java.service.payrollcard.model.GetAuthenticationRequest;
import com.wechat.pay.java.service.payrollcard.model.GetRelationRequest;
import com.wechat.pay.java.service.payrollcard.model.ListAuthenticationsRequest;
import com.wechat.pay.java.service.payrollcard.model.ListAuthenticationsResponse;
import com.wechat.pay.java.service.payrollcard.model.PreOrderAuthenticationRequest;
import com.wechat.pay.java.service.payrollcard.model.PreOrderAuthenticationResponse;
import com.wechat.pay.java.service.payrollcard.model.PreOrderAuthenticationWithAuthRequest;
import com.wechat.pay.java.service.payrollcard.model.PreOrderAuthenticationWithAuthResponse;
import com.wechat.pay.java.service.payrollcard.model.RelationEntity;
import com.wechat.pay.java.service.payrollcard.model.TokenEntity;
import com.wechat.pay.java.service.payrollcard.model.TransferBatchEntity;

/** PayrollCardService사용 예제 */
public class PayrollCardServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static PayrollCardService service;

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
    service = new PayrollCardService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 신원 확인 결과 조회 */
  public static AuthenticationEntity getAuthentication() {

    GetAuthenticationRequest request = new GetAuthenticationRequest();
    return service.getAuthentication(request);
  }

  /** 신원 확인 기록 조회 */
  public static ListAuthenticationsResponse listAuthentications() {

    ListAuthenticationsRequest request = new ListAuthenticationsRequest();
    return service.listAuthentications(request);
  }

  /** 마이크로 급여 카드 신원 확인 사전 주문 */
  public static PreOrderAuthenticationResponse preOrderAuthentication() {
    PreOrderAuthenticationRequest request = new PreOrderAuthenticationRequest();
    return service.preOrderAuthentication(request);
  }

  /** 마이크로 급여 카드 신원 확인 사전 주문(프로세스에서 권한 부여 완료) */
  public static PreOrderAuthenticationWithAuthResponse preOrderAuthenticationWithAuth() {
    PreOrderAuthenticationWithAuthRequest request = new PreOrderAuthenticationWithAuthRequest();
    return service.preOrderAuthenticationWithAuth(request);
  }

  /** 마이크로 급여 카드 권한 부여 관계 조회 */
  public static RelationEntity getRelation() {

    GetRelationRequest request = new GetRelationRequest();
    return service.getRelation(request);
  }

  /** 권한 부여 token 생성 */
  public static TokenEntity createToken() {
    CreateTokenRequest request = new CreateTokenRequest();
    return service.createToken(request);
  }

  /** 배치 전송 시작 */
  public static TransferBatchEntity createTransferBatch() {
    CreateTransferBatchRequest request = new CreateTransferBatchRequest();
    return service.createTransferBatch(request);
  }
}
