package com.taobao.accs.utl;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.pm.ApplicationInfo;
import android.media.AudioAttributes;
import android.net.Uri;
import android.service.notification.StatusBarNotification;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class KeepAliveManager {
    /* access modifiers changed from: private */
    public static final String TAG = "KeepAliveManager";
    private static ApplicationInfo applicationInfo = null;
    /* access modifiers changed from: private */
    public static int checkTime = 0;
    private static final String id = "accs_agoo_normal_channel";
    /* access modifiers changed from: private */
    public static NotificationManager notificationManager;
    /* access modifiers changed from: private */
    public static ScheduledFuture scheduledFuture;

    static /* synthetic */ int access$108() {
        int i = checkTime;
        checkTime = i + 1;
        return i;
    }

    @TargetApi(26)
    public static void foreground(Service service) {
        try {
            checkTime = 0;
            if (notificationManager == null) {
                notificationManager = (NotificationManager) service.getApplicationContext().getSystemService("notification");
            }
            if (applicationInfo == null) {
                applicationInfo = service.getPackageManager().getApplicationInfo(service.getPackageName(), 0);
            }
            final NotificationChannel notificationChannel = new NotificationChannel(id, id, 0);
            notificationChannel.setSound((Uri) null, (AudioAttributes) null);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
            Notification.Builder builder = new Notification.Builder(service, notificationChannel.getId());
            if (applicationInfo != null) {
                builder.setSmallIcon(applicationInfo.icon);
                builder.setContentText("手机淘宝正在运行…");
            }
            service.startForeground(1, builder.build());
            scheduledFuture = ThreadPoolExecutorFactory.getScheduledExecutor().scheduleAtFixedRate(new Runnable() {
                public void run() {
                    ALog.i(KeepAliveManager.TAG, "scan foreground notification times: ", Integer.valueOf(KeepAliveManager.checkTime));
                    if (KeepAliveManager.access$108() == 15) {
                        ALog.i(KeepAliveManager.TAG, "schedule 1.5s, but not find foreground notification, maybe it's already foreground", new Object[0]);
                        KeepAliveManager.scheduledFuture.cancel(false);
                        KeepAliveManager.notificationManager.deleteNotificationChannel(notificationChannel.getId());
                        return;
                    }
                    StatusBarNotification[] activeNotifications = KeepAliveManager.notificationManager.getActiveNotifications();
                    int length = activeNotifications.length;
                    int i = 0;
                    while (i < length) {
                        StatusBarNotification statusBarNotification = activeNotifications[i];
                        if (statusBarNotification.getNotification() == null || !statusBarNotification.getNotification().getChannelId().equals(notificationChannel.getId())) {
                            i++;
                        } else {
                            ALog.i(KeepAliveManager.TAG, "find foreground notification try to delete it", new Object[0]);
                            KeepAliveManager.scheduledFuture.cancel(false);
                            KeepAliveManager.notificationManager.deleteNotificationChannel(notificationChannel.getId());
                            return;
                        }
                    }
                }
            }, 100, 100, TimeUnit.MILLISECONDS);
        } catch (Throwable th) {
            ALog.e(TAG, "start foreground error", th, new Object[0]);
        }
    }
}
