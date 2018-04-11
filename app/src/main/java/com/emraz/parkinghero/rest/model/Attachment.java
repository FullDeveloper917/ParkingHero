package com.emraz.parkinghero.rest.model;

/**
 * Created by Yousuf on 8/1/2017.
 */

public class Attachment {

  ImagesPng imagesPng;

  public Attachment(ImagesPng imagesPng) {
    this.imagesPng = imagesPng;
  }

  public ImagesPng getImagesPng() {
    return imagesPng;
  }

  public void setImagesPng(ImagesPng imagesPng) {
    this.imagesPng = imagesPng;
  }

  @Override public String toString() {
    return "Attachment{" + "imagesPng=" + imagesPng + '}';
  }
}
