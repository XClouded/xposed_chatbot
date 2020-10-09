package com.taobao.weex.devtools.debug;

import android.text.TextUtils;
import android.util.Log;
import com.taobao.weex.devtools.common.LogRedirector;
import com.taobao.weex.devtools.common.ReflectionUtil;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OkHttp3SocketClient extends SocketClient {
    private static final String CLASS_BUFFER = "okio.Buffer";
    private static final String CLASS_BUFFER_SOURCE = "okio.BufferedSource";
    private static final String CLASS_MEDIATYPE = "okhttp3.MediaType";
    private static final String CLASS_OKHTTP_CLIENT = "okhttp3.OkHttpClient";
    private static final String CLASS_OKHTTP_CLIENT_BUILDER = "okhttp3.OkHttpClient$Builder";
    private static final String CLASS_REQUEST = "okhttp3.Request";
    private static final String CLASS_REQUEST_BODY = "okhttp3.RequestBody";
    private static final String CLASS_REQUEST_BUILDER = "okhttp3.Request$Builder";
    private static final String CLASS_RESPONSE = "okhttp3.Response";
    private static final String CLASS_RESPONSE_BODY = "okhttp3.ResponseBody";
    private static final String CLASS_WEBSOCKET = "okhttp3.ws.WebSocket";
    private static final String CLASS_WEBSOCKET_CALL = "okhttp3.ws.WebSocketCall";
    private static final String CLASS_WEBSOCKET_LISTENER = "okhttp3.ws.WebSocketListener";
    private static final String TAG = "OkHttp3SocketClient";
    private static HashMap<String, Class> sClazzMap = new HashMap<>();
    /* access modifiers changed from: private */
    public Class mBufferedSourceClazz = sClazzMap.get(CLASS_BUFFER_SOURCE);
    private Class mMediaTypeClazz = sClazzMap.get(CLASS_MEDIATYPE);
    private Class mOkHttpClientBuilderClazz = sClazzMap.get(CLASS_OKHTTP_CLIENT_BUILDER);
    private Class mOkHttpClientClazz = sClazzMap.get(CLASS_OKHTTP_CLIENT);
    private Class mRequestBodyClazz = sClazzMap.get(CLASS_REQUEST_BODY);
    private Class mRequestBuilderClazz = sClazzMap.get(CLASS_REQUEST_BUILDER);
    private Class mRequestClazz = sClazzMap.get(CLASS_REQUEST);
    /* access modifiers changed from: private */
    public Class mResponseBodyClazz = sClazzMap.get(CLASS_RESPONSE_BODY);
    private Class mWebSocketCallClazz = sClazzMap.get(CLASS_WEBSOCKET_CALL);
    /* access modifiers changed from: private */
    public Class mWebSocketClazz = sClazzMap.get(CLASS_WEBSOCKET);
    private Class mWebSocketListenerClazz = sClazzMap.get(CLASS_WEBSOCKET_LISTENER);

    static {
        for (String str : new String[]{CLASS_WEBSOCKET, CLASS_WEBSOCKET_LISTENER, CLASS_WEBSOCKET_CALL, CLASS_MEDIATYPE, CLASS_OKHTTP_CLIENT, CLASS_OKHTTP_CLIENT_BUILDER, CLASS_RESPONSE, CLASS_REQUEST, CLASS_REQUEST_BUILDER, CLASS_BUFFER, CLASS_BUFFER_SOURCE, CLASS_REQUEST_BODY, CLASS_RESPONSE_BODY}) {
            sClazzMap.put(str, ReflectionUtil.tryGetClassForName(str));
        }
    }

    public OkHttp3SocketClient(DebugServerProxy debugServerProxy) {
        super(debugServerProxy);
        this.mInvocationHandler = new WebSocketInvocationHandler();
    }

    /* access modifiers changed from: protected */
    public void connect(String str) {
        if (this.mSocketClient == null) {
            try {
                Object newInstance = this.mOkHttpClientBuilderClazz.newInstance();
                Method tryGetMethod = ReflectionUtil.tryGetMethod(this.mOkHttpClientBuilderClazz, "connectTimeout", Long.TYPE, TimeUnit.class);
                Method tryGetMethod2 = ReflectionUtil.tryGetMethod(this.mOkHttpClientBuilderClazz, "writeTimeout", Long.TYPE, TimeUnit.class);
                Method tryGetMethod3 = ReflectionUtil.tryGetMethod(this.mOkHttpClientBuilderClazz, "readTimeout", Long.TYPE, TimeUnit.class);
                this.mSocketClient = ReflectionUtil.tryInvokeMethod(ReflectionUtil.tryInvokeMethod(ReflectionUtil.tryInvokeMethod(ReflectionUtil.tryInvokeMethod(newInstance, tryGetMethod, 30, TimeUnit.SECONDS), tryGetMethod2, 30, TimeUnit.SECONDS), tryGetMethod3, 0, TimeUnit.SECONDS), ReflectionUtil.tryGetMethod(this.mOkHttpClientBuilderClazz, "build", new Class[0]), new Object[0]);
                if (!TextUtils.isEmpty(str)) {
                    Object newInstance2 = this.mRequestBuilderClazz.newInstance();
                    Method tryGetMethod4 = ReflectionUtil.tryGetMethod(this.mRequestBuilderClazz, "url", String.class);
                    Object tryInvokeMethod = ReflectionUtil.tryInvokeMethod(ReflectionUtil.tryInvokeMethod(newInstance2, tryGetMethod4, str), ReflectionUtil.tryGetMethod(this.mRequestBuilderClazz, "build", new Class[0]), new Object[0]);
                    Method tryGetMethod5 = ReflectionUtil.tryGetMethod(this.mWebSocketCallClazz, "enqueue", this.mWebSocketListenerClazz);
                    Method tryGetMethod6 = ReflectionUtil.tryGetMethod(this.mWebSocketCallClazz, "create", this.mOkHttpClientClazz, this.mRequestClazz);
                    Object tryInvokeMethod2 = ReflectionUtil.tryInvokeMethod(this.mWebSocketCallClazz, tryGetMethod6, this.mSocketClient, tryInvokeMethod);
                    this.mWebSocketListener = Proxy.newProxyInstance(this.mWebSocketListenerClazz.getClassLoader(), new Class[]{this.mWebSocketListenerClazz}, this.mInvocationHandler);
                    ReflectionUtil.tryInvokeMethod(tryInvokeMethod2, tryGetMethod5, this.mWebSocketListener);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
        } else {
            throw new IllegalStateException("OkHttp3SocketClient is already initialized.");
        }
    }

    /* access modifiers changed from: protected */
    public void close() {
        if (this.mWebSocket != null) {
            Method tryGetMethod = ReflectionUtil.tryGetMethod(this.mWebSocketClazz, "close", Integer.TYPE, String.class);
            ReflectionUtil.tryInvokeMethod(this.mWebSocket, tryGetMethod, 1000, "End of session");
            this.mWebSocket = null;
            WXLogUtils.w(TAG, "Close websocket connection");
        }
    }

    /* access modifiers changed from: protected */
    public void sendProtocolMessage(int i, String str) {
        if (this.mWebSocket != null) {
            Method tryGetMethod = ReflectionUtil.tryGetMethod(this.mRequestBodyClazz, "create", this.mMediaTypeClazz, String.class);
            Object fieldValue = ReflectionUtil.getFieldValue(ReflectionUtil.tryGetDeclaredField(this.mWebSocketClazz, "TEXT"), (Object) null);
            Object tryInvokeMethod = ReflectionUtil.tryInvokeMethod(this.mRequestBodyClazz, tryGetMethod, fieldValue, str);
            Method tryGetMethod2 = ReflectionUtil.tryGetMethod(this.mWebSocketClazz, "sendMessage", this.mRequestBodyClazz);
            ReflectionUtil.tryInvokeMethod(this.mWebSocket, tryGetMethod2, tryInvokeMethod);
        }
    }

    /* access modifiers changed from: private */
    public void abort(String str, Throwable th) {
        Log.w(TAG, "Error occurred, shutting down websocket connection: " + str);
        close();
        if (this.mConnectCallback != null) {
            this.mConnectCallback.onFailure(th);
            this.mConnectCallback = null;
        }
    }

    class WebSocketInvocationHandler implements InvocationHandler {
        WebSocketInvocationHandler() {
        }

        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            if ("onOpen".equals(method.getName())) {
                OkHttp3SocketClient.this.mWebSocket = OkHttp3SocketClient.this.mWebSocketClazz.cast(objArr[0]);
                if (OkHttp3SocketClient.this.mConnectCallback != null) {
                    OkHttp3SocketClient.this.mConnectCallback.onSuccess((String) null);
                }
            } else if ("onFailure".equals(method.getName())) {
                OkHttp3SocketClient.this.abort("Websocket onFailure", objArr[0]);
            } else if ("onMessage".equals(method.getName())) {
                Object tryInvokeMethod = ReflectionUtil.tryInvokeMethod(OkHttp3SocketClient.this.mResponseBodyClazz.cast(objArr[0]), ReflectionUtil.tryGetMethod(OkHttp3SocketClient.this.mResponseBodyClazz, "source", new Class[0]), new Object[0]);
                try {
                    OkHttp3SocketClient.this.mProxy.handleMessage((String) ReflectionUtil.tryInvokeMethod(tryInvokeMethod, ReflectionUtil.tryGetMethod(OkHttp3SocketClient.this.mBufferedSourceClazz, "readUtf8", new Class[0]), new Object[0]));
                } catch (Exception e) {
                    if (LogRedirector.isLoggable(OkHttp3SocketClient.TAG, 2)) {
                        LogRedirector.v(OkHttp3SocketClient.TAG, "Unexpected I/O exception processing message: " + e);
                    }
                } catch (Throwable th) {
                    ReflectionUtil.tryInvokeMethod(tryInvokeMethod, ReflectionUtil.tryGetMethod(OkHttp3SocketClient.this.mBufferedSourceClazz, "close", new Class[0]), new Object[0]);
                    throw th;
                }
                ReflectionUtil.tryInvokeMethod(tryInvokeMethod, ReflectionUtil.tryGetMethod(OkHttp3SocketClient.this.mBufferedSourceClazz, "close", new Class[0]), new Object[0]);
            } else if (!"onPong".equals(method.getName()) && "onClose".equals(method.getName()) && OkHttp3SocketClient.this.mHandlerThread != null && OkHttp3SocketClient.this.mHandlerThread.isAlive()) {
                OkHttp3SocketClient.this.mHandler.sendEmptyMessage(3);
            }
            return null;
        }
    }
}
