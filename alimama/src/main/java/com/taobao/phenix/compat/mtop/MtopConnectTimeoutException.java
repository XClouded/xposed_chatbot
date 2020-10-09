package com.taobao.phenix.compat.mtop;

import com.taobao.phenix.loader.network.NetworkResponseException;

public class MtopConnectTimeoutException extends NetworkResponseException {
    public MtopConnectTimeoutException(int i) {
        super(0, "Connect Server Timeout", i, (Throwable) null);
    }
}
