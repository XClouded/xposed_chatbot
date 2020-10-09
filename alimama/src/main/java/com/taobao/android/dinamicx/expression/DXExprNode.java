package com.taobao.android.dinamicx.expression;

import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import java.util.ArrayList;
import java.util.List;

public class DXExprNode {
    public static final byte BranchBlock = 5;
    public static final byte Const = 3;
    public static final byte Event = 6;
    public static final byte Method = 1;
    public static final byte None = 0;
    public static final byte SerialBlock = 4;
    public static final byte Var = 2;
    public List<DXExprNode> children;
    private short dataType;
    public long exprId;
    public String name;
    DXExprNode parent;
    public byte type;

    public byte getType() {
        return 0;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void addChildNode(DXExprNode dXExprNode) {
        if (dXExprNode != null) {
            if (this.children == null) {
                this.children = new ArrayList();
            }
            dXExprNode.parent = this;
            this.children.add(dXExprNode);
        }
    }

    public List<DXExprNode> getAllChildren() {
        if (this.children != null) {
            return this.children;
        }
        return null;
    }

    public Object evaluate(@Nullable DXEvent dXEvent, DXRuntimeContext dXRuntimeContext) {
        return this.name;
    }

    public short getDataType() {
        return this.dataType;
    }

    public void setDataType(short s) {
        this.dataType = s;
    }

    public String toString() {
        String str = this.name + ".";
        if (this.children != null) {
            for (DXExprNode dXExprNode : this.children) {
                str = str + dXExprNode.toString();
            }
        }
        return str;
    }
}
