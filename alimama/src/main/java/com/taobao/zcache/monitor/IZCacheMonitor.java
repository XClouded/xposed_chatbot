package com.taobao.zcache.monitor;

import java.util.Map;

public interface IZCacheMonitor {
    void commitStat(String str, String str2, Map<String, String> map, Map<String, Double> map2);
}
