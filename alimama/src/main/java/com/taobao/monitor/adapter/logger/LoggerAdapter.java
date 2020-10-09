package com.taobao.monitor.adapter.logger;

import com.taobao.monitor.common.ThreadUtils;
import com.taobao.monitor.impl.logger.IDataLogger;
import com.taobao.tao.log.TLog;
import java.util.Map;
import org.json.JSONObject;

public class LoggerAdapter implements IDataLogger {
    public void log(final String str, final Object... objArr) {
        ThreadUtils.start(new Runnable() {
            public void run() {
                try {
                    TLog.loge("apm", str, LoggerAdapter.this.format2String(objArr));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public String format2String(Object... objArr) {
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

    private String map2Json(Map map) {
        return new JSONObject(map).toString();
    }
}
