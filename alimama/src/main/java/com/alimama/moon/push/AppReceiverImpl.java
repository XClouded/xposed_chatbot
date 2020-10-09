package com.alimama.moon.push;

import com.alimama.union.app.logger.NewMonitorLogger;
import com.taobao.accs.IAppReceiver;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.login4android.Login;
import com.taobao.orange.accssupport.OrangeAccsService;
import java.util.HashMap;
import java.util.Map;
import org.android.agoo.accs.AgooService;
import org.android.agoo.common.AgooConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppReceiverImpl implements IAppReceiver {
    private static final Map<String, String> SERVICES = new HashMap();
    private static final String TAG = "AppReceiverImpl";
    private static AppReceiverImpl instance;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) AppReceiverImpl.class);

    static {
        SERVICES.put("accs", CallbackService.class.getName());
        SERVICES.put("accs-console", CallbackService.class.getName());
        SERVICES.put(GlobalClientInfo.AGOO_SERVICE_ID, AgooService.class.getName());
        SERVICES.put(AgooConstants.AGOO_SERVICE_AGOOACK, AgooService.class.getName());
        SERVICES.put("agooTokenReport", AgooService.class.getName());
        SERVICES.put("orange", OrangeAccsService.class.getName());
    }

    private AppReceiverImpl() {
    }

    public static AppReceiverImpl getInstance() {
        if (instance == null) {
            instance = new AppReceiverImpl();
        }
        return instance;
    }

    public void onData(String str, String str2, byte[] bArr) {
        logger.info("onData-userId: {}, dataId: {}", (Object) str, (Object) str2);
    }

    public void onBindApp(int i) {
        logger.info("onBindApp-errorCode: {}", (Object) Integer.valueOf(i));
        if (i != 200) {
            String valueOf = String.valueOf(i);
            NewMonitorLogger.Accs.bindAppFailed(TAG, valueOf, "bindAppFailed userid:" + Login.getUserId());
        }
    }

    public void onUnbindApp(int i) {
        logger.info("onUnbindApp-errorCode: {}", (Object) Integer.valueOf(i));
    }

    public void onBindUser(String str, int i) {
        logger.info("onBindUser-userId: {}, errorCode: {}", (Object) str, (Object) Integer.valueOf(i));
        if (i != 200) {
            String valueOf = String.valueOf(i);
            NewMonitorLogger.Accs.bindUserFailed(TAG, valueOf, "bindUserFailed userid:" + Login.getUserId());
        }
    }

    public void onUnbindUser(int i) {
        logger.info("onUnbindUser-errorCode: {}", (Object) Integer.valueOf(i));
    }

    public void onSendData(String str, int i) {
        logger.info("onSendData-dataId: {}, errorCode: {}", (Object) str, (Object) Integer.valueOf(i));
    }

    public String getService(String str) {
        logger.info("getService-serviceId: {}", (Object) str);
        return SERVICES.get(str);
    }

    public Map<String, String> getAllServices() {
        logger.info("getAllServices");
        return SERVICES;
    }
}
