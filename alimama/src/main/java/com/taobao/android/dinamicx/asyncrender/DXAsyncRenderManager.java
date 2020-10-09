package com.taobao.android.dinamicx.asyncrender;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import com.taobao.android.dinamicx.DXBaseClass;
import com.taobao.android.dinamicx.DXEngineContext;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXPipelineCacheManager;
import com.taobao.android.dinamicx.DXPreRenderWorkTask;
import com.taobao.android.dinamicx.DXPrefetchTask;
import com.taobao.android.dinamicx.DXRenderOptions;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.DXTemplateManager;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.log.DXLog;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.download.DXPriorityRunnable;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.thread.DXRunnableManager;
import com.taobao.android.dinamicx.widget.event.DXControlEventCenter;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class DXAsyncRenderManager extends DXBaseClass {
    public static final int MSG_ASYNC_RENDER = 3;
    public static final int MSG_CACHE_MONITOR = 8;
    public static final int MSG_CANCEL_PREFETCH_TASK = 7;
    public static final int MSG_CLEAR_EXECUTOR_TASKS = 4;
    public static final int MSG_CLEAR_TASKS = 5;
    public static final int MSG_PREFETCH = 2;
    public static final int MSG_PRE_RENDER = 1;
    public static final int MSG_RESTORE_EXECUTOR_TASKS = 6;
    private static final int NO_PREFETCH = -1;
    private static final String TAG = "DXAsyncRenderManager";
    private int addPrefetchTaskCount = -1;
    private int cacheHits;
    private int callRenderTemplateCount;
    private int cancelPrefetchTaskCount;
    private AsyncScheduledHandler handler;
    private boolean hasCleared;
    private HashMap<String, DXPrefetchTask> prefetchTasks;

    public DXAsyncRenderManager(@NonNull DXEngineContext dXEngineContext) {
        super(dXEngineContext);
        try {
            this.handler = new AsyncScheduledHandler(this, DXRunnableManager.getAsyncRenderScheduledThread().getLooper());
        } catch (Throwable th) {
            this.handler = new AsyncScheduledHandler(this, Looper.getMainLooper());
            DXAppMonitor.trackerError(this.bizType, (DXTemplateItem) null, DXMonitorConstant.DX_MONITOR_ASYNC_RENDER, DXMonitorConstant.DX_ASYNC_RENDER_INIT_CRASH, DXError.V3_ASYNC_RENDER_INIT_CRASH, DXExceptionUtil.getStackTrace(th));
        }
    }

    public void schedulePrefetchTemplate(Runnable runnable) {
        Message obtain = Message.obtain();
        obtain.what = 2;
        obtain.obj = runnable;
        this.handler.sendMessage(obtain);
    }

    public void prefetchTemplate(DXRuntimeContext dXRuntimeContext, DXRenderOptions dXRenderOptions, DXTemplateManager dXTemplateManager, DXPipelineCacheManager dXPipelineCacheManager, DXControlEventCenter dXControlEventCenter) {
        if (this.prefetchTasks == null) {
            this.prefetchTasks = new HashMap<>(100);
        }
        if (!this.prefetchTasks.containsKey(dXRuntimeContext.getCacheIdentify())) {
            if (this.addPrefetchTaskCount == -1) {
                this.addPrefetchTaskCount = 0;
            }
            DXPrefetchTask dXPrefetchTask = new DXPrefetchTask(dXRuntimeContext, dXRenderOptions, dXTemplateManager, dXPipelineCacheManager, this.engineContext, dXControlEventCenter);
            DXRunnableManager.runForPrefetch(new DXPriorityRunnable(2, dXPrefetchTask));
            this.prefetchTasks.put(dXRuntimeContext.getCacheIdentify(), dXPrefetchTask);
            this.addPrefetchTaskCount++;
        }
    }

    public void schedulePreRenderTemplate(Runnable runnable) {
        Message obtain = Message.obtain();
        obtain.what = 1;
        obtain.obj = runnable;
        this.handler.sendMessage(obtain);
    }

    public void preRenderTemplate(DXRuntimeContext dXRuntimeContext, DXRenderOptions dXRenderOptions, DXTemplateManager dXTemplateManager, DXPipelineCacheManager dXPipelineCacheManager, DXControlEventCenter dXControlEventCenter) {
        DXRunnableManager.runForPreRender(new DXPriorityRunnable(0, new DXPreRenderWorkTask(dXRuntimeContext, dXRenderOptions, dXTemplateManager, dXPipelineCacheManager, this.engineContext, dXControlEventCenter)));
    }

    public void beforeRenderTemplate(DXRuntimeContext dXRuntimeContext) {
        scheduleCancelPrefetchTask(dXRuntimeContext);
    }

    private void scheduleCancelPrefetchTask(DXRuntimeContext dXRuntimeContext) {
        Message obtain = Message.obtain();
        obtain.what = 7;
        obtain.obj = dXRuntimeContext;
        this.handler.sendMessage(obtain);
    }

    public void cancelPrefetchTask(DXRuntimeContext dXRuntimeContext) {
        this.callRenderTemplateCount++;
        if (this.prefetchTasks != null) {
            DXPrefetchTask dXPrefetchTask = this.prefetchTasks.get(dXRuntimeContext.getCacheIdentify());
            if (!dXPrefetchTask.isDone) {
                dXPrefetchTask.options.setCanceled(true);
                dXPrefetchTask.isDone = true;
                this.cancelPrefetchTaskCount++;
            } else if (!dXPrefetchTask.options.isCanceled()) {
                this.cacheHits++;
            }
        }
    }

    private void scheduleClearExecutorTasks() {
        Message obtain = Message.obtain();
        obtain.what = 4;
        this.handler.sendMessage(obtain);
    }

    private void scheduleClearTasks() {
        Message obtain = Message.obtain();
        obtain.what = 5;
        this.handler.sendMessage(obtain);
    }

    private void scheduleRestoreExecutorTasks() {
        Message obtain = Message.obtain();
        obtain.what = 6;
        this.handler.sendMessage(obtain);
    }

    /* access modifiers changed from: private */
    public void clearExecutorTasks() {
        this.hasCleared = true;
        DXRunnableManager.clearAsyncRenderTasks();
    }

    /* access modifiers changed from: private */
    public void clearTasks() {
        if (this.prefetchTasks != null) {
            this.prefetchTasks.clear();
        }
    }

    /* access modifiers changed from: private */
    public void restoreExecutorTasks() {
        if (this.hasCleared) {
            if (this.prefetchTasks != null) {
                for (DXPrefetchTask next : this.prefetchTasks.values()) {
                    if (!next.isDone) {
                        DXRunnableManager.runForPrefetch(new DXPriorityRunnable(2, next));
                    }
                }
            }
            this.hasCleared = false;
        }
    }

    public void onDestroy() {
        if (this.addPrefetchTaskCount != -1) {
            scheduleClearExecutorTasks();
        }
    }

    private void schedulePrefetchCacheMonitor() {
        Message obtain = Message.obtain();
        obtain.what = 8;
        this.handler.sendMessage(obtain);
    }

    /* access modifiers changed from: private */
    public void prefetchMonitor() {
        if (this.addPrefetchTaskCount != 0) {
            if (this.addPrefetchTaskCount > 0) {
                float f = ((float) (this.addPrefetchTaskCount - this.cancelPrefetchTaskCount)) / ((float) this.addPrefetchTaskCount);
                HashMap hashMap = new HashMap();
                hashMap.put("totalNum", String.valueOf(this.addPrefetchTaskCount));
                hashMap.put("cancelNum", String.valueOf(this.cancelPrefetchTaskCount));
                hashMap.put("fillRate", String.valueOf(f));
                DXAppMonitor.trackerAsyncRender(0, this.bizType, "PreRender", "PreRender_FillRate", hashMap);
                DXLog.i(TAG, "任务填充率=" + f + "预加载任务创建=" + this.addPrefetchTaskCount + "任务取消=" + this.cancelPrefetchTaskCount);
            }
            if (this.callRenderTemplateCount > 0) {
                float f2 = ((float) this.cacheHits) / ((float) this.callRenderTemplateCount);
                HashMap hashMap2 = new HashMap();
                hashMap2.put("totalNum", String.valueOf(this.callRenderTemplateCount));
                hashMap2.put("hitNum", String.valueOf(this.cacheHits));
                hashMap2.put("hitRate", String.valueOf(f2));
                DXAppMonitor.trackerAsyncRender(0, this.bizType, "PreRender", "PreRender_HitRate", hashMap2);
                DXLog.i(TAG, "缓存命中率=" + f2 + "模板渲染调用次数=" + this.callRenderTemplateCount + "缓存命中的调用次数=" + this.cacheHits);
            }
            if (getConfig().getPipelineCacheMaxCount() > 0) {
                float pipelineCacheMaxCount = ((float) this.addPrefetchTaskCount) / ((float) getConfig().getPipelineCacheMaxCount());
                HashMap hashMap3 = new HashMap();
                hashMap3.put("maxNum", String.valueOf(getConfig().getPipelineCacheMaxCount()));
                hashMap3.put("taskNum", String.valueOf(this.prefetchTasks != null ? this.prefetchTasks.size() : 0));
                hashMap3.put("hitRate", String.valueOf(pipelineCacheMaxCount));
                DXAppMonitor.trackerAsyncRender(0, this.bizType, "PreRender", "PreRender_OccupationRate", hashMap3);
                DXLog.i(TAG, "缓存利用率=" + pipelineCacheMaxCount + "缓存最大个数限制=" + getConfig().getPipelineCacheMaxCount() + "预加载的创建任务=" + this.addPrefetchTaskCount);
            }
            this.addPrefetchTaskCount = 0;
            this.cancelPrefetchTaskCount = 0;
            this.callRenderTemplateCount = 0;
            this.cacheHits = 0;
        }
    }

    public void reset() {
        if (this.addPrefetchTaskCount != -1) {
            schedulePrefetchCacheMonitor();
            scheduleClearExecutorTasks();
            scheduleClearTasks();
        }
    }

    public void onResume() {
        if (this.addPrefetchTaskCount != -1) {
            scheduleRestoreExecutorTasks();
        }
    }

    public void onStop() {
        if (this.addPrefetchTaskCount != -1) {
            schedulePrefetchCacheMonitor();
            scheduleClearExecutorTasks();
        }
    }

    public void cancelAllTasks() {
        if (this.addPrefetchTaskCount != -1) {
            scheduleClearExecutorTasks();
        }
    }

    public static class AsyncScheduledHandler extends Handler {
        private WeakReference<DXAsyncRenderManager> managerWeakReference;

        public AsyncScheduledHandler(DXAsyncRenderManager dXAsyncRenderManager, Looper looper) {
            super(looper);
            this.managerWeakReference = new WeakReference<>(dXAsyncRenderManager);
        }

        public void handleMessage(Message message) {
            if (message != null) {
                DXAsyncRenderManager dXAsyncRenderManager = (DXAsyncRenderManager) this.managerWeakReference.get();
                if (dXAsyncRenderManager == null) {
                    removeCallbacksAndMessages((Object) null);
                    return;
                }
                try {
                    switch (message.what) {
                        case 1:
                            ((Runnable) message.obj).run();
                            return;
                        case 2:
                            ((Runnable) message.obj).run();
                            return;
                        case 4:
                            dXAsyncRenderManager.clearExecutorTasks();
                            return;
                        case 5:
                            dXAsyncRenderManager.clearTasks();
                            return;
                        case 6:
                            dXAsyncRenderManager.restoreExecutorTasks();
                            return;
                        case 7:
                            dXAsyncRenderManager.cancelPrefetchTask((DXRuntimeContext) message.obj);
                            return;
                        case 8:
                            dXAsyncRenderManager.prefetchMonitor();
                            return;
                        default:
                            return;
                    }
                } catch (Throwable th) {
                    DXExceptionUtil.printStack(th);
                }
            }
        }
    }
}
