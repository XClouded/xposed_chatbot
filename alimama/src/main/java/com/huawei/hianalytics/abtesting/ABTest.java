package com.huawei.hianalytics.abtesting;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.hianalytics.g.b;
import java.util.LinkedHashMap;

public class ABTest {
    private static final String TAG = "ABTest";

    public static String getExpParam(String str, String str2) {
        b.b(TAG, "getExpParam() is execute");
        if (TextUtils.isEmpty(str)) {
            b.c(TAG, "paramkey is null");
        } else {
            String a = a.a().a(str);
            if (!TextUtils.isEmpty(a)) {
                return a;
            }
        }
        return str2;
    }

    public static void initABTest(Context context, ABTestConfig aBTestConfig) {
        b.b(TAG, "initABTest() is execute");
        a.a().a(context, aBTestConfig);
    }

    public static void onEvent(String str, String str2, LinkedHashMap<String, String> linkedHashMap) {
        b.b(TAG, "onEvent() is execute");
        if (TextUtils.isEmpty(str)) {
            b.c(TAG, "onEvent() paramkey is null");
        } else {
            a.a().a(str, str2, linkedHashMap);
        }
    }

    public static void onReport() {
        b.b(TAG, "onReport() is execute");
        a.a().b();
    }

    public static void setExpSyncInterval(int i) {
        b.b(TAG, "setExpSyncInterval() is execute");
        if (i < 10) {
            i = 10;
        }
        a.a().a(i);
    }

    public static void syncExpParameters() {
        b.b(TAG, "syncExpParameters() is execute");
        a.a().c();
    }
}
