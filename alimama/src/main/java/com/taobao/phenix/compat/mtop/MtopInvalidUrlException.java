package com.taobao.phenix.compat.mtop;

import com.taobao.phenix.loader.network.NetworkResponseException;

public class MtopInvalidUrlException extends NetworkResponseException {
    public MtopInvalidUrlException(int i) {
        super(0, "Invalid Url", i, (Throwable) null);
    }
}
