package com.alibaba.aliweex.adapter.adapter;

import com.taobao.aws.api.IWebSocket;
import com.taobao.weex.appfram.websocket.IWebSocketAdapter;
import com.taobao.weex.appfram.websocket.IWebSocketAdapterFactory;

public class WXWebSocketProvider implements IWebSocketAdapterFactory {
    private boolean isAwsAvailable() {
        Class<IWebSocket> cls = IWebSocket.class;
        return true;
    }

    public IWebSocketAdapter createWebSocketAdapter() {
        if (isAwsAvailable()) {
            return new WXWebSocketAdapter();
        }
        return null;
    }
}
