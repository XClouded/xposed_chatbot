package com.alipay.sdk.authjs;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;
import com.alipay.sdk.authjs.a;
import java.util.Timer;
import org.json.JSONException;
import org.json.JSONObject;

public class d {
    /* access modifiers changed from: private */
    public c a;
    private Context b;

    public d(Context context, c cVar) {
        this.b = context;
        this.a = cVar;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x004c A[SYNTHETIC, Splitter:B:17:0x004c] */
    /* JADX WARNING: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(java.lang.String r6) {
        /*
            r5 = this;
            r0 = 0
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x0045 }
            r1.<init>(r6)     // Catch:{ Exception -> 0x0045 }
            java.lang.String r6 = "clientId"
            java.lang.String r6 = r1.getString(r6)     // Catch:{ Exception -> 0x0045 }
            boolean r2 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x0043 }
            if (r2 == 0) goto L_0x0013
            return
        L_0x0013:
            java.lang.String r2 = "param"
            org.json.JSONObject r2 = r1.getJSONObject(r2)     // Catch:{ Exception -> 0x0043 }
            boolean r3 = r2 instanceof org.json.JSONObject     // Catch:{ Exception -> 0x0043 }
            if (r3 == 0) goto L_0x0020
            r0 = r2
            org.json.JSONObject r0 = (org.json.JSONObject) r0     // Catch:{ Exception -> 0x0043 }
        L_0x0020:
            java.lang.String r2 = "func"
            java.lang.String r2 = r1.getString(r2)     // Catch:{ Exception -> 0x0043 }
            java.lang.String r3 = "bundleName"
            java.lang.String r1 = r1.getString(r3)     // Catch:{ Exception -> 0x0043 }
            com.alipay.sdk.authjs.a r3 = new com.alipay.sdk.authjs.a     // Catch:{ Exception -> 0x0043 }
            java.lang.String r4 = "call"
            r3.<init>(r4)     // Catch:{ Exception -> 0x0043 }
            r3.b(r1)     // Catch:{ Exception -> 0x0043 }
            r3.c(r2)     // Catch:{ Exception -> 0x0043 }
            r3.a((org.json.JSONObject) r0)     // Catch:{ Exception -> 0x0043 }
            r3.a((java.lang.String) r6)     // Catch:{ Exception -> 0x0043 }
            r5.a((com.alipay.sdk.authjs.a) r3)     // Catch:{ Exception -> 0x0043 }
            return
        L_0x0043:
            goto L_0x0046
        L_0x0045:
            r6 = r0
        L_0x0046:
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 != 0) goto L_0x0052
            com.alipay.sdk.authjs.a$a r0 = com.alipay.sdk.authjs.a.C0000a.RUNTIME_ERROR     // Catch:{ JSONException -> 0x0052 }
            r1 = 1
            r5.a(r6, r0, r1)     // Catch:{ JSONException -> 0x0052 }
        L_0x0052:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.authjs.d.a(java.lang.String):void");
    }

    public void a(a aVar) throws JSONException {
        if (aVar != null) {
            if (TextUtils.isEmpty(aVar.d())) {
                a(aVar.b(), a.C0000a.INVALID_PARAMETER, true);
            } else {
                a((Runnable) new e(this, aVar));
            }
        }
    }

    /* access modifiers changed from: private */
    public void a(String str, a.C0000a aVar, boolean z) throws JSONException {
        if (!TextUtils.isEmpty(str)) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("error", aVar.ordinal());
            a aVar2 = new a("callback");
            aVar2.a(jSONObject);
            aVar2.a(str);
            if (z) {
                this.a.a(aVar2);
            } else {
                a(aVar2);
            }
        }
    }

    private static void a(Runnable runnable) {
        if (runnable != null) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                runnable.run();
            } else {
                new Handler(Looper.getMainLooper()).post(runnable);
            }
        }
    }

    /* access modifiers changed from: private */
    public a.C0000a b(a aVar) {
        if (aVar != null && "toast".equals(aVar.d())) {
            c(aVar);
        }
        return a.C0000a.NONE_ERROR;
    }

    private void c(a aVar) {
        JSONObject f = aVar.f();
        String optString = f.optString("content");
        int i = f.optInt("duration") < 2500 ? 0 : 1;
        Toast.makeText(this.b, optString, i).show();
        new Timer().schedule(new f(this, aVar), (long) i);
    }
}
