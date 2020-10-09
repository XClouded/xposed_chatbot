package com.taobao.weex.devtools.inspector.jsonrpc;

import com.taobao.weex.devtools.inspector.jsonrpc.protocol.JsonRpcResponse;

public interface PendingRequestCallback {
    void onResponse(JsonRpcPeer jsonRpcPeer, JsonRpcResponse jsonRpcResponse);
}
