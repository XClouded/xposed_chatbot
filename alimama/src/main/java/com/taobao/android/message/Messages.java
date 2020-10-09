package com.taobao.android.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.taobao.android.internal.RuntimeGlobals;

public class Messages {
    public static void broadcast(Context context, Intent intent) {
        context.sendBroadcast(sealPackageIfNeeded(context, intent));
    }

    public static void subscribe(Context context, BroadcastReceiver broadcastReceiver, String str) {
        context.registerReceiver(broadcastReceiver, new IntentFilter(str));
    }

    public static void subscribe(Context context, BroadcastReceiver broadcastReceiver, String... strArr) {
        IntentFilter intentFilter = new IntentFilter();
        for (String addAction : strArr) {
            intentFilter.addAction(addAction);
        }
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public static void unsubscribe(Context context, BroadcastReceiver broadcastReceiver) {
        context.unregisterReceiver(broadcastReceiver);
    }

    private static Intent sealPackageIfNeeded(Context context, Intent intent) {
        if (!RuntimeGlobals.isMultiPackageMode(context) && intent.getPackage() == null) {
            intent.setPackage(context.getPackageName());
        }
        return intent;
    }
}
