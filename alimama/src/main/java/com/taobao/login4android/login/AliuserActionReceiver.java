package com.taobao.login4android.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.utils.Constants;
import com.taobao.accs.utl.UTMini;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.constants.LoginConstants;
import com.taobao.login4android.constants.LoginSceneConstants;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.login4android.session.SessionManager;
import com.taobao.login4android.thread.LoginAsyncTask;
import com.taobao.statistic.TBS;
import com.ut.mini.UTAnalytics;

public class AliuserActionReceiver extends BroadcastReceiver {
    public static final String TAG = "login.AliuserActionReceiver";

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            final SessionManager instance = SessionManager.getInstance(DataProviderFactory.getApplicationContext());
            if (action.equals(LoginResActions.LOGIN_SUCCESS_ACTION)) {
                boolean booleanExtra = intent.getBooleanExtra(Constants.FROM_REGIST_KEY, false);
                String stringExtra = intent.getStringExtra("message");
                if (Debuggable.isDebug()) {
                    LoginTLogAdapter.d(TAG, "isFromRegist: " + booleanExtra);
                }
                if (booleanExtra) {
                    UTAnalytics.getInstance().userRegister(instance.getNick());
                    TBS.Ext.commitEvent("Page_Register", (int) UTMini.EVENTID_AGOO, (Object) "Success", (Object) "");
                } else {
                    TBS.Ext.commitEvent("Page_Login", (int) UTMini.EVENTID_AGOO, (Object) "Success", (Object) "");
                }
                LoginStatus.resetLoginFlag();
                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_LOGIN_SUCCESS, false, 0, stringExtra, "");
                LoginStatus.browserRefUrl = "";
            } else if (action.equals(LoginResActions.LOGIN_FAIL_ACTION)) {
                LoginStatus.browserRefUrl = "";
                LoginStatus.resetLoginFlag();
                sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED);
            } else if (action.equals(LoginResActions.LOGIN_CANCEL_ACTION)) {
                LoginStatus.resetLoginFlag();
                sendBroadcast(LoginAction.NOTIFY_LOGIN_CANCEL);
                LoginStatus.browserRefUrl = "";
                if (LoginStatus.isFromChangeAccount()) {
                    instance.recoverCookie();
                }
            } else if (action.equals(LoginResActions.LOGIN_OPEN_ACTION)) {
                TLogAdapter.e(TAG, "clear sessionInfo in AliUserActionReceiver");
                new CoordinatorWrapper().execute(new LoginAsyncTask<Object, Void, Void>() {
                    public Void excuteTask(Object... objArr) {
                        if (LoginStatus.isFromChangeAccount()) {
                            instance.clearCookieManager();
                            return null;
                        }
                        instance.clearSessionInfo();
                        return null;
                    }
                }, new Object[0]);
                sendBroadcast(LoginAction.NOTIFY_USER_LOGIN);
            } else if (action.equals("NOTIFY_LOGIN_STATUS_RESET")) {
                LoginStatus.resetLoginFlag();
                sendBroadcast(LoginAction.NOTIFY_RESET_STATUS);
            } else if (action.equals(LoginResActions.LOGIN_NETWORK_ERROR)) {
                LoginStatus.resetLoginFlag();
                sendBroadcast(LoginAction.NOTIFY_LOGIN_FAILED);
                LoginStatus.browserRefUrl = "";
            } else if (action.equals(LoginResActions.WEB_ACTIVITY_CANCEL)) {
                if (intent.getBooleanExtra(LoginConstants.H5_SEND_CANCEL_BROADCAST, false) && !LoginStatus.isFromChangeAccount()) {
                    sendBroadcast(LoginAction.NOTIFY_LOGIN_CANCEL);
                }
            } else if (action.equals(LoginResActions.WEB_ACTIVITY_RESULT)) {
                if ("true".equals(intent.getStringExtra("isSuc"))) {
                    String stringExtra2 = intent.getStringExtra("bizType");
                    if ("changePhoneNum".equals(stringExtra2)) {
                        sendBroadcast(LoginAction.NOTIFY_CHANGE_MOBILE_SUCCESS);
                    } else if ("bindPhoneNum".equals(stringExtra2)) {
                        sendBroadcast(LoginAction.NOTIFY_BIND_MOBILE_SUCCESS);
                    } else if (LoginSceneConstants.SCENE_CHANGEPASSWORD.equals(stringExtra2)) {
                        sendBroadcast(LoginAction.NOTIFY_CHANGE_PASSWORD_SUCCESS);
                    } else if (LoginSceneConstants.SCENE_CANCEL_SITE_ACCOUNT.equals(stringExtra2)) {
                        sendBroadcast(LoginAction.NOTIFY_H5_CANCEL_SITE_ACCOUNT_SUCCESS);
                    }
                }
            } else if (action.equals(LoginResActions.REG_CANCEL)) {
                sendBroadcast(LoginAction.NOTIFY_REGISTER_CANCEL);
            }
        }
    }

    public void sendBroadcast(LoginAction loginAction) {
        BroadCastHelper.sendBroadcast(loginAction, false, "");
    }
}
