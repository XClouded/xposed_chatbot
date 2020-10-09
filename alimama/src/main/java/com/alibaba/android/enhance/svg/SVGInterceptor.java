package com.alibaba.android.enhance.svg;

import android.graphics.Matrix;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.android.bindingx.core.BindingXPropertyInterceptor;
import com.alibaba.android.enhance.svg.component.SVGPathComponent;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SVGInterceptor implements BindingXPropertyInterceptor.IPropertyUpdateInterceptor {
    private static final String PROP_DASHOFFSET = "svg-dashoffset";
    private static final String PROP_FILLCOLOR = "svg-fillcolor";
    private static final String PROP_FOLLOW = "svg-follow";
    private static final String PROP_PATH = "svg-path";
    private static final String PROP_ROTATE = "svg-transform.rotate";
    private static final String PROP_SCALEX = "svg-transform.scaleX";
    private static final String PROP_SCALEY = "svg-transform.scaleY";
    private static final String PROP_SKEWX = "svg-transform.skewX";
    private static final String PROP_SKEWY = "svg-transform.skewY";
    private static final String PROP_STROKECOLOR = "svg-strokecolor";
    private static final String PROP_TRANSLATEX = "svg-transform.translateX";
    private static final String PROP_TRANSLATEY = "svg-transform.translateY";
    private static final List<String> SUPPORT_PROPERTIES = Collections.unmodifiableList(Arrays.asList(new String[]{PROP_DASHOFFSET, PROP_FOLLOW, PROP_PATH, PROP_TRANSLATEX, PROP_TRANSLATEY, PROP_SCALEX, PROP_SCALEY, PROP_ROTATE, PROP_SKEWX, PROP_SKEWY, PROP_FILLCOLOR, PROP_STROKECOLOR}));
    private final Matrix mPooledMatrix = new Matrix();

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002b, code lost:
        r5 = (java.lang.String) r5[1];
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean updateView(@androidx.annotation.Nullable android.view.View r17, @androidx.annotation.NonNull java.lang.String r18, @androidx.annotation.NonNull java.lang.Object r19, @androidx.annotation.NonNull com.alibaba.android.bindingx.core.PlatformManager.IDeviceResolutionTranslator r20, @androidx.annotation.NonNull java.util.Map<java.lang.String, java.lang.Object> r21, java.lang.Object... r22) {
        /*
            r16 = this;
            r0 = r16
            r1 = r18
            r2 = r19
            r3 = r20
            r4 = r21
            r5 = r22
            java.util.List<java.lang.String> r6 = SUPPORT_PROPERTIES
            boolean r6 = r6.contains(r1)
            r7 = 0
            if (r6 != 0) goto L_0x0016
            return r7
        L_0x0016:
            if (r5 == 0) goto L_0x0351
            int r6 = r5.length
            r8 = 2
            if (r6 < r8) goto L_0x0351
            r6 = r5[r7]
            boolean r6 = r6 instanceof java.lang.String
            if (r6 == 0) goto L_0x0351
            r6 = 1
            r9 = r5[r6]
            boolean r9 = r9 instanceof java.lang.String
            if (r9 != 0) goto L_0x002b
            goto L_0x0351
        L_0x002b:
            r9 = r5[r7]
            java.lang.String r9 = (java.lang.String) r9
            r5 = r5[r6]
            java.lang.String r5 = (java.lang.String) r5
            com.taobao.weex.ui.component.WXComponent r9 = com.alibaba.android.bindingx.plugin.weex.WXModuleUtils.findComponentByRef(r5, r9)
            if (r9 == 0) goto L_0x0350
            boolean r10 = r9 instanceof com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent
            if (r10 != 0) goto L_0x003f
            goto L_0x0350
        L_0x003f:
            java.lang.String r10 = "svg-dashoffset"
            boolean r10 = r10.equals(r1)
            if (r10 == 0) goto L_0x0056
            r1 = r2
            java.lang.Double r1 = (java.lang.Double) r1
            double r1 = r1.doubleValue()
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r9 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r9
            float r1 = (float) r1
            r9.setStrokeDashoffset(r1)
            goto L_0x034f
        L_0x0056:
            java.lang.String r10 = "svg-fillcolor"
            boolean r10 = r10.equals(r1)
            r11 = 1132396544(0x437f0000, float:255.0)
            r12 = 3
            r13 = 4
            if (r10 == 0) goto L_0x00aa
            boolean r1 = r2 instanceof java.lang.Integer
            if (r1 == 0) goto L_0x034f
            r1 = r2
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            java.util.Locale r2 = java.util.Locale.getDefault()
            java.lang.String r3 = "rgba(%d,%d,%d,%f)"
            java.lang.Object[] r4 = new java.lang.Object[r13]
            int r5 = android.graphics.Color.red(r1)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r4[r7] = r5
            int r5 = android.graphics.Color.green(r1)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r4[r6] = r5
            int r5 = android.graphics.Color.blue(r1)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r4[r8] = r5
            int r1 = android.graphics.Color.alpha(r1)
            float r1 = (float) r1
            float r1 = r1 / r11
            java.lang.Float r1 = java.lang.Float.valueOf(r1)
            r4[r12] = r1
            java.lang.String r1 = java.lang.String.format(r2, r3, r4)
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r9 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r9
            r9.setFill(r1)
            goto L_0x034f
        L_0x00aa:
            java.lang.String r10 = "svg-strokecolor"
            boolean r10 = r10.equals(r1)
            if (r10 == 0) goto L_0x00fa
            boolean r1 = r2 instanceof java.lang.Integer
            if (r1 == 0) goto L_0x034f
            r1 = r2
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            java.util.Locale r2 = java.util.Locale.getDefault()
            java.lang.String r3 = "rgba(%d,%d,%d,%f)"
            java.lang.Object[] r4 = new java.lang.Object[r13]
            int r5 = android.graphics.Color.red(r1)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r4[r7] = r5
            int r5 = android.graphics.Color.green(r1)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r4[r6] = r5
            int r5 = android.graphics.Color.blue(r1)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r4[r8] = r5
            int r1 = android.graphics.Color.alpha(r1)
            float r1 = (float) r1
            float r1 = r1 / r11
            java.lang.Float r1 = java.lang.Float.valueOf(r1)
            r4[r12] = r1
            java.lang.String r1 = java.lang.String.format(r2, r3, r4)
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r9 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r9
            r9.setStroke(r1)
            goto L_0x034f
        L_0x00fa:
            java.lang.String r10 = "svg-follow"
            boolean r10 = r10.equals(r1)
            r11 = 0
            if (r10 == 0) goto L_0x0159
            r1 = r2
            java.lang.Double r1 = (java.lang.Double) r1
            double r1 = r1.doubleValue()
            java.lang.String r3 = "pathRef"
            java.lang.Object r3 = r4.get(r3)
            java.lang.String r3 = com.taobao.weex.utils.WXUtils.getString(r3, r11)
            boolean r4 = android.text.TextUtils.isEmpty(r3)
            if (r4 == 0) goto L_0x011b
            return r6
        L_0x011b:
            com.taobao.weex.ui.component.WXComponent r3 = com.alibaba.android.bindingx.plugin.weex.WXModuleUtils.findComponentByRef(r5, r3)
            boolean r4 = r3 instanceof com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent
            if (r4 != 0) goto L_0x0124
            return r6
        L_0x0124:
            float[] r4 = new float[r8]
            float[] r5 = new float[r8]
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r3 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r3
            float r1 = (float) r1
            r3.getPosAndTanAtLength(r1, r4, r5)
            r1 = r5[r6]
            double r1 = (double) r1
            r3 = r5[r7]
            double r10 = (double) r3
            double r1 = java.lang.Math.atan2(r1, r10)
            double r1 = java.lang.Math.toDegrees(r1)
            android.graphics.Matrix r3 = r0.mPooledMatrix
            r3.reset()
            android.graphics.Matrix r3 = r0.mPooledMatrix
            r5 = r4[r7]
            r4 = r4[r6]
            r3.preTranslate(r5, r4)
            android.graphics.Matrix r3 = r0.mPooledMatrix
            float r1 = (float) r1
            r3.preRotate(r1)
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r9 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r9
            android.graphics.Matrix r1 = r0.mPooledMatrix
            r9.setMatrix(r1)
            goto L_0x034f
        L_0x0159:
            java.lang.String r5 = "svg-path"
            boolean r5 = r5.equals(r1)
            if (r5 == 0) goto L_0x01cd
            boolean r1 = r9 instanceof com.alibaba.android.enhance.svg.component.SVGPathComponent
            if (r1 == 0) goto L_0x034f
            boolean r1 = r2 instanceof java.util.List
            if (r1 == 0) goto L_0x01b1
            r1 = r2
            java.util.List r1 = (java.util.List) r1
            boolean r3 = r1.isEmpty()
            if (r3 != 0) goto L_0x01b1
            long r2 = java.lang.System.currentTimeMillis()
            java.lang.Object r4 = r1.get(r7)
            boolean r4 = r4 instanceof java.util.List
            if (r4 == 0) goto L_0x0184
            com.alibaba.android.enhance.svg.component.SVGPathComponent r9 = (com.alibaba.android.enhance.svg.component.SVGPathComponent) r9
            r0.handleMultiplePathProperty(r1, r9)
            goto L_0x0189
        L_0x0184:
            com.alibaba.android.enhance.svg.component.SVGPathComponent r9 = (com.alibaba.android.enhance.svg.component.SVGPathComponent) r9
            r0.handleSinglePathProperty(r1, r9)
        L_0x0189:
            boolean r1 = com.taobao.weex.WXEnvironment.isApkDebugable()
            if (r1 == 0) goto L_0x034f
            java.lang.String r1 = "svg"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "path interpolation elapsed:"
            r4.append(r5)
            long r7 = java.lang.System.currentTimeMillis()
            long r7 = r7 - r2
            r4.append(r7)
            java.lang.String r2 = "ms"
            r4.append(r2)
            java.lang.String r2 = r4.toString()
            com.taobao.weex.utils.WXLogUtils.v((java.lang.String) r1, (java.lang.String) r2)
            goto L_0x034f
        L_0x01b1:
            boolean r1 = r2 instanceof android.graphics.Path
            if (r1 == 0) goto L_0x01bf
            com.alibaba.android.enhance.svg.component.SVGPathComponent r9 = (com.alibaba.android.enhance.svg.component.SVGPathComponent) r9
            r1 = r2
            android.graphics.Path r1 = (android.graphics.Path) r1
            r9.setPath(r1)
            goto L_0x034f
        L_0x01bf:
            boolean r1 = r2 instanceof java.lang.String
            if (r1 == 0) goto L_0x034f
            com.alibaba.android.enhance.svg.component.SVGPathComponent r9 = (com.alibaba.android.enhance.svg.component.SVGPathComponent) r9
            r1 = r2
            java.lang.String r1 = (java.lang.String) r1
            r9.setD(r1)
            goto L_0x034f
        L_0x01cd:
            java.lang.String r5 = "svg-transform.translateX"
            boolean r5 = r5.equals(r1)
            r10 = 9
            if (r5 == 0) goto L_0x01f7
            r1 = r2
            java.lang.Double r1 = (java.lang.Double) r1
            double r1 = r1.doubleValue()
            float[] r4 = new float[r10]
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r9 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r9
            android.graphics.Matrix r5 = r9.getMatrix()
            r5.getValues(r4)
            java.lang.Object[] r5 = new java.lang.Object[r7]
            double r1 = r3.webToNative(r1, r5)
            float r1 = (float) r1
            r4[r8] = r1
            r9.setMatrixValues(r4)
            goto L_0x034f
        L_0x01f7:
            java.lang.String r5 = "svg-transform.translateY"
            boolean r5 = r5.equals(r1)
            r14 = 5
            if (r5 == 0) goto L_0x0220
            r1 = r2
            java.lang.Double r1 = (java.lang.Double) r1
            double r1 = r1.doubleValue()
            float[] r4 = new float[r10]
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r9 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r9
            android.graphics.Matrix r5 = r9.getMatrix()
            r5.getValues(r4)
            java.lang.Object[] r5 = new java.lang.Object[r7]
            double r1 = r3.webToNative(r1, r5)
            float r1 = (float) r1
            r4[r14] = r1
            r9.setMatrixValues(r4)
            goto L_0x034f
        L_0x0220:
            java.lang.String r5 = "svg-transform.scaleX"
            boolean r5 = r5.equals(r1)
            if (r5 == 0) goto L_0x0242
            r1 = r2
            java.lang.Double r1 = (java.lang.Double) r1
            double r1 = r1.doubleValue()
            float[] r3 = new float[r10]
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r9 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r9
            android.graphics.Matrix r4 = r9.getMatrix()
            r4.getValues(r3)
            float r1 = (float) r1
            r3[r7] = r1
            r9.setMatrixValues(r3)
            goto L_0x034f
        L_0x0242:
            java.lang.String r5 = "svg-transform.scaleY"
            boolean r5 = r5.equals(r1)
            if (r5 == 0) goto L_0x0264
            r1 = r2
            java.lang.Double r1 = (java.lang.Double) r1
            double r1 = r1.doubleValue()
            float[] r3 = new float[r10]
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r9 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r9
            android.graphics.Matrix r4 = r9.getMatrix()
            r4.getValues(r3)
            float r1 = (float) r1
            r3[r13] = r1
            r9.setMatrixValues(r3)
            goto L_0x034f
        L_0x0264:
            java.lang.String r5 = "svg-transform.rotate"
            boolean r5 = r5.equals(r1)
            if (r5 == 0) goto L_0x02fe
            r1 = r2
            java.lang.Double r1 = (java.lang.Double) r1
            double r1 = r1.doubleValue()
            float[] r5 = new float[r10]
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r9 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r9
            android.graphics.Matrix r10 = r9.getMatrix()
            r10.getValues(r5)
            double r1 = java.lang.Math.toRadians(r1)
            double r14 = java.lang.Math.cos(r1)
            float r10 = (float) r14
            r5[r7] = r10
            double r14 = java.lang.Math.sin(r1)
            double r14 = -r14
            float r10 = (float) r14
            r5[r6] = r10
            double r14 = java.lang.Math.sin(r1)
            float r10 = (float) r14
            r5[r12] = r10
            double r14 = java.lang.Math.cos(r1)
            float r10 = (float) r14
            r5[r13] = r10
            java.lang.String r10 = "transformOrigin"
            java.lang.Object r4 = r4.get(r10)
            java.lang.String r4 = com.taobao.weex.utils.WXUtils.getString(r4, r11)
            boolean r10 = android.text.TextUtils.isEmpty(r4)
            if (r10 != 0) goto L_0x02fa
            java.lang.String r4 = r4.trim()
            java.lang.String r10 = " "
            java.lang.String[] r4 = r4.split(r10)
            int r10 = r4.length
            if (r10 < r8) goto L_0x02fa
            r10 = r4[r7]
            int r10 = com.taobao.weex.utils.WXUtils.getInt(r10)
            double r10 = (double) r10
            java.lang.Object[] r12 = new java.lang.Object[r7]
            double r10 = r3.webToNative(r10, r12)
            r4 = r4[r6]
            int r4 = com.taobao.weex.utils.WXUtils.getInt(r4)
            double r12 = (double) r4
            java.lang.Object[] r4 = new java.lang.Object[r7]
            double r3 = r3.webToNative(r12, r4)
            double r12 = -r10
            double r14 = java.lang.Math.cos(r1)
            double r14 = r14 * r12
            double r14 = r14 + r10
            double r10 = java.lang.Math.sin(r1)
            double r10 = r10 * r3
            double r14 = r14 + r10
            float r7 = (float) r14
            r5[r8] = r7
            double r7 = java.lang.Math.sin(r1)
            double r12 = r12 * r7
            double r12 = r12 + r3
            double r1 = java.lang.Math.cos(r1)
            double r3 = r3 * r1
            double r12 = r12 - r3
            float r1 = (float) r12
            r2 = 5
            r5[r2] = r1
        L_0x02fa:
            r9.setMatrixValues(r5)
            goto L_0x034f
        L_0x02fe:
            java.lang.String r3 = "svg-transform.skewX"
            boolean r3 = r3.equals(r1)
            if (r3 == 0) goto L_0x0327
            r1 = r2
            java.lang.Double r1 = (java.lang.Double) r1
            double r1 = r1.doubleValue()
            float[] r3 = new float[r10]
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r9 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r9
            android.graphics.Matrix r4 = r9.getMatrix()
            r4.getValues(r3)
            double r1 = java.lang.Math.toRadians(r1)
            double r1 = java.lang.Math.tan(r1)
            float r1 = (float) r1
            r3[r6] = r1
            r9.setMatrixValues(r3)
            goto L_0x034f
        L_0x0327:
            java.lang.String r3 = "svg-transform.skewY"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L_0x034f
            r1 = r2
            java.lang.Double r1 = (java.lang.Double) r1
            double r1 = r1.doubleValue()
            float[] r3 = new float[r10]
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r9 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r9
            android.graphics.Matrix r4 = r9.getMatrix()
            r4.getValues(r3)
            double r1 = java.lang.Math.toRadians(r1)
            double r1 = java.lang.Math.tan(r1)
            float r1 = (float) r1
            r3[r12] = r1
            r9.setMatrixValues(r3)
        L_0x034f:
            return r6
        L_0x0350:
            return r7
        L_0x0351:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.svg.SVGInterceptor.updateView(android.view.View, java.lang.String, java.lang.Object, com.alibaba.android.bindingx.core.PlatformManager$IDeviceResolutionTranslator, java.util.Map, java.lang.Object[]):boolean");
    }

    private void handleMultiplePathProperty(@NonNull List list, @NonNull SVGPathComponent sVGPathComponent) {
        DrawCmd resolveDrawCmd;
        String dString = sVGPathComponent.getDString();
        if (!TextUtils.isEmpty(dString)) {
            LinkedList linkedList = new LinkedList();
            for (Object next : list) {
                if ((next instanceof List) && (resolveDrawCmd = resolveDrawCmd((List) next)) != null) {
                    linkedList.add(resolveDrawCmd);
                }
            }
            String interpolatePathArrayBy = interpolatePathArrayBy(dString, linkedList);
            if (!TextUtils.isEmpty(interpolatePathArrayBy) && !dString.equals(interpolatePathArrayBy)) {
                sVGPathComponent.setD(interpolatePathArrayBy);
            }
        }
    }

    private void handleSinglePathProperty(@NonNull List list, @NonNull SVGPathComponent sVGPathComponent) {
        DrawCmd resolveDrawCmd;
        String dString = sVGPathComponent.getDString();
        if (!TextUtils.isEmpty(dString) && (resolveDrawCmd = resolveDrawCmd(list)) != null) {
            String interpolatePathBy = interpolatePathBy(dString, resolveDrawCmd);
            if (!TextUtils.isEmpty(interpolatePathBy) && !dString.equals(interpolatePathBy)) {
                sVGPathComponent.setD(interpolatePathBy);
            }
        }
    }

    private String interpolatePathBy(@NonNull String str, @NonNull DrawCmd drawCmd) {
        int i = drawCmd.index;
        String str2 = drawCmd.cmd;
        String str3 = drawCmd.value;
        if (TextUtils.isEmpty(str2) && TextUtils.isEmpty(str3)) {
            return str;
        }
        int i2 = -1;
        int i3 = -1;
        int i4 = -1;
        char c = 0;
        for (int i5 = 0; i5 < str.length(); i5++) {
            char charAt = str.charAt(i5);
            if (Character.isLetter(charAt) && charAt != 'e') {
                i4++;
                if (i4 == i && i3 == -1) {
                    i3 = i5;
                    c = charAt;
                } else if (i4 == i + 1 && i2 == -1) {
                    i2 = i5;
                }
            }
        }
        if (i2 == -1) {
            i2 = str.length();
        }
        if (i3 == -1) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, i3));
        boolean isEmpty = TextUtils.isEmpty(str2);
        Object obj = str2;
        if (isEmpty) {
            obj = Character.valueOf(c);
        }
        sb.append(obj);
        sb.append(str3);
        sb.append(str.substring(i2, str.length()));
        return sb.toString();
    }

    private String interpolatePathArrayBy(@NonNull String str, @NonNull LinkedList<DrawCmd> linkedList) {
        Iterator it = linkedList.iterator();
        while (it.hasNext()) {
            str = interpolatePathBy(str, (DrawCmd) it.next());
        }
        return str;
    }

    private DrawCmd resolveDrawCmd(@NonNull List list) {
        int intValue = WXUtils.getInteger(list.get(0), -1).intValue();
        String str = null;
        if (intValue <= -1) {
            return null;
        }
        List list2 = (list.size() < 2 || !(list.get(1) instanceof List)) ? null : (List) list.get(1);
        if (list.size() >= 3) {
            str = WXUtils.getString(list.get(2), "");
            if ((str.startsWith(DXBindingXConstant.SINGLE_QUOTE) || str.startsWith("\"")) && str.length() >= 3) {
                str = str.substring(1, str.length() - 1);
            }
        }
        StringBuilder sb = new StringBuilder();
        if (list2 != null) {
            for (Object next : list2) {
                if (next instanceof Double) {
                    Double d = (Double) next;
                    sb.append(Double.isNaN(d.doubleValue()) ? 0.0d : d.doubleValue());
                    sb.append(Operators.SPACE_STR);
                }
            }
        }
        return new DrawCmd(intValue, str, sb.toString());
    }

    static class DrawCmd {
        String cmd;
        int index;
        String value;

        DrawCmd(int i, String str, String str2) {
            this.index = i;
            this.cmd = str;
            this.value = str2;
        }
    }
}
