package com.taobao.phenix.compat;

import android.content.Context;
import anet.channel.monitor.INetworkQualityChangeListener;
import anet.channel.monitor.NetworkSpeed;
import anet.channel.monitor.QualityChangeFilter;
import anetwork.channel.monitor.Monitor;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.compat.mtop.MtopHttpLoader;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.loader.network.HttpLoader;
import com.taobao.phenix.loader.network.NetworkQualityListener;
import com.taobao.rxm.schedule.SchedulerSupplier;

public class TBNetwork4Phenix {
    private static boolean sInited;

    public static void setupHttpLoader(Context context) {
        try {
            Phenix.instance().httpLoaderBuilder().with((HttpLoader) new MtopHttpLoader(context));
            sInited = true;
            UnitedLog.i("TBNetwork4Phenix", "http loader setup", new Object[0]);
        } catch (RuntimeException e) {
            UnitedLog.e("TBNetwork4Phenix", "http loader setup error=%s", e);
        }
    }

    public static void setupQualityChangedMonitor() {
        if (sInited) {
            Monitor.addListener(new INetworkQualityChangeListener() {
                public void onNetworkQualityChanged(NetworkSpeed networkSpeed) {
                    boolean z = true;
                    UnitedLog.d("Network", "network speed detect: %K/s", Integer.valueOf((int) (Monitor.getNetSpeedValue() * 1024.0d)));
                    SchedulerSupplier build = Phenix.instance().schedulerBuilder().build();
                    if (build instanceof NetworkQualityListener) {
                        NetworkQualityListener networkQualityListener = (NetworkQualityListener) build;
                        if (networkSpeed != NetworkSpeed.Slow) {
                            z = false;
                        }
                        networkQualityListener.onNetworkQualityChanged(z);
                    }
                }
            }, new QualityChangeFilter() {
                public boolean detectNetSpeedSlow(double d) {
                    return d <= 30.0d;
                }
            });
            UnitedLog.i("TBNetwork4Phenix", "network quality monitor setup", new Object[0]);
        }
    }
}
