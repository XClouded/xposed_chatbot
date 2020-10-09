package com.taobao.tao.log.uploader.service;

public interface TLogFileUploaderCallBack {
    Boolean onFileUpload(TLogUploadFileModel tLogUploadFileModel);
}
