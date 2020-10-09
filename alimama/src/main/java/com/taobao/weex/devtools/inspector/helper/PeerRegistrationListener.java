package com.taobao.weex.devtools.inspector.helper;

import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcPeer;

public interface PeerRegistrationListener {
    void onPeerRegistered(JsonRpcPeer jsonRpcPeer);

    void onPeerUnregistered(JsonRpcPeer jsonRpcPeer);
}
