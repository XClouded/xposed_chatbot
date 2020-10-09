package com.alibaba.android.enhance.svg.component;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent;
import com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent;
import com.alibaba.android.enhance.svg.utils.PropHelper;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class SVGUseComponent extends RenderableSVGVirtualComponent {
    private Path mContentPath;
    private String mH;
    private String mHref;
    private double mOffsetX;
    private double mOffsetY;
    private String mW;
    private String mX;
    private String mY;

    public Path getPath(Canvas canvas, Paint paint) {
        return null;
    }

    public SVGUseComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    @WXComponentProp(name = "xlink:href")
    public void setHref(String str) {
        this.mHref = PropHelper.resolveHrefFromString(str);
        markUpdated();
    }

    @WXComponentProp(name = "href")
    public void setHref2(String str) {
        this.mHref = PropHelper.resolveHrefFromString(str);
        markUpdated();
    }

    @WXComponentProp(name = "x")
    public void setX(String str) {
        this.mX = str;
        markUpdated();
    }

    @WXComponentProp(name = "y")
    public void setY(String str) {
        this.mY = str;
        markUpdated();
    }

    @WXComponentProp(name = "width")
    public void setWidth(String str) {
        this.mW = str;
        markUpdated();
    }

    @WXComponentProp(name = "height")
    public void setHeight(String str) {
        this.mH = str;
        markUpdated();
    }

    public void draw(Canvas canvas, Paint paint, float f) {
        RenderableSVGVirtualComponent definedGraphicalTemplate;
        int i;
        Canvas canvas2 = canvas;
        Paint paint2 = paint;
        SVGViewComponent sVGViewComponent = getSVGViewComponent();
        if (sVGViewComponent != null && (definedGraphicalTemplate = sVGViewComponent.getDefinedGraphicalTemplate(this.mHref)) != null) {
            try {
                definedGraphicalTemplate.mergeProperties(this);
                int saveAndSetupCanvas = definedGraphicalTemplate.saveAndSetupCanvas(canvas2);
                clip(canvas, paint);
                double relativeOnWidth = relativeOnWidth(this.mW);
                double relativeOnHeight = relativeOnHeight(this.mH);
                this.mOffsetX = relativeOnWidth(this.mX);
                this.mOffsetY = relativeOnHeight(this.mY);
                if (definedGraphicalTemplate instanceof SVGSymbolComponent) {
                    i = saveAndSetupCanvas;
                    ((SVGSymbolComponent) definedGraphicalTemplate).drawSymbol(canvas, paint, f, relativeOnWidth, relativeOnHeight, this.mOffsetX, this.mOffsetY);
                } else {
                    i = saveAndSetupCanvas;
                    canvas2.translate((float) this.mOffsetX, (float) this.mOffsetY);
                    definedGraphicalTemplate.draw(canvas2, paint2, f);
                }
                this.mContentPath = definedGraphicalTemplate.getPath(canvas2, paint2);
                definedGraphicalTemplate.restoreCanvas(canvas2, i);
            } finally {
                definedGraphicalTemplate.resetProperties();
            }
        }
    }

    public AbstractSVGVirtualComponent hitTest(float[] fArr) {
        if (fArr == null || fArr.length != 2 || this.mContentPath == null || !this.mInvertible || this.mInvMatrix == null) {
            return null;
        }
        float[] fArr2 = new float[2];
        this.mInvMatrix.mapPoints(fArr2, fArr);
        int round = Math.round(fArr2[0]);
        int round2 = Math.round(fArr2[1]);
        double d = (double) round;
        double d2 = this.mOffsetX;
        Double.isNaN(d);
        int i = (int) (d - d2);
        double d3 = (double) round2;
        double d4 = this.mOffsetY;
        Double.isNaN(d3);
        int i2 = (int) (d3 - d4);
        if (this.mRegion == null) {
            this.mRegion = getRegion(this.mContentPath);
        }
        if (!this.mRegion.contains(i, i2)) {
            return null;
        }
        Path clipPath = getClipPath();
        if (clipPath != null) {
            if (this.mClipRegionPath != clipPath) {
                this.mClipRegionPath = clipPath;
                this.mClipRegion = getRegion(clipPath);
            }
            if (!this.mClipRegion.contains(i, i2)) {
                return null;
            }
        }
        return this;
    }
}
