package com.taobao.alivfssdk.cache;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.alivfssdk.cache.IAVFSCache;
import java.io.InputStream;

public abstract class AVFSBaseCache implements IAVFSCache {
    private static final String TAG = "AVFSBaseCache";

    public boolean containObjectForKey(@NonNull String str) {
        return containObjectForKey(str, (String) null);
    }

    public void containObjectForKey(@NonNull String str, final IAVFSCache.OnObjectContainedCallback onObjectContainedCallback) {
        containObjectForKey(str, (String) null, new IAVFSCache.OnObjectContainedCallback2() {
            public void onObjectContainedCallback(@NonNull String str, String str2, boolean z) {
                onObjectContainedCallback.onObjectContainedCallback(str, z);
            }
        });
    }

    public void containObjectForKey(@NonNull final String str, final String str2, final IAVFSCache.OnObjectContainedCallback2 onObjectContainedCallback2) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                onObjectContainedCallback2.onObjectContainedCallback(str, str2, AVFSBaseCache.this.containObjectForKey(str, str2));
            }
        });
    }

    @Nullable
    public <T> T objectForKey(@NonNull String str) {
        return objectForKey(str, (String) null);
    }

    @Nullable
    public <T> T objectForKey(@NonNull String str, Class<T> cls) {
        return objectForKey(str, (String) null, cls);
    }

    public <T> void objectForKey(@NonNull String str, Class<T> cls, final IAVFSCache.OnObjectGetCallback<T> onObjectGetCallback) {
        objectForKey(str, (String) null, cls, new IAVFSCache.OnObjectGetCallback2<T>() {
            public void onObjectGetCallback(@NonNull String str, String str2, Object obj) {
                onObjectGetCallback.onObjectGetCallback(str, obj);
            }
        });
    }

    public <T> void objectForKey(@NonNull String str, String str2, Class<T> cls, IAVFSCache.OnObjectGetCallback2<T> onObjectGetCallback2) {
        final IAVFSCache.OnObjectGetCallback2<T> onObjectGetCallback22 = onObjectGetCallback2;
        final String str3 = str;
        final String str4 = str2;
        final Class<T> cls2 = cls;
        AsyncTask.execute(new Runnable() {
            public void run() {
                onObjectGetCallback22.onObjectGetCallback(str3, str4, AVFSBaseCache.this.objectForKey(str3, str4, cls2));
            }
        });
    }

    public boolean setObjectForKey(@NonNull String str, Object obj) {
        return setObjectForKey(str, (String) null, obj);
    }

    public boolean setObjectForKey(@NonNull String str, String str2, Object obj) {
        return setObjectForKey(str, (String) null, obj, 0);
    }

    public boolean setObjectForKey(@NonNull String str, Object obj, int i) {
        return setObjectForKey(str, (String) null, obj, i);
    }

    public void setObjectForKey(@NonNull String str, Object obj, IAVFSCache.OnObjectSetCallback onObjectSetCallback) {
        setObjectForKey(str, obj, 0, onObjectSetCallback);
    }

    public void setObjectForKey(@NonNull String str, String str2, Object obj, IAVFSCache.OnObjectSetCallback2 onObjectSetCallback2) {
        final IAVFSCache.OnObjectSetCallback2 onObjectSetCallback22 = onObjectSetCallback2;
        final String str3 = str;
        final String str4 = str2;
        final Object obj2 = obj;
        AsyncTask.execute(new Runnable() {
            public void run() {
                onObjectSetCallback22.onObjectSetCallback(str3, str4, AVFSBaseCache.this.setObjectForKey(str3, str4, obj2));
            }
        });
    }

    public void setObjectForKey(@NonNull String str, Object obj, int i, final IAVFSCache.OnObjectSetCallback onObjectSetCallback) {
        setObjectForKey(str, (String) null, obj, (IAVFSCache.OnObjectSetCallback2) new IAVFSCache.OnObjectSetCallback2() {
            public void onObjectSetCallback(@NonNull String str, String str2, boolean z) {
                onObjectSetCallback.onObjectSetCallback(str, z);
            }
        });
    }

    public void setObjectForKey(@NonNull String str, String str2, Object obj, int i, IAVFSCache.OnObjectSetCallback2 onObjectSetCallback2) {
        final IAVFSCache.OnObjectSetCallback2 onObjectSetCallback22 = onObjectSetCallback2;
        final String str3 = str;
        final String str4 = str2;
        final Object obj2 = obj;
        final int i2 = i;
        AsyncTask.execute(new Runnable() {
            public void run() {
                onObjectSetCallback22.onObjectSetCallback(str3, str4, AVFSBaseCache.this.setObjectForKey(str3, str4, obj2, i2));
            }
        });
    }

    public boolean removeObjectForKey(@NonNull String str) {
        return removeObjectForKey(str, (String) null);
    }

    public void removeObjectForKey(@NonNull String str, final IAVFSCache.OnObjectRemoveCallback onObjectRemoveCallback) {
        removeObjectForKey(str, (String) null, new IAVFSCache.OnObjectRemoveCallback2() {
            public void onObjectRemoveCallback(@NonNull String str, String str2, boolean z) {
                onObjectRemoveCallback.onObjectRemoveCallback(str, z);
            }
        });
    }

    public void removeObjectForKey(@NonNull final String str, final String str2, final IAVFSCache.OnObjectRemoveCallback2 onObjectRemoveCallback2) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                onObjectRemoveCallback2.onObjectRemoveCallback(str, str2, AVFSBaseCache.this.removeObjectForKey(str, str2));
            }
        });
    }

    public void removeAllObject(final IAVFSCache.OnAllObjectRemoveCallback onAllObjectRemoveCallback) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                onAllObjectRemoveCallback.onAllObjectRemoveCallback(AVFSBaseCache.this.removeAllObject());
            }
        });
    }

    @Nullable
    public InputStream inputStreamForKey(@NonNull String str) {
        return inputStreamForKey(str, (String) null);
    }

    public void inputStreamForKey(@NonNull String str, final IAVFSCache.OnStreamGetCallback onStreamGetCallback) {
        inputStreamForKey(str, (String) null, new IAVFSCache.OnStreamGetCallback2() {
            public void onStreamGetCallback(@NonNull String str, String str2, InputStream inputStream) {
                onStreamGetCallback.onStreamGetCallback(str, inputStream);
            }
        });
    }

    public void inputStreamForKey(@NonNull final String str, final String str2, final IAVFSCache.OnStreamGetCallback2 onStreamGetCallback2) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                onStreamGetCallback2.onStreamGetCallback(str, str2, AVFSBaseCache.this.inputStreamForKey(str, str2));
            }
        });
    }

    public boolean setStreamForKey(@NonNull String str, @NonNull InputStream inputStream) {
        return setStreamForKey(str, (String) null, inputStream);
    }

    public boolean setStreamForKey(@NonNull String str, String str2, @NonNull InputStream inputStream) {
        return setStreamForKey(str, str2, inputStream, 0);
    }

    public boolean setStreamForKey(@NonNull String str, @NonNull InputStream inputStream, int i) {
        return setStreamForKey(str, (String) null, inputStream, i);
    }

    public void setStreamForKey(@NonNull String str, @NonNull InputStream inputStream, IAVFSCache.OnStreamSetCallback onStreamSetCallback) {
        setStreamForKey(str, inputStream, 0, onStreamSetCallback);
    }

    public void setStreamForKey(@NonNull String str, String str2, @NonNull InputStream inputStream, IAVFSCache.OnStreamSetCallback2 onStreamSetCallback2) {
        setStreamForKey(str, str2, inputStream, 0, onStreamSetCallback2);
    }

    public void setStreamForKey(@NonNull String str, @NonNull InputStream inputStream, int i, final IAVFSCache.OnStreamSetCallback onStreamSetCallback) {
        setStreamForKey(str, (String) null, inputStream, (IAVFSCache.OnStreamSetCallback2) new IAVFSCache.OnStreamSetCallback2() {
            public void onStreamSetCallback(@NonNull String str, String str2, boolean z) {
                onStreamSetCallback.onStreamSetCallback(str, z);
            }
        });
    }

    public void setStreamForKey(@NonNull String str, String str2, @NonNull InputStream inputStream, int i, IAVFSCache.OnStreamSetCallback2 onStreamSetCallback2) {
        final IAVFSCache.OnStreamSetCallback2 onStreamSetCallback22 = onStreamSetCallback2;
        final String str3 = str;
        final String str4 = str2;
        final InputStream inputStream2 = inputStream;
        AsyncTask.execute(new Runnable() {
            public void run() {
                onStreamSetCallback22.onStreamSetCallback(str3, str4, AVFSBaseCache.this.setStreamForKey(str3, str4, inputStream2, 0));
            }
        });
    }

    public long lengthForKey(String str) {
        return lengthForKey(str, (String) null);
    }
}
