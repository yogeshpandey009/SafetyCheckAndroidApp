package com.android.safetycheck.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
public class EarthquakeDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "earthquakes.db";

    public EarthquakeDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is first created.
     * @param db The database being created, which all SQL statements will be executed on.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        addEarthquakeTable(db);
    }

    /**
     * Called whenever DATABASE_VERSION is incremented. This is used whenever schema changes need
     * to be made or new tables are added.
     * @param db The database being updated.
     * @param oldVersion The previous version of the database. Used to determine whether or not
     *                   certain updates should be run.
     * @param newVersion The new version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Inserts the movie table into the database.
     * @param db The SQLiteDatabase the table is being inserted into.
     */
    private void addEarthquakeTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + EarthquakeContract.EarthquakeEntry.TABLE_NAME + " (" +
                        EarthquakeContract.EarthquakeEntry._ID + " INTEGER PRIMARY KEY, " +
                        EarthquakeContract.EarthquakeEntry.EQ_ID + " TEXT NOT NULL, " +
                        EarthquakeContract.EarthquakeEntry.EQ_DESC + " TEXT NOT NULL, " +
                        EarthquakeContract.EarthquakeEntry.EQ_LATITUDE + " REAL NOT NULL, " +
                        EarthquakeContract.EarthquakeEntry.EQ_LONGITUDE + " REAL NOT NULL, " +
                        EarthquakeContract.EarthquakeEntry.EQ_MAGNITUDE + " REAL NOT NULL, " +
                        EarthquakeContract.EarthquakeEntry.EQ_TIME + " INTEGER NOT NULL" +
                        ");"
        );
    }
}
