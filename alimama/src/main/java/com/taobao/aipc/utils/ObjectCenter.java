package com.taobao.aipc.utils;

import com.taobao.aipc.logs.IPCLog;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectCenter {
    private static final String TAG = "ObjectCenter";
    private static volatile ObjectCenter sInstance;
    private final ConcurrentHashMap<String, Object> mObjects = new ConcurrentHashMap<>();

    private ObjectCenter() {
    }

    public static ObjectCenter getInstance() {
        if (sInstance == null) {
            synchronized (ObjectCenter.class) {
                if (sInstance == null) {
                    sInstance = new ObjectCenter();
                }
            }
        }
        return sInstance;
    }

    public Object getObject(String str) {
        return this.mObjects.get(str);
    }

    public void putObject(String str, Object obj) {
        this.mObjects.put(str, obj);
    }

    public void deleteObjects(List<String> list) {
        if (list != null) {
            for (String remove : list) {
                if (this.mObjects.remove(remove) == null) {
                    IPCLog.e(TAG, "An error occurs in the recycle.");
                }
            }
        }
    }
}
