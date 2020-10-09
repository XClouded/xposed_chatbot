package com.taobao.alivfssdk.fresco.cache.common;

import android.net.Uri;
import com.taobao.alivfssdk.fresco.common.internal.Preconditions;

public class SimpleCacheKey implements CacheKey {
    final String mKey;

    public SimpleCacheKey(String str) {
        this.mKey = (String) Preconditions.checkNotNull(str);
    }

    public String toString() {
        return this.mKey;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof SimpleCacheKey) {
            return this.mKey.equals(((SimpleCacheKey) obj).mKey);
        }
        return false;
    }

    public int hashCode() {
        return this.mKey.hashCode();
    }

    public boolean containsUri(Uri uri) {
        return this.mKey.contains(uri.toString());
    }
}
