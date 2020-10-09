package com.alibaba.android.anynetwork.core;

import com.alibaba.android.anynetwork.core.common.ANConstants;
import com.alibaba.android.anynetwork.core.utils.ILogProxy;
import com.alibaba.android.anynetwork.core.utils.Utils;
import java.util.HashMap;

public class ANConfig {
    public static final String DEBUG = "debug";
    public static final String LOG_PROXY = "log_proxy";
    public static final String NETWORK_MTOP_APP_KEY = "network_mtop_app_key";
    public static final String NETWORK_MTOP_APP_NAME = "network_mtop_app_name";
    public static final String NETWORK_MTOP_APP_SECRET = "network_mtop_app_secret";
    public static final String NETWORK_MTOP_APP_VERSION = "network_mtop_app_version";
    public static final String NETWORK_MTOP_CA_PATH = "network_mtop_ca_path";
    public static final String NETWORK_MTOP_DEVICEID = "network_mtop_deviceid";
    public static final String NETWORK_MTOP_ECODE = "network_mtop_ecode";
    public static final String NETWORK_MTOP_ENVIRONMENT = "network_mtop_environment";
    public static final String NETWORK_MTOP_IMEI = "network_mtop_imei";
    public static final String NETWORK_MTOP_IMSI = "network_mtop_imsi";
    public static final String NETWORK_MTOP_SID = "network_mtop_sid";
    public static final String NETWORK_MTOP_TTID = "network_mtop_ttid";
    private static HashMap<String, Class> mTypeCheckMap = new HashMap<>();
    private HashMap<String, Object> mPropertyMap = new HashMap<>();

    public interface MtopEnvironment {
        public static final int DAILY = 2;
        public static final int DAILY_TWO = 3;
        public static final int DEBUG = 1;
        public static final int RELEASE = 4;
    }

    public void update() {
        AnyNetworkManager.setConfig(this);
    }

    public ANConfig setProperty(String str, Object obj) {
        Class<?> cls;
        if (str == null) {
            throw new IllegalArgumentException("Property key can't be null");
        } else if (obj == null) {
            this.mPropertyMap.put(str, obj);
            return this;
        } else if (!ANConstants.DEBUG || (cls = mTypeCheckMap.get(str)) == null || obj.getClass() == cls) {
            this.mPropertyMap.put(str, obj);
            return this;
        } else {
            throw new IllegalArgumentException("Excepted " + cls + " for " + str + " but is " + obj.getClass());
        }
    }

    public Object getProperty(String str) {
        return this.mPropertyMap.get(str);
    }

    public boolean isDebug() {
        return Utils.getBoolean(getProperty("debug"), false);
    }

    public ANConfig setDebug(boolean z) {
        setProperty("debug", Boolean.valueOf(z));
        return this;
    }

    public ILogProxy getLogProxy() {
        return (ILogProxy) getProperty(LOG_PROXY);
    }

    public ANConfig setLogProxy(ILogProxy iLogProxy) {
        setProperty(LOG_PROXY, iLogProxy);
        return this;
    }

    public ANConfig setNetworkMtopEnvironment(int i) {
        setProperty(NETWORK_MTOP_ENVIRONMENT, Integer.valueOf(i));
        return this;
    }

    public int getNetworkMtopEnvironment() {
        return Utils.getInt(getProperty(NETWORK_MTOP_ENVIRONMENT), 4);
    }

    public ANConfig setNetworkMtopAppKey(String str) {
        setProperty(NETWORK_MTOP_APP_KEY, str);
        return this;
    }

    public String getNetworkMtopAppKey() {
        return (String) getProperty(NETWORK_MTOP_APP_KEY);
    }

    public ANConfig setNetworkMtopAppSecret(String str) {
        setProperty(NETWORK_MTOP_APP_SECRET, str);
        return this;
    }

    public String getNetworkMtopAppSecret() {
        return (String) getProperty(NETWORK_MTOP_APP_SECRET);
    }

    public ANConfig setNetworkMtopImei(String str) {
        setProperty(NETWORK_MTOP_IMEI, str);
        return this;
    }

    public String getNetworkMtopImei() {
        return (String) getProperty(NETWORK_MTOP_IMEI);
    }

    public ANConfig setNetworkMtopImsi(String str) {
        setProperty(NETWORK_MTOP_IMSI, str);
        return this;
    }

    public String getNetworkMtopImsi() {
        return (String) getProperty(NETWORK_MTOP_IMSI);
    }

    public ANConfig setNetworkMtopTtid(String str) {
        setProperty(NETWORK_MTOP_TTID, str);
        return this;
    }

    public String getNetworkMtopTtid() {
        return (String) getProperty(NETWORK_MTOP_TTID);
    }

    public ANConfig setNetworkMtopDeviceid(String str) {
        setProperty(NETWORK_MTOP_DEVICEID, str);
        return this;
    }

    public String getNetworkMtopDeviceid() {
        return (String) getProperty(NETWORK_MTOP_DEVICEID);
    }

    public ANConfig setNetworkMtopSid(String str) {
        setProperty(NETWORK_MTOP_SID, str);
        return this;
    }

    public String getNetworkMtopSid() {
        return (String) getProperty(NETWORK_MTOP_SID);
    }

    public ANConfig setNetworkMtopEcode(String str) {
        setProperty(NETWORK_MTOP_ECODE, str);
        return this;
    }

    public String getNetworkMtopEcode() {
        return (String) getProperty(NETWORK_MTOP_ECODE);
    }

    public ANConfig setNetworkMtopAppName(String str) {
        setProperty(NETWORK_MTOP_APP_NAME, str);
        return this;
    }

    public String getNetworkMtopAppName() {
        return (String) getProperty(NETWORK_MTOP_APP_NAME);
    }

    public ANConfig setNetworkMtopAppVersion(String str) {
        setProperty(NETWORK_MTOP_APP_VERSION, str);
        return this;
    }

    public String getNetworkMtopAppVersion() {
        return (String) getProperty(NETWORK_MTOP_APP_VERSION);
    }

    public ANConfig setNetworkMtopCAPath(String str) {
        setProperty(NETWORK_MTOP_CA_PATH, str);
        return this;
    }

    public String getNetworkMtopCAPath() {
        return (String) getProperty(NETWORK_MTOP_CA_PATH);
    }

    public String toString() {
        return "ANConfig{" + this.mPropertyMap.toString() + "}";
    }
}
