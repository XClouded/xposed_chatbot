package com.taobao.aipc.utils;

import androidx.core.util.Pair;
import com.taobao.aipc.logs.IPCLog;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

public class CallbackManager {
    private static final String TAG = "CallbackManager";
    private static volatile CallbackManager sInstance;
    private final ConcurrentHashMap<String, CallbackWrapper> mCallbackWrappers = new ConcurrentHashMap<>();

    private CallbackManager() {
    }

    public static CallbackManager getInstance() {
        if (sInstance == null) {
            synchronized (CallbackManager.class) {
                if (sInstance == null) {
                    sInstance = new CallbackManager();
                }
            }
        }
        return sInstance;
    }

    private static String getKey(String str, int i) {
        return str + "." + i;
    }

    public void addCallback(String str, int i, Object obj, boolean z, boolean z2) {
        this.mCallbackWrappers.put(getKey(str, i), new CallbackWrapper(z, obj, z2));
    }

    public Pair<Boolean, Object> getCallback(String str, int i) {
        String key = getKey(str, i);
        CallbackWrapper callbackWrapper = this.mCallbackWrappers.get(key);
        if (callbackWrapper == null) {
            return null;
        }
        Pair<Boolean, Object> pair = callbackWrapper.get();
        if (pair.second == null) {
            this.mCallbackWrappers.remove(key);
        }
        return pair;
    }

    public void removeCallback(String str, int i) {
        if (this.mCallbackWrappers.remove(getKey(str, i)) == null) {
            IPCLog.e(TAG, "An error occurs in the callback GC.");
        }
    }

    private static class CallbackWrapper {
        private Object mCallback;
        private boolean mUiThread;

        CallbackWrapper(boolean z, Object obj, boolean z2) {
            if (z) {
                this.mCallback = new WeakReference(obj);
            } else {
                this.mCallback = obj;
            }
            this.mUiThread = z2;
        }

        public Pair<Boolean, Object> get() {
            Object obj;
            if (this.mCallback instanceof WeakReference) {
                obj = ((WeakReference) this.mCallback).get();
            } else {
                obj = this.mCallback;
            }
            return new Pair<>(Boolean.valueOf(this.mUiThread), obj);
        }
    }
}
