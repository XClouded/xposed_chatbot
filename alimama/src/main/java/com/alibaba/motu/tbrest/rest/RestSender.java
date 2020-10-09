package com.alibaba.motu.tbrest.rest;

import com.alibaba.motu.tbrest.SendAsyncExecutor;
import com.alibaba.motu.tbrest.data.RestData;
import com.alibaba.motu.tbrest.request.BizResponse;
import com.alibaba.motu.tbrest.request.UrlWrapper;

class RestSender {
    private static final SendAsyncExecutor sendExecutor = new SendAsyncExecutor();
    private UrlWrapperSender innerSender = new UrlWrapperSender();

    public interface Callback {
        void onFailed(RestData restData);

        void onSuccess(RestData restData);
    }

    RestSender() {
    }

    public void sendRestDataAsync(final RestData restData, final Callback callback) {
        sendExecutor.start(new Runnable() {
            public void run() {
                boolean access$000 = RestSender.this.innerSendRestData(restData.getAppKey(), restData.getUrl(), restData.getPackRequest());
                if (callback == null) {
                    return;
                }
                if (access$000) {
                    callback.onSuccess(restData);
                } else {
                    callback.onFailed(restData);
                }
            }
        });
    }

    public boolean sendRestDataSync(RestData restData) {
        return innerSendRestData(restData.getAppKey(), restData.getUrl(), restData.getPackRequest());
    }

    /* access modifiers changed from: private */
    public boolean innerSendRestData(String str, String str2, byte[] bArr) {
        return this.innerSender.innerSendRestData(str, str2, bArr);
    }

    static class UrlWrapperSender {
        UrlWrapperSender() {
        }

        /* access modifiers changed from: package-private */
        public boolean innerSendRestData(String str, String str2, byte[] bArr) {
            BizResponse bizResponse;
            if (str != null) {
                try {
                    bizResponse = UrlWrapper.sendRequest(str2, bArr);
                } catch (Throwable th) {
                    th.printStackTrace();
                    return false;
                }
            } else {
                bizResponse = UrlWrapper.sendRequest(str, str2, bArr);
            }
            return bizResponse.isSuccess();
        }
    }
}
