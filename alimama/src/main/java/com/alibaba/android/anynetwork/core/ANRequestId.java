package com.alibaba.android.anynetwork.core;

public class ANRequestId {
    public Object idObj;
    public String serviceKey = "";

    public ANRequestId() {
    }

    public ANRequestId(Object obj) {
        this.idObj = obj;
    }

    public ANRequestId(String str, Object obj) {
        this.serviceKey = str;
        this.idObj = obj;
    }

    public String toString() {
        return "ANRequestId{serviceKey='" + this.serviceKey + '\'' + ", idObj=" + this.idObj + '}';
    }
}
