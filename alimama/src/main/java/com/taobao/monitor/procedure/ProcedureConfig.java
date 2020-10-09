package com.taobao.monitor.procedure;

public class ProcedureConfig {
    private final boolean independent;
    private final IProcedure parent;
    private final boolean parentNeedStats;
    private final boolean upload;

    private ProcedureConfig(Builder builder) {
        this.upload = builder.upload;
        this.independent = builder.independent;
        this.parent = builder.parent;
        this.parentNeedStats = builder.parentNeedStats;
    }

    public IProcedure getParent() {
        return this.parent;
    }

    public boolean isUpload() {
        return this.upload;
    }

    public boolean isIndependent() {
        return this.independent;
    }

    public boolean isParentNeedStats() {
        return this.parentNeedStats;
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public boolean independent;
        /* access modifiers changed from: private */
        public IProcedure parent;
        /* access modifiers changed from: private */
        public boolean parentNeedStats;
        /* access modifiers changed from: private */
        public boolean upload;

        public Builder setParent(IProcedure iProcedure) {
            this.parent = iProcedure;
            return this;
        }

        public Builder setUpload(boolean z) {
            this.upload = z;
            return this;
        }

        public Builder setIndependent(boolean z) {
            this.independent = z;
            return this;
        }

        public Builder setParentNeedStats(boolean z) {
            this.parentNeedStats = z;
            return this;
        }

        public ProcedureConfig build() {
            return new ProcedureConfig(this);
        }
    }
}
