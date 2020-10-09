package com.taobao.weex.utils;

import com.ali.user.mobile.rpc.ApiConstants;
import com.taobao.tao.log.TLogConstant;

public enum LogLevel {
    OFF(TLogConstant.TLOG_MODULE_OFF, 8, 7),
    WTF("wtf", 7, 7),
    TLOG("tlog", 6, 6),
    ERROR("error", 5, 6),
    WARN("warn", 4, 5),
    INFO(ApiConstants.ApiField.INFO, 3, 4),
    DEBUG("debug", 2, 3),
    VERBOSE("verbose", 1, 2),
    ALL("all", 0, 2);
    
    String name;
    int priority;
    int value;

    private LogLevel(String str, int i, int i2) {
        this.name = str;
        this.value = i;
        this.priority = i2;
    }

    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public int getPriority() {
        return this.priority;
    }

    public int compare(LogLevel logLevel) {
        return this.value - logLevel.value;
    }
}
