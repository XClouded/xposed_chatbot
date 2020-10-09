package com.alibaba.analytics.core.logbuilder;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class GoogleAdvertisingIdClient {
    private static final String TAG = "GoogleAdvertisingIdClient";
    private static AdInfo mAdInfo;

    static AdInfo getAdInfo() {
        return mAdInfo;
    }

    public static final class AdInfo {
        private final String advertisingId;
        private final boolean limitAdTrackingEnabled;

        AdInfo(String str, boolean z) {
            this.advertisingId = str;
            this.limitAdTrackingEnabled = z;
        }

        public String getId() {
            return this.advertisingId;
        }

        public boolean isLimitAdTrackingEnabled() {
            return this.limitAdTrackingEnabled;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x005e, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0050 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void initAdvertisingIdInfo(android.content.Context r6) {
        /*
            java.lang.Class<com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient> r0 = com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient.class
            monitor-enter(r0)
            com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient$AdInfo r1 = mAdInfo     // Catch:{ all -> 0x005f }
            if (r1 == 0) goto L_0x0009
            monitor-exit(r0)
            return
        L_0x0009:
            r1 = 1
            com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient$AdvertisingConnection r2 = new com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient$AdvertisingConnection     // Catch:{ Throwable -> 0x0050 }
            r3 = 0
            r2.<init>()     // Catch:{ Throwable -> 0x0050 }
            android.content.Intent r3 = new android.content.Intent     // Catch:{ Throwable -> 0x0050 }
            java.lang.String r4 = "com.google.android.gms.ads.identifier.service.START"
            r3.<init>(r4)     // Catch:{ Throwable -> 0x0050 }
            java.lang.String r4 = "com.google.android.gms"
            r3.setPackage(r4)     // Catch:{ Throwable -> 0x0050 }
            boolean r3 = r6.bindService(r3, r2, r1)     // Catch:{ Throwable -> 0x0050 }
            if (r3 == 0) goto L_0x0050
            android.os.IBinder r3 = r2.getBinder()     // Catch:{ Exception -> 0x0042 }
            if (r3 == 0) goto L_0x003c
            com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient$AdvertisingInterface r4 = new com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient$AdvertisingInterface     // Catch:{ Exception -> 0x0042 }
            r4.<init>(r3)     // Catch:{ Exception -> 0x0042 }
            com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient$AdInfo r3 = new com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient$AdInfo     // Catch:{ Exception -> 0x0042 }
            java.lang.String r5 = r4.getId()     // Catch:{ Exception -> 0x0042 }
            boolean r4 = r4.isLimitAdTrackingEnabled(r1)     // Catch:{ Exception -> 0x0042 }
            r3.<init>(r5, r4)     // Catch:{ Exception -> 0x0042 }
            mAdInfo = r3     // Catch:{ Exception -> 0x0042 }
        L_0x003c:
            r6.unbindService(r2)     // Catch:{ Throwable -> 0x0050 }
            goto L_0x0050
        L_0x0040:
            r3 = move-exception
            goto L_0x004c
        L_0x0042:
            r3 = move-exception
            java.lang.String r4 = "GoogleAdvertisingIdClient"
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x0040 }
            com.alibaba.analytics.utils.Logger.w(r4, r3, r5)     // Catch:{ all -> 0x0040 }
            goto L_0x003c
        L_0x004c:
            r6.unbindService(r2)     // Catch:{ Throwable -> 0x0050 }
            throw r3     // Catch:{ Throwable -> 0x0050 }
        L_0x0050:
            com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient$AdInfo r6 = mAdInfo     // Catch:{ all -> 0x005f }
            if (r6 != 0) goto L_0x005d
            com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient$AdInfo r6 = new com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient$AdInfo     // Catch:{ all -> 0x005f }
            java.lang.String r2 = ""
            r6.<init>(r2, r1)     // Catch:{ all -> 0x005f }
            mAdInfo = r6     // Catch:{ all -> 0x005f }
        L_0x005d:
            monitor-exit(r0)
            return
        L_0x005f:
            r6 = move-exception
            monitor-exit(r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient.initAdvertisingIdInfo(android.content.Context):void");
    }

    private static final class AdvertisingConnection implements ServiceConnection {
        private final LinkedBlockingQueue<IBinder> queue;
        boolean retrieved;

        public void onServiceDisconnected(ComponentName componentName) {
        }

        private AdvertisingConnection() {
            this.retrieved = false;
            this.queue = new LinkedBlockingQueue<>();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.queue.put(iBinder);
            } catch (InterruptedException unused) {
            }
        }

        public IBinder getBinder() throws InterruptedException {
            if (!this.retrieved) {
                this.retrieved = true;
                return this.queue.poll(5, TimeUnit.SECONDS);
            }
            throw new IllegalStateException();
        }
    }

    private static final class AdvertisingInterface implements IInterface {
        private IBinder binder;

        public AdvertisingInterface(IBinder iBinder) {
            this.binder = iBinder;
        }

        public IBinder asBinder() {
            return this.binder;
        }

        public String getId() throws RemoteException {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                this.binder.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                return obtain2.readString();
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }

        public boolean isLimitAdTrackingEnabled(boolean z) throws RemoteException {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                obtain.writeInt(z ? 1 : 0);
                boolean z2 = false;
                this.binder.transact(2, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z2 = true;
                }
                return z2;
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
        }
    }
}
