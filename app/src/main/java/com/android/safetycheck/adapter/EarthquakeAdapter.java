package com.android.safetycheck.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.safetycheck.app.MapActivity;
import com.android.safetycheck.app.R;
import com.android.safetycheck.model.Earthquake;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yogeshpandey on 19/04/16.
 */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> implements View.OnTouchListener, ListView.OnItemClickListener {
    private final Context context;
    private final ArrayList<Earthquake> values;
    private final ListView listView;

    public EarthquakeAdapter(Context context, ListView listView, ArrayList<Earthquake> values) {
        super(context, R.layout.list_earthquake, values);
        this.context = context;
        this.values = values;
        this.listView = listView;
        //listView.setOnTouchListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(this.getClass().getSimpleName(), "render earthquake for position: " + position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        Earthquake eq = values.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_earthquake, parent, false);
            viewHolder.eqIdTV = (TextView) convertView.findViewById(R.id.earthquakeId);
            viewHolder.magTv = (TextView) convertView.findViewById(R.id.magnitude);
            viewHolder.timeTV = (TextView) convertView.findViewById(R.id.time);
            viewHolder.coTV = (TextView) convertView.findViewById(R.id.coordinates);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.eqIdTV.setText(eq.getId());
        viewHolder.magTv.setText(eq.getMagnitude() + "");
        viewHolder.timeTV.setText(eq.getTime().toGMTString());
        viewHolder.coTV.setText(eq.getLatitude() + " " + eq.getLongitude());
        // Return the completed view to render on screen
        return convertView;
    }

    public ArrayList<Earthquake> getValues() {
        return values;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // when the user touches an item, onTouch is called for action down and again for action up
        // we only want to do something on one of those actions. event tells us which action.
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            android.util.Log.d(this.getClass().getSimpleName(), "in onTouch called for view of type: " +
                    v.getClass().toString());
            Integer pos = (Integer)v.getTag();
            // onTouch is passed the textview's parent view, a linearlayout - what we set the
            // event on. Look at its children to find the textview
                    //TextView eqIdTV = (TextView) layView.getChildAt(0);
                    //TextView magTV = (TextView) layView.getChildAt(1);
                    //TextView cordTV = (TextView) layView.getChildAt(3);
            Earthquake selEarthquake = values.get(pos);
//                    String eqId = eqIdTV.getText().toString();
//                    String mag = magTV.getText().toString();
//                    String cord = cordTV.getText().toString();
            showEarthquakeOnMap(selEarthquake);
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        Earthquake selEarthquake = values.get(position);
        showEarthquakeOnMap(selEarthquake);
    }

    // View lookup cache
    private static class ViewHolder {
        TextView eqIdTV;
        TextView magTv;
        TextView timeTV;
        TextView coTV;
    }

    private void showEarthquakeOnMap(Earthquake selEarthquake) {
        Intent i = new Intent(context, MapActivity.class);
        i.putExtra("action", "mapEarthquake");
        i.putExtra("earthquake", selEarthquake);
        context.startActivity(i);
    }


}
