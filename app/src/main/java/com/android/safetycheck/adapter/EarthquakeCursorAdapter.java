package com.android.safetycheck.adapter;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.android.safetycheck.R;
import com.android.safetycheck.app.MapActivity;
import com.android.safetycheck.data.EarthquakeContract.EarthquakeEntry;
import com.android.safetycheck.model.Earthquake;

import java.util.Date;

/**
 * Created by yogeshpandey on 19/04/16.
 */
public class EarthquakeCursorAdapter extends SimpleCursorAdapter implements ListView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private final Context context;
    private final ListView listView;
    private static final int EQ_LOADER_ID = 99; // From docs: A unique identifier for this loader. Can be whatever you want.


    private static String[] from = {EarthquakeEntry.EQ_MAGNITUDE, EarthquakeEntry.EQ_LATITUDE, EarthquakeEntry.EQ_LONGITUDE,
            EarthquakeEntry.EQ_TIME};
    private static int[] to = {R.id.magnitude, R.id.lat, R.id.lon, R.id.time};

    public EarthquakeCursorAdapter(Context context, ListView listView, LoaderManager loaderManager) {

        super(context, R.layout.list_earthquake, null, from, to, 0);
        this.context = context;
        this.listView = listView;
        listView.setOnItemClickListener(this);
        loaderManager.initLoader(EQ_LOADER_ID, new Bundle(), this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        this.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        this.swapCursor(null);
    }


    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //TODO: add whereArgs,order from SharedPrefs
        CursorLoader cursorLoader = new CursorLoader(context,
                EarthquakeEntry.CONTENT_URI, EarthquakeEntry.projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContentResolver cr = context.getContentResolver();

        Cursor result = cr.query(ContentUris.withAppendedId(EarthquakeEntry.CONTENT_URI, id), null, null, null, null);

        if (result.moveToFirst()) {
            String eqId = result.getString(result.getColumnIndex(EarthquakeEntry.EQ_ID));
            float mag = result.getFloat(result.getColumnIndex(EarthquakeEntry.EQ_MAGNITUDE));
            float lat = result.getFloat(result.getColumnIndex(EarthquakeEntry.EQ_LATITUDE));
            float lon = result.getFloat(result.getColumnIndex(EarthquakeEntry.EQ_LONGITUDE));
            Date time = new Date(result.getInt(result.getColumnIndex(EarthquakeEntry.EQ_TIME)));
            Earthquake selEarthquake = new Earthquake(eqId, mag, time, lat, lon);
            showEarthquakeOnMap(selEarthquake);
        }

    }

    private void showEarthquakeOnMap(Earthquake selEarthquake) {
        Intent i = new Intent(context, MapActivity.class);
        i.putExtra("action", "mapEarthquake");
        i.putExtra("earthquake", selEarthquake);
        context.startActivity(i);
    }


}
