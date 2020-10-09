package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserEventHandlerNotFound extends DXAbsDinamicDataParser {
    public static final long DX_PARSER_EVENTHANDLERNOTFOUND = 3078873525629101857L;

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr == null || objArr.length != 1) {
            return true;
        }
        boolean z = false;
        if (!(objArr[0] instanceof String)) {
            return true;
        }
        try {
            if (dXRuntimeContext.getEventHandlerMap().get(Long.parseLong(objArr[0])) == null) {
                z = true;
            }
            return Boolean.valueOf(z);
        } catch (Exception unused) {
            return true;
        }
    }
}
