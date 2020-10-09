package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;

public class DXDataParserScreenHeight extends DXAbsDinamicDataParser {
    public static final long DX_PARSER_SCREENHEIGHT = -2779958080420666907L;

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        return Integer.valueOf(DXScreenTool.getScreenHeight(dXRuntimeContext.getContext()));
    }
}
