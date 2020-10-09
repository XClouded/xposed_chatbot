package com.taobao.accs.data;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.OrangeAdapter;
import com.taobao.accs.utl.Utils;

public class MsgDistributeService extends Service {
    private static final String TAG = "MsgDistributeService";
    private Messenger messenger = new Messenger(new Handler() {
        public void handleMessage(Message message) {
            if (message != null) {
                ALog.i(MsgDistributeService.TAG, "handleMessage on receive msg", "msg", message.toString());
                Intent intent = (Intent) message.getData().getParcelable("intent");
                if (intent != null) {
                    ALog.i(MsgDistributeService.TAG, "handleMessage get intent success", "intent", intent.toString());
                    MsgDistributeService.this.onStartCommand(intent, 0, 0);
                }
            }
        }
    });

    public IBinder onBind(Intent intent) {
        if (OrangeAdapter.isBindService() && Utils.isTarget26(this)) {
            getApplicationContext().bindService(new Intent(this, getClass()), new ServiceConnection() {
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                }

                public void onServiceDisconnected(ComponentName componentName) {
                }
            }, 1);
        }
        return this.messenger.getBinder();
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public int onStartCommand(final Intent intent, int i, int i2) {
        try {
            ALog.i(TAG, "onStartCommand", "action", intent.getAction());
            if (TextUtils.isEmpty(intent.getAction()) || !TextUtils.equals(intent.getAction(), Constants.ACTION_SEND)) {
                ALog.i(TAG, "onStartCommand distribute message", new Object[0]);
                MsgDistribute.distribMessage(getApplicationContext(), intent);
            } else {
                ThreadPoolExecutorFactory.getScheduledExecutor().execute(new Runnable() {
                    public void run() {
                        ALog.i(MsgDistributeService.TAG, "onStartCommand send message", new Object[0]);
                        ACCSManager.AccsRequest accsRequest = (ACCSManager.AccsRequest) intent.getSerializableExtra(Constants.KEY_SEND_REQDATA);
                        String stringExtra = intent.getStringExtra("packageName");
                        String stringExtra2 = intent.getStringExtra("appKey");
                        String stringExtra3 = intent.getStringExtra(Constants.KEY_CONFIG_TAG);
                        if (TextUtils.isEmpty(stringExtra3)) {
                            stringExtra3 = stringExtra2;
                        }
                        ACCSManager.getAccsInstance(MsgDistributeService.this.getApplicationContext(), stringExtra2, stringExtra3).sendRequest(MsgDistributeService.this.getApplicationContext(), accsRequest, stringExtra, false);
                    }
                });
            }
        } catch (Throwable th) {
            ALog.e(TAG, "onStartCommand", th, new Object[0]);
        }
        return 2;
    }
}
