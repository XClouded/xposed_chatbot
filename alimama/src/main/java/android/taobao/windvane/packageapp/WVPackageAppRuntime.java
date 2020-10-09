package android.taobao.windvane.packageapp;

import android.annotation.SuppressLint;
import android.os.Build;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.monitor.WVPackageMonitorInterface;
import android.taobao.windvane.packageapp.adaptive.ZCacheConfigManager;
import android.taobao.windvane.packageapp.cleanup.InfoSnippet;
import android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.ZCacheResourceResponse;
import android.taobao.windvane.packageapp.zipapp.ZipPrefixesManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipUpdateInfoEnum;
import android.taobao.windvane.packageapp.zipapp.data.ZipUpdateTypeEnum;
import android.taobao.windvane.packageapp.zipapp.utils.AppResInfo;
import android.taobao.windvane.packageapp.zipapp.utils.ComboInfo;
import android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.webview.WVWrapWebResourceResponse;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import com.alimama.unionwl.utils.CommonUtils;
import com.taobao.weex.el.parse.Operators;
import com.taobao.zcache.ZCacheManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WVPackageAppRuntime {
    public static final String TAG = "PackageApp-Runtime";

    public interface ZCacheResourceCallback {
        void callback(ZCacheResourceResponse zCacheResourceResponse);
    }

    public static WVWrapWebResourceResponse getWrapResourceResponse(String str, ZipGlobalConfig.CacheFileData cacheFileData) {
        WebResourceResponse zcacheResourceResponse = getZcacheResourceResponse(str, cacheFileData);
        if (zcacheResourceResponse == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return new WVWrapWebResourceResponse(zcacheResourceResponse.getMimeType(), zcacheResourceResponse.getEncoding(), zcacheResourceResponse.getData(), zcacheResourceResponse.getResponseHeaders());
        }
        return new WVWrapWebResourceResponse(zcacheResourceResponse.getMimeType(), zcacheResourceResponse.getEncoding(), zcacheResourceResponse.getData(), (Map<String, String>) null);
    }

    @SuppressLint({"NewApi"})
    public static WebResourceResponse getZcacheResourceResponse(String str, ZipGlobalConfig.CacheFileData cacheFileData) {
        String str2;
        String str3;
        String str4;
        String str5;
        long j;
        String str6;
        String str7 = str;
        ZipGlobalConfig.CacheFileData cacheFileData2 = cacheFileData;
        if (cacheFileData2 == null) {
            return null;
        }
        try {
            long currentTimeMillis = System.currentTimeMillis();
            ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(cacheFileData2.appName);
            String isAvailable = isAvailable(str7, appInfo);
            if (appInfo != null) {
                if (isAvailable == null) {
                    byte[] read = FileAccesser.read(cacheFileData2.path);
                    String mimeTypeExtra = WVUrlUtil.getMimeTypeExtra(str);
                    long currentTimeMillis2 = System.currentTimeMillis();
                    if (read == null || read.length <= 0) {
                        cacheFileData2.errorCode = "407";
                        if (-1 != str7.indexOf("??")) {
                            return null;
                        }
                        if (WVMonitorService.getPackageMonitorInterface() != null) {
                            if (WVZipSecurityManager.getInstance().getAppResInfo(appInfo, str7) != null) {
                                WVPackageAppCleanup.getInstance().getInfoMap().get(appInfo.name).needReinstall = true;
                                WVPackageMonitorInterface packageMonitorInterface = WVMonitorService.getPackageMonitorInterface();
                                if (appInfo == null) {
                                    str5 = "unknown-0";
                                } else {
                                    str5 = appInfo.name + "-0";
                                }
                                packageMonitorInterface.commitPackageVisitError(str5, str7, "12");
                            } else {
                                WVPackageMonitorInterface packageMonitorInterface2 = WVMonitorService.getPackageMonitorInterface();
                                if (appInfo == null) {
                                    str4 = "unknown-0";
                                } else {
                                    str4 = appInfo.name + "-0";
                                }
                                packageMonitorInterface2.commitPackageVisitError(str4, str7, "12");
                            }
                        }
                        TaoLog.e(TAG, "ZcacheforDebug :命中url 但本地文件读取失败：文件流为空[" + str7 + Operators.ARRAY_END_STR);
                        return null;
                    }
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(read);
                    if (!needCheckSecurity(appInfo.name)) {
                        j = 0;
                    } else if (!WVZipSecurityManager.getInstance().isFileSecrity(str7, read, cacheFileData2.path, appInfo.name)) {
                        if (WVMonitorService.getPackageMonitorInterface() != null) {
                            WVPackageMonitorInterface packageMonitorInterface3 = WVMonitorService.getPackageMonitorInterface();
                            if (appInfo == null) {
                                str6 = "unknown-0";
                            } else {
                                str6 = appInfo.name + "-0";
                            }
                            packageMonitorInterface3.commitPackageVisitError(str6, str7, "10");
                        }
                        return null;
                    } else {
                        j = System.currentTimeMillis();
                    }
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(TAG, "ZcacheforDebug :命中[" + str7 + Operators.ARRAY_END_STR);
                    }
                    long j2 = currentTimeMillis2 - currentTimeMillis;
                    long j3 = j == 0 ? 0 : j - currentTimeMillis2;
                    if (WVMonitorService.getPackageMonitorInterface() != null) {
                        WVMonitorService.getPackageMonitorInterface().commitPackageVisitInfo(appInfo.name, j == 0 ? "false" : "true", j2 + j3, 0, j2, j3, appInfo.installedSeq);
                        WVMonitorService.getPackageMonitorInterface().commitPackageVisitSuccess(appInfo.name, appInfo.installedSeq);
                    }
                    WebResourceResponse webResourceResponse = new WebResourceResponse(mimeTypeExtra, ZipAppConstants.DEFAULT_ENCODING, byteArrayInputStream);
                    AppResInfo appResInfo = WVZipSecurityManager.getInstance().getAppResInfo(appInfo, str7);
                    if (!(appResInfo == null || appResInfo.mHeaders == null || Build.VERSION.SDK_INT < 21)) {
                        try {
                            webResourceResponse.setResponseHeaders(ZipAppUtils.toMap(appResInfo.mHeaders));
                        } catch (Exception e) {
                            TaoLog.w(TAG, "JSON to Map error ： " + e.getMessage());
                        }
                    }
                    return webResourceResponse;
                }
            }
            if (WVMonitorService.getPackageMonitorInterface() != null) {
                WVPackageMonitorInterface packageMonitorInterface4 = WVMonitorService.getPackageMonitorInterface();
                if (appInfo == null) {
                    str3 = "unknown-0";
                } else {
                    str3 = appInfo.name + "-0";
                }
                packageMonitorInterface4.commitPackageVisitError(str3, str7, isAvailable);
            }
            cacheFileData2.errorCode = ZipAppUtils.getErrorCode(isAvailable);
            if (appInfo != null) {
                return null;
            }
            cacheFileData2.errorCode = "401";
            return null;
        } catch (Exception e2) {
            if (WVMonitorService.getPackageMonitorInterface() != null) {
                WVPackageMonitorInterface packageMonitorInterface5 = WVMonitorService.getPackageMonitorInterface();
                if (cacheFileData2 == null) {
                    str2 = "unknown-0";
                } else {
                    str2 = cacheFileData2.appName + "-" + cacheFileData2.seq;
                }
                packageMonitorInterface5.commitPackageVisitError(str2, e2.getMessage(), "9");
            }
            TaoLog.e(TAG, "ZcacheforDebug 入口:访问本地zip资源失败 [" + str7 + Operators.ARRAY_END_STR + e2.getMessage());
            return null;
        }
    }

    private static String getComboUrl(String str, String str2) {
        int i = 0;
        while ('/' == str2.charAt(i)) {
            i++;
        }
        if (i != 0) {
            str2 = str2.substring(i);
        }
        return str + "/" + str2;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: IfRegionVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Don't wrap MOVE or CONST insns: 0x02a3: MOVE  (r1v9 android.taobao.windvane.packageapp.zipapp.utils.ComboInfo) = 
          (r34v0 android.taobao.windvane.packageapp.zipapp.utils.ComboInfo)
        
        	at jadx.core.dex.instructions.args.InsnArg.wrapArg(InsnArg.java:164)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.assignInline(CodeShrinkVisitor.java:133)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.checkInline(CodeShrinkVisitor.java:118)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkBlock(CodeShrinkVisitor.java:65)
        	at jadx.core.dex.visitors.shrink.CodeShrinkVisitor.shrinkMethod(CodeShrinkVisitor.java:43)
        	at jadx.core.dex.visitors.regions.TernaryMod.makeTernaryInsn(TernaryMod.java:122)
        	at jadx.core.dex.visitors.regions.TernaryMod.visitRegion(TernaryMod.java:34)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:73)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterativeStepInternal(DepthRegionTraversal.java:78)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseIterative(DepthRegionTraversal.java:27)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.visit(IfRegionVisitor.java:31)
        */
    @android.annotation.SuppressLint({"NewApi"})
    public static android.webkit.WebResourceResponse makeComboRes(java.lang.String r33, android.taobao.windvane.packageapp.zipapp.utils.ComboInfo r34, java.util.Map<java.lang.String, java.lang.String> r35) {
        /*
            r7 = r33
            r8 = r34
            long r1 = java.lang.System.currentTimeMillis()
            android.taobao.windvane.config.WVCommonConfigData r0 = android.taobao.windvane.config.WVCommonConfig.commonConfig
            boolean r0 = r0.isOpenCombo
            r9 = 0
            if (r0 == 0) goto L_0x0307
            if (r7 == 0) goto L_0x0307
            java.lang.String r0 = "??"
            int r0 = r7.indexOf(r0)
            r3 = -1
            if (r0 != r3) goto L_0x001c
            goto L_0x0307
        L_0x001c:
            java.lang.String[] r3 = android.taobao.windvane.util.WVUrlUtil.parseCombo(r33)
            if (r3 != 0) goto L_0x0023
            return r9
        L_0x0023:
            java.lang.String r0 = "??"
            int r0 = r7.indexOf(r0)
        L_0x0029:
            r4 = 47
            int r5 = r0 + -1
            char r5 = r7.charAt(r5)
            if (r4 != r5) goto L_0x0036
            int r0 = r0 + -1
            goto L_0x0029
        L_0x0036:
            r4 = 0
            java.lang.String r5 = r7.substring(r4, r0)
            java.io.ByteArrayOutputStream r6 = new java.io.ByteArrayOutputStream
            r6.<init>()
            int r0 = r3.length
            java.lang.String[] r10 = new java.lang.String[r0]
            java.util.HashSet r11 = new java.util.HashSet
            r11.<init>()
            java.lang.String r0 = "PackageApp-Runtime"
            java.lang.String r12 = "WVPackageAppRuntime.makeComboRes custom cache start"
            android.taobao.windvane.util.TaoLog.d(r0, r12)
            java.util.HashMap r12 = new java.util.HashMap
            r12.<init>()
            android.taobao.windvane.cache.WVCustomCacheManager r0 = android.taobao.windvane.cache.WVCustomCacheManager.getInstance()
            r13 = r35
            java.io.InputStream r13 = r0.getCacheResource(r7, r3, r12, r13)
            java.lang.String r0 = "PackageApp-Runtime"
            java.lang.String r14 = "WVPackageAppRuntime.makeComboRes custom cache end"
            android.taobao.windvane.util.TaoLog.d(r0, r14)
            if (r13 == 0) goto L_0x0069
            r15 = 1
            goto L_0x006a
        L_0x0069:
            r15 = 0
        L_0x006a:
            if (r15 == 0) goto L_0x0073
            java.lang.String r0 = "PackageApp-Runtime"
            java.lang.String r4 = "WVPackageAppRuntime.makeComboRes get custom cache resource"
            android.taobao.windvane.util.TaoLog.d(r0, r4)
        L_0x0073:
            java.lang.String r0 = "PackageApp-Runtime"
            java.lang.String r4 = "WVPackageAppRuntime.makeComboRes check combo urls start"
            android.taobao.windvane.util.TaoLog.d(r0, r4)
            r14 = r9
            r4 = 0
        L_0x007c:
            if (r15 != 0) goto L_0x015c
            int r0 = r3.length
            if (r4 >= r0) goto L_0x015c
            r0 = r3[r4]
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x008a
            return r9
        L_0x008a:
            r0 = r3[r4]
            java.lang.String r0 = getComboUrl(r5, r0)
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r16 = getAppInfoByUrl(r0)
            if (r16 != 0) goto L_0x00ad
            android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r9 = android.taobao.windvane.packageapp.zipapp.ConfigManager.getLocGlobalConfig()
            android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig$CacheFileData r9 = r9.isZcacheUrl(r0)
            if (r9 == 0) goto L_0x00ad
            r17 = r13
            android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r13 = android.taobao.windvane.packageapp.zipapp.ConfigManager.getLocGlobalConfig()
            java.lang.String r9 = r9.appName
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r16 = r13.getAppInfo(r9)
            goto L_0x00af
        L_0x00ad:
            r17 = r13
        L_0x00af:
            r9 = r16
            if (r9 == 0) goto L_0x0131
            java.lang.String r13 = isAvailable(r0, r9)
            if (r13 == 0) goto L_0x00bb
            goto L_0x0131
        L_0x00bb:
            java.lang.String r13 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils.getLocPathByUrl(r0)
            if (r13 != 0) goto L_0x00ec
            boolean r1 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r1 == 0) goto L_0x00ea
            java.lang.String r1 = "PackageApp-Runtime"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "ZcacheforDebug 入口:combo未命中["
            r2.append(r3)
            r2.append(r7)
            java.lang.String r3 = "] 含非zcache 资源:["
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = "]"
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            android.taobao.windvane.util.TaoLog.d(r1, r0)
        L_0x00ea:
            r1 = 0
            return r1
        L_0x00ec:
            if (r14 != 0) goto L_0x0101
            android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager r8 = android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager.getInstance()
            android.taobao.windvane.packageapp.zipapp.utils.AppResInfo r0 = r8.getAppResInfo(r9, r0)
            org.json.JSONObject r8 = r0.mHeaders
            if (r8 == 0) goto L_0x0101
            org.json.JSONObject r0 = r0.mHeaders     // Catch:{ Exception -> 0x0104 }
            java.util.Map r0 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils.toMap(r0)     // Catch:{ Exception -> 0x0104 }
            r14 = r0
        L_0x0101:
            r18 = r1
            goto L_0x0121
        L_0x0104:
            r0 = move-exception
            java.lang.String r8 = "PackageApp-Runtime"
            r18 = r1
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "JSON to Map error ： "
            r1.append(r2)
            java.lang.String r0 = r0.getMessage()
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            android.taobao.windvane.util.TaoLog.w(r8, r0)
        L_0x0121:
            r11.add(r9)
            r10[r4] = r13
            int r4 = r4 + 1
            r13 = r17
            r1 = r18
            r8 = r34
            r9 = 0
            goto L_0x007c
        L_0x0131:
            boolean r1 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r1 == 0) goto L_0x015a
            java.lang.String r1 = "PackageApp-Runtime"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "ZcacheforDebug 入口:combo未命中["
            r2.append(r3)
            r2.append(r7)
            java.lang.String r3 = "] 含非zcache 资源:["
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = "]"
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            android.taobao.windvane.util.TaoLog.d(r1, r0)
        L_0x015a:
            r1 = 0
            return r1
        L_0x015c:
            r18 = r1
            r17 = r13
            java.lang.String r0 = "PackageApp-Runtime"
            java.lang.String r1 = "WVPackageAppRuntime.makeComboRes check combo urls end"
            android.taobao.windvane.util.TaoLog.d(r0, r1)
            if (r14 == 0) goto L_0x0173
            r12.putAll(r14)
            java.lang.String r0 = "Access-Control-Allow-Origin"
            java.lang.String r1 = "*"
            r12.put(r0, r1)
        L_0x0173:
            long r0 = java.lang.System.currentTimeMillis()
            java.lang.String r2 = "PackageApp-Runtime"
            java.lang.String r4 = "WVPackageAppRuntime.makeComboRes zcache combo start"
            android.taobao.windvane.util.TaoLog.d(r2, r4)
            r2 = 0
        L_0x017f:
            if (r15 != 0) goto L_0x01f1
            int r4 = r10.length
            if (r2 >= r4) goto L_0x01f1
            r4 = r10[r2]
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 == 0) goto L_0x018d
            goto L_0x01a0
        L_0x018d:
            java.io.File r4 = new java.io.File
            r8 = r10[r2]
            r4.<init>(r8)
            byte[] r4 = android.taobao.windvane.file.FileAccesser.read((java.io.File) r4)
            if (r4 == 0) goto L_0x01a5
            int r8 = r4.length
            if (r8 <= 0) goto L_0x01a5
            r6.write(r4)     // Catch:{ Exception -> 0x01a3 }
        L_0x01a0:
            int r2 = r2 + 1
            goto L_0x017f
        L_0x01a3:
            r1 = 0
            return r1
        L_0x01a5:
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()
            if (r0 == 0) goto L_0x01ef
            r0 = r3[r2]
            java.lang.String r0 = getComboUrl(r5, r0)
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r1 = getAppInfoByUrl(r0)
            android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager r2 = android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager.getInstance()
            android.taobao.windvane.packageapp.zipapp.utils.AppResInfo r2 = r2.getAppResInfo(r1, r0)
            if (r2 == 0) goto L_0x01e1
            android.taobao.windvane.monitor.WVPackageMonitorInterface r2 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()
            if (r1 != 0) goto L_0x01c8
            java.lang.String r1 = "unknown-0"
            goto L_0x01db
        L_0x01c8:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r1 = r1.name
            r3.append(r1)
            java.lang.String r1 = "-0"
            r3.append(r1)
            java.lang.String r1 = r3.toString()
        L_0x01db:
            java.lang.String r3 = "15"
            r2.commitPackageVisitError(r1, r0, r3)
            goto L_0x01ef
        L_0x01e1:
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()
            if (r1 != 0) goto L_0x01ea
            java.lang.String r1 = "unknown"
            goto L_0x01ec
        L_0x01ea:
            java.lang.String r1 = r1.name
        L_0x01ec:
            r0.commitPackageWarning(r1, r7)
        L_0x01ef:
            r1 = 0
            return r1
        L_0x01f1:
            java.lang.String r2 = "PackageApp-Runtime"
            java.lang.String r3 = "WVPackageAppRuntime.makeComboRes zcache combo end"
            android.taobao.windvane.util.TaoLog.d(r2, r3)
            java.lang.String r2 = "PackageApp-Runtime"
            java.lang.String r3 = "WVPackageAppRuntime.makeComboRes response start"
            android.taobao.windvane.util.TaoLog.d(r2, r3)
            if (r15 == 0) goto L_0x0204
            r13 = r17
            goto L_0x020d
        L_0x0204:
            byte[] r2 = r6.toByteArray()     // Catch:{ Exception -> 0x02c1 }
            java.io.ByteArrayInputStream r13 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x02c1 }
            r13.<init>(r2)     // Catch:{ Exception -> 0x02c1 }
        L_0x020d:
            java.lang.String r2 = android.taobao.windvane.util.WVUrlUtil.getMimeTypeExtra(r33)     // Catch:{ Exception -> 0x02c1 }
            boolean r3 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x02c1 }
            if (r3 == 0) goto L_0x0232
            java.lang.String r3 = "PackageApp-Runtime"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x02c1 }
            r4.<init>()     // Catch:{ Exception -> 0x02c1 }
            java.lang.String r5 = "ZcacheforDebug :命中combo["
            r4.append(r5)     // Catch:{ Exception -> 0x02c1 }
            r4.append(r7)     // Catch:{ Exception -> 0x02c1 }
            java.lang.String r5 = "]"
            r4.append(r5)     // Catch:{ Exception -> 0x02c1 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x02c1 }
            android.taobao.windvane.util.TaoLog.d(r3, r4)     // Catch:{ Exception -> 0x02c1 }
        L_0x0232:
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x02c1 }
            r5 = 0
            long r27 = r3 - r0
            long r25 = r0 - r18
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x02c1 }
            if (r0 == 0) goto L_0x0271
            java.util.Iterator r0 = r11.iterator()     // Catch:{ Exception -> 0x02c1 }
        L_0x0245:
            boolean r1 = r0.hasNext()     // Catch:{ Exception -> 0x02c1 }
            if (r1 == 0) goto L_0x025f
            java.lang.Object r1 = r0.next()     // Catch:{ Exception -> 0x02c1 }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r1 = (android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo) r1     // Catch:{ Exception -> 0x02c1 }
            if (r1 == 0) goto L_0x0245
            android.taobao.windvane.monitor.WVPackageMonitorInterface r3 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x02c1 }
            java.lang.String r4 = r1.name     // Catch:{ Exception -> 0x02c1 }
            long r5 = r1.installedSeq     // Catch:{ Exception -> 0x02c1 }
            r3.commitPackageVisitSuccess(r4, r5)     // Catch:{ Exception -> 0x02c1 }
            goto L_0x0245
        L_0x025f:
            android.taobao.windvane.monitor.WVPackageMonitorInterface r20 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x02c1 }
            java.lang.String r21 = "COMBO"
            java.lang.String r22 = "false"
            r0 = 0
            long r23 = r27 + r25
            r29 = 0
            r31 = 1
            r20.commitPackageVisitInfo(r21, r22, r23, r25, r27, r29, r31)     // Catch:{ Exception -> 0x02c1 }
        L_0x0271:
            android.webkit.WebResourceResponse r0 = new android.webkit.WebResourceResponse     // Catch:{ Exception -> 0x02c1 }
            java.lang.String r1 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.DEFAULT_ENCODING     // Catch:{ Exception -> 0x02c1 }
            r0.<init>(r2, r1, r13)     // Catch:{ Exception -> 0x02c1 }
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()     // Catch:{ Exception -> 0x02c1 }
            if (r1 == 0) goto L_0x028d
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()     // Catch:{ Exception -> 0x02c1 }
            r3 = 200(0xc8, float:2.8E-43)
            r4 = 8
            r5 = 0
            r6 = 0
            r2 = r33
            r1.didGetResourceStatusCode(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x02c1 }
        L_0x028d:
            boolean r1 = r12.isEmpty()     // Catch:{ Exception -> 0x02c1 }
            if (r1 != 0) goto L_0x029c
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x02c1 }
            r2 = 21
            if (r1 < r2) goto L_0x029c
            r0.setResponseHeaders(r12)     // Catch:{ Exception -> 0x02c1 }
        L_0x029c:
            int r1 = r11.size()     // Catch:{ Exception -> 0x02c1 }
            r2 = 1
            if (r1 != r2) goto L_0x02b9
            r1 = r34
            if (r1 == 0) goto L_0x02b9
            java.util.Iterator r2 = r11.iterator()     // Catch:{ Exception -> 0x02c1 }
            java.lang.Object r2 = r2.next()     // Catch:{ Exception -> 0x02c1 }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r2 = (android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo) r2     // Catch:{ Exception -> 0x02c1 }
            java.lang.String r3 = r2.name     // Catch:{ Exception -> 0x02c1 }
            r1.appName = r3     // Catch:{ Exception -> 0x02c1 }
            long r2 = r2.s     // Catch:{ Exception -> 0x02c1 }
            r1.seq = r2     // Catch:{ Exception -> 0x02c1 }
        L_0x02b9:
            java.lang.String r1 = "PackageApp-Runtime"
            java.lang.String r2 = "WVPackageAppRuntime.makeComboRes response end"
            android.taobao.windvane.util.TaoLog.d(r1, r2)     // Catch:{ Exception -> 0x02c1 }
            return r0
        L_0x02c1:
            r0 = move-exception
            java.lang.String r1 = "PackageApp-Runtime"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "ZcacheforDebug 入口:访问本地combo zip资源失败 ["
            r2.append(r3)
            r2.append(r7)
            java.lang.String r3 = "]"
            r2.append(r3)
            java.lang.String r0 = r0.getMessage()
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            android.taobao.windvane.util.TaoLog.e(r1, r0)
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r0 == 0) goto L_0x0305
            java.lang.String r0 = "PackageApp-Runtime"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "ZcacheforDebug 入口:combo未命中["
            r1.append(r2)
            r1.append(r7)
            java.lang.String r2 = "]"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.taobao.windvane.util.TaoLog.d(r0, r1)
        L_0x0305:
            r1 = 0
            return r1
        L_0x0307:
            r1 = r9
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.WVPackageAppRuntime.makeComboRes(java.lang.String, android.taobao.windvane.packageapp.zipapp.utils.ComboInfo, java.util.Map):android.webkit.WebResourceResponse");
    }

    public static ZipAppInfo getAppInfoByUrl(String str) {
        String zipAppName = ZipPrefixesManager.getInstance().getZipAppName(str);
        if (zipAppName == null) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "PackageappforDebug :appName==null[" + str + Operators.ARRAY_END_STR);
            }
            return null;
        }
        try {
            ZipAppInfo appInfo = ConfigManager.getLocGlobalConfig().getAppInfo(zipAppName);
            if (appInfo != null) {
                return appInfo;
            }
            if (WVCommonConfig.commonConfig.isAutoRegisterApp) {
                ZipAppInfo zipAppInfo = new ZipAppInfo();
                zipAppInfo.name = zipAppName;
                zipAppInfo.isOptional = true;
                ConfigManager.updateGlobalConfig(zipAppInfo, (String) null, false);
                WVCustomPackageAppConfig.getInstance().resetConfig();
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "PackageappforDebug :autoRegist [" + zipAppName + Operators.ARRAY_END_STR);
                }
            }
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "PackageappforDebug :appInfo==null[" + str + Operators.ARRAY_END_STR);
            }
            return null;
        } catch (Exception e) {
            TaoLog.e(TAG, "PackageappforDebug 通过url获取APPinfo异常ul: [" + str + "  appName:" + zipAppName + "],errorMag:" + e.getMessage());
            return null;
        }
    }

    public static boolean isLocalVisit(String str) {
        String zipAppName = ZipPrefixesManager.getInstance().getZipAppName(str);
        if (zipAppName == null) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "PackageappforDebug :appName==null[" + str + Operators.ARRAY_END_STR);
            }
            return false;
        } else if (isAvailable(str, ConfigManager.getLocGlobalConfig().getAppInfo(zipAppName)) == null) {
            return true;
        } else {
            return false;
        }
    }

    public static String isAvailable(String str, ZipAppInfo zipAppInfo) {
        if (zipAppInfo == null) {
            return "20";
        }
        if (zipAppInfo.status == ZipAppConstants.ZIP_REMOVED) {
            InfoSnippet infoSnippet = WVPackageAppCleanup.getInstance().getInfoMap().get(zipAppInfo.name);
            if (infoSnippet == null || infoSnippet.count < 1.0d) {
                return "24";
            }
            zipAppInfo.status = ZipAppConstants.ZIP_NEWEST;
            return "24";
        } else if (zipAppInfo.getInfo() == ZipUpdateInfoEnum.ZIP_UPDATE_INFO_DELETE) {
            return WVPackageMonitorInterface.ZIP_REMOVED_BY_CONFIG;
        } else {
            if (zipAppInfo.installedSeq == 0) {
                return zipAppInfo.s == 0 ? WVPackageMonitorInterface.ZIP_CONFIG_EMPTY_FAILED : "20";
            }
            if (WVCommonConfig.commonConfig.packageAppStatus == 0) {
                return "23";
            }
            if (zipAppInfo.getUpdateType() == ZipUpdateTypeEnum.ZIP_APP_TYPE_ONLINE) {
                return "22";
            }
            if (zipAppInfo.getUpdateType() != ZipUpdateTypeEnum.ZIP_APP_TYPE_FORCE || zipAppInfo.installedSeq == zipAppInfo.s) {
                return null;
            }
            return "21";
        }
    }

    public static void registerApp(WVCallBackContext wVCallBackContext, String str) {
        WVResult wVResult = new WVResult();
        try {
            String optString = new JSONObject(str).optString("appName");
            ZipAppInfo zipAppInfo = new ZipAppInfo();
            zipAppInfo.name = optString;
            zipAppInfo.isOptional = true;
            ConfigManager.updateGlobalConfig(zipAppInfo, (String) null, false);
            wVCallBackContext.success();
        } catch (JSONException unused) {
            TaoLog.e(TAG, "param parse to JSON error, param=" + str);
            wVResult.setResult("HY_PARAM_ERR");
            wVCallBackContext.error(wVResult);
        }
    }

    public static boolean canSupportPackageApp(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (str.contains("http")) {
                str = str.replace("https", "http");
            } else {
                str = CommonUtils.HTTP_PRE + str;
            }
        }
        String locPathByUrl = ZipAppUtils.getLocPathByUrl(str);
        if (!TextUtils.isEmpty(locPathByUrl)) {
            return new File(locPathByUrl).exists();
        }
        return false;
    }

    public static WVWrapWebResourceResponse getWrapResourceResponse(String str, ZipAppInfo zipAppInfo) {
        WebResourceResponse resourceResponse = getResourceResponse(str, zipAppInfo);
        if (resourceResponse == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return new WVWrapWebResourceResponse(resourceResponse.getMimeType(), resourceResponse.getEncoding(), resourceResponse.getData(), resourceResponse.getResponseHeaders());
        }
        return new WVWrapWebResourceResponse(resourceResponse.getMimeType(), resourceResponse.getEncoding(), resourceResponse.getData(), (Map<String, String>) null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:93:0x0227  */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.webkit.WebResourceResponse getResourceResponse(java.lang.String r29, android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r30) {
        /*
            r1 = r30
            r2 = 0
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x021e }
            java.lang.String r5 = android.taobao.windvane.util.WVUrlUtil.removeQueryParam(r29)     // Catch:{ Exception -> 0x021e }
            java.lang.String r0 = isAvailable(r5, r1)     // Catch:{ Exception -> 0x021c }
            if (r1 == 0) goto L_0x0023
            java.lang.String r6 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils.getErrorCode(r0)     // Catch:{ Exception -> 0x021c }
            r1.errorCode = r6     // Catch:{ Exception -> 0x021c }
            java.lang.String r6 = r1.mappingUrl     // Catch:{ Exception -> 0x021c }
            boolean r6 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x021c }
            if (r6 == 0) goto L_0x0023
            java.lang.String r6 = "403"
            r1.errorCode = r6     // Catch:{ Exception -> 0x021c }
        L_0x0023:
            if (r1 == 0) goto L_0x01f6
            if (r0 != 0) goto L_0x01f6
            int r0 = r1.status     // Catch:{ Exception -> 0x021c }
            int r6 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.ZIP_REMOVED     // Catch:{ Exception -> 0x021c }
            if (r0 == r6) goto L_0x026e
            java.lang.String r0 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils.parseUrlSuffix(r1, r5)     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager r6 = android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager.getInstance()     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.packageapp.zipapp.utils.AppResInfo r6 = r6.getAppResInfo(r1, r5)     // Catch:{ Exception -> 0x021c }
            if (r6 != 0) goto L_0x003f
            java.lang.String r6 = "-1"
            r1.errorCode = r6     // Catch:{ Exception -> 0x021c }
        L_0x003f:
            if (r0 == 0) goto L_0x026e
            android.taobao.windvane.packageapp.ZipAppFileManager r6 = android.taobao.windvane.packageapp.ZipAppFileManager.getInstance()     // Catch:{ Exception -> 0x021c }
            r7 = 0
            byte[] r0 = r6.readZipAppResByte(r1, r0, r7)     // Catch:{ Exception -> 0x021c }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x021c }
            java.lang.String r6 = android.taobao.windvane.util.WVUrlUtil.getMimeType(r5)     // Catch:{ Exception -> 0x021c }
            if (r0 == 0) goto L_0x0145
            int r10 = r0.length     // Catch:{ Exception -> 0x021c }
            if (r10 <= 0) goto L_0x0145
            java.io.ByteArrayInputStream r10 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x021c }
            r10.<init>(r0)     // Catch:{ Exception -> 0x021c }
            java.lang.String r11 = r1.name     // Catch:{ Exception -> 0x021c }
            boolean r11 = needCheckSecurity(r11)     // Catch:{ Exception -> 0x021c }
            r12 = 0
            if (r11 == 0) goto L_0x00a9
            android.taobao.windvane.packageapp.ZipAppFileManager r11 = android.taobao.windvane.packageapp.ZipAppFileManager.getInstance()     // Catch:{ Exception -> 0x021c }
            java.lang.String r14 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.APP_RES_NAME     // Catch:{ Exception -> 0x021c }
            java.lang.String r7 = r11.getZipResAbsolutePath(r1, r14, r7)     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager r11 = android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager.getInstance()     // Catch:{ Exception -> 0x021c }
            java.lang.String r14 = r1.name     // Catch:{ Exception -> 0x021c }
            boolean r0 = r11.isFileSecrity(r5, r0, r7, r14)     // Catch:{ Exception -> 0x021c }
            if (r0 != 0) goto L_0x00a4
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x021c }
            if (r0 == 0) goto L_0x00a3
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x021c }
            if (r1 != 0) goto L_0x008b
            java.lang.String r3 = "unknown-0"
            goto L_0x009e
        L_0x008b:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x021c }
            r3.<init>()     // Catch:{ Exception -> 0x021c }
            java.lang.String r4 = r1.name     // Catch:{ Exception -> 0x021c }
            r3.append(r4)     // Catch:{ Exception -> 0x021c }
            java.lang.String r4 = "-0"
            r3.append(r4)     // Catch:{ Exception -> 0x021c }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x021c }
        L_0x009e:
            java.lang.String r4 = "10"
            r0.commitPackageVisitError(r3, r5, r4)     // Catch:{ Exception -> 0x021c }
        L_0x00a3:
            return r2
        L_0x00a4:
            long r14 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x021c }
            goto L_0x00aa
        L_0x00a9:
            r14 = r12
        L_0x00aa:
            java.lang.String r0 = "PackageApp-Runtime"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x021c }
            r7.<init>()     // Catch:{ Exception -> 0x021c }
            java.lang.String r11 = "PackageappforDebug  入口:命中["
            r7.append(r11)     // Catch:{ Exception -> 0x021c }
            r7.append(r5)     // Catch:{ Exception -> 0x021c }
            java.lang.String r11 = "]"
            r7.append(r11)     // Catch:{ Exception -> 0x021c }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.util.TaoLog.e(r0, r7)     // Catch:{ Exception -> 0x021c }
            r0 = 0
            long r23 = r8 - r3
            int r0 = (r14 > r12 ? 1 : (r14 == r12 ? 0 : -1))
            if (r0 != 0) goto L_0x00cf
            r25 = r12
            goto L_0x00d4
        L_0x00cf:
            r0 = 0
            long r3 = r14 - r8
            r25 = r3
        L_0x00d4:
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x021c }
            if (r0 == 0) goto L_0x0104
            android.taobao.windvane.monitor.WVPackageMonitorInterface r16 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x021c }
            java.lang.String r0 = r1.name     // Catch:{ Exception -> 0x021c }
            int r3 = (r14 > r12 ? 1 : (r14 == r12 ? 0 : -1))
            if (r3 != 0) goto L_0x00e7
            java.lang.String r3 = "false"
            goto L_0x00e9
        L_0x00e7:
            java.lang.String r3 = "true"
        L_0x00e9:
            r18 = r3
            r3 = 0
            long r19 = r23 + r25
            r21 = 0
            long r3 = r1.installedSeq     // Catch:{ Exception -> 0x021c }
            r17 = r0
            r27 = r3
            r16.commitPackageVisitInfo(r17, r18, r19, r21, r23, r25, r27)     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x021c }
            java.lang.String r3 = r1.name     // Catch:{ Exception -> 0x021c }
            long r7 = r1.installedSeq     // Catch:{ Exception -> 0x021c }
            r0.commitPackageVisitSuccess(r3, r7)     // Catch:{ Exception -> 0x021c }
        L_0x0104:
            android.webkit.WebResourceResponse r3 = new android.webkit.WebResourceResponse     // Catch:{ Exception -> 0x021c }
            java.lang.String r0 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.DEFAULT_ENCODING     // Catch:{ Exception -> 0x021c }
            r3.<init>(r6, r0, r10)     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager r0 = android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager.getInstance()     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.packageapp.zipapp.utils.AppResInfo r0 = r0.getAppResInfo(r1, r5)     // Catch:{ Exception -> 0x021c }
            if (r0 == 0) goto L_0x0144
            org.json.JSONObject r4 = r0.mHeaders     // Catch:{ Exception -> 0x021c }
            if (r4 == 0) goto L_0x0144
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ Exception -> 0x021c }
            r6 = 21
            if (r4 < r6) goto L_0x0144
            org.json.JSONObject r0 = r0.mHeaders     // Catch:{ Exception -> 0x0129 }
            java.util.Map r0 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils.toMap(r0)     // Catch:{ Exception -> 0x0129 }
            r3.setResponseHeaders(r0)     // Catch:{ Exception -> 0x0129 }
            goto L_0x0144
        L_0x0129:
            r0 = move-exception
            java.lang.String r4 = "PackageApp-Runtime"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x021c }
            r6.<init>()     // Catch:{ Exception -> 0x021c }
            java.lang.String r7 = "JSON to Map error ： "
            r6.append(r7)     // Catch:{ Exception -> 0x021c }
            java.lang.String r0 = r0.getMessage()     // Catch:{ Exception -> 0x021c }
            r6.append(r0)     // Catch:{ Exception -> 0x021c }
            java.lang.String r0 = r6.toString()     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.util.TaoLog.w(r4, r0)     // Catch:{ Exception -> 0x021c }
        L_0x0144:
            return r3
        L_0x0145:
            java.lang.String r0 = "407"
            r1.errorCode = r0     // Catch:{ Exception -> 0x021c }
            java.lang.String r0 = "??"
            int r0 = r5.indexOf(r0)     // Catch:{ Exception -> 0x021c }
            r3 = -1
            if (r3 != r0) goto L_0x026e
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x021c }
            if (r0 == 0) goto L_0x026e
            android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup r0 = android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup.getInstance()     // Catch:{ Exception -> 0x021c }
            java.util.HashMap r0 = r0.getInfoMap()     // Catch:{ Exception -> 0x021c }
            java.lang.String r3 = r1.name     // Catch:{ Exception -> 0x021c }
            java.lang.Object r0 = r0.get(r3)     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.packageapp.cleanup.InfoSnippet r0 = (android.taobao.windvane.packageapp.cleanup.InfoSnippet) r0     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager r3 = android.taobao.windvane.packageapp.zipapp.utils.WVZipSecurityManager.getInstance()     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.packageapp.zipapp.utils.AppResInfo r3 = r3.getAppResInfo(r1, r5)     // Catch:{ Exception -> 0x021c }
            if (r3 == 0) goto L_0x01b9
            int r3 = r0.failCount     // Catch:{ Exception -> 0x021c }
            r4 = 100
            if (r3 <= r4) goto L_0x017b
            r3 = 1
            r0.needReinstall = r3     // Catch:{ Exception -> 0x021c }
        L_0x017b:
            java.lang.String r0 = "PackageApp-Runtime"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x021c }
            r3.<init>()     // Catch:{ Exception -> 0x021c }
            java.lang.String r4 = "PackageappforDebug 入口:未命中["
            r3.append(r4)     // Catch:{ Exception -> 0x021c }
            r3.append(r5)     // Catch:{ Exception -> 0x021c }
            java.lang.String r4 = "]"
            r3.append(r4)     // Catch:{ Exception -> 0x021c }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.util.TaoLog.e(r0, r3)     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x021c }
            if (r1 != 0) goto L_0x019f
            java.lang.String r3 = "unknown-0"
            goto L_0x01b2
        L_0x019f:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x021c }
            r3.<init>()     // Catch:{ Exception -> 0x021c }
            java.lang.String r4 = r1.name     // Catch:{ Exception -> 0x021c }
            r3.append(r4)     // Catch:{ Exception -> 0x021c }
            java.lang.String r4 = "-0"
            r3.append(r4)     // Catch:{ Exception -> 0x021c }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x021c }
        L_0x01b2:
            java.lang.String r4 = "12"
            r0.commitPackageVisitError(r3, r5, r4)     // Catch:{ Exception -> 0x021c }
            goto L_0x026e
        L_0x01b9:
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x021c }
            if (r1 != 0) goto L_0x01c2
            java.lang.String r3 = "unknown-0"
            goto L_0x01d5
        L_0x01c2:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x021c }
            r3.<init>()     // Catch:{ Exception -> 0x021c }
            java.lang.String r4 = r1.name     // Catch:{ Exception -> 0x021c }
            r3.append(r4)     // Catch:{ Exception -> 0x021c }
            java.lang.String r4 = "-0"
            r3.append(r4)     // Catch:{ Exception -> 0x021c }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x021c }
        L_0x01d5:
            java.lang.String r4 = "12"
            r0.commitPackageVisitError(r3, r5, r4)     // Catch:{ Exception -> 0x021c }
            java.lang.String r0 = "PackageApp-Runtime"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x021c }
            r3.<init>()     // Catch:{ Exception -> 0x021c }
            java.lang.String r4 = "PackageappforDebug 入口:不在预加载包中["
            r3.append(r4)     // Catch:{ Exception -> 0x021c }
            r3.append(r5)     // Catch:{ Exception -> 0x021c }
            java.lang.String r4 = "]"
            r3.append(r4)     // Catch:{ Exception -> 0x021c }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x021c }
            android.taobao.windvane.util.TaoLog.e(r0, r3)     // Catch:{ Exception -> 0x021c }
            return r2
        L_0x01f6:
            android.taobao.windvane.monitor.WVPackageMonitorInterface r3 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x021c }
            if (r3 == 0) goto L_0x021b
            android.taobao.windvane.monitor.WVPackageMonitorInterface r3 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x021c }
            if (r1 != 0) goto L_0x0205
            java.lang.String r4 = "unknown-0"
            goto L_0x0218
        L_0x0205:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x021c }
            r4.<init>()     // Catch:{ Exception -> 0x021c }
            java.lang.String r6 = r1.name     // Catch:{ Exception -> 0x021c }
            r4.append(r6)     // Catch:{ Exception -> 0x021c }
            java.lang.String r6 = "-0"
            r4.append(r6)     // Catch:{ Exception -> 0x021c }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x021c }
        L_0x0218:
            r3.commitPackageVisitError(r4, r5, r0)     // Catch:{ Exception -> 0x021c }
        L_0x021b:
            return r2
        L_0x021c:
            r0 = move-exception
            goto L_0x0221
        L_0x021e:
            r0 = move-exception
            r5 = r29
        L_0x0221:
            android.taobao.windvane.monitor.WVPackageMonitorInterface r3 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()
            if (r3 == 0) goto L_0x024c
            android.taobao.windvane.monitor.WVPackageMonitorInterface r3 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()
            if (r1 != 0) goto L_0x0230
            java.lang.String r1 = "unknown-0"
            goto L_0x0243
        L_0x0230:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r1 = r1.name
            r4.append(r1)
            java.lang.String r1 = "-0"
            r4.append(r1)
            java.lang.String r1 = r4.toString()
        L_0x0243:
            java.lang.String r4 = r0.getMessage()
            java.lang.String r6 = "9"
            r3.commitPackageVisitError(r1, r4, r6)
        L_0x024c:
            java.lang.String r1 = "PackageApp-Runtime"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "PackageappforDebug 入口:访问本地zip资源失败 ["
            r3.append(r4)
            r3.append(r5)
            java.lang.String r4 = "]"
            r3.append(r4)
            java.lang.String r0 = r0.getMessage()
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            android.taobao.windvane.util.TaoLog.e(r1, r0)
        L_0x026e:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.WVPackageAppRuntime.getResourceResponse(java.lang.String, android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo):android.webkit.WebResourceResponse");
    }

    private static boolean needCheckSecurity(String str) {
        double appSample = WVZipSecurityManager.getInstance().getAppSample(str);
        double random = Math.random();
        if (random >= appSample) {
            if (!TaoLog.getLogStatus()) {
                return false;
            }
            TaoLog.d(TAG, "采样率不满足要求，不对【" + str + "】进行校验 当前配置采样率为: " + appSample + "  获取的随机值为:" + random);
            return false;
        } else if (!TaoLog.getLogStatus()) {
            return true;
        } else {
            TaoLog.d(TAG, "采样率满足要求，对【" + str + "】进行校验 当前配置采样率为: " + appSample + "  获取的随机值为:" + random);
            return true;
        }
    }

    public static void getZCacheResourceResponse(final String str, final ZCacheResourceCallback zCacheResourceCallback) {
        WVThreadPool.getInstance().execute(new Runnable() {
            public void run() {
                zCacheResourceCallback.callback(WVPackageAppRuntime.getZCacheResourceResponse(str));
            }
        });
    }

    public static ZCacheResourceResponse getZCacheResourceResponse(String str) {
        String str2;
        long j;
        String str3;
        WVWrapWebResourceResponse wVWrapWebResourceResponse;
        if (WVCommonConfig.commonConfig.packageAppStatus == 0) {
            TaoLog.i(TAG, "packageApp is closed");
            return null;
        }
        String removeQueryParam = WVUrlUtil.removeQueryParam(str);
        ZCacheConfigManager.getInstance().triggerZCacheConfig();
        if ("3".equals(GlobalConfig.zType)) {
            com.taobao.zcache.model.ZCacheResourceResponse zCacheResource = ZCacheManager.instance().getZCacheResource(removeQueryParam);
            ZCacheResourceResponse zCacheResourceResponse = new ZCacheResourceResponse();
            if (zCacheResource != null) {
                TaoLog.i("ZCache", "weex use ZCache 3.0, url=[" + removeQueryParam + "], with response:[" + zCacheResource.isSuccess + Operators.ARRAY_END_STR);
                zCacheResourceResponse.encoding = zCacheResource.encoding;
                zCacheResourceResponse.headers = zCacheResource.headers;
                zCacheResourceResponse.inputStream = zCacheResource.inputStream;
                zCacheResourceResponse.isSuccess = zCacheResource.isSuccess;
                zCacheResourceResponse.mimeType = zCacheResource.mimeType;
            } else {
                TaoLog.i("ZCache", "weex use ZCache 3.0, url=[" + removeQueryParam + "], with response=[null]");
            }
            return zCacheResourceResponse;
        }
        String force2HttpUrl = WVUrlUtil.force2HttpUrl(removeQueryParam);
        ZipGlobalConfig.CacheFileData isZcacheUrl = ConfigManager.getLocGlobalConfig().isZcacheUrl(force2HttpUrl);
        if (isZcacheUrl != null) {
            wVWrapWebResourceResponse = getWrapResourceResponse(force2HttpUrl, isZcacheUrl);
            str3 = isZcacheUrl.appName;
            j = isZcacheUrl.seq;
            str2 = isZcacheUrl.errorCode;
            isZcacheUrl.errorCode = null;
        } else {
            str2 = "0";
            wVWrapWebResourceResponse = null;
            str3 = null;
            j = 0;
        }
        if (wVWrapWebResourceResponse != null) {
            ZCacheResourceResponse zCacheResourceResponse2 = new ZCacheResourceResponse();
            zCacheResourceResponse2.isSuccess = true;
            zCacheResourceResponse2.inputStream = wVWrapWebResourceResponse.mInputStream;
            zCacheResourceResponse2.mimeType = wVWrapWebResourceResponse.mMimeType;
            zCacheResourceResponse2.encoding = wVWrapWebResourceResponse.mEncoding;
            zCacheResourceResponse2.headers = wVWrapWebResourceResponse.mHeaders;
            zCacheResourceResponse2.insertZCacheInfo(str3, j, str2);
            return zCacheResourceResponse2;
        }
        ZipAppInfo appInfoByUrl = getAppInfoByUrl(force2HttpUrl);
        if (appInfoByUrl != null) {
            wVWrapWebResourceResponse = getWrapResourceResponse(force2HttpUrl, appInfoByUrl);
            str3 = appInfoByUrl.name;
            j = appInfoByUrl.s;
            str2 = appInfoByUrl.errorCode;
            appInfoByUrl.errorCode = null;
        }
        String zipAppName = ZipPrefixesManager.getInstance().getZipAppName(force2HttpUrl);
        if (zipAppName != null && ConfigManager.getLocGlobalConfig().getAppInfo(zipAppName) == null) {
            str2 = "401";
        }
        if (wVWrapWebResourceResponse != null) {
            ZCacheResourceResponse zCacheResourceResponse3 = new ZCacheResourceResponse();
            zCacheResourceResponse3.isSuccess = true;
            zCacheResourceResponse3.inputStream = wVWrapWebResourceResponse.mInputStream;
            zCacheResourceResponse3.mimeType = wVWrapWebResourceResponse.mMimeType;
            zCacheResourceResponse3.encoding = wVWrapWebResourceResponse.mEncoding;
            zCacheResourceResponse3.headers = wVWrapWebResourceResponse.mHeaders;
            zCacheResourceResponse3.insertZCacheInfo(str3, j, str2);
            return zCacheResourceResponse3;
        }
        ComboInfo comboInfo = new ComboInfo();
        WebResourceResponse makeComboRes = makeComboRes(force2HttpUrl, comboInfo, new HashMap());
        if (makeComboRes != null) {
            String str4 = comboInfo.appName != null ? comboInfo.appName : "COMBO";
            if (comboInfo.seq != 0) {
                j = comboInfo.seq;
            }
            ZCacheResourceResponse zCacheResourceResponse4 = new ZCacheResourceResponse();
            zCacheResourceResponse4.isSuccess = true;
            zCacheResourceResponse4.inputStream = makeComboRes.getData();
            zCacheResourceResponse4.mimeType = makeComboRes.getMimeType();
            zCacheResourceResponse4.encoding = makeComboRes.getEncoding();
            if (Build.VERSION.SDK_INT >= 21) {
                zCacheResourceResponse4.headers = makeComboRes.getResponseHeaders();
            }
            zCacheResourceResponse4.insertZCacheInfo(str4, j, str2);
            return zCacheResourceResponse4;
        } else if (TextUtils.isEmpty(str3) || "-1".equals(str2)) {
            return null;
        } else {
            ZCacheResourceResponse zCacheResourceResponse5 = new ZCacheResourceResponse();
            zCacheResourceResponse5.isSuccess = false;
            zCacheResourceResponse5.insertZCacheInfo(str3, j, str2);
            return zCacheResourceResponse5;
        }
    }
}
