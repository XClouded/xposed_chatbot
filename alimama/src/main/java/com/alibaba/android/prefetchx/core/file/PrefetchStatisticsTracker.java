package com.alibaba.android.prefetchx.core.file;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import anetwork.channel.NetworkEvent;
import anetwork.channel.statist.StatisticData;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.core.file.PrefetchManager;
import com.alibaba.mtl.appmonitor.AppMonitor;

public class PrefetchStatisticsTracker {
    static final String MODULE_NAME = "Aliweex_JSPrefetch";
    private static final String MONITOR_CACHE_HIT = "cache_hit";
    static final String MONITOR_TASK_SUCCESS = "task_success";
    private static final String MONITOR_TOTAL_CACHE_HIT = "total_cache_hit";

    private static boolean shouldTrack() {
        return true;
    }

    public static void trackPrefetchCacheHitRatio(@Nullable String str, @Nullable NetworkEvent.FinishEvent finishEvent) {
        if (!TextUtils.isEmpty(str) && finishEvent != null && shouldTrack() && !PrefetchManager.shouldReport.compareAndSet(false, true)) {
            if (finishEvent.getHttpCode() == 200 || finishEvent.getHttpCode() == 304) {
                String str2 = "{\"pageName\":\"" + str + "\"}";
                StatisticData statisticData = finishEvent.getStatisticData();
                if (statisticData != null) {
                    int i = statisticData.resultCode;
                    String str3 = statisticData.connectionType;
                    PrefetchManager.PrefetchEntry findAlikeEntryInCache = PrefetchManager.findAlikeEntryInCache(str, PrefetchManager.getPrefetchEntries());
                    if (findAlikeEntryInCache == null) {
                        AppMonitor.Alarm.commitFail(MODULE_NAME, MONITOR_CACHE_HIT, str2, "-1", "url_not_in_cache");
                        trackTotalCacheHitRatio(str, false, "-1", "url_not_in_cache");
                    } else if (TextUtils.isEmpty(str3) || !"cache".equalsIgnoreCase(str3)) {
                        if (i == 304) {
                            AppMonitor.Alarm.commitSuccess(MODULE_NAME, MONITOR_CACHE_HIT, str2);
                            trackTotalCacheHitRatio(str, true, new String[0]);
                            return;
                        }
                        AppMonitor.Alarm.commitFail(MODULE_NAME, MONITOR_CACHE_HIT, str2, "-2", PFConstant.File.PF_FILE_ERROR_CONNECTION_MSG);
                        trackTotalCacheHitRatio(str, false, "-2", PFConstant.File.PF_FILE_ERROR_CONNECTION_MSG);
                    } else if (findAlikeEntryInCache.isFresh()) {
                        AppMonitor.Alarm.commitSuccess(MODULE_NAME, MONITOR_CACHE_HIT, str2);
                    } else {
                        AppMonitor.Alarm.commitFail(MODULE_NAME, MONITOR_CACHE_HIT, str2, "-3", "exceed_max_age");
                        trackTotalCacheHitRatio(str, false, "-3", "exceed_max_age");
                    }
                } else {
                    AppMonitor.Alarm.commitFail(MODULE_NAME, MONITOR_CACHE_HIT, str2, "-4", "unknown_error");
                    trackTotalCacheHitRatio(str, false, "-4", "unknown_error");
                }
            }
        }
    }

    public static void trackTotalCacheHitRatio(@Nullable String str, boolean z, String... strArr) {
        if (!TextUtils.isEmpty(str) && shouldTrack()) {
            String str2 = "{\"pageName\":\"" + str + "\"}";
            if (z) {
                AppMonitor.Alarm.commitSuccess(MODULE_NAME, MONITOR_TOTAL_CACHE_HIT, str2);
            } else if (strArr.length >= 2) {
                AppMonitor.Alarm.commitFail(MODULE_NAME, MONITOR_TOTAL_CACHE_HIT, str2, strArr[0], strArr[1]);
            }
        }
    }
}
