package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.alibaba.fastjson.JSONArray;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserFind extends DXAbsDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr == null || objArr.length != 2) {
            return null;
        }
        JSONArray jSONArray = objArr[0];
        Object obj = objArr[1];
        if ((jSONArray instanceof JSONArray) && jSONArray.contains(obj)) {
            return obj;
        }
        return null;
    }
}
