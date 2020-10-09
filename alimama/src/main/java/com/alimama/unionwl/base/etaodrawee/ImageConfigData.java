package com.alimama.unionwl.base.etaodrawee;

import org.json.JSONObject;

public class ImageConfigData {
    public int frescoMemory;
    public int maxGif;

    public ImageConfigData(JSONObject jSONObject) {
        if (jSONObject != null) {
            this.frescoMemory = jSONObject.optInt("frescoMemory");
            this.maxGif = jSONObject.optInt("maxGif");
        }
    }

    public ImageConfigData(int i, int i2) {
        this.frescoMemory = i;
        this.maxGif = i2;
    }
}
