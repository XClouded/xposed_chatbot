package com.alibaba.ut.abtest.pipeline.request;

import com.alibaba.ut.abtest.internal.util.JsonUtil;
import java.util.List;
import java.util.Map;

public class RequestParam {
    private Object value;

    private RequestParam(List<Object> list) {
        this.value = list;
    }

    private RequestParam(Map<String, Object> map) {
        this.value = map;
    }

    public static RequestParam create(List<Object> list) {
        return new RequestParam(list);
    }

    public static RequestParam create(Map<String, Object> map) {
        return new RequestParam(map);
    }

    public Object getValue() {
        return this.value;
    }

    public String toString() {
        try {
            return super.toString() + JsonUtil.toJson(this.value);
        } catch (Exception unused) {
            return super.toString();
        }
    }
}
