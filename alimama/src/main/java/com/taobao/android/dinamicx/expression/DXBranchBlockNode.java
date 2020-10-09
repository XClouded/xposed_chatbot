package com.taobao.android.dinamicx.expression;

import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.event.DXEvent;

public class DXBranchBlockNode extends DXExprNode {
    public byte getType() {
        return 5;
    }

    public Object evaluate(@Nullable DXEvent dXEvent, DXRuntimeContext dXRuntimeContext) {
        int size = this.children.size();
        if (size <= 1) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            Object evaluate = ((DXExprNode) this.children.get(i)).evaluate(dXEvent, dXRuntimeContext);
            if (evaluate != null) {
                return evaluate;
            }
        }
        return null;
    }

    public DXBranchBlockNode() {
        this.type = 5;
        this.name = "branch";
    }
}
