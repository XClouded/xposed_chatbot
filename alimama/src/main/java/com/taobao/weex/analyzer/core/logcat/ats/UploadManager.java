package com.taobao.weex.analyzer.core.logcat.ats;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.analyzer.core.logcat.LogcatDumpBuilder;
import com.taobao.weex.analyzer.core.logcat.LogcatDumper;
import com.taobao.weex.analyzer.core.ws.IWebSocketBridge;
import com.taobao.weex.analyzer.core.ws.WebSocketClient;
import com.taobao.weex.analyzer.core.ws.WebSocketClientFactory;
import com.taobao.weex.performance.WXAnalyzerDataTransfer;
import java.util.Collections;
import java.util.List;

class UploadManager implements IWebSocketBridge, WebSocketClient.Callback {
    private static final String ACTION_RECEIVE_RESULT = "ReturnOpenTimeLogUrl";
    private static final String ACTION_STOP_UPLOAD = "EndSaveOpenTimeLog";
    private static final String ACTION_UPLOAD_LOG = "SaveOpenTimeLog";
    private boolean isConnecting = false;
    /* access modifiers changed from: private */
    public boolean isUploading = false;
    /* access modifiers changed from: private */
    public CallbackProxy mCallbackProxy = new CallbackProxy();
    /* access modifiers changed from: private */
    public int mCount = 0;
    private LogcatDumper mLogcatDumper;
    private WebSocketClient mWebSocketClient;
    private String mWebsocketUrl;

    interface Callback {
        void onBeforeConnect();

        void onBeforeDisconnect();

        void onBeforeUpload();

        void onClose(int i, String str);

        void onError(String str);

        void onOpen();

        void onReceivedOSSUrl(String str);

        void onUploadLog(int i, String str);
    }

    static /* synthetic */ int access$108(UploadManager uploadManager) {
        int i = uploadManager.mCount;
        uploadManager.mCount = i + 1;
        return i;
    }

    private UploadManager() {
    }

    static UploadManager create() {
        return new UploadManager();
    }

    /* access modifiers changed from: package-private */
    public void connect(@NonNull String str, @NonNull Callback callback) {
        try {
            WXAnalyzerDataTransfer.switchInteractionLog(true);
        } catch (Throwable th) {
            Log.e("weex-analyzer", th.getMessage());
        }
        this.mWebsocketUrl = str;
        this.mCallbackProxy.setCallback(callback);
        if (this.mWebSocketClient != null) {
            this.mWebSocketClient.close(-1, (String) null);
        }
        this.mCallbackProxy.onBeforeConnect();
        this.mWebSocketClient = WebSocketClientFactory.create(this);
        if (this.mWebSocketClient != null) {
            this.mWebSocketClient.connectWithHeaders(str, Collections.emptyMap(), this);
        } else {
            this.mCallbackProxy.onError("服务建立失败 | webSocket实例创建失败 @楚奕");
        }
    }

    /* access modifiers changed from: package-private */
    public void disconnect() {
        if (this.mWebSocketClient != null) {
            this.mWebSocketClient.close(-11000, "close by client");
            this.mCallbackProxy.onBeforeDisconnect();
        }
        this.mWebsocketUrl = null;
        try {
            WXAnalyzerDataTransfer.switchInteractionLog(false);
        } catch (Throwable th) {
            Log.e("weex-analyzer", th.getMessage());
        }
    }

    public void startUpload() {
        if (this.isConnecting && !this.isUploading && this.mWebSocketClient != null) {
            this.isUploading = true;
            this.mCallbackProxy.onBeforeUpload();
            this.mLogcatDumper = new LogcatDumpBuilder().listener(new LogcatDumper.OnLogReceivedListener() {
                public void onReceived(@NonNull List<LogcatDumper.LogInfo> list) {
                    if (UploadManager.this.isUploading) {
                        for (LogcatDumper.LogInfo next : list) {
                            if (!TextUtils.isEmpty(next.message)) {
                                UploadManager.access$108(UploadManager.this);
                                UploadManager.this.mCallbackProxy.onUploadLog(UploadManager.this.mCount, next.message);
                                UploadManager.this.sendMessage(UploadManager.ACTION_UPLOAD_LOG, next.message);
                            }
                        }
                    }
                }
            }).rule(new LogcatDumper.Rule("weex日志过滤", WXAnalyzerDataTransfer.INTERACTION_TAG)).enableCache(false).cacheLimit(1000).build();
            this.mLogcatDumper.beginDump();
        }
    }

    public void stopUpload() {
        this.isUploading = false;
        if (this.mLogcatDumper != null) {
            this.mLogcatDumper.destroy();
            this.mLogcatDumper = null;
        }
        this.mCount = 0;
        sendMessage(ACTION_STOP_UPLOAD, (String) null);
    }

