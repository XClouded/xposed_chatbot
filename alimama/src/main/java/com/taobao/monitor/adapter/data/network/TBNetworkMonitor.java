package com.taobao.monitor.adapter.data.network;

import android.text.TextUtils;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.appmonitor.IAppMonitor;
import anet.channel.statist.AlarmObject;
import anet.channel.statist.CountObject;
import anet.channel.statist.RequestStatistic;
import anet.channel.statist.StatObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.monitor.adapter.logger.LoggerAdapter;
import com.taobao.monitor.impl.logger.IDataLogger;
import com.taobao.network.lifecycle.NetworkLifecycleManager;
import com.taobao.network.lifecycle.Subject;
import java.util.HashMap;

public class TBNetworkMonitor {
    private static IDataLogger dataLogger = new LoggerAdapter();

    public static void init() {
        AppMonitor.setApmMonitor(new IAppMonitor() {
            public void commitAlarm(AlarmObject alarmObject) {
            }

            public void commitCount(CountObject countObject) {
            }

            public void register() {
            }

            public void register(Class<?> cls) {
            }

            public void commitStat(StatObject statObject) {
                if (statObject instanceof RequestStatistic) {
                    TBNetworkMonitor.transformPojo2Events((RequestStatistic) statObject);
                    Subject.instance().notify(statObject);
                }
            }
        });
    }

    public static void transformPojo2Events(RequestStatistic requestStatistic) {
        if (requestStatistic != null) {
            try {
                String jSONString = JSON.toJSONString(requestStatistic);
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("procedureName", (Object) "NetworkLib");
                jSONObject.put("stage", (Object) "procedureSuccess");
                jSONObject.put("content", (Object) jSONString);
                dataLogger.log("network", jSONObject.toJSONString());
            } catch (Exception unused) {
            }
            String str = requestStatistic.url;
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append((str + System.currentTimeMillis()).hashCode());
            String sb2 = sb.toString();
            if (!TextUtils.isEmpty(sb2)) {
                HashMap hashMap = new HashMap();
                hashMap.put("timestamp", Long.valueOf(requestStatistic.start - requestStatistic.retryCostTime));
                NetworkLifecycleManager.instance().onRequest(sb2, str, hashMap);
                HashMap hashMap2 = new HashMap();
                hashMap2.put("timestamp", Long.valueOf(requestStatistic.start));
                NetworkLifecycleManager.instance().onValidRequest(sb2, str, hashMap2);
                HashMap hashMap3 = new HashMap();
                hashMap3.put("timestamp", Long.valueOf(requestStatistic.reqStart));
                NetworkLifecycleManager.instance().onEvent(sb2, "data_request", hashMap3);
                HashMap hashMap4 = new HashMap();
                hashMap4.put("timestamp", Long.valueOf(requestStatistic.rspStart));
                NetworkLifecycleManager.instance().onEvent(sb2, "first_package_response", hashMap4);
                HashMap hashMap5 = new HashMap();
                hashMap5.put("timestamp", Long.valueOf(requestStatistic.rspEnd));
                hashMap5.put("statusCode", Integer.valueOf(requestStatistic.statusCode));
                hashMap5.put("tnetErrorCode", Integer.valueOf(requestStatistic.tnetErrorCode));
                NetworkLifecycleManager.instance().onFinished(sb2, hashMap5);
            }
        }
    }
}
