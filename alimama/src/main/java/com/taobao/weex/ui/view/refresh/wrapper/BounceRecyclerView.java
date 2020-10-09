package com.taobao.weex.ui.view.refresh.wrapper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import androidx.annotation.Nullable;
import com.taobao.weex.ui.component.list.ListComponentView;
import com.taobao.weex.ui.component.list.StickyHeaderHelper;
import com.taobao.weex.ui.component.list.WXCell;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.ui.view.listview.WXRecyclerView;
import com.taobao.weex.ui.view.listview.adapter.RecyclerViewBaseAdapter;

@SuppressLint({"ViewConstructor"})
public class BounceRecyclerView extends BaseBounceView<WXRecyclerView> implements ListComponentView, WXGestureObservable {
    public static final int DEFAULT_COLUMN_COUNT = 1;
    public static final int DEFAULT_COLUMN_GAP = 1;
    private RecyclerViewBaseAdapter adapter;
    private int mColumnCount;
    private float mColumnGap;
    private WXGesture mGesture;
    private int mLayoutType;
    private StickyHeaderHelper mStickyHeaderHelper;

    public /* bridge */ /* synthetic */ WXRecyclerView getInnerView() {
        return (WXRecyclerView) super.getInnerView();
    }

    public BounceRecyclerView(Context context, int i, int i2, float f, int i3) {
        super(context, i3);
        this.adapter = null;
        this.mLayoutType = 1;
        this.mColumnCount = 1;
        this.mColumnGap = 1.0f;
        this.mLayoutType = i;
        this.mColumnCount = i2;
        this.mColumnGap = f;
        init(context);
        this.mStickyHeaderHelper = new StickyHeaderHelper(this);
    }

    public BounceRecyclerView(Context context, int i, int i2) {
        this(context, i, 1, 1.0f, i2);
    }

    public void setRecyclerViewBaseAdapter(RecyclerViewBaseAdapter recyclerViewBaseAdapter) {
        this.adapter = recyclerViewBaseAdapter;
        if (getInnerView() != null) {
            ((WXRecyclerView) getInnerView()).setAdapter(recyclerViewBaseAdapter);
        }
    }

    public RecyclerViewBaseAdapter getRecyclerViewBaseAdapter() {
        return this.adapter;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        return this.mGesture != null ? dispatchTouchEvent | this.mGesture.onTouch(this, motionEvent) : dispatchTouchEvent;
    }

    public WXRecyclerView setInnerView(Context context) {
        WXRecyclerView wXRecyclerView = new WXRecyclerView(context);
        wXRecyclerView.initView(context, this.mLayoutType, this.mColumnCount, this.mColumnGap, getOrientation());
        return wXRecyclerView;
    }

    public void onRefreshingComplete() {
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    public void onLoadmoreComplete() {
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    public void notifyStickyShow(WXCell wXCell) {
        this.mStickyHeaderHelper.notifyStickyShow(wXCell);
    }

    public void updateStickyView(int i) {
        this.mStickyHeaderHelper.updateStickyView(i);
    }

    public void notifyStickyRemove(WXCell wXCell) {
        this.mStickyHeaderHelper.notifyStickyRemove(wXCell);
    }

    public StickyHeaderHelper getStickyHeaderHelper() {
        return this.mStickyHeaderHelper;
    }

    public void registerGestureListener(@Nullable WXGesture wXGesture) {
        this.mGesture = wXGesture;
        ((WXRecyclerView) getInnerView()).registerGestureListener(wXGesture);
    }

    public WXGesture getGestureListener() {
        return this.mGesture;
    }
}
