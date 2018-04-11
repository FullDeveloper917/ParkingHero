package com.emraz.parkinghero.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yousuf on 7/11/2017.
 */

public class ResponseLocation {

  @SerializedName("lat")
  double latitude;
  @SerializedName("lng")
  double longitude;

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  @Override public String toString() {
    return "ResponseLocation{" + "latitude=" + latitude + ", longitude=" + longitude + '}';
  }
}
