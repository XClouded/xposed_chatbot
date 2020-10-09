package com.alimama.union.app.configproperties;

import android.content.Context;
import android.util.Log;
import com.alimama.moon.utils.StringUtil;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigProperties {
    private static final String KEY_API_ENVIRONMENT = "Env";
    private static final String KEY_SWITCH_RELEASE = "switch_release";
    private static final String PROPERTIES_CONFIG_PATH = "config/config.ini";
    private static final String TAG = "ConfigProperties";
    private static final Map<String, String> sMap = new LinkedHashMap();

    private ConfigProperties() {
    }

    public static void init(Context context) {
        loadProperties(context, PROPERTIES_CONFIG_PATH);
    }

    private static void loadProperties(Context context, String str) {
        Properties properties = new Properties();
        try {
            properties.load(context.getAssets().open(str));
            for (Map.Entry entry : properties.entrySet()) {
                sMap.put(entry.getKey().toString(), entry.getValue().toString());
            }
        } catch (IOException e) {
            Log.d(TAG, "load error:" + str);
            e.printStackTrace();
        }
    }

    public static String getApiEnvironment() {
        return (String) StringUtil.optVal(sMap.get(KEY_API_ENVIRONMENT), "");
    }

    public static boolean isRelease() {
        return "1".equals(sMap.get(KEY_SWITCH_RELEASE));
    }
}
