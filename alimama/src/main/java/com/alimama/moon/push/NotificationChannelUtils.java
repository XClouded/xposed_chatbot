package com.alimama.moon.push;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationChannelUtils {
    public static final String CHANNEL_ID = "union_notification_subscribe";
    public static final String CHANNEL_NAME = "订阅消息";

    public static void createNotificationChannel(Context context, String str, String str2) {
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                ((NotificationManager) context.getSystemService("notification")).createNotificationChannel(new NotificationChannel(str, str2, 4));
            } catch (Exception unused) {
            }
        }
    }
}
