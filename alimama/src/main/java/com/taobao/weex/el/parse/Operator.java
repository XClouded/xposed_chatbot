package com.taobao.weex.el.parse;

class Operator extends Token {
    public Token first;
    public Token second;
    public Token self;

    public Operator(String str, int i) {
        super(str, i);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object execute(java.lang.Object r7) {
        /*
            r6 = this;
            java.lang.String r0 = r6.getToken()
            int r1 = r0.hashCode()
            r2 = 0
            r3 = 1
            switch(r1) {
                case 33: goto L_0x00d8;
                case 37: goto L_0x00cd;
                case 42: goto L_0x00c2;
                case 43: goto L_0x00b7;
                case 45: goto L_0x00ac;
                case 46: goto L_0x00a2;
                case 47: goto L_0x0097;
                case 60: goto L_0x008c;
                case 62: goto L_0x0081;
                case 63: goto L_0x0076;
                case 91: goto L_0x006b;
                case 1084: goto L_0x0060;
                case 1216: goto L_0x0054;
                case 1921: goto L_0x0048;
                case 1952: goto L_0x003d;
                case 1983: goto L_0x0031;
                case 3968: goto L_0x0025;
                case 33665: goto L_0x001a;
                case 60573: goto L_0x000f;
                default: goto L_0x000d;
            }
        L_0x000d:
            goto L_0x00e2
        L_0x000f:
            java.lang.String r1 = "==="
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 2
            goto L_0x00e3
        L_0x001a:
            java.lang.String r1 = "!=="
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 4
            goto L_0x00e3
        L_0x0025:
            java.lang.String r1 = "||"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 9
            goto L_0x00e3
        L_0x0031:
            java.lang.String r1 = ">="
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 11
            goto L_0x00e3
        L_0x003d:
            java.lang.String r1 = "=="
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 3
            goto L_0x00e3
        L_0x0048:
            java.lang.String r1 = "<="
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 13
            goto L_0x00e3
        L_0x0054:
            java.lang.String r1 = "&&"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 8
            goto L_0x00e3
        L_0x0060:
            java.lang.String r1 = "!="
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 5
            goto L_0x00e3
        L_0x006b:
            java.lang.String r1 = "["
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 1
            goto L_0x00e3
        L_0x0076:
            java.lang.String r1 = "?"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 6
            goto L_0x00e3
        L_0x0081:
            java.lang.String r1 = ">"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 10
            goto L_0x00e3
        L_0x008c:
            java.lang.String r1 = "<"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 12
            goto L_0x00e3
        L_0x0097:
            java.lang.String r1 = "/"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 17
            goto L_0x00e3
        L_0x00a2:
            java.lang.String r1 = "."
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 0
            goto L_0x00e3
        L_0x00ac:
            java.lang.String r1 = "-"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 15
            goto L_0x00e3
        L_0x00b7:
            java.lang.String r1 = "+"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 14
            goto L_0x00e3
        L_0x00c2:
            java.lang.String r1 = "*"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 16
            goto L_0x00e3
        L_0x00cd:
            java.lang.String r1 = "%"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 18
            goto L_0x00e3
        L_0x00d8:
            java.lang.String r1 = "!"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x00e2
            r1 = 7
            goto L_0x00e3
        L_0x00e2:
            r1 = -1
        L_0x00e3:
            switch(r1) {
                case 0: goto L_0x01e0;
                case 1: goto L_0x01e0;
                case 2: goto L_0x01d3;
                case 3: goto L_0x01d3;
                case 4: goto L_0x01c5;
                case 5: goto L_0x01c5;
                case 6: goto L_0x01ba;
                case 7: goto L_0x01ae;
                case 8: goto L_0x0198;
                case 9: goto L_0x0182;
                case 10: goto L_0x016c;
                case 11: goto L_0x0156;
                case 12: goto L_0x0140;
                case 13: goto L_0x012a;
                case 14: goto L_0x0121;
                case 15: goto L_0x0118;
                case 16: goto L_0x010f;
                case 17: goto L_0x0106;
                case 18: goto L_0x00fd;
                default: goto L_0x00e6;
            }
        L_0x00e6:
            java.lang.IllegalArgumentException r7 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r0 = " operator is not supported"
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            r7.<init>(r0)
            throw r7
        L_0x00fd:
            com.taobao.weex.el.parse.Token r0 = r6.first
            com.taobao.weex.el.parse.Token r1 = r6.second
            java.lang.Object r7 = com.taobao.weex.el.parse.Operators.mod(r0, r1, r7)
            return r7
        L_0x0106:
            com.taobao.weex.el.parse.Token r0 = r6.first
            com.taobao.weex.el.parse.Token r1 = r6.second
            java.lang.Object r7 = com.taobao.weex.el.parse.Operators.div(r0, r1, r7)
            return r7
        L_0x010f:
            com.taobao.weex.el.parse.Token r0 = r6.first
            com.taobao.weex.el.parse.Token r1 = r6.second
            java.lang.Object r7 = com.taobao.weex.el.parse.Operators.mul(r0, r1, r7)
            return r7
        L_0x0118:
            com.taobao.weex.el.parse.Token r0 = r6.first
            com.taobao.weex.el.parse.Token r1 = r6.second
            java.lang.Object r7 = com.taobao.weex.el.parse.Operators.sub(r0, r1, r7)
            return r7
        L_0x0121:
            com.taobao.weex.el.parse.Token r0 = r6.first
            com.taobao.weex.el.parse.Token r1 = r6.second
            java.lang.Object r7 = com.taobao.weex.el.parse.Operators.plus(r0, r1, r7)
            return r7
        L_0x012a:
            com.taobao.weex.el.parse.Token r0 = r6.first
            double r0 = com.taobao.weex.el.parse.Operators.tokenNumber(r0, r7)
            com.taobao.weex.el.parse.Token r4 = r6.second
            double r4 = com.taobao.weex.el.parse.Operators.tokenNumber(r4, r7)
            int r7 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r7 > 0) goto L_0x013b
            r2 = 1
        L_0x013b:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r2)
            return r7
        L_0x0140:
            com.taobao.weex.el.parse.Token r0 = r6.first
            double r0 = com.taobao.weex.el.parse.Operators.tokenNumber(r0, r7)
            com.taobao.weex.el.parse.Token r4 = r6.second
            double r4 = com.taobao.weex.el.parse.Operators.tokenNumber(r4, r7)
            int r7 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r7 >= 0) goto L_0x0151
            r2 = 1
        L_0x0151:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r2)
            return r7
        L_0x0156:
            com.taobao.weex.el.parse.Token r0 = r6.first
            double r0 = com.taobao.weex.el.parse.Operators.tokenNumber(r0, r7)
            com.taobao.weex.el.parse.Token r4 = r6.second
            double r4 = com.taobao.weex.el.parse.Operators.tokenNumber(r4, r7)
            int r7 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r7 < 0) goto L_0x0167
            r2 = 1
        L_0x0167:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r2)
            return r7
        L_0x016c:
            com.taobao.weex.el.parse.Token r0 = r6.first
            double r0 = com.taobao.weex.el.parse.Operators.tokenNumber(r0, r7)
            com.taobao.weex.el.parse.Token r4 = r6.second
            double r4 = com.taobao.weex.el.parse.Operators.tokenNumber(r4, r7)
            int r7 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r7 <= 0) goto L_0x017d
            r2 = 1
        L_0x017d:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r2)
            return r7
        L_0x0182:
            com.taobao.weex.el.parse.Token r0 = r6.first
            boolean r0 = com.taobao.weex.el.parse.Operators.tokenTrue(r0, r7)
            if (r0 != 0) goto L_0x0192
            com.taobao.weex.el.parse.Token r0 = r6.second
            boolean r7 = com.taobao.weex.el.parse.Operators.tokenTrue(r0, r7)
            if (r7 == 0) goto L_0x0193
        L_0x0192:
            r2 = 1
        L_0x0193:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r2)
            return r7
        L_0x0198:
            com.taobao.weex.el.parse.Token r0 = r6.first
            boolean r0 = com.taobao.weex.el.parse.Operators.tokenTrue(r0, r7)
            if (r0 == 0) goto L_0x01a9
            com.taobao.weex.el.parse.Token r0 = r6.second
            boolean r7 = com.taobao.weex.el.parse.Operators.tokenTrue(r0, r7)
            if (r7 == 0) goto L_0x01a9
            r2 = 1
        L_0x01a9:
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r2)
            return r7
        L_0x01ae:
            com.taobao.weex.el.parse.Token r0 = r6.self
            boolean r7 = com.taobao.weex.el.parse.Operators.tokenTrue(r0, r7)
            r7 = r7 ^ r3
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)
            return r7
        L_0x01ba:
            com.taobao.weex.el.parse.Token r0 = r6.self
            com.taobao.weex.el.parse.Token r1 = r6.first
            com.taobao.weex.el.parse.Token r2 = r6.second
            java.lang.Object r7 = com.taobao.weex.el.parse.Operators.condition(r0, r1, r2, r7)
            return r7
        L_0x01c5:
            com.taobao.weex.el.parse.Token r0 = r6.first
            com.taobao.weex.el.parse.Token r1 = r6.second
            boolean r7 = com.taobao.weex.el.parse.Operators.isEquals(r0, r1, r7)
            r7 = r7 ^ r3
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)
            return r7
        L_0x01d3:
            com.taobao.weex.el.parse.Token r0 = r6.first
            com.taobao.weex.el.parse.Token r1 = r6.second
            boolean r7 = com.taobao.weex.el.parse.Operators.isEquals(r0, r1, r7)
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)
            return r7
        L_0x01e0:
            com.taobao.weex.el.parse.Token r0 = r6.first
            com.taobao.weex.el.parse.Token r1 = r6.second
            java.lang.Object r7 = com.taobao.weex.el.parse.Operators.dot(r0, r1, r7)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.el.parse.Operator.execute(java.lang.Object):java.lang.Object");
    }

    public String toString() {
        if (Operators.AND_NOT.equals(getToken())) {
            return "{!" + this.self + "}";
        } else if (this.self == null) {
            return Operators.BLOCK_START_STR + this.first + getToken() + this.second + "}";
        } else {
            return Operators.BLOCK_START_STR + this.self + getToken() + this.first + ":" + this.second + "}";
        }
    }
}
