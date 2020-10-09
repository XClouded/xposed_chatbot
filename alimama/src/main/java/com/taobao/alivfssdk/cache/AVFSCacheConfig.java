package com.taobao.alivfssdk.cache;

import com.taobao.alivfssdk.utils.AVFSCacheLog;

public class AVFSCacheConfig {
    public long fileMemMaxSize;
    public Long limitSize;
    public long sqliteMemMaxSize;

    public AVFSCacheConfig() {
        this.limitSize = -1L;
        this.fileMemMaxSize = -1;
        this.sqliteMemMaxSize = -1;
    }

    private AVFSCacheConfig(Builder builder) {
        this.limitSize = -1L;
        this.fileMemMaxSize = -1;
        this.sqliteMemMaxSize = -1;
        this.limitSize = Long.valueOf(builder.limitSize);
        this.fileMemMaxSize = builder.fileMemMaxSize;
        this.sqliteMemMaxSize = builder.sqliteMemMaxSize;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("AVFSCacheConfig{");
        stringBuffer.append("limitSize=");
        stringBuffer.append(AVFSCacheLog.bytesIntoHumanReadable(this.limitSize.longValue()));
        stringBuffer.append(", fileMemMaxSize=");
        stringBuffer.append(AVFSCacheLog.bytesIntoHumanReadable(this.fileMemMaxSize));
        stringBuffer.append(", sqliteMemMaxSize=");
        stringBuffer.append(AVFSCacheLog.bytesIntoHumanReadable(this.sqliteMemMaxSize));
        stringBuffer.append('}');
        return stringBuffer.toString();
    }

    public static AVFSCacheConfig newDefaultConfig() {
        AVFSCacheConfig aVFSCacheConfig = new AVFSCacheConfig();
        aVFSCacheConfig.limitSize = 10485760L;
        aVFSCacheConfig.fileMemMaxSize = 0;
        aVFSCacheConfig.sqliteMemMaxSize = 0;
        return aVFSCacheConfig;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public void setConfig(AVFSCacheConfig aVFSCacheConfig) {
        if (aVFSCacheConfig.limitSize.longValue() >= 0) {
            this.limitSize = aVFSCacheConfig.limitSize;
        }
        if (aVFSCacheConfig.fileMemMaxSize >= 0) {
            this.fileMemMaxSize = aVFSCacheConfig.fileMemMaxSize;
        }
        if (aVFSCacheConfig.sqliteMemMaxSize >= 0) {
            this.sqliteMemMaxSize = aVFSCacheConfig.sqliteMemMaxSize;
        }
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public long fileMemMaxSize;
        /* access modifiers changed from: private */
        public long limitSize;
        /* access modifiers changed from: private */
        public long sqliteMemMaxSize;

        private Builder() {
            this.limitSize = -1;
            this.fileMemMaxSize = -1;
            this.sqliteMemMaxSize = -1;
        }

        public Builder limitSize(long j) {
            this.limitSize = j;
            return this;
        }

        public Builder fileMemMaxSize(long j) {
            this.fileMemMaxSize = j;
            return this;
        }

        public Builder sqliteMemMaxSize(long j) {
            this.sqliteMemMaxSize = j;
            return this;
        }

        public AVFSCacheConfig build() {
            return new AVFSCacheConfig(this);
        }
    }
}
