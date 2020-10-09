package com.alibaba.android.update4mtl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.ali.user.mobile.rpc.ApiConstants;
import com.alibaba.android.update4mtl.data.ResponseData;
import com.alibaba.fastjson.JSON;
import com.taobao.weex.el.parse.Operators;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    private static final String TAG = "Utils";
    private static String sClientVersion;

    public static String getClientVersion(Context context) {
        String str;
        if (TextUtils.isEmpty(sClientVersion)) {
            String str2 = "0.0.1";
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                if (packageInfo != null && !TextUtils.isEmpty(packageInfo.versionName)) {
                    if (packageInfo.versionName.contains("_")) {
                        String[] split = packageInfo.versionName.split("_");
                        if (split != null && split.length > 0 && !TextUtils.isEmpty(split[0])) {
                            str = split[0];
                        }
                    } else {
                        str = packageInfo.versionName;
                    }
                    str2 = str;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            sClientVersion = str2;
        }
        return sClientVersion;
    }

    public static String converMapToDataStr(Map<String, String> map) {
        StringBuilder sb = new StringBuilder(Operators.BLOCK_START_STR);
        if (map != null && map.size() > 0) {
            StringBuilder sb2 = new StringBuilder();
            for (Map.Entry next : map.entrySet()) {
                String str = (String) next.getKey();
                String str2 = (String) next.getValue();
                if (!(str == null || str2 == null)) {
                    StringBuilder sb3 = new StringBuilder();
                    try {
                        sb3.append(JSON.toJSONString(str));
                        sb3.append(":");
                        sb3.append(JSON.toJSONString(str2));
                        sb3.append(",");
                        sb2.append(sb3);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            }
            int length = sb2.toString().length();
            if (length > 1) {
                sb.append(sb2.substring(0, length - 1));
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public static ResponseData byte2Object(byte[] bArr) {
        ResponseData responseData = new ResponseData();
        if (bArr == null) {
            return responseData;
        }
        try {
            JSONObject jSONObject = new JSONObject(new String(bArr)).getJSONObject("data");
            if (jSONObject != null && TextUtils.equals("true", jSONObject.getString("hasAvailableUpdate"))) {
                responseData.hasAvailableUpdate = true;
                JSONObject jSONObject2 = jSONObject.getJSONObject("updateInfo");
                if (jSONObject2 != null) {
                    responseData.updateInfo.name = jSONObject2.getString("name");
                    responseData.updateInfo.size = jSONObject2.getString("size");
                    responseData.updateInfo.version = jSONObject2.getString("version");
                    responseData.updateInfo.pri = jSONObject2.getString("pri");
                    responseData.updateInfo.info = jSONObject2.getString(ApiConstants.ApiField.INFO);
                    responseData.updateInfo.url = jSONObject2.getString("url");
                    responseData.updateInfo.md5 = jSONObject2.getString("md5");
                    if (jSONObject2.has("patchUrl")) {
                        responseData.updateInfo.patchUrl = jSONObject2.getString("patchUrl");
                    }
                    if (jSONObject2.has("patchSize")) {
                        responseData.updateInfo.patchSize = jSONObject2.getString("patchSize");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseData;
    }
}
