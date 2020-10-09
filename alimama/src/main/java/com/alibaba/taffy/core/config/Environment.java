package com.alibaba.taffy.core.config;

import android.os.Bundle;
import com.alibaba.taffy.core.util.TLog;
import com.alibaba.taffy.core.util.lang.StringUtil;

public class Environment {
    private static final int DEFAULT_INT_VALUE = 0;
    private static final String DEFAULT_STR_VALUE = null;
    private static final String TAG = "Environment";
    public static final String UNKNOWN_APP_KEY = DEFAULT_STR_VALUE;
    public static final String UNKNOWN_APP_TAG = DEFAULT_STR_VALUE;
    public static final String UNKNOWN_CHANNEL_ID = DEFAULT_STR_VALUE;
    public static final String UNKNOWN_CHANNEL_NAME = DEFAULT_STR_VALUE;
    public static final int UNKNOWN_VERSION_CODE = 0;
    public static final String UNKNOWN_VERSION_NAME = DEFAULT_STR_VALUE;
    private static Environment instance;
    private Bundle mEnvBundle;

    public interface Keys {
        public static final String ENV_APP_KEY = "APP_KEY";
        public static final String ENV_APP_TAG = "APP_TAG";
        public static final String ENV_CHANNEL_ID = "CHANNEL_ID";
        public static final String ENV_CHANNEL_NAME = "CHANNEL_NAME";
        public static final String ENV_MODE = "ENV_MODE";
        public static final String ENV_VERSION_CODE = "VERSION_CODE";
        public static final String ENV_VERSION_NAME = "VERSION_NAME";
    }

    private Environment(Bundle bundle) {
        this.mEnvBundle = bundle;
    }

    public static Environment getInstance() {
        return getInstance((Bundle) null);
    }

    public static Environment getInstance(Bundle bundle) {
        if (instance == null) {
            if (bundle != null) {
                synchronized (Environment.class) {
                    if (instance == null) {
                        instance = new Environment(bundle);
                    }
                }
            } else {
                throw new IllegalArgumentException("in the first time call, bundle cannot be null");
            }
        }
        return instance;
    }

    public int getVersionCode() {
        return getInt(Keys.ENV_VERSION_CODE, 0);
    }

    public String getVersionName() {
        return getString(Keys.ENV_VERSION_NAME, UNKNOWN_VERSION_NAME);
    }

    public String getChannelId() {
        return getString(Keys.ENV_CHANNEL_ID, UNKNOWN_CHANNEL_ID);
    }

    public String getChannelName() {
        return getString(Keys.ENV_CHANNEL_NAME, UNKNOWN_CHANNEL_NAME);
    }

    public String getAppKey() {
        return getString("APP_KEY", UNKNOWN_APP_KEY);
    }

    public String getAppTag() {
        return getString(Keys.ENV_APP_TAG, UNKNOWN_APP_TAG);
    }

    public EnvironmentMode getEnvironmentMode() {
        String string = getString(Keys.ENV_MODE);
        if (StringUtil.isNotBlank(string)) {
            try {
                return EnvironmentMode.valueOf(string);
            } catch (Exception e) {
                TLog.w(TAG, "", e);
            }
        }
        return EnvironmentMode.ONLINE;
    }

    public Object get(String str) {
        if (this.mEnvBundle == null) {
            return null;
        }
        return this.mEnvBundle.get(str);
    }

    public boolean getBoolean(String str) {
        return getBoolean(str, false);
    }

    public boolean getBoolean(String str, boolean z) {
        return this.mEnvBundle != null && this.mEnvBundle.getBoolean(str, z);
    }

    public String getString(String str) {
        return getString(str, DEFAULT_STR_VALUE);
    }

    public String getString(String str, String str2) {
        return this.mEnvBundle == null ? DEFAULT_STR_VALUE : this.mEnvBundle.getString(str, str2);
    }

    public long getLong(String str) {
        return getLong(str, 0);
    }

    public long getLong(String str, long j) {
        if (this.mEnvBundle == null) {
            return 0;
        }
        return this.mEnvBundle.getLong(str, j);
    }

    public int getInt(String str) {
        return getInt(str, 0);
    }

    public int getInt(String str, int i) {
        if (this.mEnvBundle == null) {
            return 0;
        }
        return this.mEnvBundle.getInt(str, i);
    }

    public float getFloat(String str) {
        return getFloat(str, 0.0f);
    }

    public float getFloat(String str, float f) {
        if (this.mEnvBundle == null) {
            return 0.0f;
        }
        return this.mEnvBundle.getFloat(str, f);
    }

    public double getDouble(String str) {
        return getDouble(str, 0.0d);
    }

    public double getDouble(String str, double d) {
        if (this.mEnvBundle == null) {
            return 0.0d;
        }
        return this.mEnvBundle.getDouble(str, d);
    }
}
