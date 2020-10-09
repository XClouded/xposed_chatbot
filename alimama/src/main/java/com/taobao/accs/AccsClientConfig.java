package com.taobao.accs;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.Utils;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.SymbolExpUtil;

public class AccsClientConfig implements Serializable {
    public static final String[] DEFAULT_CENTER_HOSTS = {"msgacs.m.taobao.com", "msgacs.wapa.taobao.com", "msgacs.waptest.taobao.com"};
    /* access modifiers changed from: private */
    public static final String[] DEFAULT_CHANNEL_HOSTS = {"accscdn.m.taobao.com", "acs.wapa.taobao.com", "acs.waptest.taobao.com"};
    public static final String DEFAULT_CONFIGTAG = "default";
    public static final int SECURITY_OFF = 2;
    public static final int SECURITY_OPEN = 1;
    public static final int SECURITY_TAOBAO = 0;
    private static final String TAG = "AccsClientConfig";
    public static boolean loadedStaticConfig;
    private static Context mContext;
    public static Map<String, AccsClientConfig> mDebugConfigs = new ConcurrentHashMap(1);
    @ENV
    public static int mEnv = 0;
    public static Map<String, AccsClientConfig> mPreviewConfigs = new ConcurrentHashMap(1);
    public static Map<String, AccsClientConfig> mReleaseConfigs = new ConcurrentHashMap(1);
    /* access modifiers changed from: private */
    public boolean mAccsHeartbeatEnable;
    /* access modifiers changed from: private */
    public String mAppKey;
    /* access modifiers changed from: private */
    public String mAppSecret;
    /* access modifiers changed from: private */
    public String mAuthCode;
    /* access modifiers changed from: private */
    public boolean mAutoUnit;
    /* access modifiers changed from: private */
    public String mChannelHost;
    /* access modifiers changed from: private */
    public int mChannelPubKey;
    /* access modifiers changed from: private */
    public int mConfigEnv;
    /* access modifiers changed from: private */
    public boolean mDisableChannel;
    /* access modifiers changed from: private */
    public String mInappHost;
    /* access modifiers changed from: private */
    public int mInappPubKey;
    /* access modifiers changed from: private */
    public boolean mKeepalive;
    /* access modifiers changed from: private */
    public boolean mQuickReconnect;
    /* access modifiers changed from: private */
    public int mSecurity;
    /* access modifiers changed from: private */
    public String mStoreId;
    /* access modifiers changed from: private */
    public String mTag;

    @Retention(RetentionPolicy.CLASS)
    public @interface ENV {
    }

    @Retention(RetentionPolicy.CLASS)
    public @interface SECURITY_TYPE {
    }

    static {
        int i;
        String str;
        boolean z = true;
        loadedStaticConfig = false;
        try {
            Bundle metaInfo = Utils.getMetaInfo(getContext());
            if (metaInfo != null) {
                String str2 = null;
                String string = metaInfo.getString("accsConfigTags", (String) null);
                ALog.i(TAG, "init config from xml", "configtags", string);
                if (!TextUtils.isEmpty(string)) {
                    String[] split = string.split(SymbolExpUtil.SYMBOL_VERTICALBAR);
                    if (split == null) {
                        split = new String[]{string};
                    }
                    int length = split.length;
                    int i2 = 0;
                    while (i2 < length) {
                        String str3 = split[i2];
                        if (TextUtils.isEmpty(str3)) {
                            i = length;
                        } else {
                            int i3 = metaInfo.getInt(str3 + "_accsAppkey", -1);
                            if (i3 < 0) {
                                str = str2;
                            } else {
                                str = String.valueOf(i3);
                            }
                            String string2 = metaInfo.getString(str3 + "_accsAppSecret");
                            String string3 = metaInfo.getString(str3 + "_authCode");
                            boolean z2 = metaInfo.getBoolean(str3 + "_keepAlive", z);
                            boolean z3 = metaInfo.getBoolean(str3 + "_autoUnit", z);
                            int i4 = metaInfo.getInt(str3 + "_inappPubkey", -1);
                            int i5 = metaInfo.getInt(str3 + "_channelPubkey", -1);
                            String string4 = metaInfo.getString(str3 + "_inappHost");
                            String string5 = metaInfo.getString(str3 + "_channelHost");
                            int i6 = metaInfo.getInt(str3 + "_configEnv", 0);
                            StringBuilder sb = new StringBuilder();
                            sb.append(str3);
                            i = length;
                            sb.append("_disableChannel");
                            boolean z4 = metaInfo.getBoolean(sb.toString());
                            if (!TextUtils.isEmpty(str)) {
                                new Builder().setTag(str3).setConfigEnv(i6).setAppKey(str).setAppSecret(string2).setAutoCode(string3).setKeepAlive(z2).setAutoUnit(z3).setInappHost(string4).setInappPubKey(i4).setChannelHost(string5).setChannelPubKey(i5).setDisableChannel(z4).build();
                                ALog.i(TAG, "init config from xml", new Object[0]);
                            }
                        }
                        i2++;
                        length = i;
                        str2 = null;
                        z = true;
                    }
                    loadedStaticConfig = true;
                }
            }
        } catch (Throwable th) {
            ALog.e(TAG, "init config from xml", th, new Object[0]);
        }
    }

