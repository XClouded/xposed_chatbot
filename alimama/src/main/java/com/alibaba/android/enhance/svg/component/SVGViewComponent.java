package com.alibaba.android.enhance.svg.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent;
import com.alibaba.android.enhance.svg.Brush;
import com.alibaba.android.enhance.svg.ISVGVirtualNode;
import com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent;
import com.alibaba.android.enhance.svg.component.mask.MaskNode;
import com.alibaba.android.enhance.svg.event.GestureEventDispatcher;
import com.alibaba.android.enhance.svg.utils.PropHelper;
import com.alibaba.android.enhance.svg.utils.ViewBox;
import com.alibaba.android.enhance.svg.view.WXSVGView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.transition.WXTransition;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.Scrollable;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.gesture.WXGestureType;
import com.taobao.weex.utils.WXUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SVGViewComponent extends WXVContainer<WXSVGView> {
    private String mAlign = "xMidYMid";
    private Canvas mCanvas;
    private final Map<String, Brush> mDefinedBrusheMap = new HashMap();
    private final Map<String, SVGClipPathComponent> mDefinedClipPathMap = new HashMap();
    private final Map<String, MaskNode> mDefinedMaskNodeMap = new HashMap();
    private final Map<String, RenderableSVGVirtualComponent> mDefinedTemplateMap = new HashMap();
    private GestureEventDispatcher mGestureDispatcher;
    private final Set<String> mGestureEvents = new HashSet();
    private boolean mHasViewBox = false;
    private Matrix mInvViewBoxMatrix = new Matrix();
    private boolean mInvertible = true;
    private int mMeetOrSlice = 0;
    private float mMinX;
    private float mMinY;
    /* access modifiers changed from: private */
    public Paint mPaint;
    private Set<String> mSVGGestureType;
    private final float mScale = PropHelper.calculateScale(getInstance());
    private float mVbHeight;
    private float mVbWidth;

    public SVGViewComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public WXSVGView initComponentHostView(@NonNull Context context) {
        WXSVGView wXSVGView = new WXSVGView(context);
        wXSVGView.setShadowComponent(this);
        return wXSVGView;
    }

    @WXComponentProp(name = "hardwareAcceleration")
    public void setHardwareAcceleration(boolean z) {
        setLayerType(z ? 2 : 1, (Paint) null);
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
        setHardwareAcceleration(false);
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

    @WXComponentProp(name = "width")
    public void setWidth(float f) {
        WXTransition.asynchronouslyUpdateLayout(this, "width", this.mScale * f);
    }

    @WXComponentProp(name = "height")
    public void setHeight(float f) {
        WXTransition.asynchronouslyUpdateLayout(this, "height", this.mScale * f);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x002a  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x003f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r4, java.lang.Object r5) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = -1221029593(0xffffffffb7389127, float:-1.1001051E-5)
            r2 = 1
            if (r0 == r1) goto L_0x001b
            r1 = 113126854(0x6be2dc6, float:7.1537315E-35)
            if (r0 == r1) goto L_0x0010
            goto L_0x0025
        L_0x0010:
            java.lang.String r0 = "width"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0025
            r0 = 0
            goto L_0x0026
        L_0x001b:
            java.lang.String r0 = "height"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x0025
            r0 = 1
            goto L_0x0026
        L_0x0025:
            r0 = -1
        L_0x0026:
            r1 = 0
            switch(r0) {
                case 0: goto L_0x003f;
                case 1: goto L_0x002f;
                default: goto L_0x002a;
            }
        L_0x002a:
            boolean r4 = super.setProperty(r4, r5)
            return r4
        L_0x002f:
            java.lang.Float r4 = java.lang.Float.valueOf(r1)
            java.lang.Float r4 = com.taobao.weex.utils.WXUtils.getFloat(r5, r4)
            float r4 = r4.floatValue()
            r3.setHeight(r4)
            return r2
        L_0x003f:
            java.lang.Float r4 = java.lang.Float.valueOf(r1)
            java.lang.Float r4 = com.taobao.weex.utils.WXUtils.getFloat(r5, r4)
            float r4 = r4.floatValue()
            r3.setWidth(r4)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.svg.component.SVGViewComponent.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @NonNull
    public List<AbstractSVGVirtualComponent> hitTest(float f, float f2) {
        AbstractSVGVirtualComponent hitTest;
        if (!this.mInvertible || this.mInvViewBoxMatrix == null) {
            return Collections.emptyList();
        }
        float[] fArr = new float[2];
        this.mInvViewBoxMatrix.mapPoints(fArr, new float[]{f, f2});
        LinkedList linkedList = new LinkedList();
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            WXComponent child = getChild(childCount);
            if ((child instanceof AbstractSVGVirtualComponent) && (hitTest = ((AbstractSVGVirtualComponent) child).hitTest(fArr)) != null) {
                linkedList.add(hitTest);
            }
        }
        return linkedList;
    }

    public void addEvent(String str) {
        if (!TextUtils.isEmpty(str) && !this.mGestureEvents.contains(str)) {
            if (GestureEventDispatcher.isGestureEvent(str)) {
                boolean booleanValue = WXUtils.getBoolean(getAttrs().get("preventMoveEvent"), false).booleanValue();
                this.mGestureDispatcher = new GestureEventDispatcher(this, getContext());
                this.mGestureDispatcher.setPreventMoveEvent(booleanValue);
                if (this.mSVGGestureType == null) {
                    this.mSVGGestureType = new HashSet();
                }
                this.mSVGGestureType.add(str);
            } else if (Constants.Event.FOCUS.equals(str) || Constants.Event.BLUR.equals(str)) {
                super.addEvent(str);
            } else {
                Scrollable parentScroller = getParentScroller();
                if (parentScroller != null) {
                    if (str.equals(Constants.Event.APPEAR)) {
                        parentScroller.bindAppearEvent(this);
                    } else if (str.equals(Constants.Event.DISAPPEAR)) {
                        parentScroller.bindDisappearEvent(this);
                    }
                } else {
                    return;
                }
            }
            this.mGestureEvents.add(str);
        }
    }

    public boolean containsEvent(String str) {
        return super.containsEvent(str) || this.mGestureEvents.contains(str);
    }

    public boolean containsGesture(WXGestureType wXGestureType) {
        return this.mSVGGestureType != null && this.mSVGGestureType.contains(wXGestureType.toString());
    }

    public GestureEventDispatcher getGestureDispatcher() {
        return this.mGestureDispatcher;
    }

    private Bitmap drawToBitmap() {
        Bitmap createBitmap = Bitmap.createBitmap((int) getLayoutWidth(), (int) getLayoutHeight(), Bitmap.Config.ARGB_8888);
        drawChildren(new Canvas(createBitmap));
        return createBitmap;
    }

    public void drawChildren(final Canvas canvas) {
        this.mCanvas = canvas;
        if (this.mHasViewBox) {
            Matrix transform = ViewBox.getTransform(getViewBox(), new RectF(0.0f, 0.0f, getLayoutWidth(), getLayoutHeight()), this.mAlign, this.mMeetOrSlice);
            this.mInvertible = transform.invert(this.mInvViewBoxMatrix);
            canvas.concat(transform);
        }
        if (this.mPaint == null) {
            this.mPaint = new Paint();
        } else {
            this.mPaint.reset();
        }
        this.mPaint.setFlags(385);
        this.mPaint.setTypeface(Typeface.DEFAULT);
        traverseChildren(new AbstractSVGVirtualComponent.NodeRunnable() {
            public void run(ISVGVirtualNode iSVGVirtualNode) {
                if (iSVGVirtualNode instanceof AbstractSVGVirtualComponent) {
                    ((AbstractSVGVirtualComponent) iSVGVirtualNode).saveDefinition();
                }
            }
        });
        traverseChildren(new AbstractSVGVirtualComponent.NodeRunnable() {
            public void run(ISVGVirtualNode iSVGVirtualNode) {
                if (iSVGVirtualNode instanceof AbstractSVGVirtualComponent) {
                    AbstractSVGVirtualComponent abstractSVGVirtualComponent = (AbstractSVGVirtualComponent) iSVGVirtualNode;
                    int saveAndSetupCanvas = abstractSVGVirtualComponent.saveAndSetupCanvas(canvas);
                    abstractSVGVirtualComponent.draw(canvas, SVGViewComponent.this.mPaint, 1.0f);
                    abstractSVGVirtualComponent.restoreCanvas(canvas, saveAndSetupCanvas);
                }
            }
        });
    }

    private RectF getViewBox() {
        return new RectF(this.mMinX * this.mScale, this.mMinY * this.mScale, (this.mMinX + this.mVbWidth) * this.mScale, (this.mMinY + this.mVbHeight) * this.mScale);
    }

    public void markUpdated() {
        ((WXSVGView) getHostView()).setWillNotDraw(false);
        ((WXSVGView) getHostView()).postInvalidate();
    }

    public void setLayerType(int i, @Nullable Paint paint) {
        if (getHostView() != null) {
            ((WXSVGView) getHostView()).setLayerType(i, paint);
        }
    }

    @NonNull
    public Rect getCanvasBounds() {
        return this.mCanvas.getClipBounds();
    }

    /* access modifiers changed from: package-private */
    public void traverseChildren(AbstractSVGVirtualComponent.NodeRunnable nodeRunnable) {
        for (int i = 0; i < getChildCount(); i++) {
            WXComponent child = getChild(i);
            if (child instanceof ISVGVirtualNode) {
                nodeRunnable.run((ISVGVirtualNode) child);
            }
        }
    }

    public void defineClipPath(String str, SVGClipPathComponent sVGClipPathComponent) {
        if (!TextUtils.isEmpty(str) && sVGClipPathComponent != null) {
            this.mDefinedClipPathMap.put(str, sVGClipPathComponent);
        }
    }

    @Nullable
    public SVGClipPathComponent getDefinedClipPath(String str) {
        return this.mDefinedClipPathMap.get(str);
    }

    public void defineBrush(Brush brush, String str) {
        if (!TextUtils.isEmpty(str) && brush != null) {
            this.mDefinedBrusheMap.put(str, brush);
        }
    }

    @Nullable
    public Brush getDefinedBrush(String str) {
        return this.mDefinedBrusheMap.get(str);
    }

    public void defineGraphicalTemplate(String str, RenderableSVGVirtualComponent renderableSVGVirtualComponent) {
        if (!TextUtils.isEmpty(str) && renderableSVGVirtualComponent != null) {
            this.mDefinedTemplateMap.put(str, renderableSVGVirtualComponent);
        }
    }

    @Nullable
    public RenderableSVGVirtualComponent getDefinedGraphicalTemplate(String str) {
        return this.mDefinedTemplateMap.get(str);
    }

    public void defineMask(String str, MaskNode maskNode) {
        if (!TextUtils.isEmpty(str) && maskNode != null) {
            this.mDefinedMaskNodeMap.put(str, maskNode);
        }
    }

    @Nullable
    public MaskNode getDefinedMaskNode(String str) {
        return this.mDefinedMaskNodeMap.get(str);
    }
}
