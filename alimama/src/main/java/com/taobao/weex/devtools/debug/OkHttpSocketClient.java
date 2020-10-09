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

public class OkHttpSocketClient extends SocketClient {
    private static final String CLASS_BUFFER = "okio.Buffer";
    private static final String CLASS_BUFFER_SOURCE = "okio.BufferedSource";
    private static final String CLASS_MEDIA_TYPE_NEW = "com.squareup.okhttp.MediaType";
    private static final String CLASS_OKHTTP_CLIENT = "com.squareup.okhttp.OkHttpClient";
    private static final String CLASS_REQUEST = "com.squareup.okhttp.Request";
    private static final String CLASS_REQUEST_BODY = "com.squareup.okhttp.RequestBody";
    private static final String CLASS_REQUEST_BUILDER = "com.squareup.okhttp.Request$Builder";
    private static final String CLASS_RESPONSE = "com.squareup.okhttp.Response";
    private static final String CLASS_RESPONSE_BODY = "com.squareup.okhttp.ResponseBody";
    private static final String CLASS_WEBSOCKET = "com.squareup.okhttp.ws.WebSocket";
    private static final String CLASS_WEBSOCKET_CALL = "com.squareup.okhttp.ws.WebSocketCall";
    private static final String CLASS_WEBSOCKET_LISTENER = "com.squareup.okhttp.ws.WebSocketListener";
    private static final String CLASS_WEBSOCKET_PAYLOADTYPE = "com.squareup.okhttp.ws.WebSocket$PayloadType";
    private static final String TAG = "OkHttpSocketClient";
    private static HashMap<String, Class> sClazzMap = new HashMap<>();
    private Class mBufferClazz = sClazzMap.get(CLASS_BUFFER);
    /* access modifiers changed from: private */
    public Class mBufferedSourceClazz = sClazzMap.get(CLASS_BUFFER_SOURCE);
    private Class mMediaTypeClazz = sClazzMap.get(CLASS_WEBSOCKET_PAYLOADTYPE);
    private Class mMediaTypeNewClazz = sClazzMap.get(CLASS_MEDIA_TYPE_NEW);
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
        for (String str : new String[]{CLASS_WEBSOCKET, CLASS_WEBSOCKET_LISTENER, CLASS_WEBSOCKET_CALL, CLASS_WEBSOCKET_PAYLOADTYPE, CLASS_OKHTTP_CLIENT, CLASS_RESPONSE, CLASS_REQUEST, CLASS_REQUEST_BUILDER, CLASS_BUFFER, CLASS_BUFFER_SOURCE, CLASS_REQUEST_BODY, CLASS_MEDIA_TYPE_NEW, CLASS_RESPONSE_BODY}) {
            sClazzMap.put(str, ReflectionUtil.tryGetClassForName(str));
        }
    }

    public OkHttpSocketClient(DebugServerProxy debugServerProxy) {
        super(debugServerProxy);
        this.mInvocationHandler = new WebSocketInvocationHandler();
    }

    /* access modifiers changed from: protected */
    public void connect(String str) {
        if (this.mSocketClient == null) {
            try {
                this.mSocketClient = this.mOkHttpClientClazz.newInstance();
                Method tryGetMethod = ReflectionUtil.tryGetMethod(this.mOkHttpClientClazz, "setConnectTimeout", Long.TYPE, TimeUnit.class);
                Method tryGetMethod2 = ReflectionUtil.tryGetMethod(this.mOkHttpClientClazz, "setWriteTimeout", Long.TYPE, TimeUnit.class);
                Method tryGetMethod3 = ReflectionUtil.tryGetMethod(this.mOkHttpClientClazz, "setReadTimeout", Long.TYPE, TimeUnit.class);
                ReflectionUtil.tryInvokeMethod(this.mSocketClient, tryGetMethod, 0, TimeUnit.SECONDS);
                ReflectionUtil.tryInvokeMethod(this.mSocketClient, tryGetMethod2, 0, TimeUnit.SECONDS);
                ReflectionUtil.tryInvokeMethod(this.mSocketClient, tryGetMethod3, 0, TimeUnit.SECONDS);
                if (!TextUtils.isEmpty(str)) {
                    Object newInstance = this.mRequestBuilderClazz.newInstance();
                    Method tryGetMethod4 = ReflectionUtil.tryGetMethod(this.mRequestBuilderClazz, "url", String.class);
                    Object tryInvokeMethod = ReflectionUtil.tryInvokeMethod(ReflectionUtil.tryInvokeMethod(newInstance, tryGetMethod4, str), ReflectionUtil.tryGetMethod(this.mRequestBuilderClazz, "build", new Class[0]), new Object[0]);
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
            throw new IllegalStateException("OkHttpSocketClient is already initialized.");
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
    /* JADX WARNING: Can't wrap try/catch for region: R(2:8|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x009a, code lost:
        r10.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x009e, code lost:
        r10.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0013, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0016, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
        r0 = com.taobao.weex.devtools.common.ReflectionUtil.getFieldValue(com.taobao.weex.devtools.common.ReflectionUtil.tryGetDeclaredField(r9.mWebSocketClazz, "TEXT"), (java.lang.Object) null);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0019 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendProtocolMessage(int r10, java.lang.String r11) {
        /*
            r9 = this;
            java.lang.Object r10 = r9.mWebSocket
            if (r10 != 0) goto L_0x0005
            return
        L_0x0005:
            r10 = 0
            java.lang.Class r0 = r9.mMediaTypeClazz     // Catch:{ Exception -> 0x0019 }
            java.lang.String r1 = "TEXT"
            java.lang.reflect.Field r0 = com.taobao.weex.devtools.common.ReflectionUtil.tryGetDeclaredField(r0, r1)     // Catch:{ Exception -> 0x0019 }
            java.lang.Object r0 = com.taobao.weex.devtools.common.ReflectionUtil.getFieldValue(r0, r10)     // Catch:{ Exception -> 0x0019 }
            goto L_0x0025
        L_0x0013:
            r10 = move-exception
            goto L_0x009a
        L_0x0016:
            r10 = move-exception
            goto L_0x009e
        L_0x0019:
            java.lang.Class r0 = r9.mWebSocketClazz     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.String r1 = "TEXT"
            java.lang.reflect.Field r0 = com.taobao.weex.devtools.common.ReflectionUtil.tryGetDeclaredField(r0, r1)     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Object r0 = com.taobao.weex.devtools.common.ReflectionUtil.getFieldValue(r0, r10)     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
        L_0x0025:
            java.lang.Class r10 = r9.mWebSocketClazz     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.String r1 = "sendMessage"
            r2 = 2
            java.lang.Class[] r3 = new java.lang.Class[r2]     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Class r4 = r9.mMediaTypeClazz     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            r5 = 0
            r3[r5] = r4     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Class r4 = r9.mBufferClazz     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            r6 = 1
            r3[r6] = r4     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.reflect.Method r10 = com.taobao.weex.devtools.common.ReflectionUtil.tryGetMethod(r10, r1, r3)     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            if (r10 == 0) goto L_0x0064
            java.lang.Class r1 = r9.mBufferClazz     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Object r1 = r1.newInstance()     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Class r3 = r9.mBufferClazz     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.String r4 = "writeUtf8"
            java.lang.Class[] r7 = new java.lang.Class[r6]     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Class<java.lang.String> r8 = java.lang.String.class
            r7[r5] = r8     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.reflect.Method r3 = com.taobao.weex.devtools.common.ReflectionUtil.tryGetMethod(r3, r4, r7)     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Object r4 = r9.mWebSocket     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            r2[r5] = r0     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Object[] r0 = new java.lang.Object[r6]     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            r0[r5] = r11     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Object r11 = com.taobao.weex.devtools.common.ReflectionUtil.tryInvokeMethod(r1, r3, r0)     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            r2[r6] = r11     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            com.taobao.weex.devtools.common.ReflectionUtil.tryInvokeMethod(r4, r10, r2)     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            goto L_0x00a1
        L_0x0064:
            java.lang.Class r10 = r9.mWebSocketClazz     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.String r1 = "sendMessage"
            java.lang.Class[] r3 = new java.lang.Class[r6]     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Class r4 = r9.mRequestBodyClazz     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            r3[r5] = r4     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.reflect.Method r10 = com.taobao.weex.devtools.common.ReflectionUtil.tryGetMethod(r10, r1, r3)     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Class r1 = r9.mRequestBodyClazz     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.String r3 = "create"
            java.lang.Class[] r4 = new java.lang.Class[r2]     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Class r7 = r9.mMediaTypeNewClazz     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            r4[r5] = r7     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Class<java.lang.String> r7 = java.lang.String.class
            r4[r6] = r7     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.reflect.Method r1 = com.taobao.weex.devtools.common.ReflectionUtil.tryGetMethod(r1, r3, r4)     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Class r3 = r9.mRequestBodyClazz     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            r2[r5] = r0     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            r2[r6] = r11     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Object r11 = com.taobao.weex.devtools.common.ReflectionUtil.tryInvokeMethod(r3, r1, r2)     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Object r0 = r9.mWebSocket     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            java.lang.Object[] r1 = new java.lang.Object[r6]     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            r1[r5] = r11     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            com.taobao.weex.devtools.common.ReflectionUtil.tryInvokeMethod(r0, r10, r1)     // Catch:{ IllegalAccessException -> 0x0016, InstantiationException -> 0x0013 }
            goto L_0x00a1
        L_0x009a:
            r10.printStackTrace()
            goto L_0x00a1
        L_0x009e:
            r10.printStackTrace()
        L_0x00a1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.devtools.debug.OkHttpSocketClient.sendProtocolMessage(int, java.lang.String):void");
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
            Object obj2;
            boolean z;
            if ("onOpen".equals(method.getName())) {
                OkHttpSocketClient.this.mWebSocket = OkHttpSocketClient.this.mWebSocketClazz.cast(objArr[0]);
                if (OkHttpSocketClient.this.mConnectCallback != null) {
                    OkHttpSocketClient.this.mConnectCallback.onSuccess((String) null);
                }
            } else if ("onFailure".equals(method.getName())) {
                OkHttpSocketClient.this.abort("Websocket onFailure", objArr[0]);
            } else if ("onMessage".equals(method.getName())) {
                try {
                    obj2 = OkHttpSocketClient.this.mBufferedSourceClazz.cast(objArr[0]);
                    z = false;
                } catch (Throwable unused) {
                    z = true;
                    obj2 = null;
                }
                if (z) {
                    OkHttpSocketClient.this.mProxy.handleMessage(ReflectionUtil.tryInvokeMethod(OkHttpSocketClient.this.mResponseBodyClazz.cast(objArr[0]), ReflectionUtil.tryGetMethod(OkHttpSocketClient.this.mResponseBodyClazz, "string", new Class[0]), new Object[0]).toString());
                } else {
                    try {
                        OkHttpSocketClient.this.mProxy.handleMessage((String) ReflectionUtil.tryInvokeMethod(obj2, ReflectionUtil.tryGetMethod(OkHttpSocketClient.this.mBufferedSourceClazz, "readUtf8", new Class[0]), new Object[0]));
                    } catch (Exception e) {
                        if (LogRedirector.isLoggable(OkHttpSocketClient.TAG, 2)) {
                            LogRedirector.w(OkHttpSocketClient.TAG, "Unexpected I/O exception processing message: " + e);
                        }
                    } catch (Throwable th) {
                        ReflectionUtil.tryInvokeMethod(obj2, ReflectionUtil.tryGetMethod(OkHttpSocketClient.this.mBufferedSourceClazz, "close", new Class[0]), new Object[0]);
                        throw th;
                    }
                    ReflectionUtil.tryInvokeMethod(obj2, ReflectionUtil.tryGetMethod(OkHttpSocketClient.this.mBufferedSourceClazz, "close", new Class[0]), new Object[0]);
                }
            } else if (!"onPong".equals(method.getName()) && "onClose".equals(method.getName()) && OkHttpSocketClient.this.mHandlerThread != null && OkHttpSocketClient.this.mHandlerThread.isAlive()) {
                OkHttpSocketClient.this.mHandler.sendEmptyMessage(3);
            }
            return null;
        }
    }
}
