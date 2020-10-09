package android.taobao.windvane.config;

import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;
import com.taobao.weex.el.parse.Operators;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class WVCookieConfig implements IConfig {
    private static final String TAG = "WVCookieConfig";
    private static volatile WVCookieConfig instance;
    public String cookieBlackList = "";
    private AtomicBoolean inited = new AtomicBoolean(false);

    public static WVCookieConfig getInstance() {
        if (instance == null) {
            synchronized (WVCookieConfig.class) {
                if (instance == null) {
                    instance = new WVCookieConfig();
                }
            }
        }
        return instance;
    }

    public void init() {
        if (this.inited.compareAndSet(false, true)) {
            String stringVal = ConfigStorage.getStringVal(WVConfigManager.SPNAME_CONFIG, WVConfigManager.CONFIGNAME_COOKIE);
            TaoLog.i(TAG, "get cookie config local = [" + stringVal + Operators.ARRAY_END_STR);
            parseConfig(stringVal);
            try {
                OrangeConfig.getInstance().registerListener(new String[]{WVConfigManager.CONFIGNAME_COOKIE}, (OrangeConfigListenerV1) new OrangeConfigListenerV1() {
                    public void onConfigUpdate(String str, boolean z) {
                        Map<String, String> configs = OrangeConfig.getInstance().getConfigs(str);
                        if (configs.size() == 0) {
                            WVConfigManager.getInstance().updateConfigByKey(str, "");
                            return;
                        }
                        JSONObject jSONObject = new JSONObject();
                        try {
                            for (Map.Entry next : configs.entrySet()) {
                                jSONObject.put((String) next.getKey(), next.getValue());
                            }
                            jSONObject.put("appVersion", GlobalConfig.getInstance().getAppVersion());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        WVConfigManager.getInstance().updateConfigByKey(str, jSONObject.toString());
                        TaoLog.i("WVConfig", "receive name=[" + str + "]; config=[" + jSONObject.toString() + Operators.ARRAY_END_STR);
                    }
                });
            } catch (Throwable unused) {
            }
        }
    }

    private void parseConfig(String str) {
        if (!TextUtils.isEmpty(str)) {
            JSONObject jSONObject = null;
            try {
                jSONObject = new JSONObject(str);
            } catch (JSONException unused) {
            }
            if (jSONObject != null) {
                this.cookieBlackList = jSONObject.optString("cookieBlackList", this.cookieBlackList);
            }
        }
    }

    public void setConfig(String str) {
        TaoLog.i(TAG, "receive cookie config = [" + str + Operators.ARRAY_END_STR);
        parseConfig(str);
        ConfigStorage.putStringVal(WVConfigManager.SPNAME_CONFIG, WVConfigManager.CONFIGNAME_COOKIE, str);
    }

    public boolean hasInited() {
        return this.inited.get();
    }
}
