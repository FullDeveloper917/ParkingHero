package com.emraz.parkinghero.rest.model;

/**
 * Created by Yousuf on 8/1/2017.
 */

public class PromotionLocation {

  String id;
  IbmGeometry geometry;
  PromotionDoc promotionDoc;
  Attachment attachment;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public IbmGeometry getGeometry() {
    return geometry;
  }

  public void setGeometry(IbmGeometry geometry) {
    this.geometry = geometry;
  }

  public PromotionDoc getPromotionDoc() {
    return promotionDoc;
  }

  public void setPromotionDoc(PromotionDoc promotionDoc) {
    this.promotionDoc = promotionDoc;
  }

  public Attachment getAttachment() {
    return attachment;
  }

  public void setAttachment(Attachment attachment) {
    this.attachment = attachment;
  }
}
