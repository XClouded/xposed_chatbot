package com.taobao.android.ultron.datamodel.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.android.ultron.datamodel.cache.db.FileCache;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemplateCache {
    private static final int BUFFER_SIZE = 8192;
    private static final int MAX_BUFFER_SIZE = 2147483639;
    private static final String TAG = "TemplateCache";
    protected final Context context;
    protected final String dbName;
    protected FileCache fileCache;
    protected final long fileCapacity;
    protected final int memCacheSize;
    protected final File rootDir;
    protected final String rootDirName;
    protected final boolean useTemplateIdAsFileName;

    private TemplateCache(Builder builder) {
        this.rootDirName = builder.rootDirName;
        this.context = builder.context;
        this.dbName = builder.dbName;
        this.memCacheSize = builder.memCacheSize;
        this.fileCapacity = builder.fileCapacity;
        this.useTemplateIdAsFileName = builder.useTemplateIdAsFileName;
        this.rootDir = createRootDir();
        this.fileCache = new FileCache(this.context, this.rootDir, this.dbName, this.fileCapacity);
    }

    public List<String> getTemplateIds() {
        ArrayList arrayList = new ArrayList();
        List<FileCache.CacheEntry> all = this.fileCache.getAll();
        if (!(all == null || all.size() == 0)) {
            for (FileCache.CacheEntry cacheEntry : all) {
                arrayList.add(cacheEntry.tag);
            }
        }
        return arrayList;
    }

    public synchronized void delTemplateById(String str) {
        this.fileCache.del(str);
    }

    public byte[] fetchTemplate(String str) {
        FileCache.CacheEntry lookup = this.fileCache.lookup(str);
        if (lookup == null) {
            return null;
        }
        byte[] readTemplateFromFile = readTemplateFromFile(lookup.cacheFile);
        if (readTemplateFromFile == null || readTemplateFromFile.length <= 0) {
            return readTemplateFromFile;
        }
        Log.d(TAG, "[getTemplateById #" + str + "] get template from file.");
        return readTemplateFromFile;
    }

    /* access modifiers changed from: protected */
    public byte[] readTemplateFromFile(File file) {
        try {
            return readAllBytes(file);
        } catch (IOException e) {
            Log.e(TAG, "Read all bytes exception:", e);
            return null;
        }
    }

    private byte[] readAllBytes(File file) throws IOException {
        int read;
        long length = file.length();
        if (length <= 2147483639) {
            FileInputStream fileInputStream = new FileInputStream(file);
            int i = (int) length;
            byte[] bArr = new byte[i];
            int i2 = 0;
            while (true) {
                int read2 = fileInputStream.read(bArr, i2, i - i2);
                if (read2 > 0) {
                    i2 += read2;
                } else if (read2 < 0 || (read = fileInputStream.read()) < 0) {
                    fileInputStream.close();
                } else {
                    if (i <= MAX_BUFFER_SIZE - i) {
                        i = Math.max(i << 1, 8192);
                    } else if (i != MAX_BUFFER_SIZE) {
                        i = MAX_BUFFER_SIZE;
                    } else {
                        throw new OutOfMemoryError("Required array size too large");
                    }
                    bArr = Arrays.copyOf(bArr, i);
                    bArr[i2] = (byte) read;
                    i2++;
                }
            }
            fileInputStream.close();
            if (i == i2) {
                return bArr;
            }
            return Arrays.copyOf(bArr, i2);
        }
        throw new OutOfMemoryError("Required array size too large");
    }

    public void saveTemplate(final String str, final byte[] bArr) {
        if (bArr == null) {
            Log.d(TAG, "[getTemplateById #" + str + "] template from server is null.");
            return;
        }
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            /* JADX WARNING: Code restructure failed: missing block: B:10:0x002b, code lost:
                if (r1.length() <= 0) goto L_0x003f;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
                r7.this$0.fileCache.store(r3, r1);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:13:0x0037, code lost:
                r8 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:14:0x0038, code lost:
                android.util.Log.e(com.taobao.android.ultron.datamodel.cache.TemplateCache.TAG, "File cache store exception", r8);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:8:0x0021, code lost:
                if (r1.isFile() == false) goto L_0x003f;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public java.lang.Void doInBackground(java.lang.Void... r8) {
                /*
                    r7 = this;
                    java.lang.String r8 = r3
                    java.lang.Class<com.taobao.android.ultron.datamodel.cache.TemplateCache> r0 = com.taobao.android.ultron.datamodel.cache.TemplateCache.class
                    monitor-enter(r0)
                    java.io.File r1 = new java.io.File     // Catch:{ all -> 0x0042 }
                    com.taobao.android.ultron.datamodel.cache.TemplateCache r2 = com.taobao.android.ultron.datamodel.cache.TemplateCache.this     // Catch:{ all -> 0x0042 }
                    java.io.File r2 = r2.rootDir     // Catch:{ all -> 0x0042 }
                    r1.<init>(r2, r8)     // Catch:{ all -> 0x0042 }
                    boolean r8 = r1.exists()     // Catch:{ all -> 0x0042 }
                    r2 = 0
                    if (r8 != 0) goto L_0x0040
                    com.taobao.android.ultron.datamodel.cache.TemplateCache r8 = com.taobao.android.ultron.datamodel.cache.TemplateCache.this     // Catch:{ all -> 0x0042 }
                    byte[] r3 = r4     // Catch:{ all -> 0x0042 }
                    java.io.File unused = r8.writeTemplateToFile(r3, r1)     // Catch:{ all -> 0x0042 }
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    boolean r8 = r1.isFile()
                    if (r8 == 0) goto L_0x003f
                    long r3 = r1.length()
                    r5 = 0
                    int r8 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                    if (r8 <= 0) goto L_0x003f
                    com.taobao.android.ultron.datamodel.cache.TemplateCache r8 = com.taobao.android.ultron.datamodel.cache.TemplateCache.this     // Catch:{ Throwable -> 0x0037 }
                    com.taobao.android.ultron.datamodel.cache.db.FileCache r8 = r8.fileCache     // Catch:{ Throwable -> 0x0037 }
                    java.lang.String r0 = r3     // Catch:{ Throwable -> 0x0037 }
                    r8.store(r0, r1)     // Catch:{ Throwable -> 0x0037 }
                    goto L_0x003f
                L_0x0037:
                    r8 = move-exception
                    java.lang.String r0 = "TemplateCache"
                    java.lang.String r1 = "File cache store exception"
                    android.util.Log.e(r0, r1, r8)
                L_0x003f:
                    return r2
                L_0x0040:
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    return r2
                L_0x0042:
                    r8 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x0042 }
                    throw r8
                */
                throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.ultron.datamodel.cache.TemplateCache.AnonymousClass1.doInBackground(java.lang.Void[]):java.lang.Void");
            }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0026, code lost:
        if (r1 == null) goto L_0x0029;
     */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0019 A[SYNTHETIC, Splitter:B:12:0x0019] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0022 A[SYNTHETIC, Splitter:B:18:0x0022] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.File writeTemplateToFile(byte[] r4, java.io.File r5) {
        /*
            r3 = this;
            r0 = 0
            java.io.BufferedOutputStream r1 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x0016, all -> 0x0014 }
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0016, all -> 0x0014 }
            r2.<init>(r5)     // Catch:{ Exception -> 0x0016, all -> 0x0014 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x0016, all -> 0x0014 }
            r1.write(r4)     // Catch:{ Exception -> 0x0012 }
        L_0x000e:
            r1.close()     // Catch:{ IOException -> 0x0029 }
            goto L_0x0029
        L_0x0012:
            goto L_0x0017
        L_0x0014:
            r4 = move-exception
            goto L_0x0020
        L_0x0016:
            r1 = r0
        L_0x0017:
            if (r5 == 0) goto L_0x0026
            r5.delete()     // Catch:{ all -> 0x001e }
            r5 = r0
            goto L_0x0026
        L_0x001e:
            r4 = move-exception
            r0 = r1
        L_0x0020:
            if (r0 == 0) goto L_0x0025
            r0.close()     // Catch:{ IOException -> 0x0025 }
        L_0x0025:
            throw r4
        L_0x0026:
            if (r1 == 0) goto L_0x0029
            goto L_0x000e
        L_0x0029:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.ultron.datamodel.cache.TemplateCache.writeTemplateToFile(byte[], java.io.File):java.io.File");
    }

    private File createRootDir() {
        File availableParentDir = getAvailableParentDir();
        if (availableParentDir == null) {
            return null;
        }
        File file = new File(availableParentDir, this.rootDirName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private File getAvailableParentDir() {
        File filesDir = this.context.getFilesDir();
        if (filesDir != null && filesDir.canWrite()) {
            return filesDir;
        }
        File cacheDir = this.context.getCacheDir();
        if (cacheDir != null && cacheDir.canWrite()) {
            return cacheDir;
        }
        if ("mounted".equals(Environment.getExternalStorageState())) {
            try {
                File externalFilesDir = this.context.getExternalFilesDir((String) null);
                if (externalFilesDir != null && externalFilesDir.canWrite()) {
                    return externalFilesDir;
                }
                File externalCacheDir = this.context.getExternalCacheDir();
                if (externalCacheDir == null || !externalCacheDir.canWrite()) {
                    return null;
                }
                return externalCacheDir;
            } catch (Exception e) {
                Log.e(TAG, "get external files dir exception", e);
                return null;
            }
        }
        return null;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public Context context;
        /* access modifiers changed from: private */
        public String dbName;
        /* access modifiers changed from: private */
        public long fileCapacity = 4194304;
        /* access modifiers changed from: private */
        public int memCacheSize = 8;
        /* access modifiers changed from: private */
        public String rootDirName;
        /* access modifiers changed from: private */
        public boolean useTemplateIdAsFileName = true;

        public TemplateCache build() {
            if (this.context != null && !TextUtils.isEmpty(this.rootDirName) && !TextUtils.isEmpty(this.dbName)) {
                return new TemplateCache(this);
            }
            throw new IllegalArgumentException();
        }

        public Builder withContext(Context context2) {
            this.context = context2;
            return this;
        }

        public Builder withRootDirName(String str) {
            this.rootDirName = str;
            return this;
        }

        public Builder withDbName(String str) {
            this.dbName = str;
            return this;
        }

        public Builder withMemCacheSize(int i) {
            this.memCacheSize = i;
            return this;
        }

        public Builder withFileCapacity(long j) {
            this.fileCapacity = j;
            return this;
        }

        public Builder withUseTemplateIdAsFileName(boolean z) {
            this.useTemplateIdAsFileName = z;
            return this;
        }
    }
}
