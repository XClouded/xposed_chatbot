package com.alibaba.motu.tbrest.rest;

import java.util.Map;

public class RestReqDataBuildResult {
    String mPostUrl;
    Map<String, Object> postReqData;

    public void setReqUrl(String str) {
        this.mPostUrl = str;
    }

    public String getReqUrl() {
        return this.mPostUrl;
    }

    public void setPostReqData(Map<String, Object> map) {
        this.postReqData = map;
    }

    public Map<String, Object> getPostReqData() {
        return this.postReqData;
    }
}
