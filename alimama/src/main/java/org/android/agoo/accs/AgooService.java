package org.android.agoo.accs;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.UTMini;
import java.nio.charset.Charset;
import org.android.agoo.common.AgooConstants;
import org.android.agoo.common.CallBack;
import org.android.agoo.common.Config;
import org.android.agoo.control.AgooFactory;
import org.android.agoo.control.NotifManager;
import org.android.agoo.message.MessageService;

public class AgooService extends TaoBaseService {
    private static final String AGOOSERVICE_MESSAGE = "accs.agooService";
    private static final String TAG = "AgooService";
    public static CallBack mBindServiceCallback;
    public static CallBack mUnBindServiceCallback;
    private AgooFactory agooFactory;

    public void excuteAccsData(Intent intent) {
    }

    public void init(Context context) {
    }

    public void onCreate() {
        super.onCreate();
        ALog.d(TAG, "into--[onCreate]", new Object[0]);
        this.agooFactory = new AgooFactory();
        this.agooFactory.init(getApplicationContext(), (NotifManager) null, (MessageService) null);
    }

    public void onData(String str, String str2, String str3, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
        if (ALog.isPrintLog(ALog.Level.I)) {
            ALog.i(TAG, "into--[onData]:serviceId:" + str + ",dataId=" + str3, new Object[0]);
            StringBuilder sb = new StringBuilder();
            sb.append("push data:");
            sb.append(new String(bArr, Charset.forName("UTF-8")));
            ALog.d(TAG, sb.toString(), new Object[0]);
        }
        UTMini.getInstance().commitEvent(AgooConstants.AGOO_EVENT_ID, AGOOSERVICE_MESSAGE, AdapterUtilityImpl.getDeviceId(getApplicationContext()), str3);
        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_TOTAL_ARRIVE, "total_arrive", 0.0d);
        try {
            this.agooFactory.saveMsg(bArr);
            this.agooFactory.msgRecevie(bArr, "accs", extraInfo);
        } catch (Throwable th) {
            UTMini.getInstance().commitEvent(AgooConstants.AGOO_EVENT_ID, AGOOSERVICE_MESSAGE, "onDataError", th);
            ALog.e(TAG, "into--[onData,dealMessage]:error:" + th, new Object[0]);
        }
    }

    public void onBind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
        if (ALog.isPrintLog(ALog.Level.E)) {
            ALog.e(TAG, "into--[onBind]:serviceId:" + str + ",errorCode=" + i, new Object[0]);
        }
        if (mBindServiceCallback != null && GlobalClientInfo.AGOO_SERVICE_ID.equals(str)) {
            if (i == 200) {
                mBindServiceCallback.onSuccess();
            } else {
                mBindServiceCallback.onFailure(String.valueOf(i), "bind Agoo service fail");
            }
        }
        mBindServiceCallback = null;
    }

    public void onUnbind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
        if (ALog.isPrintLog(ALog.Level.E)) {
            ALog.e(TAG, "into--[onUnbind]:serviceId:" + str + ",errorCode=" + i, new Object[0]);
        }
        if (mUnBindServiceCallback != null && GlobalClientInfo.AGOO_SERVICE_ID.equals(str)) {
            if (i == 200) {
                mUnBindServiceCallback.onSuccess();
            } else {
                mUnBindServiceCallback.onFailure(String.valueOf(i), "unbind Agoo service fail");
            }
        }
        mUnBindServiceCallback = null;
    }

    public void onSendData(String str, String str2, int i, TaoBaseService.ExtraInfo extraInfo) {
        try {
            if (ALog.isPrintLog(ALog.Level.I)) {
                ALog.i(TAG, "onSendData,dataId=" + str2 + ",errorCode=" + i + ",serviceId=" + str, new Object[0]);
            }
            if (i == 200) {
                if (TextUtils.equals(AgooConstants.AGOO_SERVICE_AGOOACK, str)) {
                    AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_SUCCESS_ACK, "8/9", 0.0d);
                }
                if (TextUtils.isEmpty(str) || !TextUtils.equals(str, AgooConstants.AGOO_SERVICE_AGOOACK) || Long.parseLong(str2) <= 300000000 || Long.parseLong(str2) >= 600000000) {
                    if (!TextUtils.isEmpty(str) && TextUtils.equals(str, AgooConstants.AGOO_SERVICE_AGOOACK) && Long.parseLong(str2) > 600000000 && ALog.isPrintLog(ALog.Level.I)) {
                        ALog.i(TAG, "onSendData,reportData=" + str2 + ",serviceId=" + str, new Object[0]);
                    }
                } else if (ALog.isPrintLog(ALog.Level.I)) {
                    ALog.i(TAG, "onSendData,AckData=" + str2 + ",serviceId=" + str, new Object[0]);
                }
            } else {
                if (TextUtils.equals(AgooConstants.AGOO_SERVICE_AGOOACK, str)) {
                    Config.setReportTimes(getApplicationContext(), 1);
                    AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_FAIL_ACK, String.valueOf(i), 0.0d);
                }
                if (ALog.isPrintLog(ALog.Level.I)) {
                    ALog.i(TAG, "onSendData error,dataId=" + str2 + ",serviceId=" + str, new Object[0]);
                    ALog.e(TAG, "into--[parseError]", new Object[0]);
                }
                UTMini instance = UTMini.getInstance();
                String deviceId = AdapterUtilityImpl.getDeviceId(getApplicationContext());
                instance.commitEvent(AgooConstants.AGOO_EVENT_ID, AGOOSERVICE_MESSAGE, deviceId, "errorCode", str2 + ",serviceId=" + str + ",errorCode=" + i);
            }
        } catch (Throwable th) {
            if (ALog.isPrintLog(ALog.Level.E)) {
                ALog.e(TAG, "onSendData exception,e=" + th.getMessage() + ",e.getStackMsg=" + getStackMsg(th), new Object[0]);
            }
            UTMini.getInstance().commitEvent(AgooConstants.AGOO_EVENT_ID, AGOOSERVICE_MESSAGE, AdapterUtilityImpl.getDeviceId(getApplicationContext()), "onSendDataException", getStackMsg(th));
        }
    }

    public void onResponse(String str, String str2, int i, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
        if (ALog.isPrintLog(ALog.Level.I)) {
            ALog.i(TAG, "onResponse,dataId=" + str2 + ",errorCode=" + i + ",data=" + bArr + ",serviceId=" + str, new Object[0]);
        }
        String str3 = null;
        if (bArr != null) {
            try {
                if (bArr.length > 0) {
                    str3 = new String(bArr, "utf-8");
                }
            } catch (Throwable th) {
                ALog.e(TAG, "onResponse get data error,e=" + th, new Object[0]);
            }
        }
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d(TAG, "onResponse,message=" + str3, new Object[0]);
        }
        if (i == 200 && TextUtils.equals(str, AgooConstants.AGOO_SERVICE_AGOOACK)) {
            if (ALog.isPrintLog(ALog.Level.E)) {
                ALog.e(TAG, "request is success", Constants.KEY_DATA_ID, str2);
            }
            this.agooFactory.updateMsg(bArr, true);
        } else if (i != 200 && TextUtils.equals(str, AgooConstants.AGOO_SERVICE_AGOOACK)) {
            if (ALog.isPrintLog(ALog.Level.E)) {
                ALog.e(TAG, "request is error", Constants.KEY_DATA_ID, str2, "errorid", Integer.valueOf(i));
            }
            Config.setReportTimes(getApplicationContext(), 1);
            AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_FAIL_ACK, String.valueOf(i), 0.0d);
        } else if (ALog.isPrintLog(ALog.Level.E)) {
            ALog.e(TAG, "business request is error,message=" + str3, new Object[0]);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private String getStackMsg(Throwable th) {
        StringBuffer stringBuffer = new StringBuffer();
        StackTraceElement[] stackTrace = th.getStackTrace();
        if (stackTrace != null && stackTrace.length > 0) {
            for (StackTraceElement stackTraceElement : stackTrace) {
                stringBuffer.append(stackTraceElement.toString());
                stringBuffer.append("\n");
            }
        }
        return stringBuffer.toString();
    }
}
