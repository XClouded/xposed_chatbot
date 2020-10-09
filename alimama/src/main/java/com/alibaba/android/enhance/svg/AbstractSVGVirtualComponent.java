package com.alibaba.android.enhance.svg;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.enhance.svg.SVGPlugin;
import com.alibaba.android.enhance.svg.component.SVGViewComponent;
import com.alibaba.android.enhance.svg.parser.SVGTransformParser;
import com.alibaba.android.enhance.svg.utils.PropHelper;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;

public abstract class AbstractSVGVirtualComponent extends WXVContainer implements ISVGVirtualNode {
    protected static final String FILL_RULE_EVENODD = "evenodd";
    protected static final String FILL_RULE_NONZERO = "nonzero";
    private static final double M_SQRT1_2l = 0.7071067811865476d;
    public static final String UNIT_OBJECT_BOUNDING_BOX = "objectBoundingBox";
    public static final String UNIT_USER_SPACE_ON_USE = "userSpaceOnUse";
    private float canvasHeight = -1.0f;
    private float canvasWidth = -1.0f;
    protected RectF mCachedBox;
    protected Matrix mInvMatrix = new Matrix();
    protected boolean mInvertible = true;
    protected Matrix mMatrix = new Matrix();
    protected float mOpacity = 1.0f;
    protected Path mPath;
    protected Region mRegion;
    protected final float mScale = PropHelper.calculateScale(getInstance());
    private SVGViewComponent mSvgViewNode;

    public interface NodeRunnable {
        void run(ISVGVirtualNode iSVGVirtualNode);
    }

    private double getFontSizeFromContext() {
        return 0.0d;
    }

    public abstract AbstractSVGVirtualComponent hitTest(float[] fArr);

    public boolean isVirtualComponent() {
        return true;
    }

    public void saveDefinition() {
    }

