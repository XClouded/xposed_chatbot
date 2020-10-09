package com.taobao.weex.jsEngine;

public class CallBackCode {
    public static final CallBackCode ERROR_JSENGINE_CRASHED = new CallBackCode(-1, "js engine Crashed");
    public static final CallBackCode JSENGINE_INIT_FINISH = new CallBackCode(1, "js Engine init finished");
    public int code;
    public String msg;

    private CallBackCode(int i, String str) {
        this.code = i;
        this.msg = str;
    }
}
