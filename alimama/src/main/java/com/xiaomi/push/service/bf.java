package com.xiaomi.push.service;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ai;
import com.xiaomi.push.av;
import com.xiaomi.push.ay;
import com.xiaomi.push.hk;
import com.xiaomi.push.p;
import java.util.Arrays;

public class bf {
    public static final Object a = new Object();

    public static void a(Context context, hk hkVar) {
        if (be.a(hkVar.e())) {
            b.a("TinyData TinyDataStorage.cacheTinyData cache data to file begin item:" + hkVar.d() + "  ts:" + System.currentTimeMillis());
            ai.a(context).a((Runnable) new bg(context, hkVar));
        }
    }

    public static byte[] a(Context context) {
        String a2 = p.a(context).a("mipush", "td_key", "");
        if (TextUtils.isEmpty(a2)) {
            a2 = ay.a(20);
            p.a(context).a("mipush", "td_key", a2);
        }
        return a(a2);
    }

    private static byte[] a(String str) {
        byte[] copyOf = Arrays.copyOf(av.a(str), 16);
        copyOf[0] = 68;
        copyOf[15] = 84;
        return copyOf;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v5, resolved type: java.io.IOException} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v13, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v14, resolved type: java.io.IOException} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v16, resolved type: byte[]} */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v3, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v8 */
    /* JADX WARNING: type inference failed for: r0v17 */
    /* JADX WARNING: type inference failed for: r0v24 */
    /* JADX WARNING: type inference failed for: r0v25 */
    /* JADX WARNING: type inference failed for: r0v27 */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void c(android.content.Context r6, com.xiaomi.push.hk r7) {
        /*
            byte[] r0 = a((android.content.Context) r6)
            r1 = 0
            byte[] r2 = com.xiaomi.push.iq.a(r7)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            byte[] r0 = com.xiaomi.push.h.b(r0, r2)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            if (r0 == 0) goto L_0x00a3
            int r2 = r0.length     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            r3 = 1
            if (r2 >= r3) goto L_0x0015
            goto L_0x00a3
        L_0x0015:
            int r2 = r0.length     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            r4 = 10240(0x2800, float:1.4349E-41)
            if (r2 <= r4) goto L_0x0045
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            r6.<init>()     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            java.lang.String r0 = "TinyData write to cache file failed case too much data content item:"
            r6.append(r0)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            java.lang.String r0 = r7.d()     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            r6.append(r0)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            java.lang.String r0 = "  ts:"
            r6.append(r0)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            r6.append(r2)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            java.lang.String r6 = r6.toString()     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
        L_0x003b:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r6)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            com.xiaomi.push.y.a((java.io.Closeable) r1)
            com.xiaomi.push.y.a((java.io.Closeable) r1)
            return
        L_0x0045:
            java.io.File r2 = new java.io.File     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            java.io.File r6 = r6.getFilesDir()     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            java.lang.String r4 = "tiny_data.data"
            r2.<init>(r6, r4)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            java.io.BufferedOutputStream r6 = new java.io.BufferedOutputStream     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            r4.<init>(r2, r3)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            r6.<init>(r4)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            int r2 = r0.length     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            byte[] r2 = com.xiaomi.push.ac.a((int) r2)     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            r6.write(r2)     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            r6.write(r0)     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            r6.flush()     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            r0.<init>()     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            java.lang.String r2 = "TinyData write to cache file success item:"
            r0.append(r2)     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            java.lang.String r2 = r7.d()     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            r0.append(r2)     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            java.lang.String r2 = "  ts:"
            r0.append(r2)     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            r0.append(r2)     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            java.lang.String r0 = r0.toString()     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)     // Catch:{ IOException -> 0x009e, Exception -> 0x0099, all -> 0x0094 }
            com.xiaomi.push.y.a((java.io.Closeable) r1)
            com.xiaomi.push.y.a((java.io.Closeable) r6)
            goto L_0x0101
        L_0x0094:
            r7 = move-exception
            r0 = r6
            r6 = r7
            goto L_0x0103
        L_0x0099:
            r0 = move-exception
            r5 = r0
            r0 = r6
            r6 = r5
            goto L_0x00cb
        L_0x009e:
            r0 = move-exception
            r5 = r0
            r0 = r6
            r6 = r5
            goto L_0x00e3
        L_0x00a3:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            r6.<init>()     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            java.lang.String r0 = "TinyData write to cache file failed case encryption fail item:"
            r6.append(r0)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            java.lang.String r0 = r7.d()     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            r6.append(r0)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            java.lang.String r0 = "  ts:"
            r6.append(r0)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            r6.append(r2)     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            java.lang.String r6 = r6.toString()     // Catch:{ IOException -> 0x00e1, Exception -> 0x00c9, all -> 0x00c6 }
            goto L_0x003b
        L_0x00c6:
            r6 = move-exception
            r0 = r1
            goto L_0x0103
        L_0x00c9:
            r6 = move-exception
            r0 = r1
        L_0x00cb:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0102 }
            r2.<init>()     // Catch:{ all -> 0x0102 }
            java.lang.String r3 = "TinyData write to cache file  failed item:"
            r2.append(r3)     // Catch:{ all -> 0x0102 }
            java.lang.String r7 = r7.d()     // Catch:{ all -> 0x0102 }
            r2.append(r7)     // Catch:{ all -> 0x0102 }
            java.lang.String r7 = r2.toString()     // Catch:{ all -> 0x0102 }
            goto L_0x00f8
        L_0x00e1:
            r6 = move-exception
            r0 = r1
        L_0x00e3:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0102 }
            r2.<init>()     // Catch:{ all -> 0x0102 }
            java.lang.String r3 = "TinyData write to cache file failed cause io exception item:"
            r2.append(r3)     // Catch:{ all -> 0x0102 }
            java.lang.String r7 = r7.d()     // Catch:{ all -> 0x0102 }
            r2.append(r7)     // Catch:{ all -> 0x0102 }
            java.lang.String r7 = r2.toString()     // Catch:{ all -> 0x0102 }
        L_0x00f8:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r7, (java.lang.Throwable) r6)     // Catch:{ all -> 0x0102 }
            com.xiaomi.push.y.a((java.io.Closeable) r1)
            com.xiaomi.push.y.a((java.io.Closeable) r0)
        L_0x0101:
            return
        L_0x0102:
            r6 = move-exception
        L_0x0103:
            com.xiaomi.push.y.a((java.io.Closeable) r1)
            com.xiaomi.push.y.a((java.io.Closeable) r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.bf.c(android.content.Context, com.xiaomi.push.hk):void");
    }
}
