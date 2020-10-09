package com.ali.telescope.internal.plugins.systemcompoment;

import alimama.com.unweventparse.constants.EventConstants;
import android.app.ActivityThread;
import android.app.Application;
import android.app.Service;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Keep;
import com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData;
import com.ali.telescope.util.Reflector;
import com.ali.telescope.util.StrictRuntime;
import java.lang.reflect.Field;
import java.util.Map;

@Keep
public class FakeActivityThread$FakeActivityThread$H extends ActivityThread.H {
    private static final String TAG = "HandlerCallback";
    private Map<IBinder, Object> mActivities;
    private Object mActivityThread;
    private Class mActivityThreadClass;
    private Application mApplication;
    private Field mArgs1ForSomeArgs;
    private Field mConnectionField;
    private LifecycleCallStateDispatchListener mMessageDispatchListener;
    private Handler mOriginH;
    private Field mOuterReceiverDispatcherField;
    private Field mOuterServiceDispatcherField;
    private Class mReceiverDispatcher_ArgsClz;
    private Field mReceiverField;
    private Class mServiceDispatcher_RunConnectionClz;
    private Map<IBinder, Service> mServices;
    private Class mSomeArgs;
    final /* synthetic */ FakeActivityThread this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FakeActivityThread$FakeActivityThread$H(FakeActivityThread fakeActivityThread, Handler handler, Application application, Object obj, LifecycleCallStateDispatchListener lifecycleCallStateDispatchListener) {
        super();
        this.this$0 = fakeActivityThread;
        this.mOriginH = handler;
        this.mApplication = application;
        this.mActivityThread = obj;
        this.mMessageDispatchListener = lifecycleCallStateDispatchListener;
        try {
            this.mActivityThreadClass = Class.forName("android.app.ActivityThread");
            this.mReceiverDispatcher_ArgsClz = Class.forName("android.app.LoadedApk$ReceiverDispatcher$Args");
            Class<?> cls = Class.forName("android.app.LoadedApk$ReceiverDispatcher");
            this.mReceiverField = Reflector.field(cls, "mReceiver");
            this.mOuterReceiverDispatcherField = Reflector.fieldWithMatchType(this.mReceiverDispatcher_ArgsClz, cls).get(0);
            this.mServiceDispatcher_RunConnectionClz = Class.forName("android.app.LoadedApk$ServiceDispatcher$RunConnection");
            Class<?> cls2 = Class.forName("android.app.LoadedApk$ServiceDispatcher");
            this.mConnectionField = Reflector.field(cls2, "mConnection");
            this.mOuterServiceDispatcherField = Reflector.fieldWithMatchType(this.mServiceDispatcher_RunConnectionClz, cls2).get(0);
            if (Build.VERSION.SDK_INT > 23) {
                this.mSomeArgs = Class.forName("com.android.internal.os.SomeArgs");
                this.mArgs1ForSomeArgs = Reflector.field(this.mSomeArgs, EventConstants.UT.ARG1);
            }
        } catch (Exception e) {
            StrictRuntime.onHandle(e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:91:0x0256 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:94:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispatchMessage(android.os.Message r9) {
        /*
            r8 = this;
            com.ali.telescope.internal.plugins.systemcompoment.LifecycleCallStateDispatchListener r0 = r8.mMessageDispatchListener
            if (r0 != 0) goto L_0x000a
            android.os.Handler r0 = r8.mOriginH
            r0.dispatchMessage(r9)
            return
        L_0x000a:
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.LAUNCH_ACTIVITY
            r2 = 0
            r3 = 1
            r4 = 0
            if (r0 != r1) goto L_0x002b
            java.lang.Object r0 = r9.obj
            com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData$ActivityClientRecord r0 = com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData.ActivityClientRecord.wrap(r0)
            android.content.Intent r0 = r0.intent
            android.app.Application r1 = r8.mApplication
            android.content.pm.PackageManager r1 = r1.getPackageManager()
            android.content.ComponentName r0 = r0.resolveActivity(r1)
            java.lang.String r0 = r0.getClassName()
            goto L_0x01c8
        L_0x002b:
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.RESUME_ACTIVITY
            if (r0 == r1) goto L_0x017a
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.PAUSE_ACTIVITY
            if (r0 == r1) goto L_0x017a
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.PAUSE_ACTIVITY_FINISHING
            if (r0 == r1) goto L_0x017a
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.STOP_ACTIVITY_HIDE
            if (r0 == r1) goto L_0x017a
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.STOP_ACTIVITY_SHOW
            if (r0 != r1) goto L_0x004b
            goto L_0x017a
        L_0x004b:
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.DESTROY_ACTIVITY
            if (r0 != r1) goto L_0x008d
            java.lang.Object r0 = r9.obj
            android.os.IBinder r0 = (android.os.IBinder) r0
            com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData$ActivityClientRecord r1 = r8.getActivity(r0)
            if (r1 == 0) goto L_0x006b
            com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData$ActivityClientRecord r0 = r8.getActivity(r0)
            android.content.Intent r0 = r0.intent
            android.content.ComponentName r0 = r0.getComponent()
            java.lang.String r0 = r0.getClassName()
            goto L_0x01c8
        L_0x006b:
            java.lang.String r0 = "HandlerCallback"
            java.lang.String[] r1 = new java.lang.String[r3]
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            int r6 = r9.what
            java.lang.String r6 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.readableString(r6)
            r5.append(r6)
            java.lang.String r6 = "Got Null Record !! "
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            r1[r4] = r5
            com.ali.telescope.util.TelescopeLog.w(r0, r1)
            goto L_0x013f
        L_0x008d:
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.CREATE_SERVICE
            if (r0 != r1) goto L_0x009f
            java.lang.Object r0 = r9.obj
            com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData$CreateServiceData r0 = com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData.CreateServiceData.wrap(r0)
            android.content.pm.ServiceInfo r0 = r0.info
            java.lang.String r0 = r0.name
            goto L_0x01c8
        L_0x009f:
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.BIND_SERVICE
            if (r0 == r1) goto L_0x0142
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.UNBIND_SERVICE
            if (r0 != r1) goto L_0x00ad
            goto L_0x0142
        L_0x00ad:
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.SERVICE_ARGS
            if (r0 != r1) goto L_0x00ec
            java.lang.Object r0 = r9.obj
            com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData$ServiceArgsData r0 = com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData.ServiceArgsData.wrap(r0)
            android.os.IBinder r0 = r0.token
            android.app.Service r0 = r8.getService(r0)
            if (r0 == 0) goto L_0x00cb
            java.lang.Class r0 = r0.getClass()
            java.lang.String r0 = r0.getName()
            goto L_0x01c8
        L_0x00cb:
            java.lang.String r0 = "HandlerCallback"
            java.lang.String[] r1 = new java.lang.String[r3]
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            int r6 = r9.what
            java.lang.String r6 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.readableString(r6)
            r5.append(r6)
            java.lang.String r6 = "Got Null SERVICE !! "
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            r1[r4] = r5
            com.ali.telescope.util.TelescopeLog.w(r0, r1)
            goto L_0x013f
        L_0x00ec:
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.STOP_SERVICE
            if (r0 != r1) goto L_0x0127
            java.lang.Object r0 = r9.obj
            android.os.IBinder r0 = (android.os.IBinder) r0
            android.app.Service r0 = r8.getService(r0)
            if (r0 == 0) goto L_0x0106
            java.lang.Class r0 = r0.getClass()
            java.lang.String r0 = r0.getName()
            goto L_0x01c8
        L_0x0106:
            java.lang.String r0 = "HandlerCallback"
            java.lang.String[] r1 = new java.lang.String[r3]
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            int r6 = r9.what
            java.lang.String r6 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.readableString(r6)
            r5.append(r6)
            java.lang.String r6 = "Got Null SERVICE !! "
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            r1[r4] = r5
            com.ali.telescope.util.TelescopeLog.w(r0, r1)
            goto L_0x013f
        L_0x0127:
            int r0 = r9.what
            int r1 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.RECEIVER
            if (r0 != r1) goto L_0x013f
            java.lang.Object r0 = r9.obj
            com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData$ReceiverData r0 = com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData.ReceiverData.wrap(r0)
            android.content.Intent r0 = r0.intent
            android.content.ComponentName r0 = r0.getComponent()
            java.lang.String r0 = r0.getClassName()
            goto L_0x01c8
        L_0x013f:
            r0 = r2
            goto L_0x01c8
        L_0x0142:
            java.lang.Object r0 = r9.obj
            com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData$BindServiceData r0 = com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData.BindServiceData.wrap(r0)
            android.os.IBinder r0 = r0.token
            android.app.Service r0 = r8.getService(r0)
            if (r0 == 0) goto L_0x0159
            java.lang.Class r0 = r0.getClass()
            java.lang.String r0 = r0.getName()
            goto L_0x01c8
        L_0x0159:
            java.lang.String r0 = "HandlerCallback"
            java.lang.String[] r1 = new java.lang.String[r3]
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            int r6 = r9.what
            java.lang.String r6 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.readableString(r6)
            r5.append(r6)
            java.lang.String r6 = "Got Null SERVICE !! "
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            r1[r4] = r5
            com.ali.telescope.util.TelescopeLog.w(r0, r1)
            goto L_0x013f
        L_0x017a:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 23
            if (r0 > r1) goto L_0x0185
            java.lang.Object r0 = r9.obj
            android.os.IBinder r0 = (android.os.IBinder) r0
            goto L_0x0195
        L_0x0185:
            java.lang.Object r0 = r9.obj
            java.lang.reflect.Field r1 = r8.mArgs1ForSomeArgs     // Catch:{ Exception -> 0x0190 }
            java.lang.Object r0 = r1.get(r0)     // Catch:{ Exception -> 0x0190 }
            android.os.IBinder r0 = (android.os.IBinder) r0     // Catch:{ Exception -> 0x0190 }
            goto L_0x0195
        L_0x0190:
            r0 = move-exception
            com.ali.telescope.util.StrictRuntime.onHandle(r0)
            r0 = r2
        L_0x0195:
            com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData$ActivityClientRecord r0 = r8.getActivity(r0)
            if (r0 == 0) goto L_0x01a6
            android.content.Intent r0 = r0.intent
            android.content.ComponentName r0 = r0.getComponent()
            java.lang.String r0 = r0.getClassName()
            goto L_0x01c8
        L_0x01a6:
            java.lang.String r0 = "HandlerCallback"
            java.lang.String[] r1 = new java.lang.String[r3]
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            int r6 = r9.what
            java.lang.String r6 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.readableString(r6)
            r5.append(r6)
            java.lang.String r6 = "Got Null Record !! "
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            r1[r4] = r5
            com.ali.telescope.util.TelescopeLog.w(r0, r1)
            goto L_0x013f
        L_0x01c8:
            int r1 = r9.what
            int r5 = r9.what
            if (r5 != 0) goto L_0x0231
            java.lang.Runnable r5 = r9.getCallback()
            java.lang.Class r5 = r5.getClass()
            java.lang.Class r6 = r8.mReceiverDispatcher_ArgsClz
            if (r5 != r6) goto L_0x0201
            java.lang.reflect.Field r5 = r8.mOuterReceiverDispatcherField     // Catch:{ Exception -> 0x01fc }
            java.lang.Runnable r6 = r9.getCallback()     // Catch:{ Exception -> 0x01fc }
            java.lang.Object r5 = r5.get(r6)     // Catch:{ Exception -> 0x01fc }
            java.lang.reflect.Field r6 = r8.mReceiverField     // Catch:{ Exception -> 0x01fc }
            java.lang.Object r5 = r6.get(r5)     // Catch:{ Exception -> 0x01fc }
            java.lang.Class r5 = r5.getClass()     // Catch:{ Exception -> 0x01fc }
            java.lang.String r5 = r5.getName()     // Catch:{ Exception -> 0x01fc }
            int r0 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.DYNAMICAL_RECEIVER     // Catch:{ Exception -> 0x01f7 }
        L_0x01f4:
            r1 = r0
            r0 = r5
            goto L_0x0231
        L_0x01f7:
            r0 = move-exception
            r7 = r5
            r5 = r0
            r0 = r7
            goto L_0x01fd
        L_0x01fc:
            r5 = move-exception
        L_0x01fd:
            com.ali.telescope.util.StrictRuntime.onHandle(r5)
            goto L_0x0231
        L_0x0201:
            java.lang.Runnable r5 = r9.getCallback()
            java.lang.Class r5 = r5.getClass()
            java.lang.Class r6 = r8.mServiceDispatcher_RunConnectionClz
            if (r5 != r6) goto L_0x0231
            java.lang.reflect.Field r5 = r8.mOuterServiceDispatcherField     // Catch:{ Exception -> 0x022d }
            java.lang.Runnable r6 = r9.getCallback()     // Catch:{ Exception -> 0x022d }
            java.lang.Object r5 = r5.get(r6)     // Catch:{ Exception -> 0x022d }
            java.lang.reflect.Field r6 = r8.mConnectionField     // Catch:{ Exception -> 0x022d }
            java.lang.Object r5 = r6.get(r5)     // Catch:{ Exception -> 0x022d }
            java.lang.Class r5 = r5.getClass()     // Catch:{ Exception -> 0x022d }
            java.lang.String r5 = r5.getName()     // Catch:{ Exception -> 0x022d }
            int r0 = com.ali.telescope.internal.plugins.systemcompoment.MessageConstants.CALL_BACK_SERVICE_CONNECTION     // Catch:{ Exception -> 0x0228 }
            goto L_0x01f4
        L_0x0228:
            r0 = move-exception
            r7 = r5
            r5 = r0
            r0 = r7
            goto L_0x022e
        L_0x022d:
            r5 = move-exception
        L_0x022e:
            com.ali.telescope.util.StrictRuntime.onHandle(r5)
        L_0x0231:
            com.ali.telescope.internal.plugins.systemcompoment.LifecycleCallStateDispatchListener r5 = r8.mMessageDispatchListener
            if (r5 == 0) goto L_0x024d
            if (r0 == 0) goto L_0x024d
            com.ali.telescope.internal.plugins.systemcompoment.LifecycleCallState r2 = new com.ali.telescope.internal.plugins.systemcompoment.LifecycleCallState
            r2.<init>()
            r2.className = r0
            r2.handleState = r4
            r2.what = r1
            long r0 = com.ali.telescope.util.TimeUtils.getTime()
            r2.beforeHandleTime = r0
            com.ali.telescope.internal.plugins.systemcompoment.LifecycleCallStateDispatchListener r0 = r8.mMessageDispatchListener
            r0.onLifecycleStateChange(r2)
        L_0x024d:
            android.os.Handler r0 = r8.mOriginH
            r0.dispatchMessage(r9)
            com.ali.telescope.internal.plugins.systemcompoment.LifecycleCallStateDispatchListener r9 = r8.mMessageDispatchListener
            if (r9 == 0) goto L_0x0265
            if (r2 == 0) goto L_0x0265
            r2.handleState = r3
            long r0 = com.ali.telescope.util.TimeUtils.getTime()
            r2.afterHandleTime = r0
            com.ali.telescope.internal.plugins.systemcompoment.LifecycleCallStateDispatchListener r9 = r8.mMessageDispatchListener
            r9.onLifecycleStateChange(r2)
        L_0x0265:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.internal.plugins.systemcompoment.FakeActivityThread$FakeActivityThread$H.dispatchMessage(android.os.Message):void");
    }

    public Service getService(IBinder iBinder) {
        if (this.mServices == null) {
            try {
                this.mServices = (Map) Reflector.field(this.mActivityThreadClass, "mServices").get(this.mActivityThread);
            } catch (Exception e) {
                StrictRuntime.onHandle(e);
            }
        }
        if (this.mServices != null) {
            return this.mServices.get(iBinder);
        }
        return null;
    }

    public ActivityThreadData.ActivityClientRecord getActivity(IBinder iBinder) {
        if (this.mActivities == null) {
            try {
                this.mActivities = (Map) Reflector.field(this.mActivityThreadClass, "mActivities").get(this.mActivityThread);
            } catch (Exception e) {
                StrictRuntime.onHandle(e);
            }
        }
        if (this.mActivities != null) {
            return ActivityThreadData.ActivityClientRecord.wrap(this.mActivities.get(iBinder));
        }
        return null;
    }
}
