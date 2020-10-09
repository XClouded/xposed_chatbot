package com.coloros.mcssdk.a;

import android.content.Context;
import android.content.Intent;
import com.coloros.mcssdk.PushManager;
import com.coloros.mcssdk.c.b;
import com.coloros.mcssdk.c.d;
import com.coloros.mcssdk.mode.Message;
import com.coloros.mcssdk.mode.SptDataMessage;

public final class e extends c {
    public final Message a(Context context, int i, Intent intent) {
        if (4103 != i) {
            return null;
        }
        Message a = a(intent);
        PushManager.statisticMessage(context, (SptDataMessage) a, PushManager.EVENT_ID_PUSH_TRANSMIT);
        return a;
    }

    public final Message a(Intent intent) {
        try {
            SptDataMessage sptDataMessage = new SptDataMessage();
            sptDataMessage.setMessageID(Integer.parseInt(b.a(intent.getStringExtra("messageID"))));
            sptDataMessage.setTaskID(b.a(intent.getStringExtra("taskID")));
            sptDataMessage.setAppPackage(b.a(intent.getStringExtra("appPackage")));
            sptDataMessage.setContent(b.a(intent.getStringExtra("content")));
            sptDataMessage.setDescription(b.a(intent.getStringExtra(Message.DESCRIPTION)));
            sptDataMessage.setAppID(b.a(intent.getStringExtra(Message.APP_ID)));
            sptDataMessage.setGlobalID(b.a(intent.getStringExtra(Message.GLOBAL_ID)));
            return sptDataMessage;
        } catch (Exception e) {
            d.a("OnHandleIntent--" + e.getMessage());
            return null;
        }
    }
}
