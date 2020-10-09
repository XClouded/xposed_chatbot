package mtopsdk.mtop.upload;

import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.upload.domain.UploadConstants;
import mtopsdk.mtop.upload.domain.UploadFileInfo;
import mtopsdk.mtop.upload.domain.UploadResult;
import mtopsdk.mtop.upload.domain.UploadToken;
import mtopsdk.mtop.upload.service.UploadFileService;
import mtopsdk.mtop.util.Result;

class SegmentFileUploadTask extends FileUploadTask {
    private static final String TAG = "mtopsdk.SegmentFileUploadTask";
    private long offset;
    private UploadToken token;
    private UploadFileService uploadService;

    public SegmentFileUploadTask(UploadFileInfo uploadFileInfo, DefaultFileUploadListenerWrapper defaultFileUploadListenerWrapper, UploadToken uploadToken, long j, UploadFileService uploadFileService) {
        super(uploadFileInfo, defaultFileUploadListenerWrapper);
        this.token = uploadToken;
        this.offset = j;
        this.uploadService = uploadFileService;
    }

    public void upload() {
        if (!isCancelled()) {
            int i = 0;
            do {
                Result<UploadResult> fileUpload = this.uploadService.fileUpload(this.token, this.offset, i);
                if (!isCancelled()) {
                    boolean isSuccess = fileUpload.isSuccess();
                    if (isSuccess) {
                        UploadResult model = fileUpload.getModel();
                        notifyProgress(this.token.uploadedLength.addAndGet(Math.min(this.token.segmentSize, this.token.fileBaseInfo.fileSize - this.offset)), this.token.fileBaseInfo.fileSize);
                        if (fileUpload.getModel().isFinish) {
                            this.listener.onFinish(this.fileInfo, model.location);
                            parseServerRT(model.serverRT);
                            commitUploadStatsRecord(fileUpload, this.token);
                            FileUploadMgr.getInstance().removeTask(this.fileInfo);
                            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                                TBSdkLog.d(TAG, "[upload]entire file upload succeed.");
                                return;
                            }
                            return;
                        } else if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                            TBSdkLog.d(TAG, "[upload] segment upload succeed.offset=" + this.offset);
                            return;
                        } else {
                            return;
                        }
                    } else {
                        if (((long) i) == this.token.retryCount && this.listener.isFinished().compareAndSet(false, true)) {
                            this.listener.onError(fileUpload.getErrType(), fileUpload.getErrCode(), fileUpload.getErrInfo());
                            this.listener.cancel();
                            commitUploadStatsRecord(fileUpload, this.token);
                        }
                        this.listener.countRetryTimes();
                        if (UploadConstants.ERRCODE_TOKEN_EXPIRED.equalsIgnoreCase(fileUpload.getErrCode())) {
                            Result<UploadToken> uploadToken = this.uploadService.getUploadToken(this.fileInfo);
                            if (uploadToken.isSuccess()) {
                                this.token = uploadToken.getModel();
                            }
                        }
                        if (!isSuccess) {
                            i++;
                        } else {
                            return;
                        }
                    }
                } else {
                    return;
                }
            } while (((long) i) <= this.token.retryCount);
        }
    }

    private void parseServerRT(String str) {
        if (StringUtils.isNotBlank(str)) {
            try {
                this.listener.serverRT = Long.parseLong(str);
            } catch (NumberFormatException unused) {
                TBSdkLog.w(TAG, "[parseServerRT] invalid X-Server-Rt header. X-Server-Rt=" + str);
            }
        }
    }
}
