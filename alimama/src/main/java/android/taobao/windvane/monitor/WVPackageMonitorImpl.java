package android.taobao.windvane.monitor;

import android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;

public class WVPackageMonitorImpl implements WVPackageMonitorInterface {
    private static final String FIRST_INSTALL_FLAG = "FIRST_INSTALL";
    private static final String TAG = "WVPackageMonitorImpl";
    private long appBackgroundTime = 0;
    private long appResumeTime = 0;
    private long diffTime = 0;
    private long ignoreTime = 0;
    boolean isPkgInUse = true;

    public void packageApp(ZipAppInfo zipAppInfo, String str, String str2, String str3, boolean z, long j, long j2, int i, String str4, boolean z2, long j3) {
        String str5;
        ZipAppInfo zipAppInfo2 = zipAppInfo;
        String str6 = z2 ? "1" : "0";
        long j4 = zipAppInfo2.t + this.diffTime;
        if (this.appResumeTime > j4) {
            str5 = "0";
        } else {
            str5 = (this.appResumeTime <= 0 || this.appResumeTime <= this.appBackgroundTime) ? "2" : "1";
        }
        AppMonitorUtil.commitPackageAppUpdateInfo(zipAppInfo, str5, str6, j, j2, System.currentTimeMillis() - j4, j3 - j4);
        if (z) {
            AppMonitorUtil.commitPackageAppUpdateSuccess(zipAppInfo2.name);
        } else {
            AppMonitorUtil.commitPackageAppUpdateError(String.valueOf(i), str4, zipAppInfo2.name);
        }
    }

    public void uploadStartAppTime(long j) {
        this.appResumeTime = j;
        TaoLog.i(TAG, "uploadAppResumeTime : " + j);
    }

    public void uploadBackgroundTime(long j) {
        this.appBackgroundTime = j;
        TaoLog.i(TAG, "uploadBackgroundTime : " + j);
    }

    public void uploadDiffTimeTime(long j) {
        this.diffTime = j;
        TaoLog.i(TAG, "uploadDiffTime : " + j);
    }

    public void onStartCleanAppCache(long j, int i, int i2, int i3, float f, int i4, int i5, float f2, int i6) {
        AppMonitorUtil.commitPackageClearUpInfo(j, i, i2, i3, f, i4, i5, f2, i6);
    }

    public void commitPackageQueueInfo(String str, long j, long j2) {
        AppMonitorUtil.commitPackageQueueInfo(str, j, j2);
    }

