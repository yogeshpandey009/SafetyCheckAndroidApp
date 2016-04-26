package com.android.safetycheck.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by yogeshpandey on 25/04/16.
 */
public class EarthquakeContract {

    /**
     * The Content Authority is a name for the entire content provider, similar to the relationship
     * between a domain name and its website. A convenient string to use for content authority is
     * the package name for the app, since it is guaranteed to be unique on the device.
     */
    public static final String CONTENT_AUTHORITY = "com.android.safetycheck";

    /**
     * The content authority is used to create the base of all URIs which apps will use to
     * contact this content provider.
     */
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * A list of possible paths that will be appended to the base URI for each of the different
     * tables.
     */
    public static final String PATH_EARTHQUAKE = "earthquake";


    /**
     * Create one class for each table that handles all information regarding the table schema and
     * the URIs related to it.
     */
    public static final class EarthquakeEntry implements BaseColumns {
        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EARTHQUAKE).build();

        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_EARTHQUAKE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_EARTHQUAKE;

        // Define the table schema
        public static final String TABLE_NAME = "EarthquakeTable";
        public static final String EQ_ID = "earthquakeId";
        public static final String EQ_MAGNITUDE = "magnitude";
        public static final String EQ_LATITUDE = "latitude";
        public static final String EQ_LONGITUDE = "longitude";
        public static final String EQ_TIME = "time";
        public static final String[] projection = {
                _ID, EQ_ID, EQ_MAGNITUDE, EQ_LATITUDE, EQ_LONGITUDE, EQ_TIME
        };

        // Define a function to build a URI to find a specific earthquake by it's identifier
        public static Uri buildEarthquakeUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}
