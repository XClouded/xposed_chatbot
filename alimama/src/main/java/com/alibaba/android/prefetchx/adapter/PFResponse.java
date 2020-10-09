package com.alibaba.android.prefetchx.adapter;

import com.taobao.weex.common.WXResponse;
import java.util.Map;

public class PFResponse {
    public String data;
    public String errorCode;
    public String errorMsg;
    public Map<String, Object> extendParams;
    public byte[] originalData;
    public String statusCode;
    public String toastMsg;

    public PFResponse() {
    }

    public PFResponse(WXResponse wXResponse) {
        this.statusCode = wXResponse.statusCode;
        this.data = wXResponse.data;
        this.originalData = wXResponse.originalData;
        this.errorCode = wXResponse.errorCode;
        this.errorMsg = wXResponse.errorMsg;
        this.toastMsg = wXResponse.toastMsg;
        this.extendParams = wXResponse.extendParams;
    }

    public String toSimpleString() {
        StringBuilder sb = new StringBuilder();
        sb.append("statusCode:");
        sb.append(this.statusCode);
        sb.append(", errorCode:");
        sb.append(this.errorCode);
        sb.append(", errorMsg:");
        sb.append(this.errorMsg);
        sb.append(", toastMsg:");
        sb.append(this.toastMsg);
        sb.append(", extendParams:");
        sb.append(this.extendParams);
        if (this.data != null) {
            sb.append(", dataSize:");
            sb.append(this.data.length());
        }
        if (this.originalData != null) {
            sb.append(", originalDataSize:");
            sb.append(this.originalData.length);
        }
        return sb.toString();
    }
}
