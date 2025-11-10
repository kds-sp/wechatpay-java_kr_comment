package com.wechat.pay.java.core.certificate;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.auth.Credential;
import com.wechat.pay.java.core.auth.Validator;
import com.wechat.pay.java.core.auth.WechatPay2Credential;
import com.wechat.pay.java.core.cipher.AeadAesCipher;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.RSASigner;
import com.wechat.pay.java.core.http.AbstractHttpClientBuilder;
import com.wechat.pay.java.core.http.DefaultHttpClientBuilder;
import com.wechat.pay.java.core.http.HttpClient;
import com.wechat.pay.java.core.http.HttpHeaders;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/** RSA 자동 업데이트 플랫폼 인증서 제공기 */
public class RSAAutoCertificateProvider implements CertificateProvider {

  private static final CertificateHandler rsaCertificateHandler = new RSACertificateHandler();
  private static final String ALGORITHM_TYPE = "RSA";
  private static final String REQUEST_URL =
      "https://api.mch.weixin.qq.com/v3/certificates?algorithm_type=" + ALGORITHM_TYPE;

  private final String merchantId;

  private RSAAutoCertificateProvider(
      String merchantId, AeadCipher aeadCipher, HttpClient httpClient) {
    this.merchantId = merchantId;

    CertificateDownloader downloader =
        new CertificateDownloader.Builder()
            .certificateHandler(rsaCertificateHandler)
            .downloadUrl(REQUEST_URL)
            .aeadCipher(aeadCipher)
            .httpClient(httpClient)
            .build();

    AutoCertificateService.register(merchantId, ALGORITHM_TYPE, downloader);
  }

  /**
   * 인증서 시리얼 번호에 따라 인증서 가져오기
   *
   * @param serialNumber 위챗페이 플랫폼 인증서 시리얼 번호
   * @return X.509 인증서 인스턴스
   */
  @Override
  public X509Certificate getCertificate(String serialNumber) {
    return AutoCertificateService.getCertificate(merchantId, ALGORITHM_TYPE, serialNumber);
  }

  /**
   * 최신 사용 가능한 위챗페이 플랫폼 인증서 가져오기
   *
   * @return X.509 인증서 인스턴스
   */
  @Override
  public X509Certificate getAvailableCertificate() {
    return AutoCertificateService.getAvailableCertificate(merchantId, ALGORITHM_TYPE);
  }

  public static class Builder {
    private String merchantId;
    private byte[] apiV3Key;

    private Credential credential;
    private PrivateKey privateKey;
    private String merchantSerialNumber;
    private HttpClient httpClient;
    private AbstractHttpClientBuilder<?> httpClientBuilder;

    public Builder merchantId(String merchantId) {
      this.merchantId = merchantId;
      return this;
    }

    public Builder apiV3Key(byte[] apiV3Key) {
      this.apiV3Key = apiV3Key;
      return this;
    }

    public Builder httpClient(HttpClient httpClient) {
      this.httpClient = httpClient;
      return this;
    }

    public Builder credential(Credential credential) {
      this.credential = credential;
      return this;
    }

    public Builder privateKey(PrivateKey privateKey) {
      this.privateKey = privateKey;
      return this;
    }

    public Builder merchantSerialNumber(String merchantSerialNumber) {
      this.merchantSerialNumber = merchantSerialNumber;
      return this;
    }

    public Builder httpClientBuilder(AbstractHttpClientBuilder<?> builder) {
      // httpClientBuilder는 불변이 아니므로, 과정에서 입력 파라미터나 값이 변경되는 것을 방지하기 위해 여기서 복사본을 만듦
      this.httpClientBuilder = builder.newInstance();
      return this;
    }

    private final Validator emptyValidator =
        new Validator() {
          @Override
          public boolean validate(HttpHeaders responseHeaders, String body) {
            return true;
          }

          @Override
          public <T> String getSerialNumber() {
            return "";
          }
        };

    public RSAAutoCertificateProvider build() {
      if (httpClient == null) {
        if (httpClientBuilder == null) {
          httpClientBuilder = new DefaultHttpClientBuilder();
        }

        if (credential == null && privateKey != null) {
          credential =
              new WechatPay2Credential(
                  requireNonNull(merchantId),
                  new RSASigner(requireNonNull(merchantSerialNumber), privateKey));
        }

        httpClient = httpClientBuilder.credential(credential).validator(emptyValidator).build();
      }
      return new RSAAutoCertificateProvider(
          merchantId, new AeadAesCipher(requireNonNull(apiV3Key)), httpClient);
    }
  }
}
