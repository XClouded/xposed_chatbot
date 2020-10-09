package com.huawei.hms.support.api.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.huawei.hms.c.h;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.RejectedExecutionException;

public abstract class PushReceiver extends BroadcastReceiver {

    public interface BOUND_KEY {
        public static final String deviceTokenKey = "deviceToken";
        public static final String pushMsgKey = "pushMsg";
        public static final String pushNotifyId = "pushNotifyId";
    }

    public enum Event {
        NOTIFICATION_OPENED,
        NOTIFICATION_CLICK_BTN
    }

    public void onEvent(Context context, Event event, Bundle bundle) {
    }

    public void onPushMsg(Context context, byte[] bArr, String str) {
    }

    public void onPushState(Context context, boolean z) {
    }

    public void onToken(Context context, String str) {
    }

    private static void a(Context context, Intent intent) {
        if (intent.hasExtra("selfshow_info")) {
            if (!com.huawei.hms.support.api.push.b.a.a.a(context)) {
                com.huawei.hms.support.log.a.b("PushReceiver", context.getPackageName() + " disable display notification.");
            }
            new com.huawei.hms.support.api.push.a.a().a(context, intent);
        }
    }

    public void onToken(Context context, String str, Bundle bundle) {
        onToken(context, str);
    }

    public boolean onPushMsg(Context context, byte[] bArr, Bundle bundle) {
        String str = "";
        if (bundle != null) {
            str = bundle.getString(BOUND_KEY.deviceTokenKey);
        }
        onPushMsg(context, bArr, str);
        return true;
    }

    public final void onReceive(Context context, Intent intent) {
        if (intent != null && context != null) {
            com.huawei.hms.support.log.a.b("PushReceiver", "push receive broadcast message, Intent:" + intent.getAction() + " pkgName:" + context.getPackageName());
            try {
                intent.getStringExtra("TestIntent");
                String action = intent.getAction();
                if (h.a() == null) {
                    h.a(context.getApplicationContext());
                }
                if ("com.huawei.android.push.intent.REGISTRATION".equals(action)) {
                    b(context, intent);
                } else if ("com.huawei.android.push.intent.RECEIVE".equals(action) || "com.huawei.android.push.intent.PASSBY_MESSAGE".equals(action)) {
                    c(context, intent);
                } else if ("com.huawei.android.push.intent.CLICK".equals(action)) {
                    d(context, intent);
                } else if ("com.huawei.intent.action.PUSH_STATE".equals(action)) {
                    com.huawei.hms.support.api.push.b.a.c.a().execute(new c(context, intent));
                } else if ("com.huawei.intent.action.PUSH_DELAY_NOTIFY".equals(action)) {
                    a(context, intent);
                } else {
                    com.huawei.hms.support.log.a.b("PushReceiver", "message can't be recognised:" + intent.toUri(0));
                }
            } catch (Exception unused) {
                com.huawei.hms.support.log.a.d("PushReceiver", "intent has some error");
            }
        }
    }

    private void b(Context context, Intent intent) {
        try {
            if (intent.hasExtra("device_token")) {
                com.huawei.hms.support.api.push.b.a.c.a().execute(new e(context, intent));
            } else {
                com.huawei.hms.support.log.a.b("PushReceiver", "This message dose not sent by hwpush.");
            }
        } catch (RuntimeException unused) {
            com.huawei.hms.support.log.a.d("PushReceiver", "handlePushMessageEvent execute task runtime exception.");
        } catch (Exception unused2) {
            com.huawei.hms.support.log.a.d("PushReceiver", "handlePushTokenEvent execute task error");
        }
    }

    private void c(Context context, Intent intent) {
        try {
            if (intent.hasExtra("msg_data")) {
                com.huawei.hms.support.api.push.b.a.c.a().execute(new d(context, intent));
            } else {
                com.huawei.hms.support.log.a.b("PushReceiver", "This push message dose not sent by hwpush.");
            }
        } catch (RuntimeException unused) {
            com.huawei.hms.support.log.a.d("PushReceiver", "handlePushMessageEvent execute task runtime exception.");
        } catch (Exception unused2) {
            com.huawei.hms.support.log.a.d("PushReceiver", "handlePushMessageEvent execute task error");
        }
    }

