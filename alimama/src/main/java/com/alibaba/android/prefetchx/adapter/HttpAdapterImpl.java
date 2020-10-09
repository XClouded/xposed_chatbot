package com.alibaba.android.prefetchx.adapter;

import android.text.TextUtils;
import anet.channel.request.Request;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFUtil;
import com.alibaba.android.prefetchx.PrefetchX;
import com.alibaba.android.prefetchx.adapter.HttpAdapter;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.WXResponse;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpAdapterImpl implements HttpAdapter {
    public void sendRequest(final PFRequest pFRequest, final HttpAdapter.HttpListener httpListener) {
        if (pFRequest != null) {
            if (PrefetchX.getInstance().hasWeex()) {
                IWXHttpAdapter iWXHttpAdapter = WXSDKManager.getInstance().getIWXHttpAdapter();
                if (iWXHttpAdapter != null) {
                    iWXHttpAdapter.sendRequest(pFRequest.toWXRequest(), new IWXHttpAdapter.OnHttpListener() {
                        public void onHttpStart() {
                            if (httpListener != null) {
                                httpListener.onHttpStart();
                            }
                        }

                        public void onHeadersReceived(int i, Map<String, List<String>> map) {
                            if (httpListener != null) {
                                httpListener.onHeadersReceived(i, map);
                            }
                        }

                        public void onHttpUploadProgress(int i) {
                            if (httpListener != null) {
                                httpListener.onHttpUploadProgress(i);
                            }
                        }

                        public void onHttpResponseProgress(int i) {
                            if (httpListener != null) {
                                httpListener.onHttpResponseProgress(i);
                            }
                        }

                        public void onHttpFinish(WXResponse wXResponse) {
                            if (httpListener != null) {
                                PFResponse pFResponse = new PFResponse(wXResponse);
                                try {
                                    if (pFResponse.originalData != null) {
                                        pFResponse.data = new String(pFResponse.originalData, "utf-8");
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                httpListener.onHttpFinish(pFResponse);
                            }
                        }
                    });
                    return;
                }
                return;
            }
            if (httpListener != null) {
                httpListener.onHttpStart();
            }
            if (PFUtil.isUiThread()) {
                PrefetchX.getInstance().getThreadExecutor().executeImmediately(new Runnable() {
                    public void run() {
                        HttpAdapterImpl.this.fireRequest(pFRequest, httpListener);
                    }
                });
            } else {
                fireRequest(pFRequest, httpListener);
            }
        }
    }

    /* access modifiers changed from: private */
    public void fireRequest(PFRequest pFRequest, HttpAdapter.HttpListener httpListener) {
        PFResponse pFResponse = new PFResponse();
        try {
            HttpURLConnection openConnection = openConnection(pFRequest, httpListener);
            PFLog.d("PrefetchX", "open connection of ", pFRequest.url);
            Map headerFields = openConnection.getHeaderFields();
            int responseCode = openConnection.getResponseCode();
            if (httpListener != null) {
                httpListener.onHeadersReceived(responseCode, headerFields);
            }
            pFResponse.statusCode = String.valueOf(responseCode);
            if (responseCode < 200 || responseCode > 299) {
                pFResponse.errorMsg = readInputStream(openConnection.getErrorStream(), httpListener);
            } else {
                pFResponse.originalData = readInputStreamAsBytes(openConnection.getInputStream(), httpListener);
            }
            if (httpListener != null) {
                httpListener.onHttpFinish(pFResponse);
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            pFResponse.statusCode = "-1";
            pFResponse.errorCode = "-1";
            pFResponse.errorMsg = e.getMessage();
            if (httpListener != null) {
                httpListener.onHttpFinish(pFResponse);
            }
            if (e instanceof IOException) {
                PFLog.w("PrefetchX", "error in send http request", new Throwable[0]);
            }
        }
    }

    public void sendRequest(String str, HttpAdapter.HttpListener httpListener) {
        PFRequest pFRequest = new PFRequest();
        pFRequest.url = str;
        sendRequest(pFRequest, httpListener);
    }

    private HttpURLConnection openConnection(PFRequest pFRequest, HttpAdapter.HttpListener httpListener) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(pFRequest.url).openConnection();
        httpURLConnection.setConnectTimeout(pFRequest.timeoutMs);
        httpURLConnection.setReadTimeout(pFRequest.timeoutMs);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoInput(true);
        if (pFRequest.paramMap != null) {
            for (String next : pFRequest.paramMap.keySet()) {
                httpURLConnection.addRequestProperty(next, pFRequest.paramMap.get(next));
            }
        }
        if ("POST".equals(pFRequest.method) || Request.Method.PUT.equals(pFRequest.method) || "PATCH".equals(pFRequest.method)) {
            httpURLConnection.setRequestMethod(pFRequest.method);
            if (pFRequest.body != null) {
                if (httpListener != null) {
                    httpListener.onHttpUploadProgress(0);
                }
                httpURLConnection.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.write(pFRequest.body.getBytes());
                dataOutputStream.close();
                if (httpListener != null) {
                    httpListener.onHttpUploadProgress(100);
                }
            }
        } else if (!TextUtils.isEmpty(pFRequest.method)) {
            httpURLConnection.setRequestMethod(pFRequest.method);
        } else {
            httpURLConnection.setRequestMethod("GET");
        }
        return httpURLConnection;
    }

    private byte[] readInputStreamAsBytes(InputStream inputStream, HttpAdapter.HttpListener httpListener) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[2048];
        int i = 0;
        while (true) {
            int read = inputStream.read(bArr, 0, bArr.length);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
                i += read;
                if (httpListener != null) {
                    httpListener.onHttpResponseProgress(i);
                }
            } else {
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    private String readInputStream(InputStream inputStream, HttpAdapter.HttpListener httpListener) throws IOException {
        if (inputStream == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] cArr = new char[2048];
        while (true) {
            int read = bufferedReader.read(cArr);
            if (read != -1) {
                sb.append(cArr, 0, read);
                if (httpListener != null) {
                    httpListener.onHttpResponseProgress(sb.length());
                }
            } else {
                bufferedReader.close();
                return sb.toString();
            }
        }
    }
}
