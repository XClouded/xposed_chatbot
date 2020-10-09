package com.taobao.alivfsadapter;

public interface AVFSSDKAppMonitor {
    void hitMemoryCacheForModule(String str, boolean z);

    void writeEvent(MonitorCacheEvent monitorCacheEvent);
}
