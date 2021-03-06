package com.android.safetycheck.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.android.safetycheck.R;
import com.android.safetycheck.app.MapActivity;
import com.android.safetycheck.data.EarthquakeContract.EarthquakeEntry;
import com.android.safetycheck.model.Earthquake;

import java.util.HashMap;
import java.util.List;

/**
 * Copyright (c) 2016 Yogesh Pandey,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by yogeshpandey on 25/04/16.
 */
public class EarthquakeService extends IntentService {

    private long[] mVibrationPattern = { 0, 200, 200, 300 };
    private Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


    // Must create a default constructor
    public EarthquakeService() {
        // Used to name the worker thread, important only for debugging.
        super("EarthquakeService");
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // This describes what will happen when service is triggered
        loadEarthquakes();
    }

    private void loadEarthquakes() {
        try {
            MethodInformation mi = new MethodInformation(this, getString(R.string.defaultURL) + "earthquakes", "saveEarthquakes", new HashMap<String, String>());
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                    ex.getMessage());
        }
    }

    public void saveEarthquakes(List<Earthquake> earthquakes) {
        ContentResolver cr = getContentResolver();
        for(Earthquake eq: earthquakes) {
            String where = EarthquakeEntry.EQ_ID + " = ?";
            Cursor query = cr.query(EarthquakeEntry.CONTENT_URI, null, where, new String[] { eq.getId() }, null);
            if (query.getCount() == 0) {
                long id = insertEarthquake(eq);
                createNotification((int)id, R.drawable.ic_eq, eq);
            }
        }
    }

    private long insertEarthquake(Earthquake earthquake) {
        ContentValues newValues = new ContentValues();
        newValues.put(EarthquakeEntry.EQ_ID, earthquake.getId());
        newValues.put(EarthquakeEntry.EQ_TIME, earthquake.getTime().getTime());
        newValues.put(EarthquakeEntry.EQ_MAGNITUDE, earthquake.getMagnitude());
        newValues.put(EarthquakeEntry.EQ_LATITUDE, earthquake.getLatitude());
        newValues.put(EarthquakeEntry.EQ_LONGITUDE, earthquake.getLongitude());
        newValues.put(EarthquakeEntry.EQ_DESC, earthquake.getDesc());

        ContentResolver cr = getContentResolver();
        Uri createdRow = cr.insert(EarthquakeEntry.CONTENT_URI, newValues);
        return Long.valueOf(createdRow.getLastPathSegment());
    }


    //  createNotification(56, R.drawable.ic_launcher, "New Message",
    //      "There is a new message from Bob!");
    private void createNotification(int nId, int iconRes, Earthquake eq) {
        String title = "New Earthquake detected!";
        String txt = eq.getDesc();
        //String body = eq.getLatitude() + " , " + eq.getLongitude();
        // First let's define the intent to trigger when notification is selected
        // Start out by creating a normal intent (in this case to open an activity)
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("action", "mapEarthquake");
        intent.putExtra("earthquake", eq);
        // Next, let's turn this into a PendingIntent using
        int requestID = (int) System.currentTimeMillis();
        //unique requestID to differentiate between various notification with same NotifId
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, flags);
        // Now we can attach this to the notification using setContentIntent
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(iconRes)//R.drawable.notification_icon
                .setContentTitle(title)
                .setContentIntent(pIntent)
                .setVibrate(mVibrationPattern)
                        .setSound(soundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(txt))
                //.setLargeIcon(largeIcon)
                .setAutoCancel(true);
                //.setContentText(body);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(nId, mBuilder.build());
    }
}
