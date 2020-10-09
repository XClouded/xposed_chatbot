package com.taobao.android.dinamic.expressionv2;

import com.taobao.android.dinamic.expressionv2.DinamicASTNode;
import com.taobao.android.dinamic.log.DinamicLog;

public class DinamicVarNode extends DinamicASTNode {
    public DinamicASTNode.DinamicASTNodeType getType() {
        return DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeVar;
    }

    public Object evaluate() {
        DinamicLog.print("VarName:" + this.name);
        return this.name;
    }
}
