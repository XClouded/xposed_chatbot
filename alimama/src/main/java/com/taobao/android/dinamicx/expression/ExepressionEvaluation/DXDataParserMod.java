package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;
import com.taobao.android.dinamicx.expression.utils.DXNumArithmeticUtils;

public class DXDataParserMod extends DXAbsDinamicDataParser {
    public static final long DX_PARSER_MOD = 523365723;

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        return DXNumArithmeticUtils.evalWithArgs(objArr, 5);
    }
}
