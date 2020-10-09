package com.alibaba.aliweex.adapter.adapter;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import anetwork.channel.Response;
import anetwork.channel.entity.RequestImpl;
import com.taobao.aws.WebSocketCenter;
import com.taobao.aws.api.IWebSocket;
import com.taobao.aws.listener.WebSocketListener;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.appfram.websocket.IWebSocketAdapter;
import com.taobao.weex.utils.WXLogUtils;
import java.net.URI;

public class WXWebSocketAdapter implements IWebSocketAdapter {
    private static final String TAG = "WXWebSocketAdapter";
    private IWebSocket mCurrentSession;
    /* access modifiers changed from: private */
    public IWebSocketAdapter.EventListener mListener;

    public void connect(String str, @Nullable String str2, IWebSocketAdapter.EventListener eventListener) {
        if (eventListener == null) {
            WXLogUtils.e(TAG, "Listener is null!");
        } else if (WXEnvironment.getApplication() == null) {
            eventListener.onError("Application is null");
        } else if (TextUtils.isEmpty(str)) {
            eventListener.onError("Invalid URL:" + str);
        } else {
            this.mListener = eventListener;
            try {
                RequestImpl requestImpl = new RequestImpl(URI.create(str));
                if (!TextUtils.isEmpty(str2)) {
                    requestImpl.addHeader(IWebSocketAdapter.HEADER_SEC_WEBSOCKET_PROTOCOL, str2);
                }
                this.mCurrentSession = WebSocketCenter.getInstance().newWebSocket(WXEnvironment.getApplication(), requestImpl, new WebSocketListener() {
                    public void onClosing(IWebSocket iWebSocket, int i, String str) {
                    }

                    public void onOpen(IWebSocket iWebSocket, Response response) {
                        WXWebSocketAdapter.this.mListener.onOpen();
                    }

                    public void onMessage(IWebSocket iWebSocket, String str) {
                        WXWebSocketAdapter.this.mListener.onMessage(str);
                    }

                    public void onMessage(IWebSocket iWebSocket, byte[] bArr) {
                        WXLogUtils.w(WXWebSocketAdapter.TAG, "Binary message was not supported.");
                        WXWebSocketAdapter.this.mListener.onMessage(new String(bArr));
                    }

                    public void onClosed(IWebSocket iWebSocket, int i, String str) {
                        WXWebSocketAdapter.this.mListener.onClose(i, str, true);
                    }

                    public void onFailure(IWebSocket iWebSocket, Throwable th, Response response) {
                        WXWebSocketAdapter.this.mListener.onError(th.getMessage());
                    }
                });
            } catch (Throwable th) {
                eventListener.onError("Invalid URI:" + th.getMessage());
            }
        }
    }

    public void send(String str) {
        if (isSessionActive()) {
            this.mCurrentSession.send(str);
        }
    }

    public void close(int i, String str) {
        if (this.mCurrentSession != null) {
            this.mCurrentSession.close();
            this.mCurrentSession = null;
        }
    }

    private boolean isSessionActive() {
        if (this.mCurrentSession != null && this.mCurrentSession.getConnState() == 2) {
            return true;
        }
        if (this.mListener == null) {
            return false;
        }
        if (this.mCurrentSession != null) {
            IWebSocketAdapter.EventListener eventListener = this.mListener;
            eventListener.onError("WebSocket session not active: " + this.mCurrentSession.getConnState());
            return false;
        }
        this.mListener.onError("WebSocket session not existed");
        return false;
    }

    public void destroy() {
        close(-1, "Context destroyed");
    }

    private static class WSListener implements WebSocketListener {
        public void onClosed(IWebSocket iWebSocket, int i, String str) {
        }

        public void onClosing(IWebSocket iWebSocket, int i, String str) {
        }

        public void onFailure(IWebSocket iWebSocket, Throwable th, Response response) {
        }

        public void onMessage(IWebSocket iWebSocket, String str) {
        }

        public void onMessage(IWebSocket iWebSocket, byte[] bArr) {
        }

        public void onOpen(IWebSocket iWebSocket, Response response) {
        }

        private WSListener() {
        }
    }
}