    public void commitPackageVisitInfo(String str, String str2, long j, long j2, long j3, long j4, long j5) {
        AppMonitorUtil.commitPackageVisitInfo(str, str2, j, j2, j3, j4, j5);
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x008e A[Catch:{ Throwable -> 0x0083 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void commitPackageVisitError(java.lang.String r8, java.lang.String r9, java.lang.String r10) {
        /*
            r7 = this;
            long r0 = r7.appResumeTime
            r2 = 0
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 != 0) goto L_0x0010
            java.lang.String r8 = "WVPackageMonitorImpl"
            java.lang.String r9 = "WVMonitor must be init first"
            android.taobao.windvane.util.TaoLog.i(r8, r9)
            return
        L_0x0010:
            java.lang.String r0 = "22"
            boolean r0 = r10.equals(r0)
            if (r0 == 0) goto L_0x0020
            java.lang.String r8 = "WVPackageMonitorImpl"
            java.lang.String r9 = "Ignore force online failed"
            android.taobao.windvane.util.TaoLog.d(r8, r9)
            return
        L_0x0020:
            java.lang.String r0 = "WVPackageMonitorImpl"
            java.lang.String r1 = "FIRST_INSTALL"
            long r0 = android.taobao.windvane.util.ConfigStorage.getLongVal(r0, r1, r2)
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 != 0) goto L_0x0069
            long r0 = java.lang.System.currentTimeMillis()
            long r2 = r7.appResumeTime
            long r2 = r0 - r2
            r4 = 30000(0x7530, double:1.4822E-319)
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 <= 0) goto L_0x0042
            java.lang.String r2 = "WVPackageMonitorImpl"
            java.lang.String r3 = "FIRST_INSTALL"
            android.taobao.windvane.util.ConfigStorage.putLongVal(r2, r3, r0)
            goto L_0x0069
        L_0x0042:
            java.lang.String r0 = "WVPackageMonitorImpl"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "ignore visit error : pkgName : "
            r1.append(r2)
            r1.append(r8)
            java.lang.String r8 = " errorMsg : "
            r1.append(r8)
            r1.append(r9)
            java.lang.String r8 = " errorCode :"
            r1.append(r8)
            r1.append(r10)
            java.lang.String r8 = r1.toString()
            android.taobao.windvane.util.TaoLog.i(r0, r8)
            return
        L_0x0069:
            boolean r0 = r7.isPkgInUse
            if (r0 == 0) goto L_0x00ae
            r0 = 0
            if (r8 == 0) goto L_0x0085
            java.lang.String r1 = "-"
            boolean r1 = r8.contains(r1)     // Catch:{ Throwable -> 0x0083 }
            if (r1 == 0) goto L_0x0085
            java.lang.String r1 = "-"
            int r1 = r8.lastIndexOf(r1)     // Catch:{ Throwable -> 0x0083 }
            java.lang.String r1 = r8.substring(r0, r1)     // Catch:{ Throwable -> 0x0083 }
            goto L_0x0086
        L_0x0083:
            r1 = move-exception
            goto L_0x00a8
        L_0x0085:
            r1 = r8
        L_0x0086:
            java.lang.String r2 = "12"
            boolean r2 = r10.equals(r2)     // Catch:{ Throwable -> 0x0083 }
            if (r2 != 0) goto L_0x009f
            java.lang.String r2 = "15"
            boolean r2 = r10.equals(r2)     // Catch:{ Throwable -> 0x0083 }
            if (r2 == 0) goto L_0x0097
            goto L_0x009f
        L_0x0097:
            android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup r2 = android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup.getInstance()     // Catch:{ Throwable -> 0x0083 }
            r2.updateAccessTimes(r1, r0)     // Catch:{ Throwable -> 0x0083 }
            goto L_0x00ae
        L_0x009f:
            android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup r2 = android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup.getInstance()     // Catch:{ Throwable -> 0x0083 }
            r3 = 1
            r2.updateAccessTimes(r1, r3)     // Catch:{ Throwable -> 0x0083 }
            goto L_0x00ae
        L_0x00a8:
            boolean r1 = r1 instanceof java.lang.ClassNotFoundException
            if (r1 == 0) goto L_0x00ae
            r7.isPkgInUse = r0
        L_0x00ae:
            android.taobao.windvane.monitor.AppMonitorUtil.commitPackageAppVisitError(r8, r9, r10)
            java.lang.String r0 = "WVPackageMonitorImpl"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "pkgName : "
            r1.append(r2)
            r1.append(r8)
            java.lang.String r8 = " errorMsg : "
            r1.append(r8)
            r1.append(r9)
            java.lang.String r8 = " errorCode :"
            r1.append(r8)
            r1.append(r10)
            java.lang.String r8 = r1.toString()
            android.taobao.windvane.util.TaoLog.d(r0, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.monitor.WVPackageMonitorImpl.commitPackageVisitError(java.lang.String, java.lang.String, java.lang.String):void");
    }

    public void commitPackageWarning(String str, String str2) {
        TaoLog.w(TAG, "pkgName : " + str + " url : " + str2 + " not in zipApp");
        AppMonitorUtil.commitPackageAppWarning(str, str2);
    }

    public void commitPackageVisitSuccess(String str, long j) {
        if (this.appResumeTime == 0) {
            TaoLog.i(TAG, "WVMonitor must be init first");
            return;
        }
        if (ConfigStorage.getLongVal(TAG, FIRST_INSTALL_FLAG, 0) == 0) {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - this.appResumeTime > 30000) {
                ConfigStorage.putLongVal(TAG, FIRST_INSTALL_FLAG, currentTimeMillis);
            } else {
                TaoLog.i(TAG, "ignore visit success : pkgName : " + str);
                return;
            }
        }
        if (this.isPkgInUse) {
            try {
                WVPackageAppCleanup.getInstance().updateAccessTimes(str, false);
            } catch (Throwable th) {
                if (th instanceof ClassNotFoundException) {
                    this.isPkgInUse = false;
                }
            }
        }
        AppMonitorUtil.commitPackageVisitSuccess(str, j);
    }

    public void commitPackageUpdateStartInfo(long j, long j2) {
        AppMonitorUtil.commitPackageUpdateStartInfo(j, j2);
    }

    public void commitFail(String str, int i, String str2, String str3) {
        AppMonitorUtil.commitFail(str, i, str2, str3);
    }

    public void commitZCacheDiurnalOverview(String str) {
        AppMonitorUtil.commitZCacheDiurnalOverview(str);
    }

    public void commitZCacheDownLoadTime(String str, long j, long j2, long j3, String str2, boolean z) {
        AppMonitorUtil.commitZCacheDownLoadTime(str, j, j2, j3, str2, z);
    }
}
