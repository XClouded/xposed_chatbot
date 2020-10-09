package com.taobao.tao.log.task;

import android.util.Log;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.reply.ApplyTokenReply;
import com.taobao.android.tlog.protocol.model.reply.base.UploadTokenInfo;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.monitor.TLogStage;

public class ApplyTokenReplyTask implements ICommandTask {
    private String TAG = "TLOG.ApplyTokenReplyTask";

    public ICommandTask execute(CommandInfo commandInfo) {
        try {
            TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_HANDLE, this.TAG, "消息处理：申请token回复消息");
            ApplyTokenReply applyTokenReply = new ApplyTokenReply();
            applyTokenReply.parse(commandInfo.data, commandInfo);
            String str = applyTokenReply.uploadId;
            UploadTokenInfo[] uploadTokenInfoArr = applyTokenReply.tokenInfos;
            if (uploadTokenInfoArr != null && uploadTokenInfoArr.length > 0) {
                UploadFileTask.taskExecute(commandInfo, str, applyTokenReply.tokenType, applyTokenReply.tokenInfos);
            }
        } catch (Exception e) {
            Log.e(this.TAG, "execute error", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, this.TAG, (Throwable) e);
        }
        return this;
    }
}
