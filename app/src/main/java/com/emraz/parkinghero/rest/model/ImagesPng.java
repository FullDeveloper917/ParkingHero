package com.emraz.parkinghero.rest.model;

/**
 * Created by Yousuf on 8/1/2017.
 */

public class ImagesPng {

  String contentType;
  int revPos;
  String digest;
  int length;
  boolean stub;

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public int getRevPos() {
    return revPos;
  }

  public void setRevPos(int revPos) {
    this.revPos = revPos;
  }

  public String getDigest() {
    return digest;
  }

  public void setDigest(String digest) {
    this.digest = digest;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public boolean isStub() {
    return stub;
  }

  public void setStub(boolean stub) {
    this.stub = stub;
  }

  @Override public String toString() {
    return "ImagesPng{"
        + "contentType='"
        + contentType
        + '\''
        + ", revPos="
        + revPos
        + ", digest='"
        + digest
        + '\''
        + ", length="
        + length
        + ", stub="
        + stub
        + '}';
  }
}
