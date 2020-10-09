package org.android.agoo.control;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import com.ali.user.mobile.rpc.ApiConstants;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.IACCSManager;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.UTMini;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.weex.BuildConfig;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.android.agoo.common.AgooConstants;
import org.android.agoo.common.Config;
import org.android.agoo.common.MsgDO;
import org.json.JSONObject;

public class NotifManager {
    private static final String ACK_MESSAGE = "accs.ackMessage";
    private static final int EVENT_ID = 66001;
    private static final String TAG = "NotifManager";
    /* access modifiers changed from: private */
    public static Context mContext;

    public void pingApp(String str, String str2, String str3, int i) {
    }

    public void init(Context context) {
        mContext = context;
    }

    public void handlerACKMessage(MsgDO msgDO, TaoBaseService.ExtraInfo extraInfo) {
        if (msgDO != null) {
            if (!TextUtils.isEmpty(msgDO.msgIds) || !TextUtils.isEmpty(msgDO.removePacks) || !TextUtils.isEmpty(msgDO.errorCode)) {
                try {
                    HashMap hashMap = new HashMap();
                    hashMap.put("api", AgooConstants.AGOO_SERVICE_AGOOACK);
                    hashMap.put("id", msgDO.msgIds + DinamicConstant.DINAMIC_PREFIX_AT + msgDO.messageSource);
                    if (!TextUtils.isEmpty(msgDO.removePacks)) {
                        hashMap.put("del_pack", msgDO.removePacks);
                    }
                    if (!TextUtils.isEmpty(msgDO.errorCode)) {
                        hashMap.put("ec", msgDO.errorCode);
                    }
                    if (!TextUtils.isEmpty(msgDO.type)) {
                        hashMap.put("type", msgDO.type);
                    }
                    if (!TextUtils.isEmpty(msgDO.extData)) {
                        hashMap.put(ApiConstants.ApiField.EXT, msgDO.extData);
                    }
                    hashMap.put("appkey", Config.getAgooAppKey(mContext));
                    hashMap.put("utdid", AdapterUtilityImpl.getDeviceId(mContext));
                    byte[] bytes = new JSONObject(hashMap).toString().getBytes("UTF-8");
                    UTMini.getInstance().commitEvent(AgooConstants.AGOO_EVENT_ID, ACK_MESSAGE, AdapterUtilityImpl.getDeviceId(mContext), "handlerACKMessageSendData", msgDO.msgIds);
                    AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_ACK, "handlerACKMessage", 0.0d);
                    ACCSManager.AccsRequest accsRequest = new ACCSManager.AccsRequest((String) null, AgooConstants.AGOO_SERVICE_AGOOACK, bytes, (String) null, (String) null, (URL) null, (String) null);
                    if (msgDO != null) {
                        accsRequest.setTag(msgDO.msgIds);
                    }
                    String sendPushResponse = ACCSManager.getAccsInstance(mContext, Config.getAgooAppKey(mContext), Config.getAccsConfigTag(mContext)).sendPushResponse(mContext, accsRequest, extraInfo);
                    ALog.i(TAG, "handlerACKMessage,endRequest,dataId=" + sendPushResponse, new Object[0]);
                } catch (Throwable th) {
                    if (ALog.isPrintLog(ALog.Level.E)) {
                        ALog.e(TAG, "handlerACKMessage Throwable,msgIds=" + msgDO.msgIds + ",type=" + msgDO.type + ",e=" + th.toString(), new Object[0]);
                    }
                    UTMini.getInstance().commitEvent(AgooConstants.AGOO_EVENT_ID, ACK_MESSAGE, AdapterUtilityImpl.getDeviceId(mContext), "handlerACKMessageExceptionFailed", th.toString());
                }
            } else {
                UTMini instance = UTMini.getInstance();
                String deviceId = AdapterUtilityImpl.getDeviceId(mContext);
                instance.commitEvent(AgooConstants.AGOO_EVENT_ID, ACK_MESSAGE, deviceId, "handlerACKMessageRetuen", "msgids=" + msgDO.msgIds + ",removePacks=" + msgDO.removePacks + ",errorCode=" + msgDO.errorCode);
            }
        }
    }

    public void report(MsgDO msgDO, TaoBaseService.ExtraInfo extraInfo) {
        if (!TextUtils.isEmpty(msgDO.reportStr)) {
            try {
                if (Integer.parseInt(msgDO.reportStr) >= -1) {
                    reportMethod(msgDO, extraInfo);
                    if (!msgDO.isFromCache) {
                        AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_ACK, msgDO.msgStatus, 0.0d);
                    }
                }
            } catch (Throwable th) {
                ALog.e(TAG, "[report] is error", th, new Object[0]);
            }
        }
    }

    public void reportNotifyMessage(MsgDO msgDO) {
        if (msgDO != null) {
            try {
                AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_REPORT_ID, msgDO.msgIds, 0.0d);
                ACCSManager.AccsRequest accsRequest = new ACCSManager.AccsRequest((String) null, AgooConstants.AGOO_SERVICE_AGOOACK, convertMsgToBytes(msgDO), (String) null, (String) null, (URL) null, (String) null);
                IACCSManager accsInstance = ACCSManager.getAccsInstance(mContext, Config.getAgooAppKey(mContext), Config.getAccsConfigTag(mContext));
                String sendRequest = accsInstance.sendRequest(mContext, accsRequest);
                accsInstance.sendPushResponse(mContext, accsRequest, (TaoBaseService.ExtraInfo) null);
                if (ALog.isPrintLog(ALog.Level.E)) {
                    ALog.e(TAG, "reportNotifyMessage", Constants.KEY_DATA_ID, sendRequest, "status", msgDO.msgStatus);
                }
                AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_CLICK, msgDO.msgStatus, 0.0d);
                AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_AGOO_ACK, msgDO.msgStatus, 0.0d);
            } catch (Throwable th) {
                ALog.e(TAG, "[reportNotifyMessage] is error", th, new Object[0]);
                AppMonitorAdapter.commitCount("accs", "error", th.toString(), 0.0d);
            }
        }
    }

    private byte[] convertMsgToBytes(MsgDO msgDO) throws UnsupportedEncodingException {
        HashMap hashMap = new HashMap();
        hashMap.put("api", "agooReport");
        hashMap.put("id", msgDO.msgIds + DinamicConstant.DINAMIC_PREFIX_AT + msgDO.messageSource);
        hashMap.put(ApiConstants.ApiField.EXT, msgDO.extData);
        hashMap.put("status", msgDO.msgStatus);
        if (!TextUtils.isEmpty(msgDO.errorCode)) {
            hashMap.put("ec", msgDO.errorCode);
        }
        if (!TextUtils.isEmpty(msgDO.type)) {
            hashMap.put("type", msgDO.type);
        }
        if (!TextUtils.isEmpty(msgDO.fromPkg)) {
            hashMap.put("fromPkg", msgDO.fromPkg);
        }
        if (!TextUtils.isEmpty(msgDO.fromAppkey)) {
            hashMap.put(AgooConstants.MESSAGE_FROM_APPKEY, msgDO.fromAppkey);
        }
        if (!TextUtils.isEmpty(msgDO.notifyEnable)) {
            hashMap.put("notifyEnable", msgDO.notifyEnable);
        }
        if (!TextUtils.isEmpty(msgDO.extData)) {
            hashMap.put(ApiConstants.ApiField.EXT, msgDO.extData);
        }
        hashMap.put("isStartProc", Boolean.toString(msgDO.isStartProc));
        hashMap.put("appkey", Config.getAgooAppKey(mContext));
        hashMap.put("utdid", AdapterUtilityImpl.getDeviceId(mContext));
        hashMap.put("evokeAppStatus", String.valueOf(msgDO.evokeAppStatus));
        hashMap.put("lastActiveTime", String.valueOf(msgDO.lastActiveTime));
        hashMap.put("isGlobalClick", String.valueOf(msgDO.isGlobalClick));
        return new JSONObject(hashMap).toString().getBytes("UTF-8");
    }

    private void reportMethod(MsgDO msgDO, TaoBaseService.ExtraInfo extraInfo) {
        if (msgDO == null) {
            try {
                ALog.e(TAG, "reportMethod msg null", new Object[0]);
            } catch (Throwable th) {
                AppMonitorAdapter.commitCount("accs", "error", th.toString(), 0.0d);
            }
        } else {
            ACCSManager.AccsRequest accsRequest = new ACCSManager.AccsRequest((String) null, AgooConstants.AGOO_SERVICE_AGOOACK, convertMsgToBytes(msgDO), (String) null, (String) null, (URL) null, (String) null);
            accsRequest.setTag(msgDO.msgIds);
            String sendPushResponse = ACCSManager.getAccsInstance(mContext, Config.getAgooAppKey(mContext), Config.getAccsConfigTag(mContext)).sendPushResponse(mContext, accsRequest, extraInfo);
            if (ALog.isPrintLog(ALog.Level.E)) {
                ALog.e(TAG, AgooConstants.MESSAGE_REPORT, Constants.KEY_DATA_ID, sendPushResponse, "status", msgDO.msgStatus, "errorcode", msgDO.errorCode);
            }
        }
    }

    public void reportThirdPushToken(final String str, final String str2, final boolean z) {
        ThreadPoolExecutorFactory.schedule(new Runnable() {
            public void run() {
                String str;
                try {
                    HashMap hashMap = new HashMap();
                    hashMap.put("thirdTokenType", str2);
                    hashMap.put("token", str);
                    hashMap.put("appkey", Config.getAgooAppKey(NotifManager.mContext));
                    hashMap.put("utdid", AdapterUtilityImpl.getDeviceId(NotifManager.mContext));
                    ALog.d(NotifManager.TAG, "report,utdid=" + AdapterUtilityImpl.getDeviceId(NotifManager.mContext) + ",regId=" + str + ",type=" + str2, new Object[0]);
                    ACCSManager.AccsRequest accsRequest = new ACCSManager.AccsRequest((String) null, "agooTokenReport", new JSONObject(hashMap).toString().getBytes("UTF-8"), (String) null, (String) null, (URL) null, (String) null);
                    IACCSManager accsInstance = ACCSManager.getAccsInstance(NotifManager.mContext, Config.getAgooAppKey(NotifManager.mContext), Config.getAccsConfigTag(NotifManager.mContext));
                    if (z) {
                        str = accsInstance.sendData(NotifManager.mContext, accsRequest);
                    } else {
                        str = accsInstance.sendPushResponse(NotifManager.mContext, accsRequest, new TaoBaseService.ExtraInfo());
                    }
                    if (ALog.isPrintLog(ALog.Level.D)) {
                        ALog.i(NotifManager.TAG, "reportThirdPushToken,dataId=" + str + ",regId=" + str + ",type=" + str2, new Object[0]);
                    }
                } catch (Throwable th) {
                    UTMini.getInstance().commitEvent(AgooConstants.AGOO_EVENT_ID, "reportThirdPushToken", AdapterUtilityImpl.getDeviceId(NotifManager.mContext), th.toString());
                    if (ALog.isPrintLog(ALog.Level.E)) {
                        ALog.e(NotifManager.TAG, "[report] is error", th, new Object[0]);
                    }
                }
            }
        }, 10, TimeUnit.SECONDS);
    }

    public void reportThirdPushToken(String str, String str2, String str3, boolean z) {
        final String str4 = str2;
        final String str5 = str;
        final String str6 = str3;
        final boolean z2 = z;
        ThreadPoolExecutorFactory.schedule(new Runnable() {
            public void run() {
                String str;
                try {
                    HashMap hashMap = new HashMap();
                    hashMap.put("thirdTokenType", str4);
                    hashMap.put("token", str5);
                    hashMap.put("appkey", Config.getAgooAppKey(NotifManager.mContext));
                    hashMap.put("utdid", AdapterUtilityImpl.getDeviceId(NotifManager.mContext));
                    hashMap.put("vendorSdkVersion", str6);
                    ALog.d(NotifManager.TAG, "report,utdid=" + AdapterUtilityImpl.getDeviceId(NotifManager.mContext) + ",regId=" + str5 + ",type=" + str4, new Object[0]);
                    ACCSManager.AccsRequest accsRequest = new ACCSManager.AccsRequest((String) null, "agooTokenReport", new JSONObject(hashMap).toString().getBytes("UTF-8"), (String) null, (String) null, (URL) null, (String) null);
                    IACCSManager accsInstance = ACCSManager.getAccsInstance(NotifManager.mContext, Config.getAgooAppKey(NotifManager.mContext), Config.getAccsConfigTag(NotifManager.mContext));
                    if (z2) {
                        str = accsInstance.sendData(NotifManager.mContext, accsRequest);
                    } else {
                        str = accsInstance.sendPushResponse(NotifManager.mContext, accsRequest, new TaoBaseService.ExtraInfo());
                    }
                    if (ALog.isPrintLog(ALog.Level.D)) {
                        ALog.i(NotifManager.TAG, "reportThirdPushToken,dataId=" + str + ",regId=" + str5 + ",type=" + str4, new Object[0]);
                    }
                } catch (Throwable th) {
                    UTMini.getInstance().commitEvent(AgooConstants.AGOO_EVENT_ID, "reportThirdPushToken", AdapterUtilityImpl.getDeviceId(NotifManager.mContext), th.toString());
                    if (ALog.isPrintLog(ALog.Level.E)) {
                        ALog.e(NotifManager.TAG, "[report] is error", th, new Object[0]);
                    }
                }
            }
        }, 10, TimeUnit.SECONDS);
    }

    public void reportThirdPushToken(String str, String str2) {
        reportThirdPushToken(str, str2, true);
    }

    public void doUninstall(String str, boolean z) {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("pack", str);
            hashMap.put("appkey", Config.getAgooAppKey(mContext));
            hashMap.put("utdid", AdapterUtilityImpl.getDeviceId(mContext));
            ACCSManager.getAccsInstance(mContext, Config.getAgooAppKey(mContext), Config.getAccsConfigTag(mContext)).sendPushResponse(mContext, new ACCSManager.AccsRequest((String) null, "agooKick", new JSONObject(hashMap).toString().getBytes("UTF-8"), (String) null, (String) null, (URL) null, (String) null), new TaoBaseService.ExtraInfo());
        } catch (Throwable th) {
            ALog.e(TAG, "[doUninstall] is error", th, new Object[0]);
        }
    }

    private boolean isAppInstalled(String str) {
        PackageInfo packageInfo;
        try {
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            packageInfo = mContext.getPackageManager().getPackageInfo(str, 0);
            if (packageInfo == null) {
                return false;
            }
            ALog.i(TAG, "isAppInstalled true..", new Object[0]);
            return true;
        } catch (Throwable unused) {
            packageInfo = null;
        }
    }

    private String getVersion(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return BuildConfig.buildJavascriptFrameworkVersion;
            }
            String str2 = mContext.getPackageManager().getPackageInfo(str, 0).versionName;
            ALog.d(TAG, "getVersion###版本号为 : " + str2, new Object[0]);
            return str2;
        } catch (Throwable unused) {
            return BuildConfig.buildJavascriptFrameworkVersion;
        }
    }
}
