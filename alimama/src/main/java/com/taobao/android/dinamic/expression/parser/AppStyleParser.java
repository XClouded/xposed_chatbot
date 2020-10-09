package com.taobao.android.dinamic.expression.parser;

import android.os.AsyncTask;

public class AppStyleParser extends AbsDinamicDataParser {
    private static final String DELIMITER = " .[]";

    public AppStyleParser() {
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                AppStyleParserManager.getInstance().loadJsonObj();
                return null;
            }
        }.execute(new Void[0]);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0044 A[Catch:{ Exception -> 0x0049 }] */
    /* JADX WARNING: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object evalWithArgs(java.util.List r6, com.taobao.android.dinamic.model.DinamicParams r7) {
        /*
            r5 = this;
            r0 = 0
            if (r6 == 0) goto L_0x005d
            int r1 = r6.size()     // Catch:{ Exception -> 0x0049 }
            if (r1 != 0) goto L_0x000a
            goto L_0x005d
        L_0x000a:
            r1 = 0
            java.lang.Object r2 = r6.get(r1)     // Catch:{ Exception -> 0x0049 }
            if (r2 == 0) goto L_0x0039
            java.util.StringTokenizer r3 = new java.util.StringTokenizer     // Catch:{ Exception -> 0x0049 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0049 }
            java.lang.String r4 = " .[]"
            r3.<init>(r2, r4, r1)     // Catch:{ Exception -> 0x0049 }
            boolean r1 = r3.hasMoreTokens()     // Catch:{ Exception -> 0x0049 }
            if (r1 == 0) goto L_0x0039
            com.taobao.android.dinamic.expression.parser.AppStyleParserManager r1 = com.taobao.android.dinamic.expression.parser.AppStyleParserManager.getInstance()     // Catch:{ Exception -> 0x0049 }
            com.alibaba.fastjson.JSONObject r1 = r1.loadJsonObj()     // Catch:{ Exception -> 0x0049 }
        L_0x002a:
            boolean r2 = r3.hasMoreTokens()     // Catch:{ Exception -> 0x0049 }
            if (r2 == 0) goto L_0x003a
            java.lang.String r2 = r3.nextToken()     // Catch:{ Exception -> 0x0049 }
            java.lang.Object r1 = com.taobao.android.dinamic.expression.parser.resolver.ValueResolverFactory.getValue(r1, r2)     // Catch:{ Exception -> 0x0049 }
            goto L_0x002a
        L_0x0039:
            r1 = r0
        L_0x003a:
            if (r1 == 0) goto L_0x003d
            return r1
        L_0x003d:
            int r2 = r6.size()     // Catch:{ Exception -> 0x0049 }
            r3 = 1
            if (r2 <= r3) goto L_0x0048
            java.lang.Object r1 = r6.get(r3)     // Catch:{ Exception -> 0x0049 }
        L_0x0048:
            return r1
        L_0x0049:
            r6 = move-exception
            r6.printStackTrace()
            com.taobao.android.dinamic.view.ViewResult r6 = r7.getViewResult()
            com.taobao.android.dinamic.view.DinamicError r6 = r6.getDinamicError()
            java.lang.String r7 = "parserException"
            java.lang.String r1 = "AppStyleParser parse error"
            r6.addErrorCodeWithInfo(r7, r1)
            return r0
        L_0x005d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.expression.parser.AppStyleParser.evalWithArgs(java.util.List, com.taobao.android.dinamic.model.DinamicParams):java.lang.Object");
    }
}
