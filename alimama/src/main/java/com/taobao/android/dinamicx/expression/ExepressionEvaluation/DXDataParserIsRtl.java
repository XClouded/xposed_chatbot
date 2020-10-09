package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import android.os.Build;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserIsRtl extends DXAbsDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext) {
        return Boolean.valueOf(isRtl());
    }

    private boolean isRtl() {
        return Build.VERSION.SDK_INT >= 17 && DinamicXEngine.getApplicationContext().getResources().getConfiguration().getLayoutDirection() == 1;
    }
}
