package com.taobao.android.tlog.uploader;

import android.content.Context;
import android.os.Handler;
import com.alibaba.fastjson.JSON;
import com.taobao.tao.log.TLogConstant;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.monitor.TLogMonitor;
import com.taobao.tao.log.monitor.TLogStage;
import com.taobao.tao.log.upload.FileUploadListener;
import com.taobao.tao.log.upload.LogUploader;
import com.taobao.tao.log.upload.UploaderInfo;
import com.taobao.tao.log.upload.UploaderParam;
import com.uploader.export.ITaskListener;
import com.uploader.export.ITaskResult;
import com.uploader.export.IUploaderManager;
import com.uploader.export.IUploaderTask;
import com.uploader.export.TaskError;
import com.uploader.export.UploaderCreator;
import com.uploader.portal.UploaderDependencyImpl;
import com.uploader.portal.UploaderEnvironmentImpl;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TLogUploader implements LogUploader {
    private static String TAG = "TLogUploader.arup";
    private IUploaderTask mTask;
    private IUploaderManager mUploadManager;
    private Map<String, Object> metaInfo;

    public void startUpload(UploaderParam uploaderParam, String str, FileUploadListener fileUploadListener) {
        if (uploaderParam.params == null) {
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_LOG_UPLOAD, TAG, "服务端下发的参数为空(upload param)");
            return;
        }
        Context context = uploaderParam.context;
        final String str2 = uploaderParam.appVersion;
        final String str3 = uploaderParam.appKey;
        String str4 = uploaderParam.params.get("arupBizType");
        String str5 = uploaderParam.params.get("ossObjectKey");
        if (str4 == null || str5 == null) {
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_LOG_UPLOAD, TAG, "服务端下发的arupBizType或者ossObjectKey有一个为空，终止上传");
            return;
        }
        this.mUploadManager = UploaderCreator.get();
        if (!this.mUploadManager.isInitialized()) {
            this.mUploadManager.initialize(context, new UploaderDependencyImpl(context, new UploaderEnvironmentImpl(context) {
                public int getEnvironment() {
                    return 0;
                }

                public String getAppVersion() {
                    return str2;
                }

                public String getAppKey() {
                    return str3;
                }
            }));
        }
        UploadTask uploadTask = new UploadTask();
        uploadTask.bizType = str4;
        uploadTask.fileType = ".log";
        if (uploadTask.metaInfo == null) {
            uploadTask.metaInfo = new HashMap();
        }
        if (this.metaInfo != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("action", JSON.toJSON(this.metaInfo).toString());
            uploadTask.metaInfo.putAll(hashMap);
        }
        uploadTask.metaInfo.put("arupBizType", str4);
        uploadTask.metaInfo.put("ossObjectKey", str5);
        File file = new File(uploaderParam.logFilePathTmp);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(str);
        try {
            File copyFile = TLogFileUtils.copyFile(file2, new File(file, file2.getName()));
            if (copyFile == null || !copyFile.exists()) {
                uploadTask.filePath = str;
            } else {
                uploadTask.filePath = copyFile.getAbsolutePath();
            }
            this.mTask = uploadTask;
            upload(uploadTask.filePath, fileUploadListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upload(String str, FileUploadListener fileUploadListener) {
        TLogMonitor tLogMonitor = TLogInitializer.getInstance().gettLogMonitor();
        String str2 = TLogStage.MSG_LOG_UPLOAD;
        String str3 = TAG;
        tLogMonitor.stageInfo(str2, str3, "开始调用arup接口异步上传文件，文件路径为：" + str);
        this.mUploadManager.uploadAsync(this.mTask, new TaskListenerImp(fileUploadListener), (Handler) null);
    }

    public void cancel() {
        if (this.mTask != null && this.mUploadManager != null) {
            this.mUploadManager.cancelAsync(this.mTask);
        }
    }

    public void setMetaInfo(Map<String, Object> map) {
        this.metaInfo = map;
    }

    public UploaderInfo getUploadInfo() {
        UploaderInfo uploaderInfo = new UploaderInfo();
        uploaderInfo.type = TLogConstant.TOKEN_TYPE_ARUP;
        return uploaderInfo;
    }

    class TaskListenerImp implements ITaskListener {
        FileUploadListener listener;

        public void onPause(IUploaderTask iUploaderTask) {
        }

        public void onProgress(IUploaderTask iUploaderTask, int i) {
        }

        public void onResume(IUploaderTask iUploaderTask) {
        }

        public void onStart(IUploaderTask iUploaderTask) {
        }

        public void onWait(IUploaderTask iUploaderTask) {
        }

        TaskListenerImp(FileUploadListener fileUploadListener) {
            this.listener = fileUploadListener;
        }

        public void onSuccess(IUploaderTask iUploaderTask, ITaskResult iTaskResult) {
            if (this.listener != null) {
                this.listener.onSucessed(iUploaderTask.getFilePath(), iTaskResult.getFileUrl());
            }
        }

        public void onFailure(IUploaderTask iUploaderTask, TaskError taskError) {
            if (this.listener != null) {
                this.listener.onError(taskError.code, taskError.subcode, taskError.info);
            }
        }

        public void onCancel(IUploaderTask iUploaderTask) {
            if (this.listener != null) {
                this.listener.onError("cancel", "1", "the upload task is canceled!");
            }
        }
    }

    class UploadTask implements IUploaderTask {
        public String bizType;
        public String filePath;
        public String fileType;
        public Map<String, String> metaInfo;

        UploadTask() {
        }

        public String getBizType() {
            return this.bizType;
        }

        public String getFilePath() {
            return this.filePath;
        }

        public String getFileType() {
            return this.fileType;
        }

        public Map<String, String> getMetaInfo() {
            return this.metaInfo;
        }
    }
}
