package com.alipay.literpc.android.phone.mrpc.core;

import android.content.Context;

public class DefaultRpcClient extends RpcClient {
    /* access modifiers changed from: private */
    public Context mContext;

    public DefaultRpcClient(Context context) {
        this.mContext = context;
    }

    public <T> T getRpcProxy(Class<T> cls, RpcParams rpcParams) {
        return new RpcFactory(createConfig(rpcParams)).getRpcProxy(cls);
    }

    private Config createConfig(final RpcParams rpcParams) {
        return new Config() {
            public String getUrl() {
                return rpcParams.getGwUrl();
            }

            public Transport getTransport() {
                return HttpManager.getInstance(getApplicationContext());
            }

            public RpcParams getRpcParams() {
                return rpcParams;
            }

            public Context getApplicationContext() {
                return DefaultRpcClient.this.mContext.getApplicationContext();
            }

            public boolean isGzip() {
                return rpcParams.isGzip();
            }
        };
    }
}
