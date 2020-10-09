package com.taobao.phenix.chain;

import com.taobao.phenix.cache.memory.MemOnlyFailedException;
import com.taobao.phenix.cache.memory.PassableBitmapDrawable;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.intf.PhenixCreator;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.PhenixEvent;
import com.taobao.phenix.intf.event.ProgressPhenixEvent;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.phenix.loader.network.HttpCodeResponseException;
import com.taobao.phenix.request.ImageFlowMonitor;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.phenix.request.ImageStatistics;
import com.taobao.rxm.consume.BaseConsumer;
import com.taobao.rxm.consume.Consumer;
import com.taobao.rxm.produce.ProducerListener;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.rxm.schedule.SchedulerSupplier;
import java.util.HashMap;
import java.util.Map;

public class PhenixLastConsumer extends BaseConsumer<PassableBitmapDrawable, ImageRequest> {
    private final PhenixCreator mCreator;
    private final ImageFlowMonitor mImageFlowMonitor;
    private final SchedulerSupplier mSchedulerSupplier;

    public PhenixLastConsumer(ImageRequest imageRequest, PhenixCreator phenixCreator, ImageFlowMonitor imageFlowMonitor, SchedulerSupplier schedulerSupplier, ImageDecodingListener imageDecodingListener) {
        super(imageRequest);
        this.mCreator = phenixCreator;
        this.mImageFlowMonitor = imageFlowMonitor;
        this.mSchedulerSupplier = schedulerSupplier;
        imageRequest.setProducerListener(new PhenixProduceListener(imageRequest, phenixCreator.getMemCacheMissListener(), imageDecodingListener));
    }

