package com.alibaba.android.umbrella.trace;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import androidx.annotation.Keep;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.android.umbrella.trace.UmbrellaInfo;
import com.alibaba.mtl.appmonitor.AppMonitor;
import java.util.HashMap;
import java.util.Map;

@Keep
public class UmbrellaTracker {
    public static final String ERROR_BUSINESS_TYPE = "UMBRELLA_ERROR";
    public static final String PURCHASE_MODULE = "Page_Trade_Govern";
    public static final String PURCHASE_POINT_POST = "_Service";
    public static final String PURCHASE_POINT_PRE = "Monitor_";
    private static int currIndex = 1;
    private static HashMap<String, Integer> identifyMap = new HashMap<>();
    private static Handler sHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.obj instanceof UmbrellaInfo) {
                UmbrellaInfo umbrellaInfo = (UmbrellaInfo) message.obj;
                String str = "";
                String str2 = "";
                if (umbrellaInfo.args != null) {
                    str = umbrellaInfo.args.get("errorCode");
                    str2 = umbrellaInfo.args.get(ILocatable.ERROR_MSG);
                }
                UmbrellaTracker.commitFailureStability(umbrellaInfo, str, str2);
            }
        }
    };

    public static void commitSuccessStability(UmbrellaInfo umbrellaInfo) {
        if (!UmbrellaSimple.isForceCloseSuccess() && umbrellaInfo != null && !TextUtils.isEmpty(umbrellaInfo.mainBizName) && UmbrellaSimple.getSampleResult(umbrellaInfo)) {
            AppMonitor.Alarm.commitSuccess(PURCHASE_MODULE, "Monitor_" + umbrellaInfo.mainBizName + "_Service", umbrellaInfo.toJsonString());
        }
    }

    public static void commitSuccessStability(String str, String str2, String str3, String str4, String str5, Map<String, String> map) {
        if (!UmbrellaSimple.isForceCloseSuccess()) {
            UmbrellaInfo.UmbrellaBuilder umbrellaBuilder = new UmbrellaInfo.UmbrellaBuilder(str2, str3, str, str4, str5);
            umbrellaBuilder.setVersion(str3).setParams(map);
            commitSuccessStability(umbrellaBuilder.build());
        }
    }

    public static void commitFailureStability(UmbrellaInfo umbrellaInfo, String str, String str2) {
        if (!UmbrellaSimple.isForceCloseFailure() && !UmbrellaUtils.isFlowControl(str) && !TextUtils.isEmpty(str) && umbrellaInfo != null && UmbrellaSimple.getFailSampleResult(umbrellaInfo, str)) {
            AppMonitor.Alarm.commitFail(PURCHASE_MODULE, "Monitor_" + umbrellaInfo.mainBizName + "_Service", umbrellaInfo.toJsonString(), str, str2);
        }
    }

    public static void commitFailureStability(String str, String str2, String str3, String str4, String str5, Map<String, String> map, String str6, String str7) {
        if (!UmbrellaSimple.isForceCloseFailure()) {
            UmbrellaInfo.UmbrellaBuilder umbrellaBuilder = new UmbrellaInfo.UmbrellaBuilder(str2, str3, str, str4, str5);
            umbrellaBuilder.setVersion(str3).setParams(map);
            commitFailureStability(umbrellaBuilder.build(), str6, str7);
        }
    }

    private static int getMessageIndex(String str) {
        Integer num = identifyMap.get(str);
        if (num == null) {
            num = Integer.valueOf(currIndex);
            identifyMap.put(str, Integer.valueOf(currIndex));
            currIndex++;
        }
        return num.intValue();
    }

    private static String getIdentify(String str, String str2, String str3) {
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        if (str3 == null) {
            str3 = "";
        }
        return str + "_" + str2 + "_" + str3;
    }

    public static void traceProcessBegin(String str, String str2, String str3, String str4, String str5, Map<String, String> map, String str6, String str7, long j) {
        if (!UmbrellaSimple.isForceCloseFailure()) {
            int messageIndex = getMessageIndex(getIdentify(str, str2, str3));
            UmbrellaInfo.UmbrellaBuilder umbrellaBuilder = new UmbrellaInfo.UmbrellaBuilder(str2, str3, str, str4, str5);
            HashMap hashMap = new HashMap();
            String str8 = str6;
            hashMap.put("errorCode", str6);
            String str9 = str7;
            hashMap.put(ILocatable.ERROR_MSG, str7);
            umbrellaBuilder.setParams(hashMap);
            String str10 = str3;
            Map<String, String> map2 = map;
            umbrellaBuilder.setVersion(str3).setParams(map);
            Message obtainMessage = sHandler.obtainMessage(messageIndex);
            obtainMessage.obj = umbrellaBuilder.build();
            sHandler.removeMessages(messageIndex);
            sHandler.sendMessageDelayed(obtainMessage, j);
        }
    }

    public static void traceProcessEnd(String str, String str2, String str3) {
        if (!UmbrellaSimple.isForceCloseFailure()) {
            sHandler.removeMessages(getMessageIndex(getIdentify(str, str2, str3)));
        }
    }
}
