package com.taobao.weex.devtools.inspector.network.utils;

import java.util.Map;

public class ExtractUtil {
    public static <T> T getValue(Map<String, Object> map, String str, T t) {
        if (!(str == null || map.get(str) == null)) {
            try {
                return map.get(str);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return t;
    }
}
