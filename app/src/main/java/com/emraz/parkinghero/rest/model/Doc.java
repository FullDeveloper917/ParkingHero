package com.emraz.parkinghero.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yousuf on 7/13/2017.
 */

public class Doc {

  @SerializedName("geometry")
  IbmGeometry geometry;
  @SerializedName("deviceType")
  String deviceType;
  @SerializedName("deviceInfo")
  DeviceInfo deviceInfo;
  @SerializedName("metadata")
  Metadata metadata;

  public IbmGeometry getGeometry() {
    return geometry;
  }

  public void setGeometry(IbmGeometry geometry) {
    this.geometry = geometry;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public DeviceInfo getDeviceInfo() {
    return deviceInfo;
  }

  public void setDeviceInfo(DeviceInfo deviceInfo) {
    this.deviceInfo = deviceInfo;
  }

  @Override public String toString() {
    return "Doc{"
        + "geometry="
        + geometry
        + ", deviceType='"
        + deviceType
        + '\''
        + ", deviceInfo="
        + deviceInfo
        + ", metadata="
        + metadata
        + '}';
  }
}
