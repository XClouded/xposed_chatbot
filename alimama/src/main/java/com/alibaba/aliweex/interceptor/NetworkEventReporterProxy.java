package com.alibaba.aliweex.interceptor;

import com.alibaba.aliweex.interceptor.utils.ReflectionUtil;
import com.taobao.weex.utils.WXLogUtils;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkEventReporterProxy {
    private static final String NETWORK_REPORTER_IMPL_CLASS = "com.taobao.weex.devtools.inspector.network.NetworkEventReporterImpl";
    private static final String REMOTE_REPORTER_CLASS = "com.taobao.weex.devtools.inspector.network.GeneralEventReporter";
    private static NetworkEventReporterProxy sInstance;
    private ExecutorService inspectorExecutor;
    private Object remoteReporter;

    private NetworkEventReporterProxy() {
        Method method;
        try {
            Class<?> cls = Class.forName(REMOTE_REPORTER_CLASS);
            if (cls != null && (method = cls.getMethod("getInstance", new Class[0])) != null) {
                this.remoteReporter = method.invoke((Object) null, new Object[0]);
                this.inspectorExecutor = Executors.newSingleThreadExecutor();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static NetworkEventReporterProxy getInstance() {
        if (sInstance == null) {
            synchronized (NetworkEventReporterProxy.class) {
                if (sInstance == null) {
                    sInstance = new NetworkEventReporterProxy();
                }
            }
        }
        return sInstance;
    }

    public boolean isEnabled() {
        Object invoke;
        try {
            Class<?> tryGetClassForName = ReflectionUtil.tryGetClassForName(NETWORK_REPORTER_IMPL_CLASS);
            if (tryGetClassForName != null) {
                Method tryGetMethod = ReflectionUtil.tryGetMethod(tryGetClassForName, "get", new Class[0]);
                Method tryGetMethod2 = ReflectionUtil.tryGetMethod(tryGetClassForName, "isEnabled", new Class[0]);
                if (!(tryGetMethod == null || (invoke = tryGetMethod.invoke((Object) null, new Object[0])) == null || tryGetMethod2 == null)) {
                    boolean booleanValue = ((Boolean) ReflectionUtil.tryInvokeMethod(invoke, tryGetMethod2, new Object[0])).booleanValue();
                    WXLogUtils.d("NetworkEventReporterProxy", "Is reporter enabled ? " + booleanValue);
                    return booleanValue;
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return false;
    }

    public void execAsync(Runnable runnable) {
        if (this.inspectorExecutor != null) {
            try {
                this.inspectorExecutor.execute(runnable);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public void requestWillBeSent(InspectRequest inspectRequest) {
        Method tryGetMethod;
        if (this.remoteReporter != null && (tryGetMethod = ReflectionUtil.tryGetMethod(this.remoteReporter.getClass(), "requestWillBeSent", Map.class)) != null) {
            ReflectionUtil.tryInvokeMethod(this.remoteReporter, tryGetMethod, inspectRequest.getData());
        }
    }

    public void responseHeadersReceived(InspectResponse inspectResponse) {
        Method tryGetMethod;
        if (this.remoteReporter != null && (tryGetMethod = ReflectionUtil.tryGetMethod(this.remoteReporter.getClass(), "responseHeadersReceived", Map.class)) != null) {
            ReflectionUtil.tryInvokeMethod(this.remoteReporter, tryGetMethod, inspectResponse.getData());
        }
    }

    public void httpExchangeFailed(String str, String str2) {
        Method tryGetMethod;
        if (this.remoteReporter != null && (tryGetMethod = ReflectionUtil.tryGetMethod(this.remoteReporter.getClass(), "httpExchangeFailed", String.class, String.class)) != null) {
            ReflectionUtil.tryInvokeMethod(this.remoteReporter, tryGetMethod, str, str2);
        }
    }

    public InputStream interpretResponseStream(String str, String str2, String str3, InputStream inputStream, boolean z) {
        Method tryGetMethod;
        if (this.remoteReporter == null || (tryGetMethod = ReflectionUtil.tryGetMethod(this.remoteReporter.getClass(), "interpretResponseStream", String.class, String.class, String.class, InputStream.class, Boolean.TYPE)) == null) {
            return inputStream;
        }
        return (InputStream) ReflectionUtil.tryInvokeMethod(this.remoteReporter, tryGetMethod, str, str2, str3, inputStream, Boolean.valueOf(z));
    }

    public void responseReadFailed(String str, String str2) {
        Method tryGetMethod;
        if (this.remoteReporter != null && (tryGetMethod = ReflectionUtil.tryGetMethod(this.remoteReporter.getClass(), "responseReadFailed", String.class, String.class)) != null) {
            ReflectionUtil.tryInvokeMethod(this.remoteReporter, tryGetMethod, str, str2);
        }
    }

    public void responseReadFinished(String str) {
        Method tryGetMethod;
        if (this.remoteReporter != null && (tryGetMethod = ReflectionUtil.tryGetMethod(this.remoteReporter.getClass(), "responseReadFinished", String.class)) != null) {
            ReflectionUtil.tryInvokeMethod(this.remoteReporter, tryGetMethod, str);
        }
    }

    public void dataSent(String str, int i, int i2) {
        Method tryGetMethod;
        if (this.remoteReporter != null && (tryGetMethod = ReflectionUtil.tryGetMethod(this.remoteReporter.getClass(), "dataSent", String.class, Integer.TYPE, Integer.TYPE)) != null) {
            ReflectionUtil.tryInvokeMethod(this.remoteReporter, tryGetMethod, str, Integer.valueOf(i), Integer.valueOf(i2));
        }
    }

    public void dataReceived(String str, int i, int i2) {
        Method tryGetMethod;
        if (this.remoteReporter != null && (tryGetMethod = ReflectionUtil.tryGetMethod(this.remoteReporter.getClass(), "dataReceived", String.class, Integer.TYPE, Integer.TYPE)) != null) {
            ReflectionUtil.tryInvokeMethod(this.remoteReporter, tryGetMethod, str, Integer.valueOf(i), Integer.valueOf(i2));
        }
    }
}
