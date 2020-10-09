package com.taobao.orange.util;

import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OConstant;
import com.taobao.orange.model.CheckDO;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FileUtil {
    public static final String ORANGE_DIR = "orange_config";
    private static final String TAG = "FileUtil";
    public static AtomicLong ioTime = new AtomicLong(0);
    public static AtomicInteger persistCount = new AtomicInteger(0);
    public static AtomicLong persistTime = new AtomicLong(0);
    public static AtomicInteger restoreCount = new AtomicInteger(0);
    public static AtomicLong restoreTime = new AtomicLong(0);
    private static File targetDir = getTargetDir();

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v0, resolved type: java.io.ObjectOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v0, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v2, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v2, resolved type: java.io.ObjectOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v6, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v7, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v8, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v3, resolved type: java.io.ObjectOutputStream} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00df A[SYNTHETIC, Splitter:B:54:0x00df] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0113 A[SYNTHETIC, Splitter:B:68:0x0113] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void persistObject(java.lang.Object r9, java.lang.String r10) {
        /*
            r0 = 1
            boolean r1 = com.taobao.orange.util.OLog.isPrintLog(r0)
            r2 = 0
            if (r1 == 0) goto L_0x0018
            java.lang.String r1 = "FileUtil"
            java.lang.String r3 = "persistObject"
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]
            java.lang.String r5 = "filename"
            r4[r2] = r5
            r4[r0] = r10
            com.taobao.orange.util.OLog.d(r1, r3, r4)
        L_0x0018:
            long r3 = java.lang.System.currentTimeMillis()
            r1 = 0
            java.lang.String r5 = ".tmp"
            java.io.File r6 = targetDir     // Catch:{ Throwable -> 0x00cb, all -> 0x00c6 }
            java.io.File r5 = java.io.File.createTempFile(r10, r5, r6)     // Catch:{ Throwable -> 0x00cb, all -> 0x00c6 }
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x00c3, all -> 0x00c0 }
            r6.<init>(r5)     // Catch:{ Throwable -> 0x00c3, all -> 0x00c0 }
            java.io.ObjectOutputStream r7 = new java.io.ObjectOutputStream     // Catch:{ Throwable -> 0x00be }
            java.io.BufferedOutputStream r8 = new java.io.BufferedOutputStream     // Catch:{ Throwable -> 0x00be }
            r8.<init>(r6)     // Catch:{ Throwable -> 0x00be }
            r7.<init>(r8)     // Catch:{ Throwable -> 0x00be }
            r7.writeObject(r9)     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            r7.flush()     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            java.io.File r9 = new java.io.File     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            java.io.File r1 = targetDir     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            r9.<init>(r1, r10)     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            boolean r9 = r5.renameTo(r9)     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            if (r9 != 0) goto L_0x0071
            java.lang.String r9 = "FileUtil"
            java.lang.String r1 = "persistObject rename fail"
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            r0[r2] = r10     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            com.taobao.orange.util.OLog.w(r9, r1, r0)     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            com.taobao.orange.util.OrangeUtils.close(r7)
            com.taobao.orange.util.OrangeUtils.close(r6)
            if (r5 == 0) goto L_0x00ff
            boolean r9 = r5.exists()     // Catch:{ Throwable -> 0x00f5 }
            if (r9 == 0) goto L_0x00ff
            boolean r9 = r5.delete()     // Catch:{ Throwable -> 0x00f5 }
            if (r9 != 0) goto L_0x00ff
            java.lang.String r9 = "FileUtil"
            java.lang.String r0 = "persistObject temp file delete fail"
            java.lang.Object[] r1 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x00f5 }
            com.taobao.orange.util.OLog.w(r9, r0, r1)     // Catch:{ Throwable -> 0x00f5 }
            goto L_0x00ff
        L_0x0071:
            boolean r9 = com.taobao.orange.util.OrangeMonitor.mPerformanceInfoRecordDone     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            if (r9 != 0) goto L_0x0090
            java.util.concurrent.atomic.AtomicInteger r9 = persistCount     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            r9.incrementAndGet()     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            java.util.concurrent.atomic.AtomicLong r9 = persistTime     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            r8 = 0
            long r0 = r0 - r3
            r9.addAndGet(r0)     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            java.util.concurrent.atomic.AtomicLong r9 = ioTime     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
            r8 = 0
            long r0 = r0 - r3
            r9.addAndGet(r0)     // Catch:{ Throwable -> 0x00bb, all -> 0x00b9 }
        L_0x0090:
            com.taobao.orange.util.OrangeUtils.close(r7)
            com.taobao.orange.util.OrangeUtils.close(r6)
            if (r5 == 0) goto L_0x00b8
            boolean r9 = r5.exists()     // Catch:{ Throwable -> 0x00ae }
            if (r9 == 0) goto L_0x00b8
            boolean r9 = r5.delete()     // Catch:{ Throwable -> 0x00ae }
            if (r9 != 0) goto L_0x00b8
            java.lang.String r9 = "FileUtil"
            java.lang.String r10 = "persistObject temp file delete fail"
            java.lang.Object[] r0 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x00ae }
            com.taobao.orange.util.OLog.w(r9, r10, r0)     // Catch:{ Throwable -> 0x00ae }
            goto L_0x00b8
        L_0x00ae:
            r9 = move-exception
            java.lang.String r10 = "FileUtil"
            java.lang.String r0 = "persistObject temp file delete cause exception"
            java.lang.Object[] r1 = new java.lang.Object[r2]
            com.taobao.orange.util.OLog.e(r10, r0, r9, r1)
        L_0x00b8:
            return
        L_0x00b9:
            r9 = move-exception
            goto L_0x010b
        L_0x00bb:
            r9 = move-exception
            r1 = r7
            goto L_0x00ce
        L_0x00be:
            r9 = move-exception
            goto L_0x00ce
        L_0x00c0:
            r9 = move-exception
            r6 = r1
            goto L_0x00c9
        L_0x00c3:
            r9 = move-exception
            r6 = r1
            goto L_0x00ce
        L_0x00c6:
            r9 = move-exception
            r5 = r1
            r6 = r5
        L_0x00c9:
            r7 = r6
            goto L_0x010b
        L_0x00cb:
            r9 = move-exception
            r5 = r1
            r6 = r5
        L_0x00ce:
            java.lang.String r0 = "FileUtil"
            java.lang.String r3 = "persistObject"
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ all -> 0x0109 }
            com.taobao.orange.util.OLog.e(r0, r3, r9, r4)     // Catch:{ all -> 0x0109 }
            com.taobao.orange.util.OrangeUtils.close(r1)
            com.taobao.orange.util.OrangeUtils.close(r6)
            if (r5 == 0) goto L_0x00ff
            boolean r9 = r5.exists()     // Catch:{ Throwable -> 0x00f5 }
            if (r9 == 0) goto L_0x00ff
            boolean r9 = r5.delete()     // Catch:{ Throwable -> 0x00f5 }
            if (r9 != 0) goto L_0x00ff
            java.lang.String r9 = "FileUtil"
            java.lang.String r0 = "persistObject temp file delete fail"
            java.lang.Object[] r1 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x00f5 }
            com.taobao.orange.util.OLog.w(r9, r0, r1)     // Catch:{ Throwable -> 0x00f5 }
            goto L_0x00ff
        L_0x00f5:
            r9 = move-exception
            java.lang.String r0 = "FileUtil"
            java.lang.String r1 = "persistObject temp file delete cause exception"
            java.lang.Object[] r2 = new java.lang.Object[r2]
            com.taobao.orange.util.OLog.e(r0, r1, r9, r2)
        L_0x00ff:
            java.lang.String r9 = "private_orange"
            java.lang.String r0 = "persist_fail_counts"
            r1 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            com.taobao.orange.util.OrangeMonitor.commitCount(r9, r0, r10, r1)
            return
        L_0x0109:
            r9 = move-exception
            r7 = r1
        L_0x010b:
            com.taobao.orange.util.OrangeUtils.close(r7)
            com.taobao.orange.util.OrangeUtils.close(r6)
            if (r5 == 0) goto L_0x0133
            boolean r10 = r5.exists()     // Catch:{ Throwable -> 0x0129 }
            if (r10 == 0) goto L_0x0133
            boolean r10 = r5.delete()     // Catch:{ Throwable -> 0x0129 }
            if (r10 != 0) goto L_0x0133
            java.lang.String r10 = "FileUtil"
            java.lang.String r0 = "persistObject temp file delete fail"
            java.lang.Object[] r1 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x0129 }
            com.taobao.orange.util.OLog.w(r10, r0, r1)     // Catch:{ Throwable -> 0x0129 }
            goto L_0x0133
        L_0x0129:
            r10 = move-exception
            java.lang.Object[] r0 = new java.lang.Object[r2]
            java.lang.String r1 = "FileUtil"
            java.lang.String r2 = "persistObject temp file delete cause exception"
            com.taobao.orange.util.OLog.e(r1, r2, r10, r0)
        L_0x0133:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.orange.util.FileUtil.persistObject(java.lang.Object, java.lang.String):void");
    }

    public static <T extends CheckDO> T restoreObject(String str) {
        ObjectInputStream objectInputStream;
        FileInputStream fileInputStream;
        Exception e;
        if (OLog.isPrintLog(1)) {
            OLog.d(TAG, "restoreObject", "filename", str);
        }
        long currentTimeMillis = System.currentTimeMillis();
        try {
            File file = new File(targetDir, str);
            if (!file.exists()) {
                if (OLog.isPrintLog(3)) {
                    OLog.w(TAG, "restoreObject not exists", "filename", str);
                }
                OrangeUtils.close((Closeable) null);
                OrangeUtils.close((Closeable) null);
                return null;
            }
            fileInputStream = new FileInputStream(file);
            try {
                objectInputStream = new ObjectInputStream(new BufferedInputStream(fileInputStream));
                try {
                    T t = (CheckDO) objectInputStream.readObject();
                    if (t.checkValid()) {
                        if (!OrangeMonitor.mPerformanceInfoRecordDone) {
                            restoreCount.incrementAndGet();
                            restoreTime.addAndGet(System.currentTimeMillis() - currentTimeMillis);
                            ioTime.addAndGet(System.currentTimeMillis() - currentTimeMillis);
                        }
                        OrangeUtils.close(objectInputStream);
                        OrangeUtils.close(fileInputStream);
                        return t;
                    }
                    throw new RuntimeException("check not vaild:" + str);
                } catch (Exception e2) {
                    e = e2;
                    try {
                        OLog.e(TAG, "restoreObject", e, new Object[0]);
                        OrangeUtils.close(objectInputStream);
                        OrangeUtils.close(fileInputStream);
                        OrangeMonitor.commitCount(OConstant.MONITOR_PRIVATE_MODULE, OConstant.POINT_RESTORE_FAIL_COUNTS, str, 1.0d);
                        return null;
                    } catch (Throwable th) {
                        th = th;
                        OrangeUtils.close(objectInputStream);
                        OrangeUtils.close(fileInputStream);
                        throw th;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                objectInputStream = null;
                OLog.e(TAG, "restoreObject", e, new Object[0]);
                OrangeUtils.close(objectInputStream);
                OrangeUtils.close(fileInputStream);
                OrangeMonitor.commitCount(OConstant.MONITOR_PRIVATE_MODULE, OConstant.POINT_RESTORE_FAIL_COUNTS, str, 1.0d);
                return null;
            } catch (Throwable th2) {
                th = th2;
                objectInputStream = null;
                OrangeUtils.close(objectInputStream);
                OrangeUtils.close(fileInputStream);
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            fileInputStream = null;
            objectInputStream = null;
            OLog.e(TAG, "restoreObject", e, new Object[0]);
            OrangeUtils.close(objectInputStream);
            OrangeUtils.close(fileInputStream);
            OrangeMonitor.commitCount(OConstant.MONITOR_PRIVATE_MODULE, OConstant.POINT_RESTORE_FAIL_COUNTS, str, 1.0d);
            return null;
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            objectInputStream = null;
            OrangeUtils.close(objectInputStream);
            OrangeUtils.close(fileInputStream);
            throw th;
        }
    }

    public static void deleteConfigFile(String str) {
        File file = new File(targetDir, str);
        if (file.exists()) {
            boolean delete = file.delete();
            if (OLog.isPrintLog(1)) {
                OLog.d(TAG, "deleteConfigFile", "filename", str, "result", Boolean.valueOf(delete));
            }
        }
    }

    public static void clearCacheFile() {
        OLog.i(TAG, "clearCacheFile", new Object[0]);
        cleanDir(targetDir);
    }

    public static File getOrangeConfigDir() {
        return targetDir;
    }

    private static void cleanDir(File file) {
        if (file != null && file.exists() && file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                if (file2.isFile()) {
                    file2.delete();
                } else if (file2.isDirectory()) {
                    cleanDir(file2);
                }
            }
        }
    }

    private static File getTargetDir() {
        File file;
        Throwable th;
        try {
            file = new File(new File(GlobalOrange.context.getFilesDir(), ORANGE_DIR), GlobalOrange.env.getDes());
            try {
                if (file.exists() && file.isFile() && !file.delete()) {
                    OLog.w(TAG, "getTargetDir target dir delete fail", new Object[0]);
                }
                if (!file.exists() && !file.mkdirs()) {
                    OLog.w(TAG, "getTargetDir mkdirs fail", new Object[0]);
                    OrangeMonitor.commitFail(OConstant.MONITOR_MODULE, OConstant.POINT_EXCEPTION, "getTargetDir", OConstant.CODE_POINT_EXP_GET_TARGET_DIR, "getTargetDir mkdirs fail");
                }
                OLog.d(TAG, "getTargetDir", file.getAbsolutePath());
            } catch (Throwable th2) {
                th = th2;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                th.printStackTrace(new PrintStream(byteArrayOutputStream));
                OrangeMonitor.commitFail(OConstant.MONITOR_MODULE, OConstant.POINT_EXCEPTION, "0", OConstant.CODE_POINT_EXP_GET_TARGET_DIR, byteArrayOutputStream.toString());
                return file;
            }
        } catch (Throwable th3) {
            file = null;
            th = th3;
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            th.printStackTrace(new PrintStream(byteArrayOutputStream2));
            OrangeMonitor.commitFail(OConstant.MONITOR_MODULE, OConstant.POINT_EXCEPTION, "0", OConstant.CODE_POINT_EXP_GET_TARGET_DIR, byteArrayOutputStream2.toString());
            return file;
        }
        return file;
    }
}
