package com.taobao.weex.devtools.inspector.protocol.module;

import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcPeer;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcResult;
import com.taobao.weex.devtools.inspector.protocol.ChromeDevtoolsDomain;
import com.taobao.weex.devtools.inspector.protocol.ChromeDevtoolsMethod;
import com.taobao.weex.devtools.json.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

public class HeapProfiler implements ChromeDevtoolsDomain {
    @ChromeDevtoolsMethod
    public JsonRpcResult getProfileHeaders(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        ProfileHeaderResponse profileHeaderResponse = new ProfileHeaderResponse();
        profileHeaderResponse.headers = Collections.emptyList();
        return profileHeaderResponse;
    }

    private static class ProfileHeaderResponse implements JsonRpcResult {
        @JsonProperty(required = true)
        public List<ProfileHeader> headers;

        private ProfileHeaderResponse() {
        }
    }

    private static class ProfileHeader {
        @JsonProperty(required = true)
        public String title;
        @JsonProperty(required = true)
        public int uid;

        private ProfileHeader() {
        }
    }
}
