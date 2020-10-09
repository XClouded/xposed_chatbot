package com.huawei.hianalytics.abtesting;

import android.text.TextUtils;
import com.huawei.hianalytics.abtesting.a.a;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.process.HiAnalyticsConfig;
import com.huawei.hianalytics.util.f;

public class ABTestConfig {
    private HiAnalyticsConfig a;
    private a b;

    public static final class Builder {
        /* access modifiers changed from: private */
        public HiAnalyticsConfig config;
        /* access modifiers changed from: private */
        public int expSyncInterval = 1440;
        /* access modifiers changed from: private */
        public String secretKey;
        /* access modifiers changed from: private */
        public String url;
        /* access modifiers changed from: private */
        public String userId;

        public ABTestConfig build() {
            return new ABTestConfig(this);
        }

        public Builder setExpSyncInterval(int i) {
            b.b("ABTest/ABTestConfig", "setExpSyncInterval() is execute");
            if (i < 10) {
                b.b("ABTest/ABTestConfig", " setExpSyncInterval : expSyncInterval check failed");
                i = 10;
            }
            this.expSyncInterval = i;
            return this;
        }

        public Builder setHiAnalyticsConfig(HiAnalyticsConfig hiAnalyticsConfig) {
            b.b("ABTest/ABTestConfig", "setHiAnalyticsConfig() is execute");
            this.config = hiAnalyticsConfig;
            return this;
        }

        public Builder setSecretKey(String str) {
            b.b("ABTest/ABTestConfig", "setSecretKey() is execute");
            if (TextUtils.isEmpty(str) || str.length() > 200) {
                b.b("ABTest/ABTestConfig", " setSecretKey : secretKey check failed");
                str = "";
            }
            this.secretKey = str;
            return this;
        }

        public Builder setUrl(String str) {
            b.b("ABTest/ABTestConfig", "setUrl() is execute");
            if (!f.a(str, "(https://abn-)[a-zA-Z0-9]{1,10}(\\.dt\\.hicloud\\.com)(:(\\d){2,5})?(\\\\|\\/)?")) {
                str = "";
            }
            this.url = str;
            return this;
        }

        public Builder setUserId(String str) {
            b.b("ABTest/ABTestConfig", "setUserId() is execute");
            if (TextUtils.isEmpty(str) || str.length() > 128) {
                b.b("ABTest/ABTestConfig", " setUserId : userId check failed");
                str = "";
            }
            this.userId = str;
            return this;
        }
    }

    private ABTestConfig(Builder builder) {
        this.a = builder.config;
        this.b = new a();
        this.b.b(builder.url);
        this.b.a(builder.secretKey);
        this.b.c(builder.userId);
        this.b.a(builder.expSyncInterval);
    }

    public HiAnalyticsConfig a() {
        return this.a;
    }

    public a b() {
        return this.b;
    }
}
