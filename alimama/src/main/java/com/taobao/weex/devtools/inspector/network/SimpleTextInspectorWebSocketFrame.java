package com.taobao.weex.devtools.inspector.network;

import com.taobao.weex.devtools.inspector.network.NetworkEventReporter;

public class SimpleTextInspectorWebSocketFrame implements NetworkEventReporter.InspectorWebSocketFrame {
    private final String mPayload;
    private final String mRequestId;

    public boolean mask() {
        return false;
    }

    public int opcode() {
        return 1;
    }

    public SimpleTextInspectorWebSocketFrame(String str, String str2) {
        this.mRequestId = str;
        this.mPayload = str2;
    }

    public String requestId() {
        return this.mRequestId;
    }

    public String payloadData() {
        return this.mPayload;
    }
}
