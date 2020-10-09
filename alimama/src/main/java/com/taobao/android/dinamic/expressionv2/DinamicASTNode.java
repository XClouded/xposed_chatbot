package com.taobao.android.dinamic.expressionv2;

import java.util.LinkedList;
import java.util.List;

public class DinamicASTNode {
    public List<DinamicASTNode> children;
    public Object data;
    public String name;
    DinamicASTNode parent;
    public DinamicASTNodeType type;
    int value;

    public enum DinamicASTNodeType {
        DinamicASTNodeTypeNone,
        DinamicASTNodeTypeRoot,
        DinamicASTNodeTypeMethod,
        DinamicASTNodeTypeVar,
        DinamicASTNodeTypeConst,
        DinamicASTNodeTypeBranchBlock,
        DinamicASTNodeTypeSerialBlock
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public DinamicASTNodeType getType() {
        return DinamicASTNodeType.DinamicASTNodeTypeNone;
    }

    public void addChildNode(DinamicASTNode dinamicASTNode) {
        if (dinamicASTNode != null) {
            if (this.children == null) {
                this.children = new LinkedList();
            }
            this.children.add(dinamicASTNode);
        }
    }

    public void bindData(Object obj) {
        if (this.data != obj) {
            this.data = obj;
            if (this.children != null) {
                int size = this.children.size();
                for (int i = 0; i < size; i++) {
                    this.children.get(i).bindData(obj);
                }
            }
        }
    }

    public List<DinamicASTNode> getAllChildren() {
        if (this.children != null) {
            return this.children;
        }
        return null;
    }

    public Object evaluate() {
        return this.name;
    }
}
