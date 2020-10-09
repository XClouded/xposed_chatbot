package com.alibaba.android.enhance.svg.component;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import com.alibaba.android.enhance.svg.utils.PropHelper;
import com.alibaba.android.enhance.svg.utils.ViewBox;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import java.util.List;

public class SVGSymbolComponent extends SVGGroupComponent {
    private String mAlign = "xMidYMid";
    private boolean mHasViewBox = false;
    private int mMeetOrSlice = 0;
    private float mMinX;
    private float mMinY;
    private float mVbHeight;
    private float mVbWidth;
    private Matrix mViewBoxMatrix = new Matrix();

    public void draw(Canvas canvas, Paint paint, float f) {
    }

    public SVGSymbolComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public void drawSymbol(Canvas canvas, Paint paint, float f, double d, double d2, double d3, double d4) {
        float f2;
        canvas.translate((float) d3, (float) d4);
        SVGViewComponent sVGViewComponent = getSVGViewComponent();
        float f3 = Float.MAX_VALUE;
        if (sVGViewComponent != null) {
            f3 = sVGViewComponent.getLayoutWidth();
            f2 = sVGViewComponent.getLayoutHeight();
        } else {
            f2 = Float.MAX_VALUE;
        }
        if (d > 0.0d) {
            f3 = (float) d;
        }
        if (d2 > 0.0d) {
            f2 = (float) d2;
        }
        canvas.clipRect(new RectF(0.0f, 0.0f, f3, f2));
        if (this.mHasViewBox) {
            Matrix transform = ViewBox.getTransform(new RectF(this.mMinX * this.mScale, this.mMinY * this.mScale, (this.mMinX + this.mVbWidth) * this.mScale, (this.mMinY + this.mVbHeight) * this.mScale), new RectF(0.0f, 0.0f, f3, f2), this.mAlign, this.mMeetOrSlice);
            this.mViewBoxMatrix = transform;
            canvas.concat(transform);
        }
        super.draw(canvas, paint, f);
    }

    public Path getPath(Canvas canvas, Paint paint) {
        Matrix matrix = new Matrix();
        matrix.preConcat(this.mViewBoxMatrix);
        Path path = super.getPath(canvas, paint);
        path.transform(matrix);
        return path;
    }

    @WXComponentProp(name = "viewBox")
    public void setViewBox(String str) {
        List<Float> resolveViewBoxFromString = PropHelper.resolveViewBoxFromString(str);
        if (resolveViewBoxFromString == null || resolveViewBoxFromString.size() != 4) {
            this.mHasViewBox = false;
            return;
        }
        this.mMinX = resolveViewBoxFromString.get(0).floatValue();
        this.mMinY = resolveViewBoxFromString.get(1).floatValue();
        this.mVbWidth = resolveViewBoxFromString.get(2).floatValue();
        this.mVbHeight = resolveViewBoxFromString.get(3).floatValue();
        this.mHasViewBox = true;
        if (getSVGViewComponent() != null) {
            getSVGViewComponent().setHardwareAcceleration(false);
        }
        markUpdated();
    }

    @WXComponentProp(name = "preserveAspectRatio")
    public void setPreserveAspectRatio(String str) {
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.trim().split("\\s+");
            if (split.length >= 1 && split.length <= 2) {
                if (split.length != 1) {
                    this.mAlign = split[0];
                    this.mMeetOrSlice = ViewBox.parseMeetOrSlice(split[1]);
                } else if (split[0].equals("none")) {
                    this.mMeetOrSlice = 2;
                } else {
                    this.mAlign = split[0];
                    this.mMeetOrSlice = ViewBox.parseMeetOrSlice((String) null);
                }
                markUpdated();
            }
        }
    }
}
