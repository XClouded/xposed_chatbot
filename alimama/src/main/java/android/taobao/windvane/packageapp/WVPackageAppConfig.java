package android.taobao.windvane.packageapp;

import android.os.Build;
import android.taobao.windvane.WindvaneException;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUtils;
import android.taobao.windvane.connect.HttpConnectListener;
import android.taobao.windvane.connect.HttpConnector;
import android.taobao.windvane.connect.HttpRequest;
import android.taobao.windvane.connect.HttpResponse;
import android.taobao.windvane.packageapp.adaptive.ZCacheConfigManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ConfigDataUtils;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import android.webkit.ValueCallback;

import java.io.UnsupportedEncodingException;

public class WVPackageAppConfig implements WVPackageAppConfigInterface {
    /* access modifiers changed from: private */
    public static final String TAG = "WVPackageAppConfig";
    private static volatile ZipGlobalConfig config;
    /* access modifiers changed from: private */
    public boolean forceFullNextTime = false;
    private long lastUpdateTime = 0;
    public String packageCfg = "{}";

    public ZipGlobalConfig getGlobalConfig() {
        ZipGlobalConfig zipGlobalConfig;
        synchronized (TAG) {
            if (config == null) {
                String readGlobalConfig = ZipAppFileManager.getInstance().readGlobalConfig(false);
                try {
                    config = ConfigDataUtils.parseGlobalConfig(readGlobalConfig);
                    if (TaoLog.getLogStatus()) {
                        String str = TAG;
                        TaoLog.d(str, "PackageAppforDebug 首次总控解析成功 data:【" + readGlobalConfig + "】");
                    }
                } catch (Exception e) {
                    String str2 = TAG;
                    TaoLog.e(str2, "PackageAppforDebug 总控解析本地总控文件失败 :【 " + e.getMessage() + "】");
                }
                if (config == null) {
                    config = new ZipGlobalConfig();
                }
            }
            zipGlobalConfig = config;
        }
        return zipGlobalConfig;
    }

    public void requestFullConfigNextTime() {
        this.forceFullNextTime = true;
    }

