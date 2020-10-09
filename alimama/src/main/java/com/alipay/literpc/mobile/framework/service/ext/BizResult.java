package com.alipay.literpc.mobile.framework.service.ext;

public class BizResult {
    public String appName;
    public String message;
    public int resultCode;
    public boolean success;

    public void setSuccess(boolean z) {
        this.success = z;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setResultCode(int i) {
        this.resultCode = i;
    }

    public int getResultCode() {
        return this.resultCode;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public String getAppName() {
        return this.appName;
    }
}
