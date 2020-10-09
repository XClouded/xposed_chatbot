package com.xiaomi.push;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

public abstract class cy {

    public static class a extends cx {
        public a() {
            super(1);
        }

        public String a(Context context, String str, List<ar> list) {
            URL url;
            if (list == null) {
                url = new URL(str);
            } else {
                Uri.Builder buildUpon = Uri.parse(str).buildUpon();
                for (ar next : list) {
                    buildUpon.appendQueryParameter(next.a(), next.b());
                }
                url = new URL(buildUpon.toString());
            }
            return as.a(context, url);
        }
    }

    static int a(int i, int i2) {
        return (((i2 + 243) / 1448) * 132) + 1080 + i + i2;
    }

    static int a(int i, int i2, int i3) {
        return (((i2 + 200) / 1448) * 132) + 1011 + i2 + i + i3;
    }

    private static int a(cx cxVar, String str, List<ar> list, String str2) {
        if (cxVar.a() == 1) {
            return a(str.length(), a(str2));
        }
        if (cxVar.a() != 2) {
            return -1;
        }
        return a(str.length(), a(list), a(str2));
    }

    static int a(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return str.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException unused) {
            return 0;
        }
    }

    static int a(List<ar> list) {
        int i = 0;
        for (ar next : list) {
            if (!TextUtils.isEmpty(next.a())) {
                i += next.a().length();
            }
            if (!TextUtils.isEmpty(next.b())) {
                i += next.b().length();
            }
        }
        return i * 2;
    }

    public static String a(Context context, String str, List<ar> list) {
        return a(context, str, list, new a(), true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:47:0x009f A[SYNTHETIC, Splitter:B:47:0x009f] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00b4 A[Catch:{ MalformedURLException -> 0x00bf }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(android.content.Context r19, java.lang.String r20, java.util.List<com.xiaomi.push.ar> r21, com.xiaomi.push.cx r22, boolean r23) {
        /*
            r1 = r19
            r0 = r20
            r2 = r21
            r3 = r22
            boolean r4 = com.xiaomi.push.as.b(r19)
            r5 = 0
            if (r4 == 0) goto L_0x00c3
            java.util.ArrayList r4 = new java.util.ArrayList     // Catch:{ MalformedURLException -> 0x00bf }
            r4.<init>()     // Catch:{ MalformedURLException -> 0x00bf }
            if (r23 == 0) goto L_0x0026
            com.xiaomi.push.cu r6 = com.xiaomi.push.cu.a()     // Catch:{ MalformedURLException -> 0x00bf }
            com.xiaomi.push.cq r6 = r6.a((java.lang.String) r0)     // Catch:{ MalformedURLException -> 0x00bf }
            if (r6 == 0) goto L_0x0024
            java.util.ArrayList r4 = r6.a((java.lang.String) r0)     // Catch:{ MalformedURLException -> 0x00bf }
        L_0x0024:
            r13 = r6
            goto L_0x0027
        L_0x0026:
            r13 = r5
        L_0x0027:
            boolean r6 = r4.contains(r0)     // Catch:{ MalformedURLException -> 0x00bf }
            if (r6 != 0) goto L_0x0030
            r4.add(r0)     // Catch:{ MalformedURLException -> 0x00bf }
        L_0x0030:
            java.util.Iterator r4 = r4.iterator()     // Catch:{ MalformedURLException -> 0x00bf }
            r6 = r5
        L_0x0035:
            boolean r0 = r4.hasNext()     // Catch:{ MalformedURLException -> 0x00bf }
            if (r0 == 0) goto L_0x00bd
            java.lang.Object r0 = r4.next()     // Catch:{ MalformedURLException -> 0x00bf }
            r14 = r0
            java.lang.String r14 = (java.lang.String) r14     // Catch:{ MalformedURLException -> 0x00bf }
            if (r2 == 0) goto L_0x004b
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ MalformedURLException -> 0x00bf }
            r0.<init>(r2)     // Catch:{ MalformedURLException -> 0x00bf }
            r15 = r0
            goto L_0x004c
        L_0x004b:
            r15 = r5
        L_0x004c:
            long r16 = java.lang.System.currentTimeMillis()     // Catch:{ MalformedURLException -> 0x00bf }
            boolean r0 = r3.a((android.content.Context) r1, (java.lang.String) r14, (java.util.List<com.xiaomi.push.ar>) r15)     // Catch:{ IOException -> 0x009b }
            if (r0 != 0) goto L_0x0058
            goto L_0x00bd
        L_0x0058:
            java.lang.String r12 = r3.a((android.content.Context) r1, (java.lang.String) r14, (java.util.List<com.xiaomi.push.ar>) r15)     // Catch:{ IOException -> 0x009b }
            boolean r0 = android.text.TextUtils.isEmpty(r12)     // Catch:{ IOException -> 0x0097 }
            if (r0 != 0) goto L_0x0078
            if (r13 == 0) goto L_0x00be
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x0076 }
            r0 = 0
            long r8 = r6 - r16
            int r0 = a(r3, r14, r15, r12)     // Catch:{ IOException -> 0x0076 }
            long r10 = (long) r0     // Catch:{ IOException -> 0x0076 }
            r6 = r13
            r7 = r14
            r6.a(r7, r8, r10)     // Catch:{ IOException -> 0x0076 }
            goto L_0x00be
        L_0x0076:
            r0 = move-exception
            goto L_0x009d
        L_0x0078:
            if (r13 == 0) goto L_0x0094
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x0097 }
            r0 = 0
            long r8 = r6 - r16
            int r0 = a(r3, r14, r15, r12)     // Catch:{ IOException -> 0x0097 }
            long r10 = (long) r0
            r0 = 0
            r6 = r13
            r7 = r14
            r18 = r12
            r12 = r0
            r6.a(r7, r8, r10, r12)     // Catch:{ IOException -> 0x0090 }
            goto L_0x00b9
        L_0x0090:
            r0 = move-exception
            r12 = r18
            goto L_0x009d
        L_0x0094:
            r18 = r12
            goto L_0x00b9
        L_0x0097:
            r0 = move-exception
            r18 = r12
            goto L_0x009d
        L_0x009b:
            r0 = move-exception
            r12 = r6
        L_0x009d:
            if (r13 == 0) goto L_0x00b4
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ MalformedURLException -> 0x00bf }
            r8 = 0
            long r8 = r6 - r16
            int r6 = a(r3, r14, r15, r12)     // Catch:{ MalformedURLException -> 0x00bf }
            long r10 = (long) r6     // Catch:{ MalformedURLException -> 0x00bf }
            r6 = r13
            r7 = r14
            r18 = r12
            r12 = r0
            r6.a(r7, r8, r10, r12)     // Catch:{ MalformedURLException -> 0x00bf }
            goto L_0x00b6
        L_0x00b4:
            r18 = r12
        L_0x00b6:
            r0.printStackTrace()     // Catch:{ MalformedURLException -> 0x00bf }
        L_0x00b9:
            r6 = r18
            goto L_0x0035
        L_0x00bd:
            r12 = r6
        L_0x00be:
            return r12
        L_0x00bf:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00c3:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.cy.a(android.content.Context, java.lang.String, java.util.List, com.xiaomi.push.cx, boolean):java.lang.String");
    }
}
