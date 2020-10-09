package com.alibaba.android.enhance.svg.component;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent;
import com.alibaba.android.enhance.svg.ISVGVirtualNode;
import com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class SVGClipPathComponent extends SVGGroupComponent {
    private boolean isDirty;
    private String mClipPathUnits = AbstractSVGVirtualComponent.UNIT_USER_SPACE_ON_USE;

    public void draw(Canvas canvas, Paint paint, float f) {
    }

    public AbstractSVGVirtualComponent hitTest(float[] fArr) {
        return null;
    }

    public void mergeProperties(RenderableSVGVirtualComponent renderableSVGVirtualComponent) {
    }

    public void resetProperties() {
    }

    public SVGClipPathComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public void saveDefinition() {
        SVGViewComponent sVGViewComponent = getSVGViewComponent();
        if (sVGViewComponent != null) {
            sVGViewComponent.defineClipPath(findComponentId(), this);
        }
    }

    @WXComponentProp(name = "clipPathUnits")
    public void setClipPathUnits(String str) {
        if (AbstractSVGVirtualComponent.UNIT_USER_SPACE_ON_USE.equals(str)) {
            this.mClipPathUnits = AbstractSVGVirtualComponent.UNIT_USER_SPACE_ON_USE;
        } else if (AbstractSVGVirtualComponent.UNIT_OBJECT_BOUNDING_BOX.equals(str)) {
            this.mClipPathUnits = AbstractSVGVirtualComponent.UNIT_OBJECT_BOUNDING_BOX;
        } else {
            this.mClipPathUnits = AbstractSVGVirtualComponent.UNIT_USER_SPACE_ON_USE;
        }
    }

    public Path getPath(final Canvas canvas, final Paint paint) {
        final Path path = new Path();
        traverseChildren(new AbstractSVGVirtualComponent.NodeRunnable() {
            public void run(ISVGVirtualNode iSVGVirtualNode) {
                Path path;
                if (!(iSVGVirtualNode instanceof SVGGroupComponent) && (path = iSVGVirtualNode.getPath(canvas, paint)) != null) {
                    path.addPath(path);
                }
            }
        });
        if (this.mMatrix != null) {
            path.transform(this.mMatrix);
        }
        return path;
    }

    public Path getPath(Canvas canvas, Paint paint, RectF rectF, float f) {
        if (AbstractSVGVirtualComponent.UNIT_USER_SPACE_ON_USE.equals(this.mClipPathUnits) || rectF == null || rectF.width() == 0.0f || rectF.height() == 0.0f) {
            return getPath(canvas, paint);
        }
        Path path = new Path();
        final Canvas canvas2 = canvas;
        final Paint paint2 = paint;
        final RectF rectF2 = rectF;
        final Path path2 = path;
        traverseChildren(new AbstractSVGVirtualComponent.NodeRunnable() {
            public void run(ISVGVirtualNode iSVGVirtualNode) {
                Path path;
                if (!(iSVGVirtualNode instanceof SVGGroupComponent) && (path = iSVGVirtualNode.getPath(canvas2, paint2, rectF2)) != null) {
                    path2.addPath(path);
                }
            }
        });
        if (this.mMatrix != null) {
            path.transform(this.mMatrix);
        }
        return path;
    }

    public void setDirty(boolean z) {
        this.isDirty = z;
    }

    public boolean isDirty() {
        return this.isDirty;
    }
}
