package com.taobao.vessel.local;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import com.taobao.vessel.callback.OnLoadListener;
import com.taobao.vessel.callback.ScrollViewListener;
import com.taobao.vessel.callback.VesselViewCallback;
import com.taobao.vessel.callback.ViewLifeCircle;
import com.taobao.vessel.model.VesselError;
import java.util.Map;

public abstract class VesselNativeFrameLayout extends FrameLayout implements ViewLifeCircle {
    protected OnLoadListener mOnLoadListener;
    protected ScrollViewListener mScrollViewListener;
    protected VesselNativePlugin mVesselNativePlugin;
    protected VesselViewCallback mViewCallback;
    protected Map<String, Object> mfireEventParams;
    protected Object vesselParams;

    @Nullable
    public View getView() {
        return this;
    }

    @Nullable
    public abstract View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle);

    public void onPause() {
    }

    public void onResume() {
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public VesselNativeFrameLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public VesselNativeFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VesselNativeFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mVesselNativePlugin = new VesselNativePlugin();
        this.mVesselNativePlugin.mViewCallback = this.mViewCallback;
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        if (view.getParent() == null) {
            addView(view);
        }
    }

    public Object getVesselParams() {
        return this.vesselParams;
    }

    public void setVesselParams(Object obj) {
        this.vesselParams = obj;
    }

    public void onDestroy() {
        this.mOnLoadListener = null;
        this.mScrollViewListener = null;
        this.mViewCallback = null;
        this.mVesselNativePlugin = null;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.mScrollViewListener = scrollViewListener;
    }

    public void setVesselViewCallback(VesselViewCallback vesselViewCallback) {
        this.mViewCallback = vesselViewCallback;
        if (this.mVesselNativePlugin != null) {
            this.mVesselNativePlugin.mViewCallback = this.mViewCallback;
        }
    }

    public void onFireEvent(Map<String, Object> map) {
        this.mfireEventParams = map;
    }

    public void callVessel(Map<String, Object> map, NativeCallbackContext nativeCallbackContext) {
        if (this.mVesselNativePlugin != null) {
            this.mVesselNativePlugin.execute(map, nativeCallbackContext);
        }
    }

    public void notifyPageFinish(View view) {
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener.onLoadFinish(view);
        }
    }

    public void notityPageError(VesselError vesselError) {
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener.onLoadError(vesselError);
        }
    }

    public void notifyPositionOnBottom(int i, int i2) {
        if (this.mScrollViewListener != null) {
            this.mScrollViewListener.onScrollToBottom(this, i, i2);
        }
    }

    public void notifyPositionOnTop(int i, int i2) {
        if (this.mScrollViewListener != null) {
            this.mScrollViewListener.onScrollToBottom(this, i, i2);
        }
    }

    public void notifyPositionOnScroll(int i, int i2, int i3, int i4) {
        if (this.mScrollViewListener != null) {
            this.mScrollViewListener.onScrollChanged(this, i, i2, i3, i4);
        }
    }

    public void notifyPositionOnRightOrLeftEdge(int i, int i2) {
        if (this.mScrollViewListener != null) {
            this.mScrollViewListener.onScrollRightOrLeftEdge(this, i, i2);
        }
    }
}
