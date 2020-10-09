package com.alibaba.android.prefetchx.core.data;

import androidx.annotation.Nullable;
import com.alibaba.android.prefetchx.PFException;

public interface StorageInterface<T> {

    public interface Callback {
        void onError(String str, String str2);

        void onSuccess(String str);
    }

    boolean containsKey(String str) throws PFException;

    @Nullable
    T read(String str) throws PFException;

    void readAsync(String str, Callback callback);

    void remove(String str) throws PFException;

    void save(String str, T t) throws PFException;
}
