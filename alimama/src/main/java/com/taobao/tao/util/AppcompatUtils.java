package com.taobao.tao.util;

import androidx.appcompat.taobao.util.Globals;

@Deprecated
public class AppcompatUtils {
    public static String getMenuTitle(String str, String str2) {
        return str2 + ":" + str;
    }

    public static String getMenuTitle(String str, int i) {
        return Globals.getApplication().getString(i) + ":" + str;
    }
}
