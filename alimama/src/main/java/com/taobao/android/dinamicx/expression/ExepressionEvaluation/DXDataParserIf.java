package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.DXMethodNode;
import com.taobao.android.dinamicx.expression.DXNumberUtil;
import com.taobao.android.dinamicx.expression.parser.DXAbsFastReturnDinamicDataParser;

public class DXDataParserIf extends DXAbsFastReturnDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext, DXMethodNode.DXBoolean dXBoolean, int i) {
        if (i == 0) {
            dXBoolean.value = !DXNumberUtil.parseBoolean(objArr[0]);
            return null;
        }
        dXBoolean.value = true;
        if (i == 1) {
            return objArr[1];
        }
        return null;
    }
}
