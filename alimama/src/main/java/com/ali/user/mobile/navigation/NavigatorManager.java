package com.ali.user.mobile.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.ali.user.mobile.login.ui.UserLoginActivity;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.RegistParam;
import com.ali.user.mobile.register.RegistConstants;
import com.ali.user.mobile.register.ui.AliUserRegisterActivity;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.ui.AliUserVerificationActivity;
import com.ali.user.mobile.webview.AliUserRegisterWebviewActivity;
import com.ali.user.mobile.webview.WebViewActivity;

public class NavigatorManager {
    private static volatile NavigatorManager navigatorManager;

    public void navFromGuide2UserLoginPage(Context context, String str) {
    }

    private NavigatorManager() {
    }

    public static NavigatorManager getInstance() {
        if (navigatorManager == null) {
            synchronized (NavigatorManager.class) {
                if (navigatorManager == null) {
                    navigatorManager = new NavigatorManager();
                }
            }
        }
        return navigatorManager;
    }

    public void navToLoginPage(Context context, String str, boolean z, boolean z2) {
        if (context != null) {
            Intent callingIntent = UserLoginActivity.getCallingIntent(context, str, z, z2);
            if (!(context instanceof Activity)) {
                callingIntent.addFlags(268435456);
            }
            context.startActivity(callingIntent);
        }
    }

    public void navToRegisterPage(Context context, RegistParam registParam) {
        if (context != null) {
            Intent callingIntent = AliUserRegisterActivity.getCallingIntent(context);
            if (registParam != null) {
                callingIntent.putExtra(RegistConstants.REGISTPARAM, registParam);
            }
            if (!(context instanceof Activity)) {
                callingIntent.addFlags(268435456);
            }
            try {
                context.startActivity(callingIntent);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public void navToWebViewPage(Activity activity, String str, LoginParam loginParam, LoginReturnData loginReturnData) {
        if (activity != null) {
            activity.startActivityForResult(WebViewActivity.getCallingIntent(activity, str, loginParam, loginReturnData), 257);
        }
    }

    public void navToRegWebViewPage(Activity activity, String str, String str2) {
        if (activity != null) {
            activity.startActivity(AliUserRegisterWebviewActivity.getCallingIntent(activity, str, str2));
        }
    }

    public void navToVerificationPage(Activity activity, String str, int i) {
        if (activity != null) {
            AliUserVerificationActivity.startVerifyActivity(activity, str, i);
        } else {
            Log.e("login.navigator", "activity is null");
        }
    }
}
