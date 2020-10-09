package com.alibaba.ut.abtest.push;

public class UTABPushConfiguration {

    public static final class Builder {
        private UTABPushConfiguration config = new UTABPushConfiguration();

        public UTABPushConfiguration create() {
            return this.config;
        }
    }
}
