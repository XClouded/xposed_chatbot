package com.taobao.android.tlog.message;

import android.content.Context;
import android.util.Log;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.common.Constants;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.message.MessageInfo;
import com.taobao.tao.log.message.MessageReponse;
import com.taobao.tao.log.message.MessageSender;
import com.taobao.tao.log.message.SenderInfo;
import com.taobao.tao.log.monitor.TLogStage;
import mtopsdk.mtop.upload.domain.UploadConstants;

public class TLogTaobaoMessage implements MessageSender {
    private static final String TAG = "tlogMessage";

    public MessageReponse pullMsg(MessageInfo messageInfo) {
        return null;
    }

    public void init(MessageInfo messageInfo) {
        try {
            String str = messageInfo.appKey;
            String str2 = messageInfo.accsServiceId;
            String str3 = messageInfo.accsTag;
            Context context = messageInfo.context;
            if (str3 == null || str3.length() <= 0) {
                AccsClientConfig.getConfig(str).getTag();
            }
            ACCSManager.registerDataListener(context, str2, new TLogCommandDataCenter());
            TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_SEND, "MSG INIT", "初始化消息通道");
            TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_SEND, "MSG INIT", "初始化消息通道成功");
        } catch (Exception e) {
            Log.e(TAG, "registerDataListener failure : ", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_SEND, "MSG INIT", (Throwable) e);
        }
    }

    public MessageReponse sendStartUp(MessageInfo messageInfo) {
        return sendMsg(messageInfo);
    }

    public MessageReponse sendMsg(MessageInfo messageInfo) {
        Context context = messageInfo.context;
        String str = messageInfo.appKey;
        String str2 = messageInfo.content;
        String str3 = messageInfo.accsServiceId;
        String str4 = messageInfo.accsTag;
        if (str4 == null || str4.length() <= 0) {
            AccsClientConfig.getConfig(str).getTag();
        }
        try {
            ACCSManager.sendRequest(context, UploadConstants.USERID, str3, str2.getBytes(), (String) null);
            MessageReponse messageReponse = new MessageReponse();
            messageReponse.result = null;
            messageReponse.dataId = Constants.KEY_DATA_ID;
            messageReponse.serviceId = str3;
            messageReponse.userId = "userId";
            TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_SEND, "SEND MSG", "异步发送消息完成");
            return messageReponse;
        } catch (Exception e) {
            Log.e(TAG, "send accs message failure : ", e);
            TLogInitializer.getInstance().gettLogMonitor().stageError(TLogStage.MSG_SEND, "SEND MSG", (Throwable) e);
            return null;
        }
    }

    public SenderInfo getSenderInfo() {
        SenderInfo senderInfo = new SenderInfo();
        senderInfo.type = "accs";
        return senderInfo;
    }
}
