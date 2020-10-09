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

public class SVGLineComponent extends RenderableSVGVirtualComponent {
    private String mX1;
    private String mX2;
    private String mY1;
    private String mY2;

    public SVGLineComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    @WXComponentProp(name = "x1")
    public void setX1(String str) {
        this.mX1 = str;
        markUpdated();
    }

    @WXComponentProp(name = "y1")
    public void setY1(String str) {
        this.mY1 = str;
        markUpdated();
    }

    @WXComponentProp(name = "x2")
    public void setX2(String str) {
        this.mX2 = str;
        markUpdated();
    }

    @WXComponentProp(name = "y2")
    public void setY2(String str) {
        this.mY2 = str;
        markUpdated();
    }

    public Path getPath(Canvas canvas, Paint paint) {
        return getPath(canvas, paint, (RectF) null);
    }

    public Path getPath(Canvas canvas, Paint paint, RectF rectF) {
        double d;
        Path path = new Path();
        double relativeOnWidth = relativeOnWidth(this.mX1);
        double relativeOnHeight = relativeOnHeight(this.mY1);
        double relativeOnWidth2 = relativeOnWidth(this.mX2);
        double relativeOnHeight2 = relativeOnHeight(this.mY2);
        if (rectF != null) {
            float width = rectF.width();
            float height = rectF.height();
            float f = rectF.left;
            float f2 = rectF.top;
            d = (double) ((PropHelper.resolveFloatFromString(this.mY2) * height) + f2);
            relativeOnWidth = (double) ((PropHelper.resolveFloatFromString(this.mX1) * width) + f);
            relativeOnHeight = (double) ((PropHelper.resolveFloatFromString(this.mY1) * height) + f2);
            relativeOnWidth2 = (double) ((PropHelper.resolveFloatFromString(this.mX2) * width) + f);
        } else {
            d = relativeOnHeight2;
        }
        path.moveTo((float) relativeOnWidth, (float) relativeOnHeight);
        path.lineTo((float) relativeOnWidth2, (float) d);
        return path;
    }
}
