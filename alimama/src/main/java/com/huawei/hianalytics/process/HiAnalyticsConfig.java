package com.huawei.hianalytics.process;

import com.huawei.hianalytics.e.c;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.util.f;

public class HiAnalyticsConfig {
    c cfgData;

    public static final class Builder {
        /* access modifiers changed from: private */
        public String androidIdCustom;
        /* access modifiers changed from: private */
        public String channel;
        /* access modifiers changed from: private */
        public String collectURL;
        /* access modifiers changed from: private */
        public int expiryTime = 7;
        /* access modifiers changed from: private */
        public String imeiCustom;
        /* access modifiers changed from: private */
        public boolean isAndroidIdEnabled;
        /* access modifiers changed from: private */
        public boolean isImeiEnabled;
        /* access modifiers changed from: private */
        public boolean isMccMncEnabled;
        /* access modifiers changed from: private */
        public boolean isSNEnabled;
        /* access modifiers changed from: private */
        public boolean isSessionEnabled;
        /* access modifiers changed from: private */
        public boolean isUDIDEnabled;
        /* access modifiers changed from: private */
        public boolean isUUIDEnabled = true;
        /* access modifiers changed from: private */
        public int portLimitSize = 10;
        /* access modifiers changed from: private */
        public String snCustom;
        /* access modifiers changed from: private */
        public String udidCustom;

        public HiAnalyticsConfig build() {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.build() is executed.");
            return new HiAnalyticsConfig(this);
        }

        public Builder setAndroidId(String str) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setAndroidId() is executed.");
            if (!f.a("AndroidId_CustomSet", str, 4096)) {
                str = "";
            }
            this.androidIdCustom = str;
            return this;
        }

        public Builder setAutoReportThreshold(int i) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setAutoReportThreshold() is executed.");
            this.portLimitSize = f.a(i, 500, 10);
            return this;
        }

        public Builder setCacheExpireTime(int i) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setCacheExpireTime() is executed.");
            this.expiryTime = f.a(i, 7, 2);
            return this;
        }

        public Builder setChannel(String str) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setChannel() is executed.");
            if (!f.a("channel", str, 256)) {
                str = "";
            }
            this.channel = str;
            return this;
        }

        public Builder setCollectURL(String str) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setCollectURL() is executed.");
            if (!f.a(str, "(https://)[a-zA-Z0-9-_]+[\\.a-zA-Z0-9_-]*(\\.hicloud\\.com)(:(\\d){2,5})?(\\\\|\\/)?")) {
                str = "";
            }
            if (str.endsWith("/") || str.endsWith("\\")) {
                str = str.substring(0, str.length() - 1);
            }
            this.collectURL = str;
            return this;
        }

        @Deprecated
        public Builder setEnableAndroidID(boolean z) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setEnableAndroidID() is executed.");
            this.isAndroidIdEnabled = z;
            return this;
        }

        @Deprecated
        public Builder setEnableImei(boolean z) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setEnableImei() is executed.");
            this.isImeiEnabled = z;
            return this;
        }

        public Builder setEnableMccMnc(boolean z) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setEnableMccMnc() is executed.");
            this.isMccMncEnabled = z;
            return this;
        }

        @Deprecated
        public Builder setEnableSN(boolean z) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setEnableSN() is executed.");
            this.isSNEnabled = z;
            return this;
        }

        public Builder setEnableSession(boolean z) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setEnableSession() is executed.");
            this.isSessionEnabled = z;
            return this;
        }

        @Deprecated
        public Builder setEnableUDID(boolean z) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setEnableUDID() is executed.");
            this.isUDIDEnabled = z;
            return this;
        }

        public Builder setEnableUUID(boolean z) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setEnableUUID() is executed.");
            this.isUUIDEnabled = z;
            return this;
        }

        public Builder setImei(String str) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setImei() is executed.");
            if (!f.a("IMEI_CustomSet", str, 4096)) {
                str = "";
            }
            this.imeiCustom = str;
            return this;
        }

        public Builder setSN(String str) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setSN() is executed.");
            if (!f.a("SN_CustomSet", str, 4096)) {
                str = "";
            }
            this.snCustom = str;
            return this;
        }

        public Builder setUdid(String str) {
            b.a("HianalyticsSDK", "HiAnalyticsConf.Builder.setUdid() is executed.");
            if (!f.a("Udid_CustomSet", str, 4096)) {
                str = "";
            }
            this.udidCustom = str;
            return this;
        }
    }

    private HiAnalyticsConfig(Builder builder) {
        this.cfgData = new c();
        setDeviceConfig(builder);
        setChannel(builder.channel);
        setCollectURL(builder.collectURL);
        setMccMncEnabled(builder.isMccMncEnabled);
        setSionEnable(builder.isSessionEnabled);
        setLimitSize(builder.portLimitSize);
        setCacheExpiryTime(builder.expiryTime);
        setUUIDEnabled(builder.isUUIDEnabled);
    }

    public HiAnalyticsConfig(HiAnalyticsConfig hiAnalyticsConfig) {
        this.cfgData = new c(hiAnalyticsConfig.cfgData);
    }

    private void setCacheExpiryTime(int i) {
        this.cfgData.a(i);
    }

    private void setChannel(String str) {
        this.cfgData.a(str);
    }

    private void setCollectURL(String str) {
        this.cfgData.b(str);
    }

    private void setDeviceConfig(Builder builder) {
        com.huawei.hianalytics.e.b a = this.cfgData.a();
        a.a(builder.isImeiEnabled);
        a.a(builder.imeiCustom);
        a.d(builder.isAndroidIdEnabled);
        a.c(builder.androidIdCustom);
        a.b(builder.isSNEnabled);
        a.d(builder.snCustom);
        a.c(builder.isUDIDEnabled);
        a.b(builder.udidCustom);
    }

    private void setLimitSize(int i) {
        this.cfgData.b(i);
    }

    private void setMccMncEnabled(boolean z) {
        this.cfgData.b(z);
    }

    private void setSionEnable(boolean z) {
        this.cfgData.a(z);
    }

    public void setUUIDEnabled(boolean z) {
        this.cfgData.c(z);
    }
}
