package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.push.ai;

public abstract class dx extends ai.a {
    protected int a;

    /* renamed from: a  reason: collision with other field name */
    protected Context f241a;

    public dx(Context context, int i) {
        this.a = i;
        this.f241a = context;
    }

    public static void a(Context context, ho hoVar) {
        dk a2 = dl.a().a();
        String a3 = a2 == null ? "" : a2.a();
        if (!TextUtils.isEmpty(a3) && !TextUtils.isEmpty(hoVar.a())) {
            a(context, hoVar, a3);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: java.nio.channels.FileLock} */
    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r0v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r0v2 */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: Can't wrap try/catch for region: R(10:32|33|38|39|(0)|46|47|23|48|49) */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:13|14|(3:16|17|(2:19|20))|21|22) */
    /* JADX WARNING: Can't wrap try/catch for region: R(7:50|51|52|(2:56|57)|59|60|61) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x005f */
    /* JADX WARNING: Missing exception handler attribute for start block: B:46:0x008b */
    /* JADX WARNING: Missing exception handler attribute for start block: B:59:0x00a2 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0082 A[SYNTHETIC, Splitter:B:41:0x0082] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:59:0x00a2=Splitter:B:59:0x00a2, B:46:0x008b=Splitter:B:46:0x008b, B:38:0x007d=Splitter:B:38:0x007d, B:21:0x005f=Splitter:B:21:0x005f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void a(android.content.Context r6, com.xiaomi.push.ho r7, java.lang.String r8) {
        /*
            byte[] r7 = com.xiaomi.push.iq.a(r7)
            byte[] r7 = com.xiaomi.push.dp.b(r8, r7)
            if (r7 == 0) goto L_0x00ab
            int r8 = r7.length
            if (r8 != 0) goto L_0x000f
            goto L_0x00ab
        L_0x000f:
            java.lang.Object r8 = com.xiaomi.push.dq.a
            monitor-enter(r8)
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch:{ IOException -> 0x007a, all -> 0x0076 }
            java.io.File r2 = r6.getExternalFilesDir(r0)     // Catch:{ IOException -> 0x007a, all -> 0x0076 }
            java.lang.String r3 = "push_cdata.lock"
            r1.<init>(r2, r3)     // Catch:{ IOException -> 0x007a, all -> 0x0076 }
            com.xiaomi.push.y.a((java.io.File) r1)     // Catch:{ IOException -> 0x007a, all -> 0x0076 }
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x007a, all -> 0x0076 }
            java.lang.String r3 = "rw"
            r2.<init>(r1, r3)     // Catch:{ IOException -> 0x007a, all -> 0x0076 }
            java.nio.channels.FileChannel r1 = r2.getChannel()     // Catch:{ IOException -> 0x0073, all -> 0x0070 }
            java.nio.channels.FileLock r1 = r1.lock()     // Catch:{ IOException -> 0x0073, all -> 0x0070 }
            java.io.File r3 = new java.io.File     // Catch:{ IOException -> 0x006c, all -> 0x006a }
            java.io.File r6 = r6.getExternalFilesDir(r0)     // Catch:{ IOException -> 0x006c, all -> 0x006a }
            java.lang.String r4 = "push_cdata.data"
            r3.<init>(r6, r4)     // Catch:{ IOException -> 0x006c, all -> 0x006a }
            java.io.BufferedOutputStream r6 = new java.io.BufferedOutputStream     // Catch:{ IOException -> 0x006c, all -> 0x006a }
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x006c, all -> 0x006a }
            r5 = 1
            r4.<init>(r3, r5)     // Catch:{ IOException -> 0x006c, all -> 0x006a }
            r6.<init>(r4)     // Catch:{ IOException -> 0x006c, all -> 0x006a }
            int r0 = r7.length     // Catch:{ IOException -> 0x0068, all -> 0x0066 }
            byte[] r0 = com.xiaomi.push.ac.a((int) r0)     // Catch:{ IOException -> 0x0068, all -> 0x0066 }
            r6.write(r0)     // Catch:{ IOException -> 0x0068, all -> 0x0066 }
            r6.write(r7)     // Catch:{ IOException -> 0x0068, all -> 0x0066 }
            r6.flush()     // Catch:{ IOException -> 0x0068, all -> 0x0066 }
            if (r1 == 0) goto L_0x005f
            boolean r7 = r1.isValid()     // Catch:{ all -> 0x00a0 }
            if (r7 == 0) goto L_0x005f
            r1.release()     // Catch:{ IOException -> 0x005f }
        L_0x005f:
            com.xiaomi.push.y.a((java.io.Closeable) r6)     // Catch:{ all -> 0x00a0 }
        L_0x0062:
            com.xiaomi.push.y.a((java.io.Closeable) r2)     // Catch:{ all -> 0x00a0 }
            goto L_0x008f
        L_0x0066:
            r7 = move-exception
            goto L_0x0093
        L_0x0068:
            r7 = move-exception
            goto L_0x006e
        L_0x006a:
            r7 = move-exception
            goto L_0x0094
        L_0x006c:
            r7 = move-exception
            r6 = r0
        L_0x006e:
            r0 = r1
            goto L_0x007d
        L_0x0070:
            r7 = move-exception
            r1 = r0
            goto L_0x0094
        L_0x0073:
            r7 = move-exception
            r6 = r0
            goto L_0x007d
        L_0x0076:
            r7 = move-exception
            r1 = r0
            r2 = r1
            goto L_0x0094
        L_0x007a:
            r7 = move-exception
            r6 = r0
            r2 = r6
        L_0x007d:
            r7.printStackTrace()     // Catch:{ all -> 0x0091 }
            if (r0 == 0) goto L_0x008b
            boolean r7 = r0.isValid()     // Catch:{ all -> 0x00a0 }
            if (r7 == 0) goto L_0x008b
            r0.release()     // Catch:{ IOException -> 0x008b }
        L_0x008b:
            com.xiaomi.push.y.a((java.io.Closeable) r6)     // Catch:{ all -> 0x00a0 }
            goto L_0x0062
        L_0x008f:
            monitor-exit(r8)     // Catch:{ all -> 0x00a0 }
            return
        L_0x0091:
            r7 = move-exception
            r1 = r0
        L_0x0093:
            r0 = r6
        L_0x0094:
            if (r1 == 0) goto L_0x00a2
            boolean r6 = r1.isValid()     // Catch:{ all -> 0x00a0 }
            if (r6 == 0) goto L_0x00a2
            r1.release()     // Catch:{ IOException -> 0x00a2 }
            goto L_0x00a2
        L_0x00a0:
            r6 = move-exception
            goto L_0x00a9
        L_0x00a2:
            com.xiaomi.push.y.a((java.io.Closeable) r0)     // Catch:{ all -> 0x00a0 }
            com.xiaomi.push.y.a((java.io.Closeable) r2)     // Catch:{ all -> 0x00a0 }
            throw r7     // Catch:{ all -> 0x00a0 }
        L_0x00a9:
            monitor-exit(r8)     // Catch:{ all -> 0x00a0 }
            throw r6
        L_0x00ab:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.dx.a(android.content.Context, com.xiaomi.push.ho, java.lang.String):void");
    }

    public abstract hi a();

    /* renamed from: a  reason: collision with other method in class */
    public abstract String m185a();

    /* access modifiers changed from: protected */
    /* renamed from: a  reason: collision with other method in class */
    public boolean m186a() {
        return ag.a(this.f241a, String.valueOf(a()), (long) this.a);
    }

    /* access modifiers changed from: protected */
    public boolean b() {
        return true;
    }

    public void run() {
        if (a()) {
            dk a2 = dl.a().a();
            String a3 = a2 == null ? "" : a2.a();
            if (!TextUtils.isEmpty(a3) && b()) {
                String a4 = a();
                if (!TextUtils.isEmpty(a4)) {
                    ho hoVar = new ho();
                    hoVar.a(a4);
                    hoVar.a(System.currentTimeMillis());
                    hoVar.a(a());
                    a(this.f241a, hoVar, a3);
                }
            }
        }
    }
}
