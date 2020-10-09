package com.uploader.portal;

import android.util.Log;
import com.taobao.tlog.adapter.AdapterForTLog;
import com.uploader.export.IUploaderLog;
import java.util.HashMap;
import java.util.Map;

public class UploaderLogImpl implements IUploaderLog {
    private static Map<String, Integer> levelMap = new HashMap(6);
    private volatile boolean enableTLog = true;
    private volatile int priority = 31;

    static {
        levelMap.put("V", 31);
        levelMap.put("D", 30);
        levelMap.put("I", 28);
        levelMap.put("W", 24);
        levelMap.put("E", 16);
        levelMap.put("L", 0);
    }

    public void enable(int i) {
        this.priority = i;
    }

    public boolean isEnabled(int i) {
        if (this.enableTLog) {
            return isTLogLevelEnable(i);
        }
        return (i & this.priority) != 0;
    }

    public int print(int i, String str, String str2, Throwable th) {
        if (i != 4) {
            if (i != 8) {
                if (i != 16) {
                    switch (i) {
                        case 1:
                            if (!this.enableTLog) {
                                return Log.v(str, str2);
                            }
                            AdapterForTLog.logv(str, str2);
                            return 0;
                        case 2:
                            if (!this.enableTLog) {
                                return Log.d(str, str2);
                            }
                            AdapterForTLog.logd(str, str2);
                            return 0;
                        default:
                            return 0;
                    }
                } else if (!this.enableTLog) {
                    return Log.e(str, str2, th);
                } else {
                    AdapterForTLog.loge(str, str2, th);
                    return 0;
                }
            } else if (!this.enableTLog) {
                return Log.w(str, str2, th);
            } else {
                AdapterForTLog.logw(str, str2, th);
                return 0;
            }
        } else if (!this.enableTLog) {
            return Log.i(str, str2);
        } else {
            AdapterForTLog.logi(str, str2);
            return 0;
        }
    }

    public void setEnableTLog(boolean z) {
        this.enableTLog = z;
    }

    public int v(String str, String str2) {
        return print(1, str, str2, (Throwable) null);
    }

    public int d(String str, String str2) {
        return print(2, str, str2, (Throwable) null);
    }

    public int i(String str, String str2) {
        return print(4, str, str2, (Throwable) null);
    }

    public int w(String str, String str2) {
        return print(8, str, str2, (Throwable) null);
    }

    public int e(String str, String str2) {
        return print(16, str, str2, (Throwable) null);
    }

    public int w(String str, String str2, Throwable th) {
        return print(8, str, str2, th);
    }

    public int e(String str, String str2, Throwable th) {
        return print(16, str, str2, th);
    }

    private boolean isTLogLevelEnable(int i) {
        int intValue = levelMap.get(AdapterForTLog.getLogLevel()).intValue();
        if (intValue != this.priority) {
            this.priority = intValue;
        }
        return (i & this.priority) != 0;
    }
}