    private void d(Context context, Intent intent) {
        if (intent.hasExtra("click")) {
            com.huawei.hms.support.api.push.b.a.c.a().execute(new b(context, intent));
        } else if (intent.hasExtra("clickBtn")) {
            com.huawei.hms.support.api.push.b.a.c.a().execute(new a(context, intent));
        }
    }

    /* access modifiers changed from: private */
    public void a(Context context, Intent intent, String str) {
        String str2;
        if (context != null && intent != null) {
            com.huawei.hms.support.log.a.b("PushReceiver", "send response to frameworkPush that app receive the passby message");
            try {
                str2 = intent.getStringExtra("msgIdStr");
            } catch (Exception unused) {
                com.huawei.hms.support.log.a.d("PushReceiver", "responseToFrameworkPush error");
                str2 = null;
            }
            if (!TextUtils.isEmpty(str2) && com.huawei.hms.support.api.push.b.a.a(context)) {
                Intent intent2 = new Intent("com.huawei.android.push.intent.MSG_RESPONSE");
                intent2.putExtra("msgIdStr", str2);
                intent2.putExtra("resultCode", str);
                intent2.setPackage("android");
                intent2.setFlags(32);
                context.sendBroadcast(intent2);
            }
        }
    }

    private class b implements Runnable {
        private Context b;
        private Intent c;

        private b(Context context, Intent intent) {
            this.b = context;
            this.c = intent;
        }

        public void run() {
            try {
                if (this.c.hasExtra("click")) {
                    String stringExtra = this.c.getStringExtra("click");
                    Bundle bundle = new Bundle();
                    bundle.putString(BOUND_KEY.pushMsgKey, stringExtra);
                    PushReceiver.this.onEvent(this.b, Event.NOTIFICATION_OPENED, bundle);
                }
            } catch (RejectedExecutionException unused) {
                com.huawei.hms.support.log.a.d("PushReceiver", "execute task error");
            } catch (Exception unused2) {
                com.huawei.hms.support.log.a.d("PushReceiver", "handle click event error");
            }
        }
    }

    private class a implements Runnable {
        private Context b;
        private Intent c;

        private a(Context context, Intent intent) {
            this.b = context;
            this.c = intent;
        }

        public void run() {
            try {
                if (this.c.hasExtra("clickBtn")) {
                    String stringExtra = this.c.getStringExtra("clickBtn");
                    int intExtra = this.c.getIntExtra("notifyId", 0);
                    Bundle bundle = new Bundle();
                    bundle.putString(BOUND_KEY.pushMsgKey, stringExtra);
                    bundle.putInt(BOUND_KEY.pushNotifyId, intExtra);
                    PushReceiver.this.onEvent(this.b, Event.NOTIFICATION_CLICK_BTN, bundle);
                }
            } catch (RejectedExecutionException unused) {
                com.huawei.hms.support.log.a.d("PushReceiver", "execute task error");
            } catch (Exception unused2) {
                com.huawei.hms.support.log.a.d("PushReceiver", "handleNotificationBtnOnclickEvent error");
            }
        }
    }

    private class c implements Runnable {
        private Context b;
        private Intent c;

        private c(Context context, Intent intent) {
            this.b = context;
            this.c = intent;
        }

        public void run() {
            try {
                PushReceiver.this.onPushState(this.b, this.c.getBooleanExtra("push_state", false));
            } catch (RejectedExecutionException unused) {
                com.huawei.hms.support.log.a.d("PushReceiver", "execute task error");
            } catch (Exception unused2) {
                com.huawei.hms.support.log.a.d("PushReceiver", "handlePushStateEvent error");
            }
        }
    }

    private class e implements Runnable {
        private Context b;
        private Intent c;

        private e(Context context, Intent intent) {
            this.b = context;
            this.c = intent;
        }

