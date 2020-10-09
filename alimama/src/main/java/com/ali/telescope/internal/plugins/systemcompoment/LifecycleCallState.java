package com.ali.telescope.internal.plugins.systemcompoment;

import org.json.JSONObject;

public class LifecycleCallState {
    public static final int AFTER_HANDLE = 1;
    public static final int BEFORE_HANDLE = 0;
    public long afterHandleTime;
    public long beforeHandleTime;
    public String className;
    public int handleState;
    public volatile boolean isHandling;
    public JSONObject methodTrace;
    public volatile int sampleTimes;
    public int what;
}
