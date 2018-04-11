package com.emraz.parkinghero.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.emraz.parkinghero.R;
import com.emraz.parkinghero.util.ConnectionDetector;
import com.emraz.parkinghero.util.Constants;
import com.emraz.parkinghero.util.FontManager;
import com.emraz.parkinghero.util.Log;
import com.emraz.parkinghero.util.UIUtil;


import com.ibm.bluemix.appid.android.api.AppID;
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager;
import com.ibm.bluemix.appid.android.api.AuthorizationException;
import com.ibm.bluemix.appid.android.api.AuthorizationListener;
import com.ibm.bluemix.appid.android.api.LoginWidget;
import com.ibm.bluemix.appid.android.api.tokens.AccessToken;
import com.ibm.bluemix.appid.android.api.tokens.IdentityToken;


import com.ibm.bluemix.appid.android.api.tokens.RefreshToken;
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

import java.util.ArrayList;
import java.util.List;

//import static com.squareup.okhttp.internal.Internal.logger;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private final static String region = AppID.REGION_UK;
    private BMSClient bmsClient;
    private AppID appId;
    // private AppIDAuthorizationManager appIDAuthorizationManager;
    LoginWidget loginWidget;
    AuthorizationListener loginAuthorization;


    ConnectionDetector cd;

    Button buttonLogin, buttonSkip;
    ProgressDialog pDialog;


    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        bmsClient = BMSClient.getInstance();
        bmsClient.initialize(this, region);

        appId = AppID.getInstance();

        appId.initialize(this, getResources().getString(R.string.appidTenantId), region);

        cd = new ConnectionDetector(this);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonSkip = (Button) findViewById(R.id.buttonSkip);

        buttonLogin.setOnClickListener(this);
        buttonSkip.setOnClickListener(this);



        loginWidget = appId.getLoginWidget();

        loginAuthorization = new AuthorizationListener() {
            @Override
            public void onAuthorizationFailure(AuthorizationException exception) {
//                logger.info("onAuthorizationFailure");

                dismissProgressDialog();

                String errorMessage = exception.getMessage();
                String customTabErrorMessage = "Could NOT find installed browser that support Chrome tabs on the device.";
                System.out.println(errorMessage);

                if (errorMessage.equals(customTabErrorMessage)) {
                    errorMessage = "Please install Chrome Browser before continue";
                }

                final String finalErrorMessage = errorMessage;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIUtil.showSimpleAlertDialog(LoginActivity.this, "Alert!", finalErrorMessage);
                    }
                });


            }

            @Override
            public void onAuthorizationCanceled() {
                dismissProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIUtil.showSimpleAlertDialog(LoginActivity.this, "Alert!", "Authorization Canceled");
                    }
                });
//                logger.info("onAuthorizationCanceled");
            }

            @Override
            public void onAuthorizationSuccess(AccessToken accessToken, final IdentityToken identityToken, RefreshToken refreshToken) {

                final String uid = identityToken.getEmail();

                dismissProgressDialog();

                storeLoginStatus();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Success")
                                .setMessage("You have successfully logged in")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        goToMapsActivity(true, uid);
                                        dialog.dismiss();
                                    }
                                }).setCancelable(false).show();
                    }
                });

            }
        };

    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        // Sends analytics data to the Mobile Analytics service. Your analytics data will only show in the Analytics dashboard after this call.
        Analytics.send();
        // Sends Logger data to the Mobile Analytics service. Your Logger data will only show in the Analytics dashboard after this call.
        Logger.send();
    }


    @Override
    protected void onPause() {
        dismissProgressDialog();
        super.onPause();
    }

    private void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.isShowing();
            pDialog.dismiss();
        }
    }


    private String getDeviceId() {
        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceId;
    }


    private void attemptLogin() {

        Log.d(TAG, "attempt Login");
        showProgressDialog();
        loginWidget.launch(LoginActivity.this, loginAuthorization);

    }

    private void showProgressDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(this);
        }
        pDialog.setMessage("Processing..");
        pDialog.show();
    }

    private void storeLoginStatus() {

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefFileName, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.TAG_LOGIN_STATUS, true);

        editor.apply();
    }


    private void onLoginClick() {

        if (cd.isConnectedToInternet()) {
            attemptLogin();
        } else {

            UIUtil.showSimpleAlertDialog(LoginActivity.this, getString(R.string.warning_dialog_title),
                    getString(R.string.internet_connection_error_message));

        }

    }

    private void onSkipClick() {
        goToMapsActivity(false, "Test");
    }

    private void goToMapsActivity(boolean justLoggedIn, String uid) {

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(Constants.USER_ID, uid);
        startActivity(intent);
        finish();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                onLoginClick();
                break;

            case R.id.buttonSkip:
                onSkipClick();
                break;

            default:
                break;
        }
    }
}
