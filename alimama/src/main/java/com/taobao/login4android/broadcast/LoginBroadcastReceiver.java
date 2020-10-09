package com.taobao.login4android.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.taobao.login4android.constants.LoginConstants;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.login4android.thread.LoginThreadHelper;

public class LoginBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "login.LoginBroadcastReceiver";

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            try {
                LoginAction valueOf = LoginAction.valueOf(intent.getAction());
                if (valueOf != null) {
                    switch (valueOf) {
                        case NOTIFY_LOGIN_SUCCESS:
                        case NOTIFY_LOGIN_CANCEL:
                        case NOTIFY_LOGIN_FAILED:
                        case NOTIFY_LOGOUT:
                        case NOTIFY_RESET_STATUS:
                            LoginStatus.resetLoginFlag();
                            try {
                                if (LoginAction.NOTIFY_LOGIN_FAILED == valueOf && intent.getBooleanExtra(LoginConstants.SHOW_TOAST, false) && LoginThreadHelper.isMainThread()) {
                                    Toast.makeText(context, "小二很忙，系统很累，请稍后重试", 0).show();
                                    return;
                                }
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        case NOTIFY_USER_LOGIN:
                            LoginStatus.setUserLogin(true);
                            return;
                        default:
                            return;
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
