package com.taobao.weex.devtools.inspector.network.utils;

import alimama.com.unweventparse.constants.EventConstants;
import android.util.Pair;
import androidx.annotation.Nullable;
import com.taobao.weex.devtools.inspector.network.NetworkEventReporter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestConverter {
    public static NetworkEventReporter.InspectorRequest convertFrom(Map<String, Object> map) {
        return new GeneralRequest(map);
    }

    private static class GeneralRequest implements NetworkEventReporter.InspectorRequest {
        private Map<String, Object> data;
        List<Pair<String, String>> headers = new ArrayList(0);

        public GeneralRequest(Map<String, Object> map) {
            this.data = map;
            this.headers = (List) ExtractUtil.getValue(map, EventConstants.Mtop.HEADERS, this.headers);
        }

        @Nullable
        public Integer friendlyNameExtra() {
            return (Integer) ExtractUtil.getValue(this.data, "friendlyNameExtra", null);
        }

        public String url() {
            return (String) ExtractUtil.getValue(this.data, "url", "unknown");
        }

        public String method() {
            return (String) ExtractUtil.getValue(this.data, "method", "GET");
        }

        @Nullable
        public byte[] body() throws IOException {
            Object obj = this.data.get("body");
            if (obj != null) {
                if (obj.getClass().isArray() && obj.getClass().getComponentType().getName().equals("byte")) {
                    return (byte[]) obj;
                }
                if (obj instanceof String) {
                    return ((String) obj).getBytes();
                }
            }
            return new byte[0];
        }

        public String id() {
            return (String) ExtractUtil.getValue(this.data, "requestId", "-1");
        }

        public String friendlyName() {
            return (String) ExtractUtil.getValue(this.data, "friendlyName", "None");
        }

        public int headerCount() {
            return this.headers.size();
        }

        public String headerName(int i) {
            return (String) this.headers.get(i).first;
        }

        public String headerValue(int i) {
            return (String) this.headers.get(i).second;
        }

        @Nullable
        public String firstHeaderValue(String str) {
            for (Pair next : this.headers) {
                if (next.first != null && ((String) next.first).equalsIgnoreCase(str)) {
                    return (String) next.second;
                }
            }
            return null;
        }
    }
}
