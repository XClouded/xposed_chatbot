package com.alimama.union.app.aalogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.service.BeanContext;
import com.alimama.moon.ui.SafeProgressDialog;
import com.alimama.moon.utils.ToastUtil;
import com.taobao.login4android.Login;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.util.ErrorConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginManager {
    public static final String ACTION_LOGIN_STATE_CHANGE = "action_login_state_change";
    private static final String TAG = "LoginManager";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) LoginManager.class);
    private static int loginSuccessCount;
    private static Handler mHandler;
    private static LoginCallbackListener mLoginCallbackListener;
    private Activity mContext;
    /* access modifiers changed from: private */
    public SafeProgressDialog mProgressDialog;

    public interface LoginCallbackListener {
        void onLoginFailed();

        void onLoginSuccess();
    }

    private LoginManager() {
    }

    private static final class SingleClassHolder {
        /* access modifiers changed from: private */
        public static final LoginManager instance = new LoginManager();

        private SingleClassHolder() {
        }
    }

    public static LoginManager getInstance() {
        return SingleClassHolder.instance;
    }

    public void login(final Activity activity, LoginCallbackListener loginCallbackListener) {
        logger.info("login {}", (Object) activity.getClass().getName());
        this.mContext = null;
        this.mContext = activity;
        if (mHandler == null) {
            mHandler = new Handler(activity.getMainLooper());
        }
        mHandler.post(new Runnable() {
            public void run() {
                if (LoginManager.this.mProgressDialog == null) {
                    SafeProgressDialog unused = LoginManager.this.mProgressDialog = new SafeProgressDialog(activity);
                    LoginManager.this.mProgressDialog.setMessage("正在登录，请稍候...");
                }
                if (activity != null && !activity.isFinishing()) {
                    LoginManager.this.mProgressDialog.show();
                }
            }
        });
        mLoginCallbackListener = loginCallbackListener;
        Login.login(true);
    }

    public void showTbLoginErr() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
        if (this.mContext != null) {
            ToastUtil.toast((Context) this.mContext, (CharSequence) ErrorConstant.ERRMSG_ANDROID_SYS_LOGIN_FAIL);
        }
        if (mLoginCallbackListener != null) {
            mLoginCallbackListener.onLoginFailed();
        }
    }

    public void dismissDialog() {
        if (this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
        }
        if (mLoginCallbackListener != null) {
            mLoginCallbackListener.onLoginFailed();
        }
    }

    public void loginCancel() {
        dismissDialog();
        if (mLoginCallbackListener != null) {
            mLoginCallbackListener.onLoginFailed();
        }
    }

    public static void loginOut() {
        Login.logout();
        Mtop.instance((Context) null).logout();
        ((SettingManager) BeanContext.get(SettingManager.class)).logout();
        removeCookies();
    }

    private static synchronized void removeCookies() {
        synchronized (LoginManager.class) {
            CookieManager instance = CookieManager.getInstance();
            instance.removeSessionCookie();
            instance.removeAllCookie();
            CookieSyncManager.getInstance().sync();
        }
    }

    public static void notifyLoginStateChanged(boolean z) {
        Intent intent = new Intent(ACTION_LOGIN_STATE_CHANGE);
        intent.putExtra(ACTION_LOGIN_STATE_CHANGE, z);
        ((LocalBroadcastManager) BeanContext.get(LocalBroadcastManager.class)).sendBroadcast(intent);
    }
}
