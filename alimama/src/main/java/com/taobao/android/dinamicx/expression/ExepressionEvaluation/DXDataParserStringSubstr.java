package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.DXNumberUtil;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserStringSubstr extends DXAbsDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        String str = null;
        if (objArr == null || objArr.length > 3 || objArr.length < 2) {
            return null;
        }
        int i = 0;
        String str2 = objArr[0];
        if (!(str2 instanceof String)) {
            return null;
        }
        String str3 = str2;
        String str4 = objArr[1];
        if (!(str4 instanceof String)) {
            return null;
        }
        int parseInt = DXNumberUtil.parseInt(str4);
        if (objArr.length == 3) {
            String str5 = objArr[2];
            if (!(str5 instanceof String)) {
                return null;
            }
            int parseInt2 = DXNumberUtil.parseInt(str5);
            if (parseInt2 < 0 || parseInt + 1 > str3.length()) {
                return "";
            }
            if (parseInt >= 0) {
                i = parseInt;
            }
            int i2 = parseInt2 + i;
            if (i2 > str3.length()) {
                str = str3.substring(i);
            }
            if (i2 - 1 < str3.length()) {
                return str3.substring(i, i2);
            }
            return str;
        } else if (parseInt + 1 > str3.length()) {
            return "";
        } else {
            if (parseInt >= 0) {
                i = parseInt;
            }
            return str3.substring(i);
        }
    }
}