    /* access modifiers changed from: protected */
    public void onNewResultImpl(PassableBitmapDrawable passableBitmapDrawable, boolean z) {
        long currentTimeMillis = System.currentTimeMillis();
        UnitedLog.e("Phenix", "UI Thread Process CallBack Started.", (ImageRequest) getContext());
        IPhenixListener<SuccPhenixEvent> successListener = this.mCreator.getSuccessListener();
        UnitedLog.d("PhenixConsumer", (ImageRequest) getContext(), "received new result=%s, isLast=%b", passableBitmapDrawable, Boolean.valueOf(z));
        ((ImageRequest) getContext()).getStatistics().mRspCbStart = System.currentTimeMillis();
        if (successListener != null) {
            SuccPhenixEvent succPhenixEvent = new SuccPhenixEvent(((ImageRequest) getContext()).getPhenixTicket());
            succPhenixEvent.setDrawable(passableBitmapDrawable);
            succPhenixEvent.setUrl(((ImageRequest) getContext()).getPath());
            succPhenixEvent.setImmediate(passableBitmapDrawable.isFromMemory());
            succPhenixEvent.setIntermediate(!z);
            succPhenixEvent.fromDisk(passableBitmapDrawable.isFromDisk());
            succPhenixEvent.fromSecondary(passableBitmapDrawable.isFromSecondary());
            successListener.onHappen(succPhenixEvent);
        }
        UnitedLog.e("Phenix", "UI Thread Process CallBack End.", (ImageRequest) getContext());
        ((ImageRequest) getContext()).getStatistics().mRspCbEnd = System.currentTimeMillis();
        if (z) {
            statSuccessFlowIfNeed(currentTimeMillis);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0023, code lost:
        if (android.text.TextUtils.isEmpty(r0) != false) goto L_0x0025;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean willRetry(com.taobao.phenix.request.ImageRequest r7, java.lang.Throwable r8) {
        /*
            r6 = this;
            boolean r0 = r7.isRetrying()
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            r0 = 0
            com.taobao.phenix.request.ImageUriInfo r2 = r7.getImageUriInfo()
            java.lang.String r2 = r2.getPath()
            com.taobao.phenix.intf.PhenixCreator r3 = r6.mCreator
            com.taobao.phenix.intf.event.IRetryHandlerOnFailure r3 = r3.getRetryHandlerOnFailure()
            if (r3 == 0) goto L_0x0025
            com.taobao.phenix.intf.PhenixCreator r0 = r6.mCreator
            java.lang.String r0 = r3.getRetryUrl(r0, r8)
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 == 0) goto L_0x004e
        L_0x0025:
            boolean r3 = r8 instanceof com.taobao.phenix.decode.DecodeException
            if (r3 == 0) goto L_0x004e
            r3 = r8
            com.taobao.phenix.decode.DecodeException r3 = (com.taobao.phenix.decode.DecodeException) r3
            com.taobao.rxm.request.RequestContext r4 = r6.getContext()
            com.taobao.phenix.request.ImageRequest r4 = (com.taobao.phenix.request.ImageRequest) r4
            com.taobao.phenix.request.ImageUriInfo r4 = r4.getImageUriInfo()
            boolean r5 = r3.isDataFromDisk()
            if (r5 == 0) goto L_0x004e
            com.taobao.phenix.decode.DecodeException$DecodedError r5 = com.taobao.phenix.decode.DecodeException.DecodedError.UNLINK_SO_ERROR
            com.taobao.phenix.decode.DecodeException$DecodedError r3 = r3.getDecodedError()
            if (r5 == r3) goto L_0x004e
            boolean r3 = r4.isLocalUri()
            if (r3 != 0) goto L_0x004e
            r7.skipCache()
            r0 = r2
        L_0x004e:
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 == 0) goto L_0x0055
            return r1
        L_0x0055:
            r7.resetBeforeRetry(r0)
            java.lang.String r0 = "PhenixConsumer"
            java.lang.String r3 = "retry to load when received failure=%s, raw=%s"
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r4[r1] = r8
            r8 = 1
            r4[r8] = r2
            com.taobao.phenix.common.UnitedLog.w(r0, r7, r3, r4)
            com.taobao.phenix.intf.PhenixCreator r7 = r6.mCreator
            r7.fetch()
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.chain.PhenixLastConsumer.willRetry(com.taobao.phenix.request.ImageRequest, java.lang.Throwable):boolean");
    }

    /* access modifiers changed from: protected */
    public void onFailureImpl(Throwable th) {
        ImageRequest imageRequest = (ImageRequest) getContext();
        if (!willRetry(imageRequest, th)) {
            if (th instanceof MemOnlyFailedException) {
                UnitedLog.d("PhenixConsumer", (ImageRequest) getContext(), "ignored MemOnlyFailedException(%s)", th);
                return;
            }
            UnitedLog.e("PhenixConsumer", (ImageRequest) getContext(), "received failure=%s", th);
            if (UnitedLog.isLoggable(3) && th != null) {
                th.printStackTrace();
            }
            ((ImageRequest) getContext()).getStatistics().mRspCbStart = System.currentTimeMillis();
            if (this.mCreator.getFailureListener() != null) {
                FailPhenixEvent failPhenixEvent = new FailPhenixEvent(imageRequest.getPhenixTicket());
                if (th != null && (th instanceof HttpCodeResponseException)) {
                    HttpCodeResponseException httpCodeResponseException = (HttpCodeResponseException) th;
                    failPhenixEvent.setHttpCode(httpCodeResponseException.getHttpCode());
                    failPhenixEvent.setHttpMessage(httpCodeResponseException.getMessage());
                }
                failPhenixEvent.setResultCode(404);
                failPhenixEvent.setUrl(((ImageRequest) getContext()).getPath());
                this.mCreator.getFailureListener().onHappen(failPhenixEvent);
            }
            ((ImageRequest) getContext()).getStatistics().mRspCbEnd = System.currentTimeMillis();
            if (this.mImageFlowMonitor != null) {
                this.mImageFlowMonitor.onFail(imageRequest.getStatistics(), th);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCancellationImpl() {
        String path = ((ImageRequest) getContext()).getPath();
        UnitedLog.d("PhenixConsumer", (ImageRequest) getContext(), "received cancellation, cost=%dms", Long.valueOf(System.currentTimeMillis() - ((ImageRequest) getContext()).getRequestStartTime()));
        ((ImageRequest) getContext()).getStatistics().mRspCbDispatch = System.currentTimeMillis();
        ((ImageRequest) getContext()).getStatistics().mRspCbStart = System.currentTimeMillis();
        if (this.mCreator.getCancelListener() != null) {
            this.mCreator.getCancelListener().onHappen(new PhenixEvent(path, ((ImageRequest) getContext()).getPhenixTicket()));
        }
        ((ImageRequest) getContext()).getStatistics().mRspCbEnd = System.currentTimeMillis();
        if (this.mImageFlowMonitor != null) {
            this.mImageFlowMonitor.onCancel(((ImageRequest) getContext()).getStatistics());
        }
        UnitedLog.e(6, "Phenix", "Cancelled | requestId:" + ((ImageRequest) getContext()).getId() + "| url:" + path);
    }

    public void onProgressUpdateImpl(float f) {
        if (this.mCreator.getProgressListener() != null) {
            ProgressPhenixEvent progressPhenixEvent = new ProgressPhenixEvent(((ImageRequest) getContext()).getPhenixTicket(), f);
            progressPhenixEvent.setUrl(((ImageRequest) getContext()).getPath());
            this.mCreator.getProgressListener().onHappen(progressPhenixEvent);
        }
    }

    public Consumer<PassableBitmapDrawable, ImageRequest> consumeOn(Scheduler scheduler) {
        super.consumeOn(scheduler);
        ProducerListener producerListener = ((ImageRequest) getContext()).getProducerListener();
        if (producerListener != null) {
            ((PhenixProduceListener) producerListener).setMemMissScheduler(scheduler);
        }
        return this;
    }

    private void statSuccessFlowIfNeed(long j) {
        if (this.mImageFlowMonitor != null) {
            ImageStatistics statistics = ((ImageRequest) getContext()).getStatistics();
            statistics.setDetailCost(traverseDetailCost(j, false, true, ((ImageRequest) getContext()).getId()));
            this.mImageFlowMonitor.onSuccess(statistics);
        }
    }

    private Map<String, Integer> traverseDetailCost(long j, boolean z, boolean z2, int i) {
        StringBuilder sb;
        HashMap hashMap = null;
        if (!z && !z2) {
            return null;
        }
        int requestStartTime = (int) (j - ((ImageRequest) getContext()).getRequestStartTime());
        int workThreadEndTime = ((ImageRequest) getContext()).getWorkThreadEndTime() <= 0 ? 0 : (int) (j - ((ImageRequest) getContext()).getWorkThreadEndTime());
        if (z) {
            sb = new StringBuilder(150);
            sb.append("User-Callback: ");
            sb.append(System.currentTimeMillis() - j);
            sb.append(10);
            sb.append("Total-Time: ");
            sb.append(requestStartTime);
            sb.append(10);
            sb.append("Wait-Main: ");
            sb.append(workThreadEndTime);
            sb.append(10);
        } else {
            sb = null;
        }
        if (z2) {
            hashMap = new HashMap();
        }
        int i2 = 0;
        for (Map.Entry next : ((ImageRequest) getContext()).getProduceTimeline().entrySet()) {
            String str = (String) next.getKey();
            int intValue = ((Long) next.getValue()).intValue();
            if (z) {
                sb.append(str);
                sb.append(": ");
                if (intValue < 0) {
                    sb.append("Unknown(cause interrupted)");
                } else {
                    sb.append(intValue);
                }
                sb.append(10);
            }
            if (intValue >= 0) {
                i2 += intValue;
                if (z2) {
                    hashMap.put(str, Integer.valueOf(intValue));
                }
            }
        }
        int i3 = i2 + workThreadEndTime;
        if (requestStartTime >= i3) {
            i3 = requestStartTime;
        }
        int i4 = (i3 - i2) - workThreadEndTime;
        if (z2) {
            hashMap.put(ImageStatistics.KEY_TOTAL_TIME, Integer.valueOf(i3));
            hashMap.put(ImageStatistics.KEY_SCHEDULE_TIME, Integer.valueOf(i4));
            if (!(this.mImageFlowMonitor == null || this.mSchedulerSupplier == null || i4 < this.mImageFlowMonitor.getMinimumScheduleTime2StatWaitSize())) {
                hashMap.put(ImageStatistics.KEY_MASTER_WAIT_SIZE, Integer.valueOf(this.mSchedulerSupplier.forCpuBound().getQueueSize()));
                hashMap.put(ImageStatistics.KEY_NETWORK_WAIT_SIZE, Integer.valueOf(this.mSchedulerSupplier.forNetwork().getQueueSize()));
                hashMap.put(ImageStatistics.KEY_DECODE_WAIT_SIZE, Integer.valueOf(this.mSchedulerSupplier.forDecode().getQueueSize()));
            }
            hashMap.put(ImageStatistics.KEY_WAIT_FOR_MAIN, Integer.valueOf(workThreadEndTime));
        }
        if (z) {
            sb.append("Schedule-Time: ");
            sb.append(i4);
            UnitedLog.d("PhenixConsumer", (ImageRequest) getContext(), "Detail-Cost:\n%s\n", sb.substring(0));
        }
        UnitedLog.e("Phenix", "requestId=%d,UI_QUEUE_SIZE=%d", Integer.valueOf(i), Integer.valueOf(this.mSchedulerSupplier.forUiThread().getQueueSize()));
        return hashMap;
    }
}
