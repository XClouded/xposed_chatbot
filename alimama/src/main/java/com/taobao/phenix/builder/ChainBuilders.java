package com.taobao.phenix.builder;

import android.content.Context;

public interface ChainBuilders {
    Context applicationContext();

    DiskCacheBuilder diskCacheBuilder();

    DiskCacheKVBuilder diskCacheKVBuilder();

    FileLoaderBuilder fileLoaderBuilder();

    HttpLoaderBuilder httpLoaderBuilder();

    boolean isGenericTypeCheckEnabled();

    MemCacheBuilder memCacheBuilder();

    SchedulerBuilder schedulerBuilder();
}
