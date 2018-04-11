package com.emraz.parkinghero.rest.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yousuf on 7/11/2017.
 */

public class Opening {
  @SerializedName("open_now")
  boolean openNow;
  @SerializedName("weekday_text")
  ArrayList<String> weekDayTextList;

  public boolean isOpenNow() {
    return openNow;
  }

  public void setOpenNow(boolean openNow) {
    this.openNow = openNow;
  }

  public ArrayList<String> getWeekDayTextList() {
    return weekDayTextList;
  }

  public void setWeekDayTextList(ArrayList<String> weekDayTextList) {
    this.weekDayTextList = weekDayTextList;
  }

  @Override public String toString() {
    return "Opening{" +
        "openNow=" + openNow +
        ", weekDayTextList=" + weekDayTextList == null ? "null" : Arrays.toString(weekDayTextList.toArray()) +
        '}';
  }
}
