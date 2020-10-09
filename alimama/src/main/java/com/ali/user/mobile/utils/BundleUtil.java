package com.ali.user.mobile.utils;

import android.os.Bundle;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import mtopsdk.common.util.SymbolExpUtil;

public class BundleUtil {
    public static final String TAG = "login.BundleUitl";

    public static Bundle serialBundle(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        String[] split = str.split("&");
        Bundle bundle = new Bundle();
        for (String str2 : split) {
            int indexOf = str2.indexOf(SymbolExpUtil.SYMBOL_EQUAL);
            if (indexOf > 0 && indexOf < str2.length() - 1) {
                bundle.putString(str2.substring(0, indexOf), str2.substring(indexOf + 1));
            }
        }
        return bundle;
    }

    public static JSONObject bundleToJSON(String str) {
        JSONObject jSONObject = new JSONObject();
        if (str != null && str.length() > 0) {
            for (String str2 : str.split("&")) {
                int indexOf = str2.indexOf(SymbolExpUtil.SYMBOL_EQUAL);
                if (indexOf > 0 && indexOf < str2.length() - 1) {
                    try {
                        jSONObject.put(str2.substring(0, indexOf), (Object) str2.substring(indexOf + 1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return jSONObject;
    }
}
