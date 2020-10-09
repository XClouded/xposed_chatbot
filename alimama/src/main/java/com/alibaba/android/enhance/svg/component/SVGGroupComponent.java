package com.alibaba.android.enhance.svg.component;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent;
import com.alibaba.android.enhance.svg.ISVGVirtualNode;
import com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;

public class SVGGroupComponent extends RenderableSVGVirtualComponent {
    public SVGGroupComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public void draw(Canvas canvas, Paint paint, float f) {
        draw(canvas, paint, f, (RectF) null);
    }

    public void draw(Canvas canvas, Paint paint, float f, @Nullable RectF rectF) {
        if (f > 0.01f) {
            preProcessIfHasMask(canvas);
            clip(canvas, paint);
            drawGroup(canvas, paint, f, rectF);
            applyMask(canvas, paint);
        }
    }

    private void drawGroup(Canvas canvas, Paint paint, float f, RectF rectF) {
        final Canvas canvas2 = canvas;
        final RectF rectF2 = rectF;
        final Paint paint2 = paint;
        final float f2 = f;
        traverseChildren(new AbstractSVGVirtualComponent.NodeRunnable() {
            /* JADX WARNING: Removed duplicated region for block: B:18:0x0064 A[DONT_GENERATE] */
            /* JADX WARNING: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run(com.alibaba.android.enhance.svg.ISVGVirtualNode r6) {
                /*
                    r5 = this;
                    boolean r0 = r6 instanceof com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent
                    if (r0 == 0) goto L_0x0075
                    boolean r0 = r6 instanceof com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent     // Catch:{ all -> 0x006a }
                    if (r0 == 0) goto L_0x0010
                    r0 = r6
                    com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r0 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r0     // Catch:{ all -> 0x006a }
                    com.alibaba.android.enhance.svg.component.SVGGroupComponent r1 = r2     // Catch:{ all -> 0x006a }
                    r0.mergeProperties(r1)     // Catch:{ all -> 0x006a }
                L_0x0010:
                    r0 = r6
                    com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent r0 = (com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent) r0     // Catch:{ all -> 0x006a }
                    android.graphics.Canvas r1 = r3     // Catch:{ all -> 0x006a }
                    int r0 = r0.saveAndSetupCanvas(r1)     // Catch:{ all -> 0x006a }
                    android.graphics.RectF r1 = r4     // Catch:{ all -> 0x006a }
                    if (r1 == 0) goto L_0x0047
                    android.graphics.RectF r1 = r4     // Catch:{ all -> 0x006a }
                    float r1 = r1.width()     // Catch:{ all -> 0x006a }
                    r2 = 0
                    int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
                    if (r1 == 0) goto L_0x0047
                    android.graphics.RectF r1 = r4     // Catch:{ all -> 0x006a }
                    float r1 = r1.height()     // Catch:{ all -> 0x006a }
                    int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
                    if (r1 != 0) goto L_0x0033
                    goto L_0x0047
                L_0x0033:
                    android.graphics.Canvas r1 = r3     // Catch:{ all -> 0x006a }
                    android.graphics.Paint r2 = r5     // Catch:{ all -> 0x006a }
                    float r3 = r6     // Catch:{ all -> 0x006a }
                    com.alibaba.android.enhance.svg.component.SVGGroupComponent r4 = com.alibaba.android.enhance.svg.component.SVGGroupComponent.this     // Catch:{ all -> 0x006a }
                    float r4 = r4.mOpacity     // Catch:{ all -> 0x006a }
                    float r3 = r3 * r4
                    android.graphics.RectF r4 = r4     // Catch:{ all -> 0x006a }
                    r6.draw(r1, r2, r3, r4)     // Catch:{ all -> 0x006a }
                    goto L_0x0058
                L_0x0047:
                    android.graphics.Canvas r1 = r3     // Catch:{ all -> 0x006a }
                    android.graphics.Paint r2 = r5     // Catch:{ all -> 0x006a }
                    float r3 = r6     // Catch:{ all -> 0x006a }
                    com.alibaba.android.enhance.svg.component.SVGGroupComponent r4 = com.alibaba.android.enhance.svg.component.SVGGroupComponent.this     // Catch:{ all -> 0x006a }
                    float r4 = r4.mOpacity     // Catch:{ all -> 0x006a }
                    float r3 = r3 * r4
                    r6.draw(r1, r2, r3)     // Catch:{ all -> 0x006a }
                L_0x0058:
                    r1 = r6
                    com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent r1 = (com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent) r1     // Catch:{ all -> 0x006a }
                    android.graphics.Canvas r2 = r3     // Catch:{ all -> 0x006a }
                    r1.restoreCanvas(r2, r0)     // Catch:{ all -> 0x006a }
                    boolean r0 = r6 instanceof com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent
                    if (r0 == 0) goto L_0x0075
                    com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r6 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r6
                    r6.resetProperties()
                    goto L_0x0075
                L_0x006a:
                    r0 = move-exception
                    boolean r1 = r6 instanceof com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent
                    if (r1 == 0) goto L_0x0074
                    com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r6 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r6
                    r6.resetProperties()
                L_0x0074:
                    throw r0
                L_0x0075:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.svg.component.SVGGroupComponent.AnonymousClass1.run(com.alibaba.android.enhance.svg.ISVGVirtualNode):void");
            }
        });
    }

    public Path getPath(final Canvas canvas, final Paint paint) {
        final Path path = new Path();
        traverseChildren(new AbstractSVGVirtualComponent.NodeRunnable() {
            public void run(ISVGVirtualNode iSVGVirtualNode) {
                Path path = iSVGVirtualNode.getPath(canvas, paint);
                if (path != null) {
                    path.addPath(path);
                }
            }
        });
        return path;
    }

    /* access modifiers changed from: protected */
    public void drawPath(Canvas canvas, Paint paint, float f) {
        super.draw(canvas, paint, f);
    }

    public AbstractSVGVirtualComponent hitTest(float[] fArr) {
        if (fArr == null || fArr.length != 2 || !this.mInvertible || this.mInvMatrix == null) {
            return null;
        }
        float[] fArr2 = new float[2];
        this.mInvMatrix.mapPoints(fArr2, fArr);
        int round = Math.round(fArr2[0]);
        int round2 = Math.round(fArr2[1]);
        Path clipPath = getClipPath();
        if (clipPath != null) {
            if (this.mClipRegionPath != clipPath) {
                this.mClipRegionPath = clipPath;
                this.mClipRegion = getRegion(clipPath);
            }
            if (!this.mClipRegion.contains(round, round2)) {
                return null;
            }
        }
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            WXComponent child = getChild(childCount);
            if ((child instanceof RenderableSVGVirtualComponent) && ((RenderableSVGVirtualComponent) child).hitTest(fArr2) != null) {
                return this;
            }
        }
        return null;
    }

    public void saveDefinition() {
        super.saveDefinition();
        traverseChildren(new AbstractSVGVirtualComponent.NodeRunnable() {
            public void run(ISVGVirtualNode iSVGVirtualNode) {
                if (iSVGVirtualNode instanceof AbstractSVGVirtualComponent) {
                    ((AbstractSVGVirtualComponent) iSVGVirtualNode).saveDefinition();
                }
            }
        });
    }
}
