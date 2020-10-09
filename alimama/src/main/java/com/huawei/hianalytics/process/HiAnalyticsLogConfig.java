package com.huawei.hianalytics.process;

import com.huawei.hianalytics.e.f;
import com.huawei.hianalytics.g.b;
import java.util.Arrays;

public class HiAnalyticsLogConfig {
    private f logData;

    public static final class Builder {
        /* access modifiers changed from: private */
        public int failedFileMaxSize = 5;
        /* access modifiers changed from: private */
        public int fileMaxSize = 3;
        /* access modifiers changed from: private */
        public String logImei = "";
        /* access modifiers changed from: private */
        public boolean logIsEnableImei;
        /* access modifiers changed from: private */
        public boolean logIsEnableSN;
        /* access modifiers changed from: private */
        public boolean logIsEnableUdid;
        /* access modifiers changed from: private */
        public String logSN = "";
        /* access modifiers changed from: private */
        public String logServerUrl;
        /* access modifiers changed from: private */
        public String logUdid = "";
        /* access modifiers changed from: private */
        public String mcc;
        /* access modifiers changed from: private */
        public int minLogLevel;
        /* access modifiers changed from: private */
        public int throwableFlag = 0;
        /* access modifiers changed from: private */
        public String[] throwableInfo = new String[0];

        public Builder(int i, String str, String str2) {
            this.minLogLevel = i;
            this.mcc = str;
            this.logServerUrl = str2;
        }

        public HiAnalyticsLogConfig build() {
            return new HiAnalyticsLogConfig(this);
        }

        public Builder setFailedFileMaxSize(int i) {
            this.failedFileMaxSize = i;
            return this;
        }

        public Builder setFileMaxSize(int i) {
            this.fileMaxSize = i;
            return this;
        }

        @Deprecated
        public Builder setLogEnableImei(boolean z) {
            this.logIsEnableImei = z;
            return this;
        }

        @Deprecated
        public Builder setLogEnableSN(boolean z) {
            this.logIsEnableSN = z;
            return this;
        }

        @Deprecated
        public Builder setLogEnableUdid(boolean z) {
            this.logIsEnableUdid = z;
            return this;
        }

        public Builder setLogImei(String str) {
            if (!com.huawei.hianalytics.util.f.a("logimei", str, 4096)) {
                str = "";
            }
            this.logImei = str;
            return this;
        }

        public Builder setLogSN(String str) {
            if (!com.huawei.hianalytics.util.f.a("logsn", str, 4096)) {
                str = "";
            }
            this.logSN = str;
            return this;
        }

        public Builder setLogUdid(String str) {
            if (!com.huawei.hianalytics.util.f.a("logudid", str, 4096)) {
                str = "";
            }
            this.logUdid = str;
            return this;
        }

        public Builder setThrowableInfo(int i, String[] strArr) {
            this.throwableFlag = i;
            this.throwableInfo = strArr != null ? (String[]) strArr.clone() : new String[0];
            return this;
        }
    }

    private HiAnalyticsLogConfig(Builder builder) {
        this.logData = new f();
        setMinLogLevel(builder.minLogLevel);
        setFileMaxSize(builder.fileMaxSize);
        setFailedFileMaxSize(builder.failedFileMaxSize);
        setMcc(builder.mcc);
        setLogServerUrl(builder.logServerUrl);
        setThrowableInfo(builder.throwableInfo);
        setThrowableFlag(builder.throwableFlag);
        setLogIsEnableImei(builder.logIsEnableImei);
        setLogIsEnableUdid(builder.logIsEnableUdid);
        setLogIsEnableSN(builder.logIsEnableSN);
        setLogImei(builder.logImei);
        setLogUdid(builder.logUdid);
        setLogSN(builder.logSN);
    }

    private void setFailedFileMaxSize(int i) {
        this.logData.c(com.huawei.hianalytics.util.f.a(i, 10, 5));
    }

    private void setFileMaxSize(int i) {
        this.logData.b(com.huawei.hianalytics.util.f.a(i, 10, 3));
    }

    private void setLogImei(String str) {
        this.logData.c(str);
    }

    private void setLogIsEnableImei(boolean z) {
        this.logData.a(z);
    }

    private void setLogIsEnableSN(boolean z) {
        this.logData.c(z);
    }

    private void setLogIsEnableUdid(boolean z) {
        this.logData.b(z);
    }

    private void setLogSN(String str) {
        this.logData.e(str);
    }

    private void setLogServerUrl(String str) {
        String a = com.huawei.hianalytics.util.f.a("logUrl", str, "(https://)[a-zA-Z0-9-_]+[\\.a-zA-Z0-9_-]*(\\.hicloud\\.com)(:(\\d){2,5})?(\\\\|\\/)?", "");
        if (a.endsWith("/") || a.endsWith("\\")) {
            a = a.substring(0, a.length() - 1);
        }
        this.logData.b(a);
    }

    private void setLogUdid(String str) {
        this.logData.d(str);
    }

    private void setMcc(String str) {
        this.logData.a(com.huawei.hianalytics.util.f.a(str, 999, 100));
    }

    private void setMinLogLevel(int i) {
        if (3 > i || i > 6) {
            b.c("HiAnalytics/logServer", "HiAnalyticsLogConfig.setMinLogLevel(): minLogLevel: " + i + " invalid. Replaced with default value");
            this.logData.a(4);
            return;
        }
        this.logData.a(i);
    }

    private void setThrowableFlag(int i) {
        if (i == 0 || i == 1) {
            this.logData.d(i);
            return;
        }
        b.b("HiAnalytics/logServer", "The throwableFlag is wrong. Set to default value.");
        this.logData.d(0);
    }

    private void setThrowableInfo(String[] strArr) {
        if (strArr != null) {
            if (Arrays.toString(strArr).length() > 204800) {
                b.c("HiAnalytics/logServer", "The throwableInfo parameter is too long!");
            } else {
                this.logData.a((String[]) strArr.clone());
                return;
            }
        }
        this.logData.a(new String[0]);
    }

    /* access modifiers changed from: package-private */
    public f getLogData() {
        return this.logData;
    }
}
