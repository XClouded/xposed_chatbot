package com.alibaba.android.enhance.svg.component;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent;
import com.alibaba.android.enhance.svg.utils.PropHelper;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class SVGCircleComponent extends RenderableSVGVirtualComponent {
    private String mCx;
    private String mCy;
    private String mR;

    public SVGCircleComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    @WXComponentProp(name = "cx")
    public void setCx(String str) {
        this.mCx = str;
        markUpdated();
    }

    @WXComponentProp(name = "cy")
    public void setCy(String str) {
        this.mCy = str;
        markUpdated();
    }

    @WXComponentProp(name = "r")
    public void setR(String str) {
        this.mR = str;
        markUpdated();
    }

    public Path getPath(Canvas canvas, Paint paint) {
        return getPath(canvas, paint, (RectF) null);
    }

    public Path getPath(Canvas canvas, Paint paint, RectF rectF) {
        double d;
        Path path = new Path();
        double relativeOnWidth = relativeOnWidth(this.mCx);
        double relativeOnHeight = relativeOnHeight(this.mCy);
        if (PropHelper.isPercentage(this.mR)) {
            d = relativeOnOther(this.mR);
        } else {
            double parseDouble = Double.parseDouble(this.mR);
            double d2 = (double) this.mScale;
            Double.isNaN(d2);
            d = parseDouble * d2;
        }
        if (rectF != null) {
            float width = rectF.width();
            float height = rectF.height();
            float f = rectF.left;
            float f2 = rectF.top;
            double resolveFloatFromString = (double) ((PropHelper.resolveFloatFromString(this.mCx) * width) + f);
            double resolveFloatFromString2 = (double) ((PropHelper.resolveFloatFromString(this.mCy) * height) + f2);
            double resolveFloatFromString3 = (double) (PropHelper.resolveFloatFromString(this.mR) * width);
            double resolveFloatFromString4 = (double) (PropHelper.resolveFloatFromString(this.mR) * height);
            Double.isNaN(resolveFloatFromString);
            Double.isNaN(resolveFloatFromString3);
            Double.isNaN(resolveFloatFromString2);
            Double.isNaN(resolveFloatFromString4);
            Double.isNaN(resolveFloatFromString);
            Double.isNaN(resolveFloatFromString3);
            Double.isNaN(resolveFloatFromString2);
            Double.isNaN(resolveFloatFromString4);
            path.addOval(new RectF((float) (resolveFloatFromString - resolveFloatFromString3), (float) (resolveFloatFromString2 - resolveFloatFromString4), (float) (resolveFloatFromString + resolveFloatFromString3), (float) (resolveFloatFromString2 + resolveFloatFromString4)), Path.Direction.CW);
        } else {
            path.addCircle((float) relativeOnWidth, (float) relativeOnHeight, (float) d, Path.Direction.CW);
        }
        return path;
    }
}
