package com.emraz.parkinghero.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.emraz.parkinghero.R;
import com.emraz.parkinghero.util.Constants;
import com.emraz.parkinghero.util.Log;

import com.emraz.parkinghero.util.Common.*;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static final int SPLASH_TIME_OUT = 1500;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audio != null) {
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            audio.setStreamVolume(AudioManager.STREAM_RING, audio.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
        }

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                Log.d(TAG, "Splash finished");

                if (isLoggedIn()) {
                    Log.d(TAG, "logged in");
                    Intent intent = new Intent(SplashActivity.this, MapsActivity.class);
                    startActivity(intent);
                } else {
                    Log.d(TAG, "not Logged in");
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

                finish();

            }
        }, SPLASH_TIME_OUT);


    }

    private boolean isLoggedIn() {

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.sharedPrefFileName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.TAG_LOGIN_STATUS, false);
    }

}
