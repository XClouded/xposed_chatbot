package com.taobao.weex.analyzer.core.debug;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import anet.channel.entity.ConnType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.accs.common.Constants;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.core.debug.DataRepository;
import com.taobao.weex.analyzer.core.debug.IDataReporter;
import com.taobao.weex.analyzer.core.ws.IWebSocketBridge;
import com.taobao.weex.analyzer.core.ws.WebSocketClient;
import com.taobao.weex.analyzer.core.ws.WebSocketClientFactory;
import com.taobao.weex.analyzer.utils.DeviceUtils;
import com.taobao.weex.devtools.debug.WXDebugConstants;
import com.taobao.weex.utils.WXLogUtils;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

class DebugController implements IWebSocketBridge, WebSocketClient.Callback, DataRepository.OnReceivedDataCallback {
    private volatile boolean isWSConnected = false;
    private Context mContext;
    private DataRepository mDataRepository;
    private WebSocketClient mSocketClient;
    private ListenerWrapper mStatusCallback;
    private String mWebsocketUrl;

    interface OnConnectionChangedCallback {
        void onConnectionChanged(ConnectionInfo connectionInfo);
    }

    public static class WSMessage {
        public String action;
        public List<String> switchers;
        public long timeout;
        public String type;
    }

    private DebugController(@NonNull Context context, String str) {
        this.mContext = context;
        this.mDataRepository = DataRepository.newInstance(context, str);
        this.mDataRepository.setOnReceivedDataCallback(this);
        this.mDataRepository.prepare(context);
    }

    static DebugController newInstance(Context context, String str) {
        return new DebugController(context, str);
    }

    /* access modifiers changed from: package-private */
    public void setConnectionChangedCallback(@NonNull OnConnectionChangedCallback onConnectionChangedCallback) {
        this.mStatusCallback = new ListenerWrapper(onConnectionChangedCallback);
    }

    /* access modifiers changed from: package-private */
    public boolean isWSConnected() {
        return this.isWSConnected;
    }

    /* access modifiers changed from: package-private */
    public void closeWSConnection() {
        if (this.mSocketClient != null) {
            this.mSocketClient.close(-11000, "close by client");
        }
        if (this.mDataRepository != null) {
            this.mDataRepository.destroy();
        }
    }

    /* access modifiers changed from: package-private */
    public void sendRequest(@NonNull JSONObject jSONObject) {
        if (this.isWSConnected && this.mSocketClient != null) {
            this.mSocketClient.sendText(jSONObject.toJSONString());
        }
    }

