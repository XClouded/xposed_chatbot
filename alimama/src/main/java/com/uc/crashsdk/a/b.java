package com.uc.crashsdk.a;

import com.facebook.imageutils.JfifUtil;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import kotlin.UByte;

/* compiled from: ProGuard */
public final class b {
    private static final int[] a = {126, 147, 115, 241, 101, 198, JfifUtil.MARKER_RST7, 134};
    private static final int[] b = {125, 185, 233, 226, 129, 142, 151, 176};
    private static final int[] c = {238, 185, 233, 179, 129, 142, 151, 167};

    public static String a(String str) {
        FileInputStream fileInputStream;
        File file = new File(str);
        FileInputStream fileInputStream2 = null;
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fileInputStream3 = new FileInputStream(file);
            try {
                byte[] bArr = new byte[((int) file.length())];
                fileInputStream3.read(bArr);
                g.a((Closeable) fileInputStream3);
                byte[] a2 = a(bArr, a);
                if (a2 == null || a2.length <= 0) {
                    g.a((Closeable) null);
                    return null;
                }
                int length = a2.length - 1;
                String str2 = a2[length] == 10 ? new String(a2, 0, length) : new String(a2);
                g.a((Closeable) null);
                return str2;
            } catch (Exception e) {
                Exception exc = e;
                fileInputStream = fileInputStream3;
                e = exc;
                try {
                    g.a((Throwable) e);
                    g.a((Closeable) fileInputStream);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    fileInputStream2 = fileInputStream;
                    g.a((Closeable) fileInputStream2);
                    throw th;
                }
            } catch (Throwable th2) {
                fileInputStream2 = fileInputStream3;
                th = th2;
                g.a((Closeable) fileInputStream2);
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            fileInputStream = null;
            g.a((Throwable) e);
            g.a((Closeable) fileInputStream);
            return null;
        } catch (Throwable th3) {
            th = th3;
            g.a((Closeable) fileInputStream2);
            throw th;
        }
    }

