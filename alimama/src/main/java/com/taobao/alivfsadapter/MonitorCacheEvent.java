package com.taobao.alivfsadapter;

public class MonitorCacheEvent {
    public static final String CACHE_FILE = "file";
    public static final String CACHE_MMAP = "mmap";
    public static final String CACHE_SQL = "sql";
    public static final String OPERATION_READ = "read";
    public static final String OPERATION_WRITE = "write";
    public static final String RESOURCE_OBJECT = "object";
    public static final String RESOURCE_STREAM = "stream";
    public final String cache;
    public long diskTime;
    public int errorCode;
    public String errorMessage;
    public Exception exception;
    public boolean hitMemory;
    public final boolean memoryCache;
    public final String moduleName;
    public String operation;

    private MonitorCacheEvent(Builder builder) {
        this.moduleName = builder.moduleName;
        this.cache = builder.cache;
        this.exception = builder.exception;
        this.errorMessage = builder.errorMessage;
        this.errorCode = builder.errorCode;
        this.operation = builder.operation;
        this.memoryCache = builder.memoryCache;
        this.hitMemory = builder.hitMemory;
        this.diskTime = builder.diskTime;
    }

    public static Builder newBuilder(String str, String str2, boolean z) {
        return new Builder(str, str2, z);
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public final String cache;
        /* access modifiers changed from: private */
        public long diskTime;
        /* access modifiers changed from: private */
        public int errorCode;
        /* access modifiers changed from: private */
        public String errorMessage;
        /* access modifiers changed from: private */
        public Exception exception;
        /* access modifiers changed from: private */
        public boolean hitMemory;
        /* access modifiers changed from: private */
        public final boolean memoryCache;
        /* access modifiers changed from: private */
        public final String moduleName;
        /* access modifiers changed from: private */
        public String operation;

        private Builder(String str, String str2, boolean z) {
            this.moduleName = str;
            this.cache = str2;
            this.memoryCache = z;
        }

        public Builder exception(Exception exc) {
            this.exception = exc;
            return this;
        }

        public Builder errorMessage(String str) {
            this.errorMessage = str;
            return this;
        }

        public Builder errorCode(int i) {
            this.errorCode = i;
            return this;
        }

        public Builder operation(String str) {
            this.operation = str;
            return this;
        }

        public Builder hitMemory(boolean z) {
            this.hitMemory = z;
            return this;
        }

        public Builder diskTime(long j) {
            this.diskTime = j;
            return this;
        }

        public MonitorCacheEvent build() {
            return new MonitorCacheEvent(this);
        }
    }
}