    public static Context getContext() {
        if (mContext != null) {
            return mContext;
        }
        synchronized (AccsClientConfig.class) {
            if (mContext != null) {
                Context context = mContext;
                return context;
            }
            try {
                Class<?> cls = Class.forName("android.app.ActivityThread");
                Object invoke = cls.getMethod("currentActivityThread", new Class[0]).invoke(cls, new Object[0]);
                mContext = (Context) invoke.getClass().getMethod("getApplication", new Class[0]).invoke(invoke, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Context context2 = mContext;
            return context2;
        }
    }

    protected AccsClientConfig() {
    }

    @Deprecated
    public static AccsClientConfig getConfig(String str) {
        Map<String, AccsClientConfig> map;
        switch (mEnv) {
            case 1:
                map = mPreviewConfigs;
                break;
            case 2:
                map = mDebugConfigs;
                break;
            default:
                map = mReleaseConfigs;
                break;
        }
        for (AccsClientConfig next : map.values()) {
            if (next.mAppKey.equals(str) && next.mConfigEnv == mEnv) {
                return next;
            }
        }
        ALog.e(TAG, "getConfigByTag return null", "appkey", str);
        return null;
    }

    public static AccsClientConfig getConfigByTag(String str) {
        AccsClientConfig accsClientConfig;
        switch (mEnv) {
            case 0:
                accsClientConfig = mReleaseConfigs.get(str);
                break;
            case 1:
                accsClientConfig = mPreviewConfigs.get(str);
                break;
            case 2:
                accsClientConfig = mDebugConfigs.get(str);
                break;
            default:
                accsClientConfig = mReleaseConfigs.get(str);
                break;
        }
        if (accsClientConfig == null) {
            ALog.e(TAG, "getConfigByTag return null", Constants.KEY_CONFIG_TAG, str);
        }
        return accsClientConfig;
    }

    public String getAppKey() {
        return this.mAppKey;
    }

    public String getAppSecret() {
        return this.mAppSecret;
    }

    public String getInappHost() {
        return this.mInappHost;
    }

    public String getChannelHost() {
        return this.mChannelHost;
    }

    public int getSecurity() {
        return this.mSecurity;
    }

    public String getAuthCode() {
        return this.mAuthCode;
    }

    public int getInappPubKey() {
        return this.mInappPubKey;
    }

    public int getChannelPubKey() {
        return this.mChannelPubKey;
    }

    public boolean isKeepalive() {
        return this.mKeepalive;
    }

    public boolean isAutoUnit() {
        return this.mAutoUnit;
    }

    public String getTag() {
        return this.mTag;
    }

    public int getConfigEnv() {
        return this.mConfigEnv;
    }

    public boolean getDisableChannel() {
        return this.mDisableChannel;
    }

    public boolean isQuickReconnect() {
        return this.mQuickReconnect;
    }

    public String getStoreId() {
        return this.mStoreId;
    }

    public boolean isAccsHeartbeatEnable() {
        return this.mAccsHeartbeatEnable;
    }

    public String toString() {
        return "AccsClientConfig{" + "Tag=" + this.mTag + ", ConfigEnv=" + this.mConfigEnv + ", AppKey=" + this.mAppKey + ", AppSecret=" + this.mAppSecret + ", InappHost=" + this.mInappHost + ", ChannelHost=" + this.mChannelHost + ", Security=" + this.mSecurity + ", AuthCode=" + this.mAuthCode + ", InappPubKey=" + this.mInappPubKey + ", ChannelPubKey=" + this.mChannelPubKey + ", Keepalive=" + this.mKeepalive + ", AutoUnit=" + this.mAutoUnit + ", DisableChannel=" + this.mDisableChannel + ", QuickReconnect=" + this.mQuickReconnect + "}";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AccsClientConfig)) {
            return false;
        }
        AccsClientConfig accsClientConfig = (AccsClientConfig) obj;
        if (this.mSecurity == accsClientConfig.mSecurity && this.mInappPubKey == accsClientConfig.mInappPubKey && this.mChannelPubKey == accsClientConfig.mChannelPubKey && this.mKeepalive == accsClientConfig.mKeepalive && this.mAutoUnit == accsClientConfig.mAutoUnit && this.mConfigEnv == accsClientConfig.mConfigEnv && this.mDisableChannel == accsClientConfig.mDisableChannel && this.mQuickReconnect == accsClientConfig.mQuickReconnect && this.mAccsHeartbeatEnable == accsClientConfig.mAccsHeartbeatEnable && this.mAppKey.equals(accsClientConfig.mAppKey) && this.mAppSecret.equals(accsClientConfig.mAppSecret) && this.mInappHost.equals(accsClientConfig.mInappHost) && this.mChannelHost.equals(accsClientConfig.mChannelHost) && this.mStoreId.equals(accsClientConfig.mStoreId) && this.mAuthCode.equals(accsClientConfig.mAuthCode) && this.mTag.equals(accsClientConfig.mTag)) {
            return true;
        }
        return false;
    }

