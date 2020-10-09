package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dinamicx.DXRootView;
import com.taobao.android.dinamicx.DXSimpleRenderPipeline;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dinamicx.expression.event.DXPageChangeEvent;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.view.DXLinearLayoutManager;
import com.taobao.android.dinamicx.view.DXNativeAutoLoopRecyclerView;
import com.taobao.android.dinamicx.view.DXScrollLinearLayoutManager;
import com.taobao.android.dinamicx.widget.DXScrollerLayout;

public class DXSliderLayout extends DXScrollerLayout {
    public static final long DX_SLIDER_LAYOUT = 7645421793448373229L;
    public static final long DX_SLIDER_LAYOUT_AUTO_SCROLL = 2618773720063865426L;
    public static final long DX_SLIDER_LAYOUT_AUTO_SCROLL_INTERVAL = 5501313022839937951L;
    public static final long DX_SLIDER_LAYOUT_IS_INFINITE = -3537170322378136036L;
    public static final long DX_SLIDER_LAYOUT_MANUAL_SWITCH_ENABLED = -7107533083539416402L;
    public static final long DX_SLIDER_LAYOUT_ON_PAGE_CHANGE = -8975195222378757716L;
    public static final long DX_SLIDER_LAYOUT_PAGE_INDEX = 7816489696776271262L;
    private boolean autoScroll;
    private int autoScrollInterval = 1000;
    /* access modifiers changed from: private */
    public boolean isInfinite;
    private boolean manualSwitchEnabled = true;
    private int pageIndex;

