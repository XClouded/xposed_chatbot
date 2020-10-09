package com.huawei.hianalytics.f.f;

import android.os.Build;
import android.text.TextUtils;
import com.huawei.hianalytics.a.b;
import com.huawei.hianalytics.f.b.k;
import com.huawei.hianalytics.h.c;
import com.huawei.hianalytics.h.d;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

final class l implements j {
    private static l a;

    private l() {
    }

    public static j a() {
        return b();
    }

    private Map<String, String> a(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("App-Id", b.f());
        hashMap.put("App-Ver", b.g());
        hashMap.put("Sdk-Name", "hianalytics");
        hashMap.put("Sdk-Ver", "2.1.4.301");
        hashMap.put("Device-Type", Build.MODEL);
        hashMap.put("servicetag", str);
        com.huawei.hianalytics.g.b.b("HiAnalytics/event", "sendData RequestId : %s", str2);
        hashMap.put("Request-Id", str2);
        return hashMap;
    }

    private static synchronized j b() {
        l lVar;
        synchronized (l.class) {
            if (a == null) {
                a = new l();
            }
            lVar = a;
        }
        return lVar;
    }

    public String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("isoCode", str);
            d a2 = c.a("{url}/getServerInfo".replace("{url}", "https://metrics.data.hicloud.com:6447"), jSONObject.toString());
            k kVar = new k();
            kVar.a(a2.b());
            com.huawei.hianalytics.g.b.b("HiAnalytics/event", "get server add response err code: %s", kVar.a());
            return kVar.b();
        } catch (JSONException unused) {
            com.huawei.hianalytics.g.b.c("NetHandler", "getUploadServerAddr(): JSON structure Exception!");
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x00b5 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean a(byte[] r7, java.lang.String r8, java.lang.String r9, java.lang.String r10) {
        /*
            r6 = this;
            java.lang.String r0 = com.huawei.hianalytics.a.c.c(r9, r8)
            java.lang.String r1 = ""
            java.lang.String r2 = "preins"
            boolean r2 = r2.equals(r8)
            r3 = 2
            r4 = 1
            r5 = 0
            if (r2 == 0) goto L_0x0045
            java.lang.String r0 = com.huawei.hianalytics.a.b.j()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x0038
            java.lang.String r7 = "NetHandler"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "PerCollectUrl is empty, TAG : %s,TYPE: %s ,reqID:"
            r0.append(r1)
            r0.append(r10)
            java.lang.String r10 = r0.toString()
            java.lang.Object[] r0 = new java.lang.Object[r3]
            r0[r5] = r9
            r0[r4] = r8
        L_0x0034:
            com.huawei.hianalytics.g.b.c(r7, r10, r0)
            return r5
        L_0x0038:
            java.lang.String r0 = "{url}/common/hmshioperbatch"
            java.lang.String r1 = "{url}"
            java.lang.String r2 = com.huawei.hianalytics.a.b.j()
            java.lang.String r1 = r0.replace(r1, r2)
            goto L_0x007d
        L_0x0045:
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 == 0) goto L_0x0056
            java.lang.String r7 = "NetHandler"
            java.lang.String r10 = "No report address,TAG : %s,TYPE: %s "
            java.lang.Object[] r0 = new java.lang.Object[r3]
            r0[r5] = r9
            r0[r4] = r8
            goto L_0x0034
        L_0x0056:
            java.lang.String r2 = "oper"
            boolean r2 = r2.equals(r8)
            if (r2 == 0) goto L_0x0067
            java.lang.String r1 = "{url}/common/hmshioperqrt"
        L_0x0060:
            java.lang.String r2 = "{url}"
            java.lang.String r1 = r1.replace(r2, r0)
            goto L_0x007d
        L_0x0067:
            java.lang.String r2 = "maint"
            boolean r2 = r2.equals(r8)
            if (r2 == 0) goto L_0x0072
            java.lang.String r1 = "{url}/common/hmshimaintqrt"
            goto L_0x0060
        L_0x0072:
            java.lang.String r2 = "diffprivacy"
            boolean r2 = r2.equals(r8)
            if (r2 == 0) goto L_0x007d
            java.lang.String r1 = "{url}/common/common2"
            goto L_0x0060
        L_0x007d:
            java.util.Map r0 = r6.a(r9, r10)
            com.huawei.hianalytics.h.d r7 = com.huawei.hianalytics.h.c.a((java.lang.String) r1, (byte[]) r7, (java.util.Map<java.lang.String, java.lang.String>) r0)
            java.lang.String r0 = "HiAnalytics/event"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "events PostRequest sendevent TYPE : %s, TAG : %s, resultCode: %d ,reqID:"
            r1.append(r2)
            r1.append(r10)
            java.lang.String r10 = r1.toString()
            r1 = 3
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r1[r5] = r8
            r1[r4] = r9
            int r8 = r7.a()
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            r1[r3] = r8
            com.huawei.hianalytics.g.b.b(r0, r10, r1)
            int r7 = r7.a()
            r8 = 200(0xc8, float:2.8E-43)
            if (r7 != r8) goto L_0x00b5
            goto L_0x00b6
        L_0x00b5:
            r4 = 0
        L_0x00b6:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hianalytics.f.f.l.a(byte[], java.lang.String, java.lang.String, java.lang.String):boolean");
    }
}
