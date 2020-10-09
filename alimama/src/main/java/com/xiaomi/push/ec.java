package com.xiaomi.push;

import android.content.Context;
import android.content.SharedPreferences;
import com.xiaomi.push.ai;
import com.xiaomi.push.service.ag;
import java.io.File;
import java.util.List;

public class ec extends ai.a {
    private Context a;

    /* renamed from: a  reason: collision with other field name */
    private SharedPreferences f242a;

    /* renamed from: a  reason: collision with other field name */
    private ag f243a;

    public ec(Context context) {
        this.a = context;
        this.f242a = context.getSharedPreferences("mipush_extra", 0);
        this.f243a = ag.a(context);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:46|45|61|63|64|66|67|29|68|69) */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:13|14|(3:15|16|(2:73|18)(2:19|(1:74)(4:30|31|(1:75)(2:33|(2:35|76)(2:36|77))|72)))|(3:22|23|(2:25|26))|27|28) */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:43|44|(0)|55|56|57) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0066 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x009f */
    /* JADX WARNING: Missing exception handler attribute for start block: B:66:0x00b6 */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0096 A[SYNTHETIC, Splitter:B:50:0x0096] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:27:0x0066=Splitter:B:27:0x0066, B:66:0x00b6=Splitter:B:66:0x00b6, B:55:0x009f=Splitter:B:55:0x009f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<com.xiaomi.push.ho> a(java.io.File r11) {
        /*
            r10 = this;
            com.xiaomi.push.dl r0 = com.xiaomi.push.dl.a()
            com.xiaomi.push.dk r0 = r0.a()
            if (r0 != 0) goto L_0x000d
            java.lang.String r0 = ""
            goto L_0x0011
        L_0x000d:
            java.lang.String r0 = r0.a()
        L_0x0011:
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            r2 = 0
            if (r1 == 0) goto L_0x0019
            return r2
        L_0x0019:
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r3 = 4
            byte[] r4 = new byte[r3]
            java.lang.Object r5 = com.xiaomi.push.dq.a
            monitor-enter(r5)
            java.io.File r6 = new java.io.File     // Catch:{ Exception -> 0x00a6, all -> 0x0091 }
            android.content.Context r7 = r10.a     // Catch:{ Exception -> 0x00a6, all -> 0x0091 }
            java.io.File r7 = r7.getExternalFilesDir(r2)     // Catch:{ Exception -> 0x00a6, all -> 0x0091 }
            java.lang.String r8 = "push_cdata.lock"
            r6.<init>(r7, r8)     // Catch:{ Exception -> 0x00a6, all -> 0x0091 }
            com.xiaomi.push.y.a((java.io.File) r6)     // Catch:{ Exception -> 0x00a6, all -> 0x0091 }
            java.io.RandomAccessFile r7 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x00a6, all -> 0x0091 }
            java.lang.String r8 = "rw"
            r7.<init>(r6, r8)     // Catch:{ Exception -> 0x00a6, all -> 0x0091 }
            java.nio.channels.FileChannel r6 = r7.getChannel()     // Catch:{ Exception -> 0x008f, all -> 0x008c }
            java.nio.channels.FileLock r6 = r6.lock()     // Catch:{ Exception -> 0x008f, all -> 0x008c }
            java.io.FileInputStream r8 = new java.io.FileInputStream     // Catch:{ Exception -> 0x008a, all -> 0x0088 }
            r8.<init>(r11)     // Catch:{ Exception -> 0x008a, all -> 0x0088 }
        L_0x0048:
            int r11 = r8.read(r4)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            if (r11 == r3) goto L_0x004f
            goto L_0x005b
        L_0x004f:
            int r11 = com.xiaomi.push.ac.a((byte[]) r4)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            byte[] r2 = new byte[r11]     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            int r9 = r8.read(r2)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            if (r9 == r11) goto L_0x006d
        L_0x005b:
            if (r6 == 0) goto L_0x0066
            boolean r11 = r6.isValid()     // Catch:{ all -> 0x00b4 }
            if (r11 == 0) goto L_0x0066
            r6.release()     // Catch:{ IOException -> 0x0066 }
        L_0x0066:
            com.xiaomi.push.y.a((java.io.Closeable) r8)     // Catch:{ all -> 0x00b4 }
        L_0x0069:
            com.xiaomi.push.y.a((java.io.Closeable) r7)     // Catch:{ all -> 0x00b4 }
            goto L_0x00ba
        L_0x006d:
            byte[] r11 = com.xiaomi.push.dp.a(r0, r2)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            if (r11 == 0) goto L_0x0048
            int r2 = r11.length     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            if (r2 != 0) goto L_0x0077
            goto L_0x0048
        L_0x0077:
            com.xiaomi.push.ho r2 = new com.xiaomi.push.ho     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            r2.<init>()     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            com.xiaomi.push.iq.a(r2, (byte[]) r11)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            r1.add(r2)     // Catch:{ Exception -> 0x0086, all -> 0x0083 }
            goto L_0x0048
        L_0x0083:
            r11 = move-exception
            r2 = r8
            goto L_0x0094
        L_0x0086:
            r2 = r8
            goto L_0x00a8
        L_0x0088:
            r11 = move-exception
            goto L_0x0094
        L_0x008a:
            goto L_0x00a8
        L_0x008c:
            r11 = move-exception
            r6 = r2
            goto L_0x0094
        L_0x008f:
            r6 = r2
            goto L_0x00a8
        L_0x0091:
            r11 = move-exception
            r6 = r2
            r7 = r6
        L_0x0094:
            if (r6 == 0) goto L_0x009f
            boolean r0 = r6.isValid()     // Catch:{ all -> 0x00b4 }
            if (r0 == 0) goto L_0x009f
            r6.release()     // Catch:{ IOException -> 0x009f }
        L_0x009f:
            com.xiaomi.push.y.a((java.io.Closeable) r2)     // Catch:{ all -> 0x00b4 }
            com.xiaomi.push.y.a((java.io.Closeable) r7)     // Catch:{ all -> 0x00b4 }
            throw r11     // Catch:{ all -> 0x00b4 }
        L_0x00a6:
            r6 = r2
            r7 = r6
        L_0x00a8:
            if (r6 == 0) goto L_0x00b6
            boolean r11 = r6.isValid()     // Catch:{ all -> 0x00b4 }
            if (r11 == 0) goto L_0x00b6
            r6.release()     // Catch:{ IOException -> 0x00b6 }
            goto L_0x00b6
        L_0x00b4:
            r11 = move-exception
            goto L_0x00bc
        L_0x00b6:
            com.xiaomi.push.y.a((java.io.Closeable) r2)     // Catch:{ all -> 0x00b4 }
            goto L_0x0069
        L_0x00ba:
            monitor-exit(r5)     // Catch:{ all -> 0x00b4 }
            return r1
        L_0x00bc:
            monitor-exit(r5)     // Catch:{ all -> 0x00b4 }
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ec.a(java.io.File):java.util.List");
    }

    private void a() {
        SharedPreferences.Editor edit = this.f242a.edit();
        edit.putLong("last_upload_data_timestamp", System.currentTimeMillis() / 1000);
        edit.commit();
    }

    /* renamed from: a  reason: collision with other method in class */
    private boolean m197a() {
        if (as.d(this.a)) {
            return false;
        }
        if (!as.f(this.a) || c()) {
            return (as.g(this.a) && !b()) || as.h(this.a);
        }
        return true;
    }

    private boolean b() {
        if (!this.f243a.a(hl.Upload3GSwitch.a(), true)) {
            return false;
        }
        return Math.abs((System.currentTimeMillis() / 1000) - this.f242a.getLong("last_upload_data_timestamp", -1)) > ((long) Math.max(86400, this.f243a.a(hl.Upload3GFrequency.a(), 432000)));
    }

    private boolean c() {
        if (!this.f243a.a(hl.Upload4GSwitch.a(), true)) {
            return false;
        }
        return Math.abs((System.currentTimeMillis() / 1000) - this.f242a.getLong("last_upload_data_timestamp", -1)) > ((long) Math.max(86400, this.f243a.a(hl.Upload4GFrequency.a(), 259200)));
    }

    /* renamed from: a  reason: collision with other method in class */
    public int m198a() {
        return 1;
    }

    public void run() {
        File file = new File(this.a.getExternalFilesDir((String) null), "push_cdata.data");
        if (!as.c(this.a)) {
            if (file.length() > 1863680) {
                file.delete();
            }
        } else if (!a() && file.exists()) {
            List<ho> a2 = a(file);
            if (!ad.a(a2)) {
                int size = a2.size();
                if (size > 4000) {
                    a2 = a2.subList(size - 4000, size);
                }
                hz hzVar = new hz();
                hzVar.a(a2);
                byte[] a3 = y.a(iq.a(hzVar));
                Cif ifVar = new Cif("-1", false);
                ifVar.c(hq.DataCollection.f485a);
                ifVar.a(a3);
                dk a4 = dl.a().a();
                if (a4 != null) {
                    a4.a(ifVar, hg.Notification, (ht) null);
                }
                a();
            }
            file.delete();
            this.f242a.edit().remove("ltapn").commit();
        }
    }
}
