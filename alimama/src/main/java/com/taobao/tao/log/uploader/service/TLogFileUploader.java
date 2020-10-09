package com.taobao.tao.log.uploader.service;

public interface TLogFileUploader {
    void executeUploadTask(TLogUploadMsg tLogUploadMsg, TLogFileUploaderCallBack tLogFileUploaderCallBack);

    String getBizCode();
}
