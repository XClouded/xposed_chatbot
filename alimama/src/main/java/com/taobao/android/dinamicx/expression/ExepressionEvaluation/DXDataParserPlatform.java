package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserPlatform extends DXAbsDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr == null || objArr.length != 1) {
            return null;
        }
        String str = objArr[0];
        if (!(str instanceof String)) {
            return null;
        }
        return Boolean.valueOf(str.equalsIgnoreCase("android"));
    }
}
