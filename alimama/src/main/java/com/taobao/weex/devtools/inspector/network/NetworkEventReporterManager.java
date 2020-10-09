package com.taobao.weex.devtools.inspector.network;

import android.util.Log;
import androidx.annotation.Nullable;
import com.taobao.weex.devtools.inspector.network.NetworkEventReporter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NetworkEventReporterManager {
    private static final String DEFAULT_NETWORK_EVENT_REPORTER_IMPL_CLASS = "com.taobao.weex.devtools.inspector.network.NetworkEventReporterImpl";
    private static Map<String, Object> inspectParams;
    private static NetworkEventReporter sReporter;

    public static class NetworkEventReporterAdapter implements NetworkEventReporter {
        public void dataReceived(String str, int i, int i2) {
        }

        public void dataSent(String str, int i, int i2) {
        }

        public void httpExchangeFailed(String str, String str2) {
        }

        @Nullable
        public InputStream interpretResponseStream(String str, @Nullable String str2, @Nullable String str3, @Nullable InputStream inputStream, ResponseHandler responseHandler) {
            return null;
        }

        public boolean isEnabled() {
            return false;
        }

        public String nextRequestId() {
            return null;
        }

        public void requestWillBeSent(NetworkEventReporter.InspectorRequest inspectorRequest) {
        }

        public void responseHeadersReceived(NetworkEventReporter.InspectorResponse inspectorResponse) {
        }

        public void responseReadFailed(String str, String str2) {
        }

        public void responseReadFinished(String str) {
        }

        public void webSocketClosed(String str) {
        }

        public void webSocketCreated(String str, String str2) {
        }

        public void webSocketFrameError(String str, String str2) {
        }

        public void webSocketFrameReceived(NetworkEventReporter.InspectorWebSocketFrame inspectorWebSocketFrame) {
        }

        public void webSocketFrameSent(NetworkEventReporter.InspectorWebSocketFrame inspectorWebSocketFrame) {
        }

        public void webSocketHandshakeResponseReceived(NetworkEventReporter.InspectorWebSocketResponse inspectorWebSocketResponse) {
        }

        public void webSocketWillSendHandshakeRequest(NetworkEventReporter.InspectorWebSocketRequest inspectorWebSocketRequest) {
        }
    }

    @Nullable
    public static NetworkEventReporter get() {
        if (!allowReport()) {
            return null;
        }
        if (sReporter == null) {
            synchronized (NetworkEventReporterManager.class) {
                if (sReporter == null) {
                    try {
                        sReporter = (NetworkEventReporter) Class.forName(DEFAULT_NETWORK_EVENT_REPORTER_IMPL_CLASS).getMethod("get", new Class[0]).invoke((Object) null, new Object[0]);
                    } catch (Exception e) {
                        Log.w("NetworkEventReporter", e);
                    }
                }
            }
        }
        return sReporter;
    }

    public static void putParam(String str, Object obj) {
        if (inspectParams == null) {
            inspectParams = new HashMap();
        }
        inspectParams.put(str, obj);
    }

    public static <T> T getParam(String str, T t) {
        if (inspectParams == null) {
            return t;
        }
        return inspectParams.get(str);
    }

    public static NetworkEventReporter newEmptyReporter() {
        return new NetworkEventReporterAdapter();
    }

    private static boolean allowReport() {
        try {
            return ((Boolean) Class.forName("com.taobao.weex.WXEnvironment").getMethod("isApkDebugable", new Class[0]).invoke((Object) null, new Object[0])).booleanValue();
        } catch (Exception e) {
            Log.w("NetworkEventReporter", e);
            return false;
        }
    }
}
