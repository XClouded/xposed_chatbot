package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamic.R;
import com.taobao.android.dinamicx.DXEngineContext;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXMsgConstant;
import com.taobao.android.dinamicx.DXRootView;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.DXSimpleRenderPipeline;
import com.taobao.android.dinamicx.ItemSize;
import com.taobao.android.dinamicx.expression.event.DXScrollEvent;
import com.taobao.android.dinamicx.expression.event.DXViewEvent;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.view.DXLinearLayoutManager;
import com.taobao.android.dinamicx.view.DXNativeFrameLayout;
import com.taobao.android.dinamicx.view.DXNativeRecyclerView;
import java.util.ArrayList;

public class DXScrollerLayout extends DXScrollLayoutBase {
    public static final long DXSCROLLERLAYOUT_OPENSCROLLERANIMATION = -7123870390816445523L;
    public static final long DX_SCROLLER_LAYOUT = 5192418215958133202L;
    public static final long DX_SCROLLER_LAYOUT_CONTENT_OFFSET = 1750803361827314031L;
    public static final long DX_SCROLLER_LAYOUT_ITEM_PREFETCH = 3722067687195294700L;
    public static final int DX_TAG_HAS_SCROLL_LISTENER = R.id.dx_recycler_view_has_scroll_listener;
    /* access modifiers changed from: private */
    public int contentOffset = -1;
    private boolean itemPrefetch = true;
    /* access modifiers changed from: private */
    public boolean openScrollerAnimation = false;

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXScrollerLayout();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXScrollerLayout();
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXScrollerLayout) {
            DXScrollerLayout dXScrollerLayout = (DXScrollerLayout) dXWidgetNode;
            this.contentOffset = dXScrollerLayout.contentOffset;
            this.itemPrefetch = dXScrollerLayout.itemPrefetch;
            this.openScrollerAnimation = dXScrollerLayout.openScrollerAnimation;
        }
    }

    public void onSetIntAttribute(long j, int i) {
        if (j == DX_SCROLLER_LAYOUT_CONTENT_OFFSET) {
            this.contentOffset = i;
            return;
        }
        boolean z = false;
        if (j == DX_SCROLLER_LAYOUT_ITEM_PREFETCH) {
            if (i != 0) {
                z = true;
            }
            this.itemPrefetch = z;
        } else if (j == DXSCROLLERLAYOUT_OPENSCROLLERANIMATION) {
            if (i == 1) {
                z = true;
            }
            this.openScrollerAnimation = z;
        } else {
            super.onSetIntAttribute(j, i);
        }
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        DXNativeRecyclerView dXNativeRecyclerView = new DXNativeRecyclerView(context);
        closeDefaultAnimator(dXNativeRecyclerView);
        return dXNativeRecyclerView;
    }

    public void closeDefaultAnimator(RecyclerView recyclerView) {
        try {
            recyclerView.getItemAnimator().setAddDuration(0);
            recyclerView.getItemAnimator().setChangeDuration(0);
            recyclerView.getItemAnimator().setMoveDuration(0);
            recyclerView.getItemAnimator().setRemoveDuration(0);
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Throwable unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        DXScrollerLayout dXScrollerLayout;
        super.onRenderView(context, view);
        if ((view instanceof RecyclerView) && (dXScrollerLayout = (DXScrollerLayout) getDXRuntimeContext().getWidgetNode()) != null) {
            DXNativeRecyclerView dXNativeRecyclerView = (DXNativeRecyclerView) view;
            setLayoutManager(context, dXScrollerLayout, dXNativeRecyclerView);
            if (dXScrollerLayout.contentOffset > -1) {
                int i = dXScrollerLayout.contentOffset;
                if (getOrientation() == 1) {
                    dXNativeRecyclerView.needScrollAfterLayout(0, i, dXScrollerLayout.contentHorizontalLength, dXScrollerLayout.contentVerticalLength);
                } else {
                    dXNativeRecyclerView.needScrollAfterLayout(i, 0, dXScrollerLayout.contentHorizontalLength, dXScrollerLayout.contentVerticalLength);
                }
            }
            setAdapter(dXScrollerLayout, dXNativeRecyclerView, context);
            setScrollListener(dXScrollerLayout, dXNativeRecyclerView);
        }
    }

    /* access modifiers changed from: protected */
    public void setLayoutManager(Context context, DXScrollerLayout dXScrollerLayout, RecyclerView recyclerView) {
        DXLinearLayoutManager dXLinearLayoutManager = (DXLinearLayoutManager) recyclerView.getLayoutManager();
        if (dXLinearLayoutManager == null) {
            dXLinearLayoutManager = newLinearLayoutManager(context);
            recyclerView.setLayoutManager(dXLinearLayoutManager);
        }
        if (getOrientation() == 1) {
            dXLinearLayoutManager.setOrientation(1);
        } else {
            dXLinearLayoutManager.setOrientation(0);
        }
        dXLinearLayoutManager.setItemPrefetchEnabled(dXScrollerLayout.itemPrefetch);
        dXLinearLayoutManager.setScrollEnabled(dXScrollerLayout.scrollEnabled);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public DXLinearLayoutManager newLinearLayoutManager(Context context) {
        return new DXLinearLayoutManager(context);
    }

    /* access modifiers changed from: protected */
    public void setScrollListener(DXScrollerLayout dXScrollerLayout, RecyclerView recyclerView) {
        ScrollListener scrollListener = (ScrollListener) recyclerView.getTag(DX_TAG_HAS_SCROLL_LISTENER);
        if (scrollListener == null) {
            ScrollListener newScrollListener = newScrollListener();
            newScrollListener.setScrollerLayout(dXScrollerLayout, recyclerView);
            recyclerView.addOnScrollListener(newScrollListener);
            recyclerView.setTag(DX_TAG_HAS_SCROLL_LISTENER, newScrollListener);
            newScrollListener.fireScrollEventWithInit(recyclerView);
            newScrollListener.initAnimation();
            return;
        }
        scrollListener.setScrollerLayout(dXScrollerLayout, recyclerView);
        scrollListener.fireScrollEventWithInit(recyclerView);
        scrollListener.initAnimation();
    }

    /* access modifiers changed from: protected */
    public ScrollListener newScrollListener() {
        return new ScrollListener();
    }

    /* access modifiers changed from: protected */
    public void setAdapter(DXScrollerLayout dXScrollerLayout, RecyclerView recyclerView, Context context) {
        ScrollerAdapter scrollerAdapter = (ScrollerAdapter) recyclerView.getAdapter();
        if (scrollerAdapter == null) {
            ScrollerAdapter scrollerAdapter2 = new ScrollerAdapter(dXScrollerLayout.pipeline, context, dXScrollerLayout);
            scrollerAdapter2.setHasStableIds(true);
            scrollerAdapter2.setDataSource(dXScrollerLayout.itemWidgetNodes);
            recyclerView.setAdapter(scrollerAdapter2);
            return;
        }
        scrollerAdapter.setDataSource(dXScrollerLayout.itemWidgetNodes);
        if (this.contentOffset <= -1) {
            ((DXNativeRecyclerView) recyclerView).needScrollAfterLayout(0, 0, dXScrollerLayout.contentHorizontalLength, dXScrollerLayout.contentVerticalLength);
        }
        scrollerAdapter.notifyDataSetChanged();
    }

    public static class ScrollerAdapter extends RecyclerView.Adapter {
        private DXViewEvent appearViewEvent = new DXViewEvent(DXScrollLayoutBase.DX_SCROLL_LAYOUT_BASE_ON_PAGE_APPEAR);
        private Context context;
        protected ArrayList<DXWidgetNode> dataSource;
        private DXViewEvent disAppearViewEvent = new DXViewEvent(DXScrollLayoutBase.DX_SCROLL_LAYOUT_BASE_ON_PAGE_DISAPPEAR);
        private boolean needSetLayoutParams = true;
        private DXScrollerLayout scrollerLayout;
        private DXSimpleRenderPipeline simpleRenderPipeline;

        public long getItemId(int i) {
            return (long) i;
        }

        public boolean isNeedSetLayoutParams() {
            return this.needSetLayoutParams;
        }

        public void setNeedSetLayoutParams(boolean z) {
            this.needSetLayoutParams = z;
        }

        public ScrollerAdapter(DXSimpleRenderPipeline dXSimpleRenderPipeline, Context context2, DXScrollerLayout dXScrollerLayout) {
            this.simpleRenderPipeline = dXSimpleRenderPipeline;
            this.context = context2;
            this.scrollerLayout = dXScrollerLayout;
        }

        public void setDataSource(ArrayList<DXWidgetNode> arrayList) {
            this.dataSource = arrayList;
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ItemViewHolder(new DXNativeFrameLayout(this.context));
        }

        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            DXWidgetNode item = getItem(i);
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            if (this.needSetLayoutParams) {
                ViewGroup.LayoutParams layoutParams = itemViewHolder.itemView.getLayoutParams();
                if (layoutParams instanceof RecyclerView.LayoutParams) {
                    setRecyclerViewParams(i, (RecyclerView.LayoutParams) layoutParams);
                } else {
                    RecyclerView.LayoutParams layoutParams2 = new RecyclerView.LayoutParams(this.scrollerLayout.getMeasuredWidth(), this.scrollerLayout.getMeasuredHeight());
                    itemViewHolder.itemView.setLayoutParams(layoutParams2);
                    setRecyclerViewParams(i, layoutParams2);
                }
            }
            if (itemViewHolder.itemWidgetNode == item) {
                this.appearViewEvent.setItemIndex(i);
                if (item.getBindingXExecutingMap() != null) {
                    item.getBindingXExecutingMap().clear();
                }
                item.sendBroadcastEvent(this.appearViewEvent);
                this.scrollerLayout.postEvent(this.appearViewEvent);
                this.scrollerLayout.addAppearWidget(item);
                return;
            }
            DXRuntimeContext cloneDxRuntimeContextResetError = cloneDxRuntimeContextResetError(item);
            this.simpleRenderPipeline.renderWidgetNode(item, (DXWidgetNode) null, viewHolder.itemView, cloneDxRuntimeContextResetError, 2, 8, this.scrollerLayout.oldWidthMeasureSpec, this.scrollerLayout.oldHeightMeasureSpec, i);
            if (cloneDxRuntimeContextResetError.hasError()) {
                DXAppMonitor.trackerError(cloneDxRuntimeContextResetError.getDxError(), true);
            }
            itemViewHolder.itemWidgetNode = item;
            this.appearViewEvent.setItemIndex(i);
            if (item.getBindingXExecutingMap() != null) {
                item.getBindingXExecutingMap().clear();
            }
            item.sendBroadcastEvent(this.appearViewEvent);
            this.scrollerLayout.postEvent(this.appearViewEvent);
            this.scrollerLayout.addAppearWidget(item);
        }

        @NonNull
        private DXRuntimeContext cloneDxRuntimeContextResetError(DXWidgetNode dXWidgetNode) {
            DXRuntimeContext cloneWithWidgetNode = dXWidgetNode.getDXRuntimeContext().cloneWithWidgetNode(dXWidgetNode);
            DXError dXError = new DXError(cloneWithWidgetNode.getBizType());
            dXError.dxTemplateItem = cloneWithWidgetNode.getDxTemplateItem();
            cloneWithWidgetNode.setDxError(dXError);
            return cloneWithWidgetNode;
        }

        private void setRecyclerViewParams(int i, RecyclerView.LayoutParams layoutParams) {
            if (this.scrollerLayout.getOrientation() == 0) {
                if (i == 0) {
                    layoutParams.setMargins(this.scrollerLayout.getPaddingLeft(), this.scrollerLayout.getPaddingTop(), 0, this.scrollerLayout.getPaddingBottom());
                } else if (i == this.dataSource.size() - 1) {
                    layoutParams.setMargins(0, this.scrollerLayout.getPaddingTop(), this.scrollerLayout.getPaddingRight(), this.scrollerLayout.getPaddingBottom());
                } else {
                    layoutParams.setMargins(0, this.scrollerLayout.getPaddingTop(), 0, this.scrollerLayout.getPaddingBottom());
                }
            } else if (i == 0) {
                layoutParams.setMargins(this.scrollerLayout.getPaddingLeft(), this.scrollerLayout.getPaddingTop(), this.scrollerLayout.getPaddingRight(), 0);
            } else if (i == this.dataSource.size() - 1) {
                layoutParams.setMargins(this.scrollerLayout.getPaddingLeft(), 0, this.scrollerLayout.getPaddingRight(), this.scrollerLayout.getPaddingBottom());
            } else {
                layoutParams.setMargins(this.scrollerLayout.getPaddingLeft(), 0, this.scrollerLayout.getPaddingRight(), 0);
            }
        }

        public DXWidgetNode getItem(int i) {
            return this.dataSource.get(i);
        }

        public int getItemCount() {
            if (this.dataSource == null) {
                return 0;
            }
            return this.dataSource.size();
        }

        public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
            this.disAppearViewEvent.setItemIndex(viewHolder.getAdapterPosition());
            this.scrollerLayout.postEvent(this.disAppearViewEvent);
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            itemViewHolder.itemWidgetNode.sendBroadcastEvent(this.disAppearViewEvent);
            this.scrollerLayout.removeAppearWidget(itemViewHolder.itemWidgetNode);
        }

        public static class ItemViewHolder extends RecyclerView.ViewHolder {
            public DXWidgetNode itemWidgetNode;

            public ItemViewHolder(View view) {
                super(view);
            }
        }
    }

    public static class ScrollListener extends RecyclerView.OnScrollListener {
        private ItemSize contentSize = new ItemSize();
        private DXEngineContext engineContext;
        private JSONObject msg;
        /* access modifiers changed from: private */
        public int offsetX;
        /* access modifiers changed from: private */
        public int offsetY;
        private JSONObject params;
        DXRootView rootView;
        /* access modifiers changed from: private */
        public DXScrollEvent scrollEventOnScroll = new DXScrollEvent(DXScrollLayoutBase.DX_SCROLL_LAYOUT_BASE_ON_SCROLL);
        private DXScrollEvent scrollEventOnScrollBegin = new DXScrollEvent(DXScrollLayoutBase.DX_SCROLL_LAYOUT_BASE_ON_SCROLL_BEGIN);
        private DXScrollEvent scrollEventOnScrollEnd = new DXScrollEvent(DXScrollLayoutBase.DX_SCROLL_LAYOUT_BASE_ON_SCROLL_END);
        /* access modifiers changed from: private */
        public DXScrollerLayout scrollerLayout;
        private ItemSize scrollerSize = new ItemSize();

        /* access modifiers changed from: private */
        public void initAnimation() {
            if (this.scrollerLayout.openScrollerAnimation) {
                this.msg = new JSONObject();
                this.msg.put("type", (Object) DXMsgConstant.DX_MSG_TYPE_BNDX);
                this.params = new JSONObject();
                this.msg.put("params", (Object) this.params);
                this.params.put(DXMsgConstant.DX_MSG_WIDGET, (Object) this.scrollerLayout);
                this.rootView = this.scrollerLayout.getDXRuntimeContext().getRootView();
                this.engineContext = this.scrollerLayout.getDXRuntimeContext().getEngineContext();
            }
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            this.offsetX += i;
            this.offsetY += i2;
            sendScrollEvent(recyclerView, this.scrollEventOnScroll);
            sendAnimationMsg(DXMsgConstant.DX_MSG_ACTION_SCROLLING_BINDINGX);
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 1) {
                sendScrollEvent(recyclerView, this.scrollEventOnScrollBegin);
                sendAnimationMsg(DXMsgConstant.DX_MSG_ACTION_SCROLL_BEGIN_BINDINGX);
            } else if (i == 0) {
                sendScrollEvent(recyclerView, this.scrollEventOnScrollEnd);
                sendAnimationMsg(DXMsgConstant.DX_MSG_ACTION_SCROLL_END_BINDINGX);
            }
        }

        public void fireScrollEventWithInit(RecyclerView recyclerView) {
            if (recyclerView instanceof DXNativeRecyclerView) {
                DXNativeRecyclerView dXNativeRecyclerView = (DXNativeRecyclerView) recyclerView;
                setOffsetX(dXNativeRecyclerView.getScrolledX());
                setOffsetY(dXNativeRecyclerView.getScrolledY());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        ScrollListener.this.scrollEventOnScroll.setOffsetX(ScrollListener.this.offsetX);
                        ScrollListener.this.scrollEventOnScroll.setOffsetY(ScrollListener.this.offsetY);
                        if (ScrollListener.this.scrollerLayout.indicatorWidgetNode != null) {
                            ScrollListener.this.scrollerLayout.indicatorWidgetNode.postEvent(ScrollListener.this.scrollEventOnScroll);
                        }
                        ScrollListener.this.scrollerLayout.postEvent(ScrollListener.this.scrollEventOnScroll);
                    }
                });
            }
        }

        private void sendScrollEvent(RecyclerView recyclerView, DXScrollEvent dXScrollEvent) {
            dXScrollEvent.setOffsetX(this.offsetX);
            dXScrollEvent.setOffsetY(this.offsetY);
            if (this.scrollerLayout.getOrientation() == 0) {
                int unused = this.scrollerLayout.contentOffset = this.offsetX;
            } else {
                int unused2 = this.scrollerLayout.contentOffset = this.offsetY;
            }
            if (this.scrollerLayout.indicatorWidgetNode != null) {
                this.scrollerLayout.indicatorWidgetNode.postEvent(dXScrollEvent);
            }
            this.scrollerLayout.postEvent(dXScrollEvent);
        }

        private void sendAnimationMsg(String str) {
            if (this.scrollerLayout.openScrollerAnimation) {
                this.params.put(DXMsgConstant.DX_MSG_OFFSET_X, (Object) Integer.valueOf(this.offsetX));
                this.params.put(DXMsgConstant.DX_MSG_OFFSET_Y, (Object) Integer.valueOf(this.offsetY));
                this.params.put("action", (Object) str);
                this.params.put(DXMsgConstant.DX_MSG_SOURCE_ID, (Object) this.scrollerLayout.getUserId());
                this.engineContext.postMessage(this.rootView, this.msg);
            }
        }

        public void setOffsetX(int i) {
            this.offsetX = i;
        }

        public void setOffsetY(int i) {
            this.offsetY = i;
        }

        public DXScrollerLayout getScrollerLayout() {
            return this.scrollerLayout;
        }

        public void setScrollerLayout(DXScrollerLayout dXScrollerLayout, RecyclerView recyclerView) {
            this.scrollerLayout = dXScrollerLayout;
            if (dXScrollerLayout.getOrientation() == 0) {
                this.contentSize.width = dXScrollerLayout.contentHorizontalLength;
                this.contentSize.height = dXScrollerLayout.getMeasuredHeight();
                this.scrollEventOnScroll.setContentSize(this.contentSize);
                this.scrollEventOnScrollBegin.setContentSize(this.contentSize);
                this.scrollEventOnScrollEnd.setContentSize(this.contentSize);
            } else {
                this.contentSize.width = dXScrollerLayout.getMeasuredWidth();
                this.contentSize.height = dXScrollerLayout.contentVerticalLength;
                this.scrollEventOnScroll.setContentSize(this.contentSize);
                this.scrollEventOnScrollBegin.setContentSize(this.contentSize);
                this.scrollEventOnScrollEnd.setContentSize(this.contentSize);
            }
            this.scrollerSize.width = dXScrollerLayout.getMeasuredWidth();
            this.scrollerSize.height = dXScrollerLayout.getMeasuredHeight();
            this.scrollEventOnScroll.setScrollerSize(this.scrollerSize);
            this.scrollEventOnScrollBegin.setScrollerSize(this.scrollerSize);
            this.scrollEventOnScrollEnd.setScrollerSize(this.scrollerSize);
            this.scrollEventOnScroll.setRecyclerView(recyclerView);
            this.scrollEventOnScrollBegin.setRecyclerView(recyclerView);
            this.scrollEventOnScrollEnd.setRecyclerView(recyclerView);
        }
    }
}
