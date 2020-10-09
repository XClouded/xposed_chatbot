package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserDataParserNotFound extends DXAbsDinamicDataParser {
    public static final long DX_PARSER_DATAPARSERNOTFOUND = 1583580654108865350L;

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr == null || objArr.length != 1) {
            return true;
        }
        boolean z = false;
        if (!(objArr[0] instanceof String)) {
            return true;
        }
        try {
            if (dXRuntimeContext.getParserMap().get(Long.parseLong(objArr[0])) == null) {
                z = true;
            }
            return Boolean.valueOf(z);
        } catch (Exception unused) {
            return true;
        }
    }
}
