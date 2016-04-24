package com.android.safetycheck.model;

import java.util.List;

/**
 * Created by yogeshpandey on 20/04/16.
 */
public class SCResponse {
    private Boolean success;
    private Object data;
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
