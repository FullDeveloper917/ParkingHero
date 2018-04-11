package com.emraz.parkinghero.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yousuf on 7/13/2017.
 */

public class IbmResponseLocation {

  @SerializedName("id")
  String id;
  @SerializedName("geometry")
  IbmGeometry geometry;

  @SerializedName("doc")
  Doc doc;

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

  public Doc getDoc() {
    return doc;
  }

  public void setDoc(Doc doc) {
    this.doc = doc;
  }

  @Override public String toString() {
    return "IbmResponseLocation{"
        + "id='"
        + id
        + '\''
        + ", geometry="
        + geometry
        + ", doc="
        + doc
        + '}';
  }
}
