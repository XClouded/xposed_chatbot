package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.DXMethodNode;
import com.taobao.android.dinamicx.expression.DXNumberUtil;
import com.taobao.android.dinamicx.expression.parser.DXAbsFastReturnDinamicDataParser;

public class DXDataParserTriple extends DXAbsFastReturnDinamicDataParser {
    public Object evalWithArgs(Object[] objArr, DXRuntimeContext dXRuntimeContext, DXMethodNode.DXBoolean dXBoolean, int i) {
        if (i < 1) {
            return null;
        }
        if (i != 1) {
            dXBoolean.value = true;
            if (i == 2) {
                return objArr[2];
            }
            return null;
        } else if (!DXNumberUtil.parseBoolean(objArr[0])) {
            return null;
        } else {
            dXBoolean.value = true;
            return objArr[1];
        }
    }
}
