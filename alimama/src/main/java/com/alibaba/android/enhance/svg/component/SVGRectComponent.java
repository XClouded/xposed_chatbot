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

public class SVGRectComponent extends RenderableSVGVirtualComponent {
    private String mH;
    private String mRx;
    private String mRy;
    private String mW;
    private String mX;
    private String mY;

    public SVGRectComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
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

    @WXComponentProp(name = "rx")
    public void setRx(String str) {
        this.mRx = str;
        markUpdated();
    }

    @WXComponentProp(name = "ry")
    public void setRy(String str) {
        this.mRy = str;
        markUpdated();
    }

    public Path getPath(Canvas canvas, Paint paint) {
        return getPath(canvas, paint, (RectF) null);
    }

    public Path getPath(Canvas canvas, Paint paint, RectF rectF) {
        RectF rectF2 = rectF;
        Path path = new Path();
        double relativeOnWidth = relativeOnWidth(this.mX);
        double relativeOnHeight = relativeOnHeight(this.mY);
        double relativeOnWidth2 = relativeOnWidth(this.mW);
        double relativeOnHeight2 = relativeOnHeight(this.mH);
        double relativeOnWidth3 = relativeOnWidth(this.mRx);
        double relativeOnHeight3 = relativeOnHeight(this.mRy);
        if (rectF2 != null) {
            float width = rectF.width();
            float height = rectF.height();
            float f = rectF2.left;
            double resolveFloatFromString = (double) ((PropHelper.resolveFloatFromString(this.mY) * height) + rectF2.top);
            double resolveFloatFromString2 = (double) (PropHelper.resolveFloatFromString(this.mW) * width);
            double resolveFloatFromString3 = (double) (PropHelper.resolveFloatFromString(this.mH) * height);
            relativeOnHeight3 = (double) (PropHelper.resolveFloatFromString(this.mRy) * height);
            double d = resolveFloatFromString2;
            relativeOnHeight2 = resolveFloatFromString3;
            relativeOnWidth3 = (double) (PropHelper.resolveFloatFromString(this.mRx) * width);
            relativeOnWidth = (double) ((PropHelper.resolveFloatFromString(this.mX) * width) + f);
            relativeOnHeight = resolveFloatFromString;
            relativeOnWidth2 = d;
        }
        if (relativeOnWidth3 == 0.0d && relativeOnHeight3 == 0.0d) {
            path.addRect((float) relativeOnWidth, (float) relativeOnHeight, (float) (relativeOnWidth + relativeOnWidth2), (float) (relativeOnHeight + relativeOnHeight2), Path.Direction.CW);
        } else {
            if (relativeOnWidth3 == 0.0d) {
                relativeOnWidth3 = relativeOnHeight3;
            } else if (relativeOnHeight3 == 0.0d) {
                relativeOnHeight3 = relativeOnWidth3;
            }
            double d2 = relativeOnWidth2 / 2.0d;
            if (relativeOnWidth3 > d2) {
                relativeOnWidth3 = d2;
            }
            double d3 = relativeOnHeight2 / 2.0d;
            if (relativeOnHeight3 > d3) {
                relativeOnHeight3 = d3;
            }
            path.addRoundRect(new RectF((float) relativeOnWidth, (float) relativeOnHeight, (float) (relativeOnWidth + relativeOnWidth2), (float) (relativeOnHeight + relativeOnHeight2)), (float) relativeOnWidth3, (float) relativeOnHeight3, Path.Direction.CW);
        }
        return path;
    }
}
