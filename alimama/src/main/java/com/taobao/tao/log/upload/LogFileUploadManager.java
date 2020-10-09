package com.taobao.tao.log.upload;

import alimama.com.unwetaologger.base.UNWLogger;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.tlog.protocol.OpCode;
import com.taobao.android.tlog.protocol.TLogSecret;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.reply.base.UploadTokenInfo;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import com.taobao.tao.log.CommandDataCenter;
import com.taobao.tao.log.TLog;
import com.taobao.tao.log.TLogConstant;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.TLogNative;
import com.taobao.tao.log.TLogUtils;
import com.taobao.tao.log.message.MessageInfo;
import com.taobao.tao.log.message.MessageReponse;
import com.taobao.tao.log.message.MessageSender;
import com.taobao.tao.log.monitor.TLogMonitor;
import com.taobao.tao.log.monitor.TLogStage;
import com.taobao.tao.log.task.ApplyUploadCompleteRequestTask;
import com.taobao.tao.log.task.ApplyUploadFileRequestTask;
import com.taobao.tao.log.task.LogUploadReplyTask;
import com.taobao.tao.log.utils.SimpleCrypto;
import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.extension.UCCore;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.android.agoo.common.AgooConstants;

public class LogFileUploadManager {
    private static final int RENAME_FILE = 4;
    private static String TAG = "TLog.LogFileUploadManager";
    private static final int UPLOAD_CANCEL = 3;
    private static final int UPLOAD_FINISH = 2;
    public boolean isForceUpload = false;
    private boolean isUploading = false;
    private Context mContext;
    private JSONObject mExtData;
    /* access modifiers changed from: private */
    public List<String> mFiles;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private HandlerThread mHandlerThread;
    public CommandInfo mParmas;
    public UploadTokenInfo[] tokenInfos;
    public String tokenType;
    public String uploadId;

    @Deprecated
    public void uploadWithFilePrefix(String str, String str2, String str3, FileUploadListener fileUploadListener) {
    }

    public LogFileUploadManager(Context context) {
        this.mContext = context.getApplicationContext();
        this.mFiles = new ArrayList();
    }

    private Map<String, String> getTokenParam(String str) {
        if (this.tokenInfos != null) {
            UploadTokenInfo[] uploadTokenInfoArr = this.tokenInfos;
            int length = uploadTokenInfoArr.length;
            int i = 0;
            while (i < length) {
                UploadTokenInfo uploadTokenInfo = uploadTokenInfoArr[i];
                if (uploadTokenInfo.fileInfo == null || !str.equals(uploadTokenInfo.fileInfo.absolutePath)) {
                    i++;
                } else {
                    HashMap hashMap = new HashMap();
                    for (Map.Entry entry : uploadTokenInfo.entrySet()) {
                        hashMap.put(entry.getKey(), entry.getValue());
                    }
                    return hashMap;
                }
            }
        }
        uploadFailed(str, "unknown", "1", "404", "tokenNotFound");
        return null;
    }

    private String getFileContentType(String str) {
        if (this.tokenInfos == null) {
            return null;
        }
        for (UploadTokenInfo uploadTokenInfo : this.tokenInfos) {
            if (uploadTokenInfo.fileInfo != null && str.equals(uploadTokenInfo.fileInfo.absolutePath)) {
                return uploadTokenInfo.fileInfo.contentType;
            }
        }
        return null;
    }

    public void startUpload() {
        if (this.mFiles.size() > 0) {
            if (this.mHandler == null || !this.mHandler.getLooper().getThread().isAlive()) {
                init();
            }
            TLogNative.appenderFlush(true);
            this.mHandler.sendEmptyMessage(2);
            return;
        }
        uploadFinish("There is not files to upload!", false, "3", "");
    }

