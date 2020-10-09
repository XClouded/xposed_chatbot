package com.taobao.orange.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.taobao.orange.ConfigCenter;
import com.taobao.orange.OThreadFactory;
import com.taobao.orange.util.AndroidUtil;
import com.taobao.orange.util.OLog;

public class OrangeReceiver extends BroadcastReceiver {
    private static final String TAG = "OrangeReceiver";
    public static volatile boolean networkValid = false;

    public void onReceive(Context context, Intent intent) {
        if (intent == null || !"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            return;
        }
        if (!AndroidUtil.isNetworkConnected(context)) {
            networkValid = false;
        } else if (!networkValid) {
            networkValid = true;
            OLog.i(TAG, "onReceive network valid", new Object[0]);
            OThreadFactory.execute(new Runnable() {
                public void run() {
                    ConfigCenter.getInstance().retryFailRequests();
                }
            });
        }
    }
}
