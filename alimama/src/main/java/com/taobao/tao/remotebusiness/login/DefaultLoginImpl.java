package com.taobao.tao.remotebusiness.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSON;
import com.alipay.sdk.cons.c;
import com.taobao.login4android.constants.LoginConstants;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.stat.IUploadStats;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.xstate.XState;

public final class DefaultLoginImpl implements IRemoteLogin {
    private static final String MTOP_API_REFERENCE = "apiReferer";
    private static final String STATS_MODULE_MTOPRB = "mtoprb";
    private static final String STATS_MONITOR_POINT_SESSION_INVALID = "SessionInvalid";
    private static final String TAG = "mtopsdk.DefaultLoginImpl";
    public static volatile DefaultLoginImpl instance = null;
    /* access modifiers changed from: private */
    public static volatile AtomicBoolean isRegistered = new AtomicBoolean(false);
    static Context mContext;
    private static ThreadLocal<SessionInvalidEvent> threadLocal = new ThreadLocal<>();
    private Method checkSessionValidMethod;
    private Method getNickMethod;
    private Method getSidMethod;
    private Method getUserIdMethod;
    private Method isLoginingMethod;
    private Class<?> loginBroadcastHelperCls;
    private Class<?> loginCls;
    private LoginContext loginContext = new LoginContext();
    private Method loginMethod;
    private Class<?> loginStatusCls;
    protected BroadcastReceiver receiver = null;
    private Method registerReceiverMethod;

    public static DefaultLoginImpl getDefaultLoginImpl(@NonNull Context context) {
        if (instance == null) {
            synchronized (DefaultLoginImpl.class) {
                if (instance == null) {
                    if (context == null) {
                        try {
                            context = MtopUtils.getContext();
                            if (context == null) {
                                TBSdkLog.e(TAG, "[getDefaultLoginImpl]context can't be null.reflect context is still null.");
                                Mtop instance2 = Mtop.instance(Mtop.Id.INNER, (Context) null);
                                if (instance2.getMtopConfig().context == null) {
                                    TBSdkLog.e(TAG, "[getDefaultLoginImpl]context can't be null.wait INNER mtopInstance init.");
                                    instance2.checkMtopSDKInit();
                                }
                                context = instance2.getMtopConfig().context;
                                if (context == null) {
                                    TBSdkLog.e(TAG, "[getDefaultLoginImpl]context can't be null.wait INNER mtopInstance init finish,context is still null");
                                    DefaultLoginImpl defaultLoginImpl = instance;
                                    return defaultLoginImpl;
                                }
                                TBSdkLog.e(TAG, "[getDefaultLoginImpl]context can't be null.wait INNER mtopInstance init finish.context=" + context);
                            }
                        } catch (Exception e) {
                            TBSdkLog.e(TAG, "[getDefaultLoginImpl]get DefaultLoginImpl instance error", (Throwable) e);
                            return instance;
                        }
                    }
                    mContext = context;
                    instance = new DefaultLoginImpl();
                }
            }
        }
        return instance;
    }

    private DefaultLoginImpl() throws ClassNotFoundException, NoSuchMethodException {
        try {
            this.loginCls = Class.forName("com.taobao.login4android.api.Login");
        } catch (ClassNotFoundException unused) {
            this.loginCls = Class.forName("com.taobao.login4android.Login");
        }
        this.loginMethod = this.loginCls.getDeclaredMethod("login", new Class[]{Boolean.TYPE, Bundle.class});
        this.checkSessionValidMethod = this.loginCls.getDeclaredMethod("checkSessionValid", new Class[0]);
        this.getSidMethod = this.loginCls.getDeclaredMethod("getSid", new Class[0]);
        this.getUserIdMethod = this.loginCls.getDeclaredMethod("getUserId", new Class[0]);
        this.getNickMethod = this.loginCls.getDeclaredMethod("getNick", new Class[0]);
        this.loginStatusCls = Class.forName("com.taobao.login4android.constants.LoginStatus");
        this.isLoginingMethod = this.loginStatusCls.getDeclaredMethod("isLogining", new Class[0]);
        this.loginBroadcastHelperCls = Class.forName("com.taobao.login4android.broadcast.LoginBroadcastHelper");
        this.registerReceiverMethod = this.loginBroadcastHelperCls.getMethod("registerLoginReceiver", new Class[]{Context.class, BroadcastReceiver.class});
        registerReceiver();
        TBSdkLog.e(TAG, "register login event receiver");
    }

