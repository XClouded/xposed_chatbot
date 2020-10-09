package com.alibaba.android.enhance.gpuimage.core;

import android.util.Log;

class Benchmark {
    private static final String TAG = "Benchmark";
    private static long sBeginTime;
    private static long sBitmapProcessTime;
    private static long sPureFilterBeginTime;
    private static long sPureFilterTime;
    private static long sTotalTime;

    Benchmark() {
    }

    static void markTaskStart() {
        sBeginTime = System.currentTimeMillis();
    }

    static void markBitmapProcessEnd() {
        sBitmapProcessTime = System.currentTimeMillis() - sBeginTime;
    }

    static void markPureFilterStart() {
        sPureFilterBeginTime = System.currentTimeMillis();
    }

    static void markPureFilterEnd() {
        sPureFilterTime = System.currentTimeMillis() - sPureFilterBeginTime;
    }

    static void markTaskEnd() {
        sTotalTime = System.currentTimeMillis() - sBeginTime;
    }

    static void printResult() {
        Log.d(TAG, "bitmap process time: " + sBitmapProcessTime + "ms");
        Log.d(TAG, "pure filter time: " + sPureFilterTime + "ms");
        Log.d(TAG, "total time: " + sTotalTime + "ms");
    }
}
