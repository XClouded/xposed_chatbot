package com.alibaba.aliweex.interceptor;

import alimama.com.unweventparse.constants.EventConstants;
import java.util.Map;

public class InspectResponse extends InspectCommon {
    public InspectResponse() {
        this.payload.put(EventConstants.Mtop.HEADERS, this.headers);
    }

    public Map<String, Object> getData() {
        return this.payload;
    }

    public void setConnectionId(int i) {
        this.payload.put("connectionId", Integer.valueOf(i));
    }

    public void setConnectionReused(boolean z) {
        this.payload.put("connectionReused", Boolean.valueOf(z));
    }

    public void setFromDiskCache(boolean z) {
        this.payload.put("fromDiskCache", Boolean.valueOf(z));
    }

    public void setStatusCode(int i) {
        this.payload.put("statusCode", Integer.valueOf(i));
    }

    public void setReasonPhrase(String str) {
        this.payload.put("reasonPhrase", str);
    }

    public void setTiming(Map<String, Object> map) {
        this.payload.put("timing", map);
    }
}
