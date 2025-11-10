package com.wechat.pay.java.core.http;

import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import java.io.InputStream;

/** HTTP 요청 클라이언트, 자동으로 서명 생성 및 검증 */
public interface HttpClient {

  /**
   * HTTP 요청 전송
   *
   * @param request HTTP 요청
   * @param responseClass 비즈니스 응답 클래스의 Class 객체, 비즈니스 응답 타입을 확정할 수 없거나 해당 요청에 응답 본문이 없는 경우 Object.class를 전달할 수 있음
   * @param <T> Class 객체로 모델링된 클래스의 타입
   * @return HTTP 응답
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 파라미터 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 응답 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 응답 예외. 예: 응답 상태 코드가 200 미만이거나 300 이상.
   * @throws MalformedMessageException 서비스 응답 성공, content-type이 application/json이 아니거나 응답 본문 파싱 실패.
   */
  <T> HttpResponse<T> execute(HttpRequest request, Class<T> responseClass);

  /**
   * GET 요청 전송
   *
   * @param headers 요청 헤더
   * @param url 요청 URL
   * @param responseClass 비즈니스 응답 클래스의 Class 객체, 비즈니스 응답 타입을 확정할 수 없거나 해당 요청에 응답 본문이 없는 경우 Object.class를 전달할 수 있음
   * @param <T> Class 객체로 모델링된 클래스의 타입
   * @return HTTP 응답
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 파라미터 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 응답 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 응답 예외. 예: 응답 상태 코드가 200 미만이거나 300 이상.
   * @throws MalformedMessageException 서비스 응답 성공, content-type이 application/json이 아니거나 응답 본문 파싱 실패.
   */
  default <T> HttpResponse<T> get(HttpHeaders headers, String url, Class<T> responseClass) {
    HttpRequest httpRequest =
        new HttpRequest.Builder().url(url).httpMethod(HttpMethod.GET).headers(headers).build();
    return execute(httpRequest, responseClass);
  }

  /**
   * POST 요청 전송
   *
   * @param headers 요청 헤더
   * @param url 요청 URL
   * @param body 요청 본문
   * @param responseClass 비즈니스 응답 클래스의 Class 객체, 비즈니스 응답 타입을 확정할 수 없거나 해당 요청에 응답 본문이 없는 경우 Object.class를 전달할 수 있음
   * @param <T> Class 객체로 모델링된 클래스의 타입
   * @return HTTP 응답
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 파라미터 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 응답 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 응답 예외. 예: 응답 상태 코드가 200 미만이거나 300 이상.
   * @throws MalformedMessageException 서비스 응답 성공, content-type이 application/json이 아니거나 응답 본문 파싱 실패.
   */
  default <T> HttpResponse<T> post(
      HttpHeaders headers, String url, RequestBody body, Class<T> responseClass) {
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .url(url)
            .httpMethod(HttpMethod.POST)
            .headers(headers)
            .body(body)
            .build();
    return execute(httpRequest, responseClass);
  }

  /**
   * PATCH 요청 전송
   *
   * @param headers 요청 헤더
   * @param url 요청 URL
   * @param body 요청 본문
   * @param responseClass 비즈니스 응답 클래스의 Class 객체, 비즈니스 응답 타입을 확정할 수 없거나 해당 요청에 응답 본문이 없는 경우 Object.class를 전달할 수 있음
   * @param <T> Class 객체로 모델링된 클래스의 타입
   * @return HTTP 응답
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 파라미터 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 응답 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 응답 예외. 예: 응답 상태 코드가 200 미만이거나 300 이상.
   * @throws MalformedMessageException 서비스 응답 성공, content-type이 application/json이 아니거나 응답 본문 파싱 실패.
   */
  default <T> HttpResponse<T> patch(
      HttpHeaders headers, String url, RequestBody body, Class<T> responseClass) {
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .url(url)
            .httpMethod(HttpMethod.PATCH)
            .headers(headers)
            .body(body)
            .build();
    return execute(httpRequest, responseClass);
  }

  /**
   * PUT 요청 전송
   *
   * @param headers 요청 헤더
   * @param url 요청 URL
   * @param body 요청 본문
   * @param responseClass 비즈니스 응답 클래스의 Class 객체, 비즈니스 응답 타입을 확정할 수 없거나 해당 요청에 응답 본문이 없는 경우 Object.class를 전달할 수 있음
   * @param <T> Class 객체로 모델링된 클래스의 타입
   * @return HTTP 응답
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 파라미터 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 응답 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 응답 예외. 예: 응답 상태 코드가 200 미만이거나 300 이상.
   * @throws MalformedMessageException 서비스 응답 성공, content-type이 application/json이 아니거나 응답 본문 파싱 실패.
   */
  default <T> HttpResponse<T> put(
      HttpHeaders headers, String url, RequestBody body, Class<T> responseClass) {
    HttpRequest httpRequest =
        new HttpRequest.Builder()
            .url(url)
            .httpMethod(HttpMethod.PUT)
            .headers(headers)
            .body(body)
            .build();
    return execute(httpRequest, responseClass);
  }

  /**
   * DELETE 요청 전송
   *
   * @param headers 요청 헤더
   * @param url 요청 URL
   * @param responseClass 비즈니스 응답 클래스의 Class 객체, 비즈니스 응답 타입을 확정할 수 없거나 해당 요청에 응답 본문이 없는 경우 Object.class를 전달할 수 있음
   * @param <T> Class 객체로 모델링된 클래스의 타입
   * @return HTTP 응답
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 파라미터 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 응답 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 응답 예외. 예: 응답 상태 코드가 200 미만이거나 300 이상.
   * @throws MalformedMessageException 서비스 응답 성공, content-type이 application/json이 아니거나 응답 본문 파싱 실패.
   */
  default <T> HttpResponse<T> delete(HttpHeaders headers, String url, Class<T> responseClass) {
    HttpRequest httpRequest =
        new HttpRequest.Builder().url(url).httpMethod(HttpMethod.DELETE).headers(headers).build();
    return execute(httpRequest, responseClass);
  }

  /**
   * 파일 다운로드, 파일 스트림 사용 후 닫아야 함
   *
   * @param url 요청 URL
   * @return 파일 스트림
   */
  InputStream download(String url);
}
