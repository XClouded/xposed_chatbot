package com.alibaba.ut.abtest.multiprocess;

public class UTABMultiProcessConfiguration {

    public static final class Builder {
        private UTABMultiProcessConfiguration config = new UTABMultiProcessConfiguration();

        public UTABMultiProcessConfiguration create() {
            return this.config;
        }
    }
}
