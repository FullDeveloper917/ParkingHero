package com.emraz.parkinghero.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.emraz.parkinghero.R;
import com.emraz.parkinghero.adapter.CustomInfoWindowAdapter;
import com.emraz.parkinghero.rest.ApiClient;
import com.emraz.parkinghero.rest.ApiInterface;
import com.emraz.parkinghero.rest.model.Attachment;
import com.emraz.parkinghero.rest.model.DeviceInfo;
import com.emraz.parkinghero.rest.model.Doc;
import com.emraz.parkinghero.rest.model.IBMParkingLocationStreet;
import com.emraz.parkinghero.rest.model.IbmGeometry;
import com.emraz.parkinghero.rest.model.IbmParkingLocationClass;
import com.emraz.parkinghero.rest.model.IbmResponseLocation;
import com.emraz.parkinghero.rest.model.ImagesPng;
import com.emraz.parkinghero.rest.model.Metadata;
import com.emraz.parkinghero.rest.model.PromotionDoc;
import com.emraz.parkinghero.rest.model.PromotionLocation;
import com.emraz.parkinghero.rest.model.PromotionResponse;
import com.emraz.parkinghero.rest.response.GoogleResponse;
import com.emraz.parkinghero.rest.model.GoogleResponseLocation;
import com.emraz.parkinghero.rest.model.Opening;
import com.emraz.parkinghero.rest.model.ResponseLocation;
import com.emraz.parkinghero.rest.response.IbmResponse;
import com.emraz.parkinghero.util.ConnectionDetector;
import com.emraz.parkinghero.util.Constants;
import com.emraz.parkinghero.util.FontManager;
import com.emraz.parkinghero.util.Log;
import com.emraz.parkinghero.util.OnPermissionResult;
import com.emraz.parkinghero.util.PermissionRequest;
import com.emraz.parkinghero.util.UIUtil;

import android.support.design.widget.NavigationView;
import android.widget.Toast;

