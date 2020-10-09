package com.taobao.alivfssdk.cache;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;
import com.taobao.alivfsadapter.AVFSAdapterManager;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.alivfssdk.utils.AVFSCacheLog;
import com.taobao.alivfssdk.utils.IoUtils;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AVFSCacheManager {
    private static final int AVFS_MAX_CACHE_NUMBER = 5;
    private static final String TAG = "AVFSCacheManager";
    private static volatile AVFSCacheManager sInstance;
    private final LruCache<String, AVFSCache> mCaches;
    private final ConcurrentHashMap<String, AVFSCacheConfig> mConfigs = new ConcurrentHashMap<>();
    private final Context mContext;

    public static AVFSCacheManager getInstance() {
        if (sInstance == null) {
            synchronized (AVFSCacheManager.class) {
                if (sInstance == null) {
                    sInstance = new AVFSCacheManager();
                }
            }
        }
        return sInstance;
    }

    AVFSCacheManager() {
        Application application = AVFSAdapterManager.getInstance().getApplication();
        Context applicationContext = application.getApplicationContext();
        if (applicationContext == null) {
            this.mContext = application;
        } else {
            this.mContext = applicationContext;
        }
        this.mCaches = new LruCache<String, AVFSCache>(5) {
            /* access modifiers changed from: protected */
            public void entryRemoved(boolean z, String str, AVFSCache aVFSCache, AVFSCache aVFSCache2) {
            }
        };
    }

    public AVFSCache defaultCache() {
        return cacheForModule(AVFSCacheConstants.AVFS_DEFAULT_MODULE_NAME);
    }

    @Nullable
    public AVFSCache cacheForModule(@NonNull String str) {
        File file;
        if (str != null) {
            try {
                file = getRootDir();
            } catch (IOException e) {
                AVFSCacheLog.e(TAG, e, new Object[0]);
                file = null;
            }
            return createCache(file, str);
        }
        throw new IllegalArgumentException("moduleName cannot be null");
    }

    public void removeCacheForModule(@NonNull String str) {
        if (str != null) {
            synchronized (this.mCaches) {
                AVFSCache remove = this.mCaches.remove(str);
                if (remove != null) {
                    remove.clearAll();
                    return;
                }
                return;
            }
        }
        throw new IllegalArgumentException("moduleName cannot be null");
    }

    @NonNull
    public AVFSCache cacheForModule(@NonNull String str, boolean z) {
        File file;
        if (str != null) {
            try {
                file = getRootDir(z);
            } catch (IOException e) {
                AVFSCacheLog.e(TAG, e, new Object[0]);
                file = null;
            }
            return createCache(file, str);
        }
        throw new IllegalArgumentException("moduleName cannot be null");
    }

    @NonNull
    private AVFSCache createCache(File file, String str) {
        AVFSCache aVFSCache;
        File file2;
        synchronized (this.mCaches) {
            aVFSCache = this.mCaches.get(str);
            if (aVFSCache == null) {
                if (file == null) {
                    file2 = null;
                } else {
                    file2 = new File(file, str);
                }
                aVFSCache = new AVFSCache(str, file2);
                AVFSCacheConfig aVFSCacheConfig = this.mConfigs.get(str);
                if (aVFSCacheConfig != null) {
                    aVFSCache.moduleConfig(aVFSCacheConfig);
                }
                this.mCaches.put(str, aVFSCache);
            }
        }
        return aVFSCache;
    }

    /* access modifiers changed from: package-private */
    public LruCache<String, AVFSCache> getCaches() {
        return this.mCaches;
    }

    public void putConfigs(@NonNull Map<? extends String, ? extends AVFSCacheConfig> map) {
        this.mConfigs.putAll(map);
    }

    public File getRootDir() throws IOException {
        try {
            return getRootDir(true);
        } catch (IOException e) {
            AVFSCacheLog.e(TAG, e, new Object[0]);
            return getRootDir(false);
        }
    }

    public File getRootDir(boolean z) throws IOException {
        if (z) {
            try {
                File externalFilesDir = this.mContext.getExternalFilesDir("AVFSCache");
                if (externalFilesDir != null) {
                    return externalFilesDir;
                }
                throw new IOException("Couldn't create directory AVFSCache");
            } catch (Exception e) {
                throw new IOException(e);
            }
        } else {
            File file = new File(this.mContext.getFilesDir(), "AVFSCache");
            IoUtils.ensureDirectory(file);
            return file;
        }
    }

    public Context getContext() {
        return this.mContext;
    }
}
