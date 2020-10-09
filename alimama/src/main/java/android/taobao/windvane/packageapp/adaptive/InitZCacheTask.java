package android.taobao.windvane.packageapp.adaptive;

import android.app.Application;
import android.taobao.windvane.util.TaoLog;

import com.taobao.zcache.ZCacheManager;

import java.io.Serializable;
import java.util.HashMap;

public class InitZCacheTask implements Serializable {
    public void init(Application application, HashMap<String, Object> hashMap) {
        if ("3".equals(ZCacheConfigManager.getInstance().getzType())) {
            ZCacheManager.instance().startUpdateQueue();
            TaoLog.i("ZCache", "zcache 3.0 startQueue");
        }
    }
}
