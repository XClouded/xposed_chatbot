package com.alibaba.ha.adapter.service.godeye;

import com.taobao.android.tlog.protocol.Constants;
import com.taobao.onlinemonitor.OnLineMonitor;
import com.taobao.tao.log.godeye.core.GodEyeAppListener;
import java.util.HashMap;
import java.util.Map;

public class GodEyeAppAllInfoListener implements GodEyeAppListener {
    public Map<String, Object> getAppInfo() {
        try {
            OnLineMonitor.OnLineStat onLineStat = OnLineMonitor.getOnLineStat();
            if (onLineStat == null) {
                return null;
            }
            HashMap hashMap = new HashMap();
            hashMap.put(Constants.DevicePropertyKey.KEY_DEVICE_INFO, onLineStat.deviceInfo);
            hashMap.put(Constants.DevicePropertyKey.KEY_PERFORMANCE_INFO, onLineStat.performanceInfo);
            hashMap.put(Constants.DevicePropertyKey.KEY_IO_STAT, onLineStat.iOStat);
            hashMap.put(Constants.DevicePropertyKey.KEY_CPU_STAT, onLineStat.cpuStat);
            hashMap.put(Constants.DevicePropertyKey.KEY_TRAFFIC_STAT_INFO, onLineStat.trafficStatsInfo);
            hashMap.put(Constants.DevicePropertyKey.KEY_BATTERY_INFO, onLineStat.batteryInfo);
            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
