package com.alibaba.android.update.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.alibaba.android.common.ILogger;
import com.alibaba.android.common.ServiceProxy;
import com.alibaba.android.common.ServiceProxyFactory;
import com.alibaba.android.update.UpdateManager;
import com.alibaba.android.update.UpdatePreference;

public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";

    public void onReceive(Context context, Intent intent) {
        ILogger iLogger;
        ServiceProxy proxy = ServiceProxyFactory.getProxy();
        if (proxy != null && (iLogger = (ILogger) proxy.getService(ServiceProxy.COMMON_SERVICE_LOGGER)) != null) {
            iLogger.logd(TAG, "update->onReceive");
            long currentTimeMillis = System.currentTimeMillis();
            SharedPreferences sharedPreferences = UpdatePreference.getInstance(context).getSharedPreferences();
            if (currentTimeMillis - sharedPreferences.getLong(UpdatePreference.KEY_NETWORK_CHANGE_TIMESTAMP, 0) > 10000) {
                UpdateManager.getInstance().executeInSilent(context);
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putLong(UpdatePreference.KEY_NETWORK_CHANGE_TIMESTAMP, currentTimeMillis);
            edit.commit();
        }
    }
}
