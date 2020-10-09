package com.alipay.sdk.app.statistic;

import android.content.Context;

final class b implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ String b;

    b(Context context, String str) {
        this.a = context;
        this.b = str;
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0023 */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x002b A[Catch:{ IOException -> 0x0033, Throwable -> 0x003c }] */
    /* JADX WARNING: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r4 = this;
            com.alipay.sdk.packet.impl.d r0 = new com.alipay.sdk.packet.impl.d
            r0.<init>()
            android.content.Context r1 = r4.a     // Catch:{ Throwable -> 0x0023 }
            java.lang.String r2 = "alipay_cashier_statistic_record"
            r3 = 0
            java.lang.String r1 = com.alipay.sdk.util.j.b(r1, r2, r3)     // Catch:{ Throwable -> 0x0023 }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x0023 }
            if (r2 != 0) goto L_0x0023
            android.content.Context r2 = r4.a     // Catch:{ Throwable -> 0x0023 }
            com.alipay.sdk.packet.b r1 = r0.a((android.content.Context) r2, (java.lang.String) r1)     // Catch:{ Throwable -> 0x0023 }
            if (r1 == 0) goto L_0x0023
            android.content.Context r1 = r4.a     // Catch:{ Throwable -> 0x0023 }
            java.lang.String r2 = "alipay_cashier_statistic_record"
            com.alipay.sdk.util.j.b(r1, r2)     // Catch:{ Throwable -> 0x0023 }
        L_0x0023:
            java.lang.String r1 = r4.b     // Catch:{ IOException -> 0x0033, Throwable -> 0x003c }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ IOException -> 0x0033, Throwable -> 0x003c }
            if (r1 != 0) goto L_0x003c
            android.content.Context r1 = r4.a     // Catch:{ IOException -> 0x0033, Throwable -> 0x003c }
            java.lang.String r2 = r4.b     // Catch:{ IOException -> 0x0033, Throwable -> 0x003c }
            r0.a((android.content.Context) r1, (java.lang.String) r2)     // Catch:{ IOException -> 0x0033, Throwable -> 0x003c }
            goto L_0x003c
        L_0x0033:
            android.content.Context r0 = r4.a
            java.lang.String r1 = "alipay_cashier_statistic_record"
            java.lang.String r2 = r4.b
            com.alipay.sdk.util.j.a(r0, r1, r2)
        L_0x003c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.app.statistic.b.run():void");
    }
}
