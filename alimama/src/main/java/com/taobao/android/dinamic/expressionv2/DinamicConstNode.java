package com.taobao.android.dinamic.expressionv2;

import com.taobao.android.dinamic.expressionv2.DinamicASTNode;
import com.taobao.android.dinamic.log.DinamicLog;

public class DinamicConstNode extends DinamicASTNode {
    public DinamicASTNode.DinamicASTNodeType getType() {
        return DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeConst;
    }

    public Object evaluate() {
        DinamicLog.print("ConstName:" + this.name);
        return super.evaluate();
    }
}
