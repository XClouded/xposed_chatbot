package com.taobao.accs.dispatch;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.Utils;

public class IntentDispatch {
    public static final String TAG = "IntentDispatch";

    public static void dispatchIntent(Context context, final Intent intent) {
        if (context == null || intent == null) {
            ALog.e(TAG, "dispatchIntent context or intent is null", new Object[0]);
            return;
        }
        final Context applicationContext = context.getApplicationContext();
        try {
            if (Utils.isTarget26(applicationContext)) {
                ALog.i(TAG, "dispatchIntent bind service start", "intent", intent.toString());
                applicationContext.bindService(intent, new ServiceConnection() {
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        ALog.d(IntentDispatch.TAG, "bindService connected", "componentName", componentName.toString());
                        Messenger messenger = new Messenger(iBinder);
                        Message message = new Message();
                        try {
                            message.getData().putParcelable("intent", intent);
                            messenger.send(message);
                        } catch (Exception e) {
                            ALog.e(IntentDispatch.TAG, "dispatch intent with exception", e.toString());
                        } catch (Throwable th) {
                            applicationContext.unbindService(this);
                            throw th;
                        }
                        applicationContext.unbindService(this);
                    }

                    public void onServiceDisconnected(ComponentName componentName) {
                        ALog.d(IntentDispatch.TAG, "bindService on disconnect", "componentName", componentName.toString());
                        applicationContext.unbindService(this);
                    }
                }, 1);
                return;
            }
            ALog.i(TAG, "dispatchIntent start service ", new Object[0]);
            applicationContext.startService(intent);
        } catch (Exception e) {
            ALog.e(TAG, "dispatchIntent method call with exception ", e.toString());
            e.printStackTrace();
        }
    }
}
