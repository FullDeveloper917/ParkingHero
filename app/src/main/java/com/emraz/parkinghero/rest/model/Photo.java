package com.emraz.parkinghero.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yousuf on 7/11/2017.
 */

public class Photo {

  @SerializedName("width")
  int width;
  @SerializedName("height")
  int height;
  @SerializedName("photo_reference")
  String photoRef;

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public String getPhotoRef() {
    return photoRef;
  }

  public void setPhotoRef(String photoRef) {
    this.photoRef = photoRef;
  }

  @Override public String toString() {
    return "Photo{"
        + "width="
        + width
        + ", height="
        + height
        + ", photoRef='"
        + photoRef
        + '\''
        + '}';
  }
}
