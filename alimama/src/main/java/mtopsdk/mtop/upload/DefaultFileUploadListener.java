package mtopsdk.mtop.upload;

import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.upload.domain.UploadFileInfo;

public class DefaultFileUploadListener implements FileUploadBaseListener {
    private static final String TAG = "mtopsdk.DefaultFileUploadListener";
    private FileUploadListener oldListener;

    public DefaultFileUploadListener() {
    }

    public DefaultFileUploadListener(FileUploadListener fileUploadListener) {
        this.oldListener = fileUploadListener;
    }

    public void onStart() {
        if (this.oldListener != null) {
            this.oldListener.onStart();
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, "[onStart]onStart called." + ", ThreadName:" + Thread.currentThread().getName());
        }
    }

    public void onProgress(int i) {
        if (this.oldListener != null) {
            this.oldListener.onProgress(i);
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, "[onProgress]onProgress (percentage=" + i + "), ThreadName:" + Thread.currentThread().getName());
        }
    }

    public void onFinish(UploadFileInfo uploadFileInfo, String str) {
        if (this.oldListener != null) {
            this.oldListener.onFinish(str);
        } else {
            onFinish(str);
        }
    }

    public void onError(String str, String str2, String str3) {
        if (this.oldListener != null) {
            this.oldListener.onError(str2, str3);
        } else {
            onError(str2, str3);
        }
    }

    @Deprecated
    public void onError(String str, String str2) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, "[onError]onError errCode=" + str + ", errMsg=" + str2 + " , ThreadName:" + Thread.currentThread().getName());
        }
    }

    @Deprecated
    public void onFinish(String str) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, "[onFinish]onFinish url=" + str + ", ThreadName:" + Thread.currentThread().getName());
        }
    }
}