    public int measureSpecForChild(int i, int i2) {
        return i2;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(int i) {
        this.pageIndex = i;
    }

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXSliderLayout();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXSliderLayout();
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXSliderLayout) {
            DXSliderLayout dXSliderLayout = (DXSliderLayout) dXWidgetNode;
            this.isInfinite = dXSliderLayout.isInfinite;
            this.pageIndex = dXSliderLayout.pageIndex;
            this.autoScrollInterval = dXSliderLayout.autoScrollInterval;
            this.autoScroll = dXSliderLayout.autoScroll;
            this.manualSwitchEnabled = dXSliderLayout.manualSwitchEnabled;
        }
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return new DXNativeAutoLoopRecyclerView(context);
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        DXSliderLayout dXSliderLayout;
        final int i;
        super.onRenderView(context, view);
        if ((view instanceof DXNativeAutoLoopRecyclerView) && (dXSliderLayout = (DXSliderLayout) getDXRuntimeContext().getWidgetNode()) != null) {
            final DXNativeAutoLoopRecyclerView dXNativeAutoLoopRecyclerView = (DXNativeAutoLoopRecyclerView) view;
            dXNativeAutoLoopRecyclerView.setDinamicXEngine(getDXRuntimeContext().getEngineContext().getEngine());
            int size = dXSliderLayout.itemWidgetNodes != null ? dXSliderLayout.itemWidgetNodes.size() : 0;
            if (dXSliderLayout.isInfinite) {
                i = size != 0 ? ((536870911 / size) * size) + dXSliderLayout.pageIndex : 0;
            } else {
                i = dXSliderLayout.pageIndex;
            }
            DXRootView rootView = getDXRuntimeContext().getRootView();
            if (rootView != null) {
                dXNativeAutoLoopRecyclerView.setNeedProcessViewLifeCycle(!rootView.hasDXRootViewLifeCycle());
                dXNativeAutoLoopRecyclerView.setCurrentIndex(i);
                new Handler().post(new Runnable() {
                    public void run() {
                        dXNativeAutoLoopRecyclerView.scrollToPosition(i);
                    }
                });
                SliderPageChangeListener sliderPageChangeListener = new SliderPageChangeListener(dXSliderLayout, size);
                dXNativeAutoLoopRecyclerView.setOnPageChangeListener(sliderPageChangeListener);
                sliderPageChangeListener.onPageSelected(i);
                dXNativeAutoLoopRecyclerView.setManualSwitchEnabled(this.manualSwitchEnabled);
                if (!dXSliderLayout.isInfinite || dXSliderLayout.autoScrollInterval <= 0 || !dXSliderLayout.autoScroll || !dXSliderLayout.scrollEnabled) {
                    dXNativeAutoLoopRecyclerView.setAutoPlay(false);
                    dXNativeAutoLoopRecyclerView.stopTimer();
                    return;
                }
                dXNativeAutoLoopRecyclerView.setInterval(dXSliderLayout.autoScrollInterval);
                dXNativeAutoLoopRecyclerView.setAutoPlay(true);
                dXNativeAutoLoopRecyclerView.startTimer();
            }
        }
    }

    /* access modifiers changed from: protected */
    public DXScrollerLayout.ScrollListener newScrollListener() {
        return new SliderScrollListener();
    }

    /* access modifiers changed from: protected */
    @NonNull
    public DXLinearLayoutManager newLinearLayoutManager(Context context) {
        return new DXScrollLinearLayoutManager(context, getOrientation(), false);
    }

    /* access modifiers changed from: protected */
    public void setLayoutManager(Context context, DXScrollerLayout dXScrollerLayout, RecyclerView recyclerView) {
        super.setLayoutManager(context, dXScrollerLayout, recyclerView);
        DXScrollLinearLayoutManager dXScrollLinearLayoutManager = (DXScrollLinearLayoutManager) recyclerView.getLayoutManager();
        if (getOrientation() == 1) {
            dXScrollLinearLayoutManager.calculateSpeedPerPixel(getHeight());
        } else {
            dXScrollLinearLayoutManager.calculateSpeedPerPixel(getWidth());
        }
    }

    /* access modifiers changed from: protected */
    public void setAdapter(DXScrollerLayout dXScrollerLayout, RecyclerView recyclerView, Context context) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (!((DXSliderLayout) dXScrollerLayout).isInfinite) {
            if (adapter instanceof AutoLoopScrollerAdapter) {
                recyclerView.setAdapter((RecyclerView.Adapter) null);
            }
            super.setAdapter(dXScrollerLayout, recyclerView, context);
            ((DXScrollerLayout.ScrollerAdapter) recyclerView.getAdapter()).setNeedSetLayoutParams(false);
        } else if (adapter instanceof AutoLoopScrollerAdapter) {
            AutoLoopScrollerAdapter autoLoopScrollerAdapter = (AutoLoopScrollerAdapter) adapter;
            autoLoopScrollerAdapter.setDataSource(dXScrollerLayout.itemWidgetNodes);
            autoLoopScrollerAdapter.notifyDataSetChanged();
        } else {
            AutoLoopScrollerAdapter autoLoopScrollerAdapter2 = new AutoLoopScrollerAdapter(dXScrollerLayout.pipeline, context, dXScrollerLayout);
            autoLoopScrollerAdapter2.setDataSource(dXScrollerLayout.itemWidgetNodes);
            recyclerView.setAdapter(autoLoopScrollerAdapter2);
        }
    }

    /* access modifiers changed from: protected */
    public boolean onEvent(DXEvent dXEvent) {
        DXRootView rootView;
        if (super.onEvent(dXEvent) || (rootView = getDXRuntimeContext().getRootView()) == null) {
            return true;
        }
        if (rootView.hasDXRootViewLifeCycle()) {
            DXNativeAutoLoopRecyclerView dXNativeAutoLoopRecyclerView = (DXNativeAutoLoopRecyclerView) getDXRuntimeContext().getNativeView();
            dXNativeAutoLoopRecyclerView.setNeedProcessViewLifeCycle(false);
            long eventId = dXEvent.getEventId();
            if (DXHashConstant.DX_VIEW_EVENT_ON_APPEAR == eventId) {
                dXNativeAutoLoopRecyclerView.startTimer();
                return true;
            } else if (DXHashConstant.DX_VIEW_EVENT_ON_DISAPPEAR == eventId) {
                dXNativeAutoLoopRecyclerView.stopTimer();
                return true;
            }
        }
        return false;
    }

    public void onSetIntAttribute(long j, int i) {
        boolean z = true;
        if (j == DX_SLIDER_LAYOUT_AUTO_SCROLL) {
            if (i == 0) {
                z = false;
            }
            this.autoScroll = z;
        } else if (j == DX_SLIDER_LAYOUT_AUTO_SCROLL_INTERVAL) {
            this.autoScrollInterval = Math.max(0, i);
        } else if (j == DX_SLIDER_LAYOUT_PAGE_INDEX) {
            this.pageIndex = Math.max(0, i);
        } else if (j == DX_SLIDER_LAYOUT_IS_INFINITE) {
            if (i == 0) {
                z = false;
            }
            this.isInfinite = z;
        } else if (j == DX_SLIDER_LAYOUT_MANUAL_SWITCH_ENABLED) {
            if (i == 0) {
                z = false;
            }
            this.manualSwitchEnabled = z;
        } else {
            super.onSetIntAttribute(j, i);
        }
    }

    public int getDefaultValueForIntAttr(long j) {
        if (j == DX_SLIDER_LAYOUT_AUTO_SCROLL) {
            return 0;
        }
        if (j == DX_SLIDER_LAYOUT_AUTO_SCROLL_INTERVAL) {
            return 1000;
        }
        if (j == DX_SLIDER_LAYOUT_IS_INFINITE) {
            return 0;
        }
        if (j == DX_SLIDER_LAYOUT_MANUAL_SWITCH_ENABLED) {
            return 1;
        }
        if (j == DX_SLIDER_LAYOUT_PAGE_INDEX) {
            return 0;
        }
        return super.getDefaultValueForIntAttr(j);
    }

    public static class SliderScrollListener extends DXScrollerLayout.ScrollListener {
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            DXNativeAutoLoopRecyclerView dXNativeAutoLoopRecyclerView = (DXNativeAutoLoopRecyclerView) recyclerView;
            if (i == 0) {
                if (dXNativeAutoLoopRecyclerView.getCurrentIndex() != findFirstVisibleItemPosition && findFirstVisibleItemPosition == findLastVisibleItemPosition) {
                    dXNativeAutoLoopRecyclerView.setCurrentIndex(findFirstVisibleItemPosition);
                    if (dXNativeAutoLoopRecyclerView.getOnPageChangeListener() != null) {
                        dXNativeAutoLoopRecyclerView.getOnPageChangeListener().onPageSelected(findFirstVisibleItemPosition);
                    }
                }
            } else if (findFirstVisibleItemPosition == 0 && findLastVisibleItemPosition == 1 && i == 1) {
                dXNativeAutoLoopRecyclerView.setCurrentIndex(findFirstVisibleItemPosition);
                if (dXNativeAutoLoopRecyclerView.getOnPageChangeListener() != null) {
                    dXNativeAutoLoopRecyclerView.getOnPageChangeListener().onPageSelected(findFirstVisibleItemPosition);
                }
            }
        }
    }

    public static class SliderPageChangeListener implements DXNativeAutoLoopRecyclerView.OnPageChangeListener {
        private DXPageChangeEvent dxPageChangeEvent = new DXPageChangeEvent(DXSliderLayout.DX_SLIDER_LAYOUT_ON_PAGE_CHANGE);
        private int itemCount;
        private DXSliderLayout sliderLayout;

        public SliderPageChangeListener(DXSliderLayout dXSliderLayout, int i) {
            this.sliderLayout = dXSliderLayout;
            this.itemCount = i;
        }

        public void onPageSelected(int i) {
            if (this.sliderLayout.isInfinite) {
                this.dxPageChangeEvent.pageIndex = i % this.itemCount;
            } else {
                this.dxPageChangeEvent.pageIndex = i;
            }
            if (this.sliderLayout.indicatorWidgetNode != null) {
                this.sliderLayout.indicatorWidgetNode.postEvent(this.dxPageChangeEvent);
            }
            this.sliderLayout.setPageIndex(this.dxPageChangeEvent.pageIndex);
            this.sliderLayout.postEvent(this.dxPageChangeEvent);
        }
    }

    public static class AutoLoopScrollerAdapter extends DXScrollerLayout.ScrollerAdapter {
        public AutoLoopScrollerAdapter(DXSimpleRenderPipeline dXSimpleRenderPipeline, Context context, DXScrollerLayout dXScrollerLayout) {
            super(dXSimpleRenderPipeline, context, dXScrollerLayout);
        }

        public int getItemCount() {
            return (this.dataSource == null || this.dataSource.size() == 0) ? 0 : Integer.MAX_VALUE;
        }

        public int getItemViewType(int i) {
            return ((DXWidgetNode) this.dataSource.get(i % this.dataSource.size())).getAutoId();
        }

        public DXWidgetNode getItem(int i) {
            return super.getItem(i % this.dataSource.size());
        }
    }
}
