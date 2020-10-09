package com.taobao.tao.log.task;

import android.util.Log;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.request.UserDefineUploadRequest;
import com.taobao.tao.log.TLogConstant;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.monitor.TLogMonitor;
import com.taobao.tao.log.monitor.TLogStage;
import com.taobao.tao.log.upload.FileUploadListener;
import com.taobao.tao.log.upload.LogFileUploadManager;
import com.taobao.tao.log.uploader.service.TLogFileUploader;
import com.taobao.tao.log.uploader.service.TLogFileUploaderCallBack;
import com.taobao.tao.log.uploader.service.TLogUploadFileModel;
import com.taobao.tao.log.uploader.service.TLogUploadMsg;
import java.util.Map;

public class UDFUploadRequestTask implements ICommandTask {
    /* access modifiers changed from: private */
    public String TAG = "TLOG.UDFUploadRequestTask";
    /* access modifiers changed from: private */
    public String defaultBizType = "USER_UPLOAD";
    /* access modifiers changed from: private */
    public String defaultDebugType = TLogConstant.MODEL;

    public ICommandTask execute(CommandInfo commandInfo) {
        TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_HANDLE, this.TAG, "消息处理：服务端请求上传用户自定义文件");
        try {
            UserDefineUploadRequest userDefineUploadRequest = new UserDefineUploadRequest();
            userDefineUploadRequest.parse(commandInfo.data, commandInfo);
            String str = userDefineUploadRequest.bizType;
            String str2 = userDefineUploadRequest.bizCode;
            if (str2 != null) {
                if (str != null) {
                    if (!this.defaultBizType.equals(str)) {
                        UDFUploadReplyTask.executeFailure(commandInfo, "400", "消息处理：自定义文件上传失败，bizType不对:" + str);
                        return null;
                    }
                    Map<String, String> map = userDefineUploadRequest.extraInfo;
                    Map<String, TLogFileUploader> map2 = TLogInitializer.getInstance().fileUploaderMap;
                    if (map2 == null) {
                        UDFUploadReplyTask.executeFailure(commandInfo, "400", "消息处理：自定义文件上传失败，客户端没有注册任何uploader:" + str2);
                        return null;
                    }
                    TLogFileUploader tLogFileUploader = map2.get(str2);
                    if (tLogFileUploader == null) {
                        UDFUploadReplyTask.executeFailure(commandInfo, "400", "消息处理：自定义文件上传失败，客户端没有可处理的uploader:" + str2);
                        return null;
                    }
                    String bizCode = tLogFileUploader.getBizCode();
                    if (!bizCode.equals(str2)) {
                        UDFUploadReplyTask.executeFailure(commandInfo, "400", "消息处理：自定义文件上传失败，bizCode校验失败,uploader的bizCode=" + bizCode);
                        return null;
                    }
                    UDFUploadReplyTask.executeSuccess(commandInfo);
                    TLogUploadMsg tLogUploadMsg = new TLogUploadMsg();
                    tLogUploadMsg.extInfo = map;
                    tLogFileUploader.executeUploadTask(tLogUploadMsg, new TLogFileUploaderCallBack() {
                        public Boolean onFileUpload(TLogUploadFileModel tLogUploadFileModel) {
                            String str = tLogUploadFileModel.filePath;
                            String str2 = tLogUploadFileModel.bizCode;
                            if (str != null && str2 != null) {
                                return fileUpload(tLogUploadFileModel);
                            }
                            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, UDFUploadRequestTask.this.TAG, "消息处理：执行用户自定义文件上传校验失败，业务返回参数有错，filePath或者bizcode为空");
                            return false;
                        }

                        private Boolean fileUpload(TLogUploadFileModel tLogUploadFileModel) {
                            new LogFileUploadManager(TLogInitializer.getInstance().getContext()).uploadWithFilePath(tLogUploadFileModel.filePath, UDFUploadRequestTask.this.defaultDebugType, UDFUploadRequestTask.this.defaultBizType, tLogUploadFileModel.bizCode, tLogUploadFileModel.extraInfos, new FileUploadListener() {
                                public void onError(String str, String str2, String str3) {
                                    TLogMonitor tLogMonitor = TLogInitializer.getInstance().gettLogMonitor();
                                    String str4 = TLogStage.MSG_HANDLE;
                                    String access$000 = UDFUploadRequestTask.this.TAG;
                                    tLogMonitor.stageError(str4, access$000, "消息处理：执行用户自定义文件上传失败：" + str2 + ":" + str3);
                                }

                                public void onSucessed(String str, String str2) {
                                    TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_HANDLE, UDFUploadRequestTask.this.TAG, "消息处理：执行用户自定义文件上传成功");
                                }
                            });
                            return true;
                        }
                    });
                    return null;
                }
            }
            UDFUploadReplyTask.executeFailure(commandInfo, "400", "消息处理：自定义文件上传失败，bizCode 或者 bizType为空");
            return null;
        } catch (Exception e) {
            Log.e(this.TAG, "execute error", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, this.TAG, (Throwable) e);
            UDFUploadReplyTask.executeFailure(commandInfo, "500", "消息处理：自定义文件上传失败，抛错，请查看错误日志");
        }
    }
}
