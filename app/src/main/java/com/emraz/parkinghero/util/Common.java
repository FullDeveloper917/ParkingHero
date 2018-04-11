package com.emraz.parkinghero.util;

import android.media.MediaPlayer;

import com.google.android.gms.common.api.GoogleApiClient;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;
import com.mapbox.geojson.Point;

/**
 * Created by david on 23/03/2018.
 */

public class Common {

    public static MFPPush push;
    public static int displayRadius;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    public static GoogleApiClient mGoogleApiClient;

    public static Point destinationPoint;
    public static Point originPoint;
    public static String destinationParkingClassType;

    public static MediaPlayer ringtonePlayer;
}
