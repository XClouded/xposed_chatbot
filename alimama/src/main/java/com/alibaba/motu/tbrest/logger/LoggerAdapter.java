package com.alibaba.motu.tbrest.logger;

import com.taobao.tao.log.TLog;
import java.util.Map;
import org.json.JSONObject;

public class LoggerAdapter {
    public static void log(String str, Object... objArr) {
        try {
            TLog.loge("tbrest", str, format2String(objArr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String format2String(Object... objArr) {
        String str;
        if (objArr == null || objArr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map map : objArr) {
            if (map != null) {
                if (map instanceof Map) {
                    str = map2Json(map);
                } else {
                    str = map.toString();
                }
                sb.append("->");
                sb.append(str);
            }
        }
        return sb.toString();
    }

    private static String map2Json(Map map) {
        return new JSONObject(map).toString();
    }
}
