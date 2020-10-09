package anetwork.channel.http;

import alimama.com.unwetaologger.base.UNWLogger;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.SessionCenter;
import anet.channel.entity.ENV;
import anet.channel.util.ALog;
import anet.channel.util.Utils;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.cookie.CookieManager;
import anetwork.channel.monitor.Monitor;
import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkSdkSetting implements Serializable {
    public static ENV CURRENT_ENV = ENV.ONLINE;
    private static final String TAG = "anet.NetworkSdkSetting";
    private static Context context;
    private static HashMap<String, Object> initParams = null;
    private static AtomicBoolean isInit = new AtomicBoolean(false);

    public static void init(Context context2) {
        if (context2 != null) {
            try {
                if (isInit.compareAndSet(false, true)) {
                    ALog.e(TAG, "NetworkSdkSetting init", (String) null, new Object[0]);
                    context = context2;
                    GlobalAppRuntimeInfo.setInitTime(System.currentTimeMillis());
                    GlobalAppRuntimeInfo.setContext(context2);
                    initTaobaoAdapter();
                    Monitor.init();
                    NetworkConfigCenter.init();
                    if (!AwcnConfig.isTbNextLaunch()) {
                        CookieManager.setup(context2);
                    }
                    SessionCenter.init(context2);
                }
            } catch (Throwable th) {
                ALog.e(TAG, "Network SDK initial failed!", (String) null, th, new Object[0]);
            }
        }
    }

    public static void init(Application application, HashMap<String, Object> hashMap) {
        try {
            GlobalAppRuntimeInfo.setTtid((String) hashMap.get("ttid"));
            GlobalAppRuntimeInfo.setUtdid((String) hashMap.get("deviceId"));
            String str = (String) hashMap.get(UNWLogger.LOG_VALUE_TYPE_PROCESS);
            if (!TextUtils.isEmpty(str)) {
                GlobalAppRuntimeInfo.setCurrentProcess(str);
            }
            initParams = new HashMap<>(hashMap);
            init(application.getApplicationContext());
            initParams = null;
        } catch (Exception e) {
            ALog.e(TAG, "Network SDK initial failed!", (String) null, e, new Object[0]);
        }
    }

    public static void setTtid(String str) {
        GlobalAppRuntimeInfo.setTtid(str);
    }

    public static Context getContext() {
        return context;
    }

    private static void initTaobaoAdapter() {
        try {
            Utils.invokeStaticMethodThrowException("anet.channel.TaobaoNetworkAdapter", "init", new Class[]{Context.class, HashMap.class}, context, initParams);
            ALog.i(TAG, "init taobao adapter success", (String) null, new Object[0]);
        } catch (Exception e) {
            ALog.i(TAG, "initTaobaoAdapter failed. maybe not taobao app", (String) null, e);
        }
    }
}
