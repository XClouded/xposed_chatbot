package com.taobao.zcache.network;

import com.taobao.zcache.log.ZLog;
import com.taobao.zcache.thread.ZCacheThreadPool;

public class ConnectManager {
    private static ConnectManager mConnectManager;

    public static synchronized ConnectManager getInstance() {
        ConnectManager connectManager;
        synchronized (ConnectManager.class) {
            if (mConnectManager == null) {
                synchronized (ConnectManager.class) {
                    if (mConnectManager == null) {
                        mConnectManager = new ConnectManager();
                    }
                }
            }
            connectManager = mConnectManager;
        }
        return connectManager;
    }

    private ConnectManager() {
    }

    public void connect(String str, HttpConnectListener<HttpResponse> httpConnectListener) {
        connect(str, httpConnectListener, (String) null);
    }

    public void connect(final String str, final HttpConnectListener<HttpResponse> httpConnectListener, String str2) {
        if (str != null) {
            ZCacheThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    ZLog.d("ZCacheThreadPool", "Task has been executed");
                    try {
                        new HttpConnector().syncConnect(new HttpRequest(str), httpConnectListener);
                    } catch (Exception e) {
                        ZLog.d("ZCacheThreadPool", "Task exception:" + e.getMessage());
                    }
                }
            }, str2);
        }
    }

    public void connect(final HttpRequest httpRequest, final HttpConnectListener<HttpResponse> httpConnectListener) {
        if (httpRequest != null) {
            ZCacheThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    new HttpConnector().syncConnect(httpRequest, httpConnectListener);
                }
            });
        }
    }

    public HttpResponse connectSync(String str, HttpConnectListener<HttpResponse> httpConnectListener) {
        if (str == null) {
            return null;
        }
        try {
            return new HttpConnector().syncConnect(new HttpRequest(str), httpConnectListener);
        } catch (Exception unused) {
            return null;
        }
    }
}
