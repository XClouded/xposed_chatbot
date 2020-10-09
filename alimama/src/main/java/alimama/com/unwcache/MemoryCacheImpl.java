package alimama.com.unwcache;

import alimama.com.unwbase.interfaces.IMemoryCache;
import android.util.LruCache;

public class MemoryCacheImpl implements IMemoryCache {
    private static final int DEFULT_MEMORY_DIZE = 1024;
    private LruCache<String, Object> mMemoryCache;
    private int memoryCacheSizeInKB = 1;

    public MemoryCacheImpl(int i) {
        this.memoryCacheSizeInKB = i;
    }

    public void removeDataFromMemory(String str) {
        this.mMemoryCache.remove(str);
    }

    public Object getDateFromMemory(String str) {
        return this.mMemoryCache.get(str);
    }

    public void putDataToMemory(String str, Object obj) {
        this.mMemoryCache.put(str, obj);
    }

    public void init() {
        this.mMemoryCache = new LruCache<>(this.memoryCacheSizeInKB * 1024);
    }
}
