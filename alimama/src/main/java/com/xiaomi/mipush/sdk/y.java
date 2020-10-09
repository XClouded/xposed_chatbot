package com.xiaomi.mipush.sdk;

class y implements Runnable {
    final /* synthetic */ x a;

    y(x xVar) {
        this.a = xVar;
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(Unknown Source)
        	at java.util.ArrayList.get(Unknown Source)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processExcHandler(RegionMaker.java:1043)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:975)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    public void run() {
        /*
            r9 = this;
            r0 = 0
            com.xiaomi.mipush.sdk.x r1 = r9.a     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            android.content.Context r1 = com.xiaomi.mipush.sdk.x.a((com.xiaomi.mipush.sdk.x) r1)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            com.xiaomi.mipush.sdk.u r1 = com.xiaomi.mipush.sdk.u.a((android.content.Context) r1)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            java.util.ArrayList r1 = r1.a()     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            if (r1 == 0) goto L_0x00e0
            int r2 = r1.size()     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            r3 = 1
            if (r2 >= r3) goto L_0x001a
            goto L_0x00e0
        L_0x001a:
            com.xiaomi.mipush.sdk.x r2 = r9.a     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            android.content.Context r2 = com.xiaomi.mipush.sdk.x.a((com.xiaomi.mipush.sdk.x) r2)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            java.lang.String r3 = "C100000"
            java.util.HashMap r2 = com.xiaomi.mipush.sdk.ak.a(r2, r3)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            r3 = 0
        L_0x0027:
            int r4 = r1.size()     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            if (r3 >= r4) goto L_0x00ef
            java.lang.Object r4 = r1.get(r3)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            java.io.File r4 = (java.io.File) r4     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            com.xiaomi.mipush.sdk.x r5 = r9.a     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            android.content.Context r5 = com.xiaomi.mipush.sdk.x.a((com.xiaomi.mipush.sdk.x) r5)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            com.xiaomi.mipush.sdk.u r5 = com.xiaomi.mipush.sdk.u.a((android.content.Context) r5)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            java.lang.String r5 = r5.a((java.io.File) r4)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            java.io.File r6 = new java.io.File     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            r7.<init>()     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            com.xiaomi.mipush.sdk.x r8 = r9.a     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            android.content.Context r8 = com.xiaomi.mipush.sdk.x.a((com.xiaomi.mipush.sdk.x) r8)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            java.io.File r8 = r8.getFilesDir()     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            r7.append(r8)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            java.lang.String r8 = "/crash"
            r7.append(r8)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            java.lang.String r8 = "/"
            r7.append(r8)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            java.lang.String r8 = r4.getName()     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            r7.append(r8)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            java.lang.String r8 = ".zip"
            r7.append(r8)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            java.lang.String r7 = r7.toString()     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            r6.<init>(r7)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            com.xiaomi.push.y.a((java.io.File) r6, (java.io.File) r4)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            boolean r0 = r6.exists()     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            if (r0 == 0) goto L_0x00d4
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            r0.<init>()     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.lang.String r7 = "https://api.xmpush.xiaomi.com/upload/crash_log?file="
            r0.append(r7)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.lang.String r7 = r6.getName()     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            r0.append(r7)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.lang.String r0 = r0.toString()     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.lang.String r7 = "file"
            com.xiaomi.push.as.a(r0, r2, r6, r7)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.io.File r0 = new java.io.File     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            r7.<init>()     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            com.xiaomi.mipush.sdk.x r8 = r9.a     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            android.content.Context r8 = com.xiaomi.mipush.sdk.x.a((com.xiaomi.mipush.sdk.x) r8)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.io.File r8 = r8.getFilesDir()     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            r7.append(r8)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.lang.String r8 = "/crash"
            r7.append(r8)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.lang.String r7 = r7.toString()     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            r8.<init>()     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            r8.append(r5)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.lang.String r5 = ":"
            r8.append(r5)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.lang.String r5 = "0"
            r8.append(r5)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            java.lang.String r5 = r8.toString()     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            r0.<init>(r7, r5)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            r4.renameTo(r0)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            com.xiaomi.mipush.sdk.x r0 = r9.a     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            com.xiaomi.mipush.sdk.x.a((com.xiaomi.mipush.sdk.x) r0)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
            goto L_0x00d9
        L_0x00d4:
            java.lang.String r0 = "zip crash file failed"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)     // Catch:{ IOException -> 0x00de, Throwable -> 0x00ee }
        L_0x00d9:
            int r3 = r3 + 1
            r0 = r6
            goto L_0x0027
        L_0x00de:
            r0 = move-exception
            goto L_0x00eb
        L_0x00e0:
            java.lang.String r1 = "no crash file to upload"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r1)     // Catch:{ IOException -> 0x00e8, Throwable -> 0x00e6 }
            return
        L_0x00e6:
            goto L_0x00ef
        L_0x00e8:
            r1 = move-exception
            r6 = r0
            r0 = r1
        L_0x00eb:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)
        L_0x00ee:
            r0 = r6
        L_0x00ef:
            if (r0 == 0) goto L_0x0102
            boolean r1 = r0.exists()
            if (r1 == 0) goto L_0x0102
            boolean r0 = r0.delete()
            if (r0 != 0) goto L_0x0102
            java.lang.String r0 = "delete zip crash file failed"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
        L_0x0102:
            java.lang.Object r0 = com.xiaomi.mipush.sdk.x.a()
            monitor-enter(r0)
            java.lang.Object r1 = com.xiaomi.mipush.sdk.x.a()     // Catch:{ all -> 0x0110 }
            r1.notifyAll()     // Catch:{ all -> 0x0110 }
            monitor-exit(r0)     // Catch:{ all -> 0x0110 }
            return
        L_0x0110:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0110 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mipush.sdk.y.run():void");
    }
}
