package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamic.BuildConfig;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserDXVersionGreaterThanOrEqualTo extends DXAbsDinamicDataParser {
    public static final long DX_PARSER_DXVERSION_GREATERTHANOREQUALTO = 87712825513562832L;

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr == null || objArr.length != 1) {
            return false;
        }
        if (!(objArr[0] instanceof String)) {
            return false;
        }
        String[] split = BuildConfig.DINAMICX_SDK_VERSION.split("\\.");
        String[] split2 = objArr[0].split("\\.");
        if (split.length == 4 && split2.length == 4) {
            for (int i = 0; i < 4; i++) {
                String str = split[i];
                String str2 = split2[i];
                if (i == 3) {
                    try {
                        if (str.contains("-")) {
                            str = str.split("-")[0];
                        }
                        if (str2.contains("-")) {
                            str2 = str2.split("-")[0];
                        }
                    } catch (Exception unused) {
                        return false;
                    }
                }
                int parseInt = Integer.parseInt(str);
                int parseInt2 = Integer.parseInt(str2);
                if (parseInt2 > parseInt) {
                    return true;
                }
                if (parseInt2 < parseInt) {
                    return false;
                }
                if (i == 3) {
                    return true;
                }
            }
        }
        return false;
    }
}
