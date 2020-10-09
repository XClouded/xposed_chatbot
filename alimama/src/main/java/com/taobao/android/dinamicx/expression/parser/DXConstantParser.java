package com.taobao.android.dinamicx.expression.parser;

import com.taobao.android.dinamicx.DXRuntimeContext;

public class DXConstantParser extends DXAbsDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        if (objArr == null || objArr.length == 0) {
            return null;
        }
        return objArr[0];
    }
}
