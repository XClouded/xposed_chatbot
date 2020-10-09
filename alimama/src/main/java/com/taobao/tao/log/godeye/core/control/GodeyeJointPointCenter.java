package com.taobao.tao.log.godeye.core.control;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.android.tlog.protocol.model.joint.point.BackgroundJointPoint;
import com.taobao.android.tlog.protocol.model.joint.point.EventJointPoint;
import com.taobao.android.tlog.protocol.model.joint.point.ForegroundJointPoint;
import com.taobao.android.tlog.protocol.model.joint.point.JointPoint;
import com.taobao.android.tlog.protocol.model.joint.point.LifecycleJointPoint;
import com.taobao.android.tlog.protocol.model.joint.point.NotificationJointPoint;
import com.taobao.android.tlog.protocol.model.joint.point.StartupJointPoint;
import com.taobao.android.tlog.protocol.model.joint.point.TimerJointPoint;
import com.taobao.tao.log.godeye.api.control.IGodeyeJointPointCenter;
import com.taobao.tao.log.godeye.protocol.model.ClientEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class GodeyeJointPointCenter implements IGodeyeJointPointCenter {
    /* access modifiers changed from: private */
    public String lastVisitedPage;
    /* access modifiers changed from: private */
    public ConcurrentHashMap<String, List<IGodeyeJointPointCenter.GodeyeJointPointCallback>> mActivityLifecycleJointPointHandlers = new ConcurrentHashMap<>();
    private final Application mApplication;
    private ConcurrentHashMap<String, List<IGodeyeJointPointCenter.GodeyeJointPointCallback>> mCustomEventJointPointHandlers = new ConcurrentHashMap<>();
    /* access modifiers changed from: private */
    public Vector<IGodeyeJointPointCenter.GodeyeJointPointCallback> mEnterBackgroundJointPointHandlers = new Vector<>();
    /* access modifiers changed from: private */
    public Vector<IGodeyeJointPointCenter.GodeyeJointPointCallback> mEnterForegroundJointPointHandlers = new Vector<>();

    GodeyeJointPointCenter(Application application) {
        this.mApplication = application;
        this.mApplication.registerActivityLifecycleCallbacks(new GodeyeAppLifecycleCallback());
    }

    public void installJointPoints(JointPoint jointPoint, IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback, JointPoint jointPoint2, IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback2, boolean z) {
        long j;
        boolean z2;
        if (jointPoint2.type.equals(TimerJointPoint.TYPE)) {
            z2 = true;
            j = (long) ((TimerJointPoint) jointPoint2).waitMilliseconds;
        } else {
            z2 = false;
            j = -1;
        }
        GodeyeJointPointCallbackWithStartHook godeyeJointPointCallbackWithStartHook = new GodeyeJointPointCallbackWithStartHook(godeyeJointPointCallback);
        GodeyeJointPointCallbackWithStopHook godeyeJointPointCallbackWithStopHook = new GodeyeJointPointCallbackWithStopHook(godeyeJointPointCallback2);
        if (z2 && j > 0) {
            installStartJointPoint(jointPoint, new GodeyeStartJointPointCallbackWithDelayStop(j, godeyeJointPointCallbackWithStartHook, godeyeJointPointCallbackWithStopHook), z);
        }
        if (!z2) {
            installStartJointPoint(jointPoint, godeyeJointPointCallbackWithStartHook, z);
            installStopJointPoint(jointPoint2, godeyeJointPointCallbackWithStopHook);
        }
    }

    public void invokeCustomEventJointPointHandlersIfExist(String str) {
        if (Godeye.sharedInstance().isDebugMode()) {
            List<IGodeyeJointPointCenter.GodeyeJointPointCallback> list = this.mCustomEventJointPointHandlers.get(str);
            if (list != null) {
                for (IGodeyeJointPointCenter.GodeyeJointPointCallback doCallback : list) {
                    doCallback.doCallback();
                }
            }
            this.mCustomEventJointPointHandlers.remove(str);
        }
    }

    public String getLastVisitedPage() {
        return this.lastVisitedPage;
    }

    private void registerActivityLifecycleCallbackHandler(String str, String str2, IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback) {
        String buildLifecycleKey = buildLifecycleKey(str, str2);
        List list = this.mActivityLifecycleJointPointHandlers.get(buildLifecycleKey);
        if (list != null) {
            list.add(godeyeJointPointCallback);
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(godeyeJointPointCallback);
        this.mActivityLifecycleJointPointHandlers.put(buildLifecycleKey, arrayList);
    }

    private void registerEnterBackgroundCallbackHandler(IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback) {
        this.mEnterBackgroundJointPointHandlers.add(godeyeJointPointCallback);
    }

    private void registerEnterForegroundCallbackHandler(IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback) {
        this.mEnterForegroundJointPointHandlers.add(godeyeJointPointCallback);
    }

    private void registerBroadcastHandler(String str, IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(str);
        this.mApplication.registerReceiver(new GodeyeBroadcastReceiver(this.mApplication, godeyeJointPointCallback), intentFilter);
    }

    private void registerCustomEventHandler(String str, IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback) {
        List list = this.mCustomEventJointPointHandlers.get(str);
        if (list != null) {
            list.add(godeyeJointPointCallback);
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(godeyeJointPointCallback);
        this.mCustomEventJointPointHandlers.put(str, arrayList);
    }

    /* access modifiers changed from: private */
    public void executeAndClearCallbacks(List<IGodeyeJointPointCenter.GodeyeJointPointCallback> list) {
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback = list.get(size);
                godeyeJointPointCallback.doCallback();
                if (godeyeJointPointCallback.isDisposable()) {
                    list.remove(size);
                }
            }
        }
    }

    @TargetApi(14)
    class GodeyeAppLifecycleCallback implements Application.ActivityLifecycleCallbacks {
        private int mActivityCounter = 0;

        GodeyeAppLifecycleCallback() {
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
            if (Godeye.sharedInstance().isDebugMode()) {
                GodeyeJointPointCenter.this.executeAndClearCallbacks((List) GodeyeJointPointCenter.this.mActivityLifecycleJointPointHandlers.get(GodeyeJointPointCenter.buildLifecycleKey(activity.getClass().getName(), Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_CREATED)));
            }
        }

        public void onActivityStarted(Activity activity) {
            this.mActivityCounter++;
            if (Godeye.sharedInstance().isDebugMode()) {
                String access$000 = GodeyeJointPointCenter.buildLifecycleKey(activity.getClass().getName(), Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STARTED);
                GodeyeJointPointCenter.this.executeAndClearCallbacks((List) GodeyeJointPointCenter.this.mActivityLifecycleJointPointHandlers.get(access$000));
                Godeye.sharedInstance().addClientEvent(new ClientEvent(Long.valueOf(System.currentTimeMillis()), access$000, (Object) null));
                if (this.mActivityCounter == 1) {
                    GodeyeJointPointCenter.this.executeAndClearCallbacks(GodeyeJointPointCenter.this.mEnterForegroundJointPointHandlers);
                    Godeye.sharedInstance().addClientEvent(new ClientEvent(Long.valueOf(System.currentTimeMillis()), "enter_foreground", (Object) null));
                }
            }
        }

        public void onActivityResumed(Activity activity) {
            if (Godeye.sharedInstance().isDebugMode()) {
                String name = activity.getClass().getName();
                String unused = GodeyeJointPointCenter.this.lastVisitedPage = name;
                String access$000 = GodeyeJointPointCenter.buildLifecycleKey(name, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_RESUMED);
                GodeyeJointPointCenter.this.executeAndClearCallbacks((List) GodeyeJointPointCenter.this.mActivityLifecycleJointPointHandlers.get(access$000));
                Godeye.sharedInstance().addClientEvent(new ClientEvent(Long.valueOf(System.currentTimeMillis()), access$000, (Object) null));
            }
        }

        public void onActivityPaused(Activity activity) {
            if (Godeye.sharedInstance().isDebugMode()) {
                String access$000 = GodeyeJointPointCenter.buildLifecycleKey(activity.getClass().getName(), Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_PAUSED);
                GodeyeJointPointCenter.this.executeAndClearCallbacks((List) GodeyeJointPointCenter.this.mActivityLifecycleJointPointHandlers.get(access$000));
                Godeye.sharedInstance().addClientEvent(new ClientEvent(Long.valueOf(System.currentTimeMillis()), access$000, (Object) null));
            }
        }

        public void onActivityStopped(Activity activity) {
            this.mActivityCounter--;
            if (Godeye.sharedInstance().isDebugMode()) {
                String access$000 = GodeyeJointPointCenter.buildLifecycleKey(activity.getClass().getName(), Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STOPPED);
                GodeyeJointPointCenter.this.executeAndClearCallbacks((List) GodeyeJointPointCenter.this.mActivityLifecycleJointPointHandlers.get(access$000));
                Godeye.sharedInstance().addClientEvent(new ClientEvent(Long.valueOf(System.currentTimeMillis()), access$000, (Object) null));
                if (this.mActivityCounter == 0) {
                    GodeyeJointPointCenter.this.executeAndClearCallbacks(GodeyeJointPointCenter.this.mEnterBackgroundJointPointHandlers);
                    Godeye.sharedInstance().addClientEvent(new ClientEvent(Long.valueOf(System.currentTimeMillis()), "enter_background", (Object) null));
                }
            }
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            if (Godeye.sharedInstance().isDebugMode()) {
                GodeyeJointPointCenter.this.executeAndClearCallbacks((List) GodeyeJointPointCenter.this.mActivityLifecycleJointPointHandlers.get(GodeyeJointPointCenter.buildLifecycleKey(activity.getClass().getName(), Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_SAVEINSTANCESTATE)));
            }
        }

        public void onActivityDestroyed(Activity activity) {
            if (Godeye.sharedInstance().isDebugMode()) {
                String access$000 = GodeyeJointPointCenter.buildLifecycleKey(activity.getClass().getName(), Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_DESTROYED);
                GodeyeJointPointCenter.this.executeAndClearCallbacks((List) GodeyeJointPointCenter.this.mActivityLifecycleJointPointHandlers.get(access$000));
                Godeye.sharedInstance().addClientEvent(new ClientEvent(Long.valueOf(System.currentTimeMillis()), access$000, (Object) null));
            }
        }
    }

    private static class GodeyeBroadcastReceiver extends BroadcastReceiver {
        private final Context mContext;
        private final IGodeyeJointPointCenter.GodeyeJointPointCallback mGodeyeJointPointCallback;

        GodeyeBroadcastReceiver(Context context, IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback) {
            this.mContext = context;
            this.mGodeyeJointPointCallback = godeyeJointPointCallback;
        }

        public void onReceive(Context context, Intent intent) {
            this.mGodeyeJointPointCallback.doCallback();
            if (this.mGodeyeJointPointCallback.isDisposable()) {
                this.mContext.unregisterReceiver(this);
            }
        }
    }

    /* access modifiers changed from: private */
    public static String buildLifecycleKey(String str, String str2) {
        return str + "." + str2;
    }

    private void installStartJointPoint(JointPoint jointPoint, IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback, boolean z) {
        if (z && jointPoint.type.equals(StartupJointPoint.TYPE)) {
            godeyeJointPointCallback.doCallback();
        } else if (jointPoint.type.equals("lifecycle")) {
            LifecycleJointPoint lifecycleJointPoint = (LifecycleJointPoint) jointPoint;
            registerActivityLifecycleCallbackHandler(lifecycleJointPoint.page, lifecycleJointPoint.lifecycleMethod, godeyeJointPointCallback);
        } else if (jointPoint.type.equals("notification")) {
            registerBroadcastHandler(((NotificationJointPoint) jointPoint).action, godeyeJointPointCallback);
        } else if (jointPoint.type.equals(BackgroundJointPoint.TYPE)) {
            registerEnterBackgroundCallbackHandler(godeyeJointPointCallback);
        } else if (jointPoint.type.equals(ForegroundJointPoint.TYPE)) {
            registerEnterForegroundCallbackHandler(godeyeJointPointCallback);
        } else if (jointPoint.type.equals("event")) {
            registerCustomEventHandler(((EventJointPoint) jointPoint).eventName, godeyeJointPointCallback);
        }
    }

    private void installStopJointPoint(JointPoint jointPoint, IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback) {
        if (jointPoint.type.equals("lifecycle")) {
            LifecycleJointPoint lifecycleJointPoint = (LifecycleJointPoint) jointPoint;
            registerActivityLifecycleCallbackHandler(lifecycleJointPoint.page, lifecycleJointPoint.lifecycleMethod, godeyeJointPointCallback);
        } else if (jointPoint.type.equals("notification")) {
            registerBroadcastHandler(((NotificationJointPoint) jointPoint).action, godeyeJointPointCallback);
        } else if (jointPoint.type.equals(BackgroundJointPoint.TYPE)) {
            registerEnterBackgroundCallbackHandler(godeyeJointPointCallback);
        } else if (jointPoint.type.equals(ForegroundJointPoint.TYPE)) {
            registerEnterForegroundCallbackHandler(godeyeJointPointCallback);
        } else if (jointPoint.type.equals("event")) {
            registerCustomEventHandler(((EventJointPoint) jointPoint).eventName, godeyeJointPointCallback);
        }
    }

    private static class GodeyeStartJointPointCallbackWithDelayStop extends IGodeyeJointPointCenter.GodeyeJointPointCallback {
        private final long mDelayStopMilliSeconds;
        /* access modifiers changed from: private */
        public final IGodeyeJointPointCenter.GodeyeJointPointCallback mGodeyeJointPointCallbackStart;
        /* access modifiers changed from: private */
        public final IGodeyeJointPointCenter.GodeyeJointPointCallback mGodeyeJointPointCallbackStop;

        GodeyeStartJointPointCallbackWithDelayStop(long j, IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback, IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback2) {
            this.mDelayStopMilliSeconds = j;
            this.mGodeyeJointPointCallbackStart = godeyeJointPointCallback;
            this.mGodeyeJointPointCallbackStop = godeyeJointPointCallback2;
        }

        public void doCallback() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    GodeyeStartJointPointCallbackWithDelayStop.this.mGodeyeJointPointCallbackStart.doCallback();
                }
            });
            if (this.mDelayStopMilliSeconds > 0) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        GodeyeStartJointPointCallbackWithDelayStop.this.mGodeyeJointPointCallbackStop.doCallback();
                    }
                }, this.mDelayStopMilliSeconds);
            }
        }
    }

    private static class DelayStopHandler extends Handler {
        private final IGodeyeJointPointCenter.GodeyeJointPointCallback mGodeyeJointPointCallback;

        DelayStopHandler(IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback) {
            this.mGodeyeJointPointCallback = godeyeJointPointCallback;
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            this.mGodeyeJointPointCallback.doCallback();
        }
    }

    private static class GodeyeJointPointCallbackWithStartHook extends IGodeyeJointPointCenter.GodeyeJointPointCallback {
        private final IGodeyeJointPointCenter.GodeyeJointPointCallback mGodeyeJointPointCallback;

        public GodeyeJointPointCallbackWithStartHook(IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback) {
            this.mGodeyeJointPointCallback = godeyeJointPointCallback;
        }

        public boolean isDisposable() {
            return this.mGodeyeJointPointCallback.isDisposable();
        }

        public void doCallback() {
            this.mGodeyeJointPointCallback.doCallback();
            Godeye.sharedInstance().addClientEvent(new ClientEvent(Long.valueOf(System.currentTimeMillis()), "global_start", (Object) null));
        }
    }

    private static class GodeyeJointPointCallbackWithStopHook extends IGodeyeJointPointCenter.GodeyeJointPointCallback {
        private final IGodeyeJointPointCenter.GodeyeJointPointCallback mGodeyeJointPointCallback;

        public GodeyeJointPointCallbackWithStopHook(IGodeyeJointPointCenter.GodeyeJointPointCallback godeyeJointPointCallback) {
            this.mGodeyeJointPointCallback = godeyeJointPointCallback;
        }

        public boolean isDisposable() {
            return this.mGodeyeJointPointCallback.isDisposable();
        }

        public void doCallback() {
            Godeye.sharedInstance().addClientEvent(new ClientEvent(Long.valueOf(System.currentTimeMillis()), "global_end", (Object) null));
            this.mGodeyeJointPointCallback.doCallback();
        }
    }
}
