package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.alibaba.fastjson.JSONArray;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserIndexOf extends DXAbsDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        int indexOf;
        if (objArr == null || objArr.length != 2) {
            return null;
        }
        JSONArray jSONArray = objArr[0];
        if ((jSONArray instanceof JSONArray) && (indexOf = indexOf(jSONArray, objArr[1])) != -1) {
            return String.valueOf(indexOf);
        }
        return null;
    }

    private int indexOf(JSONArray jSONArray, Object obj) {
        int size = jSONArray.size();
        for (int i = 0; i < size; i++) {
            if (jSONArray.get(i) == obj) {
                return i;
            }
        }
        return -1;
    }
}
