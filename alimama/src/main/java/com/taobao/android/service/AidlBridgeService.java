package com.taobao.android.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import com.taobao.android.modular.IAidlServiceBridge;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AidlBridgeService extends Service {
    private static final String KBridgeServiceName = "com.taobao.android.service.AidlBridgeService";
    private static final BroadcastReceiver sDummyReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
        }
    };
    private IAidlServiceBridge mBridgeBinder = new IAidlServiceBridge.Stub() {
        private Map<ComponentName, SingletonServiceConnection> mServices = new HashMap();

        public synchronized IBinder bindService(Intent intent) {
            boolean z;
            ComponentName component = intent.getComponent();
            if (component == null) {
                ResolveInfo resolveService = AidlBridgeService.this.getPackageManager().resolveService(intent, 0);
                if (resolveService == null) {
                    return null;
                }
                component = new ComponentName(resolveService.serviceInfo.packageName, resolveService.serviceInfo.name);
            }
            SingletonServiceConnection singletonServiceConnection = this.mServices.get(component);
            if (singletonServiceConnection != null) {
                return singletonServiceConnection.mBinder;
            }
            intent.setComponent(component);
            SingletonServiceConnection singletonServiceConnection2 = new SingletonServiceConnection();
            try {
                z = LocalAidlServices.bindService(AidlBridgeService.this, intent, singletonServiceConnection2);
            } catch (ClassNotFoundException unused) {
                z = false;
            }
            if (!z) {
                return null;
            }
            this.mServices.put(component, singletonServiceConnection2);
            return singletonServiceConnection2.mBinder;
        }

        public synchronized void unbindService(IBinder iBinder) {
            Iterator<Map.Entry<ComponentName, SingletonServiceConnection>> it = this.mServices.entrySet().iterator();
            while (it.hasNext()) {
                SingletonServiceConnection singletonServiceConnection = (SingletonServiceConnection) it.next().getValue();
                if (singletonServiceConnection.mBinder == iBinder) {
                    LocalAidlServices.unbindService(AidlBridgeService.this, singletonServiceConnection);
                    it.remove();
                }
            }
        }
    };

    public static boolean init(Context context, ServiceConnection serviceConnection) {
        return context.getApplicationContext().bindService(new Intent(context, AidlBridgeService.class), serviceConnection, 1);
    }

    public IBinder onBind(Intent intent) {
        if (intent.getAction() == null) {
            return (IBinder) this.mBridgeBinder;
        }
        intent.setComponent((ComponentName) null).setPackage(getPackageName());
        List<ResolveInfo> queryIntentServices = getPackageManager().queryIntentServices(intent, 0);
        if (queryIntentServices == null || queryIntentServices.isEmpty()) {
            return null;
        }
        for (ResolveInfo next : queryIntentServices) {
            if (!KBridgeServiceName.equals(next.serviceInfo.name)) {
                intent.setComponent(new ComponentName(next.serviceInfo.packageName, next.serviceInfo.name));
                try {
                    IBinder bindService = this.mBridgeBinder.bindService(intent);
                    intent.setComponent((ComponentName) null);
                    return bindService;
                } catch (RemoteException unused) {
                    return null;
                }
            }
        }
        return null;
    }

    private static class SingletonServiceConnection implements ServiceConnection {
        IBinder mBinder;

        public void onServiceDisconnected(ComponentName componentName) {
        }

        private SingletonServiceConnection() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            this.mBinder = iBinder;
        }
    }

    public static IBinder peekMe(Context context) {
        return sDummyReceiver.peekService(context, new Intent(context, AidlBridgeService.class));
    }
}
