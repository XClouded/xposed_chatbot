package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.DXNumberUtil;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserEqual extends DXAbsDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr == null || objArr.length != 2) {
            return null;
        }
        Number number = objArr[0];
        Number number2 = objArr[1];
        if (number == null && number2 == null) {
            return true;
        }
        if (number == null || number2 == null) {
            return false;
        }
        if (number.getClass().equals(number2.getClass())) {
            return Boolean.valueOf(number.equals(number2));
        }
        if (!(number instanceof Number) || !(number2 instanceof Number)) {
            return Boolean.valueOf(number.equals(number2));
        }
        return Boolean.valueOf(DXNumberUtil.compareNumber(number, number2));
    }
}
