package com.android.safetycheck.service;

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
 * Purpose: This class is used as an Params and Result for AsyncCollectionConnect
 *
 * @author Yogesh Pandey ypandey@asu.edu
 *         MS Software Engineering, CIDSE, Arizona State University Polytechnic
 * @version February 2016
 */

import android.app.Activity;
import android.view.View;

import java.util.Map;

public class MethodInformation {
    public String method;
    public Map<String, String> params;
    public View parentView;
    public Activity parent;
    public String urlString;
    public String resultAsJson;

    public MethodInformation(View parentView, String urlString, String method, Map<String, String> params){
        this.method = method;
        this.parentView = parentView;
        this.urlString = urlString;
        this.params = params;
        this.resultAsJson = "{}";
    }

    public MethodInformation(Activity parent, String urlString, String method, Map<String, String> params){
        this.method = method;
        this.parent = parent;
        this.urlString = urlString;
        this.params = params;
        this.resultAsJson = "{}";
    }
}
