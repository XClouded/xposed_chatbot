package com.taobao.monitor.adapter;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.taobao.login4android.api.Login;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.broadcast.LoginBroadcastHelper;
import com.taobao.login4android.session.SessionManager;
import com.taobao.monitor.adapter.common.TBAPMConstants;
import com.taobao.monitor.common.ThreadUtils;
import com.taobao.monitor.impl.common.DynamicConstants;
import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.impl.logger.Logger;
import com.taobao.monitor.impl.util.TimeUtils;
import com.taobao.monitor.procedure.Header;
import com.taobao.orange.OConfigListener;
import com.taobao.orange.OrangeConfig;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TBAPMAdapterLauncherPart2 implements Serializable {
    private static final float DEFAULT_SAMPLE = 1.0f;
    private static final String FRAGMENT_PAGE_LOAD_POP_SAMPLE = "fragment_page_load_pop_sample";
    private static final String FRAGMENT_PAGE_LOAD_SAMPLE = "fragment_page_load_sample";
    private static final String GLOBAL_SAMPLE = "global_sample";
    private static final String IMAGE_PROCESSOR_SAMPLE = "image_processor_sample";
    private static final String IS_APM = "isApm";
    private static final String LAUNCHER_PROCESSOR_SAMPLE = "launcher_sample";
    private static final String NEED_START_ACTIVITY_TRACE_SWITCH = "need_start_activity_trace_switch";
    private static final String NETWORK_PROCESSOR_SAMPLE = "network_processor_sample";
    private static final String NETWORK_SAMPLE = "network_sample";
    private static final String ORANGE_NAMESPACE = "applicationmonitor";
    private static final String PAGE_LOAD_POP_SAMPLE = "page_load_pop_sample";
    private static final String PAGE_LOAD_SAMPLE = "page_load_sample";
    private static final String TAG = "TBAPMAdapterLauncherPart2";
    private static final String USE_NEW_APM_SAMPLE = "use_new_apm_sample";
    private static final String WEEX_PROCESSOR_SAMPLE = "weex_processor_sample";
    private static boolean init = false;
    private long apmStartTime = TimeUtils.currentTimeMillis();

    public void init(Application application, HashMap<String, Object> hashMap) {
        if (!init) {
            init = true;
            Logger.i(TAG, "init start");
            if (TBAPMConstants.open) {
                initAPMFunction(application, hashMap);
            }
            configOrange();
            Logger.i(TAG, "init end");
        }
        Logger.i(TAG, "apmStartTime:" + (TimeUtils.currentTimeMillis() - this.apmStartTime));
    }

    private void initAPMFunction(Application application, HashMap<String, Object> hashMap) {
        initLogin(application);
    }

    private void configOrange() {
        ThreadUtils.start(new Runnable() {
            public void run() {
                OrangeConfig.getInstance().getConfigs(TBAPMAdapterLauncherPart2.ORANGE_NAMESPACE);
                OrangeConfig.getInstance().registerListener(new String[]{TBAPMAdapterLauncherPart2.ORANGE_NAMESPACE}, new OrangeListener(), true);
            }
        });
    }

    private void initLogin(final Context context) {
        ThreadUtils.start(new Runnable() {
            public void run() {
                Header.userNick = SessionManager.getInstance(context).getNick();
                Header.userId = SessionManager.getInstance(context).getUserId();
            }
        });
        LoginBroadcastHelper.registerLoginReceiver(context, new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null && LoginAction.valueOf(intent.getAction()) != null) {
                    Header.userNick = Login.getNick();
                    Header.userId = Login.getUserId();
                }
            }
        });
    }

    private static class OrangeListener implements OConfigListener {
        private OrangeListener() {
        }

        public void onConfigUpdate(String str, Map<String, String> map) {
            Map<String, String> configs = OrangeConfig.getInstance().getConfigs(TBAPMAdapterLauncherPart2.ORANGE_NAMESPACE);
            if (configs != null && configs.size() > 0) {
                onUpdate(configs);
            }
        }

        private void onUpdate(Map<String, String> map) {
            float nextFloat = new Random(System.currentTimeMillis()).nextFloat();
            boolean z = false;
            boolean z2 = nextFloat < getSafeFloat(map.get(TBAPMAdapterLauncherPart2.GLOBAL_SAMPLE));
            TBAPMConstants.needUpdateData = nextFloat < getSafeFloat(map.get(TBAPMAdapterLauncherPart2.NETWORK_SAMPLE)) && z2;
            DynamicConstants.needLauncher = nextFloat < getSafeFloat(map.get(TBAPMAdapterLauncherPart2.LAUNCHER_PROCESSOR_SAMPLE)) && z2;
            DynamicConstants.needPageLoad = nextFloat < getSafeFloat(map.get(TBAPMAdapterLauncherPart2.PAGE_LOAD_SAMPLE)) && z2;
            DynamicConstants.needPageLoadPop = nextFloat < getSafeFloat(map.get(TBAPMAdapterLauncherPart2.PAGE_LOAD_POP_SAMPLE)) && z2;
            DynamicConstants.needFragment = nextFloat < getSafeFloat(map.get(TBAPMAdapterLauncherPart2.FRAGMENT_PAGE_LOAD_SAMPLE)) && z2;
            DynamicConstants.needFragmentPop = nextFloat < getSafeFloat(map.get(TBAPMAdapterLauncherPart2.FRAGMENT_PAGE_LOAD_POP_SAMPLE)) && z2;
            DynamicConstants.needNetwork = nextFloat < getSafeFloat(map.get(TBAPMAdapterLauncherPart2.NETWORK_PROCESSOR_SAMPLE)) && z2;
            DynamicConstants.needImage = nextFloat < getSafeFloat(map.get(TBAPMAdapterLauncherPart2.IMAGE_PROCESSOR_SAMPLE)) && z2;
            DynamicConstants.needWeex = nextFloat < getSafeFloat(map.get(TBAPMAdapterLauncherPart2.WEEX_PROCESSOR_SAMPLE)) && z2;
            if ("true".equals(map.get(TBAPMAdapterLauncherPart2.NEED_START_ACTIVITY_TRACE_SWITCH))) {
                DynamicConstants.needStartActivityTrace = true;
            } else {
                DynamicConstants.needStartActivityTrace = false;
            }
            DynamicConstants.needNewApm = nextFloat < getSafeFloat(map.get(TBAPMAdapterLauncherPart2.USE_NEW_APM_SAMPLE)) && z2;
            SharedPreferences sharedPreferences = Global.instance().context().getSharedPreferences("apm", 0);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            String str = map.get(TBAPMAdapterLauncherPart2.IS_APM);
            boolean z3 = TextUtils.isEmpty(str) || !str.equalsIgnoreCase("close");
            if (z3 != sharedPreferences.getBoolean(TBAPMAdapterLauncherPart2.IS_APM, true)) {
                edit.putBoolean(TBAPMAdapterLauncherPart2.IS_APM, z3);
                z = true;
            }
            if (z) {
                edit.commit();
            }
        }

        private float getSafeFloat(String str) {
            try {
                if (!TextUtils.isEmpty(str)) {
                    return Float.valueOf(str).floatValue();
                }
                return 1.0f;
            } catch (Exception unused) {
                return 1.0f;
            }
        }
    }
}
