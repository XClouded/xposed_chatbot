package com.alimama.union.app.rxnetwork;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import androidx.annotation.Nullable;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.jakewharton.disklrucache.DiskLruCache;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import kotlin.UByte;

public class EtaoDiskLruCache {
    private static final int DEFAULT_DISK_CACHE_SIZE = 20971520;
    private static final String DIR_NAME = "etaolrudisk";
    private static final int DISK_CACHE_INDEX = 0;
    private static final int DISK_EXTRA_INDEX = 1;
    private static CacheParams mCacheParams;
    private static EtaoDiskLruCache mResponseCache;
    private static Application sApplication;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private DiskLruCache mDiskLruCache;

    public static void init(Application application) {
        sApplication = application;
    }

    private EtaoDiskLruCache() {
        initDiskCache();
    }

    public static EtaoDiskLruCache getInstance() {
        if (mResponseCache == null) {
            if (sApplication != null) {
                mCacheParams = new CacheParams(sApplication, DIR_NAME);
                mResponseCache = new EtaoDiskLruCache();
            } else {
                throw new IllegalArgumentException("application to be null");
            }
        }
        return mResponseCache;
    }

    @Nullable
    public DiskLruResult getDataFromDisk(String str) {
        return getDataFromDisk(str, true);
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:2:0x0007 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x0087 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:48:0x0099 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:56:0x00a0 */
    /* JADX WARNING: Removed duplicated region for block: B:2:0x0007 A[LOOP:0: B:2:0x0007->B:65:0x0007, LOOP_START, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0096 A[SYNTHETIC, Splitter:B:46:0x0096] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x009d A[SYNTHETIC, Splitter:B:54:0x009d] */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alimama.union.app.rxnetwork.EtaoDiskLruCache.DiskLruResult getDataFromDisk(java.lang.String r10, boolean r11) {
        /*
            r9 = this;
            java.lang.String r0 = hashKeyForDisk(r10)
            java.lang.Object r1 = r9.mDiskCacheLock
            monitor-enter(r1)
        L_0x0007:
            boolean r2 = r9.mDiskCacheStarting     // Catch:{ all -> 0x00a4 }
            if (r2 == 0) goto L_0x0011
            java.lang.Object r2 = r9.mDiskCacheLock     // Catch:{ InterruptedException -> 0x0007 }
            r2.wait()     // Catch:{ InterruptedException -> 0x0007 }
            goto L_0x0007
        L_0x0011:
            com.jakewharton.disklrucache.DiskLruCache r2 = r9.mDiskLruCache     // Catch:{ all -> 0x00a4 }
            r3 = 0
            if (r2 == 0) goto L_0x00a2
            com.jakewharton.disklrucache.DiskLruCache r2 = r9.mDiskLruCache     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            com.jakewharton.disklrucache.DiskLruCache$Snapshot r2 = r2.get(r0)     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            if (r2 == 0) goto L_0x008c
            r4 = 1
            java.lang.String r5 = r2.getString(r4)     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            java.io.File r6 = new java.io.File     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            com.jakewharton.disklrucache.DiskLruCache r7 = r9.mDiskLruCache     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            java.io.File r7 = r7.getDirectory()     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            r8.<init>()     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            r8.append(r0)     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            java.lang.String r0 = ".0"
            r8.append(r0)     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            java.lang.String r0 = r8.toString()     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            r6.<init>(r7, r0)     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            if (r11 != 0) goto L_0x0058
            boolean r11 = r6.exists()     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            if (r11 == 0) goto L_0x0058
            com.alimama.union.app.rxnetwork.EtaoDiskLruCache$DiskLruResult r10 = new com.alimama.union.app.rxnetwork.EtaoDiskLruCache$DiskLruResult     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            r10.<init>()     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            r10.isSuccess = r4     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            r10.extra = r5     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            java.lang.String r11 = r6.getAbsolutePath()     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            r10.path = r11     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            monitor-exit(r1)     // Catch:{ all -> 0x00a4 }
            return r10
        L_0x0058:
            r11 = 0
            java.io.InputStream r0 = r2.getInputStream(r11)     // Catch:{ IOException -> 0x009a, all -> 0x0093 }
            if (r0 == 0) goto L_0x008d
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x009b, all -> 0x0089 }
            r2.<init>()     // Catch:{ IOException -> 0x009b, all -> 0x0089 }
            r4 = 1024(0x400, float:1.435E-42)
            byte[] r4 = new byte[r4]     // Catch:{ IOException -> 0x009b, all -> 0x0089 }
        L_0x0068:
            int r7 = r0.read(r4)     // Catch:{ IOException -> 0x009b, all -> 0x0089 }
            r8 = -1
            if (r7 == r8) goto L_0x0073
            r2.write(r4, r11, r7)     // Catch:{ IOException -> 0x009b, all -> 0x0089 }
            goto L_0x0068
        L_0x0073:
            com.alimama.union.app.rxnetwork.EtaoDiskLruCache$DiskLruResult r11 = new com.alimama.union.app.rxnetwork.EtaoDiskLruCache$DiskLruResult     // Catch:{ IOException -> 0x009b, all -> 0x0089 }
            byte[] r2 = r2.toByteArray()     // Catch:{ IOException -> 0x009b, all -> 0x0089 }
            r11.<init>(r10, r5, r2)     // Catch:{ IOException -> 0x009b, all -> 0x0089 }
            java.lang.String r10 = r6.getAbsolutePath()     // Catch:{ IOException -> 0x009b, all -> 0x0089 }
            r11.path = r10     // Catch:{ IOException -> 0x009b, all -> 0x0089 }
            if (r0 == 0) goto L_0x0087
            r0.close()     // Catch:{ IOException -> 0x0087 }
        L_0x0087:
            monitor-exit(r1)     // Catch:{ all -> 0x00a4 }
            return r11
        L_0x0089:
            r10 = move-exception
            r3 = r0
            goto L_0x0094
        L_0x008c:
            r0 = r3
        L_0x008d:
            if (r0 == 0) goto L_0x00a2
            r0.close()     // Catch:{ IOException -> 0x00a2 }
            goto L_0x00a2
        L_0x0093:
            r10 = move-exception
        L_0x0094:
            if (r3 == 0) goto L_0x0099
            r3.close()     // Catch:{ IOException -> 0x0099 }
        L_0x0099:
            throw r10     // Catch:{ all -> 0x00a4 }
        L_0x009a:
            r0 = r3
        L_0x009b:
            if (r0 == 0) goto L_0x00a0
            r0.close()     // Catch:{ IOException -> 0x00a0 }
        L_0x00a0:
            monitor-exit(r1)     // Catch:{ all -> 0x00a4 }
            return r3
        L_0x00a2:
            monitor-exit(r1)     // Catch:{ all -> 0x00a4 }
            return r3
        L_0x00a4:
            r10 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x00a4 }
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.rxnetwork.EtaoDiskLruCache.getDataFromDisk(java.lang.String, boolean):com.alimama.union.app.rxnetwork.EtaoDiskLruCache$DiskLruResult");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:14|(0)|23|24) */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|(6:5|6|7|8|(4:10|11|12|13)|(2:16|17))|35|36) */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0044, code lost:
        if (r2 != null) goto L_0x0046;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x004b, code lost:
        if (r2 != null) goto L_0x0046;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0042 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x004e */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x003f A[SYNTHETIC, Splitter:B:21:0x003f] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:23:0x0042=Splitter:B:23:0x0042, B:35:0x004e=Splitter:B:35:0x004e} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void putDataToDisk(java.lang.String r4, java.lang.String r5, byte[] r6) {
        /*
            r3 = this;
            java.lang.Object r0 = r3.mDiskCacheLock
            monitor-enter(r0)
            com.jakewharton.disklrucache.DiskLruCache r1 = r3.mDiskLruCache     // Catch:{ all -> 0x0050 }
            if (r1 == 0) goto L_0x004e
            java.lang.String r4 = hashKeyForDisk(r4)     // Catch:{ all -> 0x0050 }
            r1 = 0
            com.jakewharton.disklrucache.DiskLruCache r2 = r3.mDiskLruCache     // Catch:{ IOException -> 0x004a, Exception -> 0x0043, all -> 0x003b }
            com.jakewharton.disklrucache.DiskLruCache$Editor r4 = r2.edit(r4)     // Catch:{ IOException -> 0x004a, Exception -> 0x0043, all -> 0x003b }
            if (r4 == 0) goto L_0x0035
            r2 = 0
            java.io.OutputStream r2 = r4.newOutputStream(r2)     // Catch:{ IOException -> 0x004a, Exception -> 0x0043, all -> 0x003b }
            java.io.BufferedOutputStream r1 = new java.io.BufferedOutputStream     // Catch:{ IOException -> 0x004b, Exception -> 0x0044, all -> 0x0033 }
            r1.<init>(r2)     // Catch:{ IOException -> 0x004b, Exception -> 0x0044, all -> 0x0033 }
            r1.write(r6)     // Catch:{ IOException -> 0x004b, Exception -> 0x0044, all -> 0x0033 }
            r1.flush()     // Catch:{ IOException -> 0x004b, Exception -> 0x0044, all -> 0x0033 }
            r1.close()     // Catch:{ IOException -> 0x004b, Exception -> 0x0044, all -> 0x0033 }
            r2.close()     // Catch:{ IOException -> 0x004b, Exception -> 0x0044, all -> 0x0033 }
            r6 = 1
            r4.set(r6, r5)     // Catch:{ IOException -> 0x004b, Exception -> 0x0044, all -> 0x0033 }
            r4.commit()     // Catch:{ IOException -> 0x004b, Exception -> 0x0044, all -> 0x0033 }
            r1 = r2
            goto L_0x0035
        L_0x0033:
            r4 = move-exception
            goto L_0x003d
        L_0x0035:
            if (r1 == 0) goto L_0x004e
            r1.close()     // Catch:{ IOException -> 0x004e }
            goto L_0x004e
        L_0x003b:
            r4 = move-exception
            r2 = r1
        L_0x003d:
            if (r2 == 0) goto L_0x0042
            r2.close()     // Catch:{ IOException -> 0x0042 }
        L_0x0042:
            throw r4     // Catch:{ all -> 0x0050 }
        L_0x0043:
            r2 = r1
        L_0x0044:
            if (r2 == 0) goto L_0x004e
        L_0x0046:
            r2.close()     // Catch:{ IOException -> 0x004e }
            goto L_0x004e
        L_0x004a:
            r2 = r1
        L_0x004b:
            if (r2 == 0) goto L_0x004e
            goto L_0x0046
        L_0x004e:
            monitor-exit(r0)     // Catch:{ all -> 0x0050 }
            return
        L_0x0050:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0050 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.rxnetwork.EtaoDiskLruCache.putDataToDisk(java.lang.String, java.lang.String, byte[]):void");
    }

    public static String hashKeyForDisk(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            instance.update(str.getBytes());
            return bytesToHexString(instance.digest());
        } catch (NoSuchAlgorithmException unused) {
            return String.valueOf(str.hashCode());
        }
    }

    private static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & UByte.MAX_VALUE);
            if (hexString.length() == 1) {
                sb.append('0');
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    @TargetApi(9)
    private static long getUsableSpace(File file) {
        if (hasGingerbread()) {
            return file.getUsableSpace();
        }
        StatFs statFs = new StatFs(file.getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks());
    }

    public File getDiskCacheFolder() {
        if (mCacheParams != null) {
            return mCacheParams.diskCacheDir;
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static File getDiskCacheDir(Context context, String str) {
        String str2;
        if ("mounted".equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable()) {
            File externalCacheDir = getExternalCacheDir(context);
            if (externalCacheDir != null) {
                str2 = externalCacheDir.getPath();
            } else {
                str2 = context.getCacheDir().getPath();
            }
        } else {
            str2 = context.getCacheDir().getPath();
        }
        return new File(str2 + File.separator + str);
    }

    @TargetApi(9)
    private static boolean isExternalStorageRemovable() {
        if (hasGingerbread()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    @TargetApi(8)
    private static File getExternalCacheDir(Context context) {
        if (hasFroyo()) {
            return context.getExternalCacheDir();
        }
        return new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/cache/"));
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:16|17|18|19|20) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x003f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initDiskCache() {
        /*
            r7 = this;
            java.lang.Object r0 = r7.mDiskCacheLock
            monitor-enter(r0)
            com.jakewharton.disklrucache.DiskLruCache r1 = r7.mDiskLruCache     // Catch:{ all -> 0x004e }
            if (r1 == 0) goto L_0x000f
            com.jakewharton.disklrucache.DiskLruCache r1 = r7.mDiskLruCache     // Catch:{ all -> 0x004e }
            boolean r1 = r1.isClosed()     // Catch:{ all -> 0x004e }
            if (r1 == 0) goto L_0x0044
        L_0x000f:
            com.alimama.union.app.rxnetwork.EtaoDiskLruCache$CacheParams r1 = mCacheParams     // Catch:{ all -> 0x004e }
            java.io.File r1 = r1.diskCacheDir     // Catch:{ all -> 0x004e }
            com.alimama.union.app.rxnetwork.EtaoDiskLruCache$CacheParams r2 = mCacheParams     // Catch:{ all -> 0x004e }
            boolean r2 = r2.diskCacheEnabled     // Catch:{ all -> 0x004e }
            if (r2 == 0) goto L_0x0044
            if (r1 == 0) goto L_0x0044
            boolean r2 = r1.exists()     // Catch:{ all -> 0x004e }
            if (r2 != 0) goto L_0x0024
            r1.mkdirs()     // Catch:{ all -> 0x004e }
        L_0x0024:
            long r2 = getUsableSpace(r1)     // Catch:{ all -> 0x004e }
            com.alimama.union.app.rxnetwork.EtaoDiskLruCache$CacheParams r4 = mCacheParams     // Catch:{ all -> 0x004e }
            int r4 = r4.diskCacheSize     // Catch:{ all -> 0x004e }
            long r4 = (long) r4
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 <= 0) goto L_0x0044
            r2 = 1
            r3 = 2
            com.alimama.union.app.rxnetwork.EtaoDiskLruCache$CacheParams r4 = mCacheParams     // Catch:{ IOException -> 0x003f }
            int r4 = r4.diskCacheSize     // Catch:{ IOException -> 0x003f }
            long r4 = (long) r4     // Catch:{ IOException -> 0x003f }
            com.jakewharton.disklrucache.DiskLruCache r1 = com.jakewharton.disklrucache.DiskLruCache.open(r1, r2, r3, r4)     // Catch:{ IOException -> 0x003f }
            r7.mDiskLruCache = r1     // Catch:{ IOException -> 0x003f }
            goto L_0x0044
        L_0x003f:
            com.alimama.union.app.rxnetwork.EtaoDiskLruCache$CacheParams r1 = mCacheParams     // Catch:{ all -> 0x004e }
            r2 = 0
            r1.diskCacheDir = r2     // Catch:{ all -> 0x004e }
        L_0x0044:
            r1 = 0
            r7.mDiskCacheStarting = r1     // Catch:{ all -> 0x004e }
            java.lang.Object r1 = r7.mDiskCacheLock     // Catch:{ all -> 0x004e }
            r1.notifyAll()     // Catch:{ all -> 0x004e }
            monitor-exit(r0)     // Catch:{ all -> 0x004e }
            return
        L_0x004e:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x004e }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.rxnetwork.EtaoDiskLruCache.initDiskCache():void");
    }

    public static class DiskLruResult {
        public byte[] data;
        public String extra;
        public boolean isSuccess;
        public String key;
        public String path;

        public DiskLruResult() {
        }

        public DiskLruResult(String str, String str2, byte[] bArr) {
            this.key = str;
            this.extra = str2;
            this.data = bArr;
        }
    }

    public static class CacheParams {
        public File diskCacheDir;
        public boolean diskCacheEnabled = true;
        public int diskCacheSize = EtaoDiskLruCache.DEFAULT_DISK_CACHE_SIZE;

        public CacheParams(Context context, String str) {
            this.diskCacheDir = EtaoDiskLruCache.getDiskCacheDir(context, str);
        }
    }

    private static long safeLongValue(String str) {
        try {
            return Long.valueOf(str).longValue();
        } catch (Exception unused) {
            return System.currentTimeMillis();
        }
    }

    private static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= 8;
    }

    private static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= 9;
    }
}
