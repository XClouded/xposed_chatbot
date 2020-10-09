package com.alibaba.android.enhance.svg.component.mask;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent;
import com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent;
import com.alibaba.android.enhance.svg.component.SVGGroupComponent;
import com.alibaba.android.enhance.svg.component.SVGViewComponent;
import com.alibaba.android.enhance.svg.component.mask.MaskNode;
import com.alibaba.android.enhance.svg.utils.PropHelper;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class SVGMaskComponent extends SVGGroupComponent {
    private float mH = 1.2f;
    private MaskNode.Units mMaskContentUnits = MaskNode.Units.USER_SPACE_ON_USE;
    private MaskNode.Units mMaskUnits = MaskNode.Units.OBJECT_BOUNDING_BOX;
    private float mW = 1.2f;
    private float mX = -0.1f;
    private float mY = -0.1f;

    public void draw(Canvas canvas, Paint paint, float f) {
    }

    public AbstractSVGVirtualComponent hitTest(float[] fArr) {
        return null;
    }

    public void mergeProperties(RenderableSVGVirtualComponent renderableSVGVirtualComponent) {
    }

    public void resetProperties() {
    }

    public SVGMaskComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    @WXComponentProp(name = "maskUnits")
    public void setMaskUnits(String str) {
        if (AbstractSVGVirtualComponent.UNIT_USER_SPACE_ON_USE.equals(str)) {
            this.mMaskUnits = MaskNode.Units.USER_SPACE_ON_USE;
        } else if (AbstractSVGVirtualComponent.UNIT_OBJECT_BOUNDING_BOX.equals(str)) {
            this.mMaskUnits = MaskNode.Units.OBJECT_BOUNDING_BOX;
        } else {
            this.mMaskUnits = MaskNode.Units.OBJECT_BOUNDING_BOX;
        }
        markUpdated();
    }

    @WXComponentProp(name = "maskContentUnits")
    public void setMaskContentUnits(String str) {
        if (AbstractSVGVirtualComponent.UNIT_USER_SPACE_ON_USE.equals(str)) {
            this.mMaskContentUnits = MaskNode.Units.USER_SPACE_ON_USE;
        } else if (AbstractSVGVirtualComponent.UNIT_OBJECT_BOUNDING_BOX.equals(str)) {
            this.mMaskContentUnits = MaskNode.Units.OBJECT_BOUNDING_BOX;
        } else {
            this.mMaskContentUnits = MaskNode.Units.USER_SPACE_ON_USE;
        }
        markUpdated();
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

    public void saveDefinition() {
        SVGViewComponent sVGViewComponent;
        if (!TextUtils.isEmpty(findComponentId()) && (sVGViewComponent = getSVGViewComponent()) != null) {
            MaskNode maskNode = new MaskNode(this.mX, this.mY, this.mW, this.mH, new MaskNode.MaskContentDrawer() {
                public void drawMaskContent(Canvas canvas, Paint paint, @Nullable RectF rectF) {
                    SVGMaskComponent.super.draw(canvas, new Paint(), 1.0f, rectF);
                }
            });
            maskNode.setMaskUnits(this.mMaskUnits);
            maskNode.setMaskContentUnits(this.mMaskContentUnits);
            sVGViewComponent.defineMask(findComponentId(), maskNode);
        }
    }
}
