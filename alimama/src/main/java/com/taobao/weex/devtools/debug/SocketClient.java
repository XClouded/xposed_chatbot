package com.taobao.weex.devtools.debug;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.taobao.weex.devtools.websocket.SimpleSession;
import java.lang.reflect.InvocationHandler;

public abstract class SocketClient implements SimpleSession {
    protected static final int CLOSE_WEB_SOCKET = 3;
    protected static final int CONNECT_TO_WEB_SOCKET = 1;
    protected static final int DISCONNECT_LOOPER = 4;
    private static final String KEY_MESSAGE = "web_socket_message";
    protected static final int SEND_MESSAGE = 2;
    protected Callback mConnectCallback;
    protected Handler mHandler;
    protected HandlerThread mHandlerThread;
    protected InvocationHandler mInvocationHandler;
    protected DebugServerProxy mProxy;
    protected Object mSocketClient;
    private String mUrl;
    protected Object mWebSocket;
    protected Object mWebSocketListener;

    public interface Callback {
        void onFailure(Throwable th);

        void onSuccess(String str);
    }

    /* access modifiers changed from: protected */
    public abstract void close();

    /* access modifiers changed from: protected */
    public abstract void connect(String str);

    public void sendBinary(byte[] bArr) {
    }

    /* access modifiers changed from: protected */
    public abstract void sendProtocolMessage(int i, String str);

    public SocketClient(DebugServerProxy debugServerProxy) {
        init(debugServerProxy);
    }

    public void connect(String str, Callback callback) {
        this.mUrl = str;
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

    public String getUrl() {
        return this.mUrl;
    }

    /* access modifiers changed from: protected */
    public void init(DebugServerProxy debugServerProxy) {
        this.mUrl = debugServerProxy.mRemoteUrl;
        this.mProxy = debugServerProxy;
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

    public void post(Runnable runnable) {
        if (this.mHandler != null) {
            this.mHandler.post(runnable);
        }
    }

    class MessageHandler extends Handler {
        MessageHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    SocketClient.this.connect(message.getData().getString(SocketClient.KEY_MESSAGE));
                    return;
                case 2:
                    SocketClient.this.sendProtocolMessage(0, message.getData().getString(SocketClient.KEY_MESSAGE));
                    return;
                case 3:
                    SocketClient.this.close();
                    SocketClient.this.mHandlerThread.quit();
                    return;
                case 4:
                    SocketClient.this.close();
                    SocketClient.this.mHandlerThread.quit();
                    return;
                default:
                    return;
            }
        }
    }
}
