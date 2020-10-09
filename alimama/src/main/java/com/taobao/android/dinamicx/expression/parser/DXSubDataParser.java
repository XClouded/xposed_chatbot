package com.taobao.android.dinamicx.expression.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.log.DXLog;
import java.util.StringTokenizer;

public class DXSubDataParser extends DXAbsDinamicDataParser {
    private static final String DELIMITER = " .[]";

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        Object subData = dXRuntimeContext.getSubData();
        if (objArr == null || objArr.length == 0) {
            return subData;
        }
        if (objArr.length > 1) {
            return null;
        }
        String str = objArr[0];
        if (!(str instanceof String)) {
            return null;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, DELIMITER, false);
        while (stringTokenizer.hasMoreTokens()) {
            subData = getValue(subData, stringTokenizer.nextToken());
        }
        return subData;
    }

    private Object getValue(Object obj, String str) {
        if (obj == null || str == null) {
            return null;
        }
        if (obj instanceof JSONObject) {
            return ((JSONObject) obj).get(str);
        }
        if (!(obj instanceof JSONArray)) {
            return null;
        }
        try {
            return ((JSONArray) obj).get(Integer.parseInt(str));
        } catch (Exception unused) {
            DXLog.print("DXExpressionParser list index is not number");
            return null;
        }
    }
}
