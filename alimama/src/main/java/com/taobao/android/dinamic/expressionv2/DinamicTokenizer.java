package com.taobao.android.dinamic.expressionv2;

public class DinamicTokenizer {
    public static final int MAX_CONST_LEN = 255;
    public static final int MAX_MTHNAME_LEN = 255;
    public static final int MAX_VARNAME_LEN = 255;
    public static final char TokenCMA = ',';
    public static final char TokenDLR = '@';
    public static final char TokenESC = '\\';
    public static final char TokenLBR = '{';
    public static final char TokenLPR = '(';
    public static final char TokenRBR = '}';
    public static final char TokenRPR = ')';
    public static final char TokenSEM = ';';
    public static final char TokenSQ = '\'';
    public static final int kNumTokens = 14;
    public static char[] method_name_map = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static boolean[][] state_transition_map = {new boolean[]{false, true, false, false, false, false, true, false, false, false, false, false, false, false, true}, new boolean[]{false, false, true, false, false, false, false, false, false, false, false, false, false, false, true}, new boolean[]{false, false, true, true, false, false, false, false, false, false, false, false, false, false, true}, new boolean[]{false, true, false, false, true, false, true, false, false, false, false, true, false, false, true}, new boolean[]{false, false, false, false, true, true, false, false, false, true, false, false, false, false, true}, new boolean[]{false, false, false, false, false, true, false, false, false, true, true, false, true, true, true}, new boolean[]{false, false, false, false, false, false, false, true, true, false, false, false, false, false, true}, new boolean[]{false, false, false, false, false, false, false, true, true, false, false, false, false, false, true}, new boolean[]{false, false, false, false, false, true, false, false, false, true, true, false, false, true, true}, new boolean[]{false, true, false, false, true, false, true, false, false, false, false, true, false, false, true}, new boolean[]{false, true, false, false, false, false, true, false, false, false, false, false, false, false, true}, new boolean[]{false, true, false, false, false, false, true, false, false, false, false, false, false, false, true}, new boolean[]{false, false, false, false, false, true, false, false, false, true, false, false, false, true, true}, new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}, new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, true}};
    public static char[] var_name_map = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.util.Pair<java.util.List, java.util.List> tokensWithExpr2(java.lang.String r25) {
        /*
            r24 = this;
            r0 = 0
            if (r25 == 0) goto L_0x0279
            boolean r2 = r25.isEmpty()
            if (r2 == 0) goto L_0x000b
            goto L_0x0279
        L_0x000b:
            char[] r1 = r25.toCharArray()
            int r2 = r1.length
            r3 = 0
            char r4 = r1[r3]
            r5 = 64
            if (r4 == r5) goto L_0x001c
            r5 = 39
            if (r4 == r5) goto L_0x001c
            return r0
        L_0x001c:
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            java.lang.StringBuffer r6 = new java.lang.StringBuffer
            r6.<init>()
            java.lang.StringBuffer r7 = new java.lang.StringBuffer
            r7.<init>()
            java.lang.StringBuffer r8 = new java.lang.StringBuffer
            r8.<init>()
            java.util.Stack r9 = new java.util.Stack
            r9.<init>()
            r13 = r6
            r12 = r7
            r14 = r8
            r6 = 0
            r7 = 0
            r8 = 0
            r10 = 0
            r11 = 0
        L_0x0042:
            r16 = 1
            r3 = 14
            if (r6 >= r2) goto L_0x023b
            if (r7 == r3) goto L_0x023b
            r17 = 8
            r18 = 5
            r19 = 3
            r20 = 2
            r21 = 4
            r22 = 12
            r23 = 11
            if (r8 == 0) goto L_0x005d
            r8 = 0
            goto L_0x0235
        L_0x005d:
            char r15 = r1[r6]
            r0 = 44
            r3 = 255(0xff, float:3.57E-43)
            if (r15 == r0) goto L_0x01fa
            r0 = 59
            if (r15 == r0) goto L_0x01da
            r0 = 64
            if (r15 == r0) goto L_0x01c4
            r0 = 92
            if (r15 == r0) goto L_0x01bc
            r0 = 123(0x7b, float:1.72E-43)
            if (r15 == r0) goto L_0x0183
            r0 = 125(0x7d, float:1.75E-43)
            if (r15 == r0) goto L_0x014a
            switch(r15) {
                case 39: goto L_0x00f9;
                case 40: goto L_0x00d4;
                case 41: goto L_0x00b3;
                default: goto L_0x007c;
            }
        L_0x007c:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r20]
            if (r0 == 0) goto L_0x0090
            char[] r0 = method_name_map
            char r0 = r0[r15]
            if (r0 == 0) goto L_0x0090
            r13.append(r15)
            r7 = 2
            goto L_0x0235
        L_0x0090:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r21]
            if (r0 == 0) goto L_0x00a4
            char[] r0 = var_name_map
            char r0 = r0[r15]
            if (r0 == 0) goto L_0x00a4
            r12.append(r15)
            r7 = 4
            goto L_0x0235
        L_0x00a4:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            r3 = 7
            boolean r0 = r0[r3]
            if (r0 == 0) goto L_0x00f5
            r14.append(r15)
            r7 = 7
            goto L_0x0235
        L_0x00b3:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r22]
            if (r0 == 0) goto L_0x00f5
            java.lang.Integer r0 = java.lang.Integer.valueOf(r22)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            int r11 = r11 + -1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r22)
            r9.push(r0)
            r7 = 12
            goto L_0x0235
        L_0x00d4:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r23]
            if (r0 == 0) goto L_0x00f5
            java.lang.Integer r0 = java.lang.Integer.valueOf(r23)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            int r11 = r11 + 1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r23)
            r9.push(r0)
            r7 = 11
            goto L_0x0235
        L_0x00f5:
            r7 = 14
            goto L_0x0235
        L_0x00f9:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            r15 = 6
            boolean r0 = r0[r15]
            if (r0 == 0) goto L_0x0111
            java.lang.Integer r0 = java.lang.Integer.valueOf(r15)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            r7 = 6
            goto L_0x0235
        L_0x0111:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r17]
            if (r0 == 0) goto L_0x00f5
            int r0 = r14.length()
            if (r0 <= 0) goto L_0x013a
            int r0 = r14.length()
            if (r0 <= r3) goto L_0x0126
            goto L_0x00f5
        L_0x0126:
            r0 = 7
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r4.add(r0)
            java.lang.String r0 = r14.toString()
            r5.add(r0)
            java.lang.StringBuffer r14 = new java.lang.StringBuffer
            r14.<init>()
        L_0x013a:
            java.lang.Integer r0 = java.lang.Integer.valueOf(r17)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            r7 = 8
            goto L_0x0235
        L_0x014a:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r18]
            if (r0 == 0) goto L_0x00f5
            int r0 = r12.length()
            if (r0 <= 0) goto L_0x0172
            int r0 = r12.length()
            if (r0 <= r3) goto L_0x015f
            goto L_0x00f5
        L_0x015f:
            java.lang.Integer r0 = java.lang.Integer.valueOf(r21)
            r4.add(r0)
            java.lang.String r0 = r12.toString()
            r5.add(r0)
            java.lang.StringBuffer r12 = new java.lang.StringBuffer
            r12.<init>()
        L_0x0172:
            int r10 = r10 + -1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r18)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            r7 = 5
            goto L_0x0235
        L_0x0183:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r19]
            if (r0 == 0) goto L_0x00f5
            int r0 = r13.length()
            if (r0 <= 0) goto L_0x00f5
            int r0 = r13.length()
            if (r0 > r3) goto L_0x00f5
            java.lang.Integer r0 = java.lang.Integer.valueOf(r20)
            r4.add(r0)
            java.lang.String r0 = r13.toString()
            r5.add(r0)
            java.lang.StringBuffer r0 = new java.lang.StringBuffer
            r0.<init>()
            int r10 = r10 + 1
            java.lang.Integer r3 = java.lang.Integer.valueOf(r19)
            r4.add(r3)
            java.lang.String r3 = " "
            r5.add(r3)
            r13 = r0
            r7 = 3
            goto L_0x0235
        L_0x01bc:
            r0 = 7
            if (r7 == r0) goto L_0x01c2
            r0 = 6
            if (r7 != r0) goto L_0x00f5
        L_0x01c2:
            r8 = 1
            goto L_0x0235
        L_0x01c4:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r16]
            if (r0 == 0) goto L_0x00f5
            java.lang.Integer r0 = java.lang.Integer.valueOf(r16)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            r7 = 1
            goto L_0x0235
        L_0x01da:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            r3 = 10
            boolean r0 = r0[r3]
            if (r0 == 0) goto L_0x00f5
            java.lang.Integer r0 = java.lang.Integer.valueOf(r3)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r3)
            r9.push(r0)
            r7 = 10
            goto L_0x0235
        L_0x01fa:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            r7 = 9
            boolean r0 = r0[r7]
            if (r0 == 0) goto L_0x00f5
            int r0 = r12.length()
            if (r0 <= 0) goto L_0x0225
            int r0 = r12.length()
            if (r0 <= r3) goto L_0x0212
            goto L_0x00f5
        L_0x0212:
            java.lang.Integer r0 = java.lang.Integer.valueOf(r21)
            r4.add(r0)
            java.lang.String r0 = r12.toString()
            r5.add(r0)
            java.lang.StringBuffer r12 = new java.lang.StringBuffer
            r12.<init>()
        L_0x0225:
            r0 = 9
            java.lang.Integer r3 = java.lang.Integer.valueOf(r0)
            r4.add(r3)
            java.lang.String r3 = " "
            r5.add(r3)
            r7 = 9
        L_0x0235:
            int r6 = r6 + 1
            r0 = 0
            r3 = 0
            goto L_0x0042
        L_0x023b:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            r1 = 13
            boolean r0 = r0[r1]
            if (r0 == 0) goto L_0x0247
            r7 = 13
        L_0x0247:
            if (r10 != 0) goto L_0x0277
            if (r11 == 0) goto L_0x024c
            goto L_0x0277
        L_0x024c:
            int r0 = r9.size()
            if (r0 <= 0) goto L_0x026c
            r0 = r0 & 1
            if (r0 != 0) goto L_0x026c
        L_0x0256:
            boolean r0 = r9.empty()
            if (r0 != 0) goto L_0x026c
            java.lang.Object r0 = r9.pop()
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r1 = 10
            if (r0 == r1) goto L_0x0256
            r0 = 0
            return r0
        L_0x026c:
            r0 = 0
            r1 = 14
            if (r7 == r1) goto L_0x0276
            android.util.Pair r0 = android.util.Pair.create(r4, r5)
            return r0
        L_0x0276:
            return r0
        L_0x0277:
            r0 = 0
            return r0
        L_0x0279:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.expressionv2.DinamicTokenizer.tokensWithExpr2(java.lang.String):android.util.Pair");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.util.Pair<java.util.List, java.util.List> tokensWithExpr(java.lang.String r26) {
        /*
            r25 = this;
            r0 = 0
            if (r26 == 0) goto L_0x02f9
            boolean r2 = r26.isEmpty()
            if (r2 == 0) goto L_0x000b
            goto L_0x02f9
        L_0x000b:
            char[] r1 = r26.toCharArray()
            int r2 = r1.length
            r3 = 0
            char r4 = r1[r3]
            r5 = 64
            if (r4 == r5) goto L_0x001c
            r5 = 39
            if (r4 == r5) goto L_0x001c
            return r0
        L_0x001c:
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.util.Stack r9 = new java.util.Stack
            r9.<init>()
            r14 = r6
            r13 = r7
            r3 = r8
            r6 = 0
            r7 = 0
            r8 = 0
            r11 = 1
            r12 = 0
            r15 = 0
        L_0x0043:
            r0 = 14
            if (r6 >= r2) goto L_0x02bb
            if (r7 == r0) goto L_0x02bb
            r16 = 8
            r17 = 5
            r18 = 3
            r19 = 2
            r20 = 4
            r21 = 12
            r22 = 11
            if (r8 == 0) goto L_0x005f
            r24 = r1
            r0 = 1
            r8 = 0
            goto L_0x02b6
        L_0x005f:
            char r10 = r1[r6]
            if (r11 == 0) goto L_0x007b
            r0 = 32
            if (r10 != r0) goto L_0x007b
            r23 = r8
            r0 = 0
        L_0x006a:
            r8 = 32
            if (r10 != r8) goto L_0x0077
            int r6 = r6 + 1
            if (r6 >= r2) goto L_0x0077
            int r0 = r0 + 1
            char r10 = r1[r6]
            goto L_0x006a
        L_0x0077:
            if (r6 < r2) goto L_0x007e
            goto L_0x02bb
        L_0x007b:
            r23 = r8
            r0 = 0
        L_0x007e:
            r8 = 44
            r24 = r1
            r1 = 255(0xff, float:3.57E-43)
            if (r10 == r8) goto L_0x0273
            r8 = 59
            if (r10 == r8) goto L_0x024b
            r8 = 64
            if (r10 == r8) goto L_0x022c
            r8 = 92
            if (r10 == r8) goto L_0x0222
            r8 = 123(0x7b, float:1.72E-43)
            if (r10 == r8) goto L_0x01dc
            r8 = 125(0x7d, float:1.75E-43)
            if (r10 == r8) goto L_0x0196
            switch(r10) {
                case 39: goto L_0x0133;
                case 40: goto L_0x0108;
                case 41: goto L_0x00e4;
                default: goto L_0x009d;
            }
        L_0x009d:
            boolean[][] r1 = state_transition_map
            r1 = r1[r7]
            boolean r1 = r1[r19]
            if (r1 == 0) goto L_0x00b8
            char[] r1 = method_name_map
            char r1 = r1[r10]
            if (r1 == 0) goto L_0x00b8
            if (r0 <= 0) goto L_0x00af
            goto L_0x012c
        L_0x00af:
            r14.append(r10)
            r8 = r23
            r0 = 1
            r7 = 2
            goto L_0x02b6
        L_0x00b8:
            boolean[][] r1 = state_transition_map
            r1 = r1[r7]
            boolean r1 = r1[r20]
            if (r1 == 0) goto L_0x00d2
            char[] r1 = var_name_map
            char r1 = r1[r10]
            if (r1 == 0) goto L_0x00d2
            if (r0 <= 0) goto L_0x00c9
            goto L_0x012c
        L_0x00c9:
            r13.append(r10)
            r8 = r23
            r0 = 1
            r7 = 4
            goto L_0x02b6
        L_0x00d2:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            r1 = 7
            boolean r0 = r0[r1]
            if (r0 == 0) goto L_0x012c
            r3.append(r10)
            r8 = r23
            r0 = 1
            r7 = 7
            goto L_0x02b6
        L_0x00e4:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r21]
            if (r0 == 0) goto L_0x012c
            java.lang.Integer r0 = java.lang.Integer.valueOf(r21)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            int r12 = r12 + -1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r21)
            r9.push(r0)
            r8 = r23
            r0 = 1
            r7 = 12
            goto L_0x02b6
        L_0x0108:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r22]
            if (r0 == 0) goto L_0x012c
            java.lang.Integer r0 = java.lang.Integer.valueOf(r22)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            int r12 = r12 + 1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r22)
            r9.push(r0)
            r8 = r23
            r0 = 1
            r7 = 11
            goto L_0x02b6
        L_0x012c:
            r8 = r23
            r0 = 1
            r7 = 14
            goto L_0x02b6
        L_0x0133:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            r8 = 6
            boolean r0 = r0[r8]
            if (r0 == 0) goto L_0x0154
            java.lang.Integer r0 = java.lang.Integer.valueOf(r8)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            java.lang.String r0 = "TokenizerState.kTokenizerStateConstBegin"
            com.taobao.android.dinamic.log.DinamicLog.print(r0)
            r8 = r23
            r0 = 1
            r7 = 6
            r11 = 0
            goto L_0x02b6
        L_0x0154:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r16]
            if (r0 == 0) goto L_0x012c
            int r0 = r3.length()
            if (r0 <= r1) goto L_0x0163
            goto L_0x012c
        L_0x0163:
            r0 = 7
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r4.add(r0)
            java.lang.String r0 = r3.toString()
            r5.add(r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "TokenizerState.kTokenizerStateConstName"
            com.taobao.android.dinamic.log.DinamicLog.print(r1)
            java.lang.Integer r1 = java.lang.Integer.valueOf(r16)
            r4.add(r1)
            java.lang.String r1 = " "
            r5.add(r1)
            java.lang.String r1 = "TokenizerState.kTokenizerStateConstEnd"
            com.taobao.android.dinamic.log.DinamicLog.print(r1)
            r3 = r0
            r8 = r23
            r0 = 1
            r7 = 8
            r11 = 1
            goto L_0x02b6
        L_0x0196:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r17]
            if (r0 == 0) goto L_0x012c
            int r0 = r13.length()
            if (r0 <= 0) goto L_0x01c3
            int r0 = r13.length()
            if (r0 <= r1) goto L_0x01ab
            goto L_0x012c
        L_0x01ab:
            java.lang.Integer r0 = java.lang.Integer.valueOf(r20)
            r4.add(r0)
            java.lang.String r0 = r13.toString()
            r5.add(r0)
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r0 = "TokenizerState.kTokenizerStateVarName"
            com.taobao.android.dinamic.log.DinamicLog.print(r0)
        L_0x01c3:
            int r15 = r15 + -1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r17)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            java.lang.String r0 = "TokenizerState.kTokenizerStateMethodBodyEnd"
            com.taobao.android.dinamic.log.DinamicLog.print(r0)
            r8 = r23
            r0 = 1
            r7 = 5
            goto L_0x02b6
        L_0x01dc:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            boolean r0 = r0[r18]
            if (r0 == 0) goto L_0x012c
            int r0 = r14.length()
            if (r0 <= 0) goto L_0x012c
            int r0 = r14.length()
            if (r0 > r1) goto L_0x012c
            java.lang.Integer r0 = java.lang.Integer.valueOf(r19)
            r4.add(r0)
            java.lang.String r0 = r14.toString()
            r5.add(r0)
            java.lang.String r0 = "TokenizerState.kTokenizerStateMethodName"
            com.taobao.android.dinamic.log.DinamicLog.print(r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            int r15 = r15 + 1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r18)
            r4.add(r1)
            java.lang.String r1 = " "
            r5.add(r1)
            java.lang.String r1 = "TokenizerState.kTokenizerStateMethodBodyBegin"
            com.taobao.android.dinamic.log.DinamicLog.print(r1)
            r14 = r0
            r8 = r23
            r0 = 1
            r7 = 3
            goto L_0x02b6
        L_0x0222:
            r0 = 7
            if (r7 == r0) goto L_0x0228
            r0 = 6
            if (r7 != r0) goto L_0x012c
        L_0x0228:
            r0 = 1
            r8 = 1
            goto L_0x02b6
        L_0x022c:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            r1 = 1
            boolean r0 = r0[r1]
            if (r0 == 0) goto L_0x012c
            java.lang.Integer r0 = java.lang.Integer.valueOf(r1)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            java.lang.String r0 = "TokenizerState.kTokenizerStateMethodBegin"
            com.taobao.android.dinamic.log.DinamicLog.print(r0)
            r8 = r23
            r0 = 1
            r7 = 1
            goto L_0x02b6
        L_0x024b:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            r1 = 10
            boolean r0 = r0[r1]
            if (r0 == 0) goto L_0x012c
            java.lang.Integer r0 = java.lang.Integer.valueOf(r1)
            r4.add(r0)
            java.lang.String r0 = " "
            r5.add(r0)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r1)
            r9.push(r0)
            java.lang.String r0 = "TokenizerState.kTokenizerStateMethodSep"
            com.taobao.android.dinamic.log.DinamicLog.print(r0)
            r8 = r23
            r0 = 1
            r7 = 10
            goto L_0x02b6
        L_0x0273:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            r7 = 9
            boolean r0 = r0[r7]
            if (r0 == 0) goto L_0x012c
            int r0 = r13.length()
            if (r0 <= 0) goto L_0x02a3
            int r0 = r13.length()
            if (r0 <= r1) goto L_0x028b
            goto L_0x012c
        L_0x028b:
            java.lang.Integer r0 = java.lang.Integer.valueOf(r20)
            r4.add(r0)
            java.lang.String r0 = r13.toString()
            r5.add(r0)
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r0 = "TokenizerState.kTokenizerStateVarName"
            com.taobao.android.dinamic.log.DinamicLog.print(r0)
        L_0x02a3:
            r0 = 9
            java.lang.Integer r1 = java.lang.Integer.valueOf(r0)
            r4.add(r1)
            java.lang.String r1 = " "
            r5.add(r1)
            r8 = r23
            r0 = 1
            r7 = 9
        L_0x02b6:
            int r6 = r6 + r0
            r1 = r24
            goto L_0x0043
        L_0x02bb:
            boolean[][] r0 = state_transition_map
            r0 = r0[r7]
            r1 = 13
            boolean r0 = r0[r1]
            if (r0 == 0) goto L_0x02c7
            r7 = 13
        L_0x02c7:
            if (r15 != 0) goto L_0x02f7
            if (r12 == 0) goto L_0x02cc
            goto L_0x02f7
        L_0x02cc:
            int r0 = r9.size()
            if (r0 <= 0) goto L_0x02ec
            r1 = 1
            r0 = r0 & r1
            if (r0 != 0) goto L_0x02ec
        L_0x02d6:
            boolean r0 = r9.empty()
            if (r0 != 0) goto L_0x02ec
            java.lang.Object r0 = r9.pop()
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r1 = 10
            if (r0 == r1) goto L_0x02d6
            r0 = 0
            return r0
        L_0x02ec:
            r0 = 0
            r1 = 14
            if (r7 == r1) goto L_0x02f6
            android.util.Pair r0 = android.util.Pair.create(r4, r5)
            return r0
        L_0x02f6:
            return r0
        L_0x02f7:
            r0 = 0
            return r0
        L_0x02f9:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.expressionv2.DinamicTokenizer.tokensWithExpr(java.lang.String):android.util.Pair");
    }
}
