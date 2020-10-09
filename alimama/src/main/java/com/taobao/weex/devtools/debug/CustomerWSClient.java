package com.taobao.weex.devtools.debug;

import com.taobao.weex.devtools.WeexInspector;
import com.taobao.weex.devtools.debug.IWebSocketClient;
import java.io.IOException;

public class CustomerWSClient extends SocketClient {
    private IWebSocketClient webSocketClient = WeexInspector.getCustomerWSClient();

    public CustomerWSClient(DebugServerProxy debugServerProxy) {
        super(debugServerProxy);
    }

    /* access modifiers changed from: protected */
    public void connect(String str) {
        if (this.webSocketClient != null) {
            this.webSocketClient.connect(str, new IWebSocketClient.WSListener() {
                public void onOpen() {
                    if (CustomerWSClient.this.mConnectCallback != null) {
                        CustomerWSClient.this.mConnectCallback.onSuccess((String) null);
                    }
                }

                public void onMessage(String str) {
                    try {
                        CustomerWSClient.this.mProxy.handleMessage(str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                public void onClose() {
                    if (CustomerWSClient.this.mHandlerThread != null && CustomerWSClient.this.mHandlerThread.isAlive()) {
                        CustomerWSClient.this.mHandler.sendEmptyMessage(3);
                    }
                }

                public void onFailure(Throwable th) {
                    if (CustomerWSClient.this.mConnectCallback != null) {
                        CustomerWSClient.this.mConnectCallback.onFailure(th);
                        CustomerWSClient.this.mConnectCallback = null;
                    }
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void close() {
        if (this.webSocketClient != null) {
            this.webSocketClient.close();
        }
    }

    /* access modifiers changed from: protected */
    public void sendProtocolMessage(int i, String str) {
        if (this.webSocketClient != null) {
            this.webSocketClient.sendMessage(i, str);
        }
    }

    public boolean isOpen() {
        return this.webSocketClient != null && this.webSocketClient.isOpen();
    }

    public boolean isAvailed() {
        return this.webSocketClient != null;
    }
}
