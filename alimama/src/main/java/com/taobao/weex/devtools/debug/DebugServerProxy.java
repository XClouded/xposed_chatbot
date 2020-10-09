package com.taobao.weex.devtools.debug;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.taobao.accs.common.Constants;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.bridge.WXDebugJsBridge;
import com.taobao.weex.common.IWXBridge;
import com.taobao.weex.common.IWXDebugConfig;
import com.taobao.weex.devtools.WeexInspector;
import com.taobao.weex.devtools.common.LogRedirector;
import com.taobao.weex.devtools.common.Util;
import com.taobao.weex.devtools.debug.SocketClient;
import com.taobao.weex.devtools.inspector.MessageHandlingException;
import com.taobao.weex.devtools.inspector.MethodDispatcher;
import com.taobao.weex.devtools.inspector.MismatchedResponseException;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcException;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcPeer;
import com.taobao.weex.devtools.inspector.jsonrpc.PendingRequest;
import com.taobao.weex.devtools.inspector.jsonrpc.protocol.JsonRpcError;
import com.taobao.weex.devtools.inspector.jsonrpc.protocol.JsonRpcRequest;
import com.taobao.weex.devtools.inspector.jsonrpc.protocol.JsonRpcResponse;
import com.taobao.weex.devtools.inspector.protocol.ChromeDevtoolsDomain;
import com.taobao.weex.devtools.json.ObjectMapper;
import com.taobao.weex.utils.WXLogUtils;
import java.io.IOException;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class DebugServerProxy {
    private static final String DEVTOOL_VERSION = "0.24.2.11";
    private static final String TAG = "DebugServerProxy";
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public Iterable<ChromeDevtoolsDomain> mDomainModules;
    /* access modifiers changed from: private */
    public MethodDispatcher mMethodDispatcher;
    /* access modifiers changed from: private */
    public ObjectMapper mObjectMapper = new ObjectMapper();
    private JsonRpcPeer mPeer;
    public String mRemoteUrl = WXEnvironment.sRemoteDebugProxyUrl;
    /* access modifiers changed from: private */
    public WXDebugBridge mWXBridge;
    private WXDebugJsBridge mWXDebugJsBridge;
    /* access modifiers changed from: private */
    public WXBridgeManager mWXJsManager;
    /* access modifiers changed from: private */
    public SocketClient mWebSocketClient;

    public DebugServerProxy(Context context, IWXDebugConfig iWXDebugConfig) {
        if (context != null) {
            this.mContext = context;
            this.mWebSocketClient = SocketClientFactory.create(this);
            this.mPeer = new JsonRpcPeer(this.mObjectMapper, this.mWebSocketClient);
            if (iWXDebugConfig != null) {
                if (iWXDebugConfig.getWXJSManager() != null) {
                    this.mWXJsManager = iWXDebugConfig.getWXJSManager();
                }
                if (iWXDebugConfig.getWXDebugJsBridge() != null) {
                    this.mWXDebugJsBridge = iWXDebugConfig.getWXDebugJsBridge();
                    return;
                }
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Context of DebugServerProxy should not be null");
    }

    /* renamed from: com.taobao.weex.devtools.debug.DebugServerProxy$2  reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$taobao$weex$devtools$inspector$jsonrpc$protocol$JsonRpcError$ErrorCode = new int[JsonRpcError.ErrorCode.values().length];

        static {
            try {
                $SwitchMap$com$taobao$weex$devtools$inspector$jsonrpc$protocol$JsonRpcError$ErrorCode[JsonRpcError.ErrorCode.METHOD_NOT_FOUND.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private static void logDispatchException(JsonRpcException jsonRpcException) {
        JsonRpcError errorMessage = jsonRpcException.getErrorMessage();
        if (AnonymousClass2.$SwitchMap$com$taobao$weex$devtools$inspector$jsonrpc$protocol$JsonRpcError$ErrorCode[errorMessage.code.ordinal()] != 1) {
            LogRedirector.w(TAG, "Error processing remote message", jsonRpcException);
            return;
        }
        LogRedirector.d(TAG, "Method not implemented: " + errorMessage.message);
    }

    /* access modifiers changed from: private */
    public String getDeviceId(Context context) {
        String str = Build.VERSION.SDK_INT > 8 ? Build.SERIAL : null;
        return TextUtils.isEmpty(str) ? Settings.Secure.getString(context.getContentResolver(), "android_id") : str;
    }

    public void start() {
        synchronized (DebugServerProxy.class) {
            if (this.mContext == null) {
                new IllegalArgumentException("Context is null").printStackTrace();
                return;
            }
            WXEnvironment.sDebugServerConnectable = true;
            WeexInspector.initializeWithDefaults(this.mContext);
            this.mWXBridge = WXDebugBridge.getInstance();
            this.mWXBridge.setSession(this.mWebSocketClient);
            this.mWXBridge.setWXDebugJsBridge(this.mWXDebugJsBridge);
            this.mWebSocketClient.connect(this.mRemoteUrl, new SocketClient.Callback() {
                private String getShakeHandsMessage() {
                    HashMap hashMap = new HashMap();
                    hashMap.put("name", WXEnvironment.getApplication().getPackageName() + " : " + Process.myPid());
                    hashMap.put(Constants.KEY_MODEL, WXEnvironment.SYS_MODEL);
                    hashMap.put("weexVersion", WXEnvironment.WXSDK_VERSION);
                    hashMap.put("devtoolVersion", "0.24.2.11");
                    hashMap.put("platform", "android");
                    hashMap.put("deviceId", DebugServerProxy.this.getDeviceId(DebugServerProxy.this.mContext));
                    hashMap.put("network", Boolean.valueOf(WXEnvironment.sDebugNetworkEventReporterEnable));
                    if (WXEnvironment.sLogLevel != null) {
                        hashMap.put("logLevel", WXEnvironment.sLogLevel.getName());
                    }
                    hashMap.put("remoteDebug", Boolean.valueOf(WXEnvironment.sRemoteDebugMode));
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("id", "0");
                    hashMap2.put("method", "WxDebug.registerDevice");
                    hashMap2.put("params", hashMap);
                    return JSON.toJSONString(hashMap2);
                }

                public void onSuccess(String str) {
                    synchronized (DebugServerProxy.class) {
                        if (DebugServerProxy.this.mWebSocketClient != null && DebugServerProxy.this.mWebSocketClient.isOpen()) {
                            DebugServerProxy.this.mWebSocketClient.sendText(getShakeHandsMessage());
                            if (DebugServerProxy.this.mWXBridge != null) {
                                DebugServerProxy.this.mWXBridge.onConnected();
                            }
                            Iterable unused = DebugServerProxy.this.mDomainModules = new WeexInspector.DefaultInspectorModulesBuilder(DebugServerProxy.this.mContext).finish();
                            MethodDispatcher unused2 = DebugServerProxy.this.mMethodDispatcher = new MethodDispatcher(DebugServerProxy.this.mObjectMapper, DebugServerProxy.this.mDomainModules);
                            WXSDKManager.getInstance().postOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(WXEnvironment.sApplication, "debug server connected", 0).show();
                                }
                            }, 0);
                            Log.d(DebugServerProxy.TAG, "connect debugger server success!");
                            if (DebugServerProxy.this.mWXJsManager != null) {
                                DebugServerProxy.this.mWXJsManager.initScriptsFramework((String) null);
                            }
                        }
                    }
                }

                public void onFailure(Throwable th) {
                    synchronized (DebugServerProxy.class) {
                        if (DebugServerProxy.this.mWXBridge != null) {
                            DebugServerProxy.this.mWXBridge.onDisConnected();
                        }
                        Log.w(DebugServerProxy.TAG, "connect debugger server failure!!");
                        th.printStackTrace();
                    }
                }
            });
        }
    }

    public void stop(boolean z) {
        synchronized (DebugServerProxy.class) {
            if (this.mWebSocketClient != null) {
                this.mWebSocketClient.close(0, (String) null);
                this.mWebSocketClient = null;
            }
            this.mWXBridge = null;
            if (z) {
                switchLocalRuntime();
            }
        }
    }

    private void switchLocalRuntime() {
        WXSDKEngine.reload(WXEnvironment.getApplication(), false);
        WXEnvironment.getApplication().sendBroadcast(new Intent().setAction(WXSDKInstance.ACTION_DEBUG_INSTANCE_REFRESH).putExtra("params", ""));
    }

    public IWXBridge getWXBridge() {
        if (this.mWXBridge == null) {
            WXLogUtils.e(TAG, "WXDebugBridge is null!");
        }
        return this.mWXBridge;
    }

    public void handleMessage(String str) throws IOException {
        try {
            Util.throwIfNull(this.mPeer);
            handleRemoteMessage(this.mPeer, str);
        } catch (Exception e) {
            if (LogRedirector.isLoggable(TAG, 2)) {
                LogRedirector.v(TAG, "Unexpected I/O exception processing message: " + e);
            }
        }
    }

    private void handleRemoteMessage(JsonRpcPeer jsonRpcPeer, String str) throws IOException, MessageHandlingException, JSONException {
        JSONObject jSONObject = new JSONObject(str);
        if (jSONObject.has("method")) {
            handleRemoteRequest(jsonRpcPeer, jSONObject);
        } else if (jSONObject.has("result")) {
            handleRemoteResponse(jsonRpcPeer, jSONObject);
        } else {
            throw new MessageHandlingException("Improper JSON-RPC message: " + str);
        }
    }

    private void handleRemoteRequest(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) throws MessageHandlingException {
        JSONObject jSONObject2;
        JSONObject jSONObject3;
        String str;
        JsonRpcRequest jsonRpcRequest = (JsonRpcRequest) this.mObjectMapper.convertValue(jSONObject, JsonRpcRequest.class);
        try {
            jSONObject3 = this.mMethodDispatcher.dispatch(jsonRpcPeer, jsonRpcRequest.method, jsonRpcRequest.params);
            jSONObject2 = null;
        } catch (JsonRpcException e) {
            logDispatchException(e);
            jSONObject2 = (JSONObject) this.mObjectMapper.convertValue(e.getErrorMessage(), JSONObject.class);
            jSONObject3 = null;
        }
        if (jsonRpcRequest.id != null) {
            JsonRpcResponse jsonRpcResponse = new JsonRpcResponse();
            jsonRpcResponse.id = jsonRpcRequest.id.longValue();
            jsonRpcResponse.result = jSONObject3;
            jsonRpcResponse.error = jSONObject2;
            try {
                str = ((JSONObject) this.mObjectMapper.convertValue(jsonRpcResponse, JSONObject.class)).toString();
            } catch (OutOfMemoryError e2) {
                jsonRpcResponse.result = null;
                jsonRpcResponse.error = (JSONObject) this.mObjectMapper.convertValue(e2.getMessage(), JSONObject.class);
                str = ((JSONObject) this.mObjectMapper.convertValue(jsonRpcResponse, JSONObject.class)).toString();
            }
            jsonRpcPeer.getWebSocket().sendText(str);
        }
    }

    private void handleRemoteResponse(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) throws MismatchedResponseException {
        JsonRpcResponse jsonRpcResponse = (JsonRpcResponse) this.mObjectMapper.convertValue(jSONObject, JsonRpcResponse.class);
        PendingRequest andRemovePendingRequest = jsonRpcPeer.getAndRemovePendingRequest(jsonRpcResponse.id);
        if (andRemovePendingRequest == null) {
            throw new MismatchedResponseException(jsonRpcResponse.id);
        } else if (andRemovePendingRequest.callback != null) {
            andRemovePendingRequest.callback.onResponse(jsonRpcPeer, jsonRpcResponse);
        }
    }
}
