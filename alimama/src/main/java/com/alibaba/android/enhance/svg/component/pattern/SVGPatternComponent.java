package com.alibaba.android.enhance.svg.component.pattern;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent;
import com.alibaba.android.enhance.svg.Brush;
import com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent;
import com.alibaba.android.enhance.svg.component.SVGGroupComponent;
import com.alibaba.android.enhance.svg.component.SVGViewComponent;
import com.alibaba.android.enhance.svg.component.pattern.PatternBrush;
import com.alibaba.android.enhance.svg.parser.SVGTransformParser;
import com.alibaba.android.enhance.svg.utils.PropHelper;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class SVGPatternComponent extends SVGGroupComponent {
    private float mH;
    private Brush.BrushUnits mPatternContentUnits = Brush.BrushUnits.USER_SPACE_ON_USE;
    private Matrix mPatternMatrix = null;
    private Brush.BrushUnits mPatternUnits = Brush.BrushUnits.OBJECT_BOUNDING_BOX;
    private float mW;
    private float mX;
    private float mY;

    public void draw(Canvas canvas, Paint paint, float f) {
    }

    public AbstractSVGVirtualComponent hitTest(float[] fArr) {
        return null;
    }

    public void mergeProperties(RenderableSVGVirtualComponent renderableSVGVirtualComponent) {
    }

    public void resetProperties() {
    }

    public SVGPatternComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    @WXComponentProp(name = "x")
    public void setX(String str) {
        this.mX = PropHelper.resolveFloatFromString(str);
        markUpdated();
    }

    @WXComponentProp(name = "y")
    public void setY(String str) {
        this.mY = PropHelper.resolveFloatFromString(str);
        markUpdated();
    }

    @WXComponentProp(name = "width")
    public void setWidth(String str) {
        this.mW = PropHelper.resolveFloatFromString(str);
        markUpdated();
    }

    @WXComponentProp(name = "height")
    public void setHeight(String str) {
        this.mH = PropHelper.resolveFloatFromString(str);
        markUpdated();
    }

    @WXComponentProp(name = "patternUnits")
    public void setPatternUnits(String str) {
        if (AbstractSVGVirtualComponent.UNIT_USER_SPACE_ON_USE.equals(str)) {
            this.mPatternUnits = Brush.BrushUnits.USER_SPACE_ON_USE;
        } else if (AbstractSVGVirtualComponent.UNIT_OBJECT_BOUNDING_BOX.equals(str)) {
            this.mPatternUnits = Brush.BrushUnits.OBJECT_BOUNDING_BOX;
        } else {
            this.mPatternUnits = Brush.BrushUnits.OBJECT_BOUNDING_BOX;
        }
        markUpdated();
    }

    @WXComponentProp(name = "patternContentUnits")
    public void setPatternContentUnits(String str) {
        if (AbstractSVGVirtualComponent.UNIT_USER_SPACE_ON_USE.equals(str)) {
            this.mPatternContentUnits = Brush.BrushUnits.USER_SPACE_ON_USE;
        } else if (AbstractSVGVirtualComponent.UNIT_OBJECT_BOUNDING_BOX.equals(str)) {
            this.mPatternContentUnits = Brush.BrushUnits.OBJECT_BOUNDING_BOX;
        } else {
            this.mPatternContentUnits = Brush.BrushUnits.USER_SPACE_ON_USE;
        }
        markUpdated();
    }

    @WXComponentProp(name = "patternTransform")
    public void setPatternTransform(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (this.mPatternMatrix == null) {
                this.mPatternMatrix = new Matrix();
            }
            this.mPatternMatrix = new SVGTransformParser().parseTransform(str, this.mScale != 0.0f ? this.mScale : 1.0f);
        } else {
            this.mPatternMatrix = null;
        }
        markUpdated();
    }

    public void saveDefinition() {
        SVGViewComponent sVGViewComponent;
        String findComponentId = findComponentId();
        if (!TextUtils.isEmpty(findComponentId) && (sVGViewComponent = getSVGViewComponent()) != null) {
            PatternBrush patternBrush = new PatternBrush(this.mX, this.mY, this.mW, this.mH, new PatternBrush.PatternDrawer() {
                public void drawPattern(Canvas canvas, Paint paint, @Nullable RectF rectF) {
                    SVGPatternComponent.super.draw(canvas, new Paint(), 1.0f, rectF);
                }
            });
            patternBrush.setPatternUnits(this.mPatternUnits);
            patternBrush.setPatternContentUnits(this.mPatternContentUnits);
            if (this.mPatternMatrix != null) {
                patternBrush.setPatternTransform(this.mPatternMatrix);
            }
            sVGViewComponent.defineBrush(patternBrush, findComponentId);
        }
    }
}