import com.emraz.parkinghero.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.ui.IconGenerator;
import com.ibm.bluemix.appid.android.api.AppID;
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager;
import com.ibm.mobilefirstplatform.clientsdk.android.analytics.api.Analytics;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.logger.api.Logger;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushException;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationButton;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationCategory;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationOptions;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPSimplePushNotification;
import com.koushikdutta.ion.Ion;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.navigation.ui.v5.NavigationActivity;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.turf.TurfMeasurement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import okhttp3.Credentials;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.emraz.parkinghero.util.Common.*;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener,
        PlaceSelectionListener, View.OnClickListener,
        OnPermissionResult,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnInfoWindowClickListener,
        OnMarkerClickListener {

    private GoogleMap mMap;
    private ConnectionDetector cd;
    private LinearLayout linearLayoutPlaceInfo;
    private TextView textViewPlaceCount;
    private TextView textViewAddress;
    private TextView textViewPlaceAvailability;
    private TextView textViewPlaceType;
    private ImageView imageViewPlace;
    private Button buttonPlaceNavigation;
    private ImageView imageViewPlaceClose;

    // view for marker in map
    private View markerView;
    private ImageView imageViewMarker;
    private TextView textViewFree;

    // FloatingActionButton fabMyLocation;
    private ImageView imageViewMyLocation;

    private String[] accessLocation = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

    private Location mLastKnownLocation;
    private List<String> permissionRequest;
    private PermissionRequest mLocationPermissionRequest;

    private ApiInterface apiServiceGoogle, apiServiceIbm;

    private Marker selectedMarker = null;
    private List<Marker> parkingMarkerList;
    private Circle circle;

    // will Store Ibm Response location for filter
    private List<IBMParkingLocationStreet> ibmLocationList;

    private MFPPushNotificationListener notificationListener;

    // Marker icon
    private int publicIcons[] = {R.drawable.ic_pin_map, R.drawable.ic_pin_map, R.drawable.ic_pin_map};
    private int disabledIcons[] = {R.drawable.ic_disabled_yellow, R.drawable.ic_disabled_red, R.drawable.ic_disabled_green};
    private int pickupIcons[] = {R.drawable.ic_pickup_yellow, R.drawable.ic_pickup_red, R.drawable.ic_pickup_green};
    private int taxiIcons[] = {R.drawable.ic_taxi_yellow, R.drawable.ic_taxi_red, R.drawable.ic_taxi_green};
    private int touristIcons[] = {R.drawable.ic_tourist_yellow, R.drawable.ic_tourist_red, R.drawable.ic_tourist_green};
    private int truckIcons[] = {R.drawable.ic_truck_yellow, R.drawable.ic_truck_red, R.drawable.ic_truck_green};
    private int valetIcons[] = {R.drawable.ic_valet_yellow, R.drawable.ic_valet_red, R.drawable.ic_valet_green};
    private int chargingIcons[] = {R.drawable.ic_charging_yellow, R.drawable.ic_charging_red, R.drawable.ic_charging_green};

    // LatLng for displayPromotion
    private LatLng mApiLatLng = null;


    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_maps);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefFileName, Context.MODE_PRIVATE);
        displayRadius = sharedPreferences.getInt(Constants.TAG_DISPLAY_RADIUS, 500);

        parkingMarkerList = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_action_menu);
        toggle.syncState();

        // setting navigation click listener
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }

            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment =
                (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setHint(getString(R.string.place_search_hint));

        // change search button and EditText property
        try {

            EditText editTextSearch = (EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input);
            AppCompatImageButton searchIconView = (AppCompatImageButton) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button);

            editTextSearch.setTextSize(18.f);

            editTextSearch.setPadding(0, 0, 0, 0);
            searchIconView.setPadding(0, 0, 0, 0);

        } catch (Exception e) {

        }


        initializeUI();

        cd = new ConnectionDetector(this);

        // Build the Play services client for use by the Fused Location Provider and the Places API.
        // Use the addApi() method to request the Google Places API and the Fused Location Provider.
        mGoogleApiClient =
                new GoogleApiClient.Builder(this).enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                        .addConnectionCallbacks(this)
                        .addApi(LocationServices.API)
                        .addApi(Places.GEO_DATA_API)
                        .addApi(Places.PLACE_DETECTION_API)
                        .build();
        mGoogleApiClient.connect();

        setUpPushNotification();
    }

    private void setUpPushNotification() {

        Intent intent = getIntent();
        String uid = intent.getStringExtra(Constants.USER_ID);

        Log.d(TAG, "Device Id: " + uid);
        //
        // Core SDK must be initialized to interact with Bluemix Mobile services.
        BMSClient.getInstance().initialize(getApplicationContext(), BMSClient.REGION_UK);

        // In this code example, Analytics is configured to record lifecycle events.
        Analytics.init(getApplication(), getString(R.string.server_app_name), getString(R.string.analyticsApiKey), false, Analytics.DeviceEvent.LIFECYCLE);

        // Enable Logger (disabled by default), and set level to ERROR (DEBUG by default).
        Logger.storeLogs(true);
        Logger.setLogLevel(Logger.LEVEL.ERROR);


        // Actionable Notifications
        MFPPushNotificationOptions options = new MFPPushNotificationOptions();

        MFPPushNotificationButton firstButton = new MFPPushNotificationButton.Builder("Accept Button")
                .setIcon("check_circle_icon")
                .setLabel("Accept")
                .build();

        MFPPushNotificationButton secondButton = new MFPPushNotificationButton.Builder("Decline Button")
                .setIcon("extension_circle_icon")
                .setLabel("Decline")
                .build();

        MFPPushNotificationButton secondButton1 = new MFPPushNotificationButton.Builder("Decline Button2")
                .setIcon("extension_circle_icon")
                .setLabel("Decline2")
                .build();

        List<MFPPushNotificationButton> getButtons = new ArrayList<>();
        getButtons.add(firstButton);
        getButtons.add(secondButton);
        getButtons.add(secondButton1);

        List<MFPPushNotificationButton> getButtons1 = new ArrayList<>();
        getButtons1.add(firstButton);
        getButtons1.add(secondButton);

        List<MFPPushNotificationButton> getButtons2 = new ArrayList<>();
        getButtons2.add(firstButton);

        MFPPushNotificationCategory category = new MFPPushNotificationCategory.Builder("First_Button_Group1").setButtons(getButtons).build();
        MFPPushNotificationCategory category1 = new MFPPushNotificationCategory.Builder("First_Button_Group2").setButtons(getButtons1).build();
        MFPPushNotificationCategory category2 = new MFPPushNotificationCategory.Builder("First_Button_Group3").setButtons(getButtons2).build();

        List<MFPPushNotificationCategory> categoryList = new ArrayList<MFPPushNotificationCategory>();
        categoryList.add(category);
        categoryList.add(category1);
        categoryList.add(category2);

        options.setInteractiveNotificationCategories(categoryList);
        options.setDeviceid(uid);

        push = MFPPush.getInstance();
        push.initialize(getApplicationContext(), getString(R.string.pushAppGuid), getString(R.string.pushClientSecret), options);


        push.registerDevice(new MFPPushResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "Device is registered with Push Service.");
                Log.d(TAG, "Response: " + response);

                // Split response and convert to JSON object to display User ID confirmation from the backend.
                String[] resp = response.split("Text: ");
                String userId = "";
                try {
                    org.json.JSONObject responseJSON = new org.json.JSONObject(resp[1]);
                    userId = responseJSON.getString("userId");
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "Successfully registered for Bluemix Push Notifications with USER ID: " + userId);

                // displayTags();
            }

            @Override
            public void onFailure(MFPPushException ex) {
                Log.e(TAG, "Error registering with Push Service...\n" + ex.toString()
                        + "Push notifications will not be received.");
            }
        });

        //
        //
        //    /*
        //     * Initialize the Push Notifications client SDK with the App Guid and Client Secret from your Push Notifications service instance on Bluemix.
        //     * This enables authenticated interactions with your Push Notifications service instance.
        //     */
        //push = MFPPush.getInstance();
        //push.initialize(getApplicationContext(), getString(R.string.pushAppGuid), getString(R.string.pushClientSecret));
        //
        //    /*
        //     * Attempt to register your Android device with your Bluemix Push Notifications service instance.
        //     * Developers should put their user ID as the first argument.
        //     */
        //push.registerDeviceWithUserId(deviceId, new MFPPushResponseListener<String>() {
        //
        //  @Override
        //  public void onSuccess(String response) {
        //
        //    // Split response and convert to JSON object to display User ID confirmation from the backend.
        //    String[] resp = response.split("Text: ");
        //    String userId = "";
        //    try {
        //      org.json.JSONObject responseJSON = new org.json.JSONObject(resp[1]);
        //      userId = responseJSON.getString("userId");
        //    } catch (org.json.JSONException e) {
        //      e.printStackTrace();
        //    }
        //
        //    android.util.Log.i(TAG, "Successfully registered for Bluemix Push Notifications with USER ID: " + userId);
        //  }
        //
        //  @Override
        //  public void onFailure(MFPPushException ex) {
        //
        //    String errLog = "Error registering for Bluemix Push Notifications: ";
        //    String errMessage = ex.getErrorMessage();
        //    int statusCode = ex.getStatusCode();
        //
        //    Log.d(TAG, ex.toString());
        //   ex.printStackTrace();
        //
        //    // Create an error log based on the response code and returned error message.
        //    if (statusCode == 401) {
        //      errLog += "Cannot authenticate successfully with Bluemix Push Notifications service instance. Ensure your CLIENT SECRET is correct.";
        //    } else if (statusCode == 404 && errMessage.contains("Push GCM Configuration")) {
        //      errLog += "Your Bluemix Push Notifications service instance's GCM/FCM Configuration does not exist.\n" +
        //          "Ensure you have configured GCM/FCM Push credentials on your Bluemix Push Notifications dashboard correctly.";
        //    } else if (statusCode == 404) {
        //      errLog += "Cannot find Bluemix Push Notifications service instance, ensure your APP GUID is correct.";
        //    } else if (statusCode >= 500) {
        //      errLog += "Bluemix and/or the Bluemix Push Notifications service are having problems. Please try again later.";
        //    } else if (statusCode == 0) {
        //      errLog += "Request to Bluemix Push Notifications service instance timed out. Ensure your device is connected to the Internet.";
        //    }
        //    Log.e(TAG, "Error message: " + errMessage);
        //    android.util.Log.e(TAG, errLog);
        //  }
        //});
        //
        //
        //
        //


        // A notification listener is needed to handle any incoming push notifications within the Android application.
        notificationListener = new MFPPushNotificationListener() {

            @Override
            public void onReceive(final MFPSimplePushNotification message) {

                AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float percent = 0.7f;
                int seventyVolume = (int) (maxVolume*percent);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, seventyVolume, 0);

                ringtonePlayer = MediaPlayer.create(MapsActivity.this, R.raw.ringtone);
                ringtonePlayer.setLooping(true);
                ringtonePlayer.start();

                Log.i(TAG, "Received a push notification: " + message.toString());
                runOnUiThread(new Runnable() {
                    public void run() {
                        android.app.DialogFragment fragment = PushReceiverFragment.newInstance("Push notification received", message.getAlert());
                        fragment.show(getFragmentManager(), "dialog");
                    }
                });
            }
        };


        AppID.getInstance().initialize(this, getString(R.string.authTenantId), BMSClient.REGION_UK);
        BMSClient.getInstance().setAuthorizationManager(new AppIDAuthorizationManager(AppID.getInstance()));


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (cd != null) {
            if (!cd.isConnectedToInternet()) {
                UIUtil.showSimpleAlertDialog(this, getString(R.string.warning_dialog_title),
                        getString(R.string.internet_connection_error_message));
            }
        }

        // Enable the Push Notifications client SDK to listen for push notifications using the predefined notification listener.
        if (push != null) {
            push.listen(notificationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (push != null) {
            push.hold();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initializeUI() {

        markerView = LayoutInflater.from(this).inflate(R.layout.textview_marker, null);
        imageViewMarker = (ImageView) markerView.findViewById(R.id.imageViewMarker);
        textViewFree = (TextView) markerView.findViewById(R.id.textViewFree);
        imageViewMyLocation = (ImageView) findViewById(R.id.fabMyLocation);

        linearLayoutPlaceInfo = (LinearLayout) findViewById(R.id.linearLayoutPlaceInfo);
        textViewPlaceCount = (TextView) findViewById(R.id.textViewPlaceCount);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewPlaceAvailability = (TextView) findViewById(R.id.textViewPlaceAvailability);
        textViewPlaceType = (TextView) findViewById(R.id.textViewPlaceType);

        imageViewPlace = (ImageView) findViewById(R.id.imageViewPlace);

        buttonPlaceNavigation = (Button) findViewById(R.id.buttonPlaceNavigation);
        imageViewPlaceClose = (ImageView) findViewById(R.id.imageViewPlaceClose);

        imageViewMyLocation.setOnClickListener(this);

        buttonPlaceNavigation.setOnClickListener(this);
        imageViewPlaceClose.setOnClickListener(this);


        FontManager.setFontType(findViewById(R.id.layoutMain_Map), FontManager.getTypeface(this, FontManager.RALEWAY_MEDIUM));

    }

    private PermissionRequest initializePermissionRequest() {

        mLocationPermissionRequest = new PermissionRequest(this, Constants.LOCATION_ACCESS_PERMISSION_ID);

        mLocationPermissionRequest.permissions(accessLocation)
                .rationale(R.string.location_access_permission_rationale_message)
                .snackbar(findViewById(android.R.id.content))
                .callback(this);
        return mLocationPermissionRequest;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        loadMyLocation();

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    private void loadLocation(LatLng latLng) {

        if (latLng == null) {
           Log.d(TAG, "latlang null");
            return;
        }

        drawCircle(latLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, getZoomLevel(displayRadius)));
        selectedMarker = null;
        originPoint = Point.fromLngLat(latLng.longitude, latLng.latitude);
        getApiResponse(latLng);
    }

    public float getZoomLevel(int displayRadius) {
        switch (displayRadius) {
            case 300:
                return 15.7f;
            case 500:
                return 15.0f;
            case 800:
                return 14.3f;
            case 1000:
                return 14.0f;
            default:
                return 15.0f;
        }
    }


    private void drawCircle(LatLng latLng) {
        if (latLng == null) {
            return;
        }
        // Add a circle location
        if (circle == null) {
            circle = mMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .radius(displayRadius)
                    .strokeColor(Color.RED)
                    .strokeWidth(4f)
                    .fillColor(Color.argb(255 / 10, 255, 0, 0)));
        } else {
            circle.setCenter(latLng);
        }

    }


    private void getApiResponse(LatLng currtentLatLang) {

        if (parkingMarkerList == null) {
            parkingMarkerList = new ArrayList<>();
        }
        if (parkingMarkerList.size() > 0) {
            clearPreviousMarker(parkingMarkerList);
            parkingMarkerList.clear();
        }

        mApiLatLng = currtentLatLang;
        getGoogleResponse(currtentLatLang);
        getIbmResponse(currtentLatLang);

        displaySpecialPromotionMarker();
    }


    private void getGoogleResponse(LatLng latLng) {

        if (latLng == null) {
            Log.d(TAG, "latlang null");
            return;
        }


        if (apiServiceGoogle == null) {
            apiServiceGoogle = ApiClient.getClient(Constants.RETROFIT_TYPE_GOOGLE).create(ApiInterface.class);
        }

        //String apiKey = getString(R.string.google_maps_key);
        String apiKey = "AIzaSyDGPf4HZ5tBC9xJPbsGI475d7aswEF2ITA";
        Call<GoogleResponse> call = apiServiceGoogle.getParkingPlaceFromGoogle(
                Util.getLocationAsString(latLng), displayRadius, "parking", apiKey);
        call.enqueue(new Callback<GoogleResponse>() {
            @Override
            public void onResponse(Call<GoogleResponse> call, Response<GoogleResponse> response) {
                int statusCode = response.code();
                Log.d(TAG, "status Code: " + statusCode);

                GoogleResponse googleResponse = response.body();

                if (googleResponse != null) {

                    Log.d(TAG, "Response: " + googleResponse.toString());

                    List<GoogleResponseLocation> locationList = googleResponse.getResultList();

                    if (locationList != null) {
                        for (GoogleResponseLocation location : locationList) {
                            Log.d(TAG, "Place: ");
                            Log.d(TAG, location.toString());
                        }

                        addAnnotationForGoogle(locationList);
                    }

                } else {
                    Log.d(TAG, "Response is null");
                }

            }

            @Override
            public void onFailure(Call<GoogleResponse> call, Throwable t) {

            }
        });

    }


    private void getIbmResponse(LatLng latLng) {

        if (latLng == null) {
            Log.d(TAG, "latlang null");
            return;
        }

        if (apiServiceIbm == null) {
            apiServiceIbm = ApiClient.getClient(Constants.RETROFIT_TYPE_IBM).create(ApiInterface.class);
        }

        String authToken = Credentials.basic("imentstralliatherederess", "9dea22ce6e159f2f7c9f709e7359660117006e8c");
        Log.d(TAG, authToken);

        Call<IbmResponse> call = apiServiceIbm.getParkingPlaceFromIbm(authToken, latLng.latitude, latLng.longitude, displayRadius, "contains", true);

        call.enqueue(new Callback<IbmResponse>() {
            @Override
            public void onResponse(Call<IbmResponse> call, Response<IbmResponse> response) {
                int statusCode = response.code();
                Log.d(TAG, "status Code: " + statusCode);

                IbmResponse resultResponse = response.body();

                if (resultResponse != null) {

                    // Log.d(TAG, "IBM Response" + resultResponse.toString());

                    List<IbmResponseLocation> resultList = resultResponse.getResultList();
                    if (resultList != null) {

                        for (IbmResponseLocation ibmResponseLocation : resultList) {
                            Log.d(TAG, "Place: ");
                            Log.d(TAG, ibmResponseLocation.toString());
                            Log.d(TAG, " ------- ");
                        }

                        ibmLocationList = getParkingLocationGroupByStreet(resultList);

                        Log.d(TAG, "Total " + ibmLocationList.size() + " location area found");
                        addAnnotationForIbm(ibmLocationList);

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

    private List<IBMParkingLocationStreet> getParkingLocationGroupByStreet(List<IbmResponseLocation> resultList) {

        ibmLocationList = new ArrayList<>();
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

        return ibmLocationList;
    }


    private void addAnnotationForGoogle(List<GoogleResponseLocation> locationList) {

        //mMap.clear();

        imageViewMarker.setImageResource(R.drawable.ic_pin_map);
        int color = ContextCompat.getColor(this, R.color.blue_marker);
        imageViewMarker.setColorFilter(color);


        textViewFree.setText("");

        if (markerView.getParent() != null) {
            ViewParent viewParent = markerView.getParent();
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).removeAllViews();
            }
        }

        IconGenerator iconGenerator = new IconGenerator(this);
        iconGenerator.setContentView(markerView);
        iconGenerator.setBackground(null);

        Bitmap markerIconBitmap = iconGenerator.makeIcon();

        for (GoogleResponseLocation location : locationList) {

            if (location.getGeometry() != null) {
                ResponseLocation resLocation = location.getGeometry().getLocation();
                if (resLocation != null) {

                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(resLocation.getLatitude(), resLocation.getLongitude()))
                            .title(location.getName())
                            .snippet(location.getVicinity())
                            .icon(BitmapDescriptorFactory.fromBitmap(markerIconBitmap))
                    );

                    marker.setTag(location);

                    parkingMarkerList.add(marker);
                }
            }

        }
    }

    private void addAnnotationForPromotion(List<PromotionLocation> locationList) {

        //mMap.clear();
        // int color = ContextCompat.getColor(this,R.color.red_marker);
        imageViewMarker.clearColorFilter();
        imageViewMarker.setImageResource(R.drawable.ic_special_promotion);


        textViewFree.setText("");

        if (markerView.getParent() != null) {
            ViewParent viewParent = markerView.getParent();
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).removeAllViews();
            }
        }

        IconGenerator iconGenerator = new IconGenerator(this);
        iconGenerator.setContentView(markerView);
        iconGenerator.setBackground(null);

        Bitmap markerIconBitmap = iconGenerator.makeIcon();

        for (PromotionLocation location : locationList) {

            if (location.getGeometry() != null) {
                LatLng latLng = location.getGeometry().getLatLng();
                if (latLng != null) {

                    String title = "";
                    String description = "";

                    if (location.getPromotionDoc() != null) {
                        title = location.getPromotionDoc().getTitle();
                        description = location.getPromotionDoc().getDescription();
                    }

                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(title)
                            .snippet(description)
                            .icon(BitmapDescriptorFactory.fromBitmap(markerIconBitmap))
                    );

                    marker.setTag(location);

                    parkingMarkerList.add(marker);
                }
            }

        }
    }

    private void addAnnotationForIbm(List<IBMParkingLocationStreet> locationList) {

        for (IBMParkingLocationStreet parkingLocation : locationList) {
            if (parkingLocation != null) {

//                int displayParkingId = selectedParking;
                IbmParkingLocationClass locationClass;

                // public
                locationClass = parkingLocation.getPublicType();
                addAnnotationForIbmLocationClass(locationClass, publicIcons);


                //disabled
//                if (displayParkingId == Constants.PARKING_TYPE_DISABLED) {
                locationClass = parkingLocation.getDisabledType();
                addAnnotationForIbmLocationClass(locationClass, disabledIcons);
//                }

                // taxi
//                if (displayParkingId == Constants.PARKING_TYPE_TAXI) {
                locationClass = parkingLocation.getTaxiType();
                addAnnotationForIbmLocationClass(locationClass, taxiIcons);
//                }


                // truck
//                if (displayParkingId == Constants.PARKING_TYPE_TRUCK) {
                locationClass = parkingLocation.getTruckType();
                addAnnotationForIbmLocationClass(locationClass, truckIcons);
//                }

                // pickup
//                if (displayParkingId == Constants.PARKING_TYPE_PICKUP) {
                locationClass = parkingLocation.getPickupType();
                addAnnotationForIbmLocationClass(locationClass, pickupIcons);
//                }


                // touringBus
//                if (displayParkingId == Constants.PARKING_TYPE_TOURIST) {
                locationClass = parkingLocation.getTouringBusType();
                addAnnotationForIbmLocationClass(locationClass, touristIcons);
//                }


                locationClass = parkingLocation.getValetParkingType();
                addAnnotationForIbmLocationClass(locationClass, valetIcons);

                locationClass = parkingLocation.getChargingStationType();
                addAnnotationForIbmLocationClass(locationClass, chargingIcons);

            }
        }

    }

    private void addAnnotationForIbmLocationClass(IbmParkingLocationClass ibmParkingLocationClass, int[] markerIcons) {


        List<IbmResponseLocation> locationList = ibmParkingLocationClass.getLocationList();

        LatLng latLng = null;

        if (locationList.size() > 0) {
            IbmResponseLocation location = locationList.get(locationList.size() - 1);
            IbmGeometry geometry = location.getGeometry();
            latLng = geometry.getLatLng();

            if (location.getDoc() != null) {
                Metadata metadata = location.getDoc().getMetadata();
                if (metadata != null) {
                    if (metadata.getStreet() != null) {
                        ibmParkingLocationClass.setTitle(metadata.getStreet());
                    }
                    if (metadata.getCity() != null) {
                        ibmParkingLocationClass.setSubTitle(metadata.getCity());
                    }

                }
            }

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

            ibmParkingLocationClass.setnBusy(busy);
            ibmParkingLocationClass.setnFree(total - busy);

            String freeSpaceText = "Medium Availability";
            int freeSpacePercentage = (100 * ibmParkingLocationClass.getnFree()) / total;
            int color = ContextCompat.getColor(this, R.color.yellow_marker);
            float colorB = BitmapDescriptorFactory.HUE_YELLOW;
            int iconResource = markerIcons[0];

            if (freeSpacePercentage <= 20) {
                color = ContextCompat.getColor(this, R.color.red_marker);
                colorB = BitmapDescriptorFactory.HUE_RED;
                iconResource = markerIcons[1];
                freeSpaceText = "Low Availability";
            } else if (freeSpacePercentage >= 60) {
                color = ContextCompat.getColor(this, R.color.green_marker);
                colorB = BitmapDescriptorFactory.HUE_GREEN;
                iconResource = markerIcons[2];
                freeSpaceText = "High Availability";
            }


            textViewFree.setText(String.valueOf(ibmParkingLocationClass.getnFree()));
            imageViewMarker.setImageResource(iconResource);
            textViewFree.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            imageViewMarker.clearColorFilter();
            // check its for public
            if (iconResource == R.drawable.ic_pin_map) {
                imageViewMarker.setColorFilter(color);
                textViewFree.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            }

            if (markerView.getParent() != null) {
                ViewParent viewParent = markerView.getParent();
                if (viewParent instanceof ViewGroup) {
                    ((ViewGroup) viewParent).removeAllViews();
                }
            }


            IconGenerator iconGenerator = new IconGenerator(this);
            iconGenerator.setBackground(null);
            iconGenerator.setContentView(markerView);


            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(ibmParkingLocationClass.getTitle())
                    .snippet(freeSpaceText)
                    .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()))

            );

            marker.setTag(ibmParkingLocationClass);
            parkingMarkerList.add(marker);

        }


    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void clearPreviousMarker(List<Marker> markerList) {

        if (markerList != null) {
            for (Marker marker : markerList) {
                marker.remove();
            }
        }

    }

    private void clearPreviousIbmMarker(List<Marker> markerList) {

        if (markerList != null) {
            List<Marker> unUsedMarker = new ArrayList<>();
            for (Marker marker : markerList) {
                Object obj = marker.getTag();
                if (obj != null && obj instanceof IbmParkingLocationClass) {
                    marker.remove();
                    unUsedMarker.add(marker);
                }

            }
            markerList.removeAll(unUsedMarker);
        }

    }


    private void onSignOutClick() {
        if (isLoggedIn()) {
            storeLogoutStatus();
            push.unregister(new MFPPushResponseListener<String>() {

                @Override
                public void onSuccess(String s) {
                    // Handle success
                    goToLogin();
                }

                @Override
                public void onFailure(MFPPushException e) {
                    // Handle Failure
                    Toast.makeText(MapsActivity.this, "Can not unregister from notification! Try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

//    private void onLoginClick() {
//        if (!isLoggedIn()) {
//            goToLogin();
//        } else {
//            UIUtil.showSimpleAlertDialog(this, "Alert !", "You have already logged in");
//        }
//    }

    private void onHelpClick() {
        MediaPlayer.create(MapsActivity.this, R.raw.ringtone).start();
//        String url = "https://www.yazamtec.com/parking-finder.html";
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse(url));
//        try {
//            startActivity(intent);
//        } catch (Exception e) {
//
//        }
    }

    private void onShareClick() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "https://www.yazamtec.com/parking-finder.html");
        startActivity(intent);
    }

    private void onNewOptionClick() {

        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);

    }

    private void onRegisterClick() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void onReferenceClick() {
        Intent intent = new Intent(this, PreferenceActivity.class);
        startActivity(intent);
    }

    private void onAboutClick() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void onSpecialPromotionClick() {

        displaySpecialPromotionMarker();
    }

//    private void onShowListClick() {
//
//        int visibility = linearLayoutTransportType.getVisibility();
//
//        if (visibility == View.INVISIBLE) {
//
//            linearLayoutTransportType.setVisibility(View.VISIBLE);
//        } else {
//
//            linearLayoutTransportType.setVisibility(View.INVISIBLE);
//        }
//    }

//    private void onSelectTransportType(int type) {
//        Log.d(TAG, "type: " + type);
//        linearLayoutTransportType.setVisibility(View.INVISIBLE);
//        selectedParking = type;
//        clearPreviousIbmMarker(parkingMarkerList);
//        addAnnotationForIbm(ibmLocationList);
//    }

    private void storeLogoutStatus() {

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.TAG_LOGIN_STATUS, false);

        editor.apply();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isLoggedIn() {

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefFileName, Context.MODE_PRIVATE);
        boolean status = sharedPreferences.getBoolean(Constants.TAG_LOGIN_STATUS, false);
        return status;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_show_list) {

//            onShowListClick();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_help) {
            onHelpClick();
        } else if (id == R.id.nav_share) {
            onShareClick();
        } else if (id == R.id.nav_signout) {
            onSignOutClick();
        }
        //else if(id == R.id.nav_special_promotion){
        //
        //  onSpecialPromotionClick();
        //
        //}
        else if (id == R.id.nav_new) {
            onNewOptionClick();
        } else if (id == R.id.nav_register) {
            onRegisterClick();
        } else if (id == R.id.nav_about) {
            onAboutClick();
        } else if (id == R.id.nav_preference) {
            onReferenceClick();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /*
     *  Location access callbacks
     */

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (linearLayoutPlaceInfo != null) {
            linearLayoutPlaceInfo.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
        Log.d(TAG, "Play services connection suspended");
    }

    /**
     * Handles failure to connect to the Google Play services client.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
        // Refer to the reference doc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    private boolean isGPSEnabled() {
        //check location service is enable
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void loadMyLocation() {

        Log.d(TAG, "Loading my location");
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        if (!isGPSEnabled()) {
            UIUtil.showSimpleAlertDialog(this, getString(R.string.warning_dialog_title),
                    "Please enable GPS");
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            if (mLocationPermissionRequest == null) {
                mLocationPermissionRequest = initializePermissionRequest();
            }
            mLocationPermissionRequest.submit();
            Log.d(TAG, "Location permission is not granted");
            return;
        }

        mMap.setMyLocationEnabled(true);

        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastKnownLocation != null) {
            LatLng currtentLatLang = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
            Log.d(TAG, "last location: " + mLastKnownLocation.getLatitude() + ", " + mLastKnownLocation.getLongitude());
            drawCircle(currtentLatLang);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currtentLatLang, getZoomLevel(displayRadius)));
            selectedMarker = null;

            originPoint = Point.fromLngLat(currtentLatLang.longitude, currtentLatLang.latitude);
            getApiResponse(currtentLatLang);


        } else {
            Log.d(TAG, "Location is null");
            // Toast.makeText(this, "Can't get your location ", Toast.LENGTH_LONG).show();
        }
    }


    private void loadPlaceInformationToLayout(Marker marker) {
        if (marker == null) {
            return;
        }

        Object obj = marker.getTag();
        if (obj != null) {
            if (obj instanceof GoogleResponseLocation) {

                // load image for google api
                loadStreetViewImage(marker);
                GoogleResponseLocation googleLocation = (GoogleResponseLocation) obj;

                Opening opening = googleLocation.getOpening();

                textViewPlaceCount.setText(googleLocation.getName());
                textViewAddress.setVisibility(View.VISIBLE);
                textViewAddress.setText(googleLocation.getVicinity());

                if (opening != null) {
                    textViewPlaceAvailability.setText(opening.isOpenNow() ? "Status: Open" : "Status: Close");
                } else {
                    textViewPlaceAvailability.setText("");
                }

                textViewPlaceType.setText("Type: Public Parking");

            } else if (obj instanceof IbmParkingLocationClass) {
                IbmParkingLocationClass ibmParkingLocationClass = (IbmParkingLocationClass) obj;

                textViewPlaceCount.setText(new StringBuffer("Free Parking Places: ").append(ibmParkingLocationClass.getnFree()));
                textViewAddress.setVisibility(View.GONE);

                int total = ibmParkingLocationClass.getnBusy() + ibmParkingLocationClass.getnFree();

                int freeSpacePercentage = 0;
                if (total > 0)
                    freeSpacePercentage = (100 * ibmParkingLocationClass.getnFree()) / total;
                textViewPlaceAvailability.setText(new StringBuilder("Availability: ").append(freeSpacePercentage).append("%"));

                switch (ibmParkingLocationClass.getLocationClass()) {
                    case Constants.LOCATION_CLASS_DISABLED:
                        textViewPlaceType.setText("Type: People With Special Needs Parking");
                        imageViewPlace.setImageResource(R.drawable.an_disabled);
                        break;
                    case Constants.LOCATION_CLASS_TRUCK:
                        textViewPlaceType.setText("Type: Truck Parking");
                        imageViewPlace.setImageResource(R.drawable.an_truck);
                        break;
                    case Constants.LOCATION_CLASS_PICKUP:
                        textViewPlaceType.setText("Type: Pickup And Delivery Parking");
                        imageViewPlace.setImageResource(R.drawable.an_pickup);
                        break;
                    case Constants.LOCATION_CLASS_TOURING_BUS:
                        textViewPlaceType.setText("Type: Tourist Bus Parking");
                        imageViewPlace.setImageResource(R.drawable.an_tourist_bus);
                        break;
                    case Constants.LOCATION_CLASS_TAXI:
                        textViewPlaceType.setText("Type: Taxi Parking");
                        imageViewPlace.setImageResource(R.drawable.an_taxi);
                        break;
                    case Constants.LOCATION_CLASS_VALET_PARKING:
                        textViewPlaceType.setText("Type: Valet Parking");
                        imageViewPlace.setImageResource(R.drawable.an_valet);
                        break;
                    case Constants.LOCATION_CLASS_CHARGING_STATION:
                        textViewPlaceType.setText("Type: Electric Charging Location");
                        imageViewPlace.setImageResource(R.drawable.an_charging);
                        break;
                    case Constants.LOCATION_CLASS_PUBLIC:
                        textViewPlaceType.setText("Type: Public Street Parking");
                        imageViewPlace.setImageResource(R.drawable.street_view_placeholder);
                        break;
                    default:
                        textViewPlaceType.setText("Type: Public Street Parking");
                        imageViewPlace.setImageResource(R.drawable.street_view_placeholder);
                        break;
                }


            } else if (obj instanceof PromotionLocation) {

                PromotionLocation promotionLocation = (PromotionLocation) obj;
                loadStreetViewImage(marker);

                textViewPlaceCount.setText(promotionLocation.getPromotionDoc().getTitle());
                textViewAddress.setVisibility(View.GONE);
                textViewPlaceAvailability.setText(promotionLocation.getPromotionDoc().getDescription());
                textViewPlaceType.setText("");

            }
        }


    }


    private void loadStreetViewImage(Marker marker) {
        if (marker == null) {
            return;
        }

        LatLng latLng = marker.getPosition();


        String url = null;

        Object obj = marker.getTag();
        if (obj != null) {
            if (obj instanceof GoogleResponseLocation) {
                String apiKey = getString(R.string.google_maps_key);
                url = "https://maps.googleapis.com/maps/api/streetview?size=600x300&location=" +
                        Util.getLocationAsString(latLng) + "&heading=151.78&pitch=-0.76&key=" +
                        apiKey;

                Glide.with(imageViewPlace.getContext()).asBitmap().load(url).into(imageViewPlace);


            } else if (obj instanceof PromotionLocation) {
                PromotionLocation location = (PromotionLocation) obj;
                String id = location.getId();
                url = "https://b18e8d85-eafc-4e77-90fa-5fda27fc9e64-bluemix.cloudant.com/parking_promotions/"
                        + id + "/images.png";

                String credentials = Constants.PROMOTION_USERNAME + ":" + Constants.PROMOTION_PASSWORD;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                Log.d(TAG, "url: " + url);
                Log.d(TAG, "auth: " + auth);

                GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", auth)
                        .build());

                Glide.with(imageViewPlace.getContext()).asBitmap().load(glideUrl).into(imageViewPlace);

            }

        }

    }


    private void navigateToPlace() {
        if (selectedMarker != null) {

            Object obj = selectedMarker.getTag();
            if (obj != null) {
                if (obj instanceof GoogleResponseLocation) {
                    GoogleResponseLocation googleLocation = (GoogleResponseLocation) obj;

                    if (googleLocation.getGeometry() != null) {
                        ResponseLocation location = googleLocation.getGeometry().getLocation();
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        destinationParkingClassType = "google";
                        startNavigation(latLng);
                    }

                } else if (obj instanceof IbmParkingLocationClass) {

                    IbmParkingLocationClass ibmParkingLocationClass = (IbmParkingLocationClass) obj;
                    if (ibmParkingLocationClass.getLocationList() != null && ibmParkingLocationClass.getLocationList().size() > 0) {
                        IbmResponseLocation location = ibmParkingLocationClass.getLocationList().get(0);
                        if (location.getGeometry() != null) {
                            IbmGeometry geometry = location.getGeometry();
                            LatLng latLng = geometry.getLatLng();
                            if (latLng != null) {
                                destinationParkingClassType = ibmParkingLocationClass.getLocationClass();
                                startNavigation(latLng);
                            }
                        }
                    }

                } else if (obj instanceof PromotionLocation) {

                    PromotionLocation location = (PromotionLocation) obj;
                    if (location.getGeometry() != null) {
                        IbmGeometry geometry = location.getGeometry();
                        LatLng latLng = geometry.getLatLng();
                        if (latLng != null) {
                            destinationParkingClassType = "promotion";
                            startNavigation(latLng);
                        }
                    }

                }
            }

        } else {
            Log.d(TAG, "Selected marker is null");
        }
    }

    private void startNavigation(LatLng latLng) {
        if (latLng == null) {
            return;
        }
        Log.d(TAG, "Starting navigation");

        destinationPoint = Point.fromLngLat(latLng.longitude, latLng.latitude);
        // Pass in your Amazon Polly pool id for speech synthesis using Amazon Polly
        // Set to null to use the default Android speech synthesizer
        NavigationViewOptions options = NavigationViewOptions.builder()
                .origin(originPoint)
                .destination(destinationPoint)
                .awsPoolId(null)
                .shouldSimulateRoute(false)
                .build();

        // Call this method with Context from within an Activity
        MyNavigationLauncher.startNavigation(MapsActivity.this, options);

    }


    private void displaySpecialPromotionMarker() {

        if (mApiLatLng != null) {


            clearPreviousPromotionMarker(parkingMarkerList);

            String url = "https://b18e8d85-eafc-4e77-90fa-5fda27fc9e64-bluemix.cloudant.com/parking_promotions/_design/promotionsindex/_geo/newGeoIndex?lat=" + mApiLatLng.latitude + "&lon=" + mApiLatLng.longitude +
                    "&radius=" + String.valueOf(displayRadius) + "&limit=20&relation=contains&include_docs=true";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d(TAG, "Response: " + response.toString());
                            PromotionResponse promotionResponse = parsePromotionResponse(response);
                            Log.d(TAG, promotionResponse.toString());

                            addAnnotationForPromotion(promotionResponse.getLocationList());

                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = "disistedideareppelyinere:38d2cb503cb5f4f7584c6c6281a20bcf6f221ec1";
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }
            };

            Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);


        }

    }

    private void clearPreviousPromotionMarker(List<Marker> markerList) {
        if (markerList != null) {
            List<Marker> unUsedMarker = new ArrayList<>();
            for (Marker marker : markerList) {
                Object obj = marker.getTag();
                if (obj != null) {
                    if (obj instanceof PromotionLocation) {
                        marker.remove();
                        unUsedMarker.add(marker);
                    }
                }
            }

            markerList.removeAll(unUsedMarker);
        }
    }


    private PromotionResponse parsePromotionResponse(JSONObject jsonObject) {

        List<PromotionLocation> locationList = new ArrayList<>();

        if (jsonObject != null) {

            if (jsonObject.has(Constants.NODE_ROWS)) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray(Constants.NODE_ROWS);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        PromotionLocation promotionLocation = new PromotionLocation();
                        JSONObject jsonLocation = jsonArray.getJSONObject(i);

                        //id
                        if (jsonLocation.has(Constants.NODE_ID)) {
                            promotionLocation.setId(jsonLocation.getString(Constants.NODE_ID));
                        }

                        // Geometry
                        if (jsonLocation.has(Constants.NODE_GEOMETRY)) {
                            JSONObject jsonGeometry = jsonLocation.getJSONObject(Constants.NODE_GEOMETRY);
                            IbmGeometry geometry = new IbmGeometry();
                            if (jsonGeometry.has(Constants.NODE_TYPE)) {
                                geometry.setType(jsonGeometry.getString(Constants.NODE_TYPE));
                            }

                            if (jsonGeometry.has(Constants.NODE_COORDINATES)) {

                                JSONArray jsonCoordinates = jsonGeometry.getJSONArray(Constants.NODE_COORDINATES);
                                if (jsonCoordinates.length() >= 2) {
                                    List<Double> coordinateList = new ArrayList<>();
                                    coordinateList.add(jsonCoordinates.getDouble(0));
                                    coordinateList.add(jsonCoordinates.getDouble(1));

                                    geometry.setCoordinates(coordinateList);
                                }

                            }

                            promotionLocation.setGeometry(geometry);
                        }

                        // doc..
                        if (jsonLocation.has(Constants.NODE_DOC)) {
                            JSONObject jsonDoc = jsonLocation.getJSONObject(Constants.NODE_DOC);
                            PromotionDoc promotionDoc = new PromotionDoc();
                            if (jsonDoc.has(Constants.NODE__ID)) {
                                promotionDoc.setId(jsonDoc.getString(Constants.NODE__ID));
                            }

                            if (jsonDoc.has(Constants.NODE_START_DATE)) {
                                promotionDoc.setStartDate(jsonDoc.getString(Constants.NODE_START_DATE));
                            }

                            if (jsonDoc.has(Constants.NODE_END_DATE)) {
                                promotionDoc.setEndDate(Constants.NODE_END_DATE);
                            }

                            if (jsonDoc.has(Constants.NODE_TITLE)) {
                                promotionDoc.setTitle(jsonDoc.getString(Constants.NODE_TITLE));
                            }

                            if (jsonDoc.has(Constants.NODE_DESCRIPTION)) {
                                promotionDoc.setDescription(jsonDoc.getString(Constants.NODE_DESCRIPTION));
                            }

                            promotionLocation.setPromotionDoc(promotionDoc);
                        }


                        // attachment
                        if (jsonLocation.has(Constants.NODE_ATTACHMENT)) {
                            JSONObject jsonAttachment = jsonLocation.getJSONObject(Constants.NODE_ATTACHMENT);

                            if (jsonAttachment.has(Constants.NODE_IMAGES_PNG)) {
                                JSONObject jsonImagesPng = jsonAttachment.getJSONObject(Constants.NODE_IMAGES_PNG);

                                ImagesPng imagesPng = new ImagesPng();

                                if (jsonImagesPng.has(Constants.NODE_CONTENT_TYPE)) {
                                    imagesPng.setContentType(jsonImagesPng.getString(Constants.NODE_CONTENT_TYPE));
                                }

                                if (jsonImagesPng.has(Constants.NODE_REV_POS)) {
                                    imagesPng.setRevPos(jsonImagesPng.getInt(Constants.NODE_REV_POS));
                                }

                                if (jsonImagesPng.has(Constants.NODE_DIGEST)) {
                                    imagesPng.setDigest(jsonImagesPng.getString(Constants.NODE_DIGEST));
                                }

                                if (jsonImagesPng.has(Constants.NODE_LENGTH)) {
                                    imagesPng.setLength(jsonImagesPng.getInt(Constants.NODE_LENGTH));
                                }

                                if (jsonImagesPng.has(Constants.NODE_STUB)) {
                                    imagesPng.setStub(jsonImagesPng.getBoolean(Constants.NODE_STUB));
                                }

                                Attachment attachment = new Attachment(imagesPng);
                                promotionLocation.setAttachment(attachment);
                            }
                        }

                        locationList.add(promotionLocation);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        PromotionResponse promotionResponse = new PromotionResponse(locationList);

        return promotionResponse;

    }


    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {
        Log.d(TAG, "Place Selected: " + place.getName());

        loadLocation(place.getLatLng());
    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        //  Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
        //     Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabMyLocation:
                loadMyLocation();
                break;

//            case R.id.layoutTypeDisabled:
//                onSelectTransportType(Constants.PARKING_TYPE_DISABLED);
//                break;
//
//            case R.id.layoutTypeTaxi:
//                onSelectTransportType(Constants.PARKING_TYPE_TAXI);
//                break;
//
//            case R.id.layoutTypeTruck:
//                onSelectTransportType(Constants.PARKING_TYPE_TRUCK);
//                break;
//
//            case R.id.layoutTypePickup:
//                onSelectTransportType(Constants.PARKING_TYPE_PICKUP);
//                break;
//
//            case R.id.layoutTypeTourist:
//                onSelectTransportType(Constants.PARKING_TYPE_TOURIST);
//                break;

            case R.id.buttonPlaceNavigation:
                Log.d(TAG, "navigate place");
                navigateToPlace();
                linearLayoutPlaceInfo.setVisibility(View.INVISIBLE);
                selectedMarker = null;
                break;

            case R.id.imageViewPlaceClose:
                Log.d(TAG, "close layout");
                linearLayoutPlaceInfo.setVisibility(View.INVISIBLE);
                selectedMarker = null;
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        Log.d(TAG, Arrays.toString(grantResults));
        switch (requestCode) {
            case Constants.LOCATION_ACCESS_PERMISSION_ID:
                if (mLocationPermissionRequest != null) {
                    mLocationPermissionRequest.onRequestPermissionsResult(requestCode, permissions,
                            grantResults);
                }
                return;
            default:
                return;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, String[] permissions) {
        Log.d(TAG, "All permission granted");

        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        //    != PackageManager.PERMISSION_GRANTED
        //    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        //    != PackageManager.PERMISSION_GRANTED) {
        //  // TODO: Consider calling
        //  //    ActivityCompat#requestPermissions
        //  // here to request the missing permissions, and then overriding
        //  //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //  //                                          int[] grantResults)
        //  // to handle the case where the user grants the permission. See the documentation
        //  // for ActivityCompat#requestPermissions for more details.
        //  return;
        //}

        loadMyLocation();

    }

    @Override
    public void onPermissionsDenied(int requestCode, String[] permissions) {
        Log.d(TAG, "onPermission Denied");
    }

    /**
     * When Marker info window is clicked
     *
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {

        if (marker != null) {
            linearLayoutPlaceInfo.setVisibility(View.VISIBLE);
            loadPlaceInformationToLayout(marker);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarker Click");

        selectedMarker = marker;
        linearLayoutPlaceInfo.setVisibility(View.INVISIBLE);
        return false;
    }


}

