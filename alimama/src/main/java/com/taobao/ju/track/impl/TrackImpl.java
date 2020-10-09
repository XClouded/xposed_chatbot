package com.taobao.ju.track.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.TextUtils;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.csv.AsyncUtCsvLoader;
import com.taobao.ju.track.impl.interfaces.ITrack;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.SymbolExpUtil;

public class TrackImpl implements ITrack {
    protected static final String PARAM_INTERNAL_AUTOSEND = "_autosend";
    protected static final String PARAM_INTERNAL_MARK = "_";
    protected static final String PARAM_INTERNAL_PARAM_DYNAMIC_LEFT = "{";
    protected static final String PARAM_INTERNAL_PARAM_DYNAMIC_RIGHT = "}";
    protected static final String PARAM_INTERNAL_PARAM_REFER_LEFT = "[";
    protected static final String PARAM_INTERNAL_PARAM_REFER_RIGHT = "]";
    protected static final String PARAM_INTERNAL_SPMA = "_spma";
    protected static final String PARAM_INTERNAL_SPMB = "_spmb";
    protected static final String PARAM_INTERNAL_SPMC = "_spmc";
    protected static final String PARAM_INTERNAL_SPMD = "_spmd";
    protected static final String PARAM_INTERNAL_SPM_NONE = "0";
    public static final String PARAM_INTERNAL_SPM_SPLIT = ".";
    protected static final String PARAM_OUTER_AUTOSEND = "autosend";
    private static final String TAG = "TrackImpl";
    protected Context mContext;
    protected String mFileName;
    protected Map<String, Map<String, String>> mParams = new ConcurrentHashMap();

    public TrackImpl(Context context, String str) {
        this.mFileName = str;
        this.mContext = context;
        loadParams(context, str);
    }

    @TargetApi(3)
    private void loadParams(Context context, String str) {
        new AsyncUtCsvLoader(this.mParams).execute(new Object[]{context, str});
    }

    public String[] getParamKvs(String str) {
        Map<String, String> paramMap = getParamMap(str);
        int i = 0;
        if (paramMap == null || paramMap.size() <= 0) {
            return new String[0];
        }
        String[] strArr = new String[paramMap.size()];
        for (String next : paramMap.keySet()) {
            strArr[i] = next + SymbolExpUtil.SYMBOL_EQUAL + paramMap.get(next);
            i++;
        }
        return strArr;
    }

    public Properties getParamProp(String str) {
        Map<String, String> paramMap = getParamMap(str);
        Properties properties = new Properties();
        if (paramMap != null && paramMap.size() > 0) {
            properties.putAll(paramMap);
        }
        return properties;
    }

    public Map<String, String> getParamMap(String str) {
        if (this.mParams != null && this.mParams.containsKey(str)) {
            return this.mParams.get(str);
        }
        if (this.mParams == null) {
            return null;
        }
        for (Map.Entry next : this.mParams.entrySet()) {
            if (next != null) {
                String str2 = (String) next.getKey();
                Map<String, String> map = (Map) next.getValue();
                if (str != null && str2 != null && str2.contains("|") && str.matches(str2)) {
                    return map;
                }
            }
        }
        return null;
    }

    public Map<String, String> getValidParams(String str) {
        HashMap hashMap = new HashMap();
        Map<String, String> paramMap = getParamMap(str);
        if (paramMap != null) {
            for (Map.Entry next : paramMap.entrySet()) {
                if (!isInternal((String) next.getKey()) && (isStatic((String) next.getValue()) || isRefer((String) next.getValue()) || isDynamic((String) next.getValue()))) {
                    hashMap.put(next.getKey(), next.getValue());
                }
            }
        }
        return hashMap;
    }

    public String getParamValue(String str, String str2) {
        return getParamValue(str, str2, (String) null);
    }

    public String getParamValue(String str, String str2, String str3) {
        Map<String, String> paramMap = getParamMap(str);
        return (paramMap == null || !paramMap.containsKey(str2)) ? str3 : paramMap.get(str2);
    }

