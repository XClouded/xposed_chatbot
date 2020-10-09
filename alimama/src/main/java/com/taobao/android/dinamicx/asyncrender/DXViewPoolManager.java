package com.taobao.android.dinamicx.asyncrender;

import android.content.Context;
import android.text.TextUtils;
import androidx.core.util.Pools;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.view.ViewResult;
import com.taobao.android.dinamicx.DXRootView;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DXViewPoolManager {
    private final Map<String, Map<String, Pools.Pool<ViewResult>>> v2TemplateViewCache = new ConcurrentHashMap();
    private final Map<String, Map<String, Pools.Pool<DXRootView>>> v3TemplateViewCache = new ConcurrentHashMap();

    public static DXViewPoolManager getInstance() {
        return DXViewPoolManagerHolder.INSTANCE;
    }

    private static class DXViewPoolManagerHolder {
        /* access modifiers changed from: private */
        public static final DXViewPoolManager INSTANCE = new DXViewPoolManager();

        private DXViewPoolManagerHolder() {
        }
    }

    public static String getV2TemplateKey(DinamicTemplate dinamicTemplate) {
        if (dinamicTemplate == null) {
            return "";
        }
        return dinamicTemplate.name + "_" + dinamicTemplate.version;
    }

    public void cacheV2ViewResult(ViewResult viewResult, DinamicTemplate dinamicTemplate, String str) {
        if (viewResult != null && !TextUtils.isEmpty(str) && dinamicTemplate != null) {
            String v2TemplateKey = getV2TemplateKey(dinamicTemplate);
            Map map = this.v2TemplateViewCache.get(str);
            if (map == null) {
                map = new ConcurrentHashMap();
                this.v2TemplateViewCache.put(str, map);
            }
            Pools.Pool pool = (Pools.Pool) map.get(v2TemplateKey);
            if (pool == null) {
                pool = new Pools.SynchronizedPool(6);
                map.put(v2TemplateKey, pool);
            }
            pool.release(viewResult);
        }
    }

    public ViewResult obtainV2View(Context context, DinamicTemplate dinamicTemplate, String str) {
        Map map;
        Pools.Pool pool;
        if (dinamicTemplate == null || TextUtils.isEmpty(str) || (map = this.v2TemplateViewCache.get(str)) == null || (pool = (Pools.Pool) map.get(getV2TemplateKey(dinamicTemplate))) == null) {
            return null;
        }
        ViewResult viewResult = (ViewResult) pool.acquire();
        if (!(viewResult == null || viewResult.getView() == null)) {
            Context context2 = viewResult.getView().getContext();
            if ((context2 instanceof ViewContext) && context != null) {
                ((ViewContext) context2).setCurrentContext(context);
            }
        }
        return viewResult;
    }

    public void clearV2Cache(String str) {
        if (!TextUtils.isEmpty(str) && this.v2TemplateViewCache != null) {
            this.v2TemplateViewCache.remove(str);
        }
    }

    public void cacheV3View(DXRootView dXRootView, DXTemplateItem dXTemplateItem, String str) {
        if (dXRootView != null && !TextUtils.isEmpty(str) && dXTemplateItem != null) {
            Map map = this.v3TemplateViewCache.get(str);
            if (map == null) {
                map = new ConcurrentHashMap();
                this.v3TemplateViewCache.put(str, map);
            }
            Pools.Pool pool = (Pools.Pool) map.get(dXTemplateItem.getIdentifier());
            if (pool == null) {
                pool = new Pools.SynchronizedPool(6);
                map.put(dXTemplateItem.getIdentifier(), pool);
            }
            pool.release(dXRootView);
        }
    }

    public DXRootView obtainV3View(Context context, DXTemplateItem dXTemplateItem, String str) {
        Map map;
        Pools.Pool pool;
        if (dXTemplateItem == null || TextUtils.isEmpty(str) || (map = this.v3TemplateViewCache.get(str)) == null || (pool = (Pools.Pool) map.get(dXTemplateItem.getIdentifier())) == null) {
            return null;
        }
        DXRootView dXRootView = (DXRootView) pool.acquire();
        if (!(dXRootView == null || !(dXRootView.getContext() instanceof ViewContext) || context == null)) {
            ((ViewContext) dXRootView.getContext()).setCurrentContext(context);
        }
        return dXRootView;
    }

    public void clearV3Cache(String str) {
        if (!TextUtils.isEmpty(str) && this.v3TemplateViewCache != null) {
            this.v3TemplateViewCache.remove(str);
        }
    }

    /* access modifiers changed from: package-private */
    public Map<String, Map<String, Pools.Pool<ViewResult>>> getV2TemplateViewCache() {
        return this.v2TemplateViewCache;
    }

    /* access modifiers changed from: package-private */
    public Map<String, Map<String, Pools.Pool<DXRootView>>> getV3TemplateViewCache() {
        return this.v3TemplateViewCache;
    }
}
