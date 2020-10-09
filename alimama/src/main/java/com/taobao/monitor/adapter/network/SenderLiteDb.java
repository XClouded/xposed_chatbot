package com.taobao.monitor.adapter.network;

import com.taobao.monitor.impl.common.Global;
import java.io.File;

class SenderLiteDb implements ILiteDb {
    private static final String FILE_NAME = "apm_db.db";
    private static final long KB_4 = 4194304;
    private final File file = new File(Global.instance().context().getCacheDir() + "/" + FILE_NAME);

    /* JADX WARNING: Removed duplicated region for block: B:15:0x002c A[Catch:{ Exception -> 0x0030 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void insert(java.lang.String r6) {
        /*
            r5 = this;
            r5.createSafeFile()     // Catch:{ Exception -> 0x0030 }
            java.io.File r0 = r5.file     // Catch:{ Exception -> 0x0030 }
            long r0 = r0.length()     // Catch:{ Exception -> 0x0030 }
            r2 = 4194304(0x400000, double:2.0722615E-317)
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 >= 0) goto L_0x0034
            r0 = 0
            java.io.FileWriter r1 = new java.io.FileWriter     // Catch:{ all -> 0x0029 }
            java.io.File r2 = r5.file     // Catch:{ all -> 0x0029 }
            r3 = 1
            r1.<init>(r2, r3)     // Catch:{ all -> 0x0029 }
            java.io.Writer r6 = r1.append(r6)     // Catch:{ all -> 0x0026 }
            java.lang.String r0 = "\n"
            r6.append(r0)     // Catch:{ all -> 0x0026 }
            r1.close()     // Catch:{ Exception -> 0x0030 }
            goto L_0x0034
        L_0x0026:
            r6 = move-exception
            r0 = r1
            goto L_0x002a
        L_0x0029:
            r6 = move-exception
        L_0x002a:
            if (r0 == 0) goto L_0x002f
            r0.close()     // Catch:{ Exception -> 0x0030 }
        L_0x002f:
            throw r6     // Catch:{ Exception -> 0x0030 }
        L_0x0030:
            r6 = move-exception
            r6.printStackTrace()
        L_0x0034:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.monitor.adapter.network.SenderLiteDb.insert(java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0039 A[Catch:{ Exception -> 0x003d }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.lang.String> select() {
        /*
            r6 = this;
            r0 = 0
            r6.createSafeFile()     // Catch:{ Exception -> 0x003d }
            java.io.File r1 = r6.file     // Catch:{ Exception -> 0x003d }
            long r1 = r1.length()     // Catch:{ Exception -> 0x003d }
            r3 = 0
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 <= 0) goto L_0x003d
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ all -> 0x0035 }
            java.io.FileReader r2 = new java.io.FileReader     // Catch:{ all -> 0x0035 }
            java.io.File r3 = r6.file     // Catch:{ all -> 0x0035 }
            r2.<init>(r3)     // Catch:{ all -> 0x0035 }
            r1.<init>(r2)     // Catch:{ all -> 0x0035 }
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0033 }
            r2.<init>()     // Catch:{ all -> 0x0033 }
            java.lang.String r3 = r1.readLine()     // Catch:{ all -> 0x0033 }
        L_0x0025:
            if (r3 == 0) goto L_0x002f
            r2.add(r3)     // Catch:{ all -> 0x0033 }
            java.lang.String r3 = r1.readLine()     // Catch:{ all -> 0x0033 }
            goto L_0x0025
        L_0x002f:
            r1.close()     // Catch:{ Exception -> 0x003d }
            return r2
        L_0x0033:
            r2 = move-exception
            goto L_0x0037
        L_0x0035:
            r2 = move-exception
            r1 = r0
        L_0x0037:
            if (r1 == 0) goto L_0x003c
            r1.close()     // Catch:{ Exception -> 0x003d }
        L_0x003c:
            throw r2     // Catch:{ Exception -> 0x003d }
        L_0x003d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.monitor.adapter.network.SenderLiteDb.select():java.util.List");
    }

    public void delete() {
        if (this.file.exists()) {
            this.file.delete();
        }
    }

    private void createSafeFile() throws Exception {
        if (!this.file.exists()) {
            this.file.createNewFile();
        } else if (this.file.isDirectory()) {
            this.file.delete();
            this.file.createNewFile();
        }
    }
}
