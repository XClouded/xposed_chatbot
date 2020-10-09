package com.alibaba.appmonitor.event;

import com.alibaba.analytics.utils.Logger;
import com.alibaba.appmonitor.model.Metric;
import com.alibaba.appmonitor.model.MetricRepo;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureValue;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DurationEvent extends Event {
    private static final Long DEFAULT_TIMEOUT = 300000L;
    private static final String TAG = "DurationEvent";
    private DimensionValueSet dimensionValues;
    private Long initTime;
    private MeasureValueSet measureValues;
    private Metric metric;
    private Map<String, MeasureValue> undonePeriod;

    public boolean isExpired() {
        long currentTimeMillis = System.currentTimeMillis();
        List<Measure> measures = this.metric.getMeasureSet().getMeasures();
        if (measures != null) {
            int size = measures.size();
            for (int i = 0; i < size; i++) {
                Measure measure = measures.get(i);
                if (measure != null) {
                    double doubleValue = measure.getMax() != null ? measure.getMax().doubleValue() : (double) DEFAULT_TIMEOUT.longValue();
                    MeasureValue measureValue = this.undonePeriod.get(measure.getName());
                    if (measureValue != null && !measureValue.isFinish()) {
                        double d = (double) currentTimeMillis;
                        double value = measureValue.getValue();
                        Double.isNaN(d);
                        if (d - value > doubleValue) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void start(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.undonePeriod.isEmpty()) {
            this.initTime = Long.valueOf(currentTimeMillis);
        }
        Object[] objArr = {Double.valueOf((double) currentTimeMillis), Double.valueOf((double) (currentTimeMillis - this.initTime.longValue()))};
        this.undonePeriod.put(str, (MeasureValue) BalancedPool.getInstance().poll(MeasureValue.class, objArr));
        super.commit((Long) null);
    }

    public boolean end(String str) {
        MeasureValue measureValue = this.undonePeriod.get(str);
        if (measureValue != null) {
            double currentTimeMillis = (double) System.currentTimeMillis();
            double value = measureValue.getValue();
            Double.isNaN(currentTimeMillis);
            Logger.d(TAG, "statEvent consumeTime. module:", this.module, " monitorPoint:", this.monitorPoint, " measureName:", str, " time:", Double.valueOf(currentTimeMillis - value));
            double value2 = measureValue.getValue();
            Double.isNaN(currentTimeMillis);
            measureValue.setValue(currentTimeMillis - value2);
            measureValue.setFinish(true);
            this.measureValues.setValue(str, measureValue);
            if (this.metric.getMeasureSet().valid(this.measureValues)) {
                return true;
            }
        }
        super.commit((Long) null);
        return false;
    }

    public void commitDimensionValue(DimensionValueSet dimensionValueSet) {
        if (this.dimensionValues == null) {
            this.dimensionValues = dimensionValueSet;
        } else {
            this.dimensionValues.addValues(dimensionValueSet);
        }
    }

    public MeasureValueSet getMeasureValues() {
        return this.measureValues;
    }

    public DimensionValueSet getDimensionValues() {
        return this.dimensionValues;
    }

    public void clean() {
        super.clean();
        this.metric = null;
        this.initTime = null;
        for (MeasureValue offer : this.undonePeriod.values()) {
            BalancedPool.getInstance().offer(offer);
        }
        this.undonePeriod.clear();
        if (this.measureValues != null) {
            BalancedPool.getInstance().offer(this.measureValues);
            this.measureValues = null;
        }
        if (this.dimensionValues != null) {
            BalancedPool.getInstance().offer(this.dimensionValues);
            this.dimensionValues = null;
        }
    }

    public void fill(Object... objArr) {
        super.fill(objArr);
        if (this.undonePeriod == null) {
            this.undonePeriod = new HashMap();
        }
        this.metric = MetricRepo.getRepo().getMetric(this.module, this.monitorPoint);
        if (this.metric.getDimensionSet() != null) {
            this.dimensionValues = (DimensionValueSet) BalancedPool.getInstance().poll(DimensionValueSet.class, new Object[0]);
            this.metric.getDimensionSet().setConstantValue(this.dimensionValues);
        }
        this.measureValues = (MeasureValueSet) BalancedPool.getInstance().poll(MeasureValueSet.class, new Object[0]);
    }
}