    public static void setAccsConfig(int i, AccsClientConfig accsClientConfig) {
        Map<String, AccsClientConfig> map;
        switch (i) {
            case 1:
                map = mPreviewConfigs;
                break;
            case 2:
                map = mDebugConfigs;
                break;
            default:
                map = mReleaseConfigs;
                break;
        }
        AccsClientConfig accsClientConfig2 = map.get(accsClientConfig.getTag());
        if (accsClientConfig2 != null) {
            ALog.w(TAG, "build conver", "old config", accsClientConfig2);
        }
        map.put(accsClientConfig.getTag(), accsClientConfig);
    }

    public static class Builder {
        private boolean mAccsHeartbeatEnable = false;
        private String mAppKey = "";
        private String mAppSecret = "";
        private String mAuthCode = "";
        private boolean mAutoUnit = true;
        private String mChannelHost = "";
        private int mChannelPubKey = -1;
        private int mConfigEnv = -1;
        private boolean mDisableChannel = false;
        private String mInappHost = "";
        private int mInappPubKey = -1;
        private boolean mKeepalive = true;
        private boolean mQuickReconnect = false;
        private String mStoreId = "";
        private String mTag = "";

        public Builder setAppKey(String str) {
            this.mAppKey = str;
            return this;
        }

        public Builder setAppSecret(String str) {
            this.mAppSecret = str;
            return this;
        }

        public Builder setInappHost(String str) {
            this.mInappHost = str;
            return this;
        }

        public Builder setChannelHost(String str) {
            this.mChannelHost = str;
            return this;
        }

        public Builder setAutoCode(String str) {
            this.mAuthCode = str;
            return this;
        }

        public Builder setInappPubKey(int i) {
            this.mInappPubKey = i;
            return this;
        }

        public Builder setChannelPubKey(int i) {
            this.mChannelPubKey = i;
            return this;
        }

        public Builder setKeepAlive(boolean z) {
            this.mKeepalive = z;
            return this;
        }

        public Builder setAutoUnit(boolean z) {
            this.mAutoUnit = z;
            return this;
        }

