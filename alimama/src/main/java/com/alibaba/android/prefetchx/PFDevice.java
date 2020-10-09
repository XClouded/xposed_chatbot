package com.alibaba.android.prefetchx;

import androidx.annotation.NonNull;
import com.ali.alihadeviceevaluator.AliHAHardware;
import java.util.HashMap;
import java.util.Map;

public class PFDevice {
    @NonNull
    public static boolean isLowEndDevices() {
        try {
            AliHAHardware.OutlineInfo outlineInfo = AliHAHardware.getInstance().getOutlineInfo();
            if (outlineInfo == null || outlineInfo.deviceLevel != 2) {
                return false;
            }
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    @NonNull
    public static Map<String, Integer> getOutlineInfo() {
        HashMap hashMap = new HashMap(2);
        try {
            AliHAHardware.OutlineInfo outlineInfo = AliHAHardware.getInstance().getOutlineInfo();
            if (outlineInfo != null) {
                hashMap.put("deviceLevel", Integer.valueOf(outlineInfo.deviceLevel));
                hashMap.put("runtimeLevel", Integer.valueOf(outlineInfo.runtimeLevel));
            }
        } catch (Throwable unused) {
        }
        return hashMap;
    }

    @NonNull
    public static int getMemoryRuntimeLevel() {
        try {
            return AliHAHardware.getInstance().getMemoryInfo().runtimeLevel;
        } catch (Throwable unused) {
            return -1;
        }
    }
}
