package com.taobao.phenix.chain;

import com.taobao.phenix.bitmap.BitmapProcessProducer;
import com.taobao.phenix.builder.ChainBuilders;
import com.taobao.phenix.cache.disk.DiskCacheReader;
import com.taobao.phenix.cache.disk.DiskCacheWriter;
import com.taobao.phenix.cache.memory.MemoryCacheProducer;
import com.taobao.phenix.cache.memory.NonOpMemoryCache;
import com.taobao.phenix.cache.memory.PassableBitmapDrawable;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.decode.DecodeProducer;
import com.taobao.phenix.entity.DecodedImage;
import com.taobao.phenix.loader.file.DefaultFileLoader;
import com.taobao.phenix.loader.file.LocalImageProducer;
import com.taobao.phenix.loader.network.DefaultHttpLoader;
import com.taobao.phenix.loader.network.NetworkImageProducer;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.rxm.common.ChainProducerBuilder;
import com.taobao.rxm.produce.Producer;
import com.taobao.rxm.produce.RequestMultiplexProducer;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.rxm.schedule.SchedulerSupplier;
import com.taobao.tcommon.core.Preconditions;
import com.taobao.tcommon.core.Supplier;

public class NormalChainProducerSupplier implements Supplier<Producer<PassableBitmapDrawable, ImageRequest>> {
    private final ChainBuilders mChainBuilders;
    private Producer<PassableBitmapDrawable, ImageRequest> mHeadProducer;
    private Producer<PassableBitmapDrawable, ImageRequest> mPreBuildProducer;
    private SchedulerSupplier mSchedulerSupplier;
    private boolean mUseNewThreadModel = true;

    public NormalChainProducerSupplier(ChainBuilders chainBuilders) {
        Preconditions.checkNotNull(chainBuilders, "ChainBuilders cannot be NULL when DrawableChainProducerSupplier constructed");
        this.mChainBuilders = chainBuilders;
    }

    public SchedulerSupplier getSchedulerSupplierUsedInProducer() {
        return this.mSchedulerSupplier;
    }

    public synchronized void buildChain() {
        if (this.mHeadProducer == null) {
            this.mSchedulerSupplier = this.mChainBuilders.schedulerBuilder().useNewThreadModel(this.mUseNewThreadModel).build();
            boolean isGenericTypeCheckEnabled = this.mChainBuilders.isGenericTypeCheckEnabled();
            if (this.mUseNewThreadModel) {
                this.mHeadProducer = ChainProducerBuilder.newBuilderWithHead(new MemoryCacheProducer(this.mChainBuilders.memCacheBuilder().build()), isGenericTypeCheckEnabled).next(new RequestMultiplexProducer(DecodedImage.class)).next(new DiskCacheWriter(this.mChainBuilders.diskCacheBuilder().build(), this.mChainBuilders.diskCacheKVBuilder().build())).next(new BitmapProcessProducer()).next(new DecodeProducer().consumeOn(this.mSchedulerSupplier.forDecode())).next(new LocalImageProducer(this.mChainBuilders.fileLoaderBuilder().build()).produceOn(this.mSchedulerSupplier.forCpuBound())).next(new DiskCacheReader(this.mChainBuilders.diskCacheBuilder().build(), this.mChainBuilders.diskCacheKVBuilder().build())).next(new NetworkImageProducer(this.mChainBuilders.httpLoaderBuilder().build()).consumeOn(this.mSchedulerSupplier.forNetwork())).build();
            } else {
                this.mHeadProducer = ChainProducerBuilder.newBuilderWithHead(new MemoryCacheProducer(this.mChainBuilders.memCacheBuilder().build()), isGenericTypeCheckEnabled).next(new RequestMultiplexProducer(DecodedImage.class)).next(new DiskCacheWriter(this.mChainBuilders.diskCacheBuilder().build(), this.mChainBuilders.diskCacheKVBuilder().build()).consumeOn(this.mSchedulerSupplier.forIoBound())).next(new BitmapProcessProducer().consumeOn(this.mSchedulerSupplier.forCpuBound())).next(new DecodeProducer().consumeOn(this.mSchedulerSupplier.forDecode())).next(new LocalImageProducer(this.mChainBuilders.fileLoaderBuilder().build()).produceOn(this.mSchedulerSupplier.forIoBound())).next(new DiskCacheReader(this.mChainBuilders.diskCacheBuilder().build(), this.mChainBuilders.diskCacheKVBuilder().build())).next(new NetworkImageProducer(this.mChainBuilders.httpLoaderBuilder().build()).produceOn(this.mSchedulerSupplier.forNetwork()).consumeOn(this.mSchedulerSupplier.forNetwork())).build();
            }
            this.mPreBuildProducer = null;
        }
    }

    public synchronized Producer<PassableBitmapDrawable, ImageRequest> get() {
        if (this.mHeadProducer != null) {
            return this.mHeadProducer;
        }
        if (this.mPreBuildProducer == null) {
            this.mSchedulerSupplier = new DefaultSchedulerSupplier((Scheduler) null, 0, 6, 8, 5, 1500, 3, 5, 2, -1, this.mUseNewThreadModel);
            if (this.mUseNewThreadModel) {
                this.mPreBuildProducer = ChainProducerBuilder.newBuilderWithHead(new MemoryCacheProducer(new NonOpMemoryCache()), this.mChainBuilders.isGenericTypeCheckEnabled()).next(new RequestMultiplexProducer(DecodedImage.class)).next(new DecodeProducer().consumeOn(this.mSchedulerSupplier.forDecode())).next(new LocalImageProducer(new DefaultFileLoader()).produceOn(this.mSchedulerSupplier.forCpuBound())).next(new NetworkImageProducer(new DefaultHttpLoader()).consumeOn(this.mSchedulerSupplier.forNetwork())).build();
            } else {
                this.mPreBuildProducer = ChainProducerBuilder.newBuilderWithHead(new MemoryCacheProducer(new NonOpMemoryCache()), this.mChainBuilders.isGenericTypeCheckEnabled()).next(new RequestMultiplexProducer(DecodedImage.class)).next(new DecodeProducer().consumeOn(this.mSchedulerSupplier.forDecode())).next(new LocalImageProducer(new DefaultFileLoader()).produceOn(this.mSchedulerSupplier.forIoBound())).next(new NetworkImageProducer(new DefaultHttpLoader()).produceOn(this.mSchedulerSupplier.forNetwork()).consumeOn(this.mSchedulerSupplier.forNetwork())).build();
            }
        }
        UnitedLog.w("NormalChain", "use temporary chain producer before Phenix.instance().build() calling", new Object[0]);
        return this.mPreBuildProducer;
    }

    public void useNewThreadModel(boolean z) {
        this.mUseNewThreadModel = z;
    }
}
