package com.emraz.parkinghero.util;

import com.emraz.parkinghero.BuildConfig;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;

/**
 * Created by Yousuf on 7/5/2017.
 */

public class Constants {

  public static final float DEFAULT_ZOOM = 15f;
  public static final String USER_ID = "userID";

  public static String TAG_LOGIN_STATUS = "loginStatus";
  public static String TAG_DISPLAY_RADIUS = "displayRadius";

  public static String sharedPrefFileName = "PH_SP";

  public final static int LOCATION_ACCESS_PERMISSION_ID = 100;

  public static float DEFAULT_API_LOC_LAT = 52.077064f;
  public static float DEFAULT_API_LOC_LNG = 4.344115f;

  public static String GOOGLE_API_BASE_URL = BuildConfig.GOOGLE_API_BASE_URL;
  public static String IBM_API_BASE_URL = BuildConfig.IBM_API_BASE_URL;

  public final static int RETROFIT_TYPE_GOOGLE = 1;
  public final static int RETROFIT_TYPE_IBM = 2;

  public static String SENSOR_TYPE = "Parking_Sensor";

  public static final String LOCATION_CLASS_PUBLIC = "public";
  public static final String LOCATION_CLASS_TAXI = "taxi";
  public static final String LOCATION_CLASS_TOURING_BUS = "touringbus";
  public static final String LOCATION_CLASS_PICKUP = "pickup";
  public static final String LOCATION_CLASS_TRUCK = "truck";
  public static final String LOCATION_CLASS_DISABLED = "disabled";
  public static final String LOCATION_CLASS_VALET_PARKING="valet";
  public static final String LOCATION_CLASS_CHARGING_STATION="electric";


  public static String STATUS_BUSY = "Busy";
  public static String STATUS_FREE = "Free";

  public static int PARKING_TYPE_PUBLIC = -1;
  public static int PARKING_TYPE_DISABLED = 0;
  public static int PARKING_TYPE_TAXI = 1;
  public static int PARKING_TYPE_TRUCK = 2;
  public static int PARKING_TYPE_PICKUP = 3;
  public static int PARKING_TYPE_TOURIST = 4;
  public static int PARKING_TYPE_VALET = 5;
  public static int PARKING_TYPE_CHARGING = 6;

  // news response nodes
  public static final String NODE_ROWS = "rows";
  public static final String NODE_DOC = "doc";
  public static final String NODE_TITLE = "title";
  public static final String NODE_DATE = "date";
  public static final String NODE_DESCRIPTION = "description";
  public static final String NODE_GEOMETRY = "geometry";
  public static final String NODE_TYPE = "type";
  public static final String NODE_COORDINATES = "coordinates";
  public static final String NODE_ID = "id";
  public static final String NODE__ID = "_id";
  public static final String NODE_START_DATE = "start_date";
  public static final String NODE_END_DATE = "end_date";
  public static final String NODE_ATTACHMENT = "_attachments";
  public static final String NODE_IMAGES_PNG = "images.png";
  public static final String NODE_CONTENT_TYPE = "content_type";
  public static final String NODE_REV_POS = "revpos";
  public static final String NODE_DIGEST = "digest";
  public static final String NODE_LENGTH = "length";
  public static final String NODE_STUB = "stub";

  public static String PROMOTION_USERNAME = "disistedideareppelyinere";
  public static String PROMOTION_PASSWORD = "38d2cb503cb5f4f7584c6c6281a20bcf6f221ec1";



}
