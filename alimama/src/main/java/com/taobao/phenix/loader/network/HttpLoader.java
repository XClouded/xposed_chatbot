package com.taobao.phenix.loader.network;

import com.taobao.phenix.entity.ResponseData;
import java.util.Map;
import java.util.concurrent.Future;

public interface HttpLoader {

    public interface FinishCallback {
        void onError(Exception exc);

        void onFinished(ResponseData responseData);
    }

    void connectTimeout(int i);

    Future<?> load(String str, Map<String, String> map, FinishCallback finishCallback);

    void readTimeout(int i);
}
