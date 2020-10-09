package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.DXNumberUtil;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserAbs extends DXAbsDinamicDataParser {
    private static final long DEFAULT_VALUE = 0;
    public static final long DX_PARSER_ABS = 516202497;

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr != null) {
            try {
                if (objArr.length == 1) {
                    Number number = objArr[0];
                    if (!(number instanceof Long)) {
                        if (!(number instanceof Integer)) {
                            if (DXNumberUtil.isFloatPointNum(number)) {
                                return Double.valueOf(Math.abs(number.doubleValue()));
                            }
                            if (!(number instanceof String)) {
                                return 0L;
                            }
                            String str = (String) number;
                            if (DXNumberUtil.hasDigit(str)) {
                                return Double.valueOf(Math.abs(Double.valueOf(str).doubleValue()));
                            }
                            return Long.valueOf(Math.abs(Long.valueOf(str).longValue()));
                        }
                    }
                    return Long.valueOf(Math.abs(number.longValue()));
                }
            } catch (Throwable unused) {
                return 0L;
            }
        }
        return 0L;
    }
}
