package com.taobao.phenix.cache.disk;

import java.util.Collection;

public interface DiskCacheSupplier {
    DiskCache get(int i);

    Collection<DiskCache> getAll();
}
