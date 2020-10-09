package com.taobao.weex.analyzer.core.ws;

import android.text.TextUtils;
import com.taobao.weex.analyzer.utils.ReflectionUtil;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class OkHttpWebSocketClient extends WebSocketClient {
    private static final String CLASS_BUFFER = "okio.Buffer";
    private static final String CLASS_BUFFER_SOURCE = "okio.BufferedSource";
    private static final String CLASS_MEDIATYPE = "com.squareup.okhttp.MediaType";
    private static final String CLASS_OKHTTP_CLIENT = "com.squareup.okhttp.OkHttpClient";
    private static final String CLASS_REQUEST = "com.squareup.okhttp.Request";
    private static final String CLASS_REQUESTBODY = "com.squareup.okhttp.RequestBody";
    private static final String CLASS_REQUEST_BUILDER = "com.squareup.okhttp.Request$Builder";
    private static final String CLASS_RESPONSE = "com.squareup.okhttp.Response";
    private static final String CLASS_RESPONSEBODY = "com.squareup.okhttp.ResponseBody";
    private static final String CLASS_WEBSOCKET = "com.squareup.okhttp.ws.WebSocket";
    private static final String CLASS_WEBSOCKET_CALL = "com.squareup.okhttp.ws.WebSocketCall";
    private static final String CLASS_WEBSOCKET_LISTENER = "com.squareup.okhttp.ws.WebSocketListener";
    private static final String CLASS_WEBSOCKET_PAYLOADTYPE = "com.squareup.okhttp.ws.WebSocket$PayloadType";
    private static final String TAG = "OkHttpSocketClient";
    private static HashMap<String, Class> sClazzMap = new HashMap<>();
    private Class mBufferClazz = sClazzMap.get(CLASS_BUFFER);
    /* access modifiers changed from: private */
    public Class mBufferedSourceClazz = sClazzMap.get(CLASS_BUFFER_SOURCE);
    private Class mMediaTypeClazz = sClazzMap.get(CLASS_MEDIATYPE);
    private Class mOkHttpClientClazz = sClazzMap.get(CLASS_OKHTTP_CLIENT);
    /* access modifiers changed from: private */
    public Class mPayloadTypeClazz = sClazzMap.get(CLASS_WEBSOCKET_PAYLOADTYPE);
    private Class mRequestBodyClazz = sClazzMap.get(CLASS_REQUESTBODY);
    private Class mRequestBuilderClazz = sClazzMap.get(CLASS_REQUEST_BUILDER);
    private Class mRequestClazz = sClazzMap.get(CLASS_REQUEST);
    /* access modifiers changed from: private */
    public Class mResponseBodyClazz = sClazzMap.get(CLASS_RESPONSEBODY);
    private Class mWebSocketCallClazz = sClazzMap.get(CLASS_WEBSOCKET_CALL);
    /* access modifiers changed from: private */
    public Class mWebSocketClazz = sClazzMap.get(CLASS_WEBSOCKET);
    private Class mWebSocketListenerClazz = sClazzMap.get(CLASS_WEBSOCKET_LISTENER);

    static {
        for (String str : new String[]{CLASS_WEBSOCKET, CLASS_WEBSOCKET_LISTENER, CLASS_WEBSOCKET_CALL, CLASS_WEBSOCKET_PAYLOADTYPE, CLASS_OKHTTP_CLIENT, CLASS_RESPONSE, CLASS_REQUEST, CLASS_REQUEST_BUILDER, CLASS_BUFFER, CLASS_BUFFER_SOURCE, CLASS_MEDIATYPE, CLASS_REQUESTBODY, CLASS_RESPONSEBODY}) {
            sClazzMap.put(str, ReflectionUtil.tryGetClassForName(str));
        }
    }

    public OkHttpWebSocketClient(IWebSocketBridge iWebSocketBridge) {
        super(iWebSocketBridge);
        this.mInvocationHandler = new WebSocketInvocationHandler();
    }

    /* access modifiers changed from: protected */
    public void connect(String str) {
        connectWithHeaders(str, (Map<String, String>) null);
    }

    /* access modifiers changed from: protected */
    public void connectWithHeaders(String str, Map<String, String> map) {
        if (this.mSocketClient == null) {
            try {
                this.mSocketClient = this.mOkHttpClientClazz.newInstance();
                Method tryGetMethod = ReflectionUtil.tryGetMethod(this.mOkHttpClientClazz, "setConnectTimeout", Long.TYPE, TimeUnit.class);
                Method tryGetMethod2 = ReflectionUtil.tryGetMethod(this.mOkHttpClientClazz, "setWriteTimeout", Long.TYPE, TimeUnit.class);
                Method tryGetMethod3 = ReflectionUtil.tryGetMethod(this.mOkHttpClientClazz, "setReadTimeout", Long.TYPE, TimeUnit.class);
                ReflectionUtil.tryInvokeMethod(this.mSocketClient, tryGetMethod, 5, TimeUnit.SECONDS);
                ReflectionUtil.tryInvokeMethod(this.mSocketClient, tryGetMethod2, 10, TimeUnit.SECONDS);
                ReflectionUtil.tryInvokeMethod(this.mSocketClient, tryGetMethod3, 0, TimeUnit.SECONDS);
                if (!TextUtils.isEmpty(str)) {
                    Object newInstance = this.mRequestBuilderClazz.newInstance();
                    Method tryGetMethod4 = ReflectionUtil.tryGetMethod(this.mRequestBuilderClazz, "url", String.class);
                    Method tryGetMethod5 = ReflectionUtil.tryGetMethod(this.mRequestBuilderClazz, "build", new Class[0]);
                    Method tryGetMethod6 = ReflectionUtil.tryGetMethod(this.mRequestBuilderClazz, "addHeader", String.class, String.class);
                    Object tryInvokeMethod = ReflectionUtil.tryInvokeMethod(newInstance, tryGetMethod4, str);
                    if (map != null && !map.isEmpty()) {
                        for (Map.Entry next : map.entrySet()) {
                            tryInvokeMethod = ReflectionUtil.tryInvokeMethod(tryInvokeMethod, tryGetMethod6, next.getKey(), next.getValue());
                        }
                    }
                    Object tryInvokeMethod2 = ReflectionUtil.tryInvokeMethod(tryInvokeMethod, tryGetMethod5, new Object[0]);
                    Method tryGetMethod7 = ReflectionUtil.tryGetMethod(this.mWebSocketCallClazz, "enqueue", this.mWebSocketListenerClazz);
                    Method tryGetMethod8 = ReflectionUtil.tryGetMethod(this.mWebSocketCallClazz, "create", this.mOkHttpClientClazz, this.mRequestClazz);
                    Object tryInvokeMethod3 = ReflectionUtil.tryInvokeMethod(this.mWebSocketCallClazz, tryGetMethod8, this.mSocketClient, tryInvokeMethod2);
                    this.mWebSocketListener = Proxy.newProxyInstance(this.mWebSocketListenerClazz.getClassLoader(), new Class[]{this.mWebSocketListenerClazz}, this.mInvocationHandler);
                    ReflectionUtil.tryInvokeMethod(tryInvokeMethod3, tryGetMethod7, this.mWebSocketListener);
                }
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.getMessage());
            }
        } else {
            throw new IllegalStateException("OkHttpSocketClient is already initialized.");
        }
    }

    /* access modifiers changed from: protected */
    public void close() {
        if (this.mWebSocket != null) {
            Method tryGetMethod = ReflectionUtil.tryGetMethod(this.mWebSocketClazz, "close", Integer.TYPE, String.class);
            ReflectionUtil.tryInvokeMethod(this.mWebSocket, tryGetMethod, 1000, "End of session");
            this.mWebSocket = null;
            if (this.mConnectCallback != null) {
                this.mConnectCallback.onClose(-1, "close");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void sendProtocolMessage(int i, String str) {
        if (this.mWebSocket != null) {
            try {
                if (this.mPayloadTypeClazz != null) {
                    Object fieldValue = ReflectionUtil.getFieldValue(ReflectionUtil.tryGetDeclaredField(this.mPayloadTypeClazz, "TEXT"), (Object) null);
                    Method tryGetMethod = ReflectionUtil.tryGetMethod(this.mWebSocketClazz, "sendMessage", this.mPayloadTypeClazz, this.mBufferClazz);
                    Object newInstance = this.mBufferClazz.newInstance();
                    Method tryGetMethod2 = ReflectionUtil.tryGetMethod(this.mBufferClazz, "writeUtf8", String.class);
                    ReflectionUtil.tryInvokeMethod(this.mWebSocket, tryGetMethod, fieldValue, ReflectionUtil.tryInvokeMethod(newInstance, tryGetMethod2, str));
                    return;
                }
                Object fieldValue2 = ReflectionUtil.getFieldValue(ReflectionUtil.tryGetDeclaredField(this.mWebSocketClazz, "TEXT"), (Object) null);
                Method tryGetMethod3 = ReflectionUtil.tryGetMethod(this.mRequestBodyClazz, "create", this.mMediaTypeClazz, String.class);
                Object tryInvokeMethod = ReflectionUtil.tryInvokeMethod(this.mRequestBodyClazz, tryGetMethod3, fieldValue2, str);
                Method tryGetMethod4 = ReflectionUtil.tryGetMethod(this.mWebSocketClazz, "sendMessage", this.mRequestBodyClazz);
                ReflectionUtil.tryInvokeMethod(this.mWebSocket, tryGetMethod4, tryInvokeMethod);
            } catch (Exception e) {
                WXLogUtils.e(TAG, e.getMessage());
            }
        }
    }

    /* access modifiers changed from: private */
    public void abort(String str, Throwable th) {
        WXLogUtils.v(TAG, "Error occurred, shutting down websocket connection: " + str);
        close();
        if (this.mConnectCallback != null) {
            this.mConnectCallback.onFailure(th);
            this.mConnectCallback = null;
        }
    }

    private class WebSocketInvocationHandler implements InvocationHandler {
        private WebSocketInvocationHandler() {
        }

        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            Object obj2;
            if ("onOpen".equals(method.getName())) {
                OkHttpWebSocketClient.this.mWebSocket = OkHttpWebSocketClient.this.mWebSocketClazz.cast(objArr[0]);
                if (OkHttpWebSocketClient.this.mConnectCallback != null) {
                    OkHttpWebSocketClient.this.mConnectCallback.onOpen((String) null);
                }
            } else if ("onFailure".equals(method.getName())) {
                OkHttpWebSocketClient.this.abort("Websocket exception", objArr[0]);
            } else if ("onMessage".equals(method.getName())) {
                if (OkHttpWebSocketClient.this.mPayloadTypeClazz != null) {
                    obj2 = OkHttpWebSocketClient.this.mBufferedSourceClazz.cast(objArr[0]);
                } else {
                    obj2 = ReflectionUtil.tryInvokeMethod(OkHttpWebSocketClient.this.mResponseBodyClazz.cast(objArr[0]), ReflectionUtil.tryGetMethod(OkHttpWebSocketClient.this.mResponseBodyClazz, "source", new Class[0]), new Object[0]);
                }
                try {
                    OkHttpWebSocketClient.this.mProxy.handleMessage((String) ReflectionUtil.tryInvokeMethod(obj2, ReflectionUtil.tryGetMethod(OkHttpWebSocketClient.this.mBufferedSourceClazz, "readUtf8", new Class[0]), new Object[0]));
                } catch (Exception e) {
                    WXLogUtils.v(OkHttpWebSocketClient.TAG, "Unexpected I/O exception processing message: " + e);
                } catch (Throwable th) {
                    ReflectionUtil.tryInvokeMethod(obj2, ReflectionUtil.tryGetMethod(OkHttpWebSocketClient.this.mBufferedSourceClazz, "close", new Class[0]), new Object[0]);
                    throw th;
                }
                ReflectionUtil.tryInvokeMethod(obj2, ReflectionUtil.tryGetMethod(OkHttpWebSocketClient.this.mBufferedSourceClazz, "close", new Class[0]), new Object[0]);
            } else if (!"onPong".equals(method.getName()) && "onClose".equals(method.getName()) && OkHttpWebSocketClient.this.mHandlerThread != null && OkHttpWebSocketClient.this.mHandlerThread.isAlive()) {
                OkHttpWebSocketClient.this.mHandler.sendEmptyMessage(3);
            }
            return null;
        }
    }
}
