package mtopsdk.mtop.cache.handler;

import anetwork.network.cache.RpcCache;

public class CacheParserFactory {
    public static ICacheParser createCacheParser(RpcCache.CacheStatus cacheStatus) {
        if (cacheStatus == null) {
            return new EmptyCacheParser();
        }
        switch (cacheStatus) {
            case FRESH:
                return new FreshCacheParser();
            case NEED_UPDATE:
                return new ExpiredCacheParser();
            default:
                return new EmptyCacheParser();
        }
    }
}
