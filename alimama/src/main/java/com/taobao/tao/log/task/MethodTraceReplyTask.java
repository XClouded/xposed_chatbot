package com.taobao.tao.log.task;

import alimama.com.unwetaologger.base.UNWLogger;
import android.util.Log;
import com.alipay.auth.mobile.common.AlipayAuthConstant;
import com.taobao.android.tlog.protocol.OpCode;
import com.taobao.android.tlog.protocol.model.RequestResult;
import com.taobao.android.tlog.protocol.model.reply.MethodTraceReply;
import com.taobao.android.tlog.protocol.model.reply.base.LogReplyBaseInfo;
import com.taobao.android.tlog.protocol.model.reply.base.RemoteFileInfo;
import com.taobao.android.tlog.protocol.model.reply.base.StorageInfo;
import com.taobao.android.tlog.protocol.model.reply.base.UploadTokenInfo;
import com.taobao.tao.log.TLog;
import com.taobao.tao.log.TLogConstant;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.godeye.core.GodEyeReponse;
import com.taobao.tao.log.message.SendMessage;
import com.taobao.tao.log.monitor.TLogStage;
import com.taobao.tao.log.upload.FileUploadListener;
import com.taobao.tao.log.upload.UploadQueue;
import com.taobao.tao.log.upload.UploaderInfo;
import java.io.File;
import java.util.ArrayList;

public class MethodTraceReplyTask implements GodEyeReponse {
    /* access modifiers changed from: private */
    public static String TAG = "TLOG.MethodTraceReplyTask";

    public static void execute(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_HANDLE, TAG, "消息处理：method trace 服务端回复消息");
        LogReplyBaseInfo logReplyBaseInfo = new LogReplyBaseInfo();
        logReplyBaseInfo.replyOpCode = OpCode.METHOD_TRACE_DUMP_REPLY;
        logReplyBaseInfo.replyCode = AlipayAuthConstant.LoginResult.SUCCESS;
        logReplyBaseInfo.replyMsg = "";
        logReplyBaseInfo.utdid = TLogInitializer.getUTDID();
        logReplyBaseInfo.appKey = TLogInitializer.getInstance().getAppkey();
        logReplyBaseInfo.appId = TLogInitializer.getInstance().getAppId();
        UploaderInfo uploadInfo = TLogInitializer.getInstance().getLogUploader().getUploadInfo();
        StorageInfo storageInfo = new StorageInfo();
        storageInfo.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
        storageInfo.put("ossObjectKey", str6);
        if (uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_OSS) && str7 != null) {
            storageInfo.put("ossPath", "http://" + TLogInitializer.getInstance().ossBucketName + "/" + str7 + "/" + str4);
        }
        storageInfo.put(UNWLogger.LOG_VALUE_TYPE_USER, TLogInitializer.getInstance().getUserNick());
        MethodTraceReply methodTraceReply = new MethodTraceReply();
        UploadTokenInfo uploadTokenInfo = new UploadTokenInfo();
        methodTraceReply.tokenType = uploadInfo.type;
        if (uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_OSS) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_ARUP) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_CEPH)) {
            uploadTokenInfo.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
        }
        methodTraceReply.tokenInfo = uploadTokenInfo;
        RemoteFileInfo[] remoteFileInfoArr = new RemoteFileInfo[1];
        RemoteFileInfo remoteFileInfo = new RemoteFileInfo();
        if (str3 != null && str3.length() > 0) {
            File file = new File(str3);
            if (file.exists()) {
                remoteFileInfo.absolutePath = file.getAbsolutePath();
                remoteFileInfo.contentLength = Long.valueOf(file.length());
                remoteFileInfo.fileName = file.getName();
                remoteFileInfo.contentEncoding = "gzip";
                remoteFileInfo.contentType = str5;
            }
        }
        remoteFileInfoArr[0] = remoteFileInfo;
        remoteFileInfo.storageType = uploadInfo.type;
        remoteFileInfo.storageInfo = storageInfo;
        methodTraceReply.uploadId = str2;
        methodTraceReply.remoteFileInfos = remoteFileInfoArr;
        try {
            String build = methodTraceReply.build(str, logReplyBaseInfo);
            if (build != null) {
                Log.e("xxxxxxxxxxxxxxx", build);
                RequestResult requestResult = new RequestResult();
                requestResult.content = build;
                SendMessage.send(TLogInitializer.getInstance().getContext(), requestResult);
                return;
            }
            Log.w(TAG, "content build failure");
        } catch (Exception e) {
            Log.e(TAG, "method trace reply error", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, TAG, (Throwable) e);
        }
    }

    public void sendFile(String str, String str2, FileUploadListener fileUploadListener) {
        new MethodTraceThread("method trace", str, str2, fileUploadListener).start();
    }

    public class MethodTraceThread extends Thread {
        private String filePath;
        private FileUploadListener fileUploadListener;
        private String uploadId;

        public MethodTraceThread(String str, String str2, String str3, FileUploadListener fileUploadListener2) {
            super(str);
            this.uploadId = str2;
            this.filePath = str3;
            this.fileUploadListener = fileUploadListener2;
        }

        public void run() {
            try {
                ArrayList arrayList = new ArrayList();
                arrayList.add(this.filePath);
                if (this.uploadId != null) {
                    UploadQueue.getInstance().pushListener(this.uploadId, this.fileUploadListener);
                    ApplyTokenRequestTask.execute(this.uploadId, arrayList, "application/x-perf-methodtrace");
                    return;
                }
                Log.e(MethodTraceReplyTask.TAG, "upload id is null ");
                TLog.loge(TLogConstant.MODEL, MethodTraceReplyTask.TAG, "method trace upload id is null");
            } catch (Exception e) {
                e.printStackTrace();
                TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, MethodTraceReplyTask.TAG, (Throwable) e);
            }
        }
    }
}
