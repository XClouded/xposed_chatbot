package com.alibaba.android.enhance.svg;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.text.TextUtils;
import android.view.ViewGroup;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.enhance.svg.component.SVGClipPathComponent;
import com.alibaba.android.enhance.svg.component.SVGViewComponent;
import com.alibaba.android.enhance.svg.component.mask.MaskNode;
import com.alibaba.android.enhance.svg.event.GestureEventDispatcher;
import com.alibaba.android.enhance.svg.utils.PropHelper;
import com.alibaba.android.enhance.svg.view.WXSVGView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.Scrollable;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.gesture.WXGestureType;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public abstract class RenderableSVGVirtualComponent extends AbstractSVGVirtualComponent {
    private static final String CAP_BUTT = "butt";
    private static final String CAP_ROUND = "round";
    private static final String CAP_SQUARE = "square";
    private static final String JOIN_BEVEL = "bevel";
    private static final String JOIN_MITER = "miter";
    private static final String JOIN_ROUND = "round";
    protected static final float MIN_OPACITY_FOR_DRAW = 0.01f;
    private boolean hasSetFillOpacity = false;
    private boolean hasSetStrokeLinecap = false;
    private boolean hasSetStrokeLinejoin = false;
    private boolean hasSetStrokeOpacity = false;
    private Path mCachedClipPath;
    private String mClipPathName;
    protected Region mClipRegion;
    protected Path mClipRegionPath;
    private String mClipRule = "nonzero";
    @Nullable
    private String mFill = "#FF000000";
    private float mFillOpacity = 1.0f;
    private Path.FillType mFillRule = Path.FillType.WINDING;
    private GestureEventDispatcher mGestureDispatcher;
    private Set<String> mGestureEvents;
    @Nullable
    private RenderableSVGVirtualComponent mGroupComponentRef;
    private String mMaskName;
    private final PathMeasure mPathMeasure = new PathMeasure();
    private Set<String> mSVGGestureType;
    @Nullable
    private String mStroke = "none";
    @Nullable
    private LinkedList<String> mStrokeDasharray;
    private float mStrokeDashoffset = 0.0f;
    private Paint.Cap mStrokeLinecap = Paint.Cap.BUTT;
    private Paint.Join mStrokeLinejoin = Paint.Join.MITER;
    private float mStrokeMiterlimit = 4.0f;
    private float mStrokeOpacity = 1.0f;
    private String mStrokeWidth = "1";

    public RenderableSVGVirtualComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    @WXComponentProp(name = "stroke")
    public void setStroke(@Nullable String str) {
        this.mStroke = str;
        markUpdated();
    }

    public String getStroke() {
        if (this.mGroupComponentRef == null) {
            return this.mStroke;
        }
        if ("none".equals(this.mStroke) || TextUtils.isEmpty(this.mStroke)) {
            return this.mGroupComponentRef.getStroke();
        }
        return this.mStroke;
    }

    @WXComponentProp(name = "strokeWidth")
    public void setStrokeWidth(String str) {
        this.mStrokeWidth = str;
        markUpdated();
    }

    public String getStrokeWidth() {
        if (this.mGroupComponentRef == null) {
            return this.mStrokeWidth;
        }
        if ("1".equals(this.mStrokeWidth) || TextUtils.isEmpty(this.mStrokeWidth)) {
            return this.mGroupComponentRef.getStrokeWidth();
        }
        return this.mStrokeWidth;
    }

    @WXComponentProp(name = "strokeOpacity")
    public void setStrokeOpacity(String str) {
        this.mStrokeOpacity = PropHelper.parseOpacityFromString(str);
        this.mStrokeOpacity = Math.max(Math.min(1.0f, this.mStrokeOpacity), 0.0f);
        this.hasSetStrokeOpacity = true;
        markUpdated();
    }

    public float getStrokeOpacity() {
        if (this.mGroupComponentRef == null) {
            return this.mStrokeOpacity;
        }
        if (!this.hasSetStrokeOpacity) {
            return this.mGroupComponentRef.getStrokeOpacity();
        }
        return this.mStrokeOpacity;
    }

    @WXComponentProp(name = "strokeDasharray")
    public void setStrokeDasharray(String str) {
        if (TextUtils.isEmpty(str)) {
            this.mStrokeDasharray = null;
            return;
        }
        String[] split = str.split(",");
        if (split.length <= 0) {
            this.mStrokeDasharray = null;
            return;
        }
        this.mStrokeDasharray = new LinkedList<>();
        for (String trim : split) {
            this.mStrokeDasharray.add(trim.trim());
        }
        markUpdated();
    }

    public LinkedList<String> getStrokeDasharray() {
        if (this.mGroupComponentRef == null) {
            return this.mStrokeDasharray;
        }
        if (this.mStrokeDasharray == null) {
            return this.mGroupComponentRef.getStrokeDasharray();
        }
        return this.mStrokeDasharray;
    }

    @WXComponentProp(name = "strokeDashoffset")
    public void setStrokeDashoffset(float f) {
        this.mStrokeDashoffset = f * this.mScale;
        markUpdated();
    }

    public float getStrokeDashoffset() {
        if (this.mGroupComponentRef == null) {
            return this.mStrokeDashoffset;
        }
        if (this.mStrokeDashoffset == 0.0f) {
            return this.mGroupComponentRef.getStrokeDashoffset();
        }
        return this.mStrokeDashoffset;
    }

    @WXComponentProp(name = "strokeLinecap")
    public void setStrokeLinecap(String str) {
        if (!TextUtils.isEmpty(str)) {
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != -894674659) {
                if (hashCode != 3035667) {
                    if (hashCode == 108704142 && str.equals("round")) {
                        c = 1;
                    }
                } else if (str.equals(CAP_BUTT)) {
                    c = 0;
                }
            } else if (str.equals(CAP_SQUARE)) {
                c = 2;
            }
            switch (c) {
                case 0:
                    this.mStrokeLinecap = Paint.Cap.BUTT;
                    break;
                case 1:
                    this.mStrokeLinecap = Paint.Cap.ROUND;
                    break;
                case 2:
                    this.mStrokeLinecap = Paint.Cap.SQUARE;
                    break;
                default:
                    this.mStrokeLinecap = Paint.Cap.BUTT;
                    break;
            }
            this.hasSetStrokeLinecap = true;
            markUpdated();
        }
    }

    public Paint.Cap getStrokeLinecap() {
        if (this.mGroupComponentRef == null) {
            return this.mStrokeLinecap;
        }
        if (!this.hasSetStrokeLinecap) {
            return this.mGroupComponentRef.getStrokeLinecap();
        }
        return this.mStrokeLinecap;
    }

    @WXComponentProp(name = "strokeLinejoin")
    public void setStrokeLinejoin(String str) {
        if (!TextUtils.isEmpty(str)) {
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != 93630586) {
                if (hashCode != 103906565) {
                    if (hashCode == 108704142 && str.equals("round")) {
                        c = 2;
                    }
                } else if (str.equals(JOIN_MITER)) {
                    c = 1;
                }
            } else if (str.equals(JOIN_BEVEL)) {
                c = 0;
            }
            switch (c) {
                case 0:
                    this.mStrokeLinejoin = Paint.Join.BEVEL;
                    break;
                case 1:
                    this.mStrokeLinejoin = Paint.Join.MITER;
                    break;
                case 2:
                    this.mStrokeLinejoin = Paint.Join.ROUND;
                    break;
                default:
                    this.mStrokeLinejoin = Paint.Join.MITER;
                    break;
            }
            this.hasSetStrokeLinejoin = true;
            markUpdated();
        }
    }

    public Paint.Join getStrokeLinejoin() {
        if (this.mGroupComponentRef == null) {
            return this.mStrokeLinejoin;
        }
        if (!this.hasSetStrokeLinejoin) {
            return this.mGroupComponentRef.getStrokeLinejoin();
        }
        return this.mStrokeLinejoin;
    }

    @WXComponentProp(name = "strokeMiterlimit")
    public void setStrokeMiterlimit(float f) {
        this.mStrokeMiterlimit = f;
        markUpdated();
    }

    public float getStrokeMiterlimit() {
        if (this.mGroupComponentRef == null) {
            return this.mStrokeMiterlimit;
        }
        if (this.mStrokeMiterlimit == 4.0f) {
            return this.mGroupComponentRef.getStrokeMiterlimit();
        }
        return this.mStrokeMiterlimit;
    }

    @WXComponentProp(name = "fill")
    public void setFill(@Nullable String str) {
        this.mFill = str;
        markUpdated();
    }

    public String getFill() {
        if (this.mGroupComponentRef == null) {
            return this.mFill;
        }
        if ("#FF000000".equals(this.mFill)) {
            return this.mGroupComponentRef.getFill();
        }
        return this.mFill;
    }

    @WXComponentProp(name = "fillRule")
    public void setFillRule(String str) {
        if (!TextUtils.isEmpty(str)) {
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != -1376506411) {
                if (hashCode == 2124315381 && str.equals("nonzero")) {
                    c = 1;
                }
            } else if (str.equals("evenodd")) {
                c = 0;
            }
            switch (c) {
                case 0:
                    this.mFillRule = Path.FillType.EVEN_ODD;
                    break;
                case 1:
                    this.mFillRule = Path.FillType.WINDING;
                    break;
                default:
                    this.mFillRule = Path.FillType.WINDING;
                    break;
            }
            markUpdated();
        }
    }

    public Path.FillType getFillRule() {
        if (this.mGroupComponentRef == null) {
            return this.mFillRule;
        }
        if (Path.FillType.WINDING == this.mFillRule) {
            return this.mGroupComponentRef.getFillRule();
        }
        return this.mFillRule;
    }

    @WXComponentProp(name = "fillOpacity")
    public void setFillOpacity(String str) {
        this.mFillOpacity = PropHelper.parseOpacityFromString(str);
        this.mFillOpacity = Math.max(Math.min(1.0f, this.mFillOpacity), 0.0f);
        this.hasSetFillOpacity = true;
        markUpdated();
    }

    public float getFillOpacity() {
        if (this.mGroupComponentRef == null) {
            return this.mFillOpacity;
        }
        if (!this.hasSetFillOpacity) {
            return this.mGroupComponentRef.getFillOpacity();
        }
        return this.mFillOpacity;
    }

    public float getOpacity() {
        return this.mOpacity;
    }

    @WXComponentProp(name = "mask")
    public void setMask(String str) {
        this.mMaskName = PropHelper.resolveIDFromString(str);
        markUpdated();
    }

    @WXComponentProp(name = "clipPath")
    public void setClipPath(String str) {
        this.mCachedClipPath = null;
        this.mClipPathName = PropHelper.resolveIDFromString(str);
        markUpdated();
    }

    @WXComponentProp(name = "clipRule")
    public void setClipRule(String str) {
        this.mClipRule = str;
        markUpdated();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Path getClipPath() {
        return this.mCachedClipPath;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public Path getClipPath(Canvas canvas, Paint paint) {
        if (!TextUtils.isEmpty(this.mClipPathName)) {
            SVGViewComponent sVGViewComponent = getSVGViewComponent();
            if (sVGViewComponent == null) {
                return null;
            }
            SVGClipPathComponent definedClipPath = sVGViewComponent.getDefinedClipPath(this.mClipPathName);
            if (definedClipPath == null || !definedClipPath.isDirty()) {
                if (definedClipPath != null) {
                    definedClipPath.setDirty(false);
                }
                return getClipPath();
            }
            if (this.mCachedBox == null) {
                this.mCachedBox = new RectF();
                if (this.mPath == null) {
                    this.mPath = getPath(canvas, paint);
                }
                if (this.mPath != null) {
                    this.mPath.computeBounds(this.mCachedBox, true);
                }
            }
            Path path = definedClipPath.getPath(canvas, paint, this.mCachedBox, this.mScale);
            if (path == null) {
                return null;
            }
            if (!TextUtils.isEmpty(this.mClipRule)) {
                String str = this.mClipRule;
                char c = 65535;
                int hashCode = str.hashCode();
                if (hashCode != -1376506411) {
                    if (hashCode == 2124315381 && str.equals("nonzero")) {
                        c = 1;
                    }
                } else if (str.equals("evenodd")) {
                    c = 0;
                }
                switch (c) {
                    case 0:
                        path.setFillType(Path.FillType.EVEN_ODD);
                        break;
                    case 1:
                        path.setFillType(Path.FillType.WINDING);
                        break;
                }
            }
            this.mCachedClipPath = path;
        }
        return getClipPath();
    }

    /* access modifiers changed from: protected */
    public void clip(Canvas canvas, Paint paint) {
        Path clipPath = getClipPath(canvas, paint);
        if (clipPath != null) {
            canvas.clipPath(clipPath);
        }
    }

    public int getTotalLength() {
        if (this.mPath == null) {
            this.mPath = getPath((Canvas) null, (Paint) null);
        }
        if (this.mPath == null) {
            return 0;
        }
        this.mPathMeasure.setPath(this.mPath, false);
        return (int) WXViewUtils.getWebPxByWidth(this.mPathMeasure.getLength(), PropHelper.getViewPort(getInstance()));
    }

    public Map<String, Float> getPointAtLength(float f) {
        if (this.mPath == null) {
            this.mPath = getPath((Canvas) null, (Paint) null);
        }
        float[] fArr = new float[2];
        float[] fArr2 = new float[2];
        HashMap hashMap = new HashMap(2);
        if (this.mPath != null) {
            this.mPathMeasure.setPath(this.mPath, false);
            this.mPathMeasure.getPosTan(f * this.mScale, fArr, fArr2);
            hashMap.put(Constants.Name.X, Float.valueOf(WXViewUtils.getWebPxByWidth(fArr[0], PropHelper.getViewPort(getInstance()))));
            hashMap.put(Constants.Name.Y, Float.valueOf(WXViewUtils.getWebPxByWidth(fArr[1], PropHelper.getViewPort(getInstance()))));
            hashMap.put("alpha", Float.valueOf((float) Math.toDegrees(Math.atan2((double) fArr2[1], (double) fArr2[0]))));
        }
        return hashMap;
    }

    public double getDegreeAtLength(float f) {
        if (this.mPath == null) {
            this.mPath = getPath((Canvas) null, (Paint) null);
        }
        float[] fArr = new float[2];
        float[] fArr2 = new float[2];
        if (this.mPath == null) {
            return 0.0d;
        }
        this.mPathMeasure.setPath(this.mPath, false);
        this.mPathMeasure.getPosTan(f * this.mScale, fArr, fArr2);
        return Math.toDegrees(Math.atan2((double) fArr2[1], (double) fArr2[0]));
    }

    public void getPosAndTanAtLength(float f, float[] fArr, float[] fArr2) {
        if (this.mPath == null) {
            this.mPath = getPath((Canvas) null, (Paint) null);
        }
        if (this.mPath != null) {
            this.mPathMeasure.setPath(this.mPath, false);
            this.mPathMeasure.getPosTan(f * this.mScale, fArr, fArr2);
        }
    }

    @JSMethod(uiThread = false)
    public void getTotalLengthAsync(JSCallback jSCallback) {
        if (jSCallback != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("length", Integer.valueOf(getTotalLength()));
            jSCallback.invoke(hashMap);
        }
    }

    @JSMethod(uiThread = false)
    public void getPointAtLengthAsync(float f, JSCallback jSCallback) {
        if (jSCallback != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("position", getPointAtLength(f));
            jSCallback.invoke(hashMap);
        }
    }

    public AbstractSVGVirtualComponent hitTest(float[] fArr) {
        if (fArr == null || fArr.length != 2 || this.mPath == null || !this.mInvertible || this.mInvMatrix == null) {
            return null;
        }
        float[] fArr2 = new float[2];
        this.mInvMatrix.mapPoints(fArr2, fArr);
        int round = Math.round(fArr2[0]);
        int round2 = Math.round(fArr2[1]);
        if (this.mRegion == null) {
            this.mRegion = getRegion(this.mPath);
        }
        if (!this.mRegion.contains(round, round2)) {
            return null;
        }
        Path clipPath = getClipPath();
        if (clipPath != null) {
            if (this.mClipRegionPath != clipPath) {
                this.mClipRegionPath = clipPath;
                this.mClipRegion = getRegion(clipPath);
            }
            if (!this.mClipRegion.contains(round, round2)) {
                return null;
            }
        }
        return this;
    }

    public void addEvent(String str) {
        if (this.mGestureEvents == null) {
            this.mGestureEvents = new HashSet();
        }
        if (!TextUtils.isEmpty(str) && !this.mGestureEvents.contains(str)) {
            if (GestureEventDispatcher.isGestureEvent(str)) {
                boolean booleanValue = WXUtils.getBoolean(getAttrs().get("preventMoveEvent"), false).booleanValue();
                this.mGestureDispatcher = new GestureEventDispatcher(this, getContext());
                this.mGestureDispatcher.setPreventMoveEvent(booleanValue);
                if (this.mSVGGestureType == null) {
                    this.mSVGGestureType = new HashSet();
                }
                this.mSVGGestureType.add(str);
            } else if (!Constants.Event.FOCUS.equals(str) && !Constants.Event.BLUR.equals(str)) {
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

    public ViewGroup getRealView() {
        if (getSVGViewComponent() != null) {
            return (WXSVGView) getSVGViewComponent().getHostView();
        }
        return null;
    }

    public boolean containsGesture(WXGestureType wXGestureType) {
        return this.mSVGGestureType != null && this.mSVGGestureType.contains(wXGestureType.toString());
    }

    public boolean containsEvent(String str) {
        return super.containsEvent(str) || (this.mGestureEvents != null && this.mGestureEvents.contains(str));
    }

    public GestureEventDispatcher getGestureDispatcher() {
        return this.mGestureDispatcher;
    }

    public void draw(Canvas canvas, Paint paint, @FloatRange(from = 0.0d, to = 1.0d) float f) {
        draw(canvas, paint, f, (RectF) null);
    }

    public void draw(Canvas canvas, Paint paint, @FloatRange(from = 0.0d, to = 1.0d) float f, @Nullable RectF rectF) {
        float opacity = f * getOpacity();
        if (opacity > MIN_OPACITY_FOR_DRAW) {
            if (this.mPath == null) {
                if (rectF == null || rectF.width() == 0.0f || rectF.height() == 0.0f) {
                    this.mPath = getPath(canvas, paint);
                } else {
                    this.mPath = getPath(canvas, paint, rectF);
                }
                if (this.mPath == null) {
                    WXLogUtils.e(SVGPlugin.TAG, "draw error! can not get path");
                    return;
                }
                this.mPath.setFillType(getFillRule());
            }
            preProcessIfHasMask(canvas);
            clip(canvas, paint);
            if (setupFillPaint(paint, getFillOpacity() * opacity)) {
                canvas.drawPath(this.mPath, paint);
            }
            if (setupStrokePaint(paint, opacity * getStrokeOpacity())) {
                canvas.drawPath(this.mPath, paint);
            }
            applyMask(canvas, paint);
        }
    }

    /* access modifiers changed from: protected */
    public void preProcessIfHasMask(Canvas canvas) {
        SVGViewComponent sVGViewComponent;
        MaskNode definedMaskNode;
        if (!TextUtils.isEmpty(this.mMaskName) && (sVGViewComponent = getSVGViewComponent()) != null && (definedMaskNode = sVGViewComponent.getDefinedMaskNode(this.mMaskName)) != null) {
            if (this.mCachedBox == null) {
                this.mCachedBox = new RectF();
                if (this.mPath != null) {
                    this.mPath.computeBounds(this.mCachedBox, true);
                } else {
                    Path path = getPath(canvas, (Paint) null);
                    if (path != null) {
                        path.computeBounds(this.mCachedBox, true);
                    }
                }
            }
            definedMaskNode.clipBoundingBox(canvas, this.mCachedBox, this.mScale);
            canvas.saveLayerAlpha((RectF) null, ((int) getOpacity()) * 255, 31);
        }
    }

    /* access modifiers changed from: protected */
    public void applyMask(Canvas canvas, Paint paint) {
        SVGViewComponent sVGViewComponent;
        MaskNode definedMaskNode;
        if (!TextUtils.isEmpty(this.mMaskName) && (sVGViewComponent = getSVGViewComponent()) != null && (definedMaskNode = sVGViewComponent.getDefinedMaskNode(this.mMaskName)) != null) {
            if (this.mCachedBox == null) {
                this.mCachedBox = new RectF();
                if (this.mPath != null) {
                    this.mPath.computeBounds(this.mCachedBox, true);
                }
            }
            definedMaskNode.renderMask(canvas, paint, this.mCachedBox, this.mScale);
            canvas.restore();
        }
    }

    private boolean setupFillPaint(Paint paint, float f) {
        String fill = getFill();
        if (fill == null || TextUtils.isEmpty(fill) || !PropHelper.isColorValid(fill)) {
            return false;
        }
        paint.reset();
        paint.setFlags(385);
        paint.setStyle(Paint.Style.FILL);
        setupPaint(paint, f, fill);
        return true;
    }

    private boolean setupStrokePaint(Paint paint, float f) {
        float[] fArr;
        paint.reset();
        double relativeOnOther = relativeOnOther(getStrokeWidth());
        String stroke = getStroke();
        int i = 0;
        if (relativeOnOther == 0.0d || stroke == null || TextUtils.isEmpty(stroke) || !PropHelper.isColorValid(stroke)) {
            return false;
        }
        paint.setFlags(385);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(getStrokeLinecap());
        paint.setStrokeJoin(getStrokeLinejoin());
        paint.setStrokeMiter(getStrokeMiterlimit() * this.mScale);
        paint.setStrokeWidth((float) relativeOnOther);
        setupPaint(paint, f, stroke);
        LinkedList<String> strokeDasharray = getStrokeDasharray();
        if (strokeDasharray == null || strokeDasharray.isEmpty()) {
            return true;
        }
        int size = strokeDasharray.size();
        if (size % 2 != 0) {
            fArr = new float[(size * 2)];
            for (int i2 = 0; i2 < size; i2++) {
                fArr[i2] = (float) relativeOnOther(strokeDasharray.get(i2));
            }
            while (i < size) {
                fArr[size + i] = (float) relativeOnOther(strokeDasharray.get(i));
                i++;
            }
        } else {
            fArr = new float[size];
            while (i < size) {
                fArr[i] = (float) relativeOnOther(strokeDasharray.get(i));
                i++;
            }
        }
        paint.setPathEffect(new DashPathEffect(fArr, getStrokeDashoffset()));
        return true;
    }

    private void setupPaint(Paint paint, float f, String str) {
        Brush definedBrush;
        String resolveIDFromString = PropHelper.resolveIDFromString(str);
        if (TextUtils.isEmpty(resolveIDFromString)) {
            int color = WXResourceUtils.getColor(str, -16777216);
            paint.setColor(Color.argb((int) ((((float) Color.alpha(color)) / 255.0f) * f * 255.0f), Color.red(color), Color.green(color), Color.blue(color)));
            return;
        }
        SVGViewComponent sVGViewComponent = getSVGViewComponent();
        if (sVGViewComponent != null && (definedBrush = sVGViewComponent.getDefinedBrush(resolveIDFromString)) != null) {
            if (this.mCachedBox == null) {
                this.mCachedBox = new RectF();
                if (this.mPath != null) {
                    this.mPath.computeBounds(this.mCachedBox, true);
                }
            }
            definedBrush.setupPaint(paint, this.mCachedBox, this.mScale, f);
        }
    }

    /* access modifiers changed from: protected */
    public Region getRegion(@NonNull Path path) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        Region region = new Region();
        region.setPath(path, new Region((int) Math.floor((double) rectF.left), (int) Math.floor((double) rectF.top), (int) Math.ceil((double) rectF.right), (int) Math.ceil((double) rectF.bottom)));
        return region;
    }

    public Rect getBounds() {
        if (this.mRegion == null) {
            if (this.mPath == null) {
                this.mPath = getPath((Canvas) null, (Paint) null);
            }
            this.mRegion = getRegion(this.mPath);
        }
        return this.mRegion.getBounds();
    }

    public void mergeProperties(RenderableSVGVirtualComponent renderableSVGVirtualComponent) {
        this.mGroupComponentRef = renderableSVGVirtualComponent;
    }

    public void resetProperties() {
        this.mGroupComponentRef = null;
    }

    /* access modifiers changed from: protected */
    public void markUpdated() {
        super.markUpdated();
        this.mClipRegion = null;
        this.mClipRegionPath = null;
        this.mCachedClipPath = null;
        if (getParent() instanceof SVGClipPathComponent) {
            ((SVGClipPathComponent) getParent()).setDirty(true);
        }
    }

    public void saveDefinition() {
        String findComponentId = findComponentId();
        SVGViewComponent sVGViewComponent = getSVGViewComponent();
        if (!TextUtils.isEmpty(findComponentId) && sVGViewComponent != null) {
            sVGViewComponent.defineGraphicalTemplate(findComponentId, this);
        }
    }

    public void computeVisiblePointInViewCoordinate(PointF pointF) {
        Rect bounds = getBounds();
        pointF.set((float) (-bounds.left), (float) (-bounds.top));
    }
}
