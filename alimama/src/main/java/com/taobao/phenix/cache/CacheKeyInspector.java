package com.taobao.phenix.cache;

public abstract class CacheKeyInspector {
    public int inspectDiskCacheCatalog(String str, int i) {
        return i;
    }

    public abstract String inspectDiskCacheKey(String str, String str2);

    public abstract String inspectMemoryCacheKey(String str, String str2);
}
