package anetwork.channel.monitor;

import android.content.Context;
import anet.channel.monitor.BandWidthListenerHelper;
import anet.channel.monitor.BandWidthSampler;
import anet.channel.monitor.INetworkQualityChangeListener;
import anet.channel.monitor.QualityChangeFilter;
import anet.channel.util.ALog;
import anetwork.channel.monitor.speed.NetworkSpeed;
import java.util.concurrent.atomic.AtomicBoolean;

public class Monitor {
    private static final String TAG = "anet.Monitor";
    static AtomicBoolean isInit = new AtomicBoolean(false);

    public static synchronized void init() {
        synchronized (Monitor.class) {
            if (isInit.compareAndSet(false, true)) {
                BandWidthSampler.getInstance().startNetworkMeter();
            }
        }
    }

    @Deprecated
    public static synchronized void init(Context context) {
        synchronized (Monitor.class) {
            init();
        }
    }

    public static void start() {
        try {
            BandWidthSampler.getInstance().startNetworkMeter();
        } catch (Throwable th) {
            ALog.e(TAG, "start failed", (String) null, th, new Object[0]);
        }
    }

    public static void stop() {
        try {
            BandWidthSampler.getInstance().stopNetworkMeter();
        } catch (Throwable th) {
            ALog.e(TAG, "stop failed", (String) null, th, new Object[0]);
        }
    }

    @Deprecated
    public static NetworkSpeed getNetworkSpeed() {
        return NetworkSpeed.valueOfCode(getNetSpeed().getCode());
    }

    public static anet.channel.monitor.NetworkSpeed getNetSpeed() {
        anet.channel.monitor.NetworkSpeed networkSpeed = anet.channel.monitor.NetworkSpeed.Fast;
        try {
            return anet.channel.monitor.NetworkSpeed.valueOfCode(BandWidthSampler.getInstance().getNetworkSpeed());
        } catch (Throwable th) {
            ALog.e(TAG, "getNetworkSpeed failed", (String) null, th, new Object[0]);
            return networkSpeed;
        }
    }

    public static void addListener(INetworkQualityChangeListener iNetworkQualityChangeListener) {
        addListener(iNetworkQualityChangeListener, (QualityChangeFilter) null);
    }

    public static void addListener(INetworkQualityChangeListener iNetworkQualityChangeListener, QualityChangeFilter qualityChangeFilter) {
        BandWidthListenerHelper.getInstance().addQualityChangeListener(iNetworkQualityChangeListener, qualityChangeFilter);
    }

    public static void removeListener(INetworkQualityChangeListener iNetworkQualityChangeListener) {
        BandWidthListenerHelper.getInstance().removeQualityChangeListener(iNetworkQualityChangeListener);
    }

    public static double getNetSpeedValue() {
        return BandWidthSampler.getInstance().getNetSpeedValue();
    }
}
