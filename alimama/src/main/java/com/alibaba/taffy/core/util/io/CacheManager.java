package com.alibaba.taffy.core.util.io;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.util.Date;

public class CacheManager {
    static String USER_DIR = "user";
    private static final long VALIDITY = 1800000;

    public static void saveToFile(Context context, Object obj, String str) {
        if (!TextUtils.isEmpty(str)) {
            File file = new File(context.getFilesDir(), str);
            if (file.exists() || file.isDirectory()) {
                file.delete();
            }
            saveObj(file, obj);
        }
    }

    public static <T> T getObjFromFile(Context context, Class<T> cls, String str) {
        File filesDir = context.getFilesDir();
        if (str == null) {
            return null;
        }
        File file = new File(filesDir, str);
        if (!file.exists()) {
            return null;
        }
        return getObj(cls, file);
    }

    public static void saveToCache(Context context, Object obj, String str, boolean z) {
        File file;
        File cacheDir = context.getCacheDir();
        if (((double) cacheDir.length()) > 2.0E21d) {
            long time = new Date().getTime();
            for (File file2 : cacheDir.listFiles()) {
                if (file2.lastModified() - time > 1800000) {
                    file2.delete();
                }
            }
        }
        if (z) {
            File file3 = new File(cacheDir, USER_DIR);
            if (!file3.exists()) {
                file3.mkdirs();
            }
            file = new File(file3, str);
        } else {
            file = new File(cacheDir, str);
        }
        if (file.exists() || file.isDirectory()) {
            file.delete();
        }
        saveObj(file, obj);
    }

    public static void saveToCache(Context context, Object obj, String str) {
        saveToCache(context, obj, str, false);
    }

    public static <T> T getObjFromCache(Context context, Class<T> cls, String str) {
        return getObjFromCache(context, cls, str, false);
    }

    public static <T> T getObjFromCache(Context context, Class<T> cls, String str, boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(context.getCacheDir().getAbsolutePath());
        if (z) {
            stringBuffer.append(File.separatorChar);
            stringBuffer.append(USER_DIR);
            stringBuffer.append(File.separatorChar);
            stringBuffer.append(str);
        } else {
            stringBuffer.append(File.separatorChar);
            stringBuffer.append(str);
        }
        return getObj(cls, new File(context.getCacheDir(), str));
    }

    public static void clearCache(Context context) {
        FileUtil.deleteDir(context.getCacheDir());
    }

    public static void clearFiles(Context context) {
        FileUtil.deleteDir(context.getFilesDir());
    }

