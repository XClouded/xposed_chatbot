package com.taobao.android.dinamicx;

import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.collection.LruCache;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.widget.DXWidgetNode;

public class DXPipelineCacheManager extends DXBaseClass {
    LruCache<String, DXPipelineCacheObj> expandWidgetLruCache = new LruCache<>(this.config.getPipelineCacheMaxCount());

    protected DXPipelineCacheManager(@NonNull DXEngineContext dXEngineContext) {
        super(dXEngineContext);
    }

    public boolean needReadExpandedWidgetNode(DXWidgetNode dXWidgetNode, DXRenderOptions dXRenderOptions) {
        if (!this.config.isUsePipelineCache() || dXRenderOptions.isControlEvent()) {
            return false;
        }
        return (dXWidgetNode == null || dXWidgetNode.getParentWidget() == null) && dXRenderOptions.getRenderType() != 1;
    }

    public DXWidgetNode readExpandedWidgetNode(DXRuntimeContext dXRuntimeContext, View view) {
        try {
            DXPipelineCacheObj expandWidgetLruCache2 = getExpandWidgetLruCache(dXRuntimeContext);
            if (expandWidgetLruCache2 == null || expandWidgetLruCache2.cacheWidgetNode == null) {
                return null;
            }
            DXWidgetNode dXWidgetNode = expandWidgetLruCache2.cacheWidgetNode;
            if (dXRuntimeContext.getData() != dXWidgetNode.getDXRuntimeContext().getData()) {
                return null;
            }
            if (expandWidgetLruCache2.hasError()) {
                dXRuntimeContext.dxError.dxErrorInfoList.addAll(expandWidgetLruCache2.error.dxErrorInfoList);
            }
            dXWidgetNode.bindRuntimeContext(dXRuntimeContext, true);
            if (view != null) {
                view.setTag(DXPublicConstant.TAG_EXPANDED_WIDGET_ON_VIEW, dXWidgetNode);
            }
            return dXWidgetNode;
        } catch (Exception e) {
            DXAppMonitor.trackerError(this.bizType, dXRuntimeContext.getDxTemplateItem(), DXMonitorConstant.DX_MONITOR_RENDER, DXMonitorConstant.RENDER_GET_EXPAND_TREE_CRASH, DXError.GET_EXPAND_TREE_CRASH, DXExceptionUtil.getStackTrace(e));
            return null;
        }
    }

    public void putExpandWidgetLruCache(DXRuntimeContext dXRuntimeContext, DXPipelineCacheObj dXPipelineCacheObj) {
        String cacheIdentify = dXRuntimeContext.getCacheIdentify();
        if (this.config.isUsePipelineCache() && !TextUtils.isEmpty(cacheIdentify) && dXPipelineCacheObj != null && this.expandWidgetLruCache != null) {
            this.expandWidgetLruCache.put(cacheIdentify, dXPipelineCacheObj);
        }
    }

    public DXPipelineCacheObj getExpandWidgetLruCache(DXRuntimeContext dXRuntimeContext) {
        return getExpandWidgetLruCache(dXRuntimeContext.getCacheIdentify());
    }

    public DXPipelineCacheObj getExpandWidgetLruCache(String str) {
        if (!this.config.isUsePipelineCache() || TextUtils.isEmpty(str) || this.expandWidgetLruCache == null) {
            return null;
        }
        return this.expandWidgetLruCache.get(str);
    }

    public DXPipelineCacheObj buildPipelineCacheObj(DXWidgetNode dXWidgetNode, DXError dXError) {
        DXPipelineCacheObj dXPipelineCacheObj = new DXPipelineCacheObj();
        dXPipelineCacheObj.cacheWidgetNode = dXWidgetNode;
        dXPipelineCacheObj.error = dXError;
        return dXPipelineCacheObj;
    }

    public void removeCache(String str) {
        if (!TextUtils.isEmpty(str) && this.expandWidgetLruCache != null) {
            DXPipelineCacheObj remove = this.expandWidgetLruCache.remove(str);
        }
    }

    public int getSize() {
        return this.expandWidgetLruCache.size();
    }

    public void clearCache() {
        this.expandWidgetLruCache.evictAll();
    }

    public static class DXPipelineCacheObj {
        public DXWidgetNode cacheWidgetNode;
        public DXError error;

        public boolean hasError() {
            return (this.error == null || this.error.dxErrorInfoList == null || this.error.dxErrorInfoList.size() <= 0) ? false : true;
        }
    }
}
