package com.taobao.alivfssdk.fresco.cache.disk;

import com.taobao.alivfssdk.fresco.binaryresource.BinaryResource;
import com.taobao.alivfssdk.fresco.cache.common.CacheKey;
import com.taobao.alivfssdk.fresco.cache.common.WriterCallback;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DiskStorage extends Closeable {

    public static class DiskDumpInfo {
        public List<DiskDumpInfoEntry> entries = new ArrayList();
        public Map<String, Integer> typeCounts = new HashMap();
    }

    public interface Entry {
        String getId();

        BinaryResource getResource();

        long getSize();

        long getTimestamp();
    }

    public interface Inserter {
        boolean cleanUp();

        BinaryResource commit(CacheKey cacheKey, Object obj) throws IOException;

        void writeData(WriterCallback writerCallback, CacheKey cacheKey, Object obj) throws IOException;
    }

    void clearAll() throws IOException;

    boolean contains(String str, CacheKey cacheKey, Object obj) throws IOException;

    List<String> getCatalogs(String str);

    DiskDumpInfo getDumpInfo() throws IOException;

    Collection<Entry> getEntries() throws IOException;

    BinaryResource getResource(String str, CacheKey cacheKey, Object obj) throws IOException;

    String getStorageName();

    Inserter insert(String str, CacheKey cacheKey, Object obj) throws IOException;

    boolean isEnabled();

    boolean isExternal();

    void purgeUnexpectedResources() throws IOException;

    long remove(Entry entry) throws IOException;

    long remove(String str, CacheKey cacheKey) throws IOException;

    boolean touch(String str, CacheKey cacheKey, Object obj) throws IOException;

    public static class DiskDumpInfoEntry {
        public final String firstBits;
        public final String path;
        public final float size;
        public final String type;

        public DiskDumpInfoEntry(String str, String str2, float f, String str3) {
            this.path = str;
            this.type = str2;
            this.size = f;
            this.firstBits = str3;
        }
    }
}
