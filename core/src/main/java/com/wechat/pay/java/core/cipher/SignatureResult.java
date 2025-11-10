package com.wechat.pay.java.core.cipher;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.util.GsonUtil;

/** 서명 결과 */
public class SignatureResult {

  private final String signature;

  private final String certificateSerialNumber;

  public SignatureResult(String signature, String certificateSerialNumber) {
    this.signature = requireNonNull(signature);
    this.certificateSerialNumber = requireNonNull(certificateSerialNumber);
  }

  /**
   * 서명 가져오기
   *
   * @return 서명
   */
  public String getSign() {
    return signature;
  }

  /**
   * 서명에 해당하는 가맹점 인증서 시리얼 번호 가져오기
   *
   * @return 가맹점 인증서 시리얼 번호
   */
  public String getCertificateSerialNumber() {
    return certificateSerialNumber;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }
}
