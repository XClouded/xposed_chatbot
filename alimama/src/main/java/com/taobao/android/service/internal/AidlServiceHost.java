package com.taobao.android.service.internal;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.util.Log;
import com.taobao.android.service.AidlService;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AidlServiceHost extends Service {
    private static final String TAG = "SvcHost";
    private static final Field mAppField;
    private final Map<String, AidlService<?, ?>> mBoundServices = new HashMap();
    final Map<ServiceConnection, ArrayList<AidlService<?, ?>>> mServiceConnections = new HashMap();

    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        ComponentName resolveService = resolveService(intent);
        if (resolveService == null) {
            Log.e(TAG, "No declared service found for " + intent);
            return false;
        }
        String className = resolveService.getClassName();
        AidlService aidlService = this.mBoundServices.get(className);
        AidlService<?, ?> createAidlService = createAidlService(className);
        attachService(createAidlService);
        try {
            createAidlService.onCreate();
            try {
                serviceConnection.onServiceConnected(resolveService, createAidlService.onBind(intent));
                return true;
            } catch (RuntimeException e) {
                Log.w(TAG, "Failure sending service " + className + " to connection " + serviceConnection, e);
                return false;
            }
        } catch (RuntimeException e2) {
            Log.e(TAG, "Failure creating or binding service " + className, e2);
            return false;
        }
    }

    private ComponentName resolveService(Intent intent) {
        ResolveInfo resolveService = getPackageManager().resolveService(intent, 0);
        if (resolveService == null) {
            return null;
        }
        ServiceInfo serviceInfo = resolveService.serviceInfo;
        return new ComponentName(serviceInfo.packageName, serviceInfo.name);
    }

    private AidlService<?, ?> createAidlService(String str) {
        try {
            Class<?> cls = Class.forName(str);
            try {
                return (AidlService) cls.newInstance();
            } catch (Exception e) {
                try {
                    Log.e(TAG, "Failed to instantiate service " + cls, e);
                    return null;
                } catch (ClassCastException unused) {
                    Log.e(TAG, "Resolved service class is not derived from Service: " + str);
                    return null;
                }
            }
        } catch (ClassNotFoundException unused2) {
            Log.e(TAG, "Resolved service class cannot be found: " + str);
            return null;
        }
    }

    private void attachService(Service service) {
        if (mAppField != null) {
            try {
                mAppField.set(service, getApplication());
            } catch (IllegalAccessException | IllegalArgumentException unused) {
            }
        }
    }

    static {
        Field field;
        try {
            field = Service.class.getDeclaredField("mApplication");
            field.setAccessible(true);
        } catch (NoSuchFieldException unused) {
            field = null;
        }
        mAppField = field;
    }
}
