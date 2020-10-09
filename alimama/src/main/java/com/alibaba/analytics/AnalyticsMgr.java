package com.alibaba.analytics;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import com.alibaba.analytics.IAnalytics;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.analytics.version.UTBuildInfo;
import com.alibaba.motu.tbrest.rest.RestUrlWrapper;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnalyticsMgr {
    private static final String TAG = "AnalyticsMgr";
    private static String appKey = null;
    private static String appVersion = null;
    private static Application application;
    private static String channel = null;
    public static WaitingHandler handler;
    private static HandlerThread handlerThread;
    public static IAnalytics iAnalytics;
    /* access modifiers changed from: private */
    public static boolean isBindSuccess = false;
    public static boolean isDebug = false;
    private static boolean isEncode = false;
    public static volatile boolean isInit = false;
    /* access modifiers changed from: private */
    public static boolean isNeedRestart = false;
    private static boolean isSecurity = false;
    private static boolean isTurnOnRealTimeDebug = false;
    /* access modifiers changed from: private */
    public static ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Logger.d("onServiceConnected", "this", AnalyticsMgr.mConnection);
            if (RunMode.Service == AnalyticsMgr.mode) {
                AnalyticsMgr.iAnalytics = IAnalytics.Stub.asInterface(iBinder);
                Logger.i("onServiceConnected", "iAnalytics", AnalyticsMgr.iAnalytics);
            }
            synchronized (AnalyticsMgr.sWaitServiceConnectedLock) {
                AnalyticsMgr.sWaitServiceConnectedLock.notifyAll();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Logger.d(AnalyticsMgr.TAG, "[onServiceDisconnected]");
            synchronized (AnalyticsMgr.sWaitServiceConnectedLock) {
                AnalyticsMgr.sWaitServiceConnectedLock.notifyAll();
            }
            boolean unused = AnalyticsMgr.isNeedRestart = true;
        }
    };
    private static Map<String, String> mGlobalArgsMap = new ConcurrentHashMap();
    private static String mOpenid = null;
    public static final List<Entity> mRegisterList = Collections.synchronizedList(new ArrayList());
    public static RunMode mode = RunMode.Service;
    private static Map<String, String> realTimeDebugParams = null;
    /* access modifiers changed from: private */
    public static final Object sWaitMainProcessLock = new Object();
    /* access modifiers changed from: private */
    public static final Object sWaitServiceConnectedLock = new Object();
    private static String secret = null;
    private static Map<String, String> updateSessionProperties = null;
    private static String userId = null;
    private static String userNick = null;

    public static class Entity {
        public DimensionSet dimensionSet;
        public boolean isCommitDetail;
        public MeasureSet measureSet;
        public String module;
        public String monitorPoint;
    }

    enum RunMode {
        Local,
        Service
    }

    public static synchronized void init(Application application2) {
        Looper looper;
        synchronized (AnalyticsMgr.class) {
            try {
                if (!isInit) {
                    Logger.i("AnalyticsMgr[init] start", RestUrlWrapper.FIELD_SDK_VERSION, UTBuildInfo.getInstance().getFullSDKVersion());
                    application = application2;
                    handlerThread = new HandlerThread("Analytics_Client");
                    handlerThread.start();
                    Looper looper2 = null;
                    int i = 0;
                    while (true) {
                        if (i >= 3) {
                            looper = looper2;
                            break;
                        }
                        try {
                            looper = handlerThread.getLooper();
                            if (looper != null) {
                                break;
                            }
                            try {
                                Thread.sleep(10);
                            } catch (Throwable th) {
                                th = th;
                            }
                            i++;
                            looper2 = looper;
                        } catch (Throwable th2) {
                            th = th2;
                            looper = looper2;
                            Logger.e(TAG, "3", th);
                            handler = new WaitingHandler(looper);
                            handler.postAtFrontOfQueue(new UtDelayInitTask());
                            isInit = true;
                            Logger.d("外面init完成", new Object[0]);
                            Logger.w(TAG, "isInit", Boolean.valueOf(isInit), RestUrlWrapper.FIELD_SDK_VERSION, UTBuildInfo.getInstance().getFullSDKVersion());
                        }
                    }
                    handler = new WaitingHandler(looper);
                    try {
                        handler.postAtFrontOfQueue(new UtDelayInitTask());
                    } catch (Throwable th3) {
                        Logger.e(TAG, "4", th3);
                    }
                    isInit = true;
                    Logger.d("外面init完成", new Object[0]);
                }
            } catch (Throwable th4) {
                Logger.w(TAG, "5", th4);
            }
            Logger.w(TAG, "isInit", Boolean.valueOf(isInit), RestUrlWrapper.FIELD_SDK_VERSION, UTBuildInfo.getInstance().getFullSDKVersion());
        }
    }

    public static void notifyWaitLocked() {
        try {
            synchronized (sWaitMainProcessLock) {
                sWaitMainProcessLock.notifyAll();
            }
        } catch (Throwable unused) {
        }
    }

    public static void setChanel(String str) {
        if (checkInit()) {
            handler.postWatingTask(createSetChannelTask(str));
            channel = str;
        }
    }

    public static void setRequestAuthInfo(boolean z, boolean z2, String str, String str2) {
        if (checkInit()) {
            handler.postWatingTask(createSetRequestAuthTask(z, z2, str, str2));
            isSecurity = z;
            appKey = str;
            secret = str2;
            isEncode = z2;
        }
    }

    public static void turnOnRealTimeDebug(Map<String, String> map) {
        if (checkInit()) {
            handler.postWatingTask(createTurnOnRealTimeDebugTask(map));
            realTimeDebugParams = map;
            isTurnOnRealTimeDebug = true;
        }
    }

    public static void turnOffRealTimeDebug() {
        if (checkInit()) {
            handler.postWatingTask(createTurnOffRealTimeDebugTask());
            isTurnOnRealTimeDebug = false;
        }
    }

    public static void setAppVersion(String str) {
        Logger.i((String) null, "aAppVersion", str);
        if (checkInit()) {
            handler.postWatingTask(createSetAppVersionTask(str));
            appVersion = str;
        }
    }

    public static void turnOnDebug() {
        Logger.i("turnOnDebug", new Object[0]);
        if (checkInit()) {
            handler.postWatingTask(createTurnOnDebugTask());
            isDebug = true;
            Logger.setDebug(true);
        }
    }

    public static void updateUserAccount(String str, String str2, String str3) {
        Logger.i(TAG, "Usernick", str, "Userid", str2, "openid", str3);
        if (checkInit()) {
            handler.postWatingTask(createUpdateUserAccountTask(str, str2, str3));
            updateUserAccountWithOpenId(str, str2, str3);
        }
    }

    public static void updateSessionProperties(Map<String, String> map) {
        if (checkInit()) {
            handler.postWatingTask(createUpdateSessionProperties(map));
            updateSessionProperties = map;
        }
    }

    public static String getValue(String str) {
        if (iAnalytics == null) {
            return null;
        }
        try {
            return iAnalytics.getValue(str);
        } catch (Exception unused) {
            return null;
        }
    }

    public static void setSessionProperties(Map<String, String> map) {
        if (checkInit()) {
            handler.postWatingTask(createSetSessionPropertiesTask(map));
        }
    }

    public static void sessionTimeout() {
        if (checkInit()) {
            handler.postWatingTask(createSessionTimeoutTask());
        }
    }

    public static boolean checkInit() {
        if (!isInit) {
            Logger.d("Please call init() before call other method", new Object[0]);
        }
        return isInit;
    }

    /* access modifiers changed from: private */
    public static void newLocalAnalytics() {
        mode = RunMode.Local;
        iAnalytics = new AnalyticsImp(application);
        Logger.w("Start AppMonitor Service failed,AppMonitor run in local Mode...", new Object[0]);
    }

    /* access modifiers changed from: private */
    public static boolean bindService() {
        if (application == null) {
            return false;
        }
        boolean bindService = application.getApplicationContext().bindService(new Intent(application.getApplicationContext(), AnalyticsService.class), mConnection, 1);
        if (!bindService) {
            newLocalAnalytics();
        }
        Logger.i(TAG, "bindsuccess", Boolean.valueOf(bindService));
        return bindService;
    }

    public static void dispatchLocalHits() {
        if (checkInit()) {
            handler.postWatingTask(createDispatchLocalHitTask());
        }
    }

    public static void dispatchSaveCacheDataToLocal() {
        if (checkInit()) {
            handler.postWatingTask(createSaveCacheDataToLocalTask());
        }
    }

    /* access modifiers changed from: private */
    public static Runnable createInitTask() {
        return new Runnable() {
            public void run() {
                Logger.i("call Remote init start...", new Object[0]);
                try {
                    AnalyticsMgr.iAnalytics.initUT();
                } catch (Throwable th) {
                    Logger.e("initut error", th, new Object[0]);
                }
                Logger.i("call Remote init end", new Object[0]);
            }
        };
    }

    public static void restart() {
        Logger.d("[restart]", new Object[0]);
        try {
            if (isNeedRestart) {
                isNeedRestart = false;
                newLocalAnalytics();
                createInitTask().run();
                createSetRequestAuthTask(isSecurity, isEncode, appKey, secret).run();
                createSetChannelTask(channel).run();
                createSetAppVersionTask(appVersion).run();
                createUpdateUserAccountTask(userNick, userId, mOpenid).run();
                createUpdateSessionProperties(updateSessionProperties).run();
                if (isDebug) {
                    createTurnOnDebugTask().run();
                }
                if (isTurnOnRealTimeDebug && realTimeDebugParams != null) {
                    createSetSessionPropertiesTask(realTimeDebugParams).run();
                } else if (isTurnOnRealTimeDebug) {
                    createTurnOffRealTimeDebugTask().run();
                }
                synchronized (mRegisterList) {
                    for (int i = 0; i < mRegisterList.size(); i++) {
                        Entity entity = mRegisterList.get(i);
                        if (entity != null) {
                            try {
                                createRegisterTask(entity.module, entity.monitorPoint, entity.measureSet, entity.dimensionSet, entity.isCommitDetail).run();
                            } catch (Throwable th) {
                                Logger.e(TAG, "[RegisterTask.run]", th);
                            }
                        }
                    }
                }
                for (Map.Entry next : mGlobalArgsMap.entrySet()) {
                    setGlobalProperty((String) next.getKey(), (String) next.getValue());
                }
            }
        } catch (Throwable th2) {
            Logger.e(TAG, "[restart]", th2);
        }
    }

    private static Runnable createTurnOnRealTimeDebugTask(final Map<String, String> map) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.turnOnRealTimeDebug(map);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static Runnable createTurnOffRealTimeDebugTask() {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.turnOffRealTimeDebug();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static Runnable createSetRequestAuthTask(final boolean z, final boolean z2, final String str, final String str2) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.setRequestAuthInfo(z, z2, str, str2);
                } catch (Throwable unused) {
                }
            }
        };
    }

    private static Runnable createSetChannelTask(final String str) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.setChannel(str);
                } catch (Throwable unused) {
                }
            }
        };
    }

    private static Runnable createRegisterTask(String str, String str2, MeasureSet measureSet, DimensionSet dimensionSet, boolean z) {
        Logger.d("", new Object[0]);
        final String str3 = str;
        final String str4 = str2;
        final MeasureSet measureSet2 = measureSet;
        final DimensionSet dimensionSet2 = dimensionSet;
        final boolean z2 = z;
        return new Runnable() {
            public void run() {
                try {
                    Logger.d("register stat event", "module", str3, " monitorPoint: ", str4);
                    AnalyticsMgr.iAnalytics.register4(str3, str4, measureSet2, dimensionSet2, z2);
                } catch (RemoteException e) {
                    AnalyticsMgr.handleRemoteException(e);
                }
            }
        };
    }

    private static Runnable createSetAppVersionTask(final String str) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.setAppVersion(str);
                } catch (Throwable unused) {
                }
            }
        };
    }

    private static Runnable createTurnOnDebugTask() {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.turnOnDebug();
                } catch (Throwable unused) {
                }
            }
        };
    }

    private static Runnable createUpdateUserAccountTask(final String str, final String str2, final String str3) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.updateUserAccount(str, str2, str3);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        };
    }

    private static Runnable createUpdateSessionProperties(final Map<String, String> map) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.updateSessionProperties(map);
                } catch (Throwable unused) {
                }
            }
        };
    }

    private static Runnable createDispatchLocalHitTask() {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.dispatchLocalHits();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    static Runnable createSaveCacheDataToLocalTask() {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.saveCacheDataToLocal();
                } catch (RemoteException e) {
                    Logger.w(AnalyticsMgr.TAG, e, new Object[0]);
                }
            }
        };
    }

    private static Runnable createSetSessionPropertiesTask(final Map<String, String> map) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.setSessionProperties(map);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static Runnable createSessionTimeoutTask() {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.sessionTimeout();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static void handleRemoteException(Exception exc) {
        Logger.w("", exc, new Object[0]);
        if (exc instanceof DeadObjectException) {
            restart();
        }
    }

    public static class WaitingHandler extends Handler {
        public WaitingHandler(Looper looper) {
            super(looper);
        }

        public void postWatingTask(Runnable runnable) {
            if (runnable != null) {
                try {
                    Message obtain = Message.obtain();
                    obtain.what = 1;
                    obtain.obj = runnable;
                    sendMessage(obtain);
                } catch (Throwable unused) {
                }
            }
        }

        public void handleMessage(Message message) {
            try {
                if (message.obj != null && (message.obj instanceof Runnable)) {
                    ((Runnable) message.obj).run();
                }
            } catch (Throwable th) {
                Logger.e(AnalyticsMgr.TAG, th, new Object[0]);
            }
            super.handleMessage(message);
        }
    }

    public static class UtDelayInitTask implements Runnable {
        public void run() {
            try {
                Logger.i("延时启动任务", new Object[0]);
                synchronized (AnalyticsMgr.sWaitMainProcessLock) {
                    int access$500 = AnalyticsMgr.getCoreProcessWaitTime();
                    if (access$500 > 0) {
                        Logger.i("delay " + access$500 + " second to start service,waiting...", new Object[0]);
                        try {
                            AnalyticsMgr.sWaitMainProcessLock.wait((long) (access$500 * 1000));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                boolean unused = AnalyticsMgr.isBindSuccess = AnalyticsMgr.bindService();
                AnalyticsMgr.handler.postAtFrontOfQueue(new UTInitTimeoutTask());
            } catch (Throwable th) {
                Logger.e(AnalyticsMgr.TAG, Constants.LogTransferLevel.L6, th);
            }
        }
    }

    /* access modifiers changed from: private */
    public static int getCoreProcessWaitTime() {
        String string = AppInfoUtil.getString(application.getApplicationContext(), "UTANALYTICS_REMOTE_SERVICE_DELAY_SECOND");
        if (TextUtils.isEmpty(string)) {
            return 10;
        }
        try {
            int intValue = Integer.valueOf(string).intValue();
            if (intValue < 0 || intValue > 30) {
                return 10;
            }
            return intValue;
        } catch (Throwable unused) {
            return 10;
        }
    }

    public static class UTInitTimeoutTask implements Runnable {
        public void run() {
            try {
                if (AnalyticsMgr.isBindSuccess) {
                    Logger.i("delay 30 sec to wait the Remote service connected,waiting...", new Object[0]);
                    synchronized (AnalyticsMgr.sWaitServiceConnectedLock) {
                        try {
                            AnalyticsMgr.sWaitServiceConnectedLock.wait(30000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (AnalyticsMgr.iAnalytics == null) {
                    Logger.i("cannot get remote analytics object,new local object", new Object[0]);
                    AnalyticsMgr.newLocalAnalytics();
                }
                AnalyticsMgr.createInitTask().run();
            } catch (Throwable th) {
                Logger.e(AnalyticsMgr.TAG, Constants.LogTransferLevel.L7, th);
            }
        }
    }

    private static void updateUserAccountWithOpenId(String str, String str2, String str3) {
        userNick = str;
        if (TextUtils.isEmpty(str2)) {
            userId = null;
            mOpenid = null;
        } else if (!TextUtils.isEmpty(str3) || !str2.equals(userId)) {
            userId = str2;
            mOpenid = str3;
        }
    }

    public static void setGlobalProperty(String str, String str2) {
        if (checkInit()) {
            if (StringUtils.isEmpty(str) || str2 == null) {
                Logger.e("setGlobalProperty", "key is null or key is empty or value is null,please check it!");
                return;
            }
            mGlobalArgsMap.put(str, str2);
            handler.postWatingTask(createSetGlobalPropertyTask(str, str2));
        }
    }

    public static void removeGlobalProperty(String str) {
        if (checkInit() && !StringUtils.isEmpty(str) && mGlobalArgsMap.containsKey(str)) {
            mGlobalArgsMap.remove(str);
            handler.postWatingTask(createRemoveGlobalPropertyTask(str));
        }
    }

    public static String getGlobalProperty(String str) {
        if (checkInit() && str != null) {
            return mGlobalArgsMap.get(str);
        }
        return null;
    }

    private static Runnable createSetGlobalPropertyTask(final String str, final String str2) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.setGlobalProperty(str, str2);
                } catch (RemoteException e) {
                    AnalyticsMgr.handleRemoteException(e);
                }
            }
        };
    }

    private static Runnable createRemoveGlobalPropertyTask(final String str) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.removeGlobalProperty(str);
                } catch (RemoteException e) {
                    AnalyticsMgr.handleRemoteException(e);
                }
            }
        };
    }
}