        public Builder setConfigEnv(@ENV int i) {
            this.mConfigEnv = i;
            return this;
        }

        public Builder setStoreId(String str) {
            this.mStoreId = str;
            return this;
        }

        public Builder setTag(String str) {
            this.mTag = str;
            return this;
        }

        public Builder setDisableChannel(boolean z) {
            this.mDisableChannel = z;
            return this;
        }

        public Builder setQuickReconnect(boolean z) {
            this.mQuickReconnect = z;
            return this;
        }

        public Builder setAccsHeartbeatEnable(boolean z) {
            this.mAccsHeartbeatEnable = z;
            return this;
        }

        public AccsClientConfig build() throws AccsException {
            Map<String, AccsClientConfig> map;
            if (!TextUtils.isEmpty(this.mAppKey)) {
                AccsClientConfig accsClientConfig = new AccsClientConfig();
                String unused = accsClientConfig.mAppKey = this.mAppKey;
                String unused2 = accsClientConfig.mAppSecret = this.mAppSecret;
                String unused3 = accsClientConfig.mAuthCode = this.mAuthCode;
                boolean unused4 = accsClientConfig.mKeepalive = this.mKeepalive;
                boolean unused5 = accsClientConfig.mAutoUnit = this.mAutoUnit;
                int unused6 = accsClientConfig.mInappPubKey = this.mInappPubKey;
                int unused7 = accsClientConfig.mChannelPubKey = this.mChannelPubKey;
                String unused8 = accsClientConfig.mInappHost = this.mInappHost;
                String unused9 = accsClientConfig.mChannelHost = this.mChannelHost;
                String unused10 = accsClientConfig.mTag = this.mTag;
                String unused11 = accsClientConfig.mStoreId = this.mStoreId;
                int unused12 = accsClientConfig.mConfigEnv = this.mConfigEnv;
                boolean unused13 = accsClientConfig.mDisableChannel = this.mDisableChannel;
                boolean unused14 = accsClientConfig.mQuickReconnect = this.mQuickReconnect;
                boolean unused15 = accsClientConfig.mAccsHeartbeatEnable = this.mAccsHeartbeatEnable;
                if (accsClientConfig.mConfigEnv < 0) {
                    int unused16 = accsClientConfig.mConfigEnv = AccsClientConfig.mEnv;
                }
                if (TextUtils.isEmpty(accsClientConfig.mAppSecret)) {
                    int unused17 = accsClientConfig.mSecurity = 0;
                } else {
                    int unused18 = accsClientConfig.mSecurity = 2;
                }
                if (TextUtils.isEmpty(accsClientConfig.mInappHost)) {
                    String unused19 = accsClientConfig.mInappHost = AccsClientConfig.DEFAULT_CENTER_HOSTS[accsClientConfig.mConfigEnv];
                }
                if (TextUtils.isEmpty(accsClientConfig.mChannelHost)) {
                    String unused20 = accsClientConfig.mChannelHost = AccsClientConfig.DEFAULT_CHANNEL_HOSTS[accsClientConfig.mConfigEnv];
                }
                if (TextUtils.isEmpty(accsClientConfig.mTag)) {
                    String unused21 = accsClientConfig.mTag = accsClientConfig.mAppKey;
                }
                switch (accsClientConfig.mConfigEnv) {
                    case 1:
                        map = AccsClientConfig.mPreviewConfigs;
                        break;
                    case 2:
                        map = AccsClientConfig.mDebugConfigs;
                        break;
                    default:
                        map = AccsClientConfig.mReleaseConfigs;
                        break;
                }
                ALog.d(AccsClientConfig.TAG, "build", BindingXConstants.KEY_CONFIG, accsClientConfig);
                AccsClientConfig accsClientConfig2 = map.get(accsClientConfig.getTag());
                if (accsClientConfig2 != null) {
                    ALog.w(AccsClientConfig.TAG, "build conver", "old config", accsClientConfig2);
                }
                map.put(accsClientConfig.getTag(), accsClientConfig);
                return accsClientConfig;
            }
            throw new AccsException("appkey null");
        }
    }
}
