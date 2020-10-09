package com.xiaomi.push;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import java.io.File;

public class ea extends dx {
    public ea(Context context, int i) {
        super(context, i);
    }

    private double a(double d) {
        int i = 1;
        while (true) {
            double d2 = (double) i;
            if (d2 >= d) {
                return d2;
            }
            i <<= 1;
        }
    }

    private long a(File file) {
        StatFs statFs = new StatFs(file.getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getBlockCount());
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x006b A[SYNTHETIC, Splitter:B:24:0x006b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String b() {
        /*
            r6 = this;
            java.lang.String r0 = "0"
            java.lang.String r1 = "/proc/meminfo"
            java.io.File r2 = new java.io.File
            r2.<init>(r1)
            boolean r2 = r2.exists()
            if (r2 == 0) goto L_0x0075
            r2 = 0
            java.io.FileReader r3 = new java.io.FileReader     // Catch:{ Exception -> 0x0067 }
            r3.<init>(r1)     // Catch:{ Exception -> 0x0067 }
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0067 }
            r4 = 8192(0x2000, float:1.14794E-41)
            r1.<init>(r3, r4)     // Catch:{ Exception -> 0x0067 }
            java.lang.String r2 = r1.readLine()     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
            if (r3 != 0) goto L_0x005c
            java.lang.String r3 = "\\s+"
            java.lang.String[] r2 = r2.split(r3)     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
            if (r2 == 0) goto L_0x005c
            int r3 = r2.length     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
            r4 = 2
            if (r3 < r4) goto L_0x005c
            r0 = 1
            r0 = r2[r0]     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
            java.lang.Double r0 = java.lang.Double.valueOf(r0)     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
            double r2 = r0.doubleValue()     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
            r4 = 4652218415073722368(0x4090000000000000, double:1024.0)
            double r2 = r2 / r4
            double r2 = r2 / r4
            r4 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x004b
            double r2 = java.lang.Math.ceil(r2)     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
        L_0x004b:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
            r0.<init>()     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
            r0.append(r2)     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
            java.lang.String r2 = ""
            r0.append(r2)     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0062, all -> 0x0060 }
        L_0x005c:
            r1.close()     // Catch:{ IOException -> 0x0075 }
            goto L_0x0075
        L_0x0060:
            r0 = move-exception
            goto L_0x006f
        L_0x0062:
            r2 = r1
            goto L_0x0067
        L_0x0064:
            r0 = move-exception
            r1 = r2
            goto L_0x006f
        L_0x0067:
            java.lang.String r0 = "0"
            if (r2 == 0) goto L_0x0075
            r2.close()     // Catch:{ IOException -> 0x0075 }
            goto L_0x0075
        L_0x006f:
            if (r1 == 0) goto L_0x0074
            r1.close()     // Catch:{ IOException -> 0x0074 }
        L_0x0074:
            throw r0
        L_0x0075:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r0 = "GB"
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ea.b():java.lang.String");
    }

    private String c() {
        double a = (double) a(Environment.getDataDirectory());
        Double.isNaN(a);
        double a2 = a(((a / 1024.0d) / 1024.0d) / 1024.0d);
        return a2 + "GB";
    }

    public int a() {
        return 23;
    }

    /* renamed from: a  reason: collision with other method in class */
    public hi m193a() {
        return hi.Storage;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m194a() {
        return "ram:" + b() + "," + "rom:" + c();
    }
}
