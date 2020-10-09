package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.push.bw;

class by implements Runnable {
    final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ bw.a f169a;

    by(bw.a aVar, Context context) {
        this.f169a = aVar;
        this.a = context;
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0043 A[SYNTHETIC, Splitter:B:24:0x0043] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x004f A[Catch:{ Exception -> 0x0047 }] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0065 A[SYNTHETIC, Splitter:B:36:0x0065] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0071 A[Catch:{ Exception -> 0x0069 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r4 = this;
            r0 = 0
            com.xiaomi.push.bw$a r1 = r4.f169a     // Catch:{ Exception -> 0x003a, all -> 0x0035 }
            android.database.sqlite.SQLiteDatabase r1 = r1.a()     // Catch:{ Exception -> 0x003a, all -> 0x0035 }
            if (r1 == 0) goto L_0x001f
            boolean r0 = r1.isOpen()     // Catch:{ Exception -> 0x001d }
            if (r0 == 0) goto L_0x001f
            r1.beginTransaction()     // Catch:{ Exception -> 0x001d }
            com.xiaomi.push.bw$a r0 = r4.f169a     // Catch:{ Exception -> 0x001d }
            android.content.Context r2 = r4.a     // Catch:{ Exception -> 0x001d }
            r0.a((android.content.Context) r2, (android.database.sqlite.SQLiteDatabase) r1)     // Catch:{ Exception -> 0x001d }
            r1.setTransactionSuccessful()     // Catch:{ Exception -> 0x001d }
            goto L_0x001f
        L_0x001d:
            r0 = move-exception
            goto L_0x003e
        L_0x001f:
            if (r1 == 0) goto L_0x0027
            r1.endTransaction()     // Catch:{ Exception -> 0x0025 }
            goto L_0x0027
        L_0x0025:
            r0 = move-exception
            goto L_0x0057
        L_0x0027:
            com.xiaomi.push.bw$a r0 = r4.f169a     // Catch:{ Exception -> 0x0025 }
            com.xiaomi.push.bu r0 = r0.f160a     // Catch:{ Exception -> 0x0025 }
            if (r0 == 0) goto L_0x005a
            com.xiaomi.push.bw$a r0 = r4.f169a     // Catch:{ Exception -> 0x0025 }
            com.xiaomi.push.bu r0 = r0.f160a     // Catch:{ Exception -> 0x0025 }
            r0.close()     // Catch:{ Exception -> 0x0025 }
            goto L_0x005a
        L_0x0035:
            r1 = move-exception
            r3 = r1
            r1 = r0
            r0 = r3
            goto L_0x0063
        L_0x003a:
            r1 = move-exception
            r3 = r1
            r1 = r0
            r0 = r3
        L_0x003e:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)     // Catch:{ all -> 0x0062 }
            if (r1 == 0) goto L_0x0049
            r1.endTransaction()     // Catch:{ Exception -> 0x0047 }
            goto L_0x0049
        L_0x0047:
            r0 = move-exception
            goto L_0x0057
        L_0x0049:
            com.xiaomi.push.bw$a r0 = r4.f169a     // Catch:{ Exception -> 0x0047 }
            com.xiaomi.push.bu r0 = r0.f160a     // Catch:{ Exception -> 0x0047 }
            if (r0 == 0) goto L_0x005a
            com.xiaomi.push.bw$a r0 = r4.f169a     // Catch:{ Exception -> 0x0047 }
            com.xiaomi.push.bu r0 = r0.f160a     // Catch:{ Exception -> 0x0047 }
            r0.close()     // Catch:{ Exception -> 0x0047 }
            goto L_0x005a
        L_0x0057:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r0)
        L_0x005a:
            com.xiaomi.push.bw$a r0 = r4.f169a
            android.content.Context r1 = r4.a
            r0.a((android.content.Context) r1)
            return
        L_0x0062:
            r0 = move-exception
        L_0x0063:
            if (r1 == 0) goto L_0x006b
            r1.endTransaction()     // Catch:{ Exception -> 0x0069 }
            goto L_0x006b
        L_0x0069:
            r1 = move-exception
            goto L_0x0079
        L_0x006b:
            com.xiaomi.push.bw$a r1 = r4.f169a     // Catch:{ Exception -> 0x0069 }
            com.xiaomi.push.bu r1 = r1.f160a     // Catch:{ Exception -> 0x0069 }
            if (r1 == 0) goto L_0x007c
            com.xiaomi.push.bw$a r1 = r4.f169a     // Catch:{ Exception -> 0x0069 }
            com.xiaomi.push.bu r1 = r1.f160a     // Catch:{ Exception -> 0x0069 }
            r1.close()     // Catch:{ Exception -> 0x0069 }
            goto L_0x007c
        L_0x0079:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r1)
        L_0x007c:
            com.xiaomi.push.bw$a r1 = r4.f169a
            android.content.Context r2 = r4.a
            r1.a((android.content.Context) r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.by.run():void");
    }
}
