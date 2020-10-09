package android.taobao.windvane.packageapp.cleanup;

import android.taobao.windvane.util.TaoLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonUtil {
    private static final String TAG = "JsonUtil";

    public static String getJsonString(HashMap<String, InfoSnippet> hashMap) {
        if (hashMap == null) {
            return "{}";
        }
        try {
            JSONObject jSONObject = new JSONObject();
            Set<Map.Entry<String, InfoSnippet>> entrySet = hashMap.entrySet();
            if (entrySet == null) {
                return "{}";
            }
            for (Map.Entry next : entrySet) {
                InfoSnippet infoSnippet = (InfoSnippet) next.getValue();
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("count", infoSnippet.count);
                jSONObject2.put("failCount", infoSnippet.failCount);
                jSONObject2.put("needReinstall", infoSnippet.needReinstall);
                jSONObject2.put("lastAccessTime", infoSnippet.lastAccessTime);
                jSONObject2.put("name", infoSnippet.name);
                jSONObject.put((String) next.getKey(), jSONObject2);
            }
            return jSONObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            TaoLog.e(TAG, "缓存算法模块序列化失败 : " + e.toString());
            return "{}";
        }
    }
}
