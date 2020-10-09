package com.taobao.alivfssdk.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ali.protodb.lsdb.Iterator;
import com.ali.protodb.lsdb.Key;
import com.ali.protodb.lsdb.LSDB;
import com.ali.protodb.lsdb.LSDBConfig;
import com.taobao.alivfssdk.cache.IAVFSCache;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LSMCache implements IAVFSCache {
    private static final char BREAK_LINE = '-';
    private final LSDB lsdb;

    public void clearMemCache() {
    }

    public void close() throws IOException {
    }

    public void containObjectForKey(@NonNull String str, IAVFSCache.OnObjectContainedCallback onObjectContainedCallback) {
    }

    public void containObjectForKey(@NonNull String str, String str2, IAVFSCache.OnObjectContainedCallback2 onObjectContainedCallback2) {
    }

    public boolean containObjectForKey(@NonNull String str) {
        return false;
    }

    public boolean containObjectForKey(@NonNull String str, int i) {
        return false;
    }

    public boolean containObjectForKey(@NonNull String str, String str2) {
        return false;
    }

    public boolean containObjectForKey(@NonNull String str, String str2, int i) {
        return false;
    }

    public InputStream inputStreamForKey(@NonNull String str, String str2, int i) {
        return null;
    }

    public void inputStreamForKey(@NonNull String str, IAVFSCache.OnStreamGetCallback onStreamGetCallback) {
    }

    public void inputStreamForKey(@NonNull String str, String str2, IAVFSCache.OnStreamGetCallback2 onStreamGetCallback2) {
    }

    public long lengthForKey(String str, String str2, int i) {
        return 0;
    }

    @Nullable
    public <T> T objectForKey(@NonNull String str) {
        return null;
    }

    @Nullable
    public <T> T objectForKey(@NonNull String str, int i) {
        return null;
    }

    @Nullable
    public <T> T objectForKey(@NonNull String str, Class<T> cls) {
        return null;
    }

    @Nullable
    public <T> T objectForKey(@NonNull String str, String str2) {
        return null;
    }

    @Nullable
    public <T> T objectForKey(@NonNull String str, String str2, Class<T> cls) {
        return null;
    }

    @Nullable
    public <T> T objectForKey(@NonNull String str, String str2, Class<T> cls, int i) {
        return null;
    }

    public <T> void objectForKey(@NonNull String str, Class<T> cls, IAVFSCache.OnObjectGetCallback<T> onObjectGetCallback) {
    }

    public <T> void objectForKey(@NonNull String str, String str2, Class<T> cls, IAVFSCache.OnObjectGetCallback2<T> onObjectGetCallback2) {
    }

    public void removeAllObject(IAVFSCache.OnAllObjectRemoveCallback onAllObjectRemoveCallback) {
    }

    public boolean removeAllObject() {
        return false;
    }

    public void removeObjectForKey(@NonNull String str, IAVFSCache.OnObjectRemoveCallback onObjectRemoveCallback) {
    }

    public void removeObjectForKey(@NonNull String str, String str2, IAVFSCache.OnObjectRemoveCallback2 onObjectRemoveCallback2) {
    }

    public boolean removeObjectForKey(@NonNull String str, int i) {
        return false;
    }

    public boolean removeObjectForKey(@NonNull String str, String str2, int i) {
        return false;
    }

    public void setObjectForKey(@NonNull String str, Object obj, int i, IAVFSCache.OnObjectSetCallback onObjectSetCallback) {
    }

    public void setObjectForKey(@NonNull String str, Object obj, IAVFSCache.OnObjectSetCallback onObjectSetCallback) {
    }

    public void setObjectForKey(@NonNull String str, String str2, Object obj, int i, IAVFSCache.OnObjectSetCallback2 onObjectSetCallback2) {
    }

    public void setObjectForKey(@NonNull String str, String str2, Object obj, IAVFSCache.OnObjectSetCallback2 onObjectSetCallback2) {
    }

    public boolean setObjectForKey(@NonNull String str, Object obj) {
        return false;
    }

    public boolean setObjectForKey(@NonNull String str, Object obj, int i) {
        return false;
    }

    public boolean setObjectForKey(@NonNull String str, String str2, Object obj) {
        return false;
    }

    public boolean setObjectForKey(@NonNull String str, String str2, Object obj, int i) {
        return false;
    }

    public void setStreamForKey(@NonNull String str, @NonNull InputStream inputStream, int i, IAVFSCache.OnStreamSetCallback onStreamSetCallback) {
    }

    public void setStreamForKey(@NonNull String str, @NonNull InputStream inputStream, IAVFSCache.OnStreamSetCallback onStreamSetCallback) {
    }

    public void setStreamForKey(@NonNull String str, String str2, @NonNull InputStream inputStream, int i, IAVFSCache.OnStreamSetCallback2 onStreamSetCallback2) {
    }

    public void setStreamForKey(@NonNull String str, String str2, @NonNull InputStream inputStream, IAVFSCache.OnStreamSetCallback2 onStreamSetCallback2) {
    }

    public boolean setStreamForKey(@NonNull String str, @NonNull InputStream inputStream, int i) {
        return false;
    }

    public boolean setStreamForKey(@NonNull String str, String str2, @NonNull InputStream inputStream, int i) {
        return false;
    }

    public static LSMCache open(String str, int i, long j) {
        LSDBConfig lSDBConfig = new LSDBConfig();
        lSDBConfig.setWalSize(i);
        lSDBConfig.setTimeToLive(j);
        return new LSMCache(LSDB.open(str, lSDBConfig));
    }

    private LSMCache(LSDB lsdb2) {
        this.lsdb = lsdb2;
    }

    private String makeCacheKey(String str, String str2) {
        return str + BREAK_LINE + str2;
    }

    private String makeCacheKeyLowerLimit(String str) {
        return str + BREAK_LINE;
    }

    private String makeCacheKeyUpperLimit(String str) {
        return str + '.';
    }

    public Set<String> keySet() {
        Iterator<Key> keyIterator = this.lsdb.keyIterator();
        HashSet hashSet = new HashSet();
        if (keyIterator != null) {
            while (true) {
                Key next = keyIterator.next();
                if (next == null) {
                    break;
                }
                hashSet.add(next.getStringKey());
            }
        }
        return hashSet;
    }

    public List<String> extendsKeysForKey(@NonNull String str) {
        Iterator<Key> keyIterator = this.lsdb.keyIterator(new Key(makeCacheKeyLowerLimit(str)), new Key(makeCacheKeyUpperLimit(str)));
        if (keyIterator == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        while (true) {
            Key next = keyIterator.next();
            if (next == null) {
                return arrayList;
            }
            arrayList.add(next.getStringKey());
        }
    }

    public List<String> extendsKeysForKey(@NonNull String str, int i) {
        return new ArrayList();
    }

    public boolean removeObjectForKey(@NonNull String str) {
        return this.lsdb.delete(new Key(str));
    }

    public boolean removeObjectForKey(@NonNull String str, String str2) {
        return this.lsdb.delete(new Key(makeCacheKey(str, str2)));
    }

    @Nullable
    public InputStream inputStreamForKey(@NonNull String str) {
        byte[] binary = this.lsdb.getBinary(new Key(str));
        if (binary != null) {
            return new ByteArrayInputStream(binary);
        }
        return null;
    }

    public InputStream inputStreamForKey(@NonNull String str, String str2) {
        byte[] binary = this.lsdb.getBinary(new Key(makeCacheKey(str, str2)));
        if (binary != null) {
            return new ByteArrayInputStream(binary);
        }
        return null;
    }

    public boolean setStreamForKey(@NonNull String str, @NonNull InputStream inputStream) {
        return this.lsdb.insertStream(new Key(str), inputStream);
    }

    public boolean setStreamForKey(@NonNull String str, String str2, @NonNull InputStream inputStream) {
        return this.lsdb.insertStream(new Key(makeCacheKey(str, str2)), inputStream);
    }

    public long lengthForKey(String str) {
        return this.lsdb.getDataSize(new Key(str));
    }

    public long lengthForKey(String str, String str2) {
        return this.lsdb.getDataSize(new Key(makeCacheKey(str, str2)));
    }
}
