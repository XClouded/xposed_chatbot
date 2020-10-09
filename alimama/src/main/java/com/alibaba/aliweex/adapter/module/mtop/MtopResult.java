package com.alibaba.aliweex.adapter.module.mtop;

import com.taobao.weex.bridge.JSCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class MtopResult {
    public String callApi;
    private JSCallback callback;
    private String dataString = null;
    private JSCallback failure;
    private JSONObject result = new JSONObject();
    private String retCode;
    private boolean success = false;

    public MtopResult(JSCallback jSCallback, JSCallback jSCallback2) {
        this.callback = jSCallback;
        this.failure = jSCallback2;
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

    public String getRetCode() {
        return this.retCode;
    }

    public void setRetCode(String str) {
        this.retCode = str;
    }

    public JSCallback getCallback() {
        return this.callback;
    }

    public JSCallback getFailureCallback() {
        return this.failure;
    }

    public JSONObject getResult() {
        return this.result;
    }

    public String toString() {
        if (this.dataString != null) {
            return this.dataString;
        }
        return this.result.toString();
    }
}
