package com.huawei.hianalytics.v2;

import android.content.Context;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.process.HiAnalyticsConfig;
import com.huawei.hianalytics.process.HiAnalyticsLogConfig;
import com.huawei.hianalytics.process.HiAnalyticsManager;
import com.huawei.hianalytics.process.a;
import com.huawei.hianalytics.process.d;

public class HiAnalyticsConf {

    public static class Builder {
        String appid;
        HiAnalyticsConfig.Builder diffConfigBuilder;
        HiAnalyticsLogConfig logConfig;
        Context mContext;
        HiAnalyticsConfig.Builder maintConfigBuilder;
        HiAnalyticsConfig.Builder operConfigBuilder;
        HiAnalyticsConfig.Builder preConfigBuilder;

        public Builder(Context context) {
            if (context != null) {
                this.mContext = context.getApplicationContext();
            }
            this.maintConfigBuilder = new HiAnalyticsConfig.Builder();
            this.operConfigBuilder = new HiAnalyticsConfig.Builder();
            this.diffConfigBuilder = new HiAnalyticsConfig.Builder();
            this.preConfigBuilder = new HiAnalyticsConfig.Builder();
        }

        public void create() {
            if (this.mContext == null) {
                b.d("HianalyticsSDK", "analyticsConf create(): context is null,create failed!");
                return;
            }
            b.b("HianalyticsSDK", "Builder.create() is execute.");
            HiAnalyticsConfig build = this.maintConfigBuilder.build();
            HiAnalyticsConfig build2 = this.operConfigBuilder.build();
            HiAnalyticsConfig build3 = this.diffConfigBuilder.build();
            HiAnalyticsConfig build4 = this.preConfigBuilder.build();
            d dVar = new d("_default_config_tag");
            dVar.c(build2);
            dVar.a(build);
            dVar.b(build3);
            dVar.d(build4);
            a.b().a(this.mContext);
            com.huawei.hianalytics.process.b.a().a(this.mContext);
            a.b().a("_default_config_tag", dVar);
            HiAnalyticsManager.setAppid(this.appid);
            a.b().a(this.mContext, this.logConfig);
        }

        public void refresh(boolean z) {
            b.b("HianalyticsSDK", "Builder.refresh() is execute.");
            HiAnalyticsConfig build = this.maintConfigBuilder.build();
            HiAnalyticsConfig build2 = this.operConfigBuilder.build();
            HiAnalyticsConfig build3 = this.diffConfigBuilder.build();
            HiAnalyticsConfig build4 = this.preConfigBuilder.build();
            d a = a.b().a("_default_config_tag");
            if (a == null) {
                b.c("HianalyticsSDK", "HiAnalyticsInstance.Builder.Refresh(): calling refresh before create. TAG: _default_config_tag has no instance. ");
                return;
            }
            a.refresh(1, build);
            a.refresh(0, build2);
            a.refresh(3, build3);
            a.refresh(2, build4);
            if (z) {
                a.b().d("_default_config_tag");
            }
            a.b().a(this.logConfig, z);
            HiAnalyticsManager.setAppid(this.appid);
        }

        public Builder setAndroidId(String str) {
            b.b("HianalyticsSDK", "setAndroidId(String androidId) is execute.");
            this.operConfigBuilder.setAndroidId(str);
            this.maintConfigBuilder.setAndroidId(str);
            this.diffConfigBuilder.setAndroidId(str);
            this.preConfigBuilder.setAndroidId(str);
            return this;
        }

        public Builder setAppID(String str) {
            b.b("HianalyticsSDK", "Builder.setAppID is execute");
            this.appid = str;
            return this;
        }

        public Builder setAutoReportThreshold(int i) {
            b.b("HianalyticsSDK", "Builder.setAutoReportThreshold is execute");
            this.operConfigBuilder.setAutoReportThreshold(i);
            this.maintConfigBuilder.setAutoReportThreshold(i);
            this.diffConfigBuilder.setAutoReportThreshold(i);
            this.preConfigBuilder.setAutoReportThreshold(i);
            return this;
        }

        public Builder setCacheExpireTime(int i) {
            b.b("HianalyticsSDK", "Builder.setCacheExpireTime is execute");
            this.operConfigBuilder.setCacheExpireTime(i);
            this.maintConfigBuilder.setCacheExpireTime(i);
            this.diffConfigBuilder.setCacheExpireTime(i);
            this.preConfigBuilder.setCacheExpireTime(i);
            return this;
        }

        public Builder setChannel(String str) {
            b.b("HianalyticsSDK", "Builder.setChannel(String channel) is execute.");
            this.operConfigBuilder.setChannel(str);
            this.maintConfigBuilder.setChannel(str);
            this.diffConfigBuilder.setChannel(str);
            this.preConfigBuilder.setChannel(str);
            return this;
        }

