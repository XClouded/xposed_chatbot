package org.android.agoo.common;

import android.text.TextUtils;

public final class MurmurHashUtil {
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final int hash(byte[] r7, int r8, int r9, int r10) {
        /*
            r10 = r10 ^ r9
            int r0 = r9 >> 2
            r1 = 0
            r2 = r10
            r10 = 0
        L_0x0006:
            r3 = 1540483477(0x5bd1e995, float:1.18170193E17)
            if (r10 >= r0) goto L_0x0039
            int r4 = r10 << 2
            int r4 = r4 + r8
            int r5 = r4 + 3
            byte r5 = r7[r5]
            int r5 = r5 << 8
            int r6 = r4 + 2
            byte r6 = r7[r6]
            r6 = r6 & 255(0xff, float:3.57E-43)
            r5 = r5 | r6
            int r5 = r5 << 8
            int r6 = r4 + 1
            byte r6 = r7[r6]
            r6 = r6 & 255(0xff, float:3.57E-43)
            r5 = r5 | r6
            int r5 = r5 << 8
            int r4 = r4 + r1
            byte r4 = r7[r4]
            r4 = r4 & 255(0xff, float:3.57E-43)
            r4 = r4 | r5
            int r4 = r4 * r3
            int r5 = r4 >>> 24
            r4 = r4 ^ r5
            int r4 = r4 * r3
            int r2 = r2 * r3
            r2 = r2 ^ r4
            int r10 = r10 + 1
            goto L_0x0006
        L_0x0039:
            r10 = 2
            int r0 = r0 << r10
            int r0 = r9 - r0
            if (r0 == 0) goto L_0x005e
            r1 = 3
            if (r0 < r1) goto L_0x004a
            int r4 = r8 + r9
            int r4 = r4 - r1
            byte r1 = r7[r4]
            int r1 = r1 << 16
            r2 = r2 ^ r1
        L_0x004a:
            if (r0 < r10) goto L_0x0054
            int r1 = r8 + r9
            int r1 = r1 - r10
            byte r10 = r7[r1]
            int r10 = r10 << 8
            r2 = r2 ^ r10
        L_0x0054:
            r10 = 1
            if (r0 < r10) goto L_0x005c
            int r8 = r8 + r9
            int r8 = r8 - r10
            byte r7 = r7[r8]
            r2 = r2 ^ r7
        L_0x005c:
            int r2 = r2 * r3
        L_0x005e:
            int r7 = r2 >>> 13
            r7 = r7 ^ r2
            int r7 = r7 * r3
            int r8 = r7 >>> 15
            r7 = r7 ^ r8
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.agoo.common.MurmurHashUtil.hash(byte[], int, int, int):int");
    }

    public static final int getRandom(long j, String str) {
        if (!TextUtils.isEmpty(str)) {
            byte[] bytes = str.getBytes();
            return (int) (((long) Math.abs(hash(bytes, 0, bytes.length, Integer.MAX_VALUE))) % j);
        }
        double random = Math.random();
        double d = (double) j;
        Double.isNaN(d);
        return (int) (random * d);
    }
}
