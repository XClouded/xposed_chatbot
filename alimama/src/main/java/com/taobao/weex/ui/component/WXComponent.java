package com.taobao.weex.ui.component;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.JSONLexer;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.ju.track.csv.CsvReader;
import com.taobao.weex.ComponentObserver;
import com.taobao.weex.IWXActivityStateListener;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXAccessibilityRoleAdapter;
import com.taobao.weex.adapter.IWXConfigAdapter;
import com.taobao.weex.bridge.EventResult;
import com.taobao.weex.bridge.Invoker;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.IWXObject;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.common.WXRuntimeException;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.dom.transition.WXTransition;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.performance.WXAnalyzerDataTransfer;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.tracing.Stopwatch;
import com.taobao.weex.tracing.WXTracing;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.ui.WXRenderManager;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.action.GraphicActionAnimation;
import com.taobao.weex.ui.action.GraphicActionUpdateStyle;
import com.taobao.weex.ui.action.GraphicPosition;
import com.taobao.weex.ui.action.GraphicSize;
import com.taobao.weex.ui.animation.WXAnimationBean;
import com.taobao.weex.ui.animation.WXAnimationModule;
import com.taobao.weex.ui.component.basic.WXBasicComponent;
import com.taobao.weex.ui.component.binding.Statements;
import com.taobao.weex.ui.component.list.WXCell;
import com.taobao.weex.ui.component.list.template.jni.NativeRenderObjectUtils;
import com.taobao.weex.ui.component.pesudo.OnActivePseudoListner;
import com.taobao.weex.ui.component.pesudo.PesudoStatus;
import com.taobao.weex.ui.component.pesudo.TouchActivePseudoListener;
import com.taobao.weex.ui.flat.FlatComponent;
import com.taobao.weex.ui.flat.FlatGUIContext;
import com.taobao.weex.ui.flat.widget.AndroidViewWidget;
import com.taobao.weex.ui.flat.widget.Widget;
import com.taobao.weex.ui.view.border.BorderDrawable;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.ui.view.gesture.WXGestureType;
import com.taobao.weex.utils.BoxShadowUtil;
import com.taobao.weex.utils.WXDataStructureUtil;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXReflectionUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import kotlin.text.Typography;

public abstract class WXComponent<T extends View> extends WXBasicComponent implements IWXObject, IWXActivityStateListener, OnActivePseudoListner {
    public static final String PROP_FIXED_SIZE = "fixedSize";
    public static final String PROP_FS_MATCH_PARENT = "m";
    public static final String PROP_FS_WRAP_CONTENT = "w";
    public static final String ROOT = "_root";
    public static final int STATE_ALL_FINISH = 2;
    public static final int STATE_DOM_FINISH = 0;
    public static final int STATE_UI_FINISH = 1;
    public static final String TYPE = "type";
    public static final int TYPE_COMMON = 0;
    public static final int TYPE_VIRTUAL = 1;
    @Nullable
    private ConcurrentLinkedQueue<Pair<String, Map<String, Object>>> animations;
    protected ContentBoxMeasurement contentBoxMeasurement;
    public int interactionAbsoluteX;
    public int interactionAbsoluteY;
    public boolean isIgnoreInteraction;
    private boolean isLastLayoutDirectionRTL;
    private boolean isUsing;
    private int mAbsoluteX;
    private int mAbsoluteY;
    private WXAnimationModule.AnimationHolder mAnimationHolder;
    private Set<String> mAppendEvents;
    /* access modifiers changed from: private */
    public BorderDrawable mBackgroundDrawable;
    protected int mChildrensWidth;
    private WXComponent<T>.OnClickListenerImp mClickEventListener;
    private Context mContext;
    public int mDeepInComponentTree;
    private int mFixedProp;
    /* access modifiers changed from: private */
    public List<OnFocusChangeListener> mFocusChangeListeners;
    protected WXGesture mGesture;
    @Nullable
    private Set<String> mGestureType;
    private boolean mHasAddFocusListener;
    private IFComponentHolder mHolder;
    T mHost;
    /* access modifiers changed from: private */
    public List<OnClickListener> mHostClickListeners;
    /* access modifiers changed from: private */
    public WXSDKInstance mInstance;
    public boolean mIsAddElementToTree;
    private boolean mIsDestroyed;
    private boolean mIsDisabled;
    private String mLastBoxShadowId;
    private boolean mLazy;
    private boolean mNeedLayoutOnAnimation;
    private volatile WXVContainer mParent;
    private PesudoStatus mPesudoStatus;
    private int mPreRealHeight;
    private int mPreRealLeft;
    private int mPreRealRight;
    private int mPreRealTop;
    private int mPreRealWidth;
    private GraphicSize mPseudoResetGraphicSize;
    private Drawable mRippleBackground;
    private int mStickyOffset;
    public WXTracing.TraceInfo mTraceInfo;
    private WXTransition mTransition;
    private int mType;
    private String mViewTreeKey;
    private boolean waste;

    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RenderState {
    }

    public static class MeasureOutput {
        public int height;
        public int width;
    }

    public interface OnClickListener {
        void onHostViewClick();
    }

    public interface OnFocusChangeListener {
        void onFocusChange(boolean z);
    }

    /* access modifiers changed from: protected */
    public void appendEventToDOM(String str) {
    }

    public String getAttrByKey(String str) {
        return "default";
    }

    public int getLayoutTopOffsetForSibling() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public T initComponentHostView(@NonNull Context context) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void layoutDirectionDidChanged(boolean z) {
    }

    public boolean onActivityBack() {
        return false;
    }

    public void onActivityCreate() {
    }

    public void onActivityDestroy() {
    }

    public void onActivityPause() {
    }

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public void onActivityResume() {
    }

    public void onActivityStart() {
    }

    public void onActivityStop() {
    }

