package com.huawei.hms.support.api;

import android.os.Handler;
import android.os.Looper;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.Result;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.client.Status;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/* compiled from: ErrorResultImpl */
public abstract class a<R extends Result> extends PendingResult<R> {
    private R a = null;
    /* access modifiers changed from: private */
    public int b;

    public a(int i) {
        this.b = i;
    }

    public final R await() {
        return await(0, (TimeUnit) null);
    }

    public R await(long j, TimeUnit timeUnit) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return a(this.b);
        }
        throw new IllegalStateException("await must not be called on the UI thread");
    }

    public final void setResultCallback(ResultCallback<R> resultCallback) {
        setResultCallback(Looper.getMainLooper(), resultCallback);
    }

    public final void setResultCallback(Looper looper, ResultCallback<R> resultCallback) {
        if (looper == null) {
            looper = Looper.myLooper();
        }
        new Handler(looper).post(new b(this, resultCallback));
    }

    /* access modifiers changed from: private */
    public R a(int i) {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass == null) {
            return null;
        }
        try {
            this.a = (Result) com.huawei.hms.support.a.a.a(((ParameterizedType) genericSuperclass).getActualTypeArguments()[0]).newInstance();
            this.a.setStatus(new Status(i));
        } catch (InstantiationException unused) {
            com.huawei.hms.support.log.a.d("ErrorResultImpl", "InstantiationException");
        } catch (IllegalAccessException unused2) {
            com.huawei.hms.support.log.a.d("ErrorResultImpl", "IllegalAccessException");
        }
        return this.a;
    }
}
