package com.alimama.moon.emas;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;

public class FlutterApmReporter {
    private static final String DIMENSION_SET_NAME_PAGE_ID = "pageId";
    private static final String FLUTTER_PERF_MODULE = "flutter_performance";
    private static final String MEASURE_NAME_FIRST_FRAME_TIME = "firstFrameTime";
    private static final String MEASURE_NAME_FPS = "fps";
    private static final String MEASURE_NAME_INTERACTIVE_TIME = "interactiveTime";
    private static final String MONITOR_POINT_FPS = "reportFPS";
    private static final String MONITOR_POINT_TIME = "reportTime";

    public static void init() {
        DimensionSet addDimension = DimensionSet.create().addDimension("pageId");
        MeasureSet create = MeasureSet.create();
        Measure measure = new Measure("fps");
        measure.setRange(Double.valueOf(0.0d), Double.valueOf(60.0d));
        create.addMeasure(measure);
        AppMonitor.register(FLUTTER_PERF_MODULE, MONITOR_POINT_FPS, create, addDimension);
        MeasureSet create2 = MeasureSet.create();
        create2.addMeasure(MEASURE_NAME_FIRST_FRAME_TIME);
        create2.addMeasure(MEASURE_NAME_INTERACTIVE_TIME);
        AppMonitor.register(FLUTTER_PERF_MODULE, MONITOR_POINT_TIME, create2, addDimension);
    }

    public static void reportTime(@Nullable Integer num, @Nullable Integer num2, @Nullable String str) {
        double d;
        DimensionValueSet create = DimensionValueSet.create();
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        create.setValue("pageId", str);
        MeasureValueSet create2 = MeasureValueSet.create();
        double d2 = 0.0d;
        if (num == null) {
            d = 0.0d;
        } else {
            d = (double) num.intValue();
        }
        create2.setValue(MEASURE_NAME_FIRST_FRAME_TIME, d);
        if (num2 != null) {
            d2 = (double) num2.intValue();
        }
        create2.setValue(MEASURE_NAME_INTERACTIVE_TIME, d2);
        AppMonitor.Stat.commit(FLUTTER_PERF_MODULE, MONITOR_POINT_TIME, create, create2);
    }

    public static void fps(@Nullable Integer num, @Nullable String str) {
        double d;
        DimensionValueSet create = DimensionValueSet.create();
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        create.setValue("pageId", str);
        MeasureValueSet create2 = MeasureValueSet.create();
        if (num == null) {
            d = 0.0d;
        } else {
            d = (double) num.intValue();
        }
        create2.setValue("fps", d);
        AppMonitor.Stat.commit(FLUTTER_PERF_MODULE, MONITOR_POINT_FPS, create, create2);
    }
}