    public void updateGlobalConfig(boolean z, final ValueCallback<ZipGlobalConfig> valueCallback, final ValueCallback<WindvaneException> valueCallback2, String str, String str2) {
        String str3;
        Long l;
        Long l2;
        if (Build.VERSION.SDK_INT > 11 && WVCommonConfig.commonConfig.packageAppStatus >= 2) {
            long currentTimeMillis = System.currentTimeMillis();
            if (z || this.lastUpdateTime == 0 || currentTimeMillis - this.lastUpdateTime >= 300000) {
                this.lastUpdateTime = currentTimeMillis;
                String str4 = "0";
                long longVal = ConfigStorage.getLongVal(WVConfigManager.SPNAME_CONFIG, "package_updateTime", 0);
                if (TextUtils.isEmpty(str2)) {
                    long currentTimeMillis2 = System.currentTimeMillis() - longVal;
                    if (currentTimeMillis2 > ((long) WVCommonConfig.commonConfig.recoveryInterval) || currentTimeMillis2 < 0) {
                        str4 = "0";
                        str3 = "0";
                        ConfigStorage.putLongVal(WVConfigManager.SPNAME_CONFIG, "package_updateTime", System.currentTimeMillis());
                    } else {
                        if (getGlobalConfig() != null) {
                            str4 = getGlobalConfig().v;
                            try {
                                Long.valueOf(0);
                                Long.valueOf(0);
                                if (str.contains(".")) {
                                    String[] split = str.split("\\.");
                                    if (split != null) {
                                        if (split.length >= 2) {
                                            l = Long.valueOf(Long.parseLong(split[0]));
                                        }
                                    }
                                    String str5 = TAG;
                                    TaoLog.w(str5, "PackageApp snapshortN is error :  version = " + str);
                                    return;
                                }
                                l = Long.valueOf(str);
                                if (str4.contains(".")) {
                                    String[] split2 = str4.split("\\.");
                                    if (split2 != null) {
                                        if (split2.length >= 2) {
                                            l2 = Long.valueOf(Long.parseLong(split2[0]));
                                            if (Math.abs(l.longValue() - l2.longValue()) > 846720000) {
                                                str4 = "0";
                                            }
                                        }
                                    }
                                    String str6 = TAG;
                                    TaoLog.w(str6, "PackageApp version is error :  version = " + str4);
                                    return;
                                }
                                l2 = Long.valueOf(Long.parseLong(str4));
                                if (Math.abs(l.longValue() - l2.longValue()) > 846720000) {
                                    str4 = "0";
                                }
                            } catch (Exception unused) {
                                TaoLog.e(TAG, "PackageApp version check error");
                                return;
                            }
                        }
                        str3 = str;
                    }
                    if (this.forceFullNextTime) {
                        str4 = "0";
                        str3 = "0";
                        TaoLog.w(TAG, "force get full package config");
                    }
                    str2 = WVConfigManager.getInstance().getConfigUrl("5", str4, WVConfigUtils.getTargetValue(), str3);
                }
                if ("3".equals(ZCacheConfigManager.getInstance().getzType())) {
                    str2 = WVConfigManager.getInstance().getConfigUrl("5", "0", WVConfigUtils.getTargetValue(), str);
                }
                new HttpConnector().syncConnect(new HttpRequest(str2), new HttpConnectListener<HttpResponse>() {
                    /* JADX WARNING: Code restructure failed: missing block: B:22:0x007c, code lost:
                        r6 = move-exception;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:23:0x007d, code lost:
                        r7 = android.taobao.windvane.packageapp.WVPackageAppConfig.access$000();
                        android.taobao.windvane.util.TaoLog.e(r7, "PackageAppforDebug 总控文件解析异常 fail: " + r6.getMessage());
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:24:0x009b, code lost:
                        if ((r6 instanceof android.taobao.windvane.WindvaneException) != false) goto L_0x009d;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:25:0x009d, code lost:
                        r13.onReceiveValue((android.taobao.windvane.WindvaneException) r6);
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00a5, code lost:
                        r13.onReceiveValue(new android.taobao.windvane.WindvaneException((java.lang.Throwable) r6, android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_APPS_CONFIG_PARSE));
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00b2, code lost:
                        r6 = e;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
                        return;
                     */
                    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
                        return;
                     */
                    /* JADX WARNING: Failed to process nested try/catch */
                    /* JADX WARNING: Removed duplicated region for block: B:22:0x007c A[ExcHandler: Exception (r6v10 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:2:0x0003] */
                    /* JADX WARNING: Removed duplicated region for block: B:30:0x00d6  */
                    /* JADX WARNING: Removed duplicated region for block: B:32:0x00ee  */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void onFinish(android.taobao.windvane.connect.HttpResponse r6, int r7) {
                        /*
                            r5 = this;
                            r7 = 0
                            if (r6 == 0) goto L_0x00d4
                            byte[] r0 = r6.getData()     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            if (r0 == 0) goto L_0x00d4
                            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            if (r0 == 0) goto L_0x0035
                            java.lang.String r0 = android.taobao.windvane.packageapp.WVPackageAppConfig.TAG     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            r1.<init>()     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            java.lang.String r2 = "PackageAppforDebug 下载总控配置文件成功 data:【"
                            r1.append(r2)     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            java.lang.String r2 = new java.lang.String     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            byte[] r3 = r6.getData()     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            r2.<init>(r3)     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            r1.append(r2)     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            java.lang.String r2 = "】"
                            r1.append(r2)     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            java.lang.String r1 = r1.toString()     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            android.taobao.windvane.util.TaoLog.d(r0, r1)     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                        L_0x0035:
                            java.lang.String r0 = new java.lang.String     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            byte[] r6 = r6.getData()     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            java.lang.String r1 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.DEFAULT_ENCODING     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            r0.<init>(r6, r1)     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            java.lang.String r6 = "3"
                            android.taobao.windvane.packageapp.adaptive.ZCacheConfigManager r1 = android.taobao.windvane.packageapp.adaptive.ZCacheConfigManager.getInstance()     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            java.lang.String r1 = r1.getzType()     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            boolean r6 = r6.equals(r1)     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            if (r6 == 0) goto L_0x005e
                            android.taobao.windvane.packageapp.WVPackageAppConfigInterface r6 = android.taobao.windvane.packageapp.WVPackageAppService.getWvPackageAppConfig()     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            android.taobao.windvane.packageapp.WVPackageAppConfig r6 = (android.taobao.windvane.packageapp.WVPackageAppConfig) r6     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            r6.packageCfg = r0     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            android.webkit.ValueCallback r6 = r12     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            r6.onReceiveValue(r7)     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            return
                        L_0x005e:
                            android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r6 = android.taobao.windvane.packageapp.zipapp.utils.ConfigDataUtils.parseGlobalConfig(r0)     // Catch:{ UnsupportedEncodingException -> 0x00b2, Exception -> 0x007c }
                            if (r6 == 0) goto L_0x007a
                            boolean r7 = r6.isAvailableData()     // Catch:{ UnsupportedEncodingException -> 0x0075, Exception -> 0x007c }
                            if (r7 != 0) goto L_0x007a
                            android.webkit.ValueCallback r7 = r13     // Catch:{ UnsupportedEncodingException -> 0x0075, Exception -> 0x007c }
                            android.taobao.windvane.WindvaneException r0 = new android.taobao.windvane.WindvaneException     // Catch:{ UnsupportedEncodingException -> 0x0075, Exception -> 0x007c }
                            r0.<init>()     // Catch:{ UnsupportedEncodingException -> 0x0075, Exception -> 0x007c }
                            r7.onReceiveValue(r0)     // Catch:{ UnsupportedEncodingException -> 0x0075, Exception -> 0x007c }
                            return
                        L_0x0075:
                            r7 = move-exception
                            r4 = r7
                            r7 = r6
                            r6 = r4
                            goto L_0x00b3
                        L_0x007a:
                            r7 = r6
                            goto L_0x00d4
                        L_0x007c:
                            r6 = move-exception
                            java.lang.String r7 = android.taobao.windvane.packageapp.WVPackageAppConfig.TAG
                            java.lang.StringBuilder r0 = new java.lang.StringBuilder
                            r0.<init>()
                            java.lang.String r1 = "PackageAppforDebug 总控文件解析异常 fail: "
                            r0.append(r1)
                            java.lang.String r1 = r6.getMessage()
                            r0.append(r1)
                            java.lang.String r0 = r0.toString()
                            android.taobao.windvane.util.TaoLog.e(r7, r0)
                            boolean r7 = r6 instanceof android.taobao.windvane.WindvaneException
                            if (r7 == 0) goto L_0x00a5
                            android.webkit.ValueCallback r7 = r13
                            android.taobao.windvane.WindvaneException r6 = (android.taobao.windvane.WindvaneException) r6
                            r7.onReceiveValue(r6)
                            goto L_0x00b1
                        L_0x00a5:
                            android.webkit.ValueCallback r7 = r13
                            android.taobao.windvane.WindvaneException r0 = new android.taobao.windvane.WindvaneException
                            int r1 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_APPS_CONFIG_PARSE
                            r0.<init>((java.lang.Throwable) r6, (int) r1)
                            r7.onReceiveValue(r0)
                        L_0x00b1:
                            return
                        L_0x00b2:
                            r6 = move-exception
                        L_0x00b3:
                            java.lang.String r0 = android.taobao.windvane.packageapp.WVPackageAppConfig.TAG
                            java.lang.StringBuilder r1 = new java.lang.StringBuilder
                            r1.<init>()
                            java.lang.String r2 = "PackageAppforDebug 总控文件编码异常 encoding error:【"
                            r1.append(r2)
                            java.lang.String r6 = r6.getMessage()
                            r1.append(r6)
                            java.lang.String r6 = "】"
                            r1.append(r6)
                            java.lang.String r6 = r1.toString()
                            android.taobao.windvane.util.TaoLog.e(r0, r6)
                        L_0x00d4:
                            if (r7 != 0) goto L_0x00ee
                            java.lang.String r6 = android.taobao.windvane.packageapp.WVPackageAppConfig.TAG
                            java.lang.String r7 = "PackageAppforDebug startUpdateApps: GlobalConfig file parse error or invalid!"
                            android.taobao.windvane.util.TaoLog.d(r6, r7)
                            android.webkit.ValueCallback r6 = r13
                            android.taobao.windvane.WindvaneException r7 = new android.taobao.windvane.WindvaneException
                            java.lang.String r0 = "GlobalConfig file parse error or invalid"
                            int r1 = android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode.ERR_APPS_CONFIG_PARSE
                            r7.<init>((java.lang.String) r0, (int) r1)
                            r6.onReceiveValue(r7)
                            return
                        L_0x00ee:
                            android.taobao.windvane.packageapp.WVPackageAppConfig r6 = android.taobao.windvane.packageapp.WVPackageAppConfig.this
                            r0 = 0
                            boolean unused = r6.forceFullNextTime = r0
                            android.webkit.ValueCallback r6 = r12
                            r6.onReceiveValue(r7)
                            return
                        */
                        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.WVPackageAppConfig.AnonymousClass1.onFinish(android.taobao.windvane.connect.HttpResponse, int):void");
                    }

                    public void onError(int i, String str) {
                        valueCallback2.onReceiveValue(new WindvaneException());
                        String access$000 = WVPackageAppConfig.TAG;
                        TaoLog.d(access$000, "update package failed! : " + str);
                        super.onError(i, str);
                    }
                });
                return;
            }
            TaoLog.d(TAG, "PackageAppforDebug 总控更新时机未到(非强制更新或间隔不超过5分钟)");
        }
    }

    public boolean saveLocalConfig(ZipGlobalConfig zipGlobalConfig) {
        config = zipGlobalConfig;
        if (!(zipGlobalConfig == null || zipGlobalConfig.getAppsTable() == null || zipGlobalConfig.getAppsTable().size() <= 0)) {
            try {
                String parseGlobalConfig2String = ZipAppUtils.parseGlobalConfig2String(zipGlobalConfig);
                if (parseGlobalConfig2String == null) {
                    return false;
                }
                if ("3".equals(GlobalConfig.zType)) {
                    this.packageCfg = parseGlobalConfig2String;
                    TaoLog.i("ZCache", "package 3.0");
                }
                return ZipAppFileManager.getInstance().saveGlobalConfig(parseGlobalConfig2String.getBytes(ZipAppConstants.DEFAULT_ENCODING), false);
            } catch (UnsupportedEncodingException unused) {
                TaoLog.e(TAG, "PackageAppforDebug fail to save global config to disk");
            }
        }
        return false;
    }
}
