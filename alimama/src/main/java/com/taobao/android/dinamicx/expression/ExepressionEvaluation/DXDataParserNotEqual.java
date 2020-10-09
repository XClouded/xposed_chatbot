package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.DXNumberUtil;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserNotEqual extends DXAbsDinamicDataParser {
    public static final Object DEFAULT_VALUE = null;
    public static final long DX_PARSER_NOTEQUAL = 4995563293267863121L;

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr != null) {
            try {
                if (objArr.length == 2) {
                    Number number = objArr[0];
                    Number number2 = objArr[1];
                    if (number == null && number2 == null) {
                        return false;
                    }
                    if (number != null) {
                        if (number2 != null) {
                            if (number.getClass().equals(number2.getClass())) {
                                return Boolean.valueOf(!number.equals(number2));
                            }
                            if (!(number instanceof Number) || !(number2 instanceof Number)) {
                                return Boolean.valueOf(!number.equals(number2));
                            }
                            return Boolean.valueOf(!DXNumberUtil.compareNumber(number, number2));
                        }
                    }
                    return true;
                }
            } catch (Throwable unused) {
                return DEFAULT_VALUE;
            }
        }
        return DEFAULT_VALUE;
    }
}
