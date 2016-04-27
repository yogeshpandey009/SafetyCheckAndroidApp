package com.android.safetycheck.service;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;

import com.android.safetycheck.adapter.PersonAdapter;
import com.android.safetycheck.app.MapActivity;
import com.android.safetycheck.model.Earthquake;
import com.android.safetycheck.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/*
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
 * Purpose: Lab5 - Android application that uses an AsyncTask to accomplish the same effect
 * as using a Thread and android.os.Handler
 *
 * @author Yogesh Pandey ypandey@asu.edu
 *         MS Software Engineering, CIDSE, Arizona State University Polytechnic
 * @version February 2016
 */

public class AsyncCollectionConnect extends AsyncTask<MethodInformation, Integer, MethodInformation> {


    private static final boolean debugOn = true;

    @Override
    protected void onPreExecute(){
        Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
    }

    @Override
    protected MethodInformation doInBackground(MethodInformation... aRequest){
        Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
        try {
            String requestURL = aRequest[0].urlString;
            Map<String, String> paramMap = aRequest[0].params;
            Log.d(this.getClass().getSimpleName(), "params: " + paramMap + " url: " + requestURL);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            headers.set("Accept-Encoding", "gzip");
            Uri.Builder uriBuilder = Uri.parse(requestURL).buildUpon();
            for(String key: paramMap.keySet()) {
                uriBuilder.appendQueryParameter(key, paramMap.get(key));
            }
            requestURL = uriBuilder.build().toString();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            String response = restTemplate.getForObject(requestURL, String.class);
            //JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(requestURL)));
            //String resultStr = conn.call(requestData);
            aRequest[0].resultAsJson = response;
        }catch (Exception ex){
            Log.d(this.getClass().getSimpleName(), "exception in remote call " +
                    ex.getMessage());
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(MethodInformation res){
        Log.d(this.getClass().getSimpleName(), "in onPostExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        //Log.d(this.getClass().getSimpleName(), " resulting is: " + res.resultAsJson);
        try {
            if(res.method.equals("listPersons")) {
                List<Person> persons = getPersons(res);
                ListView listView = ((ListView) res.parentView);
                PersonAdapter adapter = ((PersonAdapter) listView.getAdapter());
                adapter.addAll(persons);
                adapter.notifyDataSetChanged();
                //mainActivity.myListAdapter.moviesInGenre = moviesInGenre;
                //mainActivity.myListAdapter.notifyDataSetChanged();
            } else if(res.method.equals("mapPersons")) {
                List<Person> persons = getPersons(res);
                MapActivity mapAV = ((MapActivity) res.parent);
                mapAV.showPersons(persons);
            } else if(res.method.equals("saveEarthquakes")) {
                List<Earthquake> earthquakes = getEarthquakes(res);
                EarthquakeService eqService = (EarthquakeService) res.parentService;
                eqService.saveEarthquakes(earthquakes);
            }
        }catch (Exception ex){
            Log.e(this.getClass().getSimpleName(), "Exception: " + ex.getMessage());
        }
    }

    /*
    private  void showDialog(String msg, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    */

    private void debug(String msg) {
        if(debugOn)
            Log.d(this.getClass().getSimpleName(), msg);
    }

    private List<Earthquake> getEarthquakes(MethodInformation res) throws Exception {
        List<Earthquake> earthquakes = new ArrayList<>();
        JSONObject result = new JSONObject(res.resultAsJson);
        JSONArray earthquakesJA = result.getJSONArray("data");
        ObjectMapper objectMapper = new ObjectMapper();
        for(int i = 0; i < earthquakesJA.length() && i < 100; i++) {
            Earthquake eq = objectMapper.readValue(earthquakesJA.getJSONObject(i).toString(), Earthquake.class);
            earthquakes.add(eq);
        }
        return earthquakes;
    }


    private List<Person> getPersons(MethodInformation res) throws Exception {
        List<Person> persons = new ArrayList<>();
        JSONObject result = new JSONObject(res.resultAsJson);
        JSONArray personJA = result.getJSONArray("data");
        ObjectMapper objectMapper = new ObjectMapper();
        for(int i = 0; i < personJA.length(); i++) {
            Person per = objectMapper.readValue(personJA.getJSONObject(i).toString(), Person.class);
            persons.add(per);
        }
        return persons;
    }

}
