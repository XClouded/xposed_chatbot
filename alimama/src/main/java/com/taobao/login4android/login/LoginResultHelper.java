package com.taobao.login4android.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.base.helper.LoginDataHelper;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.UrlParam;
import com.ali.user.mobile.rpc.login.model.AliUserResponseData;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.ServiceFactory;
import com.alibaba.fastjson.JSON;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.login4android.session.ISession;
import com.taobao.login4android.thread.LoginAsyncTask;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LoginResultHelper {
    public static final String TAG = "Login.LoginResultHelper";

    public static void saveLoginData(final LoginReturnData loginReturnData, final ISession iSession) {
        new CoordinatorWrapper().execute(new LoginAsyncTask<Object, Void, Void>() {
            public Void excuteTask(Object... objArr) {
                if (loginReturnData.data == null) {
                    return null;
                }
                if (Debuggable.isDebug()) {
                    LoginTLogAdapter.d(LoginAsyncTask.TAG, "LoginResponse Data=" + loginReturnData.data);
                }
                try {
                    AliUserResponseData aliUserResponseData = (AliUserResponseData) JSON.parseObject(loginReturnData.data, AliUserResponseData.class);
                    if (!(aliUserResponseData.loginServiceExt == null || iSession == null)) {
                        iSession.setExtJson(JSON.toJSONString(aliUserResponseData.loginServiceExt));
                    }
                    LoginDataHelper.onLoginSuccess(loginReturnData, aliUserResponseData, iSession);
                    LoginDataHelper.handleHistory(loginReturnData, iSession, aliUserResponseData);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    Properties properties = new Properties();
                    properties.setProperty("username", loginReturnData.showLoginId);
                    properties.setProperty("errorCode", e.getMessage());
                    if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
                        properties.setProperty("appName", DataProviderFactory.getDataProvider().getAppkey());
                    }
                    UserTrackAdapter.sendUT("Event_LoginFail", properties);
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_SUCCESS_ACTION));
            }
        }, new Object[0]);
    }

    public static void gotoH5PlaceHolder(Context context, LoginReturnData loginReturnData, LoginParam loginParam) {
        if (context != null && !TextUtils.isEmpty(loginReturnData.h5Url)) {
            LoginParam loginParam2 = new LoginParam();
            loginParam2.scene = loginReturnData.scene;
            loginParam2.token = loginReturnData.token;
            loginParam2.isFromRegister = false;
            loginParam2.isFoundPassword = false;
            loginParam2.h5QueryString = null;
            if (loginReturnData.extMap != null) {
                if (loginParam2.externParams == null) {
                    loginParam2.externParams = loginReturnData.extMap;
                } else {
                    loginParam2.externParams = new HashMap();
                    for (Map.Entry next : loginReturnData.extMap.entrySet()) {
                        loginParam2.externParams.put(next.getKey(), next.getValue());
                    }
                }
            }
            if (Debuggable.isDebug()) {
                TLogAdapter.d(TAG, "showLoginId = " + loginReturnData.showLoginId);
            }
            UrlParam urlParam = new UrlParam();
            urlParam.url = loginReturnData.h5Url;
            urlParam.token = loginReturnData.token;
            urlParam.scene = loginReturnData.scene;
            urlParam.requestCode = 257;
            urlParam.loginParam = loginParam2;
            if (ServiceFactory.getService(NavigatorService.class) == null) {
                TLogAdapter.e(TAG, "NavigationService is null!");
            } else if (context instanceof Activity) {
                ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).startWebViewForResult((Activity) context, urlParam);
            } else {
                ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openWebViewPage(context, urlParam);
            }
        }
    }
}
