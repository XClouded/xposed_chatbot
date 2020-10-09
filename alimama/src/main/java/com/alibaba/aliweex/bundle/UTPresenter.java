package com.alibaba.aliweex.bundle;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import com.alibaba.aliweex.bundle.WeexPageContract;
import com.alimama.moon.usertrack.UTHelper;
import com.taobao.ju.track.constants.Constants;
import com.taobao.weex.ComponentObserver;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.component.WXComponent;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTPageHitHelper;
import com.ut.mini.internal.ExposureViewHandle;
import com.ut.mini.internal.UTTeamWork;
import java.util.HashMap;

public class UTPresenter implements WeexPageContract.IUTPresenter {
    private Activity mActivity;
    private boolean mEnable;

    public UTPresenter(Activity activity) {
        this.mEnable = true;
        this.mEnable = true;
        this.mActivity = activity;
    }

    public boolean enable() {
        return this.mEnable;
    }

    public void pageAppear(String str) {
        if (getActivity() != null && enable()) {
            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(getActivity());
            UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(getActivity());
            if (!TextUtils.isEmpty(str)) {
                Uri parse = Uri.parse(str);
                UTAnalytics.getInstance().getDefaultTracker().updatePageUrl(getActivity(), parse);
                if (parse.isHierarchical() && parse.getQueryParameter(UTHelper.SCM_URI_PARAMETER_KEY) != null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put(UTHelper.SCM_URI_PARAMETER_KEY, parse.getQueryParameter(UTHelper.SCM_URI_PARAMETER_KEY));
                    UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(getActivity(), hashMap);
                }
            }
        }
    }

    public void pageDisappear() {
        if (getActivity() != null && enable()) {
            UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(getActivity());
        }
    }

    public void refreshUT(String str) {
        if (getActivity() != null && enable()) {
            pageDisappear();
            UTAnalytics.getInstance().getDefaultTracker().skipPage(getActivity());
            pageAppear(str);
        }
    }

    public void skipPage() {
        if (enable()) {
            UTAnalytics.getInstance().getDefaultTracker().skipPage(getActivity());
        }
    }

    public void tryToUpdatePageSpmCnt(WXSDKInstance wXSDKInstance) {
        WXComponent rootComponent;
        if (enable() && (rootComponent = wXSDKInstance.getRootComponent()) != null) {
            String str = (String) rootComponent.getAttrs().get("spmId");
            if (!TextUtils.isEmpty(str)) {
                HashMap hashMap = new HashMap();
                hashMap.put(Constants.PARAM_OUTER_SPM_CNT, str + ".0.0");
                UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(getActivity(), hashMap);
            }
        }
    }

    public void updatePageName(String str) {
        if (enable() && getActivity() != null && !TextUtils.isEmpty(str)) {
            UTAnalytics.getInstance().getDefaultTracker().updatePageName(getActivity(), Uri.parse(str).buildUpon().clearQuery().build().toString());
        }
    }

    public void viewAutoExposure(WXSDKInstance wXSDKInstance) {
        wXSDKInstance.setComponentObserver(new ComponentObserver() {
            public void onCreate(WXComponent wXComponent) {
            }

            public void onPreDestory(WXComponent wXComponent) {
            }

            public void onViewCreated(WXComponent wXComponent, View view) {
                ExposureViewHandle exposureViewHandler;
                if (UTPresenter.this.enable() && (exposureViewHandler = UTTeamWork.getInstance().getExposureViewHandler(UTPresenter.this.getActivity())) != null && exposureViewHandler.isExposureView(UTPageHitHelper.getInstance().getPageUrl(UTPresenter.this.getActivity()), view)) {
                    UTTeamWork.getInstance().setExposureTagForWeex(view);
                }
            }
        });
    }

    public void destroy() {
        this.mEnable = true;
        this.mActivity = null;
    }

    /* access modifiers changed from: private */
    public Activity getActivity() {
        return this.mActivity;
    }
}
