package com.emraz.parkinghero.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yousuf on 7/11/2017.
 */

public class GoogleGeometry {

  @SerializedName("location")
  ResponseLocation location;

  public ResponseLocation getLocation() {
    return location;
  }

  public void setLocation(ResponseLocation location) {
    this.location = location;
  }

  @Override public String toString() {
    return "GoogleGeometry{" + "location=" + location + '}';
  }
}
