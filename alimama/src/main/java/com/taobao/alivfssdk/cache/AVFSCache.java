package com.taobao.alivfssdk.cache;

import androidx.annotation.Nullable;
import com.taobao.alivfsadapter.MonitorCacheEvent;
import com.taobao.alivfssdk.fresco.cache.common.NoOpCacheErrorLogger;
import com.taobao.alivfssdk.fresco.cache.disk.DiskStorageCache;
import com.taobao.alivfssdk.fresco.common.file.FileTree;
import com.taobao.alivfssdk.utils.AVFSCacheLog;
import com.taobao.orange.OConfigListener;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AVFSCache implements Closeable {
    private static final String ALIVFS_CONFIG_GROUP = "ali_database_es";
    private static final String ALIVFS_LSM_FOR_PHENIX = "lsm_white_list";
    private static final String ALIVFS_SWITCH_FLAG_FILE = "alivfs.cfg";
    private static final long DEFAULT_TIME_TO_LIVE_SECONDS = 21600;
    private static final String TAG = "AVFSCache";
    private static boolean sInitialized = false;
    private ClassLoader mClassLoader;
    private final AVFSCacheConfig mConfig;
    private final File mDir;
    private IAVFSCache mEncryptedSQLiteCache;
    private IAVFSCache mFileCache;
    private final String mModuleName;
    private IAVFSCache mSQLiteCache;
    private Set<String> sLSMWhiteList;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public AVFSCache(@Nullable File file) {
        this(file == null ? null : file.getName(), file);
    }

    public AVFSCache(@Nullable String str, @Nullable File file) {
        this.sLSMWhiteList = new HashSet();
        this.mConfig = AVFSCacheConfig.newDefaultConfig();
        this.mModuleName = str;
        this.mDir = file;
        if (this.mDir == null) {
            NoOpAVFSCache instance = NoOpAVFSCache.getInstance();
            this.mEncryptedSQLiteCache = instance;
            this.mSQLiteCache = instance;
            this.mFileCache = instance;
        }
    }

    public IAVFSCache getFileCacheV2(long j) {
        return LSMCache.open(this.mModuleName, 16777216, j);
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0092 A[SYNTHETIC, Splitter:B:30:0x0092] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x009a A[SYNTHETIC, Splitter:B:34:0x009a] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00c1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.taobao.alivfssdk.cache.IAVFSCache getFileCache() {
        /*
            r15 = this;
            com.taobao.alivfssdk.cache.IAVFSCache r0 = r15.mFileCache
            if (r0 != 0) goto L_0x00f4
            boolean r0 = sInitialized
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x00aa
            monitor-enter(r15)
            boolean r0 = sInitialized     // Catch:{ all -> 0x00a7 }
            if (r0 != 0) goto L_0x00a3
            com.taobao.orange.OrangeConfig r0 = com.taobao.orange.OrangeConfig.getInstance()     // Catch:{ all -> 0x00a7 }
            java.lang.String r3 = "ali_database_es"
            java.lang.String r4 = "lsm_white_list"
            java.lang.String r5 = "false"
            r0.getConfig(r3, r4, r5)     // Catch:{ all -> 0x00a7 }
            com.taobao.orange.OrangeConfig r0 = com.taobao.orange.OrangeConfig.getInstance()     // Catch:{ all -> 0x00a7 }
            java.lang.String[] r3 = new java.lang.String[r1]     // Catch:{ all -> 0x00a7 }
            java.lang.String r4 = "ali_database_es"
            r3[r2] = r4     // Catch:{ all -> 0x00a7 }
            com.taobao.alivfssdk.cache.AVFSCache$AvfsOrangeListener r4 = new com.taobao.alivfssdk.cache.AVFSCache$AvfsOrangeListener     // Catch:{ all -> 0x00a7 }
            r5 = 0
            r4.<init>()     // Catch:{ all -> 0x00a7 }
            r0.registerListener(r3, r4, r2)     // Catch:{ all -> 0x00a7 }
            android.app.Application r0 = com.taobao.alivfsadapter.utils.AVFSApplicationUtils.getApplication()     // Catch:{ all -> 0x00a7 }
            java.io.File r3 = new java.io.File     // Catch:{ all -> 0x00a7 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a7 }
            r4.<init>()     // Catch:{ all -> 0x00a7 }
            java.io.File r0 = r0.getFilesDir()     // Catch:{ all -> 0x00a7 }
            java.lang.String r0 = r0.getAbsolutePath()     // Catch:{ all -> 0x00a7 }
            r4.append(r0)     // Catch:{ all -> 0x00a7 }
            java.lang.String r0 = java.io.File.pathSeparator     // Catch:{ all -> 0x00a7 }
            r4.append(r0)     // Catch:{ all -> 0x00a7 }
            java.lang.String r0 = "alivfs.cfg"
            r4.append(r0)     // Catch:{ all -> 0x00a7 }
            java.lang.String r0 = r4.toString()     // Catch:{ all -> 0x00a7 }
            r3.<init>(r0)     // Catch:{ all -> 0x00a7 }
            boolean r0 = r3.exists()     // Catch:{ all -> 0x00a7 }
            if (r0 == 0) goto L_0x00a3
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ IOException -> 0x008c }
            java.io.FileReader r4 = new java.io.FileReader     // Catch:{ IOException -> 0x008c }
            r4.<init>(r3)     // Catch:{ IOException -> 0x008c }
            r0.<init>(r4)     // Catch:{ IOException -> 0x008c }
            java.lang.String r3 = r0.readLine()     // Catch:{ IOException -> 0x0086, all -> 0x0084 }
            if (r3 == 0) goto L_0x007b
            java.lang.String r4 = ","
            java.lang.String[] r3 = r3.split(r4)     // Catch:{ IOException -> 0x0086, all -> 0x0084 }
            java.util.Set<java.lang.String> r4 = r15.sLSMWhiteList     // Catch:{ IOException -> 0x0086, all -> 0x0084 }
            java.util.List r3 = java.util.Arrays.asList(r3)     // Catch:{ IOException -> 0x0086, all -> 0x0084 }
            r4.addAll(r3)     // Catch:{ IOException -> 0x0086, all -> 0x0084 }
        L_0x007b:
            r0.close()     // Catch:{ IOException -> 0x007f }
            goto L_0x00a3
        L_0x007f:
            r0 = move-exception
        L_0x0080:
            r0.printStackTrace()     // Catch:{ all -> 0x00a7 }
            goto L_0x00a3
        L_0x0084:
            r1 = move-exception
            goto L_0x0098
        L_0x0086:
            r3 = move-exception
            r5 = r0
            goto L_0x008d
        L_0x0089:
            r1 = move-exception
            r0 = r5
            goto L_0x0098
        L_0x008c:
            r3 = move-exception
        L_0x008d:
            r3.printStackTrace()     // Catch:{ all -> 0x0089 }
            if (r5 == 0) goto L_0x00a3
            r5.close()     // Catch:{ IOException -> 0x0096 }
            goto L_0x00a3
        L_0x0096:
            r0 = move-exception
            goto L_0x0080
        L_0x0098:
            if (r0 == 0) goto L_0x00a2
            r0.close()     // Catch:{ IOException -> 0x009e }
            goto L_0x00a2
        L_0x009e:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x00a7 }
        L_0x00a2:
            throw r1     // Catch:{ all -> 0x00a7 }
        L_0x00a3:
            sInitialized = r1     // Catch:{ all -> 0x00a7 }
            monitor-exit(r15)     // Catch:{ all -> 0x00a7 }
            goto L_0x00aa
        L_0x00a7:
            r0 = move-exception
            monitor-exit(r15)     // Catch:{ all -> 0x00a7 }
            throw r0
        L_0x00aa:
            java.util.Set<java.lang.String> r0 = r15.sLSMWhiteList
            java.lang.String r3 = r15.mModuleName
            boolean r0 = r0.contains(r3)
            if (r0 == 0) goto L_0x00c1
            java.lang.String r0 = r15.mModuleName
            r1 = 16777216(0x1000000, float:2.3509887E-38)
            r2 = 21600(0x5460, double:1.0672E-319)
            com.taobao.alivfssdk.cache.LSMCache r0 = com.taobao.alivfssdk.cache.LSMCache.open(r0, r1, r2)
            r15.mFileCache = r0
            goto L_0x00f4
        L_0x00c1:
            r5 = 0
            com.taobao.alivfssdk.cache.AVFSCacheConfig r0 = r15.mConfig
            java.lang.Long r0 = r0.limitSize
            long r7 = r0.longValue()
            com.taobao.alivfssdk.fresco.cache.disk.DiskStorageCache$Params r13 = new com.taobao.alivfssdk.fresco.cache.disk.DiskStorageCache$Params
            long r3 = (long) r2
            r2 = r13
            r2.<init>(r3, r5, r7)
            com.taobao.alivfssdk.fresco.cache.disk.DefaultDiskStorage r12 = new com.taobao.alivfssdk.fresco.cache.disk.DefaultDiskStorage
            java.io.File r0 = new java.io.File
            java.io.File r2 = r15.mDir
            java.lang.String r3 = "files"
            r0.<init>(r2, r3)
            com.taobao.alivfssdk.fresco.cache.common.NoOpCacheErrorLogger r2 = com.taobao.alivfssdk.fresco.cache.common.NoOpCacheErrorLogger.getInstance()
            r12.<init>(r0, r1, r2)
            com.taobao.alivfssdk.cache.AVFSCacheConfig r0 = r15.mConfig
            long r0 = r0.fileMemMaxSize
            int r14 = (int) r0
            com.taobao.alivfssdk.cache.AVFSDiskCache r0 = new com.taobao.alivfssdk.cache.AVFSDiskCache
            java.lang.String r11 = "file"
            r9 = r0
            r10 = r15
            r9.<init>(r10, r11, r12, r13, r14)
            r15.mFileCache = r0
        L_0x00f4:
            com.taobao.alivfssdk.cache.IAVFSCache r0 = r15.mFileCache
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.alivfssdk.cache.AVFSCache.getFileCache():com.taobao.alivfssdk.cache.IAVFSCache");
    }

    public IAVFSCache getSQLiteCache() {
        return getSQLiteCache(false);
    }

    public IAVFSCache getSQLiteCache(boolean z) {
        if (z) {
            if (this.mEncryptedSQLiteCache == null) {
                this.mEncryptedSQLiteCache = createSQLiteCache(z);
            }
            return this.mEncryptedSQLiteCache;
        }
        if (this.mSQLiteCache == null) {
            this.mSQLiteCache = createSQLiteCache(z);
        }
        return this.mSQLiteCache;
    }

    private IAVFSCache createSQLiteCache(boolean z) {
        return new AVFSDiskCache(this, MonitorCacheEvent.CACHE_SQL, new SQLiteDefaultDiskStorage(this.mDir, 1, z, NoOpCacheErrorLogger.getInstance()), new DiskStorageCache.Params((long) 0, 0, this.mConfig.limitSize.longValue()), (int) this.mConfig.sqliteMemMaxSize);
    }

    public AVFSCache moduleConfig(AVFSCacheConfig aVFSCacheConfig) {
        this.mConfig.setConfig(aVFSCacheConfig);
        return this;
    }

    public ClassLoader getClassLoader() {
        return this.mClassLoader;
    }

    public String getModuleName() {
        return this.mModuleName;
    }

    public File getDir() {
        return this.mDir;
    }

    public AVFSCache setClassLoader(ClassLoader classLoader) {
        this.mClassLoader = classLoader;
        return this;
    }

    public void clearAll() {
        try {
            close();
        } catch (IOException e) {
            AVFSCacheLog.e(TAG, e, new Object[0]);
        }
        if (this.mDir != null) {
            FileTree.deleteContents(this.mDir);
        }
    }

    public void close() throws IOException {
        if (this.mFileCache != null) {
            this.mFileCache.close();
            this.mFileCache = null;
        }
        if (this.mSQLiteCache != null) {
            this.mSQLiteCache.close();
            this.mSQLiteCache = null;
        }
        if (this.mEncryptedSQLiteCache != null) {
            this.mEncryptedSQLiteCache.close();
            this.mEncryptedSQLiteCache = null;
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
    }

    private static class AvfsOrangeListener implements OConfigListener {
        private AvfsOrangeListener() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:22:0x005b A[SYNTHETIC, Splitter:B:22:0x005b] */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x0066 A[SYNTHETIC, Splitter:B:27:0x0066] */
        /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onConfigUpdate(java.lang.String r4, java.util.Map<java.lang.String, java.lang.String> r5) {
            /*
                r3 = this;
                java.lang.String r5 = "ali_database_es"
                boolean r4 = r5.equals(r4)
                if (r4 == 0) goto L_0x006f
                com.taobao.orange.OrangeConfig r4 = com.taobao.orange.OrangeConfig.getInstance()
                java.lang.String r5 = "ali_database_es"
                java.lang.String r0 = "lsm_white_list"
                java.lang.String r1 = ""
                java.lang.String r4 = r4.getConfig(r5, r0, r1)
                if (r4 == 0) goto L_0x006f
                int r5 = r4.length()
                if (r5 <= 0) goto L_0x006f
                android.app.Application r5 = com.taobao.alivfsadapter.utils.AVFSApplicationUtils.getApplication()
                r0 = 0
                java.io.FileWriter r1 = new java.io.FileWriter     // Catch:{ IOException -> 0x0055 }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0055 }
                r2.<init>()     // Catch:{ IOException -> 0x0055 }
                java.io.File r5 = r5.getFilesDir()     // Catch:{ IOException -> 0x0055 }
                java.lang.String r5 = r5.getAbsolutePath()     // Catch:{ IOException -> 0x0055 }
                r2.append(r5)     // Catch:{ IOException -> 0x0055 }
                java.lang.String r5 = java.io.File.pathSeparator     // Catch:{ IOException -> 0x0055 }
                r2.append(r5)     // Catch:{ IOException -> 0x0055 }
                java.lang.String r5 = "alivfs.cfg"
                r2.append(r5)     // Catch:{ IOException -> 0x0055 }
                java.lang.String r5 = r2.toString()     // Catch:{ IOException -> 0x0055 }
                r1.<init>(r5)     // Catch:{ IOException -> 0x0055 }
                r1.write(r4)     // Catch:{ IOException -> 0x0050, all -> 0x004d }
                r1.close()     // Catch:{ IOException -> 0x005f }
                goto L_0x006f
            L_0x004d:
                r4 = move-exception
                r0 = r1
                goto L_0x0064
            L_0x0050:
                r4 = move-exception
                r0 = r1
                goto L_0x0056
            L_0x0053:
                r4 = move-exception
                goto L_0x0064
            L_0x0055:
                r4 = move-exception
            L_0x0056:
                r4.printStackTrace()     // Catch:{ all -> 0x0053 }
                if (r0 == 0) goto L_0x006f
                r0.close()     // Catch:{ IOException -> 0x005f }
                goto L_0x006f
            L_0x005f:
                r4 = move-exception
                r4.printStackTrace()
                goto L_0x006f
            L_0x0064:
                if (r0 == 0) goto L_0x006e
                r0.close()     // Catch:{ IOException -> 0x006a }
                goto L_0x006e
            L_0x006a:
                r5 = move-exception
                r5.printStackTrace()
            L_0x006e:
                throw r4
            L_0x006f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.alivfssdk.cache.AVFSCache.AvfsOrangeListener.onConfigUpdate(java.lang.String, java.util.Map):void");
        }
    }
}
