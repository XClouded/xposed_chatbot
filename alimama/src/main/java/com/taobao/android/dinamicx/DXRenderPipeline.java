package com.taobao.android.dinamicx;

import android.view.View;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXRenderOptions;
import com.taobao.android.dinamicx.bindingx.DXBindingXManager;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.notification.DXNotificationCenter;
import com.taobao.android.dinamicx.notification.DXTemplateUpdateRequest;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.thread.DXRunnableManager;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.event.DXControlEvent;
import com.taobao.android.dinamicx.widget.event.DXControlEventCenter;
import com.taobao.android.dinamicx.widget.event.DXPipelineScheduleEvent;
import com.taobao.android.dinamicx.widget.event.IDXControlEventListener;
import java.lang.ref.WeakReference;
import java.util.Map;

public class DXRenderPipeline extends DXRenderPipelineBase implements IDXControlEventListener {
    WeakReference<DXControlEventCenter> dxControlEventCenterWeakReference;
    DXLayoutManager dxLayoutManager = new DXLayoutManager();
    WeakReference<DXPipelineCacheManager> dxPipelineCacheManagerWeakReference;
    DXRenderManager dxRenderManager = new DXRenderManager();
    DXTemplateManager dxTemplateManager;
    DXTemplateParser dxTemplateParser = new DXTemplateParser();
    DXNotificationCenter notificationCenter;

    DXRenderPipeline(DXEngineContext dXEngineContext, DXTemplateManager dXTemplateManager) {
        super(dXEngineContext);
        DinamicXEngine engine = dXEngineContext.getEngine();
        if (engine != null) {
            this.notificationCenter = engine.dxNotificationCenter;
            this.dxTemplateManager = dXTemplateManager;
            this.dxControlEventCenterWeakReference = new WeakReference<>(engine.dxControlEventCenter);
            this.dxPipelineCacheManagerWeakReference = new WeakReference<>(engine.dxPipelineCacheManager);
            registerControlEvents();
        }
    }

    public DXTemplateManager getTemplateManager() {
        return this.dxTemplateManager;
    }

    public DXControlEventCenter getControlEventCenter() {
        return (DXControlEventCenter) this.dxControlEventCenterWeakReference.get();
    }

    public DXPipelineCacheManager getPipelineCacheManager() {
        return (DXPipelineCacheManager) this.dxPipelineCacheManagerWeakReference.get();
    }

