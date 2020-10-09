package com.taobao.phenix.compat.alivfs;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.alivfssdk.cache.AVFSCache;
import com.taobao.alivfssdk.cache.AVFSCacheConfig;
import com.taobao.alivfssdk.cache.AVFSCacheManager;
import com.taobao.alivfssdk.cache.IAVFSCache;
import com.taobao.phenix.cache.disk.DiskCache;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.entity.ResponseData;
import com.taobao.tcommon.core.Preconditions;
import com.taobao.weex.el.parse.Operators;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class AlivfsDiskCache implements DiskCache {
    private static final String ALIVFS_IMAGE_PREFIX = "phximgs_";
    private IAVFSCache mAVFSExtCache;
    private volatile int mMaxSize;
    private final String mName;
    private final int mPriority;

    public boolean close() {
        return false;
    }

    public boolean isSupportCatalogs() {
        return true;
    }

    public AlivfsDiskCache(int i, String str) {
        Preconditions.checkArgument(!TextUtils.isEmpty(str), "name cannot be empty when constructing AlivfsDiskCache");
        this.mPriority = i;
        this.mName = ALIVFS_IMAGE_PREFIX + str;
    }

    public int getPriority() {
        return this.mPriority;
    }

    private synchronized boolean ensureAVFSExtCache() {
        AVFSCache cacheForModule;
        if (this.mAVFSExtCache == null && (cacheForModule = AVFSCacheManager.getInstance().cacheForModule(this.mName)) != null) {
            AVFSCacheConfig aVFSCacheConfig = new AVFSCacheConfig();
            aVFSCacheConfig.limitSize = Long.valueOf((long) this.mMaxSize);
            cacheForModule.moduleConfig(aVFSCacheConfig);
            this.mAVFSExtCache = cacheForModule.getFileCache();
        }
        return this.mAVFSExtCache != null;
    }

    public String toString() {
        return "AlivfsDiskCache(" + Integer.toHexString(hashCode()) + ", name@" + this.mName + Operators.BRACKET_END_STR;
    }

    public synchronized void clear() {
        AVFSCacheManager.getInstance().removeCacheForModule(this.mName);
        UnitedLog.i("DiskCache", "remove alivfs cache module(%s)", this.mName);
        this.mAVFSExtCache = null;
    }

    public void clearMemory() {
        if (ensureAVFSExtCache()) {
            this.mAVFSExtCache.clearMemCache();
        }
    }

    public boolean remove(String str, int i) {
        return ensureAVFSExtCache() && this.mAVFSExtCache.removeObjectForKey(str, String.valueOf(i));
    }

    public void maxSize(int i) {
        this.mMaxSize = i;
    }

    public boolean open(Context context) {
        return ensureAVFSExtCache();
    }

    public long getLength(String str, int i) {
        if (!ensureAVFSExtCache()) {
            return -1;
        }
        long lengthForKey = (long) ((int) this.mAVFSExtCache.lengthForKey(str, String.valueOf(i)));
        if (lengthForKey > 0) {
            return lengthForKey;
        }
        return -1;
    }

    public int[] getCatalogs(String str) {
        List<String> extendsKeysForKey;
        if (!ensureAVFSExtCache() || (extendsKeysForKey = this.mAVFSExtCache.extendsKeysForKey(str)) == null || extendsKeysForKey.size() <= 0) {
            return new int[0];
        }
        int[] iArr = new int[extendsKeysForKey.size()];
        for (int i = 0; i < extendsKeysForKey.size(); i++) {
            try {
                iArr[i] = Integer.parseInt(extendsKeysForKey.get(i));
            } catch (NumberFormatException unused) {
            }
        }
        return iArr;
    }

    public ResponseData get(String str, int i) {
        int lengthForKey;
        InputStream inputStreamForKey;
        if (!ensureAVFSExtCache() || (lengthForKey = (int) this.mAVFSExtCache.lengthForKey(str, String.valueOf(i))) <= 0 || (inputStreamForKey = this.mAVFSExtCache.inputStreamForKey(str, String.valueOf(i))) == null) {
            return null;
        }
        return new ResponseData(inputStreamForKey, lengthForKey);
    }

    public boolean put(String str, int i, InputStream inputStream) {
        return ensureAVFSExtCache() && inputStream != null && this.mAVFSExtCache.setStreamForKey(str, String.valueOf(i), inputStream);
    }

    public boolean put(String str, int i, byte[] bArr, int i2, int i3) {
        return put(str, i, (bArr == null || i3 <= 0) ? null : new ByteArrayInputStream(bArr, i2, i3));
    }
}