    private void registerReceiver() {
        if (this.receiver != null) {
            return;
        }
        if (mContext == null) {
            TBSdkLog.e(TAG, "[registerReceiver]Context is null, register receiver fail.");
            return;
        }
        synchronized (DefaultLoginImpl.class) {
            if (this.receiver == null) {
                this.receiver = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        if (intent != null) {
                            String action = intent.getAction();
                            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                                TBSdkLog.e(DefaultLoginImpl.TAG, "[onReceive]Login Broadcast Received. action=" + action);
                            }
                            char c = 65535;
                            int hashCode = action.hashCode();
                            if (hashCode != -1186442906) {
                                if (hashCode != -1100695767) {
                                    if (hashCode == -542410121 && action.equals("NOTIFY_LOGIN_SUCCESS")) {
                                        c = 0;
                                    }
                                } else if (action.equals("NOTIFY_LOGIN_FAILED")) {
                                    c = 1;
                                }
                            } else if (action.equals("NOTIFY_LOGIN_CANCEL")) {
                                c = 2;
                            }
                            switch (c) {
                                case 0:
                                    LoginHandler.instance().onLoginSuccess();
                                    return;
                                case 1:
                                    LoginHandler.instance().onLoginFail();
                                    return;
                                case 2:
                                    LoginHandler.instance().onLoginCancel();
                                    return;
                                default:
                                    return;
                            }
                        }
                    }
                };
                invokeMethod(this.registerReceiverMethod, mContext, this.receiver);
            }
        }
    }

    public void setSessionInvalid(Object obj) {
        if (obj instanceof MtopResponse) {
            threadLocal.set(new SessionInvalidEvent((MtopResponse) obj, (String) invokeMethod(this.getNickMethod, new Object[0])));
        } else if (obj instanceof MtopRequest) {
            threadLocal.set(new SessionInvalidEvent((MtopRequest) obj));
        }
    }

    private <T> T invokeMethod(Method method, Object... objArr) {
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(this.loginCls, objArr);
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[invokeMethod]invokeMethod error,method:" + method + ",args:" + objArr, (Throwable) e);
            return null;
        }
    }

    public void login(onLoginListener onloginlistener, boolean z) {
        Bundle bundle;
        Exception e;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
            TBSdkLog.e(TAG, "[login]call login,showLoginUI:" + z + " , listener:" + onloginlistener);
        }
        final SessionInvalidEvent sessionInvalidEvent = threadLocal.get();
        if (sessionInvalidEvent != null) {
            try {
                bundle = new Bundle();
                try {
                    String jSONString = sessionInvalidEvent.toJSONString();
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                        TBSdkLog.e(TAG, "[login]apiRefer=" + jSONString);
                    }
                    bundle.putString("apiReferer", jSONString);
                    final IUploadStats iUploadStats = Mtop.instance(mContext).getMtopConfig().uploadStats;
                    if (iUploadStats == null) {
                        threadLocal.remove();
                        return;
                    } else {
                        MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
                            public void run() {
                                try {
                                    if (DefaultLoginImpl.isRegistered.compareAndSet(false, true)) {
                                        HashSet hashSet = new HashSet();
                                        hashSet.add("long_nick");
                                        hashSet.add(c.n);
                                        hashSet.add("apiV");
                                        hashSet.add("msgCode");
                                        hashSet.add("S_STATUS");
                                        hashSet.add("processName");
                                        hashSet.add("appBackGround");
                                        if (iUploadStats != null) {
                                            iUploadStats.onRegister(DefaultLoginImpl.STATS_MODULE_MTOPRB, DefaultLoginImpl.STATS_MONITOR_POINT_SESSION_INVALID, hashSet, (Set<String>) null, false);
                                        }
                                        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                                            TBSdkLog.e(DefaultLoginImpl.TAG, "onRegister called. module=mtoprb,monitorPoint=SessionInvalid");
                                        }
                                    }
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("long_nick", sessionInvalidEvent.long_nick);
                                    hashMap.put(c.n, sessionInvalidEvent.apiName);
                                    hashMap.put("apiV", sessionInvalidEvent.v);
                                    hashMap.put("msgCode", sessionInvalidEvent.msgCode);
                                    hashMap.put("S_STATUS", sessionInvalidEvent.S_STATUS);
                                    hashMap.put("processName", sessionInvalidEvent.processName);
                                    hashMap.put("appBackGround", sessionInvalidEvent.appBackGround ? "1" : "0");
                                    if (iUploadStats != null) {
                                        iUploadStats.onCommit(DefaultLoginImpl.STATS_MODULE_MTOPRB, DefaultLoginImpl.STATS_MONITOR_POINT_SESSION_INVALID, hashMap, (Map<String, Double>) null);
                                    }
                                } catch (Exception e) {
                                    TBSdkLog.e(DefaultLoginImpl.TAG, "upload  SessionInvalid Stats error.", (Throwable) e);
                                }
                            }
                        });
                        threadLocal.remove();
                    }
                } catch (Exception e2) {
                    e = e2;
                }
            } catch (Exception e3) {
                bundle = null;
                e = e3;
                try {
                    TBSdkLog.e(TAG, "[login]  login extra bundle error.", (Throwable) e);
                    threadLocal.remove();
                    registerReceiver();
                    invokeMethod(this.loginMethod, Boolean.valueOf(z), bundle);
                } catch (Throwable th) {
                    threadLocal.remove();
                    throw th;
                }
            }
        } else {
            bundle = null;
        }
        registerReceiver();
        invokeMethod(this.loginMethod, Boolean.valueOf(z), bundle);
    }

    public boolean isSessionValid() {
        Boolean bool = (Boolean) invokeMethod(this.checkSessionValidMethod, new Object[0]);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public boolean isLogining() {
        Boolean bool = (Boolean) invokeMethod(this.isLoginingMethod, new Object[0]);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public LoginContext getLoginContext() {
        this.loginContext.sid = (String) invokeMethod(this.getSidMethod, new Object[0]);
        this.loginContext.userId = (String) invokeMethod(this.getUserIdMethod, new Object[0]);
        this.loginContext.nickname = (String) invokeMethod(this.getNickMethod, new Object[0]);
        return this.loginContext;
    }

    private static class SessionInvalidEvent {
        private static final String HEADER_KEY = "S";
        public String S_STATUS;
        public String apiName;
        public boolean appBackGround;
        public String eventName;
        public String long_nick;
        public String msgCode;
        public String processName;
        public String v;

        public SessionInvalidEvent(MtopResponse mtopResponse, String str) {
            this.eventName = LoginConstants.EVENT_SESSION_INVALID;
            this.long_nick = str;
            this.apiName = mtopResponse.getApi();
            this.v = mtopResponse.getV();
            this.msgCode = mtopResponse.getRetCode();
            this.S_STATUS = HeaderHandlerUtil.getSingleHeaderFieldByKey(mtopResponse.getHeaderFields(), HEADER_KEY);
            this.processName = MtopUtils.getCurrentProcessName(DefaultLoginImpl.mContext);
            this.appBackGround = XState.isAppBackground();
        }

        public SessionInvalidEvent(MtopRequest mtopRequest) {
            this.apiName = mtopRequest.getApiName();
            this.v = mtopRequest.getVersion();
            this.processName = MtopUtils.getCurrentProcessName(DefaultLoginImpl.mContext);
            this.appBackGround = XState.isAppBackground();
        }

        public String toJSONString() {
            return JSON.toJSONString(this);
        }
    }
}
