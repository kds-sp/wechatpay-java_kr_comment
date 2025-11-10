package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

/** 위챗페이 도메인 */
public enum HostName {
  API("api.mch.weixin.qq.com"),
  APIHK("apihk.mch.weixin.qq.com");
  private final String value;

  HostName(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public boolean equalsWith(String string) {
    requireNonNull(string);
    return string.startsWith(value);
  }
}
