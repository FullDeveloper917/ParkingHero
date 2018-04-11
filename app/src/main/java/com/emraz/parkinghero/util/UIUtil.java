package com.emraz.parkinghero.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by Yousuf on 6/17/2017.
 */

public class UIUtil {


    static String TAG = UIUtil.class.getSimpleName();

    public static void showSimpleAlertDialog(Context context, String title, String message) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
}



