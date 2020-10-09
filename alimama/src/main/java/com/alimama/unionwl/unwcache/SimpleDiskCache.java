package com.alimama.unionwl.unwcache;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.taobao.windvane.jsbridge.api.WVFile;
import androidx.annotation.Nullable;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import kotlin.UByte;

public class SimpleDiskCache {
    private static final int DEFAULT_DISK_CACHE_SIZE = 5242880;
    private static final String S_DISK_EXTRA = "simple_disk_extra";
    private static final String dirName = "disk";
    private static boolean isInit = false;
    private static Application sApplication;
    private static SimpleDiskCache sDiskCache;
    private Application mApplication;
    private File mDiskCacheDir;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;

    public static void init(Application application) {
        sApplication = application;
    }

    private SimpleDiskCache(Application application) {
        this.mApplication = application;
        initDiskCacheDir();
        initDiskCache();
    }

    public static SimpleDiskCache getInstance() {
        if (sDiskCache == null) {
            if (sApplication != null) {
                sDiskCache = new SimpleDiskCache(sApplication);
            } else {
                throw new IllegalArgumentException("Application to be null");
            }
        }
        return sDiskCache;
    }

    public File getDiskCacheDir() {
        return this.mDiskCacheDir;
    }

    public String putDataToDisk(String str, String str2, byte[] bArr) {
        return putDataToDisk(str, str2, bArr, true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0045, code lost:
        if (r1 != null) goto L_0x0047;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x004c, code lost:
        if (r1 != null) goto L_0x0047;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0050, code lost:
        if (r1 != null) goto L_0x0047;
     */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0040 A[SYNTHETIC, Splitter:B:21:0x0040] */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String putDataToDisk(java.lang.String r4, java.lang.String r5, byte[] r6, boolean r7) {
        /*
            r3 = this;
            r0 = 0
            if (r6 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.io.File r1 = r3.mDiskCacheDir
            if (r1 == 0) goto L_0x0053
            boolean r1 = isInit
            if (r1 == 0) goto L_0x0053
            if (r7 == 0) goto L_0x0012
            java.lang.String r4 = hashKeyForDisk(r4)
        L_0x0012:
            java.io.File r7 = new java.io.File     // Catch:{ FileNotFoundException -> 0x004f, IOException -> 0x004b, NullPointerException -> 0x0044, all -> 0x003c }
            java.io.File r1 = r3.mDiskCacheDir     // Catch:{ FileNotFoundException -> 0x004f, IOException -> 0x004b, NullPointerException -> 0x0044, all -> 0x003c }
            r7.<init>(r1, r4)     // Catch:{ FileNotFoundException -> 0x004f, IOException -> 0x004b, NullPointerException -> 0x0044, all -> 0x003c }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x004f, IOException -> 0x004b, NullPointerException -> 0x0044, all -> 0x003c }
            r1.<init>(r7)     // Catch:{ FileNotFoundException -> 0x004f, IOException -> 0x004b, NullPointerException -> 0x0044, all -> 0x003c }
            java.io.BufferedOutputStream r2 = new java.io.BufferedOutputStream     // Catch:{ FileNotFoundException -> 0x0050, IOException -> 0x004c, NullPointerException -> 0x0045, all -> 0x003a }
            r2.<init>(r1)     // Catch:{ FileNotFoundException -> 0x0050, IOException -> 0x004c, NullPointerException -> 0x0045, all -> 0x003a }
            r2.write(r6)     // Catch:{ FileNotFoundException -> 0x0050, IOException -> 0x004c, NullPointerException -> 0x0045, all -> 0x003a }
            r2.flush()     // Catch:{ FileNotFoundException -> 0x0050, IOException -> 0x004c, NullPointerException -> 0x0045, all -> 0x003a }
            r2.close()     // Catch:{ FileNotFoundException -> 0x0050, IOException -> 0x004c, NullPointerException -> 0x0045, all -> 0x003a }
            r1.close()     // Catch:{ FileNotFoundException -> 0x0050, IOException -> 0x004c, NullPointerException -> 0x0045, all -> 0x003a }
            r3.writeExtraData(r4, r5)     // Catch:{ FileNotFoundException -> 0x0050, IOException -> 0x004c, NullPointerException -> 0x0045, all -> 0x003a }
            java.lang.String r4 = r7.getAbsolutePath()     // Catch:{ FileNotFoundException -> 0x0050, IOException -> 0x004c, NullPointerException -> 0x0045, all -> 0x003a }
            r1.close()     // Catch:{ IOException -> 0x0039 }
        L_0x0039:
            return r4
        L_0x003a:
            r4 = move-exception
            goto L_0x003e
        L_0x003c:
            r4 = move-exception
            r1 = r0
        L_0x003e:
            if (r1 == 0) goto L_0x0043
            r1.close()     // Catch:{ IOException -> 0x0043 }
        L_0x0043:
            throw r4
        L_0x0044:
            r1 = r0
        L_0x0045:
            if (r1 == 0) goto L_0x0053
        L_0x0047:
            r1.close()     // Catch:{ IOException -> 0x0053 }
            goto L_0x0053
        L_0x004b:
            r1 = r0
        L_0x004c:
            if (r1 == 0) goto L_0x0053
            goto L_0x0047
        L_0x004f:
            r1 = r0
        L_0x0050:
            if (r1 == 0) goto L_0x0053
            goto L_0x0047
        L_0x0053:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.unionwl.unwcache.SimpleDiskCache.putDataToDisk(java.lang.String, java.lang.String, byte[], boolean):java.lang.String");
    }

    @Nullable
    public SimpleDiskResult getDataFromDisk(String str, boolean z) {
        return getDataFromDisk(str, z, true);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(11:24|25|26|27|(2:28|(1:30)(1:65))|31|32|33|34|35|36) */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:37|(0)|43|44) */
    /* JADX WARNING: Can't wrap try/catch for region: R(8:(6:7|8|64|63|5|4)|9|10|(2:14|(3:16|17|18)(2:19|(3:21|22|23)(11:24|25|26|27|(2:28|(1:30)(1:65))|31|32|33|34|35|36)))|55|56|57|58) */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0086, code lost:
        if (r10 != null) goto L_0x0088;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        r10.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x008d, code lost:
        if (r10 != null) goto L_0x0088;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:34:0x0079 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x0084 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0009 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x0090 */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0081 A[SYNTHETIC, Splitter:B:41:0x0081] */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0009 A[LOOP:0: B:4:0x0009->B:63:0x0009, LOOP_START, SYNTHETIC] */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alimama.unionwl.unwcache.SimpleDiskCache.SimpleDiskResult getDataFromDisk(java.lang.String r9, boolean r10, boolean r11) {
        /*
            r8 = this;
            if (r11 == 0) goto L_0x0006
            java.lang.String r9 = hashKeyForDisk(r9)
        L_0x0006:
            java.lang.Object r11 = r8.mDiskCacheLock
            monitor-enter(r11)
        L_0x0009:
            boolean r0 = r8.mDiskCacheStarting     // Catch:{ all -> 0x0099 }
            if (r0 == 0) goto L_0x0013
            java.lang.Object r0 = r8.mDiskCacheLock     // Catch:{ InterruptedException -> 0x0009 }
            r0.wait()     // Catch:{ InterruptedException -> 0x0009 }
            goto L_0x0009
        L_0x0013:
            java.io.File r0 = r8.mDiskCacheDir     // Catch:{ all -> 0x0099 }
            r1 = 0
            if (r0 == 0) goto L_0x0090
            boolean r0 = isInit     // Catch:{ all -> 0x0099 }
            if (r0 == 0) goto L_0x0090
            java.lang.String r0 = r8.getExtraData(r9)     // Catch:{ all -> 0x0099 }
            java.io.File r2 = new java.io.File     // Catch:{ all -> 0x0099 }
            java.io.File r3 = r8.mDiskCacheDir     // Catch:{ all -> 0x0099 }
            r2.<init>(r3, r9)     // Catch:{ all -> 0x0099 }
            boolean r3 = r2.exists()     // Catch:{ all -> 0x0099 }
            if (r3 != 0) goto L_0x0036
            com.alimama.unionwl.unwcache.SimpleDiskCache$SimpleDiskResult r9 = new com.alimama.unionwl.unwcache.SimpleDiskCache$SimpleDiskResult     // Catch:{ all -> 0x0099 }
            r9.<init>()     // Catch:{ all -> 0x0099 }
            r9.isSuccess = r1     // Catch:{ all -> 0x0099 }
            monitor-exit(r11)     // Catch:{ all -> 0x0099 }
            return r9
        L_0x0036:
            r3 = 1
            r4 = 0
            if (r10 != 0) goto L_0x0049
            com.alimama.unionwl.unwcache.SimpleDiskCache$SimpleDiskResult r10 = new com.alimama.unionwl.unwcache.SimpleDiskCache$SimpleDiskResult     // Catch:{ all -> 0x0099 }
            java.lang.String r1 = r2.getAbsolutePath()     // Catch:{ all -> 0x0099 }
            r10.<init>(r9, r1, r0, r4)     // Catch:{ all -> 0x0099 }
            r10.isSuccess = r3     // Catch:{ all -> 0x0099 }
            r10.extra = r0     // Catch:{ all -> 0x0099 }
            monitor-exit(r11)     // Catch:{ all -> 0x0099 }
            return r10
        L_0x0049:
            java.io.BufferedInputStream r10 = new java.io.BufferedInputStream     // Catch:{ FileNotFoundException -> 0x008c, IOException -> 0x0085, all -> 0x007d }
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x008c, IOException -> 0x0085, all -> 0x007d }
            r5.<init>(r2)     // Catch:{ FileNotFoundException -> 0x008c, IOException -> 0x0085, all -> 0x007d }
            r10.<init>(r5)     // Catch:{ FileNotFoundException -> 0x008c, IOException -> 0x0085, all -> 0x007d }
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream     // Catch:{ FileNotFoundException -> 0x008d, IOException -> 0x0086, all -> 0x007b }
            r4.<init>()     // Catch:{ FileNotFoundException -> 0x008d, IOException -> 0x0086, all -> 0x007b }
            r5 = 1024(0x400, float:1.435E-42)
            byte[] r5 = new byte[r5]     // Catch:{ FileNotFoundException -> 0x008d, IOException -> 0x0086, all -> 0x007b }
        L_0x005c:
            int r6 = r10.read(r5)     // Catch:{ FileNotFoundException -> 0x008d, IOException -> 0x0086, all -> 0x007b }
            r7 = -1
            if (r6 == r7) goto L_0x0067
            r4.write(r5, r1, r6)     // Catch:{ FileNotFoundException -> 0x008d, IOException -> 0x0086, all -> 0x007b }
            goto L_0x005c
        L_0x0067:
            com.alimama.unionwl.unwcache.SimpleDiskCache$SimpleDiskResult r5 = new com.alimama.unionwl.unwcache.SimpleDiskCache$SimpleDiskResult     // Catch:{ FileNotFoundException -> 0x008d, IOException -> 0x0086, all -> 0x007b }
            java.lang.String r2 = r2.getAbsolutePath()     // Catch:{ FileNotFoundException -> 0x008d, IOException -> 0x0086, all -> 0x007b }
            byte[] r4 = r4.toByteArray()     // Catch:{ FileNotFoundException -> 0x008d, IOException -> 0x0086, all -> 0x007b }
            r5.<init>(r9, r2, r0, r4)     // Catch:{ FileNotFoundException -> 0x008d, IOException -> 0x0086, all -> 0x007b }
            r5.isSuccess = r3     // Catch:{ FileNotFoundException -> 0x008d, IOException -> 0x0086, all -> 0x007b }
            r10.close()     // Catch:{ IOException -> 0x0079 }
        L_0x0079:
            monitor-exit(r11)     // Catch:{ all -> 0x0099 }
            return r5
        L_0x007b:
            r9 = move-exception
            goto L_0x007f
        L_0x007d:
            r9 = move-exception
            r10 = r4
        L_0x007f:
            if (r10 == 0) goto L_0x0084
            r10.close()     // Catch:{ IOException -> 0x0084 }
        L_0x0084:
            throw r9     // Catch:{ all -> 0x0099 }
        L_0x0085:
            r10 = r4
        L_0x0086:
            if (r10 == 0) goto L_0x0090
        L_0x0088:
            r10.close()     // Catch:{ IOException -> 0x0090 }
            goto L_0x0090
        L_0x008c:
            r10 = r4
        L_0x008d:
            if (r10 == 0) goto L_0x0090
            goto L_0x0088
        L_0x0090:
            monitor-exit(r11)     // Catch:{ all -> 0x0099 }
            com.alimama.unionwl.unwcache.SimpleDiskCache$SimpleDiskResult r9 = new com.alimama.unionwl.unwcache.SimpleDiskCache$SimpleDiskResult
            r9.<init>()
            r9.isSuccess = r1
            return r9
        L_0x0099:
            r9 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x0099 }
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.unionwl.unwcache.SimpleDiskCache.getDataFromDisk(java.lang.String, boolean, boolean):com.alimama.unionwl.unwcache.SimpleDiskCache$SimpleDiskResult");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0030, code lost:
        return false;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:2:0x0007 */
    /* JADX WARNING: Removed duplicated region for block: B:2:0x0007 A[LOOP:0: B:2:0x0007->B:22:0x0007, LOOP_START, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean removeDataFromDisk(java.lang.String r4) {
        /*
            r3 = this;
            java.lang.String r4 = hashKeyForDisk(r4)
            java.lang.Object r0 = r3.mDiskCacheLock
            monitor-enter(r0)
        L_0x0007:
            boolean r1 = r3.mDiskCacheStarting     // Catch:{ all -> 0x0032 }
            if (r1 == 0) goto L_0x0011
            java.lang.Object r1 = r3.mDiskCacheLock     // Catch:{ InterruptedException -> 0x0007 }
            r1.wait()     // Catch:{ InterruptedException -> 0x0007 }
            goto L_0x0007
        L_0x0011:
            java.io.File r1 = r3.mDiskCacheDir     // Catch:{ all -> 0x0032 }
            if (r1 == 0) goto L_0x002f
            boolean r1 = isInit     // Catch:{ all -> 0x0032 }
            if (r1 == 0) goto L_0x002f
            r3.removeExtraData(r4)     // Catch:{ all -> 0x0032 }
            java.io.File r1 = new java.io.File     // Catch:{ all -> 0x0032 }
            java.io.File r2 = r3.mDiskCacheDir     // Catch:{ all -> 0x0032 }
            r1.<init>(r2, r4)     // Catch:{ all -> 0x0032 }
            boolean r4 = r1.exists()     // Catch:{ all -> 0x0032 }
            if (r4 == 0) goto L_0x002f
            r1.delete()     // Catch:{ all -> 0x0032 }
            r4 = 1
            monitor-exit(r0)     // Catch:{ all -> 0x0032 }
            return r4
        L_0x002f:
            monitor-exit(r0)     // Catch:{ all -> 0x0032 }
            r4 = 0
            return r4
        L_0x0032:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0032 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.unionwl.unwcache.SimpleDiskCache.removeDataFromDisk(java.lang.String):boolean");
    }

    private void writeExtraData(String str, String str2) {
        SharedPreferences.Editor edit = this.mApplication.getSharedPreferences(S_DISK_EXTRA, 0).edit();
        edit.putString(str, str2);
        edit.apply();
    }

    private String getExtraData(String str) {
        return this.mApplication.getSharedPreferences(S_DISK_EXTRA, 0).getString(str, (String) null);
    }

    private void removeExtraData(String str) {
        SharedPreferences sharedPreferences = this.mApplication.getSharedPreferences(S_DISK_EXTRA, 0);
        if (sharedPreferences.contains(str)) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(str);
            edit.apply();
        }
    }

    private void initDiskCacheDir() {
        this.mDiskCacheDir = getDiskCacheDir(this.mApplication, dirName);
    }

    private void initDiskCache() {
        synchronized (this.mDiskCacheLock) {
            if (!this.mDiskCacheDir.exists()) {
                this.mDiskCacheDir.mkdirs();
            }
            if (getUsableSpace(this.mDiskCacheDir) > WVFile.FILE_MAX_SIZE) {
                isInit = true;
            } else {
                isInit = false;
            }
            this.mDiskCacheStarting = false;
            this.mDiskCacheLock.notifyAll();
        }
    }

    private static File getDiskCacheDir(Context context, String str) {
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

    @TargetApi(9)
    private static long getUsableSpace(File file) {
        if (hasGingerbread()) {
            return file.getUsableSpace();
        }
        StatFs statFs = new StatFs(file.getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks());
    }

    private static String hashKeyForDisk(String str) {
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

    private static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= 8;
    }

    private static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= 9;
    }

    public static class SimpleDiskResult {
        public byte[] data;
        public String extra;
        public boolean isSuccess;
        public String key;
        public String path;

        public SimpleDiskResult() {
        }

        public SimpleDiskResult(String str, String str2, String str3, byte[] bArr) {
            this.key = str;
            this.extra = str3;
            this.data = bArr;
            this.path = str2;
        }
    }
}
