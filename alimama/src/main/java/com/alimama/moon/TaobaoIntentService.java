package com.alimama.moon;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.model.PushMsg;
import com.alimama.moon.model.PushMsgExts;
import com.alimama.moon.push.AccsPushRedirectActivity;
import com.alimama.moon.push.NotificationChannelUtils;
import com.alimama.union.app.logger.NewMonitorLogger;
import com.taobao.agoo.TaobaoBaseIntentService;
import org.android.agoo.common.AgooConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaobaoIntentService extends TaobaoBaseIntentService {
    public static final String TAG = "TaobaoIntentService";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) TaobaoIntentService.class);
    private static int sNotificationId;

    /* access modifiers changed from: protected */
    public void onMessage(Context context, Intent intent) {
        try {
            logger.info("onMessage: {}", (Object) intent.getExtras());
            String stringExtra = intent.getStringExtra("body");
            String stringExtra2 = intent.getStringExtra("id");
            String stringExtra3 = intent.getStringExtra(AgooConstants.MESSAGE_TASK_ID);
            String stringExtra4 = intent.getStringExtra(AgooConstants.MESSAGE_EXT);
            PushMsg pushMsg = (PushMsg) JSON.parseObject(stringExtra, PushMsg.class);
            pushMsg.setMessageId(stringExtra2);
            pushMsg.setTaskId(stringExtra3);
            pushMsg.setExtData(stringExtra4);
            fireNotification(context, pushMsg);
        } catch (Exception e) {
            NewMonitorLogger.Agoo.pushCrash(TAG, e.toString());
        }
    }

    /* access modifiers changed from: protected */
    public void onError(Context context, String str) {
        logger.error("onError-");
        NewMonitorLogger.Agoo.intentServiceError(TAG, str);
    }

    /* access modifiers changed from: protected */
    public void onRegistered(Context context, String str) {
        logger.info("onRegistered-");
    }

    /* access modifiers changed from: protected */
    public void onUnregistered(Context context, String str) {
        logger.info("onUnregistered");
    }

    private void fireNotification(Context context, PushMsg pushMsg) {
        PushMsgExts exts = pushMsg.getExts();
        if (exts == null) {
            NewMonitorLogger.Agoo.pushNotShow(TAG, pushMsg.toString());
            return;
        }
        String url = exts.getUrl();
        Intent intent = new Intent(context, AccsPushRedirectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", pushMsg.getTitle());
        bundle.putString("messageId", pushMsg.getMessageId());
        bundle.putString(AgooConstants.MESSAGE_EXT, pushMsg.getExtData());
        intent.putExtras(bundle);
        ((NotificationManager) context.getSystemService("notification")).notify(sNotificationId, new NotificationCompat.Builder(context, NotificationChannelUtils.CHANNEL_ID).setSmallIcon(R.drawable.ic_logo).setContentTitle(pushMsg.getTitle()).setContentText(pushMsg.getText()).setContentIntent(PendingIntent.getActivity(context, 200, intent, 134217728)).setWhen(System.currentTimeMillis()).setAutoCancel(true).build());
        sNotificationId++;
    }
}
