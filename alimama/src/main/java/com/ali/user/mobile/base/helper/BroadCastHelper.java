package com.ali.user.mobile.base.helper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.model.LoginParam;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.constants.LoginConstants;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.login4android.session.SessionManager;
import com.taobao.login4android.thread.LoginAsyncTask;
import com.taobao.login4android.thread.LoginThreadHelper;
import java.util.Map;

public class BroadCastHelper {
    public static final String TAG = "login.BroadCastHelper";
    public static Bundle sLoginBundle;

    public static void sendCancelBroadcast(String str, String str2) {
        Intent intent = new Intent(LoginResActions.LOGIN_CANCEL_ACTION);
        try {
            intent.putExtra("errorCode", str);
            intent.putExtra("message", str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendLocalBroadCast(intent);
    }

    public static void sendLoginFailBroadcast(String str, String str2) {
        sendLoginFailBroadcast((LoginParam) null, str, str2);
    }

    public static void sendLoginFailBroadcast(LoginParam loginParam, String str, String str2) {
        Intent intent = new Intent(LoginResActions.LOGIN_FAIL_ACTION);
        if (loginParam != null) {
            try {
                if (!TextUtils.isEmpty(loginParam.loginAccount)) {
                    intent.putExtra("username", loginParam.loginAccount);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        intent.putExtra("errorCode", str);
        intent.putExtra("message", str2);
        sendLocalBroadCast(intent);
    }

    public static boolean sendLocalBroadCast(Intent intent) {
        boolean sendBroadcast = LocalBroadcastManager.getInstance(DataProviderFactory.getApplicationContext()).sendBroadcast(intent);
        TLogAdapter.d(TAG, intent.getAction() + "; sendResult=" + sendBroadcast);
        return sendBroadcast;
    }

    public static void sendBroadcast(LoginAction loginAction) {
        sendBroadcast(loginAction, false, "");
    }

    public static void sendBroadcast(LoginAction loginAction, boolean z, String str) {
        sendBroadcast(loginAction, z, 0, "", str);
    }

    public static void sendBroadcast(LoginAction loginAction, boolean z, Map<String, String> map, String str) {
        sendBroadcast(loginAction, z, 0, "", map, str);
    }

    public static void sendBroadcast(LoginAction loginAction, boolean z, int i, String str, String str2) {
        sendBroadcast(loginAction, z, i, str, (Map<String, String>) null, str2);
    }

    public static void sendBroadcast(LoginAction loginAction, Map<String, String> map) {
        sendBroadcast(loginAction, false, 0, "", map, "");
    }

    public static void sendBroadcast(LoginAction loginAction, boolean z, int i, String str, Map<String, String> map, String str2) {
        if (loginAction != null) {
            if (LoginAction.NOTIFY_LOGIN_SUCCESS == loginAction) {
                final SessionManager instance = SessionManager.getInstance(DataProviderFactory.getApplicationContext());
                if (LoginThreadHelper.isMainThread()) {
                    new CoordinatorWrapper().execute(new LoginAsyncTask<Object, Void, Void>() {
                        public Void excuteTask(Object... objArr) {
                            instance.removeEventTrace();
                            return null;
                        }
                    }, new Object[0]);
                } else {
                    instance.removeEventTrace();
                }
            }
            Intent intent = new Intent();
            intent.setAction(loginAction.name());
            intent.setPackage(DataProviderFactory.getApplicationContext().getPackageName());
            intent.putExtra(LoginConstants.SHOW_TOAST, z);
            if (!TextUtils.isEmpty(str)) {
                intent.putExtra("message", str);
            }
            intent.putExtra("errorCode", i);
            if (map != null) {
                for (Map.Entry next : map.entrySet()) {
                    Object key = next.getKey();
                    Object value = next.getValue();
                    if (!(key == null || value == null)) {
                        intent.putExtra((String) next.getKey(), (String) next.getValue());
                    }
                }
            }
            if (sLoginBundle != null) {
                TLogAdapter.d(TAG, "sLoginBundle not null:");
                try {
                    intent.putExtras((Bundle) sLoginBundle.clone());
                } catch (Throwable unused) {
                }
                if (LoginAction.NOTIFY_LOGIN_SUCCESS == loginAction || LoginAction.NOTIFY_LOGIN_FAILED == loginAction || LoginAction.NOTIFY_LOGIN_CANCEL == loginAction) {
                    sLoginBundle = null;
                }
            } else {
                TLogAdapter.d(TAG, "sLoginBundle is null:");
            }
            intent.putExtra(LoginConstants.BROWSER_REF_URL, str2);
            DataProviderFactory.getApplicationContext().sendBroadcast(intent);
            LoginTLogAdapter.i(TAG, "sendBroadcast: " + loginAction);
        }
    }
}
