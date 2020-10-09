package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserToLong extends DXAbsDinamicDataParser {
    private static final long DEFAULT_VALUE = 0;
    public static final long DX_PARSER_TOLONG = 10223887357506745L;

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr != null) {
            try {
                if (objArr.length == 1) {
                    Number number = objArr[0];
                    if (number instanceof Number) {
                        return Long.valueOf(number.longValue());
                    }
                    if (number instanceof String) {
                        return Long.valueOf(new Double((String) number).longValue());
                    }
                    return 0L;
                }
            } catch (Throwable unused) {
                return 0L;
            }
        }
        return 0L;
    }
}
