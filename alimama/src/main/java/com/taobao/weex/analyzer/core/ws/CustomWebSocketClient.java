package com.taobao.weex.analyzer.core.ws;

import androidx.annotation.NonNull;
import com.taobao.weex.analyzer.IWebSocket;
import java.util.Map;

public class CustomWebSocketClient extends WebSocketClient implements IWebSocket.EventListener {
    @NonNull
    private IWebSocket mCustomImpl;

    CustomWebSocketClient(IWebSocketBridge iWebSocketBridge, @NonNull IWebSocket iWebSocket) {
        super(iWebSocketBridge);
        this.mCustomImpl = iWebSocket;
    }

    /* access modifiers changed from: protected */
    public void connect(String str) {
        connectWithHeaders(str, (Map<String, String>) null);
    }

    /* access modifiers changed from: protected */
    public void connectWithHeaders(String str, Map<String, String> map) {
        this.mCustomImpl.connect(str, map, this);
    }

    /* access modifiers changed from: protected */
    public void close() {
        this.mCustomImpl.close();
    }

    /* access modifiers changed from: protected */
    public void sendProtocolMessage(int i, String str) {
        this.mCustomImpl.send(str);
    }

    public void onOpen() {
        if (this.mConnectCallback != null) {
            this.mConnectCallback.onOpen((String) null);
        }
    }

    public void onMessage(String str) {
        if (this.mProxy != null) {
            this.mProxy.handleMessage(str);
        }
    }

    public void onClose(int i, String str) {
        if (this.mConnectCallback != null) {
            this.mConnectCallback.onClose(i, str);
        }
    }

    public void onError(Throwable th) {
        if (this.mConnectCallback != null) {
            this.mConnectCallback.onFailure(th);
        }
    }
}
