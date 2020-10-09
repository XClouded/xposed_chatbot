package com.taobao.vessel;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.taobao.tao.log.TLog;
import com.taobao.vessel.base.VesselBaseView;
import com.taobao.vessel.callback.DowngradeListener;
import com.taobao.vessel.callback.OnLoadListener;
import com.taobao.vessel.callback.ScrollViewListener;
import com.taobao.vessel.callback.VesselViewCallback;
import com.taobao.vessel.local.VesselNativeView;
import com.taobao.vessel.model.VesselError;
import com.taobao.vessel.utils.Utils;
import com.taobao.vessel.utils.VesselConstants;
import com.taobao.vessel.utils.VesselType;
import com.taobao.vessel.web.VesselWebView;
import com.taobao.vessel.weex.VesselWeexView;
import java.util.HashMap;
import java.util.Map;

public class VesselView extends VesselParentView {
    protected boolean mDowngradeEnable = true;

    public /* bridge */ /* synthetic */ boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return super.dispatchTouchEvent(motionEvent);
    }

    public /* bridge */ /* synthetic */ void fireEvent(String str, Map map) {
        super.fireEvent(str, map);
    }

    public /* bridge */ /* synthetic */ View getChildView() {
        return super.getChildView();
    }

    @Deprecated
    public /* bridge */ /* synthetic */ View getVesselView() {
        return super.getVesselView();
    }

    public /* bridge */ /* synthetic */ void onDestroy() {
        super.onDestroy();
    }

    public /* bridge */ /* synthetic */ boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return super.onInterceptTouchEvent(motionEvent);
    }

    public /* bridge */ /* synthetic */ void onPause() {
        super.onPause();
    }

    public /* bridge */ /* synthetic */ void onResume() {
        super.onResume();
    }

    public /* bridge */ /* synthetic */ boolean onScrollEnabled(View view, boolean z) {
        return super.onScrollEnabled(view, z);
    }

    public /* bridge */ /* synthetic */ void onScrollRightOrLeftEdge(View view, int i, int i2) {
        super.onScrollRightOrLeftEdge(view, i, i2);
    }

    public /* bridge */ /* synthetic */ void onScrollToBottom(View view, int i, int i2) {
        super.onScrollToBottom(view, i, i2);
    }

    public /* bridge */ /* synthetic */ void onScrollToTop(View view, int i, int i2) {
        super.onScrollToTop(view, i, i2);
    }

    public /* bridge */ /* synthetic */ void onStart() {
        super.onStart();
    }

    public /* bridge */ /* synthetic */ void onStop() {
        super.onStop();
    }

    public /* bridge */ /* synthetic */ boolean refresh(Object obj) {
        return super.refresh(obj);
    }

    public /* bridge */ /* synthetic */ void releaseMemory() {
        super.releaseMemory();
    }

    public /* bridge */ /* synthetic */ void setDowngradeUrl(String str) {
        super.setDowngradeUrl(str);
    }

    public /* bridge */ /* synthetic */ void setInterceptException(boolean z) {
        super.setInterceptException(z);
    }

    public /* bridge */ /* synthetic */ void setOnLoadListener(OnLoadListener onLoadListener) {
        super.setOnLoadListener(onLoadListener);
    }

    public /* bridge */ /* synthetic */ void setOnScrollViewListener(ScrollViewListener scrollViewListener) {
        super.setOnScrollViewListener(scrollViewListener);
    }

    public /* bridge */ /* synthetic */ void setShowLoading(boolean z) {
        super.setShowLoading(z);
    }

    public /* bridge */ /* synthetic */ void setVesselViewCallback(VesselViewCallback vesselViewCallback) {
        super.setVesselViewCallback(vesselViewCallback);
    }

    public VesselView(Context context) {
        super(context);
    }

    public VesselView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public VesselView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void loadUrl(String str) {
        loadUrl(str, (Object) null);
    }

    public void loadUrl(String str, Object obj) {
        loadUrl((VesselType) null, str, obj);
    }

    public void loadUrl(VesselType vesselType, String str, Object obj) {
        if (vesselType == null) {
            vesselType = Utils.getUrlType(str);
        }
        if (vesselType == null) {
            onLoadError(new VesselError());
            return;
        }
        this.mOriginUrl = str;
        this.mOriginParams = obj;
        this.mCurrentVesselType = vesselType;
        this.mProxyView = createView(getContext(), vesselType);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        if (this.mProxyView.getParent() == null) {
            addView(this.mProxyView, layoutParams);
        }
        if (this.mVesselViewCallback != null) {
            this.mProxyView.setVesselViewCallback(this.mVesselViewCallback);
        }
        this.mProxyView.mInstanceId = this.mInstanceId;
        this.mProxyView.setOnLoadListener(this);
        this.mProxyView.loadUrl(str, obj);
        this.mProxyView.setOnScrollViewListener(this);
    }

    public View getChildProxyView() {
        return this.mProxyView;
    }

    public void setDowngradeEnable(boolean z) {
        this.mDowngradeEnable = z;
    }

    public void onLoadStart() {
        super.onLoadStart();
        if (this.mNoDataMaskView != null) {
            this.mNoDataMaskView.startLoading();
        }
    }

    public void loadData(VesselType vesselType, String str, Map<String, Object> map) {
        if (vesselType == null) {
            vesselType = VesselType.Weex;
        }
        this.mCurrentVesselType = vesselType;
        if (this.mProxyView == null) {
            this.mProxyView = createView(getContext(), vesselType);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        if (this.mProxyView.getParent() == null) {
            addView(this.mProxyView, layoutParams);
        }
        if (this.mVesselViewCallback != null) {
            this.mProxyView.setVesselViewCallback(this.mVesselViewCallback);
        }
        this.mProxyView.mInstanceId = this.mInstanceId;
        this.mProxyView.setOnLoadListener(this);
        this.mProxyView.loadData(vesselType, str, map);
        this.mProxyView.setOnScrollViewListener(this);
    }

    public void onLoadFinish(View view) {
        super.onLoadFinish(view);
        if (this.mNoDataMaskView != null) {
            this.mNoDataMaskView.finish();
        }
    }

    public void onLoadError(VesselError vesselError) {
        String str;
        TLog.logd(TAG, "onLoadError");
        if (vesselError != null) {
            str = vesselError.errorMsg + "URL =" + this.mOriginUrl;
        } else {
            str = VesselConstants.LOAD_ERROR;
        }
        Utils.commitFail(VesselConstants.LOAD_ERROR, str);
        if (this.mDowngradeEnable && this.mCurrentVesselType != VesselType.Web) {
            if (this.mOnLoadListener != null && (this.mOnLoadListener instanceof DowngradeListener)) {
                ((DowngradeListener) this.mOnLoadListener).beforeDowngrade(this.mCurrentVesselType, getDowngradeType());
            }
            if (downgrade(this.mOriginUrl)) {
                HashMap hashMap = new HashMap();
                hashMap.put("url", this.mDowngradeUrl);
                if (this.mOnLoadListener != null) {
                    this.mOnLoadListener.onDowngrade(vesselError, hashMap);
                }
            } else if (this.mOnLoadListener != null) {
                this.mOnLoadListener.onLoadError(vesselError);
            }
        } else if (this.mOnLoadListener != null) {
            this.mOnLoadListener.onLoadError(vesselError);
        }
    }

    private VesselType getDowngradeType() {
        return Utils.getUrlType(TextUtils.isEmpty(this.mDowngradeUrl) ? Utils.parseUrlDowngradeParamter(this.mOriginUrl) : this.mDowngradeUrl);
    }

    public boolean downgrade(String str) {
        String str2 = TAG;
        TLog.logd(str2, "downgrade url:" + str);
        if (str == null) {
            return false;
        }
        removeAllViews();
        this.mProxyView.onDestroy();
        this.mProxyView = null;
        if (!TextUtils.isEmpty(this.mDowngradeUrl)) {
            createView(this.mDowngradeUrl);
            return true;
        } else if (this.mCurrentVesselType == VesselType.Weex) {
            this.mCurrentVesselType = VesselType.Web;
            loadUrl(this.mCurrentVesselType, str, (Object) null);
            return true;
        } else {
            String parseUrlDowngradeParamter = Utils.parseUrlDowngradeParamter(this.mOriginUrl);
            if (this.mCurrentVesselType != VesselType.Native || parseUrlDowngradeParamter == null) {
                return false;
            }
            this.mCurrentVesselType = Utils.getUrlType(parseUrlDowngradeParamter);
            this.mDowngradeUrl = parseUrlDowngradeParamter;
            loadUrl(this.mCurrentVesselType, str, (Object) null);
            return true;
        }
    }

    private VesselBaseView createView(Context context, VesselType vesselType) {
        switch (vesselType) {
            case Weex:
                return new VesselWeexView(context);
            case Web:
                return new VesselWebView(context);
            case Native:
                return new VesselNativeView(context);
            default:
                return new VesselWebView(context);
        }
    }
}
