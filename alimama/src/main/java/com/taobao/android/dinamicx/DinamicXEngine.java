package com.taobao.android.dinamicx;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXRenderOptions;
import com.taobao.android.dinamicx.DXRootView;
import com.taobao.android.dinamicx.asyncrender.DXAsyncRenderManager;
import com.taobao.android.dinamicx.asyncrender.DXViewPoolManager;
import com.taobao.android.dinamicx.bindingx.DXBindingXManager;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.expression.event.DXMsgCenterEvent;
import com.taobao.android.dinamicx.expression.parser.IDXDataParser;
import com.taobao.android.dinamicx.log.DXLog;
import com.taobao.android.dinamicx.log.DXRemoteLog;
import com.taobao.android.dinamicx.model.DXLongSparseArray;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.monitor.DXUmbrellaUtil;
import com.taobao.android.dinamicx.notification.DXNotificationCenter;
import com.taobao.android.dinamicx.notification.IDXNotificationListener;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.timer.DXTimerListener;
import com.taobao.android.dinamicx.timer.DXTimerManager;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;
import com.taobao.android.dinamicx.widget.event.DXControlEventCenter;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public final class DinamicXEngine extends DXBaseClass {
    private static Context context = null;
    private static boolean hasInitialize = false;
    private static boolean isDebug = false;
    DXBindingXManager bindingXManager;
    /* access modifiers changed from: private */
    public DXAsyncRenderManager dxAsyncRenderManager;
    DXControlEventCenter dxControlEventCenter;
    protected DXNotificationCenter dxNotificationCenter;
    DXPipelineCacheManager dxPipelineCacheManager;
    private DXRemoteTimeInterface dxRemoteTimeInterface;
    DXTemplateManager dxTemplateManager;
    private DXTimerManager dxTimerManager;
    private DXLongSparseArray<IDXEventHandler> eventHandlerMap;
    private boolean fullInitSuccess = true;
    private DXLongSparseArray<IDXDataParser> parserMap;
    DXRenderPipeline pipeline;
    private DXLongSparseArray<IDXBuilderWidgetNode> widgetNodeMap;

    public void afterMountView(DXRootView dXRootView) {
    }

    public void beforeMountView(DXRootView dXRootView, int i) {
    }

    public void onCreate() {
    }

    public void onPause() {
    }

    public void onRestart() {
    }

    public void onStart() {
    }

    public void unmountView(DXRootView dXRootView) {
    }

    public static void initialize(Context context2) {
        initialize(context2, (DXGlobalInitConfig) null);
    }

    public static void initialize(@NonNull Context context2, @Nullable DXGlobalInitConfig dXGlobalInitConfig) {
        try {
            long nanoTime = System.nanoTime();
            if (!hasInitialize) {
                Context applicationContext = context2.getApplicationContext();
                if (applicationContext != null) {
                    context = applicationContext;
                } else {
                    context = context2;
                }
                hasInitialize = true;
                if (dXGlobalInitConfig != null) {
                    isDebug = dXGlobalInitConfig.isDebug;
                    if (dXGlobalInitConfig.remoteDebugLog != null) {
                        DXRemoteLog.setDinamicRemoteDebugLog(dXGlobalInitConfig.remoteDebugLog);
                    }
                    if (dXGlobalInitConfig.appMonitor != null) {
                        DXAppMonitor.setDxAppMonitor(dXGlobalInitConfig.appMonitor);
                    }
                    if (dXGlobalInitConfig.dxDataParserMap != null) {
                        DXGlobalCenter.globalParserMap.putAll(dXGlobalInitConfig.dxDataParserMap);
                    }
                    if (dXGlobalInitConfig.dxEventHandlerMap != null) {
                        DXGlobalCenter.globalEventHandlerMap.putAll(dXGlobalInitConfig.dxEventHandlerMap);
                    }
                    if (dXGlobalInitConfig.dxWidgetMap != null) {
                        DXGlobalCenter.globalWidgetNodeMap.putAll(dXGlobalInitConfig.dxWidgetMap);
                    }
                    if (dXGlobalInitConfig.dxDownloader != null) {
                        DXGlobalCenter.dxDownloader = dXGlobalInitConfig.dxDownloader;
                    }
                    if (dXGlobalInitConfig.dxWebImageInterface != null) {
                        DXGlobalCenter.dxWebImageInterface = dXGlobalInitConfig.dxWebImageInterface;
                    }
                    if (dXGlobalInitConfig.screenOrientation != 0) {
                        DXScreenTool._setGlobalOrientation(dXGlobalInitConfig.screenOrientation);
                    }
                    if (dXGlobalInitConfig.umbrellaImpl != null) {
                        DXUmbrellaUtil.setUmbrellaImpl(dXGlobalInitConfig.umbrellaImpl);
                    }
                    if (dXGlobalInitConfig.dxDarkModeInterface != null) {
                        DXDarkModeCenter.dxDarkModeInterface = dXGlobalInitConfig.dxDarkModeInterface;
                    }
                    DXDarkModeCenter.enableDarkModeSupport = dXGlobalInitConfig.enableDarkModeSupport;
                    DXAppMonitor.trackerPerform(0, "DinamicX", DXMonitorConstant.DX_MONITOR_ENGINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_INIT_ENV, (DXTemplateItem) null, (Map<String, String>) null, (double) (System.nanoTime() - nanoTime), false);
                }
            }
        } catch (Throwable unused) {
        }
    }

    public DinamicXEngine(@NonNull DXEngineConfig dXEngineConfig) {
        super(new DXEngineContext(dXEngineConfig));
        if (dXEngineConfig != null || !isDebug()) {
            String str = null;
            if (!hasInitialize || getApplicationContext() == null) {
                if (!isDebug()) {
                    DXError dXError = new DXError(this.bizType);
                    DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_ENGINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_INIT, DXError.DXError_EngineInitContextNUll);
                    dXErrorInfo.reason = !hasInitialize ? "没有初始化" : "context == null";
                    String str2 = dXErrorInfo.reason;
                    dXError.dxErrorInfoList.add(dXErrorInfo);
                    DXAppMonitor.trackerError(dXError);
                    this.fullInitSuccess = false;
                    str = str2;
                } else {
                    throw new RuntimeException("DinamicX not initialize");
                }
            }
            try {
                this.engineContext.setEngine(this);
                this.parserMap = new DXLongSparseArray<>(DXGlobalCenter.globalParserMap);
                this.eventHandlerMap = new DXLongSparseArray<>(DXGlobalCenter.globalEventHandlerMap);
                this.widgetNodeMap = new DXLongSparseArray<>(DXGlobalCenter.globalWidgetNodeMap);
                this.dxControlEventCenter = new DXControlEventCenter();
                this.dxNotificationCenter = new DXNotificationCenter(this.config);
                this.dxTemplateManager = new DXTemplateManager(this.engineContext, context);
                this.dxTemplateManager.setUpMaxDowngradeCount(this.config.downgradeType);
                this.dxPipelineCacheManager = new DXPipelineCacheManager(this.engineContext);
                this.pipeline = new DXRenderPipeline(this.engineContext, this.dxTemplateManager);
            } catch (Throwable th) {
                this.fullInitSuccess = false;
                DXError dXError2 = new DXError(this.bizType);
                DXError.DXErrorInfo dXErrorInfo2 = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_ENGINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_INIT, DXError.DXError_EngineInitException);
                dXErrorInfo2.reason = "30011reason=" + str + "-" + DXExceptionUtil.getStackTrace(th);
                dXError2.dxErrorInfoList.add(dXErrorInfo2);
                DXAppMonitor.trackerError(dXError2);
            }
            processWindowChanged(false);
            initBindingX();
            initAsyncRenderManager();
            initTimerManager(dXEngineConfig);
            return;
        }
        throw new RuntimeException("DXEngineConfig cannot be null");
    }

    @Deprecated
    public boolean engineInitSuccess() {
        return this.fullInitSuccess;
    }

    private void initTimerManager(@NonNull DXEngineConfig dXEngineConfig) {
        try {
            this.dxTimerManager = new DXTimerManager(dXEngineConfig.getTickInterval());
        } catch (Throwable th) {
            DXExceptionUtil.printStack(th);
            DXAppMonitor.trackerError(this.bizType, (DXTemplateItem) null, DXMonitorConstant.DX_MONITOR_ENGINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_INIT, DXError.DXERROR_ENGINE_INIT_EXCEPTION_TIMER_ERROR, DXExceptionUtil.getStackTrace(th));
        }
    }

    public void registerTimerListener(DXTimerListener dXTimerListener, long j) {
        this.dxTimerManager.registerListener(dXTimerListener, j);
    }

    public void unregisterTimerListener(DXTimerListener dXTimerListener) {
        this.dxTimerManager.unregisterListener(dXTimerListener);
    }

    private void initAsyncRenderManager() {
        try {
            this.dxAsyncRenderManager = new DXAsyncRenderManager(this.engineContext);
        } catch (Throwable th) {
            DXAppMonitor.trackerError(this.bizType, (DXTemplateItem) null, DXMonitorConstant.DX_MONITOR_ASYNC_RENDER, DXMonitorConstant.DX_ASYNC_RENDER_INIT_CRASH, DXError.V3_ASYNC_RENDER_INIT_CRASH, DXExceptionUtil.getStackTrace(th));
        }
    }

    private void initBindingX() {
        try {
            this.bindingXManager = new DXBindingXManager(this.engineContext);
        } catch (Throwable th) {
            DXExceptionUtil.printStack(th);
            DXAppMonitor.trackerError(this.bizType, (DXTemplateItem) null, DXMonitorConstant.DX_MONITOR_BINDINGX, DXMonitorConstant.DX_BINDINGX_CRASH, DXError.BINDINGX_INIT_CRASH, DXExceptionUtil.getStackTrace(th));
        }
    }

    public DXResult<DXRootView> renderTemplate(DXRootView dXRootView, JSONObject jSONObject) {
        if (dXRootView != null) {
            try {
                if (!(dXRootView.getContext() == null || dXRootView.dxTemplateItem == null)) {
                    Context context2 = dXRootView.getContext();
                    DXTemplateItem dXTemplateItem = dXRootView.dxTemplateItem;
                    return renderTemplate(context2, jSONObject, dXRootView, DXScreenTool.getDefaultWidthSpec(), DXScreenTool.getDefaultHeightSpec(), (Object) null);
                }
            } catch (Throwable th) {
                if (isDebug()) {
                    th.printStackTrace();
                }
                DXError dXError = new DXError(this.bizType);
                if (dXRootView != null) {
                    dXError.dxTemplateItem = dXRootView.dxTemplateItem;
                }
                DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_ENGINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_RENDER, DXError.DXError_EngineRenderException);
                dXErrorInfo.reason = DXExceptionUtil.getStackTrace(th);
                dXError.dxErrorInfoList.add(dXErrorInfo);
                DXAppMonitor.trackerError(dXError);
                return new DXResult<>(dXError);
            }
        }
        DXError dXError2 = new DXError(this.bizType);
        DXError.DXErrorInfo dXErrorInfo2 = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_ENGINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_RENDER, DXError.DXError_EngineRenderException);
        dXErrorInfo2.reason = "dxRootView == null || dxRootView.getContext() == null || dxRootView.dxTemplateItem == null";
        dXError2.dxErrorInfoList.add(dXErrorInfo2);
        DXAppMonitor.trackerError(dXError2);
        return new DXResult<>(dXError2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.taobao.android.dinamicx.DXResult<com.taobao.android.dinamicx.DXRootView> renderTemplate(android.content.Context r10, com.alibaba.fastjson.JSONObject r11, @androidx.annotation.NonNull com.taobao.android.dinamicx.DXRootView r12, int r13, int r14, java.lang.Object r15) {
        /*
            r9 = this;
            r0 = 0
            com.taobao.android.dinamicx.template.download.DXTemplateItem r8 = r12.dxTemplateItem     // Catch:{ Throwable -> 0x0025 }
            com.taobao.android.dinamicx.DXRenderOptions$Builder r1 = new com.taobao.android.dinamicx.DXRenderOptions$Builder     // Catch:{ Throwable -> 0x0023 }
            r1.<init>()     // Catch:{ Throwable -> 0x0023 }
            com.taobao.android.dinamicx.DXRenderOptions$Builder r13 = r1.withWidthSpec(r13)     // Catch:{ Throwable -> 0x0023 }
            com.taobao.android.dinamicx.DXRenderOptions$Builder r13 = r13.withHeightSpec(r14)     // Catch:{ Throwable -> 0x0023 }
            com.taobao.android.dinamicx.DXRenderOptions$Builder r13 = r13.withObjectUserContext(r15)     // Catch:{ Throwable -> 0x0023 }
            r6 = -1
            com.taobao.android.dinamicx.DXRenderOptions r7 = r13.build()     // Catch:{ Throwable -> 0x0023 }
            r1 = r9
            r2 = r10
            r3 = r12
            r4 = r8
            r5 = r11
            com.taobao.android.dinamicx.DXResult r10 = r1.renderTemplate((android.content.Context) r2, (com.taobao.android.dinamicx.DXRootView) r3, (com.taobao.android.dinamicx.template.download.DXTemplateItem) r4, (com.alibaba.fastjson.JSONObject) r5, (int) r6, (com.taobao.android.dinamicx.DXRenderOptions) r7)     // Catch:{ Throwable -> 0x0023 }
            return r10
        L_0x0023:
            r10 = move-exception
            goto L_0x0027
        L_0x0025:
            r10 = move-exception
            r8 = r0
        L_0x0027:
            boolean r11 = isDebug()
            if (r11 == 0) goto L_0x0030
            r10.printStackTrace()
        L_0x0030:
            com.taobao.android.dinamicx.DXError r11 = new com.taobao.android.dinamicx.DXError
            java.lang.String r12 = r9.bizType
            r11.<init>(r12)
            r11.dxTemplateItem = r8
            com.taobao.android.dinamicx.DXError$DXErrorInfo r12 = new com.taobao.android.dinamicx.DXError$DXErrorInfo
            java.lang.String r13 = "Engine"
            java.lang.String r14 = "Engine_Render"
            r15 = 30004(0x7534, float:4.2045E-41)
            r12.<init>(r13, r14, r15)
            java.lang.String r10 = com.taobao.android.dinamicx.exception.DXExceptionUtil.getStackTrace(r10)
            r12.reason = r10
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r10 = r11.dxErrorInfoList
            r10.add(r12)
            com.taobao.android.dinamicx.monitor.DXAppMonitor.trackerError(r11)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.DinamicXEngine.renderTemplate(android.content.Context, com.alibaba.fastjson.JSONObject, com.taobao.android.dinamicx.DXRootView, int, int, java.lang.Object):com.taobao.android.dinamicx.DXResult");
    }

    public DXResult<DXRootView> renderTemplate(Context context2, DXRootView dXRootView, DXTemplateItem dXTemplateItem, JSONObject jSONObject, int i, DXRenderOptions dXRenderOptions) {
        try {
            long nanoTime = System.nanoTime();
            if (dXRenderOptions == null) {
                dXRenderOptions = DXRenderOptions.DEFAULT_RENDER_OPTIONS;
            }
            DXRuntimeContext makeRuntimeContext = makeRuntimeContext(context2, dXRootView, dXTemplateItem, jSONObject, this.pipeline, dXRenderOptions);
            if (this.dxAsyncRenderManager != null) {
                this.dxAsyncRenderManager.beforeRenderTemplate(makeRuntimeContext);
            }
            DXResult<DXRootView> renderInRootView = this.pipeline.renderInRootView(dXRootView, makeRuntimeContext, i, dXRenderOptions);
            if (isDebug() && renderInRootView != null && renderInRootView.hasError()) {
                DXLog.e("DinamicX", renderInRootView.getDxError().toString());
            }
            trackerPerform(dXTemplateItem, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_RENDER, System.nanoTime() - nanoTime, (Map<String, String>) null);
            return renderInRootView;
        } catch (Throwable th) {
            if (isDebug()) {
                th.printStackTrace();
            }
            DXError dXError = new DXError(this.bizType);
            dXError.dxTemplateItem = dXTemplateItem;
            DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_ENGINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_RENDER, DXError.DXError_EngineRenderException);
            dXErrorInfo.reason = DXExceptionUtil.getStackTrace(th);
            dXError.dxErrorInfoList.add(dXErrorInfo);
            DXAppMonitor.trackerError(dXError);
            return null;
        }
    }

    public void preRenderTemplate(Context context2, DXTemplateItem dXTemplateItem, JSONObject jSONObject, int i, DXRenderOptions dXRenderOptions) {
        if (this.dxAsyncRenderManager != null) {
            final DXRenderOptions dXRenderOptions2 = dXRenderOptions;
            final Context context3 = context2;
            final DXTemplateItem dXTemplateItem2 = dXTemplateItem;
            final JSONObject jSONObject2 = jSONObject;
            this.dxAsyncRenderManager.schedulePreRenderTemplate(new Runnable() {
                public void run() {
                    try {
                        DXRenderOptions dXRenderOptions = dXRenderOptions2 == null ? DXRenderOptions.DEFAULT_PRERENDER_OPTIONS : dXRenderOptions2;
                        DinamicXEngine.this.dxAsyncRenderManager.preRenderTemplate(DinamicXEngine.this.makeRuntimeContext(context3, (DXRootView) null, dXTemplateItem2, jSONObject2, (DXRenderPipeline) null, dXRenderOptions), dXRenderOptions, DinamicXEngine.this.dxTemplateManager, DinamicXEngine.this.dxPipelineCacheManager, DinamicXEngine.this.dxControlEventCenter);
                    } catch (Throwable th) {
                        DXExceptionUtil.printStack(th);
                    }
                }
            });
        }
    }

    public void clearPreRenderViewPoolCache() {
        DXViewPoolManager.getInstance().clearV3Cache(this.bizType);
    }

    public void prefetchTemplate(final Context context2, final JSONObject jSONObject, final DXTemplateItem dXTemplateItem, int i) {
        if (this.dxAsyncRenderManager != null) {
            this.dxAsyncRenderManager.schedulePrefetchTemplate(new Runnable() {
                public void run() {
                    try {
                        DXRenderOptions build = new DXRenderOptions.Builder().withPreType(1).withToStage(4).build();
                        DXRuntimeContext access$000 = DinamicXEngine.this.makeRuntimeContext(context2, (DXRootView) null, dXTemplateItem, jSONObject, (DXRenderPipeline) null, build);
                        DinamicXEngine.this.dxAsyncRenderManager.prefetchTemplate(access$000, build, DinamicXEngine.this.dxTemplateManager, DinamicXEngine.this.dxPipelineCacheManager, DinamicXEngine.this.dxControlEventCenter);
                    } catch (Throwable th) {
                        DXExceptionUtil.printStack(th);
                    }
                }
            });
        }
    }

    public DXResult<DXRootView> preCreateView(Context context2, DXTemplateItem dXTemplateItem) {
        DXRootView obtainV3View = DXViewPoolManager.getInstance().obtainV3View(context2, dXTemplateItem, this.bizType);
        if (obtainV3View == null) {
            return createView(context2, dXTemplateItem);
        }
        if (isDebug()) {
            DXLog.print("命中3.0预加载view:  " + dXTemplateItem.toString());
        }
        return new DXResult<>(obtainV3View);
    }

    public boolean registerDataParser(long j, IDXDataParser iDXDataParser) {
        if (j == 0 || iDXDataParser == null || this.parserMap == null) {
            return false;
        }
        this.parserMap.put(j, iDXDataParser);
        return true;
    }

    public boolean registerEventHandler(long j, IDXEventHandler iDXEventHandler) {
        if (j == 0 || this.eventHandlerMap == null || iDXEventHandler == null) {
            return false;
        }
        this.eventHandlerMap.put(j, iDXEventHandler);
        return true;
    }

    public boolean registerWidget(long j, IDXBuilderWidgetNode iDXBuilderWidgetNode) {
        if (j == 0 || iDXBuilderWidgetNode == null || this.widgetNodeMap == null) {
            return false;
        }
        this.widgetNodeMap.put(j, iDXBuilderWidgetNode);
        return true;
    }

    public void registerNotificationListener(IDXNotificationListener iDXNotificationListener) {
        if (iDXNotificationListener != null) {
            try {
                if (this.dxNotificationCenter != null) {
                    this.dxNotificationCenter.registerNotificationListener(iDXNotificationListener);
                }
            } catch (Throwable th) {
                DXError dXError = new DXError(this.config.bizType);
                DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_ENGINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_REGISTER_NOTIFICATION, DXError.DXERROR_REGISTER_NOTIFICATION_CRASH);
                dXErrorInfo.reason = DXExceptionUtil.getStackTrace(th);
                dXError.dxErrorInfoList.add(dXErrorInfo);
                DXAppMonitor.trackerError(dXError);
            }
        }
    }

    public void downLoadTemplates(List<DXTemplateItem> list) {
        try {
            this.dxTemplateManager.downloadTemplates(list);
        } catch (Throwable th) {
            if (isDebug()) {
                th.printStackTrace();
            }
            DXError dXError = new DXError(this.bizType);
            DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_ENGINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_DOWNLOAD, DXError.DXError_EngineDownloadException);
            dXErrorInfo.reason = "downLoadTemplates error " + DXExceptionUtil.getStackTrace(th);
            dXError.dxErrorInfoList.add(dXErrorInfo);
            DXAppMonitor.trackerError(dXError);
        }
    }

    public DXTemplateItem fetchTemplate(DXTemplateItem dXTemplateItem) {
        if (dXTemplateItem == null) {
            return null;
        }
        try {
            long nanoTime = System.nanoTime();
            DXTemplateItem fetchTemplate = this.dxTemplateManager.fetchTemplate(dXTemplateItem);
            trackerPerform(dXTemplateItem, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_FETCH, System.nanoTime() - nanoTime, (Map<String, String>) null);
            return fetchTemplate;
        } catch (Throwable th) {
            if (isDebug()) {
                th.printStackTrace();
            }
            DXError dXError = new DXError(this.bizType);
            DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_ENGINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_FETCH, DXError.DXError_EngineFetchException);
            dXError.dxTemplateItem = dXTemplateItem;
            dXErrorInfo.reason = DXExceptionUtil.getStackTrace(th);
            dXError.dxErrorInfoList.add(dXErrorInfo);
            DXAppMonitor.trackerError(dXError);
            return null;
        }
    }

    public DXResult<DXRootView> createView(Context context2, DXTemplateItem dXTemplateItem) {
        DXRootView dXRootView = new DXRootView(context2);
        dXRootView.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
        dXRootView.dxTemplateItem = dXTemplateItem;
        dXRootView.setBindingXManagerWeakReference(this.bindingXManager);
        return new DXResult<>(dXRootView);
    }

    public static Context getApplicationContext() {
        return context;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    /* access modifiers changed from: private */
    public DXRuntimeContext makeRuntimeContext(Context context2, DXRootView dXRootView, DXTemplateItem dXTemplateItem, JSONObject jSONObject, DXRenderPipeline dXRenderPipeline, DXRenderOptions dXRenderOptions) {
        DXRuntimeContext dXRuntimeContext = new DXRuntimeContext(this.engineContext);
        dXRuntimeContext.contextWeakReference = new WeakReference<>(context2);
        dXRuntimeContext.parserMap = this.parserMap;
        dXRuntimeContext.eventHandlerMap = this.eventHandlerMap;
        dXRuntimeContext.widgetNodeMap = this.widgetNodeMap;
        dXRuntimeContext.dxControlEventCenterWeakReference = new WeakReference<>(this.dxControlEventCenter);
        dXRuntimeContext.dxRenderPipelineWeakReference = new WeakReference<>(dXRenderPipeline);
        dXRuntimeContext.dxNotificationCenterWeakReference = new WeakReference<>(this.dxNotificationCenter);
        dXRuntimeContext.dxTemplateItem = dXTemplateItem;
        dXRuntimeContext.rootViewWeakReference = new WeakReference<>(dXRootView);
        dXRuntimeContext.setData(jSONObject);
        dXRuntimeContext.dxError = new DXError(this.bizType);
        dXRuntimeContext.dxError.dxTemplateItem = dXTemplateItem;
        if (dXRenderOptions != null) {
            dXRuntimeContext.dxUserContext = dXRenderOptions.getObjectUserContext();
            dXRuntimeContext.userContext = dXRenderOptions.getUserContext();
            dXRuntimeContext.renderType = dXRenderOptions.getRenderType();
            dXRuntimeContext.rootWidthSpec = dXRenderOptions.getWidthSpec();
            dXRuntimeContext.rootHeightSpec = dXRenderOptions.getHeightSpec();
        }
        return dXRuntimeContext;
    }

    private void trackerPerform(DXTemplateItem dXTemplateItem, String str, long j, Map<String, String> map) {
        DXAppMonitor.trackerPerform(0, this.bizType, DXMonitorConstant.DX_MONITOR_ENGINE, str, dXTemplateItem, map, (double) j, true);
    }

    public void reset() {
        if (this.dxPipelineCacheManager != null) {
            this.dxPipelineCacheManager.clearCache();
        }
        if (this.dxAsyncRenderManager != null) {
            this.dxAsyncRenderManager.reset();
        }
    }

    public DXResult<DXRootView> renderTemplateWithLifeCycle(Context context2, DXRootView dXRootView, DXTemplateItem dXTemplateItem, JSONObject jSONObject, int i) {
        beforeMountView(dXRootView, i);
        DXResult<DXRootView> renderTemplate = renderTemplate(context2, dXRootView, dXTemplateItem, jSONObject, i, DXRenderOptions.DEFAULT_RENDER_OPTIONS);
        afterMountView(dXRootView);
        return renderTemplate;
    }

    public void onResume() {
        if (this.dxAsyncRenderManager != null) {
            this.dxAsyncRenderManager.onResume();
        }
    }

    public void onStop() {
        if (this.dxAsyncRenderManager != null) {
            this.dxAsyncRenderManager.onStop();
        }
    }

    public void onDestroy() {
        if (this.dxAsyncRenderManager != null) {
            this.dxAsyncRenderManager.onDestroy();
        }
        if (!(this.bindingXManager == null || this.bindingXManager.getBindingX() == null)) {
            this.bindingXManager.getBindingX().onDestroy();
        }
        if (this.dxTimerManager != null) {
            this.dxTimerManager.onDestroy();
        }
    }

    public void cancelAllTasks() {
        if (this.dxAsyncRenderManager != null) {
            this.dxAsyncRenderManager.cancelAllTasks();
        }
    }

    public void onLowMemory() {
        reset();
    }

    public void onRootViewDisappear(DXRootView dXRootView) {
        if (dXRootView != null) {
            dXRootView.onRootViewDisappear(-1);
        }
    }

    public void onRootViewAppear(DXRootView dXRootView) {
        if (dXRootView != null) {
            dXRootView.onRootViewAppear(-1);
        }
    }

    public void onRootViewDisappear(DXRootView dXRootView, int i) {
        if (dXRootView != null) {
            dXRootView.onRootViewDisappear(i);
        }
    }

    public void onRootViewAppear(DXRootView dXRootView, int i) {
        if (dXRootView != null) {
            dXRootView.onRootViewAppear(i);
        }
    }

    public void postMessage(DXRootView dXRootView, Object obj) {
        DXWidgetNode expandWidgetNode;
        JSONObject jSONObject;
        try {
            if (obj instanceof JSONObject) {
                JSONObject jSONObject2 = (JSONObject) obj;
                String string = jSONObject2.getString("type");
                if (DXMsgConstant.DX_MSG_TYPE_BNDX.equalsIgnoreCase(string) && this.bindingXManager != null) {
                    this.bindingXManager.processDXMsg(dXRootView, jSONObject2);
                } else if (dXRootView != null && (expandWidgetNode = dXRootView.getExpandWidgetNode()) != null && (jSONObject = jSONObject2.getJSONObject("params")) != null) {
                    String string2 = jSONObject.getString(DXMsgConstant.DX_MSG_TARGET_ID);
                    DXMsgCenterEvent dXMsgCenterEvent = new DXMsgCenterEvent(DXHashConstant.DX_VIEW_EVENT_ON_MSG_CENTER_EVENT);
                    dXMsgCenterEvent.setParams(jSONObject);
                    dXMsgCenterEvent.setTargetId(string2);
                    dXMsgCenterEvent.setType(string);
                    DXWidgetNode queryWidgetNodeByUserId = expandWidgetNode.queryWidgetNodeByUserId(string2);
                    if (queryWidgetNodeByUserId == null) {
                        expandWidgetNode.sendBroadcastEvent(dXMsgCenterEvent);
                    } else {
                        queryWidgetNodeByUserId.postEvent(dXMsgCenterEvent);
                    }
                }
            }
        } catch (Throwable th) {
            DXExceptionUtil.printStack(th);
            String bizType = getBizType();
            if (TextUtils.isEmpty(bizType)) {
                bizType = "dinamicx";
            }
            DXAppMonitor.trackerError(bizType, (DXTemplateItem) null, DXMonitorConstant.DX_MONITOR_ENGINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_POST_MSG, DXError.ENGINE_POST_MSG_CRASH, DXExceptionUtil.getStackTrace(th));
        }
    }

    public void registerDXRootViewLifeCycle(DXRootView dXRootView, DXRootView.DXRootViewLifeCycle dXRootViewLifeCycle) {
        if (dXRootView != null) {
            dXRootView.registerDXRootViewLifeCycle(dXRootViewLifeCycle);
        }
    }

    public void registerDXRemoteTimeImpl(DXRemoteTimeInterface dXRemoteTimeInterface) {
        if (dXRemoteTimeInterface != null) {
            this.dxRemoteTimeInterface = dXRemoteTimeInterface;
        }
    }

    /* access modifiers changed from: protected */
    public DXRemoteTimeInterface getDxRemoteTimeInterface() {
        return this.dxRemoteTimeInterface;
    }

    public static void processWindowChanged(boolean z) {
        try {
            DXLog.d("DinamicX", "DinamicX processWindowChanged forceChange" + z);
            DXScreenTool.forceResetScreenSize(z);
        } catch (Exception e) {
            DXExceptionUtil.printStack(e);
        }
    }

    public static void commitSuccess(String str, String str2, @NonNull String str3, DXTemplateItem dXTemplateItem, Map<String, String> map) {
        DXAppMonitor.trackerPerform(0, str, str2, str3, dXTemplateItem, map, 0.0d, true);
    }

    public static void commitFail(String str, DXTemplateItem dXTemplateItem, List<DXError.DXErrorInfo> list) {
        DXError dXError = new DXError(str);
        dXError.dxTemplateItem = dXTemplateItem;
        dXError.dxErrorInfoList.addAll(list);
        DXAppMonitor.trackerError(dXError);
    }
}
