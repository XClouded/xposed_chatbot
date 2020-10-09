package com.taobao.tao.log.task;

import android.util.Log;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.reply.ApplyUploadReply;
import com.taobao.android.tlog.protocol.model.reply.base.UploadTokenInfo;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.monitor.TLogStage;

public class ApplyUploadFileReplyTask implements ICommandTask {
    private String TAG = "TLOG.ApplyUploadFileReplyTask";

    public ICommandTask execute(CommandInfo commandInfo) {
        try {
            TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_HANDLE, this.TAG, "消息处理：请求文件上传服务端回复消息");
            ApplyUploadReply applyUploadReply = new ApplyUploadReply();
            applyUploadReply.parse(commandInfo.data, commandInfo);
            String str = applyUploadReply.uploadId;
            UploadTokenInfo[] uploadTokenInfoArr = applyUploadReply.tokenInfos;
            if (uploadTokenInfoArr != null && uploadTokenInfoArr.length > 0) {
                UploadFileTask.taskExecute(commandInfo, str, applyUploadReply.tokenType, uploadTokenInfoArr);
            }
        } catch (Exception e) {
            Log.e(this.TAG, "execute error");
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, this.TAG, (Throwable) e);
        }
        return this;
    }
}
