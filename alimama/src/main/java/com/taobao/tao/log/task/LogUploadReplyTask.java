package com.taobao.tao.log.task;

import android.util.Log;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alipay.auth.mobile.common.AlipayAuthConstant;
import com.taobao.android.tlog.protocol.OpCode;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.RequestResult;
import com.taobao.android.tlog.protocol.model.reply.LogUploadReply;
import com.taobao.android.tlog.protocol.model.reply.base.LogReplyBaseInfo;
import com.taobao.android.tlog.protocol.model.reply.base.RemoteFileInfo;
import com.taobao.android.tlog.protocol.model.reply.base.StorageInfo;
import com.taobao.android.tlog.protocol.model.reply.base.UploadTokenInfo;
import com.taobao.tao.log.TLogConstant;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.message.SendMessage;
import com.taobao.tao.log.monitor.TLogMonitor;
import com.taobao.tao.log.monitor.TLogStage;
import com.taobao.tao.log.upload.UploaderInfo;
import java.io.File;

public class LogUploadReplyTask {
    private static String TAG = "TLOG.LogUploadReplyTask";

    public static void executeSuccess(CommandInfo commandInfo, String str, String str2, String str3, String str4, String str5, String str6) {
        TLogMonitor tLogMonitor = TLogInitializer.getInstance().gettLogMonitor();
        String str7 = TLogStage.MSG_HANDLE;
        String str8 = TAG;
        tLogMonitor.stageInfo(str7, str8, "消息处理：服务端主动要求上传文件回复，uploadId=" + str);
        LogUploadReply logUploadReply = new LogUploadReply();
        UploaderInfo uploadInfo = TLogInitializer.getInstance().getLogUploader().getUploadInfo();
        logUploadReply.uploadId = str;
        StorageInfo storageInfo = new StorageInfo();
        UploadTokenInfo uploadTokenInfo = new UploadTokenInfo();
        if (uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_OSS) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_ARUP) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_CEPH)) {
            uploadTokenInfo.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
            storageInfo.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
            storageInfo.put("ossObjectKey", str5);
            if (uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_OSS)) {
                if (str6 != null) {
                    storageInfo.put("ossPath", "http://" + TLogInitializer.getInstance().ossBucketName + "/" + str6 + "/" + str3);
                }
            } else if (uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_ARUP)) {
                storageInfo.put("ossPath", str3);
            }
        }
        RemoteFileInfo[] remoteFileInfoArr = new RemoteFileInfo[1];
        RemoteFileInfo remoteFileInfo = new RemoteFileInfo();
        File file = new File(str2);
        if (file.exists()) {
            remoteFileInfo.absolutePath = file.getAbsolutePath();
            remoteFileInfo.contentLength = Long.valueOf(file.length());
            remoteFileInfo.fileName = file.getName();
            remoteFileInfo.contentEncoding = "gzip";
            remoteFileInfo.contentType = str4;
            if (remoteFileInfo.contentType == null) {
                remoteFileInfo.contentType = "application/x-tlog";
            }
        }
        remoteFileInfoArr[0] = remoteFileInfo;
        remoteFileInfo.storageType = uploadInfo.type;
        remoteFileInfo.storageInfo = storageInfo;
        logUploadReply.remoteFileInfos = remoteFileInfoArr;
        logUploadReply.tokenType = uploadInfo.type;
        logUploadReply.tokenInfo = uploadTokenInfo;
        String appkey = TLogInitializer.getInstance().getAppkey();
        String utdid = TLogInitializer.getUTDID();
        LogReplyBaseInfo logReplyBaseInfo = new LogReplyBaseInfo();
        logReplyBaseInfo.appKey = appkey;
        logReplyBaseInfo.appId = TLogInitializer.getInstance().getAppId();
        logReplyBaseInfo.utdid = utdid;
        logReplyBaseInfo.replyOpCode = OpCode.LOG_UPLOAD_REPLY;
        logReplyBaseInfo.replyCode = AlipayAuthConstant.LoginResult.SUCCESS;
        logReplyBaseInfo.replyMsg = "";
        try {
            String build = logUploadReply.build(commandInfo, logReplyBaseInfo);
            if (build != null) {
                RequestResult requestResult = new RequestResult();
                requestResult.content = build;
                SendMessage.send(TLogInitializer.getInstance().getContext(), requestResult);
                return;
            }
            Log.w(TAG, "content build failure");
        } catch (Exception e) {
            Log.e(TAG, "log upload reply error", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, TAG, (Throwable) e);
        }
    }

    public static void executeFailure(CommandInfo commandInfo, String str, String str2, String str3, String str4, String str5, String str6) {
        TLogMonitor tLogMonitor = TLogInitializer.getInstance().gettLogMonitor();
        String str7 = TLogStage.MSG_HANDLE;
        String str8 = TAG;
        tLogMonitor.stageError(str7, str8, str4 + ":" + str5);
        LogUploadReply logUploadReply = new LogUploadReply();
        UploaderInfo uploadInfo = TLogInitializer.getInstance().getLogUploader().getUploadInfo();
        logUploadReply.uploadId = str;
        UploadTokenInfo uploadTokenInfo = new UploadTokenInfo();
        StorageInfo storageInfo = new StorageInfo();
        if (uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_OSS) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_ARUP) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_CEPH)) {
            uploadTokenInfo.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
            storageInfo.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
            storageInfo.put("ossObjectKey", "");
            storageInfo.put("ossPath", "");
        }
        storageInfo.put("errorCode", str4);
        storageInfo.put(ILocatable.ERROR_MSG, str5);
        RemoteFileInfo[] remoteFileInfoArr = new RemoteFileInfo[1];
        RemoteFileInfo remoteFileInfo = new RemoteFileInfo();
        File file = new File(str2);
        if (file.exists()) {
            remoteFileInfo.absolutePath = file.getAbsolutePath();
            remoteFileInfo.contentLength = Long.valueOf(file.length());
            remoteFileInfo.fileName = file.getName();
            remoteFileInfo.contentEncoding = "gzip";
            remoteFileInfo.contentType = str6;
            if (remoteFileInfo.contentType == null) {
                remoteFileInfo.contentType = "application/x-tlog";
            }
        }
        remoteFileInfoArr[0] = remoteFileInfo;
        remoteFileInfo.storageType = uploadInfo.type;
        remoteFileInfo.storageInfo = storageInfo;
        logUploadReply.remoteFileInfos = remoteFileInfoArr;
        logUploadReply.tokenInfo = uploadTokenInfo;
        logUploadReply.tokenType = uploadInfo.type;
        String appkey = TLogInitializer.getInstance().getAppkey();
        String utdid = TLogInitializer.getUTDID();
        LogReplyBaseInfo logReplyBaseInfo = new LogReplyBaseInfo();
        logReplyBaseInfo.appKey = appkey;
        logReplyBaseInfo.appId = TLogInitializer.getInstance().getAppId();
        logReplyBaseInfo.utdid = utdid;
        logReplyBaseInfo.replyOpCode = OpCode.LOG_UPLOAD_REPLY;
        logReplyBaseInfo.replyCode = str4;
        logReplyBaseInfo.replyMsg = str5;
        try {
            String build = logUploadReply.build(commandInfo, logReplyBaseInfo);
            if (build != null) {
                RequestResult requestResult = new RequestResult();
                requestResult.content = build;
                SendMessage.send(TLogInitializer.getInstance().getContext(), requestResult);
                return;
            }
            Log.w(TAG, "content build failure");
        } catch (Exception e) {
            Log.e(TAG, "log upload reply error", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, TAG, (Throwable) e);
        }
    }
}
