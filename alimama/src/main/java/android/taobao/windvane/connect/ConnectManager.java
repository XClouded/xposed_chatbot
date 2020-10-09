package android.taobao.windvane.connect;

import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.TaoLog;

public class ConnectManager {
    private static ConnectManager mConnectManager;

    public static synchronized ConnectManager getInstance() {
        ConnectManager connectManager;
        synchronized (ConnectManager.class) {
            if (mConnectManager == null) {
                mConnectManager = new ConnectManager();
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
            WVThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    TaoLog.d("WVThreadPool", "Task has been executed");
                    try {
                        new HttpConnector().syncConnect(new HttpRequest(str), httpConnectListener);
                    } catch (Exception e) {
                        TaoLog.d("WVThreadPool", "Task exception:" + e.getMessage());
                    }
                }
            }, str2);
        }
    }

    public void connect(final HttpRequest httpRequest, final HttpConnectListener<HttpResponse> httpConnectListener) {
        if (httpRequest != null) {
            WVThreadPool.getInstance().execute(new Runnable() {
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
