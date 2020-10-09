package com.taobao.android.dinamic.tempate.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import com.taobao.android.dinamic.DRegisterCenter;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.dinamic.DinamicPerformMonitor;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.log.DinamicLogThread;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.tempate.db.FileCache;
import com.taobao.android.dinamic.view.DinamicError;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class TemplateCache {
    private static final int BUFFER_SIZE = 8192;
    public static HttpLoader DEFAULT_HTTP_LOADER = new HttpLoader() {
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: java.io.InputStream} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.io.ByteArrayOutputStream} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: java.io.ByteArrayOutputStream} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: java.io.ByteArrayOutputStream} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: java.io.InputStream} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v14, resolved type: java.io.InputStream} */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0048, code lost:
            if (r8 == null) goto L_0x0087;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x004a, code lost:
            r8.disconnect();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x0084, code lost:
            if (r8 == null) goto L_0x0087;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x0087, code lost:
            if (r2 == null) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
            return r2.toByteArray();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
            return null;
         */
        /* JADX WARNING: Multi-variable type inference failed */
        /* JADX WARNING: Removed duplicated region for block: B:40:0x0069 A[SYNTHETIC, Splitter:B:40:0x0069] */
        /* JADX WARNING: Removed duplicated region for block: B:43:0x006e A[Catch:{ IOException -> 0x0071 }] */
        /* JADX WARNING: Removed duplicated region for block: B:46:0x0073  */
        /* JADX WARNING: Removed duplicated region for block: B:53:0x007c A[SYNTHETIC, Splitter:B:53:0x007c] */
        /* JADX WARNING: Removed duplicated region for block: B:56:0x0081 A[Catch:{ IOException -> 0x0084 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public byte[] loadUrl(java.lang.String r8) {
            /*
                r7 = this;
                r0 = 0
                java.net.URL r1 = new java.net.URL     // Catch:{ Exception -> 0x0077, all -> 0x0063 }
                r1.<init>(r8)     // Catch:{ Exception -> 0x0077, all -> 0x0063 }
                java.net.URLConnection r8 = r1.openConnection()     // Catch:{ Exception -> 0x0077, all -> 0x0063 }
                java.net.HttpURLConnection r8 = (java.net.HttpURLConnection) r8     // Catch:{ Exception -> 0x0077, all -> 0x0063 }
                r1 = 10000(0x2710, float:1.4013E-41)
                r8.setConnectTimeout(r1)     // Catch:{ Exception -> 0x0061, all -> 0x005d }
                r1 = 12000(0x2ee0, float:1.6816E-41)
                r8.setReadTimeout(r1)     // Catch:{ Exception -> 0x0061, all -> 0x005d }
                r8.connect()     // Catch:{ Exception -> 0x0061, all -> 0x005d }
                int r1 = r8.getResponseCode()     // Catch:{ Exception -> 0x0061, all -> 0x005d }
                r2 = 200(0xc8, float:2.8E-43)
                if (r1 == r2) goto L_0x0027
                if (r8 == 0) goto L_0x0026
                r8.disconnect()
            L_0x0026:
                return r0
            L_0x0027:
                java.io.InputStream r1 = r8.getInputStream()     // Catch:{ Exception -> 0x0061, all -> 0x005d }
                java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x005b, all -> 0x0055 }
                r3 = 4096(0x1000, float:5.74E-42)
                r2.<init>(r3)     // Catch:{ Exception -> 0x005b, all -> 0x0055 }
                byte[] r3 = new byte[r3]     // Catch:{ Exception -> 0x007a, all -> 0x004e }
            L_0x0034:
                int r4 = r1.read(r3)     // Catch:{ Exception -> 0x007a, all -> 0x004e }
                r5 = -1
                if (r4 == r5) goto L_0x0040
                r5 = 0
                r2.write(r3, r5, r4)     // Catch:{ Exception -> 0x007a, all -> 0x004e }
                goto L_0x0034
            L_0x0040:
                r2.close()     // Catch:{ IOException -> 0x0048 }
                if (r1 == 0) goto L_0x0048
                r1.close()     // Catch:{ IOException -> 0x0048 }
            L_0x0048:
                if (r8 == 0) goto L_0x0087
            L_0x004a:
                r8.disconnect()
                goto L_0x0087
            L_0x004e:
                r0 = move-exception
                r6 = r2
                r2 = r8
                r8 = r1
                r1 = r0
                r0 = r6
                goto L_0x0067
            L_0x0055:
                r2 = move-exception
                r6 = r2
                r2 = r8
                r8 = r1
                r1 = r6
                goto L_0x0067
            L_0x005b:
                r2 = r0
                goto L_0x007a
            L_0x005d:
                r1 = move-exception
                r2 = r8
                r8 = r0
                goto L_0x0067
            L_0x0061:
                r1 = r0
                goto L_0x0079
            L_0x0063:
                r8 = move-exception
                r1 = r8
                r8 = r0
                r2 = r8
            L_0x0067:
                if (r0 == 0) goto L_0x006c
                r0.close()     // Catch:{ IOException -> 0x0071 }
            L_0x006c:
                if (r8 == 0) goto L_0x0071
                r8.close()     // Catch:{ IOException -> 0x0071 }
            L_0x0071:
                if (r2 == 0) goto L_0x0076
                r2.disconnect()
            L_0x0076:
                throw r1
            L_0x0077:
                r8 = r0
                r1 = r8
            L_0x0079:
                r2 = r1
            L_0x007a:
                if (r2 == 0) goto L_0x007f
                r2.close()     // Catch:{ IOException -> 0x0084 }
            L_0x007f:
                if (r1 == 0) goto L_0x0084
                r1.close()     // Catch:{ IOException -> 0x0084 }
            L_0x0084:
                if (r8 == 0) goto L_0x0087
                goto L_0x004a
            L_0x0087:
                if (r2 == 0) goto L_0x008d
                byte[] r0 = r2.toByteArray()
            L_0x008d:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.tempate.manager.TemplateCache.AnonymousClass4.loadUrl(java.lang.String):byte[]");
        }
    };
    private static final int MAX_BUFFER_SIZE = 2147483639;
    private static final String TAG = "TemplateCache";
    protected final Context context;
    protected final String dbName;
    protected FileCache fileCache;
    protected final long fileCapacity;
    protected HttpLoader httpLoader;
    protected LruCache<String, byte[]> memCache;
    protected final int memCacheSize;
    protected final File rootDir;
    protected final String rootDirName;
    protected final boolean useTemplateIdAsFileName;

    public interface HttpLoader {
        byte[] loadUrl(String str);
    }

    private TemplateCache(Builder builder) {
        this.httpLoader = DEFAULT_HTTP_LOADER;
        this.rootDirName = builder.rootDirName;
        this.context = builder.context;
        this.dbName = builder.dbName;
        this.memCacheSize = builder.memCacheSize;
        this.fileCapacity = builder.fileCapacity;
        this.useTemplateIdAsFileName = builder.useTemplateIdAsFileName;
        this.rootDir = createRootDir();
        this.memCache = new LruCache<>(this.memCacheSize);
        this.fileCache = new FileCache(this.context, this.rootDir, this.dbName, this.fileCapacity);
    }

    public void updateMemCacheSize(int i) {
        this.memCache = new LruCache<>(i);
    }

    public byte[] getTemplateById(DinamicTemplate dinamicTemplate, String str, String str2, TemplatePerfInfo templatePerfInfo) {
        byte[] fetchTemplateFromMemory = fetchTemplateFromMemory(str, templatePerfInfo);
        if (fetchTemplateFromMemory != null) {
            return fetchTemplateFromMemory;
        }
        try {
            fetchTemplateFromMemory = fetchTemplateFromDisk(str, templatePerfInfo);
        } catch (IOException unused) {
        }
        if (fetchTemplateFromMemory != null) {
            return fetchTemplateFromMemory;
        }
        return fetchTemplateFromRemote(dinamicTemplate, str, str2, templatePerfInfo);
    }

    public synchronized void clearMemCache() {
        this.memCache.evictAll();
    }

    public synchronized void clearFileCache() {
        FileCache.deleteFiles(this.context, this.rootDir, this.dbName);
        this.fileCache = new FileCache(this.context, this.rootDir, this.dbName, this.fileCapacity);
        AppCacheCleaner.deleteCache(this.context);
    }

    /* access modifiers changed from: protected */
    public byte[] fetchTemplateFromMemory(String str, TemplatePerfInfo templatePerfInfo) {
        long currentTimeMillis = System.currentTimeMillis();
        byte[] bArr = this.memCache.get(str);
        long currentTimeMillis2 = System.currentTimeMillis();
        templatePerfInfo.phase = 1;
        templatePerfInfo.memCostTimeMillis = currentTimeMillis2 - currentTimeMillis;
        Log.d(TAG, "[getTemplateById #" + str + "] get template from memory.");
        return bArr;
    }

    /* access modifiers changed from: protected */
    public byte[] fetchTemplateFromDisk(String str, TemplatePerfInfo templatePerfInfo) throws IOException {
        byte[] bArr;
        long currentTimeMillis = System.currentTimeMillis();
        FileCache.CacheEntry lookup = this.fileCache.lookup(str);
        if (lookup != null) {
            bArr = readTemplateFromFile(lookup.cacheFile);
            if (bArr != null && bArr.length > 0) {
                Log.d(TAG, "[getTemplateById #" + str + "] get template from file.");
                this.memCache.put(str, bArr);
            }
        } else {
            bArr = null;
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        templatePerfInfo.phase = 2;
        templatePerfInfo.fileCostTimeMillis = currentTimeMillis2 - currentTimeMillis;
        return bArr;
    }

    private byte[] fetchTemplateFromRemote(DinamicTemplate dinamicTemplate, String str, String str2, TemplatePerfInfo templatePerfInfo) {
        long nanoTime = System.nanoTime();
        byte[] loadUrl = this.httpLoader.loadUrl(str2);
        long nanoTime2 = System.nanoTime();
        templatePerfInfo.phase = 3;
        templatePerfInfo.networkCostTimeMillis = nanoTime2 - nanoTime;
        logDownloadTemplate(templatePerfInfo, dinamicTemplate, loadUrl != null);
        if (loadUrl != null) {
            this.memCache.put(str, loadUrl);
            Log.d(TAG, "[getTemplateById #" + str + "] get template from server.");
            final String str3 = str;
            final String str4 = str2;
            final byte[] bArr = loadUrl;
            final DinamicTemplate dinamicTemplate2 = dinamicTemplate;
            final TemplatePerfInfo templatePerfInfo2 = templatePerfInfo;
            new AsyncTask<Void, Void, Void>() {
                /* access modifiers changed from: protected */
                /* JADX WARNING: Code restructure failed: missing block: B:12:0x003c, code lost:
                    if (r1.isFile() == false) goto L_0x005a;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:14:0x0046, code lost:
                    if (r1.length() <= 0) goto L_0x005a;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
                    r7.this$0.fileCache.store(r5, r1);
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:17:0x0052, code lost:
                    r8 = move-exception;
                 */
                /* JADX WARNING: Code restructure failed: missing block: B:18:0x0053, code lost:
                    android.util.Log.e(com.taobao.android.dinamic.tempate.manager.TemplateCache.TAG, "File cache store exception", r8);
                 */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public java.lang.Void doInBackground(java.lang.Void... r8) {
                    /*
                        r7 = this;
                        com.taobao.android.dinamic.tempate.manager.TemplateCache r8 = com.taobao.android.dinamic.tempate.manager.TemplateCache.this
                        boolean r8 = r8.useTemplateIdAsFileName
                        if (r8 == 0) goto L_0x0009
                        java.lang.String r8 = r5
                        goto L_0x0017
                    L_0x0009:
                        java.lang.String r8 = r6
                        java.lang.String r8 = android.net.Uri.decode(r8)
                        android.net.Uri r8 = android.net.Uri.parse(r8)
                        java.lang.String r8 = r8.getLastPathSegment()
                    L_0x0017:
                        java.lang.Class<com.taobao.android.dinamic.tempate.manager.TemplateCache> r0 = com.taobao.android.dinamic.tempate.manager.TemplateCache.class
                        monitor-enter(r0)
                        java.io.File r1 = new java.io.File     // Catch:{ all -> 0x005d }
                        com.taobao.android.dinamic.tempate.manager.TemplateCache r2 = com.taobao.android.dinamic.tempate.manager.TemplateCache.this     // Catch:{ all -> 0x005d }
                        java.io.File r2 = r2.rootDir     // Catch:{ all -> 0x005d }
                        r1.<init>(r2, r8)     // Catch:{ all -> 0x005d }
                        boolean r8 = r1.exists()     // Catch:{ all -> 0x005d }
                        r2 = 0
                        if (r8 != 0) goto L_0x005b
                        com.taobao.android.dinamic.tempate.manager.TemplateCache r8 = com.taobao.android.dinamic.tempate.manager.TemplateCache.this     // Catch:{ all -> 0x005d }
                        byte[] r3 = r7     // Catch:{ all -> 0x005d }
                        com.taobao.android.dinamic.tempate.DinamicTemplate r4 = r8     // Catch:{ all -> 0x005d }
                        com.taobao.android.dinamic.tempate.manager.TemplatePerfInfo r5 = r9     // Catch:{ all -> 0x005d }
                        java.lang.String r5 = r5.module     // Catch:{ all -> 0x005d }
                        java.io.File unused = r8.writeTemplateToFile(r3, r1, r4, r5)     // Catch:{ all -> 0x005d }
                        monitor-exit(r0)     // Catch:{ all -> 0x005d }
                        boolean r8 = r1.isFile()
                        if (r8 == 0) goto L_0x005a
                        long r3 = r1.length()
                        r5 = 0
                        int r8 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                        if (r8 <= 0) goto L_0x005a
                        com.taobao.android.dinamic.tempate.manager.TemplateCache r8 = com.taobao.android.dinamic.tempate.manager.TemplateCache.this     // Catch:{ Throwable -> 0x0052 }
                        com.taobao.android.dinamic.tempate.db.FileCache r8 = r8.fileCache     // Catch:{ Throwable -> 0x0052 }
                        java.lang.String r0 = r5     // Catch:{ Throwable -> 0x0052 }
                        r8.store(r0, r1)     // Catch:{ Throwable -> 0x0052 }
                        goto L_0x005a
                    L_0x0052:
                        r8 = move-exception
                        java.lang.String r0 = "TemplateCache"
                        java.lang.String r1 = "File cache store exception"
                        android.util.Log.e(r0, r1, r8)
                    L_0x005a:
                        return r2
                    L_0x005b:
                        monitor-exit(r0)     // Catch:{ all -> 0x005d }
                        return r2
                    L_0x005d:
                        r8 = move-exception
                        monitor-exit(r0)     // Catch:{ all -> 0x005d }
                        throw r8
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.tempate.manager.TemplateCache.AnonymousClass1.doInBackground(java.lang.Void[]):java.lang.Void");
                }
            }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
            return loadUrl;
        }
        Log.d(TAG, "[getTemplateById #" + str + "] template from server is null.");
        return null;
    }

    private void logDownloadTemplate(final TemplatePerfInfo templatePerfInfo, final DinamicTemplate dinamicTemplate, final boolean z) {
        if (DRegisterCenter.shareCenter().getPerformMonitor() != null && DinamicLogThread.checkInit()) {
            DinamicLogThread.threadHandler.postTask(new Runnable() {
                public void run() {
                    if (Dinamic.isDebugable()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("download template=");
                        sb.append(dinamicTemplate);
                        double d = (double) ((float) templatePerfInfo.networkCostTimeMillis);
                        Double.isNaN(d);
                        sb.append(d / 1000000.0d);
                        DinamicLog.d(Dinamic.TAG, sb.toString());
                    }
                    DinamicPerformMonitor performMonitor = DRegisterCenter.shareCenter().getPerformMonitor();
                    String str = templatePerfInfo.module;
                    DinamicTemplate dinamicTemplate = dinamicTemplate;
                    boolean z = z;
                    double d2 = (double) templatePerfInfo.networkCostTimeMillis;
                    Double.isNaN(d2);
                    performMonitor.trackDownloadTemplate(str, dinamicTemplate, z, (DinamicError) null, d2 / 1000000.0d);
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public byte[] readTemplateFromFile(File file) throws IOException {
        return readAllBytes(file);
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

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0041, code lost:
        if (r3 != null) goto L_0x0020;
     */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x002c A[SYNTHETIC, Splitter:B:13:0x002c] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0047 A[SYNTHETIC, Splitter:B:22:0x0047] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.io.File writeTemplateToFile(byte[] r12, java.io.File r13, com.taobao.android.dinamic.tempate.DinamicTemplate r14, java.lang.String r15) {
        /*
            r11 = this;
            long r0 = java.lang.System.nanoTime()
            r2 = 0
            java.io.BufferedOutputStream r3 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x0029, all -> 0x0026 }
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0029, all -> 0x0026 }
            r4.<init>(r13)     // Catch:{ Exception -> 0x0029, all -> 0x0026 }
            r3.<init>(r4)     // Catch:{ Exception -> 0x0029, all -> 0x0026 }
            r3.write(r12)     // Catch:{ Exception -> 0x0024 }
            r8 = 1
            long r4 = java.lang.System.nanoTime()     // Catch:{ Exception -> 0x0024 }
            r12 = 0
            long r9 = r4 - r0
            r5 = r11
            r6 = r15
            r7 = r14
            r5.logWriteTemplate(r6, r7, r8, r9)     // Catch:{ Exception -> 0x0024 }
        L_0x0020:
            r3.close()     // Catch:{ IOException -> 0x0044 }
            goto L_0x0044
        L_0x0024:
            goto L_0x002a
        L_0x0026:
            r12 = move-exception
            r3 = r2
            goto L_0x0045
        L_0x0029:
            r3 = r2
        L_0x002a:
            if (r13 == 0) goto L_0x0033
            r13.delete()     // Catch:{ all -> 0x0031 }
            r13 = r2
            goto L_0x0033
        L_0x0031:
            r12 = move-exception
            goto L_0x0045
        L_0x0033:
            r7 = 0
            long r4 = java.lang.System.nanoTime()     // Catch:{ all -> 0x0031 }
            r12 = 0
            long r8 = r4 - r0
            r4 = r11
            r5 = r15
            r6 = r14
            r4.logWriteTemplate(r5, r6, r7, r8)     // Catch:{ all -> 0x0031 }
            if (r3 == 0) goto L_0x0044
            goto L_0x0020
        L_0x0044:
            return r13
        L_0x0045:
            if (r3 == 0) goto L_0x004a
            r3.close()     // Catch:{ IOException -> 0x004a }
        L_0x004a:
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.tempate.manager.TemplateCache.writeTemplateToFile(byte[], java.io.File, com.taobao.android.dinamic.tempate.DinamicTemplate, java.lang.String):java.io.File");
    }

    private void logWriteTemplate(String str, DinamicTemplate dinamicTemplate, boolean z, long j) {
        if (DRegisterCenter.shareCenter().getPerformMonitor() != null && DinamicLogThread.checkInit()) {
            final DinamicTemplate dinamicTemplate2 = dinamicTemplate;
            final long j2 = j;
            final String str2 = str;
            final boolean z2 = z;
            DinamicLogThread.threadHandler.postTask(new Runnable() {
                public void run() {
                    if (Dinamic.isDebugable()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("write template=");
                        sb.append(dinamicTemplate2);
                        double d = (double) ((float) j2);
                        Double.isNaN(d);
                        sb.append(d / 1000000.0d);
                        DinamicLog.d(Dinamic.TAG, sb.toString());
                    }
                    DinamicPerformMonitor performMonitor = DRegisterCenter.shareCenter().getPerformMonitor();
                    String str = str2;
                    DinamicTemplate dinamicTemplate = dinamicTemplate2;
                    boolean z = z2;
                    double d2 = (double) j2;
                    Double.isNaN(d2);
                    performMonitor.trackWriteTemplate(str, dinamicTemplate, z, (DinamicError) null, d2 / 1000000.0d);
                }
            });
        }
    }

    private File createRootDir() {
        File availableParentDir;
        if (this.context == null || (availableParentDir = getAvailableParentDir()) == null) {
            return null;
        }
        File file = new File(availableParentDir, this.rootDirName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getRootDir() {
        return this.rootDir;
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

    private static class AppCacheCleaner {
        private AppCacheCleaner() {
        }

        public static void deleteCache(Context context) {
            try {
                File cacheDir = context.getCacheDir();
                if (cacheDir != null && cacheDir.isDirectory()) {
                    deleteDir(cacheDir);
                }
            } catch (Exception unused) {
            }
        }

        private static boolean deleteDir(File file) {
            String[] list;
            if (!(file == null || !file.isDirectory() || (list = file.list()) == null)) {
                for (String file2 : list) {
                    if (!deleteDir(new File(file, file2))) {
                        return false;
                    }
                }
            }
            if (file == null || !file.delete()) {
                return false;
            }
            return true;
        }
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
            if (!TextUtils.isEmpty(this.rootDirName) && !TextUtils.isEmpty(this.dbName)) {
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
