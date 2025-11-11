package com.wechat.pay.java.service.transferbatch;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.transferbatch.model.GetTransferBatchByNoRequest;
import com.wechat.pay.java.service.transferbatch.model.GetTransferBatchByOutNoRequest;
import com.wechat.pay.java.service.transferbatch.model.GetTransferDetailByNoRequest;
import com.wechat.pay.java.service.transferbatch.model.GetTransferDetailByOutNoRequest;
import com.wechat.pay.java.service.transferbatch.model.InitiateBatchTransferRequest;
import com.wechat.pay.java.service.transferbatch.model.InitiateBatchTransferResponse;
import com.wechat.pay.java.service.transferbatch.model.TransferBatchEntity;
import com.wechat.pay.java.service.transferbatch.model.TransferDetailEntity;

/** TransferBatchService사용 예제 */
public class TransferBatchServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static TransferBatchService service;

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
    service = new TransferBatchService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 위챗페이 배치 번호로 배치 조회 */
  public static TransferBatchEntity getTransferBatchByNo() {

    GetTransferBatchByNoRequest request = new GetTransferBatchByNoRequest();
    return service.getTransferBatchByNo(request);
  }

  /** 가맹점 배치 번호로 배치 조회 */
  public static TransferBatchEntity getTransferBatchByOutNo() {

    GetTransferBatchByOutNoRequest request = new GetTransferBatchByOutNoRequest();
    return service.getTransferBatchByOutNo(request);
  }

  /** 가맹점 전송 시작 */
  public static InitiateBatchTransferResponse initiateBatchTransfer() {
    InitiateBatchTransferRequest request = new InitiateBatchTransferRequest();
    return service.initiateBatchTransfer(request);
  }

  /** 위챗페이 명세 번호로 명세 조회 */
  public static TransferDetailEntity getTransferDetailByNo() {

    GetTransferDetailByNoRequest request = new GetTransferDetailByNoRequest();
    return service.getTransferDetailByNo(request);
  }

  /** 가맹점 명세 번호로 명세 조회 */
  public static TransferDetailEntity getTransferDetailByOutNo() {

    GetTransferDetailByOutNoRequest request = new GetTransferDetailByOutNoRequest();
    return service.getTransferDetailByOutNo(request);
  }
}
