package com.alibaba.analytics.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.text.TextUtils;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.core.config.UTBaseConfMgr;
import com.alibaba.analytics.core.db.DBMgr;
import com.alibaba.analytics.core.device.Device;
import com.alibaba.analytics.core.device.DeviceInfo;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.sync.UploadMgr;
import com.alibaba.analytics.core.sync.UploadMode;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.Base64;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.SpSetting;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.analytics.utils.UTMCDevice;
import com.alibaba.appmonitor.delegate.AppMonitorDelegate;
import com.ut.mini.UTAnalyticsDelegate;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Variables {
    private static final String DEBUG_DATE = "debug_date";
    private static final long DEBUG_TIME = 14400000;
    private static final String TAG_OPENID = "_openid";
    private static final String TAG_TURNOFF_REAL_TIME = "real_time_debug";
    private static final String UTRTD_NAME = "UTRealTimeDebug";
    public static final Variables s_instance = new Variables();
    private volatile boolean bApIsRealTimeDebugging = false;
    private volatile boolean bInit = false;
    private boolean bPackageDebugSwitch = false;
    private boolean hasReadPackageBuildId = false;
    private boolean hasReadPackageDebugSwitch = false;
    private boolean isAllServiceClosed = false;
    private boolean isGzipUpload = false;
    private boolean isHttpService = false;
    private boolean isRealtimeServiceClosed = false;
    private String mAppVersion = null;
    private String mAppkey = null;
    private String mChannel = null;
    private UTBaseConfMgr mConfMgr = null;
    /* access modifiers changed from: private */
    public Context mContext = null;
    private DBMgr mDbMgr = null;
    private boolean mDebugSamplingOption = false;
    private String mDebuggingKey = null;
    private boolean mIsOldDevice = false;
    private boolean mIsRealTimeDebugging = false;
    private boolean mIsSelfMonitorTurnOn = true;
    private volatile boolean mIsTurnOffDebugPlugin = false;
    private String mLUserid = null;
    private String mLUsernick = null;
    /* access modifiers changed from: private */
    public String mOaid = "";
    private String mOpenid;
    private String mPackageBuildId = null;
    private volatile IUTRequestAuthentication mRequestAuthenticationInstance = null;
    private String mSecret = null;
    private Map<String, String> mSessionProperties = null;
    private volatile String mTPKString = null;
    private String mTransferUrl = null;
    private String mUserid = null;
    private String mUsernick = null;

    public static boolean isNotDisAM() {
        return true;
    }

    public static Variables getInstance() {
        return s_instance;
    }

    @Deprecated
    public void turnOffDebugPlugin() {
        this.mIsTurnOffDebugPlugin = true;
    }

    public void turnOnSelfMonitor() {
        this.mIsSelfMonitorTurnOn = true;
    }

    public void turnOffSelfMonitor() {
        this.mIsSelfMonitorTurnOn = false;
    }

    public boolean isSelfMonitorTurnOn() {
        return this.mIsSelfMonitorTurnOn;
    }

    public synchronized void setHttpService(boolean z) {
        this.isHttpService = z;
    }

    public synchronized boolean isHttpService() {
        return this.isHttpService;
    }

    public synchronized void setAllServiceClosed(boolean z) {
        this.isAllServiceClosed = z;
    }

    public synchronized boolean isAllServiceClosed() {
        return this.isAllServiceClosed;
    }

    public synchronized void setRealtimeServiceClosed(boolean z) {
        this.isRealtimeServiceClosed = z;
    }

    public synchronized boolean isRealtimeServiceClosed() {
        return this.isRealtimeServiceClosed;
    }

    public boolean isGzipUpload() {
        return this.isGzipUpload;
    }

    public void setGzipUpload(boolean z) {
        this.isGzipUpload = z;
    }

    public String getTpkMD5() {
        if (this.mTPKString == null) {
            return null;
        }
        return "" + this.mTPKString.hashCode();
    }

    public String getTPKString() {
        return this.mTPKString;
    }

    public void setTPKString(String str) {
        this.mTPKString = str;
    }

    @Deprecated
    public boolean isTurnOffDebugPlugin() {
        return this.mIsTurnOffDebugPlugin;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(14:11|12|13|17|18|22|23|(2:24|25)|(2:29|30)(1:31)|32|33|34|35|36) */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x011f, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x00ec */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void init(android.app.Application r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            android.content.Context r0 = r6.getApplicationContext()     // Catch:{ all -> 0x0120 }
            r5.mContext = r0     // Catch:{ all -> 0x0120 }
            android.content.Context r0 = r5.mContext     // Catch:{ all -> 0x0120 }
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x001a
            java.lang.String r6 = "UTDC init failed"
            java.lang.Object[] r0 = new java.lang.Object[r1]     // Catch:{ all -> 0x0120 }
            java.lang.String r1 = "context:null"
            r0[r2] = r1     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r6, (java.lang.Object[]) r0)     // Catch:{ all -> 0x0120 }
            monitor-exit(r5)
            return
        L_0x001a:
            r0 = 2
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0120 }
            java.lang.String r3 = "init"
            r0[r2] = r3     // Catch:{ all -> 0x0120 }
            boolean r3 = r5.bInit     // Catch:{ all -> 0x0120 }
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)     // Catch:{ all -> 0x0120 }
            r0[r1] = r3     // Catch:{ all -> 0x0120 }
            r3 = 0
            com.alibaba.analytics.utils.Logger.i((java.lang.String) r3, (java.lang.Object[]) r0)     // Catch:{ all -> 0x0120 }
            boolean r0 = r5.bInit     // Catch:{ all -> 0x0120 }
            if (r0 != 0) goto L_0x011b
            java.lang.Thread r0 = new java.lang.Thread     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.Variables$1 r4 = new com.alibaba.analytics.core.Variables$1     // Catch:{ all -> 0x0120 }
            r4.<init>()     // Catch:{ all -> 0x0120 }
            r0.<init>(r4)     // Catch:{ all -> 0x0120 }
            r0.start()     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.selfmonitor.CrashDispatcher r0 = com.alibaba.analytics.core.selfmonitor.CrashDispatcher.getInstance()     // Catch:{ Throwable -> 0x0046 }
            r0.init()     // Catch:{ Throwable -> 0x0046 }
            goto L_0x004c
        L_0x0046:
            r0 = move-exception
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.utils.Logger.e(r3, r0, r4)     // Catch:{ all -> 0x0120 }
        L_0x004c:
            com.alibaba.analytics.core.selfmonitor.SelfMonitorHandle r0 = com.alibaba.analytics.core.selfmonitor.SelfMonitorHandle.getInstance()     // Catch:{ Throwable -> 0x0054 }
            r0.init()     // Catch:{ Throwable -> 0x0054 }
            goto L_0x005a
        L_0x0054:
            r0 = move-exception
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.utils.Logger.e(r3, r0, r2)     // Catch:{ all -> 0x0120 }
        L_0x005a:
            r5.getLocalInfo()     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.db.SQLiteCheckHelper r0 = new com.alibaba.analytics.core.db.SQLiteCheckHelper     // Catch:{ all -> 0x0120 }
            android.content.Context r2 = r5.mContext     // Catch:{ all -> 0x0120 }
            java.lang.String r4 = "ut.db"
            r0.<init>(r2, r4)     // Catch:{ all -> 0x0120 }
            r0.checkIntegrity()     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.db.DBMgr r0 = new com.alibaba.analytics.core.db.DBMgr     // Catch:{ all -> 0x0120 }
            android.content.Context r2 = r5.mContext     // Catch:{ all -> 0x0120 }
            java.lang.String r4 = "ut.db"
            r0.<init>(r2, r4)     // Catch:{ all -> 0x0120 }
            r5.mDbMgr = r0     // Catch:{ all -> 0x0120 }
            android.content.Context r0 = r5.mContext     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.network.NetworkUtil.register(r0)     // Catch:{ all -> 0x0120 }
            java.lang.String r0 = "com.taobao.orange.OrangeConfig"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Throwable -> 0x0080 }
            goto L_0x0081
        L_0x0080:
            r0 = r3
        L_0x0081:
            if (r0 == 0) goto L_0x008b
            com.alibaba.analytics.core.config.UTOrangeConfMgr r0 = new com.alibaba.analytics.core.config.UTOrangeConfMgr     // Catch:{ all -> 0x0120 }
            r0.<init>()     // Catch:{ all -> 0x0120 }
            r5.mConfMgr = r0     // Catch:{ all -> 0x0120 }
            goto L_0x0092
        L_0x008b:
            com.alibaba.analytics.core.config.UTDefaultConfMgr r0 = new com.alibaba.analytics.core.config.UTDefaultConfMgr     // Catch:{ all -> 0x0120 }
            r0.<init>()     // Catch:{ all -> 0x0120 }
            r5.mConfMgr = r0     // Catch:{ all -> 0x0120 }
        L_0x0092:
            com.alibaba.analytics.core.config.UTBaseConfMgr r0 = r5.mConfMgr     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.config.UTSampleConfBiz r2 = com.alibaba.analytics.core.config.UTSampleConfBiz.getInstance()     // Catch:{ all -> 0x0120 }
            r0.addConfBiz(r2)     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.config.UTBaseConfMgr r0 = r5.mConfMgr     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.config.UTStreamConfBiz r2 = com.alibaba.analytics.core.config.UTStreamConfBiz.getInstance()     // Catch:{ all -> 0x0120 }
            r0.addConfBiz(r2)     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.config.UTBaseConfMgr r0 = r5.mConfMgr     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.config.UTBussinessConfBiz r2 = new com.alibaba.analytics.core.config.UTBussinessConfBiz     // Catch:{ all -> 0x0120 }
            r2.<init>()     // Catch:{ all -> 0x0120 }
            r0.addConfBiz(r2)     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.config.UTBaseConfMgr r0 = r5.mConfMgr     // Catch:{ all -> 0x0120 }
            com.alibaba.appmonitor.sample.AMSamplingMgr r2 = com.alibaba.appmonitor.sample.AMSamplingMgr.getInstance()     // Catch:{ all -> 0x0120 }
            r0.addConfBiz(r2)     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.config.UTBaseConfMgr r0 = r5.mConfMgr     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.config.UTRealtimeConfBiz r2 = com.alibaba.analytics.core.config.UTRealtimeConfBiz.getInstance()     // Catch:{ all -> 0x0120 }
            r0.addConfBiz(r2)     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.config.UTBaseConfMgr r0 = r5.mConfMgr     // Catch:{ Throwable -> 0x00ec }
            com.alibaba.analytics.core.config.SystemConfigMgr r2 = com.alibaba.analytics.core.config.SystemConfigMgr.getInstance()     // Catch:{ Throwable -> 0x00ec }
            r0.addConfBiz(r2)     // Catch:{ Throwable -> 0x00ec }
            com.alibaba.analytics.core.ipv6.TnetIpv6Manager r0 = com.alibaba.analytics.core.ipv6.TnetIpv6Manager.getInstance()     // Catch:{ Throwable -> 0x00ec }
            r0.registerConfigListener()     // Catch:{ Throwable -> 0x00ec }
            com.alibaba.analytics.core.config.SystemConfigMgr r0 = com.alibaba.analytics.core.config.SystemConfigMgr.getInstance()     // Catch:{ Throwable -> 0x00ec }
            java.lang.String r2 = "sw_plugin"
            com.alibaba.analytics.core.config.DebugPluginSwitch r3 = new com.alibaba.analytics.core.config.DebugPluginSwitch     // Catch:{ Throwable -> 0x00ec }
            r3.<init>()     // Catch:{ Throwable -> 0x00ec }
            r0.register(r2, r3)     // Catch:{ Throwable -> 0x00ec }
            com.alibaba.analytics.core.config.SystemConfigMgr r0 = com.alibaba.analytics.core.config.SystemConfigMgr.getInstance()     // Catch:{ Throwable -> 0x00ec }
            java.lang.String r2 = "audid"
            com.alibaba.analytics.core.config.AudidConfigListener r3 = new com.alibaba.analytics.core.config.AudidConfigListener     // Catch:{ Throwable -> 0x00ec }
            r3.<init>()     // Catch:{ Throwable -> 0x00ec }
            r0.register(r2, r3)     // Catch:{ Throwable -> 0x00ec }
        L_0x00ec:
            com.alibaba.analytics.core.config.UTBaseConfMgr r0 = r5.mConfMgr     // Catch:{ all -> 0x0120 }
            r0.requestOnlineConfig()     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.logbuilder.TimeStampAdjustMgr r0 = com.alibaba.analytics.core.logbuilder.TimeStampAdjustMgr.getInstance()     // Catch:{ all -> 0x0120 }
            r0.startSync()     // Catch:{ all -> 0x0120 }
            com.alibaba.appmonitor.delegate.AppMonitorDelegate.init(r6)     // Catch:{ all -> 0x0120 }
            com.ut.mini.UTAnalyticsDelegate r0 = com.ut.mini.UTAnalyticsDelegate.getInstance()     // Catch:{ all -> 0x0120 }
            r0.initUT(r6)     // Catch:{ all -> 0x0120 }
            r5.initRealTimeDebug()     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.sync.UploadMgr r6 = com.alibaba.analytics.core.sync.UploadMgr.getInstance()     // Catch:{ all -> 0x0120 }
            r6.start()     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.utils.TaskExecutor r6 = com.alibaba.analytics.utils.TaskExecutor.getInstance()     // Catch:{ all -> 0x0120 }
            com.alibaba.analytics.core.Variables$2 r0 = new com.alibaba.analytics.core.Variables$2     // Catch:{ all -> 0x0120 }
            r0.<init>()     // Catch:{ all -> 0x0120 }
            r6.submit(r0)     // Catch:{ all -> 0x0120 }
            r5.bInit = r1     // Catch:{ all -> 0x0120 }
            goto L_0x011e
        L_0x011b:
            com.alibaba.analytics.core.config.UTConfigMgr.postAllServerConfig()     // Catch:{ all -> 0x0120 }
        L_0x011e:
            monitor-exit(r5)
            return
        L_0x0120:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.Variables.init(android.app.Application):void");
    }

    /* access modifiers changed from: private */
    public void trackInfoForPreLoad() {
        try {
            Map<String, String> infoForPreApk = AppInfoUtil.getInfoForPreApk(this.mContext);
            if (infoForPreApk != null && infoForPreApk.size() > 0) {
                HashMap hashMap = new HashMap();
                hashMap.put(LogField.EVENTID.toString(), "1021");
                hashMap.putAll(infoForPreApk);
                UTAnalyticsDelegate.getInstance().transferLog(hashMap);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0050 */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0078 A[Catch:{ Throwable -> 0x011d }] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0082 A[Catch:{ Throwable -> 0x011d }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void trackUtdidType() {
        /*
            r10 = this;
            android.content.Context r0 = r10.mContext     // Catch:{ Throwable -> 0x011d }
            com.ut.device.UTDevice.getUtdid(r0)     // Catch:{ Throwable -> 0x011d }
            int r0 = com.ut.device.UTDevice.getType()     // Catch:{ Throwable -> 0x011d }
            java.lang.String r1 = ""
            android.content.Context r2 = r10.mContext     // Catch:{ Throwable -> 0x011d }
            java.lang.String r3 = "Alvin2"
            r4 = 0
            android.content.SharedPreferences r2 = r2.getSharedPreferences(r3, r4)     // Catch:{ Throwable -> 0x011d }
            if (r2 == 0) goto L_0x0032
            java.lang.String r3 = "t2"
            r5 = 0
            long r2 = r2.getLong(r3, r5)     // Catch:{ Throwable -> 0x011d }
            int r7 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x0032
            java.text.SimpleDateFormat r1 = new java.text.SimpleDateFormat     // Catch:{ Throwable -> 0x011d }
            java.lang.String r5 = "yyyyMMdd"
            r1.<init>(r5)     // Catch:{ Throwable -> 0x011d }
            java.lang.Long r2 = java.lang.Long.valueOf(r2)     // Catch:{ Throwable -> 0x011d }
            java.lang.String r1 = r1.format(r2)     // Catch:{ Throwable -> 0x011d }
        L_0x0032:
            java.lang.String r2 = ""
            android.content.Context r3 = r10.mContext     // Catch:{ Exception -> 0x0041 }
            android.content.ContentResolver r3 = r3.getContentResolver()     // Catch:{ Exception -> 0x0041 }
            java.lang.String r5 = "mqBRboGZkQPcAkyk"
            java.lang.String r3 = android.provider.Settings.System.getString(r3, r5)     // Catch:{ Exception -> 0x0041 }
            r2 = r3
        L_0x0041:
            java.lang.String r3 = ""
            android.content.Context r5 = r10.mContext     // Catch:{ Exception -> 0x0050 }
            android.content.ContentResolver r5 = r5.getContentResolver()     // Catch:{ Exception -> 0x0050 }
            java.lang.String r6 = "dxCRMxhQkdGePGnp"
            java.lang.String r5 = android.provider.Settings.System.getString(r5, r6)     // Catch:{ Exception -> 0x0050 }
            r3 = r5
        L_0x0050:
            android.content.Context r5 = r10.mContext     // Catch:{ Exception -> 0x006a }
            android.content.ContentResolver r5 = r5.getContentResolver()     // Catch:{ Exception -> 0x006a }
            java.lang.String r6 = "af3de43b346c5838"
            java.lang.String r7 = "1"
            android.provider.Settings.System.putString(r5, r6, r7)     // Catch:{ Exception -> 0x006a }
            android.content.Context r5 = r10.mContext     // Catch:{ Exception -> 0x006a }
            android.content.ContentResolver r5 = r5.getContentResolver()     // Catch:{ Exception -> 0x006a }
            java.lang.String r6 = "af3de43b346c5838"
            java.lang.String r5 = android.provider.Settings.System.getString(r5, r6)     // Catch:{ Exception -> 0x006a }
            goto L_0x006c
        L_0x006a:
            java.lang.String r5 = "0"
        L_0x006c:
            java.lang.String r6 = ""
            android.content.Context r7 = r10.mContext     // Catch:{ Throwable -> 0x011d }
            java.lang.String r8 = "ContextData"
            android.content.SharedPreferences r4 = r7.getSharedPreferences(r8, r4)     // Catch:{ Throwable -> 0x011d }
            if (r4 == 0) goto L_0x0080
            java.lang.String r6 = "K_1171477665"
            java.lang.String r7 = ""
            java.lang.String r6 = r4.getString(r6, r7)     // Catch:{ Throwable -> 0x011d }
        L_0x0080:
            if (r0 <= 0) goto L_0x011d
            com.ut.mini.UTHitBuilders$UTCustomHitBuilder r4 = new com.ut.mini.UTHitBuilders$UTCustomHitBuilder     // Catch:{ Throwable -> 0x011d }
            java.lang.String r7 = "UTDID"
            r4.<init>(r7)     // Catch:{ Throwable -> 0x011d }
            java.lang.String r7 = "type"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x011d }
            r8.<init>()     // Catch:{ Throwable -> 0x011d }
            java.lang.String r9 = ""
            r8.append(r9)     // Catch:{ Throwable -> 0x011d }
            r8.append(r0)     // Catch:{ Throwable -> 0x011d }
            java.lang.String r0 = r8.toString()     // Catch:{ Throwable -> 0x011d }
            r4.setProperty(r7, r0)     // Catch:{ Throwable -> 0x011d }
            boolean r0 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x011d }
            if (r0 != 0) goto L_0x00aa
            java.lang.String r0 = "time"
            r4.setProperty(r0, r1)     // Catch:{ Throwable -> 0x011d }
        L_0x00aa:
            boolean r0 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x011d }
            if (r0 != 0) goto L_0x00b5
            java.lang.String r0 = "settings"
            r4.setProperty(r0, r2)     // Catch:{ Throwable -> 0x011d }
        L_0x00b5:
            boolean r0 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x011d }
            if (r0 != 0) goto L_0x00c0
            java.lang.String r0 = "oldSettings"
            r4.setProperty(r0, r3)     // Catch:{ Throwable -> 0x011d }
        L_0x00c0:
            boolean r0 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Throwable -> 0x011d }
            if (r0 != 0) goto L_0x00cb
            java.lang.String r0 = "utdid2"
            r4.setProperty(r0, r6)     // Catch:{ Throwable -> 0x011d }
        L_0x00cb:
            boolean r0 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x011d }
            if (r0 != 0) goto L_0x00e1
            java.lang.String r0 = "1"
            boolean r0 = r0.equalsIgnoreCase(r5)     // Catch:{ Throwable -> 0x011d }
            if (r0 == 0) goto L_0x00e1
            java.lang.String r0 = "utdidws"
            java.lang.String r1 = "1"
            r4.setProperty(r0, r1)     // Catch:{ Throwable -> 0x011d }
            goto L_0x00e8
        L_0x00e1:
            java.lang.String r0 = "utdidws"
            java.lang.String r1 = "0"
            r4.setProperty(r0, r1)     // Catch:{ Throwable -> 0x011d }
        L_0x00e8:
            boolean r0 = r10.checkSettingsPermissionGranted()     // Catch:{ Throwable -> 0x011d }
            if (r0 == 0) goto L_0x00f6
            java.lang.String r0 = "checkws"
            java.lang.String r1 = "1"
            r4.setProperty(r0, r1)     // Catch:{ Throwable -> 0x011d }
            goto L_0x00fd
        L_0x00f6:
            java.lang.String r0 = "checkws"
            java.lang.String r1 = "0"
            r4.setProperty(r0, r1)     // Catch:{ Throwable -> 0x011d }
        L_0x00fd:
            boolean r0 = r10.checkStoragePermissionGranted()     // Catch:{ Throwable -> 0x011d }
            if (r0 == 0) goto L_0x010b
            java.lang.String r0 = "checkwes"
            java.lang.String r1 = "1"
            r4.setProperty(r0, r1)     // Catch:{ Throwable -> 0x011d }
            goto L_0x0112
        L_0x010b:
            java.lang.String r0 = "checkwes"
            java.lang.String r1 = "0"
            r4.setProperty(r0, r1)     // Catch:{ Throwable -> 0x011d }
        L_0x0112:
            com.ut.mini.UTAnalyticsDelegate r0 = com.ut.mini.UTAnalyticsDelegate.getInstance()     // Catch:{ Throwable -> 0x011d }
            java.util.Map r1 = r4.build()     // Catch:{ Throwable -> 0x011d }
            r0.transferLog(r1)     // Catch:{ Throwable -> 0x011d }
        L_0x011d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.Variables.trackUtdidType():void");
    }

    private boolean checkSettingsPermissionGranted() {
        try {
            return this.mContext.checkPermission("android.permission.WRITE_SETTINGS", Binder.getCallingPid(), Binder.getCallingUid()) == 0;
        } catch (Throwable unused) {
            return false;
        }
    }

    private boolean checkStoragePermissionGranted() {
        try {
            return this.mContext.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", Binder.getCallingPid(), Binder.getCallingUid()) == 0;
        } catch (Throwable unused) {
            return false;
        }
    }

    public UTBaseConfMgr getConfMgr() {
        return this.mConfMgr;
    }

    private void getLocalInfo() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("UTCommon", 0);
        String string = sharedPreferences.getString("_lun", "");
        if (!StringUtils.isEmpty(string)) {
            try {
                this.mLUsernick = new String(Base64.decode(string.getBytes(), 2), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String string2 = sharedPreferences.getString("_luid", "");
        if (!StringUtils.isEmpty(string2)) {
            try {
                this.mLUserid = new String(Base64.decode(string2.getBytes(), 2), "UTF-8");
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
        }
        String string3 = sharedPreferences.getString(TAG_OPENID, "");
        if (!StringUtils.isEmpty(string3)) {
            try {
                this.mOpenid = new String(Base64.decode(string3.getBytes(), 2), "UTF-8");
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public String getAppkey() {
        return this.mAppkey;
    }

    public String getSecret() {
        return this.mSecret;
    }

    public void setSecret(String str) {
        this.mSecret = str;
    }

    public void setAppVersion(String str) {
        this.mAppVersion = str;
    }

    public String getAppVersion() {
        Map<String, String> deviceInfo;
        if (TextUtils.isEmpty(this.mAppVersion) && (deviceInfo = UTMCDevice.getDeviceInfo(getContext())) != null) {
            this.mAppVersion = deviceInfo.get(LogField.APPVERSION);
        }
        return this.mAppVersion;
    }

    public void setRequestAuthenticationInstance(IUTRequestAuthentication iUTRequestAuthentication) {
        this.mRequestAuthenticationInstance = iUTRequestAuthentication;
        if (iUTRequestAuthentication != null) {
            this.mAppkey = iUTRequestAuthentication.getAppkey();
        }
    }

    public IUTRequestAuthentication getRequestAuthenticationInstance() {
        return this.mRequestAuthenticationInstance;
    }

    public void turnOnDebug() {
        setDebug(true);
    }

    public void setChannel(String str) {
        Logger.d((String) null, str, str);
        this.mChannel = str;
    }

    public String getLongLoginUsernick() {
        return this.mLUsernick;
    }

    public String getLongLoingUserid() {
        return this.mLUserid;
    }

    public String getChannel() {
        if (TextUtils.isEmpty(this.mChannel)) {
            String str = SpSetting.get(getContext(), "channel");
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        }
        return this.mChannel;
    }

    public String getUsernick() {
        return this.mUsernick;
    }

    public String getUserid() {
        return this.mUserid;
    }

    public String getOpenid() {
        return this.mOpenid;
    }

    public void updateUserAccount(String str, String str2, String str3) {
        setUsernick(str);
        updateUserIdAndOpenId(str2, str3);
        storeUsernick(str);
    }

    private void updateUserIdAndOpenId(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            setUserid((String) null);
            setOpenid((String) null);
        } else if (!TextUtils.isEmpty(str2) || !str.equals(this.mUserid)) {
            setUserid(str);
            setOpenid(str2);
            storeUserId(str);
            storeOpenId(str2);
        }
    }

    @Deprecated
    public void setIsOldDevice(boolean z) {
        if (this.mContext != null) {
            this.mContext.getSharedPreferences("UTCommon", 0).edit().putBoolean("_isolddevice", z).commit();
        }
    }

    @Deprecated
    public boolean isOldDevice() {
        if (!this.mIsOldDevice && this.mContext != null) {
            this.mIsOldDevice = this.mContext.getSharedPreferences("UTCommon", 0).getBoolean("_isolddevice", false);
        }
        return this.mIsOldDevice;
    }

    public synchronized String getHostPackageImei() {
        DeviceInfo device = Device.getDevice(this.mContext);
        if (device == null) {
            return "";
        }
        return device.getImei();
    }

    public synchronized String getHostPackageImsi() {
        DeviceInfo device = Device.getDevice(this.mContext);
        if (device == null) {
            return "";
        }
        return device.getImsi();
    }

    public synchronized void setDebugSamplingOption() {
        this.mDebugSamplingOption = true;
        AppMonitorDelegate.IS_DEBUG = true;
    }

    public synchronized boolean getDebugSamplingOption() {
        return this.mDebugSamplingOption;
    }

    public synchronized void setSessionProperties(Map<String, String> map) {
        this.mSessionProperties = map;
    }

    public synchronized Map<String, String> getSessionProperties() {
        return this.mSessionProperties;
    }

    public synchronized void setDebugKey(String str) {
        this.mDebuggingKey = str;
    }

    public synchronized String getDebugKey() {
        return this.mDebuggingKey;
    }

    public synchronized boolean isRealTimeDebug() {
        return this.mIsRealTimeDebugging;
    }

    public synchronized void setRealTimeDebugFlag() {
        this.mIsRealTimeDebugging = true;
    }

    public synchronized void resetRealTimeDebugFlag() {
        this.mIsRealTimeDebugging = false;
    }

    public boolean isApRealTimeDebugging() {
        return this.bApIsRealTimeDebugging;
    }

    public void turnOffRealTimeDebug() {
        resetRealTimeDebugFlag();
        setDebugKey((String) null);
        UploadMgr.getInstance().setMode(UploadMode.INTERVAL);
        storeRealTimeDebugSharePreference((Map<String, String>) null);
        this.bApIsRealTimeDebugging = false;
    }

    public void turnOnRealTimeDebug(Map<String, String> map) {
        Logger.d();
        if ("0".equalsIgnoreCase(SystemConfigMgr.getInstance().get(TAG_TURNOFF_REAL_TIME))) {
            Logger.w("Variables", "Server Config turn off RealTimeDebug Mode!");
            return;
        }
        if (map != null && map.containsKey(Constants.RealTimeDebug.DEBUG_API_URL) && map.containsKey("debug_key")) {
            String str = map.get("debug_key");
            if (!StringUtils.isEmpty(map.get(Constants.RealTimeDebug.DEBUG_API_URL)) && !StringUtils.isEmpty(str)) {
                setRealTimeDebugFlag();
                setDebugKey(str);
            }
            if (map.containsKey(Constants.RealTimeDebug.DEBUG_SAMPLING_OPTION)) {
                setDebugSamplingOption();
            }
            setDebug(true);
            UploadMgr.getInstance().setMode(UploadMode.REALTIME);
        }
        storeRealTimeDebugSharePreference(map);
    }

    private void storeRealTimeDebugSharePreference(Map<String, String> map) {
        if (this.mContext != null) {
            Logger.d("", map);
            SharedPreferences.Editor edit = this.mContext.getSharedPreferences(UTRTD_NAME, 0).edit();
            if (map == null || !map.containsKey(Constants.RealTimeDebug.DEBUG_STORE)) {
                edit.putLong(DEBUG_DATE, 0);
            } else {
                edit.putString(Constants.RealTimeDebug.DEBUG_API_URL, map.get(Constants.RealTimeDebug.DEBUG_API_URL));
                edit.putString("debug_key", map.get("debug_key"));
                edit.putLong(DEBUG_DATE, System.currentTimeMillis());
            }
            edit.commit();
        }
    }

    private void initRealTimeDebug() {
        if (this.mContext != null) {
            Logger.d();
            SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(UTRTD_NAME, 0);
            long j = sharedPreferences.getLong(DEBUG_DATE, 0);
            Logger.d("", "debugDate", Long.valueOf(j));
            if (System.currentTimeMillis() - j <= DEBUG_TIME) {
                HashMap hashMap = new HashMap();
                hashMap.put(Constants.RealTimeDebug.DEBUG_API_URL, sharedPreferences.getString(Constants.RealTimeDebug.DEBUG_API_URL, ""));
                hashMap.put("debug_key", sharedPreferences.getString("debug_key", ""));
                turnOnRealTimeDebug(hashMap);
            }
        }
    }

    public void setDebug(boolean z) {
        Logger.setDebug(z);
    }

    @Deprecated
    public String getTransferUrl() {
        return this.mTransferUrl;
    }

    public DBMgr getDbMgr() {
        return this.mDbMgr;
    }

    public boolean isInit() {
        return this.bInit;
    }

    public boolean isDebugPackage() {
        if (this.hasReadPackageDebugSwitch) {
            return this.bPackageDebugSwitch;
        }
        Context context = getContext();
        if (context == null) {
            return false;
        }
        if ("1".equalsIgnoreCase(AppInfoUtil.getString(context, "package_type"))) {
            this.bPackageDebugSwitch = true;
            this.hasReadPackageDebugSwitch = true;
        }
        return this.bPackageDebugSwitch;
    }

    public String getPackageBuildId() {
        if (this.hasReadPackageBuildId) {
            return this.mPackageBuildId;
        }
        Context context = getContext();
        if (context == null) {
            return null;
        }
        this.mPackageBuildId = AppInfoUtil.getString(context, "build_id");
        this.hasReadPackageBuildId = true;
        return this.mPackageBuildId;
    }

    private void setUsernick(String str) {
        this.mUsernick = str;
        if (!StringUtils.isEmpty(str)) {
            this.mLUsernick = str;
        }
    }

    private void storeUsernick(String str) {
        if (!StringUtils.isEmpty(str) && this.mContext != null) {
            try {
                SharedPreferences.Editor edit = this.mContext.getSharedPreferences("UTCommon", 0).edit();
                edit.putString("_lun", new String(Base64.encode(str.getBytes("UTF-8"), 2)));
                edit.commit();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUserid(String str) {
        this.mUserid = str;
        if (!StringUtils.isEmpty(str)) {
            this.mLUserid = str;
        }
    }

    private void storeUserId(String str) {
        if (!StringUtils.isEmpty(str) && this.mContext != null) {
            try {
                SharedPreferences.Editor edit = this.mContext.getSharedPreferences("UTCommon", 0).edit();
                edit.putString("_luid", new String(Base64.encode(str.getBytes("UTF-8"), 2)));
                edit.commit();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void setOpenid(String str) {
        this.mOpenid = str;
    }

    private void storeOpenId(String str) {
        if (this.mContext != null) {
            try {
                SharedPreferences.Editor edit = this.mContext.getSharedPreferences("UTCommon", 0).edit();
                if (TextUtils.isEmpty(str)) {
                    edit.putString(TAG_OPENID, (String) null);
                } else {
                    edit.putString(TAG_OPENID, new String(Base64.encode(str.getBytes("UTF-8"), 2)));
                }
                edit.commit();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public String getOaid() {
        return this.mOaid;
    }
}
