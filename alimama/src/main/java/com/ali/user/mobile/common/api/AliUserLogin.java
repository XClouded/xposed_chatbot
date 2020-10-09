package com.ali.user.mobile.common.api;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.filter.LoginFilter;
import com.ali.user.mobile.filter.OnActivityResultHandler;
import com.ali.user.mobile.filter.PreLoginFilter;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.model.BindParam;
import com.ali.user.mobile.rpc.h5.MtopAccountCenterUrlResponseData;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.ui.widget.WidgetExtension;
import com.ali.user.mobile.url.model.AccountCenterParam;
import com.ali.user.mobile.url.service.impl.UrlFetchServiceImpl;
import com.taobao.login4android.constants.LoginSceneConstants;

public class AliUserLogin {
    private static final String TAG = "Login.AliUserLogin";
    private static volatile AliUserLogin aliUserLogin;
    public static LoginApprearanceExtensions mAppreanceExtentions;
    public static OnBindCaller mBindCaller;
    public static PreLoginFilter mFindPwdFilter;
    public static OnLoginCaller mLoginCaller;
    public static LoginFilter mLoginFilter;
    public static OnActivityResultHandler mOnActivityResultHandler;
    public static PreLoginFilter mPreLoginFiler;
    private String mApiRefer;

    private AliUserLogin() {
    }

    public static AliUserLogin getInstance() {
        if (aliUserLogin == null) {
            synchronized (AliUserLogin.class) {
                if (aliUserLogin == null) {
                    aliUserLogin = new AliUserLogin();
                }
            }
        }
        return aliUserLogin;
    }

    public static void registOnLoginCaller(Context context, OnLoginCaller onLoginCaller) {
        if (mLoginCaller == null) {
            mLoginCaller = onLoginCaller;
        }
    }

    public static void setLoginFilter(LoginFilter loginFilter) {
        mLoginFilter = loginFilter;
    }

    public static void setLoginAppreanceExtions(LoginApprearanceExtensions loginApprearanceExtensions) {
        mAppreanceExtentions = loginApprearanceExtensions;
        WidgetExtension.widgetExtension = loginApprearanceExtensions;
    }

    public void bind(final Context context, final BindParam bindParam, final OnBindCaller onBindCaller) {
        preCheckBindParam(bindParam);
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, String>() {
            public String doInBackground(Object... objArr) {
                try {
                    AccountCenterParam accountCenterParam = new AccountCenterParam();
                    accountCenterParam.fromSite = DataProviderFactory.getDataProvider().getSite();
                    accountCenterParam.scene = LoginSceneConstants.SCNEN_BINDALIPAY;
                    MtopAccountCenterUrlResponseData navByScene = UrlFetchServiceImpl.getInstance().navByScene(accountCenterParam);
                    if (navByScene != null) {
                        return navByScene.h5Url;
                    }
                    return null;
                } catch (Throwable th) {
                    th.printStackTrace();
                    return null;
                }
            }

            public void onPostExecute(String str) {
                if (!TextUtils.isEmpty(str)) {
                    AliUserLogin.mBindCaller = onBindCaller;
                    ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openAccountBindPage(context, str + "?" + bindParam.toString());
                } else if (onBindCaller != null) {
                    onBindCaller.onBindError((Bundle) null);
                }
            }
        }, new Object[0]);
    }

    private void preCheckBindParam(BindParam bindParam) {
        bindParam.appKey = DataProviderFactory.getDataProvider().getAppkey();
        bindParam.apdid = AlipayInfo.getInstance().getApdid();
    }
}
