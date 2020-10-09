package com.taobao.android.zcache_monitor;

import com.taobao.zcache.monitor.IZCacheMonitor;
import java.util.Map;

public class ZMonitorImpl implements IZCacheMonitor {
    public void commitStat(String str, String str2, Map<String, String> map, Map<String, Double> map2) {
        ZMonitor.getInstance().commitStat(str, str2, map, map2);
    }
}
