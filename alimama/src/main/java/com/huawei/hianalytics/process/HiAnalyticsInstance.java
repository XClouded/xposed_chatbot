package com.huawei.hianalytics.process;

import android.content.Context;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.util.f;
import java.util.LinkedHashMap;
import java.util.Map;

public interface HiAnalyticsInstance {

    public static final class Builder {
        private HiAnalyticsConfig diffConf = null;
        private Context mContext;
        private HiAnalyticsConfig maintConf = null;
        private HiAnalyticsConfig operConf = null;

        public Builder(Context context) {
            if (context != null) {
                this.mContext = context.getApplicationContext();
            }
        }

        private void setConf(d dVar) {
            if (this.operConf == null) {
                dVar.c((HiAnalyticsConfig) null);
            } else {
                dVar.c(new HiAnalyticsConfig(this.operConf));
            }
            if (this.maintConf == null) {
                dVar.a((HiAnalyticsConfig) null);
            } else {
                dVar.a(new HiAnalyticsConfig(this.maintConf));
            }
            if (this.diffConf == null) {
                dVar.b((HiAnalyticsConfig) null);
            } else {
                dVar.b(new HiAnalyticsConfig(this.diffConf));
            }
        }

        public HiAnalyticsInstance create(String str) {
            if (this.mContext == null) {
                b.d("HianalyticsSDK", "create(): instance context is null,create failed!");
                return null;
            } else if (str == null || !f.a("tag", str, "[a-zA-Z0-9][a-zA-Z0-9_]{0,255}")) {
                b.d("HianalyticsSDK", "create(): check tag failed! TAG: " + str);
                return null;
            } else if (HiAnalyticsManager.getInitFlag(str)) {
                b.d("HianalyticsSDK", "This tag already exists");
                return null;
            } else if (a.b().c(str)) {
                b.d("HianalyticsSDK", "create(): black tag is not allowed here.");
                return null;
            } else if (a.b().a() - a.b().e() > 50) {
                b.d("HianalyticsSDK", "The number of TAGs exceeds the limit!");
                return null;
            } else {
                d dVar = new d(str);
                setConf(dVar);
                a.b().a(this.mContext);
                b.a().a(this.mContext);
                d a = a.b().a(str, dVar);
                return a == null ? dVar : a;
            }
        }

        public HiAnalyticsInstance refresh(String str) {
            d a = a.b().a(str);
            if (a == null) {
                b.c("HianalyticsSDK", "HiAnalyticsInstance.Builder.Refresh(): calling refresh before create. TAG: " + str + " has no instance. ");
                return create(str);
            }
            a.refresh(1, this.maintConf);
            a.refresh(0, this.operConf);
            a.refresh(3, this.diffConf);
            return a;
        }

        public Builder setDiffConf(HiAnalyticsConfig hiAnalyticsConfig) {
            this.diffConf = hiAnalyticsConfig;
            return this;
        }

        public Builder setMaintConf(HiAnalyticsConfig hiAnalyticsConfig) {
            this.maintConf = hiAnalyticsConfig;
            return this;
        }

        public Builder setOperConf(HiAnalyticsConfig hiAnalyticsConfig) {
            this.operConf = hiAnalyticsConfig;
            return this;
        }
    }

    void clearData();

    void onBackground(long j);

    void onEvent(int i, String str, LinkedHashMap<String, String> linkedHashMap);

    @Deprecated
    void onEvent(Context context, String str, String str2);

    void onEvent(String str, LinkedHashMap<String, String> linkedHashMap);

    void onForeground(long j);

    void onPause(Context context);

    void onPause(Context context, LinkedHashMap<String, String> linkedHashMap);

    void onPause(String str, LinkedHashMap<String, String> linkedHashMap);

    void onReport(int i);

    @Deprecated
    void onReport(Context context, int i);

    void onResume(Context context);

    void onResume(Context context, LinkedHashMap<String, String> linkedHashMap);

    void onResume(String str, LinkedHashMap<String, String> linkedHashMap);

    void onStreamEvent(int i, String str, LinkedHashMap<String, String> linkedHashMap);

    void refresh(int i, HiAnalyticsConfig hiAnalyticsConfig);

    void setCommonProp(int i, Map<String, String> map);

    void setOAID(int i, String str);

    void setOAIDTrackingFlag(int i, boolean z);

    void setUpid(int i, String str);
}
