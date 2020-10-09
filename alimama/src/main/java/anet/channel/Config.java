package anet.channel;

import android.text.TextUtils;
import anet.channel.entity.ENV;
import anet.channel.security.ISecurity;
import anet.channel.security.SecurityManager;
import anet.channel.util.ALog;
import anet.channel.util.StringUtils;
import java.util.HashMap;
import java.util.Map;

public final class Config {
    public static final Config DEFAULT_CONFIG = new Builder().setTag("[default]").setAppkey("[default]").setEnv(ENV.ONLINE).build();
    private static final String TAG = "awcn.Config";
    /* access modifiers changed from: private */
    public static Map<String, Config> configMap = new HashMap();
    /* access modifiers changed from: private */
    public String appkey;
    /* access modifiers changed from: private */
    public ENV env = ENV.ONLINE;
    /* access modifiers changed from: private */
    public ISecurity iSecurity;
    /* access modifiers changed from: private */
    public String tag;

    protected Config() {
    }

    public static Config getConfigByTag(String str) {
        Config config;
        synchronized (configMap) {
            config = configMap.get(str);
        }
        return config;
    }

    public static Config getConfig(String str, ENV env2) {
        synchronized (configMap) {
            for (Config next : configMap.values()) {
                if (next.env == env2 && next.appkey.equals(str)) {
                    return next;
                }
            }
            return null;
        }
    }

    public String getTag() {
        return this.tag;
    }

    public String getAppkey() {
        return this.appkey;
    }

    public ENV getEnv() {
        return this.env;
    }

    public ISecurity getSecurity() {
        return this.iSecurity;
    }

    public String toString() {
        return this.tag;
    }

    public static class Builder {
        private String appSecret;
        private String appkey;
        private String authCode;
        private ENV env = ENV.ONLINE;
        private String tag;

        public Builder setTag(String str) {
            this.tag = str;
            return this;
        }

        public Builder setAppkey(String str) {
            this.appkey = str;
            return this;
        }

        public Builder setEnv(ENV env2) {
            this.env = env2;
            return this;
        }

        public Builder setAuthCode(String str) {
            this.authCode = str;
            return this;
        }

        public Builder setAppSecret(String str) {
            this.appSecret = str;
            return this;
        }

        public Config build() {
            if (!TextUtils.isEmpty(this.appkey)) {
                for (Config config : Config.configMap.values()) {
                    if (config.env == this.env && config.appkey.equals(this.appkey)) {
                        ALog.w(Config.TAG, "duplicated config exist!", (String) null, "appkey", this.appkey, "env", this.env);
                        if (!TextUtils.isEmpty(this.tag)) {
                            synchronized (Config.configMap) {
                                Config.configMap.put(this.tag, config);
                            }
                        }
                        return config;
                    }
                }
                Config config2 = new Config();
                String unused = config2.appkey = this.appkey;
                ENV unused2 = config2.env = this.env;
                if (TextUtils.isEmpty(this.tag)) {
                    String unused3 = config2.tag = StringUtils.concatString(this.appkey, "$", this.env.toString());
                } else {
                    String unused4 = config2.tag = this.tag;
                }
                if (!TextUtils.isEmpty(this.appSecret)) {
                    ISecurity unused5 = config2.iSecurity = SecurityManager.getSecurityFactory().createNonSecurity(this.appSecret);
                } else {
                    ISecurity unused6 = config2.iSecurity = SecurityManager.getSecurityFactory().createSecurity(this.authCode);
                }
                synchronized (Config.configMap) {
                    Config.configMap.put(config2.tag, config2);
                }
                return config2;
            }
            throw new RuntimeException("appkey can not be null or empty!");
        }
    }
}
