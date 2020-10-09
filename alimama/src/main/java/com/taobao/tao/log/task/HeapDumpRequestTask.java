package com.taobao.tao.log.task;

import android.util.Log;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.android.tlog.protocol.model.GodeyeInfo;
import com.taobao.android.tlog.protocol.model.request.HeapDumpRequest;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.godeye.GodeyeInitializer;
import com.taobao.tao.log.monitor.TLogStage;

public class HeapDumpRequestTask implements ICommandTask {
    private String TAG = "TLOG.HeapDumpRequestTask";

    public ICommandTask execute(CommandInfo commandInfo) {
        try {
            TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_HANDLE, this.TAG, "消息处理：堆栈dump请求消息");
            HeapDumpRequest heapDumpRequest = new HeapDumpRequest();
            heapDumpRequest.parse(commandInfo.data, commandInfo);
            GodeyeInfo godeyeInfo = new GodeyeInfo();
            godeyeInfo.commandInfo = commandInfo;
            godeyeInfo.uploadId = heapDumpRequest.uploadId;
            godeyeInfo.threshold = Double.valueOf((double) heapDumpRequest.heapSizeThreshold.intValue());
            GodeyeInitializer.getInstance().handleRemoteCommand(godeyeInfo);
        } catch (Exception e) {
            Log.e(this.TAG, "execute error", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, this.TAG, (Throwable) e);
        }
        return this;
    }
}
