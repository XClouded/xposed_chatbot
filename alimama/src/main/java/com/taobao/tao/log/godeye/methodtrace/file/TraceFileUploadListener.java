package com.taobao.tao.log.godeye.methodtrace.file;

import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.tao.log.godeye.api.command.ResponseData;
import com.taobao.tao.log.godeye.api.command.TraceProgress;
import com.taobao.tao.log.godeye.api.command.TraceTask;
import com.taobao.tao.log.godeye.methodtrace.MethodTraceController;
import com.taobao.tao.log.godeye.methodtrace.MethodTraceInitializer;
import com.taobao.tao.log.upload.OSSUploadListener;
import java.io.File;

public class TraceFileUploadListener extends OSSUploadListener {
    private final MethodTraceController mMethodTraceController;
    private final TraceTask mTraceTask;

    public TraceFileUploadListener(MethodTraceController methodTraceController, TraceTask traceTask) {
        this.mTraceTask = traceTask;
        this.mMethodTraceController = methodTraceController;
    }

    public void onError(String str, String str2, String str3) {
        Log.e("xxxxxx", "TraceFileUploadListener onError...");
        if (this.mTraceTask != null) {
            this.mTraceTask.progress = TraceProgress.EXCEPTION_ON_UPLOAD.name();
            this.mMethodTraceController.saveTaskRunningStatus(this.mTraceTask);
            MethodTraceInitializer.sGodeye.response(this.mMethodTraceController, new ResponseData(6, str3, (JSONObject) null));
        }
    }

    public void onSucessed(String str, String str2) {
        if (this.mTraceTask != null) {
            this.mMethodTraceController.saveTaskRunningStatus(this.mTraceTask);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("fileName", (Object) str);
            jSONObject.put(Constants.KEY_FILE_URL, (Object) str2);
            ResponseData responseData = new ResponseData(5, "file-upload-success", jSONObject);
            responseData.tokenData = new JSONObject();
            responseData.tokenData.put("ossObjectKey", (Object) this.ossObjectKey);
            responseData.tokenData.put("ossEndpoint", (Object) this.ossEndpoint);
            MethodTraceInitializer.sGodeye.response(this.mMethodTraceController, responseData);
            MethodTraceInitializer.sGodeye.defaultCommandManager().removeLocalCommand(this.mMethodTraceController);
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
