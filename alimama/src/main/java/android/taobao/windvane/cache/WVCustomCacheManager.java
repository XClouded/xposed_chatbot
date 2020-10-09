package android.taobao.windvane.cache;

import android.taobao.windvane.util.TaoLog;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WVCustomCacheManager {
    private static final String TAG = "WVCustomCacheManager";
    private static WVCustomCacheManager sInstance;
    private List<WVCustomCacheHandler> mCacheHandlers = new ArrayList();

    public static WVCustomCacheManager getInstance() {
        if (sInstance == null) {
            synchronized (WVCustomCacheManager.class) {
                if (sInstance == null) {
                    sInstance = new WVCustomCacheManager();
                }
            }
        }
        return sInstance;
    }

    private WVCustomCacheManager() {
    }

    public void registerHandler(WVCustomCacheHandler wVCustomCacheHandler) {
        if (this.mCacheHandlers != null && wVCustomCacheHandler != null) {
            this.mCacheHandlers.add(this.mCacheHandlers.size(), wVCustomCacheHandler);
        }
    }

    public InputStream getCacheResource(String str, String[] strArr, Map<String, String> map, Map<String, String> map2) {
        if (this.mCacheHandlers != null) {
            for (WVCustomCacheHandler next : this.mCacheHandlers) {
                try {
                    InputStream loadRequest = next.loadRequest(strArr, str, map, map2);
                    if (loadRequest != null) {
                        TaoLog.d(TAG, "hit custom cache by " + next.toString() + " with url " + str);
                        return loadRequest;
                    }
                } catch (Throwable unused) {
                }
            }
        }
        TaoLog.d(TAG, "custom cache not hit " + str);
        return null;
    }
}
