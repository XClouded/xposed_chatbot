package android.taobao.windvane.packageapp.adaptive;

import com.taobao.zcache.config.IZCacheUpdate;

import java.util.HashMap;
import java.util.Map;

public class ZCacheAdapter implements IZCacheUpdate {
    private static final String TAG = "ZCacheUpdate";
    private Map<String, String> packPathMap = new HashMap();

    public void firstUpdateCount(int i) {
    }
}
