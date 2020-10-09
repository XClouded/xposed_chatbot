package com.alibaba.aliweex.interceptor;

import alimama.com.unweventparse.constants.EventConstants;
import android.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InspectCommon {
    protected List<Pair<String, String>> headers = new ArrayList();
    protected Map<String, Object> payload = new HashMap();

    private String nonNull(String str) {
        return str == null ? "NULL" : str;
    }

    public InspectCommon() {
        this.payload.put(EventConstants.Mtop.HEADERS, this.headers);
    }

    public Map<String, Object> getData() {
        return this.payload;
    }

    public void addHeader(String str, String str2) {
        this.headers.add(new Pair(nonNull(str), strip(str2)));
    }

    public void setRequestId(String str) {
        this.payload.put("requestId", str);
    }

    public void setUrl(String str) {
        this.payload.put("url", str);
    }

    public String firstHeaderValue(String str) {
        for (Pair next : this.headers) {
            if (next.first != null && ((String) next.first).equalsIgnoreCase(str)) {
                return (String) next.second;
            }
        }
        return null;
    }

    public String contentType() {
        String firstHeaderValue = firstHeaderValue("Content-Type");
        return firstHeaderValue == null ? "text/plain" : firstHeaderValue;
    }

    public String contentEncoding() {
        return firstHeaderValue("Content-Encoding");
    }

    public int contentLength() {
        String firstHeaderValue = firstHeaderValue("Content-Length");
        if (firstHeaderValue == null) {
            return -1;
        }
        try {
            return Integer.parseInt(firstHeaderValue);
        } catch (NumberFormatException unused) {
            return -1;
        }
    }

    private String strip(String str) {
        return str != null ? str.replaceFirst("\\[", "").replaceFirst("\\]", "") : str;
    }
}
