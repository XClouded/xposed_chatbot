package com.ut.mini.internal;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.ipv6.Ipv6ConfigConstant;
import com.alibaba.analytics.core.logbuilder.TimeStampAdjustMgr;
import com.alibaba.analytics.core.sync.HttpsHostPortMgr;
import com.alibaba.analytics.core.sync.TnetHostPortMgr;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.SpSetting;
import com.alibaba.analytics.utils.StringUtils;
import com.ta.utdid2.device.UTDevice;
import com.ut.mini.UTAnalytics;
import com.ut.mini.exposure.ExposureUtils;
import com.ut.mini.exposure.TrackerManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UTTeamWork {
    private static final String TAG = "UTTeamWork";
    private static UTTeamWork s_instance;
    private List<H5JSCallback> callbacks = new ArrayList();

    public interface H5JSCallback {
        void onH5JSCall(Object obj, Map<String, String> map);
    }

    public void initialized() {
    }

    public static synchronized UTTeamWork getInstance() {
        UTTeamWork uTTeamWork;
        synchronized (UTTeamWork.class) {
            if (s_instance == null) {
                s_instance = new UTTeamWork();
            }
            uTTeamWork = s_instance;
        }
        return uTTeamWork;
    }

    public void turnOnRealTimeDebug(Map<String, String> map) {
        Logger.d(TAG, "", map.entrySet().toArray());
        UTAnalytics.getInstance().turnOnRealTimeDebug(map);
    }

    public void turnOffRealTimeDebug() {
        Logger.e();
        UTAnalytics.getInstance().turnOffRealTimeDebug();
    }

    public void dispatchLocalHits() {
        UTAnalytics.getInstance().dispatchLocalHits();
    }

    public void saveCacheDataToLocal() {
        UTAnalytics.getInstance().saveCacheDataToLocal();
    }

    public void setToAliyunOsPlatform() {
        UTAnalytics.getInstance().setToAliyunOsPlatform();
    }

    public void closeAuto1010Track() {
        ClientVariables.getInstance().set1010AutoTrackClose();
    }

    public String getUtsid() {
        try {
            String appKey = ClientVariables.getInstance().getAppKey();
            String utdid = UTDevice.getUtdid(ClientVariables.getInstance().getContext());
            long parseLong = Long.parseLong(AnalyticsMgr.getValue("session_timestamp"));
            if (StringUtils.isEmpty(appKey) || StringUtils.isEmpty(utdid)) {
                return null;
            }
            return utdid + "_" + appKey + "_" + parseLong;
        } catch (Exception e) {
            Logger.w("", e, new Object[0]);
            return null;
        }
    }

    public void setHostPort4Tnet(Context context, String str, int i) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else if (TextUtils.isEmpty(str)) {
            Log.w("UTAnalytics", "host or port is empty");
        } else {
            SpSetting.put(context, TnetHostPortMgr.TAG_TNET_HOST_PORT, str + ":" + i);
        }
    }

    public void clearHostPort4Tnet(Context context) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else {
            SpSetting.put(context, TnetHostPortMgr.TAG_TNET_HOST_PORT, (String) null);
        }
    }

    public void setHostPort4TnetIpv6(Context context, String str, int i) {
        if (context == null) {
            Log.w(TAG, "context is null");
        } else if (TextUtils.isEmpty(str)) {
            Log.w(TAG, "host or port is empty");
        } else {
            SpSetting.put(context, Ipv6ConfigConstant.UTANALYTICS_TNET_HOST_PORT_IPV6, str + ":" + i);
        }
    }

    public void clearHostPort4TnetIpv6(Context context) {
        if (context == null) {
            Log.w(TAG, "context is null");
        } else {
            SpSetting.put(context, Ipv6ConfigConstant.UTANALYTICS_TNET_HOST_PORT_IPV6, (String) null);
        }
    }

    public void setHost4Https(Context context, String str) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else if (TextUtils.isEmpty(str)) {
            Log.w("UTAnalytics", "host or port is empty");
        } else {
            SpSetting.put(context, HttpsHostPortMgr.TAG_HTTPS_HOST_PORT, str);
        }
    }

    public void clearHost4Https(Context context) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else {
            SpSetting.put(context, HttpsHostPortMgr.TAG_HTTPS_HOST_PORT, (String) null);
        }
    }

    public void setHostPort4Http(Context context, String str) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else if (TextUtils.isEmpty(str)) {
            Log.w("UTAnalytics", "host  is empty");
        } else {
            SpSetting.put(context, Constants.UT.TAG_SP_HTTP_TRANSFER_HOST, str);
        }
    }

    public void clearHostPort4Http(Context context) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else {
            SpSetting.put(context, Constants.UT.TAG_SP_HTTP_TRANSFER_HOST, (String) null);
        }
    }

    public void setHost4TimeAdjustService(Context context, String str) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else if (TextUtils.isEmpty(str)) {
            Log.w("UTAnalytics", "host is empty");
        } else {
            SpSetting.put(context, TimeStampAdjustMgr.TAG_TIME_ADJUST_HOST_PORT, str);
        }
    }

    public void clearHost4TimeAdjustService(Context context) {
        if (context == null) {
            Log.w("UTAnalytics", "context =null");
        } else {
            SpSetting.put(context, TimeStampAdjustMgr.TAG_TIME_ADJUST_HOST_PORT, (String) null);
        }
    }

    public void registerExposureViewHandler(ExposureViewHandle exposureViewHandle) {
        TrackerManager.getInstance().registerExposureViewHandler(exposureViewHandle);
    }

    public void unRegisterExposureViewHandler(ExposureViewHandle exposureViewHandle) {
        TrackerManager.getInstance().unRegisterExposureViewHandler(exposureViewHandle);
    }

    public ExposureViewHandle getExposureViewHandler(Activity activity) {
        return TrackerManager.getInstance().getExposureViewHandle();
    }

    public void setExposureTagForWeex(View view) {
        ExposureUtils.setExposureForWeex(view);
    }

    public boolean startExpoTrack(Activity activity) {
        return TrackerManager.getInstance().addToTrack(activity);
    }

    public boolean stopExpoTrack(Activity activity) {
        return TrackerManager.getInstance().removeToTrack(activity);
    }

    public void setIgnoreTagForExposureView(View view) {
        ExposureUtils.setIgnoreTagForExposureView(view);
    }

    public void clearIgnoreTagForExposureView(View view) {
        ExposureUtils.clearIgnoreTagForExposureView(view);
    }

    public void registerH5JSCallback(H5JSCallback h5JSCallback) {
        if (h5JSCallback != null && !this.callbacks.contains(h5JSCallback)) {
            this.callbacks.add(h5JSCallback);
        }
    }

    public void unRegisterH5JSCallback(H5JSCallback h5JSCallback) {
        if (h5JSCallback != null) {
            this.callbacks.remove(h5JSCallback);
        }
    }

    public void dispatchH5JSCall(Object obj, Map<String, String> map) {
        int size = this.callbacks.size();
        for (int i = 0; i < size; i++) {
            this.callbacks.get(i).onH5JSCall(obj, map);
        }
    }
}
