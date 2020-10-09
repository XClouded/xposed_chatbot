package android.taobao.windvane.cache;

import android.taobao.windvane.util.WVUrlUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WVMemoryCache {
    public static final long DEFAULT_CACHE_TIME = 2000;
    private static WVMemoryCache mMemoryCache;
    private HashMap<String, WVMemoryCacheInfo> mCachedInfos = null;

    public static synchronized WVMemoryCache getInstance() {
        WVMemoryCache wVMemoryCache;
        synchronized (WVMemoryCache.class) {
            if (mMemoryCache == null) {
                mMemoryCache = new WVMemoryCache();
            }
            wVMemoryCache = mMemoryCache;
        }
        return wVMemoryCache;
    }

    public void addMemoryCache(String str, Map<String, List<String>> map, byte[] bArr) {
        if (this.mCachedInfos == null) {
            this.mCachedInfos = new HashMap<>();
        }
        if (str != null) {
            this.mCachedInfos.put(WVUrlUtil.force2HttpUrl(WVUrlUtil.removeQueryParam(str)), new WVMemoryCacheInfo(map, bArr));
        }
    }

    public WVMemoryCacheInfo getMemoryCacheByUrl(String str) {
        if (this.mCachedInfos == null || str == null) {
            return null;
        }
        return this.mCachedInfos.get(WVUrlUtil.force2HttpUrl(WVUrlUtil.removeQueryParam(str)));
    }

    public void clearCacheByUrl(String str) {
        if (this.mCachedInfos != null && this.mCachedInfos.containsKey(str)) {
            this.mCachedInfos.remove(str);
        }
    }

    public void clearAllCaches() {
        if (this.mCachedInfos != null) {
            this.mCachedInfos.clear();
        }
    }
}
