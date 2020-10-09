package com.alibaba.analytics.core.config;

import android.net.Uri;
import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import com.alipay.sdk.sys.a;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class UTTPKBiz {
    private static final String TAG = "UTMCTPKBiz";
    private static UTTPKBiz s_instance;
    private String[] TPK_ONLINECONF_KEY = {"B01N16"};
    private Map<String, String> mTPKCache = new HashMap();
    private List<UTTPKItem> mTPKItems = new LinkedList();
    private String mTPKMD5 = null;

    private UTTPKBiz() {
    }

    public synchronized void addTPKItem(UTTPKItem uTTPKItem) {
        if (uTTPKItem != null) {
            this.mTPKItems.add(uTTPKItem);
        }
    }

    public synchronized void addTPKCache(String str, String str2) {
        if (!StringUtils.isEmpty(str)) {
            if (str2 == null) {
                this.mTPKCache.remove(str);
            } else {
                this.mTPKCache.put(str, str2);
            }
        }
    }

    public static synchronized UTTPKBiz getInstance() {
        UTTPKBiz uTTPKBiz;
        synchronized (UTTPKBiz.class) {
            if (s_instance == null) {
                s_instance = new UTTPKBiz();
            }
            uTTPKBiz = s_instance;
        }
        return uTTPKBiz;
    }

    private void _onTPKConfArrive(String str, String str2) {
        Logger.d(TAG, "", "pConfName", str, "pConfContent", str2);
        if (!StringUtils.isEmpty(str2)) {
            try {
                JSONArray jSONArray = new JSONArray(str2);
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    if (optJSONObject != null && optJSONObject.has("kn") && !optJSONObject.isNull("kn")) {
                        String string = optJSONObject.getString("kn");
                        if (!"a".equals(string)) {
                            UTTPKItem uTTPKItem = new UTTPKItem();
                            String optString = optJSONObject.optString("v");
                            if (StringUtils.isEmpty(optString)) {
                                optString = "${" + string + "}";
                            }
                            String optString2 = optJSONObject.optString(a.g, UTTPKItem.TYPE_FAR);
                            uTTPKItem.setKname(string);
                            uTTPKItem.setKvalue(optString);
                            uTTPKItem.setType(optString2);
                            this.mTPKItems.add(uTTPKItem);
                        }
                    }
                }
            } catch (Exception unused) {
            }
        }
    }

    private String _getTPKValue(String str, Uri uri, Map<String, String> map) {
        String str2;
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        if (str.startsWith("${url|") && str.length() > 7) {
            String substring = str.substring("${url|".length(), str.length() - 1);
            if (!StringUtils.isEmpty(substring) && uri != null) {
                return uri.getQueryParameter(substring);
            }
        } else if (str.startsWith("${ut|") && str.length() > 6) {
            String substring2 = str.substring("${ut|".length(), str.length() - 1);
            if (!StringUtils.isEmpty(substring2) && map != null) {
                return map.get(substring2);
            }
        } else if (!str.startsWith("${") || str.length() <= 3) {
            return str;
        } else {
            String substring3 = str.substring("${".length(), str.length() - 1);
            if (!StringUtils.isEmpty(substring3)) {
                if (map != null && (str2 = map.get(substring3)) != null) {
                    return str2;
                }
                if (uri != null) {
                    return uri.getQueryParameter(substring3);
                }
            }
        }
        return null;
    }

    public synchronized String getTpkString(Uri uri, Map<String, String> map) {
        String value;
        String str = UTClientConfigMgr.getInstance().get("tpk_md5");
        Logger.d("UTTPKBiz", "tpk_md5", str);
        if (!(str == null || str.equals(this.mTPKMD5) || (value = AnalyticsMgr.getValue("tpk_string")) == null)) {
            _onTPKConfArrive((String) null, value);
            this.mTPKMD5 = "" + value.hashCode();
        }
        for (UTTPKItem next : this.mTPKItems) {
            String kname = next.getKname();
            String type = next.getType();
            String kvalue = next.getKvalue();
            if (StringUtils.isEmpty(kname)) {
                return null;
            }
            if (StringUtils.isEmpty(this.mTPKCache.get(kname))) {
                String _getTPKValue = _getTPKValue(kvalue, uri, map);
                if (!StringUtils.isEmpty(_getTPKValue)) {
                    this.mTPKCache.put(kname, _getTPKValue);
                }
            } else if (!UTTPKItem.TYPE_FAR.equals(type)) {
                String _getTPKValue2 = _getTPKValue(kvalue, uri, map);
                if (!StringUtils.isEmpty(_getTPKValue2)) {
                    this.mTPKCache.put(kname, _getTPKValue2);
                }
            }
        }
        if (!this.mTPKCache.containsKey("ttid") && !StringUtils.isEmpty(ClientVariables.getInstance().getOutsideTTID())) {
            this.mTPKCache.put("ttid", ClientVariables.getInstance().getOutsideTTID());
        }
        if (this.mTPKCache.size() <= 0) {
            return null;
        }
        return Operators.BLOCK_START_STR + StringUtils.convertMapToString(this.mTPKCache) + "}";
    }

    public synchronized void sessionTimeout() {
        this.mTPKCache.clear();
    }
}
