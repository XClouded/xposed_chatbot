package com.alimama.unionwl.unwcache;

import androidx.collection.LruCache;
import com.alimama.unionwl.unwcache.ICache;

public class MemoryCache implements ICache.IMemoryCache {
    private static final int DEFULT_MEMORY_DIZE = 1024;
    private LruCache<String, MemoryResult> mMemoryCache;

    public MemoryCache(int i) {
        this.mMemoryCache = new LruCache<>(i * 1024);
    }

    public void putDataToMemory(String str, String str2, byte[] bArr) {
        this.mMemoryCache.put(str, new MemoryResult(bArr, str2));
    }

    public void removeDataFromMemory(String str) {
        this.mMemoryCache.remove(str);
    }

    public MemoryResult getDateFromMemory(String str) {
        return this.mMemoryCache.get(str);
    }
}
