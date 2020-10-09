package mtopsdk.common.util;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IInterface;
import android.text.TextUtils;
import mtopsdk.common.util.TBSdkLog;

public abstract class AsyncServiceBinder<T extends IInterface> {
    static final String TAG = "mtopsdk.AsyncServiceBinder";
    private ServiceConnection conn = new ServiceConnection() {
        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0040 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onServiceDisconnected(android.content.ComponentName r4) {
            /*
                r3 = this;
                mtopsdk.common.util.AsyncServiceBinder r4 = mtopsdk.common.util.AsyncServiceBinder.this
                byte[] r4 = r4.lock
                monitor-enter(r4)
                mtopsdk.common.util.TBSdkLog$LogEnable r0 = mtopsdk.common.util.TBSdkLog.LogEnable.WarnEnable     // Catch:{ Exception -> 0x0040 }
                boolean r0 = mtopsdk.common.util.TBSdkLog.isLogEnable(r0)     // Catch:{ Exception -> 0x0040 }
                if (r0 == 0) goto L_0x0040
                mtopsdk.common.util.AsyncServiceBinder r0 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ Exception -> 0x0040 }
                java.lang.String r0 = r0.interfaceName     // Catch:{ Exception -> 0x0040 }
                boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0040 }
                if (r0 == 0) goto L_0x0023
                mtopsdk.common.util.AsyncServiceBinder r0 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ Exception -> 0x0040 }
                mtopsdk.common.util.AsyncServiceBinder r1 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ Exception -> 0x0040 }
                java.lang.Class<? extends android.os.IInterface> r1 = r1.interfaceCls     // Catch:{ Exception -> 0x0040 }
                java.lang.String r1 = r1.getSimpleName()     // Catch:{ Exception -> 0x0040 }
                r0.interfaceName = r1     // Catch:{ Exception -> 0x0040 }
            L_0x0023:
                java.lang.String r0 = "mtopsdk.AsyncServiceBinder"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0040 }
                r1.<init>()     // Catch:{ Exception -> 0x0040 }
                java.lang.String r2 = "[onServiceDisconnected] Service disconnected called,interfaceName="
                r1.append(r2)     // Catch:{ Exception -> 0x0040 }
                mtopsdk.common.util.AsyncServiceBinder r2 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ Exception -> 0x0040 }
                java.lang.String r2 = r2.interfaceName     // Catch:{ Exception -> 0x0040 }
                r1.append(r2)     // Catch:{ Exception -> 0x0040 }
                java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x0040 }
                mtopsdk.common.util.TBSdkLog.w(r0, r1)     // Catch:{ Exception -> 0x0040 }
                goto L_0x0040
            L_0x003e:
                r0 = move-exception
                goto L_0x004c
            L_0x0040:
                mtopsdk.common.util.AsyncServiceBinder r0 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ all -> 0x003e }
                r1 = 0
                r0.service = r1     // Catch:{ all -> 0x003e }
                mtopsdk.common.util.AsyncServiceBinder r0 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ all -> 0x003e }
                r1 = 0
                r0.mBinding = r1     // Catch:{ all -> 0x003e }
                monitor-exit(r4)     // Catch:{ all -> 0x003e }
                return
            L_0x004c:
                monitor-exit(r4)     // Catch:{ all -> 0x003e }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: mtopsdk.common.util.AsyncServiceBinder.AnonymousClass1.onServiceDisconnected(android.content.ComponentName):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
            r9.this$0.mBindFailed = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0082, code lost:
            if (mtopsdk.common.util.TBSdkLog.isLogEnable(mtopsdk.common.util.TBSdkLog.LogEnable.WarnEnable) != false) goto L_0x0084;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0084, code lost:
            mtopsdk.common.util.TBSdkLog.w(mtopsdk.common.util.AsyncServiceBinder.TAG, "[onServiceConnected] Service bind failed. mBindFailed=" + r9.this$0.mBindFailed + ",interfaceName=" + r9.this$0.interfaceName);
         */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0078 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onServiceConnected(android.content.ComponentName r10, android.os.IBinder r11) {
            /*
                r9 = this;
                mtopsdk.common.util.AsyncServiceBinder r10 = mtopsdk.common.util.AsyncServiceBinder.this
                byte[] r10 = r10.lock
                monitor-enter(r10)
                r0 = 1
                r1 = 0
                mtopsdk.common.util.AsyncServiceBinder r2 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ Exception -> 0x0078 }
                java.lang.String r2 = r2.interfaceName     // Catch:{ Exception -> 0x0078 }
                boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x0078 }
                if (r2 == 0) goto L_0x001d
                mtopsdk.common.util.AsyncServiceBinder r2 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ Exception -> 0x0078 }
                mtopsdk.common.util.AsyncServiceBinder r3 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ Exception -> 0x0078 }
                java.lang.Class<? extends android.os.IInterface> r3 = r3.interfaceCls     // Catch:{ Exception -> 0x0078 }
                java.lang.String r3 = r3.getSimpleName()     // Catch:{ Exception -> 0x0078 }
                r2.interfaceName = r3     // Catch:{ Exception -> 0x0078 }
            L_0x001d:
                mtopsdk.common.util.TBSdkLog$LogEnable r2 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable     // Catch:{ Exception -> 0x0078 }
                boolean r2 = mtopsdk.common.util.TBSdkLog.isLogEnable(r2)     // Catch:{ Exception -> 0x0078 }
                if (r2 == 0) goto L_0x003f
                java.lang.String r2 = "mtopsdk.AsyncServiceBinder"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0078 }
                r3.<init>()     // Catch:{ Exception -> 0x0078 }
                java.lang.String r4 = "[onServiceConnected] Service connected called. interfaceName ="
                r3.append(r4)     // Catch:{ Exception -> 0x0078 }
                mtopsdk.common.util.AsyncServiceBinder r4 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ Exception -> 0x0078 }
                java.lang.String r4 = r4.interfaceName     // Catch:{ Exception -> 0x0078 }
                r3.append(r4)     // Catch:{ Exception -> 0x0078 }
                java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0078 }
                mtopsdk.common.util.TBSdkLog.i(r2, r3)     // Catch:{ Exception -> 0x0078 }
            L_0x003f:
                mtopsdk.common.util.AsyncServiceBinder r2 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ Exception -> 0x0078 }
                java.lang.Class<? extends android.os.IInterface> r2 = r2.interfaceCls     // Catch:{ Exception -> 0x0078 }
                java.lang.Class[] r2 = r2.getDeclaredClasses()     // Catch:{ Exception -> 0x0078 }
                int r3 = r2.length     // Catch:{ Exception -> 0x0078 }
                r4 = 0
            L_0x0049:
                if (r4 >= r3) goto L_0x00aa
                r5 = r2[r4]     // Catch:{ Exception -> 0x0078 }
                java.lang.String r6 = r5.getSimpleName()     // Catch:{ Exception -> 0x0078 }
                java.lang.String r7 = "Stub"
                boolean r6 = r6.equals(r7)     // Catch:{ Exception -> 0x0078 }
                if (r6 == 0) goto L_0x0073
                java.lang.String r6 = "asInterface"
                java.lang.Class[] r7 = new java.lang.Class[r0]     // Catch:{ Exception -> 0x0078 }
                java.lang.Class<android.os.IBinder> r8 = android.os.IBinder.class
                r7[r1] = r8     // Catch:{ Exception -> 0x0078 }
                java.lang.reflect.Method r6 = r5.getDeclaredMethod(r6, r7)     // Catch:{ Exception -> 0x0078 }
                mtopsdk.common.util.AsyncServiceBinder r7 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ Exception -> 0x0078 }
                java.lang.Object[] r8 = new java.lang.Object[r0]     // Catch:{ Exception -> 0x0078 }
                r8[r1] = r11     // Catch:{ Exception -> 0x0078 }
                java.lang.Object r5 = r6.invoke(r5, r8)     // Catch:{ Exception -> 0x0078 }
                android.os.IInterface r5 = (android.os.IInterface) r5     // Catch:{ Exception -> 0x0078 }
                r7.service = r5     // Catch:{ Exception -> 0x0078 }
            L_0x0073:
                int r4 = r4 + 1
                goto L_0x0049
            L_0x0076:
                r11 = move-exception
                goto L_0x00bf
            L_0x0078:
                mtopsdk.common.util.AsyncServiceBinder r11 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ all -> 0x0076 }
                r11.mBindFailed = r0     // Catch:{ all -> 0x0076 }
                mtopsdk.common.util.TBSdkLog$LogEnable r11 = mtopsdk.common.util.TBSdkLog.LogEnable.WarnEnable     // Catch:{ all -> 0x0076 }
                boolean r11 = mtopsdk.common.util.TBSdkLog.isLogEnable(r11)     // Catch:{ all -> 0x0076 }
                if (r11 == 0) goto L_0x00aa
                java.lang.String r11 = "mtopsdk.AsyncServiceBinder"
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0076 }
                r0.<init>()     // Catch:{ all -> 0x0076 }
                java.lang.String r2 = "[onServiceConnected] Service bind failed. mBindFailed="
                r0.append(r2)     // Catch:{ all -> 0x0076 }
                mtopsdk.common.util.AsyncServiceBinder r2 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ all -> 0x0076 }
                boolean r2 = r2.mBindFailed     // Catch:{ all -> 0x0076 }
                r0.append(r2)     // Catch:{ all -> 0x0076 }
                java.lang.String r2 = ",interfaceName="
                r0.append(r2)     // Catch:{ all -> 0x0076 }
                mtopsdk.common.util.AsyncServiceBinder r2 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ all -> 0x0076 }
                java.lang.String r2 = r2.interfaceName     // Catch:{ all -> 0x0076 }
                r0.append(r2)     // Catch:{ all -> 0x0076 }
                java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0076 }
                mtopsdk.common.util.TBSdkLog.w(r11, r0)     // Catch:{ all -> 0x0076 }
            L_0x00aa:
                mtopsdk.common.util.AsyncServiceBinder r11 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ all -> 0x0076 }
                T r11 = r11.service     // Catch:{ all -> 0x0076 }
                if (r11 == 0) goto L_0x00b9
                mtopsdk.common.util.AsyncServiceBinder r11 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ all -> 0x0076 }
                r11.mBindFailed = r1     // Catch:{ all -> 0x0076 }
                mtopsdk.common.util.AsyncServiceBinder r11 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ all -> 0x0076 }
                r11.afterAsyncBind()     // Catch:{ all -> 0x0076 }
            L_0x00b9:
                mtopsdk.common.util.AsyncServiceBinder r11 = mtopsdk.common.util.AsyncServiceBinder.this     // Catch:{ all -> 0x0076 }
                r11.mBinding = r1     // Catch:{ all -> 0x0076 }
                monitor-exit(r10)     // Catch:{ all -> 0x0076 }
                return
            L_0x00bf:
                monitor-exit(r10)     // Catch:{ all -> 0x0076 }
                throw r11
            */
            throw new UnsupportedOperationException("Method not decompiled: mtopsdk.common.util.AsyncServiceBinder.AnonymousClass1.onServiceConnected(android.content.ComponentName, android.os.IBinder):void");
        }
    };
    Class<? extends IInterface> interfaceCls;
    String interfaceName;
    final byte[] lock = new byte[0];
    volatile boolean mBindFailed = false;
    volatile boolean mBinding = false;
    protected volatile T service = null;
    Class<? extends Service> serviceCls;

    /* access modifiers changed from: protected */
    public abstract void afterAsyncBind();

    public AsyncServiceBinder(Class<? extends IInterface> cls, Class<? extends Service> cls2) {
        this.interfaceCls = cls;
        this.serviceCls = cls2;
    }

    @TargetApi(4)
    public void asyncBind(Context context) {
        if (this.service == null && context != null && !this.mBindFailed && !this.mBinding) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[asyncBind] mContext=" + context + ",mBindFailed= " + this.mBindFailed + ",mBinding=" + this.mBinding);
            }
            this.mBinding = true;
            try {
                if (TextUtils.isEmpty(this.interfaceName)) {
                    this.interfaceName = this.interfaceCls.getSimpleName();
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[asyncBind]try to bind service for " + this.interfaceName);
                }
                Intent intent = new Intent(context.getApplicationContext(), this.serviceCls);
                intent.setAction(this.interfaceCls.getName());
                intent.setPackage(context.getPackageName());
                intent.addCategory("android.intent.category.DEFAULT");
                boolean bindService = context.bindService(intent, this.conn, 1);
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[asyncBind]use intent bind service ret=" + bindService + " for interfaceName=" + this.interfaceName);
                }
                this.mBindFailed = !bindService;
            } catch (Throwable th) {
                this.mBindFailed = true;
                TBSdkLog.e(TAG, "[asyncBind] use intent bind service failed. mBindFailed=" + this.mBindFailed + ",interfaceName = " + this.interfaceName, th);
            }
            if (this.mBindFailed) {
                this.mBinding = false;
            }
        }
    }

    public T getService() {
        return this.service;
    }
}
