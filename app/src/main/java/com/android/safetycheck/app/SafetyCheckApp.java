package com.android.safetycheck.app;


import android.app.Application;

import com.android.safetycheck.model.Earthquake;
import com.android.safetycheck.model.Person;

import java.util.List;

public class SafetyCheckApp extends Application {
    private List<Earthquake> earthquakesData = null;
    private List<Person> personsData = null;


    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
        initSingletons();
    }

    protected void initSingletons() {
        // Initialize the instance of MySingleton
        //MovieLibrary.initInstance(this.getApplicationContext());
    }

    public List<Earthquake> getEarthquakesData() {
        return earthquakesData;
    }

    public void setEarthquakesData(List<Earthquake> earthquakesData) {
        this.earthquakesData = earthquakesData;
    }

    public List<Person> getPersonsData() {
        return personsData;
    }

    public void setPersonsData(List<Person> personsData) {
        this.personsData = personsData;
    }
}