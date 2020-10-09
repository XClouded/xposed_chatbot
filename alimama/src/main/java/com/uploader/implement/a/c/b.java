package com.uploader.implement.a.c;

import android.text.TextUtils;
import android.util.Pair;
import com.alipay.auth.mobile.common.AlipayAuthConstant;
import com.taobao.tao.image.Logger;
import com.uploader.export.IUploaderTask;
import com.uploader.implement.c.a;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

/* compiled from: ProtocolUtils */
public class b {
    public static Pair<a, com.uploader.implement.a.a.b> a(IUploaderTask iUploaderTask) {
        if (iUploaderTask != null) {
            try {
                if (!TextUtils.isEmpty(iUploaderTask.getFilePath())) {
                    if (!TextUtils.isEmpty(iUploaderTask.getBizType())) {
                        com.uploader.implement.a.a.b bVar = new com.uploader.implement.a.a.b();
                        bVar.a = iUploaderTask.getFilePath();
                        bVar.f = iUploaderTask.getBizType();
                        bVar.i = iUploaderTask.getMetaInfo();
                        bVar.c = iUploaderTask.getFileType();
                        File file = new File(bVar.a);
                        if (!file.exists()) {
                            return new Pair<>(new a(AlipayAuthConstant.LoginResult.SUCCESS, "3", "!file.exists()", false), (Object) null);
                        }
                        if (file.length() == 0) {
                            return new Pair<>(new a(AlipayAuthConstant.LoginResult.SUCCESS, "9", "file.length() == 0", false), (Object) null);
                        }
                        bVar.b = file;
                        bVar.d = file.getName();
                        Pair<String, byte[]> a = a(file);
                        bVar.h = (String) a.first;
                        bVar.l = (byte[]) a.second;
                        bVar.e = UUID.randomUUID().toString().replaceAll("-", "");
                        bVar.g = file.length();
                        bVar.k = file.lastModified();
                        return new Pair<>((Object) null, bVar);
                    }
                }
            } catch (Exception e) {
                if (com.uploader.implement.a.a(16)) {
                    com.uploader.implement.a.a(16, "ProtocolUtils", "createFileDescription", e);
                }
                return new Pair<>(new a(AlipayAuthConstant.LoginResult.SUCCESS, "4", e.toString(), false), (Object) null);
            }
        }
        return new Pair<>(new a(AlipayAuthConstant.LoginResult.SUCCESS, "4", "task getFilePath == null || getBizType == null", false), (Object) null);
    }

    public static String a(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return URLDecoder.decode(str, "utf-8");
            } catch (UnsupportedEncodingException unused) {
            }
        }
        return str;
    }

    public static String b(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return URLEncoder.encode(str, "utf-8");
            } catch (UnsupportedEncodingException unused) {
            }
        }
        return str;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: byte[]} */
    /* JADX WARNING: type inference failed for: r3v0, types: [java.io.FileInputStream] */
    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: type inference failed for: r3v3 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x005f A[SYNTHETIC, Splitter:B:34:0x005f] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.util.Pair<java.lang.String, byte[]> a(java.io.File r7) throws java.lang.Exception {
        /*
            r0 = 204800(0x32000, float:2.86986E-40)
            java.nio.ByteBuffer r0 = java.nio.ByteBuffer.allocate(r0)     // Catch:{ OutOfMemoryError -> 0x0008 }
            goto L_0x000e
        L_0x0008:
            r0 = 131072(0x20000, float:1.83671E-40)
            java.nio.ByteBuffer r0 = java.nio.ByteBuffer.allocate(r0)
        L_0x000e:
            r1 = 0
            java.lang.String r2 = "MD5"
            java.security.MessageDigest r2 = java.security.MessageDigest.getInstance(r2)     // Catch:{ NoSuchAlgorithmException -> 0x005b, Exception -> 0x0059 }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ NoSuchAlgorithmException -> 0x005b, Exception -> 0x0059 }
            r3.<init>(r7)     // Catch:{ NoSuchAlgorithmException -> 0x005b, Exception -> 0x0059 }
            r7 = 0
        L_0x001b:
            java.nio.channels.FileChannel r4 = r3.getChannel()     // Catch:{ NoSuchAlgorithmException -> 0x0053, Exception -> 0x0050, all -> 0x004e }
            int r4 = r4.read(r0)     // Catch:{ NoSuchAlgorithmException -> 0x0053, Exception -> 0x0050, all -> 0x004e }
            if (r4 <= 0) goto L_0x0036
            byte[] r5 = r0.array()     // Catch:{ NoSuchAlgorithmException -> 0x0053, Exception -> 0x0050, all -> 0x004e }
            int r6 = r0.arrayOffset()     // Catch:{ NoSuchAlgorithmException -> 0x0053, Exception -> 0x0050, all -> 0x004e }
            r2.update(r5, r6, r4)     // Catch:{ NoSuchAlgorithmException -> 0x0053, Exception -> 0x0050, all -> 0x004e }
            r0.clear()     // Catch:{ NoSuchAlgorithmException -> 0x0053, Exception -> 0x0050, all -> 0x004e }
            int r7 = r7 + 1
            goto L_0x001b
        L_0x0036:
            android.util.Pair r4 = new android.util.Pair     // Catch:{ NoSuchAlgorithmException -> 0x0053, Exception -> 0x0050, all -> 0x004e }
            byte[] r2 = r2.digest()     // Catch:{ NoSuchAlgorithmException -> 0x0053, Exception -> 0x0050, all -> 0x004e }
            java.lang.String r2 = a((byte[]) r2)     // Catch:{ NoSuchAlgorithmException -> 0x0053, Exception -> 0x0050, all -> 0x004e }
            r5 = 1
            if (r7 != r5) goto L_0x0047
            byte[] r1 = r0.array()     // Catch:{ NoSuchAlgorithmException -> 0x0053, Exception -> 0x0050, all -> 0x004e }
        L_0x0047:
            r4.<init>(r2, r1)     // Catch:{ NoSuchAlgorithmException -> 0x0053, Exception -> 0x0050, all -> 0x004e }
            r3.close()     // Catch:{ Exception -> 0x004d }
        L_0x004d:
            return r4
        L_0x004e:
            r7 = move-exception
            goto L_0x005d
        L_0x0050:
            r7 = move-exception
            r1 = r3
            goto L_0x005a
        L_0x0053:
            r7 = move-exception
            r1 = r3
            goto L_0x005c
        L_0x0056:
            r7 = move-exception
            r3 = r1
            goto L_0x005d
        L_0x0059:
            r7 = move-exception
        L_0x005a:
            throw r7     // Catch:{ all -> 0x0056 }
        L_0x005b:
            r7 = move-exception
        L_0x005c:
            throw r7     // Catch:{ all -> 0x0056 }
        L_0x005d:
            if (r3 == 0) goto L_0x0062
            r3.close()     // Catch:{ Exception -> 0x0062 }
        L_0x0062:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uploader.implement.a.c.b.a(java.io.File):android.util.Pair");
    }

    private static String a(byte[] bArr) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', Logger.LEVEL_D, Logger.LEVEL_E, 'F'};
        char[] cArr2 = new char[(bArr.length * 2)];
        int i = 0;
        for (byte b : bArr) {
            int i2 = i + 1;
            cArr2[i] = cArr[(b >>> 4) & 15];
            i = i2 + 1;
            cArr2[i2] = cArr[b & 15];
        }
        return new String(cArr2);
    }
}
