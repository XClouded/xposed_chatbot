package com.uc.webview.export.internal.uc.wa;

/* compiled from: U4Source */
final class e implements Runnable {
    final /* synthetic */ a a;

    e(a aVar) {
        this.a = aVar;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x009b, code lost:
        return;
     */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x013b A[EDGE_INSN: B:101:0x013b->B:72:0x013b ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0119 A[Catch:{ Exception -> 0x00de }] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0131 A[Catch:{ Exception -> 0x00de }] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0140 A[SYNTHETIC, Splitter:B:75:0x0140] */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0164 A[Catch:{ Exception -> 0x00de }] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0181 A[Catch:{ Exception -> 0x00de }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r17 = this;
            r1 = r17
            com.uc.webview.export.internal.uc.wa.a r0 = r1.a     // Catch:{ Throwable -> 0x0194 }
            r0.f()     // Catch:{ Throwable -> 0x0194 }
            com.uc.webview.export.internal.uc.wa.a r2 = r1.a     // Catch:{ Throwable -> 0x0194 }
            monitor-enter(r2)     // Catch:{ Throwable -> 0x0194 }
            com.uc.webview.export.internal.uc.wa.a r0 = r1.a     // Catch:{ all -> 0x0191 }
            android.content.Context r0 = r0.k     // Catch:{ all -> 0x0191 }
            java.lang.String r3 = "UC_WA_STAT"
            r4 = 4
            android.content.SharedPreferences r0 = r0.getSharedPreferences(r3, r4)     // Catch:{ all -> 0x0191 }
            java.lang.String r3 = com.uc.webview.export.internal.uc.wa.a.i()     // Catch:{ all -> 0x0191 }
            r4 = 0
            long r6 = r0.getLong(r3, r4)     // Catch:{ all -> 0x0191 }
            boolean r3 = com.uc.webview.export.internal.uc.wa.a.b     // Catch:{ all -> 0x0191 }
            if (r3 == 0) goto L_0x0048
            java.lang.String r3 = "SDKWaStat"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x0191 }
            java.lang.String r9 = "==handlUpload==last upload time:"
            r8.<init>(r9)     // Catch:{ all -> 0x0191 }
            java.text.SimpleDateFormat r9 = new java.text.SimpleDateFormat     // Catch:{ all -> 0x0191 }
            java.lang.String r10 = "yyyy-MM-dd HH:mm:ss"
            r9.<init>(r10)     // Catch:{ all -> 0x0191 }
            java.util.Date r10 = new java.util.Date     // Catch:{ all -> 0x0191 }
            r10.<init>(r6)     // Catch:{ all -> 0x0191 }
            java.lang.String r9 = r9.format(r10)     // Catch:{ all -> 0x0191 }
            r8.append(r9)     // Catch:{ all -> 0x0191 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x0191 }
            com.uc.webview.export.internal.utility.Log.d(r3, r8)     // Catch:{ all -> 0x0191 }
        L_0x0048:
            int r3 = com.uc.webview.export.internal.uc.wa.a.c     // Catch:{ all -> 0x0191 }
            if (r3 <= 0) goto L_0x004f
            int r3 = com.uc.webview.export.internal.uc.wa.a.c     // Catch:{ all -> 0x0191 }
            goto L_0x0052
        L_0x004f:
            r3 = 43200000(0x2932e00, float:2.1626111E-37)
        L_0x0052:
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0191 }
            int r10 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r10 == 0) goto L_0x009c
            long r10 = r8 - r6
            long r12 = (long) r3     // Catch:{ all -> 0x0191 }
            int r3 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r3 >= 0) goto L_0x009c
            java.util.Calendar r3 = java.util.Calendar.getInstance()     // Catch:{ all -> 0x0191 }
            r3.setTimeInMillis(r6)     // Catch:{ all -> 0x0191 }
            r10 = 11
            int r11 = r3.get(r10)     // Catch:{ all -> 0x0191 }
            r3.setTimeInMillis(r8)     // Catch:{ all -> 0x0191 }
            int r3 = r3.get(r10)     // Catch:{ all -> 0x0191 }
            r10 = 12
            if (r11 < 0) goto L_0x007d
            if (r11 >= r10) goto L_0x007d
            if (r3 >= r10) goto L_0x0083
        L_0x007d:
            if (r11 < r10) goto L_0x008f
            if (r3 < 0) goto L_0x008f
            if (r3 >= r10) goto L_0x008f
        L_0x0083:
            boolean r3 = com.uc.webview.export.internal.uc.wa.a.b     // Catch:{ all -> 0x0191 }
            if (r3 == 0) goto L_0x009c
            java.lang.String r3 = "SDKWaStat"
            java.lang.String r10 = "跨0点或12点"
            com.uc.webview.export.internal.utility.Log.d(r3, r10)     // Catch:{ all -> 0x0191 }
            goto L_0x009c
        L_0x008f:
            boolean r0 = com.uc.webview.export.internal.uc.wa.a.b     // Catch:{ all -> 0x0191 }
            if (r0 == 0) goto L_0x009a
            java.lang.String r0 = "SDKWaStat"
            java.lang.String r3 = "时间间隔小于12小时,不上传"
            com.uc.webview.export.internal.utility.Log.d(r0, r3)     // Catch:{ all -> 0x0191 }
        L_0x009a:
            monitor-exit(r2)     // Catch:{ all -> 0x0191 }
            return
        L_0x009c:
            r3 = 1
            java.lang.String[] r10 = new java.lang.String[r3]     // Catch:{ all -> 0x0191 }
            r11 = 0
            r12 = 0
            r10[r12] = r11     // Catch:{ all -> 0x0191 }
            int r11 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r11 == 0) goto L_0x0160
            com.uc.webview.export.internal.uc.wa.a r11 = r1.a     // Catch:{ all -> 0x0191 }
            java.lang.String r11 = r11.a((android.content.SharedPreferences) r0)     // Catch:{ all -> 0x0191 }
            android.webkit.ValueCallback<java.lang.String> r0 = com.uc.webview.export.internal.SDKFactory.m     // Catch:{ all -> 0x0191 }
            if (r0 == 0) goto L_0x00c3
            com.uc.webview.export.internal.uc.wa.a r0 = r1.a     // Catch:{ all -> 0x0191 }
            android.content.Context r0 = r0.k     // Catch:{ all -> 0x0191 }
            java.lang.String r0 = r0.getPackageName()     // Catch:{ all -> 0x0191 }
            java.lang.String r13 = "com.taobao.taobao"
            boolean r0 = r0.equals(r13)     // Catch:{ all -> 0x0191 }
            if (r0 != 0) goto L_0x013b
        L_0x00c3:
            com.uc.webview.export.internal.uc.wa.a r0 = r1.a     // Catch:{ all -> 0x0191 }
            byte[] r13 = com.uc.webview.export.internal.uc.wa.a.a((com.uc.webview.export.internal.uc.wa.a) r0, (java.lang.String[]) r10)     // Catch:{ all -> 0x0191 }
            if (r13 != 0) goto L_0x00cd
            monitor-exit(r2)     // Catch:{ all -> 0x0191 }
            return
        L_0x00cd:
            android.webkit.ValueCallback<java.lang.String> r0 = com.uc.webview.export.internal.SDKFactory.n     // Catch:{ all -> 0x0191 }
            if (r0 == 0) goto L_0x00e6
            android.webkit.ValueCallback<java.lang.String> r0 = com.uc.webview.export.internal.SDKFactory.n     // Catch:{ Exception -> 0x00de }
            java.lang.String r14 = new java.lang.String     // Catch:{ Exception -> 0x00de }
            java.lang.String r15 = "UTF-8"
            r14.<init>(r13, r15)     // Catch:{ Exception -> 0x00de }
            r0.onReceiveValue(r14)     // Catch:{ Exception -> 0x00de }
            goto L_0x00e6
        L_0x00de:
            r0 = move-exception
            java.lang.String r14 = "SDKWaStat"
            java.lang.String r15 = "byte 转 String异常!"
            com.uc.webview.export.internal.utility.Log.d(r14, r15, r0)     // Catch:{ all -> 0x0191 }
        L_0x00e6:
            byte[] r14 = com.uc.webview.export.internal.uc.wa.f.a((byte[]) r13)     // Catch:{ Exception -> 0x0106 }
            boolean r0 = com.uc.webview.export.internal.uc.wa.a.b     // Catch:{ Exception -> 0x0103 }
            if (r0 == 0) goto L_0x0111
            java.lang.String r0 = "SDKWaStat"
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0103 }
            java.lang.String r15 = "getUploadData encrypt:"
            r13.<init>(r15)     // Catch:{ Exception -> 0x0103 }
            int r15 = r14.length     // Catch:{ Exception -> 0x0103 }
            r13.append(r15)     // Catch:{ Exception -> 0x0103 }
            java.lang.String r13 = r13.toString()     // Catch:{ Exception -> 0x0103 }
            com.uc.webview.export.internal.utility.Log.d(r0, r13)     // Catch:{ Exception -> 0x0103 }
            goto L_0x0111
        L_0x0103:
            r0 = move-exception
            r13 = 1
            goto L_0x0109
        L_0x0106:
            r0 = move-exception
            r14 = r13
            r13 = 0
        L_0x0109:
            java.lang.String r15 = "SDKWaStat"
            java.lang.String r3 = "data encrypt error:"
            com.uc.webview.export.internal.utility.Log.e(r15, r3, r0)     // Catch:{ all -> 0x0191 }
            r3 = r13
        L_0x0111:
            java.lang.String r0 = com.uc.webview.export.internal.uc.wa.a.a((java.lang.String) r11, (boolean) r3)     // Catch:{ all -> 0x0191 }
            boolean r3 = com.uc.webview.export.internal.uc.wa.a.b     // Catch:{ all -> 0x0191 }
            if (r3 == 0) goto L_0x012c
            java.lang.String r3 = "SDKWaStat"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x0191 }
            java.lang.String r13 = "request url:"
            r11.<init>(r13)     // Catch:{ all -> 0x0191 }
            r11.append(r0)     // Catch:{ all -> 0x0191 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x0191 }
            com.uc.webview.export.internal.utility.Log.d(r3, r11)     // Catch:{ all -> 0x0191 }
        L_0x012c:
            r3 = 3
        L_0x012d:
            int r11 = r3 + -1
            if (r3 <= 0) goto L_0x013b
            boolean r3 = com.uc.webview.export.internal.uc.wa.a.b((java.lang.String) r0, (byte[]) r14)     // Catch:{ all -> 0x0191 }
            if (r3 == 0) goto L_0x0139
            r3 = 1
            goto L_0x013c
        L_0x0139:
            r3 = r11
            goto L_0x012d
        L_0x013b:
            r3 = 0
        L_0x013c:
            android.webkit.ValueCallback<java.lang.String> r0 = com.uc.webview.export.internal.SDKFactory.m     // Catch:{ all -> 0x0191 }
            if (r0 == 0) goto L_0x015d
            com.uc.webview.export.internal.uc.wa.a r0 = r1.a     // Catch:{ Exception -> 0x0155 }
            java.lang.String r0 = r0.a((java.lang.String[]) r10)     // Catch:{ Exception -> 0x0155 }
            if (r0 == 0) goto L_0x015d
            java.lang.String r11 = "SDKWaStat"
            com.uc.webview.export.internal.utility.Log.i(r11, r0)     // Catch:{ Exception -> 0x0155 }
            android.webkit.ValueCallback<java.lang.String> r11 = com.uc.webview.export.internal.SDKFactory.m     // Catch:{ Exception -> 0x0155 }
            r11.onReceiveValue(r0)     // Catch:{ Exception -> 0x0155 }
            r16 = 1
            goto L_0x0162
        L_0x0155:
            r0 = move-exception
            java.lang.String r11 = "SDKWaStat"
            java.lang.String r13 = "第三方上传数据出错!"
            com.uc.webview.export.internal.utility.Log.d(r11, r13, r0)     // Catch:{ all -> 0x0191 }
        L_0x015d:
            r16 = r3
            goto L_0x0162
        L_0x0160:
            r16 = 0
        L_0x0162:
            if (r16 == 0) goto L_0x017d
            java.io.File r0 = new java.io.File     // Catch:{ all -> 0x0191 }
            com.uc.webview.export.internal.uc.wa.a r3 = r1.a     // Catch:{ all -> 0x0191 }
            java.lang.String r3 = r3.g()     // Catch:{ all -> 0x0191 }
            java.lang.String r11 = com.uc.webview.export.internal.uc.wa.a.h()     // Catch:{ all -> 0x0191 }
            r0.<init>(r3, r11)     // Catch:{ all -> 0x0191 }
            r0.delete()     // Catch:{ all -> 0x0191 }
            com.uc.webview.export.internal.uc.wa.a r0 = r1.a     // Catch:{ all -> 0x0191 }
            r3 = r10[r12]     // Catch:{ all -> 0x0191 }
            com.uc.webview.export.internal.uc.wa.a.a((com.uc.webview.export.internal.uc.wa.a) r0, (long) r8, (java.lang.String) r3)     // Catch:{ all -> 0x0191 }
        L_0x017d:
            int r0 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x018f
            java.lang.String r0 = "SDKWaStat"
            java.lang.String r3 = "首次不上传数据"
            com.uc.webview.export.internal.utility.Log.d(r0, r3)     // Catch:{ all -> 0x0191 }
            com.uc.webview.export.internal.uc.wa.a r0 = r1.a     // Catch:{ all -> 0x0191 }
            r3 = r10[r12]     // Catch:{ all -> 0x0191 }
            com.uc.webview.export.internal.uc.wa.a.a((com.uc.webview.export.internal.uc.wa.a) r0, (long) r8, (java.lang.String) r3)     // Catch:{ all -> 0x0191 }
        L_0x018f:
            monitor-exit(r2)     // Catch:{ all -> 0x0191 }
            return
        L_0x0191:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0191 }
            throw r0     // Catch:{ Throwable -> 0x0194 }
        L_0x0194:
            r0 = move-exception
            java.lang.String r2 = "SDKWaStat"
            java.lang.String r3 = "handlUpload"
            com.uc.webview.export.internal.utility.Log.i(r2, r3, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.uc.wa.e.run():void");
    }
}
