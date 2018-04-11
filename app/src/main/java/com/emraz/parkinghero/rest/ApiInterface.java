package com.emraz.parkinghero.rest;

import com.emraz.parkinghero.rest.response.GoogleResponse;
import com.emraz.parkinghero.rest.response.IbmResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by Yousuf on 7/11/2017.
 */

public interface ApiInterface {

  @GET("json?")
  Call<GoogleResponse> getParkingPlaceFromGoogle(
      @Query("location") String latLang,
      @Query("radius") int radius,
      @Query("type") String type,
      @Query("key") String key


  );

  //newGeoIndex?lat=52.07725093804627&lon=4.345052540302277&radius=200&relation=contains&include_docs=true
  @GET("newGeoIndex?")
  Call<IbmResponse> getParkingPlaceFromIbm(
      @Header("Authorization") String key,
      @Query("lat") double lat,
      @Query("lon") double lng,
      @Query("radius") int radius,
      @Query("relation") String contains,
      @Query("include_docs") boolean status
  );

}
