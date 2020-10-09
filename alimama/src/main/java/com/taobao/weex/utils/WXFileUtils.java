package com.taobao.weex.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class WXFileUtils {
    public static String loadFileOrAsset(String str, Context context) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        File file = new File(str);
        if (!file.exists()) {
            return loadAsset(str, context);
        }
        try {
            return readStreamToString(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String loadAsset(String str, Context context) {
        if (context == null || TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return readStreamToString(context.getAssets().open(str));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0055 A[SYNTHETIC, Splitter:B:29:0x0055] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0061 A[SYNTHETIC, Splitter:B:34:0x0061] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0070 A[SYNTHETIC, Splitter:B:39:0x0070] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x007c A[SYNTHETIC, Splitter:B:44:0x007c] */
    /* JADX WARNING: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readStreamToString(java.io.InputStream r5) {
        /*
            r0 = 0
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x004a }
            int r2 = r5.available()     // Catch:{ IOException -> 0x004a }
            int r2 = r2 + 10
            r1.<init>(r2)     // Catch:{ IOException -> 0x004a }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x004a }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x004a }
            r3.<init>(r5)     // Catch:{ IOException -> 0x004a }
            r2.<init>(r3)     // Catch:{ IOException -> 0x004a }
            r0 = 4096(0x1000, float:5.74E-42)
            char[] r0 = new char[r0]     // Catch:{ IOException -> 0x0044, all -> 0x0040 }
        L_0x001a:
            int r3 = r2.read(r0)     // Catch:{ IOException -> 0x0044, all -> 0x0040 }
            if (r3 <= 0) goto L_0x0025
            r4 = 0
            r1.append(r0, r4, r3)     // Catch:{ IOException -> 0x0044, all -> 0x0040 }
            goto L_0x001a
        L_0x0025:
            java.lang.String r0 = r1.toString()     // Catch:{ IOException -> 0x0044, all -> 0x0040 }
            r2.close()     // Catch:{ IOException -> 0x002d }
            goto L_0x0033
        L_0x002d:
            r1 = move-exception
            java.lang.String r2 = "WXFileUtils loadAsset: "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r2, (java.lang.Throwable) r1)
        L_0x0033:
            if (r5 == 0) goto L_0x003f
            r5.close()     // Catch:{ IOException -> 0x0039 }
            goto L_0x003f
        L_0x0039:
            r5 = move-exception
            java.lang.String r1 = "WXFileUtils loadAsset: "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.Throwable) r5)
        L_0x003f:
            return r0
        L_0x0040:
            r0 = move-exception
            r1 = r0
            r0 = r2
            goto L_0x006e
        L_0x0044:
            r0 = move-exception
            r1 = r0
            r0 = r2
            goto L_0x004b
        L_0x0048:
            r1 = move-exception
            goto L_0x006e
        L_0x004a:
            r1 = move-exception
        L_0x004b:
            r1.printStackTrace()     // Catch:{ all -> 0x0048 }
            java.lang.String r2 = ""
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r2, (java.lang.Throwable) r1)     // Catch:{ all -> 0x0048 }
            if (r0 == 0) goto L_0x005f
            r0.close()     // Catch:{ IOException -> 0x0059 }
            goto L_0x005f
        L_0x0059:
            r0 = move-exception
            java.lang.String r1 = "WXFileUtils loadAsset: "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.Throwable) r0)
        L_0x005f:
            if (r5 == 0) goto L_0x006b
            r5.close()     // Catch:{ IOException -> 0x0065 }
            goto L_0x006b
        L_0x0065:
            r5 = move-exception
            java.lang.String r0 = "WXFileUtils loadAsset: "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.Throwable) r5)
        L_0x006b:
            java.lang.String r5 = ""
            return r5
        L_0x006e:
            if (r0 == 0) goto L_0x007a
            r0.close()     // Catch:{ IOException -> 0x0074 }
            goto L_0x007a
        L_0x0074:
            r0 = move-exception
            java.lang.String r2 = "WXFileUtils loadAsset: "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r2, (java.lang.Throwable) r0)
        L_0x007a:
            if (r5 == 0) goto L_0x0086
            r5.close()     // Catch:{ IOException -> 0x0080 }
            goto L_0x0086
        L_0x0080:
            r5 = move-exception
            java.lang.String r0 = "WXFileUtils loadAsset: "
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.Throwable) r5)
        L_0x0086:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXFileUtils.readStreamToString(java.io.InputStream):java.lang.String");
    }

    public static byte[] readBytesFromAssets(String str, Context context) {
        if (context == null || TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            InputStream open = context.getAssets().open(str);
            byte[] bArr = new byte[4096];
            int read = open.read(bArr);
            byte[] bArr2 = new byte[read];
            System.arraycopy(bArr, 0, bArr2, 0, read);
            return bArr2;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0042 A[SYNTHETIC, Splitter:B:24:0x0042] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x004d A[SYNTHETIC, Splitter:B:30:0x004d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean saveFile(java.lang.String r2, byte[] r3, android.content.Context r4) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            r1 = 0
            if (r0 != 0) goto L_0x0056
            if (r3 == 0) goto L_0x0056
            if (r4 != 0) goto L_0x000c
            goto L_0x0056
        L_0x000c:
            r4 = 0
            java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0027 }
            r0.<init>(r2)     // Catch:{ Exception -> 0x0027 }
            r0.write(r3)     // Catch:{ Exception -> 0x0022, all -> 0x001f }
            r2 = 1
            r0.close()     // Catch:{ IOException -> 0x001a }
            goto L_0x001e
        L_0x001a:
            r3 = move-exception
            r3.printStackTrace()
        L_0x001e:
            return r2
        L_0x001f:
            r2 = move-exception
            r4 = r0
            goto L_0x004b
        L_0x0022:
            r2 = move-exception
            r4 = r0
            goto L_0x0028
        L_0x0025:
            r2 = move-exception
            goto L_0x004b
        L_0x0027:
            r2 = move-exception
        L_0x0028:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0025 }
            r3.<init>()     // Catch:{ all -> 0x0025 }
            java.lang.String r0 = "WXFileUtils saveFile: "
            r3.append(r0)     // Catch:{ all -> 0x0025 }
            java.lang.String r2 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r2)     // Catch:{ all -> 0x0025 }
            r3.append(r2)     // Catch:{ all -> 0x0025 }
            java.lang.String r2 = r3.toString()     // Catch:{ all -> 0x0025 }
            com.taobao.weex.utils.WXLogUtils.e(r2)     // Catch:{ all -> 0x0025 }
            if (r4 == 0) goto L_0x004a
            r4.close()     // Catch:{ IOException -> 0x0046 }
            goto L_0x004a
        L_0x0046:
            r2 = move-exception
            r2.printStackTrace()
        L_0x004a:
            return r1
        L_0x004b:
            if (r4 == 0) goto L_0x0055
            r4.close()     // Catch:{ IOException -> 0x0051 }
            goto L_0x0055
        L_0x0051:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0055:
            throw r2
        L_0x0056:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXFileUtils.saveFile(java.lang.String, byte[], android.content.Context):boolean");
    }

    public static String md5(String str) {
        if (str == null) {
            return "";
        }
        try {
            return md5(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException unused) {
            return "";
        }
    }

    public static String md5(byte[] bArr) {
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            instance.update(bArr);
            return new BigInteger(1, instance.digest()).toString(16);
        } catch (NoSuchAlgorithmException unused) {
            return "";
        }
    }

    public static String base64Md5(String str) {
        if (str == null) {
            return "";
        }
        try {
            return base64Md5(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException unused) {
            return "";
        }
    }

    public static String base64Md5(byte[] bArr) {
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            instance.update(bArr);
            return Base64.encodeToString(instance.digest(), 2);
        } catch (NoSuchAlgorithmException unused) {
            return "";
        }
    }

    public static void extractSo(String str, String str2) throws IOException {
        ZipFile zipFile = new ZipFile(str);
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(str)));
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                zipInputStream.closeEntry();
                return;
            } else if (!nextEntry.isDirectory()) {
                String name = nextEntry.getName();
                if (name.contains("lib/" + WXSoInstallMgrSdk._cpuType() + "/") && (nextEntry.getName().contains("weex") || nextEntry.getName().equals("libJavaScriptCore.so"))) {
                    String[] split = nextEntry.getName().split("/");
                    String str3 = split[split.length - 1];
                    InputStream inputStream = zipFile.getInputStream(nextEntry);
                    byte[] bArr = new byte[1024];
                    File file = new File(str2 + "/" + str3);
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while (inputStream.read(bArr) != -1) {
                        fileOutputStream.write(bArr);
                    }
                    fileOutputStream.close();
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x005d A[SYNTHETIC, Splitter:B:18:0x005d] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0067 A[SYNTHETIC, Splitter:B:23:0x0067] */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyFile(java.io.File r5, java.io.File r6) {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0027 }
            r1.<init>(r5)     // Catch:{ Exception -> 0x0027 }
            r2 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r2]     // Catch:{ Exception -> 0x0023 }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0023 }
            r3.<init>(r6)     // Catch:{ Exception -> 0x0023 }
        L_0x000f:
            int r0 = r1.read(r2)     // Catch:{ Exception -> 0x0021 }
            r4 = -1
            if (r0 == r4) goto L_0x001a
            r3.write(r2)     // Catch:{ Exception -> 0x0021 }
            goto L_0x000f
        L_0x001a:
            r1.close()     // Catch:{ Exception -> 0x0021 }
            r3.close()     // Catch:{ Exception -> 0x0021 }
            goto L_0x006f
        L_0x0021:
            r0 = move-exception
            goto L_0x002b
        L_0x0023:
            r2 = move-exception
            r3 = r0
            r0 = r2
            goto L_0x002b
        L_0x0027:
            r1 = move-exception
            r3 = r0
            r0 = r1
            r1 = r3
        L_0x002b:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = "copyFile "
            r2.append(r4)
            java.lang.String r0 = r0.getMessage()
            r2.append(r0)
            java.lang.String r0 = ": "
            r2.append(r0)
            java.lang.String r5 = r5.getAbsolutePath()
            r2.append(r5)
            java.lang.String r5 = ": "
            r2.append(r5)
            java.lang.String r5 = r6.getAbsolutePath()
            r2.append(r5)
            java.lang.String r5 = r2.toString()
            com.taobao.weex.utils.WXLogUtils.e(r5)
            if (r1 == 0) goto L_0x0065
            r1.close()     // Catch:{ IOException -> 0x0061 }
            goto L_0x0065
        L_0x0061:
            r5 = move-exception
            r5.printStackTrace()
        L_0x0065:
            if (r3 == 0) goto L_0x006f
            r3.close()     // Catch:{ IOException -> 0x006b }
            goto L_0x006f
        L_0x006b:
            r5 = move-exception
            r5.printStackTrace()
        L_0x006f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXFileUtils.copyFile(java.io.File, java.io.File):void");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v6, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v9, resolved type: java.io.FileInputStream} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyFileWithException(java.io.File r3, java.io.File r4) throws java.lang.Exception {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ Exception -> 0x002e, all -> 0x002b }
            r1.<init>(r3)     // Catch:{ Exception -> 0x002e, all -> 0x002b }
            r3 = 1024(0x400, float:1.435E-42)
            byte[] r3 = new byte[r3]     // Catch:{ Exception -> 0x0027, all -> 0x0025 }
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0027, all -> 0x0025 }
            r2.<init>(r4)     // Catch:{ Exception -> 0x0027, all -> 0x0025 }
        L_0x000f:
            int r4 = r1.read(r3)     // Catch:{ Exception -> 0x0023, all -> 0x0021 }
            r0 = -1
            if (r4 == r0) goto L_0x001a
            r2.write(r3)     // Catch:{ Exception -> 0x0023, all -> 0x0021 }
            goto L_0x000f
        L_0x001a:
            closeIo(r1)
            closeIo(r2)
            return
        L_0x0021:
            r3 = move-exception
            goto L_0x0033
        L_0x0023:
            r3 = move-exception
            goto L_0x0029
        L_0x0025:
            r3 = move-exception
            goto L_0x0034
        L_0x0027:
            r3 = move-exception
            r2 = r0
        L_0x0029:
            r0 = r1
            goto L_0x0030
        L_0x002b:
            r3 = move-exception
            r1 = r0
            goto L_0x0034
        L_0x002e:
            r3 = move-exception
            r2 = r0
        L_0x0030:
            throw r3     // Catch:{ all -> 0x0031 }
        L_0x0031:
            r3 = move-exception
            r1 = r0
        L_0x0033:
            r0 = r2
        L_0x0034:
            closeIo(r1)
            closeIo(r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXFileUtils.copyFileWithException(java.io.File, java.io.File):void");
    }

    public static void closeIo(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }
}
