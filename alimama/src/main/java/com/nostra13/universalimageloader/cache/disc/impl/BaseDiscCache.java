package com.nostra13.universalimageloader.cache.disc.impl;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.utils.IoUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class BaseDiscCache implements DiskCache {
    public static final int DEFAULT_BUFFER_SIZE = 32768;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    public static final int DEFAULT_COMPRESS_QUALITY = 100;
    private static final String ERROR_ARG_NULL = " argument must be not null";
    private static final String TEMP_IMAGE_POSTFIX = ".tmp";
    protected int bufferSize;
    protected final File cacheDir;
    protected Bitmap.CompressFormat compressFormat;
    protected int compressQuality;
    protected final FileNameGenerator fileNameGenerator;
    protected final File reserveCacheDir;

    public void close() {
    }

    public BaseDiscCache(File file) {
        this(file, (File) null);
    }

    public BaseDiscCache(File file, File file2) {
        this(file, file2, DefaultConfigurationFactory.createFileNameGenerator());
    }

    public BaseDiscCache(File file, File file2, FileNameGenerator fileNameGenerator2) {
        this.bufferSize = 32768;
        this.compressFormat = DEFAULT_COMPRESS_FORMAT;
        this.compressQuality = 100;
        if (file == null) {
            throw new IllegalArgumentException("cacheDir argument must be not null");
        } else if (fileNameGenerator2 != null) {
            this.cacheDir = file;
            this.reserveCacheDir = file2;
            this.fileNameGenerator = fileNameGenerator2;
        } else {
            throw new IllegalArgumentException("fileNameGenerator argument must be not null");
        }
    }

    public File getDirectory() {
        return this.cacheDir;
    }

    public File get(String str) {
        return getFile(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x005d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean save(java.lang.String r6, java.io.InputStream r7, com.nostra13.universalimageloader.utils.IoUtils.CopyListener r8) throws java.io.IOException {
        /*
            r5 = this;
            java.io.File r6 = r5.getFile(r6)
            java.io.File r0 = new java.io.File
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = r6.getAbsolutePath()
            r1.append(r2)
            java.lang.String r2 = ".tmp"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            r1 = 0
            java.io.BufferedOutputStream r2 = new java.io.BufferedOutputStream     // Catch:{ all -> 0x004d }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ all -> 0x004d }
            r3.<init>(r0)     // Catch:{ all -> 0x004d }
            int r4 = r5.bufferSize     // Catch:{ all -> 0x004d }
            r2.<init>(r3, r4)     // Catch:{ all -> 0x004d }
            int r3 = r5.bufferSize     // Catch:{ all -> 0x0048 }
            boolean r8 = com.nostra13.universalimageloader.utils.IoUtils.copyStream(r7, r2, r8, r3)     // Catch:{ all -> 0x0048 }
            com.nostra13.universalimageloader.utils.IoUtils.closeSilently(r2)     // Catch:{ all -> 0x0046 }
            com.nostra13.universalimageloader.utils.IoUtils.closeSilently(r7)
            if (r8 == 0) goto L_0x0040
            boolean r6 = r0.renameTo(r6)
            if (r6 != 0) goto L_0x0040
            r8 = 0
        L_0x0040:
            if (r8 != 0) goto L_0x0045
            r0.delete()
        L_0x0045:
            return r8
        L_0x0046:
            r2 = move-exception
            goto L_0x004f
        L_0x0048:
            r8 = move-exception
            com.nostra13.universalimageloader.utils.IoUtils.closeSilently(r2)     // Catch:{ all -> 0x004d }
            throw r8     // Catch:{ all -> 0x004d }
        L_0x004d:
            r2 = move-exception
            r8 = 0
        L_0x004f:
            com.nostra13.universalimageloader.utils.IoUtils.closeSilently(r7)
            if (r8 == 0) goto L_0x005b
            boolean r6 = r0.renameTo(r6)
            if (r6 != 0) goto L_0x005b
            r8 = 0
        L_0x005b:
            if (r8 != 0) goto L_0x0060
            r0.delete()
        L_0x0060:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.cache.disc.impl.BaseDiscCache.save(java.lang.String, java.io.InputStream, com.nostra13.universalimageloader.utils.IoUtils$CopyListener):boolean");
    }

    public boolean save(String str, Bitmap bitmap) throws IOException {
        File file = getFile(str);
        File file2 = new File(file.getAbsolutePath() + ".tmp");
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2), this.bufferSize);
        try {
            boolean compress = bitmap.compress(this.compressFormat, this.compressQuality, bufferedOutputStream);
            IoUtils.closeSilently(bufferedOutputStream);
            if (compress && !file2.renameTo(file)) {
                compress = false;
            }
            if (!compress) {
                file2.delete();
            }
            bitmap.recycle();
            return compress;
        } catch (Throwable th) {
            IoUtils.closeSilently(bufferedOutputStream);
            file2.delete();
            throw th;
        }
    }

    public boolean remove(String str) {
        return getFile(str).delete();
    }

    public void clear() {
        File[] listFiles = this.cacheDir.listFiles();
        if (listFiles != null) {
            for (File delete : listFiles) {
                delete.delete();
            }
        }
    }

    /* access modifiers changed from: protected */
    public File getFile(String str) {
        String generate = this.fileNameGenerator.generate(str);
        File file = this.cacheDir;
        if (!this.cacheDir.exists() && !this.cacheDir.mkdirs() && this.reserveCacheDir != null && (this.reserveCacheDir.exists() || this.reserveCacheDir.mkdirs())) {
            file = this.reserveCacheDir;
        }
        return new File(file, generate);
    }

    public void setBufferSize(int i) {
        this.bufferSize = i;
    }

    public void setCompressFormat(Bitmap.CompressFormat compressFormat2) {
        this.compressFormat = compressFormat2;
    }

    public void setCompressQuality(int i) {
        this.compressQuality = i;
    }
}
