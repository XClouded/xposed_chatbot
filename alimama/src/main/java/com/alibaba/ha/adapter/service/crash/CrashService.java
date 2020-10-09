package com.alibaba.ha.adapter.service.crash;

import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import com.alibaba.motu.crashreporter.MotuCrashReporter;

public class CrashService {
    public void addJavaCrashListener(JavaCrashListener javaCrashListener) {
        MotuCrashReporter.getInstance().setCrashCaughtListener((IUTCrashCaughtListener) new JavaCrashListenerAdapter(javaCrashListener));
    }

    public void updateApppVersion(String str) {
        MotuCrashReporter.getInstance().setAppVersion(str);
    }

    public void updateUserNick(String str) {
        MotuCrashReporter.getInstance().setUserNick(str);
    }

    public void updateChannel(String str) {
        MotuCrashReporter.getInstance().setTTid(str);
    }

    public void addNativeHeaderInfo(String str, String str2) {
        MotuCrashReporter.getInstance().addNativeHeaderInfo(str, str2);
    }
}
