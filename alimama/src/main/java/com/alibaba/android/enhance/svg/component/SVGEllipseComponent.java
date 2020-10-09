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

public class SVGEllipseComponent extends RenderableSVGVirtualComponent {
    private String mCx;
    private String mCy;
    private String mRx;
    private String mRy;

    public SVGEllipseComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
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
        double d;
        Path path = new Path();
        double relativeOnWidth = relativeOnWidth(this.mCx);
        double relativeOnHeight = relativeOnHeight(this.mCy);
        double relativeOnWidth2 = relativeOnWidth(this.mRx);
        double relativeOnHeight2 = relativeOnHeight(this.mRy);
        if (rectF != null) {
            float width = rectF.width();
            float height = rectF.height();
            float f = rectF.left;
            float f2 = rectF.top;
            d = (double) (PropHelper.resolveFloatFromString(this.mRx) * width);
            relativeOnHeight2 = (double) (PropHelper.resolveFloatFromString(this.mRy) * height);
            relativeOnWidth = (double) ((PropHelper.resolveFloatFromString(this.mCx) * width) + f);
            relativeOnHeight = (double) ((PropHelper.resolveFloatFromString(this.mCy) * height) + f2);
        } else {
            d = relativeOnWidth2;
        }
        path.addOval(new RectF((float) (relativeOnWidth - d), (float) (relativeOnHeight - relativeOnHeight2), (float) (relativeOnWidth + d), (float) (relativeOnHeight + relativeOnHeight2)), Path.Direction.CW);
        return path;
    }
}
