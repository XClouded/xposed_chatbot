package com.uc.webview.export.internal.setup;

import java.util.List;

/* compiled from: U4Source */
public final class e extends UCSubSetupTask<e, e> {
    private List<av> a;

    public e(List<av> list) {
        this.a = list;
    }

    /* JADX WARNING: type inference failed for: r9v0, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r9v10, types: [java.io.File] */
    /* JADX WARNING: type inference failed for: r9v12 */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:44:0x01a6 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x01ae A[Catch:{ Throwable -> 0x01f9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0206  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r21 = this;
            r1 = r21
            java.lang.String r0 = "DeleteCoreTask"
            java.lang.String r2 = "======deleteSo====="
            com.uc.webview.export.internal.utility.Log.d(r0, r2)
            java.lang.String r0 = "CONTEXT"
            java.lang.Object r0 = r1.getOption(r0)
            r2 = r0
            android.content.Context r2 = (android.content.Context) r2
            r3 = 14
            java.lang.String[] r4 = new java.lang.String[r3]
            java.lang.String r0 = "libWebCore_UC.so"
            r5 = 0
            r4[r5] = r0
            java.lang.String r0 = "libV8_UC.so"
            r6 = 1
            r4[r6] = r0
            java.lang.String r0 = "libandroid_uc_40.so"
            r7 = 2
            r4[r7] = r0
            java.lang.String r0 = "libandroid_uc_41.so"
            r7 = 3
            r4[r7] = r0
            java.lang.String r0 = "libandroid_uc_42.so"
            r7 = 4
            r4[r7] = r0
            java.lang.String r0 = "libandroid_uc_43.so"
            r7 = 5
            r4[r7] = r0
            java.lang.String r0 = "libandroid_uc_44.so"
            r7 = 6
            r4[r7] = r0
            java.lang.String r0 = "libandroid_uc_50.so"
            r7 = 7
            r4[r7] = r0
            java.lang.String r0 = "libskia_neon_uc.so"
            r7 = 8
            r4[r7] = r0
            java.lang.String r0 = "libwebviewuc.so"
            r7 = 9
            r4[r7] = r0
            java.lang.String r0 = "libimagehelper.so"
            r7 = 10
            r4[r7] = r0
            java.lang.String r0 = "libvinit.so"
            r7 = 11
            r4[r7] = r0
            java.lang.String r0 = "libInitHelper_UC.so"
            r7 = 12
            r4[r7] = r0
            java.lang.String r0 = "libcrashsdk.so"
            r7 = 13
            r4[r7] = r0
            java.util.List<com.uc.webview.export.internal.setup.av> r0 = r1.a
            java.util.Iterator r7 = r0.iterator()
            r0 = 0
        L_0x0069:
            boolean r8 = r7.hasNext()
            if (r8 == 0) goto L_0x034a
            java.lang.Object r8 = r7.next()
            com.uc.webview.export.internal.setup.av r8 = (com.uc.webview.export.internal.setup.av) r8
            boolean r8 = r8 instanceof com.uc.webview.export.internal.setup.az
            r9 = 0
            if (r8 == 0) goto L_0x0090
            java.util.concurrent.ConcurrentHashMap r8 = r1.mOptions
            java.lang.String r10 = "soFilePath"
            java.lang.Object r8 = r8.get(r10)
            java.lang.String r8 = (java.lang.String) r8
            java.util.concurrent.ConcurrentHashMap r10 = r1.mOptions
            java.lang.String r11 = "resFilePath"
            java.lang.Object r10 = r10.get(r11)
            java.lang.String r10 = (java.lang.String) r10
            r11 = 1
            goto L_0x0093
        L_0x0090:
            r8 = r9
            r10 = r8
            r11 = 0
        L_0x0093:
            if (r8 == 0) goto L_0x00a2
            android.content.pm.ApplicationInfo r12 = r2.getApplicationInfo()
            java.lang.String r12 = r12.nativeLibraryDir
            boolean r12 = r8.equals(r12)
            if (r12 == 0) goto L_0x00a2
            r8 = r9
        L_0x00a2:
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            r12.append(r9)
            java.lang.String r13 = "_"
            r12.append(r13)
            r12.append(r8)
            java.lang.String r13 = "_"
            r12.append(r13)
            r12.append(r10)
            java.lang.String r10 = r12.toString()
            java.lang.String r10 = com.uc.webview.export.cyclone.UCCyclone.getSourceHash(r10)
            java.lang.String r12 = "flags"
            java.io.File r12 = com.uc.webview.export.internal.utility.k.a((android.content.Context) r2, (java.lang.String) r12)
            java.lang.String r13 = "delcore"
            java.io.File r12 = com.uc.webview.export.internal.utility.k.b((java.io.File) r12, (java.lang.String) r13)
            java.io.File r13 = new java.io.File
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r14.append(r10)
            java.lang.String r15 = "_1"
            r14.append(r15)
            java.lang.String r14 = r14.toString()
            r13.<init>(r12, r14)
            java.io.File r14 = new java.io.File
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            r15.append(r10)
            java.lang.String r5 = "_2"
            r15.append(r5)
            java.lang.String r5 = r15.toString()
            r14.<init>(r12, r5)
            java.io.File r5 = new java.io.File
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            r15.append(r10)
            java.lang.String r10 = "_3"
            r15.append(r10)
            java.lang.String r10 = r15.toString()
            r5.<init>(r12, r10)
            boolean r10 = r5.exists()
            if (r10 == 0) goto L_0x011f
            java.lang.String r2 = "DeleteCoreTask"
            java.lang.String r3 = "Skip delete UC files (over 3 times)."
            com.uc.webview.export.internal.utility.Log.d(r2, r3)
            goto L_0x034a
        L_0x011f:
            boolean r10 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r9)
            if (r10 != 0) goto L_0x0159
            java.io.File r10 = com.uc.webview.export.internal.utility.k.a((android.content.Context) r2, (java.lang.String) r9)
            java.lang.String r10 = r10.getAbsolutePath()
            boolean r10 = r9.startsWith(r10)
            if (r10 == 0) goto L_0x0159
            java.io.File r0 = new java.io.File
            r0.<init>(r9)
            boolean r3 = r0.exists()
            if (r3 == 0) goto L_0x0158
            com.uc.webview.export.internal.setup.UCMRunningInfo r3 = com.uc.webview.export.utility.SetupTask.getTotalLoadedUCM()
            com.uc.webview.export.internal.setup.br r4 = r3.ucmPackageInfo
            if (r4 == 0) goto L_0x0155
            com.uc.webview.export.internal.setup.br r4 = r3.ucmPackageInfo
            java.lang.String r4 = r4.dataDir
            if (r4 == 0) goto L_0x0155
            java.io.File r9 = new java.io.File
            com.uc.webview.export.internal.setup.br r3 = r3.ucmPackageInfo
            java.lang.String r3 = r3.dataDir
            r9.<init>(r3)
        L_0x0155:
            com.uc.webview.export.internal.utility.k.a((android.content.Context) r2, (java.io.File) r0, (java.io.File) r9)
        L_0x0158:
            return
        L_0x0159:
            boolean r10 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r8)
            if (r10 != 0) goto L_0x0237
            java.io.File r0 = new java.io.File
            r0.<init>(r8)
            java.io.File r0 = r0.getParentFile()
            r16 = 0
            java.io.File r10 = new java.io.File     // Catch:{ Throwable -> 0x01a6 }
            java.lang.String r15 = "2e67cdbeb4ec133dcc8204d930aa7145"
            r10.<init>(r0, r15)     // Catch:{ Throwable -> 0x01a6 }
            java.io.File r15 = new java.io.File     // Catch:{ Throwable -> 0x01a6 }
            java.lang.String r6 = "299772b0fd1634653ae3c31f366de3f8"
            r15.<init>(r0, r6)     // Catch:{ Throwable -> 0x01a6 }
            boolean r6 = r10.exists()     // Catch:{ Throwable -> 0x01a6 }
            if (r6 == 0) goto L_0x018f
            boolean r6 = r10.isFile()     // Catch:{ Throwable -> 0x01a6 }
            if (r6 == 0) goto L_0x018f
            long r18 = r10.length()     // Catch:{ Throwable -> 0x01a6 }
            int r6 = (r18 > r16 ? 1 : (r18 == r16 ? 0 : -1))
            if (r6 != 0) goto L_0x018f
            r10.delete()     // Catch:{ Throwable -> 0x01a6 }
        L_0x018f:
            boolean r6 = r15.exists()     // Catch:{ Throwable -> 0x01a6 }
            if (r6 == 0) goto L_0x01a6
            boolean r6 = r15.isFile()     // Catch:{ Throwable -> 0x01a6 }
            if (r6 == 0) goto L_0x01a6
            long r18 = r15.length()     // Catch:{ Throwable -> 0x01a6 }
            int r6 = (r18 > r16 ? 1 : (r18 == r16 ? 0 : -1))
            if (r6 != 0) goto L_0x01a6
            r15.delete()     // Catch:{ Throwable -> 0x01a6 }
        L_0x01a6:
            java.io.File[] r0 = r0.listFiles()     // Catch:{ Throwable -> 0x01f9 }
            int r6 = r0.length     // Catch:{ Throwable -> 0x01f9 }
            r10 = 0
        L_0x01ac:
            if (r10 >= r6) goto L_0x0201
            r15 = r0[r10]     // Catch:{ Throwable -> 0x01f9 }
            java.lang.String r9 = r15.getName()     // Catch:{ Throwable -> 0x01f9 }
            java.io.File r18 = r15.getParentFile()     // Catch:{ Throwable -> 0x01f9 }
            java.io.File r19 = r18.getParentFile()     // Catch:{ Throwable -> 0x01f9 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01f9 }
            r3.<init>()     // Catch:{ Throwable -> 0x01f9 }
            r20 = r0
            java.lang.String r0 = r19.getName()     // Catch:{ Throwable -> 0x01f9 }
            r3.append(r0)     // Catch:{ Throwable -> 0x01f9 }
            java.lang.String r0 = "_"
            r3.append(r0)     // Catch:{ Throwable -> 0x01f9 }
            java.lang.String r0 = r18.getName()     // Catch:{ Throwable -> 0x01f9 }
            r3.append(r0)     // Catch:{ Throwable -> 0x01f9 }
            java.lang.String r0 = r3.toString()     // Catch:{ Throwable -> 0x01f9 }
            boolean r0 = r9.startsWith(r0)     // Catch:{ Throwable -> 0x01f9 }
            if (r0 == 0) goto L_0x01f1
            boolean r0 = r15.isFile()     // Catch:{ Throwable -> 0x01f9 }
            if (r0 == 0) goto L_0x01f1
            long r18 = r15.length()     // Catch:{ Throwable -> 0x01f9 }
            int r0 = (r18 > r16 ? 1 : (r18 == r16 ? 0 : -1))
            if (r0 != 0) goto L_0x01f1
            r15.delete()     // Catch:{ Throwable -> 0x01f9 }
        L_0x01f1:
            int r10 = r10 + 1
            r0 = r20
            r3 = 14
            r9 = 0
            goto L_0x01ac
        L_0x01f9:
            r0 = move-exception
            java.lang.String r3 = "DeleteCoreTask"
            java.lang.String r6 = "delete flag:"
            com.uc.webview.export.internal.utility.Log.w(r3, r6, r0)
        L_0x0201:
            r3 = 0
            r6 = 14
        L_0x0204:
            if (r3 >= r6) goto L_0x0235
            r0 = r4[r3]
            java.io.File r9 = new java.io.File     // Catch:{ Throwable -> 0x022a }
            r9.<init>(r8, r0)     // Catch:{ Throwable -> 0x022a }
            boolean r0 = r9.exists()     // Catch:{ Throwable -> 0x022a }
            if (r0 == 0) goto L_0x0216
            r9.delete()     // Catch:{ Throwable -> 0x022a }
        L_0x0216:
            java.lang.String r0 = "DeleteCoreTask"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x022a }
            java.lang.String r15 = "deleteSo:"
            r10.<init>(r15)     // Catch:{ Throwable -> 0x022a }
            r10.append(r9)     // Catch:{ Throwable -> 0x022a }
            java.lang.String r9 = r10.toString()     // Catch:{ Throwable -> 0x022a }
            com.uc.webview.export.internal.utility.Log.d(r0, r9)     // Catch:{ Throwable -> 0x022a }
            goto L_0x0232
        L_0x022a:
            r0 = move-exception
            java.lang.String r9 = "DeleteCoreTask"
            java.lang.String r10 = "deleteSo:"
            com.uc.webview.export.internal.utility.Log.w(r9, r10, r0)
        L_0x0232:
            int r3 = r3 + 1
            goto L_0x0204
        L_0x0235:
            r0 = 1
            goto L_0x0239
        L_0x0237:
            r6 = 14
        L_0x0239:
            if (r11 != 0) goto L_0x0309
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x0265 }
            r3 = 0
            android.util.Pair<java.lang.String, java.lang.String> r8 = r3.coreImplModule     // Catch:{ Throwable -> 0x0265 }
            java.lang.Object r3 = r8.first     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x0265 }
            r0.<init>(r3)     // Catch:{ Throwable -> 0x0265 }
            r0.delete()     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r0 = "DeleteCoreTask"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = "delete dex:"
            r3.<init>(r8)     // Catch:{ Throwable -> 0x0265 }
            r8 = 0
            android.util.Pair<java.lang.String, java.lang.String> r9 = r8.coreImplModule     // Catch:{ Throwable -> 0x0265 }
            java.lang.Object r8 = r9.first     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ Throwable -> 0x0265 }
            r3.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0265 }
            com.uc.webview.export.internal.utility.Log.d(r0, r3)     // Catch:{ Throwable -> 0x0265 }
            goto L_0x026d
        L_0x0265:
            r0 = move-exception
            java.lang.String r3 = "DeleteCoreTask"
            java.lang.String r8 = "deleteSo:"
            com.uc.webview.export.internal.utility.Log.w(r3, r8, r0)
        L_0x026d:
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x0297 }
            r3 = 0
            android.util.Pair<java.lang.String, java.lang.String> r8 = r3.sdkShellModule     // Catch:{ Throwable -> 0x0297 }
            java.lang.Object r3 = r8.first     // Catch:{ Throwable -> 0x0297 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x0297 }
            r0.<init>(r3)     // Catch:{ Throwable -> 0x0297 }
            r0.delete()     // Catch:{ Throwable -> 0x0297 }
            java.lang.String r0 = "DeleteCoreTask"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0297 }
            java.lang.String r8 = "delete dex:"
            r3.<init>(r8)     // Catch:{ Throwable -> 0x0297 }
            r8 = 0
            android.util.Pair<java.lang.String, java.lang.String> r9 = r8.sdkShellModule     // Catch:{ Throwable -> 0x0297 }
            java.lang.Object r8 = r9.first     // Catch:{ Throwable -> 0x0297 }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ Throwable -> 0x0297 }
            r3.append(r8)     // Catch:{ Throwable -> 0x0297 }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0297 }
            com.uc.webview.export.internal.utility.Log.d(r0, r3)     // Catch:{ Throwable -> 0x0297 }
            goto L_0x029f
        L_0x0297:
            r0 = move-exception
            java.lang.String r3 = "DeleteCoreTask"
            java.lang.String r8 = "deleteSo:"
            com.uc.webview.export.internal.utility.Log.w(r3, r8, r0)
        L_0x029f:
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x02cb }
            r3 = 0
            android.util.Pair<java.lang.String, java.lang.String> r8 = r3.coreImplModule     // Catch:{ Throwable -> 0x02cb }
            java.lang.Object r3 = r8.second     // Catch:{ Throwable -> 0x02cb }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x02cb }
            java.lang.String r8 = "dex.dex"
            r0.<init>(r3, r8)     // Catch:{ Throwable -> 0x02cb }
            r0.delete()     // Catch:{ Throwable -> 0x02cb }
            java.lang.String r0 = "DeleteCoreTask"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02cb }
            java.lang.String r8 = "delete odex:"
            r3.<init>(r8)     // Catch:{ Throwable -> 0x02cb }
            r8 = 0
            android.util.Pair<java.lang.String, java.lang.String> r9 = r8.coreImplModule     // Catch:{ Throwable -> 0x02cb }
            java.lang.Object r8 = r9.first     // Catch:{ Throwable -> 0x02cb }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ Throwable -> 0x02cb }
            r3.append(r8)     // Catch:{ Throwable -> 0x02cb }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x02cb }
            com.uc.webview.export.internal.utility.Log.d(r0, r3)     // Catch:{ Throwable -> 0x02cb }
            goto L_0x02d3
        L_0x02cb:
            r0 = move-exception
            java.lang.String r3 = "DeleteCoreTask"
            java.lang.String r8 = "deleteSo:"
            com.uc.webview.export.internal.utility.Log.w(r3, r8, r0)
        L_0x02d3:
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x02ff }
            r3 = 0
            android.util.Pair<java.lang.String, java.lang.String> r8 = r3.sdkShellModule     // Catch:{ Throwable -> 0x02ff }
            java.lang.Object r3 = r8.second     // Catch:{ Throwable -> 0x02ff }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x02ff }
            java.lang.String r8 = "sdk_shell.dex"
            r0.<init>(r3, r8)     // Catch:{ Throwable -> 0x02ff }
            r0.delete()     // Catch:{ Throwable -> 0x02ff }
            java.lang.String r0 = "DeleteCoreTask"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02ff }
            java.lang.String r8 = "delete odex:"
            r3.<init>(r8)     // Catch:{ Throwable -> 0x02ff }
            r8 = 0
            android.util.Pair<java.lang.String, java.lang.String> r8 = r8.sdkShellModule     // Catch:{ Throwable -> 0x02ff }
            java.lang.Object r8 = r8.first     // Catch:{ Throwable -> 0x02ff }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ Throwable -> 0x02ff }
            r3.append(r8)     // Catch:{ Throwable -> 0x02ff }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x02ff }
            com.uc.webview.export.internal.utility.Log.d(r0, r3)     // Catch:{ Throwable -> 0x02ff }
            goto L_0x0307
        L_0x02ff:
            r0 = move-exception
            java.lang.String r3 = "DeleteCoreTask"
            java.lang.String r8 = "deleteSo:"
            com.uc.webview.export.internal.utility.Log.w(r3, r8, r0)
        L_0x0307:
            r3 = 1
            goto L_0x030a
        L_0x0309:
            r3 = r0
        L_0x030a:
            java.lang.String r0 = "DeleteCoreTask"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x033b }
            java.lang.String r9 = "deleteCoreFlagDir:"
            r8.<init>(r9)     // Catch:{ Throwable -> 0x033b }
            r8.append(r12)     // Catch:{ Throwable -> 0x033b }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x033b }
            com.uc.webview.export.internal.utility.Log.d(r0, r8)     // Catch:{ Throwable -> 0x033b }
            boolean r0 = r13.exists()     // Catch:{ Throwable -> 0x033b }
            if (r0 != 0) goto L_0x0327
            r13.createNewFile()     // Catch:{ Throwable -> 0x033b }
            goto L_0x0343
        L_0x0327:
            boolean r0 = r14.exists()     // Catch:{ Throwable -> 0x033b }
            if (r0 != 0) goto L_0x0331
            r14.createNewFile()     // Catch:{ Throwable -> 0x033b }
            goto L_0x0343
        L_0x0331:
            boolean r0 = r5.exists()     // Catch:{ Throwable -> 0x033b }
            if (r0 != 0) goto L_0x0343
            r5.createNewFile()     // Catch:{ Throwable -> 0x033b }
            goto L_0x0343
        L_0x033b:
            r0 = move-exception
            java.lang.String r5 = "DeleteCoreTask"
            java.lang.String r8 = "deleteCoreFlag:"
            com.uc.webview.export.internal.utility.Log.w(r5, r8, r0)
        L_0x0343:
            r0 = r3
            r3 = 14
            r5 = 0
            r6 = 1
            goto L_0x0069
        L_0x034a:
            java.util.List<com.uc.webview.export.internal.setup.av> r2 = r1.a
            r2.clear()
            if (r0 == 0) goto L_0x0356
            java.lang.String r0 = "sdk_stp_dcc"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
        L_0x0356:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.e.run():void");
    }
}
