package com.alibaba.ha.adapter.plugin;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.ali.telescope.api.Telescope;
import com.ali.telescope.base.plugin.INameConverter;
import com.alibaba.ha.adapter.AliHaAdapter;
import com.alibaba.ha.adapter.Plugin;
import com.alibaba.ha.adapter.service.telescope.TelescopeDataListener;
import com.alibaba.ha.protocol.AliHaParam;
import com.alibaba.ha.protocol.AliHaPlugin;
import com.alibaba.motu.tbrest.utils.AppUtils;
import java.util.concurrent.atomic.AtomicBoolean;

public class TelescopePlugin implements AliHaPlugin {
    AtomicBoolean enabling = new AtomicBoolean(false);

    public String getName() {
        return Plugin.telescope.name();
    }

    public void start(AliHaParam aliHaParam) {
        String str = aliHaParam.appId;
        String str2 = aliHaParam.appKey;
        String str3 = aliHaParam.appVersion;
        Application application = aliHaParam.application;
        Context context = aliHaParam.context;
        if (context == null || application == null || str == null || str2 == null || str3 == null) {
            Log.e(AliHaAdapter.TAG, "param is unlegal, crashreporter plugin start failure ");
            return;
        }
        String myProcessNameByAppProcessInfo = AppUtils.getMyProcessNameByAppProcessInfo(context);
        String str4 = AliHaAdapter.TAG;
        Log.i(str4, "init telescope, appId is " + str + " appKey is " + str2 + " appVersion is " + str3 + " package name " + myProcessNameByAppProcessInfo);
        if (this.enabling.compareAndSet(false, true)) {
            try {
                Telescope.start(new Telescope.TelescopeConfig().application(application).logLevel(3).strictMode(false).appKey(str2).appVersion(str3).packageName(myProcessNameByAppProcessInfo).nameConverter(INameConverter.DEFAULT_CONVERTR).channel(aliHaParam.channel));
                AliHaAdapter.getInstance().telescopeService.addTelescopeDataListener(new TelescopeDataListener());
            } catch (Exception e) {
                Log.e(AliHaAdapter.TAG, "param is unlegal, telescope plugin start failure ", e);
            }
        }
    }
}
