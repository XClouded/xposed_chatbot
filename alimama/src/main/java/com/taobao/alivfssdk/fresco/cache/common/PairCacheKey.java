package com.taobao.alivfssdk.fresco.cache.common;

import android.net.Uri;
import com.taobao.alivfssdk.fresco.common.internal.Preconditions;

public class PairCacheKey implements CacheKey {
    final String mKey;
    public final String mKey2;

    public PairCacheKey(String str, String str2) {
        this.mKey = (String) Preconditions.checkNotNull(str);
        this.mKey2 = str2;
    }

    public String toString() {
        return this.mKey;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PairCacheKey pairCacheKey = (PairCacheKey) obj;
        if (this.mKey == null ? pairCacheKey.mKey != null : !this.mKey.equals(pairCacheKey.mKey)) {
            return false;
        }
        if (this.mKey2 != null) {
            return this.mKey2.equals(pairCacheKey.mKey2);
        }
        if (pairCacheKey.mKey2 == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = (this.mKey != null ? this.mKey.hashCode() : 0) * 31;
        if (this.mKey2 != null) {
            i = this.mKey2.hashCode();
        }
        return hashCode + i;
    }

    public boolean containsUri(Uri uri) {
        return this.mKey.contains(uri.toString());
    }
}
