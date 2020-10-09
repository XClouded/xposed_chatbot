package com.taobao.weex.analyzer;

import java.util.Map;

public interface IWebSocket {

    public interface EventListener {
        void onClose(int i, String str);

        void onError(Throwable th);

        void onMessage(String str);

        void onOpen();
    }

    void close();

    void connect(String str, Map<String, String> map, EventListener eventListener);

    void send(String str);
}
