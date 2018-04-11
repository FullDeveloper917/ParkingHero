package com.emraz.parkinghero.rest.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Yousuf on 8/1/2017.
 */

public class PromotionResponse {

  List<PromotionLocation> locationList;

  public PromotionResponse(List<PromotionLocation> locationList) {
    this.locationList = locationList;
  }

  public List<PromotionLocation> getLocationList() {
    return locationList;
  }

  public void setLocationList(List<PromotionLocation> locationList) {
    this.locationList = locationList;
  }

  @Override public String toString() {
    return "PromotionResponse{" + "locationList=" + locationList == null ? "null" : Arrays.toString(locationList.toArray()) + '}';
  }
}
