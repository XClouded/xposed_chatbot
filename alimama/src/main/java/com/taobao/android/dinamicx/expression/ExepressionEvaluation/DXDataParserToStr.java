package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserToStr extends DXAbsDinamicDataParser {
    private static final String DEFAULT_VALUE = "";
    public static final long DX_PARSER_TOSTR = 19624365692481L;

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object evalWithArgs(java.lang.Object[] r8, com.taobao.android.dinamicx.DXRuntimeContext r9) {
        /*
            r7 = this;
            if (r8 == 0) goto L_0x007f
            int r9 = r8.length     // Catch:{ Throwable -> 0x0082 }
            if (r9 == 0) goto L_0x007f
            int r9 = r8.length     // Catch:{ Throwable -> 0x0082 }
            r0 = 2
            if (r9 <= r0) goto L_0x000b
            goto L_0x007f
        L_0x000b:
            r9 = 0
            r0 = r8[r9]     // Catch:{ Throwable -> 0x0082 }
            boolean r1 = r0 instanceof java.lang.String     // Catch:{ Throwable -> 0x0082 }
            if (r1 == 0) goto L_0x0013
            return r0
        L_0x0013:
            int r1 = r8.length     // Catch:{ Throwable -> 0x0082 }
            r2 = 1
            if (r1 == r2) goto L_0x007a
            boolean r1 = r0 instanceof java.lang.Integer     // Catch:{ Throwable -> 0x0082 }
            if (r1 != 0) goto L_0x007a
            boolean r1 = r0 instanceof java.lang.Long     // Catch:{ Throwable -> 0x0082 }
            if (r1 == 0) goto L_0x0020
            goto L_0x007a
        L_0x0020:
            boolean r1 = r0 instanceof java.lang.Number     // Catch:{ Throwable -> 0x0082 }
            if (r1 == 0) goto L_0x0077
            r8 = r8[r2]     // Catch:{ Throwable -> 0x0082 }
            boolean r1 = r8 instanceof java.lang.Number     // Catch:{ Throwable -> 0x0082 }
            r3 = 0
            if (r1 == 0) goto L_0x0033
            java.lang.Number r8 = (java.lang.Number) r8     // Catch:{ Throwable -> 0x0082 }
            long r5 = r8.longValue()     // Catch:{ Throwable -> 0x0082 }
            goto L_0x0045
        L_0x0033:
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x0040 }
            java.lang.Double r8 = java.lang.Double.valueOf(r8)     // Catch:{ Throwable -> 0x0040 }
            long r5 = r8.longValue()     // Catch:{ Throwable -> 0x0040 }
            goto L_0x0045
        L_0x0040:
            r8 = move-exception
            com.taobao.android.dinamicx.exception.DXExceptionUtil.printStack(r8)     // Catch:{ Throwable -> 0x0082 }
            r5 = r3
        L_0x0045:
            int r8 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r8 < 0) goto L_0x0072
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ Throwable -> 0x0082 }
            double r0 = r0.doubleValue()     // Catch:{ Throwable -> 0x0082 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0082 }
            r8.<init>()     // Catch:{ Throwable -> 0x0082 }
            java.lang.String r3 = "%."
            r8.append(r3)     // Catch:{ Throwable -> 0x0082 }
            r8.append(r5)     // Catch:{ Throwable -> 0x0082 }
            java.lang.String r3 = "f"
            r8.append(r3)     // Catch:{ Throwable -> 0x0082 }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x0082 }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0082 }
            java.lang.Double r0 = java.lang.Double.valueOf(r0)     // Catch:{ Throwable -> 0x0082 }
            r2[r9] = r0     // Catch:{ Throwable -> 0x0082 }
            java.lang.String r8 = java.lang.String.format(r8, r2)     // Catch:{ Throwable -> 0x0082 }
            return r8
        L_0x0072:
            java.lang.String r8 = r0.toString()     // Catch:{ Throwable -> 0x0082 }
            return r8
        L_0x0077:
            java.lang.String r8 = ""
            return r8
        L_0x007a:
            java.lang.String r8 = r0.toString()     // Catch:{ Throwable -> 0x0082 }
            return r8
        L_0x007f:
            java.lang.String r8 = ""
            return r8
        L_0x0082:
            java.lang.String r8 = ""
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserToStr.evalWithArgs(java.lang.Object[], com.taobao.android.dinamicx.DXRuntimeContext):java.lang.Object");
    }
}
