package com.wechat.pay.java.service;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;

/** JSAPI 주문 예제 */
public class QuickStart {

  /** 가맹점 번호 */
  public static String merchantId = "";

  /** 가맹점 API 개인키 경로 */
  public static String privateKeyPath = "";

  /** 가맹점 인증서 일련번호 */
  public static String merchantSerialNumber = "";

  /** 가맹점 APIV3 키 */
  public static String apiV3Key = "";

  public static void main(String[] args) {
    // 플랫폼 인증서 자동 업데이트 RSA 구성 사용
    // 하나의 가맹점 번호는 하나의 구성만 초기화 가능, 그렇지 않으면 중복 다운로드 작업으로 인해 오류 발생
    Config config =
        new RSAAutoCertificateConfig.Builder()
            .merchantId(merchantId)
            .privateKeyFromPath(privateKeyPath)
            .merchantSerialNumber(merchantSerialNumber)
            .apiV3Key(apiV3Key)
            .build();
    JsapiService service = new JsapiService.Builder().config(config).build();
    // request.setXxx(val)로 필요한 매개변수 설정, 구체적인 매개변수는 Request 정의 참조
    PrepayRequest request = new PrepayRequest();
    Amount amount = new Amount();
    amount.setTotal(100);
    request.setAmount(amount);
    request.setAppid("wxa9d9651ae******");
    request.setMchid("190000****");
    request.setDescription("테스트 상품 제목");
    request.setNotifyUrl("https://notify_url");
    request.setOutTradeNo("out_trade_no_001");
    Payer payer = new Payer();
    payer.setOpenid("oLTPCuN5a-nBD4rAL_fa********");
    request.setPayer(payer);
    PrepayResponse response = service.prepay(request);
    System.out.println(response.getPrepayId());
  }
}
