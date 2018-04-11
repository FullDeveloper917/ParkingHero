package com.emraz.parkinghero.util;

import android.content.Context;
import android.content.Intent;

import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationDismissHandler;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationStatus;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationStatusListener;

/**
 * Created by david on 23/03/2018.
 */

public class MyDismissHandler extends MFPPushNotificationDismissHandler {

    @Override
    public void onReceive(Context context, Intent intent) {
        MFPPush.getInstance().setNotificationStatusListener(new MFPPushNotificationStatusListener() {
            @Override
            public void onStatusChange(String messageId, MFPPushNotificationStatus status) {
                // Handle status change
            }
        });
        super.onReceive(context, intent);
    }
}
