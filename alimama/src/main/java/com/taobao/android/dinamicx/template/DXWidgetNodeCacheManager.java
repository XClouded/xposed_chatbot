package com.taobao.android.dinamicx.template;

import android.util.LruCache;
import androidx.annotation.NonNull;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import java.util.HashMap;
import java.util.Map;

public class DXWidgetNodeCacheManager {
    private final String PUBLIC_CACHE;
    private final Map<String, LruCache<String, DXWidgetNode>> cacheCenter;

    private void initWhiteList() {
    }

    private void initPublicCache() {
        this.cacheCenter.put("public_cache", new LruCache(100));
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DXWidgetNodeCacheManager INSTANCE = new DXWidgetNodeCacheManager();

        private SingletonHolder() {
        }
    }

    public static DXWidgetNodeCacheManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private DXWidgetNodeCacheManager() {
        this.PUBLIC_CACHE = "public_cache";
        this.cacheCenter = new HashMap();
        initPublicCache();
        initWhiteList();
    }

    public void putCache(String str, DXTemplateItem dXTemplateItem, DXWidgetNode dXWidgetNode) {
        if (DXTemplateNamePathUtil.isValid(str, dXTemplateItem) && dXWidgetNode != null) {
            synchronized (this.cacheCenter) {
                LruCache lruCache = this.cacheCenter.get(getCacheKey(str));
                if (lruCache != null) {
                    lruCache.put(generateIdentify(str, dXTemplateItem), dXWidgetNode);
                }
            }
        }
    }

    public DXWidgetNode getCache(String str, DXTemplateItem dXTemplateItem) {
        if (!DXTemplateNamePathUtil.isValid(str, dXTemplateItem)) {
            return null;
        }
        synchronized (this.cacheCenter) {
            LruCache lruCache = this.cacheCenter.get(getCacheKey(str));
            if (lruCache == null) {
                return null;
            }
            DXWidgetNode dXWidgetNode = (DXWidgetNode) lruCache.get(generateIdentify(str, dXTemplateItem));
            return dXWidgetNode;
        }
    }

    private String generateIdentify(String str, DXTemplateItem dXTemplateItem) {
        return str + dXTemplateItem.getIdentifier() + "_" + DXScreenTool.getScreenWidth(DinamicXEngine.getApplicationContext());
    }

    private String getCacheKey(@NonNull String str) {
        return this.cacheCenter.get(str) != null ? str : "public_cache";
    }
}
