package com.alibaba.android.umbrella.performance;

import android.os.SystemClock;
import java.util.Map;

public class ProcessEvent {
    public static final int ADD_ABTEST = 6;
    public static final int ADD_ARGS = 2;
    public static final int ADD_OTHER_PROCESS = 8;
    public static final int ADD_PROCESS_POINT = 3;
    public static final int ADD_SUB_PROCESS_POINT = 4;
    public static final int COMMIT = 5;
    public static final int REGISTER_PAGE_POINT = 1;
    public static final int SET_CHILD_BIZ = 7;
    public String ab;
    public String abBucket;
    public Map<String, String> args;
    public String bizName;
    public String childBizName;
    public long costTime;
    public String eventPoint;
    public int eventType;
    public UmbrellaProcess process;
    public long uptimeMillis;

    private ProcessEvent(ProcessEventBuilder processEventBuilder) {
        this.eventType = processEventBuilder.eventType;
        this.bizName = processEventBuilder.bizName;
        this.childBizName = processEventBuilder.childBizName;
        this.ab = processEventBuilder.ab;
        this.abBucket = processEventBuilder.abBucket;
        this.uptimeMillis = processEventBuilder.uptimeMillis;
        this.process = processEventBuilder.process;
        this.eventPoint = processEventBuilder.point;
        this.args = processEventBuilder.args;
        this.costTime = processEventBuilder.costTime;
    }

    public static class ProcessEventBuilder {
        public String ab;
        public String abBucket;
        /* access modifiers changed from: private */
        public Map<String, String> args;
        /* access modifiers changed from: private */
        public String bizName;
        public String childBizName;
        /* access modifiers changed from: private */
        public long costTime;
        /* access modifiers changed from: private */
        public int eventType;
        /* access modifiers changed from: private */
        public String point;
        public UmbrellaProcess process;
        /* access modifiers changed from: private */
        public long uptimeMillis;

        public ProcessEventBuilder(String str) {
            this.bizName = str;
            this.uptimeMillis = SystemClock.uptimeMillis();
        }

        public ProcessEventBuilder(String str, long j) {
            this.bizName = str;
            if (j > 0) {
                this.uptimeMillis = j;
            } else {
                this.uptimeMillis = SystemClock.uptimeMillis();
            }
        }

        public ProcessEventBuilder eventType(int i) {
            this.eventType = i;
            return this;
        }

        public ProcessEventBuilder eventPoint(String str) {
            this.point = str;
            return this;
        }

        public ProcessEventBuilder args(Map<String, String> map) {
            this.args = map;
            return this;
        }

        public ProcessEventBuilder umbrellaProcess(UmbrellaProcess umbrellaProcess) {
            this.process = umbrellaProcess;
            return this;
        }

        public ProcessEventBuilder costTime(long j) {
            this.costTime = j;
            return this;
        }

        public ProcessEventBuilder abId(String str) {
            this.ab = str;
            return this;
        }

        public ProcessEventBuilder abBucket(String str) {
            this.abBucket = str;
            return this;
        }

        public ProcessEventBuilder childBizName(String str) {
            this.childBizName = str;
            return this;
        }

        public ProcessEvent build() {
            return new ProcessEvent(this);
        }
    }
}
