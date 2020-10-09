package com.taobao.tao.log;

import android.util.Log;
import com.taobao.android.tlog.protocol.TLogReply;
import com.taobao.tao.log.monitor.TLogStage;
import com.taobao.tao.log.task.CommandManager;

public class CommandDataCenter {
    private static final String TAG = "TLOG.CommandDataCenter";

    private CommandDataCenter() {
    }

    private static class CreateInstance {
        /* access modifiers changed from: private */
        public static CommandDataCenter instance = new CommandDataCenter();

        private CreateInstance() {
        }
    }

    public static synchronized CommandDataCenter getInstance() {
        CommandDataCenter access$100;
        synchronized (CommandDataCenter.class) {
            access$100 = CreateInstance.instance;
        }
        return access$100;
    }

    public void onData(String str, String str2, String str3, byte[] bArr) {
        String str4;
        TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_REVEIVE_COUNT, "RECEIVE MESSAGE COUNT", "成功接收到消息，还未开始处理");
        if (bArr == null) {
            Log.e(TAG, "msg is null");
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_REVEIVE, "NULL MESSAGE", "接收到的服务端消息为空");
            return;
        }
        try {
            str4 = TLogReply.getInstance().parseContent(str, str2, str3, bArr);
        } catch (Exception e) {
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_REVEIVE, "PARSE MESSAGE ERROR", (Throwable) e);
            str4 = null;
        }
        TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_REVEIVE, "RECEIVE MESSAGE", "成功接收到消息");
        Log.i(TAG, "CommandDataCenter.onData : " + str4);
        CommandManager.getInstance().dealCommandData(bArr, str4, str2, str);
    }
}
