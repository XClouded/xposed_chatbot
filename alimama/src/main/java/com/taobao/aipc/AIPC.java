package com.taobao.aipc;

import android.app.ActivityThread;
import android.content.Context;
import com.taobao.aipc.core.channel.IPCInvocationHandler;
import com.taobao.aipc.core.entity.Reply;
import com.taobao.aipc.core.sender.impl.InstanceGettingSender;
import com.taobao.aipc.core.sender.impl.SenderDesignator;
import com.taobao.aipc.core.wrapper.ObjectWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.aipc.logs.IPCLog;
import com.taobao.aipc.utils.IPCRecycle;
import com.taobao.aipc.utils.TypeCenter;
import com.taobao.aipc.utils.TypeUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AIPC {
    private static final IPCRecycle IPC_RECYCLER = IPCRecycle.getInstance();
    private static final String TAG = "AIPC";
    private static final TypeCenter TYPE_CENTER = TypeCenter.getInstance();
    private static Context sContext;

    public static Context getContext() {
        if (sContext == null) {
            try {
                ActivityThread currentActivityThread = ActivityThread.currentActivityThread();
                if (currentActivityThread != null) {
                    sContext = currentActivityThread.getApplication();
                }
            } catch (Exception e) {
                IPCLog.eTag(TAG, "get context Error: ", e);
            }
        }
        return sContext;
    }

    public static void init(Context context) {
        if (sContext == null) {
            sContext = context.getApplicationContext();
            if ((sContext.getApplicationInfo().flags & 2) != 0) {
                debug(true);
            }
        }
    }

    public static void register(Class<?> cls) {
        TYPE_CENTER.register(cls);
    }

    private static <T> T getProxy(ObjectWrapper objectWrapper) {
        Class<?> objectClass = objectWrapper.getObjectClass();
        T newProxyInstance = Proxy.newProxyInstance(objectClass.getClassLoader(), new Class[]{objectClass}, new IPCInvocationHandler(objectWrapper));
        IPC_RECYCLER.register(newProxyInstance, objectWrapper.getTimeStamp());
        return newProxyInstance;
    }

    public static <T> T getService(Class<T> cls, Object... objArr) {
        TypeUtils.validateServiceInterface(cls);
        ObjectWrapper objectWrapper = new ObjectWrapper(cls, 0);
        try {
            Reply send = SenderDesignator.getPostOffice(0, objectWrapper).send((Method) null, objArr);
            if (send == null || send.success()) {
                objectWrapper.setType(3);
                return getProxy(objectWrapper);
            }
            String str = TAG;
            IPCLog.e(str, "Error occurs during creating instance. Error code: " + send.getErrorCode());
            String str2 = TAG;
            IPCLog.e(str2, "Error message: " + send.getMessage());
            return null;
        } catch (IPCException e) {
            IPCLog.eTag(TAG, "get remote service Error: ", e);
            return null;
        }
    }

    public static <T> T getInstance(Class<T> cls, Object... objArr) {
        return getInstanceWithMethodNameInService(cls, "", objArr);
    }

    public static <T> T getInstanceWithMethodName(Class<T> cls, String str, Object... objArr) {
        return getInstanceWithMethodNameInService(cls, str, objArr);
    }

    private static <T> T getInstanceWithMethodNameInService(Class<T> cls, String str, Object... objArr) {
        TypeUtils.validateServiceInterface(cls);
        ObjectWrapper objectWrapper = new ObjectWrapper(cls, 1);
        try {
            Reply send = ((InstanceGettingSender) SenderDesignator.getPostOffice(1, objectWrapper)).send(str, objArr);
            if (send == null || send.success()) {
                objectWrapper.setType(3);
                return getProxy(objectWrapper);
            }
            String str2 = TAG;
            IPCLog.e(str2, "Error occurs during getting instance. Error code: " + send.getErrorCode());
            String str3 = TAG;
            IPCLog.e(str3, "Error message: " + send.getMessage());
            return null;
        } catch (IPCException e) {
            IPCLog.eTag(TAG, "get remote instance Error: ", e);
            return null;
        }
    }

    public static <T> T getUtilityClass(Class<T> cls) {
        TypeUtils.validateServiceInterface(cls);
        ObjectWrapper objectWrapper = new ObjectWrapper(cls, 2);
        try {
            Reply send = SenderDesignator.getPostOffice(2, objectWrapper).send((Method) null, (Object[]) null);
            if (send == null || send.success()) {
                objectWrapper.setType(4);
                return getProxy(objectWrapper);
            }
            String str = TAG;
            IPCLog.e(str, "Error occurs during getting utility class. Error code: " + send.getErrorCode());
            String str2 = TAG;
            IPCLog.e(str2, "Error message: " + send.getMessage());
            return null;
        } catch (IPCException e) {
            IPCLog.eTag(TAG, "get remote utility class Error: ", e);
            return null;
        }
    }

    public static void debug(boolean z) {
        IPCLog.debug(z);
    }
}
