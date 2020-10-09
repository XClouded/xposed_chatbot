package com.taobao.zcache.global;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

public class ZCacheGlobal {
    public static final int DAILY = 2;
    public static final int ONLINE = 0;
    public static final int PRE = 1;
    public static final String TAG = "ZCache3.0";
    public static final int ZCacheFeatureDefault = 0;
    public static final int ZCacheFeatureDisableIncrement = 65536;
    public static final int ZCacheFeatureEncryptA = 1;
    public static final int ZCacheFeatureUseOldAWPServerAPI = 131072;
    public static final int ZCacheFeatureWaitUntilUpdateQueue = 2;
    private String appKey;
    private String appVersion;
    private Context context;
    private int env;
    private Handler mHandler;

    private ZCacheGlobal() {
        HandlerThread handlerThread = new HandlerThread("zcache");
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
    }

    private static final class SingleHolder {
        public static final ZCacheGlobal INSTANCE = new ZCacheGlobal();

        private SingleHolder() {
        }
    }

    public static ZCacheGlobal instance() {
        return SingleHolder.INSTANCE;
    }

    public Handler handler() {
        return this.mHandler;
    }

    public int env() {
        return this.env;
    }

    public void setEnv(int i) {
        this.env = i;
    }

    public Context context() {
        return this.context;
    }

    public void setContext(Context context2) {
        this.context = context2;
    }

    public String appKey() {
        return this.appKey;
    }

    public void setAppKey(String str) {
        this.appKey = str;
    }

    public String appVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String str) {
        this.appVersion = str;
    }
}
