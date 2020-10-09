package com.taobao.weex.analyzer.core.ws;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

public abstract class WebSocketClient implements SimpleSession {
    protected static final int CLOSE_WEB_SOCKET = 3;
    protected static final int CONNECT_TO_WEB_SOCKET = 1;
    protected static final int CONNECT_TO_WEB_SOCKET_WITH_HEADER = 10086;
    protected static final int DISCONNECT_LOOPER = 4;
    private static final String KEY_MESSAGE = "web_socket_message";
    protected static final int SEND_MESSAGE = 2;
    protected Callback mConnectCallback;
    protected Handler mHandler;
    protected HandlerThread mHandlerThread;
    protected InvocationHandler mInvocationHandler;
    protected IWebSocketBridge mProxy;
    protected Object mSocketClient;
    protected Object mWebSocket;
    protected Object mWebSocketListener;

    public interface Callback {
        void onClose(int i, String str);

        void onFailure(Throwable th);

        void onOpen(String str);
    }

    /* access modifiers changed from: protected */
    public abstract void close();

    /* access modifiers changed from: protected */
    public abstract void connect(String str);

    /* access modifiers changed from: protected */
    public abstract void connectWithHeaders(String str, Map<String, String> map);

    public void sendBinary(byte[] bArr) {
    }

    /* access modifiers changed from: protected */
    public abstract void sendProtocolMessage(int i, String str);

    public WebSocketClient(IWebSocketBridge iWebSocketBridge) {
        init(iWebSocketBridge);
    }

    public void connect(String str, Callback callback) {
        this.mConnectCallback = callback;
        Message obtain = Message.obtain();
        obtain.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE, str);
        obtain.setData(bundle);
        if (this.mHandlerThread != null && this.mHandlerThread.isAlive()) {
            this.mHandler.sendMessage(obtain);
        }
    }

    public void connectWithHeaders(String str, Map<String, String> map, Callback callback) {
        this.mConnectCallback = callback;
        Message obtain = Message.obtain();
        obtain.what = CONNECT_TO_WEB_SOCKET_WITH_HEADER;
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE, str);
        for (Map.Entry next : map.entrySet()) {
            bundle.putString((String) next.getKey(), (String) next.getValue());
        }
        obtain.setData(bundle);
        if (this.mHandlerThread != null && this.mHandlerThread.isAlive()) {
            this.mHandler.sendMessage(obtain);
        }
    }

    /* access modifiers changed from: protected */
    public void init(IWebSocketBridge iWebSocketBridge) {
        this.mProxy = iWebSocketBridge;
        this.mHandlerThread = new HandlerThread("DebugServerProxy");
        this.mHandlerThread.start();
        this.mHandler = new MessageHandler(this.mHandlerThread.getLooper());
    }

    public void sendText(String str) {
        Message obtain = Message.obtain();
        obtain.what = 2;
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE, str);
        obtain.setData(bundle);
        if (this.mHandlerThread != null && this.mHandlerThread.isAlive()) {
            this.mHandler.sendMessage(obtain);
        }
    }

    public void close(int i, String str) {
        if (this.mHandlerThread != null && this.mHandlerThread.isAlive()) {
            this.mHandler.sendEmptyMessage(3);
        }
    }

    public boolean isOpen() {
        return this.mWebSocket != null;
    }

    private class MessageHandler extends Handler {
        MessageHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i != WebSocketClient.CONNECT_TO_WEB_SOCKET_WITH_HEADER) {
                switch (i) {
                    case 1:
                        WebSocketClient.this.connect(message.getData().getString(WebSocketClient.KEY_MESSAGE));
                        return;
                    case 2:
                        WebSocketClient.this.sendProtocolMessage(0, message.getData().getString(WebSocketClient.KEY_MESSAGE));
                        return;
                    case 3:
                        WebSocketClient.this.close();
                        WebSocketClient.this.mHandlerThread.quit();
                        return;
                    case 4:
                        WebSocketClient.this.close();
                        WebSocketClient.this.mHandlerThread.quit();
                        return;
                    default:
                        return;
                }
            } else {
                Bundle data = message.getData();
                HashMap hashMap = new HashMap();
                String str = null;
                for (String str2 : data.keySet()) {
                    if (WebSocketClient.KEY_MESSAGE.equals(str2)) {
                        str = data.getString(str2);
                    } else {
                        hashMap.put(str2, data.getString(str2));
                    }
                }
                WebSocketClient.this.connectWithHeaders(str, hashMap);
            }
        }
    }
}
