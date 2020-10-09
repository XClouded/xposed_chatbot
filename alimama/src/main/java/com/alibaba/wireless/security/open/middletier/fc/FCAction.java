package com.alibaba.wireless.security.open.middletier.fc;

public class FCAction {

    public enum FCMainAction {
        SUCCESS,
        FAIL,
        CANCEL,
        RETRY,
        TIMEOUT
    }

    public enum FCSubAction {
        WUA(1),
        NC(2),
        FL(4),
        LOGIN(8),
        DENY(16);
        
        private long a;

        private FCSubAction(long j) {
            this.a = 0;
            this.a = j;
        }

        public long getValue() {
            return this.a;
        }
    }
}
