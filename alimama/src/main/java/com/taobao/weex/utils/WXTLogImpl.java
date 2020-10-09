package com.taobao.weex.utils;

import android.os.Process;
import com.ali.user.mobile.rpc.ApiConstants;
import com.taobao.tao.log.LogLevel;
import com.taobao.tao.log.TLog;
import com.taobao.tao.log.TLogController;
import com.taobao.weex.utils.WXLogUtils;

public class WXTLogImpl {
    /* access modifiers changed from: private */
    public static final int sMainPid = Process.myPid();
    private static WXLogUtils.LogWatcher sWXLogWatcher;

    public static void initWXLogWatcher() {
        try {
            if (sWXLogWatcher == null) {
                sWXLogWatcher = new LogWatcherImpl();
            }
            WXLogUtils.setLogWatcher(sWXLogWatcher);
            TLogController.getInstance().addModuleFilter("weex", LogLevel.D);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static class LogWatcherImpl implements WXLogUtils.LogWatcher {
        private LogWatcherImpl() {
        }

        public void onLog(String str, String str2, String str3) {
            try {
                if (WXTLogImpl.sMainPid == Process.myPid()) {
                    char c = 65535;
                    switch (str.hashCode()) {
                        case 118057:
                            if (str.equals("wtf")) {
                                c = 5;
                                break;
                            }
                            break;
                        case 3237038:
                            if (str.equals(ApiConstants.ApiField.INFO)) {
                                c = 0;
                                break;
                            }
                            break;
                        case 3641990:
                            if (str.equals("warn")) {
                                c = 3;
                                break;
                            }
                            break;
                        case 95458899:
                            if (str.equals("debug")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 96784904:
                            if (str.equals("error")) {
                                c = 4;
                                break;
                            }
                            break;
                        case 351107458:
                            if (str.equals("verbose")) {
                                c = 2;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                        case 1:
                            TLog.logi("weex", str2, str3);
                            return;
                        case 2:
                            TLog.logv("weex", str2, str3);
                            return;
                        case 3:
                            TLog.logw("weex", str2, str3);
                            return;
                        case 4:
                        case 5:
                            TLog.loge("weex", str2, str3);
                            break;
                    }
                    TLog.logi("weex", str2 + "-" + str, str3);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
