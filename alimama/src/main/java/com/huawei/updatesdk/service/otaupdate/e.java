package com.huawei.updatesdk.service.otaupdate;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class e {
    private static e c = new e();
    private String a;
    private String b;

    private static class a extends AsyncTask<Void, Void, String> {
        private a() {
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v8, resolved type: java.lang.String} */
        /* JADX WARNING: type inference failed for: r8v2 */
        /* JADX WARNING: type inference failed for: r8v5, types: [android.database.Cursor] */
        /* JADX WARNING: type inference failed for: r8v9 */
        /* JADX WARNING: type inference failed for: r8v13 */
        /* JADX WARNING: type inference failed for: r8v15 */
        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0036, code lost:
            r8 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0037, code lost:
            r6 = r1;
            r1 = r8;
            r8 = r0;
            r0 = r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x003c, code lost:
            r8 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x003d, code lost:
            r6 = r0;
            r0 = r8;
            r8 = r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
            r8.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0080, code lost:
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("ServiceZoneUtil", "cursor Execption");
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
            r8.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x008f, code lost:
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("ServiceZoneUtil", "cursor Execption");
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Multi-variable type inference failed */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x003c A[ExcHandler: all (r8v10 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:4:0x001d] */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x004f A[SYNTHETIC, Splitter:B:21:0x004f] */
        /* JADX WARNING: Removed duplicated region for block: B:32:0x007c A[SYNTHETIC, Splitter:B:32:0x007c] */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x008b A[SYNTHETIC, Splitter:B:38:0x008b] */
        /* JADX WARNING: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
        /* renamed from: a */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.String doInBackground(java.lang.Void... r8) {
            /*
                r7 = this;
                java.lang.String r8 = "content://com.huawei.appmarket.commondata/item/1"
                android.net.Uri r1 = android.net.Uri.parse(r8)
                r8 = 0
                com.huawei.updatesdk.sdk.service.a.a r0 = com.huawei.updatesdk.sdk.service.a.a.a()     // Catch:{ Exception -> 0x005d }
                android.content.Context r0 = r0.b()     // Catch:{ Exception -> 0x005d }
                android.content.ContentResolver r0 = r0.getContentResolver()     // Catch:{ Exception -> 0x005d }
                r2 = 0
                r3 = 0
                r4 = 0
                r5 = 0
                android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x005d }
                if (r0 == 0) goto L_0x0046
                boolean r1 = r0.moveToFirst()     // Catch:{ Exception -> 0x0041, all -> 0x003c }
                if (r1 == 0) goto L_0x0046
                java.lang.String r1 = "homecountry"
                int r1 = r0.getColumnIndex(r1)     // Catch:{ Exception -> 0x0041, all -> 0x003c }
                java.lang.String r1 = r0.getString(r1)     // Catch:{ Exception -> 0x0041, all -> 0x003c }
                com.huawei.updatesdk.service.otaupdate.e r8 = com.huawei.updatesdk.service.otaupdate.e.a()     // Catch:{ Exception -> 0x0036, all -> 0x003c }
                r8.b(r1)     // Catch:{ Exception -> 0x0036, all -> 0x003c }
                r8 = r1
                goto L_0x004d
            L_0x0036:
                r8 = move-exception
                r6 = r1
                r1 = r8
                r8 = r0
                r0 = r6
                goto L_0x0060
            L_0x003c:
                r8 = move-exception
                r6 = r0
                r0 = r8
                r8 = r6
                goto L_0x0089
            L_0x0041:
                r1 = move-exception
                r6 = r0
                r0 = r8
                r8 = r6
                goto L_0x0060
            L_0x0046:
                java.lang.String r1 = "ServiceZoneUtil"
                java.lang.String r2 = "cursor == null: "
                android.util.Log.e(r1, r2)     // Catch:{ Exception -> 0x0041, all -> 0x003c }
            L_0x004d:
                if (r0 == 0) goto L_0x0088
                r0.close()     // Catch:{ Exception -> 0x0053 }
                goto L_0x0088
            L_0x0053:
                java.lang.String r0 = "ServiceZoneUtil"
                java.lang.String r1 = "cursor Execption"
                com.huawei.updatesdk.sdk.a.c.a.a.a.d(r0, r1)
                goto L_0x0088
            L_0x005b:
                r0 = move-exception
                goto L_0x0089
            L_0x005d:
                r0 = move-exception
                r1 = r0
                r0 = r8
            L_0x0060:
                java.lang.String r2 = "ServiceZoneUtil"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x005b }
                r3.<init>()     // Catch:{ all -> 0x005b }
                java.lang.String r4 = "close cursor error: "
                r3.append(r4)     // Catch:{ all -> 0x005b }
                java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x005b }
                r3.append(r1)     // Catch:{ all -> 0x005b }
                java.lang.String r1 = r3.toString()     // Catch:{ all -> 0x005b }
                android.util.Log.e(r2, r1)     // Catch:{ all -> 0x005b }
                if (r8 == 0) goto L_0x0087
                r8.close()     // Catch:{ Exception -> 0x0080 }
                goto L_0x0087
            L_0x0080:
                java.lang.String r8 = "ServiceZoneUtil"
                java.lang.String r1 = "cursor Execption"
                com.huawei.updatesdk.sdk.a.c.a.a.a.d(r8, r1)
            L_0x0087:
                r8 = r0
            L_0x0088:
                return r8
            L_0x0089:
                if (r8 == 0) goto L_0x0096
                r8.close()     // Catch:{ Exception -> 0x008f }
                goto L_0x0096
            L_0x008f:
                java.lang.String r8 = "ServiceZoneUtil"
                java.lang.String r1 = "cursor Execption"
                com.huawei.updatesdk.sdk.a.c.a.a.a.d(r8, r1)
            L_0x0096:
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.service.otaupdate.e.a.doInBackground(java.lang.Void[]):java.lang.String");
        }
    }

    private e() {
    }

    public static e a() {
        return c;
    }

    public void a(Context context) {
        this.b = null;
        if (com.huawei.updatesdk.support.c.a.a(context)) {
            a aVar = new a();
            aVar.executeOnExecutor(Executors.newSingleThreadExecutor(), new Void[0]);
            try {
                aVar.get(1000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                aVar.cancel(true);
                Log.e("ServiceZoneUtil", "init AccountZone error: " + e.toString());
            }
        }
    }

    public void a(String str) {
        this.a = str;
    }

    public String b() {
        return this.a;
    }

    public void b(String str) {
        this.b = str;
    }

    public String c() {
        return !TextUtils.isEmpty(this.a) ? this.a : this.b;
    }
}
