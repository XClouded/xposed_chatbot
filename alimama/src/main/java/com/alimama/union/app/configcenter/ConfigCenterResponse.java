package com.alimama.union.app.configcenter;

import com.alimamaunion.base.configcenter.ConfigData;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.util.HashMap;
import java.util.Map;

public class ConfigCenterResponse {
    public Map<String, ConfigData> configItems = new HashMap();

    public ConfigCenterResponse(SafeJSONObject safeJSONObject) {
        SafeJSONArray optJSONArray = safeJSONObject.optJSONArray("result");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                SafeJSONObject optJSONObject = optJSONArray.optJSONObject(i);
                if (optJSONObject != null) {
                    this.configItems.put(optJSONObject.optString("key"), new ConfigData(optJSONObject));
                }
            }
        }
    }
}
