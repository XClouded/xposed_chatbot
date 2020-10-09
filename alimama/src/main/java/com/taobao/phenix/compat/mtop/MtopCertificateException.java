package com.taobao.phenix.compat.mtop;

import com.taobao.phenix.loader.network.NetworkResponseException;

public class MtopCertificateException extends NetworkResponseException {
    public MtopCertificateException(int i) {
        super(0, "SSL Certificate Failed", i, (Throwable) null);
    }
}
