package com.alibaba.ut.abtest.track;

import android.text.TextUtils;
import com.alibaba.motu.tbrest.SendService;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.util.Analytics;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.StringUtils;
import com.alibaba.ut.abtest.internal.util.TrackUtils;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTPageHitHelper;
import com.ut.mini.module.plugin.UTPlugin;
import java.util.HashMap;
import java.util.Map;

public class TrackUTPlugin extends UTPlugin implements UTPageHitHelper.PageChangeListener {
    private static final String TAG = "TrackUTPlugin";
    public static final String UT_PARAM = "utparam-cnt";
    private String currentPageObjectKey;

    public int[] getAttentionEventIds() {
        return ABContext.getInstance().getTrackService().getSubscribeUTEventIds();
    }

    public Map<String, String> onEventDispatch(String str, int i, String str2, String str3, String str4, Map<String, String> map) {
        int i2 = i;
        if (!ABContext.getInstance().getConfigService().isTrackAutoEnabled()) {
            return null;
        }
        long nanoTime = System.nanoTime();
        HashMap hashMap = new HashMap();
        try {
            TrackId trackId = ABContext.getInstance().getTrackService().getTrackId(str, i, str2, str3, str4, map, this.currentPageObjectKey);
            if (trackId != null) {
                String trackUtParam = ABContext.getInstance().getTrackService().getTrackUtParam(trackId, i, map);
                hashMap.put(UT_PARAM, trackUtParam);
                if (i2 != 2101) {
                    if (i2 != 2201) {
                        if (i2 == 2001) {
                            UTAnalytics.getInstance().getDefaultTracker().updateNextPageUtparam(trackUtParam);
                            try {
                                try {
                                    SendService.getInstance().aliabPage = str;
                                    SendService.getInstance().aliabTest = trackUtParam;
                                } catch (Throwable unused) {
                                }
                            } catch (Throwable unused2) {
                            }
                            LogUtils.logD(TAG, "track, pageName=" + StringUtils.nullToEmpty(str) + ", eventId=" + i + ", currentPageObjectKey=" + this.currentPageObjectKey + ", uttrack=" + hashMap.toString());
                        }
                    }
                }
                String str5 = str;
                LogUtils.logD(TAG, "track, pageName=" + StringUtils.nullToEmpty(str) + ", eventId=" + i + ", currentPageObjectKey=" + this.currentPageObjectKey + ", uttrack=" + hashMap.toString());
            }
            Analytics.commitTrackStat(trackId != null, System.nanoTime() - nanoTime);
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
        }
        return hashMap;
    }

    public void onPageAppear(Object obj) {
        this.currentPageObjectKey = TrackUtils.generateUTPageObjectKey(obj);
    }

    public void onPageDisAppear(Object obj) {
        if (!TextUtils.isEmpty(this.currentPageObjectKey) && TextUtils.equals(TrackUtils.generateUTPageObjectKey(obj), this.currentPageObjectKey)) {
            this.currentPageObjectKey = null;
        }
    }

    public static void register() {
        try {
            TrackUTPlugin trackUTPlugin = new TrackUTPlugin();
            UTAnalytics.getInstance().registerPlugin(trackUTPlugin);
            UTPageHitHelper.addPageChangerListener(trackUTPlugin);
        } catch (Throwable th) {
            LogUtils.logE(TAG, "UT插件注册失败", th);
        }
    }
}
