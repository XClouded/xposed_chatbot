package com.taobao.zcache.network.api;

import java.util.HashMap;
import java.util.Map;

public class ApiRequest {
    private Map<String, String> dataParams = new HashMap();
    private boolean isSec = false;
    private Map<String, String> params = new HashMap();

    public Map<String, String> getParams() {
        return this.params;
    }

    public String getParam(String str) {
        return this.params.get(str);
    }

    public void addParam(String str, String str2) {
        if (str != null && str2 != null) {
            this.params.put(str, str2);
        }
    }

    public void removeParam(String str) {
        this.params.remove(str);
    }

    public Map<String, String> getDataParams() {
        return this.dataParams;
    }

    public String getDataParam(String str) {
        return this.dataParams.get(str);
    }

    public void addDataParams(Map<String, String> map) {
        if (map != null) {
            for (Map.Entry next : map.entrySet()) {
                addDataParam((String) next.getKey(), (String) next.getValue());
            }
        }
    }

    public void addDataParam(String str, String str2) {
        if (str != null && str2 != null) {
            this.dataParams.put(str, str2);
        }
    }

    public boolean isSec() {
        return this.isSec;
    }

    public void setSec(boolean z) {
        this.isSec = z;
    }
}
