package com.taobao.tao.log.task;

import android.text.TextUtils;
import android.util.Log;
import com.taobao.android.tlog.protocol.OpCode;
import com.taobao.android.tlog.protocol.TLogReply;
import com.taobao.android.tlog.protocol.model.CommandInfo;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.monitor.TLogMonitor;
import com.taobao.tao.log.monitor.TLogStage;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
    private String TAG;
    private ConcurrentHashMap<String, ICommandTask> commandTasks;

    private CommandManager() {
        this.TAG = "TLOG.CommandManager";
        this.commandTasks = new ConcurrentHashMap<>();
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final CommandManager INSTANCE = new CommandManager();

        private SingletonHolder() {
        }
    }

    public static final CommandManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() {
        addCommandTaskListener(OpCode.APPLY_UPLOAD_TOKEN_REPLY, new ApplyTokenReplyTask());
        addCommandTaskListener(OpCode.APPLY_UPLOAD_REPLY, new ApplyUploadFileReplyTask());
        addCommandTaskListener(OpCode.LOG_UPLOAD, new LogUploadRequestTask());
        addCommandTaskListener(OpCode.LOG_CONFIGURE, new LogConfigRequestTask());
        addCommandTaskListener(OpCode.METHOD_TRACE_DUMP, new MethodTraceRequestTask());
        addCommandTaskListener(OpCode.HEAP_DUMP, new HeapDumpRequestTask());
        addCommandTaskListener(OpCode.USER_DEFINED_UPLOAD, new UDFUploadRequestTask());
    }

    public void dealCommandData(byte[] bArr, String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str)) {
            try {
                CommandInfo parseCommandInfo = TLogReply.getInstance().parseCommandInfo(bArr, str, str2, str3);
                if (parseCommandInfo != null) {
                    TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_REVEIVE, "RECEIVE MESSAGE", "接收消息后，基础信息解析完成");
                    if (parseCommandInfo.msgType.equals("NOTIFY")) {
                        TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_REVEIVE, "RECEIVE MESSAGE", "接收到notify消息，开始拉任务");
                        PullTask.getInstance().pull();
                    }
                    ICommandTask iCommandTask = this.commandTasks.get(parseCommandInfo.opCode);
                    if (iCommandTask != null) {
                        TLogMonitor tLogMonitor = TLogInitializer.getInstance().gettLogMonitor();
                        String str4 = TLogStage.MSG_REVEIVE;
                        tLogMonitor.stageInfo(str4, "RECEIVE MESSAGE", "开始处理任务，opcode=" + parseCommandInfo.opCode);
                        iCommandTask.execute(parseCommandInfo);
                        return;
                    }
                    TLogMonitor tLogMonitor2 = TLogInitializer.getInstance().gettLogMonitor();
                    String str5 = TLogStage.MSG_REVEIVE;
                    tLogMonitor2.stageInfo(str5, "RECEIVE MESSAGE", "没有对应的任务存在，opcode=" + parseCommandInfo.opCode);
                }
            } catch (Exception e) {
                Log.e(this.TAG, "parse command info error", e);
                TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_HANDLE, this.TAG, (Throwable) e);
            }
        }
    }

    public void addCommandTaskListener(String str, ICommandTask iCommandTask) {
        this.commandTasks.put(str, iCommandTask);
    }
}
