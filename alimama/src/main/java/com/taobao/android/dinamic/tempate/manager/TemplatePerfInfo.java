package com.taobao.android.dinamic.tempate.manager;

public class TemplatePerfInfo {
    public static final int PERF_PHASE_IN_DEFAULT = 4;
    public static final int PERF_PHASE_IN_FILE = 2;
    public static final int PERF_PHASE_IN_JOBJC = 5;
    public static final int PERF_PHASE_IN_MEM = 1;
    public static final int PERF_PHASE_IN_REMOTE = 3;
    public long fileCostTimeMillis = 0;
    public long memCostTimeMillis = 0;
    public String module;
    public long networkCostTimeMillis = 0;
    public int phase = 0;

    public TemplatePerfInfo(String str) {
        this.module = str;
    }

    public TemplatePerfInfo() {
    }
}
