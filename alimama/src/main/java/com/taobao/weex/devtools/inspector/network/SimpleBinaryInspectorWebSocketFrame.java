package com.taobao.weex.devtools.inspector.network;

import com.taobao.weex.devtools.inspector.network.NetworkEventReporter;
import java.io.UnsupportedEncodingException;

public class SimpleBinaryInspectorWebSocketFrame implements NetworkEventReporter.InspectorWebSocketFrame {
    private final byte[] mPayload;
    private final String mRequestId;

    public boolean mask() {
        return false;
    }

    public int opcode() {
        return 2;
    }

    public SimpleBinaryInspectorWebSocketFrame(String str, byte[] bArr) {
        this.mRequestId = str;
        this.mPayload = bArr;
    }

    public String requestId() {
        return this.mRequestId;
    }

    public String payloadData() {
        try {
            return new String(this.mPayload, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
