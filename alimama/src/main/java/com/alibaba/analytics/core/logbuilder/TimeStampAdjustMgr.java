package com.alibaba.analytics.core.logbuilder;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.HttpUtils;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.SpSetting;
import com.alibaba.analytics.utils.TaskExecutor;
import com.taobao.weex.analyzer.core.NetworkEventSender;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import org.json.JSONObject;

public class TimeStampAdjustMgr {
    public static final String TAG_TIME_ADJUST_HOST_PORT = "time_adjust_host";
    private static TimeStampAdjustMgr instance = new TimeStampAdjustMgr();
    /* access modifiers changed from: private */
    public String defaultHost = "acs.m.taobao.com";
    private String defaultUrl = "http://acs.m.taobao.com/gw/mtop.common.getTimestamp/*";
    /* access modifiers changed from: private */
    public long diff = 0;
    /* access modifiers changed from: private */
    public boolean flag = false;
    /* access modifiers changed from: private */
    public String scheme = "http://";
    /* access modifiers changed from: private */
    public String urlFile = "/gw/mtop.common.getTimestamp/*";

    public static TimeStampAdjustMgr getInstance() {
        return instance;
    }

    public void startSync() {
        TaskExecutor.getInstance().schedule((ScheduledFuture) null, new Runnable() {
            public void run() {
                long currentTimeMillis = System.currentTimeMillis();
                String access$000 = TimeStampAdjustMgr.this.defaultHost;
                String str = SpSetting.get(Variables.getInstance().getContext(), TimeStampAdjustMgr.TAG_TIME_ADJUST_HOST_PORT);
                if (!TextUtils.isEmpty(str)) {
                    access$000 = str;
                }
                String str2 = TimeStampAdjustMgr.this.scheme + access$000 + TimeStampAdjustMgr.this.urlFile;
                HttpUtils.HttpResponse sendRequest = HttpUtils.sendRequest(1, str2, (Map<String, Object>) null, false);
                Logger.d("TimeStampAdjustMgr", "url", str2, NetworkEventSender.TYPE_RESPONSE, sendRequest);
                if (sendRequest != null && sendRequest.data != null) {
                    try {
                        JSONObject optJSONObject = new JSONObject(new String(sendRequest.data, 0, sendRequest.data.length)).optJSONObject("data");
                        if (optJSONObject != null) {
                            String optString = optJSONObject.optString("t");
                            if (!TextUtils.isEmpty(optString)) {
                                try {
                                    long unused = TimeStampAdjustMgr.this.diff = Long.parseLong(optString) - currentTimeMillis;
                                    boolean unused2 = TimeStampAdjustMgr.this.flag = true;
                                    Logger.d("TimeStampAdjustMgr", "t", optString, "now", Long.valueOf(currentTimeMillis), "diff", Long.valueOf(TimeStampAdjustMgr.this.diff));
                                } catch (Throwable unused3) {
                                }
                            }
                        }
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
            }
        }, 0);
    }

    public long getCurrentMils() {
        return System.currentTimeMillis() + this.diff;
    }

    public long getCurrentMils(String str) {
        long j;
        try {
            j = Long.parseLong(str);
        } catch (Exception e) {
            Logger.d("TimeStampAdjustMgr", e);
            j = 0;
        }
        if (j == 0) {
            j = System.currentTimeMillis();
        }
        return j + this.diff;
    }

    public boolean getAdjustFlag() {
        return this.flag;
    }
}
