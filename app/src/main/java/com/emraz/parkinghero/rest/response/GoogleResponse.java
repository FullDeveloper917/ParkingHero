package com.emraz.parkinghero.rest.response;

import com.emraz.parkinghero.rest.model.GoogleResponseLocation;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yousuf on 7/11/2017.
 */

public class GoogleResponse {

  @SerializedName("results")
  List<GoogleResponseLocation> resultList;

  public List<GoogleResponseLocation> getResultList() {
    return resultList;
  }

  public void setResultList(List<GoogleResponseLocation> resultList) {
    this.resultList = resultList;
  }

  @Override public String toString() {
    return "GoogleResponse{" +
        "resultList=" + resultList == null ? "null" : Arrays.toString(resultList.toArray()) +
        '}';
  }
}
