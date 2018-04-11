package com.emraz.parkinghero.rest.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yousuf on 7/18/2017.
 */

public class IbmParkingLocationClass {

  String title;
  String subTitle;
  boolean isBusy;
  int nBusy, nFree;
  int color;
  String description;
  String locationClass;

  List<IbmResponseLocation> locationList;

  public IbmParkingLocationClass(String locationClass) {
    this.locationClass = locationClass;
    this.locationList = new ArrayList<>();
    this.title = "";
    this.subTitle = "";
  }


  public void addToList(IbmResponseLocation location){
    this.locationList.add(location);
  }

  public int getnBusy() {
    return nBusy;
  }

  public void setnBusy(int nBusy) {
    this.nBusy = nBusy;
  }

  public int getnFree() {
    return nFree;
  }

  public void setnFree(int nFree) {
    this.nFree = nFree;
  }

  public String getLocationClass() {
    return locationClass;
  }

  public void setLocationClass(String locationClass) {
    this.locationClass = locationClass;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubTitle() {
    return subTitle;
  }

  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  public boolean isBusy() {
    return isBusy;
  }

  public void setBusy(boolean busy) {
    isBusy = busy;
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<IbmResponseLocation> getLocationList() {
    return locationList;
  }

  @Override public String toString() {
    return "IbmParkingLocationClass{"
        + "isBusy="
        + isBusy
        + ", color="
        + color
        + ", description='"
        + description
        + '\''
        + ", locationList="
        + locationList == null ? " Null " : Arrays.toString(locationList.toArray())
        + '}';
  }
}
