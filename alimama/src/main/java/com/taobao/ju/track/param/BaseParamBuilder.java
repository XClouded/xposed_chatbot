package com.taobao.ju.track.param;

import android.text.TextUtils;
import com.taobao.ju.track.JTrack;
import com.taobao.ju.track.impl.TrackImpl;
import com.taobao.weex.BuildConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;

public abstract class BaseParamBuilder {
    public static final String DIVIDER = "_";
    private static final Pattern DYNAMIC_REGEX_PATTERN = Pattern.compile(DYNAMIC_REPLACE_REGEX);
    private static final String DYNAMIC_REPLACE_REGEX = "\\[(\\w)+\\]";
    protected String mCSVName = null;
    protected String mCSVRowName = null;
    protected Map<String, String> mParams;

    public abstract BaseParamBuilder add(String str, Object obj);

    public abstract BaseParamBuilder add(Map<String, String> map);

    public abstract BaseParamBuilder forceAdd(String str, Object obj);

    protected static <T extends BaseParamBuilder> T makeInternal(TrackImpl trackImpl, T t, Object obj) {
        t.mCSVRowName = String.valueOf(obj);
        t.mParams = new HashMap();
        t.mParams.putAll(trackImpl.getValidParams(t.mCSVRowName));
        t.mCSVName = trackImpl.getFileName();
        return t;
    }

    public Map<String, String> getParams() {
        return getParams(false);
    }

    public Map<String, String> getParams(boolean z) {
        HashMap hashMap = new HashMap();
        if (this.mParams != null && this.mParams.size() > 0) {
            for (String next : this.mParams.keySet()) {
                if (!TextUtils.isEmpty(next)) {
                    String paramValue = getParamValue(next);
                    if (z || isParamValueValidate(paramValue)) {
                        hashMap.put(next, paramValue);
                    }
                }
            }
        }
        return hashMap;
    }

    public String[] getKvs() {
        return getKvs(false);
    }

    private String[] getKvs(boolean z) {
        if (this.mParams == null || this.mParams.size() <= 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String next : this.mParams.keySet()) {
            if (!TextUtils.isEmpty(next)) {
                String paramValue = getParamValue(next);
                if (z || isParamValueValidate(paramValue)) {
                    arrayList.add(next + SymbolExpUtil.SYMBOL_EQUAL + paramValue);
                }
            }
        }
        if (arrayList.size() <= 0) {
            return null;
        }
        String[] strArr = new String[arrayList.size()];
        arrayList.toArray(strArr);
        return strArr;
    }

    private boolean isParamValueValidate(String str) {
        boolean z = !TextUtils.isEmpty(str) && !BuildConfig.buildJavascriptFrameworkVersion.equals(str);
        if (this.mCSVName == null) {
            return z;
        }
        if (!z || !JTrack.Ctrl.isValidateToUt(str)) {
            return false;
        }
        return true;
    }

    public String getParamValue(String str) {
        return getParamValue(str, BuildConfig.buildJavascriptFrameworkVersion);
    }

    public String getParamValue(String str, String str2) {
        if (!TextUtils.isEmpty(str) && this.mParams != null && this.mParams.size() > 0) {
            String str3 = this.mParams.get(str);
            if (!TextUtils.isEmpty(str3)) {
                Matcher matcher = DYNAMIC_REGEX_PATTERN.matcher(str3);
                StringBuffer stringBuffer = new StringBuffer();
                while (matcher.find()) {
                    String group = matcher.group();
                    matcher.appendReplacement(stringBuffer, getParamValue(group.substring(1, group.length() - 1)));
                }
                return stringBuffer.length() > 0 ? stringBuffer.toString() : str3;
            }
        }
        return str2;
    }

    public String getParamsValueStr() {
        if (this.mParams == null || this.mParams.size() <= 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        boolean z = false;
        for (String paramValue : this.mParams.keySet()) {
            String paramValue2 = getParamValue(paramValue);
            if (isParamValueValidate(paramValue2)) {
                stringBuffer.append(paramValue2);
                if (z) {
                    stringBuffer.append("_");
                } else {
                    z = true;
                }
            }
        }
        return stringBuffer.toString();
    }

    public Map<String, String> getParamsByKeys(String... strArr) {
        HashMap hashMap = new HashMap();
        if (strArr != null) {
            for (String str : strArr) {
                hashMap.put(str, getParamValue(str));
            }
        }
        return hashMap;
    }

    public String toString() {
        String[] kvs = getKvs(false);
        if (kvs != null) {
            return Arrays.asList(kvs).toString();
        }
        return super.toString();
    }

    public String toSortString() {
        String[] kvs = getKvs(false);
        if (kvs == null) {
            return super.toString();
        }
        List asList = Arrays.asList(kvs);
        Collections.sort(asList);
        ArrayList arrayList = new ArrayList(asList);
        arrayList.add("_key=" + getCtrlName());
        return arrayList.toString();
    }

    public String getCtrlName() {
        return this.mCSVRowName != null ? this.mCSVRowName : BuildConfig.buildJavascriptFrameworkVersion;
    }
}