    /* access modifiers changed from: protected */
    public void onCreate() {
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onInvokeUnknownMethod(String str, JSONArray jSONArray) {
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
    }

    public void refreshData(WXComponent wXComponent) {
    }

    @Deprecated
    public void registerActivityStateListener() {
    }

    public void removeVirtualComponent() {
    }

    @Deprecated
    public WXComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    @Deprecated
    public WXComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, 0, basicComponentData);
    }

    public WXComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, int i, BasicComponentData basicComponentData) {
        super(basicComponentData);
        this.mFixedProp = 0;
        this.mAbsoluteY = 0;
        this.mAbsoluteX = 0;
        this.isLastLayoutDirectionRTL = false;
        this.mPreRealWidth = 0;
        this.mPreRealHeight = 0;
        this.mPreRealLeft = 0;
        this.mPreRealRight = 0;
        this.mPreRealTop = 0;
        this.mStickyOffset = 0;
        this.isUsing = false;
        this.mIsDestroyed = false;
        this.mIsDisabled = false;
        this.mType = 0;
        this.mNeedLayoutOnAnimation = false;
        this.mDeepInComponentTree = 0;
        this.mIsAddElementToTree = false;
        this.interactionAbsoluteX = 0;
        this.interactionAbsoluteY = 0;
        this.mChildrensWidth = 0;
        this.mHasAddFocusListener = false;
        this.mTraceInfo = new WXTracing.TraceInfo();
        this.waste = false;
        this.isIgnoreInteraction = false;
        this.mLazy = false;
        this.mInstance = wXSDKInstance;
        this.mContext = this.mInstance.getContext();
        this.mParent = wXVContainer;
        this.mType = i;
        if (wXSDKInstance != null) {
            setViewPortWidth(wXSDKInstance.getInstanceViewPortWidth());
        }
        onCreate();
        ComponentObserver componentObserver = getInstance().getComponentObserver();
        if (componentObserver != null) {
            componentObserver.onCreate(this);
        }
    }

    /* access modifiers changed from: protected */
    public final void bindComponent(WXComponent wXComponent) {
        super.bindComponent(wXComponent);
        if (getInstance() != null) {
            setViewPortWidth(getInstance().getInstanceViewPortWidth());
        }
        this.mParent = wXComponent.getParent();
        this.mType = wXComponent.getType();
    }

    /* access modifiers changed from: protected */
    public final void setContentBoxMeasurement(ContentBoxMeasurement contentBoxMeasurement2) {
        this.contentBoxMeasurement = contentBoxMeasurement2;
        this.mInstance.addContentBoxMeasurement(getRenderObjectPtr(), contentBoxMeasurement2);
        WXBridgeManager.getInstance().bindMeasurementToRenderObject(getRenderObjectPtr());
    }

    @SuppressLint({"RtlHardcoded"})
    public void setMarginsSupportRTL(ViewGroup.MarginLayoutParams marginLayoutParams, int i, int i2, int i3, int i4) {
        marginLayoutParams.setMargins(i, i2, i3, i4);
        if (marginLayoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) marginLayoutParams).gravity = 51;
        }
    }

    public void updateStyles(WXComponent wXComponent) {
        if (wXComponent != null) {
            updateProperties(wXComponent.getStyles());
            applyBorder(wXComponent);
        }
    }

    public void updateStyles(Map<String, Object> map) {
        if (map != null) {
            updateProperties(map);
            applyBorder(this);
        }
    }

    public void updateAttrs(WXComponent wXComponent) {
        if (wXComponent != null) {
            updateProperties(wXComponent.getAttrs());
        }
    }

    public void updateAttrs(Map<String, Object> map) {
        if (map != null) {
            updateProperties(map);
        }
    }

    private void applyBorder(WXComponent wXComponent) {
        CSSShorthand border = wXComponent.getBorder();
        float f = border.get(CSSShorthand.EDGE.LEFT);
        float f2 = border.get(CSSShorthand.EDGE.TOP);
        float f3 = border.get(CSSShorthand.EDGE.RIGHT);
        float f4 = border.get(CSSShorthand.EDGE.BOTTOM);
        if (this.mHost != null) {
            setBorderWidth("borderLeftWidth", f);
            setBorderWidth("borderTopWidth", f2);
            setBorderWidth("borderRightWidth", f3);
            setBorderWidth("borderBottomWidth", f4);
        }
    }

    public void setPadding(CSSShorthand cSSShorthand, CSSShorthand cSSShorthand2) {
        int i = (int) (cSSShorthand.get(CSSShorthand.EDGE.LEFT) + cSSShorthand2.get(CSSShorthand.EDGE.LEFT));
        int i2 = (int) (cSSShorthand.get(CSSShorthand.EDGE.TOP) + cSSShorthand2.get(CSSShorthand.EDGE.TOP));
        int i3 = (int) (cSSShorthand.get(CSSShorthand.EDGE.RIGHT) + cSSShorthand2.get(CSSShorthand.EDGE.RIGHT));
        int i4 = (int) (cSSShorthand.get(CSSShorthand.EDGE.BOTTOM) + cSSShorthand2.get(CSSShorthand.EDGE.BOTTOM));
        if (this instanceof FlatComponent) {
            FlatComponent flatComponent = (FlatComponent) this;
            if (!flatComponent.promoteToView(true)) {
                flatComponent.getOrCreateFlatWidget().setContentBox(i, i2, i3, i4);
                return;
            }
        }
        if (this.mHost != null) {
            this.mHost.setPadding(i, i2, i3, i4);
        }
    }

    public void applyComponentEvents() {
        applyEvents();
    }

    private void applyEvents() {
        if (getEvents() != null && !getEvents().isEmpty()) {
            WXEvent events = getEvents();
            int size = events.size();
            int i = 0;
            while (i < size && i < events.size()) {
                addEvent((String) events.get(i));
                i++;
            }
            setActiveTouchListener();
        }
    }

    public void addEvent(String str) {
        if (this.mAppendEvents == null) {
            this.mAppendEvents = new HashSet();
        }
        if (!TextUtils.isEmpty(str) && !this.mAppendEvents.contains(str)) {
            View realView = getRealView();
            if (str.equals(Constants.Event.LAYEROVERFLOW)) {
                addLayerOverFlowListener(getRef());
            }
            if (str.equals("click")) {
                if (realView != null) {
                    if (this.mClickEventListener == null) {
                        this.mClickEventListener = new OnClickListenerImp();
                    }
                    addClickListener(this.mClickEventListener);
                } else {
                    return;
                }
            } else if (str.equals(Constants.Event.FOCUS) || str.equals(Constants.Event.BLUR)) {
                if (!this.mHasAddFocusListener) {
                    this.mHasAddFocusListener = true;
                    addFocusChangeListener(new OnFocusChangeListener() {
                        public void onFocusChange(boolean z) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("timeStamp", Long.valueOf(System.currentTimeMillis()));
                            WXComponent.this.fireEvent(z ? Constants.Event.FOCUS : Constants.Event.BLUR, hashMap);
                        }
                    });
                }
            } else if (!needGestureDetector(str)) {
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
            } else if (realView != null) {
                if (realView instanceof WXGestureObservable) {
                    if (this.mGesture == null) {
                        this.mGesture = new WXGesture(this, this.mContext);
                        this.mGesture.setPreventMoveEvent(WXUtils.getBoolean(getAttrs().get("preventMoveEvent"), false).booleanValue());
                    }
                    if (this.mGestureType == null) {
                        this.mGestureType = new HashSet();
                    }
                    this.mGestureType.add(str);
                    ((WXGestureObservable) realView).registerGestureListener(this.mGesture);
                } else {
                    WXLogUtils.e(realView.getClass().getSimpleName() + " don't implement " + "WXGestureObservable, so no gesture is supported.");
                }
            } else {
                return;
            }
            this.mAppendEvents.add(str);
        }
    }

    public void bindHolder(IFComponentHolder iFComponentHolder) {
        this.mHolder = iFComponentHolder;
    }

    public WXSDKInstance getInstance() {
        return this.mInstance;
    }

    public Context getContext() {
        return this.mContext;
    }

    /* access modifiers changed from: protected */
    public final WXComponent findComponent(String str) {
        if (this.mInstance == null || str == null) {
            return null;
        }
        return WXSDKManager.getInstance().getWXRenderManager().getWXComponent(this.mInstance.getInstanceId(), str);
    }

    public void postAnimation(WXAnimationModule.AnimationHolder animationHolder) {
        this.mAnimationHolder = animationHolder;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public boolean isFlatUIEnabled() {
        return this.mParent != null && this.mParent.isFlatUIEnabled();
    }

    private class OnClickListenerImp implements OnClickListener {
        private OnClickListenerImp() {
        }

        public void onHostViewClick() {
            HashMap newHashMapWithExpectedSize = WXDataStructureUtil.newHashMapWithExpectedSize(1);
            HashMap newHashMapWithExpectedSize2 = WXDataStructureUtil.newHashMapWithExpectedSize(4);
            int[] iArr = new int[2];
            WXComponent.this.mHost.getLocationOnScreen(iArr);
            newHashMapWithExpectedSize2.put(Constants.Name.X, Float.valueOf(WXViewUtils.getWebPxByWidth((float) iArr[0], WXComponent.this.mInstance.getInstanceViewPortWidth())));
            newHashMapWithExpectedSize2.put(Constants.Name.Y, Float.valueOf(WXViewUtils.getWebPxByWidth((float) iArr[1], WXComponent.this.mInstance.getInstanceViewPortWidth())));
            newHashMapWithExpectedSize2.put("width", Float.valueOf(WXViewUtils.getWebPxByWidth(WXComponent.this.getLayoutWidth(), WXComponent.this.mInstance.getInstanceViewPortWidth())));
            newHashMapWithExpectedSize2.put("height", Float.valueOf(WXViewUtils.getWebPxByWidth(WXComponent.this.getLayoutHeight(), WXComponent.this.mInstance.getInstanceViewPortWidth())));
            newHashMapWithExpectedSize.put("position", newHashMapWithExpectedSize2);
            WXComponent.this.fireEvent("click", newHashMapWithExpectedSize);
        }
    }

    public String getInstanceId() {
        return this.mInstance.getInstanceId();
    }

    public Rect getComponentSize() {
        Rect rect = new Rect();
        if (this.mHost != null) {
            int[] iArr = new int[2];
            int[] iArr2 = new int[2];
            this.mHost.getLocationOnScreen(iArr);
            this.mInstance.getContainerView().getLocationOnScreen(iArr2);
            int i = iArr[0] - iArr2[0];
            int i2 = (iArr[1] - this.mStickyOffset) - iArr2[1];
            rect.set(i, i2, ((int) getLayoutWidth()) + i, ((int) getLayoutHeight()) + i2);
        }
        return rect;
    }

    public final void invoke(String str, JSONArray jSONArray) {
        Invoker methodInvoker = this.mHolder.getMethodInvoker(str);
        if (methodInvoker != null) {
            try {
                getInstance().getNativeInvokeHelper().invoke(this, methodInvoker, jSONArray);
            } catch (Exception e) {
                WXLogUtils.e("[WXComponent] updateProperties :class:" + getClass() + "method:" + methodInvoker.toString() + " function " + WXLogUtils.getStackTrace(e));
            }
        } else {
            onInvokeUnknownMethod(str, jSONArray);
        }
    }

    public final void fireEvent(String str) {
        fireEvent(str, (Map<String, Object>) null);
    }

    public final void fireEvent(String str, Map<String, Object> map) {
        if (WXUtils.getBoolean(getAttrs().get("fireEventSyn"), false).booleanValue()) {
            fireEventWait(str, map);
        } else {
            fireEvent(str, map, (Map<String, Object>) null, (EventResult) null);
        }
    }

    public final EventResult fireEventWait(String str, Map<String, Object> map) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        AnonymousClass2 r1 = new EventResult() {
            public void onCallback(Object obj) {
                super.onCallback(obj);
                countDownLatch.countDown();
            }
        };
        try {
            fireEvent(str, map, (Map<String, Object>) null, r1);
            countDownLatch.await(50, TimeUnit.MILLISECONDS);
            return r1;
        } catch (Exception e) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e("fireEventWait", (Throwable) e);
            }
            return r1;
        }
    }

    /* access modifiers changed from: protected */
    public final void fireEvent(String str, Map<String, Object> map, Map<String, Object> map2) {
        fireEvent(str, map, map2, (EventResult) null);
    }

    private final void fireEvent(String str, Map<String, Object> map, Map<String, Object> map2, EventResult eventResult) {
        String componentId;
        if (this.mInstance != null) {
            List list = null;
            if (!(getEvents() == null || getEvents().getEventBindingArgsValues() == null)) {
                list = getEvents().getEventBindingArgsValues().get(str);
            }
            List list2 = list;
            if (!(map == null || (componentId = Statements.getComponentId(this)) == null)) {
                map.put("componentId", componentId);
            }
            this.mInstance.fireEvent(getRef(), str, map, map2, list2, eventResult);
        }
    }

    public Object findTypeParent(WXComponent wXComponent, Class cls) {
        if (wXComponent.getClass() == cls) {
            return wXComponent;
        }
        if (wXComponent.getParent() == null) {
            return null;
        }
        findTypeParent(wXComponent.getParent(), cls);
        return null;
    }

    public boolean isLazy() {
        if (this.mLazy) {
            return true;
        }
        if (this.mParent == null || !this.mParent.isLazy()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final void addFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        View realView;
        if (onFocusChangeListener != null && (realView = getRealView()) != null) {
            if (this.mFocusChangeListeners == null) {
                this.mFocusChangeListeners = new ArrayList();
                realView.setFocusable(true);
                realView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View view, boolean z) {
                        for (OnFocusChangeListener onFocusChangeListener : WXComponent.this.mFocusChangeListeners) {
                            if (onFocusChangeListener != null) {
                                onFocusChangeListener.onFocusChange(z);
                            }
                        }
                    }
                });
            }
            this.mFocusChangeListeners.add(onFocusChangeListener);
        }
    }

    /* access modifiers changed from: protected */
    public final void addClickListener(OnClickListener onClickListener) {
        View realView;
        if (onClickListener != null && (realView = getRealView()) != null) {
            if (this.mHostClickListeners == null) {
                this.mHostClickListeners = new ArrayList();
                realView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (WXComponent.this.mGesture == null || !WXComponent.this.mGesture.isTouchEventConsumedByAdvancedGesture()) {
                            for (OnClickListener onClickListener : WXComponent.this.mHostClickListeners) {
                                if (onClickListener != null) {
                                    onClickListener.onHostViewClick();
                                }
                            }
                        }
                    }
                });
            }
            this.mHostClickListeners.add(onClickListener);
        }
    }

    /* access modifiers changed from: protected */
    public final void removeClickListener(OnClickListener onClickListener) {
        this.mHostClickListeners.remove(onClickListener);
    }

    public void bindData(WXComponent wXComponent) {
        if (!isLazy()) {
            if (wXComponent == null) {
                wXComponent = this;
            }
            bindComponent(wXComponent);
            updateStyles(wXComponent);
            updateAttrs(wXComponent);
            updateExtra(wXComponent.getExtra());
        }
    }

    public void applyLayoutAndEvent(WXComponent wXComponent) {
        if (!isLazy()) {
            if (wXComponent == null) {
                wXComponent = this;
            }
            bindComponent(wXComponent);
            setSafeLayout(wXComponent);
            setPadding(wXComponent.getPadding(), wXComponent.getBorder());
            applyEvents();
        }
    }

    public void setDemission(GraphicSize graphicSize, GraphicPosition graphicPosition) {
        setLayoutPosition(graphicPosition);
        setLayoutSize(graphicSize);
    }

    public void updateDemission(float f, float f2, float f3, float f4, float f5, float f6) {
        getLayoutPosition().update(f, f2, f3, f4);
        getLayoutSize().update(f6, f5);
    }

    public void applyLayoutOnly() {
        if (!isLazy()) {
            setSafeLayout(this);
            setPadding(getPadding(), getBorder());
        }
    }

    @Deprecated
    public void updateProperties(Map<String, Object> map) {
        if (map == null) {
            return;
        }
        if (this.mHost != null || isVirtualComponent()) {
            for (Map.Entry next : map.entrySet()) {
                Object key = next.getKey();
                String string = WXUtils.getString(key, (String) null);
                if (string != null && !(key instanceof String)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("componentType", getComponentType());
                    hashMap.put("actual key", string == null ? "" : string);
                    WXExceptionUtils.commitCriticalExceptionRT(getInstanceId(), WXErrorCode.WX_RENDER_ERR_COMPONENT_ATTR_KEY, "WXComponent.updateProperties", WXErrorCode.WX_RENDER_ERR_COMPONENT_ATTR_KEY.getErrorMsg(), hashMap);
                }
                Object value = next.getValue();
                String string2 = WXUtils.getString(value, (String) null);
                if (string == null) {
                    WXExceptionUtils.commitCriticalExceptionRT(getInstanceId(), WXErrorCode.WX_RENDER_ERR_NULL_KEY, "updateProperties", WXErrorCode.WX_RENDER_ERR_NULL_KEY.getErrorMsg(), (Map<String, String>) null);
                } else {
                    if (TextUtils.isEmpty(string2)) {
                        value = convertEmptyProperty(string, string2);
                    }
                    if (setProperty(string, value)) {
                        continue;
                    } else if (this.mHolder != null) {
                        Invoker propertyInvoker = this.mHolder.getPropertyInvoker(string);
                        if (propertyInvoker != null) {
                            try {
                                Type[] parameterTypes = propertyInvoker.getParameterTypes();
                                if (parameterTypes.length != 1) {
                                    WXLogUtils.e("[WXComponent] setX method only one parameter：" + propertyInvoker);
                                    return;
                                }
                                propertyInvoker.invoke(this, WXReflectionUtils.parseArgument(parameterTypes[0], value));
                            } catch (Exception e) {
                                WXLogUtils.e("[WXComponent] updateProperties :class:" + getClass() + "method:" + propertyInvoker.toString() + " function " + WXLogUtils.getStackTrace(e));
                            }
                        } else {
                            continue;
                        }
                    } else {
                        return;
                    }
                }
            }
            readyToRender();
            if ((this instanceof FlatComponent) && this.mBackgroundDrawable != null) {
                FlatComponent flatComponent = (FlatComponent) this;
                if (!flatComponent.promoteToView(true) && !(flatComponent.getOrCreateFlatWidget() instanceof AndroidViewWidget)) {
                    flatComponent.getOrCreateFlatWidget().setBackgroundAndBorder(this.mBackgroundDrawable);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        if (str == null) {
            return true;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -1989576717:
                if (str.equals("borderRightColor")) {
                    c = 18;
                    break;
                }
                break;
            case -1974639039:
                if (str.equals("borderRightStyle")) {
                    c = CsvReader.Letters.FORM_FEED;
                    break;
                }
                break;
            case -1971292586:
                if (str.equals("borderRightWidth")) {
                    c = '2';
                    break;
                }
                break;
            case -1501175880:
                if (str.equals("paddingLeft")) {
                    c = '-';
                    break;
                }
                break;
            case -1470826662:
                if (str.equals("borderTopColor")) {
                    c = 17;
                    break;
                }
                break;
            case -1455888984:
                if (str.equals("borderTopStyle")) {
                    c = 15;
                    break;
                }
                break;
            case -1452542531:
                if (str.equals("borderTopWidth")) {
                    c = '1';
                    break;
                }
                break;
            case -1383228885:
                if (str.equals("bottom")) {
                    c = '8';
                    break;
                }
                break;
            case -1375815020:
                if (str.equals("minWidth")) {
                    c = CsvReader.Letters.ESCAPE;
                    break;
                }
                break;
            case -1308858324:
                if (str.equals("borderBottomColor")) {
                    c = 19;
                    break;
                }
                break;
            case -1293920646:
                if (str.equals("borderBottomStyle")) {
                    c = 13;
                    break;
                }
                break;
            case -1290574193:
                if (str.equals("borderBottomWidth")) {
                    c = '3';
                    break;
                }
                break;
            case -1267206133:
                if (str.equals("opacity")) {
                    c = 5;
                    break;
                }
                break;
            case -1228066334:
                if (str.equals("borderTopLeftRadius")) {
                    c = 7;
                    break;
                }
                break;
            case -1221029593:
                if (str.equals("height")) {
                    c = 29;
                    break;
                }
                break;
            case -1111969773:
                if (str.equals(Constants.Name.ARIA_HIDDEN)) {
                    c = 25;
                    break;
                }
                break;
            case -1081309778:
                if (str.equals("margin")) {
                    c = Typography.amp;
                    break;
                }
                break;
            case -1063257157:
                if (str.equals("alignItems")) {
                    c = ' ';
                    break;
                }
                break;
            case -1044792121:
                if (str.equals("marginTop")) {
                    c = '\'';
                    break;
                }
                break;
            case -975171706:
                if (str.equals("flexDirection")) {
                    c = '#';
                    break;
                }
                break;
            case -906066005:
                if (str.equals("maxHeight")) {
                    c = 31;
                    break;
                }
                break;
            case -863700117:
                if (str.equals(Constants.Name.ARIA_LABEL)) {
                    c = 24;
                    break;
                }
                break;
            case -806339567:
                if (str.equals("padding")) {
                    c = '+';
                    break;
                }
                break;
            case -289173127:
                if (str.equals("marginBottom")) {
                    c = '*';
                    break;
                }
                break;
            case -242276144:
                if (str.equals("borderLeftColor")) {
                    c = 20;
                    break;
                }
                break;
            case -227338466:
                if (str.equals("borderLeftStyle")) {
                    c = 14;
                    break;
                }
                break;
            case -223992013:
                if (str.equals("borderLeftWidth")) {
                    c = '4';
                    break;
                }
                break;
            case -133587431:
                if (str.equals("minHeight")) {
                    c = 30;
                    break;
                }
                break;
            case -4379043:
                if (str.equals("elevation")) {
                    c = 22;
                    break;
                }
                break;
            case 115029:
                if (str.equals("top")) {
                    c = '6';
                    break;
                }
                break;
            case 3145721:
                if (str.equals("flex")) {
                    c = '\"';
                    break;
                }
                break;
            case 3317767:
                if (str.equals("left")) {
                    c = '5';
                    break;
                }
                break;
            case 3506294:
                if (str.equals(Constants.Name.ROLE)) {
                    c = Operators.CONDITION_IF_MIDDLE;
                    break;
                }
                break;
            case 90130308:
                if (str.equals("paddingTop")) {
                    c = ',';
                    break;
                }
                break;
            case 108511772:
                if (str.equals("right")) {
                    c = '7';
                    break;
                }
                break;
            case 113126854:
                if (str.equals("width")) {
                    c = JSONLexer.EOI;
                    break;
                }
                break;
            case 202355100:
                if (str.equals("paddingBottom")) {
                    c = DXTemplateNamePathUtil.DIR;
                    break;
                }
                break;
            case 270940796:
                if (str.equals("disabled")) {
                    c = 1;
                    break;
                }
                break;
            case 333432965:
                if (str.equals("borderTopRightRadius")) {
                    c = 8;
                    break;
                }
                break;
            case 400381634:
                if (str.equals("maxWidth")) {
                    c = 28;
                    break;
                }
                break;
            case 581268560:
                if (str.equals("borderBottomLeftRadius")) {
                    c = 10;
                    break;
                }
                break;
            case 588239831:
                if (str.equals("borderBottomRightRadius")) {
                    c = 9;
                    break;
                }
                break;
            case 713848971:
                if (str.equals("paddingRight")) {
                    c = '.';
                    break;
                }
                break;
            case 717381201:
                if (str.equals("preventMoveEvent")) {
                    c = 0;
                    break;
                }
                break;
            case 722830999:
                if (str.equals("borderColor")) {
                    c = 16;
                    break;
                }
                break;
            case 737768677:
                if (str.equals("borderStyle")) {
                    c = CsvReader.Letters.VERTICAL_TAB;
                    break;
                }
                break;
            case 741115130:
                if (str.equals("borderWidth")) {
                    c = '0';
                    break;
                }
                break;
            case 743055051:
                if (str.equals(Constants.Name.BOX_SHADOW)) {
                    c = '9';
                    break;
                }
                break;
            case 747463061:
                if (str.equals(PROP_FIXED_SIZE)) {
                    c = 23;
                    break;
                }
                break;
            case 747804969:
                if (str.equals("position")) {
                    c = 2;
                    break;
                }
                break;
            case 975087886:
                if (str.equals("marginRight")) {
                    c = ')';
                    break;
                }
                break;
            case 1287124693:
                if (str.equals("backgroundColor")) {
                    c = 3;
                    break;
                }
                break;
            case 1292595405:
                if (str.equals("backgroundImage")) {
                    c = 4;
                    break;
                }
                break;
            case 1349188574:
                if (str.equals("borderRadius")) {
                    c = 6;
                    break;
                }
                break;
            case 1744216035:
                if (str.equals("flexWrap")) {
                    c = WXUtils.PERCENT;
                    break;
                }
                break;
            case 1767100401:
                if (str.equals("alignSelf")) {
                    c = '!';
                    break;
                }
                break;
            case 1860657097:
                if (str.equals("justifyContent")) {
                    c = '$';
                    break;
                }
                break;
            case 1941332754:
                if (str.equals("visibility")) {
                    c = 21;
                    break;
                }
                break;
            case 1970934485:
                if (str.equals("marginLeft")) {
                    c = '(';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                if (this.mGesture != null) {
                    this.mGesture.setPreventMoveEvent(WXUtils.getBoolean(obj, false).booleanValue());
                }
                return true;
            case 1:
                Boolean bool = WXUtils.getBoolean(obj, (Boolean) null);
                if (bool != null) {
                    setDisabled(bool.booleanValue());
                    setPseudoClassStatus(Constants.PSEUDO.DISABLED, bool.booleanValue());
                }
                return true;
            case 2:
                String string = WXUtils.getString(obj, (String) null);
                if (string != null) {
                    setSticky(string);
                }
                return true;
            case 3:
                String string2 = WXUtils.getString(obj, (String) null);
                if (string2 != null) {
                    setBackgroundColor(string2);
                }
                return true;
            case 4:
                String string3 = WXUtils.getString(obj, (String) null);
                if (!(string3 == null || this.mHost == null)) {
                    setBackgroundImage(string3);
                }
                return true;
            case 5:
                Float f = WXUtils.getFloat(obj, (Float) null);
                if (f != null) {
                    setOpacity(f.floatValue());
                }
                return true;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                Float f2 = WXUtils.getFloat(obj, (Float) null);
                if (f2 != null) {
                    setBorderRadius(str, f2.floatValue());
                }
                return true;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                String string4 = WXUtils.getString(obj, (String) null);
                if (string4 != null) {
                    setBorderStyle(str, string4);
                }
                return true;
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                String string5 = WXUtils.getString(obj, (String) null);
                if (string5 != null) {
                    setBorderColor(str, string5);
                }
                return true;
            case 21:
                String string6 = WXUtils.getString(obj, (String) null);
                if (string6 != null) {
                    setVisibility(string6);
                }
                return true;
            case 22:
                if (obj != null) {
                    updateElevation();
                }
                return true;
            case 23:
                setFixedSize(WXUtils.getString(obj, PROP_FS_MATCH_PARENT));
                return true;
            case 24:
                setAriaLabel(WXUtils.getString(obj, ""));
                return true;
            case 25:
                setAriaHidden(WXUtils.getBoolean(obj, false).booleanValue());
                return true;
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case ' ':
            case '!':
            case '\"':
            case '#':
            case '$':
            case '%':
            case '&':
            case '\'':
            case '(':
            case ')':
            case '*':
            case '+':
            case ',':
            case '-':
            case '.':
            case '/':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
                return true;
            case '9':
                try {
                    updateBoxShadow();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                return true;
            case ':':
                setRole(WXUtils.getString(obj, ""));
                return true;
            default:
                return false;
        }
    }

    /* access modifiers changed from: protected */
    public BorderDrawable getOrCreateBorder() {
        if (this.mBackgroundDrawable == null) {
            this.mBackgroundDrawable = new BorderDrawable();
            if (this.mHost != null) {
                WXViewUtils.setBackGround(this.mHost, (Drawable) null, this);
                if (this.mRippleBackground == null) {
                    WXViewUtils.setBackGround(this.mHost, this.mBackgroundDrawable, this);
                } else {
                    WXViewUtils.setBackGround(this.mHost, new LayerDrawable(new Drawable[]{this.mRippleBackground, this.mBackgroundDrawable}), this);
                }
            }
        }
        return this.mBackgroundDrawable;
    }

    public void setSafeLayout(WXComponent wXComponent) {
        if (!TextUtils.isEmpty(wXComponent.getComponentType()) && !TextUtils.isEmpty(wXComponent.getRef()) && wXComponent.getLayoutPosition() != null && wXComponent.getLayoutSize() != null) {
            setLayout(wXComponent);
        }
    }

    public void setLayout(WXComponent wXComponent) {
        int i;
        int i2;
        int i3;
        float f;
        WXComponent wXComponent2 = wXComponent;
        setLayoutSize(wXComponent.getLayoutSize());
        setLayoutPosition(wXComponent.getLayoutPosition());
        setPaddings(wXComponent.getPadding());
        setMargins(wXComponent.getMargin());
        setBorders(wXComponent.getBorder());
        boolean isLayoutRTL = wXComponent.isLayoutRTL();
        setIsLayoutRTL(isLayoutRTL);
        if (isLayoutRTL != wXComponent2.isLastLayoutDirectionRTL) {
            wXComponent2.isLastLayoutDirectionRTL = isLayoutRTL;
            layoutDirectionDidChanged(isLayoutRTL);
        }
        parseAnimation();
        boolean z = this.mParent == null;
        if (z) {
            i = 0;
        } else {
            i = this.mParent.getChildrenLayoutTopOffset();
        }
        CSSShorthand cSSShorthand = z ? new CSSShorthand() : this.mParent.getPadding();
        CSSShorthand cSSShorthand2 = z ? new CSSShorthand() : this.mParent.getBorder();
        int width = (int) getLayoutSize().getWidth();
        int height = (int) getLayoutSize().getHeight();
        if (isFixed()) {
            int top = ((int) (getLayoutPosition().getTop() - ((float) getInstance().getRenderContainerPaddingTop()))) + i;
            i3 = (int) (getLayoutPosition().getLeft() - ((float) getInstance().getRenderContainerPaddingLeft()));
            i2 = top;
        } else {
            i2 = ((int) ((getLayoutPosition().getTop() - cSSShorthand.get(CSSShorthand.EDGE.TOP)) - cSSShorthand2.get(CSSShorthand.EDGE.TOP))) + i;
            i3 = (int) ((getLayoutPosition().getLeft() - cSSShorthand.get(CSSShorthand.EDGE.LEFT)) - cSSShorthand2.get(CSSShorthand.EDGE.LEFT));
        }
        int i4 = (int) getMargin().get(CSSShorthand.EDGE.RIGHT);
        int i5 = (int) getMargin().get(CSSShorthand.EDGE.BOTTOM);
        Point point = new Point((int) getLayoutPosition().getLeft(), (int) getLayoutPosition().getTop());
        if (this.mPreRealWidth != width || this.mPreRealHeight != height || this.mPreRealLeft != i3 || this.mPreRealRight != i4 || this.mPreRealTop != i2) {
            if ((this instanceof WXCell) && height >= WXPerformance.VIEW_LIMIT_HEIGHT && width >= WXPerformance.VIEW_LIMIT_WIDTH) {
                this.mInstance.getApmForInstance().updateDiffStats(WXInstanceApm.KEY_PAGE_STATS_CELL_EXCEED_NUM, 1.0d);
                this.mInstance.getWXPerformance().cellExceedNum++;
                if (WXAnalyzerDataTransfer.isOpenPerformance) {
                    WXAnalyzerDataTransfer.transferPerformance(getInstanceId(), "details", WXInstanceApm.KEY_PAGE_STATS_CELL_EXCEED_NUM, String.format(Locale.ROOT, "cell:ref:%s,[w:%d,h:%d],attrs:%s,styles:%s", new Object[]{getRef(), Integer.valueOf(width), Integer.valueOf(height), getAttrs(), getStyles()}));
                }
            }
            float f2 = 0.0f;
            if (z) {
                f = 0.0f;
            } else {
                f = ((float) this.mParent.getAbsoluteY()) + getCSSLayoutTop();
            }
            this.mAbsoluteY = (int) f;
            if (!z) {
                f2 = getCSSLayoutLeft() + ((float) this.mParent.getAbsoluteX());
            }
            this.mAbsoluteX = (int) f2;
            if (this.mHost != null) {
                if (!(this.mHost instanceof ViewGroup) && this.mAbsoluteY + height > this.mInstance.getWeexHeight() + 1) {
                    if (!this.mInstance.mEnd) {
                        this.mInstance.onOldFsRenderTimeLogic();
                    }
                    if (!this.mInstance.isNewFsEnd) {
                        this.mInstance.isNewFsEnd = true;
                        this.mInstance.getApmForInstance().arriveNewFsRenderTime();
                    }
                }
                MeasureOutput measure = measure(width, height);
                setComponentLayoutParams(measure.width, measure.height, i3, i2, i4, i5, point);
            }
        }
    }

    private void setComponentLayoutParams(int i, int i2, int i3, int i4, int i5, int i6, Point point) {
        Widget widget;
        if (getInstance() != null && !getInstance().isDestroy()) {
            FlatGUIContext flatUIContext = getInstance().getFlatUIContext();
            if (flatUIContext != null && flatUIContext.getFlatComponentAncestor(this) != null) {
                if (this instanceof FlatComponent) {
                    FlatComponent flatComponent = (FlatComponent) this;
                    if (!flatComponent.promoteToView(true)) {
                        widget = flatComponent.getOrCreateFlatWidget();
                        setWidgetParams(widget, flatUIContext, point, i, i2, i3, i5, i4, i6);
                    }
                }
                widget = flatUIContext.getAndroidViewWidget(this);
                setWidgetParams(widget, flatUIContext, point, i, i2, i3, i5, i4, i6);
            } else if (this.mHost != null) {
                clearBoxShadow();
                if (isFixed()) {
                    setFixedHostLayoutParams(this.mHost, i, i2, i3, i5, i4, i6);
                } else {
                    setHostLayoutParams(this.mHost, i, i2, i3, i5, i4, i6);
                }
                recordInteraction(i, i2);
                this.mPreRealWidth = i;
                this.mPreRealHeight = i2;
                this.mPreRealLeft = i3;
                this.mPreRealRight = i5;
                this.mPreRealTop = i4;
                onFinishLayout();
                updateBoxShadow();
            }
        }
    }

    private void recordInteraction(int i, int i2) {
        if (this.mIsAddElementToTree) {
            boolean z = false;
            this.mIsAddElementToTree = false;
            if (this.mParent == null) {
                this.interactionAbsoluteX = 0;
                this.interactionAbsoluteY = 0;
            } else {
                float cSSLayoutTop = getCSSLayoutTop();
                float cSSLayoutLeft = getCSSLayoutLeft();
                this.interactionAbsoluteX = (int) (isFixed() ? cSSLayoutLeft : ((float) (this.mParent.interactionAbsoluteX + this.mParent.mChildrensWidth)) + cSSLayoutLeft);
                if (!isFixed()) {
                    cSSLayoutTop += (float) this.mParent.interactionAbsoluteY;
                }
                this.interactionAbsoluteY = (int) cSSLayoutTop;
                if ((WXBasicComponentType.SLIDER.equalsIgnoreCase(this.mParent.getComponentType()) || WXBasicComponentType.CYCLE_SLIDER.equalsIgnoreCase(this.mParent.getComponentType())) && !WXBasicComponentType.INDICATOR.equalsIgnoreCase(getComponentType())) {
                    this.mParent.mChildrensWidth += (int) (((float) i) + cSSLayoutLeft);
                }
            }
            if (getInstance().getApmForInstance().instanceRect == null) {
                getInstance().getApmForInstance().instanceRect = new Rect();
            }
            Rect rect = getInstance().getApmForInstance().instanceRect;
            rect.set(0, 0, this.mInstance.getWeexWidth(), this.mInstance.getWeexHeight());
            if (rect.contains(this.interactionAbsoluteX, this.interactionAbsoluteY) || rect.contains(this.interactionAbsoluteX + i, this.interactionAbsoluteY) || rect.contains(this.interactionAbsoluteX, this.interactionAbsoluteY + i2) || rect.contains(this.interactionAbsoluteX + i, this.interactionAbsoluteY + i2)) {
                z = true;
            }
            this.mInstance.onChangeElement(this, !z);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:28:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setWidgetParams(com.taobao.weex.ui.flat.widget.Widget r15, com.taobao.weex.ui.flat.FlatGUIContext r16, android.graphics.Point r17, int r18, int r19, int r20, int r21, int r22, int r23) {
        /*
            r14 = this;
            r9 = r14
            r10 = r15
            r0 = r16
            r1 = r17
            android.graphics.Point r11 = new android.graphics.Point
            r11.<init>()
            com.taobao.weex.ui.component.WXVContainer r2 = r9.mParent
            if (r2 == 0) goto L_0x008b
            com.taobao.weex.ui.component.WXVContainer r2 = r9.mParent
            boolean r2 = r2 instanceof com.taobao.weex.ui.flat.FlatComponent
            if (r2 == 0) goto L_0x0031
            com.taobao.weex.ui.component.WXVContainer r2 = r9.mParent
            com.taobao.weex.ui.flat.WidgetContainer r2 = r0.getFlatComponentAncestor(r2)
            if (r2 == 0) goto L_0x0031
            com.taobao.weex.ui.component.WXVContainer r2 = r9.mParent
            com.taobao.weex.ui.flat.widget.AndroidViewWidget r2 = r0.getAndroidViewWidget(r2)
            if (r2 != 0) goto L_0x0031
            int r2 = r1.x
            int r1 = r1.y
            r11.set(r2, r1)
            r12 = r20
            r13 = r22
            goto L_0x0038
        L_0x0031:
            r12 = r20
            r13 = r22
            r11.set(r12, r13)
        L_0x0038:
            com.taobao.weex.ui.component.WXVContainer r1 = r9.mParent
            boolean r1 = r1 instanceof com.taobao.weex.ui.flat.FlatComponent
            if (r1 == 0) goto L_0x0061
            com.taobao.weex.ui.component.WXVContainer r1 = r9.mParent
            com.taobao.weex.ui.flat.WidgetContainer r1 = r0.getFlatComponentAncestor(r1)
            if (r1 == 0) goto L_0x0061
            com.taobao.weex.ui.component.WXVContainer r1 = r9.mParent
            com.taobao.weex.ui.flat.widget.AndroidViewWidget r0 = r0.getAndroidViewWidget(r1)
            if (r0 != 0) goto L_0x0061
            com.taobao.weex.ui.component.WXVContainer r0 = r9.mParent
            com.taobao.weex.ui.flat.FlatComponent r0 = (com.taobao.weex.ui.flat.FlatComponent) r0
            com.taobao.weex.ui.flat.widget.Widget r0 = r0.getOrCreateFlatWidget()
            android.graphics.Point r0 = r0.getLocInFlatContainer()
            int r1 = r0.x
            int r0 = r0.y
            r11.offset(r1, r0)
        L_0x0061:
            com.taobao.weex.ui.component.WXVContainer r0 = r9.mParent
            T r2 = r9.mHost
            r1 = r14
            r3 = r18
            r4 = r19
            r5 = r20
            r6 = r21
            r7 = r22
            r8 = r23
            android.view.ViewGroup$LayoutParams r0 = r0.getChildLayoutParams(r1, r2, r3, r4, r5, r6, r7, r8)
            boolean r1 = r0 instanceof android.view.ViewGroup.MarginLayoutParams
            if (r1 == 0) goto L_0x008f
            int r1 = r0.width
            int r2 = r0.height
            android.view.ViewGroup$MarginLayoutParams r0 = (android.view.ViewGroup.MarginLayoutParams) r0
            int r3 = r0.leftMargin
            int r4 = r0.rightMargin
            int r5 = r0.topMargin
            int r0 = r0.bottomMargin
            r12 = r3
            r13 = r5
            goto L_0x0097
        L_0x008b:
            r12 = r20
            r13 = r22
        L_0x008f:
            r1 = r18
            r2 = r19
            r4 = r21
            r0 = r23
        L_0x0097:
            r16 = r15
            r17 = r1
            r18 = r2
            r19 = r12
            r20 = r4
            r21 = r13
            r22 = r0
            r23 = r11
            r16.setLayout(r17, r18, r19, r20, r21, r22, r23)
            boolean r3 = r10 instanceof com.taobao.weex.ui.flat.widget.AndroidViewWidget
            if (r3 == 0) goto L_0x00d1
            r3 = r10
            com.taobao.weex.ui.flat.widget.AndroidViewWidget r3 = (com.taobao.weex.ui.flat.widget.AndroidViewWidget) r3
            android.view.View r5 = r3.getView()
            if (r5 == 0) goto L_0x00d1
            android.view.View r3 = r3.getView()
            int r5 = r11.x
            int r6 = r11.y
            r15 = r14
            r16 = r3
            r17 = r1
            r18 = r2
            r19 = r5
            r20 = r4
            r21 = r6
            r22 = r0
            r15.setHostLayoutParams(r16, r17, r18, r19, r20, r21, r22)
        L_0x00d1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXComponent.setWidgetParams(com.taobao.weex.ui.flat.widget.Widget, com.taobao.weex.ui.flat.FlatGUIContext, android.graphics.Point, int, int, int, int, int, int):void");
    }

    /* JADX WARNING: type inference failed for: r1v1, types: [android.view.ViewGroup$MarginLayoutParams] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setHostLayoutParams(T r11, int r12, int r13, int r14, int r15, int r16, int r17) {
        /*
            r10 = this;
            r9 = r10
            com.taobao.weex.ui.component.WXVContainer r0 = r9.mParent
            if (r0 != 0) goto L_0x0018
            android.widget.FrameLayout$LayoutParams r6 = new android.widget.FrameLayout$LayoutParams
            r3 = r12
            r4 = r13
            r6.<init>(r12, r13)
            r0 = r10
            r1 = r6
            r2 = r14
            r3 = r16
            r4 = r15
            r5 = r17
            r0.setMarginsSupportRTL(r1, r2, r3, r4, r5)
            goto L_0x0028
        L_0x0018:
            r3 = r12
            r4 = r13
            com.taobao.weex.ui.component.WXVContainer r0 = r9.mParent
            r1 = r10
            r2 = r11
            r5 = r14
            r6 = r15
            r7 = r16
            r8 = r17
            android.view.ViewGroup$LayoutParams r6 = r0.getChildLayoutParams(r1, r2, r3, r4, r5, r6, r7, r8)
        L_0x0028:
            if (r6 == 0) goto L_0x002e
            r0 = r11
            r11.setLayoutParams(r6)
        L_0x002e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXComponent.setHostLayoutParams(android.view.View, int, int, int, int, int, int):void");
    }

    private void setFixedHostLayoutParams(T t, int i, int i2, int i3, int i4, int i5, int i6) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.width = i;
        layoutParams.height = i2;
        setMarginsSupportRTL(layoutParams, i3, i5, i4, i6);
        t.setLayoutParams(layoutParams);
        this.mInstance.moveFixedView(t);
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("Weex_Fixed_Style", "WXComponent:setLayout :" + i3 + Operators.SPACE_STR + i5 + Operators.SPACE_STR + i + Operators.SPACE_STR + i2);
            StringBuilder sb = new StringBuilder();
            sb.append("WXComponent:setLayout Left:");
            sb.append(getStyles().getLeft());
            sb.append(Operators.SPACE_STR);
            sb.append((int) getStyles().getTop());
            WXLogUtils.d("Weex_Fixed_Style", sb.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void updateBoxShadow() {
        if (BoxShadowUtil.isBoxShadowEnabled()) {
            if (getStyles() != null) {
                Object obj = getStyles().get(Constants.Name.BOX_SHADOW);
                Object obj2 = getAttrs().get(Constants.Name.SHADOW_QUALITY);
                if (obj != null) {
                    T t = this.mHost;
                    if (this instanceof WXVContainer) {
                        t = ((WXVContainer) this).getBoxShadowHost(false);
                    }
                    if (t != null) {
                        float floatValue = WXUtils.getFloat(obj2, Float.valueOf(0.5f)).floatValue();
                        int instanceViewPortWidth = getInstance().getInstanceViewPortWidth();
                        String str = obj.toString() + " / [" + t.getMeasuredWidth() + "," + t.getMeasuredHeight() + "] / " + floatValue;
                        if (this.mLastBoxShadowId == null || !this.mLastBoxShadowId.equals(str)) {
                            float[] fArr = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
                            WXStyle styles = getStyles();
                            if (styles != null) {
                                float floatValue2 = WXUtils.getFloat(styles.get("borderTopLeftRadius"), Float.valueOf(0.0f)).floatValue();
                                fArr[0] = floatValue2;
                                fArr[1] = floatValue2;
                                float floatValue3 = WXUtils.getFloat(styles.get("borderTopRightRadius"), Float.valueOf(0.0f)).floatValue();
                                fArr[2] = floatValue3;
                                fArr[3] = floatValue3;
                                float floatValue4 = WXUtils.getFloat(styles.get("borderBottomRightRadius"), Float.valueOf(0.0f)).floatValue();
                                fArr[4] = floatValue4;
                                fArr[5] = floatValue4;
                                float floatValue5 = WXUtils.getFloat(styles.get("borderBottomLeftRadius"), Float.valueOf(0.0f)).floatValue();
                                fArr[6] = floatValue5;
                                fArr[7] = floatValue5;
                                if (styles.containsKey("borderRadius")) {
                                    float floatValue6 = WXUtils.getFloat(styles.get("borderRadius"), Float.valueOf(0.0f)).floatValue();
                                    for (int i = 0; i < fArr.length; i++) {
                                        fArr[i] = floatValue6;
                                    }
                                }
                            }
                            BoxShadowUtil.setBoxShadow(t, obj.toString(), fArr, instanceViewPortWidth, floatValue);
                            this.mLastBoxShadowId = str;
                            return;
                        }
                        WXLogUtils.d("BoxShadow", "box-shadow style was not modified. " + str);
                        return;
                    }
                    return;
                }
                return;
            }
            WXLogUtils.w("Can not resolve styles");
        }
    }

    /* access modifiers changed from: protected */
    public void clearBoxShadow() {
        ViewOverlay overlay;
        if (BoxShadowUtil.isBoxShadowEnabled()) {
            if (getStyles() == null || getStyles().get(Constants.Name.BOX_SHADOW) != null) {
                T t = this.mHost;
                if (this instanceof WXVContainer) {
                    t = ((WXVContainer) this).getBoxShadowHost(true);
                }
                if (!(t == null || Build.VERSION.SDK_INT < 18 || (overlay = t.getOverlay()) == null)) {
                    overlay.clear();
                }
                this.mLastBoxShadowId = null;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishLayout() {
        Object obj = getStyles() != null ? getStyles().get("backgroundImage") : null;
        if (obj != null) {
            setBackgroundImage(obj.toString());
        }
    }

    /* access modifiers changed from: protected */
    public MeasureOutput measure(int i, int i2) {
        MeasureOutput measureOutput = new MeasureOutput();
        if (this.mFixedProp != 0) {
            measureOutput.width = this.mFixedProp;
            measureOutput.height = this.mFixedProp;
        } else {
            measureOutput.width = i;
            measureOutput.height = i2;
        }
        return measureOutput;
    }

    /* access modifiers changed from: protected */
    @TargetApi(16)
    public void setAriaHidden(boolean z) {
        View hostView = getHostView();
        if (hostView != null && Build.VERSION.SDK_INT >= 16) {
            hostView.setImportantForAccessibility(z ? 2 : 1);
        }
    }

    /* access modifiers changed from: protected */
    public void setAriaLabel(String str) {
        View hostView = getHostView();
        if (hostView != null) {
            hostView.setContentDescription(str);
        }
    }

    /* access modifiers changed from: protected */
    public void setRole(final String str) {
        View hostView = getHostView();
        if (hostView != null && !TextUtils.isEmpty(str)) {
            IWXAccessibilityRoleAdapter accessibilityRoleAdapter = WXSDKManager.getInstance().getAccessibilityRoleAdapter();
            if (accessibilityRoleAdapter != null) {
                str = accessibilityRoleAdapter.getRole(str);
            }
            ViewCompat.setAccessibilityDelegate(hostView, new AccessibilityDelegateCompat() {
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    try {
                        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                        accessibilityNodeInfoCompat.setRoleDescription(str);
                    } catch (Throwable unused) {
                        WXLogUtils.e("SetRole failed!");
                    }
                }
            });
        }
    }

    private void setFixedSize(String str) {
        ViewGroup.LayoutParams layoutParams;
        if (PROP_FS_MATCH_PARENT.equals(str)) {
            this.mFixedProp = -1;
        } else if (PROP_FS_WRAP_CONTENT.equals(str)) {
            this.mFixedProp = -2;
        } else {
            this.mFixedProp = 0;
            return;
        }
        if (this.mHost != null && (layoutParams = this.mHost.getLayoutParams()) != null) {
            layoutParams.height = this.mFixedProp;
            layoutParams.width = this.mFixedProp;
            this.mHost.setLayoutParams(layoutParams);
        }
    }

    public View getRealView() {
        return this.mHost;
    }

    private boolean needGestureDetector(String str) {
        if (this.mHost != null) {
            for (WXGestureType.LowLevelGesture obj : WXGestureType.LowLevelGesture.values()) {
                if (str.equals(obj.toString())) {
                    return true;
                }
            }
            for (WXGestureType.HighLevelGesture obj2 : WXGestureType.HighLevelGesture.values()) {
                if (str.equals(obj2.toString())) {
                    return true;
                }
            }
        }
        return WXGesture.isStopPropagation(str);
    }

    public Scrollable getParentScroller() {
        boolean equals;
        WXVContainer wXVContainer = this;
        do {
            WXVContainer parent = wXVContainer.getParent();
            if (parent == null) {
                return null;
            }
            if (parent instanceof Scrollable) {
                return (Scrollable) parent;
            }
            equals = parent.getRef().equals(ROOT);
            wXVContainer = parent;
        } while (!equals);
        return null;
    }

    @Nullable
    public Scrollable getFirstScroller() {
        if (this instanceof Scrollable) {
            return (Scrollable) this;
        }
        return null;
    }

    public WXVContainer getParent() {
        return this.mParent;
    }

    public final void createView() {
        if (!isLazy()) {
            createViewImpl();
        }
    }

    /* access modifiers changed from: protected */
    public void createViewImpl() {
        if (this.mContext != null) {
            this.mHost = initComponentHostView(this.mContext);
            if (this.mHost == null && !isVirtualComponent()) {
                initView();
            }
            if (this.mHost != null) {
                if (this.mHost.getId() == -1) {
                    this.mHost.setId(WXViewUtils.generateViewId());
                }
                if (TextUtils.isEmpty(this.mHost.getContentDescription()) && WXEnvironment.isApkDebugable()) {
                    this.mHost.setContentDescription(getRef());
                }
                ComponentObserver componentObserver = getInstance().getComponentObserver();
                if (componentObserver != null) {
                    componentObserver.onViewCreated(this, this.mHost);
                }
            }
            onHostViewInitialized(this.mHost);
            return;
        }
        WXLogUtils.e("createViewImpl", "Context is null");
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void initView() {
        if (this.mContext != null) {
            this.mHost = initComponentHostView(this.mContext);
        }
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onHostViewInitialized(T t) {
        if (this.mAnimationHolder != null) {
            this.mAnimationHolder.execute(this.mInstance, this);
        }
        setActiveTouchListener();
    }

    public T getHostView() {
        return this.mHost;
    }

    @Deprecated
    public View getView() {
        return this.mHost;
    }

    public int getAbsoluteY() {
        return this.mAbsoluteY;
    }

    public int getAbsoluteX() {
        return this.mAbsoluteX;
    }

    public void removeEvent(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (str.equals(Constants.Event.LAYEROVERFLOW)) {
                removeLayerOverFlowListener(getRef());
            }
            if (getEvents() != null) {
                getEvents().remove(str);
            }
            if (this.mAppendEvents != null) {
                this.mAppendEvents.remove(str);
            }
            if (this.mGestureType != null) {
                this.mGestureType.remove(str);
            }
            removeEventFromView(str);
        }
    }

    /* access modifiers changed from: protected */
    public void removeEventFromView(String str) {
        if (!(!str.equals("click") || getRealView() == null || this.mHostClickListeners == null)) {
            if (this.mClickEventListener == null) {
                this.mClickEventListener = new OnClickListenerImp();
            }
            this.mHostClickListeners.remove(this.mClickEventListener);
        }
        Scrollable parentScroller = getParentScroller();
        if (str.equals(Constants.Event.APPEAR) && parentScroller != null) {
            parentScroller.unbindAppearEvent(this);
        }
        if (str.equals(Constants.Event.DISAPPEAR) && parentScroller != null) {
            parentScroller.unbindDisappearEvent(this);
        }
    }

    public void removeAllEvent() {
        if (getEvents().size() >= 1) {
            WXEvent events = getEvents();
            int size = events.size();
            int i = 0;
            while (i < size && i < events.size()) {
                String str = (String) events.get(i);
                if (str != null) {
                    removeEventFromView(str);
                }
                i++;
            }
            if (this.mAppendEvents != null) {
                this.mAppendEvents.clear();
            }
            if (this.mGestureType != null) {
                this.mGestureType.clear();
            }
            this.mGesture = null;
            if (getRealView() != null && (getRealView() instanceof WXGestureObservable)) {
                ((WXGestureObservable) getRealView()).registerGestureListener((WXGesture) null);
            }
            if (this.mHost != null) {
                this.mHost.setOnFocusChangeListener((View.OnFocusChangeListener) null);
                if (this.mHostClickListeners != null && this.mHostClickListeners.size() > 0) {
                    this.mHostClickListeners.clear();
                    this.mHost.setOnClickListener((View.OnClickListener) null);
                }
            }
        }
    }

    public final void removeStickyStyle() {
        Scrollable parentScroller;
        if (isSticky() && (parentScroller = getParentScroller()) != null) {
            parentScroller.unbindStickStyle(this);
        }
    }

    public boolean isSticky() {
        return getStyles().isSticky();
    }

    public boolean isFixed() {
        return getStyles().isFixed();
    }

    public void setDisabled(boolean z) {
        this.mIsDisabled = z;
        if (this.mHost != null) {
            this.mHost.setEnabled(!z);
        }
    }

    public boolean isDisabled() {
        return this.mIsDisabled;
    }

    public void setSticky(String str) {
        Scrollable parentScroller;
        if (!TextUtils.isEmpty(str) && str.equals("sticky") && (parentScroller = getParentScroller()) != null) {
            parentScroller.bindStickStyle(this);
        }
    }

    public void setBackgroundColor(String str) {
        if (!TextUtils.isEmpty(str)) {
            int color = WXResourceUtils.getColor(str);
            if (isRippleEnabled() && Build.VERSION.SDK_INT >= 21) {
                this.mRippleBackground = prepareBackgroundRipple();
                if (this.mRippleBackground != null) {
                    if (this.mBackgroundDrawable == null) {
                        WXViewUtils.setBackGround(this.mHost, this.mRippleBackground, this);
                        return;
                    }
                    WXViewUtils.setBackGround(this.mHost, new LayerDrawable(new Drawable[]{this.mRippleBackground, this.mBackgroundDrawable}), this);
                    return;
                }
            }
            if (color != 0 || this.mBackgroundDrawable != null) {
                getOrCreateBorder().setColor(color);
            }
        }
    }

    private Drawable prepareBackgroundRipple() {
        int i;
        try {
            if (!(getStyles() == null || getStyles().getPesudoResetStyles() == null)) {
                Map<String, Object> pesudoResetStyles = getStyles().getPesudoResetStyles();
                Object obj = pesudoResetStyles.get("backgroundColor");
                if (obj != null) {
                    i = WXResourceUtils.getColor(obj.toString(), 0);
                    if (i == 0) {
                        return null;
                    }
                } else {
                    i = 0;
                }
                Object obj2 = pesudoResetStyles.get("backgroundColor:active");
                if (obj2 == null) {
                    return null;
                }
                int color = WXResourceUtils.getColor(obj2.toString(), i);
                if (Build.VERSION.SDK_INT >= 21) {
                    return new RippleDrawable(new ColorStateList(new int[][]{new int[0]}, new int[]{color}), new ColorDrawable(i), (Drawable) null) {
                        @SuppressLint({"CanvasSize"})
                        public void draw(@NonNull Canvas canvas) {
                            if (WXComponent.this.mBackgroundDrawable != null) {
                                canvas.clipPath(WXComponent.this.mBackgroundDrawable.getContentPath(new RectF(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight())));
                            }
                            super.draw(canvas);
                        }
                    };
                }
            }
        } catch (Throwable th) {
            WXLogUtils.w("Exception on create ripple: ", th);
        }
        return null;
    }

    public void setBackgroundImage(@NonNull String str) {
        if ("".equals(str.trim())) {
            getOrCreateBorder().setImage((Shader) null);
            return;
        }
        getOrCreateBorder().setImage(WXResourceUtils.getShader(str, getLayoutSize().getWidth(), getLayoutSize().getHeight()));
    }

    private boolean shouldCancelHardwareAccelerate() {
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        boolean z = true;
        if (wxConfigAdapter != null) {
            try {
                z = Boolean.parseBoolean(wxConfigAdapter.getConfig("android_weex_test_gpu", "cancel_hardware_accelerate", "true"));
            } catch (Exception e) {
                WXLogUtils.e(WXLogUtils.getStackTrace(e));
            }
            WXLogUtils.i("cancel_hardware_accelerate : " + z);
        }
        return z;
    }

    public void setOpacity(float f) {
        if (f >= 0.0f && f <= 1.0f && this.mHost.getAlpha() != f) {
            int openGLRenderLimitValue = WXRenderManager.getOpenGLRenderLimitValue();
            if (isLayerTypeEnabled()) {
                this.mHost.setLayerType(2, (Paint) null);
            }
            if (isLayerTypeEnabled() && shouldCancelHardwareAccelerate() && openGLRenderLimitValue > 0) {
                float f2 = (float) openGLRenderLimitValue;
                if (getLayoutHeight() > f2 || getLayoutWidth() > f2) {
                    this.mHost.setLayerType(0, (Paint) null);
                }
            }
            this.mHost.setAlpha(f);
        }
    }

    public void setBorderRadius(String str, float f) {
        if (f >= 0.0f) {
            char c = 65535;
            switch (str.hashCode()) {
                case -1228066334:
                    if (str.equals("borderTopLeftRadius")) {
                        c = 1;
                        break;
                    }
                    break;
                case 333432965:
                    if (str.equals("borderTopRightRadius")) {
                        c = 2;
                        break;
                    }
                    break;
                case 581268560:
                    if (str.equals("borderBottomLeftRadius")) {
                        c = 4;
                        break;
                    }
                    break;
                case 588239831:
                    if (str.equals("borderBottomRightRadius")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1349188574:
                    if (str.equals("borderRadius")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    getOrCreateBorder().setBorderRadius(CSSShorthand.CORNER.ALL, WXViewUtils.getRealSubPxByWidth(f, this.mInstance.getInstanceViewPortWidth()));
                    return;
                case 1:
                    getOrCreateBorder().setBorderRadius(CSSShorthand.CORNER.BORDER_TOP_LEFT, WXViewUtils.getRealSubPxByWidth(f, this.mInstance.getInstanceViewPortWidth()));
                    return;
                case 2:
                    getOrCreateBorder().setBorderRadius(CSSShorthand.CORNER.BORDER_TOP_RIGHT, WXViewUtils.getRealSubPxByWidth(f, this.mInstance.getInstanceViewPortWidth()));
                    return;
                case 3:
                    getOrCreateBorder().setBorderRadius(CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT, WXViewUtils.getRealSubPxByWidth(f, this.mInstance.getInstanceViewPortWidth()));
                    return;
                case 4:
                    getOrCreateBorder().setBorderRadius(CSSShorthand.CORNER.BORDER_BOTTOM_LEFT, WXViewUtils.getRealSubPxByWidth(f, this.mInstance.getInstanceViewPortWidth()));
                    return;
                default:
                    return;
            }
        }
    }

    public void setBorderWidth(String str, float f) {
        if (f >= 0.0f) {
            char c = 65535;
            switch (str.hashCode()) {
                case -1971292586:
                    if (str.equals("borderRightWidth")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1452542531:
                    if (str.equals("borderTopWidth")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1290574193:
                    if (str.equals("borderBottomWidth")) {
                        c = 3;
                        break;
                    }
                    break;
                case -223992013:
                    if (str.equals("borderLeftWidth")) {
                        c = 4;
                        break;
                    }
                    break;
                case 741115130:
                    if (str.equals("borderWidth")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    getOrCreateBorder().setBorderWidth(CSSShorthand.EDGE.ALL, f);
                    return;
                case 1:
                    getOrCreateBorder().setBorderWidth(CSSShorthand.EDGE.TOP, f);
                    return;
                case 2:
                    getOrCreateBorder().setBorderWidth(CSSShorthand.EDGE.RIGHT, f);
                    return;
                case 3:
                    getOrCreateBorder().setBorderWidth(CSSShorthand.EDGE.BOTTOM, f);
                    return;
                case 4:
                    getOrCreateBorder().setBorderWidth(CSSShorthand.EDGE.LEFT, f);
                    return;
                default:
                    return;
            }
        }
    }

    public void setBorderStyle(String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            char c = 65535;
            switch (str.hashCode()) {
                case -1974639039:
                    if (str.equals("borderRightStyle")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1455888984:
                    if (str.equals("borderTopStyle")) {
                        c = 4;
                        break;
                    }
                    break;
                case -1293920646:
                    if (str.equals("borderBottomStyle")) {
                        c = 2;
                        break;
                    }
                    break;
                case -227338466:
                    if (str.equals("borderLeftStyle")) {
                        c = 3;
                        break;
                    }
                    break;
                case 737768677:
                    if (str.equals("borderStyle")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    getOrCreateBorder().setBorderStyle(CSSShorthand.EDGE.ALL, str2);
                    return;
                case 1:
                    getOrCreateBorder().setBorderStyle(CSSShorthand.EDGE.RIGHT, str2);
                    return;
                case 2:
                    getOrCreateBorder().setBorderStyle(CSSShorthand.EDGE.BOTTOM, str2);
                    return;
                case 3:
                    getOrCreateBorder().setBorderStyle(CSSShorthand.EDGE.LEFT, str2);
                    return;
                case 4:
                    getOrCreateBorder().setBorderStyle(CSSShorthand.EDGE.TOP, str2);
                    return;
                default:
                    return;
            }
        }
    }

    public void setBorderColor(String str, String str2) {
        int color;
        if (!TextUtils.isEmpty(str2) && (color = WXResourceUtils.getColor(str2)) != Integer.MIN_VALUE) {
            char c = 65535;
            switch (str.hashCode()) {
                case -1989576717:
                    if (str.equals("borderRightColor")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1470826662:
                    if (str.equals("borderTopColor")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1308858324:
                    if (str.equals("borderBottomColor")) {
                        c = 3;
                        break;
                    }
                    break;
                case -242276144:
                    if (str.equals("borderLeftColor")) {
                        c = 4;
                        break;
                    }
                    break;
                case 722830999:
                    if (str.equals("borderColor")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    getOrCreateBorder().setBorderColor(CSSShorthand.EDGE.ALL, color);
                    return;
                case 1:
                    getOrCreateBorder().setBorderColor(CSSShorthand.EDGE.TOP, color);
                    return;
                case 2:
                    getOrCreateBorder().setBorderColor(CSSShorthand.EDGE.RIGHT, color);
                    return;
                case 3:
                    getOrCreateBorder().setBorderColor(CSSShorthand.EDGE.BOTTOM, color);
                    return;
                case 4:
                    getOrCreateBorder().setBorderColor(CSSShorthand.EDGE.LEFT, color);
                    return;
                default:
                    return;
            }
        }
    }

    @Nullable
    public String getVisibility() {
        try {
            return (String) getStyles().get("visibility");
        } catch (Exception unused) {
            return "visible";
        }
    }

    public void setVisibility(String str) {
        View realView = getRealView();
        if (realView == null) {
            return;
        }
        if (TextUtils.equals(str, "visible")) {
            realView.setVisibility(0);
        } else if (TextUtils.equals(str, "hidden")) {
            realView.setVisibility(8);
        }
    }

    private void updateElevation() {
        float elevation = getAttrs().getElevation(getInstance().getInstanceViewPortWidth());
        if (!Float.isNaN(elevation)) {
            ViewCompat.setElevation(getHostView(), elevation);
        }
    }

    public void recycled() {
        if (!isFixed()) {
            clearBoxShadow();
        }
    }

    public void destroy() {
        View hostView;
        ComponentObserver componentObserver = getInstance().getComponentObserver();
        if (componentObserver != null) {
            componentObserver.onPreDestory(this);
        }
        if (!WXEnvironment.isApkDebugable() || WXUtils.isUiThread()) {
            if (this.mHost != null && this.mHost.getLayerType() == 2 && isLayerTypeEnabled()) {
                this.mHost.setLayerType(0, (Paint) null);
            }
            removeAllEvent();
            removeStickyStyle();
            if (isFixed() && (hostView = getHostView()) != null) {
                getInstance().removeFixedView(hostView);
            }
            if (this.contentBoxMeasurement != null) {
                this.contentBoxMeasurement.destroy();
                this.contentBoxMeasurement = null;
            }
            this.mIsDestroyed = true;
            if (this.animations != null) {
                this.animations.clear();
                return;
            }
            return;
        }
        throw new WXRuntimeException("[WXComponent] destroy can only be called in main thread");
    }

    public boolean isDestoryed() {
        return this.mIsDestroyed;
    }

    public View detachViewAndClearPreInfo() {
        T t = this.mHost;
        this.mPreRealLeft = 0;
        this.mPreRealRight = 0;
        this.mPreRealWidth = 0;
        this.mPreRealHeight = 0;
        this.mPreRealTop = 0;
        return t;
    }

    public void clearPreLayout() {
        this.mPreRealLeft = 0;
        this.mPreRealRight = 0;
        this.mPreRealWidth = 0;
        this.mPreRealHeight = 0;
        this.mPreRealTop = 0;
    }

    public void computeVisiblePointInViewCoordinate(PointF pointF) {
        View realView = getRealView();
        pointF.set((float) realView.getScrollX(), (float) realView.getScrollY());
    }

    public boolean containsGesture(WXGestureType wXGestureType) {
        return this.mGestureType != null && this.mGestureType.contains(wXGestureType.toString());
    }

    public boolean containsEvent(String str) {
        return getEvents().contains(str) || (this.mAppendEvents != null && this.mAppendEvents.contains(str));
    }

    public void notifyAppearStateChange(String str, String str2) {
        if (containsEvent(Constants.Event.APPEAR) || containsEvent(Constants.Event.DISAPPEAR)) {
            HashMap hashMap = new HashMap();
            hashMap.put("direction", str2);
            fireEvent(str, hashMap);
        }
    }

    public boolean isUsing() {
        return this.isUsing;
    }

    public void setUsing(boolean z) {
        this.isUsing = z;
    }

    public void readyToRender() {
        if (this.mParent != null && getInstance().isTrackComponent()) {
            this.mParent.readyToRender();
        }
    }

    public boolean isVirtualComponent() {
        return this.mType == 1;
    }

    public int getType() {
        return this.mType;
    }

    public boolean hasScrollParent(WXComponent wXComponent) {
        if (wXComponent.getParent() == null) {
            return true;
        }
        if (wXComponent.getParent() instanceof WXScroller) {
            return false;
        }
        return hasScrollParent(wXComponent.getParent());
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    @androidx.annotation.CheckResult
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object convertEmptyProperty(java.lang.String r3, java.lang.Object r4) {
        /*
            r2 = this;
            int r0 = r3.hashCode()
            r1 = 0
            switch(r0) {
                case -1989576717: goto L_0x00ae;
                case -1971292586: goto L_0x00a3;
                case -1470826662: goto L_0x0098;
                case -1452542531: goto L_0x008e;
                case -1308858324: goto L_0x0083;
                case -1290574193: goto L_0x0078;
                case -1228066334: goto L_0x006e;
                case -242276144: goto L_0x0063;
                case -223992013: goto L_0x0058;
                case 333432965: goto L_0x004d;
                case 581268560: goto L_0x0042;
                case 588239831: goto L_0x0037;
                case 722830999: goto L_0x002b;
                case 741115130: goto L_0x0020;
                case 1287124693: goto L_0x0015;
                case 1349188574: goto L_0x000a;
                default: goto L_0x0008;
            }
        L_0x0008:
            goto L_0x00b9
        L_0x000a:
            java.lang.String r0 = "borderRadius"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 1
            goto L_0x00ba
        L_0x0015:
            java.lang.String r0 = "backgroundColor"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 0
            goto L_0x00ba
        L_0x0020:
            java.lang.String r0 = "borderWidth"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 6
            goto L_0x00ba
        L_0x002b:
            java.lang.String r0 = "borderColor"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 11
            goto L_0x00ba
        L_0x0037:
            java.lang.String r0 = "borderBottomRightRadius"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 3
            goto L_0x00ba
        L_0x0042:
            java.lang.String r0 = "borderBottomLeftRadius"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 2
            goto L_0x00ba
        L_0x004d:
            java.lang.String r0 = "borderTopRightRadius"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 5
            goto L_0x00ba
        L_0x0058:
            java.lang.String r0 = "borderLeftWidth"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 8
            goto L_0x00ba
        L_0x0063:
            java.lang.String r0 = "borderLeftColor"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 13
            goto L_0x00ba
        L_0x006e:
            java.lang.String r0 = "borderTopLeftRadius"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 4
            goto L_0x00ba
        L_0x0078:
            java.lang.String r0 = "borderBottomWidth"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 10
            goto L_0x00ba
        L_0x0083:
            java.lang.String r0 = "borderBottomColor"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 15
            goto L_0x00ba
        L_0x008e:
            java.lang.String r0 = "borderTopWidth"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 7
            goto L_0x00ba
        L_0x0098:
            java.lang.String r0 = "borderTopColor"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 12
            goto L_0x00ba
        L_0x00a3:
            java.lang.String r0 = "borderRightWidth"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 9
            goto L_0x00ba
        L_0x00ae:
            java.lang.String r0 = "borderRightColor"
            boolean r3 = r3.equals(r0)
            if (r3 == 0) goto L_0x00b9
            r3 = 14
            goto L_0x00ba
        L_0x00b9:
            r3 = -1
        L_0x00ba:
            switch(r3) {
                case 0: goto L_0x00cb;
                case 1: goto L_0x00c6;
                case 2: goto L_0x00c6;
                case 3: goto L_0x00c6;
                case 4: goto L_0x00c6;
                case 5: goto L_0x00c6;
                case 6: goto L_0x00c1;
                case 7: goto L_0x00c1;
                case 8: goto L_0x00c1;
                case 9: goto L_0x00c1;
                case 10: goto L_0x00c1;
                case 11: goto L_0x00be;
                case 12: goto L_0x00be;
                case 13: goto L_0x00be;
                case 14: goto L_0x00be;
                case 15: goto L_0x00be;
                default: goto L_0x00bd;
            }
        L_0x00bd:
            return r4
        L_0x00be:
            java.lang.String r3 = "black"
            return r3
        L_0x00c1:
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)
            return r3
        L_0x00c6:
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)
            return r3
        L_0x00cb:
            java.lang.String r3 = "transparent"
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXComponent.convertEmptyProperty(java.lang.String, java.lang.Object):java.lang.Object");
    }

    private void setActiveTouchListener() {
        View realView;
        if (getStyles().getPesudoStyles().containsKey(Constants.PSEUDO.ACTIVE) && (realView = getRealView()) != null) {
            realView.setOnTouchListener(new TouchActivePseudoListener(this, !isConsumeTouch()));
        }
    }

    /* access modifiers changed from: protected */
    public boolean isConsumeTouch() {
        return (this.mHostClickListeners != null && this.mHostClickListeners.size() > 0) || this.mGesture != null;
    }

    public void updateActivePseudo(boolean z) {
        setPseudoClassStatus(Constants.PSEUDO.ACTIVE, z);
    }

    /* access modifiers changed from: protected */
    public void setPseudoClassStatus(String str, boolean z) {
        WXStyle styles = getStyles();
        Map<String, Map<String, Object>> pesudoStyles = styles.getPesudoStyles();
        if (pesudoStyles != null && pesudoStyles.size() != 0) {
            if (this.mPesudoStatus == null) {
                this.mPesudoStatus = new PesudoStatus();
            }
            Map<String, Object> updateStatusAndGetUpdateStyles = this.mPesudoStatus.updateStatusAndGetUpdateStyles(str, z, pesudoStyles, styles.getPesudoResetStyles());
            if (updateStatusAndGetUpdateStyles != null) {
                if (z) {
                    this.mPseudoResetGraphicSize = new GraphicSize(getLayoutSize().getWidth(), getLayoutSize().getHeight());
                    if (updateStatusAndGetUpdateStyles.keySet().contains("width")) {
                        getLayoutSize().setWidth(WXViewUtils.getRealPxByWidth(WXUtils.parseFloat(styles.getPesudoResetStyles().get("width:active")), getViewPortWidth()));
                    } else if (updateStatusAndGetUpdateStyles.keySet().contains("height")) {
                        getLayoutSize().setHeight(WXViewUtils.getRealPxByWidth(WXUtils.parseFloat(styles.getPesudoResetStyles().get("height:active")), getViewPortWidth()));
                    }
                } else if (this.mPseudoResetGraphicSize != null) {
                    setLayoutSize(this.mPseudoResetGraphicSize);
                }
            }
            updateStyleByPesudo(updateStatusAndGetUpdateStyles);
        }
    }

    private void updateStyleByPesudo(Map<String, Object> map) {
        new GraphicActionUpdateStyle(getInstance(), getRef(), map, getPadding(), getMargin(), getBorder(), true).executeActionOnRender();
        if (getLayoutWidth() != 0.0f || getLayoutWidth() != 0.0f) {
            WXBridgeManager.getInstance().setStyleWidth(getInstanceId(), getRef(), getLayoutWidth());
            WXBridgeManager.getInstance().setStyleHeight(getInstanceId(), getRef(), getLayoutHeight());
        }
    }

    public int getStickyOffset() {
        return this.mStickyOffset;
    }

    public boolean canRecycled() {
        return (!isFixed() || !isSticky()) && getAttrs().canRecycled();
    }

    public void setStickyOffset(int i) {
        this.mStickyOffset = i;
    }

    public boolean isLayerTypeEnabled() {
        return getInstance().isLayerTypeEnabled();
    }

    public void setNeedLayoutOnAnimation(boolean z) {
        this.mNeedLayoutOnAnimation = z;
    }

    public void notifyNativeSizeChanged(int i, int i2) {
        if (this.mNeedLayoutOnAnimation) {
            WXBridgeManager instance = WXBridgeManager.getInstance();
            instance.setStyleWidth(getInstanceId(), getRef(), (float) i);
            instance.setStyleHeight(getInstanceId(), getRef(), (float) i2);
        }
    }

    @CallSuper
    public void onRenderFinish(int i) {
        if (WXTracing.isAvailable()) {
            double nanosToMillis = Stopwatch.nanosToMillis(this.mTraceInfo.uiThreadNanos);
            if (i == 2 || i == 0) {
                WXTracing.TraceEvent newEvent = WXTracing.newEvent("DomExecute", getInstanceId(), this.mTraceInfo.rootEventId);
                newEvent.ph = "X";
                newEvent.ts = this.mTraceInfo.domThreadStart;
                newEvent.tname = "DOMThread";
                newEvent.name = getComponentType();
                newEvent.classname = getClass().getSimpleName();
                if (getParent() != null) {
                    newEvent.parentRef = getParent().getRef();
                }
                newEvent.submit();
            }
            if (i != 2 && i != 1) {
                return;
            }
            if (this.mTraceInfo.uiThreadStart != -1) {
                WXTracing.TraceEvent newEvent2 = WXTracing.newEvent("UIExecute", getInstanceId(), this.mTraceInfo.rootEventId);
                newEvent2.ph = "X";
                newEvent2.duration = nanosToMillis;
                newEvent2.ts = this.mTraceInfo.uiThreadStart;
                newEvent2.name = getComponentType();
                newEvent2.classname = getClass().getSimpleName();
                if (getParent() != null) {
                    newEvent2.parentRef = getParent().getRef();
                }
                newEvent2.submit();
            } else if (WXEnvironment.isApkDebugable()) {
                isLazy();
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isRippleEnabled() {
        try {
            return WXUtils.getBoolean(getAttrs().get(Constants.Name.RIPPLE_ENABLED), false).booleanValue();
        } catch (Throwable unused) {
            return false;
        }
    }

    public boolean isWaste() {
        return this.waste;
    }

    public void setWaste(boolean z) {
        if (this.waste != z) {
            this.waste = z;
            if (!WXBasicComponentType.RECYCLE_LIST.equals(getParent().getComponentType())) {
                NativeRenderObjectUtils.nativeRenderObjectChildWaste(getRenderObjectPtr(), z);
            }
            if (z) {
                getStyles().put("visibility", (Object) "hidden");
                if (getHostView() != null) {
                    getHostView().setVisibility(8);
                } else if (!this.mLazy) {
                    lazy(true);
                }
            } else {
                getStyles().put("visibility", (Object) "visible");
                if (getHostView() != null) {
                    getHostView().setVisibility(0);
                } else if (!this.mLazy) {
                } else {
                    if (this.mParent == null || !this.mParent.isLazy()) {
                        Statements.initLazyComponent(this, this.mParent);
                    } else {
                        lazy(false);
                    }
                }
            }
        }
    }

    public String getViewTreeKey() {
        if (this.mViewTreeKey == null) {
            if (getParent() == null) {
                this.mViewTreeKey = hashCode() + "_" + getRef();
            } else {
                this.mViewTreeKey = hashCode() + "_" + getRef() + "_" + getParent().indexOf(this);
            }
        }
        return this.mViewTreeKey;
    }

    public WXTransition getTransition() {
        return this.mTransition;
    }

    public void setTransition(WXTransition wXTransition) {
        this.mTransition = wXTransition;
    }

    public void addAnimationForElement(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            if (this.animations == null) {
                this.animations = new ConcurrentLinkedQueue<>();
            }
            this.animations.add(new Pair(getRef(), map));
        }
    }

    private void parseAnimation() {
        WXAnimationBean createAnimationBean;
        if (this.animations != null) {
            Iterator<Pair<String, Map<String, Object>>> it = this.animations.iterator();
            while (it.hasNext()) {
                Pair next = it.next();
                if (!TextUtils.isEmpty((CharSequence) next.first) && (createAnimationBean = createAnimationBean((String) next.first, (Map) next.second)) != null) {
                    new GraphicActionAnimation(getInstance(), getRef(), createAnimationBean).executeAction();
                }
            }
            this.animations.clear();
        }
    }

    private WXAnimationBean createAnimationBean(String str, Map<String, Object> map) {
        if (map != null) {
            try {
                Object obj = map.get("transform");
                if ((obj instanceof String) && !TextUtils.isEmpty((String) obj)) {
                    WXAnimationBean wXAnimationBean = new WXAnimationBean();
                    int layoutWidth = (int) getLayoutWidth();
                    int layoutHeight = (int) getLayoutHeight();
                    wXAnimationBean.styles = new WXAnimationBean.Style();
                    wXAnimationBean.styles.init((String) map.get(Constants.Name.TRANSFORM_ORIGIN), (String) obj, layoutWidth, layoutHeight, WXSDKManager.getInstanceViewPortWidth(getInstanceId()), getInstance());
                    return wXAnimationBean;
                }
            } catch (RuntimeException e) {
                WXLogUtils.e("", (Throwable) e);
                return null;
            }
        }
        return null;
    }

    public void lazy(boolean z) {
        this.mLazy = z;
    }

    public long getRenderObjectPtr() {
        if (getBasicComponentData().isRenderPtrEmpty()) {
            getBasicComponentData().setRenderObjectPr(NativeRenderObjectUtils.nativeGetRenderObject(getInstanceId(), getRef()));
        }
        return getBasicComponentData().getRenderObjectPr();
    }

    public void updateNativeAttr(String str, Object obj) {
        if (str != null) {
            if (obj == null) {
                obj = "";
            }
            getBasicComponentData().getAttrs().put(str, obj);
            NativeRenderObjectUtils.nativeUpdateRenderObjectAttr(getRenderObjectPtr(), str, obj.toString());
        }
    }

    public void nativeUpdateAttrs(Map<String, Object> map) {
        for (Map.Entry next : map.entrySet()) {
            if (next.getKey() != null) {
                updateNativeAttr((String) next.getKey(), next.getValue());
            }
        }
    }

    public void updateNativeStyle(String str, Object obj) {
        if (str != null) {
            if (obj == null) {
                obj = "";
            }
            getBasicComponentData().getStyles().put(str, obj);
            NativeRenderObjectUtils.nativeUpdateRenderObjectStyle(getRenderObjectPtr(), str, obj.toString());
        }
    }

    public void updateNativeStyles(Map<String, Object> map) {
        for (Map.Entry next : map.entrySet()) {
            if (next.getKey() != null) {
                updateNativeStyle((String) next.getKey(), next.getValue());
            }
        }
    }

    public void addLayerOverFlowListener(String str) {
        if (getInstance() != null) {
            getInstance().addLayerOverFlowListener(str);
        }
    }

    public void removeLayerOverFlowListener(String str) {
        if (getInstance() != null) {
            getInstance().removeLayerOverFlowListener(str);
        }
    }
}
