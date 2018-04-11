package com.emraz.parkinghero.rest.response;

import com.emraz.parkinghero.rest.model.IbmResponseLocation;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yousuf on 7/13/2017.
 */

public class IbmResponse {

  @SerializedName("rows")
  List<IbmResponseLocation> resultList;

  public List<IbmResponseLocation> getResultList() {
    return resultList;
  }

  public void setResultList(List<IbmResponseLocation> resultList) {
    this.resultList = resultList;
  }

  @Override public String toString() {
    return "IbmResponse{" + "resultList=" + resultList == null ? "null" : Arrays.toString(resultList.toArray()) + '}';
  }
}
