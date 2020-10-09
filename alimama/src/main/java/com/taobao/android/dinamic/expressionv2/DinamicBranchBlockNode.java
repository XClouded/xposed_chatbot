package com.taobao.android.dinamic.expressionv2;

import com.taobao.android.dinamic.expressionv2.DinamicASTNode;
import com.taobao.android.dinamic.log.DinamicLog;

public class DinamicBranchBlockNode extends DinamicASTNode {
    public DinamicASTNode.DinamicASTNodeType getType() {
        return DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeBranchBlock;
    }

    public Object evaluate() {
        DinamicLog.print("DXBranchBlockNode:" + this.name);
        int size = this.children.size();
        DinamicLog.print("children.size():" + size);
        if (size <= 1) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            Object evaluate = ((DinamicASTNode) this.children.get(i)).evaluate();
            if (evaluate != null) {
                return evaluate;
            }
        }
        return null;
    }

    public DinamicBranchBlockNode() {
        this.type = DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeBranchBlock;
        this.name = "branch";
    }
}
