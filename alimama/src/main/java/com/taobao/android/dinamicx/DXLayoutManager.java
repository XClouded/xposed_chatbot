package com.taobao.android.dinamicx;

import android.view.View;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.widget.DXLayout;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;

public class DXLayoutManager {
    /* access modifiers changed from: protected */
    public void performMeasure(DXWidgetNode dXWidgetNode, int i, int i2, DXRuntimeContext dXRuntimeContext) {
        if (dXWidgetNode != null) {
            try {
                if (dXWidgetNode instanceof DXLayout) {
                    if (sizeIsNull(i)) {
                        i = DXScreenTool.getDefaultWidthSpec();
                    }
                    if (sizeIsNull(i2)) {
                        i2 = DXScreenTool.getDefaultHeightSpec();
                    }
                    int childMeasureSpec = DXLayout.getChildMeasureSpec(i, 0, dXWidgetNode.getLayoutWidth());
                    int childMeasureSpec2 = DXLayout.getChildMeasureSpec(i2, 0, dXWidgetNode.getLayoutHeight());
                    if (dXWidgetNode.getVisibility() == 0) {
                        dXWidgetNode.measure(childMeasureSpec, childMeasureSpec2);
                        return;
                    }
                    return;
                }
            } catch (Exception e) {
                if (DinamicXEngine.isDebug()) {
                    e.printStackTrace();
                }
                if (dXRuntimeContext != null && dXRuntimeContext.getDxError() != null && dXRuntimeContext.getDxError().dxErrorInfoList != null) {
                    DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE_DETAIL, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PERFORM_MEASURE, DXError.DXERROR_PIPELINE_DETAIL_PERFORM_MEASURE_CATCH);
                    dXErrorInfo.reason = "DXLayoutManager#performMeasure" + DXExceptionUtil.getStackTrace(e);
                    dXRuntimeContext.getDxError().dxErrorInfoList.add(dXErrorInfo);
                    return;
                }
                return;
            }
        }
        DXError.DXErrorInfo dXErrorInfo2 = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE_DETAIL, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PERFORM_MEASURE, DXError.DXERROR_PIPELINE_DETAIL_PERFORM_MEASURE_WT_IS_NULL_OR_NOT_LAYOUT);
        dXErrorInfo2.reason = "DXLayoutManager#performMeasure widgetNode == null || !(widgetNode instanceof DXLayout)";
        dXRuntimeContext.getDxError().dxErrorInfoList.add(dXErrorInfo2);
    }

    private boolean sizeIsNull(int i) {
        return View.MeasureSpec.getMode(i) == 0 && View.MeasureSpec.getSize(i) == 0;
    }

    /* access modifiers changed from: protected */
    public void performLayout(DXWidgetNode dXWidgetNode, DXRuntimeContext dXRuntimeContext) {
        if (dXWidgetNode != null) {
            try {
                if (dXWidgetNode.getVisibility() == 0) {
                    dXWidgetNode.layout(0, 0, dXWidgetNode.getMeasuredWidth(), dXWidgetNode.getMeasuredHeight());
                }
            } catch (Exception e) {
                if (DinamicXEngine.isDebug()) {
                    e.printStackTrace();
                }
                if (dXRuntimeContext != null && dXRuntimeContext.getDxError() != null && dXRuntimeContext.getDxError().dxErrorInfoList != null) {
                    DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE_DETAIL, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PERFORM_LAYOUT, DXError.DXERROR_PIPELINE_DETAIL_LAYOUT_CATCH);
                    dXErrorInfo.reason = "DXLayoutManager#performLayout " + DXExceptionUtil.getStackTrace(e);
                    dXRuntimeContext.getDxError().dxErrorInfoList.add(dXErrorInfo);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0061  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.taobao.android.dinamicx.widget.DXFrameLayoutWidgetNode performFlatten(com.taobao.android.dinamicx.widget.DXWidgetNode r17, com.taobao.android.dinamicx.DXRuntimeContext r18, boolean r19) {
        /*
            r16 = this;
            r1 = r18
            r2 = 0
            if (r17 != 0) goto L_0x0006
            return r2
        L_0x0006:
            com.taobao.android.dinamicx.widget.DXFrameLayoutWidgetNode r15 = new com.taobao.android.dinamicx.widget.DXFrameLayoutWidgetNode     // Catch:{ Exception -> 0x0059 }
            r15.<init>()     // Catch:{ Exception -> 0x0059 }
            r2 = 1
            r15.setFlatten(r2)     // Catch:{ Exception -> 0x0057 }
            r15.setReferenceNode(r15)     // Catch:{ Exception -> 0x0057 }
            com.taobao.android.dinamicx.DXRuntimeContext r2 = r1.cloneWithWidgetNode(r15)     // Catch:{ Exception -> 0x0057 }
            r15.setDXRuntimeContext(r2)     // Catch:{ Exception -> 0x0057 }
            int r2 = r17.getVisibility()     // Catch:{ Exception -> 0x0057 }
            if (r2 == 0) goto L_0x0024
            r0 = 0
            r15.setMeasuredDimension(r0, r0)     // Catch:{ Exception -> 0x0057 }
            return r15
        L_0x0024:
            int r2 = r17.getLayoutWidth()     // Catch:{ Exception -> 0x0057 }
            r15.setLayoutWidth(r2)     // Catch:{ Exception -> 0x0057 }
            int r2 = r17.getLayoutHeight()     // Catch:{ Exception -> 0x0057 }
            r15.setLayoutHeight(r2)     // Catch:{ Exception -> 0x0057 }
            int r2 = r17.getMeasuredWidthAndState()     // Catch:{ Exception -> 0x0057 }
            int r3 = r17.getMeasuredHeightAndState()     // Catch:{ Exception -> 0x0057 }
            r15.setMeasuredDimension(r2, r3)     // Catch:{ Exception -> 0x0057 }
            r5 = 0
            r6 = 0
            int r7 = r17.getMeasuredWidth()     // Catch:{ Exception -> 0x0057 }
            int r8 = r17.getMeasuredHeight()     // Catch:{ Exception -> 0x0057 }
            r10 = 0
            r11 = 0
            r13 = 1
            r14 = 1065353216(0x3f800000, float:1.0)
            r3 = r16
            r4 = r17
            r9 = r15
            r12 = r19
            r3.doFlatten(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14)     // Catch:{ Exception -> 0x0057 }
            goto L_0x00a0
        L_0x0057:
            r0 = move-exception
            goto L_0x005b
        L_0x0059:
            r0 = move-exception
            r15 = r2
        L_0x005b:
            boolean r2 = com.taobao.android.dinamicx.DinamicXEngine.isDebug()
            if (r2 == 0) goto L_0x0064
            r0.printStackTrace()
        L_0x0064:
            if (r1 == 0) goto L_0x00a0
            com.taobao.android.dinamicx.DXError r2 = r18.getDxError()
            if (r2 == 0) goto L_0x00a0
            com.taobao.android.dinamicx.DXError r2 = r18.getDxError()
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r2 = r2.dxErrorInfoList
            if (r2 == 0) goto L_0x00a0
            com.taobao.android.dinamicx.DXError$DXErrorInfo r2 = new com.taobao.android.dinamicx.DXError$DXErrorInfo
            java.lang.String r3 = "Pipeline_Detail"
            java.lang.String r4 = "Pipeline_Detail_PerformFlatten"
            r5 = 80004(0x13884, float:1.1211E-40)
            r2.<init>(r3, r4, r5)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "DXLayoutManager#performFlatten "
            r3.append(r4)
            java.lang.String r0 = com.taobao.android.dinamicx.exception.DXExceptionUtil.getStackTrace(r0)
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            r2.reason = r0
            com.taobao.android.dinamicx.DXError r0 = r18.getDxError()
            java.util.List<com.taobao.android.dinamicx.DXError$DXErrorInfo> r0 = r0.dxErrorInfoList
            r0.add(r2)
        L_0x00a0:
            return r15
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.DXLayoutManager.performFlatten(com.taobao.android.dinamicx.widget.DXWidgetNode, com.taobao.android.dinamicx.DXRuntimeContext, boolean):com.taobao.android.dinamicx.widget.DXFrameLayoutWidgetNode");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x004f  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x007c A[EDGE_INSN: B:86:0x007c->B:29:0x007c ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void doFlatten(com.taobao.android.dinamicx.widget.DXWidgetNode r27, int r28, int r29, int r30, int r31, com.taobao.android.dinamicx.widget.DXWidgetNode r32, boolean r33, boolean r34, boolean r35, int r36, float r37) {
        /*
            r26 = this;
            r0 = r27
            r3 = 0
            if (r0 == 0) goto L_0x0194
            int r4 = r27.getVisibility()
            if (r4 == 0) goto L_0x000d
            goto L_0x0194
        L_0x000d:
            boolean r4 = r27.hasCornerRadius()
            int r5 = r27.getAccessibility()
            boolean r6 = r27.hasAccessibilityOn()
            int r7 = r27.getChildrenCount()
            r8 = 0
            r9 = 1
            if (r7 <= 0) goto L_0x0023
            r10 = 1
            goto L_0x0024
        L_0x0023:
            r10 = 0
        L_0x0024:
            if (r35 != 0) goto L_0x007a
            if (r10 == 0) goto L_0x007a
            if (r4 != 0) goto L_0x0036
            r4 = r0
            com.taobao.android.dinamicx.widget.DXLayout r4 = (com.taobao.android.dinamicx.widget.DXLayout) r4
            boolean r4 = r4.isDisableFlatten()
            if (r4 == 0) goto L_0x0034
            goto L_0x0036
        L_0x0034:
            r4 = 0
            goto L_0x0037
        L_0x0036:
            r4 = 1
        L_0x0037:
            if (r4 != 0) goto L_0x007c
            int r11 = r27.getMeasuredWidth()
            int r12 = r27.getMeasuredHeight()
            java.util.List r13 = r27.getChildren()
            java.util.Iterator r13 = r13.iterator()
        L_0x0049:
            boolean r14 = r13.hasNext()
            if (r14 == 0) goto L_0x007c
            java.lang.Object r14 = r13.next()
            com.taobao.android.dinamicx.widget.DXWidgetNode r14 = (com.taobao.android.dinamicx.widget.DXWidgetNode) r14
            int r15 = r14.getLeft()
            if (r15 < 0) goto L_0x0078
            int r15 = r14.getTop()
            if (r15 < 0) goto L_0x0078
            int r15 = r14.getLeft()
            int r16 = r14.getMeasuredWidth()
            int r15 = r15 + r16
            if (r15 > r11) goto L_0x0078
            int r15 = r14.getTop()
            int r14 = r14.getMeasuredHeight()
            int r15 = r15 + r14
            if (r15 <= r12) goto L_0x0049
        L_0x0078:
            r4 = 1
            goto L_0x007c
        L_0x007a:
            r4 = r35
        L_0x007c:
            if (r10 == 0) goto L_0x00ad
            if (r4 != 0) goto L_0x00ad
            if (r6 != 0) goto L_0x00ad
            int r11 = r27.getBackGroundColor()
            if (r11 != 0) goto L_0x00ad
            int r11 = r27.getBorderWidth()
            if (r11 <= 0) goto L_0x0094
            int r11 = r27.getBorderColor()
            if (r11 != 0) goto L_0x00ad
        L_0x0094:
            androidx.collection.LongSparseArray r11 = r27.getEventHandlersExprNode()
            if (r11 == 0) goto L_0x00a4
            androidx.collection.LongSparseArray r11 = r27.getEventHandlersExprNode()
            int r11 = r11.size()
            if (r11 > 0) goto L_0x00ad
        L_0x00a4:
            com.taobao.android.dinamicx.widget.DXWidgetNode$GradientInfo r11 = r27.getBackgroundGradient()
            if (r11 == 0) goto L_0x00ab
            goto L_0x00ad
        L_0x00ab:
            r11 = 0
            goto L_0x00ae
        L_0x00ad:
            r11 = 1
        L_0x00ae:
            if (r11 == 0) goto L_0x00e5
            int r3 = r27.getLeft()
            int r1 = r28 + r3
            int r3 = r27.getTop()
            int r2 = r29 + r3
            int r3 = r27.getMeasuredWidth()
            int r3 = r3 + r1
            int r12 = r27.getMeasuredHeight()
            int r12 = r12 + r2
            com.taobao.android.dinamicx.DXRuntimeContext r13 = r27.getDXRuntimeContext()
            java.lang.Object r13 = r0.shallowClone(r13, r8)
            com.taobao.android.dinamicx.widget.DXWidgetNode r13 = (com.taobao.android.dinamicx.widget.DXWidgetNode) r13
            r0.setReferenceNode(r13)
            r13.setReferenceNode(r0)
            r13.setLeft(r1)
            r13.setTop(r2)
            r13.setRight(r3)
            r13.setBottom(r12)
            r22 = r12
            goto L_0x0102
        L_0x00e5:
            int r12 = r27.getLeft()
            int r1 = r28 + r12
            int r12 = r27.getTop()
            int r2 = r29 + r12
            int r12 = r27.getMeasuredWidth()
            int r12 = r12 + r1
            int r13 = r27.getMeasuredHeight()
            int r13 = r13 + r2
            r0.setReferenceNode(r3)
            r22 = r13
            r13 = r3
            r3 = r12
        L_0x0102:
            int r12 = r27.getEnabled()
            r15 = r36 & r12
            float r12 = r27.getAlpha()
            float r12 = r12 * r37
            r14 = 3
            if (r11 == 0) goto L_0x0130
            r13.setFlatten(r9)
            r11 = r32
            r11.addChild(r13, r8)
            r13.setEnabled(r15)
            r13.setAlpha(r12)
            if (r33 == 0) goto L_0x0126
            r8 = 2
            r13.setAccessibility((int) r8)
            goto L_0x0133
        L_0x0126:
            r8 = 2
            if (r34 == 0) goto L_0x0133
            r9 = -1
            if (r5 != r9) goto L_0x0133
            r13.setAccessibility((int) r14)
            goto L_0x0133
        L_0x0130:
            r11 = r32
            r8 = 2
        L_0x0133:
            if (r5 == r8) goto L_0x013b
            if (r6 == 0) goto L_0x0138
            goto L_0x013b
        L_0x0138:
            r6 = r33
            goto L_0x013c
        L_0x013b:
            r6 = 1
        L_0x013c:
            if (r5 != r14) goto L_0x0140
            r5 = 1
            goto L_0x0142
        L_0x0140:
            r5 = r34
        L_0x0142:
            if (r5 == 0) goto L_0x014b
            com.taobao.android.dinamicx.widget.DXWidgetNode r8 = r32.queryRootWidgetNode()
            r8.setAccessibility((int) r14)
        L_0x014b:
            if (r4 == 0) goto L_0x0155
            r1 = 1065353216(0x3f800000, float:1.0)
            r1 = r13
            r2 = 0
            r4 = 0
            r8 = 1065353216(0x3f800000, float:1.0)
            goto L_0x0159
        L_0x0155:
            r4 = r2
            r8 = r12
            r2 = r1
            r1 = r11
        L_0x0159:
            if (r10 == 0) goto L_0x0193
            boolean r9 = r27.isDisableDarkMode()
            r14 = 0
        L_0x0160:
            int r10 = r27.getChildrenCount()
            if (r14 >= r10) goto L_0x0193
            com.taobao.android.dinamicx.widget.DXWidgetNode r11 = r0.getChildAt(r14)
            if (r9 == 0) goto L_0x0171
            r13 = 1
            r11.setDisableDarkMode(r13)
            goto L_0x0172
        L_0x0171:
            r13 = 1
        L_0x0172:
            r10 = r26
            r12 = r2
            r23 = 1
            r13 = r4
            r24 = r14
            r14 = r3
            r25 = r15
            r15 = r22
            r16 = r1
            r17 = r6
            r18 = r5
            r19 = r35
            r20 = r25
            r21 = r8
            r10.doFlatten(r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21)
            int r14 = r24 + 1
            r15 = r25
            goto L_0x0160
        L_0x0193:
            return
        L_0x0194:
            r0.setReferenceNode(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.DXLayoutManager.doFlatten(com.taobao.android.dinamicx.widget.DXWidgetNode, int, int, int, int, com.taobao.android.dinamicx.widget.DXWidgetNode, boolean, boolean, boolean, int, float):void");
    }
}
