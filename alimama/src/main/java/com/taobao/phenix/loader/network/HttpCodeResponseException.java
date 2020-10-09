package com.taobao.phenix.loader.network;

public class HttpCodeResponseException extends NetworkResponseException {
    public HttpCodeResponseException(int i) {
        super(i, "Failed Http Code");
    }
}