    /* access modifiers changed from: protected */
    public String getParamValue(Map<String, String> map, String str, String str2) {
        return (map == null || !map.containsKey(str)) ? str2 : map.get(str);
    }

    public Map<String, String> getStatic(String str) {
        HashMap hashMap = new HashMap();
        Map<String, String> paramMap = getParamMap(str);
        if (paramMap != null) {
            for (Map.Entry next : paramMap.entrySet()) {
                if (!isInternal((String) next.getKey()) && isStatic((String) next.getValue())) {
                    hashMap.put(next.getKey(), next.getValue());
                }
            }
        }
        return hashMap;
    }

    public Map<String, String> getRefer(String str) {
        HashMap hashMap = new HashMap();
        Map<String, String> paramMap = getParamMap(str);
        if (paramMap != null) {
            for (Map.Entry next : paramMap.entrySet()) {
                if (!isInternal((String) next.getKey()) && isRefer((String) next.getValue())) {
                    hashMap.put(next.getKey(), next.getValue());
                }
            }
        }
        return hashMap;
    }

    public Map<String, String> getDynamic(String str) {
        HashMap hashMap = new HashMap();
        Map<String, String> paramMap = getParamMap(str);
        if (paramMap != null) {
            for (Map.Entry next : paramMap.entrySet()) {
                if (!isInternal((String) next.getKey()) && isDynamic((String) next.getValue())) {
                    hashMap.put(next.getKey(), next.getValue());
                }
            }
        }
        return hashMap;
    }

    public boolean isValidateToUT(String str) {
        return isStatic(str);
    }

    public boolean hasPoint(String str) {
        return this.mParams != null && this.mParams.containsKey(str);
    }

    public boolean hasParam(String str, String str2) {
        return !TextUtils.isEmpty(getParamValue(str, str2));
    }

    public boolean isInternal(String str, String str2) {
        return isInternal(str2);
    }

    public boolean isStatic(String str, String str2) {
        return !isInternal(str, str2) && isStatic(getParamValue(str, str2));
    }

    public boolean isRefer(String str, String str2) {
        return !isInternal(str, str2) && isRefer(getParamValue(str, str2));
    }

    public boolean isDynamic(String str, String str2) {
        return !isInternal(str, str2) && isDynamic(getParamValue(str, str2));
    }

    public String getFileName() {
        return this.mFileName;
    }

    public String getSpm(String str) {
        return getSpm(getParamMap(str));
    }

    /* access modifiers changed from: protected */
    public String getSpm(Map<String, String> map) {
        return getSpmAB(map) + "." + getSpmCD(map);
    }

    /* access modifiers changed from: protected */
    public String getSpmAB(Map<String, String> map) {
        StringBuffer stringBuffer = new StringBuffer();
        if (map != null) {
            stringBuffer.append(getParamValue(map, PARAM_INTERNAL_SPMA, "0"));
            stringBuffer.append(".");
            stringBuffer.append(getParamValue(map, PARAM_INTERNAL_SPMB, "0"));
        }
        return stringBuffer.length() > 0 ? stringBuffer.toString() : Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
    }

    /* access modifiers changed from: protected */
    public String getSpmCD(Map<String, String> map) {
        StringBuffer stringBuffer = new StringBuffer();
        if (map != null) {
            stringBuffer.append(getParamValue(map, PARAM_INTERNAL_SPMC, "0"));
            stringBuffer.append(".");
            stringBuffer.append(getParamValue(map, PARAM_INTERNAL_SPMD, "0"));
        }
        return stringBuffer.length() > 0 ? stringBuffer.toString() : Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
    }

    /* access modifiers changed from: protected */
    public boolean isInternal(String str) {
        return str != null && str.startsWith("_");
    }

    /* access modifiers changed from: protected */
    public boolean isStatic(String str) {
        if (str == null || str.length() <= 0 || isRefer(str) || isDynamic(str)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isRefer(String str) {
        return str != null && str.indexOf("[") < str.lastIndexOf("]");
    }

    /* access modifiers changed from: protected */
    public boolean isDynamic(String str) {
        return str != null && str.startsWith("{") && str.endsWith("}");
    }
}