    private static byte[] a(File file) {
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2 = null;
        if (!file.exists()) {
            return null;
        }
        try {
            byte[] bArr = new byte[((int) file.length())];
            fileInputStream = new FileInputStream(file);
            try {
                fileInputStream.read(bArr);
                g.a((Closeable) fileInputStream);
                return bArr;
            } catch (Throwable th) {
                th = th;
                try {
                    g.a(th);
                    g.a((Closeable) fileInputStream);
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    fileInputStream2 = fileInputStream;
                    g.a((Closeable) fileInputStream2);
                    throw th;
                }
            }
        } catch (Throwable th3) {
            th = th3;
            g.a((Closeable) fileInputStream2);
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x006e A[ADDED_TO_REGION] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(java.lang.String r7, java.lang.String r8, boolean r9) {
        /*
            if (r9 != 0) goto L_0x0003
            return r7
        L_0x0003:
            boolean r0 = com.uc.crashsdk.a.g.a((java.lang.String) r7)
            if (r0 == 0) goto L_0x000a
            return r7
        L_0x000a:
            java.io.File r0 = new java.io.File
            r0.<init>(r7)
            boolean r1 = r0.exists()
            if (r1 == 0) goto L_0x00cd
            long r1 = r0.length()
            r3 = 3145728(0x300000, double:1.554196E-317)
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 <= 0) goto L_0x0022
            goto L_0x00cd
        L_0x0022:
            byte[] r1 = a((java.io.File) r0)
            if (r1 == 0) goto L_0x00cc
            int r2 = r1.length
            if (r2 > 0) goto L_0x002d
            goto L_0x00cc
        L_0x002d:
            r2 = 1
            r3 = 0
            if (r9 == 0) goto L_0x0090
            r9 = 0
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x0055, all -> 0x0052 }
            r4.<init>()     // Catch:{ Throwable -> 0x0055, all -> 0x0052 }
            java.util.zip.GZIPOutputStream r5 = new java.util.zip.GZIPOutputStream     // Catch:{ Throwable -> 0x004d, all -> 0x004b }
            r5.<init>(r4)     // Catch:{ Throwable -> 0x004d, all -> 0x004b }
            r5.write(r1)     // Catch:{ Throwable -> 0x0049 }
            r4.flush()     // Catch:{ Throwable -> 0x0049 }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r4)
        L_0x0045:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r5)
            goto L_0x0060
        L_0x0049:
            r9 = move-exception
            goto L_0x0059
        L_0x004b:
            r7 = move-exception
            goto L_0x0089
        L_0x004d:
            r5 = move-exception
            r6 = r5
            r5 = r9
            r9 = r6
            goto L_0x0059
        L_0x0052:
            r7 = move-exception
            r4 = r9
            goto L_0x0089
        L_0x0055:
            r4 = move-exception
            r5 = r9
            r9 = r4
            r4 = r5
        L_0x0059:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r9)     // Catch:{ all -> 0x0087 }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r4)
            goto L_0x0045
        L_0x0060:
            byte[] r9 = r4.toByteArray()     // Catch:{ Throwable -> 0x0067 }
            r1 = r9
            r9 = 1
            goto L_0x006c
        L_0x0067:
            r9 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r9)
            r9 = 0
        L_0x006c:
            if (r9 == 0) goto L_0x0086
            if (r1 == 0) goto L_0x0086
            int r9 = r1.length
            if (r9 > 0) goto L_0x0074
            goto L_0x0086
        L_0x0074:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r9.append(r7)
            r9.append(r8)
            java.lang.String r8 = r9.toString()
            r9 = r8
            r8 = 1
            goto L_0x0092
        L_0x0086:
            return r7
        L_0x0087:
            r7 = move-exception
            r9 = r5
        L_0x0089:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r4)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r9)
            throw r7
        L_0x0090:
            r9 = r7
            r8 = 0
        L_0x0092:
            if (r8 == 0) goto L_0x00cb
            java.lang.String r8 = r0.getName()
            boolean r8 = r9.equals(r8)
            if (r8 == 0) goto L_0x00b1
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r9)
            java.lang.String r4 = ".tmp"
            r8.append(r4)
            java.lang.String r8 = r8.toString()
            r4 = 1
            goto L_0x00b3
        L_0x00b1:
            r8 = r9
            r4 = 0
        L_0x00b3:
            java.io.File r5 = new java.io.File
            r5.<init>(r8)
            boolean r8 = com.uc.crashsdk.a.g.a((java.io.File) r5, (byte[]) r1)
            if (r8 != 0) goto L_0x00c0
            r2 = 0
            goto L_0x00c8
        L_0x00c0:
            if (r4 == 0) goto L_0x00c8
            r0.delete()
            r5.renameTo(r0)
        L_0x00c8:
            if (r2 == 0) goto L_0x00cb
            return r9
        L_0x00cb:
            return r7
        L_0x00cc:
            return r7
        L_0x00cd:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.a.b.a(java.lang.String, java.lang.String, boolean):java.lang.String");
    }

    private static byte[] a(byte[] bArr, int[] iArr) {
        if (bArr.length - 0 < 2 || iArr == null || iArr.length != 8) {
            return null;
        }
        int length = (bArr.length - 2) - 0;
        try {
            byte[] bArr2 = new byte[length];
            byte b2 = 0;
            for (int i = 0; i < length; i++) {
                byte b3 = (byte) (bArr[i + 0] ^ iArr[i % 8]);
                bArr2[i] = b3;
                b2 = (byte) (b2 ^ b3);
            }
            if (bArr[length + 0] == ((byte) ((iArr[0] ^ b2) & UByte.MAX_VALUE)) && bArr[length + 1 + 0] == ((byte) ((iArr[1] ^ b2) & UByte.MAX_VALUE))) {
                return bArr2;
            }
            return null;
        } catch (Exception e) {
            g.a((Throwable) e);
            return null;
        }
    }

    private static byte[] b(byte[] bArr, int[] iArr) {
        if (bArr == null || iArr == null || iArr.length != 8) {
            return null;
        }
        int length = bArr.length;
        try {
            byte[] bArr2 = new byte[(length + 2)];
            byte b2 = 0;
            for (int i = 0; i < length; i++) {
                byte b3 = bArr[i];
                bArr2[i] = (byte) (iArr[i % 8] ^ b3);
                b2 = (byte) (b2 ^ b3);
            }
            bArr2[length] = (byte) (iArr[0] ^ b2);
            bArr2[length + 1] = (byte) (iArr[1] ^ b2);
            return bArr2;
        } catch (Exception e) {
            g.a((Throwable) e);
            return null;
        }
    }

    public static boolean a(String str, String str2) {
        FileOutputStream fileOutputStream;
        try {
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
            fileOutputStream = new FileOutputStream(file);
        } catch (Throwable th) {
            g.a(th);
            fileOutputStream = null;
        }
        boolean z = false;
        if (fileOutputStream == null) {
            return false;
        }
        byte[] b2 = b(str2.getBytes(), a);
        if (b2 == null) {
            g.a((Closeable) fileOutputStream);
            return false;
        }
        try {
            fileOutputStream.write(b2);
            z = true;
        } catch (Throwable th2) {
            g.a((Closeable) fileOutputStream);
            throw th2;
        }
        g.a((Closeable) fileOutputStream);
        return z;
    }
}
