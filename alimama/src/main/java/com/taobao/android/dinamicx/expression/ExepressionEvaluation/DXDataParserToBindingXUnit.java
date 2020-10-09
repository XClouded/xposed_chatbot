package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;

public class DXDataParserToBindingXUnit extends DXAbsDinamicDataParser {
    private static final String AP = "ap";
    public static final long DX_PARSER_TOBINDINGXUNIT = 6677129169796262308L;
    private static final String NP = "np";

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        int i;
        if (objArr != null) {
            try {
                if (objArr.length == 1) {
                    Number number = objArr[0];
                    if (number instanceof Number) {
                        return Integer.valueOf(DXScreenTool.ap2px(DinamicXEngine.getApplicationContext(), number.floatValue()));
                    }
                    if (!(number instanceof String)) {
                        return 0;
                    }
                    String str = (String) number;
                    if (str.endsWith("ap")) {
                        i = DXScreenTool.ap2px(DinamicXEngine.getApplicationContext(), Float.parseFloat(str.substring(0, str.length() - 2)));
                    } else if (!str.endsWith("np")) {
                        return Integer.valueOf(DXScreenTool.ap2px(DinamicXEngine.getApplicationContext(), Float.valueOf(str).floatValue()));
                    } else {
                        i = DXScreenTool.dip2px(DinamicXEngine.getApplicationContext(), Float.parseFloat(str.substring(0, str.length() - 2)));
                    }
                    return Integer.valueOf(i);
                }
            } catch (Throwable unused) {
                return 0;
            }
        }
        return 0;
    }
}
