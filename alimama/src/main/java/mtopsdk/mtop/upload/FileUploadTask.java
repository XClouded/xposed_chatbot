package mtopsdk.mtop.upload;

import com.taobao.phenix.request.ImageStatistics;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.global.SDKConfig;
import mtopsdk.mtop.intf.IUploadStats;
import mtopsdk.mtop.upload.domain.FileBaseInfo;
import mtopsdk.mtop.upload.domain.UploadConstants;
import mtopsdk.mtop.upload.domain.UploadFileInfo;
import mtopsdk.mtop.upload.domain.UploadToken;
import mtopsdk.mtop.upload.service.UploadFileServiceImpl;
import mtopsdk.mtop.upload.util.FileUploadThreadPoolExecutorFactory;
import mtopsdk.mtop.util.Result;

class FileUploadTask implements Runnable {
    private static final String TAG = "mtopsdk.FileUploadTask";
    private static volatile AtomicBoolean isRegistered = new AtomicBoolean(false);
    protected UploadFileInfo fileInfo;
    protected DefaultFileUploadListenerWrapper listener;

    public FileUploadTask(UploadFileInfo uploadFileInfo, DefaultFileUploadListenerWrapper defaultFileUploadListenerWrapper) {
        this.fileInfo = uploadFileInfo;
        this.listener = defaultFileUploadListenerWrapper;
    }

    /* access modifiers changed from: protected */
    public void upload() {
        if (!isCancelled()) {
            this.listener.onStart();
            UploadFileServiceImpl uploadFileServiceImpl = new UploadFileServiceImpl();
            Result<UploadToken> uploadToken = uploadFileServiceImpl.getUploadToken(this.fileInfo);
            UploadToken model = uploadToken.getModel();
            if (!uploadToken.isSuccess()) {
                this.listener.onError(uploadToken.getErrType(), uploadToken.getErrCode(), uploadToken.getErrInfo());
                commitUploadStatsRecord(uploadToken, model);
                FileUploadMgr.getInstance().removeTask(this.fileInfo);
                return;
            }
            long j = model.fileBaseInfo.fileSize;
            long j2 = model.segmentSize;
            if (!isCancelled()) {
                long j3 = 1;
                if (j > j2) {
                    j3 = ((j + j2) - 1) / j2;
                    int i = 0;
                    while (true) {
                        long j4 = (long) i;
                        if (j4 >= j3) {
                            break;
                        }
                        UploadFileInfo uploadFileInfo = this.fileInfo;
                        FileUploadThreadPoolExecutorFactory.submitUploadTask(new SegmentFileUploadTask(uploadFileInfo, this.listener, model, model.segmentSize * j4, uploadFileServiceImpl));
                        i++;
                    }
                } else {
                    FileUploadThreadPoolExecutorFactory.submitUploadTask(new SegmentFileUploadTask(this.fileInfo, this.listener, model, 0, uploadFileServiceImpl));
                }
                this.listener.segmentNum = j3;
            }
        }
    }

