package com.alimamaunion.support.debugmode;

import android.app.Application;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebugFunctionPool {
    public static final String ASSISTED_FUNCTION = "ASSISTED_FUNCTION";
    public static final String MOBILE_INFO = "MOBILE_INFO";
    public static final String SWITCH = "SWITCH";
    private static DebugFunctionPool instance;
    private static Application sApplication;
    private Map<String, List<DebugItemData>> mDebugItemMap = new HashMap();
    private Map<String, String> mDebugType = new HashMap();

    public void init(Application application) {
    }

    public boolean registerDebugFun(String str, DebugItemData debugItemData) {
        return false;
    }

    public static DebugFunctionPool getInstance() {
        if (instance == null) {
            synchronized (DebugFunctionPool.class) {
                if (instance == null) {
                    instance = new DebugFunctionPool();
                }
            }
        }
        return instance;
    }

    public Map<String, String> getDebugType() {
        return this.mDebugType;
    }

    public void registDebugType(String str, String str2) {
        this.mDebugType.put(str, str2);
    }

    public Map<String, List<DebugItemData>> getDebugItemMap() {
        return this.mDebugItemMap;
    }

    public List<DebugItemData> getDebugItemList(String str) {
        return this.mDebugItemMap.get(str);
    }
}
