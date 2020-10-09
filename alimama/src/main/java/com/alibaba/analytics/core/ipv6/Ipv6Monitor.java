package com.alibaba.analytics.core.ipv6;

import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.core.store.LogStoreMgr;
import java.util.HashMap;

class Ipv6Monitor {
    private static final String EVENT_ID = "19999";
    private static final String PAGE_NAME = "UT_ANALYTICS";
    private static boolean bSendIpv6Init = false;
    private static int mIpStack;

    Ipv6Monitor() {
    }

    public static void setIpStack(int i) {
        mIpStack = i;
    }

    public static void sendIpv6DetectEvent(long j) {
        HashMap hashMap = new HashMap();
        hashMap.put("type", "" + mIpStack);
        hashMap.put("time", "" + j);
        LogStoreMgr.getInstance().add(new Log(PAGE_NAME, EVENT_ID, "IPV6_DETECT", "", "", hashMap));
    }

    public static void sendIpv6Init(boolean z, int i, long j) {
        if (!bSendIpv6Init) {
            bSendIpv6Init = true;
            HashMap hashMap = new HashMap();
            if (z) {
                hashMap.put("success", "1");
            } else {
                hashMap.put("success", "0");
                hashMap.put("errorCode", "" + i);
            }
            hashMap.put("time", "" + j);
            hashMap.put("type", "" + mIpStack);
            LogStoreMgr.getInstance().add(new Log(PAGE_NAME, EVENT_ID, "IPV6_INIT", "", "", hashMap));
        }
    }

    public static void sendIpv6Error(int i, long j) {
        HashMap hashMap = new HashMap();
        hashMap.put("errorCode", "" + i);
        hashMap.put("time", "" + j);
        hashMap.put("type", "" + mIpStack);
        LogStoreMgr.getInstance().add(new Log(PAGE_NAME, EVENT_ID, "IPV6_ERROR", "", "", hashMap));
    }
}
