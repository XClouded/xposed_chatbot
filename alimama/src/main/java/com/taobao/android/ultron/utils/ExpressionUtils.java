package com.taobao.android.ultron.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.expr.ExpressionExt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ExpressionUtils {
    public static Object parseExpressionObj(JSONObject jSONObject, Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return ExpressionExt.evaluate(jSONObject, (String) obj);
        }
        if (obj instanceof JSONObject) {
            JSONObject jSONObject2 = (JSONObject) obj;
            for (Map.Entry next : jSONObject2.entrySet()) {
                next.setValue(parseExpressionObj(jSONObject, next.getValue()));
            }
            return jSONObject2;
        } else if (!(obj instanceof JSONArray)) {
            return null;
        } else {
            JSONArray jSONArray = (JSONArray) obj;
            ArrayList arrayList = new ArrayList();
            Iterator<Object> it = jSONArray.iterator();
            while (it.hasNext()) {
                Object parseExpressionObj = parseExpressionObj(jSONObject, it.next());
                if (parseExpressionObj != null) {
                    arrayList.add(parseExpressionObj);
                }
            }
            jSONArray.clear();
            jSONArray.addAll(arrayList);
            return jSONArray;
        }
    }
}
