package com.alibaba.aliweex.interceptor;

import java.util.List;
import java.util.Map;

public interface IWeexAnalyzerInspector {
    boolean isEnabled();

    void onRequest(String str, InspectorRequest inspectorRequest);

    void onResponse(String str, InspectorResponse inspectorResponse);

    public static class InspectorRequest {
        public String api;
        public Map<String, String> headers;
        public String method;

        public InspectorRequest(String str, String str2, Map<String, String> map) {
            this.api = str;
            this.method = str2;
            this.headers = map;
        }

        public String toString() {
            return "InspectorRequest{api='" + this.api + '\'' + ", method='" + this.method + '\'' + ", headers=" + this.headers + '}';
        }
    }

    public static class InspectorResponse {
        public String api;
        public String data;
        public Map<String, List<String>> headers;
        public int statusCode;

        public InspectorResponse(String str, String str2, int i, Map<String, List<String>> map) {
            this.api = str;
            this.data = str2;
            this.statusCode = i;
            this.headers = map;
        }

        public String toString() {
            return "InspectorResponse{data='" + this.data + '\'' + ", statusCode=" + this.statusCode + ", headers=" + this.headers + ", api='" + this.api + '\'' + '}';
        }
    }
}