    /* access modifiers changed from: package-private */
    public boolean sendConnectionAlreadyEstablishedMsg() {
        if (this.mStatusCallback == null || !this.isWSConnected) {
            return false;
        }
        this.mStatusCallback.onConnectionChanged(new ConnectionInfo("连接已成功", "", 2), 200);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void connectTo(@NonNull String str) {
        this.mWebsocketUrl = str;
        if (this.mSocketClient != null) {
            this.mSocketClient.close(-1, (String) null);
        }
        this.mSocketClient = WebSocketClientFactory.create(this);
        if (this.mSocketClient != null) {
            if (this.mStatusCallback != null) {
                this.mStatusCallback.onConnectionChanged(new ConnectionInfo("正在连接中", "请耐心等待", 1), 50);
            }
            HashMap hashMap = new HashMap();
            hashMap.put("x-access-token", "cj0jdlwmy0000c3vid2c8lq85%");
            this.mSocketClient.connectWithHeaders(str, hashMap, this);
        } else if (this.mStatusCallback != null) {
            this.mStatusCallback.onConnectionChanged(new ConnectionInfo("服务建立失败", "websocket实例创建失败.请联系@楚奕", 4), 50);
        }
    }

    /* access modifiers changed from: package-private */
    public String getWebsocketUrl() {
        return this.mWebsocketUrl;
    }

    public void handleMessage(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject parseObject = JSON.parseObject(str);
                String str2 = (String) parseObject.get("action");
                if ("OpenUrl".equalsIgnoreCase(str2)) {
                    String string = parseObject.getString("url");
                    if (!TextUtils.isEmpty(string)) {
                        try {
                            RouteFactory.create().openURL(this.mContext, string);
                        } catch (Throwable th) {
                            WXLogUtils.e("weex-analyzer", th.getMessage());
                        }
                    }
                } else if ("WeexDebugProxyUrl".equalsIgnoreCase(str2)) {
                    String string2 = parseObject.getString("weexDebugServerUrl");
                    if (!TextUtils.isEmpty(string2)) {
                        DebugTool.startRemoteDebug(string2);
                    }
                } else if ("GetDeviceReport".equalsIgnoreCase(str2)) {
                    registerDevice();
                } else {
                    WSMessage wSMessage = (WSMessage) JSON.parseObject(str, WSMessage.class);
                    if (wSMessage != null && "switcher".equals(wSMessage.type) && this.mDataRepository != null) {
                        List<String> list = wSMessage.switchers;
                        String str3 = wSMessage.action;
                        for (String updateConfig : list) {
                            updateConfig(updateConfig, ConnType.PK_OPEN.equals(str3), wSMessage);
                        }
                    }
                }
            } catch (Exception e) {
                WXLogUtils.e("weex-analyzer", e.getMessage());
            }
        }
    }

    private void updateConfig(String str, boolean z, WSMessage wSMessage) {
        WXLogUtils.d("weex-analyzer", "config>>>>type:" + str + ",status:" + z);
        if (Config.TYPE_MEMORY.equals(str)) {
            this.mDataRepository.setMemoryEnabled(z);
        } else if (Config.TYPE_CPU.equals(str)) {
            this.mDataRepository.setCPUEnabled(z);
        } else if (Config.TYPE_FPS.equals(str)) {
            this.mDataRepository.setFPSEnabled(z);
        } else if (Config.TYPE_TRAFFIC.equals(str)) {
            this.mDataRepository.setTrafficEnabled(z);
        } else if (Config.TYPE_WEEX_PERFORMANCE_STATISTICS.equals(str)) {
            this.mDataRepository.setPerformanceEnabled(z);
        } else if (Config.TYPE_WEEX_PERFORMANCE_STATISTICS_V2.equals(str)) {
            this.mDataRepository.setPerformanceV2Enabled(z);
        } else if (Config.TYPE_RENDER_ANALYSIS.equals(str)) {
            this.mDataRepository.setRenderAnalysisEnabled(z, wSMessage.timeout);
        } else if ("highlight_view".equals(str)) {
            this.mDataRepository.setHighlightViewEnabled(z);
        } else if (Config.TYPE_MTOP_INSPECTOR.equals(str)) {
            this.mDataRepository.setNetworkInspectorEnabled(z);
        } else if (Config.TYPE_JS_EXCEPTION.equals(str)) {
            this.mDataRepository.setJSExceptionEnabled(z);
        } else if (Config.TYPE_WINDMILL_PERFORMANCE_STATISTICS.equals(str)) {
            this.mDataRepository.setWindmillPerformanceEnabled(z);
        } else if (Config.TYPE_NATIVE_MEMORY.equals(str)) {
            this.mDataRepository.setNativeMemoryEnabled(z);
        } else if (Config.TYPE_TOTAL_MEMORY.equals(str)) {
            this.mDataRepository.setTotalMemoryEnabled(z);
        }
    }

    public void onOpen(String str) {
        this.isWSConnected = true;
        if (this.mStatusCallback != null) {
            this.mStatusCallback.onConnectionChanged(new ConnectionInfo("连接已成功", "http://mds.alibaba-inc.com/ladder", 2), 200);
        }
    }

    public void onFailure(Throwable th) {
        this.isWSConnected = false;
        if (this.mStatusCallback != null) {
            if (th == null) {
                this.mStatusCallback.onConnectionChanged(new ConnectionInfo("服务建立失败", "服务端可能发生故障,请联系@肖焉", 4), 200);
            } else if (th.getClass().getSimpleName().equals(ProtocolException.class.getSimpleName())) {
                this.mStatusCallback.onConnectionChanged(new ConnectionInfo("服务建立失败", "服务端可能发生故障,请联系@肖焉", 4), 200);
            } else if (th.getClass().getSimpleName().equals(SocketException.class.getSimpleName()) || th.getClass().getSimpleName().equals(ConnectException.class.getSimpleName())) {
                this.mStatusCallback.onConnectionChanged(new ConnectionInfo("服务断开连接", "请检查网络情况", 3), 200);
            } else if (th.getClass().getSimpleName().equals(UnknownHostException.class.getSimpleName())) {
                this.mStatusCallback.onConnectionChanged(new ConnectionInfo("服务建立失败", "请检查网络情况", 4), 200);
            } else if (th.getClass().getSimpleName().equals(IOException.class.getSimpleName())) {
                this.mStatusCallback.onConnectionChanged(new ConnectionInfo("服务断开连接", "", 3), 200);
            } else {
                this.mStatusCallback.onConnectionChanged(new ConnectionInfo("服务断开连接", "", 3), 200);
            }
        }
    }

    public void onClose(int i, String str) {
        this.isWSConnected = false;
    }

    private void registerDevice() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("action", (Object) "DeviceReport");
        jSONObject.put("deviceId", (Object) DeviceUtils.getDeviceId(this.mContext));
        jSONObject.put(Constants.KEY_MODEL, (Object) DeviceUtils.getDeviceModel());
        jSONObject.put(WXDebugConstants.ENV_OS_VERSION, (Object) DeviceUtils.getOSVersion());
        jSONObject.put(Constants.KEY_OS_TYPE, (Object) DeviceUtils.getOSType());
        jSONObject.put("ip", (Object) DeviceUtils.getMyIp(this.mContext));
        jSONObject.put("appName", (Object) DeviceUtils.getAppName(this.mContext));
        jSONObject.put("appVersion", (Object) DeviceUtils.getAppVersion(this.mContext));
        jSONObject.put("appPackageName", (Object) DeviceUtils.getPackageName(this.mContext));
        if (this.mSocketClient != null && this.isWSConnected) {
            this.mSocketClient.sendText(jSONObject.toJSONString());
        }
    }

    public void onReceivedData(IDataReporter.ProcessedData processedData) {
        if (processedData != null && this.isWSConnected && this.mSocketClient != null) {
            this.mSocketClient.sendText(JSON.toJSONString(processedData));
        }
    }

    private static class ListenerWrapper implements OnConnectionChangedCallback {
        private final OnConnectionChangedCallback listener;
        Handler mUIHandler = new Handler(Looper.getMainLooper());

        ListenerWrapper(@NonNull OnConnectionChangedCallback onConnectionChangedCallback) {
            this.listener = onConnectionChangedCallback;
        }

        /* access modifiers changed from: package-private */
        public void onConnectionChanged(@NonNull final ConnectionInfo connectionInfo, int i) {
            this.mUIHandler.postDelayed(new Runnable() {
                public void run() {
                    ListenerWrapper.this.onConnectionChanged(connectionInfo);
                }
            }, (long) i);
        }

        public void onConnectionChanged(ConnectionInfo connectionInfo) {
            try {
                this.listener.onConnectionChanged(connectionInfo);
            } catch (Exception e) {
                WXLogUtils.e("weex-analyzer", e.getMessage());
            }
        }
    }

    static class ConnectionInfo {
        String desc;
        String msg;
        int state;

        ConnectionInfo(String str, String str2, int i) {
            this.msg = str;
            this.desc = str2;
            this.state = i;
        }

        public String toString() {
            return "ConnectionInfo{msg='" + this.msg + '\'' + ", desc='" + this.desc + '\'' + ", state=" + this.state + '}';
        }
    }

    static class ConnectionState {
        static final int STATE_CONNECTED = 2;
        static final int STATE_CONNECTING = 1;
        static final int STATE_CONNECT_FAILED = 4;
        static final int STATE_CONNECT_UNKNOWN = 0;
        static final int STATE_DISCONNECTED = 3;
        static final int STATE_UNCONNECTED = 5;

        ConnectionState() {
        }
    }
}
