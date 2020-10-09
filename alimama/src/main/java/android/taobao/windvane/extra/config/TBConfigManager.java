package android.taobao.windvane.extra.config;

import android.content.Context;

import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;

public class TBConfigManager {
    public static final String ANDROID_WINDVANE_CONFIG = "android_windvane_config";
    public static final String WINDVANE_COMMMON_CONFIG = "WindVane";
    private static volatile TBConfigManager instance;
    private OrangeConfigListenerV1 mConfigListenerV1 = null;

    public static TBConfigManager getInstance() {
        if (instance == null) {
            synchronized (TBConfigManager.class) {
                if (instance == null) {
                    instance = new TBConfigManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        if (this.mConfigListenerV1 == null) {
            try {
                String[] strArr = {ANDROID_WINDVANE_CONFIG, WINDVANE_COMMMON_CONFIG};
                this.mConfigListenerV1 = new TBConfigListenerV1();
                OrangeConfig.getInstance().registerListener(strArr, this.mConfigListenerV1);
            } catch (Throwable unused) {
                this.mConfigListenerV1 = null;
            }
        }
    }
}
