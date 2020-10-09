package anet.channel.monitor;

import anet.channel.util.ALog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BandWidthListenerHelper {
    private static final String TAG = "BandWidthListenerHelp";
    private static volatile BandWidthListenerHelper instance;
    private QualityChangeFilter defaultFilter = new QualityChangeFilter();
    private Map<INetworkQualityChangeListener, QualityChangeFilter> qualityListeners = new ConcurrentHashMap();

    private BandWidthListenerHelper() {
    }

    public static BandWidthListenerHelper getInstance() {
        if (instance == null) {
            synchronized (BandWidthListenerHelper.class) {
                if (instance == null) {
                    instance = new BandWidthListenerHelper();
                }
            }
        }
        return instance;
    }

    public void addQualityChangeListener(INetworkQualityChangeListener iNetworkQualityChangeListener, QualityChangeFilter qualityChangeFilter) {
        if (iNetworkQualityChangeListener == null) {
            ALog.e(TAG, "listener is null", (String) null, new Object[0]);
        } else if (qualityChangeFilter == null) {
            this.defaultFilter.filterAddTime = System.currentTimeMillis();
            this.qualityListeners.put(iNetworkQualityChangeListener, this.defaultFilter);
        } else {
            qualityChangeFilter.filterAddTime = System.currentTimeMillis();
            this.qualityListeners.put(iNetworkQualityChangeListener, qualityChangeFilter);
        }
    }

    public void removeQualityChangeListener(INetworkQualityChangeListener iNetworkQualityChangeListener) {
        this.qualityListeners.remove(iNetworkQualityChangeListener);
    }

    public void onNetworkSpeedValueNotify(double d) {
        boolean detectNetSpeedSlow;
        for (Map.Entry next : this.qualityListeners.entrySet()) {
            INetworkQualityChangeListener iNetworkQualityChangeListener = (INetworkQualityChangeListener) next.getKey();
            QualityChangeFilter qualityChangeFilter = (QualityChangeFilter) next.getValue();
            if (!(iNetworkQualityChangeListener == null || qualityChangeFilter == null || qualityChangeFilter.checkShouldDelay() || qualityChangeFilter.isNetSpeedSlow() == (detectNetSpeedSlow = qualityChangeFilter.detectNetSpeedSlow(d)))) {
                qualityChangeFilter.setNetSpeedSlow(detectNetSpeedSlow);
                iNetworkQualityChangeListener.onNetworkQualityChanged(detectNetSpeedSlow ? NetworkSpeed.Slow : NetworkSpeed.Fast);
            }
        }
    }
}
