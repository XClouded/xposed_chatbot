package com.taobao.vessel;

import android.content.Context;
import android.graphics.Canvas;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.taobao.vessel.base.VesselBaseView;
import com.taobao.vessel.callback.OnLoadListener;
import com.taobao.vessel.callback.ScrollViewListener;
import com.taobao.vessel.callback.VesselViewCallback;
import com.taobao.vessel.model.VesselError;
import com.taobao.vessel.utils.VesselConstants;
import com.taobao.vessel.utils.VesselType;
import com.taobao.vessel.web.VesselWebApiPlugin;
import com.taobao.vessel.widget.NoDataMaskView;
import java.util.Map;

class VesselParentView extends VesselBaseView {
    protected static final String TAG = VesselView.class.getSimpleName();
    protected boolean IsShowLoading;
    protected boolean flag;
    protected boolean interceptException;
    protected VesselType mCurrentVesselType;
    protected NoDataMaskView mNoDataMaskView;
    protected VesselBaseView mProxyView;

    public void loadUrl(String str, Object obj) {
    }

    public VesselParentView(Context context) {
        this(context, (AttributeSet) null);
    }

    public VesselParentView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VesselParentView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.flag = true;
        this.interceptException = true;
        registerWindVane();
        initNodataMaskView();
    }

    private void initNodataMaskView() {
        if (this.IsShowLoading && this.mNoDataMaskView == null) {
            this.mNoDataMaskView = new NoDataMaskView(getContext());
            this.mNoDataMaskView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            addView(this.mNoDataMaskView);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (needIntercept(motionEvent)) {
            if (motionEvent.getAction() == 0) {
                setParentScrollAble(true);
            } else if (motionEvent.getAction() != 1 && motionEvent.getAction() == 2 && this.flag) {
                setParentScrollAble(false);
            }
            this.flag = false;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Deprecated
    public View getVesselView() {
        return this.mProxyView;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (this.interceptException) {
            try {
                super.dispatchDraw(canvas);
            } catch (Exception e) {
                onLoadError(new VesselError(e.getMessage(), e.getMessage(), VesselConstants.NATIVE_TYPE));
            }
        } else {
            super.dispatchDraw(canvas);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.interceptException) {
            try {
                return super.dispatchTouchEvent(motionEvent);
            } catch (Exception e) {
                onLoadError(new VesselError(e.getMessage(), e.getMessage(), VesselConstants.NATIVE_TYPE));
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void fireEvent(String str, Map<String, Object> map) {
        if (this.mProxyView != null) {
            this.mProxyView.fireEvent(str, map);
        }
    }

    public void onScrollToTop(View view, int i, int i2) {
        super.onScrollToTop(view, i, i2);
        setParentScrollAble(false);
        this.flag = true;
    }

    public void onScrollToBottom(View view, int i, int i2) {
        super.onScrollToBottom(view, i, i2);
        setParentScrollAble(false);
        this.flag = true;
    }

    public void onScrollRightOrLeftEdge(View view, int i, int i2) {
        super.onScrollRightOrLeftEdge(view, i, i2);
        setParentScrollAble(false);
        this.flag = true;
    }

    public boolean onScrollEnabled(View view, boolean z) {
        this.flag = true;
        return super.onScrollEnabled(view, z);
    }

    public boolean refresh(Object obj) {
        if (this.mProxyView != null) {
            return this.mProxyView.refresh(obj);
        }
        return false;
    }

    public void releaseMemory() {
        if (this.mProxyView != null) {
            this.mProxyView.releaseMemory();
            this.mProxyView = null;
        }
        removeAllViews();
    }

    public View getChildView() {
        return this.mProxyView;
    }

    public void setDowngradeUrl(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mDowngradeUrl = str;
        }
    }

    public void setVesselViewCallback(VesselViewCallback vesselViewCallback) {
        super.setVesselViewCallback(vesselViewCallback);
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        super.setOnLoadListener(onLoadListener);
    }

    public void setOnScrollViewListener(ScrollViewListener scrollViewListener) {
        super.setOnScrollViewListener(scrollViewListener);
    }

    public void setInterceptException(boolean z) {
        this.interceptException = z;
    }

    public void setShowLoading(boolean z) {
        this.IsShowLoading = z;
    }

    /* access modifiers changed from: protected */
    public void setParentScrollAble(boolean z) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(z);
        }
    }

    public void onStart() {
        if (this.mProxyView != null) {
            this.mProxyView.onStart();
        }
    }

    public void onResume() {
        if (this.mProxyView != null) {
            this.mProxyView.onResume();
        }
    }

    public void onPause() {
        if (this.mProxyView != null) {
            this.mProxyView.onPause();
        }
    }

    public void onStop() {
        if (this.mProxyView != null) {
            this.mProxyView.onStop();
        }
    }

    public void onDestroy() {
        if (this.mProxyView != null) {
            this.mProxyView.onDestroy();
        }
    }

    private void registerWindVane() {
        WVPluginManager.registerPlugin(VesselConstants.JS_BRIDGE_NAME, (Class<? extends WVApiPlugin>) VesselWebApiPlugin.class);
    }
}
