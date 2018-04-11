package com.emraz.parkinghero.rest.model;

import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yousuf on 7/11/2017.
 */

public class GoogleResponseLocation {

  @SerializedName("geometry")
  GoogleGeometry geometry;
  @SerializedName("icon")
  String iconUrl;
  @SerializedName("id")
  String id;
  @SerializedName("name")
  String name;
  @SerializedName("opening_hours")
  Opening opening;
  @SerializedName("photos")
  List<Photo> photoList;
  @SerializedName("rating")
  float rating;
  @SerializedName("vicinity")
  String vicinity;

  public GoogleGeometry getGeometry() {
    return geometry;
  }

  public void setGeometry(GoogleGeometry geometry) {
    this.geometry = geometry;
  }

  public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Opening getOpening() {
    return opening;
  }

  public void setOpening(Opening opening) {
    this.opening = opening;
  }

  public List<Photo> getPhotoList() {
    return photoList;
  }

  public void setPhotoList(List<Photo> photoList) {
    this.photoList = photoList;
  }

  public float getRating() {
    return rating;
  }

  public void setRating(float rating) {
    this.rating = rating;
  }

  public String getVicinity() {
    return vicinity;
  }

  public void setVicinity(String vicinity) {
    this.vicinity = vicinity;
  }

  @Override public String toString() {
    String str =  "GoogleResponse{"
        + "geometry="
        + geometry
        + ", iconUrl='"
        + iconUrl
        + '\''
        + ", id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", opening="
        + opening
        + ", photoList=";

      if (photoList != null){
        str += Arrays.toString(photoList.toArray());
      }else {
        str+= "null";
      }


       // + photoList == null ? "null" : Arrays.toString(photoList.toArray())

    str+=  ", rating="
        + rating
        + ", vicinity='"
        + vicinity
        + '\''
        + '}';

    return str;
  }

}
