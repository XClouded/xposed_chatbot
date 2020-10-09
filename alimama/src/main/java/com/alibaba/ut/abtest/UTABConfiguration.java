package com.alibaba.ut.abtest;

public class UTABConfiguration {
    /* access modifiers changed from: private */
    public boolean debugEnable;
    /* access modifiers changed from: private */
    public UTABEnvironment environment;
    /* access modifiers changed from: private */
    public UTABMethod method = UTABMethod.Pull;
    /* access modifiers changed from: private */
    public boolean multiProcessEnable;

    public boolean isMultiProcessEnable() {
        return this.multiProcessEnable;
    }

    public boolean isDebugEnable() {
        return this.debugEnable;
    }

    public UTABEnvironment getEnvironment() {
        return this.environment;
    }

    public UTABMethod getMethod() {
        return this.method;
    }

    public static final class Builder {
        private UTABConfiguration config = new UTABConfiguration();

        public UTABConfiguration create() {
            if (this.config.environment == null) {
                UTABEnvironment unused = this.config.environment = UTABEnvironment.Product;
            }
            return this.config;
        }

        public Builder setDebugEnable(boolean z) {
            boolean unused = this.config.debugEnable = z;
            return this;
        }

        public Builder setEnvironment(UTABEnvironment uTABEnvironment) {
            UTABEnvironment unused = this.config.environment = uTABEnvironment;
            return this;
        }

        public Builder setMethod(UTABMethod uTABMethod) {
            UTABMethod unused = this.config.method = uTABMethod;
            return this;
        }

        public Builder setMultiProcessEnable(boolean z) {
            boolean unused = this.config.multiProcessEnable = z;
            return this;
        }
    }
}
