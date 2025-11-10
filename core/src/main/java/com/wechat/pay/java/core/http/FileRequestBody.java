package com.wechat.pay.java.core.http;

import static java.util.Objects.requireNonNull;

import com.wechat.pay.java.core.util.GsonUtil;
import java.net.URLConnection;

/** 파일 타입 요청 본문 */
public final class FileRequestBody implements RequestBody {

  private final String meta;
  private final String fileName;
  private final byte[] file;

  private FileRequestBody(String meta, String fileName, byte[] file) {
    this.meta = meta;
    this.fileName = fileName;
    this.file = file;
  }

  /**
   * 미디어 파일 메타 정보 가져오기
   *
   * @return 미디어 파일 메타 정보
   */
  public String getMeta() {
    return meta;
  }

  /**
   * 파일명 가져오기
   *
   * @return 파일명
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * 파일 가져오기
   *
   * @return 파일
   */
  public byte[] getFile() {
    return file;
  }

  @Override
  public String getContentType() {
    String contentTypeFromName = URLConnection.guessContentTypeFromName(fileName);
    if (contentTypeFromName == null) {
      // 어떤 종류의 바이너리 데이터임을 나타냄
      return MediaType.APPLICATION_OCTET_STREAM.getValue();
    }
    return contentTypeFromName;
  }

  @Override
  public String toString() {
    return GsonUtil.getGson().toJson(this);
  }

  public static class Builder {

    private String meta;
    private String fileName;
    private byte[] file;

    public Builder meta(String meta) {
      this.meta = meta;
      return this;
    }

    public Builder fileName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    public Builder file(byte[] file) {
      this.file = file;
      return this;
    }

    public FileRequestBody build() {
      return new FileRequestBody(
          requireNonNull(meta), requireNonNull(fileName), requireNonNull(file));
    }
  }
}
