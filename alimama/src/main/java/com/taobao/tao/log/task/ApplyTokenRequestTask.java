package com.taobao.tao.log.task;

import android.util.Log;
import com.taobao.android.tlog.protocol.OpCode;
import com.taobao.android.tlog.protocol.model.reply.base.UploadTokenInfo;
import com.taobao.android.tlog.protocol.model.request.ApplyTokenRequest;
import com.taobao.android.tlog.protocol.model.request.base.FileInfo;
import com.taobao.tao.log.TLogConstant;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.message.SendMessage;
import com.taobao.tao.log.monitor.TLogStage;
import com.taobao.tao.log.upload.UploaderInfo;
import java.io.File;
import java.util.List;

public class ApplyTokenRequestTask {
    private static String TAG = "TLOG.ApplyTokenRequestTask";

    public static void execute(String str, List<String> list, String str2) {
        try {
            TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_HANDLE, TAG, "消息处理：申请token消息");
            UploaderInfo uploadInfo = TLogInitializer.getInstance().getLogUploader().getUploadInfo();
            ApplyTokenRequest applyTokenRequest = new ApplyTokenRequest();
            applyTokenRequest.uploadId = str;
            applyTokenRequest.opCode = OpCode.APPLY_UPLOAD_TOKEN;
            applyTokenRequest.appKey = TLogInitializer.getInstance().getAppkey();
            applyTokenRequest.appId = TLogInitializer.getInstance().getAppId();
            applyTokenRequest.utdid = TLogInitializer.getUTDID();
            applyTokenRequest.user = TLogInitializer.getInstance().getUserNick();
            UploadTokenInfo uploadTokenInfo = new UploadTokenInfo();
            applyTokenRequest.tokenType = uploadInfo.type;
            if (uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_OSS) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_ARUP) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_CEPH)) {
                uploadTokenInfo.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
            }
            applyTokenRequest.tokenInfo = uploadTokenInfo;
            FileInfo[] fileInfoArr = new FileInfo[list.size()];
            for (int i = 0; i < list.size(); i++) {
                String str3 = list.get(i);
                FileInfo fileInfo = new FileInfo();
                File file = new File(str3);
                if (file.exists()) {
                    fileInfo.fileName = file.getName();
                    fileInfo.absolutePath = str3;
                    fileInfo.contentLength = Long.valueOf(file.length());
                    fileInfo.lastModified = Long.valueOf(file.lastModified());
                    fileInfo.contentType = str2;
                    fileInfo.contentEncoding = "gzip";
                    fileInfoArr[i] = fileInfo;
                }
            }
            applyTokenRequest.fileInfos = fileInfoArr;
            SendMessage.send(TLogInitializer.getInstance().getContext(), applyTokenRequest.build());
        } catch (Exception e) {
            Log.e(TAG, "execute error", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, TAG, (Throwable) e);
        }
    }
}
