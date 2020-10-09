package com.taobao.accs.internal;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import com.taobao.accs.base.IBaseService;
import com.taobao.accs.common.Constants;
import com.taobao.accs.net.BaseConnection;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.accs.utl.Utils;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ElectionServiceImpl implements IBaseService {
    private static final String TAG = "ElectionServiceImpl";
    protected static ConcurrentHashMap<String, BaseConnection> mConnections = new ConcurrentHashMap<>(2);
    private Service mBaseService = null;
    private Context mContext;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public abstract int onHostStartCommand(Intent intent, int i, int i2);

    public boolean onUnbind(Intent intent) {
        return false;
    }

    public abstract void onVotedHost();

    public ElectionServiceImpl(Service service) {
        this.mBaseService = service;
        this.mContext = service.getApplicationContext();
    }

    public void onCreate() {
        ALog.i(TAG, "onCreate,", "sdkVersion", Integer.valueOf(Constants.SDK_VERSION_CODE));
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent == null) {
            return 2;
        }
        String action = intent.getAction();
        ALog.i(TAG, "onStartCommand begin", "action", action);
        if (TextUtils.equals(action, Constants.ACTION_START_SERVICE)) {
            handleStartCommand(intent);
        }
        return onHostStartCommand(intent, i, i2);
    }

    public void onDestroy() {
        ALog.e(TAG, "Service onDestroy", new Object[0]);
        this.mContext = null;
        this.mBaseService = null;
    }

    private void handleStartCommand(Intent intent) {
        try {
            String stringExtra = intent.getStringExtra("packageName");
            String stringExtra2 = intent.getStringExtra("appKey");
            String stringExtra3 = intent.getStringExtra("ttid");
            String stringExtra4 = intent.getStringExtra("app_sercet");
            String stringExtra5 = intent.getStringExtra(Constants.KEY_CONFIG_TAG);
            int intExtra = intent.getIntExtra(Constants.KEY_MODE, 0);
            ALog.i(TAG, "handleStartCommand", Constants.KEY_CONFIG_TAG, stringExtra5, "appkey", stringExtra2, "appSecret", stringExtra4, "ttid", stringExtra3, "pkg", stringExtra);
            if (!TextUtils.isEmpty(stringExtra) && !TextUtils.isEmpty(stringExtra2) && stringExtra.equals(this.mContext.getPackageName())) {
                Utils.setMode(this.mContext, intExtra);
                BaseConnection connection = getConnection(this.mContext, stringExtra5, false, -1);
                if (connection != null) {
                    connection.mTtid = stringExtra3;
                } else {
                    ALog.e(TAG, "handleStartCommand start action, no connection", Constants.KEY_CONFIG_TAG, stringExtra5);
                }
                UtilityImpl.saveAppKey(this.mContext, stringExtra2);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "handleStartCommand", th, new Object[0]);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00ba, code lost:
        if (r10 == false) goto L_0x00cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
        r11.start();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static com.taobao.accs.net.BaseConnection getConnection(android.content.Context r8, java.lang.String r9, boolean r10, int r11) {
        /*
            r11 = 0
            r0 = 0
            boolean r1 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x00c3 }
            r2 = 2
            r3 = 1
            if (r1 == 0) goto L_0x003a
            java.lang.String r8 = "ElectionServiceImpl"
            java.lang.String r9 = "getConnection configTag null or env invalid"
            java.lang.Object[] r10 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x00c3 }
            java.lang.String r1 = "conns.size"
            r10[r0] = r1     // Catch:{ Throwable -> 0x00c3 }
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.accs.net.BaseConnection> r1 = mConnections     // Catch:{ Throwable -> 0x00c3 }
            int r1 = r1.size()     // Catch:{ Throwable -> 0x00c3 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ Throwable -> 0x00c3 }
            r10[r3] = r1     // Catch:{ Throwable -> 0x00c3 }
            com.taobao.accs.utl.ALog.w(r8, r9, r10)     // Catch:{ Throwable -> 0x00c3 }
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.accs.net.BaseConnection> r8 = mConnections     // Catch:{ Throwable -> 0x00c3 }
            int r8 = r8.size()     // Catch:{ Throwable -> 0x00c3 }
            if (r8 <= 0) goto L_0x0038
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.accs.net.BaseConnection> r8 = mConnections     // Catch:{ Throwable -> 0x00c3 }
            java.util.Enumeration r8 = r8.elements()     // Catch:{ Throwable -> 0x00c3 }
            java.lang.Object r8 = r8.nextElement()     // Catch:{ Throwable -> 0x00c3 }
            com.taobao.accs.net.BaseConnection r8 = (com.taobao.accs.net.BaseConnection) r8     // Catch:{ Throwable -> 0x00c3 }
            goto L_0x0039
        L_0x0038:
            r8 = r11
        L_0x0039:
            return r8
        L_0x003a:
            java.lang.String r1 = "ElectionServiceImpl"
            java.lang.String r4 = "getConnection"
            r5 = 4
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x00c3 }
            java.lang.String r6 = "configTag"
            r5[r0] = r6     // Catch:{ Throwable -> 0x00c3 }
            r5[r3] = r9     // Catch:{ Throwable -> 0x00c3 }
            java.lang.String r6 = "start"
            r5[r2] = r6     // Catch:{ Throwable -> 0x00c3 }
            r6 = 3
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r10)     // Catch:{ Throwable -> 0x00c3 }
            r5[r6] = r7     // Catch:{ Throwable -> 0x00c3 }
            com.taobao.accs.utl.ALog.i(r1, r4, r5)     // Catch:{ Throwable -> 0x00c3 }
            boolean r1 = com.taobao.accs.utl.OrangeAdapter.isChannelModeEnable()     // Catch:{ Throwable -> 0x00c3 }
            if (r1 != 0) goto L_0x0077
            com.taobao.accs.AccsClientConfig r1 = com.taobao.accs.AccsClientConfig.getConfigByTag(r9)     // Catch:{ Throwable -> 0x00c3 }
            if (r1 == 0) goto L_0x0077
            boolean r1 = r1.getDisableChannel()     // Catch:{ Throwable -> 0x00c3 }
            if (r1 == 0) goto L_0x0077
            java.lang.String r8 = "ElectionServiceImpl"
            java.lang.String r10 = "getConnection channel disabled!"
            java.lang.Object[] r1 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x00c3 }
            java.lang.String r2 = "configTag"
            r1[r0] = r2     // Catch:{ Throwable -> 0x00c3 }
            r1[r3] = r9     // Catch:{ Throwable -> 0x00c3 }
            com.taobao.accs.utl.ALog.e(r8, r10, r1)     // Catch:{ Throwable -> 0x00c3 }
            return r11
        L_0x0077:
            int r1 = com.taobao.accs.utl.Utils.getMode(r8)     // Catch:{ Throwable -> 0x00c3 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00c3 }
            r2.<init>()     // Catch:{ Throwable -> 0x00c3 }
            r2.append(r9)     // Catch:{ Throwable -> 0x00c3 }
            java.lang.String r4 = "|"
            r2.append(r4)     // Catch:{ Throwable -> 0x00c3 }
            r2.append(r1)     // Catch:{ Throwable -> 0x00c3 }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x00c3 }
            java.lang.Class<com.taobao.accs.internal.ElectionServiceImpl> r4 = com.taobao.accs.internal.ElectionServiceImpl.class
            monitor-enter(r4)     // Catch:{ Throwable -> 0x00c3 }
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.accs.net.BaseConnection> r5 = mConnections     // Catch:{ all -> 0x00c0 }
            java.lang.Object r5 = r5.get(r2)     // Catch:{ all -> 0x00c0 }
            com.taobao.accs.net.BaseConnection r5 = (com.taobao.accs.net.BaseConnection) r5     // Catch:{ all -> 0x00c0 }
            if (r5 != 0) goto L_0x00b8
            com.taobao.accs.AccsClientConfig.mEnv = r1     // Catch:{ all -> 0x00b5 }
            boolean r11 = com.taobao.accs.utl.OrangeAdapter.isChannelModeEnable()     // Catch:{ all -> 0x00b5 }
            if (r11 == 0) goto L_0x00aa
            com.taobao.accs.net.InAppConnection r11 = new com.taobao.accs.net.InAppConnection     // Catch:{ all -> 0x00b5 }
            r11.<init>(r8, r3, r9)     // Catch:{ all -> 0x00b5 }
            goto L_0x00af
        L_0x00aa:
            com.taobao.accs.net.SpdyConnection r11 = new com.taobao.accs.net.SpdyConnection     // Catch:{ all -> 0x00b5 }
            r11.<init>(r8, r0, r9)     // Catch:{ all -> 0x00b5 }
        L_0x00af:
            java.util.concurrent.ConcurrentHashMap<java.lang.String, com.taobao.accs.net.BaseConnection> r8 = mConnections     // Catch:{ all -> 0x00c0 }
            r8.put(r2, r11)     // Catch:{ all -> 0x00c0 }
            goto L_0x00b9
        L_0x00b5:
            r8 = move-exception
            r11 = r5
            goto L_0x00c1
        L_0x00b8:
            r11 = r5
        L_0x00b9:
            monitor-exit(r4)     // Catch:{ all -> 0x00c0 }
            if (r10 == 0) goto L_0x00cd
            r11.start()     // Catch:{ Throwable -> 0x00c3 }
            goto L_0x00cd
        L_0x00c0:
            r8 = move-exception
        L_0x00c1:
            monitor-exit(r4)     // Catch:{ all -> 0x00c0 }
            throw r8     // Catch:{ Throwable -> 0x00c3 }
        L_0x00c3:
            r8 = move-exception
            java.lang.String r9 = "ElectionServiceImpl"
            java.lang.String r10 = "getConnection"
            java.lang.Object[] r0 = new java.lang.Object[r0]
            com.taobao.accs.utl.ALog.e(r9, r10, r8, r0)
        L_0x00cd:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.internal.ElectionServiceImpl.getConnection(android.content.Context, java.lang.String, boolean, int):com.taobao.accs.net.BaseConnection");
    }
}
