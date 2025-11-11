package com.wechat.pay.java.service.marketingbankpackages;

import static com.wechat.pay.java.core.http.UrlEncoder.urlEncode;
import static com.wechat.pay.java.core.util.GsonUtil.toJson;
import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.cipher.PrivacyEncryptor;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.FileRequestBody;
import com.wechat.pay.java.core.http.HostName;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.http.MediaType;
import com.wechat.pay.java.core.util.ShaUtil;
import com.wechat.pay.java.service.marketingbankpackages.model.FileMeta;
import com.wechat.pay.java.service.marketingbankpackages.model.ListTaskRequest;
import com.wechat.pay.java.service.marketingbankpackages.model.ListTaskResponse;
import com.wechat.pay.java.service.marketingbankpackages.model.Task;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/** MarketingBankPackagesServiceExtension 서비스 */
public class MarketingBankPackagesServiceExtension {

  private final MarketingBankPackagesService packagesService;
  private final HttpClient httpClient;
  private final PrivacyEncryptor encryptor;

  private MarketingBankPackagesServiceExtension(
      HttpClient httpClient, HostName hostName, PrivacyEncryptor encryptor) {
    MarketingBankPackagesService.Builder builder = new MarketingBankPackagesService.Builder();
    builder.httpClient(httpClient);
    if (hostName != null) {
      builder.hostName(hostName);
    }
    this.encryptor = requireNonNull(encryptor);
    this.httpClient = requireNonNull(httpClient);
    this.packagesService = builder.build();
  }

  /** MarketingBankPackagesServiceExtension 생성자 */
  public static class Builder {

    private HostName hostName;
    private HttpClient httpClient;
    private PrivacyEncryptor encryptor;

    public Builder config(Config config) {
      this.httpClient = new DefaultHttpClientBuilder().config(config).build();
      this.encryptor = config.createEncryptor();
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder hostName(HostName hostName) {
      this.hostName = hostName;
      return this;
    }

    public Builder encryptor(PrivacyEncryptor encryptor) {
      this.encryptor = encryptor;
      return this;
    }

    public MarketingBankPackagesServiceExtension build() {
      return new MarketingBankPackagesServiceExtension(httpClient, hostName, encryptor);
    }
  }

  /**
   * 업로드 작업 목록 조회
   *
   * @param request 요청 매개변수
   * @return ListTaskResponse
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public ListTaskResponse listTask(ListTaskRequest request) {
    return packagesService.listTask(request);
  }

  /**
   * 타겟 사용자 계약 번호 가져오기(업로드 작업 생성)
   *
   * @param packageId 번호 패키지 ID
   * @param bankType 은행 유형
   * @param filePath 업로드할 파일의 절대 경로. 파일 행 수는 5500행을 초과하지 않는 것을 권장하며, 원본 파일이 너무 큰 경우 가맹점은 먼저 여러 개의 작은 파일로 분할한 후 하나씩 업로드해야 함
   * @return Task
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public Task uploadPackageByFile(String packageId, String bankType, String filePath)
      throws IllegalArgumentException {
    File file = new File(filePath);
    ArrayList<String> fileContentList = new ArrayList<>();
    try {
      // 파일 읽기
      BufferedReader reader;
      reader = new BufferedReader(new FileReader(file));
      String line = reader.readLine();
      while (line != null) {
        fileContentList.add(line);
        line = reader.readLine();
      }
      reader.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("Upload file, failed to open filePath:" + filePath);
    }
    return uploadPackage(packageId, bankType, file.getName(), fileContentList);
  }

  /**
   * 타겟 사용자 계약 번호 가져오기(업로드 작업 생성)
   *
   * @param packageId 번호 패키지 ID
   * @param bankType 은행 유형
   * @param fileName 파일 이름, 파일 경로가 아님
   * @param fileContentList 파일 내용, 배열의 각 요소는 하나의 사용자 계약 번호 평문을 나타냄. 각 파일 행 수는 5500행을 초과하지 않는 것을 권장
   * @return Task
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public Task uploadPackage(
      String packageId, String bankType, String fileName, ArrayList<String> fileContentList) {
    if (fileContentList.size() > 5500) {
      throw new IllegalArgumentException(
          "Number of lines should not bigger than 5500. fileContentList.size: "
              + String.valueOf(fileContentList.size()));
    }

    // 1. URL 연결
    String uploadPath =
        "https://api.mch.weixin.qq.com/v3/marketing/bank/packages/"
            + urlEncode(packageId)
            + "/tasks";

    // 2. 파일 내용을 행별로 암호화
    StringBuilder sb = new StringBuilder();
    for (String each : fileContentList) {
      sb.append(encryptor.encrypt(each)).append("\n");
    }
    String encryptedFileContent = sb.toString();

    // 3. sha256 파일 다이제스트 계산
    byte[] fileBytes = encryptedFileContent.getBytes();
    String fileSha256 = ShaUtil.getSha256HexString(fileBytes);

    // 4. meta 조립
    FileMeta fileMeta = new FileMeta();
    fileMeta.setFilename(fileName);
    fileMeta.setSha256(fileSha256);
    fileMeta.setBankType(bankType);

    // 5. 업로드 실행
    return uploadFile(uploadPath, toJson(fileMeta), fileName, fileBytes);
  }

  private Task uploadFile(String uploadPath, String meta, String fileName, byte[] fileBytes) {
    HttpRequest request =
        new HttpRequest.Builder()
            .addHeader(Constant.ACCEPT, " */*")
            .addHeader(Constant.WECHAT_PAY_SERIAL, encryptor.getWechatpaySerial())
            .addHeader(Constant.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA.getValue())
            .httpMethod(HttpMethod.POST)
            .url(uploadPath)
            .body(
                new FileRequestBody.Builder().meta(meta).fileName(fileName).file(fileBytes).build())
            .build();
    return httpClient.execute(request, Task.class).getServiceResponse();
  }
}
