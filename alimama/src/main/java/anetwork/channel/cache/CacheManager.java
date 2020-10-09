package anetwork.channel.cache;

import anet.channel.util.ALog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheManager {
    private static List<CacheItem> cacheList = new ArrayList();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private static final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    private static class CacheItem implements Comparable<CacheItem> {
        final Cache cache;
        final CachePrediction prediction;
        final int priority;

        CacheItem(Cache cache2, CachePrediction cachePrediction, int i) {
            this.cache = cache2;
            this.prediction = cachePrediction;
            this.priority = i;
        }

        public int compareTo(CacheItem cacheItem) {
            return this.priority - cacheItem.priority;
        }
    }

    public static void addCache(Cache cache, CachePrediction cachePrediction, int i) {
        if (cache == null) {
            throw new IllegalArgumentException("cache is null");
        } else if (cachePrediction != null) {
            try {
                writeLock.lock();
                cacheList.add(new CacheItem(cache, cachePrediction, i));
                Collections.sort(cacheList);
            } finally {
                writeLock.unlock();
            }
        } else {
            throw new IllegalArgumentException("prediction is null");
        }
    }

    public static void removeCache(Cache cache) {
        try {
            writeLock.lock();
            ListIterator<CacheItem> listIterator = cacheList.listIterator();
            while (true) {
                if (listIterator.hasNext()) {
                    if (listIterator.next().cache == cache) {
                        listIterator.remove();
                        break;
                    }
                } else {
                    break;
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    public static Cache getCache(String str, Map<String, String> map) {
        Cache cache;
        try {
            readLock.lock();
            Iterator<CacheItem> it = cacheList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    cache = null;
                    break;
                }
                CacheItem next = it.next();
                if (next.prediction.handleCache(str, map)) {
                    cache = next.cache;
                    break;
                }
            }
            return cache;
        } finally {
            readLock.unlock();
        }
    }

    public static void clearAllCache() {
        ALog.w("anet.CacheManager", "clearAllCache", (String) null, new Object[0]);
        for (CacheItem cacheItem : cacheList) {
            try {
                cacheItem.cache.clear();
            } catch (Exception unused) {
            }
        }
    }
}