    public DXResult<DXRootView> renderInRootView(DXRootView dXRootView, DXRuntimeContext dXRuntimeContext, int i, DXRenderOptions dXRenderOptions) {
        if (dXRootView == null) {
            return null;
        }
        resetBindingXAnimation(dXRootView);
        dXRootView.data = dXRuntimeContext.getData();
        dXRootView.setPosition(i);
        dXRootView.parentWidthSpec = dXRenderOptions.getWidthSpec();
        dXRootView.parentHeightSpec = dXRenderOptions.getHeightSpec();
        dXRootView.dxTemplateItem = dXRuntimeContext.getDxTemplateItem();
        View renderWidget = renderWidget((DXWidgetNode) null, dXRootView.getFlattenWidgetNode(), dXRootView, dXRuntimeContext, dXRenderOptions);
        DXResult<DXRootView> dXResult = new DXResult<>();
        if (renderWidget != null && (renderWidget instanceof DXRootView)) {
            dXResult.setResult((DXRootView) renderWidget);
        }
        dXResult.setDxError(dXRuntimeContext.getDxError());
        return dXResult;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: IfRegionVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Don't wrap MOVE or CONST insns: 0x02f7: MOVE  (r11v6 com.taobao.android.dinamicx.DXPipelineCacheManager$DXPipelineCacheObj) = 
          (r23v0 com.taobao.android.dinamicx.DXPipelineCacheManager$DXPipelineCacheObj)
        
        	at jadx.core.dex.instructions.args.InsnArg.wrapArg(InsnArg.java:164)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.assignInline(CodeShrinkVisitor.java:133)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.checkInline(CodeShrinkVisitor.java:118)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkBlock(CodeShrinkVisitor.java:65)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkMethod(CodeShrinkVisitor.java:43)
        	at jadx.core.dex.visitors.regions.TernaryMod.makeTernaryInsn(TernaryMod.java:122)
        	at jadx.core.dex.visitors.regions.TernaryMod.visitRegion(TernaryMod.java:34)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:73)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterative(DepthRegionTraversal.java:27)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.visit(IfRegionVisitor.java:31)
        */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0043 A[ExcHandler: Throwable (th java.lang.Throwable), PHI: r7 
  PHI: (r7v32 android.view.View) = (r7v16 android.view.View), (r7v16 android.view.View), (r7v6 android.view.View), (r7v29 android.view.View), (r7v0 android.view.View) binds: [B:66:0x015b, B:83:0x01bd, B:28:0x0069, B:39:0x009c, B:8:0x002a] A[DONT_GENERATE, DONT_INLINE], Splitter:B:8:0x002a] */
    /* JADX WARNING: Removed duplicated region for block: B:169:0x0352  */
    /* JADX WARNING: Removed duplicated region for block: B:178:0x036a A[Catch:{ all -> 0x03d8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x03d0  */
    /* JADX WARNING: Removed duplicated region for block: B:209:0x0428  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0052 A[SYNTHETIC, Splitter:B:20:0x0052] */
    /* JADX WARNING: Removed duplicated region for block: B:224:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00a5 A[Catch:{ Throwable -> 0x0043, all -> 0x0040 }, ExcHandler: all (th java.lang.Throwable), Splitter:B:28:0x0069] */
    public android.view.View renderWidget(com.taobao.android.dinamicx.widget.DXWidgetNode r25, com.taobao.android.dinamicx.widget.DXWidgetNode r26, android.view.View r27, com.taobao.android.dinamicx.DXRuntimeContext r28, com.taobao.android.dinamicx.DXRenderOptions r29) {
        /*
            r24 = this;
            r8 = r24
            r7 = r27
            r9 = r28
            r10 = r29
            boolean r0 = r8.checkNeedCancel(r10)
            r11 = 0
            if (r0 == 0) goto L_0x0010
            return r11
        L_0x0010:
            int r0 = r29.getFromStage()     // Catch:{ Throwable -> 0x0361, all -> 0x035c }
            int r13 = r29.getToStage()     // Catch:{ Throwable -> 0x0361, all -> 0x035c }
            int r14 = r29.getWidthSpec()     // Catch:{ Throwable -> 0x0361, all -> 0x035c }
            int r15 = r29.getHeightSpec()     // Catch:{ Throwable -> 0x0361, all -> 0x035c }
            com.taobao.android.dinamicx.DXPipelineCacheManager r6 = r24.getPipelineCacheManager()     // Catch:{ Throwable -> 0x0361, all -> 0x035c }
            r16 = 0
            if (r6 == 0) goto L_0x0047
            r1 = r25
            boolean r2 = r6.needReadExpandedWidgetNode(r1, r10)     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            if (r2 == 0) goto L_0x0049
            com.taobao.android.dinamicx.widget.DXWidgetNode r2 = r6.readExpandedWidgetNode(r9, r7)     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            if (r2 == 0) goto L_0x0049
            r0 = 5
            r1 = 1
            r5 = r26
            r4 = r2
            r3 = r7
            r2 = r11
            r17 = 1
            goto L_0x0050
        L_0x0040:
            r0 = move-exception
            goto L_0x03da
        L_0x0043:
            r0 = move-exception
        L_0x0044:
            r11 = r7
            goto L_0x0364
        L_0x0047:
            r1 = r25
        L_0x0049:
            r5 = r26
            r4 = r1
            r3 = r7
            r2 = r11
            r17 = 0
        L_0x0050:
            if (r0 > r13) goto L_0x02e1
            boolean r1 = r8.checkNeedCancel(r10)     // Catch:{ Throwable -> 0x02dc, all -> 0x02d7 }
            if (r1 != 0) goto L_0x02e1
            r1 = 7
            if (r0 == r1) goto L_0x028d
            switch(r0) {
                case 0: goto L_0x0153;
                case 1: goto L_0x0153;
                case 2: goto L_0x0129;
                case 3: goto L_0x0106;
                case 4: goto L_0x00b4;
                case 5: goto L_0x0067;
                default: goto L_0x005e;
            }
        L_0x005e:
            r23 = r2
            r1 = r3
            r10 = r5
            r12 = r6
            r7 = r11
            r11 = r4
            goto L_0x02c8
        L_0x0067:
            if (r4 == 0) goto L_0x00ac
            long r18 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x00a9, all -> 0x00a5 }
            com.taobao.android.dinamicx.DXEngineConfig r1 = r8.config     // Catch:{ Throwable -> 0x00a9, all -> 0x00a5 }
            if (r1 == 0) goto L_0x007c
            com.taobao.android.dinamicx.DXEngineConfig r1 = r8.config     // Catch:{ Throwable -> 0x0078, all -> 0x00a5 }
            boolean r1 = r1.isDisabledFlatten()     // Catch:{ Throwable -> 0x0078, all -> 0x00a5 }
            goto L_0x007d
        L_0x0078:
            r0 = move-exception
            r11 = r3
            goto L_0x0364
        L_0x007c:
            r1 = 0
        L_0x007d:
            com.taobao.android.dinamicx.DXLayoutManager r5 = r8.dxLayoutManager     // Catch:{ Throwable -> 0x00a9, all -> 0x00a5 }
            com.taobao.android.dinamicx.widget.DXFrameLayoutWidgetNode r20 = r5.performFlatten(r4, r9, r1)     // Catch:{ Throwable -> 0x00a9, all -> 0x00a5 }
            java.lang.String r5 = "Pipeline_Stage_FLatten_Widget"
            long r21 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x00a9, all -> 0x00a5 }
            r1 = 0
            long r18 = r21 - r18
            r21 = 0
            r1 = r24
            r23 = r2
            r2 = r28
            r7 = r3
            r3 = r5
            r11 = r4
            r4 = r18
            r12 = r6
            r6 = r21
            r1.trackerPerform(r2, r3, r4, r6)     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            r3 = r7
            r4 = r11
            r5 = r20
            goto L_0x014e
        L_0x00a5:
            r0 = move-exception
            r7 = r3
            goto L_0x03da
        L_0x00a9:
            r0 = move-exception
            r7 = r3
            goto L_0x0044
        L_0x00ac:
            r23 = r2
            r11 = r4
            r12 = r6
            r1 = r3
            r10 = r5
            goto L_0x028b
        L_0x00b4:
            r23 = r2
            r7 = r3
            r11 = r4
            r12 = r6
            if (r11 == 0) goto L_0x0103
            long r1 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            com.taobao.android.dinamicx.DXLayoutManager r3 = r8.dxLayoutManager     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            r3.performLayout(r11, r9)     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            boolean r3 = r28.hasError()     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            if (r3 == 0) goto L_0x00e5
            com.taobao.android.dinamicx.DXError r3 = new com.taobao.android.dinamicx.DXError     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            java.lang.String r4 = r28.getBizType()     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            r3.<init>(r4)     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            com.taobao.android.dinamicx.template.download.DXTemplateItem r4 = r28.getDxTemplateItem()     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            r3.dxTemplateItem = r4     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r4 = r3.dxErrorInfoList     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            com.taobao.android.dinamicx.DXError r6 = r28.getDxError()     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r6 = r6.dxErrorInfoList     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            r4.addAll(r6)     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            goto L_0x00e6
        L_0x00e5:
            r3 = 0
        L_0x00e6:
            if (r12 == 0) goto L_0x00ee
            com.taobao.android.dinamicx.DXPipelineCacheManager$DXPipelineCacheObj r3 = r12.buildPipelineCacheObj(r11, r3)     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            r23 = r3
        L_0x00ee:
            java.lang.String r3 = "Pipeline_Stage_Layout_Widget"
            long r18 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            r4 = 0
            long r18 = r18 - r1
            r6 = 0
            r1 = r24
            r2 = r28
            r10 = r5
            r4 = r18
            r1.trackerPerform(r2, r3, r4, r6)     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            goto L_0x014b
        L_0x0103:
            r10 = r5
            goto L_0x028a
        L_0x0106:
            r23 = r2
            r7 = r3
            r11 = r4
            r10 = r5
            r12 = r6
            if (r11 == 0) goto L_0x028a
            long r1 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            com.taobao.android.dinamicx.DXLayoutManager r3 = r8.dxLayoutManager     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            r3.performMeasure(r11, r14, r15, r9)     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            java.lang.String r3 = "Pipeline_Stage_Measure_Widget"
            long r4 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            r6 = 0
            long r4 = r4 - r1
            r6 = 0
            r1 = r24
            r2 = r28
            r1.trackerPerform(r2, r3, r4, r6)     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            goto L_0x028a
        L_0x0129:
            r23 = r2
            r7 = r3
            r11 = r4
            r10 = r5
            r12 = r6
            if (r11 == 0) goto L_0x028a
            long r1 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            com.taobao.android.dinamicx.DXTemplateParser r3 = r8.dxTemplateParser     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            com.taobao.android.dinamicx.widget.DXWidgetNode r11 = r3.parseWT(r11, r9)     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            java.lang.String r3 = "Pipeline_Stage_Parse_Widget"
            long r4 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
            r6 = 0
            long r4 = r4 - r1
            r6 = 0
            r1 = r24
            r2 = r28
            r1.trackerPerform(r2, r3, r4, r6)     // Catch:{ Throwable -> 0x0043, all -> 0x0040 }
        L_0x014b:
            r3 = r7
            r5 = r10
            r4 = r11
        L_0x014e:
            r2 = r23
            r7 = 0
            goto L_0x02cd
        L_0x0153:
            r23 = r2
            r7 = r3
            r11 = r4
            r10 = r5
            r12 = r6
            if (r11 != 0) goto L_0x028a
            long r1 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0043, all -> 0x0286 }
            com.taobao.android.dinamicx.DXTemplateManager r3 = r24.getTemplateManager()     // Catch:{ Throwable -> 0x0043, all -> 0x0286 }
            if (r3 != 0) goto L_0x01bd
            boolean r0 = r7 instanceof com.taobao.android.dinamicx.DXRootView
            if (r0 == 0) goto L_0x01ae
            int r0 = r29.getRenderType()
            if (r0 != 0) goto L_0x01ae
            r3 = r7
            com.taobao.android.dinamicx.DXRootView r3 = (com.taobao.android.dinamicx.DXRootView) r3
            if (r3 == 0) goto L_0x01ae
            int r0 = r3.getChildCount()
            if (r0 != 0) goto L_0x01ae
            boolean r0 = r28.hasError()
            if (r0 == 0) goto L_0x01ae
            com.taobao.android.dinamicx.DXError r0 = new com.taobao.android.dinamicx.DXError
            java.lang.String r1 = r8.bizType
            r0.<init>(r1)
            com.taobao.android.dinamicx.template.download.DXTemplateItem r1 = r28.getDxTemplateItem()
            r0.dxTemplateItem = r1
            com.taobao.android.dinamicx.DXError$DXErrorInfo r1 = new com.taobao.android.dinamicx.DXError$DXErrorInfo
            java.lang.String r2 = "Pipeline"
            java.lang.String r3 = "Pipeline_Stage_Render_Error_Downgrade"
            r4 = 90004(0x15f94, float:1.26122E-40)
            r1.<init>(r2, r3, r4)
            com.taobao.android.dinamicx.DXError r2 = r28.getDxError()
            java.lang.String r2 = r2.toString()
            r1.reason = r2
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r2 = r0.dxErrorInfoList
            r2.add(r1)
            com.taobao.android.dinamicx.monitor.DXAppMonitor.trackerError(r0)
            r8.executeDowngrade(r9)
        L_0x01ae:
            boolean r0 = r28.hasError()
            if (r0 == 0) goto L_0x01bb
            com.taobao.android.dinamicx.DXError r0 = r28.getDxError()
            com.taobao.android.dinamicx.monitor.DXAppMonitor.trackerError(r0)
        L_0x01bb:
            r0 = 0
            return r0
        L_0x01bd:
            com.taobao.android.dinamicx.widget.DXWidgetNode r3 = r3.getTemplateWT(r9)     // Catch:{ Throwable -> 0x0043, all -> 0x0286 }
            if (r3 != 0) goto L_0x0249
            int r0 = r29.getRenderType()     // Catch:{ Throwable -> 0x0043, all -> 0x0286 }
            if (r0 != 0) goto L_0x01de
            com.taobao.android.dinamicx.DXError r2 = r28.getDxError()     // Catch:{ Throwable -> 0x0043, all -> 0x0286 }
            java.lang.String r3 = "Pipeline_Render"
            r4 = 40002(0x9c42, float:5.6055E-41)
            java.lang.String r5 = "获取原型树失败"
            r6 = 0
            r0 = 0
            r1 = r24
            r11 = r7
            r7 = r0
            r1.trackerError(r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x0283 }
            goto L_0x01f1
        L_0x01de:
            r11 = r7
            com.taobao.android.dinamicx.DXError r2 = r28.getDxError()     // Catch:{ Throwable -> 0x0283 }
            java.lang.String r3 = "Pipeline_Render"
            r4 = 40006(0x9c46, float:5.606E-41)
            java.lang.String r5 = "异步获取原型树失败"
            r6 = 0
            r7 = 0
            r1 = r24
            r1.trackerError(r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x0283 }
        L_0x01f1:
            boolean r0 = r11 instanceof com.taobao.android.dinamicx.DXRootView
            if (r0 == 0) goto L_0x023a
            int r0 = r29.getRenderType()
            if (r0 != 0) goto L_0x023a
            r3 = r11
            com.taobao.android.dinamicx.DXRootView r3 = (com.taobao.android.dinamicx.DXRootView) r3
            if (r3 == 0) goto L_0x023a
            int r0 = r3.getChildCount()
            if (r0 != 0) goto L_0x023a
            boolean r0 = r28.hasError()
            if (r0 == 0) goto L_0x023a
            com.taobao.android.dinamicx.DXError r0 = new com.taobao.android.dinamicx.DXError
            java.lang.String r1 = r8.bizType
            r0.<init>(r1)
            com.taobao.android.dinamicx.template.download.DXTemplateItem r1 = r28.getDxTemplateItem()
            r0.dxTemplateItem = r1
            com.taobao.android.dinamicx.DXError$DXErrorInfo r1 = new com.taobao.android.dinamicx.DXError$DXErrorInfo
            java.lang.String r2 = "Pipeline"
            java.lang.String r3 = "Pipeline_Stage_Render_Error_Downgrade"
            r4 = 90004(0x15f94, float:1.26122E-40)
            r1.<init>(r2, r3, r4)
            com.taobao.android.dinamicx.DXError r2 = r28.getDxError()
            java.lang.String r2 = r2.toString()
            r1.reason = r2
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r2 = r0.dxErrorInfoList
            r2.add(r1)
            com.taobao.android.dinamicx.monitor.DXAppMonitor.trackerError(r0)
            r8.executeDowngrade(r9)
        L_0x023a:
            boolean r0 = r28.hasError()
            if (r0 == 0) goto L_0x0247
            com.taobao.android.dinamicx.DXError r0 = r28.getDxError()
            com.taobao.android.dinamicx.monitor.DXAppMonitor.trackerError(r0)
        L_0x0247:
            r7 = 0
            return r7
        L_0x0249:
            r11 = r7
            r7 = 0
            com.taobao.android.dinamicx.DXRuntimeContext r4 = r3.getDXRuntimeContext()     // Catch:{ Throwable -> 0x0283 }
            if (r4 == 0) goto L_0x026a
            com.taobao.android.dinamicx.DXRuntimeContext r4 = r3.getDXRuntimeContext()     // Catch:{ Throwable -> 0x0283 }
            boolean r4 = r4.hasError()     // Catch:{ Throwable -> 0x0283 }
            if (r4 == 0) goto L_0x026a
            com.taobao.android.dinamicx.DXError r4 = r9.dxError     // Catch:{ Throwable -> 0x0283 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r4 = r4.dxErrorInfoList     // Catch:{ Throwable -> 0x0283 }
            com.taobao.android.dinamicx.DXRuntimeContext r5 = r3.getDXRuntimeContext()     // Catch:{ Throwable -> 0x0283 }
            com.taobao.android.dinamicx.DXError r5 = r5.dxError     // Catch:{ Throwable -> 0x0283 }
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r5 = r5.dxErrorInfoList     // Catch:{ Throwable -> 0x0283 }
            r4.addAll(r5)     // Catch:{ Throwable -> 0x0283 }
        L_0x026a:
            com.taobao.android.dinamicx.widget.DXWidgetNode r18 = r3.deepClone(r9)     // Catch:{ Throwable -> 0x0283 }
            java.lang.String r3 = "Pipeline_Stage_Get_Template_Widget"
            long r4 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0283 }
            r6 = 0
            long r4 = r4 - r1
            r6 = 0
            r1 = r24
            r2 = r28
            r1.trackerPerform(r2, r3, r4, r6)     // Catch:{ Throwable -> 0x0283 }
            r5 = r10
            r3 = r11
            r4 = r18
            goto L_0x02cb
        L_0x0283:
            r0 = move-exception
            goto L_0x0364
        L_0x0286:
            r0 = move-exception
            r11 = r7
            goto L_0x03da
        L_0x028a:
            r1 = r7
        L_0x028b:
            r7 = 0
            goto L_0x02c8
        L_0x028d:
            r23 = r2
            r1 = r3
            r10 = r5
            r12 = r6
            r7 = r11
            r11 = r4
            if (r10 == 0) goto L_0x02c8
            long r2 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0301, all -> 0x02ff }
            com.taobao.android.dinamicx.DXRenderManager r4 = r8.dxRenderManager     // Catch:{ Throwable -> 0x0301, all -> 0x02ff }
            r6 = r27
            android.view.View r18 = r4.renderWidget(r11, r10, r6, r9)     // Catch:{ Throwable -> 0x0301, all -> 0x02ff }
            java.lang.String r4 = "Pipeline_Stage_Render_Widget"
            long r19 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x02c3, all -> 0x02be }
            r1 = 0
            long r19 = r19 - r2
            r21 = 0
            r1 = r24
            r2 = r28
            r3 = r4
            r4 = r19
            r6 = r21
            r1.trackerPerform(r2, r3, r4, r6)     // Catch:{ Throwable -> 0x02c3, all -> 0x02be }
            r5 = r10
            r4 = r11
            r3 = r18
            goto L_0x02cb
        L_0x02be:
            r0 = move-exception
            r7 = r18
            goto L_0x03da
        L_0x02c3:
            r0 = move-exception
            r11 = r18
            goto L_0x0364
        L_0x02c8:
            r3 = r1
            r5 = r10
            r4 = r11
        L_0x02cb:
            r2 = r23
        L_0x02cd:
            int r0 = r0 + 1
            r11 = r7
            r6 = r12
            r10 = r29
            r7 = r27
            goto L_0x0050
        L_0x02d7:
            r0 = move-exception
            r1 = r3
        L_0x02d9:
            r7 = r1
            goto L_0x03da
        L_0x02dc:
            r0 = move-exception
            r1 = r3
        L_0x02de:
            r11 = r1
            goto L_0x0364
        L_0x02e1:
            r23 = r2
            r1 = r3
            r11 = r4
            r12 = r6
            if (r12 == 0) goto L_0x0303
            if (r17 != 0) goto L_0x0303
            if (r11 == 0) goto L_0x0303
            com.taobao.android.dinamicx.DXEngineConfig r2 = r8.config     // Catch:{ Throwable -> 0x0301, all -> 0x02ff }
            boolean r2 = r2.isUsePipelineCache()     // Catch:{ Throwable -> 0x0301, all -> 0x02ff }
            if (r2 == 0) goto L_0x0303
            r2 = 4
            if (r0 < r2) goto L_0x0303
            r11 = r23
            if (r11 == 0) goto L_0x0303
            r12.putExpandWidgetLruCache(r9, r11)     // Catch:{ Throwable -> 0x0301, all -> 0x02ff }
            goto L_0x0303
        L_0x02ff:
            r0 = move-exception
            goto L_0x02d9
        L_0x0301:
            r0 = move-exception
            goto L_0x02de
        L_0x0303:
            boolean r0 = r1 instanceof com.taobao.android.dinamicx.DXRootView
            if (r0 == 0) goto L_0x034c
            int r0 = r29.getRenderType()
            if (r0 != 0) goto L_0x034c
            r3 = r1
            com.taobao.android.dinamicx.DXRootView r3 = (com.taobao.android.dinamicx.DXRootView) r3
            if (r3 == 0) goto L_0x034c
            int r0 = r3.getChildCount()
            if (r0 != 0) goto L_0x034c
            boolean r0 = r28.hasError()
            if (r0 == 0) goto L_0x034c
            com.taobao.android.dinamicx.DXError r0 = new com.taobao.android.dinamicx.DXError
            java.lang.String r2 = r8.bizType
            r0.<init>(r2)
            com.taobao.android.dinamicx.template.download.DXTemplateItem r2 = r28.getDxTemplateItem()
            r0.dxTemplateItem = r2
            com.taobao.android.dinamicx.DXError$DXErrorInfo r2 = new com.taobao.android.dinamicx.DXError$DXErrorInfo
            java.lang.String r3 = "Pipeline"
            java.lang.String r4 = "Pipeline_Stage_Render_Error_Downgrade"
            r5 = 90004(0x15f94, float:1.26122E-40)
            r2.<init>(r3, r4, r5)
            com.taobao.android.dinamicx.DXError r3 = r28.getDxError()
            java.lang.String r3 = r3.toString()
            r2.reason = r3
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r3 = r0.dxErrorInfoList
            r3.add(r2)
            com.taobao.android.dinamicx.monitor.DXAppMonitor.trackerError(r0)
            r8.executeDowngrade(r9)
        L_0x034c:
            boolean r0 = r28.hasError()
            if (r0 == 0) goto L_0x0359
            com.taobao.android.dinamicx.DXError r0 = r28.getDxError()
            com.taobao.android.dinamicx.monitor.DXAppMonitor.trackerError(r0)
        L_0x0359:
            r11 = r1
            goto L_0x03d7
        L_0x035c:
            r0 = move-exception
            r7 = r27
            goto L_0x03da
        L_0x0361:
            r0 = move-exception
            r11 = r27
        L_0x0364:
            boolean r1 = com.taobao.android.dinamicx.DinamicXEngine.isDebug()     // Catch:{ all -> 0x03d8 }
            if (r1 == 0) goto L_0x036d
            r0.printStackTrace()     // Catch:{ all -> 0x03d8 }
        L_0x036d:
            java.lang.String r5 = com.taobao.android.dinamicx.exception.DXExceptionUtil.getStackTrace(r0)     // Catch:{ all -> 0x03d8 }
            com.taobao.android.dinamicx.DXError r2 = r28.getDxError()     // Catch:{ all -> 0x03d8 }
            java.lang.String r3 = "Pipeline_Render"
            r4 = 40003(0x9c43, float:5.6056E-41)
            r6 = 0
            r7 = 0
            r1 = r24
            r1.trackerError(r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x03d8 }
            boolean r0 = r11 instanceof com.taobao.android.dinamicx.DXRootView
            if (r0 == 0) goto L_0x03ca
            int r0 = r29.getRenderType()
            if (r0 != 0) goto L_0x03ca
            r0 = r11
            com.taobao.android.dinamicx.DXRootView r0 = (com.taobao.android.dinamicx.DXRootView) r0
            if (r0 == 0) goto L_0x03ca
            int r0 = r0.getChildCount()
            if (r0 != 0) goto L_0x03ca
            boolean r0 = r28.hasError()
            if (r0 == 0) goto L_0x03ca
            com.taobao.android.dinamicx.DXError r0 = new com.taobao.android.dinamicx.DXError
            java.lang.String r1 = r8.bizType
            r0.<init>(r1)
            com.taobao.android.dinamicx.template.download.DXTemplateItem r1 = r28.getDxTemplateItem()
            r0.dxTemplateItem = r1
            com.taobao.android.dinamicx.DXError$DXErrorInfo r1 = new com.taobao.android.dinamicx.DXError$DXErrorInfo
            java.lang.String r2 = "Pipeline"
            java.lang.String r3 = "Pipeline_Stage_Render_Error_Downgrade"
            r4 = 90004(0x15f94, float:1.26122E-40)
            r1.<init>(r2, r3, r4)
            com.taobao.android.dinamicx.DXError r2 = r28.getDxError()
            java.lang.String r2 = r2.toString()
            r1.reason = r2
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r2 = r0.dxErrorInfoList
            r2.add(r1)
            com.taobao.android.dinamicx.monitor.DXAppMonitor.trackerError(r0)
            r8.executeDowngrade(r9)
        L_0x03ca:
            boolean r0 = r28.hasError()
            if (r0 == 0) goto L_0x03d7
            com.taobao.android.dinamicx.DXError r0 = r28.getDxError()
            com.taobao.android.dinamicx.monitor.DXAppMonitor.trackerError(r0)
        L_0x03d7:
            return r11
        L_0x03d8:
            r0 = move-exception
            r7 = r11
        L_0x03da:
            boolean r1 = r7 instanceof com.taobao.android.dinamicx.DXRootView
            if (r1 == 0) goto L_0x0422
            int r1 = r29.getRenderType()
            if (r1 != 0) goto L_0x0422
            com.taobao.android.dinamicx.DXRootView r7 = (com.taobao.android.dinamicx.DXRootView) r7
            if (r7 == 0) goto L_0x0422
            int r1 = r7.getChildCount()
            if (r1 != 0) goto L_0x0422
            boolean r1 = r28.hasError()
            if (r1 == 0) goto L_0x0422
            com.taobao.android.dinamicx.DXError r1 = new com.taobao.android.dinamicx.DXError
            java.lang.String r2 = r8.bizType
            r1.<init>(r2)
            com.taobao.android.dinamicx.template.download.DXTemplateItem r2 = r28.getDxTemplateItem()
            r1.dxTemplateItem = r2
            com.taobao.android.dinamicx.DXError$DXErrorInfo r2 = new com.taobao.android.dinamicx.DXError$DXErrorInfo
            java.lang.String r3 = "Pipeline"
            java.lang.String r4 = "Pipeline_Stage_Render_Error_Downgrade"
            r5 = 90004(0x15f94, float:1.26122E-40)
            r2.<init>(r3, r4, r5)
            com.taobao.android.dinamicx.DXError r3 = r28.getDxError()
            java.lang.String r3 = r3.toString()
            r2.reason = r3
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r3 = r1.dxErrorInfoList
            r3.add(r2)
            com.taobao.android.dinamicx.monitor.DXAppMonitor.trackerError(r1)
            r8.executeDowngrade(r9)
        L_0x0422:
            boolean r1 = r28.hasError()
            if (r1 == 0) goto L_0x042f
            com.taobao.android.dinamicx.DXError r1 = r28.getDxError()
            com.taobao.android.dinamicx.monitor.DXAppMonitor.trackerError(r1)
        L_0x042f:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.DXRenderPipeline.renderWidget(com.taobao.android.dinamicx.widget.DXWidgetNode, com.taobao.android.dinamicx.widget.DXWidgetNode, android.view.View, com.taobao.android.dinamicx.DXRuntimeContext, com.taobao.android.dinamicx.DXRenderOptions):android.view.View");
    }

    /* access modifiers changed from: package-private */
    public void registerControlEvents() {
        DXControlEventCenter controlEventCenter = getControlEventCenter();
        if (controlEventCenter != null) {
            controlEventCenter.registerListener(this, DXPipelineScheduleEvent.DX_EVENT_PIPELINE_SCHEDULE);
        }
    }

    public void receivedControlEvent(final DXControlEvent dXControlEvent) {
        DXRunnableManager.getInstance();
        DXRunnableManager.runOnUIThread(new Runnable() {
            public void run() {
                DXWidgetNode dXWidgetNode;
                DXRuntimeContext dXRuntimeContext;
                DXRootView rootView;
                if (dXControlEvent != null && (dXControlEvent instanceof DXPipelineScheduleEvent)) {
                    DXPipelineScheduleEvent dXPipelineScheduleEvent = (DXPipelineScheduleEvent) dXControlEvent;
                    if ((dXControlEvent.sender instanceof DXWidgetNode) && (dXWidgetNode = (DXWidgetNode) dXControlEvent.sender) != null && (dXRuntimeContext = dXWidgetNode.getDXRuntimeContext()) != null && dXRuntimeContext.renderType == 0 && (rootView = dXRuntimeContext.getRootView()) != null && dXRuntimeContext.dxTemplateItem.equals(rootView.dxTemplateItem) && dXRuntimeContext.getData() == rootView.data) {
                        DXRenderPipeline.this.renderWidget(dXWidgetNode, rootView.getFlattenWidgetNode(), rootView, dXWidgetNode.getDXRuntimeContext().cloneWithWidgetNode(dXWidgetNode), new DXRenderOptions.Builder().withIsControlEvent(true).withFromStage(dXPipelineScheduleEvent.stage).withWidthSpec(dXRuntimeContext.getRootWidthSpec()).withHeightSpec(dXRuntimeContext.getRootHeightSpec()).withToStage(8).build());
                    }
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void trackerPerform(DXRuntimeContext dXRuntimeContext, String str, long j, Map<String, String> map) {
        DXTemplateItem dxTemplateItem;
        try {
            String str2 = dXRuntimeContext.bizType;
            if (dXRuntimeContext == null) {
                dxTemplateItem = null;
            } else {
                dxTemplateItem = dXRuntimeContext.getDxTemplateItem();
            }
            DXAppMonitor.trackerPerform(1, str2, DXMonitorConstant.DX_MONITOR_PIPELINE, str, dxTemplateItem, map, (double) j, true);
        } catch (Exception e) {
            if (DinamicXEngine.isDebug()) {
                e.printStackTrace();
            }
        }
    }

    private void trackerError(DXError dXError, String str, int i, String str2, Map<String, String> map, boolean z) {
        if (dXError != null && dXError.dxErrorInfoList != null) {
            DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, str, i);
            dXErrorInfo.reason = str2;
            dXErrorInfo.extraParams = map;
            dXError.dxErrorInfoList.add(dXErrorInfo);
            if (z) {
                DXAppMonitor.trackerError(dXError);
            }
        }
    }

    private void executeDowngrade(DXRuntimeContext dXRuntimeContext) {
        if (dXRuntimeContext != null) {
            try {
                DXTemplateItem dxTemplateItem = dXRuntimeContext.getDxTemplateItem();
                if (this.config != null && !this.config.disabledDownGrade && getTemplateManager() != null && dxTemplateItem != null) {
                    getTemplateManager().downgradeTemplate(dxTemplateItem);
                    postNotify(dXRuntimeContext, 1000);
                }
            } catch (Exception e) {
                if (DinamicXEngine.isDebug()) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void postNotify(DXRuntimeContext dXRuntimeContext, int i) {
        if (this.notificationCenter != null && dXRuntimeContext != null) {
            DXTemplateUpdateRequest dXTemplateUpdateRequest = new DXTemplateUpdateRequest();
            dXTemplateUpdateRequest.item = dXRuntimeContext.dxTemplateItem;
            dXTemplateUpdateRequest.dxUserContext = dXRuntimeContext.getDxUserContext();
            dXTemplateUpdateRequest.data = dXRuntimeContext.getData();
            dXTemplateUpdateRequest.reason = i;
            this.notificationCenter.postNotification(dXTemplateUpdateRequest);
        }
    }

    private boolean checkNeedCancel(DXRenderOptions dXRenderOptions) {
        return dXRenderOptions.getRenderType() == 1 && dXRenderOptions.isCanceled();
    }

    private void resetBindingXAnimation(DXRootView dXRootView) {
        try {
            DXBindingXManager bindingXManager = dXRootView.getBindingXManager();
            if (bindingXManager != null) {
                bindingXManager.resetAnimationOnRootView(dXRootView);
            }
        } catch (Exception e) {
            DXExceptionUtil.printStack(e);
            DXError dXError = new DXError(getBizType());
            dXError.dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PIPELINE_STAGE_RESET_BINDINGX, DXError.RESET_ANIMATION_CRASH, DXExceptionUtil.getStackTrace(e)));
            DXAppMonitor.trackerError(dXError);
        }
    }
}
