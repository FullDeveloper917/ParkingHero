package com.emraz.parkinghero.rest;

import com.emraz.parkinghero.util.Constants;

import java.util.ConcurrentModificationException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yousuf on 7/11/2017.
 */

public class ApiClient {

    // public static final String BASE_URL = BuildConfig.API_BASE_URL;
    private static Retrofit retrofitGoogle = null;
    private static Retrofit retrofitIBM = null;


    public static Retrofit getClient(int type) {

        if (type == Constants.RETROFIT_TYPE_GOOGLE) {

            if (retrofitGoogle == null) {
                retrofitGoogle = new Retrofit.Builder()
                        .baseUrl(Constants.GOOGLE_API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }

            return retrofitGoogle;

        } else if (type == Constants.RETROFIT_TYPE_IBM) {

            if (retrofitIBM == null) {
                retrofitIBM = new Retrofit.Builder()
                        .baseUrl(Constants.IBM_API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }

            return retrofitIBM;
        }

        return null;
    }


}
