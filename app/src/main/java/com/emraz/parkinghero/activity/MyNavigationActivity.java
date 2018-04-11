package com.emraz.parkinghero.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.emraz.parkinghero.R;
import com.emraz.parkinghero.rest.ApiClient;
import com.emraz.parkinghero.rest.ApiInterface;
import com.emraz.parkinghero.rest.model.DeviceInfo;
import com.emraz.parkinghero.rest.model.Doc;
import com.emraz.parkinghero.rest.model.IBMParkingLocationStreet;
import com.emraz.parkinghero.rest.model.IbmParkingLocationClass;
import com.emraz.parkinghero.rest.model.IbmResponseLocation;
import com.emraz.parkinghero.rest.model.Metadata;
import com.emraz.parkinghero.rest.response.IbmResponse;
import com.emraz.parkinghero.util.Constants;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.mapbox.geojson.Point;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigationOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationConstants;
import com.mapbox.services.android.navigation.v5.utils.LocaleUtils;
import com.mapbox.turf.TurfMeasurement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.emraz.parkinghero.util.Common.*;
import static com.mapbox.services.android.navigation.v5.navigation.NavigationUnitType.NONE_SPECIFIED;

public class MyNavigationActivity extends AppCompatActivity implements OnNavigationReadyCallback, NavigationListener {

