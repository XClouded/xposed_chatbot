package alimama.com.unwweex.etaovessel;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.taobao.vessel.base.VesselBaseView;
import com.taobao.vessel.base.VesselCallbackManager;
import com.taobao.vessel.model.VesselError;
import com.taobao.vessel.utils.Utils;
import com.taobao.vessel.utils.VesselConstants;
import com.taobao.vessel.utils.VesselType;
import com.taobao.vessel.weex.VesselWeexModule;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXException;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.ui.view.WXScrollView;
import java.util.HashMap;
import java.util.Map;

public class UNWWeexView extends VesselBaseView implements IWXRenderListener, WXScrollView.WXScrollViewListener {
    private static final String TAG = "UNWWeexView";
    private VesselViewCallBack callBack;
    private boolean isViewLoaded;
    private Handler mHandler;
    private String mOriginJsBundleData;
    /* access modifiers changed from: private */
    public String mRequestUrl;
    /* access modifiers changed from: private */
    public WXSDKInstance mTBWXSDKInstance;
    private View weexView;

    public void onRefreshSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
    }

    public void onScroll(WXScrollView wXScrollView, int i, int i2) {
    }

    public void onScrollStopped(WXScrollView wXScrollView, int i, int i2) {
    }

    public UNWWeexView(Context context) {
        this(context, (AttributeSet) null);
    }

    public UNWWeexView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public UNWWeexView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isViewLoaded = false;
        this.mHandler = new Handler();
        initModule();
    }

    private void initModule() {
        try {
            WXSDKEngine.registerModule("vessel", VesselWeexModule.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

    public void fireEvent(String str, Map<String, Object> map) {
        if (this.mTBWXSDKInstance != null) {
            this.mTBWXSDKInstance.fireGlobalEventCallback(str, map);
        }
    }

    public void onViewCreated(WXSDKInstance wXSDKInstance, View view) {
        removeAllViews();
        addView(view, new FrameLayout.LayoutParams(-1, -1));
        this.weexView = view;
        if (this.mTBWXSDKInstance != null && this.mScrollViewListener != null && this.mTBWXSDKInstance.getScrollView() == null) {
            this.mScrollViewListener.onScrollEnabled(this.weexView, false);
        }
    }

    public void loadUrl(String str, Object obj) {
        this.mRequestUrl = str;
        this.mOriginUrl = str;
        this.mOriginParams = obj;
        createWxSdkInstance();
        if (this.callBack != null) {
            this.callBack.onLoadStart(this.weexView, str);
        }
        onLoadStart();
        String weexTemplateUrl = Utils.getWeexTemplateUrl(str);
        HashMap hashMap = new HashMap();
        hashMap.put("bundleUrl", this.mRequestUrl);
        if (this.mHandler != null) {
            this.mHandler.post(new WxJsBundleRunnable(weexTemplateUrl, hashMap));
        }
    }

    public void loadData(VesselType vesselType, String str, Map map) {
        this.mOriginJsBundleData = str;
        createWxSdkInstance();
        if (str != null) {
            startWxRender(str, map);
        } else if (this.callBack != null) {
            this.callBack.onLoadError(new VesselError(VesselConstants.LOAD_ERROR, VesselConstants.LOAD_DATA_NULL, VesselConstants.WEEX_TYPE));
        }
    }

    public void setCallBack(VesselViewCallBack vesselViewCallBack) {
        this.callBack = vesselViewCallBack;
    }

    public boolean refresh(Object obj) {
        if (!TextUtils.isEmpty(this.mOriginUrl)) {
            loadUrl(this.mOriginUrl, this.mOriginParams);
            return true;
        } else if (TextUtils.isEmpty(this.mOriginJsBundleData)) {
            return false;
        } else {
            loadData(this.mOriginJsBundleData);
            return true;
        }
    }

    public void releaseMemory() {
        onDestroy();
        this.weexView = null;
        removeAllViews();
    }

    public View getChildView() {
        return this.weexView;
    }

    public void startWxRender(String str, Map<String, Object> map) {
        if (!TextUtils.isEmpty(str) && this.mHandler != null) {
            this.mHandler.post(new WxJsDataRunnable(str, map));
        }
    }

    private void createWxSdkInstance() {
        if (this.mTBWXSDKInstance != null) {
            VesselCallbackManager.getInstance().remove(this.mTBWXSDKInstance);
            this.mTBWXSDKInstance.destroy();
        }
        this.mTBWXSDKInstance = new WXSDKInstance(getContext());
        this.mTBWXSDKInstance.registerRenderListener(this);
        this.mTBWXSDKInstance.registerScrollViewListener(this);
        this.mTBWXSDKInstance.onActivityCreate();
        VesselCallbackManager.getInstance().bindCallbackAndView((Object) this.mTBWXSDKInstance, (VesselBaseView) this);
    }

    public void onStart() {
        if (this.mTBWXSDKInstance != null) {
            this.mTBWXSDKInstance.onActivityStart();
        }
    }

    public void onResume() {
        if (this.mTBWXSDKInstance != null) {
            this.mTBWXSDKInstance.onActivityResume();
        }
    }

    public void onPause() {
        if (this.mTBWXSDKInstance != null) {
            this.mTBWXSDKInstance.onActivityPause();
        }
    }

    public void onStop() {
        if (this.mTBWXSDKInstance != null) {
            this.mTBWXSDKInstance.onActivityStop();
        }
    }

    public void onDestroy() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        VesselCallbackManager.getInstance().remove(this.mTBWXSDKInstance);
        if (this.mTBWXSDKInstance != null) {
            this.mTBWXSDKInstance.registerScrollViewListener((WXScrollView.WXScrollViewListener) null);
            this.mTBWXSDKInstance.onActivityDestroy();
        }
        this.mScrollViewListener = null;
    }

    public void onAppear() {
        if (this.mTBWXSDKInstance != null && this.isViewLoaded) {
            this.mTBWXSDKInstance.onViewAppear();
        }
    }

    public void onDisappear() {
        if (this.mTBWXSDKInstance != null && this.isViewLoaded) {
            this.mTBWXSDKInstance.onViewDisappear();
        }
    }

    public void onRenderSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
        this.isViewLoaded = true;
        if (this.callBack != null) {
            this.callBack.onLoadFinish(this.weexView, this.mRequestUrl);
        }
        onLoadFinish(this.weexView);
    }

    public void onException(WXSDKInstance wXSDKInstance, String str, String str2) {
        if (this.callBack != null) {
            this.callBack.onLoadError(new VesselError(str, str2, VesselConstants.WEEX_TYPE));
        }
        onLoadError(new VesselError(str, str2, VesselConstants.WEEX_TYPE));
    }

    public void onScrollChanged(WXScrollView wXScrollView, int i, int i2, int i3, int i4) {
        if (this.mScrollViewListener == null) {
            return;
        }
        if (wXScrollView.getHeight() - wXScrollView.getScrollY() < 1) {
            this.mScrollViewListener.onScrollToBottom(wXScrollView, i, i2);
        } else if (wXScrollView.getScrollY() == 0) {
            this.mScrollViewListener.onScrollToTop(wXScrollView, i, i2);
        } else {
            this.mScrollViewListener.onScrollChanged(wXScrollView, i, i2, i3, i4);
        }
    }

    public void onScrollToBottom(WXScrollView wXScrollView, int i, int i2) {
        if (this.mScrollViewListener != null) {
            this.mScrollViewListener.onScrollToBottom(wXScrollView, i, i2);
        }
    }

    class WxJsDataRunnable implements Runnable {
        private String jsData;
        private Map<String, Object> wxOptions;

        public WxJsDataRunnable(String str, Map<String, Object> map) {
            this.jsData = str;
            this.wxOptions = map;
        }

        public void run() {
            if (!Utils.checkActivityDestroy(UNWWeexView.this.getContext()) && UNWWeexView.this.mTBWXSDKInstance != null && !TextUtils.isEmpty(this.jsData)) {
                UNWWeexView.this.mTBWXSDKInstance.render(Utils.getPageNameFromOptions(this.wxOptions), this.jsData, this.wxOptions, (String) null, WXRenderStrategy.APPEND_ASYNC);
            }
        }
    }

    class WxJsBundleRunnable implements Runnable {
        private Map<String, Object> wxOptions;
        private String wxUrl;

        public WxJsBundleRunnable(String str, Map<String, Object> map) {
            this.wxUrl = str;
            this.wxOptions = map;
        }

        public void run() {
            if (!Utils.checkActivityDestroy(UNWWeexView.this.getContext()) && UNWWeexView.this.mTBWXSDKInstance != null && !TextUtils.isEmpty(this.wxUrl)) {
                UNWWeexView.this.mTBWXSDKInstance.renderByUrl(UNWWeexView.this.mRequestUrl, this.wxUrl, this.wxOptions, (String) null, WXRenderStrategy.APPEND_ASYNC);
            }
        }
    }
}
