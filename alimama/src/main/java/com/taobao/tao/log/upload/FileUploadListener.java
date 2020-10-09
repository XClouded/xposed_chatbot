package com.taobao.tao.log.upload;

public interface FileUploadListener {
    void onError(String str, String str2, String str3);

    void onSucessed(String str, String str2);
}
