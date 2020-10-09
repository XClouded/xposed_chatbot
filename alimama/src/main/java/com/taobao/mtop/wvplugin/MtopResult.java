package com.taobao.mtop.wvplugin;

import android.taobao.windvane.jsbridge.WVCallBackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class MtopResult {
    private String dataString = null;
    private WVCallBackContext jsContext = null;
    private JSONObject result = new JSONObject();
    private boolean success = false;

    public MtopResult() {
    }

    public MtopResult(WVCallBackContext wVCallBackContext) {
        this.jsContext = wVCallBackContext;
    }

    public void setDataString(String str) {
        this.dataString = str;
    }

    public void setData(JSONObject jSONObject) {
        if (jSONObject != null) {
            this.result = jSONObject;
        }
    }

    public void addData(String str, String str2) {
        if (str != null && str2 != null) {
            try {
                this.result.put(str, str2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void addData(String str, JSONArray jSONArray) {
        if (str != null && jSONArray != null) {
            try {
                this.result.put(str, jSONArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean z) {
        this.success = z;
    }

    public WVCallBackContext getJsContext() {
        return this.jsContext;
    }

    public void setJsContext(WVCallBackContext wVCallBackContext) {
        this.jsContext = wVCallBackContext;
    }

    public String toString() {
        if (this.dataString != null) {
            return this.dataString;
        }
        return this.result.toString();
    }
}
