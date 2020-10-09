package com.taobao.accs.base;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.internal.ServiceImpl;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.Utils;

public class BaseService extends Service {
    private static final String TAG = "BaseService";
    IBaseService mBaseService = null;
    private Messenger messenger = new Messenger(new Handler() {
        public void handleMessage(Message message) {
            if (message != null) {
                ALog.i(BaseService.TAG, "handleMessage on receive msg", "msg", message.toString());
                Intent intent = (Intent) message.getData().getParcelable("intent");
                if (intent != null) {
                    ALog.i(BaseService.TAG, "handleMessage get intent success", "intent", intent.toString());
                    BaseService.this.onStartCommand(intent, 0, 0);
                }
            }
        }
    });

    public void onCreate() {
        super.onCreate();
        ThreadPoolExecutorFactory.execute(new Runnable() {
            public void run() {
                try {
                    BaseService.this.mBaseService = new ServiceImpl(BaseService.this);
                    BaseService.this.mBaseService.onCreate();
                } catch (Exception e) {
                    ALog.e(BaseService.TAG, "create ServiceImpl error", e.getMessage());
                }
            }
        });
    }

    public int onStartCommand(final Intent intent, final int i, final int i2) {
        ThreadPoolExecutorFactory.execute(new Runnable() {
            public void run() {
                if (BaseService.this.mBaseService != null) {
                    BaseService.this.mBaseService.onStartCommand(intent, i, i2);
                    return;
                }
                BaseService.this.onCreate();
                BaseService.this.onStartCommand(intent, i, i2);
            }
        });
        return 1;
    }

    public IBinder onBind(Intent intent) {
        ALog.d(TAG, "onBind", "intent", intent);
        try {
            if (Utils.isTarget26(this)) {
                ALog.i(TAG, "onBind bind service", new Object[0]);
                getApplicationContext().bindService(new Intent(this, getClass()), new ServiceConnection() {
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    }

                    public void onServiceDisconnected(ComponentName componentName) {
                    }
                }, 1);
            }
        } catch (Throwable th) {
            ALog.i(TAG, "onBind bind service with exception", th.toString());
        }
        return this.messenger.getBinder();
    }

    public void onDestroy() {
        ThreadPoolExecutorFactory.execute(new Runnable() {
            public void run() {
                if (BaseService.this.mBaseService != null) {
                    BaseService.this.mBaseService.onDestroy();
                    BaseService.this.mBaseService = null;
                }
            }
        });
        super.onDestroy();
    }
}
