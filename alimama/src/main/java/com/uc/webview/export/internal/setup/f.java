package com.uc.webview.export.internal.setup;

/* compiled from: U4Source */
public final class f extends UCSubSetupTask<f, f> {
    /* JADX WARNING: Removed duplicated region for block: B:114:0x01ff A[Catch:{ Throwable -> 0x020d }] */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x0201 A[Catch:{ Throwable -> 0x020d }] */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x0220 A[Catch:{ Throwable -> 0x0269 }] */
    /* JADX WARNING: Removed duplicated region for block: B:159:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0099 A[Catch:{ Throwable -> 0x00b6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00c4 A[Catch:{ Throwable -> 0x0066, Throwable -> 0x026e }] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00cf A[Catch:{ Throwable -> 0x0066, Throwable -> 0x026e }] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00d2 A[Catch:{ Throwable -> 0x0066, Throwable -> 0x026e }] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0117 A[Catch:{ Throwable -> 0x01f3 }] */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0185 A[Catch:{ Throwable -> 0x01f3 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r16 = this;
            r1 = r16
            java.lang.String r0 = "CONTEXT"
            java.lang.Object r0 = r1.getOption(r0)     // Catch:{ Throwable -> 0x0274 }
            r2 = r0
            android.content.Context r2 = (android.content.Context) r2     // Catch:{ Throwable -> 0x0274 }
            com.uc.webview.export.internal.utility.k.a((android.content.Context) r2)     // Catch:{ Throwable -> 0x0274 }
            com.uc.webview.export.internal.setup.UCMRunningInfo r3 = com.uc.webview.export.utility.SetupTask.getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0274 }
            int r0 = r3.coreType     // Catch:{ Throwable -> 0x0274 }
            r4 = 2
            if (r0 != r4) goto L_0x0018
            return
        L_0x0018:
            java.lang.String r0 = "process_private_data_dir_suffix"
            java.lang.Object r0 = com.uc.webview.export.extension.UCCore.getGlobalOption(r0)     // Catch:{ Throwable -> 0x0274 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x0274 }
            boolean r5 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)     // Catch:{ Throwable -> 0x0274 }
            if (r5 != 0) goto L_0x0273
            java.lang.String r5 = "0"
            boolean r0 = r5.equals(r0)     // Catch:{ Throwable -> 0x0274 }
            if (r0 != 0) goto L_0x0030
            goto L_0x0273
        L_0x0030:
            java.lang.String r0 = "del_dec_fil"
            java.lang.Object r0 = r1.getOption(r0)     // Catch:{ Throwable -> 0x0274 }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x0274 }
            boolean r0 = com.uc.webview.export.internal.utility.k.b((java.lang.Boolean) r0)     // Catch:{ Throwable -> 0x0274 }
            r5 = 1
            r0 = r0 ^ r5
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r0)     // Catch:{ Throwable -> 0x0274 }
            java.lang.String r0 = "del_upd_fil"
            java.lang.Object r0 = r1.getOption(r0)     // Catch:{ Throwable -> 0x0274 }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x0274 }
            boolean r0 = com.uc.webview.export.internal.utility.k.b((java.lang.Boolean) r0)     // Catch:{ Throwable -> 0x0274 }
            r0 = r0 ^ r5
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r0)     // Catch:{ Throwable -> 0x0274 }
            boolean r0 = com.uc.webview.export.cyclone.UCCyclone.enableDebugLog     // Catch:{ Throwable -> 0x026e }
            if (r0 != 0) goto L_0x0059
            r9 = 0
            goto L_0x0062
        L_0x0059:
            java.lang.String r0 = "i"
            java.lang.String r9 = "DeleteFileTask"
            com.uc.webview.export.cyclone.UCLogger r0 = com.uc.webview.export.cyclone.UCLogger.create(r0, r9)     // Catch:{ Throwable -> 0x026e }
            r9 = r0
        L_0x0062:
            com.uc.webview.export.cyclone.UCCyclone.deleteUnusedFiles(r2)     // Catch:{ Throwable -> 0x0066 }
            goto L_0x006b
        L_0x0066:
            r0 = move-exception
            r10 = r0
            r10.printStackTrace()     // Catch:{ Throwable -> 0x026e }
        L_0x006b:
            com.uc.webview.export.internal.utility.k.b((android.content.Context) r2)     // Catch:{ Throwable -> 0x006f }
            goto L_0x0074
        L_0x006f:
            r0 = move-exception
            r10 = r0
            r10.printStackTrace()     // Catch:{ Throwable -> 0x026e }
        L_0x0074:
            r10 = 0
            com.uc.webview.export.internal.setup.br r0 = r3.ucmPackageInfo     // Catch:{ Throwable -> 0x00b6 }
            if (r0 == 0) goto L_0x0096
            com.uc.webview.export.internal.setup.br r0 = r3.ucmPackageInfo     // Catch:{ Throwable -> 0x00b6 }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r0.coreImplModule     // Catch:{ Throwable -> 0x00b6 }
            if (r0 == 0) goto L_0x0096
            com.uc.webview.export.internal.setup.br r0 = r3.ucmPackageInfo     // Catch:{ Throwable -> 0x00b6 }
            android.util.Pair<java.lang.String, java.lang.String> r0 = r0.coreImplModule     // Catch:{ Throwable -> 0x00b6 }
            java.lang.Object r0 = r0.second     // Catch:{ Throwable -> 0x00b6 }
            if (r0 != 0) goto L_0x0088
            goto L_0x0096
        L_0x0088:
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x00b6 }
            com.uc.webview.export.internal.setup.br r11 = r3.ucmPackageInfo     // Catch:{ Throwable -> 0x00b6 }
            android.util.Pair<java.lang.String, java.lang.String> r11 = r11.coreImplModule     // Catch:{ Throwable -> 0x00b6 }
            java.lang.Object r11 = r11.second     // Catch:{ Throwable -> 0x00b6 }
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ Throwable -> 0x00b6 }
            r0.<init>(r11)     // Catch:{ Throwable -> 0x00b6 }
            goto L_0x0097
        L_0x0096:
            r0 = 0
        L_0x0097:
            if (r9 == 0) goto L_0x00ac
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00b6 }
            java.lang.String r12 = "loadedOdexDirFile "
            r11.<init>(r12)     // Catch:{ Throwable -> 0x00b6 }
            r11.append(r0)     // Catch:{ Throwable -> 0x00b6 }
            java.lang.String r11 = r11.toString()     // Catch:{ Throwable -> 0x00b6 }
            java.lang.Throwable[] r12 = new java.lang.Throwable[r10]     // Catch:{ Throwable -> 0x00b6 }
            r9.print(r11, r12)     // Catch:{ Throwable -> 0x00b6 }
        L_0x00ac:
            java.lang.String r11 = "odexs"
            java.io.File r11 = com.uc.webview.export.internal.utility.k.a((android.content.Context) r2, (java.lang.String) r11)     // Catch:{ Throwable -> 0x00b6 }
            com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r11, r5, r0)     // Catch:{ Throwable -> 0x00b6 }
            goto L_0x00ba
        L_0x00b6:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x026e }
        L_0x00ba:
            com.uc.webview.export.internal.setup.br r0 = r3.ucmPackageInfo     // Catch:{ Throwable -> 0x026e }
            if (r0 == 0) goto L_0x00cf
            com.uc.webview.export.internal.setup.br r0 = r3.ucmPackageInfo     // Catch:{ Throwable -> 0x026e }
            java.lang.String r0 = r0.dataDir     // Catch:{ Throwable -> 0x026e }
            if (r0 == 0) goto L_0x00cf
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x026e }
            com.uc.webview.export.internal.setup.br r3 = r3.ucmPackageInfo     // Catch:{ Throwable -> 0x026e }
            java.lang.String r3 = r3.dataDir     // Catch:{ Throwable -> 0x026e }
            r0.<init>(r3)     // Catch:{ Throwable -> 0x026e }
            r3 = r0
            goto L_0x00d0
        L_0x00cf:
            r3 = 0
        L_0x00d0:
            if (r9 == 0) goto L_0x00e5
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x026e }
            java.lang.String r11 = "loadedDataDirFile "
            r0.<init>(r11)     // Catch:{ Throwable -> 0x026e }
            r0.append(r3)     // Catch:{ Throwable -> 0x026e }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x026e }
            java.lang.Throwable[] r11 = new java.lang.Throwable[r10]     // Catch:{ Throwable -> 0x026e }
            r9.print(r0, r11)     // Catch:{ Throwable -> 0x026e }
        L_0x00e5:
            java.lang.String r0 = "decompresses2"
            java.io.File r0 = com.uc.webview.export.internal.utility.k.a((android.content.Context) r2, (java.lang.String) r0)     // Catch:{ Throwable -> 0x01f3 }
            java.io.File[] r11 = r0.listFiles()     // Catch:{ Throwable -> 0x01f3 }
            if (r11 == 0) goto L_0x013c
            int r12 = r11.length     // Catch:{ Throwable -> 0x01f3 }
            if (r12 <= 0) goto L_0x013c
            boolean r12 = r6.booleanValue()     // Catch:{ Throwable -> 0x01f3 }
            if (r12 != 0) goto L_0x010d
            int r11 = r11.length     // Catch:{ Throwable -> 0x01f3 }
            if (r11 >= r4) goto L_0x010d
            if (r3 == 0) goto L_0x013c
            java.lang.String r11 = r3.getAbsolutePath()     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r12 = r0.getAbsolutePath()     // Catch:{ Throwable -> 0x01f3 }
            boolean r11 = r11.startsWith(r12)     // Catch:{ Throwable -> 0x01f3 }
            if (r11 == 0) goto L_0x013c
        L_0x010d:
            java.io.File[] r11 = r0.listFiles()     // Catch:{ Throwable -> 0x01f3 }
            if (r11 == 0) goto L_0x013c
            int r12 = r11.length     // Catch:{ Throwable -> 0x01f3 }
            r13 = 0
        L_0x0115:
            if (r13 >= r12) goto L_0x013c
            r14 = r11[r13]     // Catch:{ Throwable -> 0x01f3 }
            if (r9 == 0) goto L_0x0136
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r8 = "decompress delete files "
            r15.<init>(r8)     // Catch:{ Throwable -> 0x01f3 }
            r15.append(r14)     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r8 = ", "
            r15.append(r8)     // Catch:{ Throwable -> 0x01f3 }
            r15.append(r3)     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r8 = r15.toString()     // Catch:{ Throwable -> 0x01f3 }
            java.lang.Throwable[] r15 = new java.lang.Throwable[r10]     // Catch:{ Throwable -> 0x01f3 }
            r9.print(r8, r15)     // Catch:{ Throwable -> 0x01f3 }
        L_0x0136:
            com.uc.webview.export.internal.utility.k.a((android.content.Context) r2, (java.io.File) r14, (java.io.File) r3)     // Catch:{ Throwable -> 0x01f3 }
            int r13 = r13 + 1
            goto L_0x0115
        L_0x013c:
            java.lang.String r8 = "bo_dex_old_dex_dir"
            java.lang.Object r8 = r1.getOption(r8)     // Catch:{ Throwable -> 0x01f3 }
            java.lang.Boolean r8 = (java.lang.Boolean) r8     // Catch:{ Throwable -> 0x01f3 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.Boolean) r8)     // Catch:{ Throwable -> 0x01f3 }
            if (r8 == 0) goto L_0x01f7
            java.lang.String r8 = "bit_by_new_dex_dir"
            java.lang.String r11 = "bo_init_type"
            java.lang.Object r11 = r1.getOption(r11)     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ Throwable -> 0x01f3 }
            boolean r8 = r8.equals(r11)     // Catch:{ Throwable -> 0x01f3 }
            if (r8 != 0) goto L_0x016a
            java.lang.String r8 = "bit_by_new_zip_file"
            java.lang.String r11 = "bo_init_type"
            java.lang.Object r11 = r1.getOption(r11)     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ Throwable -> 0x01f3 }
            boolean r8 = r8.equals(r11)     // Catch:{ Throwable -> 0x01f3 }
            if (r8 == 0) goto L_0x01f7
        L_0x016a:
            java.io.File r8 = new java.io.File     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r11 = "bo_old_dex_dp"
            java.lang.Object r11 = r1.getOption(r11)     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ Throwable -> 0x01f3 }
            r8.<init>(r11)     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r11 = r8.getAbsolutePath()     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r0 = r0.getAbsolutePath()     // Catch:{ Throwable -> 0x01f3 }
            boolean r0 = r11.startsWith(r0)     // Catch:{ Throwable -> 0x01f3 }
            if (r0 != 0) goto L_0x01f7
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r11 = "bo_dec_root_dir"
            java.lang.Object r11 = r1.getOption(r11)     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r11 = (java.lang.String) r11     // Catch:{ Throwable -> 0x01f3 }
            r0.<init>(r11)     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r11 = r8.getAbsolutePath()     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r12 = r0.getAbsolutePath()     // Catch:{ Throwable -> 0x01f3 }
            boolean r11 = r11.startsWith(r12)     // Catch:{ Throwable -> 0x01f3 }
            if (r11 == 0) goto L_0x01a1
            goto L_0x01a2
        L_0x01a1:
            r0 = r8
        L_0x01a2:
            java.io.File[] r8 = r0.listFiles()     // Catch:{ Throwable -> 0x01f3 }
            if (r8 == 0) goto L_0x01f7
            int r11 = r8.length     // Catch:{ Throwable -> 0x01f3 }
            if (r11 <= 0) goto L_0x01f7
            boolean r6 = r6.booleanValue()     // Catch:{ Throwable -> 0x01f3 }
            if (r6 != 0) goto L_0x01c4
            int r6 = r8.length     // Catch:{ Throwable -> 0x01f3 }
            if (r6 >= r4) goto L_0x01c4
            if (r3 == 0) goto L_0x01f7
            java.lang.String r6 = r3.getAbsolutePath()     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r8 = r0.getAbsolutePath()     // Catch:{ Throwable -> 0x01f3 }
            boolean r6 = r6.startsWith(r8)     // Catch:{ Throwable -> 0x01f3 }
            if (r6 == 0) goto L_0x01f7
        L_0x01c4:
            java.io.File[] r0 = r0.listFiles()     // Catch:{ Throwable -> 0x01f3 }
            if (r0 == 0) goto L_0x01f7
            int r6 = r0.length     // Catch:{ Throwable -> 0x01f3 }
            r8 = 0
        L_0x01cc:
            if (r8 >= r6) goto L_0x01f7
            r11 = r0[r8]     // Catch:{ Throwable -> 0x01f3 }
            if (r9 == 0) goto L_0x01ed
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r13 = "prehead init delete files "
            r12.<init>(r13)     // Catch:{ Throwable -> 0x01f3 }
            r12.append(r11)     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r13 = ", "
            r12.append(r13)     // Catch:{ Throwable -> 0x01f3 }
            r12.append(r3)     // Catch:{ Throwable -> 0x01f3 }
            java.lang.String r12 = r12.toString()     // Catch:{ Throwable -> 0x01f3 }
            java.lang.Throwable[] r13 = new java.lang.Throwable[r10]     // Catch:{ Throwable -> 0x01f3 }
            r9.print(r12, r13)     // Catch:{ Throwable -> 0x01f3 }
        L_0x01ed:
            com.uc.webview.export.internal.utility.k.a((android.content.Context) r2, (java.io.File) r11, (java.io.File) r3)     // Catch:{ Throwable -> 0x01f3 }
            int r8 = r8 + 1
            goto L_0x01cc
        L_0x01f3:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x026e }
        L_0x01f7:
            java.lang.String r0 = "repairs"
            java.io.File r0 = com.uc.webview.export.internal.utility.k.a((android.content.Context) r2, (java.lang.String) r0)     // Catch:{ Throwable -> 0x020d }
            if (r3 != 0) goto L_0x0201
            r8 = 0
            goto L_0x0209
        L_0x0201:
            java.io.File r6 = r3.getParentFile()     // Catch:{ Throwable -> 0x020d }
            java.io.File r8 = r6.getParentFile()     // Catch:{ Throwable -> 0x020d }
        L_0x0209:
            com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r0, r5, r8)     // Catch:{ Throwable -> 0x020d }
            goto L_0x0211
        L_0x020d:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x026e }
        L_0x0211:
            java.lang.String r0 = "updates"
            java.io.File r0 = com.uc.webview.export.internal.utility.k.a((android.content.Context) r2, (java.lang.String) r0)     // Catch:{ Throwable -> 0x0269 }
            java.io.File[] r5 = r0.listFiles()     // Catch:{ Throwable -> 0x0269 }
            if (r5 == 0) goto L_0x0268
            int r6 = r5.length     // Catch:{ Throwable -> 0x0269 }
            if (r6 <= 0) goto L_0x0268
            boolean r6 = r7.booleanValue()     // Catch:{ Throwable -> 0x0269 }
            if (r6 != 0) goto L_0x0239
            int r5 = r5.length     // Catch:{ Throwable -> 0x0269 }
            if (r5 >= r4) goto L_0x0239
            if (r3 == 0) goto L_0x0268
            java.lang.String r4 = r3.getAbsolutePath()     // Catch:{ Throwable -> 0x0269 }
            java.lang.String r5 = r0.getAbsolutePath()     // Catch:{ Throwable -> 0x0269 }
            boolean r4 = r4.startsWith(r5)     // Catch:{ Throwable -> 0x0269 }
            if (r4 == 0) goto L_0x0268
        L_0x0239:
            java.io.File[] r0 = r0.listFiles()     // Catch:{ Throwable -> 0x0269 }
            if (r0 == 0) goto L_0x0268
            int r4 = r0.length     // Catch:{ Throwable -> 0x0269 }
            r5 = 0
        L_0x0241:
            if (r5 >= r4) goto L_0x0268
            r6 = r0[r5]     // Catch:{ Throwable -> 0x0269 }
            if (r9 == 0) goto L_0x0262
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0269 }
            java.lang.String r8 = "update delete files "
            r7.<init>(r8)     // Catch:{ Throwable -> 0x0269 }
            r7.append(r6)     // Catch:{ Throwable -> 0x0269 }
            java.lang.String r8 = ", "
            r7.append(r8)     // Catch:{ Throwable -> 0x0269 }
            r7.append(r3)     // Catch:{ Throwable -> 0x0269 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0269 }
            java.lang.Throwable[] r8 = new java.lang.Throwable[r10]     // Catch:{ Throwable -> 0x0269 }
            r9.print(r7, r8)     // Catch:{ Throwable -> 0x0269 }
        L_0x0262:
            com.uc.webview.export.internal.utility.k.a((android.content.Context) r2, (java.io.File) r6, (java.io.File) r3)     // Catch:{ Throwable -> 0x0269 }
            int r5 = r5 + 1
            goto L_0x0241
        L_0x0268:
            return
        L_0x0269:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x026e }
            return
        L_0x026e:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0274 }
            return
        L_0x0273:
            return
        L_0x0274:
            r0 = move-exception
            r0.printStackTrace()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.f.run():void");
    }
}
