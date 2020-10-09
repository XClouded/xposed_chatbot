package com.alibaba.android.enhance.svg.component.gradient;

import android.graphics.Matrix;
import android.text.TextUtils;
import com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent;
import com.alibaba.android.enhance.svg.Brush;
import com.alibaba.android.enhance.svg.DefinitionSVGComponent;
import com.alibaba.android.enhance.svg.ISVGVirtualNode;
import com.alibaba.android.enhance.svg.component.SVGViewComponent;
import com.alibaba.android.enhance.svg.component.gradient.SVGStopComponent;
import com.alibaba.android.enhance.svg.parser.SVGTransformParser;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXUtils;
import java.util.LinkedList;

public class SVGLinearGradientComponent extends DefinitionSVGComponent {
    private Matrix mGradientMatrix = null;
    private Brush.BrushUnits mGradientUnits = Brush.BrushUnits.OBJECT_BOUNDING_BOX;
    private Brush.SpreadMethod mSpreadMethod = Brush.SpreadMethod.PAD;
    private String mX1 = "0%";
    private String mX2 = "100%";
    private String mY1 = "0%";
    private String mY2 = "0%";

    public SVGLinearGradientComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
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

    @WXComponentProp(name = "gradientUnits")
    public void setGradientUnits(String str) {
        if (AbstractSVGVirtualComponent.UNIT_USER_SPACE_ON_USE.equals(str)) {
            this.mGradientUnits = Brush.BrushUnits.USER_SPACE_ON_USE;
        } else if (AbstractSVGVirtualComponent.UNIT_OBJECT_BOUNDING_BOX.equals(str)) {
            this.mGradientUnits = Brush.BrushUnits.OBJECT_BOUNDING_BOX;
        } else {
            this.mGradientUnits = Brush.BrushUnits.OBJECT_BOUNDING_BOX;
        }
        markUpdated();
    }

    @WXComponentProp(name = "gradientTransform")
    public void setGradientTransform(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (this.mGradientMatrix == null) {
                this.mGradientMatrix = new Matrix();
            }
            this.mGradientMatrix = new SVGTransformParser().parseTransform(str, 1.0f);
        } else {
            this.mGradientMatrix = null;
        }
        markUpdated();
    }

    @WXComponentProp(name = "spreadMethod")
    public void setSpreadMethod(String str) {
        if ("pad".equals(str)) {
            this.mSpreadMethod = Brush.SpreadMethod.PAD;
        } else if ("reflect".equals(str)) {
            this.mSpreadMethod = Brush.SpreadMethod.REFLECT;
        } else if (DXBindingXConstant.REPEAT.equals(str)) {
            this.mSpreadMethod = Brush.SpreadMethod.REPEAT;
        } else {
            this.mSpreadMethod = Brush.SpreadMethod.PAD;
        }
    }

    public void saveDefinition() {
        SVGViewComponent sVGViewComponent;
        String findComponentId = findComponentId();
        if (!TextUtils.isEmpty(findComponentId) && (sVGViewComponent = getSVGViewComponent()) != null) {
            LinkedList linkedList = new LinkedList();
            linkedList.add(this.mGradientUnits == Brush.BrushUnits.USER_SPACE_ON_USE ? this.mX1 : toRelative(this.mX1, "0%"));
            linkedList.add(this.mGradientUnits == Brush.BrushUnits.USER_SPACE_ON_USE ? this.mY1 : toRelative(this.mY1, "0%"));
            linkedList.add(this.mGradientUnits == Brush.BrushUnits.USER_SPACE_ON_USE ? this.mX2 : toRelative(this.mX2, "100%"));
            linkedList.add(this.mGradientUnits == Brush.BrushUnits.USER_SPACE_ON_USE ? this.mY2 : toRelative(this.mY2, "0%"));
            GradientBrush gradientBrush = new GradientBrush(Brush.BrushType.LINEAR_GRADIENT, linkedList, this.mGradientUnits);
            final LinkedList linkedList2 = new LinkedList();
            traverseChildren(new AbstractSVGVirtualComponent.NodeRunnable() {
                public void run(ISVGVirtualNode iSVGVirtualNode) {
                    if (iSVGVirtualNode instanceof SVGStopComponent) {
                        linkedList2.add(((SVGStopComponent) iSVGVirtualNode).getStopInfo());
                    }
                }
            });
            float[] fArr = new float[linkedList2.size()];
            int[] iArr = new int[linkedList2.size()];
            for (int i = 0; i < linkedList2.size(); i++) {
                SVGStopComponent.StopInfo stopInfo = (SVGStopComponent.StopInfo) linkedList2.get(i);
                fArr[i] = stopInfo.offset;
                iArr[i] = stopInfo.argbColor;
            }
            gradientBrush.setStopAndStopColors(fArr, iArr);
            if (this.mGradientMatrix != null) {
                gradientBrush.setGradientTransform(this.mGradientMatrix);
            }
            if (this.mGradientUnits == Brush.BrushUnits.USER_SPACE_ON_USE) {
                gradientBrush.setUserSpaceBoundingBox(sVGViewComponent.getCanvasBounds());
            }
            gradientBrush.setSpreadMethod(this.mSpreadMethod);
            sVGViewComponent.defineBrush(gradientBrush, findComponentId);
        }
    }

    private String toRelative(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        if (str.indexOf(37) > 0) {
            return str;
        }
        Float f = WXUtils.getFloat(str, Float.valueOf(0.0f));
        return (f.floatValue() * 100.0f) + Operators.MOD;
    }
}
