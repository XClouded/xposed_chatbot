package com.huawei.hianalytics.util;

import android.content.Context;
import com.huawei.hianalytics.g.b;

public abstract class HiAnalyticTools {
    public static void enableLog(Context context) {
        enableLog(context, 4);
    }

    public static void enableLog(Context context, int i) {
        b.a(context, i, "hianalytics");
    }
}
