package com.alibaba.aliweex.adapter.module.mtop;

import java.util.HashMap;
import java.util.Map;

public class MtopServerParams {
    public String api;
    private Map<String, String> dataMap = new HashMap();
    public String dataString;
    public String dataType;
    public boolean ecode;
    private Map<String, String> headers = null;
    public boolean post;
    public String sessionOption;
    public long timer;
    public String ttid;
    public String type;
    public String v;
    public int wuaFlag = -1;

    public Map<String, String> getDataMap() {
        return this.dataMap;
    }

    public void addData(String str, String str2) {
        this.dataMap.put(str, str2);
    }

    public void addHeader(String str, String str2) {
        if (this.headers == null) {
            this.headers = new HashMap();
        }
        this.headers.put(str, str2);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }
}
