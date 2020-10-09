package android.taobao.windvane.config;

import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.ConfigStorage;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

public class ModuleConfig {
    private static final String SPNAME = "ModuleConfig";
    public boolean url_updateConfig;

    private ModuleConfig() {
        this.url_updateConfig = false;
    }

    private static class SingletonHolder {
        public static final ModuleConfig instance = getInstance();

        private SingletonHolder() {
        }

        private static ModuleConfig getInstance() {
            ModuleConfig moduleConfig = new ModuleConfig();
            try {
                String stringVal = ConfigStorage.getStringVal(ModuleConfig.SPNAME, ConfigStorage.KEY_DATA);
                if (!TextUtils.isEmpty(stringVal)) {
                    JSONObject jSONObject = new JSONObject(stringVal);
                    for (Field field : moduleConfig.getClass().getFields()) {
                        field.setBoolean(moduleConfig, jSONObject.getBoolean(field.getName()));
                    }
                }
            } catch (Exception unused) {
            }
            return moduleConfig;
        }
    }

    public static ModuleConfig getInstance() {
        return SingletonHolder.instance;
    }

    public void saveConfig() {
        final JSONObject jSONObject = new JSONObject();
        for (Field field : getClass().getFields()) {
            try {
                jSONObject.put(field.getName(), field.getBoolean(this));
            } catch (IllegalAccessException unused) {
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        WVThreadPool.getInstance().execute(new Runnable() {
            public void run() {
                ConfigStorage.putStringVal(ModuleConfig.SPNAME, ConfigStorage.KEY_DATA, jSONObject.toString());
            }
        });
    }
}
