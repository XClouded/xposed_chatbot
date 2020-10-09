package alimama.com.unwweex.etaovessel;

import android.content.Context;
import android.taobao.windvane.extra.uc.WVUCWebChromeClient;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.taobao.tao.log.TLog;
import com.taobao.vessel.Vessel;
import com.taobao.vessel.base.VesselBaseView;
import com.taobao.vessel.base.VesselCallbackManager;
import com.taobao.vessel.utils.Utils;
import com.taobao.vessel.web.VesselWebView;
import java.util.Map;

public class UNWVesselWebview extends VesselBaseView {
    private static final String TAG = VesselWebView.class.getSimpleName();
    private VesselViewCallBack callBack;
    private String mOriginJsData;
    private String mUrl;
    /* access modifiers changed from: private */
    public WVUCWebView mWebView;
    private WVUCWebViewClient mWebclient;

    public void onStart() {
    }

    public void onStop() {
    }

    public UNWVesselWebview(Context context) {
        this(context, (AttributeSet) null);
    }

    public UNWVesselWebview(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public UNWVesselWebview(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mWebclient = null;
        init();
    }

    public void setCallBack(VesselViewCallBack vesselViewCallBack) {
        if (this.mWebView != null) {
            this.callBack = vesselViewCallBack;
            this.mWebView.setWebViewClient(new UNWVesselWebClient(getContext(), vesselViewCallBack));
        }
    }

    private void init() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        if (Vessel.getInstance().getWebview() != null) {
            this.mWebView = Vessel.getInstance().getWebview();
        } else {
            this.mWebView = new WVUCWebView(getContext());
        }
        VesselCallbackManager.getInstance().bindCallbackAndView((Object) this.mWebView, (VesselBaseView) this);
        this.mWebView.setWebViewClient(new UNWVesselWebClient(getContext(), this.callBack));
        this.mWebView.setWebChromeClient(new WVUCWebChromeClient(getContext()));
        this.mWebView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (Math.abs(((float) UNWVesselWebview.this.mWebView.getContentHeight()) - ((float) UNWVesselWebview.this.mWebView.getHeight())) == 0.0f && UNWVesselWebview.this.mScrollViewListener != null) {
                    UNWVesselWebview.this.mScrollViewListener.onScrollEnabled(UNWVesselWebview.this.mWebView, false);
                }
                return false;
            }
        });
        if (this.mWebView.getParent() != null) {
            ((ViewGroup) this.mWebView.getParent()).removeView(this.mWebView);
        }
        addView(this.mWebView, layoutParams);
    }

    public void fireEvent(String str, Map<String, Object> map) {
        if (this.mWebView != null) {
            this.mWebView.fireEvent(str, Utils.mapToString(map));
        }
    }

    public void loadUrl(String str, Object obj) {
        this.mUrl = str;
        hideErrorPage();
        this.mWebView.loadUrl(str);
    }

    public void loadData(String str) {
        hideErrorPage();
        this.mOriginJsData = str;
        this.mWebView.loadData(str, "text/html", "utf-8");
    }

    public void onScrollChanged(int i, int i2, int i3, int i4) {
        float contentHeight = (float) this.mWebView.getContentHeight();
        float height = (float) (this.mWebView.getHeight() + this.mWebView.getScrollY());
        if (this.mScrollViewListener == null) {
            return;
        }
        if (Math.abs(contentHeight - height) < 1.0f) {
            this.mScrollViewListener.onScrollToBottom(this.mWebView, i, i2);
        } else if (this.mWebView.getScrollY() == 0) {
            this.mScrollViewListener.onScrollToTop(this.mWebView, i, i2);
        } else {
            this.mScrollViewListener.onScrollChanged(this.mWebView, i, i2, i3, i4);
        }
    }

    @Deprecated
    public void setWebViewClient(WVUCWebViewClient wVUCWebViewClient) {
        if (wVUCWebViewClient != null) {
            this.mWebclient = wVUCWebViewClient;
            if (this.mWebView != null) {
                this.mWebView.setWebViewClient(this.mWebclient);
            }
        }
    }

    public void onDestroy() {
        TLog.logd(TAG, "onDestroy");
        VesselCallbackManager.getInstance().remove(this.mWebView);
        this.mWebView.setVisibility(8);
        this.mWebView.removeAllViews();
        if (this.mWebView.getParent() != null) {
            ((ViewGroup) this.mWebView.getParent()).removeView(this.mWebView);
        }
        this.mWebView.coreDestroy();
        this.mScrollViewListener = null;
    }

    public View getRootView() {
        return this.mWebView;
    }

    private void hideErrorPage() {
        if (this.mWebView != null && this.mWebView.getWvUIModel().getErrorView().isShown()) {
            this.mWebView.getWvUIModel().hideErrorPage();
        }
    }

    private void showErrorPage() {
        if (this.mWebView != null && !this.mWebView.getWvUIModel().getErrorView().isShown()) {
            this.mWebView.getWvUIModel().loadErrorPage();
        }
    }

    public boolean refresh(Object obj) {
        if (this.mWebView == null) {
            return false;
        }
        if (!TextUtils.isEmpty(this.mUrl)) {
            this.mWebView.loadUrl(this.mUrl);
            return true;
        } else if (TextUtils.isEmpty(this.mOriginJsData)) {
            return true;
        } else {
            this.mWebView.loadData(this.mOriginJsData, "text/html", "utf-8");
            return true;
        }
    }

    public void releaseMemory() {
        if (this.mWebView != null) {
            this.mWebView.removeAllViews();
            this.mWebView = null;
        }
        removeAllViews();
        onDestroy();
    }

    public View getChildView() {
        return this.mWebView;
    }

    public void onResume() {
        if (this.mWebView != null && this.mWebView.isLive()) {
            this.mWebView.onResume();
        }
    }

    public void onPause() {
        if (this.mWebView != null && this.mWebView.isLive()) {
            this.mWebView.onPause();
        }
    }
}
