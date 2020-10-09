package com.alipay.sdk.app;

import android.app.Activity;
import android.os.Bundle;
import com.alipay.sdk.util.l;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AlipayResultActivity extends Activity {
    public static final int a = 9000;
    public static final int b = 5000;
    public static final int c = 4000;
    public static final ConcurrentHashMap<String, WeakReference<b>> d = new ConcurrentHashMap<>();
    private static final Map<String, WeakReference<a>> e = new ConcurrentHashMap();

    public interface a {
        void a(int i, String str, Bundle bundle);
    }

    public interface b {
        void a(int i, String str, String str2);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00a8 A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCreate(android.os.Bundle r7) {
        /*
            r6 = this;
            super.onCreate(r7)
            java.lang.String r7 = "biz"
            java.lang.String r0 = "BSPReturned"
            java.lang.String r1 = ""
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r7, (java.lang.String) r0, (java.lang.String) r1)
            android.content.Intent r7 = r6.getIntent()
            java.lang.String r0 = "session"
            java.lang.String r0 = r7.getStringExtra(r0)     // Catch:{ Throwable -> 0x00ce }
            java.lang.String r1 = "result"
            android.os.Bundle r1 = r7.getBundleExtra(r1)     // Catch:{ Throwable -> 0x00ce }
            java.lang.String r2 = "scene"
            java.lang.String r2 = r7.getStringExtra(r2)     // Catch:{ Throwable -> 0x00ce }
            java.lang.String r3 = "biz"
            java.lang.String r4 = "BSPSession"
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r3, (java.lang.String) r4, (java.lang.String) r0)     // Catch:{ Throwable -> 0x00ce }
            java.lang.String r3 = "mqpSchemePay"
            boolean r2 = android.text.TextUtils.equals(r3, r2)
            if (r2 == 0) goto L_0x0035
            r6.a(r0, r1)
            return
        L_0x0035:
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 != 0) goto L_0x003d
            if (r1 != 0) goto L_0x00a2
        L_0x003d:
            android.net.Uri r2 = r7.getData()
            if (r2 == 0) goto L_0x00a2
            android.net.Uri r7 = r7.getData()     // Catch:{ Throwable -> 0x009a }
            java.lang.String r7 = r7.getQuery()     // Catch:{ Throwable -> 0x009a }
            java.lang.String r2 = new java.lang.String     // Catch:{ Throwable -> 0x009a }
            r3 = 2
            byte[] r7 = android.util.Base64.decode(r7, r3)     // Catch:{ Throwable -> 0x009a }
            java.lang.String r3 = "UTF-8"
            r2.<init>(r7, r3)     // Catch:{ Throwable -> 0x009a }
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ Throwable -> 0x009a }
            r7.<init>(r2)     // Catch:{ Throwable -> 0x009a }
            java.lang.String r2 = "result"
            org.json.JSONObject r2 = r7.getJSONObject(r2)     // Catch:{ Throwable -> 0x009a }
            java.lang.String r3 = "session"
            java.lang.String r7 = r7.getString(r3)     // Catch:{ Throwable -> 0x009a }
            java.lang.String r0 = "biz"
            java.lang.String r3 = "BSPUriSession"
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r0, (java.lang.String) r3, (java.lang.String) r7)     // Catch:{ Throwable -> 0x0095 }
            android.os.Bundle r0 = new android.os.Bundle     // Catch:{ Throwable -> 0x0095 }
            r0.<init>()     // Catch:{ Throwable -> 0x0095 }
            java.util.Iterator r1 = r2.keys()     // Catch:{ Throwable -> 0x008f }
        L_0x0078:
            boolean r3 = r1.hasNext()     // Catch:{ Throwable -> 0x008f }
            if (r3 == 0) goto L_0x008c
            java.lang.Object r3 = r1.next()     // Catch:{ Throwable -> 0x008f }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x008f }
            java.lang.String r4 = r2.getString(r3)     // Catch:{ Throwable -> 0x008f }
            r0.putString(r3, r4)     // Catch:{ Throwable -> 0x008f }
            goto L_0x0078
        L_0x008c:
            r1 = r0
            r0 = r7
            goto L_0x00a2
        L_0x008f:
            r1 = move-exception
            r5 = r0
            r0 = r7
            r7 = r1
            r1 = r5
            goto L_0x009b
        L_0x0095:
            r0 = move-exception
            r5 = r0
            r0 = r7
            r7 = r5
            goto L_0x009b
        L_0x009a:
            r7 = move-exception
        L_0x009b:
            java.lang.String r2 = "biz"
            java.lang.String r3 = "BSPResEx"
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r2, (java.lang.String) r3, (java.lang.Throwable) r7)
        L_0x00a2:
            boolean r7 = android.text.TextUtils.isEmpty(r0)
            if (r7 != 0) goto L_0x00c5
            if (r1 != 0) goto L_0x00ab
            goto L_0x00c5
        L_0x00ab:
            r7 = 9000(0x2328, float:1.2612E-41)
            java.lang.String r2 = "OK"
            a(r0, r7, r2, r1)     // Catch:{ all -> 0x00bb }
            java.lang.String r7 = ""
            com.alipay.sdk.app.statistic.a.b(r6, r7)
            r6.finish()
            goto L_0x00cd
        L_0x00bb:
            r7 = move-exception
            java.lang.String r0 = ""
            com.alipay.sdk.app.statistic.a.b(r6, r0)
            r6.finish()
            throw r7
        L_0x00c5:
            java.lang.String r7 = ""
            com.alipay.sdk.app.statistic.a.b(r6, r7)
            r6.finish()
        L_0x00cd:
            return
        L_0x00ce:
            r7 = move-exception
            java.lang.String r0 = "biz"
            java.lang.String r1 = "BSPSerError"
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r0, (java.lang.String) r1, (java.lang.Throwable) r7)
            r6.finish()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.app.AlipayResultActivity.onCreate(android.os.Bundle):void");
    }

    static void a(String str, int i, String str2, Bundle bundle) {
        WeakReference weakReference = e.get(str);
        if (weakReference != null) {
            e.remove(str);
            a aVar = (a) weakReference.get();
            if (aVar != null) {
                aVar.a(i, str2, bundle);
            }
        }
    }

    private void a(String str, Bundle bundle) {
        b bVar;
        WeakReference weakReference = d.get(str);
        if (weakReference != null) {
            bVar = (b) weakReference.get();
            d.remove(str);
        } else {
            bVar = null;
        }
        if (bVar == null) {
            finish();
            return;
        }
        try {
            bVar.a(bundle.getInt("endCode"), bundle.getString(l.b), bundle.getString("result"));
        } finally {
            finish();
        }
    }
}
