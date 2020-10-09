package com.taobao.weex.analyzer.core.storage;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.analyzer.core.weex.Performance;
import com.taobao.weex.analyzer.core.weex.PerformanceMonitor;
import com.taobao.weex.common.WXPerformance;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WXPerfStorage {
    private static final int DEFAULT_SAMPLE_NUM = 6;
    private static WXPerfStorage sInstance;
    private Map<String, LinkedList<Performance>> mHistoryData = new HashMap();
    private int mSampleNum = 6;

    private WXPerfStorage() {
    }

    public static WXPerfStorage getInstance() {
        if (sInstance == null) {
            synchronized (WXPerfStorage.class) {
                if (sInstance == null) {
                    sInstance = new WXPerfStorage();
                }
            }
        }
        return sInstance;
    }

    public String savePerformance(@NonNull WXSDKInstance wXSDKInstance) {
        Performance monitor = PerformanceMonitor.monitor(wXSDKInstance);
        if (monitor == null) {
            return null;
        }
        String fetchPageName = fetchPageName(wXSDKInstance, monitor);
        put(fetchPageName, monitor);
        return fetchPageName;
    }

    @Nullable
    public Performance getLatestPerformance(@Nullable String str) {
        List<Performance> performanceList = getPerformanceList(str);
        if (performanceList.size() == 0) {
            return null;
        }
        return performanceList.get(performanceList.size() - 1);
    }

    @NonNull
    public List<Performance> getPerformanceList(@Nullable String str) {
        if (TextUtils.isEmpty(str) || this.mHistoryData == null) {
            return Collections.emptyList();
        }
        List list = this.mHistoryData.get(str);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(list);
    }

    @Nullable
    private static String fetchPageName(@NonNull WXSDKInstance wXSDKInstance, @NonNull Performance performance) {
        Map<String, String> dimensionMap = performance.getDimensionMap();
        String str = dimensionMap != null ? dimensionMap.get(WXPerformance.Dimension.pageName.toString()) : null;
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        return wXSDKInstance.getBundleUrl();
    }

    private void put(@Nullable String str, @Nullable Performance performance) {
        if (!TextUtils.isEmpty(str) && performance != null && this.mHistoryData != null) {
            LinkedList linkedList = this.mHistoryData.get(str);
            if (linkedList == null) {
                linkedList = new LinkedList();
                this.mHistoryData.put(str, linkedList);
            }
            if (linkedList.size() >= this.mSampleNum) {
                linkedList.removeFirst();
            }
            linkedList.add(performance);
        }
    }

    public void setSampleNum(int i) {
        if (i > 0) {
            this.mSampleNum = i;
        }
    }

    public int getSampleNum() {
        return this.mSampleNum;
    }
}
