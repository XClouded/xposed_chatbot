package com.alibaba.aliweex.bundle;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.alibaba.aliweex.R;
import com.alibaba.aliweex.bundle.WeexPageContract;
import com.alibaba.aliweex.bundle.WeexPageFragment;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import java.util.HashMap;
import org.apache.commons.cli.HelpFormatter;

public class WXRenderListener implements IWXRenderListener {
    private WeexPageContract.IErrorView mErrorView;
    private WeexPageContract.IProgressBar mProgressBarView;
    private WeexPageFragment.WXRenderListenerAdapter mRenderListener;
    private ViewGroup mRootView;
    private WeexPageFragment.WXRenderListenerAdapter mSelfListener;
    private WeexPageContract.IUTPresenter mUTPresenter;

    public WeexPageFragment.WXRenderListenerAdapter getRenderListener() {
        return this.mRenderListener;
    }

    public void setRenderListener(WeexPageFragment.WXRenderListenerAdapter wXRenderListenerAdapter) {
        this.mRenderListener = wXRenderListenerAdapter;
    }

    public WXRenderListener(ViewGroup viewGroup, WeexPageContract.IProgressBar iProgressBar, WeexPageContract.IUTPresenter iUTPresenter, WeexPageFragment.WXRenderListenerAdapter wXRenderListenerAdapter, WeexPageFragment.WXRenderListenerAdapter wXRenderListenerAdapter2) {
        this.mRootView = viewGroup;
        this.mProgressBarView = iProgressBar;
        this.mUTPresenter = iUTPresenter;
        this.mRenderListener = wXRenderListenerAdapter;
        this.mSelfListener = wXRenderListenerAdapter2;
    }

    public void setErrorView(WeexPageContract.IErrorView iErrorView) {
        this.mErrorView = iErrorView;
    }

    public void onViewCreated(WXSDKInstance wXSDKInstance, View view) {
        WXLogUtils.d("WXRenderListener", "into--[onViewCreated]");
        if (this.mRootView != null) {
            if (needWrapRenderView()) {
                removeWrappedView(view);
                View onCreateView = this.mRenderListener != null ? this.mRenderListener.onCreateView(wXSDKInstance, view) : view;
                onCreateView.setId(R.id.weex_render_view);
                if (this.mRootView.getParent() instanceof FrameLayout) {
                    ((ViewGroup) this.mRootView.getParent()).addView(onCreateView);
                } else {
                    this.mRootView.addView(onCreateView);
                }
            } else if (view.getParent() == null) {
                if (this.mRootView.getChildCount() > 2) {
                    this.mRootView.removeViewAt(2);
                }
                this.mRootView.addView(view);
            }
            this.mProgressBarView.showProgressBar(false);
            this.mSelfListener.onViewCreated(wXSDKInstance, view);
            if (this.mRenderListener != null) {
                this.mRenderListener.onViewCreated(wXSDKInstance, view);
            }
            if (this.mUTPresenter != null) {
                this.mUTPresenter.tryToUpdatePageSpmCnt(wXSDKInstance);
            }
        }
    }

    public void onRenderSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
        WXLogUtils.d("WXRenderListener", "into--[onRenderSuccess]");
        if (this.mRenderListener != null) {
            this.mRenderListener.onRenderSuccess(wXSDKInstance, i, i2);
        }
        if (this.mProgressBarView != null) {
            this.mProgressBarView.showProgressBar(false);
        }
    }

    public void onRefreshSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
        WXLogUtils.d("WXRenderListener", "into--[onRefreshSuccess]");
        if (this.mProgressBarView != null) {
            this.mProgressBarView.showProgressBar(false);
        }
    }

    public void onException(WXSDKInstance wXSDKInstance, String str, String str2) {
        boolean z;
        WXLogUtils.d("WXRenderListener", "into--[onException] errCode:" + str + " msg:" + str2);
        if (TextUtils.equals(str, WXErrorCode.WX_DEGRAD_ERR_NETWORK_BUNDLE_DOWNLOAD_FAILED.getErrorCode())) {
            this.mErrorView.createErrorView(wXSDKInstance.getContext(), this.mRootView);
            this.mErrorView.showErrorView(true, "网络错误，点击刷新重试！");
            reportDownLoadError(wXSDKInstance, str2);
            z = false;
        } else {
            z = WeexPageFragment.shouldDegrade(wXSDKInstance, str, str2);
        }
        if (WXEnvironment.isApkDebugable()) {
            Toast.makeText(wXSDKInstance.getContext(), str2, 1).show();
        }
        this.mProgressBarView.showProgressBar(false);
        this.mSelfListener.onException(wXSDKInstance, str, str2);
        if (this.mRenderListener != null) {
            this.mRenderListener.onException(wXSDKInstance, z, str, str2);
        }
    }

    private void reportDownLoadError(WXSDKInstance wXSDKInstance, String str) {
        int indexOf;
        HashMap hashMap = new HashMap(1);
        if (!TextUtils.isEmpty(str) && str.contains("networkMsg==") && str.contains("networkErrorCode") && (indexOf = str.indexOf("|mWXResponse")) > 0) {
            String substring = str.substring(0, indexOf);
            hashMap.put("wxErrorMsgDetail", str);
            str = substring;
        }
        String instanceId = wXSDKInstance.getInstanceId();
        WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_JS_DOWNLOAD_FAILED;
        WXExceptionUtils.commitCriticalExceptionRT(instanceId, wXErrorCode, "WXRenderListener.onException", WXErrorCode.WX_KEY_EXCEPTION_JS_DOWNLOAD_FAILED.getErrorMsg() + HelpFormatter.DEFAULT_LONG_OPT_PREFIX + str, hashMap);
    }

    private boolean needWrapRenderView() {
        return this.mRenderListener != null && this.mRenderListener.needWrapper();
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void removeWrappedView(android.view.View r3) {
        /*
            r2 = this;
            android.view.ViewGroup r0 = r2.mRootView
            android.view.ViewParent r0 = r0.getParent()
            if (r0 == 0) goto L_0x0020
            boolean r1 = r0 instanceof android.view.ViewGroup
            if (r1 == 0) goto L_0x0020
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0
            int r1 = r0.getChildCount()
            if (r1 <= 0) goto L_0x0020
            int r1 = com.alibaba.aliweex.R.id.weex_render_view
            android.view.View r0 = r0.findViewById(r1)
            if (r0 == 0) goto L_0x0021
            r2.removeSelf(r0)
            goto L_0x0021
        L_0x0020:
            r0 = 0
        L_0x0021:
            if (r0 != 0) goto L_0x0032
            android.view.ViewGroup r0 = r2.mRootView
            int r1 = com.alibaba.aliweex.R.id.weex_render_view
            android.view.View r0 = r0.findViewById(r1)
            if (r0 == 0) goto L_0x0032
            android.view.ViewGroup r1 = r2.mRootView
            r1.removeView(r0)
        L_0x0032:
            if (r0 != 0) goto L_0x0037
            r2.removeSelf(r3)
        L_0x0037:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.bundle.WXRenderListener.removeWrappedView(android.view.View):void");
    }

    private void removeSelf(View view) {
        if (view != null && view.getParent() != null && (view.getParent() instanceof ViewGroup)) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
