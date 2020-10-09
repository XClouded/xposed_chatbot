package com.taobao.android.ultron.common.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.datamodel.DMRequestBuilder;

public class ApiModifyUtils {
    public static final String KET_API_MODIFY_CONFIG = "apiModifyConfig";

    public static String processSwitch(Activity activity, Uri uri) {
        String queryParameter;
        JSONArray jSONArray;
        if (activity == null || uri == null || (queryParameter = uri.getQueryParameter(KET_API_MODIFY_CONFIG)) == null) {
            return null;
        }
        try {
            jSONArray = JSONArray.parseArray(queryParameter);
        } catch (Exception unused) {
            jSONArray = null;
        }
        if (jSONArray == null || jSONArray.size() == 0) {
            clearApiConfig(activity);
            Toast.makeText(activity.getApplicationContext(), "已清除所有apiconfig", 0).show();
            return "已清除所有apiconfig";
        }
        SharedPreferences.Editor edit = activity.getSharedPreferences(DMRequestBuilder.NAMESPACE, 0).edit();
        for (int i = 0; i < jSONArray.size(); i++) {
            JSONObject jSONObject = (JSONObject) jSONArray.get(i);
            String string = jSONObject.getString("source");
            if (!TextUtils.isEmpty(string)) {
                edit.putString(string, jSONObject.getString("modifyApi"));
                edit.putString(string + ".version", jSONObject.getString("modifyVersion"));
            }
        }
        edit.apply();
        return "当前config： " + jSONArray.toJSONString();
    }

    private static void clearApiConfig(Activity activity) {
        activity.getSharedPreferences(DMRequestBuilder.NAMESPACE, 0).edit().clear().apply();
    }
}
