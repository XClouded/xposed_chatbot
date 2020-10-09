package com.taobao.monitor.impl.data.image;

import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.logger.DataLoggerUtils;
import com.taobao.monitor.impl.trace.DispatcherManager;
import com.taobao.monitor.impl.trace.IDispatcher;
import com.taobao.monitor.impl.trace.ImageStageDispatcher;
import com.taobao.monitor.impl.util.SafeUtils;
import com.taobao.phenix.lifecycle.IPhenixLifeCycle;
import com.uc.webview.export.media.MessageID;
import java.util.HashMap;
import java.util.Map;

public class PhenixLifeCycleImpl implements IPhenixLifeCycle {
    private static final String LOG_TAG = "image";
    private static final String ON_DISK_CACHE = "onDiskCache";
    private static final String ON_MEM_CACHE = "onMemCache";
    private static final String ON_REMOTE = "onRemote";
    private static final String TAG = "PhenixLifeCycleImpl";
    private ImageStageDispatcher stageDispatcher = null;

    public PhenixLifeCycleImpl() {
        init();
    }

    private void init() {
        IDispatcher dispatcher = DispatcherManager.getDispatcher(APMContext.IMAGE_STAGE_DISPATCHER);
        if (dispatcher instanceof ImageStageDispatcher) {
            this.stageDispatcher = (ImageStageDispatcher) dispatcher;
        }
    }

    public void onRequest(String str, String str2, Map<String, Object> map) {
        if (!DispatcherManager.isEmpty(this.stageDispatcher)) {
            this.stageDispatcher.dispatchImageStage(0);
        }
        try {
            HashMap hashMap = new HashMap(map);
            hashMap.put("procedureName", "ImageLib");
            hashMap.put("stage", "onRequest");
            hashMap.put("requestId", str);
            hashMap.put("requestUrl", str2);
            DataLoggerUtils.log("image", hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onError(String str, String str2, Map<String, Object> map) {
        if (!DispatcherManager.isEmpty(this.stageDispatcher)) {
            this.stageDispatcher.dispatchImageStage(2);
        }
        HashMap hashMap = new HashMap(map);
        hashMap.put("procedureName", "ImageLib");
        hashMap.put("stage", MessageID.onError);
        hashMap.put("requestId", str);
        hashMap.put("requestUrl", str2);
        DataLoggerUtils.log("image", hashMap);
    }

    public void onCancel(String str, String str2, Map<String, Object> map) {
        if (!DispatcherManager.isEmpty(this.stageDispatcher)) {
            this.stageDispatcher.dispatchImageStage(3);
        }
        HashMap hashMap = new HashMap(map);
        hashMap.put("procedureName", "ImageLib");
        hashMap.put("stage", "onCancel");
        hashMap.put("requestId", str);
        hashMap.put("requestUrl", str2);
        DataLoggerUtils.log("image", hashMap);
    }

    public void onFinished(String str, String str2, Map<String, Object> map) {
        if (!DispatcherManager.isEmpty(this.stageDispatcher)) {
            this.stageDispatcher.dispatchImageStage(1);
        }
        HashMap hashMap = new HashMap(map);
        hashMap.put("procedureName", "ImageLib");
        hashMap.put("stage", "onFinished");
        hashMap.put("requestId", str);
        hashMap.put("requestUrl", str2);
        DataLoggerUtils.log("image", hashMap);
    }

    public void onEvent(String str, String str2, Map<String, Object> map) {
        String safeString = map != null ? SafeUtils.getSafeString(map.get("requestUrl"), "") : null;
        HashMap hashMap = new HashMap(map);
        hashMap.put("procedureName", "ImageLib");
        hashMap.put("stage", str2);
        hashMap.put("requestId", str);
        hashMap.put("requestUrl", safeString);
        DataLoggerUtils.log("image", hashMap);
    }
}
