package com.alibaba.ut.abtest.multiprocess;

import android.os.Bundle;
import com.alibaba.ut.abtest.VariationSet;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.debug.Debug;
import com.alibaba.ut.abtest.internal.util.ClassUtils;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.ProcessUtils;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiProcessServiceImpl implements MultiProcessService {
    private static final String TAG = "MultiProcessServiceImpl";
    private AtomicBoolean initialized = new AtomicBoolean(false);
    private UTABMultiProcessClient mainMultiProcessClient;
    private UTABMultiProcessClient multiProcessClient;

    /* JADX WARNING: Can't wrap try/catch for region: R(7:22|23|(2:25|26)|27|28|29|30) */
    /* JADX WARNING: Can't wrap try/catch for region: R(8:12|13|14|(2:16|17)|18|19|20|21) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x005d */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0074 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00a6 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:50:0x00bd */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:27:0x0074=Splitter:B:27:0x0074, B:41:0x00a6=Splitter:B:41:0x00a6, B:18:0x005d=Splitter:B:18:0x005d, B:50:0x00bd=Splitter:B:50:0x00bd} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean initialize() {
        /*
            r6 = this;
            monitor-enter(r6)
            java.lang.String r0 = "MultiProcessServiceImpl"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00c3 }
            r1.<init>()     // Catch:{ all -> 0x00c3 }
            java.lang.String r2 = "initialize. initialized="
            r1.append(r2)     // Catch:{ all -> 0x00c3 }
            java.util.concurrent.atomic.AtomicBoolean r2 = r6.initialized     // Catch:{ all -> 0x00c3 }
            boolean r2 = r2.get()     // Catch:{ all -> 0x00c3 }
            r1.append(r2)     // Catch:{ all -> 0x00c3 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00c3 }
            com.alibaba.ut.abtest.internal.util.LogUtils.logD(r0, r1)     // Catch:{ all -> 0x00c3 }
            java.util.concurrent.atomic.AtomicBoolean r0 = r6.initialized     // Catch:{ all -> 0x00c3 }
            boolean r0 = r0.get()     // Catch:{ all -> 0x00c3 }
            r1 = 1
            if (r0 == 0) goto L_0x0028
            monitor-exit(r6)
            return r1
        L_0x0028:
            r0 = 0
            r6.createMultiProcessClientIfNotExist()     // Catch:{ Throwable -> 0x007d }
            java.lang.String r2 = "MultiProcessServiceImpl"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x007d }
            r3.<init>()     // Catch:{ Throwable -> 0x007d }
            java.lang.String r4 = "multiProcessClient="
            r3.append(r4)     // Catch:{ Throwable -> 0x007d }
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClient r4 = r6.multiProcessClient     // Catch:{ Throwable -> 0x007d }
            r3.append(r4)     // Catch:{ Throwable -> 0x007d }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x007d }
            com.alibaba.ut.abtest.internal.util.LogUtils.logD(r2, r3)     // Catch:{ Throwable -> 0x007d }
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClient r2 = r6.multiProcessClient     // Catch:{ Throwable -> 0x007d }
            if (r2 == 0) goto L_0x0064
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClient r2 = r6.multiProcessClient     // Catch:{ Throwable -> 0x007d }
            r2.initialize()     // Catch:{ Throwable -> 0x007d }
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClient r0 = r6.multiProcessClient     // Catch:{ all -> 0x00c3 }
            if (r0 != 0) goto L_0x005d
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClientDefault r0 = new com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClientDefault     // Catch:{ Throwable -> 0x005d }
            r0.<init>()     // Catch:{ Throwable -> 0x005d }
            r6.multiProcessClient = r0     // Catch:{ Throwable -> 0x005d }
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClient r0 = r6.multiProcessClient     // Catch:{ Throwable -> 0x005d }
            r0.initialize()     // Catch:{ Throwable -> 0x005d }
        L_0x005d:
            java.util.concurrent.atomic.AtomicBoolean r0 = r6.initialized     // Catch:{ all -> 0x00c3 }
            r0.set(r1)     // Catch:{ all -> 0x00c3 }
            monitor-exit(r6)
            return r1
        L_0x0064:
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClient r2 = r6.multiProcessClient     // Catch:{ all -> 0x00c3 }
            if (r2 != 0) goto L_0x0074
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClientDefault r2 = new com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClientDefault     // Catch:{ Throwable -> 0x0074 }
            r2.<init>()     // Catch:{ Throwable -> 0x0074 }
            r6.multiProcessClient = r2     // Catch:{ Throwable -> 0x0074 }
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClient r2 = r6.multiProcessClient     // Catch:{ Throwable -> 0x0074 }
            r2.initialize()     // Catch:{ Throwable -> 0x0074 }
        L_0x0074:
            java.util.concurrent.atomic.AtomicBoolean r2 = r6.initialized     // Catch:{ all -> 0x00c3 }
            r2.set(r1)     // Catch:{ all -> 0x00c3 }
            monitor-exit(r6)
            return r0
        L_0x007b:
            r0 = move-exception
            goto L_0x00ad
        L_0x007d:
            r2 = move-exception
            java.lang.String r3 = "MultiProcessServiceImpl"
            java.lang.String r4 = r2.getMessage()     // Catch:{ all -> 0x007b }
            com.alibaba.ut.abtest.internal.util.LogUtils.logE(r3, r4, r2)     // Catch:{ all -> 0x007b }
            java.lang.String r3 = "ServiceAlarm"
            java.lang.String r4 = "MultiProcessServiceImpl.initialize"
            java.lang.String r5 = r2.getMessage()     // Catch:{ all -> 0x007b }
            java.lang.String r2 = android.util.Log.getStackTraceString(r2)     // Catch:{ all -> 0x007b }
            com.alibaba.ut.abtest.internal.util.Analytics.commitFail((java.lang.String) r3, (java.lang.String) r4, (java.lang.String) r5, (java.lang.String) r2)     // Catch:{ all -> 0x007b }
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClient r2 = r6.multiProcessClient     // Catch:{ all -> 0x00c3 }
            if (r2 != 0) goto L_0x00a6
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClientDefault r2 = new com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClientDefault     // Catch:{ Throwable -> 0x00a6 }
            r2.<init>()     // Catch:{ Throwable -> 0x00a6 }
            r6.multiProcessClient = r2     // Catch:{ Throwable -> 0x00a6 }
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClient r2 = r6.multiProcessClient     // Catch:{ Throwable -> 0x00a6 }
            r2.initialize()     // Catch:{ Throwable -> 0x00a6 }
        L_0x00a6:
            java.util.concurrent.atomic.AtomicBoolean r2 = r6.initialized     // Catch:{ all -> 0x00c3 }
            r2.set(r1)     // Catch:{ all -> 0x00c3 }
            monitor-exit(r6)
            return r0
        L_0x00ad:
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClient r2 = r6.multiProcessClient     // Catch:{ all -> 0x00c3 }
            if (r2 != 0) goto L_0x00bd
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClientDefault r2 = new com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClientDefault     // Catch:{ Throwable -> 0x00bd }
            r2.<init>()     // Catch:{ Throwable -> 0x00bd }
            r6.multiProcessClient = r2     // Catch:{ Throwable -> 0x00bd }
            com.alibaba.ut.abtest.multiprocess.UTABMultiProcessClient r2 = r6.multiProcessClient     // Catch:{ Throwable -> 0x00bd }
            r2.initialize()     // Catch:{ Throwable -> 0x00bd }
        L_0x00bd:
            java.util.concurrent.atomic.AtomicBoolean r2 = r6.initialized     // Catch:{ all -> 0x00c3 }
            r2.set(r1)     // Catch:{ all -> 0x00c3 }
            throw r0     // Catch:{ all -> 0x00c3 }
        L_0x00c3:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ut.abtest.multiprocess.MultiProcessServiceImpl.initialize():boolean");
    }

    private UTABMultiProcessClient createMultiProcessClientIfNotExist() {
        LogUtils.logD(TAG, "createMultiProcessClientIfNotExist. multiProcessClient=" + this.multiProcessClient + ",isMultiProcessEnable=" + ABContext.getInstance().isMultiProcessEnable());
        if (this.multiProcessClient != null) {
            return this.multiProcessClient;
        }
        if (ABContext.getInstance().isMultiProcessEnable()) {
            UTABMultiProcessClient uTABMultiProcessClient = null;
            Class<?> findClassIfExists = ClassUtils.findClassIfExists(ABConstants.BasicConstants.MULTIPROCESS_CLIENT_CLASSNAME, MultiProcessServiceImpl.class.getClassLoader());
            if (findClassIfExists != null) {
                try {
                    uTABMultiProcessClient = (UTABMultiProcessClient) findClassIfExists.newInstance();
                } catch (Throwable th) {
                    LogUtils.logW(TAG, th.getMessage(), th);
                }
            }
            if (ProcessUtils.isMainProcess(ABContext.getInstance().getContext())) {
                this.multiProcessClient = new UTABMultiProcessClientDefault();
                if (uTABMultiProcessClient != null) {
                    LogUtils.logD(TAG, "主进程注册多进程通信成功");
                    this.mainMultiProcessClient = uTABMultiProcessClient;
                    this.mainMultiProcessClient.initialize();
                } else {
                    LogUtils.logW(TAG, "主进程注册多进程通信失败");
                }
            } else {
                this.multiProcessClient = uTABMultiProcessClient;
                StringBuilder sb = new StringBuilder();
                sb.append("子进程注册多进程通信");
                sb.append(uTABMultiProcessClient == null ? "失败" : "成功");
                LogUtils.logD(TAG, sb.toString());
            }
        }
        return this.multiProcessClient;
    }

    public VariationSet getVariations(String str, String str2, Map<String, Object> map, boolean z, Object obj) {
        if (!this.initialized.get()) {
            LogUtils.logWAndReport(TAG, "请先调用 initialize() 方法初始化MultiProcessServiceImpl。");
            return null;
        } else if (this.multiProcessClient != null) {
            return this.multiProcessClient.getVariations(str, str2, map, z, obj);
        } else {
            return null;
        }
    }

    public boolean addActivateServerExperimentGroup(String str, Object obj) {
        if (!this.initialized.get()) {
            LogUtils.logWAndReport(TAG, "请先调用 initialize() 方法初始化MultiProcessServiceImpl。");
            return false;
        } else if (this.multiProcessClient != null) {
            return this.multiProcessClient.addActivateServerExperimentGroup(str, obj);
        } else {
            return false;
        }
    }

    public String getAppActivateTrackId() {
        if (!this.initialized.get()) {
            LogUtils.logWAndReport(TAG, "请先调用 initialize() 方法初始化MultiProcessServiceImpl。");
            return null;
        } else if (this.multiProcessClient != null) {
            return this.multiProcessClient.getAppActivateTrackId();
        } else {
            return null;
        }
    }

    public void startRealTimeDebug(Debug debug) {
        if (!this.initialized.get()) {
            LogUtils.logW(TAG, "请先调用 initialize() 方法初始化MultiProcessServiceImpl。");
        } else if (this.multiProcessClient != null) {
            this.multiProcessClient.startRealTimeDebug(debug);
        }
    }

    public void reportLog(String str, String str2, String str3, String str4) {
        if (!this.initialized.get()) {
            LogUtils.logW(TAG, "请先调用 initialize() 方法初始化MultiProcessServiceImpl。");
        } else if (this.multiProcessClient != null) {
            this.multiProcessClient.reportLog(str, str2, str3, str4);
        }
    }

    public void sendMsgToAllSubProcess(int i, Bundle bundle) {
        if (!this.initialized.get()) {
            LogUtils.logW(TAG, "请先调用 initialize() 方法初始化MultiProcessServiceImpl。");
        } else if (this.mainMultiProcessClient != null) {
            this.mainMultiProcessClient.sendMsgToAllSubProcess(i, bundle);
        }
    }
}
