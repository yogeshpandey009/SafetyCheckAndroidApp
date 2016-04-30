package com.android.safetycheck.app;

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
 * Created by yogeshpandey on 19/04/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.safetycheck.R;
import com.android.safetycheck.adapter.EarthquakeCursorAdapter;
import com.android.safetycheck.adapter.PersonAdapter;
import com.android.safetycheck.model.Earthquake;
import com.android.safetycheck.model.Person;
import com.android.safetycheck.service.AsyncCollectionConnect;
import com.android.safetycheck.service.MethodInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public ArrayAdapter adapter = null;
    public EarthquakeCursorAdapter eqAdapter = null;
    private int mPage;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_SECTION_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int scene = getArguments().getInt(ARG_SECTION_NUMBER);
        switch (scene) {
            case 1:
                return getEarthquakeTabView(inflater, container);
            case 2:
                return getPersonTabView(inflater, container);
        }
        return null;
    }

    private View getEarthquakeTabView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_earthquakes, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("Earthquakes");
        //textView.setText(getString(R.string.section_format, 1));
        ListView listview = (ListView) rootView.findViewById(R.id.list);
        //adapter = new EarthquakeAdapter(this.getContext(), listview, new ArrayList<Earthquake>());
        //int EQ_LOADER_ID = 99; // From docs: A unique identifier for this loader. Can be whatever you want.
        eqAdapter = new EarthquakeCursorAdapter(this.getContext(), listview, getLoaderManager());
        listview.setAdapter(eqAdapter);
        //getLoaderManager().initLoader(EQ_LOADER_ID, null, eqAdapter);
        //loadEarthquakes(listview);
        Button btn = (Button) rootView.findViewById(R.id.eqBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEarthquakesOnMap(v);
            }
        });
        return rootView;
    }

    private View getPersonTabView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("Friends");
        //textView.setText(getString(R.string.section_format, 2));
        ListView listview = (ListView) rootView.findViewById(R.id.list);
        adapter = new PersonAdapter(this.getContext(), listview, new ArrayList<Person>());
        listview.setAdapter(adapter);
        loadFriends(listview);
        Button btn = (Button) rootView.findViewById(R.id.perBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPersonsOnMap(v);
            }
        });
        return rootView;
    }

    private void loadEarthquakes(View view) {
        try {
            MethodInformation mi = new MethodInformation(view, getString(R.string.defaultURL) + "earthquakes", "listEarthquakes", new HashMap<String, String>());
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                    ex.getMessage());
        }
    }

    private void loadFriends(View view) {
        try {
            MethodInformation mi = new MethodInformation(view, getString(R.string.defaultURL) + "persons", "listPersons",
                    new HashMap<String, String>());
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex) {
            android.util.Log.w(this.getClass().getSimpleName(), "Exception creating adapter: " +
                    ex.getMessage());
        }
    }

    public void showEarthquakesOnMap(View view) {
        Intent i = new Intent(this.getContext(), MapActivity.class);
        i.putExtra("action", "mapEarthquakes");
        List<Earthquake> earthquakeList = eqAdapter.getAllEarthquakes();
        SafetyCheckApp app = (SafetyCheckApp) this.getContext().getApplicationContext();
        app.setEarthquakesData(earthquakeList);
        //serializing huge list of data may crashe the application
        //i.putExtra("earthquakes", earthquakeList);
        startActivity(i);

    }

    public void showPersonsOnMap(View view) {
        Intent i = new Intent(this.getContext(), MapActivity.class);
        i.putExtra("action", "mapPersons");
        ArrayList<Person> personList = ((PersonAdapter)adapter).getValues();
        i.putExtra("persons", personList);
        startActivity(i);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(eqAdapter != null) eqAdapter.notifyDataSetChanged();
    }
}
