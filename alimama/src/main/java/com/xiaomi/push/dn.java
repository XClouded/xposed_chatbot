package com.xiaomi.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import com.xiaomi.push.service.ag;

final class dn implements dr {
    dn() {
    }

    private void a(Context context, String str, String str2) {
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str)) {
            try {
                if (ag.a(context, String.valueOf(12), 1)) {
                    ho hoVar = new ho();
                    hoVar.a(str + ":" + str2);
                    hoVar.a(System.currentTimeMillis());
                    hoVar.a(hi.BroadcastAction);
                    dx.a(context, hoVar);
                }
            } catch (Throwable unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void b(Context context, Intent intent) {
        int a;
        try {
            String dataString = intent.getDataString();
            if (!TextUtils.isEmpty(dataString)) {
                String[] split = dataString.split(":");
                if (split.length < 2) {
                    return;
                }
                if (!TextUtils.isEmpty(split[1])) {
                    String str = split[1];
                    long currentTimeMillis = System.currentTimeMillis();
                    boolean a2 = ag.a(context).a(hl.BroadcastActionCollectionSwitch.a(), true);
                    if (TextUtils.equals("android.intent.action.PACKAGE_RESTARTED", intent.getAction())) {
                        if (!ag.a(context, String.valueOf(12), 1)) {
                            return;
                        }
                        if (a2) {
                            if (TextUtils.isEmpty(dw.a)) {
                                dw.a += dq.f239a + ":";
                            }
                            dw.a += str + Operators.BRACKET_START_STR + currentTimeMillis + Operators.BRACKET_END_STR + ",";
                        }
                    } else if (!TextUtils.equals("android.intent.action.PACKAGE_CHANGED", intent.getAction())) {
                        if (TextUtils.equals("android.intent.action.PACKAGE_ADDED", intent.getAction())) {
                            if (!intent.getExtras().getBoolean("android.intent.extra.REPLACING") && a2) {
                                a = hi.BroadcastActionAdded.a();
                            } else {
                                return;
                            }
                        } else if (TextUtils.equals("android.intent.action.PACKAGE_REMOVED", intent.getAction())) {
                            if (!intent.getExtras().getBoolean("android.intent.extra.REPLACING") && a2) {
                                a = hi.BroadcastActionRemoved.a();
                            } else {
                                return;
                            }
                        } else if (TextUtils.equals("android.intent.action.PACKAGE_REPLACED", intent.getAction())) {
                            if (a2) {
                                a = hi.BroadcastActionReplaced.a();
                            } else {
                                return;
                            }
                        } else if (TextUtils.equals("android.intent.action.PACKAGE_DATA_CLEARED", intent.getAction()) && a2) {
                            a = hi.BroadcastActionDataCleared.a();
                        } else {
                            return;
                        }
                        a(context, String.valueOf(a), str);
                    } else if (!ag.a(context, String.valueOf(12), 1)) {
                    } else {
                        if (a2) {
                            if (TextUtils.isEmpty(dw.b)) {
                                dw.b += dq.b + ":";
                            }
                            dw.b += str + Operators.BRACKET_START_STR + currentTimeMillis + Operators.BRACKET_END_STR + ",";
                        }
                    }
                }
            }
        } catch (Throwable unused) {
        }
    }

    public void a(Context context, Intent intent) {
        if (intent != null) {
            ai.a(context).a((Runnable) new Cdo(this, context, intent));
        }
    }
}
