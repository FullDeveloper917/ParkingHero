package com.emraz.parkinghero.rest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yousuf on 7/13/2017.
 */

public class Metadata {

  @SerializedName("Operator")
  String operator;
  @SerializedName("Owner")
  String owner;
  @SerializedName("Location_Class")
  String locationClass;
  @SerializedName("Country")
  String country;
  @SerializedName("Street")
  String street;
  @SerializedName("City")
  String city;
  @SerializedName("Location_Type")
  String locationType;

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getLocationClass() {
    return locationClass;
  }

  public void setLocationClass(String locationClass) {
    this.locationClass = locationClass;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getLocationType() {
    return locationType;
  }

  public void setLocationType(String locationType) {
    this.locationType = locationType;
  }

  @Override public String toString() {
    return "Metadata{"
        + "operator='"
        + operator
        + '\''
        + ", owner='"
        + owner
        + '\''
        + ", locationClass='"
        + locationClass
        + '\''
        + ", country='"
        + country
        + '\''
        + ", street='"
        + street
        + '\''
        + ", city='"
        + city
        + '\''
        + ", locationType='"
        + locationType
        + '\''
        + '}';
  }
}
