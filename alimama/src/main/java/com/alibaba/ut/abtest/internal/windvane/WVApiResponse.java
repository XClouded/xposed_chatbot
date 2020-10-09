package com.alibaba.ut.abtest.internal.windvane;

import android.taobao.windvane.jsbridge.WVResult;
import android.text.TextUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.ut.abtest.internal.util.JsonUtil;
import java.io.Serializable;

public class WVApiResponse<T> implements Serializable {
    @JSONField(name = "code")
    private int code;
    @JSONField(name = "data")
    private T data;
    @JSONField(name = "message")
    private String message;
    @JSONField(name = "ret")
    private String ret;

    public WVApiResponse() {
        this.code = 0;
    }

    public WVApiResponse(int i) {
        this.code = 0;
        this.code = i;
    }

    public WVApiResponse(int i, String str) {
        this.code = 0;
        this.code = i;
        this.message = str;
    }

    public WVApiResponse(T t) {
        this.code = 0;
        this.code = 0;
        this.data = t;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T t) {
        this.data = t;
    }

    public String getRet() {
        return this.ret;
    }

    public void setRet(String str) {
        this.ret = str;
    }

    public String toJsonString() {
        if (TextUtils.isEmpty(this.ret)) {
            this.ret = WVResult.SUCCESS;
        }
        return JsonUtil.toJson(this);
    }
}
