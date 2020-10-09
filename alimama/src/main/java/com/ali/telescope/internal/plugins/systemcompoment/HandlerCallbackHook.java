package com.ali.telescope.internal.plugins.systemcompoment;

import alimama.com.unweventparse.constants.EventConstants;
import android.app.Application;
import android.app.Service;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.ali.telescope.internal.plugins.systemcompoment.ActivityThreadData;
import com.ali.telescope.util.Reflector;
import com.ali.telescope.util.StrictRuntime;
import com.ali.telescope.util.TelescopeLog;
import com.ali.telescope.util.TimeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class HandlerCallbackHook implements Handler.Callback, IHandlerHook {
    private static final String TAG = "HandlerCallback";
    private Map<IBinder, Object> mActivities;
    private Object mActivityThread;
    private Class mActivityThreadClass;
    private Application mApplication;
    private Field mArgs1ForSomeArgs;
    private Field mConnectionField;
    private Handler mHandler;
    private LifecycleCallStateDispatchListener mMessageDispatchListener;
    private Handler.Callback mOriginCallback;
    private Field mOuterReceiverDispatcherField;
    private Field mOuterServiceDispatcherField;
    private Class mReceiverDispatcher_ArgsClz;
    private Field mReceiverField;
    private Class mServiceDispatcher_RunConnectionClz;
    private Map<IBinder, Service> mServices;
    private Class mSomeArgs;

    public boolean hook(Application application, LifecycleCallStateDispatchListener lifecycleCallStateDispatchListener) {
        this.mApplication = application;
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
            Method declaredMethod = this.mActivityThreadClass.getDeclaredMethod("currentActivityThread", new Class[0]);
            declaredMethod.setAccessible(true);
            this.mActivityThread = declaredMethod.invoke((Object) null, new Object[0]);
            Field declaredField = this.mActivityThreadClass.getDeclaredField("mH");
            declaredField.setAccessible(true);
            this.mHandler = (Handler) declaredField.get(this.mActivityThread);
            Field declaredField2 = Handler.class.getDeclaredField("mCallback");
            declaredField2.setAccessible(true);
            this.mOriginCallback = (Handler.Callback) declaredField2.get(this.mHandler);
            declaredField2.set(this.mHandler, this);
            TelescopeLog.d(TAG, "HOOK mH Handler success!");
            return true;
        } catch (Exception e) {
            TelescopeLog.d(TAG, "HOOK mH Handler fail! " + e);
            StrictRuntime.onHandle(e);
            return false;
        }
    }

    public void setMessageDispatchListener(LifecycleCallStateDispatchListener lifecycleCallStateDispatchListener) {
        this.mMessageDispatchListener = lifecycleCallStateDispatchListener;
    }

    public boolean handleMessage(Message message) {
        String str;
        String name;
        int i;
        IBinder iBinder;
        if (this.mMessageDispatchListener == null) {
            return originCall(message);
        }
        LifecycleCallState lifecycleCallState = null;
        if (message.what == MessageConstants.LAUNCH_ACTIVITY) {
            str = ActivityThreadData.ActivityClientRecord.wrap(message.obj).intent.resolveActivity(this.mApplication.getPackageManager()).getClassName();
        } else {
            if (message.what == MessageConstants.RESUME_ACTIVITY || message.what == MessageConstants.PAUSE_ACTIVITY || message.what == MessageConstants.PAUSE_ACTIVITY_FINISHING || message.what == MessageConstants.STOP_ACTIVITY_HIDE || message.what == MessageConstants.STOP_ACTIVITY_SHOW) {
                if (Build.VERSION.SDK_INT <= 23) {
                    iBinder = (IBinder) message.obj;
                } else {
                    try {
                        iBinder = (IBinder) this.mArgs1ForSomeArgs.get(message.obj);
                    } catch (Exception e) {
                        StrictRuntime.onHandle(e);
                        iBinder = null;
                    }
                }
                ActivityThreadData.ActivityClientRecord activity = getActivity(iBinder);
                if (activity != null) {
                    str = activity.intent.getComponent().getClassName();
                } else {
                    TelescopeLog.w(TAG, MessageConstants.readableString(message.what) + "Got Null Record !! ");
                }
            } else if (message.what == MessageConstants.DESTROY_ACTIVITY) {
                IBinder iBinder2 = (IBinder) message.obj;
                if (getActivity(iBinder2) != null) {
                    str = getActivity(iBinder2).intent.getComponent().getClassName();
                } else {
                    TelescopeLog.w(TAG, MessageConstants.readableString(message.what) + "Got Null Record !! ");
                }
            } else if (message.what == MessageConstants.CREATE_SERVICE) {
                str = ActivityThreadData.CreateServiceData.wrap(message.obj).info.name;
            } else if (message.what == MessageConstants.BIND_SERVICE || message.what == MessageConstants.UNBIND_SERVICE) {
                Service service = getService(ActivityThreadData.BindServiceData.wrap(message.obj).token);
                if (service != null) {
                    str = service.getClass().getName();
                } else {
                    TelescopeLog.w(TAG, MessageConstants.readableString(message.what) + "Got Null SERVICE !! ");
                }
            } else if (message.what == MessageConstants.SERVICE_ARGS) {
                Service service2 = getService(ActivityThreadData.ServiceArgsData.wrap(message.obj).token);
                if (service2 != null) {
                    str = service2.getClass().getName();
                } else {
                    TelescopeLog.w(TAG, MessageConstants.readableString(message.what) + "Got Null SERVICE !! ");
                }
            } else if (message.what == MessageConstants.STOP_SERVICE) {
                Service service3 = getService((IBinder) message.obj);
                if (service3 != null) {
                    str = service3.getClass().getName();
                } else {
                    TelescopeLog.w(TAG, MessageConstants.readableString(message.what) + "Got Null SERVICE !! ");
                }
            } else if (message.what == MessageConstants.RECEIVER) {
                str = ActivityThreadData.ReceiverData.wrap(message.obj).intent.getComponent().getClassName();
            }
            str = null;
        }
        int i2 = message.what;
        if (message.what == 0) {
            if (message.getCallback().getClass() == this.mReceiverDispatcher_ArgsClz) {
                try {
                    name = this.mReceiverField.get(this.mOuterReceiverDispatcherField.get(message.getCallback())).getClass().getName();
                    try {
                        i = MessageConstants.DYNAMICAL_RECEIVER;
                    } catch (Exception e2) {
                        String str2 = name;
                        e = e2;
                        str = str2;
                        StrictRuntime.onHandle(e);
                        lifecycleCallState = new LifecycleCallState();
                        lifecycleCallState.className = str;
                        lifecycleCallState.handleState = 0;
                        lifecycleCallState.what = i2;
                        lifecycleCallState.beforeHandleTime = TimeUtils.getTime();
                        this.mMessageDispatchListener.onLifecycleStateChange(lifecycleCallState);
                        boolean originCall = originCall(message);
                        lifecycleCallState.handleState = 1;
                        lifecycleCallState.afterHandleTime = TimeUtils.getTime();
                        this.mMessageDispatchListener.onLifecycleStateChange(lifecycleCallState);
                        return originCall;
                    }
                } catch (Exception e3) {
                    e = e3;
                    StrictRuntime.onHandle(e);
                    lifecycleCallState = new LifecycleCallState();
                    lifecycleCallState.className = str;
                    lifecycleCallState.handleState = 0;
                    lifecycleCallState.what = i2;
                    lifecycleCallState.beforeHandleTime = TimeUtils.getTime();
                    this.mMessageDispatchListener.onLifecycleStateChange(lifecycleCallState);
                    boolean originCall2 = originCall(message);
                    lifecycleCallState.handleState = 1;
                    lifecycleCallState.afterHandleTime = TimeUtils.getTime();
                    this.mMessageDispatchListener.onLifecycleStateChange(lifecycleCallState);
                    return originCall2;
                }
            } else if (message.getCallback().getClass() == this.mServiceDispatcher_RunConnectionClz) {
                try {
                    name = this.mConnectionField.get(this.mOuterServiceDispatcherField.get(message.getCallback())).getClass().getName();
                    try {
                        i = MessageConstants.CALL_BACK_SERVICE_CONNECTION;
                    } catch (Exception e4) {
                        String str3 = name;
                        e = e4;
                        str = str3;
                    }
                } catch (Exception e5) {
                    e = e5;
                    StrictRuntime.onHandle(e);
                    lifecycleCallState = new LifecycleCallState();
                    lifecycleCallState.className = str;
                    lifecycleCallState.handleState = 0;
                    lifecycleCallState.what = i2;
                    lifecycleCallState.beforeHandleTime = TimeUtils.getTime();
                    this.mMessageDispatchListener.onLifecycleStateChange(lifecycleCallState);
                    boolean originCall22 = originCall(message);
                    lifecycleCallState.handleState = 1;
                    lifecycleCallState.afterHandleTime = TimeUtils.getTime();
                    this.mMessageDispatchListener.onLifecycleStateChange(lifecycleCallState);
                    return originCall22;
                }
            }
            i2 = i;
            str = name;
        }
        if (!(this.mMessageDispatchListener == null || str == null)) {
            lifecycleCallState = new LifecycleCallState();
            lifecycleCallState.className = str;
            lifecycleCallState.handleState = 0;
            lifecycleCallState.what = i2;
            lifecycleCallState.beforeHandleTime = TimeUtils.getTime();
            this.mMessageDispatchListener.onLifecycleStateChange(lifecycleCallState);
        }
        boolean originCall222 = originCall(message);
        if (!(this.mMessageDispatchListener == null || lifecycleCallState == null)) {
            lifecycleCallState.handleState = 1;
            lifecycleCallState.afterHandleTime = TimeUtils.getTime();
            this.mMessageDispatchListener.onLifecycleStateChange(lifecycleCallState);
        }
        return originCall222;
    }

    private boolean originCall(Message message) {
        if (this.mOriginCallback != null ? this.mOriginCallback.handleMessage(message) : false) {
            return true;
        }
        this.mHandler.handleMessage(message);
        return true;
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
