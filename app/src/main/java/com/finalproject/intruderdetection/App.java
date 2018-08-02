package com.finalproject.intruderdetection;

import android.app.Application;

import com.onesignal.OneSignal;

/**
 * Created by TKPC on 6/29/2018.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
