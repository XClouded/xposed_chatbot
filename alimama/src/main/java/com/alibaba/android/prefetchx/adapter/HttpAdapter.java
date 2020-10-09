package com.alibaba.android.prefetchx.adapter;

import java.util.List;
import java.util.Map;

public interface HttpAdapter {

    public static abstract class AbstractHttpListner implements HttpListener {
        public void onHeadersReceived(int i, Map<String, List<String>> map) {
        }

        public void onHttpFinish(PFResponse pFResponse) {
        }

        public void onHttpResponseProgress(int i) {
        }

        public void onHttpStart() {
        }

        public void onHttpUploadProgress(int i) {
        }
    }

    public interface HttpListener {
        void onHeadersReceived(int i, Map<String, List<String>> map);

        void onHttpFinish(PFResponse pFResponse);

        void onHttpResponseProgress(int i);

        void onHttpStart();

        void onHttpUploadProgress(int i);
    }

    void sendRequest(PFRequest pFRequest, HttpListener httpListener);

    void sendRequest(String str, HttpListener httpListener);
}
