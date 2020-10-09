package com.alimamaunion.base.configcenter;

import org.json.JSONObject;

public class ConfigData {
    public byte[] data;
    public boolean isChanged;
    public String lastModified;
    public String type;
    public String updateInterval;

    public ConfigData(JSONObject jSONObject) {
        this.type = "0";
        this.lastModified = "0";
        this.updateInterval = "0";
        this.isChanged = true;
        this.data = jSONObject.optString("content").getBytes();
        this.type = jSONObject.optString("type");
        this.lastModified = jSONObject.optString("lastModified");
        this.updateInterval = jSONObject.optString("updateInterval");
    }

    public ConfigData(byte[] bArr, String str, String str2) {
        this(bArr, str, str2, true);
    }

    public ConfigData(byte[] bArr, String str, String str2, boolean z) {
        this.type = "0";
        this.lastModified = "0";
        this.updateInterval = "0";
        this.isChanged = true;
        this.data = bArr;
        this.lastModified = str;
        this.updateInterval = str2;
        this.isChanged = z;
    }
}
