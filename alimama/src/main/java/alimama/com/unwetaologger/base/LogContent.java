package alimama.com.unwetaologger.base;

import alimama.com.unwrouter.UNWRouter;
import com.ut.mini.UTPageHitHelper;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class LogContent {
    public static final String LOG_KEY_BUSINESS = "business";
    public static final String LOG_KEY_ERROR = "error";
    public static final String LOG_KEY_ERROR_CODE = "errorCode";
    public static final String LOG_KEY_MSG = "msg";
    public static final String LOG_KEY_NAME = "name";
    public static final String LOG_KEY_POINT = "point";
    public static final String LOG_KEY_SOURCE = "source";
    public static final String LOG_KEY_SUBTYPE = "subType";
    public static final String LOG_KEY_TRACEINFO = "traceInfo";
    public static final String LOG_KEY_TYPE = "type";
    public static final String LOG_VALUE_SOURCE_DEFAULT = "app";
    private Map<String, String> allInfoMap = new HashMap();
    private Map<String, String> infoMap = new HashMap();
    private String msg;
    private String name;
    private String point;
    private String subType = "native";
    private UNWLogTracer tracer = new UNWLogTracer();
    private String type = UNWLogger.LOG_VALUE_TYPE_PROCESS;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPoint() {
        return this.point;
    }

    public void setPoint(String str) {
        this.point = str;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getSubType() {
        return this.subType;
    }

    public void setSubType(String str) {
        this.subType = str;
    }

    public UNWLogTracer getTracer() {
        return this.tracer;
    }

    public void setTracer(UNWLogTracer uNWLogTracer) {
        this.tracer = uNWLogTracer;
    }

    public synchronized Map<String, String> getNewAllInfoMap() {
        HashMap hashMap;
        hashMap = new HashMap();
        hashMap.putAll(this.allInfoMap);
        return hashMap;
    }

    public synchronized void processInfoMap() {
        this.allInfoMap.put("source", LOG_VALUE_SOURCE_DEFAULT);
        this.allInfoMap.put(LOG_KEY_BUSINESS, UNWLoggerManager.getInstance().getBusiness() != null ? UNWLoggerManager.getInstance().getBusiness() : "");
        this.allInfoMap.put("msg", getMsg() != null ? getMsg() : "");
        this.allInfoMap.put("type", getType() != null ? getType() : UNWLogger.LOG_VALUE_TYPE_PROCESS);
        this.allInfoMap.put("subType", getSubType() != null ? getSubType() : "native");
        this.allInfoMap.put("name", getName() != null ? getName() : "");
        this.allInfoMap.put("point", getPoint() != null ? getPoint() : "");
        String currentPageName = UTPageHitHelper.getInstance().getCurrentPageName();
        if (currentPageName != null) {
            this.infoMap.put(UNWRouter.PAGE_NAME, currentPageName);
        }
        this.allInfoMap.putAll(this.infoMap);
        if (getTracer().getStepList().size() > 0) {
            this.infoMap.put(LOG_KEY_TRACEINFO, getTracer().getStepList().toString());
        }
    }

    public synchronized String getInfoMapStr() {
        if (this.infoMap != null) {
            if (this.infoMap.size() != 0) {
                JSONObject jSONObject = new JSONObject();
                try {
                    for (String next : this.infoMap.keySet()) {
                        jSONObject.put(next, this.infoMap.get(next));
                    }
                    return jSONObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "";
                }
            }
        }
        return "";
    }

    public synchronized String getAllInfoMapStr() {
        if (this.allInfoMap != null) {
            if (this.allInfoMap.size() != 0) {
                JSONObject jSONObject = new JSONObject();
                try {
                    for (String next : this.allInfoMap.keySet()) {
                        jSONObject.put(next, this.allInfoMap.get(next));
                    }
                    return this.allInfoMap.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "";
                }
            }
        }
        return "";
    }

    public synchronized void clearInfoMap() {
        this.infoMap.clear();
    }

    public synchronized void setInfoMap(Map<String, String> map) {
        if (this.infoMap == null) {
            this.infoMap = new HashMap();
        }
        this.infoMap.putAll(map);
    }

    public void setError(String str) {
        addInfoItem("error", "1");
        addInfoItem("errorCode", str);
    }

    public synchronized void addInfoItem(String str, String str2) {
        this.infoMap.put(str, str2);
    }
}
