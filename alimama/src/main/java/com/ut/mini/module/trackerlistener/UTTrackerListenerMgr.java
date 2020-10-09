package com.ut.mini.module.trackerlistener;

import android.text.TextUtils;
import com.alibaba.analytics.core.config.UTClientConfigMgr;
import com.alibaba.fastjson.JSONObject;
import com.ut.mini.UTTracker;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class UTTrackerListenerMgr {
    private static UTTrackerListenerMgr mInstance;
    private Map<String, UTTrackerListener> allTrackerListenerMap = new Hashtable();
    private UTTrackerListenerConfig listenerConfig = null;
    private Map<String, UTTrackerListener> openTrackerListenerMap = new Hashtable();

    private UTTrackerListenerMgr() {
        UTClientConfigMgr.getInstance().registerConfigChangeListener(new UTClientConfigMgr.IConfigChangeListener() {
            public String getKey() {
                return "trackerListener";
            }

            public void onChange(String str) {
                UTTrackerListenerMgr.this.parseListenerConfig(str);
            }
        });
    }

    public static UTTrackerListenerMgr getInstance() {
        if (mInstance == null) {
            synchronized (UTTrackerListenerMgr.class) {
                if (mInstance == null) {
                    mInstance = new UTTrackerListenerMgr();
                }
            }
        }
        return mInstance;
    }

    public synchronized void registerListener(UTTrackerListener uTTrackerListener) {
        if (uTTrackerListener != null) {
            String trackerListenerName = uTTrackerListener.trackerListenerName();
            if (!TextUtils.isEmpty(trackerListenerName) && !this.allTrackerListenerMap.containsKey(trackerListenerName)) {
                this.allTrackerListenerMap.put(trackerListenerName, uTTrackerListener);
                if (isOpen(trackerListenerName)) {
                    this.openTrackerListenerMap.put(trackerListenerName, uTTrackerListener);
                }
            }
        }
    }

    public synchronized void unregisterListener(UTTrackerListener uTTrackerListener) {
        if (uTTrackerListener != null) {
            String trackerListenerName = uTTrackerListener.trackerListenerName();
            if (!TextUtils.isEmpty(trackerListenerName)) {
                this.allTrackerListenerMap.remove(trackerListenerName);
                this.openTrackerListenerMap.remove(trackerListenerName);
            }
        }
    }

    public void pageAppear(UTTracker uTTracker, Object obj, String str, boolean z) {
        for (Map.Entry<String, UTTrackerListener> value : this.openTrackerListenerMap.entrySet()) {
            UTTrackerListener uTTrackerListener = (UTTrackerListener) value.getValue();
            if (uTTrackerListener != null) {
                uTTrackerListener.pageAppear(uTTracker, obj, str, z);
            }
        }
    }

    public void pageDisAppear(UTTracker uTTracker, Object obj) {
        for (Map.Entry<String, UTTrackerListener> value : this.openTrackerListenerMap.entrySet()) {
            UTTrackerListener uTTrackerListener = (UTTrackerListener) value.getValue();
            if (uTTrackerListener != null) {
                uTTrackerListener.pageDisAppear(uTTracker, obj);
            }
        }
    }

    public void updateNextPageProperties(UTTracker uTTracker, Map<String, String> map) {
        for (Map.Entry<String, UTTrackerListener> value : this.openTrackerListenerMap.entrySet()) {
            UTTrackerListener uTTrackerListener = (UTTrackerListener) value.getValue();
            if (uTTrackerListener != null) {
                uTTrackerListener.updateNextPageProperties(uTTracker, map);
            }
        }
    }

    public void updatePageName(UTTracker uTTracker, Object obj, String str) {
        for (Map.Entry<String, UTTrackerListener> value : this.openTrackerListenerMap.entrySet()) {
            UTTrackerListener uTTrackerListener = (UTTrackerListener) value.getValue();
            if (uTTrackerListener != null) {
                uTTrackerListener.updatePageName(uTTracker, obj, str);
            }
        }
    }

    public void updatePageProperties(UTTracker uTTracker, Object obj, Map<String, String> map) {
        for (Map.Entry<String, UTTrackerListener> value : this.openTrackerListenerMap.entrySet()) {
            UTTrackerListener uTTrackerListener = (UTTrackerListener) value.getValue();
            if (uTTrackerListener != null) {
                uTTrackerListener.updatePageProperties(uTTracker, obj, map);
            }
        }
    }

    public void send(UTTracker uTTracker, Map<String, String> map) {
        for (Map.Entry<String, UTTrackerListener> value : this.openTrackerListenerMap.entrySet()) {
            UTTrackerListener uTTrackerListener = (UTTrackerListener) value.getValue();
            if (uTTrackerListener != null) {
                uTTrackerListener.send(uTTracker, map);
            }
        }
    }

    public void addExposureViewToCommit(String str, String str2, String str3, String str4, Map<String, String> map) {
        for (Map.Entry<String, UTTrackerListener> value : this.openTrackerListenerMap.entrySet()) {
            UTTrackerListener uTTrackerListener = (UTTrackerListener) value.getValue();
            if (uTTrackerListener != null) {
                uTTrackerListener.addExposureViewToCommit(str, str2, str3, str4, map);
            }
        }
    }

    public void updatePageUtparam(Object obj, String str) {
        for (Map.Entry<String, UTTrackerListener> value : this.openTrackerListenerMap.entrySet()) {
            UTTrackerListener uTTrackerListener = (UTTrackerListener) value.getValue();
            if (uTTrackerListener != null) {
                uTTrackerListener.updatePageUtparam(obj, str);
            }
        }
    }

    public void updateNextPageUtparam(String str) {
        for (Map.Entry<String, UTTrackerListener> value : this.openTrackerListenerMap.entrySet()) {
            UTTrackerListener uTTrackerListener = (UTTrackerListener) value.getValue();
            if (uTTrackerListener != null) {
                uTTrackerListener.updateNextPageUtparam(str);
            }
        }
    }

    public void viewBecomeVisible(String str, String str2, String str3) {
        for (Map.Entry<String, UTTrackerListener> value : this.openTrackerListenerMap.entrySet()) {
            UTTrackerListener uTTrackerListener = (UTTrackerListener) value.getValue();
            if (uTTrackerListener != null) {
                uTTrackerListener.viewBecomeVisible(str, str2, str3);
            }
        }
    }

    private boolean isOpen(String str) {
        if (this.listenerConfig == null) {
            return true;
        }
        List<String> open = this.listenerConfig.getOpen();
        if (open != null && open.contains(str)) {
            return true;
        }
        List<String> close = this.listenerConfig.getClose();
        if (close != null && close.contains(str)) {
            return false;
        }
        String other = this.listenerConfig.getOther();
        if (TextUtils.isEmpty(other) || !other.equals("close")) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public synchronized void parseListenerConfig(String str) {
        try {
            this.listenerConfig = (UTTrackerListenerConfig) JSONObject.parseObject(str, UTTrackerListenerConfig.class);
        } catch (Exception unused) {
            this.listenerConfig = null;
        }
        for (Map.Entry next : this.allTrackerListenerMap.entrySet()) {
            String str2 = (String) next.getKey();
            if (!isOpen(str2)) {
                this.openTrackerListenerMap.remove(str2);
            } else if (!this.openTrackerListenerMap.containsKey(str2)) {
                this.openTrackerListenerMap.put(str2, (UTTrackerListener) next.getValue());
            }
        }
    }
}
