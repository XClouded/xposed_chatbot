package com.alimama.moon.network;

public class MtopException extends Exception {
    private String retCode;
    private String retMsg;

    public MtopException(String str, String str2) {
        super(str2);
        this.retCode = str;
        this.retMsg = str2;
    }

    public String getRetCode() {
        return this.retCode;
    }

    public String getRetMsg() {
        return this.retMsg;
    }
}
