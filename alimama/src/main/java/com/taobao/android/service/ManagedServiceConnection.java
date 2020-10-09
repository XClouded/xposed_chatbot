package com.taobao.android.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import com.taobao.weex.el.parse.Operators;
import java.util.concurrent.TimeoutException;

/* compiled from: Services */
class ManagedServiceConnection implements ServiceConnection {
    private static final String TAG = "MgdSvcConn";
    private volatile IBinder mBinder;
    private ComponentName mComponentName;
    private String mInterfaceDescriptor;
    private final Object mLock = new Object();

    ManagedServiceConnection() {
    }

    /* access modifiers changed from: package-private */
    public IBinder waitUntilConnected(long j) throws InterruptedException, TimeoutException {
        IBinder iBinder;
        try {
            IBinder iBinder2 = this.mBinder;
            if (iBinder2 != null) {
                this.mBinder = null;
                return iBinder2;
            }
            synchronized (this.mLock) {
                long uptimeMillis = SystemClock.uptimeMillis();
                while (this.mBinder == null) {
                    this.mLock.wait(j);
                    if (SystemClock.uptimeMillis() - uptimeMillis > j) {
                        throw new TimeoutException();
                    }
                }
                iBinder = this.mBinder;
            }
            this.mBinder = null;
            return iBinder;
        } catch (Throwable th) {
            this.mBinder = null;
            throw th;
        }
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d(TAG, "onServiceConnected: " + componentName);
        synchronized (this.mLock) {
            this.mComponentName = componentName;
            this.mBinder = iBinder;
            this.mLock.notifyAll();
        }
        try {
            this.mInterfaceDescriptor = iBinder.getInterfaceDescriptor();
        } catch (RemoteException unused) {
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        Log.d(TAG, "onServiceDisconnected: " + componentName);
        synchronized (this.mLock) {
            this.mBinder = null;
        }
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("ManagedServiceConnection[");
        sb.append(this.mInterfaceDescriptor);
        sb.append(":");
        if (this.mComponentName == null) {
            str = "not connected";
        } else {
            str = this.mComponentName.flattenToShortString();
        }
        sb.append(str);
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }
}
