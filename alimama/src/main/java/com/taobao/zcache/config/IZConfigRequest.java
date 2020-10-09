package com.taobao.zcache.config;

public interface IZConfigRequest {

    public interface ZConfigCallback {
        void configBack(String str, int i, String str2);
    }

    void requestZConfig(ZConfigCallback zConfigCallback);
}
