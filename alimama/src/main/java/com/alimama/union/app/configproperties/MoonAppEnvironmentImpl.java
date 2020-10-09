package com.alimama.union.app.configproperties;

import alimama.com.unwbase.interfaces.IAppEnvironment;
import com.alimama.moon.BuildConfig;

public class MoonAppEnvironmentImpl implements IAppEnvironment {
    public String getChannelId() {
        return BuildConfig.CHANNEL;
    }

    public String getTTid() {
        return BuildConfig.TTID;
    }

    public void setEnv(String str) {
    }

    public void init() {
        EnvHelper.getInstance();
    }

    public int getEnv() {
        return EnvHelper.getInstance().getCurrentApiEnvOfint();
    }

    public boolean isProd() {
        return EnvHelper.getInstance().isOnLineEnv();
    }

    public boolean isPre() {
        return EnvHelper.getInstance().isPre();
    }

    public boolean isDaily() {
        return EnvHelper.getInstance().isDailyEnv();
    }

    public void serializeEnv(String str) {
        EnvHelper.serializeEnv(str);
    }
}
