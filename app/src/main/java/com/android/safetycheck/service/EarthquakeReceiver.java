package com.android.safetycheck.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by yogeshpandey on 25/04/16.
 */
public class EarthquakeReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 1;
    public static final String ACTION = "com.android.safetycheck.earthquakealarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, EarthquakeService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}