package com.taobao.tao.log.task;

import android.text.TextUtils;
import android.util.Log;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.reply.base.UploadTokenInfo;
import com.taobao.android.tlog.protocol.model.request.base.FileInfo;
import com.taobao.tao.log.TLog;
import com.taobao.tao.log.TLogConstant;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.TLogUtils;
import com.taobao.tao.log.monitor.TLogStage;
import com.taobao.tao.log.upload.LogFileUploadManager;
import java.util.List;

public class UploadFileTask {
    private static String TAG = "TLOG.UploadFileTask";

    public static synchronized void taskExecute(CommandInfo commandInfo, String str, String str2, UploadTokenInfo[] uploadTokenInfoArr) {
        synchronized (UploadFileTask.class) {
            try {
                TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_HANDLE, TAG, "消息处理：开始处理文件上传消息");
                LogFileUploadManager logFileUploadManager = new LogFileUploadManager(TLogInitializer.getInstance().getContext());
                logFileUploadManager.uploadId = str;
                logFileUploadManager.tokenType = str2;
                logFileUploadManager.tokenInfos = uploadTokenInfoArr;
                logFileUploadManager.mParmas = commandInfo;
                for (UploadTokenInfo uploadTokenInfo : uploadTokenInfoArr) {
                    FileInfo fileInfo = uploadTokenInfo.fileInfo;
                    String str3 = fileInfo.fileName;
                    String str4 = fileInfo.absolutePath;
                    if (logFileUploadManager.isUploading()) {
                        TLog.loge(TLogConstant.MODEL, TAG, "[persistTask] there is task!");
                    } else {
                        List<String> filePath = TLogUtils.getFilePath(str3, 1, (String[]) null);
                        if (filePath != null && filePath.size() > 0) {
                            logFileUploadManager.addFiles(filePath);
                        }
                        if (!TextUtils.isEmpty(str4)) {
                            logFileUploadManager.addFile(str4);
                        }
                        logFileUploadManager.isForceUpload = true;
                        if (logFileUploadManager.getUploadTaskCount() == 0) {
                            TLog.loge(TLogConstant.MODEL, TAG, "There are not files matching the condition");
                        } else {
                            TLog.loge(TLogConstant.MODEL, TAG, "There are " + logFileUploadManager.getUploadTaskCount() + " files to upload!");
                        }
                    }
                }
                TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_LOG_UPLOAD, TAG, "文件上传：开始触发上传文件,uploadId=" + str);
                logFileUploadManager.startUpload();
            } catch (Exception e) {
                Log.e(TAG, "task execute failure ", e);
                TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, TAG, (Throwable) e);
            }
        }
        return;
    }
}
