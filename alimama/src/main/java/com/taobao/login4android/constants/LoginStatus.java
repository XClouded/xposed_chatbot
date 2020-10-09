package com.taobao.login4android.constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.login4android.session.SessionManager;
import com.taobao.login4android.thread.LoginThreadHelper;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class LoginStatus {
    private static final String CURRENT_PROCESS = "currentProcess";
    private static final String IS_LOGIGING = "isLogining";
    private static final String IS_USER_LOGINING = "isUserLogining";
    private static final String NOTIFY_LOGIN_STATUS_CHANGE = "NOTIFY_LOGIN_STATUS_CHANGE";
    public static final String TAG = "login.LoginStatus";
    public static boolean askServerForGuide = true;
    public static String browserRefUrl = null;
    public static boolean enableSsoAlways = true;
    private static AtomicBoolean isFromAccountChange = new AtomicBoolean(false);
    /* access modifiers changed from: private */
    public static AtomicBoolean isLogining = new AtomicBoolean(false);
    private static AtomicBoolean isNewFingerPrintEnrolled = new AtomicBoolean(false);
    private static AtomicLong lastLoginTime = new AtomicLong(0);
    public static final AtomicLong lastRefreshCookieTime = new AtomicLong(0);
    private static Context mContext;
    private static BroadcastReceiver mStatusReceiver;
    public static int outline;
    /* access modifiers changed from: private */
    public static AtomicBoolean userLogin = new AtomicBoolean(false);

    public static void init(Context context) {
        LoginTLogAdapter.e(TAG, "init Login Status");
        mContext = context;
        mStatusReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    try {
                        if (!TextUtils.equals(LoginThreadHelper.getCurProcessName(context), intent.getStringExtra(LoginStatus.CURRENT_PROCESS))) {
                            LoginStatus.isLogining.set(intent.getBooleanExtra(LoginStatus.IS_LOGIGING, false));
                            LoginStatus.userLogin.set(intent.getBooleanExtra(LoginStatus.IS_USER_LOGINING, false));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mContext.registerReceiver(mStatusReceiver, new IntentFilter(NOTIFY_LOGIN_STATUS_CHANGE));
    }

    private static void nofityStatusChange() {
        if (mContext != null && mStatusReceiver != null) {
            Intent intent = new Intent(NOTIFY_LOGIN_STATUS_CHANGE);
            intent.putExtra(CURRENT_PROCESS, LoginThreadHelper.getCurProcessName(mContext));
            intent.putExtra(IS_LOGIGING, isLogining.get());
            intent.putExtra(IS_USER_LOGINING, userLogin.get());
            intent.setPackage(mContext.getPackageName());
            mContext.sendBroadcast(intent);
        }
    }

    public static boolean isLogining() {
        return isLogining.get() || userLogin.get();
    }

    public static void setLogining(boolean z) {
        LoginTLogAdapter.e(TAG, "set isLogining=" + z);
        if (isLogining.compareAndSet(!z, z)) {
            nofityStatusChange();
        }
    }

    public static synchronized boolean compareAndSetLogining(boolean z, boolean z2) {
        boolean compareAndSet;
        synchronized (LoginStatus.class) {
            LoginTLogAdapter.e(TAG, "compareAndSetLogining  expect=" + z + ",update=" + z2);
            compareAndSet = isLogining.compareAndSet(z, z2);
            if (!compareAndSet || !z || !z2) {
                nofityStatusChange();
                lastLoginTime.set(System.currentTimeMillis());
            }
        }
        return compareAndSet;
    }

    public static boolean isFromChangeAccount() {
        return isFromAccountChange.get();
    }

    public static boolean isNewFingerPrintEnrolled() {
        return isNewFingerPrintEnrolled.get();
    }

    public static synchronized boolean compareAndSetNewFingerPrintEntrolled(boolean z, boolean z2) {
        boolean compareAndSet;
        synchronized (LoginStatus.class) {
            compareAndSet = isNewFingerPrintEnrolled.compareAndSet(z, z2);
        }
        return compareAndSet;
    }

    public static synchronized boolean compareAndSetFromChangeAccount(boolean z, boolean z2) {
        boolean compareAndSet;
        synchronized (LoginStatus.class) {
            compareAndSet = isFromAccountChange.compareAndSet(z, z2);
        }
        return compareAndSet;
    }

    public static synchronized void resetFingerPrintEntrolled() {
        synchronized (LoginStatus.class) {
            compareAndSetNewFingerPrintEntrolled(true, false);
        }
    }

    public static boolean isUserLogin() {
        return userLogin.get();
    }

    public static void setUserLogin(boolean z) {
        LoginTLogAdapter.e(TAG, "set userLogin=" + z);
        if (userLogin.compareAndSet(!z, z)) {
            nofityStatusChange();
        }
    }

    public static long getLastLoginTime() {
        if (SessionManager.isDebug()) {
            LoginTLogAdapter.d(TAG, "get lastLoginTime=" + lastLoginTime.get());
        }
        return lastLoginTime.get();
    }

    public static long getLastRefreshCookieTime() {
        if (SessionManager.isDebug()) {
            LoginTLogAdapter.d(TAG, "get lastRefreshCookieTime=" + lastRefreshCookieTime.get());
        }
        if (lastRefreshCookieTime != null) {
            return lastRefreshCookieTime.get();
        }
        return 0;
    }

    public static void setLastRefreshCookieTime(long j) {
        if (SessionManager.isDebug()) {
            LoginTLogAdapter.d(TAG, "set lastRefreshCookieTime=" + j);
        }
        lastRefreshCookieTime.set(j);
    }

    public static void resetLoginFlag() {
        LoginTLogAdapter.w(TAG, "reset login status");
        boolean compareAndSet = isLogining.compareAndSet(true, false);
        boolean compareAndSet2 = userLogin.compareAndSet(true, false);
        if (compareAndSet || compareAndSet2) {
            nofityStatusChange();
        }
        isFromAccountChange.compareAndSet(true, false);
    }
}
