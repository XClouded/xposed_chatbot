package com.uc.webview.export.cyclone;

import android.content.Context;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.zip.ZipFile;

@Constant
/* compiled from: U4Source */
public class UCCyclone {
    static final String LOG_TAG = "cyclone";
    static final boolean RELEASE_BUILD = false;
    private static final String TEMP_DEC_DIR_PREFIX = "temp_dec_";
    public static String dataFolder = "cyclone";
    public static boolean enableDebugLog = true;
    public static ValueCallback<String> loadLibraryCallback = null;
    public static String logExtraTag = "cyclone.";
    @Constant
    public static ConcurrentLinkedQueue<File> sInusedFiles = null;
    public static final UCClassLoaderOnCreate sUcClassLoaderOnCreate = new UCClassLoaderOnCreate();
    public static String serverZipTag = "7z";
    public static ValueCallback<Pair<String, HashMap<String, String>>> statCallback;

    /* compiled from: U4Source */
    public static class DecFileOrign {
        public static String DecFileOrignFlag = "_dec_ori_";
        public static int Other = 1;
        public static int Sdcard = 2;
        public static int Sdcard_Share_Core = 3;
        public static int Update = 1;
    }

    /* compiled from: U4Source */
    public enum MessageDigestType {
        MD5,
        SHA1,
        SHA256
    }

    public static File expectCreateDirFile(File file) {
        int i = 3;
        while (!file.exists() && !file.mkdirs()) {
            int i2 = i - 1;
            if (i > 0) {
                i = i2;
            } else {
                throw new UCKnownException(1003, String.format("Directory [%s] mkdir failed.", new Object[]{file.getAbsolutePath()}));
            }
        }
        return file;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static Object invoke(Class<?> cls, String str, Class[] clsArr, Object[] objArr) throws Exception {
        return invoke((Object) null, cls, str, clsArr, objArr);
    }

    @Constant
    public static Object invoke(Object obj, Class<?> cls, String str, Class[] clsArr, Object[] objArr) throws Exception {
        Method method;
        try {
            method = cls.getDeclaredMethod(str, clsArr);
        } catch (Throwable unused) {
            method = cls.getMethod(str, clsArr);
        }
        return invoke(obj, cls, method, objArr);
    }

    @Constant
    public static Object invoke(Object obj, Class<?> cls, Method method, Object[] objArr) throws Exception {
        method.setAccessible(true);
        try {
            return method.invoke(obj, objArr);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof Exception) {
                throw ((Exception) targetException);
            } else if (targetException instanceof Error) {
                throw ((Error) targetException);
            } else {
                throw new RuntimeException(targetException);
            }
        }
    }

