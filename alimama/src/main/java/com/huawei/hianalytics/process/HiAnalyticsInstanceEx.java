package com.huawei.hianalytics.process;

import android.content.Context;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.global.AutoCollectEventType;
import java.util.List;

public interface HiAnalyticsInstanceEx extends HiAnalyticsInstance {

    public static final class Builder {
        private HiAnalyticsConfig diffConf = null;
        private List<AutoCollectEventType> lsCollectTypes = null;
        private Context mContext;
        private HiAnalyticsConfig maintConf = null;
        private HiAnalyticsConfig operConf = null;

        public Builder(Context context) {
            if (context != null) {
                this.mContext = context.getApplicationContext();
            }
        }

        private void setConfEx(d dVar) {
            HiAnalyticsConfig hiAnalyticsConfig = null;
            dVar.c(this.operConf == null ? null : new HiAnalyticsConfig(this.operConf));
            dVar.a(this.maintConf == null ? null : new HiAnalyticsConfig(this.maintConf));
            if (this.diffConf != null) {
                hiAnalyticsConfig = new HiAnalyticsConfig(this.diffConf);
            }
            dVar.b(hiAnalyticsConfig);
        }

        public Builder autoCollect(List<AutoCollectEventType> list) {
            this.lsCollectTypes = list;
            return this;
        }

        public HiAnalyticsInstanceEx create() {
            String str;
            String str2;
            if (this.mContext == null) {
                str = "HianalyticsSDK";
                str2 = "create(): instanceEx context is null,create failed!";
            } else if (HiAnalyticsManager.getInitFlag("_instance_ex_tag")) {
                str = "HianalyticsSDK";
                str2 = "create(): DEFAULT or existed tag is not allowed here.";
            } else {
                c cVar = new c(this.mContext);
                setConfEx(cVar);
                a.b().a(this.mContext);
                b.a().a(this.mContext);
                a.b().a(cVar);
                cVar.a(this.lsCollectTypes);
                return cVar;
            }
            b.d(str, str2);
            return null;
        }

        public HiAnalyticsInstanceEx refresh() {
            c d = a.b().d();
            if (d == null) {
                b.c("HianalyticsSDK", "HiAnalyticsInstanceEx.Builder.Refresh(): calling refresh before create. Instance not exist.");
                return create();
            }
            d.refresh(1, this.maintConf);
            d.refresh(0, this.operConf);
            d.refresh(3, this.diffConf);
            d.a(this.lsCollectTypes);
            return d;
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

    void enableLogCollection(Context context, HiAnalyticsLogConfig hiAnalyticsLogConfig);

    @Deprecated
    void handleV1Cache();

    void onStartApp(String str, String str2);

    void refreshLogCollection(HiAnalyticsLogConfig hiAnalyticsLogConfig, boolean z);
}
