package com.taobao.weex.ui.view.refresh.wrapper;

import android.annotation.SuppressLint;
import android.content.Context;
import com.taobao.weex.ui.component.WXScroller;
import com.taobao.weex.ui.view.WXScrollView;

@SuppressLint({"ViewConstructor"})
public class BounceScrollerView extends BaseBounceView<WXScrollView> {
    public void onLoadmoreComplete() {
    }

    public void onRefreshingComplete() {
    }

    public BounceScrollerView(Context context, int i, WXScroller wXScroller) {
        super(context, i);
        init(context);
        if (getInnerView() != null) {
            ((WXScrollView) getInnerView()).setWAScroller(wXScroller);
        }
    }

    public WXScrollView setInnerView(Context context) {
        return new WXScrollView(context);
    }
}
