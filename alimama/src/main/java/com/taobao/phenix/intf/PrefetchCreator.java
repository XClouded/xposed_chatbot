package com.taobao.phenix.intf;

import android.taobao.windvane.jsbridge.api.WVAPI;
import com.taobao.phenix.chain.PrefetchChainProducerSupplier;
import com.taobao.phenix.chain.PrefetchLastConsumer;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.entity.PrefetchImage;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.PrefetchEvent;
import com.taobao.phenix.request.ImageFlowMonitor;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.phenix.strategy.ModuleStrategy;
import com.taobao.rxm.produce.Producer;
import com.taobao.tcommon.core.Preconditions;
import java.util.ArrayList;
import java.util.List;

public class PrefetchCreator {
    public static final int MAX_PREFETCH_COUNT_ONCE = 100;
    private IPhenixListener<PrefetchEvent> mCompleteListener;
    private final PrefetchEvent mPrefetchEvent;
    private IPhenixListener<PrefetchEvent> mProgressListener;
    private final ModuleStrategy mStrategy;
    private List<String> mUrls;

    PrefetchCreator(ModuleStrategy moduleStrategy, List<String> list) {
        Preconditions.checkNotNull(moduleStrategy, "module strategy for prefetch cannot be null");
        Preconditions.checkArgument(list != null && list.size() > 0, "Urls of prefetch cannot be empty");
        this.mStrategy = moduleStrategy;
        this.mUrls = list;
        this.mPrefetchEvent = new PrefetchEvent(new ArrayList(), new ArrayList());
        int size = this.mUrls.size();
        if (size > 100) {
            this.mPrefetchEvent.listOfFailed.addAll(this.mUrls.subList(100, size));
            this.mUrls = this.mUrls.subList(0, 100);
            UnitedLog.w(WVAPI.PluginName.API_PREFETCH, "fetch count exceed MAX_PREFETCH_COUNT_ONCE(%d), slice the part exceeding to list of failed", 100);
        }
        this.mPrefetchEvent.totalCount = this.mUrls.size();
    }

    public PrefetchCreator completeListener(IPhenixListener<PrefetchEvent> iPhenixListener) {
        this.mCompleteListener = iPhenixListener;
        return this;
    }

    public PrefetchCreator progressListener(IPhenixListener<PrefetchEvent> iPhenixListener) {
        this.mProgressListener = iPhenixListener;
        return this;
    }

    private ImageRequest newRequest(String str) {
        ImageRequest imageRequest = new ImageRequest(str, Phenix.instance().getCacheKeyInspector(), Phenix.instance().isGenericTypeCheckEnabled());
        imageRequest.setModuleName(this.mStrategy.name);
        imageRequest.setSchedulePriority(1);
        imageRequest.setMemoryCachePriority(this.mStrategy.memoryCachePriority);
        imageRequest.setDiskCachePriority(this.mStrategy.diskCachePriority);
        imageRequest.allowSizeLevel(this.mStrategy.preloadWithSmall, 2);
        imageRequest.allowSizeLevel(this.mStrategy.scaleFromLarge, 4);
        return imageRequest;
    }

    public void fetch() {
        UnitedLog.d(WVAPI.PluginName.API_PREFETCH, "Start to prefetch with business=%s, total=%d", this.mStrategy.name, Integer.valueOf(this.mPrefetchEvent.totalCount));
        PrefetchChainProducerSupplier prefetchProducerSupplier = Phenix.instance().getPrefetchProducerSupplier();
        Producer<PrefetchImage, ImageRequest> producer = prefetchProducerSupplier.get();
        if (producer == null) {
            UnitedLog.e(WVAPI.PluginName.API_PREFETCH, "Cannot prefetch before Phenix.build() calling", new Object[0]);
            this.mPrefetchEvent.listOfFailed.addAll(this.mUrls);
            this.mCompleteListener.onHappen(this.mPrefetchEvent);
            return;
        }
        ImageFlowMonitor imageFlowMonitor = Phenix.instance().getImageFlowMonitor();
        for (String newRequest : this.mUrls) {
            PrefetchLastConsumer prefetchLastConsumer = new PrefetchLastConsumer(newRequest(newRequest), this);
            prefetchLastConsumer.setMonitor(imageFlowMonitor);
            producer.produceResults(prefetchLastConsumer.consumeOn(prefetchProducerSupplier.getSchedulerSupplierUsedInProducer().forUiThread()));
        }
    }

    public void onImageComplete(ImageRequest imageRequest, PrefetchImage prefetchImage, Throwable th) {
        if (prefetchImage != null) {
            this.mPrefetchEvent.listOfSucceeded.add(imageRequest.getPath());
            PrefetchEvent prefetchEvent = this.mPrefetchEvent;
            prefetchEvent.completeSize = (int) (((long) prefetchEvent.completeSize) + prefetchImage.length);
            PrefetchEvent prefetchEvent2 = this.mPrefetchEvent;
            prefetchEvent2.downloadSize = (int) (((long) prefetchEvent2.downloadSize) + (prefetchImage.fromDisk ? 0 : prefetchImage.length));
            this.mPrefetchEvent.downloadCount += prefetchImage.fromDisk ^ true ? 1 : 0;
        } else {
            this.mPrefetchEvent.listOfFailed.add(imageRequest.getPath());
            if (th != null) {
                this.mPrefetchEvent.listOfThrowable.add(th);
            }
        }
        this.mPrefetchEvent.completeCount++;
        if (this.mProgressListener != null) {
            UnitedLog.d(WVAPI.PluginName.API_PREFETCH, "Progress on happen with business=%s, event=%s", this.mStrategy.name, this.mPrefetchEvent);
            this.mProgressListener.onHappen(this.mPrefetchEvent);
        }
        if (this.mCompleteListener != null && this.mPrefetchEvent.completeCount == this.mPrefetchEvent.totalCount) {
            this.mPrefetchEvent.allSucceeded = this.mPrefetchEvent.listOfFailed.size() == 0;
            UnitedLog.d(WVAPI.PluginName.API_PREFETCH, "Complete on happen with business=%s, event=%s", this.mStrategy.name, this.mPrefetchEvent);
            this.mCompleteListener.onHappen(this.mPrefetchEvent);
        }
    }
}