    public AbstractSVGVirtualComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        SVGPlugin.GlobalVariablesHolder.DEVICE_SCALE = this.mScale;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003d A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r4, java.lang.Object r5) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = -1267206133(0xffffffffb477f80b, float:-2.3093905E-7)
            r2 = 0
            if (r0 == r1) goto L_0x002a
            r1 = -1221029593(0xffffffffb7389127, float:-1.1001051E-5)
            if (r0 == r1) goto L_0x0020
            r1 = 113126854(0x6be2dc6, float:7.1537315E-35)
            if (r0 == r1) goto L_0x0015
            goto L_0x0034
        L_0x0015:
            java.lang.String r0 = "width"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0034
            r0 = 0
            goto L_0x0035
        L_0x0020:
            java.lang.String r0 = "height"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0034
            r0 = 1
            goto L_0x0035
        L_0x002a:
            java.lang.String r0 = "opacity"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0034
            r0 = 2
            goto L_0x0035
        L_0x0034:
            r0 = -1
        L_0x0035:
            switch(r0) {
                case 0: goto L_0x003d;
                case 1: goto L_0x003d;
                case 2: goto L_0x003d;
                default: goto L_0x0038;
            }
        L_0x0038:
            boolean r4 = super.setProperty(r4, r5)
            return r4
        L_0x003d:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "opacity")
    public void setOpacity(String str) {
        this.mOpacity = PropHelper.parseOpacityFromString(str);
        markUpdated();
    }

    @WXComponentProp(name = "transform")
    public void setTransform(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (this.mMatrix == null) {
                this.mMatrix = new Matrix();
            }
            if (this.mInvMatrix == null) {
                this.mInvMatrix = new Matrix();
            }
            this.mMatrix = new SVGTransformParser().parseTransform(str, this.mScale);
            this.mInvertible = this.mMatrix.invert(this.mInvMatrix);
        } else {
            this.mMatrix = null;
            this.mInvMatrix = null;
            this.mInvertible = false;
        }
        markUpdated();
    }

    public void setTranslate(float f, float f2) {
        if (this.mMatrix == null) {
            this.mMatrix = new Matrix();
        }
        if (this.mInvMatrix == null) {
            this.mInvMatrix = new Matrix();
        }
        this.mMatrix.setTranslate(f, f2);
        this.mInvertible = this.mMatrix.invert(this.mInvMatrix);
        markUpdated();
    }

    public void setScale(float f, float f2) {
        if (this.mMatrix == null) {
            this.mMatrix = new Matrix();
        }
        if (this.mInvMatrix == null) {
            this.mInvMatrix = new Matrix();
        }
        this.mMatrix.setScale(f, f2);
        this.mInvertible = this.mMatrix.invert(this.mInvMatrix);
        markUpdated();
    }

    public void setRotate(float f) {
        if (this.mMatrix == null) {
            this.mMatrix = new Matrix();
        }
        if (this.mInvMatrix == null) {
            this.mInvMatrix = new Matrix();
        }
        this.mMatrix.setRotate(f);
        this.mInvertible = this.mMatrix.invert(this.mInvMatrix);
        markUpdated();
    }

    public void setRotate(float f, float f2, float f3) {
        if (this.mMatrix == null) {
            this.mMatrix = new Matrix();
        }
        if (this.mInvMatrix == null) {
            this.mInvMatrix = new Matrix();
        }
        this.mMatrix.setRotate(f, f2, f3);
        this.mInvertible = this.mMatrix.invert(this.mInvMatrix);
        markUpdated();
    }

    public void setSkew(float f, float f2) {
        if (this.mMatrix == null) {
            this.mMatrix = new Matrix();
        }
        if (this.mInvMatrix == null) {
            this.mInvMatrix = new Matrix();
        }
        this.mMatrix.setSkew(f, f2);
        this.mInvertible = this.mMatrix.invert(this.mInvMatrix);
        markUpdated();
    }

    public void setSkew(float f, float f2, float f3, float f4) {
        if (this.mMatrix == null) {
            this.mMatrix = new Matrix();
        }
        if (this.mInvMatrix == null) {
            this.mInvMatrix = new Matrix();
        }
        this.mMatrix.setSkew(f, f2, f3, f4);
        this.mInvertible = this.mMatrix.invert(this.mInvMatrix);
        markUpdated();
    }

    public void setMatrix(@Nullable Matrix matrix) {
        this.mMatrix = matrix;
        if (this.mMatrix != null) {
            this.mInvertible = this.mMatrix.invert(this.mInvMatrix);
        } else {
            this.mInvMatrix = null;
        }
        markUpdated();
    }

    public void setMatrixValues(@NonNull float[] fArr) {
        if (this.mMatrix == null) {
            this.mMatrix = new Matrix();
        }
        if (this.mInvMatrix == null) {
            this.mInvMatrix = new Matrix();
        }
        this.mMatrix.setValues(fArr);
        this.mInvertible = this.mMatrix.invert(this.mInvMatrix);
        markUpdated();
    }

    @NonNull
    public Matrix getMatrix() {
        if (this.mMatrix == null) {
            this.mMatrix = new Matrix();
        }
        return this.mMatrix;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public SVGViewComponent getSVGViewComponent() {
        if (this.mSvgViewNode != null) {
            return this.mSvgViewNode;
        }
        WXVContainer parent = getParent();
        if (parent == null) {
            return null;
        }
        if (parent instanceof SVGViewComponent) {
            this.mSvgViewNode = (SVGViewComponent) parent;
        } else if (parent instanceof AbstractSVGVirtualComponent) {
            this.mSvgViewNode = ((AbstractSVGVirtualComponent) parent).getSVGViewComponent();
        } else {
            WXLogUtils.e(SVGPlugin.TAG, getClass().getName() + " should be descendant of a SVGViewComponent.");
        }
        return this.mSvgViewNode;
    }

    public double relativeOnWidth(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return 0.0d;
        }
        return PropHelper.fromRelative(str, (double) getCanvasWidth(), 0.0d, (double) this.mScale, getFontSizeFromContext());
    }

    public double relativeOnHeight(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return 0.0d;
        }
        return PropHelper.fromRelative(str, (double) getCanvasHeight(), 0.0d, (double) this.mScale, getFontSizeFromContext());
    }

    public double relativeOnOther(String str) {
        return PropHelper.fromRelative(str, Math.sqrt(Math.pow((double) getCanvasWidth(), 2.0d) + Math.pow((double) getCanvasHeight(), 2.0d)) * M_SQRT1_2l, 0.0d, (double) this.mScale, getFontSizeFromContext());
    }

    private float getCanvasWidth() {
        if (this.canvasWidth != -1.0f) {
            return this.canvasWidth;
        }
        if (getSVGViewComponent() == null) {
            WXLogUtils.e(SVGPlugin.TAG, "err! can not get the width of canvas");
            return 0.0f;
        }
        this.canvasWidth = (float) getSVGViewComponent().getCanvasBounds().width();
        return this.canvasWidth;
    }

    private float getCanvasHeight() {
        if (this.canvasHeight != -1.0f) {
            return this.canvasHeight;
        }
        if (getSVGViewComponent() == null) {
            WXLogUtils.e(SVGPlugin.TAG, "err! can not get the height of canvas");
            return 0.0f;
        }
        this.canvasHeight = (float) getSVGViewComponent().getCanvasBounds().height();
        return this.canvasHeight;
    }

    public int saveAndSetupCanvas(Canvas canvas) {
        int save = canvas.save();
        canvas.concat(this.mMatrix);
        return save;
    }

    public void restoreCanvas(Canvas canvas, int i) {
        canvas.restoreToCount(i);
    }

    /* access modifiers changed from: protected */
    public void traverseChildren(NodeRunnable nodeRunnable) {
        for (int i = 0; i < getChildCount(); i++) {
            WXComponent child = getChild(i);
            if (child instanceof ISVGVirtualNode) {
                nodeRunnable.run((ISVGVirtualNode) child);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void markUpdated() {
        if (getSVGViewComponent() != null) {
            getSVGViewComponent().markUpdated();
        }
        this.mCachedBox = null;
        this.mPath = null;
        this.mRegion = null;
    }

    public Path getPath(Canvas canvas, Paint paint, RectF rectF) {
        return getPath(canvas, paint);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String findComponentId() {
        return WXUtils.getString(getAttrs().get("id"), (String) null);
    }
}
