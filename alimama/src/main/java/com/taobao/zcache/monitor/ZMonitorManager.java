package com.taobao.zcache.monitor;

public class ZMonitorManager {
    private static ZMonitorManager instance;
    private IZCacheMonitor monitorProxy;

    public static ZMonitorManager getInstance() {
        if (instance == null) {
            synchronized (ZMonitorManager.class) {
                if (instance == null) {
                    instance = new ZMonitorManager();
                }
            }
        }
        return instance;
    }

    public void setMonitorProxy(IZCacheMonitor iZCacheMonitor) {
        this.monitorProxy = iZCacheMonitor;
    }

    public IZCacheMonitor getMonitorProxy() {
        return this.monitorProxy;
    }
}
