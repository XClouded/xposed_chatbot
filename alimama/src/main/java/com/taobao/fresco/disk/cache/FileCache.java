package com.taobao.fresco.disk.cache;

import com.taobao.fresco.disk.common.BinaryResource;
import com.taobao.fresco.disk.common.WriterCallback;
import com.taobao.fresco.disk.storage.DiskStorage;
import java.io.IOException;

public interface FileCache {
    void clearAll();

    long clearOldEntries(long j);

    DiskStorage.DiskDumpInfo getDumpInfo() throws IOException;

    BinaryResource getResource(CacheKey cacheKey);

    long getSize();

    boolean hasKey(CacheKey cacheKey);

    BinaryResource insert(CacheKey cacheKey, WriterCallback writerCallback) throws IOException;

    boolean isEnabled();

    boolean probe(CacheKey cacheKey);

    void remove(CacheKey cacheKey);
}
