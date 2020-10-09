package com.alipay.literpc.android.phone.mrpc.core;

import android.util.Log;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class HttpCaller extends AbstractRpcCaller {
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private Config mConfig;

    private int transferCode(int i) {
        switch (i) {
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            case 7:
                return 8;
            case 8:
                return 15;
            case 9:
                return 16;
            default:
                return i;
        }
    }

    public HttpCaller(Config config, Method method, int i, String str, byte[] bArr, boolean z) {
        super(method, i, str, bArr, CONTENT_TYPE, z);
        this.mConfig = config;
    }

    public Object call() throws RpcException {
        HttpUrlRequest httpUrlRequest = new HttpUrlRequest(this.mConfig.getUrl());
        httpUrlRequest.setReqData(this.mReqData);
        httpUrlRequest.setContentType(this.mContentType);
        httpUrlRequest.setResetCookie(this.mResetCookie);
        httpUrlRequest.addTags("id", String.valueOf(this.mId));
        httpUrlRequest.addTags("operationType", this.mOperationType);
        httpUrlRequest.addTags("gzip", String.valueOf(this.mConfig.isGzip()));
        addHeader(httpUrlRequest);
        Log.i("HttpCaller", "threadid = " + Thread.currentThread().getId() + "; " + httpUrlRequest.toString());
        try {
            Response response = getTransport().execute(httpUrlRequest).get();
            if (response != null) {
                return response.getResData();
            }
            throw new RpcException((Integer) 9, "response is null");
        } catch (InterruptedException e) {
            throw new RpcException(13, "", e);
        } catch (ExecutionException e2) {
            Throwable cause = e2.getCause();
            if (cause == null || !(cause instanceof HttpException)) {
                throw new RpcException(9, "", e2);
            }
            HttpException httpException = (HttpException) cause;
            throw new RpcException(Integer.valueOf(transferCode(httpException.getCode())), httpException.getMsg());
        } catch (CancellationException e3) {
            throw new RpcException(13, "", e3);
        }
    }

    private void addHeader(HttpUrlRequest httpUrlRequest) {
        httpUrlRequest.addHeader(new BasicHeader("uuid", UUID.randomUUID().toString()));
        List<Header> headers = this.mConfig.getRpcParams().getHeaders();
        if (headers != null && !headers.isEmpty()) {
            for (Header addHeader : headers) {
                httpUrlRequest.addHeader(addHeader);
            }
        }
    }

    private Transport getTransport() throws RpcException {
        return this.mConfig.getTransport();
    }
}
