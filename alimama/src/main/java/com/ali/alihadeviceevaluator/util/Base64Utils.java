package com.ali.alihadeviceevaluator.util;

import android.text.TextUtils;
import android.util.Base64;

public class Base64Utils {
    public static String encodeBase64File(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return Base64.encodeToString(str.getBytes(), 0);
    }
}
