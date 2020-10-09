package com.taobao.weex.analyzer.core.ws;

import androidx.annotation.NonNull;
import com.taobao.weex.analyzer.IWebSocket;
import com.taobao.weex.analyzer.WeexDevOptions;
import com.taobao.weex.analyzer.utils.ReflectionUtil;

public class WebSocketClientFactory {
    public static WebSocketClient create(@NonNull IWebSocketBridge iWebSocketBridge) {
        IWebSocket webSocketImpl = WeexDevOptions.getWebSocketImpl();
        if (webSocketImpl != null) {
            return new CustomWebSocketClient(iWebSocketBridge, webSocketImpl);
        }
        if (ReflectionUtil.tryGetClassForName("okhttp3.ws.WebSocketListener") != null) {
            return new OkHttp3WebSocketClient(iWebSocketBridge);
        }
        if (ReflectionUtil.tryGetClassForName("com.squareup.okhttp.ws.WebSocketListener") != null) {
            return new OkHttpWebSocketClient(iWebSocketBridge);
        }
        return null;
    }
}
