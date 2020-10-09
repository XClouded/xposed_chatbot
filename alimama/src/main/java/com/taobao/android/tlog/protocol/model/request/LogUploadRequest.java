package com.taobao.android.tlog.protocol.model.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.request.base.LogFeature;

public class LogUploadRequest {
    private String TAG = "TLOG.Protocol.LogUploadRequest";
    public Boolean allowNotWifi = true;
    public LogFeature[] logFeatures;
    public String uploadId;

    public void parse(JSON json, CommandInfo commandInfo) throws Exception {
        JSONArray jSONArray;
        JSONObject jSONObject = (JSONObject) json;
        if (jSONObject.containsKey("allowNotWifi")) {
            this.allowNotWifi = jSONObject.getBoolean("allowNotWifi");
        }
        if (jSONObject.containsKey("uploadId")) {
            this.uploadId = jSONObject.getString("uploadId");
        }
        if (jSONObject.containsKey("logFeatures") && (jSONArray = jSONObject.getJSONArray("logFeatures")) != null && jSONArray.size() > 0) {
            this.logFeatures = parseUploadInfos(jSONArray);
        }
    }

    private LogFeature[] parseUploadInfos(JSONArray jSONArray) {
        LogFeature[] logFeatureArr = new LogFeature[jSONArray.size()];
        for (int i = 0; i < jSONArray.size(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            LogFeature logFeature = new LogFeature();
            if (jSONObject.containsKey("appenderName")) {
                logFeature.appenderName = jSONObject.getString("appenderName");
            }
            if (jSONObject.containsKey("suffix")) {
                logFeature.suffix = jSONObject.getString("suffix");
            }
            if (jSONObject.containsKey("maxHistory")) {
                logFeature.maxHistory = jSONObject.getInteger("maxHistory");
            }
            logFeatureArr[i] = logFeature;
        }
        return logFeatureArr;
    }
}
