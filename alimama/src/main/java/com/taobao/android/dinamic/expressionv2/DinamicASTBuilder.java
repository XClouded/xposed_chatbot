package com.taobao.android.dinamic.expressionv2;

import com.taobao.android.dinamic.model.DinamicParams;

public class DinamicASTBuilder {
    private DinamicParams dinamicParams;

    public DinamicParams getDinamicParams() {
        return this.dinamicParams;
    }

    public void setDinamicParams(DinamicParams dinamicParams2) {
        this.dinamicParams = dinamicParams2;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00d1, code lost:
        r7 = r9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.taobao.android.dinamic.expressionv2.DinamicASTNode parseWithTokens(android.util.Pair r12) {
        /*
            r11 = this;
            r0 = 0
            if (r12 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.lang.Object r1 = r12.first
            java.util.List r1 = (java.util.List) r1
            java.lang.Object r12 = r12.second
            java.util.List r12 = (java.util.List) r12
            int r2 = r1.size()
            r3 = 1
            if (r2 >= r3) goto L_0x0014
            return r0
        L_0x0014:
            java.util.Stack r4 = new java.util.Stack
            r4.<init>()
            r5 = 0
            r7 = r0
            r6 = 0
            r8 = 0
        L_0x001d:
            if (r5 >= r2) goto L_0x0112
            java.lang.Object r9 = r1.get(r5)
            java.lang.Integer r9 = (java.lang.Integer) r9
            int r9 = r9.intValue()
            switch(r9) {
                case 2: goto L_0x00e4;
                case 3: goto L_0x002c;
                case 4: goto L_0x00d3;
                case 5: goto L_0x00bd;
                case 6: goto L_0x002c;
                case 7: goto L_0x00aa;
                case 8: goto L_0x002c;
                case 9: goto L_0x0092;
                case 10: goto L_0x0052;
                case 11: goto L_0x0044;
                case 12: goto L_0x002e;
                default: goto L_0x002c;
            }
        L_0x002c:
            goto L_0x010e
        L_0x002e:
            int r8 = r8 + -1
            boolean r9 = r4.empty()
            if (r9 != 0) goto L_0x010e
            java.lang.Object r9 = r4.peek()
            com.taobao.android.dinamic.expressionv2.DinamicASTNode r9 = (com.taobao.android.dinamic.expressionv2.DinamicASTNode) r9
            r4.pop()
            r9.addChildNode(r7)
            goto L_0x00d1
        L_0x0044:
            int r8 = r8 + 1
            if (r7 == 0) goto L_0x004b
            r4.push(r7)
        L_0x004b:
            com.taobao.android.dinamic.expressionv2.DinamicBranchBlockNode r7 = new com.taobao.android.dinamic.expressionv2.DinamicBranchBlockNode
            r7.<init>()
            goto L_0x010e
        L_0x0052:
            if (r6 == 0) goto L_0x005c
            if (r8 != 0) goto L_0x005c
            java.lang.String r12 = "in method inner, ; and () is match!"
            com.taobao.android.dinamic.log.DinamicLog.print(r12)
            return r0
        L_0x005c:
            com.taobao.android.dinamic.expressionv2.DinamicASTNode$DinamicASTNodeType r9 = r7.type
            com.taobao.android.dinamic.expressionv2.DinamicASTNode$DinamicASTNodeType r10 = com.taobao.android.dinamic.expressionv2.DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeBranchBlock
            if (r9 == r10) goto L_0x010e
            com.taobao.android.dinamic.expressionv2.DinamicASTNode$DinamicASTNodeType r9 = r7.type
            com.taobao.android.dinamic.expressionv2.DinamicASTNode$DinamicASTNodeType r10 = com.taobao.android.dinamic.expressionv2.DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeSerialBlock
            if (r9 != r10) goto L_0x0089
            java.util.List<com.taobao.android.dinamic.expressionv2.DinamicASTNode> r9 = r7.children
            if (r9 != 0) goto L_0x006d
            return r0
        L_0x006d:
            java.util.List<com.taobao.android.dinamic.expressionv2.DinamicASTNode> r9 = r7.children
            int r9 = r9.size()
            java.util.List<com.taobao.android.dinamic.expressionv2.DinamicASTNode> r10 = r7.children
            int r9 = r9 - r3
            java.lang.Object r9 = r10.remove(r9)
            com.taobao.android.dinamic.expressionv2.DinamicASTNode r9 = (com.taobao.android.dinamic.expressionv2.DinamicASTNode) r9
            com.taobao.android.dinamic.expressionv2.DinamicBranchBlockNode r10 = new com.taobao.android.dinamic.expressionv2.DinamicBranchBlockNode
            r10.<init>()
            r10.addChildNode(r9)
            r7.addChildNode(r10)
            goto L_0x010e
        L_0x0089:
            com.taobao.android.dinamic.expressionv2.DinamicBranchBlockNode r9 = new com.taobao.android.dinamic.expressionv2.DinamicBranchBlockNode
            r9.<init>()
            r9.addChildNode(r7)
            goto L_0x00d1
        L_0x0092:
            if (r6 != 0) goto L_0x00a3
            com.taobao.android.dinamic.expressionv2.DinamicASTNode$DinamicASTNodeType r9 = r7.type
            com.taobao.android.dinamic.expressionv2.DinamicASTNode$DinamicASTNodeType r10 = com.taobao.android.dinamic.expressionv2.DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeSerialBlock
            if (r9 == r10) goto L_0x00a3
            com.taobao.android.dinamic.expressionv2.DinamicSerialBlockNode r9 = new com.taobao.android.dinamic.expressionv2.DinamicSerialBlockNode
            r9.<init>()
            r9.addChildNode(r7)
            goto L_0x00d1
        L_0x00a3:
            com.taobao.android.dinamic.expressionv2.DinamicASTNode$DinamicASTNodeType r9 = r7.type
            com.taobao.android.dinamic.expressionv2.DinamicASTNode$DinamicASTNodeType r10 = com.taobao.android.dinamic.expressionv2.DinamicASTNode.DinamicASTNodeType.DinamicASTNodeTypeBranchBlock
            if (r9 != r10) goto L_0x010e
            return r0
        L_0x00aa:
            com.taobao.android.dinamic.expressionv2.DinamicConstNode r9 = new com.taobao.android.dinamic.expressionv2.DinamicConstNode
            r9.<init>()
            java.lang.Object r10 = r12.get(r5)
            java.lang.String r10 = (java.lang.String) r10
            r9.name = r10
            if (r7 == 0) goto L_0x00d1
            r7.addChildNode(r9)
            goto L_0x010e
        L_0x00bd:
            int r6 = r6 + -1
            boolean r9 = r4.empty()
            if (r9 != 0) goto L_0x010e
            java.lang.Object r9 = r4.peek()
            com.taobao.android.dinamic.expressionv2.DinamicASTNode r9 = (com.taobao.android.dinamic.expressionv2.DinamicASTNode) r9
            r4.pop()
            r9.addChildNode(r7)
        L_0x00d1:
            r7 = r9
            goto L_0x010e
        L_0x00d3:
            com.taobao.android.dinamic.expressionv2.DinamicVarNode r9 = new com.taobao.android.dinamic.expressionv2.DinamicVarNode
            r9.<init>()
            java.lang.Object r10 = r12.get(r5)
            java.lang.String r10 = (java.lang.String) r10
            r9.name = r10
            r7.addChildNode(r9)
            goto L_0x010e
        L_0x00e4:
            int r6 = r6 + 1
            if (r7 == 0) goto L_0x0101
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "TokenizerStateMethodName:"
            r9.append(r10)
            com.taobao.android.dinamic.expressionv2.DinamicASTNode$DinamicASTNodeType r10 = r7.type
            r9.append(r10)
            java.lang.String r9 = r9.toString()
            com.taobao.android.dinamic.log.DinamicLog.print(r9)
            r4.push(r7)
        L_0x0101:
            com.taobao.android.dinamic.expressionv2.DinamicMethodNode r7 = new com.taobao.android.dinamic.expressionv2.DinamicMethodNode
            r7.<init>()
            java.lang.Object r9 = r12.get(r5)
            java.lang.String r9 = (java.lang.String) r9
            r7.name = r9
        L_0x010e:
            int r5 = r5 + 1
            goto L_0x001d
        L_0x0112:
            if (r6 == 0) goto L_0x0119
            java.lang.String r12 = "method balance error!"
            com.taobao.android.dinamic.log.DinamicLog.print(r12)
        L_0x0119:
            if (r8 == 0) goto L_0x0120
            java.lang.String r12 = "branch balance error!"
            com.taobao.android.dinamic.log.DinamicLog.print(r12)
        L_0x0120:
            if (r7 == 0) goto L_0x0127
            com.taobao.android.dinamic.model.DinamicParams r12 = r11.dinamicParams
            r7.bindData(r12)
        L_0x0127:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.expressionv2.DinamicASTBuilder.parseWithTokens(android.util.Pair):com.taobao.android.dinamic.expressionv2.DinamicASTNode");
    }
}
