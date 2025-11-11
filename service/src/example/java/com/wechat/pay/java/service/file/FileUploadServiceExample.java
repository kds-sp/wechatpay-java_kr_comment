package com.wechat.pay.java.service.file;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.file.model.FileUploadResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 파일 업로드 서비스 사용 예제 */
public class FileUploadServiceExample {

  /** 가맹점 번호 */
  public static String merchantId = "190000****";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "/Users/yourname/your/path/apiclient_key.pem";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "5157F09EFDC096DE15EBE81A47057A72********";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "...";

  public static FileUploadService fileUploadService;
  private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceExample.class);

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

    // 파일 업로드 서비스 초기화
    fileUploadService = new FileUploadService.Builder().config(config).build();
    // ... 인터페이스 호출
  }

  /** 이미지 업로드 */
  public static FileUploadResponse uploadImage() {
    String uploadPath = "";
    String imagePath = "";
    String meta = "";
    FileUploadResponse fileUploadResponse = null;
    try {
      fileUploadResponse = fileUploadService.uploadImage(uploadPath, meta, imagePath);
    } catch (IOException e) {
      // ... 모니터링 보고 및 로그 출력
      logger.error("UploadImage failed", e);
    }
    return fileUploadResponse;
  }

  /** 비디오 업로드 */
  public static FileUploadResponse uploadVideo() {
    String uploadPath = "";
    String videoPath = "";
    String meta = "";
    FileUploadResponse fileUploadResponse = null;
    try {
      fileUploadResponse = fileUploadService.uploadVideo(uploadPath, meta, videoPath);
    } catch (IOException e) {
      // ... 모니터링 보고 및 로그 출력
      logger.error("UploadVideo failed", e);
    }
    return fileUploadResponse;
  }
}
