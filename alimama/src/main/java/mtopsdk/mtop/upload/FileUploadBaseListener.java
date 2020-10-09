package mtopsdk.mtop.upload;

import mtopsdk.mtop.upload.domain.UploadFileInfo;

public interface FileUploadBaseListener extends FileUploadListener {
    void onError(String str, String str2, String str3);

    void onFinish(UploadFileInfo uploadFileInfo, String str);

    void onProgress(int i);

    void onStart();
}
