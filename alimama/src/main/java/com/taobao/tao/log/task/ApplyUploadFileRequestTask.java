package com.taobao.tao.log.task;

import android.util.Log;
import com.taobao.android.tlog.protocol.OpCode;
import com.taobao.android.tlog.protocol.model.RequestResult;
import com.taobao.android.tlog.protocol.model.reply.base.UploadTokenInfo;
import com.taobao.android.tlog.protocol.model.request.ApplyUploadRequest;
import com.taobao.android.tlog.protocol.model.request.base.FileInfo;
import com.taobao.tao.log.TLogConstant;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.message.SendMessage;
import com.taobao.tao.log.monitor.TLogStage;
import com.taobao.tao.log.upload.FileUploadListener;
import com.taobao.tao.log.upload.UploadQueue;
import com.taobao.tao.log.upload.UploaderInfo;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplyUploadFileRequestTask {
    private static String TAG = "TLOG.ApplyUploadFileRequestTask";

    public static void execute(List<String> list, String str, String str2, String str3, Map<String, String> map, FileUploadListener fileUploadListener) {
        try {
            TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_HANDLE, TAG, "消息处理：请求文件上传消息");
            UploaderInfo uploadInfo = TLogInitializer.getInstance().getLogUploader().getUploadInfo();
            ApplyUploadRequest applyUploadRequest = new ApplyUploadRequest();
            applyUploadRequest.bizType = str2;
            applyUploadRequest.debugType = str;
            applyUploadRequest.bizCode = str3;
            applyUploadRequest.tokenType = uploadInfo.type;
            UploadTokenInfo uploadTokenInfo = new UploadTokenInfo();
            applyUploadRequest.tokenType = uploadInfo.type;
            if (uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_OSS) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_ARUP) || uploadInfo.type.equals(TLogConstant.TOKEN_TYPE_CEPH)) {
                uploadTokenInfo.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
            }
            applyUploadRequest.tokenInfo = uploadTokenInfo;
            if (map != null && map.size() > 0) {
                applyUploadRequest.extraInfo = new HashMap();
                for (Map.Entry next : map.entrySet()) {
                    applyUploadRequest.extraInfo.put(next.getKey(), next.getValue());
                }
            }
            FileInfo[] fileInfoArr = new FileInfo[list.size()];
            for (int i = 0; i < list.size(); i++) {
                File file = new File(list.get(i));
                FileInfo fileInfo = new FileInfo();
                if (file.exists()) {
                    fileInfo.fileName = file.getName();
                    fileInfo.absolutePath = file.getAbsolutePath();
                    fileInfo.contentLength = Long.valueOf(file.length());
                    fileInfo.lastModified = Long.valueOf(file.lastModified());
                    if (str.equals("method_trace")) {
                        fileInfo.contentType = "application/x-perf-methodtrace";
                    } else if (str.equals("heap_dump")) {
                        fileInfo.contentType = "application/x-perf-heapdump";
                    } else if (str.equals("tlog")) {
                        fileInfo.contentType = "application/x-tlog";
                    }
                    fileInfo.contentEncoding = "gzip";
                    fileInfoArr[i] = fileInfo;
                }
            }
            applyUploadRequest.fileInfos = fileInfoArr;
            String appkey = TLogInitializer.getInstance().getAppkey();
            String utdid = TLogInitializer.getUTDID();
            applyUploadRequest.appKey = appkey;
            applyUploadRequest.appId = TLogInitializer.getInstance().getAppId();
            applyUploadRequest.utdid = utdid;
            applyUploadRequest.user = TLogInitializer.getInstance().getUserNick();
            applyUploadRequest.opCode = OpCode.APPLY_UPLOAD;
            RequestResult build = applyUploadRequest.build();
            if (fileUploadListener != null) {
                UploadQueue.getInstance().pushListener(build.requestId, fileUploadListener);
            }
            SendMessage.send(TLogInitializer.getInstance().getContext(), build);
        } catch (Exception e) {
            Log.e(TAG, "send request message error ", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, TAG, (Throwable) e);
        }
    }
}
