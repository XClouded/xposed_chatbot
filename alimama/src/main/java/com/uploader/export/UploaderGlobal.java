package com.uploader.export;

import android.content.Context;
import androidx.annotation.NonNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.ConcurrentHashMap;

public class UploaderGlobal {
    public static final int DEFAULT_INSTANCE_TYPE = 0;
    private static volatile Context context;
    private static final ConcurrentHashMap<Integer, EnvironmentElement> dailyElementMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, IUploaderDependency> dependencyMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, EnvironmentElement> onlineRelatedMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, EnvironmentElement> preparedElementMap = new ConcurrentHashMap<>();

    @Retention(RetentionPolicy.SOURCE)
    public @interface Definition {
    }

    static {
        onlineRelatedMap.put(0, new EnvironmentElement(0, "21646297", "arup.m.taobao.com", "106.11.53.94"));
        preparedElementMap.put(0, new EnvironmentElement(1, "21646297", "pre-arup.m.taobao.com", "140.205.173.180"));
        dailyElementMap.put(0, new EnvironmentElement(2, "4272", "daily.arup.m.alibaba.net", "100.69.167.214"));
    }

    public static IUploaderDependency getDependency(Integer num) {
        return dependencyMap.get(num);
    }

    public static IUploaderDependency putDependency(@NonNull IUploaderDependency iUploaderDependency) {
        return dependencyMap.put(Integer.valueOf(iUploaderDependency.getEnvironment().getInstanceType()), iUploaderDependency);
    }

    public static EnvironmentElement putElement(int i, String str) {
        return putElement(i, 0, str);
    }

    public static EnvironmentElement putElement(int i, int i2, String str) {
        ConcurrentHashMap<Integer, EnvironmentElement> concurrentHashMap;
        EnvironmentElement put;
        switch (i) {
            case 1:
                concurrentHashMap = preparedElementMap;
                break;
            case 2:
                concurrentHashMap = dailyElementMap;
                break;
            default:
                concurrentHashMap = onlineRelatedMap;
                break;
        }
        synchronized (concurrentHashMap) {
            EnvironmentElement environmentElement = concurrentHashMap.get(Integer.valueOf(i2));
            put = concurrentHashMap.put(Integer.valueOf(i2), new EnvironmentElement(i, str, environmentElement.host, environmentElement.ipAddress, environmentElement.authCode));
        }
        return put;
    }

    public static EnvironmentElement putElement(int i, int i2, String str, String str2) {
        ConcurrentHashMap<Integer, EnvironmentElement> concurrentHashMap;
        EnvironmentElement put;
        switch (i) {
            case 1:
                concurrentHashMap = preparedElementMap;
                break;
            case 2:
                concurrentHashMap = dailyElementMap;
                break;
            default:
                concurrentHashMap = onlineRelatedMap;
                break;
        }
        synchronized (concurrentHashMap) {
            EnvironmentElement environmentElement = concurrentHashMap.get(Integer.valueOf(i2));
            put = concurrentHashMap.put(Integer.valueOf(i2), new EnvironmentElement(i, str, environmentElement.host, environmentElement.ipAddress, str2));
        }
        return put;
    }

    public static EnvironmentElement getElement(int i, int i2) {
        switch (i) {
            case 1:
                return preparedElementMap.get(Integer.valueOf(i2));
            case 2:
                return dailyElementMap.get(Integer.valueOf(i2));
            default:
                return onlineRelatedMap.get(Integer.valueOf(i2));
        }
    }

    public static EnvironmentElement getElement(int i) {
        return getElement(i, 0);
    }

    public static EnvironmentElement putElement(EnvironmentElement environmentElement, int i) {
        switch (environmentElement.environment) {
            case 1:
                return preparedElementMap.put(Integer.valueOf(i), environmentElement);
            case 2:
                return dailyElementMap.put(Integer.valueOf(i), environmentElement);
            default:
                return onlineRelatedMap.put(Integer.valueOf(i), environmentElement);
        }
    }

    public static EnvironmentElement putElement(EnvironmentElement environmentElement) {
        return putElement(environmentElement, 0);
    }

    public static void setContext(Context context2) {
        if (context2 != null) {
            context = context2.getApplicationContext();
        }
    }

    public static Context retrieveContext() {
        if (context != null) {
            return context;
        }
        synchronized (UploaderGlobal.class) {
            if (context != null) {
                Context context2 = context;
                return context2;
            }
            try {
                Class<?> cls = Class.forName("android.app.ActivityThread");
                Object invoke = cls.getMethod("currentActivityThread", new Class[0]).invoke(cls, new Object[0]);
                context = (Context) invoke.getClass().getMethod("getApplication", new Class[0]).invoke(invoke, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Context context3 = context;
            return context3;
        }
    }
}
