package com.alimama.union.app.share;

import android.util.Log;
import androidx.annotation.Nullable;

public abstract class Result<T> {
    private static final String TAG = "Result";

    public abstract void onResult(T t);

    /* access modifiers changed from: package-private */
    public void onFailure(@Nullable String str) {
        Log.w("Result", "failed with error: " + str);
    }
}
