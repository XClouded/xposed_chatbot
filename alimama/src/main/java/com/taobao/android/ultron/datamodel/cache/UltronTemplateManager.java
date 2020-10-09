package com.taobao.android.ultron.datamodel.cache;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.common.utils.UnifyLog;
import com.taobao.android.ultron.datamodel.cache.TemplateCache;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UltronTemplateManager {
    private static final long FILE_CAPACITY = 16777216;
    private static final String TAG = "UltronTemplateManager";
    private static final int TEMPLATE_MEM_CACHE_SIZE = 16;
    private static Map<String, UltronTemplateManager> templateManagerMap = new HashMap();
    private final LruCache<String, CacheDataResult> cacheDataResultCache = new LruCache<>(4);
    private final Object cacheLock = new Object();
    private final TemplateCache templateFileCache;
    private volatile List<String> templateIds;
    private final LruCache<String, String> templateMemCache = new LruCache<>(16);

    private UltronTemplateManager(Context context, String str) {
        String str2 = str + "_ultron_template_cache";
        this.templateFileCache = new TemplateCache.Builder().withContext(context.getApplicationContext()).withDbName(str2 + ".db").withRootDirName(str2).withMemCacheSize(1).withFileCapacity(16777216).build();
    }

    public static UltronTemplateManager getInstance(Context context, @NonNull String str) {
        UltronTemplateManager ultronTemplateManager = templateManagerMap.get(str);
        if (ultronTemplateManager == null) {
            synchronized (UltronTemplateManager.class) {
                ultronTemplateManager = templateManagerMap.get(str);
                if (ultronTemplateManager == null) {
                    Map<String, UltronTemplateManager> map = templateManagerMap;
                    UltronTemplateManager ultronTemplateManager2 = new UltronTemplateManager(context, str);
                    map.put(str, ultronTemplateManager2);
                    ultronTemplateManager = ultronTemplateManager2;
                }
            }
        }
        return ultronTemplateManager;
    }

    public JSONObject getTemplateById(String str) {
        String str2;
        byte[] fetchTemplate;
        synchronized (this.cacheLock) {
            str2 = this.templateMemCache.get(str);
        }
        if (str2 == null && (fetchTemplate = this.templateFileCache.fetchTemplate(str)) != null && fetchTemplate.length > 0) {
            str2 = new String(fetchTemplate, Charset.forName("UTF-8"));
            synchronized (this.cacheLock) {
                this.templateMemCache.put(str, str2);
            }
        }
        return JSON.parseObject(str2);
    }

    public List<String> getTemplateIds() {
        if (this.templateIds == null) {
            List<String> templateIds2 = this.templateFileCache.getTemplateIds();
            synchronized (this.cacheLock) {
                if (this.templateIds == null) {
                    this.templateIds = templateIds2;
                }
            }
        }
        return this.templateIds == null ? Collections.emptyList() : this.templateIds;
    }

    public void saveTemplate(String str, JSONObject jSONObject) {
        if (!TextUtils.isEmpty(str) && jSONObject != null) {
            try {
                String jSONString = jSONObject.toJSONString();
                synchronized (this.cacheLock) {
                    if (this.templateIds != null && !this.templateIds.contains(str)) {
                        this.templateIds.add(str);
                    }
                    if (this.templateMemCache.get(str) == null) {
                        this.templateMemCache.put(str, jSONString);
                    }
                }
                this.templateFileCache.saveTemplate(str, jSONString.getBytes(Charset.forName("UTF-8")));
            } catch (Throwable th) {
                UnifyLog.e(TAG, "saveTemplate", Log.getStackTraceString(th));
            }
        }
    }

    public void deleteTemplateById(String str) {
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.cacheLock) {
                if (this.templateIds != null) {
                    this.templateIds.remove(str);
                }
                this.templateMemCache.remove(str);
            }
            this.templateFileCache.delTemplateById(str);
        }
    }

    public void preloadTemplates() {
        List<String> templateIds2 = getTemplateIds();
        if (templateIds2 != null) {
            for (int i = 0; i < templateIds2.size(); i++) {
                if (i < 16) {
                    getTemplateById(templateIds2.get(i));
                }
            }
        }
    }

    public void saveCacheDataResult(String str, CacheDataResult cacheDataResult) {
        if (!TextUtils.isEmpty(str) && cacheDataResult != null) {
            synchronized (this.cacheLock) {
                this.cacheDataResultCache.put(str, cacheDataResult);
            }
        }
    }

    public void deleteCacheDataResult(String str) {
        synchronized (this.cacheLock) {
            this.cacheDataResultCache.remove(str);
        }
    }

    public CacheDataResult getCacheDataResult(String str) {
        return this.cacheDataResultCache.get(str);
    }
}
