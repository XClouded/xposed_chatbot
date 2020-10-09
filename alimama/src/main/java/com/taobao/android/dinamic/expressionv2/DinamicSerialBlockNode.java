package com.taobao.android.dinamic.expressionv2;

import com.taobao.android.dinamic.expressionv2.DinamicASTNode;
import com.taobao.android.dinamic.log.DinamicLog;
import java.util.ArrayList;

public class DinamicSerialBlockNode extends DinamicASTNode {
    public DinamicASTNode.DinamicASTNodeType getType() {
        return DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeSerialBlock;
    }

    public Object evaluate() {
        DinamicLog.print("DXSerialBlockNode:" + this.name);
        if (this.children == null) {
            return null;
        }
        int size = this.children.size();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < size; i++) {
            Object evaluate = ((DinamicASTNode) this.children.get(i)).evaluate();
            if (evaluate != null) {
                arrayList.add(evaluate.toString());
            }
        }
        return arrayList;
    }

    public DinamicSerialBlockNode() {
        this.type = DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeSerialBlock;
        this.name = "branch";
    }
}
