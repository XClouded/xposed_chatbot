package com.alibaba.appmonitor.event;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.analytics.utils.TaskExecutor;
import com.alibaba.appmonitor.model.Metric;
import com.alibaba.appmonitor.model.MetricRepo;
import com.alibaba.appmonitor.model.MetricValueSet;
import com.alibaba.appmonitor.model.UTDimensionValueSet;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.util.UTUtil;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EventRepo {
    private static final String TAG = "EventRepo";
    private static final String TAG_COMMIT_DAY = "commitDay";
    private static EventRepo eventRepo;
    private Map<String, DurationEvent> durationEventMap = new ConcurrentHashMap();
    private Map<UTDimensionValueSet, MetricValueSet> eventMap = new ConcurrentHashMap();
    private AtomicInteger mAlarmCounter = new AtomicInteger(0);
    private AtomicInteger mCountCounter = new AtomicInteger(0);
    private AtomicInteger mSTATCounter = new AtomicInteger(0);
    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd");

    public static synchronized EventRepo getRepo() {
        EventRepo eventRepo2;
        synchronized (EventRepo.class) {
            if (eventRepo == null) {
                eventRepo = new EventRepo();
            }
            eventRepo2 = eventRepo;
        }
        return eventRepo2;
    }

    private EventRepo() {
    }

    private UTDimensionValueSet fetchUTDimensionValues(int i, Long l, String str, String str2) {
        UTDimensionValueSet uTDimensionValueSet = (UTDimensionValueSet) BalancedPool.getInstance().poll(UTDimensionValueSet.class, new Object[0]);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            uTDimensionValueSet.setValue(LogField.ACCESS.toString(), NetworkUtil.getAccess(Variables.getInstance().getContext()));
            uTDimensionValueSet.setValue(LogField.ACCESS_SUBTYPE.toString(), NetworkUtil.getAccsssSubType(Variables.getInstance().getContext()));
        } else {
            uTDimensionValueSet.setValue(LogField.ACCESS.toString(), str);
            uTDimensionValueSet.setValue(LogField.ACCESS_SUBTYPE.toString(), str2);
        }
        uTDimensionValueSet.setValue(LogField.USERID.toString(), Variables.getInstance().getUserid());
        uTDimensionValueSet.setValue(LogField.USERNICK.toString(), Variables.getInstance().getUsernick());
        uTDimensionValueSet.setValue(LogField.EVENTID.toString(), String.valueOf(i));
        if (l == null) {
            l = Long.valueOf(System.currentTimeMillis() / 1000);
        }
        uTDimensionValueSet.setValue(TAG_COMMIT_DAY, this.mSdf.format(new Date(l.longValue() * 1000)));
        return uTDimensionValueSet;
    }

    public void alarmEventSuccessIncr(int i, String str, String str2, String str3) {
        alarmEventSuccessIncr(i, str, str2, str3, (Long) null, (String) null, (String) null);
    }

    public void alarmEventSuccessIncr(int i, String str, String str2, String str3, Long l, String str4, String str5) {
        UTDimensionValueSet fetchUTDimensionValues = fetchUTDimensionValues(i, l, str4, str5);
        AlarmEvent alarmEvent = (AlarmEvent) getEvent(fetchUTDimensionValues, str, str2, str3, AlarmEvent.class);
        if (alarmEvent != null) {
            alarmEvent.incrSuccess(l);
        }
        if (Variables.getInstance().isApRealTimeDebugging()) {
            AlarmEvent alarmEvent2 = (AlarmEvent) BalancedPool.getInstance().poll(AlarmEvent.class, Integer.valueOf(i), str, str2, str3);
            alarmEvent2.incrSuccess(l);
            UTUtil.sendRealDebugEvent(fetchUTDimensionValues, alarmEvent2);
        }
        checkUploadEvent(EventType.getEventType(i), this.mAlarmCounter);
    }

    public void alarmEventFailIncr(int i, String str, String str2, String str3, String str4, String str5) {
        alarmEventFailIncr(i, str, str2, str3, str4, str5, (Long) null, (String) null, (String) null);
    }

    public void alarmEventFailIncr(int i, String str, String str2, String str3, String str4, String str5, Long l, String str6, String str7) {
        UTDimensionValueSet fetchUTDimensionValues = fetchUTDimensionValues(i, l, str6, str7);
        AlarmEvent alarmEvent = (AlarmEvent) getEvent(fetchUTDimensionValues, str, str2, str3, AlarmEvent.class);
        if (alarmEvent != null) {
            alarmEvent.incrFail(l);
            alarmEvent.addError(str4, str5);
        }
        if (Variables.getInstance().isApRealTimeDebugging()) {
            AlarmEvent alarmEvent2 = (AlarmEvent) BalancedPool.getInstance().poll(AlarmEvent.class, Integer.valueOf(i), str, str2, str3);
            alarmEvent2.incrFail(l);
            alarmEvent2.addError(str4, str5);
            UTUtil.sendRealDebugEvent(fetchUTDimensionValues, alarmEvent2);
        }
        checkUploadEvent(EventType.getEventType(i), this.mAlarmCounter);
    }

    public void countEventCommit(int i, String str, String str2, String str3, double d) {
        countEventCommit(i, str, str2, str3, d, (Long) null, (String) null, (String) null);
    }

    public void countEventCommit(int i, String str, String str2, String str3, double d, Long l, String str4, String str5) {
        UTDimensionValueSet fetchUTDimensionValues = fetchUTDimensionValues(i, l, str4, str5);
        CountEvent countEvent = (CountEvent) getEvent(fetchUTDimensionValues, str, str2, str3, CountEvent.class);
        if (countEvent != null) {
            countEvent.addValue(d, l);
        }
        if (Variables.getInstance().isApRealTimeDebugging()) {
            CountEvent countEvent2 = (CountEvent) BalancedPool.getInstance().poll(CountEvent.class, Integer.valueOf(i), str, str2, str3);
            countEvent2.addValue(d, l);
            UTUtil.sendRealDebugEvent(fetchUTDimensionValues, countEvent2);
        }
        checkUploadEvent(EventType.getEventType(i), this.mCountCounter);
    }

    public void commitStatEvent(int i, String str, String str2, MeasureValueSet measureValueSet, DimensionValueSet dimensionValueSet) {
        commitStatEvent(i, str, str2, measureValueSet, dimensionValueSet, (Long) null, (String) null, (String) null);
    }

    public void commitStatEvent(int i, String str, String str2, MeasureValueSet measureValueSet, DimensionValueSet dimensionValueSet, Long l, String str3, String str4) {
        String str5 = str;
        String str6 = str2;
        MeasureValueSet measureValueSet2 = measureValueSet;
        DimensionValueSet dimensionValueSet2 = dimensionValueSet;
        Metric metric = MetricRepo.getRepo().getMetric(str5, str6);
        if (metric != null) {
            if (metric.getDimensionSet() != null) {
                metric.getDimensionSet().setConstantValue(dimensionValueSet2);
            }
            if (metric.getMeasureSet() != null) {
                metric.getMeasureSet().setConstantValue(measureValueSet2);
            }
            int i2 = i;
            UTDimensionValueSet fetchUTDimensionValues = fetchUTDimensionValues(i, l, str3, str4);
            StatEvent statEvent = (StatEvent) getEvent(fetchUTDimensionValues, str, str2, (String) null, StatEvent.class);
            if (statEvent != null) {
                statEvent.commit(dimensionValueSet2, measureValueSet2);
            }
            if (Variables.getInstance().isApRealTimeDebugging()) {
                StatEvent statEvent2 = (StatEvent) BalancedPool.getInstance().poll(StatEvent.class, Integer.valueOf(i), str5, str6);
                statEvent2.commit(dimensionValueSet2, measureValueSet2);
                UTUtil.sendRealDebugEvent(fetchUTDimensionValues, statEvent2);
            }
            checkUploadEvent(EventType.getEventType(i), this.mSTATCounter);
            return;
        }
        Logger.e("metric is null,stat commit failed,please call AppMonitor.register() once before AppMonitor.STAT.commit()", new Object[0]);
    }

    public void beginStatEvent(Integer num, String str, String str2, String str3) {
        String transactionId = getTransactionId(str, str2);
        if (transactionId != null) {
            beginStatEvent(transactionId, num, str, str2, str3);
        }
    }

    /* JADX WARNING: type inference failed for: r7v4, types: [com.alibaba.appmonitor.pool.Reusable] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void beginStatEvent(java.lang.String r6, java.lang.Integer r7, java.lang.String r8, java.lang.String r9, java.lang.String r10) {
        /*
            r5 = this;
            com.alibaba.appmonitor.model.MetricRepo r0 = com.alibaba.appmonitor.model.MetricRepo.getRepo()
            com.alibaba.appmonitor.model.Metric r0 = r0.getMetric(r8, r9)
            r1 = 0
            if (r0 == 0) goto L_0x004d
            com.alibaba.mtl.appmonitor.model.MeasureSet r2 = r0.getMeasureSet()
            if (r2 == 0) goto L_0x004d
            com.alibaba.mtl.appmonitor.model.MeasureSet r0 = r0.getMeasureSet()
            com.alibaba.mtl.appmonitor.model.Measure r0 = r0.getMeasure(r10)
            if (r0 == 0) goto L_0x0054
            java.lang.Class<com.alibaba.appmonitor.event.DurationEvent> r0 = com.alibaba.appmonitor.event.DurationEvent.class
            monitor-enter(r0)
            java.util.Map<java.lang.String, com.alibaba.appmonitor.event.DurationEvent> r2 = r5.durationEventMap     // Catch:{ all -> 0x004a }
            java.lang.Object r2 = r2.get(r6)     // Catch:{ all -> 0x004a }
            com.alibaba.appmonitor.event.DurationEvent r2 = (com.alibaba.appmonitor.event.DurationEvent) r2     // Catch:{ all -> 0x004a }
            if (r2 != 0) goto L_0x0045
            com.alibaba.appmonitor.pool.BalancedPool r2 = com.alibaba.appmonitor.pool.BalancedPool.getInstance()     // Catch:{ all -> 0x004a }
            java.lang.Class<com.alibaba.appmonitor.event.DurationEvent> r3 = com.alibaba.appmonitor.event.DurationEvent.class
            r4 = 3
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x004a }
            r4[r1] = r7     // Catch:{ all -> 0x004a }
            r7 = 1
            r4[r7] = r8     // Catch:{ all -> 0x004a }
            r7 = 2
            r4[r7] = r9     // Catch:{ all -> 0x004a }
            com.alibaba.appmonitor.pool.Reusable r7 = r2.poll(r3, r4)     // Catch:{ all -> 0x004a }
            r2 = r7
            com.alibaba.appmonitor.event.DurationEvent r2 = (com.alibaba.appmonitor.event.DurationEvent) r2     // Catch:{ all -> 0x004a }
            java.util.Map<java.lang.String, com.alibaba.appmonitor.event.DurationEvent> r7 = r5.durationEventMap     // Catch:{ all -> 0x004a }
            r7.put(r6, r2)     // Catch:{ all -> 0x004a }
        L_0x0045:
            monitor-exit(r0)     // Catch:{ all -> 0x004a }
            r2.start(r10)
            goto L_0x0054
        L_0x004a:
            r6 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x004a }
            throw r6
        L_0x004d:
            java.lang.String r6 = "log discard!,metric is null,please call AppMonitor.register() once before Transaction.begin(measure)"
            java.lang.Object[] r7 = new java.lang.Object[r1]
            com.alibaba.analytics.utils.Logger.e((java.lang.String) r6, (java.lang.Object[]) r7)
        L_0x0054:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.appmonitor.event.EventRepo.beginStatEvent(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String):void");
    }

    public void endStatEvent(String str, String str2, String str3) {
        String transactionId = getTransactionId(str, str2);
        if (transactionId != null) {
            endStatEvent(transactionId, str3, true);
        }
    }

    public void endStatEvent(String str, String str2, boolean z) {
        DurationEvent durationEvent = this.durationEventMap.get(str);
        if (durationEvent != null && durationEvent.end(str2)) {
            this.durationEventMap.remove(str);
            if (z) {
                resetTransactionId(durationEvent.module, durationEvent.monitorPoint);
            }
            commitStatEvent(durationEvent.eventId, durationEvent.module, durationEvent.monitorPoint, durationEvent.getMeasureValues(), durationEvent.getDimensionValues());
            BalancedPool.getInstance().offer(durationEvent);
        }
    }

    /* JADX WARNING: type inference failed for: r7v3, types: [com.alibaba.appmonitor.pool.Reusable] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void commitElapseEventDimensionValue(java.lang.String r6, java.lang.Integer r7, java.lang.String r8, java.lang.String r9, com.alibaba.mtl.appmonitor.model.DimensionValueSet r10) {
        /*
            r5 = this;
            java.lang.Class<com.alibaba.appmonitor.event.DurationEvent> r0 = com.alibaba.appmonitor.event.DurationEvent.class
            monitor-enter(r0)
            java.util.Map<java.lang.String, com.alibaba.appmonitor.event.DurationEvent> r1 = r5.durationEventMap     // Catch:{ all -> 0x0030 }
            java.lang.Object r1 = r1.get(r6)     // Catch:{ all -> 0x0030 }
            com.alibaba.appmonitor.event.DurationEvent r1 = (com.alibaba.appmonitor.event.DurationEvent) r1     // Catch:{ all -> 0x0030 }
            if (r1 != 0) goto L_0x002b
            com.alibaba.appmonitor.pool.BalancedPool r1 = com.alibaba.appmonitor.pool.BalancedPool.getInstance()     // Catch:{ all -> 0x0030 }
            java.lang.Class<com.alibaba.appmonitor.event.DurationEvent> r2 = com.alibaba.appmonitor.event.DurationEvent.class
            r3 = 3
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x0030 }
            r4 = 0
            r3[r4] = r7     // Catch:{ all -> 0x0030 }
            r7 = 1
            r3[r7] = r8     // Catch:{ all -> 0x0030 }
            r7 = 2
            r3[r7] = r9     // Catch:{ all -> 0x0030 }
            com.alibaba.appmonitor.pool.Reusable r7 = r1.poll(r2, r3)     // Catch:{ all -> 0x0030 }
            r1 = r7
            com.alibaba.appmonitor.event.DurationEvent r1 = (com.alibaba.appmonitor.event.DurationEvent) r1     // Catch:{ all -> 0x0030 }
            java.util.Map<java.lang.String, com.alibaba.appmonitor.event.DurationEvent> r7 = r5.durationEventMap     // Catch:{ all -> 0x0030 }
            r7.put(r6, r1)     // Catch:{ all -> 0x0030 }
        L_0x002b:
            monitor-exit(r0)     // Catch:{ all -> 0x0030 }
            r1.commitDimensionValue(r10)
            return
        L_0x0030:
            r6 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0030 }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.appmonitor.event.EventRepo.commitElapseEventDimensionValue(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, com.alibaba.mtl.appmonitor.model.DimensionValueSet):void");
    }

    private String getTransactionId(String str, String str2) {
        Metric metric = MetricRepo.getRepo().getMetric(str, str2);
        if (metric != null) {
            return metric.getTransactionId();
        }
        return null;
    }

    private void resetTransactionId(String str, String str2) {
        Metric metric = MetricRepo.getRepo().getMetric(str, str2);
        if (metric != null) {
            metric.resetTransactionId();
        }
    }

    private Event getEvent(UTDimensionValueSet uTDimensionValueSet, String str, String str2, String str3, Class<? extends Event> cls) {
        Integer eventId;
        MetricValueSet metricValueSet;
        if (!StringUtils.isNotBlank(str) || !StringUtils.isNotBlank(str2) || (eventId = uTDimensionValueSet.getEventId()) == null) {
            return null;
        }
        synchronized (this.eventMap) {
            metricValueSet = this.eventMap.get(uTDimensionValueSet);
            if (metricValueSet == null) {
                metricValueSet = (MetricValueSet) BalancedPool.getInstance().poll(MetricValueSet.class, new Object[0]);
                this.eventMap.put(uTDimensionValueSet, metricValueSet);
                Logger.d(TAG, "put in Map utDimensionValues", uTDimensionValueSet);
            }
        }
        return metricValueSet.getEvent(eventId, str, str2, str3, cls);
    }

    private void checkUploadEvent(EventType eventType, AtomicInteger atomicInteger) {
        if (atomicInteger.incrementAndGet() >= eventType.getTriggerCount()) {
            Logger.d(TAG, eventType.toString(), " event size exceed trigger count.");
            uploadEvent(eventType.getEventId());
        }
    }

    public Map<UTDimensionValueSet, List<Event>> getUploadEvent(int i) {
        HashMap hashMap = new HashMap();
        synchronized (this.eventMap) {
            Iterator<Map.Entry<UTDimensionValueSet, MetricValueSet>> it = this.eventMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry next = it.next();
                UTDimensionValueSet uTDimensionValueSet = (UTDimensionValueSet) next.getKey();
                MetricValueSet metricValueSet = (MetricValueSet) next.getValue();
                if (uTDimensionValueSet.getEventId().intValue() == i) {
                    if (metricValueSet != null) {
                        hashMap.put(uTDimensionValueSet, metricValueSet.getEvents());
                    } else {
                        Logger.d("error", "utDimensionValues", uTDimensionValueSet);
                    }
                    it.remove();
                }
            }
        }
        getCounter(i).set(0);
        return hashMap;
    }

    public void cleanExpiredEvent() {
        ArrayList arrayList = new ArrayList(this.durationEventMap.keySet());
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            String str = (String) arrayList.get(i);
            DurationEvent durationEvent = this.durationEventMap.get(str);
            if (durationEvent != null && durationEvent.isExpired()) {
                this.durationEventMap.remove(str);
            }
        }
    }

    public void uploadEvent(int i) {
        final Map<UTDimensionValueSet, List<Event>> uploadEvent = getUploadEvent(i);
        TaskExecutor.getInstance().submit(new Runnable() {
            public void run() {
                UTUtil.uploadEvent(uploadEvent);
            }
        });
    }

    private AtomicInteger getCounter(int i) {
        if (65501 == i) {
            return this.mAlarmCounter;
        }
        if (65502 == i) {
            return this.mCountCounter;
        }
        if (65503 == i) {
            return this.mSTATCounter;
        }
        return null;
    }
}
