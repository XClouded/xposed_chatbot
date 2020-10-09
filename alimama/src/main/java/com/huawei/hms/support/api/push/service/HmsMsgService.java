package com.huawei.hms.support.api.push.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.c.g;
import com.huawei.hms.c.h;

public class HmsMsgService extends Service {
    /* access modifiers changed from: private */
    public static void c(Context context, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction("com.huawei.android.push.intent.RECEIVE");
        intent.putExtra("msg_data", bundle.getByteArray("msg_data"));
        intent.putExtra("device_token", bundle.getByteArray("device_token"));
        intent.putExtra("msgIdStr", bundle.getString("msgIdStr"));
        intent.setFlags(32);
        intent.setPackage(bundle.getString("push_package"));
        context.sendBroadcast(intent, context.getPackageName() + ".permission.PROCESS_PUSH_MSG");
        com.huawei.hms.support.log.a.b("HmsMsgService", "send broadcast passby done");
    }

    /* access modifiers changed from: private */
    public static void d(Context context, Bundle bundle) {
        if (!com.huawei.hms.support.api.push.b.a.a.a(context)) {
            com.huawei.hms.support.log.a.b("HmsMsgService", context.getPackageName() + " disable display notification.");
        }
        Intent intent = new Intent();
        intent.setAction("com.huawei.push.msg.NOTIFY_MSG");
        intent.putExtra("selfshow_info", bundle.getByteArray("selfshow_info"));
        intent.putExtra("selfshow_token", bundle.getByteArray("selfshow_token"));
        intent.setPackage(bundle.getString("push_package"));
        new com.huawei.hms.support.api.push.a.a().a(context, intent);
        com.huawei.hms.support.log.a.b("HmsMsgService", "invokeSelfShow done");
    }

    public IBinder onBind(Intent intent) {
        com.huawei.hms.support.log.a.b("HmsMsgService", "onBind");
        Messenger messenger = new Messenger(new a(this));
        stopService(intent);
        return messenger.getBinder();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        com.huawei.hms.support.log.a.b("HmsMsgService", "Enter onStartCommand.");
        return 2;
    }

    private static class a extends Handler {
        private Context a;

        a(Context context) {
            this.a = context;
        }

        public void handleMessage(Message message) {
            Bundle data = message.getData();
            if (this.a.getApplicationContext().getPackageManager().getNameForUid(message.sendingUid).equals(HuaweiApiAvailability.SERVICES_PACKAGE) && data != null) {
                com.huawei.hms.support.log.a.b("HmsMsgService", "get package signature");
                if (!HuaweiApiAvailability.SERVICES_SIGNATURE.equalsIgnoreCase(new g(this.a).c(HuaweiApiAvailability.SERVICES_PACKAGE))) {
                    com.huawei.hms.support.log.a.b("HmsMsgService", "service not start by hms");
                } else {
                    com.huawei.hms.support.log.a.b("HmsMsgService", "chose push type");
                    if (data.getString("push_action").equals("com.huawei.push.msg.NOTIFY_MSG")) {
                        if (h.a() == null) {
                            h.a(this.a.getApplicationContext());
                        }
                        com.huawei.hms.support.log.a.b("HmsMsgService", "invokeSelfShow");
                        HmsMsgService.d(this.a, data);
                    } else if (data.getString("push_action").equals("com.huawei.push.msg.PASSBY_MSG")) {
                        com.huawei.hms.support.log.a.b("HmsMsgService", "sendBroadcastToHms");
                        HmsMsgService.c(this.a, data);
                    }
                }
            }
            super.handleMessage(message);
        }
    }
}
