package com.taobao.android.dinamicx.expression;

import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.event.DXEvent;

public class DXConstNode extends DXExprNode {
    public byte getType() {
        return 3;
    }

    public Object evaluate(@Nullable DXEvent dXEvent, DXRuntimeContext dXRuntimeContext) {
        return super.evaluate(dXEvent, dXRuntimeContext);
    }
}
