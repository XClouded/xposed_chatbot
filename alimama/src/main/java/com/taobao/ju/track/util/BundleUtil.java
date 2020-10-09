package com.taobao.ju.track.util;

import android.os.Bundle;

public class BundleUtil {
    public static String getString(Bundle bundle, String str) {
        if (bundle == null || str == null || !bundle.containsKey(str)) {
            return null;
        }
        return bundle.getString(str);
    }

    public static String getString(Bundle bundle, String str, String str2) {
        return (bundle == null || str == null || !bundle.containsKey(str)) ? str2 : bundle.getString(str);
    }
}
