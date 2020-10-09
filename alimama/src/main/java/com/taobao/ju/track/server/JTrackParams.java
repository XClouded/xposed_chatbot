package com.taobao.ju.track.server;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JTrackParams extends HashMap<String, String> {
    private static final String KEY_SPLIT = ",";
    public static final String TRACK_PARAMS = "trackParams";
    public static final String TRACK_PARAMS_CLICK = "_click";
    public static final String TRACK_PARAMS_DETAIL_EXPOSE = "_detailExpose";
    public static final String TRACK_PARAMS_DOUBLE_CLICK = "_doubleClick";
    public static final String TRACK_PARAMS_EXPOSE = "_expose";
    public static final String TRACK_PARAMS_ID = "_trackId";
    public static final String TRACK_PARAMS_NAME = "_trackName";

    public static JTrackParams addAll(JTrackParams jTrackParams, JTrackParams jTrackParams2) {
        if (jTrackParams == null) {
            return jTrackParams2;
        }
        if (jTrackParams2 != null) {
            jTrackParams.putAll(jTrackParams2);
        }
        return jTrackParams;
    }

    public static JTrackParams create(JSONObject jSONObject) {
        if (jSONObject == null || !jSONObject.containsKey(TRACK_PARAMS)) {
            return null;
        }
        return create(jSONObject.getString(TRACK_PARAMS));
    }

    public static JTrackParams create(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                return (JTrackParams) JSON.parseObject(str, JTrackParams.class);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JTrackParams create(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        try {
            JTrackParams jTrackParams = new JTrackParams();
            jTrackParams.putAll(map);
            return jTrackParams;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JTrackParams createFromObjMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        try {
            JTrackParams jTrackParams = new JTrackParams();
            for (String next : map.keySet()) {
                jTrackParams.put(next, String.valueOf(map.get(next)));
            }
            return jTrackParams;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setFields(String str, String str2) {
        put(str, str2);
    }

    public String getAllKeys() {
        StringBuffer stringBuffer = new StringBuffer();
        Set<String> keySet = keySet();
        if (keySet != null) {
            for (String str : keySet) {
                if (str != null && !str.startsWith("_")) {
                    stringBuffer.append(str);
                    stringBuffer.append(",");
                }
            }
        }
        if (stringBuffer.length() > 1) {
            stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    public static Map<String, String> getClickParams(JTrackParams jTrackParams) {
        Map<String, String> params = getParams(jTrackParams, TRACK_PARAMS_CLICK);
        String trackId = getTrackId(jTrackParams);
        String trackName = getTrackName(jTrackParams);
        if (!(trackId == null && trackName == null)) {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put(TRACK_PARAMS_ID, trackId);
            params.put(TRACK_PARAMS_NAME, trackName);
        }
        return params;
    }

    public static Map<String, String> getDoubleClickParams(JTrackParams jTrackParams) {
        Map<String, String> params = getParams(jTrackParams, TRACK_PARAMS_DOUBLE_CLICK);
        String trackId = getTrackId(jTrackParams);
        String trackName = getTrackName(jTrackParams);
        if (!(trackId == null && trackName == null)) {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put(TRACK_PARAMS_ID, trackId);
            params.put(TRACK_PARAMS_NAME, trackName);
        }
        return params;
    }

    public static Map<String, String> getExposeParams(JTrackParams jTrackParams) {
        return getParams(jTrackParams, TRACK_PARAMS_EXPOSE);
    }

    public static Map<String, String> getDetailExposeParams(JTrackParams jTrackParams) {
        return getParams(jTrackParams, TRACK_PARAMS_DETAIL_EXPOSE);
    }

    public static Serializable getSerializableDetailExposeParams(JTrackParams jTrackParams) {
        return (Serializable) getDetailExposeParams(jTrackParams);
    }

    public static String getTrackId(JTrackParams jTrackParams) {
        return getParam(jTrackParams, TRACK_PARAMS_ID);
    }

    public static String getTrackName(JTrackParams jTrackParams) {
        return getParam(jTrackParams, TRACK_PARAMS_NAME);
    }

    private static Map<String, String> getParams(JTrackParams jTrackParams, String str) {
        if (jTrackParams == null || !jTrackParams.containsKey(str)) {
            return null;
        }
        String str2 = (String) jTrackParams.get(str);
        if (!TextUtils.isEmpty(str2)) {
            return getParams(jTrackParams, str2.split(","));
        }
        return null;
    }

    private static Map<String, String> getParams(JTrackParams jTrackParams, String[] strArr) {
        if (jTrackParams == null || strArr == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        for (String trim : strArr) {
            String trim2 = trim.trim();
            if (jTrackParams.containsKey(trim2)) {
                hashMap.put(trim2, jTrackParams.get(trim2));
            }
        }
        return hashMap;
    }

    private static String getParam(JTrackParams jTrackParams, String str) {
        if (jTrackParams == null || !jTrackParams.containsKey(str)) {
            return null;
        }
        return (String) jTrackParams.get(str);
    }
}
