package anet.channel.cache;

import android.taobao.windvane.jsbridge.api.WVFile;
import anet.channel.util.ALog;
import anet.channel.util.StringUtils;
import anetwork.channel.cache.Cache;
import com.taobao.alivfssdk.cache.AVFSCache;
import com.taobao.alivfssdk.cache.AVFSCacheConfig;
import com.taobao.alivfssdk.cache.AVFSCacheManager;
import com.taobao.alivfssdk.cache.IAVFSCache;

public class AVFSCacheImpl implements Cache {
    private static final String MODULE_NAME = "networksdk.httpcache";
    private static final String TAG = "anet.AVFSCacheImpl";
    private static boolean isAvfsCacheExist = false;
    private static Object nullAllObjectRemoveCallback;
    private static Object nullObjectRemoveCallback;
    private static Object nullObjectSetCallback;

    static {
        try {
            Class.forName("com.taobao.alivfssdk.cache.AVFSCacheManager");
            nullObjectSetCallback = new IAVFSCache.OnObjectSetCallback() {
                public void onObjectSetCallback(String str, boolean z) {
                }
            };
            nullObjectRemoveCallback = new IAVFSCache.OnObjectRemoveCallback() {
                public void onObjectRemoveCallback(String str, boolean z) {
                }
            };
            nullAllObjectRemoveCallback = new IAVFSCache.OnAllObjectRemoveCallback() {
                public void onAllObjectRemoveCallback(boolean z) {
                }
            };
        } catch (ClassNotFoundException unused) {
            ALog.w(TAG, "no alivfs sdk!", (String) null, new Object[0]);
        }
    }

    public void initialize() {
        AVFSCache cacheForModule;
        if (isAvfsCacheExist && (cacheForModule = AVFSCacheManager.getInstance().cacheForModule(MODULE_NAME)) != null) {
            AVFSCacheConfig aVFSCacheConfig = new AVFSCacheConfig();
            aVFSCacheConfig.limitSize = Long.valueOf(WVFile.FILE_MAX_SIZE);
            aVFSCacheConfig.fileMemMaxSize = 1048576;
            cacheForModule.moduleConfig(aVFSCacheConfig);
        }
    }

    public Cache.Entry get(String str) {
        if (!isAvfsCacheExist) {
            return null;
        }
        try {
            IAVFSCache fileCache = getFileCache();
            if (fileCache != null) {
                return (Cache.Entry) fileCache.objectForKey(StringUtils.md5ToHex(str));
            }
        } catch (Exception e) {
            ALog.e(TAG, "get cache failed", (String) null, e, new Object[0]);
        }
        return null;
    }

    public void put(String str, Cache.Entry entry) {
        if (isAvfsCacheExist) {
            try {
                IAVFSCache fileCache = getFileCache();
                if (fileCache != null) {
                    fileCache.setObjectForKey(StringUtils.md5ToHex(str), (Object) entry, (IAVFSCache.OnObjectSetCallback) nullObjectSetCallback);
                }
            } catch (Exception e) {
                ALog.e(TAG, "put cache failed", (String) null, e, new Object[0]);
            }
        }
    }

    public void remove(String str) {
        if (isAvfsCacheExist) {
            try {
                IAVFSCache fileCache = getFileCache();
                if (fileCache != null) {
                    fileCache.removeObjectForKey(StringUtils.md5ToHex(str), (IAVFSCache.OnObjectRemoveCallback) nullObjectRemoveCallback);
                }
            } catch (Exception e) {
                ALog.e(TAG, "remove cache failed", (String) null, e, new Object[0]);
            }
        }
    }

    public void clear() {
        if (isAvfsCacheExist) {
            try {
                IAVFSCache fileCache = getFileCache();
                if (fileCache != null) {
                    fileCache.removeAllObject((IAVFSCache.OnAllObjectRemoveCallback) nullAllObjectRemoveCallback);
                }
            } catch (Exception e) {
                ALog.e(TAG, "clear cache failed", (String) null, e, new Object[0]);
            }
        }
    }

    private IAVFSCache getFileCache() {
        AVFSCache cacheForModule = AVFSCacheManager.getInstance().cacheForModule(MODULE_NAME);
        if (cacheForModule != null) {
            return cacheForModule.getFileCache();
        }
        return null;
    }
}
