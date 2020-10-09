package com.taobao.phenix.builder;

import com.taobao.phenix.loader.network.DefaultHttpLoader;
import com.taobao.phenix.loader.network.HttpLoader;
import com.taobao.tcommon.core.Preconditions;

public class HttpLoaderBuilder implements Builder<HttpLoader> {
    public static final int DEFAULT_CONNECT_TIMEOUT = 15000;
    public static final int DEFAULT_READ_TIMEOUT = 10000;
    private Integer mConnectTimeout;
    private boolean mHaveBuilt;
    private HttpLoader mHttpLoader;
    private Integer mReadTimeout;

    public HttpLoaderBuilder with(HttpLoader httpLoader) {
        Preconditions.checkState(!this.mHaveBuilt, "HttpLoaderBuilder has been built, not allow with() now");
        this.mHttpLoader = httpLoader;
        return this;
    }

    public HttpLoaderBuilder connectTimeout(int i) {
        Preconditions.checkState(!this.mHaveBuilt, "HttpLoaderBuilder has been built, not allow connectTimeout() now");
        this.mConnectTimeout = Integer.valueOf(i);
        return this;
    }

    public HttpLoaderBuilder readTimeout(int i) {
        Preconditions.checkState(!this.mHaveBuilt, "HttpLoaderBuilder has been built, not allow readTimeout() now");
        this.mReadTimeout = Integer.valueOf(i);
        return this;
    }

    public synchronized HttpLoader build() {
        if (this.mHaveBuilt) {
            return this.mHttpLoader;
        }
        this.mHaveBuilt = true;
        if (this.mHttpLoader == null) {
            this.mHttpLoader = new DefaultHttpLoader();
        }
        this.mHttpLoader.connectTimeout(this.mConnectTimeout != null ? this.mConnectTimeout.intValue() : 15000);
        this.mHttpLoader.readTimeout(this.mReadTimeout != null ? this.mReadTimeout.intValue() : 10000);
        return this.mHttpLoader;
    }
}