    private void init() {
        this.mHandlerThread = new HandlerThread("tlog_uploadfiles", 19);
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 2:
                        LogFileUploadManager.this.uploadFinishHandler();
                        return;
                    case 3:
                        LogFileUploadManager.this.uploadCancelHandler();
                        return;
                    case 4:
                        LogFileUploadManager.this.mFiles.add((String) message.obj);
                        LogFileUploadManager.this.mHandler.sendEmptyMessage(2);
                        return;
                    default:
                        return;
                }
            }
        };
    }

    /* access modifiers changed from: private */
    public void uploadCancelHandler() {
        LogUploader logUploader = TLogInitializer.getInstance().getLogUploader();
        if (logUploader != null) {
            logUploader.cancel();
        } else {
            Log.w(TAG, "you need impl file uploader ");
        }
        TLog.logi(TLogConstant.MODEL, TAG, "Cancel : the mCurrentUploadFileInfo is null !");
        persistTask();
        finish("网络状态变更，不符合上传日志条件停止上传！", false, "5", "");
    }

    /* access modifiers changed from: private */
    public void uploadFinishHandler() {
        String str;
        String str2;
        String str3;
        String str4;
        if (!checkNetworkIsWifi()) {
            persistTask();
            finish("网路状态不符合上传条件！", false, "5", "");
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_LOG_UPLOAD, TAG, "文件上传：网路状态不符合上传条件,uploadId=" + this.uploadId + " isForceUpload:" + this.isForceUpload);
        } else if (this.mFiles.size() > 0) {
            String str5 = null;
            String str6 = null;
            while (this.mFiles.size() > 0) {
                str6 = this.mFiles.get(0);
                File file = new File(str6);
                if (file.exists() && file.length() != 0) {
                    break;
                }
                this.mFiles.remove(0);
                file.delete();
            }
            if (this.mFiles.size() != 0) {
                TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_LOG_UPLOAD_COUNT, "MSG LOG UPLOAD COUNT", "文件上传：上传文件");
                LogUploader logUploader = TLogInitializer.getInstance().getLogUploader();
                if (logUploader != null) {
                    TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_LOG_UPLOAD, TAG, "文件上传：校验通过，开始执行文件上传,uploadId=" + this.uploadId);
                    UploaderParam uploaderParam = new UploaderParam();
                    uploaderParam.build(this.mParmas);
                    uploaderParam.context = this.mContext;
                    uploaderParam.appVersion = TLogInitializer.getInstance().getAppVersion();
                    uploaderParam.logFilePathTmp = TLogInitializer.getInstance().getFileDir() + File.separator + TLogConstant.RUBBISH_DIR;
                    uploaderParam.params = getTokenParam(str6);
                    uploaderParam.fileContentType = getFileContentType(str6);
                    if (logUploader.getUploadInfo().type.equals(TLogConstant.TOKEN_TYPE_OSS)) {
                        if (uploaderParam.params != null) {
                            str5 = uploaderParam.params.get("ossObjectKey");
                            str3 = uploaderParam.params.get("ossEndpoint");
                        } else {
                            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_LOG_UPLOAD, TAG, "文件上传：oss->params is null, uploadId=" + this.uploadId);
                            str3 = null;
                        }
                        if (uploaderParam.params != null && !uploaderParam.params.containsKey(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY)) {
                            uploaderParam.params.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
                        }
                    } else if (logUploader.getUploadInfo().type.equals(TLogConstant.TOKEN_TYPE_ARUP)) {
                        if (uploaderParam.params != null) {
                            str5 = uploaderParam.params.get("ossObjectKey");
                            str4 = uploaderParam.params.get("ossEndpoint");
                        } else {
                            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_LOG_UPLOAD, TAG, "文件上传：arup->params is null, uploadId=" + this.uploadId);
                            str4 = null;
                        }
                        if (uploaderParam.params != null && !uploaderParam.params.containsKey(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY)) {
                            uploaderParam.params.put(TLogConstant.TOKEN_OSS_BUCKET_NAME_KEY, TLogInitializer.getInstance().ossBucketName);
                        }
                    } else {
                        if (!logUploader.getUploadInfo().type.equals(TLogConstant.TOKEN_TYPE_CEPH)) {
                            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_LOG_UPLOAD, TAG, "文件上传：not support this type:" + logUploader.getUploadInfo().type + ", uploadId=" + this.uploadId);
                        } else if (uploaderParam.params != null) {
                            str5 = uploaderParam.params.get("objectKey");
                            str3 = uploaderParam.params.get(ProtocolConst.KEY_ENDPOINT);
                        } else {
                            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_LOG_UPLOAD, TAG, "文件上传：ceph->params is null, uploadId=" + this.uploadId);
                        }
                        str2 = null;
                        str = null;
                        TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_LOG_UPLOAD, TAG, "文件上传：校验通过，调用上传通道,uploadId=" + this.uploadId);
                        logUploader.startUpload(uploaderParam, str6, new TLogUploadListener(str6, uploaderParam.fileContentType, str2, str));
                    }
                    str2 = str5;
                    str = str3;
                    TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_LOG_UPLOAD, TAG, "文件上传：校验通过，调用上传通道,uploadId=" + this.uploadId);
                    logUploader.startUpload(uploaderParam, str6, new TLogUploadListener(str6, uploaderParam.fileContentType, str2, str));
                } else {
                    Log.w(TAG, "you need impl file uploader ");
                    TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_LOG_UPLOAD, TAG, "文件上传：没有实现文件上传通道,uploadId=" + this.uploadId);
                }
                this.mFiles.remove(0);
                TLog.logi(TLogConstant.MODEL, TAG, "Current upload task has finished and to upload next -->  " + str6);
            }
        }
    }

    public void addFiles(List<String> list) {
        if (this.mFiles == null) {
            this.mFiles = new ArrayList();
        }
        if (list != null && list.size() > 0) {
            for (String next : list) {
                if (!this.mFiles.contains(next)) {
                    this.mFiles.add(next);
                }
            }
        }
    }

    public void addFile(String str) {
        if (this.mFiles == null) {
            this.mFiles = new ArrayList();
        }
        if (!TextUtils.isEmpty(str) && !this.mFiles.contains(str)) {
            this.mFiles.add(str);
        }
    }

    public int getUploadTaskCount() {
        if (this.mFiles != null) {
            return this.mFiles.size();
        }
        return 0;
    }

    private void finish(String str, boolean z, String str2, String str3) {
        if (this.mFiles != null) {
            this.mFiles.clear();
        }
        uploadFinish(str, z, str2, str3);
        if (!(!z || this.mHandlerThread == null || this.mHandlerThread.getLooper() == null)) {
            this.mHandlerThread.getLooper().quit();
        }
        String str4 = TAG;
        TLog.logi(TLogConstant.MODEL, str4, str + " and quit the handlerThread!");
        this.isForceUpload = false;
        TLogUtils.cleanDir(new File(TLogInitializer.getInstance().getFileDir() + File.separator + TLogConstant.RUBBISH_DIR));
    }

    private void persistTask() {
        String str = TAG;
        TLog.logi(TLogConstant.MODEL, str, "[persistTask] there is " + this.mFiles.size() + " task!");
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(TLogInitializer.getInstance().getContext()).edit();
        HashSet hashSet = new HashSet();
        int size = this.mFiles.size();
        if (size >= 1) {
            for (int i = 0; i < size; i++) {
                hashSet.add(this.mFiles.get(i));
            }
            edit.putStringSet(TLogConstant.PERSIST_UPLOAD_FILES, hashSet);
            edit.putString("userId", this.mParmas.userId);
            edit.putString("serviceId", this.mParmas.serviceId);
            edit.putString(TLogConstant.PERSIST_SERIAL_NUMBER, this.mParmas.sessionId + "");
            if (this.mExtData != null) {
                edit.putString(TLogConstant.PERSIST_EXTDATA, this.mExtData.toJSONString());
            }
            edit.commit();
        }
    }

    private void remotePersistTask() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(TLogInitializer.getInstance().getContext());
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.remove(TLogConstant.PERSIST_UPLOAD_FILES);
        edit.remove("userId");
        edit.remove("serviceId");
        edit.remove(TLogConstant.PERSIST_SERIAL_NUMBER);
        edit.remove(TLogConstant.PERSIST_TASK_ID);
        edit.remove(TLogConstant.PERSIST_UPLOAD_TYPE);
        if (defaultSharedPreferences.contains(TLogConstant.PERSIST_EXTDATA)) {
            edit.remove(TLogConstant.PERSIST_EXTDATA);
        }
        edit.commit();
    }

    private boolean checkNetworkIsWifi() {
        if (this.isForceUpload) {
            return true;
        }
        return TLogUtils.checkNetworkIsWifi(this.mContext);
    }

    public boolean isUploading() {
        return this.isUploading;
    }

    public void flushFinish(boolean z) {
        if (this.mHandler != null && this.mHandler.getLooper().getThread().isAlive()) {
            this.mHandler.sendEmptyMessage(2);
            this.isUploading = true;
        }
    }

    public void cancelUploadTask() {
        if (this.isUploading) {
            if (this.mHandler.getLooper().getThread().isAlive()) {
                this.mHandler.sendEmptyMessage(3);
                TLog.logi(TLogConstant.MODEL, TAG, " The thread isAlive!");
            }
            TLog.logi(TLogConstant.MODEL, TAG, "The ConnectionChangeReceiver to sendEmptyMessage(UPLOAD_CANCEL)");
        }
    }

    public void renameFileNotification(boolean z, String str) {
        if (this.mHandler != null && this.mHandler.getLooper().getThread().isAlive()) {
            Message obtain = Message.obtain();
            obtain.what = 4;
            obtain.obj = str;
            this.mHandler.sendMessage(obtain);
        }
    }

    private String getPrefixName(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str.substring(0, str.indexOf("_"));
        }
        return null;
    }

    public void uploadFailed(String str, String str2, String str3, String str4, String str5) {
        FileUploadListener popListener;
        FileUploadListener popListener2;
        StringBuilder sb = new StringBuilder();
        sb.append("文件上传：文件上传失败,uploadId=");
        sb.append(this.uploadId == null ? "-" : this.uploadId);
        sb.append(" file path=");
        sb.append(str == null ? "-" : str);
        sb.append(" error info=");
        sb.append(str5 == null ? "-" : str5);
        TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_LOG_UPLOAD, TAG, sb.toString());
        if (this.mParmas.opCode.equals(OpCode.APPLY_UPLOAD_TOKEN_REPLY)) {
            LogUploadReplyTask.executeFailure(this.mParmas, this.uploadId, str, str3, str4, str5, str2);
            if (!(this.uploadId == null || (popListener2 = UploadQueue.getInstance().popListener(this.uploadId)) == null)) {
                popListener2.onError(str3, str5, str4);
            }
        } else {
            ApplyUploadCompleteRequestTask.executeFailure(this.mParmas, this.uploadId, str, str3, str4, str5, str2);
            if (!(this.mParmas.requestId == null || (popListener = UploadQueue.getInstance().popListener(this.mParmas.requestId)) == null)) {
                popListener.onError(str3, str5, str4);
            }
        }
        TLog.logi(TLogConstant.MODEL, TAG, " upload remote file failure!");
        this.isForceUpload = false;
        TLogUtils.cleanDir(new File(TLogInitializer.getInstance().getFileDir() + File.separator + TLogConstant.RUBBISH_DIR));
    }

    public void uploadSuccess(String str, String str2, String str3, String str4, String str5) {
        this.uploadId = this.uploadId == null ? "-" : this.uploadId;
        if (str == null) {
            str = "-";
        }
        TLogMonitor tLogMonitor = TLogInitializer.getInstance().gettLogMonitor();
        String str6 = TLogStage.MSG_LOG_UPLOAD;
        String str7 = TAG;
        tLogMonitor.stageInfo(str6, str7, "文件上传：文件上传成功,uploadId=" + this.uploadId + " file path=" + str);
        StringBuilder sb = new StringBuilder();
        sb.append("LogFileUploadManager uploadSuccess... opCode = ");
        sb.append(this.mParmas.opCode);
        Log.e("xxxxxx", sb.toString());
        boolean z = true;
        if (this.mParmas.opCode.equals(OpCode.APPLY_UPLOAD_TOKEN_REPLY)) {
            LogUploadReplyTask.executeSuccess(this.mParmas, this.uploadId, str, str3, str2, str4, str5);
            if (this.uploadId != null) {
                FileUploadListener popListener = UploadQueue.getInstance().popListener(this.uploadId);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("find listener by uploadId, find is null = ");
                if (popListener != null) {
                    z = false;
                }
                sb2.append(z);
                Log.e("xxxxxx", sb2.toString());
                if (popListener != null) {
                    if (popListener instanceof OSSUploadListener) {
                        OSSUploadListener oSSUploadListener = (OSSUploadListener) popListener;
                        oSSUploadListener.ossEndpoint = str5;
                        oSSUploadListener.ossObjectKey = str4;
                    }
                    popListener.onSucessed(str, str3);
                }
            }
        } else {
            ApplyUploadCompleteRequestTask.executeSuccess(this.mParmas, this.uploadId, str, str3, str2, str4, str5);
            if (this.mParmas.requestId != null) {
                FileUploadListener popListener2 = UploadQueue.getInstance().popListener(this.mParmas.requestId);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("find listener by requestId, find is null = ");
                if (popListener2 != null) {
                    z = false;
                }
                sb3.append(z);
                Log.e("xxxxxx", sb3.toString());
                if (popListener2 != null) {
                    if (popListener2 instanceof OSSUploadListener) {
                        OSSUploadListener oSSUploadListener2 = (OSSUploadListener) popListener2;
                        oSSUploadListener2.ossEndpoint = str5;
                        oSSUploadListener2.ossObjectKey = str4;
                    }
                    popListener2.onSucessed(str, str3);
                }
            }
        }
        TLog.logi(TLogConstant.MODEL, TAG, " upload remote file success!");
        this.isForceUpload = false;
        TLogUtils.cleanDir(new File(TLogInitializer.getInstance().getFileDir() + File.separator + TLogConstant.RUBBISH_DIR));
    }

    public void uploadFinish(String str, boolean z, String str2, String str3) {
        if (str != null) {
            if (!z) {
                boolean isEmpty = TextUtils.isEmpty(str3);
            }
            this.mExtData = null;
        }
        synchronized (this) {
            this.isUploading = false;
        }
    }

    public class TLogUploadListener extends OSSUploadListener {
        public TLogUploadListener(String str, String str2, String str3, String str4) {
            super(str, str2, str3, str4);
        }

        public void onError(String str, String str2, String str3) {
            LogFileUploadManager.this.uploadFailed(this.fileName, this.contentType, str, str2, str3);
            if (LogFileUploadManager.this.mHandler.getLooper().getThread().isAlive()) {
                TLogMonitor tLogMonitor = TLogInitializer.getInstance().gettLogMonitor();
                String str4 = TLogStage.MSG_LOG_UPLOAD_COUNT;
                tLogMonitor.stageInfo(str4, "MSG LOG UPLOAD", "文件上传失败了：检测是否还有文件可上传  是否开启强制上传：" + LogFileUploadManager.this.isForceUpload);
                LogFileUploadManager.this.mHandler.sendEmptyMessage(2);
            }
        }

        public void onSucessed(String str, String str2) {
            LogFileUploadManager.this.uploadSuccess(str, this.contentType, str2, this.ossObjectKey, this.ossEndpoint);
            if (LogFileUploadManager.this.mHandler.getLooper().getThread().isAlive()) {
                LogFileUploadManager.this.isForceUpload = true;
                TLogMonitor tLogMonitor = TLogInitializer.getInstance().gettLogMonitor();
                String str3 = TLogStage.MSG_LOG_UPLOAD_COUNT;
                tLogMonitor.stageInfo(str3, "MSG LOG UPLOAD", "文件上传成功了：检测是否还有文件可上传  是否开启强制上传：" + LogFileUploadManager.this.isForceUpload);
                LogFileUploadManager.this.mHandler.sendEmptyMessage(2);
            }
        }
    }

    public void uploadWithFilePrefix(String str, String str2, Map<String, String> map, FileUploadListener fileUploadListener) {
        if (str2 == null) {
            Log.e(TAG, "you need set bizCode");
        } else if (str == null) {
            Log.e(TAG, "you need set bizType");
        } else {
            preFixUpload(str, str2, map, fileUploadListener);
        }
    }

    public void uploadWithFilePath(String str, String str2, String str3, String str4, Map<String, String> map, FileUploadListener fileUploadListener) {
        if (str == null) {
            Log.e(TAG, "you need set file path");
        } else if (str2 == null) {
            Log.e(TAG, "you need set debugType");
        } else if (str4 == null) {
            Log.e(TAG, "you need set bizCode");
        } else if (str3 == null) {
            Log.e(TAG, "you need set bizType");
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            filePathUpload(arrayList, str2, str3, str4, map, fileUploadListener);
        }
    }

    private void preFixUpload(String str, String str2, Map<String, String> map, FileUploadListener fileUploadListener) {
        List<String> filePath = TLogUtils.getFilePath(TLogInitializer.getInstance().getNameprefix(), 0, TLogUtils.getDays(1));
        if (filePath == null || filePath.isEmpty()) {
            Log.e(TAG, "uploadFile failure, file path is empty");
        } else {
            filePathUpload(filePath, "tlog", str, str2, map, fileUploadListener);
        }
    }

    private void filePathUpload(List<String> list, String str, String str2, String str3, Map<String, String> map, FileUploadListener fileUploadListener) {
        if (UCCore.EVENT_EXCEPTION.equalsIgnoreCase(str2) || TextUtils.isEmpty(str)) {
            String str4 = TAG;
            Log.w(str4, "unSupport type :" + str + Operators.SPACE_STR + str2);
            return;
        }
        TLogMonitor tLogMonitor = TLogInitializer.getInstance().gettLogMonitor();
        String str5 = TLogStage.MSG_LOG_UPLOAD;
        String str6 = TAG;
        tLogMonitor.stageInfo(str5, str6, "文件上传：触发主动上传文件，" + str2);
        ApplyUploadFileRequestTask.execute(list, str, str2, str3, map, fileUploadListener);
    }

    @Deprecated
    public void uploadWithFilePrefix(String str, String str2) {
        if (str == null) {
            str = TLogInitializer.getInstance().getNameprefix();
        }
        List<String> filePath = TLogUtils.getFilePath(str, 0, TLogUtils.getDays(1));
        if (filePath == null || filePath.isEmpty()) {
            Log.e(TAG, "uploadFile: ,");
        } else {
            uploadWithFilePath(filePath.get(0), str2);
        }
    }

    @Deprecated
    public void uploadWithFilePath(String str, String str2) {
        if (UCCore.EVENT_EXCEPTION.equalsIgnoreCase(str2) || TextUtils.isEmpty(str2)) {
            String str3 = TAG;
            Log.w(str3, "unSupport type :" + str2);
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("version", (Object) "1.0");
            jSONObject.put("appkey", (Object) TLogInitializer.getInstance().getAppkey());
            jSONObject.put("appVersion", (Object) TLogInitializer.getInstance().getAppVersion());
            jSONObject.put("command", (Object) 65524);
            JSONArray jSONArray = new JSONArray();
            JSONObject jSONObject2 = new JSONObject();
            jSONArray.add(jSONObject2);
            jSONObject2.put("filePath", (Object) str);
            jSONObject2.put("bizType", (Object) str2);
            jSONObject2.put("type", (Object) "client");
            jSONObject2.put(AgooConstants.MESSAGE_EXT, (Object) new JSONObject());
            jSONObject.put("data", (Object) jSONArray);
            jSONObject.put(UNWLogger.LOG_VALUE_TYPE_USER, (Object) TLogInitializer.getInstance().getUserNick());
            jSONObject.put("success", (Object) true);
            jSONObject.put("deviceId", (Object) TLogInitializer.getUTDID());
            String encrypt = SimpleCrypto.encrypt(jSONObject.toString(), TLogInitializer.getInstance().getSecurityKey());
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.context = this.mContext;
            messageInfo.content = encrypt;
            messageInfo.appKey = TLogInitializer.getInstance().getAppkey();
            messageInfo.ttid = TLogInitializer.getInstance().getTtid();
            messageInfo.deviceId = TLogInitializer.getUTDID();
            messageInfo.publicKeyDigest = TLogSecret.getInstance().getRsaMd5Value();
            MessageSender messageSender = TLogInitializer.getInstance().getMessageSender();
            if (messageSender != null) {
                MessageReponse sendMsg = messageSender.sendMsg(messageInfo);
                if (sendMsg == null || sendMsg.result == null) {
                    Log.e(TAG, "send request message error,result is null ");
                    return;
                }
                CommandDataCenter.getInstance().onData(sendMsg.serviceId, sendMsg.userId, sendMsg.dataId, sendMsg.result.getBytes());
                return;
            }
            Log.e(TAG, "send request message error,you neee impl message sender ");
        } catch (Exception e) {
            Log.e(TAG, "send request message error ", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_LOG_UPLOAD, TAG, (Throwable) e);
        }
    }
}
