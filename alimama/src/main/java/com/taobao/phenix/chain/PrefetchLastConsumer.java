package com.taobao.phenix.chain;

import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.entity.PrefetchImage;
import com.taobao.phenix.intf.PrefetchCreator;
import com.taobao.phenix.request.ImageFlowMonitor;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.rxm.consume.BaseConsumer;

public class PrefetchLastConsumer extends BaseConsumer<PrefetchImage, ImageRequest> {
    private final PrefetchCreator mCreator;
    private ImageFlowMonitor mMonitor;

    public PrefetchLastConsumer(ImageRequest imageRequest, PrefetchCreator prefetchCreator) {
        super(imageRequest);
        this.mCreator = prefetchCreator;
    }

    public void setMonitor(ImageFlowMonitor imageFlowMonitor) {
        this.mMonitor = imageFlowMonitor;
    }

    /* access modifiers changed from: protected */
    public void onNewResultImpl(PrefetchImage prefetchImage, boolean z) {
        ((ImageRequest) getContext()).getStatistics().mRspCbStart = System.currentTimeMillis();
        this.mCreator.onImageComplete((ImageRequest) getContext(), prefetchImage, (Throwable) null);
        ((ImageRequest) getContext()).getStatistics().mRspCbEnd = System.currentTimeMillis();
        if (this.mMonitor != null) {
            ((ImageRequest) getContext()).getStatistics().mIsOnlyFullTrack = true;
            this.mMonitor.onSuccess(((ImageRequest) getContext()).getStatistics());
        }
    }

    /* access modifiers changed from: protected */
    public void onFailureImpl(Throwable th) {
        if (UnitedLog.isLoggable(3) && th != null) {
            th.printStackTrace();
        }
        UnitedLog.e("PrefetchConsumer", (ImageRequest) getContext(), "received failure=%s", th);
        ((ImageRequest) getContext()).getStatistics().mRspCbStart = System.currentTimeMillis();
        this.mCreator.onImageComplete((ImageRequest) getContext(), (PrefetchImage) null, th);
        ((ImageRequest) getContext()).getStatistics().mRspCbEnd = System.currentTimeMillis();
        if (this.mMonitor != null) {
            ((ImageRequest) getContext()).getStatistics().mIsOnlyFullTrack = true;
            this.mMonitor.onFail(((ImageRequest) getContext()).getStatistics(), th);
        }
    }

    /* access modifiers changed from: protected */
    public void onCancellationImpl() {
        ((ImageRequest) getContext()).getStatistics().mRspCbDispatch = System.currentTimeMillis();
        ((ImageRequest) getContext()).getStatistics().mRspCbStart = System.currentTimeMillis();
        this.mCreator.onImageComplete((ImageRequest) getContext(), (PrefetchImage) null, (Throwable) null);
        ((ImageRequest) getContext()).getStatistics().mRspCbEnd = System.currentTimeMillis();
        if (this.mMonitor != null) {
            this.mMonitor.onCancel(((ImageRequest) getContext()).getStatistics());
        }
    }
}
