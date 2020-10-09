package com.alimamaunion.base.configcenter;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConfigResponse {
    public Map<String, ConfigData> configItems = new HashMap();

    public ConfigResponse(JSONObject jSONObject) {
        JSONArray optJSONArray = jSONObject.optJSONArray("result");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                if (optJSONObject != null) {
                    this.configItems.put(optJSONObject.optString("key"), new ConfigData(optJSONObject));
                }
            }
        }
    }
}
