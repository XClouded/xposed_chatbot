package com.taobao.agoo;

import android.content.Context;
import android.content.Intent;
import org.android.agoo.control.BaseIntentService;

public abstract class TaobaoBaseIntentService extends BaseIntentService {
    /* access modifiers changed from: protected */
    public abstract void onError(Context context, String str);

    /* access modifiers changed from: protected */
    public abstract void onMessage(Context context, Intent intent);

    /* access modifiers changed from: protected */
    public abstract void onRegistered(Context context, String str);

    /* access modifiers changed from: protected */
    public abstract void onUnregistered(Context context, String str);

    /* access modifiers changed from: protected */
    public void onUserCommand(Context context, Intent intent) {
    }
}
