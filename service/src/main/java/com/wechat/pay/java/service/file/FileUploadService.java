package com.wechat.pay.java.service.file;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.Constant;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.FileRequestBody;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpMethod;
import com.wechat.pay.java.core.http.HttpRequest;
import com.wechat.pay.java.core.http.MediaType;
import com.wechat.pay.java.core.util.IOUtil;
import com.wechat.pay.java.service.file.model.FileUploadResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/** 파일 업로드 서비스 */
public class FileUploadService {

  private final HttpClient httpClient;

  private FileUploadService(HttpClient httpClient) {
    this.httpClient = requireNonNull(httpClient);
  }

  /** FileUploadService 생성자 */
  public static class Builder {

    private HttpClient httpClient;

    public Builder config(Config config) {
      this.httpClient = new DefaultHttpClientBuilder().config(config).build();
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = requireNonNull(httpClient);
      return this;
    }

    public FileUploadService build() {
      return new FileUploadService(httpClient);
    }
  }

  /**
   * 비디오 업로드
   *
   * @param uploadPath 업로드 경로
   * @param meta 미디어 파일 메타 정보
   * @param videoPath 비디오 파일의 절대 경로
   * @return 업로드 결과
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   * @throws IOException 바이트 읽기 실패, 스트림 닫기 실패 등.
   */
  public FileUploadResponse uploadVideo(String uploadPath, String meta, String videoPath)
      throws IOException {
    File file = new File(videoPath);
    try (FileInputStream inputStream = new FileInputStream(file)) {
      return uploadFile(uploadPath, meta, file.getName(), IOUtil.toByteArray(inputStream));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Upload video, file not found in videoPath:" + videoPath);
    }
  }

  /**
   * 비디오 업로드
   *
   * @param uploadPath 업로드 경로
   * @param meta 미디어 파일 메타 정보
   * @param fileName 파일 이름
   * @param video 비디오 바이트 배열
   * @return 업로드 결과
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public FileUploadResponse uploadVideo(
      String uploadPath, String meta, String fileName, byte[] video) {
    return uploadFile(uploadPath, meta, fileName, video);
  }

  /**
   * 이미지 업로드
   *
   * @param uploadPath 업로드 경로
   * @param meta 미디어 파일 메타 정보
   * @param imagePath 이미지 파일의 절대 경로
   * @return 업로드 결과
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   * @throws IOException 이미지 읽기 실패, 이미지 스트림 닫기 실패.
   */
  public FileUploadResponse uploadImage(String uploadPath, String meta, String imagePath)
      throws IOException {
    File file = new File(imagePath);
    try (FileInputStream inputStream = new FileInputStream(file)) {
      return uploadFile(uploadPath, meta, file.getName(), IOUtil.toByteArray(inputStream));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Upload image, file not found in imagePath:" + imagePath);
    }
  }

  /**
   * 이미지 업로드
   *
   * @param uploadPath 업로드 경로
   * @param meta 미디어 파일 메타 정보
   * @param fileName 파일 이름
   * @param image 이미지 바이트 배열
   * @return 업로드 결과
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public FileUploadResponse uploadImage(
      String uploadPath, String meta, String fileName, byte[] image) {
    return uploadFile(uploadPath, meta, fileName, image);
  }

  private FileUploadResponse uploadFile(
      String uploadPath, String meta, String fileName, byte[] file) {
    HttpRequest request =
        new HttpRequest.Builder()
            .addHeader(Constant.ACCEPT, " */*")
            .addHeader(Constant.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA.getValue())
            .httpMethod(HttpMethod.POST)
            .url(uploadPath)
            .body(new FileRequestBody.Builder().meta(meta).fileName(fileName).file(file).build())
            .build();
    return httpClient.execute(request, FileUploadResponse.class).getServiceResponse();
  }
}
