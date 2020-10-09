package com.xiaomi.push.mpcd.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.xiaomi.push.dr;

public class BroadcastActionsReceiver extends BroadcastReceiver {
    private dr a;

    public BroadcastActionsReceiver(dr drVar) {
        this.a = drVar;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.a != null) {
            this.a.a(context, intent);
        }
    }
}
