package com.alimama.moon.utils;

import android.util.Log;
import com.taobao.weex.el.parse.Operators;

public class TimeCalcUtil {
    private static final String TAG = "TimeCalc";
    private static long sInitTime;

    public static void init() {
        sInitTime = System.currentTimeMillis();
        Log.d(TAG, "初始化 0.   (" + sInitTime + Operators.BRACKET_END_STR);
    }

    public static void resetInit(String str) {
        sInitTime = System.currentTimeMillis();
        Log.d(TAG, "重新初始化:" + str + "time:" + 0);
    }

    public static void recordPonit(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        Log.d(TAG, " 耗时:" + (currentTimeMillis - sInitTime) + "    业务点:" + str);
    }
}
