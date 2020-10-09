package com.taobao.phenix.compat.stat;

import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import java.util.Random;

public abstract class TBImageBaseMonitor {
    public static final String CONST_0 = "0";
    public static final String CONST_1 = "1";
    public static final int COVERAGE_RANGE_MAX = 100;
    public static final int COVERAGE_RANGE_MIN = 0;
    static final double MEASURE_TIME_MAX_VALUE = 30000.0d;
    private static Random sRandom = new Random();
    protected boolean mRegistered;

    /* access modifiers changed from: protected */
    public abstract void registerAppMonitor();

    /* access modifiers changed from: protected */
    public boolean filterOutThisStat(int i) {
        return i <= 0 || (i < 100 && !randomEnabled(i));
    }

    public static boolean randomEnabled(int i) {
        return sRandom.nextInt(100) + 1 <= i;
    }

    /* access modifiers changed from: protected */
    public void newMeasure2Set(MeasureSet measureSet, String str, Double d, Double d2, Double d3) {
        Measure measure = new Measure(str, d);
        if (!(d2 == null || d3 == null)) {
            measure.setRange(d2, d3);
        }
        measureSet.addMeasure(measure);
    }
}
