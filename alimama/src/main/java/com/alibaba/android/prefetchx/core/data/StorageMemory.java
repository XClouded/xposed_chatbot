package com.alibaba.android.prefetchx.core.data;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.alibaba.android.prefetchx.core.data.StorageInterface;
import java.util.Map;
import java.util.WeakHashMap;

public class StorageMemory implements StorageInterface<String> {
    private static volatile StorageMemory instance;
    private Map<String, String> map = new WeakHashMap();

    private StorageMemory() {
    }

    public static StorageMemory getInstance() {
        if (instance == null) {
            synchronized (StorageMemory.class) {
                if (instance == null) {
                    instance = new StorageMemory();
                }
            }
        }
        return instance;
    }

    public void save(String str, String str2) {
        this.map.put(str, str2);
    }

    @Nullable
    public String read(String str) {
        return this.map.get(str);
    }

    public void readAsync(String str, StorageInterface.Callback callback) {
        String str2 = this.map.get(str);
        if (TextUtils.isEmpty(str2)) {
            callback.onError("502", "no result find.");
        } else {
            callback.onSuccess(str2);
        }
    }

    public void remove(String str) {
        this.map.remove(str);
    }

    public boolean containsKey(String str) {
        return this.map.containsKey(str);
    }
}
