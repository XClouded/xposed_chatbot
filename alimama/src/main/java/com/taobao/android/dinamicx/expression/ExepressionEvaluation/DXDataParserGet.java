package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.DXNumberUtil;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserGet extends DXAbsDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr == null || objArr.length != 2) {
            return null;
        }
        JSONObject jSONObject = objArr[0];
        if (jSONObject instanceof JSONObject) {
            return jSONObject.get(objArr[1]);
        } else if (!(jSONObject instanceof JSONArray)) {
            return null;
        } else {
            String str = objArr[1];
            if (!(str instanceof String)) {
                return null;
            }
            JSONArray jSONArray = (JSONArray) jSONObject;
            int parseInt = DXNumberUtil.parseInt(str);
            if (parseInt < 0 || parseInt >= jSONArray.size()) {
                return null;
            }
            return jSONArray.get(parseInt);
        }
    }
}
