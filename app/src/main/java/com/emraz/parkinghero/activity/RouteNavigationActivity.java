package com.emraz.parkinghero.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.emraz.parkinghero.R;
import com.emraz.parkinghero.rest.ApiClient;
import com.emraz.parkinghero.rest.ApiInterface;
import com.emraz.parkinghero.rest.model.DeviceInfo;
import com.emraz.parkinghero.rest.model.Doc;
import com.emraz.parkinghero.rest.model.IBMParkingLocationStreet;
import com.emraz.parkinghero.rest.model.IbmGeometry;
import com.emraz.parkinghero.rest.model.IbmParkingLocationClass;
import com.emraz.parkinghero.rest.model.IbmResponseLocation;
import com.emraz.parkinghero.rest.model.Metadata;
import com.emraz.parkinghero.rest.response.IbmResponse;
import com.emraz.parkinghero.util.Constants;
import com.emraz.parkinghero.util.UIUtil;
import com.emraz.parkinghero.util.Util;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.ui.IconGenerator;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;

// classes needed to add location layer
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import android.location.Location;
import com.mapbox.mapboxsdk.geometry.LatLng;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.services.android.location.LostLocationEngine;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationUnitType;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.mapbox.services.android.telemetry.location.LocationEnginePriority;
import com.mapbox.services.android.telemetry.permissions.PermissionsListener;
import com.mapbox.services.android.telemetry.permissions.PermissionsManager;

// classes needed to add a marker
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.turf.TurfMeasurement;

import static com.emraz.parkinghero.util.Common.displayRadius;

public class RouteNavigationActivity extends AppCompatActivity implements LocationEngineListener, PermissionsListener {

    private static final String ARRIVE_TIME_API = "https://api.mapbox.com/directions-matrix/v1/mapbox/driving/";

    private MapView mapView;

    // variables for adding location layer
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private Location originLocation;

    // variables for adding a marker
    private Marker destinationMarker;
    private LatLng originCoord;
    private LatLng destinationCoord;

    // variables for calculating and drawing a route
    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;

    // destination location info
    private double destLat, destLng;
    private String destinationClassType;
    private double zoomRate;

    private ApiInterface apiServiceGoogle, apiServiceIbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_route_navigation);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        Intent intent = getIntent();
        zoomRate = intent.getDoubleExtra("zoomRate", 15.0);
        destLat = intent.getDoubleExtra("destLat", 200.0);
        destLng = intent.getDoubleExtra("destLng", 100.0);
        destinationClassType = intent.getStringExtra("locationClass");


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                map = mapboxMap;
                enableLocationPlugin();

            }
        });
    }

    private void startRouteNavigation() {
        originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());

        destinationCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());
        if (destLat < 180 && destLat > -180 && destLng < 90 && destLng > -90)
            destinationCoord = new LatLng(destLat, destLng);

        destinationMarker = map.addMarker(new MarkerOptions().position(destinationCoord));

        destinationPosition = Point.fromLngLat(destinationCoord.getLongitude(), destinationCoord.getLatitude());
        originPosition = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());
