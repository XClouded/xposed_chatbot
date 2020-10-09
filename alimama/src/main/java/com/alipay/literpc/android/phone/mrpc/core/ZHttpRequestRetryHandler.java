package com.alipay.literpc.android.phone.mrpc.core;

import android.util.Log;
import java.io.IOException;
import java.net.SocketException;
import javax.net.ssl.SSLException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

public class ZHttpRequestRetryHandler implements HttpRequestRetryHandler {
    static final String TAG = "ZHttpRequestRetryHandler";

    public boolean retryRequest(IOException iOException, int i, HttpContext httpContext) {
        if (i >= 3) {
            return false;
        }
        if (iOException instanceof NoHttpResponseException) {
            Log.v(TAG, "exception instanceof NoHttpResponseException");
            return true;
        } else if ((!(iOException instanceof SocketException) && !(iOException instanceof SSLException)) || iOException.getMessage() == null || !iOException.getMessage().contains("Broken pipe")) {
            return false;
        } else {
            Log.v(TAG, "exception instanceof SocketException:Broken pipe");
            return true;
        }
    }
}
