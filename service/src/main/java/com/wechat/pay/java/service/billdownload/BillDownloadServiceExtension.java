package com.wechat.pay.java.service.billdownload;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.cipher.PrivacyDecryptor;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HostName;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.service.billdownload.model.GetFundFlowBillRequest;
import com.wechat.pay.java.service.billdownload.model.GetTradeBillRequest;
import com.wechat.pay.java.service.billdownload.model.QueryBillEntity;
import com.wechat.pay.java.service.billdownload.model.TarType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public class BillDownloadServiceExtension {

  private final BillDownloadService service;

  private final HttpClient httpClient;

  private BillDownloadServiceExtension(Builder extBuilder) {
    this.service =
        new BillDownloadService.Builder()
            .httpClient(extBuilder.httpClient)
            .decryptor(extBuilder.decryptor)
            .hostName(extBuilder.hostName)
            .build();
    this.httpClient = extBuilder.httpClient;
  }

  /**
   * 거래 청단 조회. DigestBillEntity에서 청단 스트림을 얻으며, 소비 후 요약을 검증해야 함. 압축 해제 지원.
   *
   * @param request 요청 매개변수
   * @return DigestBillEntity
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public DigestBillEntity getTradeBill(GetTradeBillRequest request) {
    QueryBillEntity billEntity = service.getTradeBill(request);
    InputStream stream = getBillStream(billEntity, request.getTarType());

    return new DigestBillEntity(stream, billEntity.getHashValue(), billEntity.getHashType());
  }

  /**
   * 자금 청단 조회. DigestBillEntity에서 청단 스트림을 얻으며, 소비 후 요약을 검증해야 함.
   *
   * @param request 요청 매개변수
   * @return DigestBillEntity
   * @throws HttpException HTTP 요청 전송 실패. 예: 요청 매개변수 구성 실패, 요청 전송 실패, I/O 오류 등. 요청 정보 포함.
   * @throws ValidationException HTTP 요청 전송 성공, 위챗페이 반환 서명 검증 실패.
   * @throws ServiceException HTTP 요청 전송 성공, 서비스 반환 예외. 예: 반환 상태 코드가 200보다 작거나 300보다 크거나 같음.
   * @throws MalformedMessageException 서비스 반환 성공, content-type이 application/json이 아니거나 반환 본문 파싱 실패.
   */
  public DigestBillEntity getFundFlowBill(GetFundFlowBillRequest request) {
    QueryBillEntity billEntity = service.getFundFlowBill(request);
    InputStream stream = getBillStream(billEntity, request.getTarType());

    return new DigestBillEntity(stream, billEntity.getHashValue(), billEntity.getHashType());
  }

  private InputStream getBillStream(QueryBillEntity billEntity, TarType tarType) {
    InputStream stream = httpClient.download(billEntity.getDownloadUrl());
    if (tarType == TarType.GZIP) {
      try {
        stream = new GZIPInputStream(stream);
      } catch (IOException e) {
        throw new MalformedMessageException("invalid response", e);
      }
    }
    return stream;
  }

  /** BillDownloadServiceExtension 생성자 */
  public static class Builder {

    private HttpClient httpClient;
    private HostName hostName;

    private PrivacyDecryptor decryptor;

    public Builder config(Config config) {
      Objects.requireNonNull(config, "Config must not be null");
      this.httpClient = new DefaultHttpClientBuilder().config(config).build();

      this.decryptor = config.createDecryptor();
      return this;
    }

    public Builder hostName(HostName hostName) {
      this.hostName = hostName;
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder decryptor(PrivacyDecryptor decryptor) {
      this.decryptor = decryptor;
      return this;
    }

    public BillDownloadServiceExtension build() {
      Objects.requireNonNull(httpClient, "HttpClient must not be null");
      Objects.requireNonNull(decryptor, "Decryptor must not be null");

      return new BillDownloadServiceExtension(this);
    }
  }
}
