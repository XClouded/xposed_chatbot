package com.taobao.android;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import com.taobao.phenix.builder.BitmapPoolBuilder;
import com.taobao.phenix.builder.BytesPoolBuilder;
import com.taobao.phenix.builder.DiskCacheBuilder;
import com.taobao.phenix.builder.FileLoaderBuilder;
import com.taobao.phenix.builder.HttpLoaderBuilder;
import com.taobao.phenix.builder.MemCacheBuilder;
import com.taobao.phenix.builder.SchedulerBuilder;
import com.taobao.phenix.cache.CacheKeyInspector;
import com.taobao.phenix.chain.ImageDecodingListener;
import com.taobao.phenix.decode.EncodedDataInspector;
import com.taobao.phenix.entity.ResponseData;
import com.taobao.phenix.intf.ImageInfo;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.intf.PhenixTicket;
import com.taobao.phenix.intf.PrefetchCreator;
import com.taobao.phenix.loader.LocalSchemeHandler;
import com.taobao.phenix.request.ImageFlowMonitor;
import com.taobao.phenix.strategy.ModuleStrategySupplier;
import java.util.List;

public class PhenixAdapter implements AliImageInterface {
    private final Phenix mPhenix;

    public PhenixAdapter(Phenix phenix) {
        this.mPhenix = phenix;
    }

    public static Phenix instance() {
        return Phenix.instance();
    }

    public boolean isGenericTypeCheckEnabled() {
        return this.mPhenix.isGenericTypeCheckEnabled();
    }

    public void skipGenericTypeCheck(boolean z) {
        this.mPhenix.skipGenericTypeCheck(z);
    }

    public Context applicationContext() {
        return this.mPhenix.applicationContext();
    }

    public EncodedDataInspector getEncodedDataInspector() {
        return this.mPhenix.getEncodedDataInspector();
    }

    public void setCacheKeyInspector(CacheKeyInspector cacheKeyInspector) {
        this.mPhenix.setCacheKeyInspector(cacheKeyInspector);
    }

    public void setImageFlowMonitor(ImageFlowMonitor imageFlowMonitor) {
        this.mPhenix.setImageFlowMonitor(imageFlowMonitor);
    }

    public void setModuleStrategySupplier(ModuleStrategySupplier moduleStrategySupplier) {
        this.mPhenix.setModuleStrategySupplier(moduleStrategySupplier);
    }

    public void setEncodedDataInspector(EncodedDataInspector encodedDataInspector) {
        this.mPhenix.setEncodedDataInspector(encodedDataInspector);
    }

    public boolean registerLocalSchemeHandler(LocalSchemeHandler localSchemeHandler) {
        return this.mPhenix.registerLocalSchemeHandler(localSchemeHandler);
    }

    public boolean unregisterLocalSchemeHandler(LocalSchemeHandler localSchemeHandler) {
        return this.mPhenix.unregisterLocalSchemeHandler(localSchemeHandler);
    }

    public List<LocalSchemeHandler> getExtendedSchemeHandlers() {
        return this.mPhenix.getExtendedSchemeHandlers();
    }

    public MemCacheBuilder memCacheBuilder() {
        return this.mPhenix.memCacheBuilder();
    }

    public DiskCacheBuilder diskCacheBuilder() {
        return this.mPhenix.diskCacheBuilder();
    }

    public FileLoaderBuilder fileLoaderBuilder() {
        return this.mPhenix.fileLoaderBuilder();
    }

    public SchedulerBuilder schedulerBuilder() {
        return this.mPhenix.schedulerBuilder();
    }

    public HttpLoaderBuilder httpLoaderBuilder() {
        return this.mPhenix.httpLoaderBuilder();
    }

    public BytesPoolBuilder bytesPoolBuilder() {
        return this.mPhenix.bytesPoolBuilder();
    }

    public BitmapPoolBuilder bitmapPoolBuilder() {
        return this.mPhenix.bitmapPoolBuilder();
    }

    public Phenix with(Context context) {
        return this.mPhenix.with(context);
    }

    public void build() {
        this.mPhenix.build();
    }

    public Phenix scaleWithLargeImage(boolean z) {
        return this.mPhenix.scaleWithLargeImage(z);
    }

    public Phenix preloadWithLowImage(boolean z) {
        return this.mPhenix.preloadWithLowImage(z);
    }

    public AliImageCreatorInterface load(String str, String str2, CacheKeyInspector cacheKeyInspector) {
        return new PhenixCreatorAdapter(this.mPhenix.load(str, str2, cacheKeyInspector));
    }

    public AliImageCreatorInterface load(String str, CacheKeyInspector cacheKeyInspector) {
        return new PhenixCreatorAdapter(this.mPhenix.load(str, cacheKeyInspector));
    }

    public AliImageCreatorInterface load(String str, String str2) {
        return new PhenixCreatorAdapter(this.mPhenix.load(str, str2));
    }

    public AliImageCreatorInterface load(String str) {
        return new PhenixCreatorAdapter(this.mPhenix.load(str));
    }

    public PrefetchCreator preload(String str, List<String> list) {
        return this.mPhenix.preload(str, list);
    }

    public boolean clearMemory(String str, boolean z) {
        return this.mPhenix.clearMemory(str, z);
    }

    public boolean clearCache(String str, String str2) {
        return this.mPhenix.clearCache(str, str2);
    }

    @Deprecated
    public void clearCache(String str) {
        this.mPhenix.clearCache(str);
    }

    public void clearAll() {
        this.mPhenix.clearAll();
    }

    @Deprecated
    public void cancel(PhenixTicket phenixTicket) {
        this.mPhenix.cancel(phenixTicket);
    }

    @Deprecated
    public BitmapDrawable fetchMemCache(String str) {
        return this.mPhenix.fetchMemCache(str);
    }

    public ResponseData fetchDiskCache(String str, String str2, int i, boolean z) {
        return this.mPhenix.fetchDiskCache(str, str2, i, z);
    }

    @Deprecated
    public List<ImageInfo> hasCategorys(String str) {
        return this.mPhenix.hasCategorys(str);
    }

    public void setImageDecodingListener(ImageDecodingListener imageDecodingListener) {
        this.mPhenix.setImageDecodingListener(imageDecodingListener);
    }

    @Deprecated
    public void shutdown() {
        this.mPhenix.shutdown();
    }
}
