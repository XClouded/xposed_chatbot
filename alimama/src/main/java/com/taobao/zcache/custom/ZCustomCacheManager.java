package com.taobao.zcache.custom;

import com.taobao.zcache.log.ZLog;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZCustomCacheManager {
    private static ZCustomCacheManager instance;
    private List<ZCustomCacheHandler> mCacheHandlers = new ArrayList();

    public static ZCustomCacheManager getInstance() {
        if (instance == null) {
            synchronized (ZCustomCacheManager.class) {
                if (instance == null) {
                    instance = new ZCustomCacheManager();
                }
            }
        }
        return instance;
    }

    public void registerHandler(ZCustomCacheHandler zCustomCacheHandler) {
        if (this.mCacheHandlers != null && zCustomCacheHandler != null) {
            this.mCacheHandlers.add(this.mCacheHandlers.size(), zCustomCacheHandler);
        }
    }

    public InputStream getCacheResource(String str, String[] strArr, Map<String, String> map, Map<String, String> map2) {
        if (this.mCacheHandlers != null) {
            for (ZCustomCacheHandler next : this.mCacheHandlers) {
                try {
                    InputStream loadRequest = next.loadRequest(strArr, str, map, map2);
                    if (loadRequest != null) {
                        ZLog.d("ZCache", "hit custom cache by " + next.toString() + " with url " + str);
                        return loadRequest;
                    }
                } catch (Throwable unused) {
                }
            }
        }
        ZLog.d("ZCache", "custom cache not hit " + str);
        return null;
    }
}