    public void run() {
        try {
            upload();
        } catch (Exception e) {
            TBSdkLog.e(TAG, "excute uploadTask exception", (Throwable) e);
            this.listener.onError(UploadConstants.ERRTYPE_OTHER_UPLOAD_ERROR, UploadConstants.ERRCODE_FILE_ADD_TASK_FAIL, UploadConstants.ERRMSG_FILE_ADD_TASK_FAIL);
            FileUploadMgr.getInstance().removeTask(this.fileInfo);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyProgress(long j, long j2) {
        if (this.listener != null && j2 > 0) {
            this.listener.onProgress(Math.min(Math.abs(Math.round((((float) j) / ((float) j2)) * 100.0f)), 100));
        }
    }

    /* access modifiers changed from: protected */
    public boolean isCancelled() {
        if (!this.listener.isCancelled()) {
            return false;
        }
        if (!TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            return true;
        }
        TBSdkLog.d(TAG, "File Upload Task is cancelled");
        return true;
    }

    /* access modifiers changed from: protected */
    public void commitUploadStatsRecord(Result result, UploadToken uploadToken) {
        try {
            StringBuilder sb = new StringBuilder(result.getErrCode());
            if (StringUtils.isNotBlank(result.getErrInfo())) {
                sb.append(":");
                sb.append(result.getErrInfo());
            }
            FileBaseInfo fileBaseInfo = uploadToken.fileBaseInfo;
            int totalRetryTimes = this.listener.getTotalRetryTimes();
            if (!result.isSuccess() && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                StringBuilder sb2 = new StringBuilder(128);
                sb2.append("[commitUploadStatsRecord] bizCode=");
                sb2.append(uploadToken.bizCode);
                sb2.append(",statusCode=");
                sb2.append(result.getStatusCode());
                sb2.append(",errType=");
                sb2.append(result.getErrType());
                sb2.append(",errCode=");
                sb2.append(sb.toString());
                sb2.append(",retryTimes=");
                sb2.append(totalRetryTimes);
                sb2.append(",fileType=");
                sb2.append(fileBaseInfo.fileType);
                sb2.append(",fileSize=");
                sb2.append(fileBaseInfo.fileSize);
                sb2.append(",totalTime=");
                sb2.append(this.listener.getUploadTotalTime());
                sb2.append(",segmentNum=");
                sb2.append(this.listener.segmentNum);
                sb2.append(",serverRT=");
                sb2.append(this.listener.serverRT);
                TBSdkLog.e(TAG, sb2.toString());
            }
            if (isRegistered.compareAndSet(false, true)) {
                registerUploadStats();
            }
            IUploadStats globalUploadStats = SDKConfig.getInstance().getGlobalUploadStats();
            if (globalUploadStats != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("bizCode", uploadToken.bizCode);
                hashMap.put("statusCode", String.valueOf(result.getStatusCode()));
                hashMap.put("errType", result.getErrType());
                hashMap.put(IWXUserTrackAdapter.MONITOR_ERROR_CODE, sb.toString());
                hashMap.put("retryTimes", String.valueOf(totalRetryTimes));
                hashMap.put("fileType", fileBaseInfo.fileType);
                hashMap.put("segmentNum", String.valueOf(this.listener.segmentNum));
                HashMap hashMap2 = new HashMap();
                hashMap2.put(ImageStatistics.KEY_TOTAL_TIME, Double.valueOf((double) this.listener.getUploadTotalTime()));
                hashMap2.put("fileSize", Double.valueOf((double) fileBaseInfo.fileSize));
                hashMap2.put("serverRT", Double.valueOf((double) this.listener.serverRT));
                globalUploadStats.onCommit(UploadConstants.UPLOAD_MODULE, UploadConstants.UPLOAD_MONITOR_POINT, hashMap, hashMap2);
            }
        } catch (Throwable th) {
            TBSdkLog.e(TAG, "[commitUTRecord]  fileUpload commit UploadStats record error.---" + th.toString());
        }
    }

    private void registerUploadStats() {
        try {
            IUploadStats globalUploadStats = SDKConfig.getInstance().getGlobalUploadStats();
            if (globalUploadStats != null) {
                HashSet hashSet = new HashSet();
                hashSet.add("bizCode");
                hashSet.add("statusCode");
                hashSet.add("errType");
                hashSet.add(IWXUserTrackAdapter.MONITOR_ERROR_CODE);
                hashSet.add("retryTimes");
                hashSet.add("fileType");
                hashSet.add("segmentNum");
                HashSet hashSet2 = new HashSet();
                hashSet2.add(ImageStatistics.KEY_TOTAL_TIME);
                hashSet2.add("fileSize");
                hashSet2.add("serverRT");
                globalUploadStats.onRegister(UploadConstants.UPLOAD_MODULE, UploadConstants.UPLOAD_MONITOR_POINT, hashSet, hashSet2, false);
            } else if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                TBSdkLog.e(TAG, "registerUploadStats failed,uploadStats is null.");
            }
        } catch (Throwable th) {
            TBSdkLog.w(TAG, "[registerUploadStats]register UploadStats error ---", th);
        }
    }
}
