package com.taobao.android.dinamicx.expression.parser;

import com.taobao.android.dinamicx.DXRuntimeContext;

public interface IDXDataParser {
    Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext);
}
