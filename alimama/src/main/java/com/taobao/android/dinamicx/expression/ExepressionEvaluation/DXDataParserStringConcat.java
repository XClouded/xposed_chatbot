package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserStringConcat extends DXAbsDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr == null || objArr.length < 2) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : objArr) {
            if (obj instanceof String) {
                sb.append(obj);
            }
        }
        return sb.toString();
    }
}
