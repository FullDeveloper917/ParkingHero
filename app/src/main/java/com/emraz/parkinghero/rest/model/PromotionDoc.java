package com.emraz.parkinghero.rest.model;

/**
 * Created by Yousuf on 8/1/2017.
 */

public class PromotionDoc {

  String id;
  String startDate;
  String endDate;
  String title;
  String description;



  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override public String toString() {
    return "PromotionDoc{"
        + "id='"
        + id
        + '\''
        + ", startDate='"
        + startDate
        + '\''
        + ", endDate='"
        + endDate
        + '\''
        + ", title='"
        + title
        + '\''
        + ", description='"
        + description
        + '\''
        + '}';
  }
}