//        getRoute(originPosition, destinationPosition);

        // Pass in your Amazon Polly pool id for speech synthesis using Amazon Polly
        // Set to null to use the default Android speech synthesizer
        NavigationViewOptions options = NavigationViewOptions.builder()
                .origin(originPosition)
                .destination(destinationPosition)
                .awsPoolId(null)
                .shouldSimulateRoute(false)
                .build();

        // Call this method with Context from within an Activity
        NavigationLauncher.startNavigation(RouteNavigationActivity.this, options);


        // 10 seconds progressing
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        @SuppressLint("MissingPermission")
                        Location currentLocation = locationEngine.getLastLocation();

                        LatLng currentCoord = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        double arriveTime = getArriveTime(currentCoord, destinationCoord);

                        if (arriveTime < 120 && arriveTime > 0) {
                            checkCapacity(originCoord);
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });
            }
        }, 10000, 10000);
    }

    private double getArriveTime(LatLng origin, LatLng destination) {
        StringBuilder finalApi = new StringBuilder(ARRIVE_TIME_API);
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

//    private void getRoute(Point origin, Point destination) {
//        NavigationRoute.builder()
//                .accessToken(Mapbox.getAccessToken())
//                .origin(origin)
//                .destination(destination)
//                .build()
//                .getRoute(new Callback<DirectionsResponse>() {
//                    @Override
//                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//                        // You can get the generic HTTP info about the response
//                        Log.d(TAG, "Response code: " + response.code());
//                        if (response.body() == null) {
//                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
//                            return;
//                        } else if (response.body().routes().size() < 1) {
//                            Log.e(TAG, "No routes found");
//                            return;
//                        }
//
//                        currentRoute = response.body().routes().get(0);
//
//                        // Draw the route on the map
//                        if (navigationMapRoute != null) {
//                            navigationMapRoute.removeRoute();
//                        } else {
//                            navigationMapRoute = new NavigationMapRoute(null, mapView, map, R.style.NavigationMapRoute);
//                        }
//                        navigationMapRoute.addRoute(currentRoute);
//                    }
//
//                    @Override
//                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
//                        Log.e(TAG, "Error: " + throwable.getMessage());
//                    }
//                });
//    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Create an instance of LOST location engine
            initializeLocationEngine();

            locationPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
            locationPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    private void initializeLocationEngine() {
        locationEngine = new LostLocationEngine(RouteNavigationActivity.this);
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
//            setCameraPosition(lastLocation);
            startRouteNavigation();
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }

//    private void setCameraPosition(Location location) {
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                new LatLng(location.getLatitude(), location.getLongitude()), zoomRate));
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationPlugin();
        } else {
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            originLocation = location;
//            setCameraPosition(location);
            startRouteNavigation();
            locationEngine.removeLocationEngineListener(this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    private void checkCapacity(LatLng latLng) {

        if (latLng == null) {
            Log.d(TAG, "latlang null");
            return;
        }

        if (apiServiceIbm == null) {
            apiServiceIbm = ApiClient.getClient(Constants.RETROFIT_TYPE_IBM).create(ApiInterface.class);
        }

        String authToken = Credentials.basic("imentstralliatherederess", "9dea22ce6e159f2f7c9f709e7359660117006e8c");
        Log.d(TAG, authToken);

        Call<IbmResponse> call = apiServiceIbm.getParkingPlaceFromIbm(authToken, latLng.getLatitude(), latLng.getLongitude(), displayRadius, "contains", true);

        call.enqueue(new Callback<IbmResponse>() {
            @Override
            public void onResponse(Call<IbmResponse> call, Response<IbmResponse> response) {
                int statusCode = response.code();
                Log.d(TAG, "status Code: " + statusCode);

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

                                switch (destinationClassType) {
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

                                double distance = TurfMeasurement.distance(parkingPoint, destinationPosition) * 1000;

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

                        if (isCurrentParkingBusy) {
                            if (candidatePoint == null) {
                                new AlertDialog.Builder(RouteNavigationActivity.this)
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
                                new AlertDialog.Builder(RouteNavigationActivity.this)
                                        .setTitle("Warning!")
                                        .setMessage("Destination parking is busy now, Do you change parking?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                                originLocation = locationEngine.getLastLocation();
                                                originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());
                                                originPosition = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());

                                                destinationPosition = finalCandidatePoint;
                                                destinationCoord = new LatLng(finalCandidatePoint.latitude(), finalCandidatePoint.longitude());
                                                destinationMarker = map.addMarker(new MarkerOptions().position(destinationCoord));

//                                                getRoute(originPosition, destinationPosition);


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
                    Log.d(TAG, "Ibm Response is null");
                }
            }

            @Override
            public void onFailure(Call<IbmResponse> call, Throwable t) {

            }
        });
    }



}

