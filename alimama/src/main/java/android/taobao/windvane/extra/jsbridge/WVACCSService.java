package android.taobao.windvane.extra.jsbridge;

import android.content.Context;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.TaoLog;

import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.taobao.accs.base.TaoBaseService;

public class WVACCSService extends TaoBaseService {
    private static final String TAG = "CallbackService";
    private Context mContext = null;

    public void onCreate() {
        super.onCreate();
        this.mContext = this;
        TaoLog.d(TAG, UmbrellaConstants.LIFECYCLE_CREATE);
    }

    public void onData(String str, String str2, String str3, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
        if (TaoLog.getLogStatus()) {
            TaoLog.i(TAG, "serviceId : " + str + " dataId :" + str3);
        }
        WVEventService.getInstance().onEvent(WVEventId.ACCS_ONDATA, str, bArr);
    }

    public void onBind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
        TaoLog.d(TAG, "onBind");
    }

    public void onUnbind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
        TaoLog.d(TAG, UmbrellaConstants.LIFECYCLE_CREATE);
    }

    public void onSendData(String str, String str2, int i, TaoBaseService.ExtraInfo extraInfo) {
        TaoLog.d(TAG, "onSendData");
    }

    public void onResponse(String str, String str2, int i, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
        TaoLog.d(TAG, "onResponse");
    }

    public void onConnected(TaoBaseService.ConnectInfo connectInfo) {
        WVEventService.getInstance().onEvent(WVEventId.ACCS_ONCONNECTED);
        TaoLog.d(TAG, "onConnected");
    }

    public void onDisconnected(TaoBaseService.ConnectInfo connectInfo) {
        WVEventService.getInstance().onEvent(WVEventId.ACCS_ONDISONNECTED);
        TaoLog.d(TAG, "onDisconnected");
    }
}
