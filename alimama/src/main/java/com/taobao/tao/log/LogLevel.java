package com.taobao.tao.log;

import com.ali.user.mobile.rpc.ApiConstants;

public enum LogLevel {
    ALL("ALL", 0),
    V("V", 0),
    D("D", 1),
    I("I", 2),
    W("W", 3),
    E("E", 4),
    F(ApiConstants.UTConstants.UT_SUCCESS_F, 5),
    N("N", 6),
    L("L", 6);
    
    private int index;
    private String name;

    private LogLevel(String str, int i) {
        this.name = str;
        this.index = i;
    }

    public String getName() {
        return this.name;
    }

    /* access modifiers changed from: protected */
    public int getIndex() {
        return this.index;
    }

    protected static String getName(int i) {
        for (LogLevel logLevel : values()) {
            if (logLevel.getIndex() == i) {
                return logLevel.name;
            }
        }
        return null;
    }
}
