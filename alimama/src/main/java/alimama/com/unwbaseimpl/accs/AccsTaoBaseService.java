package alimama.com.unwbaseimpl.accs;

import android.content.Intent;
import android.util.Log;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.taobao.accs.base.TaoBaseService;

public class AccsTaoBaseService extends TaoBaseService {
    private static final String TAG = "AccsTaoBaseService";

    public void onAntiBrush(boolean z, TaoBaseService.ExtraInfo extraInfo) {
        super.onAntiBrush(z, extraInfo);
        Log.d(TAG, "onAntiBrush ");
    }

    public void onDisconnected(TaoBaseService.ConnectInfo connectInfo) {
        super.onDisconnected(connectInfo);
        Log.d(TAG, "onDisconnected ");
    }

    public void onConnected(TaoBaseService.ConnectInfo connectInfo) {
        super.onConnected(connectInfo);
        Log.d(TAG, "onConnected ");
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, UmbrellaConstants.LIFECYCLE_CREATE);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    public void onData(String str, String str2, String str3, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
        Log.d(TAG, "onData serviceId: " + str + " dataId: " + str3 + " userId: " + str2);
    }

    public void onBind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
        Log.d(TAG, "onBind serviceId: " + str);
    }

    public void onUnbind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
        Log.d(TAG, "onUnbind");
    }

    public void onSendData(String str, String str2, int i, TaoBaseService.ExtraInfo extraInfo) {
        Log.d(TAG, "serviceId: " + str + " dataId: " + str2 + " errorCode: " + i);
    }

    public void onResponse(String str, String str2, int i, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
        Log.d(TAG, "onResponse serviceId: " + str + " dataId: " + str2);
    }
}
