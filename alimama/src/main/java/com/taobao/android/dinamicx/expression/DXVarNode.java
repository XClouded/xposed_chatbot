package com.taobao.android.dinamicx.expression;

import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.event.DXEvent;

public class DXVarNode extends DXExprNode {
    public byte getType() {
        return 2;
    }

    public Object evaluate(@Nullable DXEvent dXEvent, DXRuntimeContext dXRuntimeContext) {
        return this.name;
    }
}
