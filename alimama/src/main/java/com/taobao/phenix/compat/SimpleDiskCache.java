package com.taobao.phenix.compat;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import com.taobao.fresco.disk.cache.DiskStorageCache;
import com.taobao.fresco.disk.cache.FileCache;
import com.taobao.fresco.disk.common.BinaryResource;
import com.taobao.fresco.disk.common.WriterCallback;
import com.taobao.fresco.disk.storage.DefaultDiskStorage;
import com.taobao.phenix.cache.disk.DiskCache;
import com.taobao.phenix.common.StreamUtil;
import com.taobao.phenix.entity.ResponseData;
import com.taobao.phenix.intf.Phenix;
import com.taobao.tcommon.log.FLog;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SimpleDiskCache implements DiskCache {
    public static final String DEFAULT_CACHE_IMAGE_DIR = "images";
    public static final String TAG = "NonCatalogDiskCache";
    public static final int VERSION = 1;
    private final String mCacheSubDir;
    private FileCache mFileCache;
    private int mLimitSize;
    private final int mPriority;

    public boolean close() {
        return true;
    }

    public int[] getCatalogs(String str) {
        return null;
    }

    public long getLength(String str, int i) {
        return -1;
    }

    public boolean isSupportCatalogs() {
        return false;
    }

    public SimpleDiskCache(int i) {
        this(i, DEFAULT_CACHE_IMAGE_DIR);
    }

    public SimpleDiskCache(int i, String str) {
        this.mPriority = i;
        this.mCacheSubDir = str;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public FileCache getFileCache() {
        return this.mFileCache;
    }

    public void clear() {
        this.mFileCache.clearAll();
    }

    public void maxSize(int i) {
        this.mLimitSize = i;
    }

    public boolean open(Context context) {
        if (this.mFileCache == null) {
            this.mFileCache = new DiskStorageCache(DefaultDiskStorage.instance(getCacheRootDir(context), 1), new DiskStorageCache.Params(0, (long) (this.mLimitSize / 2), (long) this.mLimitSize), NoOpCacheEventListener.instance());
        }
        return this.mFileCache.isEnabled();
    }

    private File getCacheRootDir(Context context) {
        File externalCacheDir;
        if (context == null || context.getApplicationContext() == null || !"mounted".equals(Environment.getExternalStorageState()) || (externalCacheDir = context.getApplicationContext().getExternalCacheDir()) == null) {
            return null;
        }
        return new File(externalCacheDir, this.mCacheSubDir);
    }

    private boolean isNotAvailable(String str) {
        return TextUtils.isEmpty(str) || this.mFileCache == null || !this.mFileCache.isEnabled();
    }

    public boolean remove(String str, int i) {
        if (isNotAvailable(str)) {
            return false;
        }
        this.mFileCache.remove(new SimpleCacheKey(str, i));
        return true;
    }

    public ResponseData get(String str, int i) {
        if (isNotAvailable(str)) {
            return null;
        }
        BinaryResource resource = this.mFileCache.getResource(new SimpleCacheKey(str, i));
        if (resource != null) {
            try {
                return new ResponseData(resource.openStream(), (int) resource.size());
            } catch (Exception e) {
                FLog.e(TAG, "read bytes from cache file error:%s", e.getMessage());
            }
        }
        return null;
    }

    public boolean put(String str, int i, final InputStream inputStream) {
        if (isNotAvailable(str)) {
            return false;
        }
        try {
            if (this.mFileCache.insert(new SimpleCacheKey(str, i), new WriterCallback() {
                public void write(OutputStream outputStream) throws IOException {
                    StreamUtil.copy(inputStream, outputStream, Phenix.instance().bytesPoolBuilder().build());
                }
            }) != null) {
                return true;
            }
            return false;
        } catch (IOException unused) {
            return false;
        }
    }

    public boolean put(String str, int i, final byte[] bArr, final int i2, final int i3) {
        if (isNotAvailable(str)) {
            return false;
        }
        try {
            if (this.mFileCache.insert(new SimpleCacheKey(str, i), new WriterCallback() {
                public void write(OutputStream outputStream) throws IOException {
                    outputStream.write(bArr, i2, i3);
                }
            }) != null) {
                return true;
            }
            return false;
        } catch (IOException unused) {
            return false;
        }
    }
}
