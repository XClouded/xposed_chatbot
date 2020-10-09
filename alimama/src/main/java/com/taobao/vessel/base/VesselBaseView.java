package com.taobao.vessel.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.taobao.vessel.callback.OnLoadListener;
import com.taobao.vessel.callback.ScrollViewListener;
import com.taobao.vessel.callback.VesselViewCallback;
import com.taobao.vessel.callback.ViewLifeCircle;
import com.taobao.vessel.model.VesselError;
import com.taobao.vessel.utils.VesselType;
import java.util.Map;

public abstract class VesselBaseView extends FrameLayout implements ViewLifeCircle, OnLoadListener, ScrollViewListener {
    protected String mDowngradeUrl;
    public EventCallback mEventCallback;
    public volatile String mInstanceId;
    protected OnLoadListener mOnLoadListener;
    protected Object mOriginParams;
    protected String mOriginUrl;
    /* access modifiers changed from: protected */
    public ScrollViewListener mScrollViewListener;
    protected VesselViewCallback mVesselViewCallback;

    public interface EventCallback {
        boolean isCanIntercept();
    }

    public abstract void fireEvent(String str, Map<String, Object> map);

    public abstract View getChildView();

    public void loadData(VesselType vesselType, String str, Map<String, Object> map) {
    }

    public abstract void loadUrl(String str, Object obj);

    public void onDowngrade(VesselError vesselError, Map<String, Object> map) {
    }

    public boolean refresh(Object obj) {
        return true;
    }

    public abstract void releaseMemory();

    public VesselBaseView(Context context) {
        super(context);
        this.mDowngradeUrl = null;
    }

    public VesselBaseView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VesselBaseView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDowngradeUrl = null;
    }

    public void loadData(String str) {
        loadData((VesselType) null, str);
    }

    public void loadData(VesselType vesselType, String str) {
        loadData(vesselType, str, (Map<String, Object>) null);
    }

    public boolean refresh() {
        return refresh((Object) null);
    }

    public void createView(String str) {
        this.mOriginUrl = str;
        loadUrl(str, (Object) null);
    }

    public void setVesselViewCallback(VesselViewCallback vesselViewCallback) {
        this.mVesselViewCallback = vesselViewCallback;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }

    public void setOnScrollViewListener(ScrollViewListener scrollViewListener) {
        this.mScrollViewListener = scrollViewListener;
    }

    public boolean needIntercept(MotionEvent motionEvent) {
        if (this.mEventCallback != null) {
            return this.mEventCallback.isCanIntercept();
        }
        return false;
    }

    public void onLoadStart() {
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener.onLoadStart();
        }
    }

    public void onLoadFinish(View view) {
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener.onLoadFinish(view);
        }
    }

    public void onLoadError(VesselError vesselError) {
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener.onLoadError(vesselError);
        }
    }

    public void onScrollChanged(View view, int i, int i2, int i3, int i4) {
        if (this.mScrollViewListener != null) {
            this.mScrollViewListener.onScrollChanged(view, i, i2, i3, i4);
        }
    }

    public void onScrollToBottom(View view, int i, int i2) {
        if (this.mScrollViewListener != null) {
            this.mScrollViewListener.onScrollToBottom(view, i, i2);
        }
    }

    public void onScrollToTop(View view, int i, int i2) {
        if (this.mScrollViewListener != null) {
            this.mScrollViewListener.onScrollToTop(view, i, i2);
        }
    }

    public void onScrollRightOrLeftEdge(View view, int i, int i2) {
        if (this.mScrollViewListener != null) {
            this.mScrollViewListener.onScrollRightOrLeftEdge(view, i, i2);
        }
    }

    public boolean onScrollEnabled(View view, boolean z) {
        if (this.mScrollViewListener != null) {
            this.mScrollViewListener.onScrollEnabled(view, z);
        }
        return z;
    }

    public void setEventCallback(EventCallback eventCallback) {
        this.mEventCallback = eventCallback;
    }
}
