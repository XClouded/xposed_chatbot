package com.taobao.downloader.request;

public class ModifyParam {
    public Integer callbackCondition;
    public Boolean foreground;
    public Integer network;
    public Integer status;

    public ModifyParam(Integer num) {
        this.status = num;
    }

    public ModifyParam() {
    }
}