    public boolean isUploading() {
        return this.isUploading;
    }

    /* access modifiers changed from: package-private */
    public boolean isConnected() {
        return this.isConnecting;
    }

    /* access modifiers changed from: private */
    public void sendMessage(String str, @Nullable String str2) {
        if (this.isConnecting && this.mWebSocketClient != null) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("action", (Object) str);
            if (!TextUtils.isEmpty(str2)) {
                jSONObject.put("data", (Object) str2);
            }
            this.mWebSocketClient.sendText(JSON.toJSONString(jSONObject));
        }
    }

    public void handleMessage(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject parseObject = JSON.parseObject(str);
                String str2 = (String) parseObject.get("action");
                if (!TextUtils.isEmpty(str2) && ACTION_RECEIVE_RESULT.equals(str2)) {
                    String str3 = (String) parseObject.get("fileUrl");
                    if (!TextUtils.isEmpty(str3)) {
                        this.mCallbackProxy.onReceivedOSSUrl(str3);
                    }
                }
            } catch (Throwable th) {
                Log.e("weex-analyzer", "parse json failed." + th.getMessage());
            }
        }
    }

    public void onOpen(String str) {
        this.isConnecting = true;
        this.mCallbackProxy.onOpen();
    }

    public void onFailure(Throwable th) {
        this.isConnecting = false;
        this.isUploading = false;
        this.mCallbackProxy.onError(th.getMessage());
    }

    public void onClose(int i, String str) {
        this.isConnecting = false;
        this.isUploading = false;
        this.mCallbackProxy.onClose(i, str);
    }

    /* access modifiers changed from: package-private */
    public int getCount() {
        return this.mCount;
    }

    private static class CallbackProxy implements Callback {
        /* access modifiers changed from: private */
        public Callback mCallback;
        private Handler mHandler = new Handler(Looper.getMainLooper());

        CallbackProxy() {
        }

        /* access modifiers changed from: package-private */
        public void setCallback(Callback callback) {
            this.mCallback = callback;
            if (callback == null) {
                this.mHandler.removeCallbacksAndMessages((Object) null);
            }
        }

        public void onBeforeConnect() {
            Log.v("weex-analyzer", "[ATS Log Upload] status: before connect");
            if (this.mCallback != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        if (CallbackProxy.this.mCallback != null) {
                            CallbackProxy.this.mCallback.onBeforeConnect();
                        }
                    }
                });
            }
        }

        public void onBeforeDisconnect() {
            Log.v("weex-analyzer", "[ATS Log Upload] status: before disconnect");
            if (this.mCallback != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        if (CallbackProxy.this.mCallback != null) {
                            CallbackProxy.this.mCallback.onBeforeDisconnect();
                        }
                    }
                });
            }
        }

        public void onOpen() {
            Log.v("weex-analyzer", "[ATS Log Upload] status: websocket open");
            if (this.mCallback != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        if (CallbackProxy.this.mCallback != null) {
                            CallbackProxy.this.mCallback.onOpen();
                        }
                    }
                });
            }
        }

        public void onReceivedOSSUrl(final String str) {
            Log.v("weex-analyzer", "[ATS Log Upload] status: received oss url > " + str);
            if (this.mCallback != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        if (CallbackProxy.this.mCallback != null) {
                            CallbackProxy.this.mCallback.onReceivedOSSUrl(str);
                        }
                    }
                });
            }
        }

        public void onBeforeUpload() {
            Log.v("weex-analyzer", "[ATS Log Upload] status: before upload");
            if (this.mCallback != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        if (CallbackProxy.this.mCallback != null) {
                            CallbackProxy.this.mCallback.onBeforeUpload();
                        }
                    }
                });
            }
        }

        public void onUploadLog(final int i, final String str) {
            Log.v("weex-analyzer", "[ATS Log Upload] status: upload log");
            if (this.mCallback != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        if (CallbackProxy.this.mCallback != null) {
                            CallbackProxy.this.mCallback.onUploadLog(i, str);
                        }
                    }
                });
            }
        }

        public void onClose(final int i, final String str) {
            Log.v("weex-analyzer", "[ATS Log Upload] status: connection closed");
            if (this.mCallback != null) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        if (CallbackProxy.this.mCallback != null) {
                            CallbackProxy.this.mCallback.onClose(i, str);
                        }
                    }
                });
            }
        }

        public void onError(final String str) {
            Log.v("weex-analyzer", "[ATS Log Upload] status: connection error > " + str);
            if (this.mCallback != null && !"closed".equals(str)) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        if (CallbackProxy.this.mCallback != null) {
                            CallbackProxy.this.mCallback.onError(str);
                        }
                    }
                });
            }
        }
    }
}
