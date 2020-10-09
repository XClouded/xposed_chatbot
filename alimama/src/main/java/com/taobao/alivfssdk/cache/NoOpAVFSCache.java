package com.taobao.alivfssdk.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NoOpAVFSCache extends AVFSBaseCache {
    private static NoOpAVFSCache sInstance;

    public void clearMemCache() {
    }

    public void close() throws IOException {
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

    public List<String> extendsKeysForKey(@NonNull String str, int i) {
        return null;
    }

    public InputStream inputStreamForKey(@NonNull String str, String str2) {
        return null;
    }

    public InputStream inputStreamForKey(@NonNull String str, String str2, int i) {
        return null;
    }

    public Set<String> keySet() {
        return null;
    }

    public long lengthForKey(String str, String str2) {
        return -1;
    }

    public long lengthForKey(String str, String str2, int i) {
        return 0;
    }

    @Nullable
    public <T> T objectForKey(@NonNull String str, int i) {
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

    public boolean removeAllObject() {
        return false;
    }

    public boolean removeObjectForKey(@NonNull String str, int i) {
        return false;
    }

    public boolean removeObjectForKey(@NonNull String str, String str2) {
        return false;
    }

    public boolean removeObjectForKey(@NonNull String str, String str2, int i) {
        return false;
    }

    public boolean setObjectForKey(@NonNull String str, String str2, Object obj, int i) {
        return false;
    }

    public boolean setStreamForKey(@NonNull String str, String str2, @NonNull InputStream inputStream, int i) {
        return false;
    }

    private NoOpAVFSCache() {
    }

    public static synchronized NoOpAVFSCache getInstance() {
        NoOpAVFSCache noOpAVFSCache;
        synchronized (NoOpAVFSCache.class) {
            if (sInstance == null) {
                sInstance = new NoOpAVFSCache();
            }
            noOpAVFSCache = sInstance;
        }
        return noOpAVFSCache;
    }

    public List<String> extendsKeysForKey(@NonNull String str) {
        return new ArrayList(0);
    }
}
