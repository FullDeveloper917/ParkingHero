package com.emraz.parkinghero.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yousuf on 7/17/2017.
 */

public class DeviceInfo {


  @SerializedName("manufacturer")
  String manufacturer;
  @SerializedName("model")
  String model;
  @SerializedName("deviceClass")
  String deviceClass;
  @SerializedName("description")
  String description;

  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getDeviceClass() {
    return deviceClass;
  }

  public void setDeviceClass(String deviceClass) {
    this.deviceClass = deviceClass;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override public String toString() {
    return "DeviceInfo{"
        + "manufacturer='"
        + manufacturer
        + '\''
        + ", model='"
        + model
        + '\''
        + ", deviceClass='"
        + deviceClass
        + '\''
        + ", description='"
        + description
        + '\''
        + '}';
  }
}
