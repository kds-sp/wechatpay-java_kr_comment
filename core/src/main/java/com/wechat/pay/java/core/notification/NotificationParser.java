package com.wechat.pay.java.core.notification;

import static java.util.Objects.requireNonNull;

import com.google.gson.Gson;
import com.wechat.pay.java.core.cipher.AeadCipher;
import com.wechat.pay.java.core.cipher.Verifier;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.util.GsonUtil;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/** 알림 파서 */
public class NotificationParser {

  private final Gson gson = GsonUtil.getGson();
  private final Map<String, Verifier> verifiers = new HashMap<>();
  private final Map<String, AeadCipher> ciphers = new HashMap<>();

  public NotificationParser(NotificationConfig... configs) {
    if (configs.length == 0) {
      throw new IllegalArgumentException("NotificationConfig is empty.");
    }
    for (NotificationConfig config : configs) {
      this.verifiers.put(config.getSignType(), config.createVerifier());
      this.ciphers.put(config.getCipherType(), config.createAeadCipher());
    }
  }

  public NotificationParser(Map<String, Verifier> verifiers, Map<String, AeadCipher> ciphers) {
    this.verifiers.putAll(verifiers);
    this.ciphers.putAll(ciphers);
  }

  /**
   * 위챗페이 콜백 알림 파싱
   *
   * @param requestParam 알림 파싱에 필요한 요청 파라미터
   * @param decryptObjectClass 복호화할 데이터의 Class 객체
   * @param <T> Class 객체로 모델링된 클래스의 타입
   * @return 복호화된 콜백 메시지
   * @throws MalformedMessageException 콜백 알림 파라미터가 올바르지 않거나 알림 데이터 파싱 실패
   * @throws ValidationException 서명 검증 실패
   */
  public <T> T parse(RequestParam requestParam, Class<T> decryptObjectClass) {
    validateRequest(requestParam);
    return getDecryptObject(requestParam, requireNonNull(decryptObjectClass));
  }

  private void validateRequest(RequestParam requestParam) {
    if (requestParam == null) {
      throw new ValidationException(
          "Verify WechatPay notification parameters, requestParam is null.");
    }
    if (requestParam.getSignType() == null) {
      throw new ValidationException(
          String.format(
              "Verify WechatPay notification parameters, signType is empty" + ".RequestParam[%s]",
              requestParam));
    }
    if (requestParam.getSerialNumber() == null) {
      throw new ValidationException(
          String.format(
              "Verify WechatPay notification parameters, serialNumber is empty"
                  + ".RequestParam[%s]",
              requestParam));
    }
    if (requestParam.getMessage() == null) {
      throw new ValidationException(
          String.format(
              "Verify WechatPay notification parameters, message is empty" + ".RequestParam[%s]",
              requestParam));
    }
    if (requestParam.getSignature() == null) {
      throw new ValidationException(
          String.format(
              "Verify WechatPay notification parameters, signature is empty" + ".RequestParam[%s]",
              requestParam));
    }
    Verifier verifier = verifiers.get(requestParam.getSignType());
    if (verifier == null) {
      throw new ValidationException(
          String.format(
              "Processing WechatPay notification, there is no verifier corresponding to signType[%s]",
              requestParam.getSignType()));
    }
    if (!verifier.verify(
        requestParam.getSerialNumber(), requestParam.getMessage(), requestParam.getSignature())) {
      throw new ValidationException(
          String.format(
              "Processing WechatPay notification,signature verification failed,"
                  + "signType[%s]\tserial[%s]\tmessage[%s]\tsign[%s]",
              requestParam.getSignType(),
              requestParam.getSerialNumber(),
              requestParam.getMessage(),
              requestParam.getSignature()));
    }
  }

  private <T> T getDecryptObject(RequestParam requestParam, Class<T> decryptObjectClass) {
    Notification notification = gson.fromJson(requestParam.getBody(), Notification.class);
    validateNotification(notification);
    String algorithm = notification.getResource().getAlgorithm();
    String associatedData = notification.getResource().getAssociatedData();
    String nonce = notification.getResource().createNonce();
    String ciphertext = notification.getResource().getCiphertext();
    String plaintext = decryptData(algorithm, associatedData, nonce, ciphertext);
    return gson.fromJson(plaintext, decryptObjectClass);
  }

  private void validateNotification(Notification notification) {
    if (notification == null) {
      throw new MalformedMessageException(
          "The notification obtained by parsing the WechatPay notification is null.");
    }
    Resource resource = notification.getResource();
    if (resource == null) {
      throw new MalformedMessageException(
          String.format(
              "The resource obtained by parsing the WechatPay notification is null"
                  + ".Notification[%s]",
              notification));
    }
    if (resource.getAlgorithm() == null) {
      throw new MalformedMessageException(
          String.format(
              "The algorithm obtained by parsing the WechatPay notification is empty.Notification[%s]",
              notification));
    }
    if (resource.getCiphertext() == null) {
      throw new MalformedMessageException(
          String.format(
              "The ciphertext obtained by parsing the WechatPay notification is empty.Notification[%s]",
              notification));
    }
    if (resource.createNonce() == null) {
      throw new MalformedMessageException(
          String.format(
              "The nonce obtained by parsing the WechatPay notification is empty.Notification[%s]",
              notification));
    }
  }

  private String decryptData(
      String algorithm, String associatedData, String nonce, String ciphertext) {
    AeadCipher aeadCipher = ciphers.get(algorithm);
    if (aeadCipher == null) {
      throw new MalformedMessageException(
          "Parse WechatPay notification,There is no AeadCipher corresponding to the algorithm.");
    }
    return aeadCipher.decrypt(
        associatedData.getBytes(StandardCharsets.UTF_8),
        nonce.getBytes(StandardCharsets.UTF_8),
        Base64.getDecoder().decode(ciphertext));
  }
}