        public void run() {
            try {
                byte[] byteArrayExtra = this.c.getByteArrayExtra("device_token");
                String stringExtra = this.c.getStringExtra("extra_notify_key");
                if (byteArrayExtra == null) {
                    com.huawei.hms.support.log.a.b("PushReceiver", "get a deviceToken, but it is null");
                    return;
                }
                com.huawei.hms.support.log.a.b("PushReceiver", "receive a push token: " + this.b.getPackageName());
                com.huawei.hms.support.api.push.b.a.a.c cVar = new com.huawei.hms.support.api.push.b.a.a.c(this.b, "push_client_self_info");
                cVar.a("reqTokenTime", Long.valueOf(System.currentTimeMillis()));
                String str = new String(byteArrayExtra, "UTF-8");
                String a2 = com.huawei.hms.support.api.push.b.a.b.a(this.b, "push_client_self_info");
                String b2 = cVar.b("push_notify_key");
                if (!TextUtils.isEmpty(stringExtra) && !stringExtra.equals(b2)) {
                    com.huawei.hms.support.log.a.b("PushReceiver", "notifyKey changed, refresh it");
                    cVar.a("push_notify_key", stringExtra);
                }
                if (!str.equals(a2)) {
                    com.huawei.hms.support.log.a.b("PushReceiver", "receive a token, refresh the local token");
                    cVar.d("token_info");
                    com.huawei.hms.support.api.push.b.a.b.a(this.b, "push_client_self_info", str);
                }
                Bundle bundle = new Bundle();
                bundle.putString(BOUND_KEY.deviceTokenKey, str);
                bundle.putByteArray(BOUND_KEY.pushMsgKey, (byte[]) null);
                if (this.c.getExtras() != null) {
                    bundle.putAll(this.c.getExtras());
                }
                PushReceiver.this.onToken(this.b, str, bundle);
            } catch (UnsupportedEncodingException unused) {
                com.huawei.hms.support.log.a.d("PushReceiver", "encode token error");
            } catch (RejectedExecutionException unused2) {
                com.huawei.hms.support.log.a.d("PushReceiver", "execute task error");
            } catch (Exception unused3) {
                com.huawei.hms.support.log.a.d("PushReceiver", "handle push token error");
            }
        }
    }

    private class d implements Runnable {
        private Context b;
        private Intent c;

        private d(Context context, Intent intent) {
            this.b = context;
            this.c = intent;
        }

        public void run() {
            if (new com.huawei.hms.support.api.push.b.a.a.c(this.b, "push_switch").a("normal_msg_enable")) {
                com.huawei.hms.support.log.a.b("PushReceiver", this.b.getPackageName() + " disable pass by push message, abandon it");
                PushReceiver.this.a(this.b, this.c, "1");
                return;
            }
            com.huawei.hms.support.log.a.b("PushReceiver", this.b.getPackageName() + " receive pass by push message");
            PushReceiver.this.a(this.b, this.c, "0");
            try {
                byte[] byteArrayExtra = this.c.getByteArrayExtra("msg_data");
                byte[] byteArrayExtra2 = this.c.getByteArrayExtra("device_token");
                if (byteArrayExtra != null) {
                    if (byteArrayExtra2 != null) {
                        String str = new String(byteArrayExtra2, "UTF-8");
                        Bundle bundle = new Bundle();
                        bundle.putString(BOUND_KEY.deviceTokenKey, str);
                        bundle.putByteArray(BOUND_KEY.pushMsgKey, byteArrayExtra);
                        PushReceiver.this.onPushMsg(this.b, byteArrayExtra, bundle);
                        return;
                    }
                }
                com.huawei.hms.support.log.a.c("PushReceiver", "PushReceiver receive a message, but message is empty.");
            } catch (UnsupportedEncodingException unused) {
                com.huawei.hms.support.log.a.d("PushReceiver", "encode token error");
            } catch (RejectedExecutionException unused2) {
                com.huawei.hms.support.log.a.d("PushReceiver", "execute task error");
            } catch (Exception unused3) {
                com.huawei.hms.support.log.a.d("PushReceiver", "handle push message error");
            }
        }
    }
}
