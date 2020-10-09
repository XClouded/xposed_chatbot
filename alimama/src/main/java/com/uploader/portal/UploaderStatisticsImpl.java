package com.uploader.portal;

import android.content.Context;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.tao.log.TLogConstant;
import com.uploader.export.IUploaderStatistics;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class UploaderStatisticsImpl implements IUploaderStatistics {
    private AtomicBoolean commitFlowStatFlag = new AtomicBoolean(true);

    public void onRegister(String str, String str2, Set<String> set, Set<String> set2, boolean z) {
        try {
            AppMonitor.register(str, str2, MeasureSet.create((Collection<String>) set), DimensionSet.create((Collection<String>) set2), z);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void onCommit(String str, String str2, Map<String, Double> map, Map<String, String> map2) {
        Map<String, Double> map3 = map;
        try {
            if (this.commitFlowStatFlag.get()) {
                Double d = map3.get("upstream");
                Double d2 = map3.get("downstream");
                if (d == null) {
                    d = Double.valueOf(0.0d);
                }
                if (d2 == null) {
                    d2 = Double.valueOf(0.0d);
                }
                Class<?> cls = Class.forName("com.taobao.analysis.FlowCenter");
                Method method = cls.getMethod("getInstance", new Class[0]);
                cls.getMethod("commitFlow", new Class[]{Context.class, String.class, Boolean.TYPE, String.class, Long.TYPE, Long.TYPE}).invoke(method.invoke(cls, new Object[0]), new Object[]{UploaderDependencyImpl.context, TLogConstant.TOKEN_TYPE_ARUP, false, TLogConstant.TOKEN_TYPE_ARUP, Long.valueOf(d.longValue()), Long.valueOf(d2.longValue())});
            }
        } catch (Throwable th) {
            if ((th instanceof ClassNotFoundException) || (th instanceof NoSuchMethodException)) {
                this.commitFlowStatFlag.compareAndSet(true, false);
            } else {
                th.printStackTrace();
            }
        }
        try {
            DimensionValueSet create = DimensionValueSet.create();
            create.setMap(map2);
            MeasureValueSet create2 = MeasureValueSet.create();
            for (Map.Entry next : map.entrySet()) {
                create2.setValue((String) next.getKey(), ((Double) next.getValue()).doubleValue());
            }
            AppMonitor.Stat.commit(str, str2, create, create2);
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
    }
}
