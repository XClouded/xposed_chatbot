package com.taobao.weex.devtools.inspector.network.utils;

import alimama.com.unweventparse.constants.EventConstants;
import android.util.Pair;
import androidx.annotation.Nullable;
import com.taobao.weex.devtools.inspector.network.NetworkEventReporter;
import com.taobao.weex.devtools.inspector.network.Timing;
import com.taobao.weex.ui.module.WXModalUIModule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseConverter {
    public static NetworkEventReporter.InspectorResponse convertFrom(Map<String, Object> map) {
        return new GeneralResponse(map);
    }

    private static class GeneralResponse implements NetworkEventReporter.TimingInspectorResponse {
        private Map<String, Object> data;
        List<Pair<String, String>> headers = new ArrayList(0);

        public GeneralResponse(Map<String, Object> map) {
            this.data = map;
            this.headers = (List) ExtractUtil.getValue(map, EventConstants.Mtop.HEADERS, this.headers);
        }

        public String url() {
            return (String) ExtractUtil.getValue(this.data, "url", "unknown");
        }

        public boolean connectionReused() {
            return ((Boolean) ExtractUtil.getValue(this.data, "connectionReused", false)).booleanValue();
        }

        public int connectionId() {
            return ((Integer) ExtractUtil.getValue(this.data, "connectionId", 0)).intValue();
        }

        public boolean fromDiskCache() {
            return ((Boolean) ExtractUtil.getValue(this.data, "fromDiskCache", false)).booleanValue();
        }

        public String requestId() {
            return (String) ExtractUtil.getValue(this.data, "requestId", "-1");
        }

        public int statusCode() {
            return ((Integer) ExtractUtil.getValue(this.data, "statusCode", 200)).intValue();
        }

        public String reasonPhrase() {
            return (String) ExtractUtil.getValue(this.data, "reasonPhrase", WXModalUIModule.OK);
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

        @Nullable
        public Timing resourceTiming() {
            Map map = (Map) ExtractUtil.getValue(this.data, "timing", new HashMap());
            if (map.isEmpty()) {
                return null;
            }
            Timing timing = new Timing();
            timing.requestTime = optValue(map, "requestTime");
            if (timing.requestTime == 0.0d) {
                return null;
            }
            timing.proxyStart = optValue(map, "sendBeforeTime") + optValue(map, "waitingTime");
            timing.proxyEnd = timing.proxyStart + 0.0d;
            timing.dnsStart = timing.proxyEnd + 0.0d;
            timing.dnsEnd = timing.dnsStart + 0.0d;
            timing.connectStart = timing.dnsEnd + 0.0d;
            timing.sslStart = timing.connectStart + 0.0d;
            timing.sendEnd = timing.sslStart + 0.0d;
            timing.connectEnd = timing.connectStart + optValue(map, "firstDataTime");
            timing.sendStart = timing.connectEnd + 0.0d;
            timing.sendEnd = timing.sendStart + optValue(map, "sendDataTime");
            timing.receiveHeadersEnd = timing.sendEnd + optValue(map, "serverRT") + optValue(map, "recDataTime");
            return timing;
        }

        private double optValue(Map<String, Object> map, String str) {
            try {
                Object obj = map.get(str);
                if (obj != null) {
                    if (!obj.toString().isEmpty()) {
                        return Double.valueOf(map.get(str).toString()).doubleValue();
                    }
                }
                return 0.0d;
            } catch (NumberFormatException unused) {
                return 0.0d;
            }
        }
    }
}
