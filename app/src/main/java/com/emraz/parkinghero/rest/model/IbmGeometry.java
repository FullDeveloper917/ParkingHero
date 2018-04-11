package com.emraz.parkinghero.rest.model;

import com.emraz.parkinghero.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yousuf on 7/13/2017.
 */

public class IbmGeometry {

  @SerializedName("type")
  String type;
  @SerializedName("coordinates")
  List<Double> coordinates;

  LatLng latLng;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<Double> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(List<Double> coordinates) {
    this.coordinates = coordinates;
    if (coordinates != null) {
      if (coordinates.size()==2) {
        setLatLng(new LatLng(coordinates.get(1), coordinates.get(0)));
      }
    }
  }

  public LatLng getLatLng() {
    if (latLng == null) {
      if (this.coordinates != null) {
        if (coordinates.size()==2) {
          latLng = new LatLng(coordinates.get(1), coordinates.get(0));
        }
      }else {
        Log.d("IbmGeometry", "coordinate is null" );
      }
    }else {
      Log.d("IbmGeometry", "LatLng is not null: " + latLng.latitude + "," + latLng.longitude );
    }
    return latLng;
  }

  public void setLatLng(LatLng latLng) {
    this.latLng = latLng;
  }

  @Override public String toString() {
    return "IbmGeometry{"
        + "type='"
        + type
        + '\''
        + ", coordinates="
        + coordinates == null ? "null" : Arrays.toString(coordinates.toArray())
        + ", latLng="
        + latLng
        + '}';
  }
}