        public Builder setCollectURL(int i, String str) {
            HiAnalyticsConfig.Builder builder;
            b.b("HianalyticsSDK", "Builder.setCollectURL(int type,String collectURL) is execute.TYPE : " + i);
            if (i != 3) {
                switch (i) {
                    case 0:
                        builder = this.operConfigBuilder;
                        break;
                    case 1:
                        builder = this.maintConfigBuilder;
                        break;
                    default:
                        b.c("HianalyticsSDK", "Builder.setCollectURL(int type,String collectURL): invalid type!");
                        break;
                }
            } else {
                builder = this.diffConfigBuilder;
            }
            builder.setCollectURL(str);
            return this;
        }

        @Deprecated
        public Builder setEnableAndroidID(boolean z) {
            b.b("HianalyticsSDK", "Builder.setEnableAndroidID(boolean reportAndroidID) is execute.");
            this.maintConfigBuilder.setEnableAndroidID(z);
            this.operConfigBuilder.setEnableAndroidID(z);
            this.diffConfigBuilder.setEnableAndroidID(z);
            this.preConfigBuilder.setEnableAndroidID(z);
            return this;
        }

        public Builder setEnableCollectLog(HiAnalyticsLogConfig hiAnalyticsLogConfig) {
            b.b("HianalyticsSDK", "Builder.setEnableCollectLog (LogConfig logConfig) is execute.");
            this.logConfig = hiAnalyticsLogConfig;
            return this;
        }

        @Deprecated
        public Builder setEnableImei(boolean z) {
            b.b("HianalyticsSDK", "Builder.setEnableImei(boolean isReportAndroidImei) is execute.");
            this.operConfigBuilder.setEnableImei(z);
            this.maintConfigBuilder.setEnableImei(z);
            this.diffConfigBuilder.setEnableImei(z);
            this.preConfigBuilder.setEnableImei(z);
            return this;
        }

        public Builder setEnableMccMnc(boolean z) {
            b.b("HianalyticsSDK", "Builder.setEnableMccMnc(boolean enableMccMnc) is execute.");
            this.maintConfigBuilder.setEnableMccMnc(z);
            this.operConfigBuilder.setEnableMccMnc(z);
            this.diffConfigBuilder.setEnableMccMnc(z);
            this.preConfigBuilder.setEnableMccMnc(z);
            return this;
        }

        @Deprecated
        public Builder setEnableSN(boolean z) {
            b.b("HianalyticsSDK", "Builder.setEnableSN(boolean isReportSN) is execute.");
            this.maintConfigBuilder.setEnableSN(z);
            this.operConfigBuilder.setEnableSN(z);
            this.diffConfigBuilder.setEnableSN(z);
            this.preConfigBuilder.setEnableSN(z);
            return this;
        }

        public Builder setEnableSession(boolean z) {
            b.b("HianalyticsSDK", "setEnableSession(boolean enableSession) is execute.");
            this.operConfigBuilder.setEnableSession(z);
            return this;
        }

        @Deprecated
        public Builder setEnableUDID(boolean z) {
            b.b("HianalyticsSDK", "Builder.setEnableUDID(boolean isReportUDID) is execute.");
            this.maintConfigBuilder.setEnableUDID(z);
            this.operConfigBuilder.setEnableUDID(z);
            this.diffConfigBuilder.setEnableUDID(z);
            this.preConfigBuilder.setEnableUDID(z);
            return this;
        }

        public Builder setEnableUUID(boolean z) {
            b.a("HianalyticsSDK", "Builder.setEnableUUID() is executed.");
            this.operConfigBuilder.setEnableUUID(z);
            this.maintConfigBuilder.setEnableUUID(z);
            this.diffConfigBuilder.setEnableUUID(z);
            this.preConfigBuilder.setEnableUUID(z);
            return this;
        }

        public Builder setIMEI(String str) {
            b.b("HianalyticsSDK", "setIMEI(String imei) is execute.");
            this.operConfigBuilder.setImei(str);
            this.maintConfigBuilder.setImei(str);
            this.diffConfigBuilder.setImei(str);
            this.preConfigBuilder.setImei(str);
            return this;
        }

        public Builder setSN(String str) {
            b.b("HianalyticsSDK", "setSN(String sn) is execute.");
            this.operConfigBuilder.setSN(str);
            this.maintConfigBuilder.setSN(str);
            this.diffConfigBuilder.setSN(str);
            this.preConfigBuilder.setSN(str);
            return this;
        }

        public Builder setUDID(String str) {
            b.b("HianalyticsSDK", "setUDID(String udid) is execute.");
            this.operConfigBuilder.setUdid(str);
            this.maintConfigBuilder.setUdid(str);
            this.diffConfigBuilder.setUdid(str);
            this.preConfigBuilder.setUdid(str);
            return this;
        }
    }
}
