package com.taobao.android.dinamicx.expression;

import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import java.util.ArrayList;

public class DXSerialBlockNode extends DXExprNode {
    public byte getType() {
        return 4;
    }

    public Object evaluate(@Nullable DXEvent dXEvent, DXRuntimeContext dXRuntimeContext) {
        if (this.children == null) {
            return null;
        }
        int size = this.children.size();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            Object evaluate = ((DXExprNode) this.children.get(i)).evaluate(dXEvent, dXRuntimeContext);
            if (evaluate != null) {
                arrayList.add(evaluate.toString());
            }
        }
        return arrayList;
    }

    public DXSerialBlockNode() {
        this.type = 4;
        this.name = "branch";
    }
}
