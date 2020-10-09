package com.ali.user.mobile.app.dataprovider;

import android.app.Application;
import android.content.Context;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DataProviderFactory {
    private static Context applicationContext;
    private static IDataProvider dataProvider;

    public static IDataProvider getDataProvider() {
        if (dataProvider == null) {
            return new DataProvider();
        }
        return dataProvider;
    }

    public static void setDataProvider(IDataProvider iDataProvider) {
        dataProvider = iDataProvider;
    }

    public static synchronized Context getApplicationContext() {
        synchronized (DataProviderFactory.class) {
            if (applicationContext != null) {
                Context context = applicationContext;
                return context;
            }
            if (dataProvider != null) {
                applicationContext = dataProvider.getContext();
            } else {
                applicationContext = getSystemApp();
            }
            Context context2 = applicationContext;
            return context2;
        }
    }

    private static Application getSystemApp() {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
            Field declaredField = cls.getDeclaredField("mInitialApplication");
            declaredField.setAccessible(true);
            return (Application) declaredField.get(declaredMethod.invoke((Object) null, new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
