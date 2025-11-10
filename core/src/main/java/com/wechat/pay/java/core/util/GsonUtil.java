package com.wechat.pay.java.core.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/** Gson 유틸리티 클래스 */
public class GsonUtil {

  private GsonUtil() {}

  private static final Gson gson;

  static {
    gson =
        new GsonBuilder()
            .disableHtmlEscaping()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .addSerializationExclusionStrategy(
                new ExclusionStrategy() {
                  @Override
                  public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                    return expose != null && !expose.serialize();
                  }

                  @Override
                  public boolean shouldSkipClass(Class<?> aClass) {
                    return false;
                  }
                })
            .addDeserializationExclusionStrategy(
                new ExclusionStrategy() {
                  @Override
                  public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    final Expose expose = fieldAttributes.getAnnotation(Expose.class);
                    return expose != null && !expose.deserialize();
                  }

                  @Override
                  public boolean shouldSkipClass(Class<?> aClass) {
                    return false;
                  }
                })
            .create();
  }

  /**
   * 사용자 정의 설정된 Gson 객체 가져오기
   *
   * @return Gson 객체
   */
  public static Gson getGson() {
    return gson;
  }

  /**
   * 객체를 JSON 형식 문자열로 변환
   *
   * @param object 변환할 객체
   * @return JSON 형식 문자열
   */
  public static String toJson(Object object) {
    return gson.toJson(object);
  }
}
