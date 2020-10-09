package com.taobao.windvane.extra.ut;

import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.text.TextUtils;
import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class UTCrashCaughtListner implements IUTCrashCaughtListener {
    public static String wv_currentStatus = "0";
    /* access modifiers changed from: private */
    public LinkedList mUrlList = null;
    /* access modifiers changed from: private */
    public String wv_currentUrl = "";

    public UTCrashCaughtListner() {
        init();
    }

    public class PageStartWVEventListener implements WVEventListener {
        public PageStartWVEventListener() {
        }

        public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
            if (i != 1001) {
                switch (i) {
                    case 3001:
                    case 3003:
                        UTCrashCaughtListner.wv_currentStatus = "1";
                        return null;
                    case 3002:
                        UTCrashCaughtListner.wv_currentStatus = "0";
                        return null;
                    default:
                        return null;
                }
            } else {
                if (!(wVEventContext == null || wVEventContext.url == null)) {
                    String str = wVEventContext.url;
                    if (UTCrashCaughtListner.this.mUrlList != null) {
                        if (UTCrashCaughtListner.this.mUrlList.size() > 9) {
                            UTCrashCaughtListner.this.mUrlList.removeFirst();
                        }
                        UTCrashCaughtListner.this.mUrlList.addLast(str);
                    }
                    String unused = UTCrashCaughtListner.this.wv_currentUrl = str;
                    TaoLog.v("WV_URL_CHANGE", "current Url : " + str);
                }
                UTCrashCaughtListner.wv_currentStatus = "2";
                return null;
            }
        }
    }

    private void init() {
        this.mUrlList = new LinkedList();
        WVEventService.getInstance().addEventListener(new PageStartWVEventListener());
    }

    public Map<String, Object> onCrashCaught(Thread thread, Throwable th) {
        int size = this.mUrlList.size();
        if (this.mUrlList == null || size < 1) {
            return null;
        }
        for (int i = 3; i < size; i++) {
            String str = (String) this.mUrlList.get(i);
            if (!TextUtils.isEmpty(str)) {
                this.mUrlList.set(i, WVUrlUtil.removeQueryParam(str));
            }
        }
        HashMap hashMap = new HashMap();
        hashMap.put("crash_url_list", this.mUrlList.toString());
        hashMap.put("wv_currentUrl", this.wv_currentUrl);
        hashMap.put("wv_currentStatus", wv_currentStatus);
        return hashMap;
    }
}
