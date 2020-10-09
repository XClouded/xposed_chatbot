package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXDarkModeCenter;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserIsDarkMode extends DXAbsDinamicDataParser {
    public static final long DX_PARSER_ISDARKMODE = 8991544260901901805L;

    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        return Boolean.valueOf(DXDarkModeCenter.isSupportDarkMode() && DXDarkModeCenter.isDark());
    }
}
