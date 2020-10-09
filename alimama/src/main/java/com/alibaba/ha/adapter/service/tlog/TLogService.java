package com.alibaba.ha.adapter.service.tlog;

import com.taobao.tao.log.TLog;
import com.taobao.tao.log.TLogInitializer;

public class TLogService {
    private static boolean isValid = false;

    static {
        try {
            Class.forName("com.taobao.tao.log.TLog");
            isValid = true;
        } catch (ClassNotFoundException unused) {
            isValid = false;
        }
    }

    public void OpenDebug(Boolean bool) {
        if (isValid) {
            TLogInitializer.getInstance().setDebugMode(bool.booleanValue());
        }
    }

    public void logv(String str, String str2, String str3) {
        if (isValid) {
            TLog.logv(str, str2, str3);
        }
    }

    public void logd(String str, String str2, String str3) {
        if (isValid) {
            TLog.logd(str, str2, str3);
        }
    }

    public void logi(String str, String str2, String str3) {
        if (isValid) {
            TLog.logi(str, str2, str3);
        }
    }

    public void logw(String str, String str2, String str3) {
        if (isValid) {
            TLog.logw(str, str2, str3);
        }
    }

    public void logw(String str, String str2, Throwable th) {
        if (isValid) {
            TLog.logw(str, str2, th);
        }
    }

    public void loge(String str, String str2, String str3) {
        if (isValid) {
            TLog.loge(str, str2, str3);
        }
    }

    public void loge(String str, String str2, Throwable th) {
        if (isValid) {
            TLog.loge(str, str2, th);
        }
    }

    public void traceLog(String str, String str2) {
        if (isValid) {
            TLog.traceLog(str, str2);
        }
    }

    public void updateLogLevel(TLogLevel tLogLevel) {
        if (isValid) {
            TLogInitializer.getInstance().updateLogLevel(tLogLevel.name());
        }
    }

    public void changeRemoteDebugHost(String str) {
        if (str != null) {
            TLogInitializer.getInstance().messageHostName = str;
        }
    }

    public void changeRemoteDebugOssBucket(String str) {
        if (str != null) {
            TLogInitializer.getInstance().ossBucketName = str;
        }
    }

    public void changeAccsServiceId(String str) {
        if (str != null) {
            TLogInitializer.getInstance().accsServiceId = str;
        }
    }

    public void changeAccsTag(String str) {
        if (str != null) {
            TLogInitializer.getInstance().accsTag = str;
        }
    }

    public void changeRasPublishKey(String str) {
        if (str != null) {
            TLogInitializer.getInstance().changeRsaPublishKey(str);
        }
    }
}
