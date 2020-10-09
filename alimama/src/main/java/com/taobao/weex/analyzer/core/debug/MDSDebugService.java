package com.taobao.weex.analyzer.core.debug;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.taobao.weex.analyzer.core.NetworkEventSender;
import com.taobao.weex.analyzer.core.debug.DebugController;
import com.taobao.weex.analyzer.utils.DeviceUtils;
import com.taobao.weex.utils.WXLogUtils;

public class MDSDebugService extends Service {
    private static final String PARAM_DEVICE_ID = "deviceId";
    private static final String PARAM_WS_URL = "url";
    private DebugController mController;

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        WXLogUtils.w("weex-analyzer", "mds debug service is created");
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        WXLogUtils.w("weex-analyzer", "mds debug service will start");
        String stringExtra = intent.getStringExtra("url");
        String stringExtra2 = intent.getStringExtra("deviceId");
        if (this.mController != null && this.mController.isWSConnected()) {
            if (TextUtils.isEmpty(stringExtra)) {
                this.mController.sendConnectionAlreadyEstablishedMsg();
                return 2;
            } else if (!TextUtils.isEmpty(stringExtra) && stringExtra.equals(this.mController.getWebsocketUrl())) {
                this.mController.sendConnectionAlreadyEstablishedMsg();
                return 2;
            }
        }
        if (this.mController == null) {
            if (stringExtra2 == null) {
                stringExtra2 = DeviceUtils.getDeviceId(this);
            }
            this.mController = DebugController.newInstance(this, stringExtra2);
            this.mController.setConnectionChangedCallback(new DebugController.OnConnectionChangedCallback() {
                public void onConnectionChanged(DebugController.ConnectionInfo connectionInfo) {
                    MDSDebugService.this.sendMessage(connectionInfo);
                }
            });
        }
        if (!TextUtils.isEmpty(stringExtra)) {
            WXLogUtils.d("weex-analyzer", "try connect to: " + stringExtra);
            this.mController.connectTo(stringExtra);
        } else {
            sendMessage(new DebugController.ConnectionInfo("连接未建立", "", 5));
            WXLogUtils.d("weex-analyzer", "empty web socket url");
        }
        return 2;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mController != null) {
            this.mController.closeWSConnection();
            this.mController = null;
        }
        WXLogUtils.w("weex-analyzer", "mds debug service is destroyed");
    }

    /* access modifiers changed from: private */
    public void sendMessage(@NonNull DebugController.ConnectionInfo connectionInfo) {
        String str = connectionInfo.msg;
        String str2 = connectionInfo.desc;
        int i = connectionInfo.state;
        Intent intent = new Intent("action_debug_message");
        intent.putExtra("message", str);
        intent.putExtra(NetworkEventSender.INTENT_EXTRA_DESC, str2);
        intent.putExtra("state", i);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        WXLogUtils.d("weex-analyzer", "[MDSDebugService] send message : " + connectionInfo.toString());
    }

    public static void launchBy(@NonNull Context context, @Nullable String str, @Nullable String str2) {
        Intent intent = new Intent(context, MDSDebugService.class);
        intent.putExtra("url", str);
        intent.putExtra("deviceId", str2);
        context.startService(intent);
    }

    public static void stop(@NonNull Context context) {
        context.stopService(new Intent(context, MDSDebugService.class));
    }
}
