package com.alibaba.aliweex.adapter.module;

import android.text.TextUtils;
import com.alibaba.aliweex.adapter.adapter.WXAPMAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.utils.WXUtils;
import java.util.Map;

public class WXPerformanceModule extends WXModule {
    private static final String KEY_PROPERTIES_JS_MTOP_TIME = "wxMtopTime";
    private static final String KEY_PROPERTIES_JS_PREFETCH_TIME = "wxJSDataPrefetchTime";
    private static final String KEY_PROPERTIES_PREFETCH_SUCCESS = "wxJSDataPrefetchSuccess";
    public static final String KEY_STAGE_JS_ASYNC_DATA_END = "wxJSAsyncDataEnd";
    public static final String KEY_STAGE_JS_ASYNC_DATA_START = "wxJSAsyncDataStart";
    private long mEndTime = -1;
    private long mStartTime = -1;

    @JSMethod
    public void recordAsyncTime(Map<String, String> map) {
        String str;
        if (map != null) {
            if (map.containsKey(KEY_PROPERTIES_JS_PREFETCH_TIME)) {
                str = KEY_PROPERTIES_JS_PREFETCH_TIME;
            } else if (map.containsKey(KEY_PROPERTIES_JS_MTOP_TIME)) {
                str = KEY_PROPERTIES_JS_MTOP_TIME;
            } else {
                return;
            }
            long j = -1;
            try {
                j = Long.valueOf(map.get(str)).longValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (j >= 0) {
                if (this.mStartTime < 0) {
                    this.mStartTime = WXUtils.getFixUnixTime() - j;
                    this.mEndTime = this.mStartTime;
                    this.mWXSDKInstance.getApmForInstance().onStageWithTime(KEY_STAGE_JS_ASYNC_DATA_START, this.mStartTime);
                }
                this.mEndTime += j;
                this.mWXSDKInstance.getApmForInstance().onStageWithTime(KEY_STAGE_JS_ASYNC_DATA_END, this.mEndTime);
                if (map.containsKey(KEY_PROPERTIES_PREFETCH_SUCCESS)) {
                    this.mWXSDKInstance.getApmForInstance().addProperty(KEY_PROPERTIES_PREFETCH_SUCCESS, Integer.valueOf("true".equalsIgnoreCase(map.get(KEY_PROPERTIES_PREFETCH_SUCCESS)) ? 1 : 0));
                }
            }
        }
    }

    @JSMethod
    public void recordInteractionTime() {
        if (this.mWXSDKInstance != null) {
            this.mWXSDKInstance.getApmForInstance().forceStopRecordInteraction = true;
            this.mWXSDKInstance.getApmForInstance().onStage(WXInstanceApm.KEY_PAGE_STAGES_INTERACTION);
        }
    }

    @JSMethod
    public void onStage(String str) {
        if (this.mWXSDKInstance != null && !TextUtils.isEmpty(str)) {
            this.mWXSDKInstance.getApmForInstance().onStage(str);
        }
    }

    @JSMethod
    public void setProperty(String str, String str2) {
        if (this.mWXSDKInstance != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            this.mWXSDKInstance.getApmForInstance().addProperty(str, str2);
        }
    }

    @JSMethod
    public void setStats(String str, double d) {
        if (this.mWXSDKInstance != null) {
            this.mWXSDKInstance.getApmForInstance().addStats(str, d);
        }
    }

    @JSMethod
    public void showPerformanceInoInRelease(Boolean bool) {
        if (bool != null) {
            WXAPMAdapter.showPerformanceInRelease = bool.booleanValue();
        }
    }
}
