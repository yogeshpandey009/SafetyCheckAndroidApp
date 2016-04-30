package com.android.safetycheck.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.safetycheck.R;
import com.android.safetycheck.model.Person;

import java.util.ArrayList;

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
public class PersonAdapter extends ArrayAdapter<Person> {
    private final Context context;
    private final ArrayList<Person> values;
    private final ListView listView;

    public PersonAdapter(Context context, ListView listView, ArrayList<Person> values) {
        super(context, R.layout.list_earthquake, values);
        this.context = context;
        this.values = values;
        this.listView = listView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(this.getClass().getSimpleName(), "render person for position: " + position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        Person person = values.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_friends, parent, false);
            viewHolder.nameTV = (TextView) convertView.findViewById(R.id.name);
            viewHolder.locTV = (TextView) convertView.findViewById(R.id.location);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.nameTV.setText(person.getName());
        viewHolder.locTV.setText(person.getLocation());
        // Return the completed view to render on screen
        return convertView;
    }

    public ArrayList<Person> getValues() {
        return values;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView nameTV;
        TextView locTV;
    }
}
