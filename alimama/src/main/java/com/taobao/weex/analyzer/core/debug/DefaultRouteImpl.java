package com.taobao.weex.analyzer.core.debug;

class DefaultRouteImpl implements IRoute {
    DefaultRouteImpl() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void openURL(android.content.Context r8, java.lang.String r9) {
        /*
            r7 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r9)
            if (r0 != 0) goto L_0x0068
            if (r8 != 0) goto L_0x0009
            goto L_0x0068
        L_0x0009:
            java.lang.String r0 = "com.taobao.android.nav.Nav"
            java.lang.Class r0 = com.taobao.weex.analyzer.utils.ReflectionUtil.tryGetClassForName(r0)
            r1 = 0
            if (r0 == 0) goto L_0x0053
            java.lang.String r2 = "from"
            r3 = 1
            java.lang.Class[] r4 = new java.lang.Class[r3]
            java.lang.Class<android.content.Context> r5 = android.content.Context.class
            r4[r1] = r5
            java.lang.reflect.Method r2 = com.taobao.weex.analyzer.utils.ReflectionUtil.tryGetMethod(r0, r2, r4)
            if (r2 == 0) goto L_0x0053
            r4 = 0
            java.lang.Object[] r5 = new java.lang.Object[r3]
            android.content.Context r6 = r8.getApplicationContext()
            r5[r1] = r6
            java.lang.Object r2 = com.taobao.weex.analyzer.utils.ReflectionUtil.tryInvokeMethod(r4, r2, r5)
            java.lang.String r4 = "toUri"
            java.lang.Class[] r5 = new java.lang.Class[r3]
            java.lang.Class<java.lang.String> r6 = java.lang.String.class
            r5[r1] = r6
            java.lang.reflect.Method r0 = com.taobao.weex.analyzer.utils.ReflectionUtil.tryGetMethod(r0, r4, r5)
            if (r2 == 0) goto L_0x0053
            if (r0 == 0) goto L_0x0053
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.String r4 = r9.trim()
            r3[r1] = r4
            java.lang.Object r0 = com.taobao.weex.analyzer.utils.ReflectionUtil.tryInvokeMethod(r2, r0, r3)
            if (r0 == 0) goto L_0x0053
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Exception -> 0x0053 }
            boolean r0 = r0.booleanValue()     // Catch:{ Exception -> 0x0053 }
            goto L_0x0054
        L_0x0053:
            r0 = 0
        L_0x0054:
            if (r0 != 0) goto L_0x0067
            android.content.Intent r0 = new android.content.Intent
            java.lang.String r1 = "android.intent.action.VIEW"
            r0.<init>(r1)
            android.net.Uri r9 = android.net.Uri.parse(r9)
            r0.setData(r9)
            r8.startActivity(r0)
        L_0x0067:
            return
        L_0x0068:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.analyzer.core.debug.DefaultRouteImpl.openURL(android.content.Context, java.lang.String):void");
    }
}
