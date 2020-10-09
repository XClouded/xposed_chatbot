package com.taobao.rxm.common;

public class RxModel4Phenix {
    private static boolean mIsUseNewThread = true;
    private static boolean mIsUseRecycle = false;
    private static boolean mUsePostAtFront = false;

    public static void setUseNewThread(boolean z) {
        mIsUseNewThread = z;
    }

    public static boolean isUseNewThread() {
        return mIsUseNewThread;
    }

    public static void setUseRecycle(boolean z) {
        mIsUseRecycle = z;
    }

    public static boolean isUseRecycle() {
        return mIsUseRecycle;
    }

    public static void setUsePostAtFront(boolean z) {
        mUsePostAtFront = z;
    }

    public static boolean isUsePostAtFront() {
        return mUsePostAtFront;
    }
}
