package com.taobao.tao.log.godeye.core;

import com.taobao.tao.log.upload.FileUploadListener;

public interface GodEyeReponse {
    void sendFile(String str, String str2, FileUploadListener fileUploadListener);
}
