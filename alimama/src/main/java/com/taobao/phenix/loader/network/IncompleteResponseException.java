package com.taobao.phenix.loader.network;

public class IncompleteResponseException extends NetworkResponseException {
    public IncompleteResponseException() {
        super(200, "Incomplete Response");
    }
}
