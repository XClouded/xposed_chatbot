package com.taobao.phenix.compat.mtop;

import com.taobao.phenix.loader.network.NetworkResponseException;

public class MtopIndifferentException extends NetworkResponseException {
    public MtopIndifferentException(int i, String str) {
        super(0, str, i, (Throwable) null);
    }
}
