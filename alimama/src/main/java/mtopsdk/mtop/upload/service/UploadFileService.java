package mtopsdk.mtop.upload.service;

import mtopsdk.mtop.upload.domain.UploadFileInfo;
import mtopsdk.mtop.upload.domain.UploadResult;
import mtopsdk.mtop.upload.domain.UploadToken;
import mtopsdk.mtop.util.Result;

public interface UploadFileService {
    Result<UploadResult> fileUpload(UploadToken uploadToken, long j, int i);

    Result<UploadToken> getUploadToken(UploadFileInfo uploadFileInfo);
}
