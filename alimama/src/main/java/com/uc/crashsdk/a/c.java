package com.uc.crashsdk.a;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.coloros.mcssdk.c.a;
import java.io.File;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: ProGuard */
public class c {
    static final /* synthetic */ boolean a = (!c.class.desiredAssertionStatus());
    private static String b = "";

    private static NetworkInfo c() {
        NetworkInfo networkInfo;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) g.d("connectivity");
            if (connectivityManager == null) {
                a.c("get CONNECTIVITY_SERVICE is null");
                return null;
            }
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                try {
                    if (networkInfo.isConnected()) {
                        return networkInfo;
                    }
                } catch (Throwable th) {
                    th = th;
                    g.a(th);
                    return networkInfo;
                }
            }
            NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
            if (allNetworkInfo == null) {
                return networkInfo;
            }
            for (int i = 0; i < allNetworkInfo.length; i++) {
                if (allNetworkInfo[i] != null && allNetworkInfo[i].isConnected()) {
                    return allNetworkInfo[i];
                }
            }
            return networkInfo;
        } catch (Throwable th2) {
            th = th2;
            networkInfo = null;
            g.a(th);
            return networkInfo;
        }
    }

    public static String a() {
        NetworkInfo c = c();
        if (c == null) {
            return "-1";
        }
        if (c.getType() == 1) {
            return "wifi";
        }
        switch (c.getSubtype()) {
            case 1:
                return "2.5G";
            case 2:
            case 7:
                return "2.75G";
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return "3G";
            case 4:
            case 11:
                return "2G";
            case 13:
                return "4G";
            default:
                String subtypeName = c.getSubtypeName();
                if (g.a(subtypeName)) {
                    return "0";
                }
                return (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")) ? "3G" : subtypeName;
        }
    }

    public static byte[] a(byte[] bArr, byte[] bArr2) {
        return a(bArr, bArr2, true, false);
    }

    public static byte[] a(byte[] bArr, byte[] bArr2, boolean z) {
        return a(bArr, bArr2, z, true);
    }

    private static byte[] a(byte[] bArr, byte[] bArr2, boolean z, boolean z2) {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
        Cipher instance = Cipher.getInstance(a.a);
        instance.init(z ? 1 : 2, secretKeySpec, ivParameterSpec);
        if (!z) {
            return instance.doFinal(bArr);
        }
        if (!z2) {
            bArr = a(bArr);
        }
        return instance.doFinal(bArr);
    }

    private static byte[] a(byte[] bArr) {
        byte[] bArr2 = new byte[(bArr.length + 16)];
        int length = bArr.length;
        bArr2[0] = (byte) ((length >> 0) & 255);
        bArr2[1] = (byte) ((length >> 8) & 255);
        bArr2[2] = (byte) ((length >> 16) & 255);
        bArr2[3] = (byte) ((length >> 24) & 255);
        for (int i = 4; i < 16; i++) {
            bArr2[i] = 0;
        }
        System.arraycopy(bArr, 0, bArr2, 16, bArr.length);
        return bArr2;
    }

    public static byte[] b() {
        return new byte[]{48, 25, 6, 55};
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v5, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v7, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v10, resolved type: java.io.ByteArrayOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v6, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: java.io.InputStream} */
    /* JADX WARNING: type inference failed for: r4v0, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r4v2, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r4v3 */
    /* JADX WARNING: type inference failed for: r4v4 */
    /* JADX WARNING: type inference failed for: r4v6 */
    /* JADX WARNING: type inference failed for: r4v9 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00b1 A[SYNTHETIC, Splitter:B:51:0x00b1] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00c4 A[SYNTHETIC, Splitter:B:61:0x00c4] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] a(java.lang.String r8, byte[] r9) {
        /*
            r0 = 0
            java.net.URL r1 = new java.net.URL     // Catch:{ Throwable -> 0x00b5, all -> 0x00a1 }
            r1.<init>(r8)     // Catch:{ Throwable -> 0x00b5, all -> 0x00a1 }
            java.net.URLConnection r8 = r1.openConnection()     // Catch:{ Throwable -> 0x00b5, all -> 0x00a1 }
            java.net.HttpURLConnection r8 = (java.net.HttpURLConnection) r8     // Catch:{ Throwable -> 0x00b5, all -> 0x00a1 }
            r1 = 5000(0x1388, float:7.006E-42)
            r8.setConnectTimeout(r1)     // Catch:{ Throwable -> 0x009f, all -> 0x009a }
            r8.setReadTimeout(r1)     // Catch:{ Throwable -> 0x009f, all -> 0x009a }
            r1 = 1
            r8.setDoInput(r1)     // Catch:{ Throwable -> 0x009f, all -> 0x009a }
            r8.setDoOutput(r1)     // Catch:{ Throwable -> 0x009f, all -> 0x009a }
            java.lang.String r1 = "POST"
            r8.setRequestMethod(r1)     // Catch:{ Throwable -> 0x009f, all -> 0x009a }
            r1 = 0
            r8.setUseCaches(r1)     // Catch:{ Throwable -> 0x009f, all -> 0x009a }
            java.lang.String r2 = "Content-Type"
            java.lang.String r3 = "application/x-www-form-urlencoded"
            r8.setRequestProperty(r2, r3)     // Catch:{ Throwable -> 0x009f, all -> 0x009a }
            java.lang.String r2 = "Content-Length"
            int r3 = r9.length     // Catch:{ Throwable -> 0x009f, all -> 0x009a }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ Throwable -> 0x009f, all -> 0x009a }
            r8.setRequestProperty(r2, r3)     // Catch:{ Throwable -> 0x009f, all -> 0x009a }
            java.io.OutputStream r2 = r8.getOutputStream()     // Catch:{ Throwable -> 0x009f, all -> 0x009a }
            r2.write(r9)     // Catch:{ Throwable -> 0x0097, all -> 0x0091 }
            int r9 = r8.getResponseCode()     // Catch:{ Throwable -> 0x0097, all -> 0x0091 }
            r3 = 200(0xc8, float:2.8E-43)
            if (r9 == r3) goto L_0x0053
            com.uc.crashsdk.a.g.a((java.io.Closeable) r2)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r0)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r0)
            if (r8 == 0) goto L_0x0052
            r8.disconnect()     // Catch:{ Throwable -> 0x0052 }
        L_0x0052:
            return r0
        L_0x0053:
            java.io.InputStream r9 = r8.getInputStream()     // Catch:{ Throwable -> 0x0097, all -> 0x0091 }
            r3 = 1024(0x400, float:1.435E-42)
            byte[] r3 = new byte[r3]     // Catch:{ Throwable -> 0x008f, all -> 0x0087 }
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x008f, all -> 0x0087 }
            int r5 = r9.available()     // Catch:{ Throwable -> 0x008f, all -> 0x0087 }
            r4.<init>(r5)     // Catch:{ Throwable -> 0x008f, all -> 0x0087 }
        L_0x0064:
            int r5 = r9.read(r3)     // Catch:{ Throwable -> 0x00b9, all -> 0x0082 }
            r6 = -1
            if (r5 == r6) goto L_0x006f
            r4.write(r3, r1, r5)     // Catch:{ Throwable -> 0x00b9, all -> 0x0082 }
            goto L_0x0064
        L_0x006f:
            byte[] r1 = r4.toByteArray()     // Catch:{ Throwable -> 0x00b9, all -> 0x0082 }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r2)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r9)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r4)
            if (r8 == 0) goto L_0x0081
            r8.disconnect()     // Catch:{ Throwable -> 0x0081 }
        L_0x0081:
            return r1
        L_0x0082:
            r0 = move-exception
            r1 = r8
            r8 = r9
            r9 = r0
            goto L_0x0095
        L_0x0087:
            r1 = move-exception
            r4 = r0
            r0 = r2
            r7 = r1
            r1 = r8
            r8 = r9
            r9 = r7
            goto L_0x00a6
        L_0x008f:
            r4 = r0
            goto L_0x00b9
        L_0x0091:
            r9 = move-exception
            r1 = r8
            r8 = r0
            r4 = r8
        L_0x0095:
            r0 = r2
            goto L_0x00a6
        L_0x0097:
            r9 = r0
            r4 = r9
            goto L_0x00b9
        L_0x009a:
            r9 = move-exception
            r1 = r8
            r8 = r0
            r4 = r8
            goto L_0x00a6
        L_0x009f:
            r9 = r0
            goto L_0x00b7
        L_0x00a1:
            r8 = move-exception
            r9 = r8
            r8 = r0
            r1 = r8
            r4 = r1
        L_0x00a6:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r0)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r8)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r4)
            if (r1 == 0) goto L_0x00b4
            r1.disconnect()     // Catch:{ Throwable -> 0x00b4 }
        L_0x00b4:
            throw r9
        L_0x00b5:
            r8 = r0
            r9 = r8
        L_0x00b7:
            r2 = r9
            r4 = r2
        L_0x00b9:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r2)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r9)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r4)
            if (r8 == 0) goto L_0x00c7
            r8.disconnect()     // Catch:{ Throwable -> 0x00c7 }
        L_0x00c7:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.a.c.a(java.lang.String, byte[]):byte[]");
    }

    public static void a(byte[] bArr, int i, byte[] bArr2) {
        if (a || bArr2.length == 4) {
            for (int i2 = 0; i2 < 4; i2++) {
                bArr[i2 + i] = bArr2[i2];
            }
            return;
        }
        throw new AssertionError();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0027, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0030, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0031, code lost:
        r1 = r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0030 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:7:0x0017] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] a(java.io.File r6) {
        /*
            boolean r0 = r6.isFile()
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            long r2 = r6.length()     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            int r0 = (int) r2     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            r2.<init>(r6)     // Catch:{ Exception -> 0x003d, all -> 0x003a }
            java.io.BufferedInputStream r6 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x0037 }
            r6.<init>(r2)     // Catch:{ Exception -> 0x0037 }
            byte[] r3 = new byte[r0]     // Catch:{ Exception -> 0x0033, all -> 0x0030 }
            r1 = 0
        L_0x001a:
            if (r1 >= r0) goto L_0x0029
            int r4 = r0 - r1
            int r4 = r6.read(r3, r1, r4)     // Catch:{ Exception -> 0x0027, all -> 0x0030 }
            r5 = -1
            if (r5 == r4) goto L_0x0029
            int r1 = r1 + r4
            goto L_0x001a
        L_0x0027:
            r0 = move-exception
            goto L_0x0035
        L_0x0029:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r6)
        L_0x002c:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r2)
            goto L_0x0047
        L_0x0030:
            r0 = move-exception
            r1 = r6
            goto L_0x0049
        L_0x0033:
            r0 = move-exception
            r3 = r1
        L_0x0035:
            r1 = r6
            goto L_0x0040
        L_0x0037:
            r0 = move-exception
            r3 = r1
            goto L_0x0040
        L_0x003a:
            r0 = move-exception
            r2 = r1
            goto L_0x0049
        L_0x003d:
            r0 = move-exception
            r2 = r1
            r3 = r2
        L_0x0040:
            com.uc.crashsdk.a.g.b((java.lang.Throwable) r0)     // Catch:{ all -> 0x0048 }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r1)
            goto L_0x002c
        L_0x0047:
            return r3
        L_0x0048:
            r0 = move-exception
        L_0x0049:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r1)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.a.c.a(java.io.File):byte[]");
    }

    public static boolean a(File file, String str, String str2) {
        for (int i = 0; i < 2; i++) {
            if (b(file, str, str2)) {
                return true;
            }
            a.b("upload try again: " + str);
        }
        return false;
    }

    private static boolean b(File file, String str, String str2) {
        try {
            byte[] a2 = a(file);
            if (a2 != null) {
                if (a2.length != 0) {
                    return a(a2, str, str2);
                }
            }
            return false;
        } catch (Exception e) {
            g.a((Throwable) e);
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:64:0x0167 A[SYNTHETIC, Splitter:B:64:0x0167] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0177 A[SYNTHETIC, Splitter:B:71:0x0177] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean a(byte[] r10, java.lang.String r11, java.lang.String r12) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Uploading to "
            r0.<init>(r1)
            r0.append(r12)
            java.lang.String r0 = r0.toString()
            com.uc.crashsdk.a.a.a(r0)
            r0 = 0
            r1 = 0
            java.net.URL r2 = new java.net.URL     // Catch:{ Throwable -> 0x0155, all -> 0x0150 }
            r2.<init>(r12)     // Catch:{ Throwable -> 0x0155, all -> 0x0150 }
            java.net.URLConnection r12 = r2.openConnection()     // Catch:{ Throwable -> 0x0155, all -> 0x0150 }
            java.net.HttpURLConnection r12 = (java.net.HttpURLConnection) r12     // Catch:{ Throwable -> 0x0155, all -> 0x0150 }
            r2 = 10000(0x2710, float:1.4013E-41)
            r12.setConnectTimeout(r2)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            r2 = 60000(0xea60, float:8.4078E-41)
            r12.setReadTimeout(r2)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            r2 = 1
            r12.setDoInput(r2)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            r12.setDoOutput(r2)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r3 = "POST"
            r12.setRequestMethod(r3)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            r12.setUseCaches(r1)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            r3.<init>()     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r4 = "------------izQ290kHh6g3Yn2IeyJCoc\r\n"
            r3.append(r4)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r4 = "Content-Disposition: form-data; name=\"file\";"
            r3.append(r4)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r4 = " filename=\""
            r3.append(r4)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            r3.append(r11)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r4 = "\"\r\n"
            r3.append(r4)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r4 = "Content-Type: application/octet-stream\r\n"
            r3.append(r4)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r4 = "\r\n"
            r3.append(r4)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r4 = "\r\n------------izQ290kHh6g3Yn2IeyJCoc--\r\n"
            int r5 = r3.length()     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            int r6 = r4.length()     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            int r5 = r5 + r6
            int r6 = r10.length     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            int r5 = r5 + r6
            java.lang.String r6 = "Content-Type"
            java.lang.String r7 = "multipart/form-data; boundary=----------izQ290kHh6g3Yn2IeyJCoc"
            r12.setRequestProperty(r6, r7)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r6 = "Content-Disposition"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r8 = "form-data; name=\"file\"; filename="
            r7.<init>(r8)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            r7.append(r11)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r11 = r7.toString()     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            r12.setRequestProperty(r6, r11)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r11 = "Content-Length"
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            r12.setRequestProperty(r11, r5)     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.io.OutputStream r11 = r12.getOutputStream()     // Catch:{ Throwable -> 0x014c, all -> 0x0148 }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            byte[] r3 = r3.getBytes()     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            r11.write(r3)     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            r11.write(r10)     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            byte[] r10 = r4.getBytes()     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            r11.write(r10)     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            int r10 = r12.getResponseCode()     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            java.lang.String r4 = "Response code: "
            r3.<init>(r4)     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            r3.append(r10)     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            com.uc.crashsdk.a.a.b(r3)     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            r3 = 200(0xc8, float:2.8E-43)
            if (r10 == r3) goto L_0x00cf
            com.uc.crashsdk.a.g.a((java.io.Closeable) r11)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r0)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r0)
            if (r12 == 0) goto L_0x00ce
            r12.disconnect()     // Catch:{ Throwable -> 0x00ce }
        L_0x00ce:
            return r1
        L_0x00cf:
            java.io.InputStream r10 = r12.getInputStream()     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            r3 = 1024(0x400, float:1.435E-42)
            byte[] r3 = new byte[r3]     // Catch:{ Throwable -> 0x0138, all -> 0x0132 }
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x0138, all -> 0x0132 }
            int r5 = r10.available()     // Catch:{ Throwable -> 0x0138, all -> 0x0132 }
            r4.<init>(r5)     // Catch:{ Throwable -> 0x0138, all -> 0x0132 }
        L_0x00e0:
            int r0 = r10.read(r3)     // Catch:{ Throwable -> 0x012c, all -> 0x0125 }
            r5 = -1
            if (r0 == r5) goto L_0x00eb
            r4.write(r3, r1, r0)     // Catch:{ Throwable -> 0x012c, all -> 0x0125 }
            goto L_0x00e0
        L_0x00eb:
            byte[] r0 = r4.toByteArray()     // Catch:{ Throwable -> 0x012c, all -> 0x0125 }
            if (r0 == 0) goto L_0x0116
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x012c, all -> 0x0125 }
            java.lang.String r5 = "Log upload response: "
            r3.<init>(r5)     // Catch:{ Throwable -> 0x012c, all -> 0x0125 }
            java.lang.String r5 = new java.lang.String     // Catch:{ Throwable -> 0x012c, all -> 0x0125 }
            r5.<init>(r0)     // Catch:{ Throwable -> 0x012c, all -> 0x0125 }
            r3.append(r5)     // Catch:{ Throwable -> 0x012c, all -> 0x0125 }
            java.lang.String r0 = r3.toString()     // Catch:{ Throwable -> 0x012c, all -> 0x0125 }
            com.uc.crashsdk.a.a.b(r0)     // Catch:{ Throwable -> 0x012c, all -> 0x0125 }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r11)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r10)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r4)
            if (r12 == 0) goto L_0x0115
            r12.disconnect()     // Catch:{ Throwable -> 0x0115 }
        L_0x0115:
            return r2
        L_0x0116:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r11)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r10)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r4)
            if (r12 == 0) goto L_0x0124
            r12.disconnect()     // Catch:{ Throwable -> 0x0124 }
        L_0x0124:
            return r1
        L_0x0125:
            r0 = move-exception
            r9 = r11
            r11 = r10
            r10 = r0
            r0 = r9
            goto L_0x016c
        L_0x012c:
            r0 = move-exception
            r9 = r11
            r11 = r10
            r10 = r0
            r0 = r9
            goto L_0x0159
        L_0x0132:
            r1 = move-exception
            r4 = r0
            r0 = r11
            r11 = r10
            r10 = r1
            goto L_0x016c
        L_0x0138:
            r2 = move-exception
            r4 = r0
            r0 = r11
            r11 = r10
            r10 = r2
            goto L_0x0159
        L_0x013e:
            r10 = move-exception
            r4 = r0
            r0 = r11
            r11 = r4
            goto L_0x016c
        L_0x0143:
            r10 = move-exception
            r4 = r0
            r0 = r11
            r11 = r4
            goto L_0x0159
        L_0x0148:
            r10 = move-exception
            r11 = r0
            r4 = r11
            goto L_0x016c
        L_0x014c:
            r10 = move-exception
            r11 = r0
            r4 = r11
            goto L_0x0159
        L_0x0150:
            r10 = move-exception
            r11 = r0
            r12 = r11
            r4 = r12
            goto L_0x016c
        L_0x0155:
            r10 = move-exception
            r11 = r0
            r12 = r11
            r4 = r12
        L_0x0159:
            com.uc.crashsdk.a.g.b((java.lang.Throwable) r10)     // Catch:{ all -> 0x016b }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r0)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r11)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r4)
            if (r12 == 0) goto L_0x016a
            r12.disconnect()     // Catch:{ Throwable -> 0x016a }
        L_0x016a:
            return r1
        L_0x016b:
            r10 = move-exception
        L_0x016c:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r0)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r11)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r4)
            if (r12 == 0) goto L_0x017a
            r12.disconnect()     // Catch:{ Throwable -> 0x017a }
        L_0x017a:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.a.c.a(byte[], java.lang.String, java.lang.String):boolean");
    }
}
