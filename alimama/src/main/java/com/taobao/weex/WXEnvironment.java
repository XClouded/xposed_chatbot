package com.taobao.weex;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.uikit.feature.features.FeatureFactory;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.utils.FontDO;
import com.taobao.weex.utils.LogLevel;
import com.taobao.weex.utils.TypefaceUtil;
import com.taobao.weex.utils.WXFileUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXSoInstallMgrSdk;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WXEnvironment {
    public static boolean AUTO_ADJUST_ENV_DEVICE_WIDTH = true;
    public static boolean AUTO_UPDATE_APPLICATION_SCREEN_SIZE = true;
    private static String COPY_SO_DES_DIR = null;
    public static final String CORE_JSB_SO_NAME = "weexjsb";
    public static String CORE_JSB_SO_PATH = null;
    public static final String CORE_JSC_SO_NAME = "JavaScriptCore";
    private static String CORE_JSC_SO_PATH = null;
    private static String CORE_JSS_ICU_PATH = null;
    public static String CORE_JSS_RUNTIME_SO_PATH = null;
    public static final String CORE_JSS_SO_NAME = "weexjss";
    private static String CORE_JSS_SO_PATH = null;
    public static final String CORE_JST_SO_NAME = "weexjst";
    public static final String CORE_SO_NAME = "weexcore";
    public static final String DEV_Id = getDevId();
    public static final String EAGLE = "eagle";
    public static final String ENVIRONMENT = "environment";
    public static String JS_LIB_SDK_VERSION = BuildConfig.buildJavascriptFrameworkVersion;
    public static volatile boolean JsFrameworkInit = false;
    private static String LIB_LD_PATH = null;
    public static final String OS = "android";
    public static final String SETTING_EXCLUDE_X86SUPPORT = "env_exclude_x86";
    public static boolean SETTING_FORCE_VERTICAL_SCREEN = false;
    public static final String SYS_MODEL = Build.MODEL;
    public static String SYS_VERSION = null;
    public static final String WEEX_CURRENT_KEY = "wx_current_url";
    public static String WXSDK_VERSION = "0.26.5.13";
    private static boolean isApkDebug = true;
    public static boolean isPerf = false;
    public static volatile boolean isWsFixMode = true;
    private static WXDefaultSettings mWXDefaultSettings = null;
    private static boolean openDebugLog = true;
    private static Map<String, String> options = new ConcurrentHashMap();
    public static Application sApplication = null;
    public static long sComponentsAndModulesReadyTime = 0;
    private static boolean sDebugFlagInit = false;
    public static boolean sDebugMode = false;
    public static boolean sDebugNetworkEventReporterEnable = false;
    public static boolean sDebugServerConnectable = false;
    public static String sDebugWsUrl = "";
    @Deprecated
    public static int sDefaultWidth = FeatureFactory.PRIORITY_ABOVE_NORMAL;
    public static boolean sDynamicMode = false;
    public static String sDynamicUrl = "";
    public static final boolean sForceEnableDevTool = true;
    private static String sGlobalFontFamily;
    public static boolean sInAliWeex = false;
    public static long sJSFMStartListenerTime = 0;
    public static long sJSLibInitTime = 0;
    public static LogLevel sLogLevel = LogLevel.DEBUG;
    public static boolean sRemoteDebugMode = false;
    public static String sRemoteDebugProxyUrl = "";
    public static long sSDKInitExecuteTime = 0;
    public static long sSDKInitInvokeTime = 0;
    public static long sSDKInitStart = 0;
    public static long sSDKInitTime = 0;
    public static volatile boolean sUseRunTimeApi = false;

    static {
        SYS_VERSION = Build.VERSION.RELEASE;
        if (SYS_VERSION != null && SYS_VERSION.toUpperCase(Locale.ROOT).equals("P")) {
            SYS_VERSION = "9.0.0";
        }
        if (SYS_VERSION != null && SYS_VERSION.toUpperCase(Locale.ROOT).equals("Q")) {
            SYS_VERSION = "10.0.0";
        }
        options.put("os", "android");
        options.put(WXConfig.osName, "android");
    }

    public static synchronized WXDefaultSettings getWXDefaultSettings() {
        WXDefaultSettings wXDefaultSettings;
        synchronized (WXEnvironment.class) {
            if (mWXDefaultSettings == null && getApplication() != null) {
                mWXDefaultSettings = new WXDefaultSettings(getApplication());
            }
            wXDefaultSettings = mWXDefaultSettings;
        }
        return wXDefaultSettings;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0017, code lost:
        return r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.lang.String getDefaultSettingValue(java.lang.String r3, java.lang.String r4) {
        /*
            java.lang.Class<com.taobao.weex.WXEnvironment> r0 = com.taobao.weex.WXEnvironment.class
            monitor-enter(r0)
            com.taobao.weex.WXEnvironment$WXDefaultSettings r1 = getWXDefaultSettings()     // Catch:{ all -> 0x0018 }
            if (r1 == 0) goto L_0x0016
            boolean r2 = android.text.TextUtils.isEmpty(r3)     // Catch:{ all -> 0x0018 }
            if (r2 == 0) goto L_0x0010
            goto L_0x0016
        L_0x0010:
            java.lang.String r3 = r1.getValue(r3, r4)     // Catch:{ all -> 0x0018 }
            monitor-exit(r0)
            return r3
        L_0x0016:
            monitor-exit(r0)
            return r4
        L_0x0018:
            r3 = move-exception
            monitor-exit(r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXEnvironment.getDefaultSettingValue(java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void writeDefaultSettingsValue(java.lang.String r3, java.lang.String r4) {
        /*
            java.lang.Class<com.taobao.weex.WXEnvironment> r0 = com.taobao.weex.WXEnvironment.class
            monitor-enter(r0)
            com.taobao.weex.WXEnvironment$WXDefaultSettings r1 = getWXDefaultSettings()     // Catch:{ all -> 0x001d }
            if (r1 == 0) goto L_0x001b
            boolean r2 = android.text.TextUtils.isEmpty(r3)     // Catch:{ all -> 0x001d }
            if (r2 != 0) goto L_0x001b
            boolean r2 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x001d }
            if (r2 == 0) goto L_0x0016
            goto L_0x001b
        L_0x0016:
            r1.saveValue(r3, r4)     // Catch:{ all -> 0x001d }
            monitor-exit(r0)
            return
        L_0x001b:
            monitor-exit(r0)
            return
        L_0x001d:
            r3 = move-exception
            monitor-exit(r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXEnvironment.writeDefaultSettingsValue(java.lang.String, java.lang.String):void");
    }

    public static Map<String, String> getConfig() {
        HashMap hashMap = new HashMap();
        hashMap.put("os", "android");
        hashMap.put("appVersion", getAppVersionName());
        hashMap.put(WXConfig.cacheDir, getAppCacheFile());
        hashMap.put(WXConfig.devId, DEV_Id);
        hashMap.put(WXConfig.sysVersion, SYS_VERSION);
        hashMap.put(WXConfig.sysModel, SYS_MODEL);
        hashMap.put("weexVersion", String.valueOf(WXSDK_VERSION));
        try {
            hashMap.put(WXConfig.layoutDirection, isLayoutDirectionRTL() ? Constants.Name.RTL : "ltr");
        } catch (Exception unused) {
            hashMap.put(WXConfig.layoutDirection, "ltr");
        }
        try {
            if (isApkDebugable()) {
                addCustomOptions(WXConfig.debugMode, "true");
            }
            addCustomOptions("scale", Float.toString(sApplication.getResources().getDisplayMetrics().density));
            addCustomOptions(WXConfig.androidStatusBarHeight, Float.toString((float) WXViewUtils.getStatusBarHeight(sApplication)));
        } catch (NullPointerException e) {
            WXLogUtils.e("WXEnvironment scale Exception: ", (Throwable) e);
        }
        hashMap.putAll(getCustomOptions());
        if (hashMap.get("appName") == null && sApplication != null) {
            hashMap.put("appName", sApplication.getPackageName());
        }
        return hashMap;
    }

    public static String getAppVersionName() {
        try {
            return sApplication.getPackageManager().getPackageInfo(sApplication.getPackageName(), 0).versionName;
        } catch (Exception e) {
            WXLogUtils.e("WXEnvironment getAppVersionName Exception: ", (Throwable) e);
            return "";
        }
    }

    private static String getAppCacheFile() {
        try {
            return sApplication.getApplicationContext().getCacheDir().getPath();
        } catch (Exception e) {
            WXLogUtils.e("WXEnvironment getAppCacheFile Exception: ", (Throwable) e);
            return "";
        }
    }

    @Deprecated
    public static Map<String, String> getCustomOptions() {
        return options;
    }

    public static void addCustomOptions(String str, String str2) {
        options.put(str, str2);
    }

    public static String getCustomOptions(String str) {
        return options.get(str);
    }

    @SuppressLint({"SdCardPath"})
    public static String copySoDesDir() {
        File file;
        try {
            if (TextUtils.isEmpty(COPY_SO_DES_DIR)) {
                if (sApplication == null) {
                    WXLogUtils.e("sApplication is null, so copy path will be null");
                    return null;
                }
                String path = getApplication().getApplicationContext().getCacheDir().getPath();
                if (TextUtils.isEmpty(path)) {
                    file = new File(path, "/cache/weex/libs");
                } else {
                    String packageName = sApplication.getPackageName();
                    file = new File("/data/data/" + packageName + "/cache/weex/libs");
                }
                if (!file.exists()) {
                    file.mkdirs();
                }
                COPY_SO_DES_DIR = file.getAbsolutePath();
            }
        } catch (Throwable th) {
            WXLogUtils.e(WXLogUtils.getStackTrace(th));
        }
        return COPY_SO_DES_DIR;
    }

    @Deprecated
    public static boolean isSupport() {
        boolean isInitialized = WXSDKEngine.isInitialized();
        if (!isInitialized) {
            WXLogUtils.e("WXSDKEngine.isInitialized():" + isInitialized);
        }
        return isHardwareSupport() && isInitialized;
    }

    public static boolean isLayoutDirectionRTL() {
        if (Build.VERSION.SDK_INT >= 17) {
            return sApplication.getApplicationContext().getResources().getBoolean(R.bool.weex_is_right_to_left);
        }
        return false;
    }

    @Deprecated
    public static boolean isHardwareSupport() {
        if (isApkDebugable()) {
            WXLogUtils.d("isTableDevice:" + WXUtils.isTabletDevice());
        }
        return isCPUSupport();
    }

    public static boolean isCPUSupport() {
        boolean z = true;
        boolean z2 = WXSoInstallMgrSdk.isX86() && "true".equals(getCustomOptions().get(SETTING_EXCLUDE_X86SUPPORT));
        if (!WXSoInstallMgrSdk.isCPUSupport() || z2) {
            z = false;
        }
        if (isApkDebugable()) {
            WXLogUtils.d("WXEnvironment.sSupport:" + z + "isX86AndExclueded: " + z2);
        }
        return z;
    }

    public static boolean isApkDebugable() {
        return isApkDebugable(sApplication);
    }

    public static boolean isApkDebugable(Application application) {
        if (application == null || isPerf) {
            return false;
        }
        if (sDebugFlagInit) {
            return isApkDebug;
        }
        try {
            String str = getCustomOptions().get(WXConfig.debugMode);
            if (TextUtils.isEmpty(str)) {
                isApkDebug = (application.getApplicationInfo().flags & 2) != 0;
            } else {
                isApkDebug = Boolean.valueOf(str).booleanValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            isApkDebug = false;
        }
        sDebugFlagInit = true;
        return isApkDebug;
    }

    public static boolean isPerf() {
        return isPerf;
    }

    @SuppressLint({"HardwareIds"})
    private static String getDevId() {
        if (sApplication == null) {
            return "";
        }
        try {
            return ((TelephonyManager) sApplication.getSystemService("phone")).getDeviceId();
        } catch (NullPointerException | SecurityException e) {
            WXLogUtils.e(WXLogUtils.getStackTrace(e));
            return "";
        }
    }

    public static Application getApplication() {
        return sApplication;
    }

    public void initMetrics() {
        if (sApplication != null) {
        }
    }

    public static String getDiskCacheDir(Context context) {
        String str;
        if (context == null) {
            return null;
        }
        try {
            if (!"mounted".equals(Environment.getExternalStorageState())) {
                if (Environment.isExternalStorageRemovable()) {
                    str = context.getCacheDir().getPath();
                    return str;
                }
            }
            str = context.getExternalCacheDir().getPath();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFilesDir(Context context) {
        if (context == null) {
            return "";
        }
        File filesDir = context.getFilesDir();
        if (filesDir != null) {
            return filesDir.getPath();
        }
        return (getApplication().getApplicationInfo().dataDir + File.separator) + AVFSCacheConstants.AVFS_FIlE_PATH_NAME;
    }

    public static String getCrashFilePath(Context context) {
        File dir;
        if (context == null || (dir = context.getDir("crash", 0)) == null) {
            return "";
        }
        return dir.getAbsolutePath();
    }

    public static String getGlobalFontFamilyName() {
        return sGlobalFontFamily;
    }

    public static void setGlobalFontFamily(String str, Typeface typeface) {
        WXLogUtils.d("GlobalFontFamily", "Set global font family: " + str);
        sGlobalFontFamily = str;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (typeface == null) {
            TypefaceUtil.removeFontDO(str);
            return;
        }
        TypefaceUtil.putFontDO(new FontDO(str, typeface));
        WXLogUtils.d("TypefaceUtil", "Add new font: " + str);
    }

    public static boolean isOpenDebugLog() {
        return openDebugLog;
    }

    public static void setOpenDebugLog(boolean z) {
        openDebugLog = z;
    }

    public static void setApkDebugable(boolean z) {
        isApkDebug = z;
        if (!isApkDebug) {
            openDebugLog = false;
        }
    }

    public static String getCacheDir() {
        Application application = getApplication();
        if (application == null || application.getApplicationContext() == null) {
            return null;
        }
        return application.getApplicationContext().getCacheDir().getPath();
    }

    public static String extractSo() {
        File file = new File(getApplication().getApplicationContext().getApplicationInfo().sourceDir);
        String copySoDesDir = copySoDesDir();
        if (!file.exists() || TextUtils.isEmpty(copySoDesDir)) {
            return null;
        }
        try {
            WXFileUtils.extractSo(file.getAbsolutePath(), copySoDesDir);
            return copySoDesDir;
        } catch (IOException e) {
            WXLogUtils.e("extractSo error " + e.getMessage());
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0043, code lost:
        if (r2 != null) goto L_0x0035;
     */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004a A[SYNTHETIC, Splitter:B:29:0x004a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String findIcuPath() {
        /*
            java.io.File r0 = new java.io.File
            java.lang.String r1 = "/proc/self/maps"
            r0.<init>(r1)
            r1 = 0
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x003e, all -> 0x003b }
            java.io.FileReader r3 = new java.io.FileReader     // Catch:{ IOException -> 0x003e, all -> 0x003b }
            r3.<init>(r0)     // Catch:{ IOException -> 0x003e, all -> 0x003b }
            r2.<init>(r3)     // Catch:{ IOException -> 0x003e, all -> 0x003b }
        L_0x0012:
            java.lang.String r0 = r2.readLine()     // Catch:{ IOException -> 0x0039 }
            if (r0 == 0) goto L_0x0032
            java.lang.String r3 = "icudt"
            boolean r3 = r0.contains(r3)     // Catch:{ IOException -> 0x0039 }
            if (r3 == 0) goto L_0x0012
            r3 = 47
            int r3 = r0.indexOf(r3)     // Catch:{ IOException -> 0x0039 }
            java.lang.String r0 = r0.substring(r3)     // Catch:{ IOException -> 0x0039 }
            java.lang.String r0 = r0.trim()     // Catch:{ IOException -> 0x0039 }
            r2.close()     // Catch:{ IOException -> 0x0031 }
        L_0x0031:
            return r0
        L_0x0032:
            r2.close()     // Catch:{ IOException -> 0x0039 }
        L_0x0035:
            r2.close()     // Catch:{ IOException -> 0x0046 }
            goto L_0x0046
        L_0x0039:
            r0 = move-exception
            goto L_0x0040
        L_0x003b:
            r0 = move-exception
            r2 = r1
            goto L_0x0048
        L_0x003e:
            r0 = move-exception
            r2 = r1
        L_0x0040:
            r0.printStackTrace()     // Catch:{ all -> 0x0047 }
            if (r2 == 0) goto L_0x0046
            goto L_0x0035
        L_0x0046:
            return r1
        L_0x0047:
            r0 = move-exception
        L_0x0048:
            if (r2 == 0) goto L_0x004d
            r2.close()     // Catch:{ IOException -> 0x004d }
        L_0x004d:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXEnvironment.findIcuPath():java.lang.String");
    }

    public static String findSoPath(String str) {
        String findLibrary = ((PathClassLoader) WXEnvironment.class.getClassLoader()).findLibrary(str);
        if (!TextUtils.isEmpty(findLibrary)) {
            File file = new File(findLibrary);
            if (file.exists()) {
                WXLogUtils.e(str + "'s Path is" + findLibrary);
                return file.getAbsolutePath();
            }
            WXLogUtils.e(str + "'s Path is " + findLibrary + " but file does not exist");
        }
        String str2 = "lib" + str + ".so";
        String cacheDir = getCacheDir();
        if (TextUtils.isEmpty(cacheDir)) {
            WXLogUtils.e("cache dir is null");
            return "";
        }
        if (cacheDir.indexOf("/cache") > 0) {
            findLibrary = new File(cacheDir.replace("/cache", "/lib"), str2).getAbsolutePath();
        }
        if (new File(findLibrary).exists()) {
            WXLogUtils.e(str + "use lib so");
            return findLibrary;
        }
        String extractSo = extractSo();
        return !TextUtils.isEmpty(extractSo) ? new File(extractSo, str2).getAbsolutePath() : findLibrary;
    }

    public static String getLibJScRealPath() {
        if (TextUtils.isEmpty(CORE_JSC_SO_PATH)) {
            CORE_JSC_SO_PATH = findSoPath(CORE_JSC_SO_NAME);
            WXLogUtils.e("findLibJscRealPath " + CORE_JSC_SO_PATH);
        }
        return CORE_JSC_SO_PATH;
    }

    public static String getLibJssRealPath() {
        if (!sUseRunTimeApi || TextUtils.isEmpty(CORE_JSS_RUNTIME_SO_PATH)) {
            if (TextUtils.isEmpty(CORE_JSS_SO_PATH)) {
                CORE_JSS_SO_PATH = findSoPath(CORE_JSS_SO_NAME);
                WXLogUtils.d("test-> findLibJssRealPath " + CORE_JSS_SO_PATH);
            }
            return CORE_JSS_SO_PATH;
        }
        WXLogUtils.d("test-> findLibJssRuntimeRealPath " + CORE_JSS_RUNTIME_SO_PATH);
        return CORE_JSS_RUNTIME_SO_PATH;
    }

    public static String getLibJssIcuPath() {
        if (TextUtils.isEmpty(CORE_JSS_ICU_PATH)) {
            CORE_JSS_ICU_PATH = findIcuPath();
        }
        return CORE_JSS_ICU_PATH;
    }

    public static String getLibLdPath() {
        if (TextUtils.isEmpty(LIB_LD_PATH)) {
            ClassLoader classLoader = WXEnvironment.class.getClassLoader();
            try {
                LIB_LD_PATH = (String) classLoader.getClass().getMethod("getLdLibraryPath", new Class[0]).invoke(classLoader, new Object[0]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e2) {
                e2.printStackTrace();
            } catch (NoSuchMethodException e3) {
                e3.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(LIB_LD_PATH)) {
            try {
                String property = System.getProperty("java.library.path");
                String libJScRealPath = getLibJScRealPath();
                if (!TextUtils.isEmpty(libJScRealPath)) {
                    LIB_LD_PATH = new File(libJScRealPath).getParent() + ":" + property;
                }
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
        WXLogUtils.d("getLibLdPath is " + LIB_LD_PATH);
        return LIB_LD_PATH;
    }

    public static class WXDefaultSettings {
        private String configName = "weex_default_settings";
        private SharedPreferences sharedPreferences = null;

        public WXDefaultSettings(Application application) {
            if (application != null) {
                this.sharedPreferences = application.getSharedPreferences(this.configName, 0);
            }
        }

        public synchronized String getValue(String str, String str2) {
            if (this.sharedPreferences != null) {
                if (!TextUtils.isEmpty(str)) {
                    String string = this.sharedPreferences.getString(str, str2);
                    WXLogUtils.i("get default settings " + str + " : " + string);
                    return string;
                }
            }
            WXLogUtils.i("get default settings " + str + " return default value :" + str2);
            return str2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:13:0x003d, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized void saveValue(java.lang.String r3, java.lang.String r4) {
            /*
                r2 = this;
                monitor-enter(r2)
                android.content.SharedPreferences r0 = r2.sharedPreferences     // Catch:{ all -> 0x003e }
                if (r0 == 0) goto L_0x003c
                boolean r0 = android.text.TextUtils.isEmpty(r3)     // Catch:{ all -> 0x003e }
                if (r0 != 0) goto L_0x003c
                boolean r0 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x003e }
                if (r0 == 0) goto L_0x0012
                goto L_0x003c
            L_0x0012:
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x003e }
                r0.<init>()     // Catch:{ all -> 0x003e }
                java.lang.String r1 = "save default settings "
                r0.append(r1)     // Catch:{ all -> 0x003e }
                r0.append(r3)     // Catch:{ all -> 0x003e }
                java.lang.String r1 = ":"
                r0.append(r1)     // Catch:{ all -> 0x003e }
                r0.append(r4)     // Catch:{ all -> 0x003e }
                java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x003e }
                com.taobao.weex.utils.WXLogUtils.i(r0)     // Catch:{ all -> 0x003e }
                android.content.SharedPreferences r0 = r2.sharedPreferences     // Catch:{ all -> 0x003e }
                android.content.SharedPreferences$Editor r0 = r0.edit()     // Catch:{ all -> 0x003e }
                r0.putString(r3, r4)     // Catch:{ all -> 0x003e }
                r0.apply()     // Catch:{ all -> 0x003e }
                monitor-exit(r2)
                return
            L_0x003c:
                monitor-exit(r2)
                return
            L_0x003e:
                r3 = move-exception
                monitor-exit(r2)
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.WXEnvironment.WXDefaultSettings.saveValue(java.lang.String, java.lang.String):void");
        }
    }
}
