package com.alibaba.ut.abtest.pipeline;

import java.io.Serializable;
import org.json.JSONObject;

public class Response implements Serializable {
    private static final long serialVersionUID = 2645560765736423053L;
    private byte[] byteData;
    private int code;
    private Object data;
    private JSONObject dataJsonObject;
    private int httpResponseCode;
    private String message;
    private boolean success;

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public int getHttpResponseCode() {
        return this.httpResponseCode;
    }

    public void setHttpResponseCode(int i) {
        this.httpResponseCode = i;
    }

    public byte[] getByteData() {
        return this.byteData;
    }

    public void setByteData(byte[] bArr) {
        this.byteData = bArr;
    }

    public JSONObject getDataJsonObject() {
        return this.dataJsonObject;
    }

    public void setDataJsonObject(JSONObject jSONObject) {
        this.dataJsonObject = jSONObject;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object obj) {
        this.data = obj;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean z) {
        this.success = z;
    }
}
