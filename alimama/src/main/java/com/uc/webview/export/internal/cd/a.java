package com.uc.webview.export.internal.cd;

import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.i;
import com.uc.webview.export.internal.utility.k;
import java.util.Map;

/* compiled from: U4Source */
public final class a {
    private static Map<String, String> a;
    private static String b;

    public static void a(String str) {
        int indexOf;
        int indexOf2;
        if (!(str == null || str.startsWith("JSON_CMD") || (indexOf2 = str.indexOf("JSON_CMD")) == -1)) {
            str = str.substring(indexOf2);
        }
        if (!k.a(str) && str.startsWith("JSON_CMD")) {
            C0024a.a(str);
        }
        if (!(str == null || str.startsWith("JSON_CD") || (indexOf = str.indexOf("JSON_CD")) == -1)) {
            str = str.substring(indexOf);
        }
        if (!k.a(str) && str.startsWith("JSON_CD")) {
            str.contains("pub_key");
        }
        b = str;
    }

    public static String b(String str) {
        return C0024a.b(str);
    }

    /* renamed from: com.uc.webview.export.internal.cd.a$a  reason: collision with other inner class name */
    /* compiled from: U4Source */
    static class C0024a {
        private static String a = "InnerImpl";
        private static Object b = new Object();
        private static String c = null;
        private static Map<String, String> d = null;
        private static boolean e = false;

