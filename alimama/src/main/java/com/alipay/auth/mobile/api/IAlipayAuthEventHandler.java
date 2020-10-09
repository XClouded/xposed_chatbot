package com.alipay.auth.mobile.api;

public interface IAlipayAuthEventHandler {
    void alipayAuthDidCancel();

    void alipayAuthFailure();

    void alipayAuthSuccess(String str);
}
