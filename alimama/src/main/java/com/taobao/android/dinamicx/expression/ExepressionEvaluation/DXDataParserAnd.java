package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.DXMethodNode;
import com.taobao.android.dinamicx.expression.DXNumberUtil;
import com.taobao.android.dinamicx.expression.parser.DXAbsFastReturnDinamicDataParser;

public class DXDataParserAnd extends DXAbsFastReturnDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext, DXMethodNode.DXBoolean dXBoolean, int i) {
        if (DXNumberUtil.parseBoolean(objArr[i])) {
            return true;
        }
        dXBoolean.value = true;
        return false;
    }
}
