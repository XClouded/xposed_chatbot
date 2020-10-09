package com.alibaba.android.enhance.svg.component;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent;
import com.alibaba.android.enhance.svg.utils.PropHelper;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class SVGPolylineComponent extends RenderableSVGVirtualComponent {
    private Path mPath;
    private PropHelper.PathParser mPathParser;

    public SVGPolylineComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    @WXComponentProp(name = "points")
    public void setPoints(String str) {
        this.mPathParser = new PropHelper.PathParser("M" + PropHelper.pointsToDString(str), this.mScale);
        this.mPath = this.mPathParser.getPath();
        markUpdated();
    }

    public Path getPath(Canvas canvas, Paint paint) {
        return this.mPath;
    }

    public Path getPath(Canvas canvas, Paint paint, RectF rectF) {
        if (this.mPathParser == null || rectF == null) {
            return this.mPath;
        }
        Path path = this.mPathParser.getPath(rectF);
        Matrix matrix = new Matrix();
        matrix.setTranslate(rectF.left, rectF.top);
        path.transform(matrix);
        return path;
    }
}
