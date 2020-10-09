package com.taobao.accs.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.internal.ReceiverImpl;
import com.taobao.accs.utl.ALog;

public class BaseReceiver extends BroadcastReceiver {
    private static final String TAG = "BaseReceiver";
    /* access modifiers changed from: private */
    public IBaseReceiver baseReceiver;

    public void onReceive(final Context context, final Intent intent) {
        try {
            if (this.baseReceiver == null) {
                this.baseReceiver = new ReceiverImpl();
            }
            ThreadPoolExecutorFactory.execute(new Runnable() {
                public void run() {
                    BaseReceiver.this.baseReceiver.onReceive(context, intent);
                }
            });
        } catch (Exception e) {
            ALog.e(TAG, "build ReceiverImpl error", e.getMessage());
        }
    }
}
