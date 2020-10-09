package com.taobao.weex.ui.component.list;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.ICheckBindingScroller;
import com.taobao.weex.common.OnWXScrollListener;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.AppearanceHelper;
import com.taobao.weex.ui.component.Scrollable;
import com.taobao.weex.ui.component.WXBaseRefresh;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXHeader;
import com.taobao.weex.ui.component.WXLoading;
import com.taobao.weex.ui.component.WXRefresh;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.helper.ScrollStartEndHelper;
import com.taobao.weex.ui.component.helper.WXStickyHelper;
import com.taobao.weex.ui.component.list.ListComponentView;
import com.taobao.weex.ui.view.listview.WXRecyclerView;
import com.taobao.weex.ui.view.listview.adapter.IOnLoadMoreListener;
import com.taobao.weex.ui.view.listview.adapter.IRecyclerAdapterListener;
import com.taobao.weex.ui.view.listview.adapter.ListBaseViewHolder;
import com.taobao.weex.ui.view.listview.adapter.RecyclerViewBaseAdapter;
import com.taobao.weex.ui.view.listview.adapter.WXRecyclerViewOnScrollListener;
import com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class BasicListComponent<T extends ViewGroup & ListComponentView> extends WXVContainer<T> implements IRecyclerAdapterListener<ListBaseViewHolder>, IOnLoadMoreListener, Scrollable {
    private static final boolean DEFAULT_EXCLUDED = false;
    private static final String DEFAULT_TRIGGER_TYPE = "longpress";
    private static final String DRAG_ANCHOR = "dragAnchor";
    private static final String DRAG_TRIGGER_TYPE = "dragTriggerType";
    private static final String EXCLUDED = "dragExcluded";
    public static final String LOADMOREOFFSET = "loadmoreoffset";
    private static final int MAX_VIEWTYPE_ALLOW_CACHE = 9;
    public static final String TRANSFORM = "transform";
    private static boolean mAllowCacheViewHolder = true;
    private static boolean mDownForBidCacheViewHolder = false;
    private static final Pattern transformPattern = Pattern.compile("([a-z]+)\\(([0-9\\.]+),?([0-9\\.]+)?\\)");
    private String TAG = "BasicListComponent";
    private boolean isScrollable = true;
    /* access modifiers changed from: private */
    public WXComponent keepPositionCell = null;
    /* access modifiers changed from: private */
    public Runnable keepPositionCellRunnable = null;
    private long keepPositionLayoutDelay = 150;
    /* access modifiers changed from: private */
    public Runnable mAppearChangeRunnable = null;
    private long mAppearChangeRunnableDelay = 50;
    private Map<String, AppearanceHelper> mAppearComponents = new HashMap();
    protected int mColumnCount = 1;
    protected float mColumnGap = 0.0f;
    protected float mColumnWidth = 0.0f;
    /* access modifiers changed from: private */
    public DragHelper mDragHelper;
    private boolean mForceLoadmoreNextTime = false;
    private boolean mHasAddScrollEvent = false;
    private RecyclerView.ItemAnimator mItemAnimator;
    private Point mLastReport = new Point(-1, -1);
    protected int mLayoutType = 1;
    protected float mLeftGap = 0.0f;
    private int mListCellCount = 0;
    private int mOffsetAccuracy = 10;
    private ArrayMap<String, Long> mRefToViewType;
    protected float mRightGap = 0.0f;
    private ScrollStartEndHelper mScrollStartEndHelper;
    private Map<String, Map<String, WXComponent>> mStickyMap = new HashMap();
    private String mTriggerType;
    /* access modifiers changed from: private */
    public WXRecyclerViewOnScrollListener mViewOnScrollListener = new WXRecyclerViewOnScrollListener(this);
    private SparseArray<ArrayList<WXComponent>> mViewTypes;
    private WXStickyHelper stickyHelper = new WXStickyHelper(this);

    interface DragTriggerType {
        public static final String LONG_PRESS = "longpress";
        public static final String PAN = "pan";
    }

    public void addSubView(View view, int i) {
    }

    /* access modifiers changed from: package-private */
    public abstract T generateListView(Context context, int i);

    /* access modifiers changed from: protected */
    public int getChildrenLayoutTopOffset() {
        return 0;
    }

    public int getOrientation() {
        return 1;
    }

    public BasicListComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    @SuppressLint({"RtlHardcoded"})
    public void setMarginsSupportRTL(ViewGroup.MarginLayoutParams marginLayoutParams, int i, int i2, int i3, int i4) {
        if (Build.VERSION.SDK_INT >= 17) {
            marginLayoutParams.setMargins(i, i2, i3, i4);
            marginLayoutParams.setMarginStart(i);
            marginLayoutParams.setMarginEnd(i3);
        } else if (marginLayoutParams instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) marginLayoutParams;
            if (isLayoutRTL()) {
                layoutParams.gravity = 53;
                marginLayoutParams.setMargins(i3, i2, i, i4);
                return;
            }
            layoutParams.gravity = 51;
            marginLayoutParams.setMargins(i, i2, i3, i4);
        } else {
            marginLayoutParams.setMargins(i, i2, i3, i4);
        }
    }

    public void setLayout(WXComponent wXComponent) {
        if (wXComponent.getHostView() != null) {
            ViewCompat.setLayoutDirection(wXComponent.getHostView(), wXComponent.isLayoutRTL() ? 1 : 0);
        }
        super.setLayout(wXComponent);
    }

    /* access modifiers changed from: protected */
    public void onHostViewInitialized(T t) {
        super.onHostViewInitialized(t);
        WXRecyclerView innerView = ((ListComponentView) t).getInnerView();
        if (innerView == null || innerView.getAdapter() == null) {
            WXLogUtils.e(this.TAG, "RecyclerView is not found or Adapter is not bound");
            return;
        }
        if (WXUtils.getBoolean(getAttrs().get("prefetchGapDisable"), false).booleanValue() && innerView.getLayoutManager() != null) {
            innerView.getLayoutManager().setItemPrefetchEnabled(false);
        }
        if (this.mChildren == null) {
            WXLogUtils.e(this.TAG, "children is null");
            return;
        }
        this.mDragHelper = new DefaultDragHelper(this.mChildren, innerView, new EventTrigger() {
            public void triggerEvent(String str, Map<String, Object> map) {
                BasicListComponent.this.fireEvent(str, map);
            }
        });
        this.mTriggerType = getTriggerType(this);
    }

    /* access modifiers changed from: protected */
    public WXComponent.MeasureOutput measure(int i, int i2) {
        int screenHeight = WXViewUtils.getScreenHeight(getInstanceId());
        int weexHeight = WXViewUtils.getWeexHeight(getInstanceId());
        if (weexHeight < screenHeight) {
            screenHeight = weexHeight;
        }
        if (i2 > screenHeight) {
            i2 = weexHeight - getAbsoluteY();
        }
        return super.measure(i, i2);
    }

    public void destroy() {
        if (!(this.mAppearChangeRunnable == null || getHostView() == null)) {
            ((ViewGroup) getHostView()).removeCallbacks(this.mAppearChangeRunnable);
            this.mAppearChangeRunnable = null;
        }
        super.destroy();
        if (this.mStickyMap != null) {
            this.mStickyMap.clear();
        }
        if (this.mViewTypes != null) {
            this.mViewTypes.clear();
        }
        if (this.mRefToViewType != null) {
            this.mRefToViewType.clear();
        }
    }

    public ViewGroup.LayoutParams getChildLayoutParams(WXComponent wXComponent, View view, int i, int i2, int i3, int i4, int i5, int i6) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if ((wXComponent instanceof WXBaseRefresh) && marginLayoutParams == null) {
            return new LinearLayout.LayoutParams(i, i2);
        }
        if (marginLayoutParams == null) {
            return new RecyclerView.LayoutParams(i, i2);
        }
        marginLayoutParams.width = i;
        marginLayoutParams.height = i2;
        setMarginsSupportRTL(marginLayoutParams, i3, 0, i4, 0);
        return marginLayoutParams;
    }

    /* access modifiers changed from: protected */
    public T initComponentHostView(@NonNull Context context) {
        T generateListView = generateListView(context, getOrientation());
        String attrByKey = getAttrByKey("transform");
        if (attrByKey != null) {
            ((ListComponentView) generateListView).getInnerView().addItemDecoration(RecyclerTransform.parseTransforms(getOrientation(), attrByKey));
        }
        if (getAttrs().get(Constants.Name.KEEP_POSITION_LAYOUT_DELAY) != null) {
            this.keepPositionLayoutDelay = (long) WXUtils.getNumberInt(getAttrs().get(Constants.Name.KEEP_POSITION_LAYOUT_DELAY), (int) this.keepPositionLayoutDelay);
        }
        if (getAttrs().get("appearActionDelay") != null) {
            this.mAppearChangeRunnableDelay = (long) WXUtils.getNumberInt(getAttrs().get("appearActionDelay"), (int) this.mAppearChangeRunnableDelay);
        }
        ListComponentView listComponentView = (ListComponentView) generateListView;
        this.mItemAnimator = listComponentView.getInnerView().getItemAnimator();
        RecyclerViewBaseAdapter recyclerViewBaseAdapter = new RecyclerViewBaseAdapter(this);
        recyclerViewBaseAdapter.setHasStableIds(true);
        listComponentView.setRecyclerViewBaseAdapter(recyclerViewBaseAdapter);
        generateListView.setOverScrollMode(2);
        listComponentView.getInnerView().addOnScrollListener(this.mViewOnScrollListener);
        if (getAttrs().get(Constants.Name.HAS_FIXED_SIZE) != null) {
            listComponentView.getInnerView().setHasFixedSize(WXUtils.getBoolean(getAttrs().get(Constants.Name.HAS_FIXED_SIZE), false).booleanValue());
        }
        listComponentView.getInnerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                int size;
                View childAt;
                super.onScrollStateChanged(recyclerView, i);
                BasicListComponent.this.getScrollStartEndHelper().onScrollStateChanged(i);
                List<OnWXScrollListener> wXScrollListeners = BasicListComponent.this.getInstance().getWXScrollListeners();
                if (wXScrollListeners != null && (size = wXScrollListeners.size()) > 0) {
                    int i2 = 0;
                    while (i2 < size && i2 < wXScrollListeners.size()) {
                        OnWXScrollListener onWXScrollListener = wXScrollListeners.get(i2);
                        if (!(onWXScrollListener == null || (childAt = recyclerView.getChildAt(0)) == null)) {
                            onWXScrollListener.onScrollStateChanged(recyclerView, 0, childAt.getTop(), i);
                        }
                        i2++;
                    }
                }
            }

            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                List<OnWXScrollListener> wXScrollListeners = BasicListComponent.this.getInstance().getWXScrollListeners();
                if (wXScrollListeners != null && wXScrollListeners.size() > 0) {
                    try {
                        for (OnWXScrollListener next : wXScrollListeners) {
                            if (next != null) {
                                if (!(next instanceof ICheckBindingScroller)) {
                                    next.onScrolled(recyclerView, i, i2);
                                } else if (((ICheckBindingScroller) next).isNeedScroller(BasicListComponent.this.getRef(), (Object) null)) {
                                    next.onScrolled(recyclerView, i, i2);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        generateListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(16)
            public void onGlobalLayout() {
                ViewGroup viewGroup = (ViewGroup) BasicListComponent.this.getHostView();
                if (viewGroup != null) {
                    BasicListComponent.this.mViewOnScrollListener.onScrolled(((ListComponentView) viewGroup).getInnerView(), 0, 0);
                    if (Build.VERSION.SDK_INT >= 16) {
                        viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        viewGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });
        return generateListView;
    }

    public void bindStickStyle(WXComponent wXComponent) {
        this.stickyHelper.bindStickStyle(wXComponent, this.mStickyMap);
    }

    public void unbindStickStyle(WXComponent wXComponent) {
        this.stickyHelper.unbindStickStyle(wXComponent, this.mStickyMap);
        WXHeader wXHeader = (WXHeader) findTypeParent(wXComponent, WXHeader.class);
        if (wXHeader != null && getHostView() != null) {
            ((ListComponentView) ((ViewGroup) getHostView())).notifyStickyRemove(wXHeader);
        }
    }

    @Nullable
    private WXComponent findDirectListChild(WXComponent wXComponent) {
        WXVContainer parent;
        if (wXComponent == null || (parent = wXComponent.getParent()) == null) {
            return null;
        }
        if (parent instanceof BasicListComponent) {
            return wXComponent;
        }
        return findDirectListChild(parent);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r4, java.lang.Object r5) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = 0
            r2 = 1
            switch(r0) {
                case -304480883: goto L_0x0032;
                case -223520855: goto L_0x0028;
                case -112563826: goto L_0x001e;
                case -5620052: goto L_0x0014;
                case 66669991: goto L_0x000a;
                default: goto L_0x0009;
            }
        L_0x0009:
            goto L_0x003c
        L_0x000a:
            java.lang.String r0 = "scrollable"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003c
            r0 = 1
            goto L_0x003d
        L_0x0014:
            java.lang.String r0 = "offsetAccuracy"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003c
            r0 = 2
            goto L_0x003d
        L_0x001e:
            java.lang.String r0 = "loadmoreoffset"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003c
            r0 = 0
            goto L_0x003d
        L_0x0028:
            java.lang.String r0 = "showScrollbar"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003c
            r0 = 4
            goto L_0x003d
        L_0x0032:
            java.lang.String r0 = "draggable"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003c
            r0 = 3
            goto L_0x003d
        L_0x003c:
            r0 = -1
        L_0x003d:
            switch(r0) {
                case 0: goto L_0x0086;
                case 1: goto L_0x0076;
                case 2: goto L_0x0064;
                case 3: goto L_0x0054;
                case 4: goto L_0x0045;
                default: goto L_0x0040;
            }
        L_0x0040:
            boolean r4 = super.setProperty(r4, r5)
            return r4
        L_0x0045:
            r4 = 0
            java.lang.Boolean r4 = com.taobao.weex.utils.WXUtils.getBoolean(r5, r4)
            if (r4 == 0) goto L_0x0053
            boolean r4 = r4.booleanValue()
            r3.setShowScrollbar(r4)
        L_0x0053:
            return r2
        L_0x0054:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r1)
            java.lang.Boolean r4 = com.taobao.weex.utils.WXUtils.getBoolean(r5, r4)
            boolean r4 = r4.booleanValue()
            r3.setDraggable(r4)
            return r2
        L_0x0064:
            r4 = 10
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            java.lang.Integer r4 = com.taobao.weex.utils.WXUtils.getInteger(r5, r4)
            int r4 = r4.intValue()
            r3.setOffsetAccuracy(r4)
            return r2
        L_0x0076:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r2)
            java.lang.Boolean r4 = com.taobao.weex.utils.WXUtils.getBoolean(r5, r4)
            boolean r4 = r4.booleanValue()
            r3.setScrollable(r4)
            return r2
        L_0x0086:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.list.BasicListComponent.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "scrollable")
    public void setScrollable(boolean z) {
        this.isScrollable = z;
        WXRecyclerView innerView = ((ListComponentView) ((ViewGroup) getHostView())).getInnerView();
        if (innerView != null) {
            innerView.setScrollable(z);
        }
    }

    @WXComponentProp(name = "offsetAccuracy")
    public void setOffsetAccuracy(int i) {
        this.mOffsetAccuracy = (int) WXViewUtils.getRealPxByWidth((float) i, getInstance().getInstanceViewPortWidth());
    }

    @WXComponentProp(name = "draggable")
    public void setDraggable(boolean z) {
        if (this.mDragHelper != null) {
            this.mDragHelper.setDraggable(z);
        }
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("set draggable : " + z);
        }
    }

    @WXComponentProp(name = "showScrollbar")
    public void setShowScrollbar(boolean z) {
        if (getHostView() != null && ((ListComponentView) ((ViewGroup) getHostView())).getInnerView() != null) {
            if (getOrientation() == 1) {
                ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().setVerticalScrollBarEnabled(z);
            } else {
                ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().setHorizontalScrollBarEnabled(z);
            }
        }
    }

    public boolean isScrollable() {
        return this.isScrollable;
    }

    private void setAppearanceWatch(WXComponent wXComponent, int i, boolean z) {
        int indexOf;
        AppearanceHelper appearanceHelper = this.mAppearComponents.get(wXComponent.getRef());
        if (appearanceHelper != null) {
            appearanceHelper.setWatchEvent(i, z);
        } else if (z && (indexOf = this.mChildren.indexOf(findDirectListChild(wXComponent))) != -1) {
            AppearanceHelper appearanceHelper2 = new AppearanceHelper(wXComponent, indexOf);
            appearanceHelper2.setWatchEvent(i, true);
            this.mAppearComponents.put(wXComponent.getRef(), appearanceHelper2);
        }
    }

    public void bindAppearEvent(WXComponent wXComponent) {
        setAppearanceWatch(wXComponent, 0, true);
        if (this.mAppearChangeRunnable == null) {
            this.mAppearChangeRunnable = new Runnable() {
                public void run() {
                    if (BasicListComponent.this.mAppearChangeRunnable != null) {
                        BasicListComponent.this.notifyAppearStateChange(0, 0, 0, 0);
                    }
                }
            };
        }
        if (getHostView() != null) {
            ((ViewGroup) getHostView()).removeCallbacks(this.mAppearChangeRunnable);
            ((ViewGroup) getHostView()).postDelayed(this.mAppearChangeRunnable, this.mAppearChangeRunnableDelay);
        }
    }

    public void bindDisappearEvent(WXComponent wXComponent) {
        setAppearanceWatch(wXComponent, 1, true);
    }

    public void unbindAppearEvent(WXComponent wXComponent) {
        setAppearanceWatch(wXComponent, 0, false);
    }

    public void unbindDisappearEvent(WXComponent wXComponent) {
        setAppearanceWatch(wXComponent, 1, false);
    }

    public void scrollTo(WXComponent wXComponent, Map<String, Object> map) {
        int indexOf;
        boolean z = true;
        float f = 0.0f;
        if (map != null) {
            String obj = map.get("offset") == null ? "0" : map.get("offset").toString();
            z = WXUtils.getBoolean(map.get("animated"), true).booleanValue();
            if (obj != null) {
                try {
                    f = WXViewUtils.getRealPxByWidth(Float.parseFloat(obj), getInstance().getInstanceViewPortWidth());
                } catch (Exception e) {
                    WXLogUtils.e("Float parseFloat error :" + e.getMessage());
                }
            }
        }
        int i = (int) f;
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup != null) {
            WXCell wXCell = null;
            while (true) {
                if (wXComponent == null) {
                    break;
                } else if (wXComponent instanceof WXCell) {
                    wXCell = (WXCell) wXComponent;
                    break;
                } else {
                    wXComponent = wXComponent.getParent();
                }
            }
            if (wXCell != null && (indexOf = this.mChildren.indexOf(wXCell)) != -1) {
                ((ListComponentView) viewGroup).getInnerView().scrollTo(z, indexOf, i, getOrientation());
            }
        }
    }

    public void onBeforeScroll(int i, int i2) {
        Map map;
        int i3;
        boolean z;
        boolean z2;
        boolean z3;
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (this.mStickyMap != null && viewGroup != null && (map = this.mStickyMap.get(getRef())) != null) {
            int i4 = -1;
            for (Map.Entry value : map.entrySet()) {
                WXComponent wXComponent = (WXComponent) value.getValue();
                if (wXComponent != null && (wXComponent instanceof WXCell)) {
                    WXCell wXCell = (WXCell) wXComponent;
                    if (wXCell.getHostView() != null) {
                        int[] iArr = new int[2];
                        wXComponent.getHostView().getLocationOnScreen(iArr);
                        int[] iArr2 = new int[2];
                        wXComponent.getParentScroller().getView().getLocationOnScreen(iArr2);
                        boolean z4 = true;
                        int i5 = iArr[1] - iArr2[1];
                        RecyclerView.LayoutManager layoutManager = ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().getLayoutManager();
                        if ((layoutManager instanceof LinearLayoutManager) || (layoutManager instanceof GridLayoutManager)) {
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                            int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                            int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                            int indexOf = this.mChildren.indexOf(wXCell);
                            wXCell.setScrollPositon(indexOf);
                            if (indexOf <= findFirstVisibleItemPosition || (wXCell.getStickyOffset() > 0 && findFirstVisibleItemPosition < indexOf && indexOf <= findLastVisibleItemPosition && i5 <= wXCell.getStickyOffset())) {
                                if (indexOf <= i4) {
                                    indexOf = i4;
                                }
                                z3 = true;
                                z = false;
                            } else {
                                indexOf = i4;
                                z3 = false;
                                z = true;
                            }
                            i3 = indexOf;
                        } else {
                            if (layoutManager instanceof StaggeredGridLayoutManager) {
                                int[] iArr3 = new int[3];
                                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                                int i6 = staggeredGridLayoutManager.findFirstVisibleItemPositions(iArr3)[0];
                                int i7 = staggeredGridLayoutManager.findLastVisibleItemPositions(iArr3)[0];
                                i3 = this.mChildren.indexOf(wXCell);
                                if (i3 <= i6 || (wXCell.getStickyOffset() > 0 && i6 < i3 && i3 <= i7 && i5 <= wXCell.getStickyOffset())) {
                                    if (i3 <= i4) {
                                        i3 = i4;
                                    }
                                    z2 = true;
                                } else {
                                    i3 = i4;
                                    z2 = false;
                                    z = true;
                                }
                            } else {
                                i3 = i4;
                                z2 = false;
                            }
                            z = false;
                        }
                        boolean z5 = z2 && wXCell.getLocationFromStart() >= 0 && i5 <= wXCell.getStickyOffset() && i2 >= 0;
                        if (wXCell.getLocationFromStart() > wXCell.getStickyOffset() || i5 <= wXCell.getStickyOffset() || i2 > 0) {
                            z4 = false;
                        }
                        if (z5) {
                            ((ListComponentView) viewGroup).notifyStickyShow(wXCell);
                        } else if (z4 || z) {
                            ((ListComponentView) viewGroup).notifyStickyRemove(wXCell);
                        }
                        wXCell.setLocationFromStart(i5);
                        i4 = i3;
                    } else {
                        return;
                    }
                }
            }
            if (i4 >= 0) {
                ((ListComponentView) viewGroup).updateStickyView(i4);
            } else if (viewGroup instanceof BounceRecyclerView) {
                ((BounceRecyclerView) viewGroup).getStickyHeaderHelper().clearStickyHeaders();
            }
        }
    }

    public int getScrollY() {
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup == null) {
            return 0;
        }
        return ((ListComponentView) viewGroup).getInnerView().getScrollY();
    }

    public int getScrollX() {
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup == null) {
            return 0;
        }
        return ((ListComponentView) viewGroup).getInnerView().getScrollX();
    }

    public void addChild(WXComponent wXComponent) {
        addChild(wXComponent, -1);
    }

    public void addChild(WXComponent wXComponent, int i) {
        super.addChild(wXComponent, i);
        if (wXComponent != null && i >= -1) {
            if (i >= this.mChildren.size()) {
                i = -1;
            }
            bindViewType(wXComponent);
            int size = i == -1 ? this.mChildren.size() - 1 : i;
            final ViewGroup viewGroup = (ViewGroup) getHostView();
            if (viewGroup != null) {
                boolean z = false;
                if (getBasicComponentData() != null && "default".equals(getAttrs().get(Constants.Name.INSERT_CELL_ANIMATION))) {
                    ((ListComponentView) viewGroup).getInnerView().setItemAnimator(this.mItemAnimator);
                } else {
                    ((ListComponentView) viewGroup).getInnerView().setItemAnimator((RecyclerView.ItemAnimator) null);
                }
                if (wXComponent.getBasicComponentData() != null && WXUtils.getBoolean(wXComponent.getAttrs().get(Constants.Name.KEEP_SCROLL_POSITION), false).booleanValue() && i <= getChildCount() && i > -1) {
                    z = true;
                }
                if (z) {
                    ListComponentView listComponentView = (ListComponentView) viewGroup;
                    if (listComponentView.getInnerView().getLayoutManager() instanceof LinearLayoutManager) {
                        if (this.keepPositionCell == null) {
                            ListBaseViewHolder listBaseViewHolder = (ListBaseViewHolder) listComponentView.getInnerView().findViewHolderForAdapterPosition(((LinearLayoutManager) listComponentView.getInnerView().getLayoutManager()).findLastCompletelyVisibleItemPosition());
                            if (listBaseViewHolder != null) {
                                this.keepPositionCell = listBaseViewHolder.getComponent();
                            }
                            if (this.keepPositionCell != null) {
                                if (!listComponentView.getInnerView().isLayoutFrozen()) {
                                    listComponentView.getInnerView().setLayoutFrozen(true);
                                }
                                if (this.keepPositionCellRunnable != null) {
                                    viewGroup.removeCallbacks(this.keepPositionCellRunnable);
                                }
                                this.keepPositionCellRunnable = new Runnable() {
                                    public void run() {
                                        if (BasicListComponent.this.keepPositionCell != null) {
                                            int indexOf = BasicListComponent.this.indexOf(BasicListComponent.this.keepPositionCell);
                                            int top = BasicListComponent.this.keepPositionCell.getHostView() != null ? BasicListComponent.this.keepPositionCell.getHostView().getTop() : 0;
                                            if (top > 0) {
                                                ((LinearLayoutManager) ((ListComponentView) viewGroup).getInnerView().getLayoutManager()).scrollToPositionWithOffset(indexOf, top);
                                            } else {
                                                ((ListComponentView) viewGroup).getInnerView().getLayoutManager().scrollToPosition(indexOf);
                                            }
                                            ((ListComponentView) viewGroup).getInnerView().setLayoutFrozen(false);
                                            WXComponent unused = BasicListComponent.this.keepPositionCell = null;
                                            Runnable unused2 = BasicListComponent.this.keepPositionCellRunnable = null;
                                        }
                                    }
                                };
                            }
                        }
                        if (this.keepPositionCellRunnable == null) {
                            listComponentView.getInnerView().scrollToPosition(((LinearLayoutManager) listComponentView.getInnerView().getLayoutManager()).findLastVisibleItemPosition());
                        }
                    }
                    listComponentView.getRecyclerViewBaseAdapter().notifyItemInserted(size);
                    if (this.keepPositionCellRunnable != null) {
                        viewGroup.removeCallbacks(this.keepPositionCellRunnable);
                        viewGroup.postDelayed(this.keepPositionCellRunnable, this.keepPositionLayoutDelay);
                    }
                } else {
                    ((ListComponentView) viewGroup).getRecyclerViewBaseAdapter().notifyItemChanged(size);
                }
            }
            relocateAppearanceHelper();
        }
    }

    private void relocateAppearanceHelper() {
        for (Map.Entry<String, AppearanceHelper> value : this.mAppearComponents.entrySet()) {
            AppearanceHelper appearanceHelper = (AppearanceHelper) value.getValue();
            appearanceHelper.setCellPosition(this.mChildren.indexOf(findDirectListChild(appearanceHelper.getAwareChild())));
        }
    }

    public void remove(WXComponent wXComponent, boolean z) {
        int indexOf = this.mChildren.indexOf(wXComponent);
        if (z) {
            wXComponent.detachViewAndClearPreInfo();
        }
        unBindViewType(wXComponent);
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup != null) {
            if ("default".equals(wXComponent.getAttrs().get(Constants.Name.DELETE_CELL_ANIMATION))) {
                ((ListComponentView) viewGroup).getInnerView().setItemAnimator(this.mItemAnimator);
            } else {
                ((ListComponentView) viewGroup).getInnerView().setItemAnimator((RecyclerView.ItemAnimator) null);
            }
            ((ListComponentView) viewGroup).getRecyclerViewBaseAdapter().notifyItemRemoved(indexOf);
            if (WXEnvironment.isApkDebugable()) {
                String str = this.TAG;
                WXLogUtils.d(str, "removeChild child at " + indexOf);
            }
            super.remove(wXComponent, z);
        }
    }

    public void computeVisiblePointInViewCoordinate(PointF pointF) {
        WXRecyclerView innerView = ((ListComponentView) ((ViewGroup) getHostView())).getInnerView();
        pointF.set((float) innerView.computeHorizontalScrollOffset(), (float) innerView.computeVerticalScrollOffset());
    }

    public void onViewRecycled(ListBaseViewHolder listBaseViewHolder) {
        long currentTimeMillis = System.currentTimeMillis();
        listBaseViewHolder.setComponentUsing(false);
        if (listBaseViewHolder == null || !listBaseViewHolder.canRecycled() || listBaseViewHolder.getComponent() == null || listBaseViewHolder.getComponent().isUsing()) {
            WXLogUtils.w(this.TAG, "this holder can not be allowed to  recycled");
        } else {
            listBaseViewHolder.recycled();
        }
        if (WXEnvironment.isApkDebugable()) {
            String str = this.TAG;
            WXLogUtils.d(str, "Recycle holder " + (System.currentTimeMillis() - currentTimeMillis) + "  Thread:" + Thread.currentThread().getName());
        }
    }

    public void onBindViewHolder(final ListBaseViewHolder listBaseViewHolder, int i) {
        if (listBaseViewHolder != null) {
            listBaseViewHolder.setComponentUsing(true);
            WXComponent child = getChild(i);
            if (child == null || (child instanceof WXRefresh) || (child instanceof WXLoading) || child.isFixed()) {
                if (WXEnvironment.isApkDebugable()) {
                    String str = this.TAG;
                    WXLogUtils.d(str, "Bind WXRefresh & WXLoading " + listBaseViewHolder);
                }
                if ((child instanceof WXBaseRefresh) && listBaseViewHolder.getView() != null && child.getAttrs().get("holderBackground") != null) {
                    listBaseViewHolder.getView().setBackgroundColor(WXResourceUtils.getColor(child.getAttrs().get("holderBackground").toString(), -1));
                    listBaseViewHolder.getView().setVisibility(0);
                    listBaseViewHolder.getView().postInvalidate();
                }
            } else if (listBaseViewHolder.getComponent() != null && (listBaseViewHolder.getComponent() instanceof WXCell)) {
                if (listBaseViewHolder.isRecycled()) {
                    listBaseViewHolder.bindData(child);
                    child.onRenderFinish(1);
                }
                if (this.mDragHelper != null && this.mDragHelper.isDraggable()) {
                    this.mTriggerType = this.mTriggerType == null ? "longpress" : this.mTriggerType;
                    WXCell wXCell = (WXCell) listBaseViewHolder.getComponent();
                    boolean booleanValue = WXUtils.getBoolean(wXCell.getAttrs().get(EXCLUDED), false).booleanValue();
                    this.mDragHelper.setDragExcluded(listBaseViewHolder, booleanValue);
                    if ("pan".equals(this.mTriggerType)) {
                        this.mDragHelper.setLongPressDragEnabled(false);
                        WXComponent findComponentByAnchorName = findComponentByAnchorName(wXCell, DRAG_ANCHOR);
                        if (findComponentByAnchorName != null && findComponentByAnchorName.getHostView() != null && !booleanValue) {
                            findComponentByAnchorName.getHostView().setOnTouchListener(new View.OnTouchListener() {
                                @SuppressLint({"ClickableViewAccessibility"})
                                public boolean onTouch(View view, MotionEvent motionEvent) {
                                    if (MotionEventCompat.getActionMasked(motionEvent) != 0) {
                                        return true;
                                    }
                                    BasicListComponent.this.mDragHelper.startDrag(listBaseViewHolder);
                                    return true;
                                }
                            });
                        } else if (!WXEnvironment.isApkDebugable()) {
                        } else {
                            if (!booleanValue) {
                                WXLogUtils.e(this.TAG, "[error] onBindViewHolder: the anchor component or view is not found");
                                return;
                            }
                            String str2 = this.TAG;
                            WXLogUtils.d(str2, "onBindViewHolder: position " + i + " is drag excluded");
                        }
                    } else if ("longpress".equals(this.mTriggerType)) {
                        this.mDragHelper.setLongPressDragEnabled(true);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void markComponentUsable() {
        Iterator it = this.mChildren.iterator();
        while (it.hasNext()) {
            ((WXComponent) it.next()).setUsing(false);
        }
    }

    public ListBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (this.mChildren != null) {
            if (this.mViewTypes == null) {
                return createVHForFakeComponent(i);
            }
            ArrayList arrayList = this.mViewTypes.get(i);
            checkRecycledViewPool(i);
            if (arrayList == null) {
                return createVHForFakeComponent(i);
            }
            int i2 = 0;
            while (i2 < arrayList.size()) {
                WXComponent wXComponent = (WXComponent) arrayList.get(i2);
                if (wXComponent == null || wXComponent.isUsing()) {
                    i2++;
                } else if (wXComponent.isFixed()) {
                    return createVHForFakeComponent(i);
                } else {
                    if (wXComponent instanceof WXCell) {
                        if (wXComponent.getRealView() != null) {
                            return new ListBaseViewHolder(wXComponent, i);
                        }
                        wXComponent.lazy(false);
                        wXComponent.createView();
                        wXComponent.applyLayoutAndEvent(wXComponent);
                        return new ListBaseViewHolder(wXComponent, i, true);
                    } else if (wXComponent instanceof WXBaseRefresh) {
                        return createVHForRefreshComponent(i);
                    } else {
                        WXLogUtils.e(this.TAG, "List cannot include element except cell、header、fixed、refresh and loading");
                        return createVHForFakeComponent(i);
                    }
                }
            }
        }
        if (WXEnvironment.isApkDebugable()) {
            String str = this.TAG;
            WXLogUtils.e(str, "Cannot find request viewType: " + i);
        }
        return createVHForFakeComponent(i);
    }

    private void checkRecycledViewPool(int i) {
        try {
            if (this.mViewTypes.size() > 9) {
                mAllowCacheViewHolder = false;
            }
            if (!(!mDownForBidCacheViewHolder || getHostView() == null || ((ListComponentView) ((ViewGroup) getHostView())).getInnerView() == null)) {
                ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().getRecycledViewPool().setMaxRecycledViews(i, 0);
            }
            if (!mDownForBidCacheViewHolder && !mAllowCacheViewHolder && getHostView() != null && ((ListComponentView) ((ViewGroup) getHostView())).getInnerView() != null) {
                for (int i2 = 0; i2 < this.mViewTypes.size(); i2++) {
                    ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().getRecycledViewPool().setMaxRecycledViews(this.mViewTypes.keyAt(i2), 0);
                }
                mDownForBidCacheViewHolder = true;
            }
        } catch (Exception unused) {
            WXLogUtils.e(this.TAG, "Clear recycledViewPool error!");
        }
    }

    public int getItemViewType(int i) {
        return generateViewType(getChild(i));
    }

    @Nullable
    private WXComponent findComponentByAnchorName(@NonNull WXComponent wXComponent, @NonNull String str) {
        String string;
        long currentTimeMillis = WXEnvironment.isApkDebugable() ? System.currentTimeMillis() : 0;
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(wXComponent);
        while (!arrayDeque.isEmpty()) {
            WXComponent wXComponent2 = (WXComponent) arrayDeque.removeFirst();
            if (wXComponent2 != null && (string = WXUtils.getString(wXComponent2.getAttrs().get(str), (String) null)) != null && string.equals("true")) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("dragPerf", "findComponentByAnchorName time: " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
                }
                return wXComponent2;
            } else if (wXComponent2 instanceof WXVContainer) {
                WXVContainer wXVContainer = (WXVContainer) wXComponent2;
                int childCount = wXVContainer.childCount();
                for (int i = 0; i < childCount; i++) {
                    arrayDeque.add(wXVContainer.getChild(i));
                }
            }
        }
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("dragPerf", "findComponentByAnchorName elapsed time: " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
        }
        return null;
    }

    private String getTriggerType(@Nullable WXComponent wXComponent) {
        if (wXComponent == null) {
            return "longpress";
        }
        String string = WXUtils.getString(wXComponent.getAttrs().get(DRAG_TRIGGER_TYPE), "longpress");
        if (!"longpress".equals(string) && !"pan".equals(string)) {
            string = "longpress";
        }
        if (WXEnvironment.isApkDebugable()) {
            String str = this.TAG;
            WXLogUtils.d(str, "trigger type is " + string);
        }
        return string;
    }

    private void bindViewType(WXComponent wXComponent) {
        int generateViewType = generateViewType(wXComponent);
        if (this.mViewTypes == null) {
            this.mViewTypes = new SparseArray<>();
        }
        ArrayList arrayList = this.mViewTypes.get(generateViewType);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.mViewTypes.put(generateViewType, arrayList);
        }
        arrayList.add(wXComponent);
    }

    private void unBindViewType(WXComponent wXComponent) {
        ArrayList arrayList;
        int generateViewType = generateViewType(wXComponent);
        if (this.mViewTypes != null && (arrayList = this.mViewTypes.get(generateViewType)) != null) {
            arrayList.remove(wXComponent);
        }
    }

    private int generateViewType(WXComponent wXComponent) {
        long j;
        try {
            j = (long) Integer.parseInt(wXComponent.getRef());
            String scope = wXComponent.getAttrs().getScope();
            if (!TextUtils.isEmpty(scope)) {
                if (this.mRefToViewType == null) {
                    this.mRefToViewType = new ArrayMap<>();
                }
                if (!this.mRefToViewType.containsKey(scope)) {
                    this.mRefToViewType.put(scope, Long.valueOf(j));
                }
                j = this.mRefToViewType.get(scope).longValue();
            }
        } catch (RuntimeException e) {
            WXLogUtils.eTag(this.TAG, e);
            j = -1;
            WXLogUtils.e(this.TAG, "getItemViewType: NO ID, this will crash the whole render system of WXListRecyclerView");
        }
        return (int) j;
    }

    public int getItemCount() {
        return getChildCount();
    }

    public boolean onFailedToRecycleView(ListBaseViewHolder listBaseViewHolder) {
        if (!WXEnvironment.isApkDebugable()) {
            return false;
        }
        String str = this.TAG;
        WXLogUtils.d(str, "Failed to recycle " + listBaseViewHolder);
        return false;
    }

    public long getItemId(int i) {
        try {
            return Long.parseLong(getChild(i).getRef());
        } catch (RuntimeException e) {
            WXLogUtils.e(this.TAG, WXLogUtils.getStackTrace(e));
            return -1;
        }
    }

    public void onLoadMore(int i) {
        try {
            String loadMoreOffset = getAttrs().getLoadMoreOffset();
            if (TextUtils.isEmpty(loadMoreOffset)) {
                loadMoreOffset = "0";
            }
            if (((float) i) <= WXViewUtils.getRealPxByWidth((float) WXUtils.getInt(loadMoreOffset), getInstance().getInstanceViewPortWidth()) && getEvents().contains(Constants.Event.LOADMORE)) {
                if (this.mListCellCount != this.mChildren.size() || this.mForceLoadmoreNextTime) {
                    fireEvent(Constants.Event.LOADMORE);
                    this.mListCellCount = this.mChildren.size();
                    this.mForceLoadmoreNextTime = false;
                }
            }
        } catch (Exception e) {
            WXLogUtils.d(this.TAG + "onLoadMore :", (Throwable) e);
        }
    }

    public void notifyAppearStateChange(int i, int i2, int i3, int i4) {
        View hostView;
        String str = null;
        if (this.mAppearChangeRunnable != null) {
            ((ViewGroup) getHostView()).removeCallbacks(this.mAppearChangeRunnable);
            this.mAppearChangeRunnable = null;
        }
        if (i4 > 0) {
            str = "up";
        } else if (i4 < 0) {
            str = "down";
        }
        if (getOrientation() == 0 && i3 != 0) {
            str = i3 > 0 ? "left" : "right";
        }
        for (AppearanceHelper next : this.mAppearComponents.values()) {
            WXComponent awareChild = next.getAwareChild();
            if (next.isWatch() && (hostView = awareChild.getHostView()) != null) {
                int appearStatus = next.setAppearStatus(!(ViewCompat.isAttachedToWindow(hostView) ^ true) && next.isViewVisible(true));
                if (appearStatus != 0) {
                    if (WXEnvironment.isApkDebugable()) {
                        WXLogUtils.d(Constants.Event.APPEAR, "item " + next.getCellPositionINScollable() + " result " + appearStatus);
                    }
                    awareChild.notifyAppearStateChange(appearStatus == 1 ? Constants.Event.APPEAR : Constants.Event.DISAPPEAR, str);
                }
            }
        }
    }

    @NonNull
    private ListBaseViewHolder createVHForFakeComponent(int i) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setBackgroundColor(-1);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
        return new ListBaseViewHolder((View) frameLayout, i);
    }

    private ListBaseViewHolder createVHForRefreshComponent(int i) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, 1));
        return new ListBaseViewHolder((View) frameLayout, i);
    }

    @JSMethod
    public void resetLoadmore() {
        this.mForceLoadmoreNextTime = true;
        this.mListCellCount = 0;
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if (ScrollStartEndHelper.isScrollEvent(str) && getHostView() != null && ((ListComponentView) ((ViewGroup) getHostView())).getInnerView() != null && !this.mHasAddScrollEvent) {
            this.mHasAddScrollEvent = true;
            ((ListComponentView) ((ViewGroup) getHostView())).getInnerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
                private boolean mFirstEvent = true;
                private int offsetXCorrection;
                private int offsetYCorrection;

                public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                    int i3;
                    int i4;
                    super.onScrolled(recyclerView, i, i2);
                    int computeHorizontalScrollOffset = recyclerView.computeHorizontalScrollOffset();
                    int computeVerticalScrollOffset = recyclerView.computeVerticalScrollOffset();
                    if (i == 0 && i2 == 0) {
                        this.offsetXCorrection = computeHorizontalScrollOffset;
                        this.offsetYCorrection = computeVerticalScrollOffset;
                        i4 = 0;
                        i3 = 0;
                    } else {
                        i4 = computeHorizontalScrollOffset - this.offsetXCorrection;
                        i3 = computeVerticalScrollOffset - this.offsetYCorrection;
                    }
                    BasicListComponent.this.getScrollStartEndHelper().onScrolled(i4, i3);
                    if (BasicListComponent.this.getEvents().contains("scroll")) {
                        if (this.mFirstEvent) {
                            this.mFirstEvent = false;
                        } else if (recyclerView.getLayoutManager().canScrollVertically() && BasicListComponent.this.shouldReport(i4, i3)) {
                            BasicListComponent.this.fireScrollEvent(recyclerView, i4, i3);
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void fireScrollEvent(RecyclerView recyclerView, int i, int i2) {
        fireEvent("scroll", getScrollEvent(recyclerView, i, i2));
    }

    public Map<String, Object> getScrollEvent(RecyclerView recyclerView, int i, int i2) {
        boolean z = true;
        if (getOrientation() == 1) {
            i2 = -calcContentOffset(recyclerView);
        }
        int measuredWidth = recyclerView.getMeasuredWidth() + recyclerView.computeHorizontalScrollRange();
        int i3 = 0;
        for (int i4 = 0; i4 < getChildCount(); i4++) {
            WXComponent child = getChild(i4);
            if (child != null) {
                i3 = (int) (((float) i3) + child.getLayoutHeight());
            }
        }
        HashMap hashMap = new HashMap(3);
        HashMap hashMap2 = new HashMap(3);
        HashMap hashMap3 = new HashMap(3);
        hashMap2.put("width", Float.valueOf(WXViewUtils.getWebPxByWidth((float) measuredWidth, getInstance().getInstanceViewPortWidth())));
        hashMap2.put("height", Float.valueOf(WXViewUtils.getWebPxByWidth((float) i3, getInstance().getInstanceViewPortWidth())));
        hashMap3.put(Constants.Name.X, Float.valueOf(-WXViewUtils.getWebPxByWidth((float) i, getInstance().getInstanceViewPortWidth())));
        hashMap3.put(Constants.Name.Y, Float.valueOf(-WXViewUtils.getWebPxByWidth((float) i2, getInstance().getInstanceViewPortWidth())));
        hashMap.put(Constants.Name.CONTENT_SIZE, hashMap2);
        hashMap.put(Constants.Name.CONTENT_OFFSET, hashMap3);
        if (recyclerView.getScrollState() != 1) {
            z = false;
        }
        hashMap.put(Constants.Name.ISDRAGGING, Boolean.valueOf(z));
        return hashMap;
    }

    /* access modifiers changed from: private */
    public boolean shouldReport(int i, int i2) {
        if (this.mLastReport.x == -1 && this.mLastReport.y == -1) {
            this.mLastReport.x = i;
            this.mLastReport.y = i2;
            return true;
        }
        int abs = Math.abs(this.mLastReport.x - i);
        int abs2 = Math.abs(this.mLastReport.y - i2);
        if (abs < this.mOffsetAccuracy && abs2 < this.mOffsetAccuracy) {
            return false;
        }
        this.mLastReport.x = i;
        this.mLastReport.y = i2;
        return true;
    }

    public int calcContentOffset(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int i = 0;
        if (layoutManager instanceof LinearLayoutManager) {
            int findFirstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            if (findFirstVisibleItemPosition == -1) {
                return 0;
            }
            View findViewByPosition = layoutManager.findViewByPosition(findFirstVisibleItemPosition);
            int top = findViewByPosition != null ? findViewByPosition.getTop() : 0;
            int i2 = 0;
            while (i < findFirstVisibleItemPosition) {
                WXComponent child = getChild(i);
                if (child != null) {
                    i2 = (int) (((float) i2) - child.getLayoutHeight());
                }
                i++;
            }
            if (layoutManager instanceof GridLayoutManager) {
                i2 /= ((GridLayoutManager) layoutManager).getSpanCount();
            }
            return i2 + top;
        } else if (!(layoutManager instanceof StaggeredGridLayoutManager)) {
            return -1;
        } else {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int spanCount = staggeredGridLayoutManager.getSpanCount();
            int i3 = staggeredGridLayoutManager.findFirstVisibleItemPositions((int[]) null)[0];
            if (i3 == -1) {
                return 0;
            }
            View findViewByPosition2 = layoutManager.findViewByPosition(i3);
            int top2 = findViewByPosition2 != null ? findViewByPosition2.getTop() : 0;
            int i4 = 0;
            while (i < i3) {
                WXComponent child2 = getChild(i);
                if (child2 != null) {
                    i4 = (int) (((float) i4) - child2.getLayoutHeight());
                }
                i++;
            }
            return (i4 / spanCount) + top2;
        }
    }

    public ScrollStartEndHelper getScrollStartEndHelper() {
        if (this.mScrollStartEndHelper == null) {
            this.mScrollStartEndHelper = new ScrollStartEndHelper(this);
        }
        return this.mScrollStartEndHelper;
    }
}
