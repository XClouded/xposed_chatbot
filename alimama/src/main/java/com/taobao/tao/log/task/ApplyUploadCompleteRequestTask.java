package com.taobao.tao.log.task;

import android.util.Log;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alipay.auth.mobile.common.AlipayAuthConstant;
import com.taobao.android.tlog.protocol.OpCode;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.reply.base.RemoteFileInfo;
import com.taobao.android.tlog.protocol.model.reply.base.StorageInfo;
import com.taobao.android.tlog.protocol.model.reply.base.UploadTokenInfo;
import com.taobao.android.tlog.protocol.model.request.ApplyUploadCompleteRequest;
import com.taobao.tao.log.TLogConstant;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.message.SendMessage;
import com.taobao.tao.log.monitor.TLogStage;
import com.taobao.tao.log.upload.UploaderInfo;
import java.io.File;

public class ApplyUploadCompleteRequestTask {
    private static String TAG = "TLOG.ApplyUploadCompleteRequestTask";

    public static void executeSuccess(CommandInfo commandInfo, String str, String str2, String str3, String str4, String str5, String str6) {
        TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_HANDLE, TAG, "消息处理：文件上传成功");
        ApplyUploadCompleteRequest applyUploadCompleteRequest = new ApplyUploadCompleteRequest();
        UploaderInfo uploadInfo = TLogInitializer.getInstance().getLogUploader().getUploadInfo();
        applyUploadCompleteRequest.uploadId = str;
        StorageInfo storageInfo = new StorageInfo();
        UploadTokenInfo uploadTokenInfo = new UploadTokenInfo();
        applyUploadCompleteRequest.tokenType = uploadInfo.type;
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
        storageInfo.put("errorCode", AlipayAuthConstant.LoginResult.SUCCESS);
        RemoteFileInfo[] remoteFileInfoArr = new RemoteFileInfo[1];
        RemoteFileInfo remoteFileInfo = new RemoteFileInfo();
        remoteFileInfo.storageType = uploadInfo.type;
        remoteFileInfo.storageInfo = storageInfo;
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
        applyUploadCompleteRequest.remoteFileInfos = remoteFileInfoArr;
        applyUploadCompleteRequest.tokenInfo = uploadTokenInfo;
        applyUploadCompleteRequest.tokenType = uploadInfo.type;
        String appkey = TLogInitializer.getInstance().getAppkey();
        String utdid = TLogInitializer.getUTDID();
        applyUploadCompleteRequest.appKey = appkey;
        applyUploadCompleteRequest.appId = TLogInitializer.getInstance().getAppId();
        applyUploadCompleteRequest.utdid = utdid;
        applyUploadCompleteRequest.user = TLogInitializer.getInstance().getUserNick();
        applyUploadCompleteRequest.opCode = OpCode.APPLY_UPLOAD_COMPLETE;
        try {
            SendMessage.send(TLogInitializer.getInstance().getContext(), applyUploadCompleteRequest.build());
        } catch (Exception e) {
            Log.e(TAG, "build apply upload complete request error", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, TAG, (Throwable) e);
        }
    }

    public static void executeFailure(CommandInfo commandInfo, String str, String str2, String str3, String str4, String str5, String str6) {
        ApplyUploadCompleteRequest applyUploadCompleteRequest = new ApplyUploadCompleteRequest();
        UploaderInfo uploadInfo = TLogInitializer.getInstance().getLogUploader().getUploadInfo();
        applyUploadCompleteRequest.uploadId = str;
        StorageInfo storageInfo = new StorageInfo();
        UploadTokenInfo uploadTokenInfo = new UploadTokenInfo();
        applyUploadCompleteRequest.tokenType = uploadInfo.type;
        if (uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_OSS) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_ARUP) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_CEPH)) {
            uploadTokenInfo.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
            storageInfo.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
            storageInfo.put("ossObjectKey", "");
            storageInfo.put("ossPath", "");
        }
        storageInfo.put("errorCode", str4);
        storageInfo.put(ILocatable.ERROR_MSG, str5);
        applyUploadCompleteRequest.tokenInfo = uploadTokenInfo;
        applyUploadCompleteRequest.tokenType = uploadInfo.type;
        RemoteFileInfo[] remoteFileInfoArr = new RemoteFileInfo[1];
        RemoteFileInfo remoteFileInfo = new RemoteFileInfo();
        remoteFileInfo.storageType = uploadInfo.type;
        remoteFileInfo.storageInfo = storageInfo;
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
        applyUploadCompleteRequest.remoteFileInfos = remoteFileInfoArr;
        String appkey = TLogInitializer.getInstance().getAppkey();
        String utdid = TLogInitializer.getUTDID();
        applyUploadCompleteRequest.appKey = appkey;
        applyUploadCompleteRequest.appId = TLogInitializer.getInstance().getAppId();
        applyUploadCompleteRequest.utdid = utdid;
        applyUploadCompleteRequest.user = TLogInitializer.getInstance().getUserNick();
        applyUploadCompleteRequest.opCode = OpCode.APPLY_UPLOAD_COMPLETE;
        try {
            SendMessage.send(TLogInitializer.getInstance().getContext(), applyUploadCompleteRequest.build());
        } catch (Exception e) {
            Log.e(TAG, "build apply upload complete request error", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, TAG, (Throwable) e);
        }
    }
}
