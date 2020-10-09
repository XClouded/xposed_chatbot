package com.alibaba.ut.abtest.internal.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private static final String TAG = "JsonUtil";

    public static <T> String toJson(T t) {
        return JSON.toJSONString(t);
    }

    public static String toJson(Map<String, ?> map) {
        return JSON.toJSONString(map);
    }

    public static <T> String toJson(List<T> list) {
        return JSON.toJSONString(list);
    }

    public static <T> T fromJson(String str, Class<T> cls) {
        try {
            return JSON.parseObject(str, cls);
        } catch (Exception e) {
            LogUtils.logE(TAG, "json can not convert to " + cls.getName(), e);
            return null;
        }
    }

    public static <T> T fromJson(String str, Type type) {
        try {
            return JSON.parseObject(str, type, new Feature[0]);
        } catch (Exception e) {
            LogUtils.logE(TAG, "json can not convert to " + type.getClass().getName(), e);
            return null;
        }
    }
}
