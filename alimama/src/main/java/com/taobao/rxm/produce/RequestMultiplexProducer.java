package com.taobao.rxm.produce;

import android.text.TextUtils;
import com.taobao.rxm.common.Constant;
import com.taobao.rxm.common.Releasable;
import com.taobao.rxm.consume.Consumer;
import com.taobao.rxm.request.MultiplexCancelListener;
import com.taobao.rxm.request.RequestContext;
import com.taobao.rxm.schedule.ScheduleResultWrapper;
import com.taobao.tcommon.log.FLog;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMultiplexProducer<OUT extends Releasable, CONTEXT extends RequestContext> extends BaseChainProducer<OUT, OUT, CONTEXT> implements MultiplexCancelListener {
    private static final int MIN_ARRAY_CAPACITY = 2;
    private Map<Integer, ArrayList<Consumer<OUT, CONTEXT>>> mConsumerGroups = new ConcurrentHashMap();
    private Map<String, Integer> mKeyToGroupId = new ConcurrentHashMap();
    public Class<OUT> mOutClass;

    public RequestMultiplexProducer(Class<OUT> cls) {
        super(1, 29);
        this.mOutClass = cls;
    }

    public Type getOutType() {
        return this.mOutClass;
    }

    public Type getNextOutType() {
        return this.mOutClass;
    }

    /* access modifiers changed from: protected */
    public boolean conductResult(Consumer<OUT, CONTEXT> consumer) {
        boolean z;
        ArrayList arrayList;
        RequestContext requestContext = (RequestContext) consumer.getContext();
        String multiplexKey = requestContext.getMultiplexKey();
        synchronized (this) {
            Integer num = this.mKeyToGroupId.get(multiplexKey);
            if (num == null) {
                num = Integer.valueOf(requestContext.getId());
                this.mKeyToGroupId.put(multiplexKey, num);
                arrayList = new ArrayList(2);
                this.mConsumerGroups.put(num, arrayList);
                z = false;
            } else {
                arrayList = this.mConsumerGroups.get(num);
                z = true;
            }
            requestContext.setMultiplexPipeline(num.intValue());
            requestContext.setMultiplexCancelListener(this);
            addConsumer2Group(arrayList, consumer);
        }
        return z;
    }

    private void addConsumer2Group(ArrayList<Consumer<OUT, CONTEXT>> arrayList, Consumer<OUT, CONTEXT> consumer) {
        arrayList.add(consumer);
        int schedulePriority = ((RequestContext) consumer.getContext()).getSchedulePriority();
        RequestContext requestContext = (RequestContext) getPipelineConsumer(arrayList).getContext();
        if (schedulePriority > requestContext.getSchedulePriority()) {
            requestContext.setSchedulePriority(schedulePriority);
        }
        if (requestContext.isCancelled() && !((RequestContext) consumer.getContext()).isCancelledInMultiplex()) {
            requestContext.cancelInMultiplex(false);
        }
    }

    private Consumer<OUT, CONTEXT> getPipelineConsumer(ArrayList<Consumer<OUT, CONTEXT>> arrayList) {
        return arrayList.get(0);
    }

    private void dispatchResultByType(Consumer<OUT, CONTEXT> consumer, ScheduleResultWrapper<OUT> scheduleResultWrapper) {
        ScheduleResultWrapper<OUT> scheduleResultWrapper2 = scheduleResultWrapper;
        RequestContext requestContext = (RequestContext) consumer.getContext();
        ArrayList arrayList = this.mConsumerGroups.get(Integer.valueOf(requestContext.getId()));
        String multiplexKey = requestContext.getMultiplexKey();
        int i = 4;
        if (arrayList == null) {
            FLog.w(Constant.RX_LOG, "[RequestMultiplex] group has been removed from multiplex, but pipeline is still producing new result(multiplex:%s, id:%d, pipeline:%d, type:%d)", multiplexKey, Integer.valueOf(requestContext.getId()), Integer.valueOf(requestContext.getMultiplexPipeline()), Integer.valueOf(scheduleResultWrapper2.consumeType));
            return;
        }
        synchronized (this) {
            int size = arrayList.size();
            int i2 = 0;
            while (i2 < size) {
                Consumer<OUT, CONTEXT> consumer2 = (Consumer) arrayList.get(i2);
                RequestContext requestContext2 = (RequestContext) consumer2.getContext();
                if (consumer2 != consumer) {
                    requestContext2.syncFrom(requestContext);
                }
                if (!requestContext2.isCancelledInMultiplex()) {
                    int i3 = scheduleResultWrapper2.consumeType;
                    if (i3 == 1) {
                        consumer2.onNewResult(scheduleResultWrapper2.newResult, scheduleResultWrapper2.isLast);
                    } else if (i3 == i) {
                        consumer2.onProgressUpdate(scheduleResultWrapper2.progress);
                    } else if (i3 == 8) {
                        FLog.e(Constant.RX_LOG, "[RequestMultiplex] ID=%d consumers of the group were not all cancelled, but pipeline dispatched cancellation result", Integer.valueOf(requestContext2.getId()));
                        consumer2.onCancellation();
                    } else if (i3 == 16) {
                        consumer2.onFailure(scheduleResultWrapper2.throwable);
                    }
                } else {
                    if (scheduleResultWrapper2.consumeType == 16) {
                        FLog.i(Constant.RX_LOG, "[RequestMultiplex] ID=%d received error after cancellation, throwable=%s", Integer.valueOf(requestContext2.getId()), scheduleResultWrapper2.throwable);
                    }
                    consumer2.onCancellation();
                }
                i2++;
                i = 4;
            }
            if (scheduleResultWrapper2.isLast) {
                if (!TextUtils.isEmpty(multiplexKey)) {
                    this.mKeyToGroupId.remove(multiplexKey);
                }
                this.mConsumerGroups.remove(Integer.valueOf(requestContext.getId()));
            }
        }
    }

    private boolean isGroupCancelled(ArrayList<Consumer<OUT, CONTEXT>> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (!((RequestContext) arrayList.get(i).getContext()).isCancelledInMultiplex()) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x003f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCancelRequest(com.taobao.rxm.request.RequestContext r8) {
        /*
            r7 = this;
            java.lang.String r0 = r8.getMultiplexKey()
            java.util.Map<java.lang.String, java.lang.Integer> r1 = r7.mKeyToGroupId
            boolean r1 = r1.containsKey(r0)
            if (r1 != 0) goto L_0x000d
            return
        L_0x000d:
            java.util.Map<java.lang.Integer, java.util.ArrayList<com.taobao.rxm.consume.Consumer<OUT, CONTEXT>>> r1 = r7.mConsumerGroups
            int r8 = r8.getMultiplexPipeline()
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            java.lang.Object r8 = r1.get(r8)
            java.util.ArrayList r8 = (java.util.ArrayList) r8
            if (r8 != 0) goto L_0x0020
            return
        L_0x0020:
            monitor-enter(r7)
            com.taobao.rxm.consume.Consumer r1 = r7.getPipelineConsumer(r8)     // Catch:{ all -> 0x005c }
            java.lang.Object r2 = r1.getContext()     // Catch:{ all -> 0x005c }
            com.taobao.rxm.request.RequestContext r2 = (com.taobao.rxm.request.RequestContext) r2     // Catch:{ all -> 0x005c }
            boolean r2 = r2.isCancelled()     // Catch:{ all -> 0x005c }
            r3 = 0
            r4 = 1
            if (r2 != 0) goto L_0x003c
            boolean r8 = r7.isGroupCancelled(r8)     // Catch:{ all -> 0x005c }
            if (r8 == 0) goto L_0x003a
            goto L_0x003c
        L_0x003a:
            r8 = 0
            goto L_0x003d
        L_0x003c:
            r8 = 1
        L_0x003d:
            if (r8 == 0) goto L_0x004f
            java.util.Map<java.lang.String, java.lang.Integer> r2 = r7.mKeyToGroupId     // Catch:{ all -> 0x005c }
            r2.remove(r0)     // Catch:{ all -> 0x005c }
            java.lang.String r2 = "RxSysLog"
            java.lang.String r5 = "[RequestMultiplex] all of context in group[key:%s] were cancelled, remove it from KeyToGroupId"
            java.lang.Object[] r6 = new java.lang.Object[r4]     // Catch:{ all -> 0x005c }
            r6[r3] = r0     // Catch:{ all -> 0x005c }
            com.taobao.tcommon.log.FLog.d(r2, r5, r6)     // Catch:{ all -> 0x005c }
        L_0x004f:
            monitor-exit(r7)     // Catch:{ all -> 0x005c }
            if (r8 == 0) goto L_0x005b
            java.lang.Object r8 = r1.getContext()
            com.taobao.rxm.request.RequestContext r8 = (com.taobao.rxm.request.RequestContext) r8
            r8.cancelInMultiplex(r4)
        L_0x005b:
            return
        L_0x005c:
            r8 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x005c }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.rxm.produce.RequestMultiplexProducer.onCancelRequest(com.taobao.rxm.request.RequestContext):void");
    }

    public void consumeNewResult(Consumer<OUT, CONTEXT> consumer, boolean z, OUT out) {
        ScheduleResultWrapper scheduleResultWrapper = new ScheduleResultWrapper(1, z);
        scheduleResultWrapper.newResult = out;
        dispatchResultByType(consumer, scheduleResultWrapper);
    }

    public void consumeProgressUpdate(Consumer<OUT, CONTEXT> consumer, float f) {
        ScheduleResultWrapper scheduleResultWrapper = new ScheduleResultWrapper(4, false);
        scheduleResultWrapper.progress = f;
        dispatchResultByType(consumer, scheduleResultWrapper);
    }

    public void consumeCancellation(Consumer<OUT, CONTEXT> consumer) {
        dispatchResultByType(consumer, new ScheduleResultWrapper(8, true));
    }

    public void consumeFailure(Consumer<OUT, CONTEXT> consumer, Throwable th) {
        ScheduleResultWrapper scheduleResultWrapper = new ScheduleResultWrapper(16, true);
        scheduleResultWrapper.throwable = th;
        dispatchResultByType(consumer, scheduleResultWrapper);
    }
}