    @Constant
    public static void stat(String str, HashMap<String, String> hashMap) {
        ValueCallback<Pair<String, HashMap<String, String>>> valueCallback = statCallback;
        if (valueCallback != null) {
            try {
                valueCallback.onReceiveValue(new Pair(str, hashMap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Constant
    static void getFile(byte[][] bArr, File file) throws IOException {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            expectCreateDirFile(file.getParentFile());
            BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(file));
            try {
                for (byte[] write : bArr) {
                    bufferedOutputStream2.write(write);
                }
                close(bufferedOutputStream2);
            } catch (Throwable th) {
                th = th;
                bufferedOutputStream = bufferedOutputStream2;
                close(bufferedOutputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            close(bufferedOutputStream);
            throw th;
        }
    }

    public static long getFolderSize(File file) {
        return getFolderSize(file, -1);
    }

    public static long getFolderSize(File file, long j) {
        Stack stack = new Stack();
        if (file.exists()) {
            stack.push(file);
        }
        long j2 = 0;
        while (!stack.empty()) {
            File[] listFiles = ((File) stack.pop()).listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    try {
                        String name = file2.getName();
                        if (!name.equals(".") && !name.equals("..") && !name.equals("./")) {
                            if (!name.equals("../")) {
                                if (file2.isDirectory() && file2.exists()) {
                                    stack.push(file2);
                                } else if (file2.exists()) {
                                    j2 += file2.length();
                                    if (j >= 0 && j2 > j) {
                                        return j2;
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                    } catch (Throwable unused) {
                    }
                }
                continue;
            }
        }
        return j2;
    }

    @Constant
    public static File getDataFolder(Context context) throws UCKnownException {
        UCLogger uCLogger = null;
        try {
            if (dataFolder == null) {
                dataFolder = LOG_TAG;
            }
            File dir = context.getDir(dataFolder, 0);
            if (sInusedFiles == null) {
                synchronized (UCCyclone.class) {
                    if (sInusedFiles == null) {
                        sInusedFiles = new ConcurrentLinkedQueue<>();
                    }
                }
            }
            if (enableDebugLog) {
                uCLogger = UCLogger.create(UploadQueueMgr.MSGTYPE_INTERVAL, LOG_TAG);
            }
            if (uCLogger != null) {
                uCLogger.print("getDataFolder: ok.", new Throwable[0]);
            }
            return dir;
        } catch (Throwable th) {
            if (enableDebugLog) {
                uCLogger = UCLogger.create("e", LOG_TAG);
            }
            if (uCLogger != null) {
                uCLogger.print("getDataFolder: from dir app_* Exception:", th);
            }
            throw new UCKnownException(1003, th);
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x001a */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x002c A[Catch:{ Throwable -> 0x003a }] */
    /* JADX WARNING: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void deleteUnusedFiles(android.content.Context r5) {
        /*
            r0 = 0
            java.io.File r1 = getDataFolder(r5)     // Catch:{ Throwable -> 0x001a }
            java.util.concurrent.ConcurrentLinkedQueue<java.io.File> r2 = sInusedFiles     // Catch:{ Throwable -> 0x001a }
            if (r2 == 0) goto L_0x0015
            java.util.concurrent.ConcurrentLinkedQueue<java.io.File> r2 = sInusedFiles     // Catch:{ Throwable -> 0x001a }
            r3 = 5
            java.io.File[] r3 = new java.io.File[r3]     // Catch:{ Throwable -> 0x001a }
            java.lang.Object[] r2 = r2.toArray(r3)     // Catch:{ Throwable -> 0x001a }
            java.io.File[] r2 = (java.io.File[]) r2     // Catch:{ Throwable -> 0x001a }
            goto L_0x0016
        L_0x0015:
            r2 = r0
        L_0x0016:
            r3 = 1
            recursiveDelete(r1, r3, r2)     // Catch:{ Throwable -> 0x001a }
        L_0x001a:
            java.io.File r5 = r5.getCacheDir()     // Catch:{ Throwable -> 0x003a }
            com.uc.webview.export.cyclone.UCCyclone$1 r1 = new com.uc.webview.export.cyclone.UCCyclone$1     // Catch:{ Throwable -> 0x003a }
            r1.<init>()     // Catch:{ Throwable -> 0x003a }
            java.io.File[] r5 = r5.listFiles(r1)     // Catch:{ Throwable -> 0x003a }
            if (r5 == 0) goto L_0x0039
            int r1 = r5.length     // Catch:{ Throwable -> 0x003a }
            if (r1 <= 0) goto L_0x0039
            int r1 = r5.length     // Catch:{ Throwable -> 0x003a }
            r2 = 0
            r3 = 0
        L_0x002f:
            if (r3 >= r1) goto L_0x0039
            r4 = r5[r3]     // Catch:{ Throwable -> 0x003a }
            recursiveDelete(r4, r2, r0)     // Catch:{ Throwable -> 0x003a }
            int r3 = r3 + 1
            goto L_0x002f
        L_0x0039:
            return
        L_0x003a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.UCCyclone.deleteUnusedFiles(android.content.Context):void");
    }

    public static File expectFile(File file, String str) throws UCKnownException {
        return expectFile(new File(file, str));
    }

    public static File expectFile(String str) throws UCKnownException {
        return expectFile(new File(str));
    }

    public static File expectFile(File file) throws UCKnownException {
        if (!file.exists()) {
            throw new UCKnownException(1001, String.format("File [%s] not exists.", new Object[]{file.getAbsolutePath()}));
        } else if (file.canRead()) {
            return file;
        } else {
            throw new UCKnownException(1016, String.format("File [%s] cannot read.", new Object[]{file.getAbsolutePath()}));
        }
    }

    @Constant
    public static String getDecompressFileHash(File file) {
        return getDecompressSourceHash(file.getAbsolutePath(), file.length(), file.lastModified(), false);
    }

    public static String getDecompressSourceHash(String str, long j, long j2, boolean z) {
        StringBuilder sb = new StringBuilder();
        if (!z) {
            str = getSourceHash(str);
        }
        sb.append(str);
        sb.append("_");
        sb.append(getSourceHash(j, j2));
        return sb.toString();
    }

    public static String getSourceHash(String str) {
        return String.valueOf(str.hashCode()).replace('-', '_');
    }

    public static String getSourceHash(long j, long j2) {
        return j + "_" + j2;
    }

    public static synchronized boolean decompressIfNeeded(Context context, String str, File file, File file2, FilenameFilter filenameFilter, boolean z) throws UCKnownException {
        boolean decompressIfNeeded;
        synchronized (UCCyclone.class) {
            decompressIfNeeded = decompressIfNeeded(context, str, file.getAbsolutePath(), file.length(), file.lastModified(), file, file2, filenameFilter, z, DecFileOrign.Other);
        }
        return decompressIfNeeded;
    }

    public static synchronized boolean decompressIfNeeded(Context context, boolean z, File file, File file2, FilenameFilter filenameFilter, boolean z2) throws UCKnownException {
        boolean decompressIfNeeded;
        synchronized (UCCyclone.class) {
            decompressIfNeeded = decompressIfNeeded(context, z, file.getAbsolutePath(), file.length(), file.lastModified(), file, file2, filenameFilter, z2, DecFileOrign.Other);
        }
        return decompressIfNeeded;
    }

    public static synchronized boolean decompressIfNeeded(Context context, boolean z, File file, File file2, FilenameFilter filenameFilter, boolean z2, int i) throws UCKnownException {
        boolean decompressIfNeeded;
        synchronized (UCCyclone.class) {
            decompressIfNeeded = decompressIfNeeded(context, z, file.getAbsolutePath(), file.length(), file.lastModified(), file, file2, filenameFilter, z2, i);
        }
        return decompressIfNeeded;
    }

    public static boolean isDecompressFinished(File file) {
        String[] split = file.getName().split("_");
        if (split.length == 2) {
            return isDecompressFinished(file.getParentFile().getName(), Long.valueOf(split[0]).longValue(), Long.valueOf(split[1]).longValue(), file, true);
        }
        return false;
    }

    static boolean isDecompressFinished(String str, long j, long j2, File file, boolean z) {
        return getDecompressStopFlgFile(str, j, j2, file, z).exists() && !getDecompressStartFlgFile(str, j, j2, file, z).exists();
    }

    public static boolean detect7zFromFileName(String str) {
        return str.endsWith(".7z") || str.contains("_7z_") || str.contains("_7z");
    }

    public static boolean detectZipByFileType(String str) {
        try {
            new ZipFile(str);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    static void expectMakeDecompressNotFinished(String str, long j, long j2, File file) throws UCKnownException {
        try {
            File decompressStartFlgFile = getDecompressStartFlgFile(str, j, j2, file, false);
            if (decompressStartFlgFile.exists()) {
                return;
            }
            if (!decompressStartFlgFile.createNewFile()) {
                throw new Exception("createNewFile return false");
            }
        } catch (Throwable th) {
            throw new UCKnownException(1006, th);
        }
    }

    static void decompress(boolean z, Context context, String str, String str2, FilenameFilter filenameFilter) throws UCKnownException {
        decompress(z, context, str, str2, "", filenameFilter);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0089, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
        close(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x015e, code lost:
        if (r10 == null) goto L_0x016b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0160, code lost:
        stat("sdk_decz_s", (java.util.HashMap<java.lang.String, java.lang.String>) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0174, code lost:
        throw new com.uc.webview.export.cyclone.UCKnownException(2002, "No entry exists in zip file. Make sure specify a valid zip file url.");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0175, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0176, code lost:
        close(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0179, code lost:
        throw r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x017a, code lost:
        stat("sdk_dec_e", (java.util.HashMap<java.lang.String, java.lang.String>) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0181, code lost:
        if ((r9 instanceof com.uc.webview.export.cyclone.UCKnownException) != false) goto L_0x0183;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0185, code lost:
        throw ((com.uc.webview.export.cyclone.UCKnownException) r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x018d, code lost:
        throw new com.uc.webview.export.cyclone.UCKnownException(2005, r9);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:3:0x000c, B:27:0x00a3] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:27:0x00a3=Splitter:B:27:0x00a3, B:18:0x0035=Splitter:B:18:0x0035} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void decompress(boolean r9, android.content.Context r10, java.lang.String r11, java.lang.String r12, java.lang.String r13, java.io.FilenameFilter r14) throws com.uc.webview.export.cyclone.UCKnownException {
        /*
            java.lang.String r0 = "sdk_dec"
            r1 = 0
            stat(r0, r1)
            r0 = 1
            r2 = 0
            if (r9 == 0) goto L_0x008c
            java.lang.Class<com.uc.webview.export.cyclone.service.UCUnSevenZip> r9 = com.uc.webview.export.cyclone.service.UCUnSevenZip.class
            com.uc.webview.export.cyclone.service.UCServiceInterface r9 = com.uc.webview.export.cyclone.UCService.initImpl(r9)     // Catch:{ Throwable -> 0x0089 }
            com.uc.webview.export.cyclone.service.UCUnSevenZip r9 = (com.uc.webview.export.cyclone.service.UCUnSevenZip) r9     // Catch:{ Throwable -> 0x0089 }
            if (r9 == 0) goto L_0x007f
            int r10 = r9.deccompress(r10, r11, r12, r13)     // Catch:{ Throwable -> 0x0089 }
            if (r10 == 0) goto L_0x0165
            java.lang.String r9 = r9.failedFilePath()     // Catch:{ Throwable -> 0x0089 }
            r13 = 7
            if (r10 != r13) goto L_0x0034
            if (r9 == 0) goto L_0x0034
            int r14 = r9.length()     // Catch:{ IOException -> 0x0032 }
            if (r14 <= 0) goto L_0x0034
            java.io.File r14 = new java.io.File     // Catch:{ IOException -> 0x0032 }
            r14.<init>(r9)     // Catch:{ IOException -> 0x0032 }
            r14.createNewFile()     // Catch:{ IOException -> 0x0032 }
            goto L_0x0034
        L_0x0032:
            r14 = move-exception
            goto L_0x0035
        L_0x0034:
            r14 = r1
        L_0x0035:
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x0089 }
            r3.<init>(r12)     // Catch:{ Throwable -> 0x0089 }
            long r3 = r3.getFreeSpace()     // Catch:{ Throwable -> 0x0089 }
            r5 = 1024(0x400, double:5.06E-321)
            long r3 = r3 / r5
            java.io.File r7 = new java.io.File     // Catch:{ Throwable -> 0x0089 }
            r7.<init>(r11)     // Catch:{ Throwable -> 0x0089 }
            long r7 = r7.length()     // Catch:{ Throwable -> 0x0089 }
            long r7 = r7 / r5
            java.lang.String r5 = "Error on 7z decoding: %d freeSize: %dKB 7z len: %dKB exception: %s failed file: %s inputFilePath: %s dirPath: %s"
            java.lang.Object[] r13 = new java.lang.Object[r13]     // Catch:{ Throwable -> 0x0089 }
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch:{ Throwable -> 0x0089 }
            r13[r2] = r10     // Catch:{ Throwable -> 0x0089 }
            java.lang.Long r10 = java.lang.Long.valueOf(r3)     // Catch:{ Throwable -> 0x0089 }
            r13[r0] = r10     // Catch:{ Throwable -> 0x0089 }
            r10 = 2
            java.lang.Long r0 = java.lang.Long.valueOf(r7)     // Catch:{ Throwable -> 0x0089 }
            r13[r10] = r0     // Catch:{ Throwable -> 0x0089 }
            r10 = 3
            r13[r10] = r14     // Catch:{ Throwable -> 0x0089 }
            r10 = 4
            r13[r10] = r9     // Catch:{ Throwable -> 0x0089 }
            r9 = 5
            r13[r9] = r11     // Catch:{ Throwable -> 0x0089 }
            r9 = 6
            r13[r9] = r12     // Catch:{ Throwable -> 0x0089 }
            java.lang.String r9 = java.lang.String.format(r5, r13)     // Catch:{ Throwable -> 0x0089 }
            java.lang.String r10 = "cyclone"
            android.util.Log.e(r10, r9)     // Catch:{ Throwable -> 0x0089 }
            com.uc.webview.export.cyclone.UCKnownException r10 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x0089 }
            r11 = 2001(0x7d1, float:2.804E-42)
            r10.<init>((int) r11, (java.lang.String) r9)     // Catch:{ Throwable -> 0x0089 }
            throw r10     // Catch:{ Throwable -> 0x0089 }
        L_0x007f:
            com.uc.webview.export.cyclone.UCKnownException r9 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x0089 }
            r10 = 2015(0x7df, float:2.824E-42)
            java.lang.String r11 = "Error on 7z decoding: no impl found."
            r9.<init>((int) r10, (java.lang.String) r11)     // Catch:{ Throwable -> 0x0089 }
            throw r9     // Catch:{ Throwable -> 0x0089 }
        L_0x0089:
            r9 = move-exception
            goto L_0x017a
        L_0x008c:
            java.lang.String r9 = "sdk_decz"
            stat(r9, r1)     // Catch:{ Throwable -> 0x0089 }
            java.util.zip.ZipInputStream r9 = new java.util.zip.ZipInputStream     // Catch:{ Throwable -> 0x0089 }
            java.io.BufferedInputStream r10 = new java.io.BufferedInputStream     // Catch:{ Throwable -> 0x0089 }
            java.io.FileInputStream r13 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0089 }
            r13.<init>(r11)     // Catch:{ Throwable -> 0x0089 }
            r10.<init>(r13)     // Catch:{ Throwable -> 0x0089 }
            r9.<init>(r10)     // Catch:{ Throwable -> 0x0089 }
            r10 = r1
            r11 = 0
            r13 = 0
        L_0x00a3:
            java.util.zip.ZipEntry r3 = r9.getNextEntry()     // Catch:{ all -> 0x0175 }
            if (r3 == 0) goto L_0x015b
            r4 = 4096(0x1000, float:5.74E-42)
            byte[] r5 = new byte[r4]     // Catch:{ all -> 0x0175 }
            java.lang.String r3 = r3.getName()     // Catch:{ all -> 0x0175 }
            java.lang.String r6 = ".."
            boolean r6 = r3.contains(r6)     // Catch:{ all -> 0x0175 }
            if (r6 != 0) goto L_0x0149
            if (r14 == 0) goto L_0x00ce
            java.io.File r6 = new java.io.File     // Catch:{ all -> 0x0175 }
            r6.<init>(r3)     // Catch:{ all -> 0x0175 }
            java.io.File r7 = r6.getParentFile()     // Catch:{ all -> 0x0175 }
            java.lang.String r6 = r6.getName()     // Catch:{ all -> 0x0175 }
            boolean r6 = r14.accept(r7, r6)     // Catch:{ all -> 0x0175 }
            if (r6 == 0) goto L_0x00a3
        L_0x00ce:
            java.io.File r6 = new java.io.File     // Catch:{ all -> 0x0175 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0175 }
            r7.<init>()     // Catch:{ all -> 0x0175 }
            r7.append(r12)     // Catch:{ all -> 0x0175 }
            java.lang.String r8 = "/"
            r7.append(r8)     // Catch:{ all -> 0x0175 }
            r7.append(r3)     // Catch:{ all -> 0x0175 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0175 }
            r6.<init>(r7)     // Catch:{ all -> 0x0175 }
            java.lang.String r7 = "/"
            boolean r7 = r3.endsWith(r7)     // Catch:{ all -> 0x0175 }
            if (r7 != 0) goto L_0x0144
            java.lang.String r7 = "\\"
            boolean r3 = r3.endsWith(r7)     // Catch:{ all -> 0x0175 }
            if (r3 == 0) goto L_0x00f8
            goto L_0x0144
        L_0x00f8:
            java.io.File r10 = new java.io.File     // Catch:{ all -> 0x0175 }
            java.lang.String r3 = r6.getParent()     // Catch:{ all -> 0x0175 }
            r10.<init>(r3)     // Catch:{ all -> 0x0175 }
            expectCreateDirFile(r10)     // Catch:{ all -> 0x0175 }
            java.io.FileOutputStream r10 = new java.io.FileOutputStream     // Catch:{ all -> 0x0175 }
            r10.<init>(r6)     // Catch:{ all -> 0x0175 }
            java.io.BufferedOutputStream r3 = new java.io.BufferedOutputStream     // Catch:{ all -> 0x0175 }
            r3.<init>(r10, r4)     // Catch:{ all -> 0x0175 }
        L_0x010e:
            int r10 = r9.read(r5, r2, r4)     // Catch:{ all -> 0x0175 }
            r6 = -1
            if (r10 == r6) goto L_0x0128
            r3.write(r5, r2, r10)     // Catch:{ all -> 0x0175 }
            int r11 = r11 + r10
            r10 = 536870912(0x20000000, float:1.0842022E-19)
            if (r11 > r10) goto L_0x011e
            goto L_0x010e
        L_0x011e:
            com.uc.webview.export.cyclone.UCKnownException r10 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x0175 }
            r11 = 2010(0x7da, float:2.817E-42)
            java.lang.String r12 = "Zip contents is too big."
            r10.<init>((int) r11, (java.lang.String) r12)     // Catch:{ all -> 0x0175 }
            throw r10     // Catch:{ all -> 0x0175 }
        L_0x0128:
            r3.flush()     // Catch:{ all -> 0x0175 }
            r3.close()     // Catch:{ all -> 0x0175 }
            r9.closeEntry()     // Catch:{ all -> 0x0175 }
            int r13 = r13 + 1
            r10 = 1024(0x400, float:1.435E-42)
            if (r13 > r10) goto L_0x013a
            r10 = r3
            goto L_0x00a3
        L_0x013a:
            com.uc.webview.export.cyclone.UCKnownException r10 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x0175 }
            r11 = 2011(0x7db, float:2.818E-42)
            java.lang.String r12 = "Too many files to unzip."
            r10.<init>((int) r11, (java.lang.String) r12)     // Catch:{ all -> 0x0175 }
            throw r10     // Catch:{ all -> 0x0175 }
        L_0x0144:
            expectCreateDirFile(r6)     // Catch:{ all -> 0x0175 }
            goto L_0x00a3
        L_0x0149:
            com.uc.webview.export.cyclone.UCKnownException r10 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x0175 }
            r11 = 2012(0x7dc, float:2.82E-42)
            java.lang.String r12 = "Zip entry [%s] not valid."
            java.lang.Object[] r13 = new java.lang.Object[r0]     // Catch:{ all -> 0x0175 }
            r13[r2] = r3     // Catch:{ all -> 0x0175 }
            java.lang.String r12 = java.lang.String.format(r12, r13)     // Catch:{ all -> 0x0175 }
            r10.<init>((int) r11, (java.lang.String) r12)     // Catch:{ all -> 0x0175 }
            throw r10     // Catch:{ all -> 0x0175 }
        L_0x015b:
            close(r9)     // Catch:{ Throwable -> 0x0089 }
            if (r10 == 0) goto L_0x016b
            java.lang.String r9 = "sdk_decz_s"
            stat(r9, r1)     // Catch:{ Throwable -> 0x0089 }
        L_0x0165:
            java.lang.String r9 = "sdk_dec_s"
            stat(r9, r1)
            return
        L_0x016b:
            com.uc.webview.export.cyclone.UCKnownException r9 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x0089 }
            r10 = 2002(0x7d2, float:2.805E-42)
            java.lang.String r11 = "No entry exists in zip file. Make sure specify a valid zip file url."
            r9.<init>((int) r10, (java.lang.String) r11)     // Catch:{ Throwable -> 0x0089 }
            throw r9     // Catch:{ Throwable -> 0x0089 }
        L_0x0175:
            r10 = move-exception
            close(r9)     // Catch:{ Throwable -> 0x0089 }
            throw r10     // Catch:{ Throwable -> 0x0089 }
        L_0x017a:
            java.lang.String r10 = "sdk_dec_e"
            stat(r10, r1)
            boolean r10 = r9 instanceof com.uc.webview.export.cyclone.UCKnownException
            if (r10 == 0) goto L_0x0186
            com.uc.webview.export.cyclone.UCKnownException r9 = (com.uc.webview.export.cyclone.UCKnownException) r9
            throw r9
        L_0x0186:
            com.uc.webview.export.cyclone.UCKnownException r10 = new com.uc.webview.export.cyclone.UCKnownException
            r11 = 2005(0x7d5, float:2.81E-42)
            r10.<init>((int) r11, (java.lang.Throwable) r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.UCCyclone.decompress(boolean, android.content.Context, java.lang.String, java.lang.String, java.lang.String, java.io.FilenameFilter):void");
    }

    public static synchronized boolean decompressIfNeeded(Context context, boolean z, String str, long j, long j2, File file, File file2, FilenameFilter filenameFilter, boolean z2, int i) throws UCKnownException {
        String str2;
        boolean decompressIfNeeded;
        synchronized (UCCyclone.class) {
            if (z) {
                try {
                    str2 = serverZipTag;
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                str2 = "";
            }
            decompressIfNeeded = decompressIfNeeded(context, str2, str, j, j2, file, file2, filenameFilter, z2, i);
        }
        return decompressIfNeeded;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0142, code lost:
        return true;
     */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b4 A[Catch:{ all -> 0x0087 }] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0143 A[SYNTHETIC, Splitter:B:56:0x0143] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized boolean decompressIfNeeded(android.content.Context r13, java.lang.String r14, java.lang.String r15, long r16, long r18, java.io.File r20, java.io.File r21, java.io.FilenameFilter r22, boolean r23, int r24) throws com.uc.webview.export.cyclone.UCKnownException {
        /*
            r0 = r14
            r8 = r21
            java.lang.Class<com.uc.webview.export.cyclone.UCCyclone> r9 = com.uc.webview.export.cyclone.UCCyclone.class
            monitor-enter(r9)
            r7 = 0
            r1 = r15
            r2 = r16
            r4 = r18
            r6 = r21
            boolean r1 = isDecompressFinished(r1, r2, r4, r6, r7)     // Catch:{ all -> 0x0174 }
            r7 = 0
            if (r1 == 0) goto L_0x0017
            monitor-exit(r9)
            return r7
        L_0x0017:
            boolean r1 = r20.exists()     // Catch:{ all -> 0x0174 }
            r10 = 1
            if (r1 == 0) goto L_0x015e
        L_0x001e:
            java.io.File r1 = r13.getCacheDir()     // Catch:{ all -> 0x0174 }
            java.lang.String r2 = r21.getAbsolutePath()     // Catch:{ all -> 0x0174 }
            java.io.File r3 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ all -> 0x0174 }
            java.lang.String r3 = r3.getAbsolutePath()     // Catch:{ all -> 0x0174 }
            boolean r2 = r2.startsWith(r3)     // Catch:{ all -> 0x0174 }
            if (r2 == 0) goto L_0x003b
            java.io.File r1 = new java.io.File     // Catch:{ all -> 0x0174 }
            java.lang.String r2 = ".cache"
            r1.<init>(r8, r2)     // Catch:{ all -> 0x0174 }
        L_0x003b:
            java.io.File r11 = new java.io.File     // Catch:{ all -> 0x0174 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0174 }
            java.lang.String r3 = "temp_dec_"
            r2.<init>(r3)     // Catch:{ all -> 0x0174 }
            int r3 = android.os.Process.myPid()     // Catch:{ all -> 0x0174 }
            r2.append(r3)     // Catch:{ all -> 0x0174 }
            java.lang.String r3 = "_"
            r2.append(r3)     // Catch:{ all -> 0x0174 }
            int r3 = android.os.Process.myTid()     // Catch:{ all -> 0x0174 }
            r2.append(r3)     // Catch:{ all -> 0x0174 }
            java.lang.String r3 = "_"
            r2.append(r3)     // Catch:{ all -> 0x0174 }
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0174 }
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch:{ all -> 0x0174 }
            r2.append(r3)     // Catch:{ all -> 0x0174 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x0174 }
            r11.<init>(r1, r2)     // Catch:{ all -> 0x0174 }
            boolean r1 = r11.exists()     // Catch:{ all -> 0x0174 }
            if (r1 != 0) goto L_0x0159
            expectCreateDirFile(r11)     // Catch:{ all -> 0x0174 }
            r12 = 0
            if (r0 == 0) goto L_0x008a
            int r1 = r14.length()     // Catch:{ all -> 0x0087 }
            if (r1 <= 0) goto L_0x008a
            java.lang.String r1 = serverZipTag     // Catch:{ all -> 0x0087 }
            boolean r0 = r1.equalsIgnoreCase(r14)     // Catch:{ all -> 0x0087 }
            goto L_0x0093
        L_0x0087:
            r0 = move-exception
            goto L_0x0155
        L_0x008a:
            java.lang.String r0 = r20.getAbsolutePath()     // Catch:{ all -> 0x0087 }
            boolean r0 = detectZipByFileType(r0)     // Catch:{ all -> 0x0087 }
            r0 = r0 ^ r10
        L_0x0093:
            java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0087 }
            java.lang.String r1 = r20.getAbsolutePath()     // Catch:{ all -> 0x0087 }
            java.lang.String r2 = r11.getAbsolutePath()     // Catch:{ all -> 0x0087 }
            r3 = r13
            r4 = r22
            decompress(r0, r13, r1, r2, r4)     // Catch:{ all -> 0x0087 }
            r0 = r15
            r1 = r16
            r3 = r18
            r5 = r21
            expectMakeDecompressNotFinished(r0, r1, r3, r5)     // Catch:{ all -> 0x0087 }
            java.io.File[] r0 = r11.listFiles()     // Catch:{ all -> 0x0087 }
            if (r0 == 0) goto L_0x0143
            int r1 = r0.length     // Catch:{ all -> 0x0087 }
            r2 = 0
        L_0x00b6:
            if (r2 >= r1) goto L_0x012d
            r3 = r0[r2]     // Catch:{ all -> 0x0087 }
            java.lang.String r4 = r3.getName()     // Catch:{ all -> 0x0087 }
            java.lang.String r5 = "."
            java.lang.String r6 = ""
            java.lang.String r4 = r4.replace(r5, r6)     // Catch:{ all -> 0x0087 }
            java.lang.String r5 = "/"
            java.lang.String r6 = ""
            java.lang.String r4 = r4.replace(r5, r6)     // Catch:{ all -> 0x0087 }
            java.lang.String r5 = " "
            java.lang.String r6 = ""
            java.lang.String r4 = r4.replace(r5, r6)     // Catch:{ all -> 0x0087 }
            int r4 = r4.length()     // Catch:{ all -> 0x0087 }
            if (r4 == 0) goto L_0x012a
            java.io.File r4 = new java.io.File     // Catch:{ all -> 0x0087 }
            java.lang.String r5 = r3.getName()     // Catch:{ all -> 0x0087 }
            r4.<init>(r8, r5)     // Catch:{ all -> 0x0087 }
            boolean r5 = r4.exists()     // Catch:{ all -> 0x0087 }
            if (r5 == 0) goto L_0x010e
            boolean r5 = r4.isDirectory()     // Catch:{ all -> 0x0087 }
            if (r5 == 0) goto L_0x00f5
            recursiveDelete(r4, r7, r12)     // Catch:{ all -> 0x0087 }
            goto L_0x010e
        L_0x00f5:
            boolean r5 = r4.delete()     // Catch:{ all -> 0x0087 }
            if (r5 == 0) goto L_0x00fc
            goto L_0x010e
        L_0x00fc:
            com.uc.webview.export.cyclone.UCKnownException r0 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x0087 }
            r1 = 1004(0x3ec, float:1.407E-42)
            java.lang.String r2 = "File [%s] delete failed."
            java.lang.Object[] r3 = new java.lang.Object[r10]     // Catch:{ all -> 0x0087 }
            r3[r7] = r4     // Catch:{ all -> 0x0087 }
            java.lang.String r2 = java.lang.String.format(r2, r3)     // Catch:{ all -> 0x0087 }
            r0.<init>((int) r1, (java.lang.String) r2)     // Catch:{ all -> 0x0087 }
            throw r0     // Catch:{ all -> 0x0087 }
        L_0x010e:
            boolean r5 = r3.renameTo(r4)     // Catch:{ all -> 0x0087 }
            if (r5 == 0) goto L_0x0115
            goto L_0x012a
        L_0x0115:
            com.uc.webview.export.cyclone.UCKnownException r0 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x0087 }
            r1 = 1005(0x3ed, float:1.408E-42)
            java.lang.String r2 = "File [%s] renameTo [%s] failed."
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x0087 }
            r5[r7] = r3     // Catch:{ all -> 0x0087 }
            r5[r10] = r4     // Catch:{ all -> 0x0087 }
            java.lang.String r2 = java.lang.String.format(r2, r5)     // Catch:{ all -> 0x0087 }
            r0.<init>((int) r1, (java.lang.String) r2)     // Catch:{ all -> 0x0087 }
            throw r0     // Catch:{ all -> 0x0087 }
        L_0x012a:
            int r2 = r2 + 1
            goto L_0x00b6
        L_0x012d:
            r0 = r15
            r1 = r16
            r3 = r18
            r5 = r21
            r6 = r24
            expectMakeDecompressFinished(r0, r1, r3, r5, r6)     // Catch:{ all -> 0x0087 }
            recursiveDelete(r11, r7, r12)     // Catch:{ all -> 0x0174 }
            if (r23 == 0) goto L_0x0141
            deleteFile(r20)     // Catch:{ all -> 0x0174 }
        L_0x0141:
            monitor-exit(r9)
            return r10
        L_0x0143:
            com.uc.webview.export.cyclone.UCKnownException r0 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x0087 }
            r1 = 2008(0x7d8, float:2.814E-42)
            java.lang.String r2 = "Zip [%s] decompress success but no items found."
            java.lang.Object[] r3 = new java.lang.Object[r10]     // Catch:{ all -> 0x0087 }
            r3[r7] = r20     // Catch:{ all -> 0x0087 }
            java.lang.String r2 = java.lang.String.format(r2, r3)     // Catch:{ all -> 0x0087 }
            r0.<init>((int) r1, (java.lang.String) r2)     // Catch:{ all -> 0x0087 }
            throw r0     // Catch:{ all -> 0x0087 }
        L_0x0155:
            recursiveDelete(r11, r7, r12)     // Catch:{ all -> 0x0174 }
            throw r0     // Catch:{ all -> 0x0174 }
        L_0x0159:
            r3 = r13
            r4 = r22
            goto L_0x001e
        L_0x015e:
            com.uc.webview.export.cyclone.UCKnownException r0 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x0174 }
            r1 = 1001(0x3e9, float:1.403E-42)
            java.lang.String r2 = "File [%s] not exists."
            java.lang.Object[] r3 = new java.lang.Object[r10]     // Catch:{ all -> 0x0174 }
            java.lang.String r4 = r20.getAbsolutePath()     // Catch:{ all -> 0x0174 }
            r3[r7] = r4     // Catch:{ all -> 0x0174 }
            java.lang.String r2 = java.lang.String.format(r2, r3)     // Catch:{ all -> 0x0174 }
            r0.<init>((int) r1, (java.lang.String) r2)     // Catch:{ all -> 0x0174 }
            throw r0     // Catch:{ all -> 0x0174 }
        L_0x0174:
            r0 = move-exception
            monitor-exit(r9)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.UCCyclone.decompressIfNeeded(android.content.Context, java.lang.String, java.lang.String, long, long, java.io.File, java.io.File, java.io.FilenameFilter, boolean, int):boolean");
    }

    static File getDecompressStopFlgFile(String str, long j, long j2, File file, boolean z) {
        return new File(file, getDecompressSourceHash(str, j, j2, z));
    }

    static File getDecompressStartFlgFile(String str, long j, long j2, File file, boolean z) {
        return new File(file, getDecompressSourceHash(str, j, j2, z) + "_start");
    }

    static File getDecompressOrignFlgFile(String str, long j, long j2, File file, boolean z, int i) {
        return new File(file, getDecompressSourceHash(str, j, j2, z) + DecFileOrign.DecFileOrignFlag + Integer.toString(i));
    }

    static void expectMakeDecompressFinished(String str, long j, long j2, File file, int i) throws UCKnownException {
        try {
            File decompressStopFlgFile = getDecompressStopFlgFile(str, j, j2, file, false);
            if (!decompressStopFlgFile.exists()) {
                if (!decompressStopFlgFile.createNewFile()) {
                    throw new Exception("createNewFile return false");
                }
            }
            try {
                File decompressStartFlgFile = getDecompressStartFlgFile(str, j, j2, file, false);
                if (decompressStartFlgFile.exists()) {
                    if (!decompressStartFlgFile.delete()) {
                        throw new Exception("delete File return false");
                    }
                }
                if (i == DecFileOrign.Sdcard_Share_Core) {
                    try {
                        File decompressOrignFlgFile = getDecompressOrignFlgFile(str, j, j2, file, false, i);
                        if (decompressOrignFlgFile.exists()) {
                            return;
                        }
                        if (!decompressOrignFlgFile.createNewFile()) {
                            throw new Exception("createNewFile return false");
                        }
                    } catch (Throwable th) {
                        throw new UCKnownException(1006, th);
                    }
                }
            } catch (Throwable th2) {
                throw new UCKnownException(1004, th2);
            }
        } finally {
            UCKnownException uCKnownException = new UCKnownException(1006, th);
        }
    }

    private static boolean containsFile(ArrayList<File> arrayList, File file) {
        if (file == null || arrayList == null || arrayList.size() == 0) {
            return false;
        }
        try {
            Iterator<File> it = arrayList.iterator();
            while (it.hasNext()) {
                if (file.getCanonicalPath().equals(it.next().getCanonicalPath())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00d5  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00e0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void recursiveDelete(java.io.File r10, boolean r11, java.lang.Object r12) {
        /*
            boolean r0 = r10.exists()
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            r0 = 0
            r1 = 0
            r2 = 1
            if (r12 == 0) goto L_0x0040
            java.util.ArrayList r3 = new java.util.ArrayList
            r4 = 2
            r3.<init>(r4)
            boolean r4 = r12 instanceof java.io.File
            if (r4 == 0) goto L_0x001c
            java.io.File r12 = (java.io.File) r12
            r3.add(r12)
            goto L_0x0041
        L_0x001c:
            boolean r4 = r12 instanceof java.io.File[]
            if (r4 == 0) goto L_0x0026
            java.io.File[] r12 = (java.io.File[]) r12
            java.util.Collections.addAll(r3, r12)
            goto L_0x0041
        L_0x0026:
            com.uc.webview.export.cyclone.UCKnownException r10 = new com.uc.webview.export.cyclone.UCKnownException
            r11 = 1010(0x3f2, float:1.415E-42)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            java.lang.Class r12 = r12.getClass()
            java.lang.String r12 = r12.getName()
            r0[r1] = r12
            java.lang.String r12 = "File or File[] argument expected, but get [%s]."
            java.lang.String r12 = java.lang.String.format(r12, r0)
            r10.<init>((int) r11, (java.lang.String) r12)
            throw r10
        L_0x0040:
            r3 = r0
        L_0x0041:
            boolean r12 = enableDebugLog
            if (r12 != 0) goto L_0x0047
            r12 = r0
            goto L_0x004f
        L_0x0047:
            java.lang.String r12 = "i"
            java.lang.String r4 = "cyclone"
            com.uc.webview.export.cyclone.UCLogger r12 = com.uc.webview.export.cyclone.UCLogger.create(r12, r4)
        L_0x004f:
            if (r12 == 0) goto L_0x0074
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "recursiveDelete "
            r4.<init>(r5)
            r4.append(r10)
            java.lang.String r5 = ", "
            r4.append(r5)
            r4.append(r11)
            java.lang.String r5 = ", "
            r4.append(r5)
            r4.append(r3)
            java.lang.String r4 = r4.toString()
            java.lang.Throwable[] r5 = new java.lang.Throwable[r1]
            r12.print(r4, r5)
        L_0x0074:
            java.util.ArrayList r12 = new java.util.ArrayList
            r4 = 20
            r12.<init>(r4)
            if (r11 == 0) goto L_0x0088
            boolean r11 = r10.isDirectory()
            if (r11 == 0) goto L_0x0088
            java.io.File[] r10 = r10.listFiles()
            goto L_0x008d
        L_0x0088:
            java.io.File[] r11 = new java.io.File[r2]
            r11[r1] = r10
            r10 = r11
        L_0x008d:
            r11 = -1
        L_0x008e:
            int r4 = r10.length
            r5 = 0
        L_0x0090:
            if (r5 >= r4) goto L_0x00ce
            r6 = r10[r5]
            if (r3 == 0) goto L_0x009c
            boolean r7 = containsFile(r3, r6)
            if (r7 != 0) goto L_0x00cb
        L_0x009c:
            boolean r7 = r6.isDirectory()
            if (r7 == 0) goto L_0x00c8
            java.lang.String r7 = r6.getName()
            java.lang.String r8 = "."
            java.lang.String r9 = ""
            java.lang.String r7 = r7.replace(r8, r9)
            java.lang.String r8 = "/"
            java.lang.String r9 = ""
            java.lang.String r7 = r7.replace(r8, r9)
            java.lang.String r8 = " "
            java.lang.String r9 = ""
            java.lang.String r7 = r7.replace(r8, r9)
            int r7 = r7.length()
            if (r7 == 0) goto L_0x00cb
            r12.add(r6)
            goto L_0x00cb
        L_0x00c8:
            r6.delete()
        L_0x00cb:
            int r5 = r5 + 1
            goto L_0x0090
        L_0x00ce:
            int r11 = r11 + r2
            int r10 = r12.size()
            if (r11 >= r10) goto L_0x00e0
            java.lang.Object r10 = r12.get(r11)
            java.io.File r10 = (java.io.File) r10
            java.io.File[] r10 = r10.listFiles()
            goto L_0x00e1
        L_0x00e0:
            r10 = r0
        L_0x00e1:
            if (r10 == 0) goto L_0x00e7
            r4 = 256(0x100, float:3.59E-43)
            if (r11 < r4) goto L_0x008e
        L_0x00e7:
            int r10 = r12.size()
            int r10 = r10 - r2
        L_0x00ec:
            if (r10 < 0) goto L_0x00fa
            java.lang.Object r11 = r12.get(r10)
            java.io.File r11 = (java.io.File) r11
            r11.delete()
            int r10 = r10 + -1
            goto L_0x00ec
        L_0x00fa:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(java.io.File, boolean, java.lang.Object):void");
    }

    static void deleteFile(File file) throws UCKnownException {
        if (!file.delete()) {
            throw new UCKnownException(1004, String.format("File [%s] delete failed.", new Object[]{file.getAbsolutePath()}));
        }
    }

    public static File optimizedFileFor(String str, String str2) {
        String name = new File(str).getName();
        if (!name.endsWith(".dex")) {
            int lastIndexOf = name.lastIndexOf(".");
            if (lastIndexOf < 0) {
                name = name + ".dex";
            } else {
                StringBuilder sb = new StringBuilder(lastIndexOf + 4);
                sb.append(name, 0, lastIndexOf);
                sb.append(".dex");
                name = sb.toString();
            }
        }
        return new File(str2, name);
    }

    public static String hashFileContents(File file, MessageDigestType messageDigestType) {
        String str;
        String str2;
        BufferedInputStream bufferedInputStream;
        BufferedInputStream bufferedInputStream2 = null;
        if (!file.isFile()) {
            return null;
        }
        if (messageDigestType == MessageDigestType.MD5) {
            str = MessageDigestAlgorithms.MD5;
            str2 = "%032x";
        } else if (messageDigestType == MessageDigestType.SHA1) {
            str = MessageDigestAlgorithms.SHA_1;
            str2 = "%040x";
        } else if (messageDigestType != MessageDigestType.SHA256) {
            return null;
        } else {
            str = MessageDigestAlgorithms.SHA_256;
            str2 = "%064x";
        }
        byte[] bArr = new byte[131072];
        try {
            MessageDigest instance = MessageDigest.getInstance(str);
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            while (true) {
                try {
                    int read = bufferedInputStream.read(bArr, 0, 131072);
                    if (read != -1) {
                        instance.update(bArr, 0, read);
                    } else {
                        close(bufferedInputStream);
                        return String.format(Locale.CHINA, str2, new Object[]{new BigInteger(1, instance.digest())});
                    }
                } catch (Throwable th) {
                    th = th;
                    try {
                        th.printStackTrace();
                        close(bufferedInputStream);
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedInputStream2 = bufferedInputStream;
                        close(bufferedInputStream2);
                        throw th;
                    }
                }
            }
        } catch (Throwable th3) {
            th = th3;
            close(bufferedInputStream2);
            throw th;
        }
    }

    public static File genFile(Context context, String str, String str2, String str3, long j, String str4, byte[][] bArr, Object... objArr) throws UCKnownException, IOException {
        File dataFolder2 = getDataFolder(context);
        if (str != null && str.length() > 0) {
            dataFolder2 = expectCreateDirFile(new File(dataFolder2, str));
        }
        if (str2 == null) {
            str2 = "";
        }
        if (str3 == null) {
            str3 = "";
        }
        String str5 = str2 + String.valueOf(j) + str3;
        File file = new File(dataFolder2, str5);
        if (!file.canRead()) {
            file.delete();
        }
        long codeLength = (long) getCodeLength(bArr);
        if (!file.exists() || file.length() != codeLength || !str4.equals(hashFileContents(file, MessageDigestType.MD5))) {
            getFile(bArr, file);
            UCLogger create = !enableDebugLog ? null : UCLogger.create("d", LOG_TAG);
            if (create != null) {
                create.print("genFile Extract new " + str5 + " to " + dataFolder2, new Throwable[0]);
            }
        }
        if (sInusedFiles != null) {
            sInusedFiles.add(file);
        }
        return file;
    }

    private static int getCodeLength(byte[][] bArr) {
        int i = 0;
        for (byte[] length : bArr) {
            i += length.length;
        }
        return i;
    }

    public static void copy(File file, File file2) throws IOException {
        FileOutputStream fileOutputStream;
        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            fileOutputStream = new FileOutputStream(file2);
            byte[] bArr = new byte[8192];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.close();
                    fileInputStream.close();
                    return;
                }
            }
        } catch (Throwable th) {
            fileInputStream.close();
            throw th;
        }
    }
}