        public static void a(String str) {
            if (str.startsWith("JSON_CMD")) {
                synchronized (b) {
                    c = str.substring(8);
                    d = null;
                    e = false;
                }
                try {
                    i.a((Runnable) new b());
                } catch (Exception e2) {
                    Log.e(a, "parser", e2);
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0023, code lost:
            return null;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static java.lang.String b(java.lang.String r3) {
            /*
                a()
                java.lang.String r0 = c
                r1 = 0
                if (r0 == 0) goto L_0x0027
                java.util.Map<java.lang.String, java.lang.String> r0 = d
                if (r0 != 0) goto L_0x000d
                goto L_0x0027
            L_0x000d:
                java.lang.Object r0 = b
                monitor-enter(r0)
                java.lang.String r2 = c     // Catch:{ all -> 0x0024 }
                if (r2 == 0) goto L_0x0022
                java.util.Map<java.lang.String, java.lang.String> r2 = d     // Catch:{ all -> 0x0024 }
                if (r2 == 0) goto L_0x0022
                java.util.Map<java.lang.String, java.lang.String> r1 = d     // Catch:{ all -> 0x0024 }
                java.lang.Object r3 = r1.get(r3)     // Catch:{ all -> 0x0024 }
                java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x0024 }
                monitor-exit(r0)     // Catch:{ all -> 0x0024 }
                return r3
            L_0x0022:
                monitor-exit(r0)     // Catch:{ all -> 0x0024 }
                return r1
            L_0x0024:
                r3 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0024 }
                throw r3
            L_0x0027:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.cd.a.C0024a.b(java.lang.String):java.lang.String");
        }

        /* JADX WARNING: Removed duplicated region for block: B:42:0x00ee A[Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085, all -> 0x00ea }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static void a() {
            /*
                java.lang.String r0 = c
                if (r0 == 0) goto L_0x0102
                java.util.Map<java.lang.String, java.lang.String> r0 = d
                if (r0 != 0) goto L_0x0102
                boolean r0 = e
                if (r0 == 0) goto L_0x000e
                goto L_0x0102
            L_0x000e:
                java.lang.Object r0 = b
                monitor-enter(r0)
                java.lang.String r1 = c     // Catch:{ all -> 0x00ff }
                if (r1 == 0) goto L_0x00fd
                java.util.Map<java.lang.String, java.lang.String> r1 = d     // Catch:{ all -> 0x00ff }
                if (r1 != 0) goto L_0x00fd
                boolean r1 = e     // Catch:{ all -> 0x00ff }
                if (r1 != 0) goto L_0x00fd
                r1 = 0
                r2 = 1
                java.lang.String r3 = a     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.String r5 = ".parser sCD : "
                r4.<init>(r5)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.String r5 = c     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                r4.append(r5)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.String r4 = r4.toString()     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                com.uc.webview.export.internal.utility.Log.d(r3, r4)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.String r3 = "cd_pp_co"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r3)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.util.HashMap r3 = new java.util.HashMap     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                r3.<init>()     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.String r5 = c     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                r4.<init>(r5)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.util.Iterator r5 = r4.keys()     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
            L_0x0049:
                boolean r6 = r5.hasNext()     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                if (r6 == 0) goto L_0x007c
                java.lang.Object r6 = r5.next()     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.String r6 = (java.lang.String) r6     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.Object r7 = r4.get(r6)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.String r7 = r7.toString()     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.String r8 = a     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.String r10 = ".parser key : "
                r9.<init>(r10)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                r9.append(r6)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.String r10 = " value: "
                r9.append(r10)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                r9.append(r7)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.String r9 = r9.toString()     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                com.uc.webview.export.internal.utility.Log.d(r8, r9)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                r3.put(r6, r7)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                goto L_0x0049
            L_0x007c:
                d = r3     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                java.lang.String r3 = "cd_pp_su"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r3)     // Catch:{ ClassCastException -> 0x00ca, JSONException -> 0x00aa, Throwable -> 0x0087, all -> 0x0085 }
                goto L_0x00fd
            L_0x0085:
                r3 = move-exception
                goto L_0x00ec
            L_0x0087:
                r1 = move-exception
                java.lang.String r3 = a     // Catch:{ all -> 0x00ea }
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
                java.lang.String r5 = ".parser cd exception java.lang.Throwable "
                r4.<init>(r5)     // Catch:{ all -> 0x00ea }
                r4.append(r1)     // Catch:{ all -> 0x00ea }
                java.lang.String r1 = r4.toString()     // Catch:{ all -> 0x00ea }
                com.uc.webview.export.internal.utility.Log.d(r3, r1)     // Catch:{ all -> 0x00ea }
                java.lang.String r1 = a     // Catch:{ all -> 0x00ff }
                java.lang.String r3 = ".parser faulure!!"
                com.uc.webview.export.internal.utility.Log.d(r1, r3)     // Catch:{ all -> 0x00ff }
                e = r2     // Catch:{ all -> 0x00ff }
                java.lang.String r1 = "cd_pp_fa"
            L_0x00a6:
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r1)     // Catch:{ all -> 0x00ff }
                goto L_0x00fd
            L_0x00aa:
                r1 = move-exception
                java.lang.String r3 = a     // Catch:{ all -> 0x00ea }
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
                java.lang.String r5 = ".parser cd exception org.json.JSONException "
                r4.<init>(r5)     // Catch:{ all -> 0x00ea }
                r4.append(r1)     // Catch:{ all -> 0x00ea }
                java.lang.String r1 = r4.toString()     // Catch:{ all -> 0x00ea }
                com.uc.webview.export.internal.utility.Log.d(r3, r1)     // Catch:{ all -> 0x00ea }
                java.lang.String r1 = a     // Catch:{ all -> 0x00ff }
                java.lang.String r3 = ".parser faulure!!"
                com.uc.webview.export.internal.utility.Log.d(r1, r3)     // Catch:{ all -> 0x00ff }
                e = r2     // Catch:{ all -> 0x00ff }
                java.lang.String r1 = "cd_pp_fa"
                goto L_0x00a6
            L_0x00ca:
                r1 = move-exception
                java.lang.String r3 = a     // Catch:{ all -> 0x00ea }
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ea }
                java.lang.String r5 = ".parser cd exception java.lang.ClassCastException "
                r4.<init>(r5)     // Catch:{ all -> 0x00ea }
                r4.append(r1)     // Catch:{ all -> 0x00ea }
                java.lang.String r1 = r4.toString()     // Catch:{ all -> 0x00ea }
                com.uc.webview.export.internal.utility.Log.d(r3, r1)     // Catch:{ all -> 0x00ea }
                java.lang.String r1 = a     // Catch:{ all -> 0x00ff }
                java.lang.String r3 = ".parser faulure!!"
                com.uc.webview.export.internal.utility.Log.d(r1, r3)     // Catch:{ all -> 0x00ff }
                e = r2     // Catch:{ all -> 0x00ff }
                java.lang.String r1 = "cd_pp_fa"
                goto L_0x00a6
            L_0x00ea:
                r3 = move-exception
                r1 = 1
            L_0x00ec:
                if (r1 == 0) goto L_0x00fc
                java.lang.String r1 = a     // Catch:{ all -> 0x00ff }
                java.lang.String r4 = ".parser faulure!!"
                com.uc.webview.export.internal.utility.Log.d(r1, r4)     // Catch:{ all -> 0x00ff }
                e = r2     // Catch:{ all -> 0x00ff }
                java.lang.String r1 = "cd_pp_fa"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r1)     // Catch:{ all -> 0x00ff }
            L_0x00fc:
                throw r3     // Catch:{ all -> 0x00ff }
            L_0x00fd:
                monitor-exit(r0)     // Catch:{ all -> 0x00ff }
                return
            L_0x00ff:
                r1 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x00ff }
                throw r1
            L_0x0102:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.cd.a.C0024a.a():void");
        }
    }

    public static Boolean c(String str) {
        String str2 = a == null ? null : a.get(str);
        return Boolean.valueOf(str2 == null ? true : "true".equalsIgnoreCase(str2));
    }
}
