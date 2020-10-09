package com.alibaba.android.umbrella.trace;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.android.umbrella.performance.PerformanceEntity;
import java.util.List;
import java.util.Map;
import mtopsdk.mtop.util.ErrorConstant;

public class UmbrellaUtils {
    public static boolean isFlowControl(String str) {
        if (!TextUtils.isEmpty(str) && !str.equals("FAIL_SYS_TRAFFIC_LIMIT") && !str.equals(ErrorConstant.ERRCODE_API_41X_ANTI_ATTACK) && !str.equals("FAIL_SYS_USER_VALIDATE") && !str.equals("FAIL_LOCAL_ERROR_FANG_XUE_FENG")) {
            return false;
        }
        return true;
    }

    public static PerformanceEntity makePerformanceEntity(String str, String str2, Map<String, Long> map, Map<String, String> map2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || map == null) {
            return null;
        }
        PerformanceEntity performanceEntity = new PerformanceEntity(str, str2, (List<String>) null);
        for (Map.Entry next : map.entrySet()) {
            Long l = (Long) next.getValue();
            Log.e("UmbrellaUtils", String.valueOf(l));
            performanceEntity.addRecordPoint((String) next.getKey(), l.longValue());
        }
        performanceEntity.addArgs(map2);
        return performanceEntity;
    }

    public static boolean checkForceCloseAndBizName(String str) {
        return UmbrellaSimple.isForceClosePerformancePage() || TextUtils.isEmpty(str);
    }
}
