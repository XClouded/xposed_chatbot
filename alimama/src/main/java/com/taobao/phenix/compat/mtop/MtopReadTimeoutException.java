package com.taobao.phenix.compat.mtop;

import com.taobao.phenix.loader.network.NetworkResponseException;

public class MtopReadTimeoutException extends NetworkResponseException {
    public MtopReadTimeoutException() {
        super(200, "Read Stream Timeout");
    }
}
