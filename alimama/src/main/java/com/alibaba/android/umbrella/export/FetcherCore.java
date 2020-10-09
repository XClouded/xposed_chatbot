package com.alibaba.android.umbrella.export;

import androidx.annotation.NonNull;
import java.lang.reflect.Method;

class FetcherCore<T> {
    private volatile T _serviceIMP;
    private volatile Class<T> t;

    interface FetchCallback<V> {
        @NonNull
        V call();
    }

    FetcherCore(Class<T> cls) {
        this.t = cls;
    }

    /* access modifiers changed from: package-private */
    public T get(FetchCallback<T> fetchCallback) {
        if (this._serviceIMP == null) {
            synchronized (FetcherCore.class) {
                if (this._serviceIMP == null) {
                    this._serviceIMP = getDefaultServiceIMP(this.t);
                    if (this._serviceIMP == null) {
                        this._serviceIMP = fetchCallback.call();
                    }
                }
            }
        }
        return this._serviceIMP;
    }

    private static <T> T getDefaultServiceIMP(Class<T> cls) {
        String str;
        String name = cls.getName();
        if (name.endsWith("Interface")) {
            str = name.replace("Interface", "Imp");
        } else {
            str = name + "Imp";
        }
        try {
            Class<?> cls2 = Class.forName(str);
            try {
                Method declaredMethod = cls2.getDeclaredMethod("getInstance", new Class[0]);
                if (declaredMethod != null) {
                    return declaredMethod.invoke((Object) null, new Object[0]);
                }
                return null;
            } catch (Exception unused) {
                System.out.println("UmbrellaServiceFetcher, error when use getInstance() -> " + str + "interface name = " + name);
                try {
                    return cls2.newInstance();
                } catch (Exception unused2) {
                    System.out.println("UmbrellaServiceFetcher, error when use constructor -> " + str + "interface name = " + name);
                    return null;
                }
            }
        } catch (Exception unused3) {
            System.out.println("UmbrellaServiceFetcher, error when Class.forName(" + str + "), interface name = " + name);
            return null;
        }
    }
}
