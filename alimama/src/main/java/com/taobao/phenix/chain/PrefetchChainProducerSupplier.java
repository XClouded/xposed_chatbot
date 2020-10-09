package com.taobao.phenix.chain;

import com.taobao.phenix.builder.ChainBuilders;
import com.taobao.phenix.cache.disk.PrefetchDiskCacheProducer;
import com.taobao.phenix.entity.PrefetchImage;
import com.taobao.phenix.loader.network.NetworkImageProducer;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.rxm.common.ChainProducerBuilder;
import com.taobao.rxm.produce.Producer;
import com.taobao.rxm.produce.RequestMultiplexProducer;
import com.taobao.rxm.schedule.SchedulerSupplier;
import com.taobao.tcommon.core.Preconditions;
import com.taobao.tcommon.core.Supplier;

public class PrefetchChainProducerSupplier implements Supplier<Producer<PrefetchImage, ImageRequest>> {
    private final ChainBuilders mChainBuilders;
    private Producer<PrefetchImage, ImageRequest> mHeadProducer;
    private SchedulerSupplier mSchedulerSupplier;

    public PrefetchChainProducerSupplier(ChainBuilders chainBuilders) {
        Preconditions.checkNotNull(chainBuilders, "ChainBuilders cannot be NULL when DrawableChainProducerSupplier constructed");
        this.mChainBuilders = chainBuilders;
    }

    public SchedulerSupplier getSchedulerSupplierUsedInProducer() {
        return this.mSchedulerSupplier;
    }

    public synchronized void buildChain() {
        if (this.mHeadProducer == null) {
            this.mSchedulerSupplier = this.mChainBuilders.schedulerBuilder().build();
            this.mHeadProducer = ChainProducerBuilder.newBuilderWithHead(new RequestMultiplexProducer(PrefetchImage.class), this.mChainBuilders.isGenericTypeCheckEnabled()).next(new PrefetchDiskCacheProducer(this.mChainBuilders.diskCacheBuilder().build()).produceOn(this.mSchedulerSupplier.forIoBound()).consumeOn(this.mSchedulerSupplier.forIoBound())).next(new NetworkImageProducer(this.mChainBuilders.httpLoaderBuilder().build()).produceOn(this.mSchedulerSupplier.forNetwork()).consumeOn(this.mSchedulerSupplier.forNetwork())).build();
        }
    }

    public synchronized Producer<PrefetchImage, ImageRequest> get() {
        return this.mHeadProducer;
    }
}
