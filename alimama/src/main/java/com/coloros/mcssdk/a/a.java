package com.coloros.mcssdk.a;

import android.content.Context;
import android.content.Intent;
import com.coloros.mcssdk.PushManager;
import com.coloros.mcssdk.c.b;
import com.coloros.mcssdk.c.d;
import com.coloros.mcssdk.mode.AppMessage;
import com.coloros.mcssdk.mode.Message;

public final class a extends c {
    public final Message a(Context context, int i, Intent intent) {
        if (4098 != i) {
            return null;
        }
        Message a = a(intent);
        PushManager.statisticMessage(context, (AppMessage) a, PushManager.EVENT_ID_PUSH_TRANSMIT);
        return a;
    }

    public final Message a(Intent intent) {
        try {
            AppMessage appMessage = new AppMessage();
            appMessage.setMessageID(Integer.parseInt(b.a(intent.getStringExtra("messageID"))));
            appMessage.setTaskID(b.a(intent.getStringExtra("taskID")));
            appMessage.setAppPackage(b.a(intent.getStringExtra("appPackage")));
            appMessage.setContent(b.a(intent.getStringExtra("content")));
            appMessage.setBalanceTime(Integer.parseInt(b.a(intent.getStringExtra(Message.BALANCE_TIME))));
            appMessage.setStartDate(Long.parseLong(b.a(intent.getStringExtra(Message.START_DATE))));
            appMessage.setEndDate(Long.parseLong(b.a(intent.getStringExtra(Message.END_DATE))));
            appMessage.setTimeRanges(b.a(intent.getStringExtra(Message.TIME_RANGES)));
            appMessage.setTitle(b.a(intent.getStringExtra("title")));
            appMessage.setRule(b.a(intent.getStringExtra(Message.RULE)));
            appMessage.setForcedDelivery(Integer.parseInt(b.a(intent.getStringExtra(Message.FORCED_DELIVERY))));
            appMessage.setDistinctBycontent(Integer.parseInt(b.a(intent.getStringExtra(Message.DISTINCT_CONTENT))));
            d.a("OnHandleIntent-message:" + appMessage.toString());
            return appMessage;
        } catch (Exception e) {
            d.a("OnHandleIntent--" + e.getMessage());
            return null;
        }
    }
}
