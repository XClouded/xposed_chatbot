package com.taobao.phenix.cache.disk;

import android.content.Context;
import com.taobao.phenix.entity.EncodedData;
import java.io.InputStream;

public class NonOpDiskCache implements DiskCache {
    private final int mPriority;

    public void clear() {
    }

    public boolean close() {
        return false;
    }

    public EncodedData get(String str, int i) {
        return null;
    }

    public long getLength(String str, int i) {
        return -1;
    }

    public boolean isSupportCatalogs() {
        return false;
    }

    public void maxSize(int i) {
    }

    public boolean open(Context context) {
        return false;
    }

    public boolean put(String str, int i, InputStream inputStream) {
        return false;
    }

    public boolean put(String str, int i, byte[] bArr, int i2, int i3) {
        return false;
    }

    public boolean remove(String str, int i) {
        return false;
    }

    public NonOpDiskCache(int i) {
        this.mPriority = i;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public int[] getCatalogs(String str) {
        return new int[0];
    }
}
