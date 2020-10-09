package mtopsdk.mtop.upload;

import com.uploader.export.ITaskListener;
import com.uploader.export.ITaskResult;
import com.uploader.export.IUploaderTask;
import com.uploader.export.TaskError;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.upload.domain.UploadFileInfo;

class TaskListenerAdapter implements ITaskListener {
    private static final String TAG = "mtopsdk.TaskListenerAdapter";
    private DefaultFileUploadListenerWrapper listenerWrapper;
    private UploadFileInfo uploadFileInfo;

    public TaskListenerAdapter(UploadFileInfo uploadFileInfo2, DefaultFileUploadListenerWrapper defaultFileUploadListenerWrapper) {
        this.listenerWrapper = defaultFileUploadListenerWrapper;
        this.uploadFileInfo = uploadFileInfo2;
    }

    public void onWait(IUploaderTask iUploaderTask) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "onWait called.");
        }
    }

    public void onStart(IUploaderTask iUploaderTask) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "onStart called.");
        }
        this.listenerWrapper.onStart();
    }

    public void onResume(IUploaderTask iUploaderTask) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "onResume called.");
        }
    }

    public void onPause(IUploaderTask iUploaderTask) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "onPause called.");
        }
    }

    public void onSuccess(IUploaderTask iUploaderTask, ITaskResult iTaskResult) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "onSuccess called.");
        }
        this.listenerWrapper.onFinish(this.uploadFileInfo, iTaskResult.getFileUrl());
        doRemove();
    }

    public void onFailure(IUploaderTask iUploaderTask, TaskError taskError) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "onFailure called.");
        }
        this.listenerWrapper.onError(taskError.code, taskError.subcode, taskError.info);
        doRemove();
    }

    public void onCancel(IUploaderTask iUploaderTask) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "onCancel called.");
        }
    }

    public void onProgress(IUploaderTask iUploaderTask, int i) {
        this.listenerWrapper.onProgress(i);
    }

    private void doRemove() {
        FileUploadMgr.getInstance().removeArupTask(this.uploadFileInfo);
    }
}