    public static void clearUersCache(Context context) {
        FileUtil.deleteDir(new File(context.getCacheDir(), USER_DIR));
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0029 A[SYNTHETIC, Splitter:B:18:0x0029] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0033 A[SYNTHETIC, Splitter:B:24:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x003e A[SYNTHETIC, Splitter:B:29:0x003e] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:15:0x0024=Splitter:B:15:0x0024, B:21:0x002e=Splitter:B:21:0x002e} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void saveObj(java.io.File r2, java.lang.Object r3) {
        /*
            r0 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x002d, IOException -> 0x0023 }
            r1.<init>(r2)     // Catch:{ FileNotFoundException -> 0x002d, IOException -> 0x0023 }
            java.io.ObjectOutputStream r2 = new java.io.ObjectOutputStream     // Catch:{ FileNotFoundException -> 0x001d, IOException -> 0x001a, all -> 0x0018 }
            r2.<init>(r1)     // Catch:{ FileNotFoundException -> 0x001d, IOException -> 0x001a, all -> 0x0018 }
            r2.writeObject(r3)     // Catch:{ FileNotFoundException -> 0x001d, IOException -> 0x001a, all -> 0x0018 }
            r2.flush()     // Catch:{ FileNotFoundException -> 0x001d, IOException -> 0x001a, all -> 0x0018 }
            r2.close()     // Catch:{ FileNotFoundException -> 0x001d, IOException -> 0x001a, all -> 0x0018 }
            r1.close()     // Catch:{ IOException -> 0x0037 }
            goto L_0x003b
        L_0x0018:
            r2 = move-exception
            goto L_0x003c
        L_0x001a:
            r2 = move-exception
            r0 = r1
            goto L_0x0024
        L_0x001d:
            r2 = move-exception
            r0 = r1
            goto L_0x002e
        L_0x0020:
            r2 = move-exception
            r1 = r0
            goto L_0x003c
        L_0x0023:
            r2 = move-exception
        L_0x0024:
            r2.printStackTrace()     // Catch:{ all -> 0x0020 }
            if (r0 == 0) goto L_0x003b
            r0.close()     // Catch:{ IOException -> 0x0037 }
            goto L_0x003b
        L_0x002d:
            r2 = move-exception
        L_0x002e:
            r2.printStackTrace()     // Catch:{ all -> 0x0020 }
            if (r0 == 0) goto L_0x003b
            r0.close()     // Catch:{ IOException -> 0x0037 }
            goto L_0x003b
        L_0x0037:
            r2 = move-exception
            r2.printStackTrace()
        L_0x003b:
            return
        L_0x003c:
            if (r1 == 0) goto L_0x0046
            r1.close()     // Catch:{ IOException -> 0x0042 }
            goto L_0x0046
        L_0x0042:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0046:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.taffy.core.util.io.CacheManager.saveObj(java.io.File, java.lang.Object):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x002f A[SYNTHETIC, Splitter:B:21:0x002f] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x003b A[SYNTHETIC, Splitter:B:27:0x003b] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static <T> T getObj(java.lang.Class<T> r1, java.io.File r2) {
        /*
            r1 = 0
            if (r2 == 0) goto L_0x0044
            boolean r0 = r2.exists()
            if (r0 != 0) goto L_0x000a
            goto L_0x0044
        L_0x000a:
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0028, all -> 0x0024 }
            r0.<init>(r2)     // Catch:{ Exception -> 0x0028, all -> 0x0024 }
            java.io.ObjectInputStream r2 = new java.io.ObjectInputStream     // Catch:{ Exception -> 0x0022 }
            r2.<init>(r0)     // Catch:{ Exception -> 0x0022 }
            java.lang.Object r2 = r2.readObject()     // Catch:{ Exception -> 0x0022 }
            r0.close()     // Catch:{ IOException -> 0x001c }
            goto L_0x0020
        L_0x001c:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0020:
            r1 = r2
            goto L_0x0037
        L_0x0022:
            r2 = move-exception
            goto L_0x002a
        L_0x0024:
            r2 = move-exception
            r0 = r1
            r1 = r2
            goto L_0x0039
        L_0x0028:
            r2 = move-exception
            r0 = r1
        L_0x002a:
            r2.printStackTrace()     // Catch:{ all -> 0x0038 }
            if (r0 == 0) goto L_0x0037
            r0.close()     // Catch:{ IOException -> 0x0033 }
            goto L_0x0037
        L_0x0033:
            r2 = move-exception
            r2.printStackTrace()
        L_0x0037:
            return r1
        L_0x0038:
            r1 = move-exception
        L_0x0039:
            if (r0 == 0) goto L_0x0043
            r0.close()     // Catch:{ IOException -> 0x003f }
            goto L_0x0043
        L_0x003f:
            r2 = move-exception
            r2.printStackTrace()
        L_0x0043:
            throw r1
        L_0x0044:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.taffy.core.util.io.CacheManager.getObj(java.lang.Class, java.io.File):java.lang.Object");
    }

    public static <T> T getObj(String str) {
        return getObj(new File(str));
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x002d A[SYNTHETIC, Splitter:B:20:0x002d] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x003a A[SYNTHETIC, Splitter:B:27:0x003a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> T getObj(java.io.File r2) {
        /*
            r0 = 0
            if (r2 == 0) goto L_0x0043
            boolean r1 = r2.exists()
            if (r1 != 0) goto L_0x000a
            goto L_0x0043
        L_0x000a:
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0026, all -> 0x0023 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x0026, all -> 0x0023 }
            java.io.ObjectInputStream r2 = new java.io.ObjectInputStream     // Catch:{ Exception -> 0x0021 }
            r2.<init>(r1)     // Catch:{ Exception -> 0x0021 }
            java.lang.Object r2 = r2.readObject()     // Catch:{ Exception -> 0x0021 }
            r1.close()     // Catch:{ IOException -> 0x001c }
            goto L_0x0036
        L_0x001c:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0036
        L_0x0021:
            r2 = move-exception
            goto L_0x0028
        L_0x0023:
            r2 = move-exception
            r1 = r0
            goto L_0x0038
        L_0x0026:
            r2 = move-exception
            r1 = r0
        L_0x0028:
            r2.printStackTrace()     // Catch:{ all -> 0x0037 }
            if (r1 == 0) goto L_0x0035
            r1.close()     // Catch:{ IOException -> 0x0031 }
            goto L_0x0035
        L_0x0031:
            r2 = move-exception
            r2.printStackTrace()
        L_0x0035:
            r2 = r0
        L_0x0036:
            return r2
        L_0x0037:
            r2 = move-exception
        L_0x0038:
            if (r1 == 0) goto L_0x0042
            r1.close()     // Catch:{ IOException -> 0x003e }
            goto L_0x0042
        L_0x003e:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0042:
            throw r2
        L_0x0043:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.taffy.core.util.io.CacheManager.getObj(java.io.File):java.lang.Object");
    }
}
