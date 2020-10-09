package com.ta.audid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.ta.audid.Variables;
import com.ta.utdid2.android.utils.Base64;
import com.ta.utdid2.android.utils.StringUtils;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class UtUtils {
    private static final String UTDID_MODULE = "UtdidMonitor";

    public static void sendUtdidMonitorEvent(String str, Map<String, String> map) {
    }

    public static String getUserNick() {
        SharedPreferences sharedPreferences;
        Context context = Variables.getInstance().getContext();
        if (context == null || (sharedPreferences = context.getSharedPreferences("UTCommon", 0)) == null) {
            return "";
        }
        String string = sharedPreferences.getString("_lun", "");
        if (StringUtils.isEmpty(string)) {
            return "";
        }
        try {
            return new String(Base64.decode(string.getBytes(), 2), "UTF-8");
        } catch (Exception e) {
            UtdidLogger.d("", e);
            return "";
        }
    }

    public static String getUserId() {
        SharedPreferences sharedPreferences;
        Context context = Variables.getInstance().getContext();
        if (context == null || (sharedPreferences = context.getSharedPreferences("UTCommon", 0)) == null) {
            return "";
        }
        String string = sharedPreferences.getString("_luid", "");
        if (StringUtils.isEmpty(string)) {
            return "";
        }
        try {
            return new String(Base64.decode(string.getBytes(), 2), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            UtdidLogger.d("", e);
            return "";
        }
    }
}