    private static final String TAG = "MyNavigationActivity";
    private NavigationView navigationView;
    private boolean isRunning;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(com.mapbox.services.android.navigation.ui.v5.R.style.Theme_AppCompat_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(com.mapbox.services.android.navigation.ui.v5.R.layout.activity_navigation);
        navigationView = findViewById(com.mapbox.services.android.navigation.ui.v5.R.id.navigationView);
        navigationView.onCreate(savedInstanceState);
        navigationView.getNavigationAsync(this);

        if ( !destinationParkingClassType.equals("google") && !destinationParkingClassType.equals("promotion")){
            // 10 seconds progressing
            final Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void run() {


                            Location mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                            com.mapbox.mapboxsdk.geometry.LatLng currentCoord = new com.mapbox.mapboxsdk.geometry.LatLng(originPoint.latitude(), originPoint.longitude());

                            if (mLastKnownLocation != null)
                                currentCoord = new com.mapbox.mapboxsdk.geometry.LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                            com.mapbox.mapboxsdk.geometry.LatLng destinationCoord = new com.mapbox.mapboxsdk.geometry.LatLng(destinationPoint.latitude(), destinationPoint.longitude());

                            double arriveTime = getArriveTime(currentCoord, destinationCoord);

                            Toast.makeText(MyNavigationActivity.this, String.valueOf(arriveTime), Toast.LENGTH_SHORT).show();

                            if (arriveTime > 0) {
                                if (arriveTime < 120)
                                    checkCapacity(currentCoord);
                                timer.cancel();
                                timer.purge();
                            }
                        }
                    });
                }
            }, 10000, 10000);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        navigationView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        // If the navigation view didn't need to do anything, call super
        if (!navigationView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        navigationView.onSaveInstanceState(outState);
        outState.putBoolean(NavigationConstants.NAVIGATION_VIEW_RUNNING, isRunning);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navigationView.onRestoreInstanceState(savedInstanceState);
        isRunning = savedInstanceState.getBoolean(NavigationConstants.NAVIGATION_VIEW_RUNNING);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigationView.onDestroy();
    }

    @Override
    public void onNavigationReady() {
        NavigationViewOptions.Builder options = NavigationViewOptions.builder();
        options.navigationListener(this);
        if (!isRunning) {
            extractRoute(options);
            extractCoordinates(options);
        }
        extractConfiguration(options);
        extractLocale(options);
        navigationView.startNavigation(options.build());
        isRunning = true;
    }

    @Override
    public void onCancelNavigation() {
        // Navigation canceled, finish the activity
        finish();
    }

    @Override
    public void onNavigationFinished() {
        // Navigation finished, finish the activity
        finish();
    }

    @Override
    public void onNavigationRunning() {
        // Intentionally empty
    }

    private void extractRoute(NavigationViewOptions.Builder options) {
        options.directionsRoute(MyNavigationLauncher.extractRoute(this));
    }

    private void extractCoordinates(NavigationViewOptions.Builder options) {
        HashMap<String, Point> coordinates = MyNavigationLauncher.extractCoordinates(this);
        if (coordinates.size() > 0) {
            options.origin(coordinates.get(NavigationConstants.NAVIGATION_VIEW_ORIGIN));
            options.destination(coordinates.get(NavigationConstants.NAVIGATION_VIEW_DESTINATION));
        }
    }

    private void extractConfiguration(NavigationViewOptions.Builder options) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        options.awsPoolId(preferences
                .getString(NavigationConstants.NAVIGATION_VIEW_AWS_POOL_ID, null));
        options.shouldSimulateRoute(preferences
                .getBoolean(NavigationConstants.NAVIGATION_VIEW_SIMULATE_ROUTE, false));
    }

    private void extractLocale(NavigationViewOptions.Builder options) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String country = preferences.getString(NavigationConstants.NAVIGATION_VIEW_LOCALE_COUNTRY, "");
        String language = preferences.getString(NavigationConstants.NAVIGATION_VIEW_LOCALE_LANGUAGE, "");
        int unitType = preferences.getInt(NavigationConstants.NAVIGATION_VIEW_UNIT_TYPE, NONE_SPECIFIED);

        Locale locale;
        if (!language.isEmpty()) {
            locale = new Locale(language, country);
        } else {
            locale = LocaleUtils.getDeviceLocale(this);
        }

        MapboxNavigationOptions navigationOptions = MapboxNavigationOptions.builder()
                .locale(locale)
                .unitType(unitType)
                .build();
        options.navigationOptions(navigationOptions);
    }



    private void checkCapacity(com.mapbox.mapboxsdk.geometry.LatLng latLng) {
        Toast.makeText(MyNavigationActivity.this, "cecking capacity...", Toast.LENGTH_SHORT).show();

        if (latLng == null) {
            android.util.Log.d(TAG, "latlang null");
            return;
        }

        ApiInterface apiServiceIbm = ApiClient.getClient(Constants.RETROFIT_TYPE_IBM).create(ApiInterface.class);

        String authToken = Credentials.basic("imentstralliatherederess", "9dea22ce6e159f2f7c9f709e7359660117006e8c");
        android.util.Log.d(TAG, authToken);

        Call<IbmResponse> call = apiServiceIbm.getParkingPlaceFromIbm(authToken, latLng.getLatitude(), latLng.getLongitude(), displayRadius, "contains", true);

        call.enqueue(new Callback<IbmResponse>() {
            @Override
            public void onResponse(Call<IbmResponse> call, Response<IbmResponse> response) {
                int statusCode = response.code();
                android.util.Log.d(TAG, "status Code: " + statusCode);

                IbmResponse resultResponse = response.body();

                if (resultResponse != null) {

                    List<IbmResponseLocation> resultList = resultResponse.getResultList();
                    if (resultList != null) {

                        List<IBMParkingLocationStreet> ibmLocationList = new ArrayList<>();
                        Map<String, IBMParkingLocationStreet> streetToLocationMap = new HashMap<>();
                        for (IbmResponseLocation ibmResponseLocation : resultList) {

                            if (ibmResponseLocation != null) {
                                Doc doc = ibmResponseLocation.getDoc();
                                if (doc != null) {
                                    String deviceType = ibmResponseLocation.getDoc().getDeviceType();
                                    // check its parking sensor
                                    if (deviceType.equals(Constants.SENSOR_TYPE)) {

                                        Metadata metadata = doc.getMetadata();
                                        String street = metadata.getStreet();
                                        if (street != null) {
                                            IBMParkingLocationStreet parkingLocation;
                                            if (streetToLocationMap.containsKey(street)) {
                                                // already exist
                                                parkingLocation = streetToLocationMap.get(street);

                                            } else {
                                                parkingLocation = new IBMParkingLocationStreet(street);
                                                streetToLocationMap.put(street, parkingLocation);
                                            }

                                            String locationClass = metadata.getLocationClass();
                                            if (parkingLocation != null && locationClass != null) {

                                                parkingLocation.addLocationToType(ibmResponseLocation, locationClass);

                                            }

                                        }

                                    }
                                }
                            }

                        }


                        for (Map.Entry<String, IBMParkingLocationStreet> entry : streetToLocationMap.entrySet()) {
                            ibmLocationList.add(entry.getValue());
                        }

                        if (ibmLocationList.size() == 0) return;

                        double minDistance = displayRadius;
                        Point candidatePoint = null;
                        boolean isCurrentParkingBusy = false;
                        for (IBMParkingLocationStreet parkingLocation: ibmLocationList) {
                            IbmParkingLocationClass locationClass = null;
                            if (parkingLocation != null) {

                                switch (destinationParkingClassType) {
                                    case Constants.LOCATION_CLASS_PUBLIC:
                                        locationClass = parkingLocation.getPublicType();
                                        break;
                                    case Constants.LOCATION_CLASS_DISABLED:
                                        locationClass = parkingLocation.getDisabledType();
                                        break;
                                    case Constants.LOCATION_CLASS_TAXI:
                                        locationClass = parkingLocation.getTaxiType();
                                        break;
                                    case Constants.LOCATION_CLASS_TRUCK:
                                        locationClass = parkingLocation.getTruckType();
                                        break;
                                    case Constants.LOCATION_CLASS_PICKUP:
                                        locationClass = parkingLocation.getPickupType();
                                        break;
                                    case Constants.LOCATION_CLASS_TOURING_BUS:
                                        locationClass = parkingLocation.getTouringBusType();
                                        break;
                                    case Constants.LOCATION_CLASS_VALET_PARKING:
                                        locationClass = parkingLocation.getValetParkingType();
                                        break;
                                    case Constants.LOCATION_CLASS_CHARGING_STATION:
                                        locationClass = parkingLocation.getChargingStationType();
                                        break;
                                    default:
                                        locationClass = parkingLocation.getPublicType();
                                        break;
                                }
                            }


                            List<IbmResponseLocation> locationList = locationClass.getLocationList();
                            if (locationList.size() > 0) {


                                int total = locationList.size();
                                int busy = 0;


                                for (IbmResponseLocation ibmResponseLocation : locationList) {
                                    if (ibmResponseLocation.getDoc() != null) {
                                        DeviceInfo deviceInfo = ibmResponseLocation.getDoc().getDeviceInfo();
                                        if (deviceInfo != null) {
                                            String status = deviceInfo.getDescription();
                                            if (status != null) {
                                                if (status.equals(Constants.STATUS_BUSY)) {
                                                    busy++;
                                                }
                                            }
                                        }
                                    }
                                }

                                locationClass.setnBusy(busy);
                                locationClass.setnFree(total - busy);

                                IbmResponseLocation location = locationList.get(locationList.size() - 1);

                                Point parkingPoint = Point.fromLngLat(location.getGeometry().getLatLng().longitude, location.getGeometry().getLatLng().latitude);

                                double distance = TurfMeasurement.distance(parkingPoint, destinationPoint) * 1000;

                                if (distance < minDistance) {
                                    if (distance < 5) {
                                        isCurrentParkingBusy = (locationClass.getnFree() == 0);
                                    } else {
                                        minDistance = distance;
                                        if (locationClass.getnFree() > 0) {
                                            candidatePoint = parkingPoint;
                                        }
                                    }
                                }
                            }
                        }

                        Toast.makeText(MyNavigationActivity.this, "isCurrentPakingBusy is " + String.valueOf(isCurrentParkingBusy), Toast.LENGTH_SHORT).show();
                        if (isCurrentParkingBusy) {
                            if (candidatePoint == null) {
                                new AlertDialog.Builder(MyNavigationActivity.this)
                                        .setTitle("Warning!")
                                        .setMessage("All parking is busy now.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            } else {
                                final Point finalCandidatePoint = candidatePoint;
                                new AlertDialog.Builder(MyNavigationActivity.this)
                                        .setTitle("Warning!")
                                        .setMessage("Destination parking is busy now, Do you change parking?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                                Location mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                                                if (mLastKnownLocation != null )
                                                    originPoint = Point.fromLngLat(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                                                destinationPoint = finalCandidatePoint;

                                                NavigationViewOptions options = NavigationViewOptions.builder()
                                                        .origin(originPoint)
                                                        .destination(destinationPoint)
                                                        .awsPoolId(null)
                                                        .shouldSimulateRoute(false)
                                                        .build();

                                                finish();
                                                // Call this method with Context from within an Activity
                                                MyNavigationLauncher.startNavigation(MyNavigationActivity.this, options);
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        }

                    }

                } else {
                    android.util.Log.d(TAG, "Ibm Response is null");
                }
            }

            @Override
            public void onFailure(Call<IbmResponse> call, Throwable t) {

            }
        });
    }

    private double getArriveTime(com.mapbox.mapboxsdk.geometry.LatLng origin, com.mapbox.mapboxsdk.geometry.LatLng destination) {
        StringBuilder finalApi = new StringBuilder("https://api.mapbox.com/directions-matrix/v1/mapbox/driving/");
        finalApi.append(origin.getLongitude()).append(",").append(origin.getLatitude()).append(";");
        finalApi.append(destination.getLongitude()).append(",").append(destination.getLatitude());
        finalApi.append("?access_token=").append(getString(R.string.mapbox_access_token));

        JsonObject resultJson = null;
        try {
            resultJson = Ion.with(this)
                    .load(finalApi.toString()).asJsonObject().get();
        } catch (InterruptedException | ExecutionException e) {
            return -1;
        }

        if (resultJson == null || resultJson.isJsonNull())
            return -1;
        if (!resultJson.has("code"))
            return -1;
        if (!resultJson.get("code").getAsString().equals("Ok"))
            return -1;
        if (!resultJson.has("durations"))
            return -1;

        JsonArray durationsJsonArray = resultJson.get("durations").getAsJsonArray();
        if (durationsJsonArray.isJsonNull())
            return  -1;

        return durationsJsonArray.get(0).getAsJsonArray().get(1).getAsDouble();
    }
}
