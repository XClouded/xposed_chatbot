package com.alimama.union.app.network.response;

import mtopsdk.mtop.domain.BaseOutDo;

public class BaseResponse<T> extends BaseOutDo {
    private T data;

    public void setData(T t) {
        this.data = t;
    }

    public T getData() {
        return this.data;
    }
}
