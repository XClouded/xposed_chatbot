package alimama.com.unwwindvane.etaojsbridge;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unwetaologger.base.UNWLogger;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class H5LogBridge extends WVApiPlugin {
    private static boolean isH5Log = true;

    public static void setIsH5Log(boolean z) {
        isH5Log = z;
    }

    public static void register() {
        WVPluginManager.registerPlugin("EtaoLog", (Class<? extends WVApiPlugin>) H5LogBridge.class);
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (!"print".equals(str) || !isH5Log) {
            return false;
        }
        try {
            JSONObject parseObject = JSONObject.parseObject(str2);
            String string = parseObject.getString("type");
            if (!TextUtils.isEmpty(string)) {
                String str3 = "1000";
                String str4 = "msg is null";
                HashMap hashMap = new HashMap();
                for (String next : parseObject.keySet()) {
                    String string2 = parseObject.getString(next);
                    if ("errorCode".equals(next)) {
                        str3 = string2;
                    } else if ("msg".equals(next)) {
                        str4 = string2;
                    } else {
                        hashMap.put(next, string2);
                    }
                }
                H5Log.logError(str4, str3, hashMap, string);
                return true;
            }
            HashMap hashMap2 = new HashMap();
            for (String next2 : parseObject.keySet()) {
                hashMap2.put(next2, parseObject.getString(next2));
            }
            H5Log.logInfo(hashMap2);
            return true;
        } catch (Throwable th) {
            UNWManager.getInstance().getLogger().error("H5Log", "H5Log", th.getLocalizedMessage());
            return false;
        }
    }

    public static class H5Log {
        public static final String ETAO_H5_LOG = "WVAlarm";
        private static final String sPoint = "WVPrint";

        public static void logInfo(Map<String, String> map) {
            IEtaoLogger logger = UNWManager.getInstance().getLogger();
            String str = "";
            if (map != null && map.containsKey("stage")) {
                str = map.get("stage");
            }
            logger.info(ETAO_H5_LOG, sPoint, str, map);
        }

        public static void logError(String str, String str2, Map<String, String> map, String str3) {
            UNWManager.getInstance().getLogger().error(ETAO_H5_LOG, str3, str, str2, UNWLogger.LOG_VALUE_SUBTYPE_H5, map);
        }
    }
}
