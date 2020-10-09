package com.huawei.hms.support.api.push.a.d;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.taobao.login4android.video.AudioRecordFunc;
import com.taobao.tao.image.Logger;
import com.uc.webview.export.extension.UCCore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/* compiled from: CommFun */
public class a {
    private static final char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', Logger.LEVEL_D, Logger.LEVEL_E, 'F'};

    public static String a(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 128)).toString();
        } catch (PackageManager.NameNotFoundException unused) {
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "get the app name of package:" + str + " failed.");
            return null;
        }
    }

    public static Intent b(Context context, String str) {
        try {
            return context.getPackageManager().getLaunchIntentForPackage(str);
        } catch (Exception unused) {
            com.huawei.hms.support.log.a.c("PushSelfShowLog", str + "not have launch activity");
            return null;
        }
    }

    public static boolean c(Context context, String str) {
        if (context == null || str == null || "".equals(str)) {
            return false;
        }
        try {
            if (context.getPackageManager().getApplicationInfo(str, 8192) == null) {
                return false;
            }
            com.huawei.hms.support.log.a.a("PushSelfShowLog", str + " is installed");
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static Boolean a(Context context, String str, Intent intent) {
        try {
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
            if (queryIntentActivities != null && queryIntentActivities.size() > 0) {
                int size = queryIntentActivities.size();
                for (int i = 0; i < size; i++) {
                    if (queryIntentActivities.get(i).activityInfo != null && str.equals(queryIntentActivities.get(i).activityInfo.applicationInfo.packageName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", e.toString(), (Throwable) e);
        }
        return false;
    }

    public static boolean a(Context context) {
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setPackage("com.android.email");
        intent.setData(Uri.fromParts("mailto", "xxxx@xxxx.com", (String) null));
        List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
        if (queryIntentActivities == null || queryIntentActivities.size() == 0) {
            return false;
        }
        return true;
    }

    public static boolean a() {
        return Build.VERSION.SDK_INT >= 11;
    }

    public static boolean b() {
        return Build.VERSION.SDK_INT >= 16;
    }

    public static long a(String str) {
        if (str == null) {
            str = "";
        }
        try {
            Date date = new Date();
            int hours = (date.getHours() * 2) + (date.getMinutes() / 30);
            String concat = str.concat(str);
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "startIndex is " + hours + ",ap is:" + concat + ",length is:" + concat.length());
            int length = concat.length();
            for (int i = hours; i < length; i++) {
                if (concat.charAt(i) != '0') {
                    long minutes = 60000 * ((long) (((i - hours) * 30) - (date.getMinutes() % 30)));
                    com.huawei.hms.support.log.a.a("PushSelfShowLog", "startIndex is:" + hours + " i is:" + i + " delay:" + minutes);
                    if (minutes >= 0) {
                        return minutes;
                    }
                    return 0;
                }
            }
        } catch (Exception e) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "error ", (Throwable) e);
        }
        return 0;
    }

    @SuppressLint({"TrulyRandom"})
    public static void a(Context context, Intent intent, long j) {
        try {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "enter setAPDelayAlarm(intent:" + intent.toURI() + " interval:" + j + "ms, context:" + context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
            if (alarmManager != null) {
                alarmManager.set(0, System.currentTimeMillis() + j, PendingIntent.getBroadcast(context, new SecureRandom().nextInt(), intent, 0));
            }
        } catch (RuntimeException unused) {
            com.huawei.hms.support.log.a.c("PushSelfShowLog", "set DelayAlarm error");
        } catch (Exception unused2) {
            com.huawei.hms.support.log.a.c("PushSelfShowLog", "set DelayAlarm error");
        }
    }

    public static boolean b(Context context) {
        return "com.huawei.android.pushagent".equals(context.getPackageName());
    }

    public static boolean c(Context context) {
        return HuaweiApiAvailability.SERVICES_PACKAGE.equals(context.getPackageName());
    }

    public static boolean a(Context context, Intent intent) {
        if (context == null) {
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "context is null");
            return false;
        } else if (intent == null) {
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "intent is null");
            return false;
        } else {
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, AudioRecordFunc.FRAME_SIZE);
            if (queryIntentActivities == null || queryIntentActivities.size() == 0) {
                com.huawei.hms.support.log.a.d("PushSelfShowLog", "no activity exist, may be system Err!! pkgName:");
                return false;
            }
            boolean z = queryIntentActivities.get(0).activityInfo.exported;
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "exportedFlag:" + z);
            String str = queryIntentActivities.get(0).activityInfo.permission;
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "need permission:" + str);
            if (!z) {
                return false;
            }
            if (TextUtils.isEmpty(str)) {
                return true;
            }
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "does't have the permission to open this activity");
            return false;
        }
    }

    public static void a(Context context, int i) {
        if (context == null) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "context is null");
            return;
        }
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager != null) {
                notificationManager.cancel(i);
            }
        } catch (Exception e) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "removeNotifiCationById err:" + e.toString());
        }
    }

    public static void b(Context context, Intent intent) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
            int i = 0;
            if (intent.hasExtra("selfshow_notify_id")) {
                i = intent.getIntExtra("selfshow_notify_id", 0) + 3;
            }
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "setDelayAlarm(cancel) alarmNotityId " + i + " and intent is " + intent.toURI());
            Intent intent2 = new Intent("com.huawei.intent.action.PUSH_DELAY_NOTIFY");
            intent2.setPackage(context.getPackageName()).setFlags(32);
            PendingIntent broadcast = PendingIntent.getBroadcast(context, i, intent2, UCCore.VERIFY_POLICY_PAK_QUICK);
            if (broadcast == null || alarmManager == null) {
                com.huawei.hms.support.log.a.a("PushSelfShowLog", "alarm not exist");
                return;
            }
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "  alarm cancel");
            alarmManager.cancel(broadcast);
        } catch (Exception e) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "cancelAlarm err:" + e.toString());
        }
    }

    public static void d(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "url is null.");
            return;
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            intent.setFlags(402653184);
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
            String str2 = null;
            Iterator<ResolveInfo> it = queryIntentActivities.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                String str3 = it.next().activityInfo.packageName;
                if (e(context, str3)) {
                    str2 = str3;
                    break;
                }
            }
            if (str2 == null) {
                Iterator<ResolveInfo> it2 = queryIntentActivities.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    String str4 = it2.next().activityInfo.packageName;
                    if ("com.android.browser".equalsIgnoreCase(str4)) {
                        str2 = str4;
                        break;
                    }
                }
            }
            if (str2 != null) {
                intent.setPackage(str2);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "start browser activity failed, exception:" + e.getMessage());
        }
    }

    public static boolean e(Context context, String str) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        context.getPackageManager().getPreferredActivities(arrayList, arrayList2, str);
        return arrayList2.size() > 0;
    }
}
