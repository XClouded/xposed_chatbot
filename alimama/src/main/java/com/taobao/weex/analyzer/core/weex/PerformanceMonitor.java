package com.taobao.weex.analyzer.core.weex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.Field;

public class PerformanceMonitor {
    @Nullable
    public static Performance monitor(@Nullable WXSDKInstance wXSDKInstance) {
        WXPerformance wXPerformance;
        if (wXSDKInstance == null) {
            return null;
        }
        try {
            Field declaredField = WXSDKInstance.class.getDeclaredField("mWXPerformance");
            declaredField.setAccessible(true);
            wXPerformance = (WXPerformance) declaredField.get(wXSDKInstance);
        } catch (Exception e) {
            WXLogUtils.e(e.getMessage());
            wXPerformance = null;
        }
        if (wXPerformance != null) {
            return filter(wXPerformance);
        }
        return null;
    }

    private static Performance filter(@NonNull WXPerformance wXPerformance) {
        Performance performance = new Performance();
        performance.setMeasureMap(wXPerformance.getMeasureMap());
        performance.setDimensionMap(wXPerformance.getDimensionMap());
        return performance;
    }
}
