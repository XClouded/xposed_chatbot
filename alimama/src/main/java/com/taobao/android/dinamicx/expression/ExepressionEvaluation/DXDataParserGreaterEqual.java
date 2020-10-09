package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;
import com.taobao.android.dinamicx.expression.utils.DXNumCompareUtils;

public class DXDataParserGreaterEqual extends DXAbsDinamicDataParser {
    public static final long DX_PARSER_GREATEREQUAL = 3589495604776427758L;

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        return DXNumCompareUtils.evalWithArgs(objArr, 3);
    }
}
