package com.taobao.phenix.loader.network;

import android.text.TextUtils;
import com.alimama.unionwl.utils.CommonUtils;
import com.taobao.phenix.entity.ResponseData;
import com.taobao.phenix.loader.network.HttpLoader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Future;

public class DefaultHttpLoader implements HttpLoader {
    private int mConnectTimeout = 15000;
    private int mReadTimeout = 10000;

    public void connectTimeout(int i) {
        this.mConnectTimeout = i;
    }

    public void readTimeout(int i) {
        this.mReadTimeout = i;
    }

    public Future<?> load(String str, Map<String, String> map, HttpLoader.FinishCallback finishCallback) {
        try {
            if (!TextUtils.isEmpty(str) && str.startsWith("//")) {
                str = CommonUtils.HTTP_PRE + str;
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setConnectTimeout(this.mConnectTimeout);
            httpURLConnection.setReadTimeout(this.mReadTimeout);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                finishCallback.onFinished(new ResponseData(httpURLConnection.getInputStream(), httpURLConnection.getContentLength()));
                return null;
            }
            finishCallback.onError(new HttpCodeResponseException(responseCode));
            return null;
        } catch (Exception e) {
            finishCallback.onError(e);
            return null;
        }
    }
}
