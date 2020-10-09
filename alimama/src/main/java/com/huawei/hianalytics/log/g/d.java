package com.huawei.hianalytics.log.g;

import android.os.Bundle;

public class d implements e {
    private String a;
    private String b;
    private Bundle c;

    public d(Bundle bundle, String str, String str2) {
        this.c = bundle;
        this.a = str;
        this.b = str2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:53:0x0172  */
    /* JADX WARNING: Removed duplicated region for block: B:59:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a() {
        /*
            r12 = this;
            java.io.File r0 = new java.io.File
            java.lang.String r1 = r12.b
            java.lang.String r2 = r12.a
            r0.<init>(r1, r2)
            long r1 = r0.length()
            r3 = 25600(0x6400, double:1.2648E-319)
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 <= 0) goto L_0x0021
            boolean r1 = r0.delete()
            if (r1 == 0) goto L_0x0021
            java.lang.String r0 = "LoginfoWrite"
            java.lang.String r1 = "EventInfo file is overrun and has been deleted"
            com.huawei.hianalytics.g.b.d(r0, r1)
            return
        L_0x0021:
            android.os.Bundle r1 = r12.c
            java.lang.String r2 = "MetaData"
            java.lang.String r1 = r1.getString(r2)
            android.os.Bundle r2 = r12.c
            java.lang.String r5 = "Eventid"
            java.lang.String r2 = r2.getString(r5)
            java.lang.String r1 = com.huawei.hianalytics.log.e.e.a(r2, r1)
            java.lang.String r5 = ""
            java.lang.String r6 = ""
            java.lang.String r7 = ""
            com.huawei.hianalytics.d.a r8 = com.huawei.hianalytics.d.a.a()
            com.huawei.hianalytics.c.a r8 = r8.b()
            int[] r9 = com.huawei.hianalytics.log.g.d.AnonymousClass1.a
            com.huawei.hianalytics.c.b r10 = r8.a()
            int r10 = r10.ordinal()
            r9 = r9[r10]
            switch(r9) {
                case 1: goto L_0x005d;
                case 2: goto L_0x0058;
                case 3: goto L_0x0053;
                default: goto L_0x0052;
            }
        L_0x0052:
            goto L_0x0061
        L_0x0053:
            java.lang.String r7 = r8.b()
            goto L_0x0061
        L_0x0058:
            java.lang.String r5 = r8.b()
            goto L_0x0061
        L_0x005d:
            java.lang.String r6 = r8.b()
        L_0x0061:
            org.json.JSONObject r8 = new org.json.JSONObject
            r8.<init>()
            java.lang.String r9 = "LogVersion"
            java.lang.String r10 = "1.1"
            r8.putOpt(r9, r10)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r9 = "LogSubversion"
            java.lang.String r10 = "1.0"
            r8.putOpt(r9, r10)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r9 = "ProductVersion"
            java.lang.String r10 = android.os.Build.DISPLAY     // Catch:{ JSONException -> 0x00ff }
            r8.putOpt(r9, r10)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r9 = "IMEI"
            java.lang.String r5 = com.huawei.hianalytics.log.e.b.a((java.lang.String) r5)     // Catch:{ JSONException -> 0x00ff }
            r8.putOpt(r9, r5)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r5 = "udid"
            java.lang.String r6 = com.huawei.hianalytics.log.e.b.a((java.lang.String) r6)     // Catch:{ JSONException -> 0x00ff }
            r8.putOpt(r5, r6)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r5 = "sn"
            java.lang.String r6 = com.huawei.hianalytics.log.e.b.a((java.lang.String) r7)     // Catch:{ JSONException -> 0x00ff }
            r8.putOpt(r5, r6)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r5 = "uuid"
            com.huawei.hianalytics.d.a r6 = com.huawei.hianalytics.d.a.a()     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r6 = r6.d()     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r6 = com.huawei.hianalytics.log.e.b.a((java.lang.String) r6)     // Catch:{ JSONException -> 0x00ff }
            r8.putOpt(r5, r6)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r5 = "Eventid"
            r8.putOpt(r5, r2)     // Catch:{ JSONException -> 0x00ff }
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r2 = "HappenTime"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x00ff }
            r7.<init>()     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r9 = ""
            r7.append(r9)     // Catch:{ JSONException -> 0x00ff }
            r7.append(r5)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r5 = r7.toString()     // Catch:{ JSONException -> 0x00ff }
            r8.putOpt(r2, r5)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r2 = "App-Id"
            com.huawei.hianalytics.d.a r5 = com.huawei.hianalytics.d.a.a()     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r5 = r5.e()     // Catch:{ JSONException -> 0x00ff }
            r8.putOpt(r2, r5)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r2 = "App-Ver"
            com.huawei.hianalytics.d.a r5 = com.huawei.hianalytics.d.a.a()     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r5 = r5.f()     // Catch:{ JSONException -> 0x00ff }
            r8.putOpt(r2, r5)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r2 = "Sdk-Name"
            java.lang.String r5 = "hianalytics"
            r8.putOpt(r2, r5)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r2 = "Sdk-Ver"
            java.lang.String r5 = "2.1.4.301"
            r8.putOpt(r2, r5)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r2 = "Device-Type"
            java.lang.String r5 = android.os.Build.MODEL     // Catch:{ JSONException -> 0x00ff }
            r8.putOpt(r2, r5)     // Catch:{ JSONException -> 0x00ff }
            java.lang.String r2 = "Package-Name"
            java.lang.String r5 = com.huawei.hianalytics.a.b.e()     // Catch:{ JSONException -> 0x00ff }
            r8.putOpt(r2, r5)     // Catch:{ JSONException -> 0x00ff }
            goto L_0x0106
        L_0x00ff:
            java.lang.String r2 = "LoginfoWrite"
            java.lang.String r5 = "writerEventLog(): json Exception!Some parameters are added to the error"
            com.huawei.hianalytics.g.b.d(r2, r5)
        L_0x0106:
            java.lang.String r2 = r8.toString()
            r5 = 0
            r6 = 0
            r7 = 9
            java.io.FileOutputStream r8 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0158, UnsupportedEncodingException -> 0x014f, IOException -> 0x0149, all -> 0x0146 }
            r8.<init>(r0)     // Catch:{ FileNotFoundException -> 0x0158, UnsupportedEncodingException -> 0x014f, IOException -> 0x0149, all -> 0x0146 }
            java.io.BufferedWriter r9 = new java.io.BufferedWriter     // Catch:{ FileNotFoundException -> 0x0159, UnsupportedEncodingException -> 0x0150, IOException -> 0x014a }
            java.io.OutputStreamWriter r10 = new java.io.OutputStreamWriter     // Catch:{ FileNotFoundException -> 0x0159, UnsupportedEncodingException -> 0x0150, IOException -> 0x014a }
            java.lang.String r11 = "UTF-8"
            r10.<init>(r8, r11)     // Catch:{ FileNotFoundException -> 0x0159, UnsupportedEncodingException -> 0x0150, IOException -> 0x014a }
            r9.<init>(r10)     // Catch:{ FileNotFoundException -> 0x0159, UnsupportedEncodingException -> 0x0150, IOException -> 0x014a }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ FileNotFoundException -> 0x0144, UnsupportedEncodingException -> 0x0142, IOException -> 0x0140, all -> 0x013d }
            r5.<init>()     // Catch:{ FileNotFoundException -> 0x0144, UnsupportedEncodingException -> 0x0142, IOException -> 0x0140, all -> 0x013d }
            r5.append(r2)     // Catch:{ FileNotFoundException -> 0x0144, UnsupportedEncodingException -> 0x0142, IOException -> 0x0140, all -> 0x013d }
            r2 = 10
            r5.append(r2)     // Catch:{ FileNotFoundException -> 0x0144, UnsupportedEncodingException -> 0x0142, IOException -> 0x0140, all -> 0x013d }
            java.lang.String r2 = r5.toString()     // Catch:{ FileNotFoundException -> 0x0144, UnsupportedEncodingException -> 0x0142, IOException -> 0x0140, all -> 0x013d }
            r9.append(r2)     // Catch:{ FileNotFoundException -> 0x0144, UnsupportedEncodingException -> 0x0142, IOException -> 0x0140, all -> 0x013d }
            r9.append(r1)     // Catch:{ FileNotFoundException -> 0x0144, UnsupportedEncodingException -> 0x0142, IOException -> 0x0140, all -> 0x013d }
            r9.flush()     // Catch:{ FileNotFoundException -> 0x0144, UnsupportedEncodingException -> 0x0142, IOException -> 0x0140, all -> 0x013d }
            com.huawei.hianalytics.log.e.d.a((int) r7, (java.io.Closeable) r9)
            goto L_0x0161
        L_0x013d:
            r0 = move-exception
            r5 = r9
            goto L_0x017b
        L_0x0140:
            r5 = r9
            goto L_0x014a
        L_0x0142:
            r5 = r9
            goto L_0x0150
        L_0x0144:
            r5 = r9
            goto L_0x0159
        L_0x0146:
            r0 = move-exception
            r8 = r5
            goto L_0x017b
        L_0x0149:
            r8 = r5
        L_0x014a:
            java.lang.String r1 = "LoginfoWrite"
            java.lang.String r2 = "writerEventLog,BufferedWriter IO Exception!"
            goto L_0x0154
        L_0x014f:
            r8 = r5
        L_0x0150:
            java.lang.String r1 = "LoginfoWrite"
            java.lang.String r2 = "writerEventLog, Unsupported coding format!"
        L_0x0154:
            com.huawei.hianalytics.g.b.d(r1, r2)     // Catch:{ all -> 0x017a }
            goto L_0x015e
        L_0x0158:
            r8 = r5
        L_0x0159:
            java.lang.String r1 = "LoginfoWrite"
            java.lang.String r2 = "writerEventLog,file not found!"
            goto L_0x0154
        L_0x015e:
            com.huawei.hianalytics.log.e.d.a((int) r7, (java.io.Closeable) r5)
        L_0x0161:
            com.huawei.hianalytics.log.e.d.a((int) r6, (java.io.Closeable) r8)
            long r1 = r0.length()
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 <= 0) goto L_0x0179
            boolean r0 = r0.delete()
            if (r0 == 0) goto L_0x0179
            java.lang.String r0 = "LoginfoWrite"
            java.lang.String r1 = "EventInfo file is overrun and has been deleted"
            com.huawei.hianalytics.g.b.d(r0, r1)
        L_0x0179:
            return
        L_0x017a:
            r0 = move-exception
        L_0x017b:
            com.huawei.hianalytics.log.e.d.a((int) r7, (java.io.Closeable) r5)
            com.huawei.hianalytics.log.e.d.a((int) r6, (java.io.Closeable) r8)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hianalytics.log.g.d.a():void");
    }

    public void run() {
        a();
    }
}
