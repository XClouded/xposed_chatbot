package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserLength extends DXAbsDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr == null || objArr.length != 1) {
            return null;
        }
        String str = objArr[0];
        if (str instanceof String) {
            return String.valueOf(str.length());
        }
        if (str instanceof JSONArray) {
            return String.valueOf(((JSONArray) str).size());
        }
        if (str instanceof JSONObject) {
            return String.valueOf(((JSONObject) str).size());
        }
        return null;
    }
}
