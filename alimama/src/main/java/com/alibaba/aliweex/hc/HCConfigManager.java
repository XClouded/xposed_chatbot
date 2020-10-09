package com.alibaba.aliweex.hc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.utils.WXLogUtils;
import java.util.LinkedList;
import java.util.List;

public class HCConfigManager {
    private static final String INDEX = "index";
    private static final String KEY = "configItems";
    public static final String TAG = "MarketConfigManager";
    private List<HCConfig> hcConfigs = new LinkedList();

    public HCConfigManager(JSONObject jSONObject) {
        try {
            JSONArray jSONArray = jSONObject.getJSONArray(KEY);
            for (int i = 0; i < jSONArray.size(); i++) {
                this.hcConfigs.add((HCConfig) jSONArray.getObject(i, HCConfig.class));
            }
        } catch (RuntimeException e) {
            WXLogUtils.e(TAG, WXLogUtils.getStackTrace(e));
        }
    }

    public HCConfig getMarketConfig(JSONObject jSONObject) {
        return getMarketConfig(jSONObject.getInteger("index").intValue());
    }

    public HCConfig getMarketConfig() {
        return getMarketConfig(0);
    }

    public HCConfig getMarketConfig(int i) {
        try {
            return this.hcConfigs.get(i);
        } catch (RuntimeException e) {
            WXLogUtils.e(TAG, WXLogUtils.getStackTrace(e));
            return new HCConfig();
        }
    }
}
