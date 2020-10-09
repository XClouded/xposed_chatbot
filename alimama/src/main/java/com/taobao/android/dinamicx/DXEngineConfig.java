package com.taobao.android.dinamicx;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.taobao.android.dinamicx.notification.DXSignalProduce;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DXEngineConfig {
    public static final int DEFAULT_MAX_CACHE_COUNT = 100;
    public static final int DEFAULT_PERIOD_TIME = (DXSignalProduce.PERIOD_TIME * 20);
    private static final long DEFAULT_TICK_INTERVAL = 100;
    public static final int DOWN_GRADE_ONCE = 2;
    public static final int DOWN_GRADE_TO_PRESET = 1;
    public static final String DX_DEFAULT_BIZTYPE = "default_bizType";
    String bizType;
    boolean disabledDownGrade;
    boolean disabledFlatten;
    int downgradeType;
    long engineId;
    int periodTime;
    int pipelineCacheMaxCount;
    long tickInterval;
    boolean usePipelineCache;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DownGradeType {
    }

    public DXEngineConfig(@NonNull String str) {
        this(str, new Builder(str));
    }

    private DXEngineConfig(@NonNull String str, Builder builder) {
        this.downgradeType = 1;
        this.bizType = str;
        this.periodTime = builder.periodTime;
        this.engineId = builder.engineId;
        this.downgradeType = builder.downgradeType;
        this.disabledDownGrade = builder.disabledDownGrade;
        this.pipelineCacheMaxCount = builder.pipelineCacheMaxCount;
        this.usePipelineCache = builder.usePipelineCache;
        this.disabledFlatten = builder.disabledFlatten;
        this.tickInterval = Math.max(builder.tickInterval, DEFAULT_TICK_INTERVAL);
        if (TextUtils.isEmpty(str)) {
            this.bizType = DX_DEFAULT_BIZTYPE;
        }
    }

    public int getPeriodTime() {
        return this.periodTime;
    }

    public long getEngineId() {
        return this.engineId;
    }

    public boolean isDisabledDownGrade() {
        return this.disabledDownGrade;
    }

    public String getBizType() {
        return this.bizType;
    }

    public int getDowngradeType() {
        return this.downgradeType;
    }

    public int getPipelineCacheMaxCount() {
        return this.pipelineCacheMaxCount;
    }

    public boolean isUsePipelineCache() {
        return this.usePipelineCache;
    }

    public boolean isDisabledFlatten() {
        return this.disabledFlatten;
    }

    public long getTickInterval() {
        return this.tickInterval;
    }

    public static final class Builder {
        private String bizType;
        /* access modifiers changed from: private */
        public boolean disabledDownGrade;
        boolean disabledFlatten;
        /* access modifiers changed from: private */
        public int downgradeType;
        /* access modifiers changed from: private */
        public long engineId;
        /* access modifiers changed from: private */
        public int periodTime;
        /* access modifiers changed from: private */
        public int pipelineCacheMaxCount;
        /* access modifiers changed from: private */
        public long tickInterval;
        /* access modifiers changed from: private */
        public boolean usePipelineCache;

        public Builder(String str) {
            this.bizType = str;
            if (!TextUtils.isEmpty(str)) {
                this.bizType = str;
            } else {
                this.bizType = DXEngineConfig.DX_DEFAULT_BIZTYPE;
            }
            this.engineId = System.currentTimeMillis();
            this.downgradeType = 1;
            this.disabledDownGrade = false;
            this.pipelineCacheMaxCount = 100;
            this.usePipelineCache = true;
            this.periodTime = DXEngineConfig.DEFAULT_PERIOD_TIME;
            this.disabledFlatten = false;
            this.tickInterval = DXEngineConfig.DEFAULT_TICK_INTERVAL;
        }

        public Builder withPeriodTime(int i) {
            this.periodTime = i;
            return this;
        }

        public Builder withDowngradeType(int i) {
            this.downgradeType = i;
            return this;
        }

        public Builder withDisabledDownGrade(boolean z) {
            this.disabledDownGrade = z;
            return this;
        }

        public Builder withPipelineCacheMaxCount(int i) {
            this.pipelineCacheMaxCount = i;
            return this;
        }

        public Builder withUsePipelineCache(boolean z) {
            this.usePipelineCache = z;
            return this;
        }

        public Builder withDisabledFlatten(boolean z) {
            this.disabledFlatten = z;
            return this;
        }

        public Builder withTickInterval(long j) {
            this.tickInterval = j;
            return this;
        }

        public DXEngineConfig build() {
            return new DXEngineConfig(this.bizType, this);
        }
    }
}
