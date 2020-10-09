package com.taobao.android.dinamicx.expression.ExepressionEvaluation;

import com.taobao.android.dinamicx.expression.parser.DXAbsDinamicDataParser;

public class DXDataParserToDouble extends DXAbsDinamicDataParser {
    private static final double DEFAULT_VALUE = 0.0d;
    public static final long DX_PARSER_TODOUBLE = 6762231815649095238L;

    /* JADX WARNING: Can't wrap try/catch for region: R(3:15|16|17) */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002e, code lost:
        return java.lang.Double.valueOf(DEFAULT_VALUE);
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x002a */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:20:0x0034=Splitter:B:20:0x0034, B:12:0x001f=Splitter:B:12:0x001f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object evalWithArgs(java.lang.Object[] r4, com.taobao.android.dinamicx.DXRuntimeContext r5) {
        /*
            r3 = this;
            r0 = 0
            if (r4 == 0) goto L_0x0034
            int r5 = r4.length     // Catch:{ Exception -> 0x0039 }
            r2 = 1
            if (r5 == r2) goto L_0x0009
            goto L_0x0034
        L_0x0009:
            r5 = 0
            r4 = r4[r5]     // Catch:{ Exception -> 0x0039 }
            boolean r5 = r4 instanceof java.lang.Number     // Catch:{ Exception -> 0x0039 }
            if (r5 == 0) goto L_0x001b
            java.lang.Number r4 = (java.lang.Number) r4     // Catch:{ Exception -> 0x0039 }
            double r4 = r4.doubleValue()     // Catch:{ Exception -> 0x0039 }
            java.lang.Double r4 = java.lang.Double.valueOf(r4)     // Catch:{ Exception -> 0x0039 }
            return r4
        L_0x001b:
            boolean r5 = r4 instanceof java.lang.String     // Catch:{ Exception -> 0x0039 }
            if (r5 == 0) goto L_0x002f
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Throwable -> 0x002a }
            double r4 = java.lang.Double.parseDouble(r4)     // Catch:{ Throwable -> 0x002a }
            java.lang.Double r4 = java.lang.Double.valueOf(r4)     // Catch:{ Throwable -> 0x002a }
            return r4
        L_0x002a:
            java.lang.Double r4 = java.lang.Double.valueOf(r0)     // Catch:{ Exception -> 0x0039 }
            return r4
        L_0x002f:
            java.lang.Double r4 = java.lang.Double.valueOf(r0)
            return r4
        L_0x0034:
            java.lang.Double r4 = java.lang.Double.valueOf(r0)     // Catch:{ Exception -> 0x0039 }
            return r4
        L_0x0039:
            java.lang.Double r4 = java.lang.Double.valueOf(r0)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserToDouble.evalWithArgs(java.lang.Object[], com.taobao.android.dinamicx.DXRuntimeContext):java.lang.Object");
    }
}
