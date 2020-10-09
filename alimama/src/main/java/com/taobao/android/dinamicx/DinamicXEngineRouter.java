package com.taobao.android.dinamicx;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamic.DRegisterCenter;
import com.taobao.android.dinamic.DViewGenerator;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.dinamic.AbsDinamicEventHandler;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.exception.DinamicException;
import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.tempate.DTemplateManager;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.tempate.DinamicTemplateDownloaderCallback;
import com.taobao.android.dinamic.tempate.DownloadResult;
import com.taobao.android.dinamic.view.ViewResult;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.expression.parser.IDXDataParser;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.notification.IDXNotificationListener;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DinamicXEngineRouter extends DXBaseClass {
    private static final int V2_TEMPLATE = 20000;
    private static final int V3_TEMPLATE = 30000;
    DinamicXEngine engine;
    DTemplateManager templateManager = DTemplateManager.templateManagerWithModule(this.bizType);

    private boolean isValidaTemplate(DXTemplateItem dXTemplateItem) {
        return dXTemplateItem != null;
    }

    public DinamicXEngine getEngine() {
        return this.engine;
    }

    public static void initialize(@NonNull Context context, @Nullable DXGlobalInitConfig dXGlobalInitConfig, boolean z) {
        try {
            DinamicXEngine.initialize(context, dXGlobalInitConfig);
            Dinamic.init(context, z);
        } catch (Throwable unused) {
        }
    }

    public static Context getApplicationContext() {
        return DinamicXEngine.getApplicationContext();
    }

    public DinamicXEngineRouter(@NonNull DXEngineConfig dXEngineConfig) {
        super(dXEngineConfig);
        this.engine = new DinamicXEngine(dXEngineConfig);
        this.engineContext = this.engine.engineContext;
    }

    public void downLoadTemplates(List<DXTemplateItem> list) {
        try {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                DXTemplateItem dXTemplateItem = list.get(i);
                if (isV3Template(dXTemplateItem)) {
                    arrayList.add(dXTemplateItem);
                } else if (!TextUtils.isEmpty(dXTemplateItem.templateUrl) && dXTemplateItem.templateUrl.endsWith(".xml")) {
                    arrayList2.add(transformTemplateToV2(dXTemplateItem));
                }
            }
            if (this.templateManager != null && arrayList2.size() > 0) {
                this.templateManager.downloadTemplates(arrayList2, new DinamicTemplateDownloaderCallback() {
                    public void onDownloadFinish(DownloadResult downloadResult) {
                        if (downloadResult != null) {
                            DinamicXEngineRouter.this.engine.dxNotificationCenter.postNotification(DinamicXEngineRouter.this.transformTemplatesToV3(downloadResult.finishedTemplates), DinamicXEngineRouter.this.transformTemplatesToV3(downloadResult.failedTemplates));
                        }
                    }
                });
            }
            if (this.engine != null && arrayList.size() > 0) {
                this.engine.downLoadTemplates(arrayList);
            }
        } catch (Throwable th) {
            trackerError(DXMonitorConstant.DX_MONITOR_SERVICE_ID_ROUTER_DOWNLOAD, (DXTemplateItem) null, DXError.DXERROR_ROUTER_DOWNLOAD_TEMPLATE_EXCEPTION, DXExceptionUtil.getStackTrace(th), (Map<String, String>) null);
        }
    }

    public DXResult<DXRootView> createView(Context context, ViewGroup viewGroup, @NonNull DXTemplateItem dXTemplateItem) {
        try {
            if (!isValidaTemplate(dXTemplateItem)) {
                return new DXResult<>(trackerError(DXMonitorConstant.DX_MONITOR_SERVICE_ID_ROUTER_CREATE_VIEW, dXTemplateItem, DXError.DXERROR_ROUTER_CREATE_VIEW_EXCEPTION_TEMPLATE_NULL, "template is null ", (Map<String, String>) null));
            }
            if (isV3Template(dXTemplateItem)) {
                return this.engine.createView(context, dXTemplateItem);
            }
            ViewResult createView = DViewGenerator.viewGeneratorWithModule(this.bizType).createView(context, (ViewGroup) null, transformTemplateToV2(dXTemplateItem));
            DXRootView dXRootView = new DXRootView(context);
            DXResult<DXRootView> dXResult = new DXResult<>(dXRootView);
            if (createView == null) {
                dXResult.setDxError(trackerError(DXMonitorConstant.DX_MONITOR_SERVICE_ID_ROUTER_CREATE_VIEW, dXTemplateItem, DXError.DXERROR_ROUTER_CREATE_VIEW_EXCEPTION_V2_FAIL, "2.0 createView 失败 viewResult == null", (Map<String, String>) null));
                dXResult.setResult(null);
                return dXResult;
            }
            if (!createView.isRenderSuccess()) {
                dXResult.setDxError(trackerError(DXMonitorConstant.DX_MONITOR_SERVICE_ID_ROUTER_CREATE_VIEW, dXTemplateItem, DXError.DXERROR_ROUTER_CREATE_VIEW_EXCEPTION_V2_FAIL, "2.0 createView 失败", createView.getDinamicError().getErrorMap()));
                if (createView.getView() == null) {
                    dXResult.setResult(null);
                    return dXResult;
                }
            }
            ViewGroup.LayoutParams layoutParams = createView.getView().getLayoutParams();
            if (layoutParams != null) {
                dXRootView.setLayoutParams(layoutParams);
            } else {
                dXRootView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            }
            dXRootView.setV2(true);
            dXRootView.dxTemplateItem = dXTemplateItem;
            dXRootView.addView(createView.getView());
            createView.setView(dXRootView);
            return dXResult;
        } catch (Throwable th) {
            if (DinamicXEngine.isDebug()) {
                th.printStackTrace();
            }
            return new DXResult<>(trackerError(DXMonitorConstant.DX_MONITOR_SERVICE_ID_ROUTER_CREATE_VIEW, dXTemplateItem, DXError.DXERROR_ROUTER_CREATE_VIEW_EXCEPTION, DXExceptionUtil.getStackTrace(th), (Map<String, String>) null));
        }
    }

    public DXResult<DXRootView> renderTemplate(DXRootView dXRootView, JSONObject jSONObject) {
        if (dXRootView != null) {
            try {
                if (!(dXRootView.getContext() == null || dXRootView.dxTemplateItem == null)) {
                    Context context = dXRootView.getContext();
                    DXTemplateItem dXTemplateItem = dXRootView.dxTemplateItem;
                    return renderTemplate(context, jSONObject, dXRootView, DXScreenTool.getDefaultWidthSpec(), DXScreenTool.getDefaultHeightSpec(), (Object) null);
                }
            } catch (Throwable th) {
                if (DinamicXEngine.isDebug()) {
                    th.printStackTrace();
                }
                DXTemplateItem dXTemplateItem2 = null;
                if (dXRootView != null) {
                    dXTemplateItem2 = dXRootView.dxTemplateItem;
                }
                return new DXResult<>(trackerError(DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_RENDER, dXTemplateItem2, DXError.DXError_EngineRenderException, DXExceptionUtil.getStackTrace(th), (Map<String, String>) null));
            }
        }
        return new DXResult<>(trackerError(DXMonitorConstant.DX_MONITOR_SERVICE_ID_ENGINE_RENDER, (DXTemplateItem) null, DXError.DXError_EngineRenderException_NULL, "dxRootView == null || dxRootView.getContext() == null || dxRootView.dxTemplateItem == null", (Map<String, String>) null));
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x008b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.taobao.android.dinamicx.DXResult<com.taobao.android.dinamicx.DXRootView> renderTemplate(android.content.Context r10, com.alibaba.fastjson.JSONObject r11, com.taobao.android.dinamicx.DXRootView r12, int r13, int r14, java.lang.Object r15) {
        /*
            r9 = this;
            com.taobao.android.dinamicx.template.download.DXTemplateItem r0 = r12.dxTemplateItem     // Catch:{ Throwable -> 0x0082 }
            boolean r1 = r9.isValidaTemplate(r0)     // Catch:{ Throwable -> 0x007f }
            if (r1 != 0) goto L_0x001b
            java.lang.String r11 = "Router_Render"
            r13 = 20006(0x4e26, float:2.8034E-41)
            java.lang.String r14 = "template is null "
            r15 = 0
            r10 = r9
            r12 = r0
            com.taobao.android.dinamicx.DXError r10 = r10.trackerError(r11, r12, r13, r14, r15)     // Catch:{ Throwable -> 0x007f }
            com.taobao.android.dinamicx.DXResult r11 = new com.taobao.android.dinamicx.DXResult     // Catch:{ Throwable -> 0x007f }
            r11.<init>((com.taobao.android.dinamicx.DXError) r10)     // Catch:{ Throwable -> 0x007f }
            return r11
        L_0x001b:
            boolean r1 = r9.isV3Template(r0)     // Catch:{ Throwable -> 0x007f }
            if (r1 == 0) goto L_0x002e
            com.taobao.android.dinamicx.DinamicXEngine r2 = r9.engine     // Catch:{ Throwable -> 0x007f }
            r3 = r10
            r4 = r11
            r5 = r12
            r6 = r13
            r7 = r14
            r8 = r15
            com.taobao.android.dinamicx.DXResult r10 = r2.renderTemplate((android.content.Context) r3, (com.alibaba.fastjson.JSONObject) r4, (com.taobao.android.dinamicx.DXRootView) r5, (int) r6, (int) r7, (java.lang.Object) r8)     // Catch:{ Throwable -> 0x007f }
            return r10
        L_0x002e:
            java.lang.String r10 = r9.bizType     // Catch:{ Throwable -> 0x007f }
            com.taobao.android.dinamic.DViewGenerator r10 = com.taobao.android.dinamic.DViewGenerator.viewGeneratorWithModule(r10)     // Catch:{ Throwable -> 0x007f }
            com.taobao.android.dinamic.view.ViewResult r1 = r10.bindData(r12, r11, r15)     // Catch:{ Throwable -> 0x007f }
            if (r1 != 0) goto L_0x004d
            java.lang.String r11 = "Router_Render"
            r13 = 20006(0x4e26, float:2.8034E-41)
            java.lang.String r14 = "2.0 render 失败"
            r15 = 0
            r10 = r9
            r12 = r0
            com.taobao.android.dinamicx.DXError r10 = r10.trackerError(r11, r12, r13, r14, r15)     // Catch:{ Throwable -> 0x007f }
            com.taobao.android.dinamicx.DXResult r11 = new com.taobao.android.dinamicx.DXResult     // Catch:{ Throwable -> 0x007f }
            r11.<init>((com.taobao.android.dinamicx.DXError) r10)     // Catch:{ Throwable -> 0x007f }
            return r11
        L_0x004d:
            boolean r10 = r1.isBindDataSuccess()     // Catch:{ Throwable -> 0x007f }
            if (r10 != 0) goto L_0x0073
            java.lang.String r11 = "Router_Render"
            r13 = 20006(0x4e26, float:2.8034E-41)
            java.lang.String r14 = "2.0 render 失败"
            com.taobao.android.dinamic.view.DinamicError r10 = r1.getDinamicError()     // Catch:{ Throwable -> 0x007f }
            java.util.HashMap r15 = r10.getErrorMap()     // Catch:{ Throwable -> 0x007f }
            r10 = r9
            r12 = r0
            com.taobao.android.dinamicx.DXError r10 = r10.trackerError(r11, r12, r13, r14, r15)     // Catch:{ Throwable -> 0x007f }
            com.taobao.android.dinamicx.DXResult r11 = new com.taobao.android.dinamicx.DXResult     // Catch:{ Throwable -> 0x007f }
            android.view.View r12 = r1.getView()     // Catch:{ Throwable -> 0x007f }
            com.taobao.android.dinamicx.DXRootView r12 = (com.taobao.android.dinamicx.DXRootView) r12     // Catch:{ Throwable -> 0x007f }
            r11.<init>(r12, r10)     // Catch:{ Throwable -> 0x007f }
            return r11
        L_0x0073:
            com.taobao.android.dinamicx.DXResult r10 = new com.taobao.android.dinamicx.DXResult     // Catch:{ Throwable -> 0x007f }
            android.view.View r11 = r1.getView()     // Catch:{ Throwable -> 0x007f }
            com.taobao.android.dinamicx.DXRootView r11 = (com.taobao.android.dinamicx.DXRootView) r11     // Catch:{ Throwable -> 0x007f }
            r10.<init>(r11)     // Catch:{ Throwable -> 0x007f }
            return r10
        L_0x007f:
            r10 = move-exception
            r2 = r0
            goto L_0x0085
        L_0x0082:
            r10 = move-exception
            r0 = 0
            r2 = r0
        L_0x0085:
            boolean r11 = com.taobao.android.dinamicx.DinamicXEngine.isDebug()
            if (r11 == 0) goto L_0x008e
            r10.printStackTrace()
        L_0x008e:
            java.lang.String r1 = "Router_Render"
            r3 = 200014(0x30d4e, float:2.8028E-40)
            java.lang.String r4 = com.taobao.android.dinamicx.exception.DXExceptionUtil.getStackTrace(r10)
            r5 = 0
            r0 = r9
            com.taobao.android.dinamicx.DXError r10 = r0.trackerError(r1, r2, r3, r4, r5)
            com.taobao.android.dinamicx.DXResult r11 = new com.taobao.android.dinamicx.DXResult
            r11.<init>((com.taobao.android.dinamicx.DXError) r10)
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.DinamicXEngineRouter.renderTemplate(android.content.Context, com.alibaba.fastjson.JSONObject, com.taobao.android.dinamicx.DXRootView, int, int, java.lang.Object):com.taobao.android.dinamicx.DXResult");
    }

    public DXTemplateItem fetchTemplate(DXTemplateItem dXTemplateItem) {
        try {
            if (!isValidaTemplate(dXTemplateItem)) {
                return null;
            }
            if (!isV3Template(dXTemplateItem) || this.engine == null) {
                DXTemplateItem transformTemplateToV3 = transformTemplateToV3(this.templateManager.fetchExactTemplate(transformTemplateToV2(dXTemplateItem)));
                if (transformTemplateToV3 != null) {
                    transformTemplateToV3.setFileVersion(20000);
                }
                return transformTemplateToV3;
            }
            DXTemplateItem fetchTemplate = this.engine.fetchTemplate(dXTemplateItem);
            if (fetchTemplate != null) {
                fetchTemplate.setFileVersion(30000);
            }
            return fetchTemplate;
        } catch (Throwable th) {
            if (DinamicXEngine.isDebug()) {
                th.printStackTrace();
            }
            trackerError(DXMonitorConstant.DX_MONITOR_SERVICE_ID_ROUTER_FETCH, dXTemplateItem, DXError.DXERROR_ROUTER_FETCH_TEMPLATE_EXCEPTION, DXExceptionUtil.getStackTrace(th), (Map<String, String>) null);
            return null;
        }
    }

    public boolean registerDataParser(long j, IDXDataParser iDXDataParser) {
        if (this.engine != null) {
            return this.engine.registerDataParser(j, iDXDataParser);
        }
        return false;
    }

    public boolean registerEventHandler(long j, IDXEventHandler iDXEventHandler) {
        if (this.engine != null) {
            return this.engine.registerEventHandler(j, iDXEventHandler);
        }
        return false;
    }

    public boolean registerWidget(long j, IDXBuilderWidgetNode iDXBuilderWidgetNode) {
        if (this.engine != null) {
            return this.engine.registerWidget(j, iDXBuilderWidgetNode);
        }
        return false;
    }

    public void registerNotificationListener(IDXNotificationListener iDXNotificationListener) {
        if (this.engine != null) {
            this.engine.registerNotificationListener(iDXNotificationListener);
        }
    }

    private boolean isV3Template(DXTemplateItem dXTemplateItem) {
        if (dXTemplateItem == null) {
            return false;
        }
        if (dXTemplateItem.getFileVersion() == 30000) {
            return true;
        }
        if (dXTemplateItem.getFileVersion() == 20000) {
            return false;
        }
        if (TextUtils.isEmpty(dXTemplateItem.templateUrl) || !dXTemplateItem.templateUrl.endsWith(".zip")) {
            return TextUtils.isEmpty(dXTemplateItem.templateUrl) && dXTemplateItem.version >= 0;
        }
        return true;
    }

    public DinamicTemplate transformTemplateToV2(DXTemplateItem dXTemplateItem) {
        if (dXTemplateItem == null) {
            return null;
        }
        try {
            DinamicTemplate dinamicTemplate = new DinamicTemplate();
            dinamicTemplate.name = dXTemplateItem.name;
            if (dXTemplateItem.version >= 0) {
                dinamicTemplate.version = dXTemplateItem.version + "";
            }
            dinamicTemplate.templateUrl = dXTemplateItem.templateUrl;
            return dinamicTemplate;
        } catch (Throwable th) {
            if (DinamicXEngine.isDebug()) {
                th.printStackTrace();
            }
            trackerError(DXMonitorConstant.DX_MONITOR_SERVICE_ID_ROUTER_TRANSFORM_TEMPLATE, (DXTemplateItem) null, DXError.DXERROR_ROUTER_TRASFORM_TEMPLATE_TOV2_EXCEPTION, "transformTemplateToV3 error:" + DXExceptionUtil.getStackTrace(th), (Map<String, String>) null);
            return null;
        }
    }

    public List<DXTemplateItem> transformTemplatesToV3(List<DinamicTemplate> list) {
        try {
            ArrayList arrayList = new ArrayList();
            if (list != null && list.size() > 0) {
                for (DinamicTemplate transformTemplateToV3 : list) {
                    DXTemplateItem transformTemplateToV32 = transformTemplateToV3(transformTemplateToV3);
                    if (transformTemplateToV32 != null) {
                        arrayList.add(transformTemplateToV32);
                    }
                }
            }
            return arrayList;
        } catch (Throwable th) {
            if (DinamicXEngine.isDebug()) {
                th.printStackTrace();
            }
            trackerError(DXMonitorConstant.DX_MONITOR_SERVICE_ID_ROUTER_TRANSFORM_TEMPLATE, (DXTemplateItem) null, DXError.DXERROR_ROUTER_TRASFORM_TEMPLATE_TOV3_EXCEPTION, "transformTemplateToV3 error:" + DXExceptionUtil.getStackTrace(th), (Map<String, String>) null);
            return null;
        }
    }

    public DXTemplateItem transformTemplateToV3(DinamicTemplate dinamicTemplate) {
        if (dinamicTemplate == null) {
            return null;
        }
        try {
            DXTemplateItem dXTemplateItem = new DXTemplateItem();
            dXTemplateItem.name = dinamicTemplate.name;
            if (!TextUtils.isEmpty(dinamicTemplate.version)) {
                dXTemplateItem.version = Long.parseLong(dinamicTemplate.version);
            } else {
                dXTemplateItem.version = -1;
            }
            dXTemplateItem.templateUrl = dinamicTemplate.templateUrl;
            return dXTemplateItem;
        } catch (Throwable th) {
            if (DinamicXEngine.isDebug()) {
                th.printStackTrace();
            }
            HashMap hashMap = new HashMap();
            hashMap.put("templateName", dinamicTemplate.name);
            hashMap.put("templateVersion", dinamicTemplate.version);
            hashMap.put("templateUrl", dinamicTemplate.templateUrl);
            trackerError(DXMonitorConstant.DX_MONITOR_SERVICE_ID_ROUTER_TRANSFORM_TEMPLATE, (DXTemplateItem) null, DXError.DXERROR_ROUTER_TRASFORM_TEMPLATE_TOV3_EXCEPTION, "transformTemplateToV3 error:" + DXExceptionUtil.getStackTrace(th), hashMap);
            return null;
        }
    }

    private DXError trackerError(String str, DXTemplateItem dXTemplateItem, int i, String str2, Map<String, String> map) {
        DXError dXError = new DXError(this.bizType);
        dXError.dxTemplateItem = dXTemplateItem;
        DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_ROUTER, str, i);
        dXErrorInfo.reason = str2;
        dXErrorInfo.extraParams = map;
        dXError.dxErrorInfoList.add(dXErrorInfo);
        DXAppMonitor.trackerError(dXError);
        return dXError;
    }

    public void v2RegisterView(String str, DinamicViewAdvancedConstructor dinamicViewAdvancedConstructor) throws DinamicException {
        DRegisterCenter.shareCenter().registerViewConstructor(str, dinamicViewAdvancedConstructor);
    }

    public void v2RegisterEventHandler(String str, AbsDinamicEventHandler absDinamicEventHandler) throws DinamicException {
        DRegisterCenter.shareCenter().registerEventHandler(str, absDinamicEventHandler);
    }

    public void v2RegisterParser(String str, AbsDinamicDataParser absDinamicDataParser) throws DinamicException {
        DRegisterCenter.shareCenter().registerDataParser(str, absDinamicDataParser);
    }
}
